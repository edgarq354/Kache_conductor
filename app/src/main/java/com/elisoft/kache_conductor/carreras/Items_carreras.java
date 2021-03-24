package com.elisoft.kache_conductor.carreras;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.elisoft.valle_grande_conductor.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;


/**
 * Created by ELIO on 28/10/2016.
 */

public  class Items_carreras extends BaseAdapter  {

    String url_pagina;
    String nombre;
    protected Activity activity;
    protected ArrayList<CCarrera> items;
    private Context mContext;
    ImageView im_mapa;

    CCarrera ped;
    TextView tv_numero;

    public Items_carreras(Context c,  Activity activity, ArrayList<CCarrera> items) {
        this.activity = activity;
        this.items = items;
        this.mContext=c;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<CCarrera> Pedidos) {
        for (int i = 0; i < Pedidos.size(); i++) {
            items.add(Pedidos.get(i));
        }
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_carreras, null);
        }
        ped = items.get(position);



        TextView fecha = (TextView) v.findViewById(R.id.fecha);
        TextView monto = (TextView) v.findViewById(R.id.monto);
         tv_numero = (TextView) v.findViewById(R.id.tv_numero);
        ImageView im_mapa=(ImageView)v.findViewById(R.id.im_mapa);
        ImageView ib_conductor=(ImageView)v.findViewById(R.id.ib_conductor);
        TextView tv_distancia=(TextView)v.findViewById(R.id.tv_distancia);
        TextView tv_direccion_inicio = (TextView) v.findViewById(R.id.tv_direccion_inicio);
        TextView tv_direccion_fin = (TextView) v.findViewById(R.id.tv_direccion_fin);


        WebView wv_mapa = (WebView)v.findViewById(R.id.wv_mapa);
        String url=mContext.getString(R.string.servidor)+"ver_carrera.php?id_carrera="+ped.getId()+"&id_pedido="+ped.getId_pedido();
        // wv_mapa.loadUrl(url);
        //wv_mapa.loadUrl("https://universoandroidstudio.blogspot.com/2016/");
        wv_mapa.getSettings().setJavaScriptEnabled(true);
        wv_mapa.setWebViewClient(new WebViewClient());
        wv_mapa.loadUrl(url);

        this.im_mapa=im_mapa;

        String  url1=  "usuario/imagen/perfil/"+String.valueOf(ped.getId_usuario())+"_perfil.png";
        Picasso.with(mContext).load(mContext.getString(R.string.servidor)+url1).placeholder(R.mipmap.ic_perfil).into(ib_conductor);

        //poner en el String todos los puntos registrados.....
        boolean sw=true;

        try {
            int id = ped.getId_pedido();
        }catch (Exception e)
        {
            sw=false;
        }
        if(sw==true) {
            fecha.setText(ped.getFecha_fin());
            monto.setText(ped.getMonto()+" Bs.");
            tv_distancia.setText( ped.getDistancia()+" mt.");

            tv_direccion_inicio.setText(ped.getDireccion_inicio());
            tv_direccion_fin.setText(ped.getDireccion_fin());
        }




        return v;
    }



}