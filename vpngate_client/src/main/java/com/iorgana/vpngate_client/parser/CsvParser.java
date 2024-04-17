package com.iorgana.vpngate_client.parser;

import android.util.Base64;

import com.iorgana.vpngate_client.model.Server;
import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;

public class CsvParser {
    private static final String TAG = "__CsvParser";

    private static final int HOST_NAME = 0;
    private static final int IP_ADDRESS = 1;
    private static final int SCORE = 2;
    private static final int PING = 3;
    private static final int SPEED = 4;
    private static final int COUNTRY_LONG = 5;
    private static final int COUNTRY_SHORT = 6;
    private static final int VPN_SESSION = 7;
    private static final int UPTIME = 8;
    private static final int TOTAL_USERS = 9;
    private static final int TOTAL_TRAFFIC = 10;
    private static final int LOG_TYPE = 11;
    private static final int OPERATOR = 12;
    private static final int MESSAGE = 13;
    private static final int OVPN_CONFIG_DATA = 14;

    private static final int PORT_INDEX = 2;
    private static final int PROTOCOL_INDEX = 1;

    private static Server stringToServer(String line) {
        String[] vpn = line.split(",");

        Server server = new Server();
        server.hostName = vpn[HOST_NAME];
        server.ipAddress = vpn[IP_ADDRESS];
        server.score = Integer.parseInt(vpn[SCORE]);
        server.ping = vpn[PING];
        server.speed = Long.parseLong(vpn[SPEED]);
        server.countryLong = vpn[COUNTRY_LONG];
        server.countryShort = vpn[COUNTRY_SHORT];
        server.vpnSessions = Long.parseLong(vpn[VPN_SESSION]);
        server.uptime = Long.parseLong(vpn[UPTIME]);
        server.totalUsers = Long.parseLong(vpn[TOTAL_USERS]);
        server.totalTraffic = vpn[TOTAL_TRAFFIC];
        server.logType = vpn[LOG_TYPE];
        server.operator = vpn[OPERATOR];
        server.message = vpn[MESSAGE];
        server.ovpnConfigData = new String(Base64.decode(
                vpn[OVPN_CONFIG_DATA], Base64.DEFAULT));

        String[] lines = server.ovpnConfigData.split("[\\r\\n]+");
        server.port = getPort(lines);
        server.protocol = getProtocol(lines);

        return server;
    }

    public static List<Server> parse(ResponseBody responseBody) {

        if(responseBody==null){
            Logger.w(TAG + " parse(): null ResponseBody");
            return null;
        }

        List<Server> serverList = new ArrayList<>();

        try (InputStream in = responseBody.byteStream(); BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("*") && !line.startsWith("#")) {
                    serverList.add(CsvParser.stringToServer(line));
                }
            }

        } catch (IOException e) {
            Logger.w(TAG + " parse(): IOException: "+e.getMessage());
            e.printStackTrace();
        }

        Logger.d(TAG + " parse(): parse success");
        return serverList;
    }

    /**
     * @return Port used in OVPN file ("remote <HOSTNAME> <PORT>")
     * */
    private static int getPort(String[] lines) {
        int port = 0;
        for (String line : lines) {
            if (!line.startsWith("#")) {
                if (line.startsWith("remote")) {
                    port = Integer.parseInt(line.split(" ")[PORT_INDEX]);
                    break;
                }
            }
        }
        return port;
    }

    /**
     * @return Protocol used in OVPN file. ("proto <TCP/UDP>")
     * */
    private static String getProtocol(String[] lines) {
        String protocol = "";
        for (String line : lines) {
            if (!line.startsWith("#")) {
                if (line.startsWith("proto")) {
                    protocol = line.split(" ")[PROTOCOL_INDEX];
                    break;
                }
            }
        }
        return protocol;
    }
}
