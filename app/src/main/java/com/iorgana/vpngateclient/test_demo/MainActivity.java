package com.iorgana.vpngateclient.test_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.iorgana.vpngate_client.api.VpnGateApi;
import com.iorgana.vpngateclient.databinding.ActivityMainBinding;
import com.iorgana.vpngate_client.listener.OnClientListener;
import com.iorgana.vpngate_client.model.Server;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.BuildConfig;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity{
    ActivityMainBinding binding;
    ServersListAdapter serversAdapter;
    Context context = this;

    @Override
    protected void onStart() {
        super.onStart();
        // initialize logger (debug)
        iniLogger();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        /**
         * Setup RecyclerView
         */
        setupRecyclerView();

        /**
         * Handle Fetch Action
         */
        binding.btnFetch.setOnClickListener(view->{
            fetch();
        });
    }


    /**
     * Setup RecyclerView
     * ---------------------------------------------------------------------
     */
    void setupRecyclerView(){
        serversAdapter = new ServersListAdapter(context);
        binding.recyclerView.setAdapter(serversAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));

    }

    /**
     * Fetch servers
     * ---------------------------------------------------------------------
     */
    void fetch(){
        // Prepare api client
        VpnGateApi vpnGateApi = VpnGateApi.getInstance()
                .setUrl("http://www.vpngate.net/api/iphone")
                .setCache(15, TimeUnit.MINUTES)
                .setListener(onClientListener);

        // Execute api client in background
        new Thread(vpnGateApi).start();

    }


    /*############################ Callback Class ############################*/
    OnClientListener onClientListener = new OnClientListener() {
        @Override
        public void onStart() {
            runOnUiThread(()->{
                Toast.makeText(context, "Start fetching..", Toast.LENGTH_SHORT).show();
                // show loader
                binding.loaderContainer.setVisibility(View.VISIBLE);
            });
        }

        @Override
        public void onComplete(List<Server> servers) {
            runOnUiThread(()->{
                serversAdapter.setListItems(servers);
                Toast.makeText(context, "Fetch completed", Toast.LENGTH_SHORT).show();
                // hide loader
                binding.loaderContainer.setVisibility(View.GONE);

            });
        }

        @Override
        public void onFailure(String errorMsg) {
            runOnUiThread(() -> {
                Toast.makeText(context, "Error: " + errorMsg, Toast.LENGTH_LONG).show();
                // hide loader
                binding.loaderContainer.setVisibility(View.GONE);
            });
        }
    };

    /**
     * Initialize Logger
     * -----------------------------------------------------------------------
     */
    void iniLogger(){
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(7)        // (Optional) Hides internal method calls up to offset. Default 5
                .tag("__App__")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
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