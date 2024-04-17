package com.iorgana.vpngateclient.test_demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iorgana.vpngateclient.R;
import com.iorgana.vpngate_client.model.Server;

import java.util.ArrayList;
import java.util.List;

public class ServersListAdapter extends  RecyclerView.Adapter<ServersListAdapter.ViewHolder> {
    private final List<Server> serverList = new ArrayList<>();
    private final Context context;

    /**
     * Constructor
     */
    public ServersListAdapter(Context context){
        this.context = context;
    }

    /**
     * Set List Items
     * -------------------------------------------------------------
     */
    @SuppressLint("NotifyDataSetChanged")
    public void setListItems(List<Server> serversList){
        this.serverList.clear();
        this.serverList.addAll(serversList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_server_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Server server = serverList.get(position);

        holder.ip.setText(server.getIpAddress());
        holder.host.setText(server.getHostName());
        holder.port.setText(String.valueOf(server.getPort()));
        holder.protocol.setText(server.getProtocol());

    }

    @Override
    public int getItemCount() {
        return serverList.size();
    }


    /*########################### ViewHolder Class #######################*/
    /**
     * ViewHolder class
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout itemContainer;
        TextView ip;
        TextView host;
        TextView port;
        TextView protocol;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize vars from itemView passed from onCreateViewHolder()
            itemContainer = itemView.findViewById(R.id.serverContainer);
            host = itemView.findViewById(R.id.txtHost);
            ip = itemView.findViewById(R.id.txtIp);
            port = itemView.findViewById(R.id.txtPort);
            protocol = itemView.findViewById(R.id.txtProtocol);

        }
    }
}
