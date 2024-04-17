package com.iorgana.vpngate_client.api;

import com.iorgana.vpngate_client.listener.IClientListener;
import com.iorgana.vpngate_client.listener.OnClientListener;
import com.iorgana.vpngate_client.model.Server;
import com.iorgana.vpngate_client.parser.CsvParser;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.BuildConfig;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * This class must be Thread-Safe / Singleton
 */
public class VpnGateApi implements Runnable {
    private static final String TAG = "__HttpClient";
    public String REQUEST_URL = "http://www.vpngate.net/api/iphone/";

    private static volatile VpnGateApi INSTANCE;
    private OnClientListener onClientListener;
    private IClientListener iClientListener;
    private int maxAge;
    private TimeUnit timeUnit;
    private final OkHttpClient okHttpClient = new OkHttpClient();
    Request request;
    Response response;



    /**
     * Constructor
     * ******************************************************************
     * use getInstance() instead
     */
    public VpnGateApi(){
        initLogger();
    }

    /**
     * Get Instance
     * ******************************************************************
     * Safe-Thread + SingleTon
     */
    public static VpnGateApi getInstance(){
        if(INSTANCE==null){
            synchronized (VpnGateApi.class){
                if(INSTANCE==null){
                    INSTANCE = new VpnGateApi();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Set Listener
     * ******************************************************************
     * Set Interface listener
     * Or abstract listener
     */
    public VpnGateApi setListener(OnClientListener onClientListener){
        this.onClientListener = onClientListener;
        return this;
    }
    public VpnGateApi setListener(IClientListener iClientListener){
        this.iClientListener = iClientListener;
        return this;
    }

    /**
     * Set URL
     * ******************************************************************
     * Default URL is: <a href="http://www.vpngate.net/api/iphone/"/>
     * Use this method if url changed for some reason
     */
    public VpnGateApi setUrl(String url){
        this.REQUEST_URL = url;
        return this;
    }


    /**
     * Set Cache
     * ******************************************************************
     * Set Http Cache (this is equivalent to browser cache)
     */
    public VpnGateApi setCache(int maxAge, TimeUnit timeUnit){
        this.maxAge = maxAge;
        this.timeUnit = timeUnit;
        return this;
    }


    /**
     * Run
     * ***********************************************************************
     * This method executed when thread started
     * [-] Notify Listener with each process [Start/Complete/Failure]
     * [-] Build the http request using Request.Builder
     * [-] Execute request and receive http response using execute()
     *     We use execute() to handle the request synchronously,
     *     we don't have to use enqueue() because we're already in Runnable
     * [-] Check result if success
     * [-] Parse response
     */
    @Override
    public void run() {
        // [-] Notify listener
        Logger.d(TAG + " run(): thread start..");
        if(onClientListener!=null) onClientListener.onStart();
        if(iClientListener!=null) iClientListener.onStart();


        // [-] Build the http request
        Logger.d(TAG + " run(): Build the http request");
        Request.Builder requestBuilder = new Request.Builder().url(REQUEST_URL);
        if(timeUnit!=null && maxAge>0){
            requestBuilder.cacheControl(new CacheControl.Builder().maxAge(maxAge, timeUnit).build());
        }
        request = requestBuilder.build();


        // [-] Execute the request and receive the response
        try{
            response = okHttpClient.newCall(request).execute();
            Logger.d(TAG + " run(): response received");
        } catch (IOException e) {
            Logger.w(TAG + " run(): response not received: "+e.getMessage());
            if(onClientListener!=null) onClientListener.onFailure("Unable to send http request");
            if(iClientListener!=null) iClientListener.onFailure("Unable to send http request");

            e.printStackTrace();
            return;
        }

        /**
         * On Failure
         */
        if(!response.isSuccessful()){
            Logger.w(TAG + " run(): response not successful");
            if(onClientListener!=null){
                onClientListener.onFailure(response.message());
                onClientListener.onFailure(response.code());
            }
            if(iClientListener!=null){
                iClientListener.onFailure(response.message());
                iClientListener.onFailure(response.code());
            }

            return;
        }

        /**
         * On Success
         */
        Logger.d(TAG + " run(): getting response body");
        ResponseBody responseBody = response.body();

        // check response body
        if(responseBody==null){
            Logger.w(TAG + " run(): null response body");
            onClientListener.onFailure("Null response body");
            return;
        }

        // [-] Parse Csv Response To Java Servers
        Logger.d(TAG + " run(): convert csv response to list of model");
        List<Server> serverList = CsvParser.parse(responseBody);

        // [-] Check response servers
        if(serverList==null){
            Logger.w(TAG + " run(): null parsed servers");
            onClientListener.onFailure("Null parsed servers");
            return;
        }

        // [-] Return result
        Logger.d(TAG + " run(): All done!  servers fetched: "+serverList.size());
        if(onClientListener!=null) onClientListener.onComplete(serverList);
        if(iClientListener!=null) iClientListener.onComplete(serverList);


    }


    /**
     * Initialize Logger
     */
    void initLogger(){
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag("__VpnGateApi__")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

        // Only run on Debug
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
    }
}
