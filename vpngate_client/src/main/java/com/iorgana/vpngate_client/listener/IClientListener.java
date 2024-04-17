package com.iorgana.vpngate_client.listener;

import com.iorgana.vpngate_client.model.Server;

import java.util.List;

public interface IClientListener {
    void onStart();
    void onComplete(List<Server> servers);
    void onFailure(String errorMsg);
    void onFailure(int errorCode);
}
