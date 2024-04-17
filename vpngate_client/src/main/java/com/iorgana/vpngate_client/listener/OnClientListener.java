package com.iorgana.vpngate_client.listener;

import com.iorgana.vpngate_client.model.Server;

import java.util.List;

public abstract class OnClientListener implements IClientListener {
    @Override public void onStart(){}
    @Override public void onComplete(List<Server> servers){}
    @Override public void onFailure(String errorMsg){}
    @Override public void onFailure(int errorCode){}
}
