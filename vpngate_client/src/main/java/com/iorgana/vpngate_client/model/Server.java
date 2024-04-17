package com.iorgana.vpngate_client.model;

import java.io.Serializable;

public class Server implements Serializable {
    public String hostName;
    public String ipAddress;
    public int score;
    public String ping;
    public long speed;
    public String countryLong;
    public String countryShort;
    public long vpnSessions;
    public long uptime;
    public long totalUsers;
    public String totalTraffic;
    public String logType;
    public String operator;
    public String message;
    public String ovpnConfigData;
    public int port;
    public String protocol;
    public boolean isStarred;

    /**
     * Empty Constructor (we set fields directly)
     */
    public Server() {}

    /**
     * Setters
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setPing(String ping) {
        this.ping = ping;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    public void setCountryLong(String countryLong) {
        this.countryLong = countryLong;
    }

    public void setCountryShort(String countryShort) {
        this.countryShort = countryShort;
    }

    public void setVpnSessions(long vpnSessions) {
        this.vpnSessions = vpnSessions;
    }

    public void setUptime(long uptime) {
        this.uptime = uptime;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public void setTotalTraffic(String totalTraffic) {
        this.totalTraffic = totalTraffic;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setOvpnConfigData(String ovpnConfigData) {
        this.ovpnConfigData = ovpnConfigData;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setStarred(boolean starred) {
        isStarred = starred;
    }

    /**
     * Getters
     */
    public String getHostName() {
        return hostName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getScore() {
        return score;
    }

    public String getPing() {
        return ping;
    }

    public long getSpeed() {
        return speed;
    }

    public String getCountryLong() {
        return countryLong;
    }

    public String getCountryShort() {
        return countryShort;
    }

    public long getVpnSessions() {
        return vpnSessions;
    }

    public long getUptime() {
        return uptime;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public String getTotalTraffic() {
        return totalTraffic;
    }

    public String getLogType() {
        return logType;
    }

    public String getOperator() {
        return operator;
    }

    public String getMessage() {
        return message;
    }

    public String getOvpnConfigData() {
        return ovpnConfigData;
    }

    public int getPort() {
        return port;
    }

    public String getProtocol() {
        return protocol;
    }

    public boolean isStarred() {
        return isStarred;
    }
}
