package com.elisoft.kache_conductor.solicitudes;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.elisoft.kache_conductor.R;

import com.squareup.picasso.Picasso;



import java.util.ArrayList;

public class Items_solicitud  extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<CSolicitud> items;
    Bundle savedInstanceState;


    public Items_solicitud(Activity activity, ArrayList<CSolicitud> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<CSolicitud> Pedidos) {
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
            v = inf.inflate(R.layout.item_solicitud, null);
        }

        CSolicitud ped = items.get(position);

        TextView tv_nombre= (TextView) v.findViewById(R.id.tv_nombre);
        TextView tv_monto_carrito= (TextView) v.findViewById(R.id.tv_monto_carrito);
        TextView tv_direccion = (TextView) v.findViewById(R.id.tv_direccion);
        TextView tv_direccion_inicio = (TextView) v.findViewById(R.id.tv_direccion_pedido);
        TextView tv_fecha = (TextView) v.findViewById(R.id.tv_fecha);
        TextView tv_estado = (TextView) v.findViewById(R.id.tv_estado);
        TextView tv_clase_vehiculo = (TextView) v.findViewById(R.id.tv_clase_vehiculo);
        TextView tv_conductor = (TextView) v.findViewById(R.id.tv_conductor);
        TextView tv_vehiculo = (TextView) v.findViewById(R.id.tv_vehiculo);
        TextView tv_distancia = (TextView) v.findViewById(R.id.tv_distancia);
        de.hdodenhof.circleimageview.CircleImageView im_perfil_pasajero=v.findViewById(R.id.im_perfil_pasajero);
        LinearLayout ll_item=v.findViewById(R.id.ll_item);


        /*
         * 2=pedido finalizado correctamente.
         * 3=pedido cancelado por el usuario
         * 4=pedido cancelado por el usuario.
         * 5=pedido cancelado por el taxista por alguna razon.
         * */

        tv_nombre.setText(ped.getNombre_usuario());
        tv_direccion.setText(ped.getDireccion());
        tv_direccion_inicio.setText(String.valueOf(ped.getDireccion_inicio()));
        tv_fecha.setText(ped.getFecha_pedido());

        if(ped.getClase_vehiculo().equals("5")) {
            tv_monto_carrito.setText(ped.getMonto_pedido() + " Bs");
        }else{
            tv_monto_carrito.setText("");
        }

        String sdistancia="";
        double distancia=Double.parseDouble(ped.getDistancia());
        if(distancia>=1000)
        {
          distancia=distancia/1000;
          sdistancia=String.valueOf(distancia)+" Km";
        }else
        {
            sdistancia=ped.getDistancia()+" Mts";
        }

        tv_distancia.setText("");





        String clase_vehiculo=ped.getClase_vehiculo();
        switch (clase_vehiculo){
            case "1": tv_clase_vehiculo.setText(this.activity.getString(R.string.taxi_normal));break;
            case "2": tv_clase_vehiculo.setText(this.activity.getString(R.string.taxi_lujo));break;
            case "3": tv_clase_vehiculo.setText(this.activity.getString(R.string.taxi_con_aire));break;
            case "4": tv_clase_vehiculo.setText(this.activity.getString(R.string.taxi_con_maletero));break;
            case "5": tv_clase_vehiculo.setText(this.activity.getString(R.string.taxi_delivery));break;
            case "6": tv_clase_vehiculo.setText("RESERVA DE UN MOVIL");break;
            case "7": tv_clase_vehiculo.setText("MOTO");break;
            case "8": tv_clase_vehiculo.setText("MOTO PARA PEDIDOS");break;
            case "11": tv_clase_vehiculo.setText(this.activity.getString(R.string.taxi_con_parrilla));break;
            case "15": tv_clase_vehiculo.setText(this.activity.getString(R.string.taxi_camioneta));break;
        }

        if(ped.getEstado().equals("100"))
        {
            try {
                tv_estado.setText(String.valueOf("☼"+ped.getTiempo()));
                tv_estado.setBackgroundResource(R.drawable.bk_cancelado);
                tv_estado.setTextColor(Color.parseColor("#536DFE"));
                ll_item.setBackgroundResource(R.color.colorSinAceptar);
                tv_distancia.setText("~"+sdistancia);
            }catch (Exception e)
            {
                Log.e("Estado",e.toString());
            }
        }else
        if(ped.getEstado().equals("0")&& ped.getNombre_conductor().equals(""))
        {
            try {
                tv_estado.setText(String.valueOf("SIN ACEPTAR"));
                tv_estado.setBackgroundResource(R.drawable.bk_cancelado);
                tv_estado.setTextColor(Color.parseColor("#536DFE"));
                ll_item.setBackgroundResource(R.color.colorCompletado);
            }catch (Exception e)
            {
                Log.e("Estado",e.toString());
            }
        }
        else
        if(ped.getEstado().equals("0"))
        {
            try {
                tv_estado.setText(String.valueOf("ACEPTADO"));
                tv_estado.setBackgroundResource(R.drawable.bk_cancelado);
                tv_estado.setTextColor(Color.parseColor("#536DFE"));
                ll_item.setBackgroundResource(R.color.colorCompletado);
                tv_conductor.setText("("+ped.getNumero_movil()+")- ."+ ped.getCalificacion_conductor());
                tv_vehiculo.setText(ped.getCalificacion_vehiculo());
                ll_item.setBackgroundResource(R.color.colorCompletado);
            }catch (Exception e)
            {
                Log.e("Estado",e.toString());
            }
        }else if(ped.getEstado().equals("1"))
        {
            try {
                tv_estado.setText(String.valueOf("EN CARRERA"));
                tv_estado.setBackgroundResource(R.drawable.bk_completado);
                tv_estado.setTextColor(Color.parseColor("#536DFE"));
                tv_conductor.setText("("+ped.getNumero_movil()+")- ."+ ped.getCalificacion_conductor());
                tv_vehiculo.setText(ped.getCalificacion_vehiculo());
                ll_item.setBackgroundResource(R.color.colorCompletado);
            }catch (Exception e)
            {
                Log.e("Estado",e.toString());
            }
        }else if(ped.getEstado().equals("2"))
        {
            try {
                tv_estado.setText(String.valueOf("COMPLETADA"));
                tv_estado.setBackgroundResource(R.drawable.bk_completado);
                tv_estado.setTextColor(Color.parseColor("#536DFE"));
                tv_conductor.setText("("+ped.getNumero_movil()+")- ."+ped.getCalificacion_conductor());
                tv_vehiculo.setText(ped.getCalificacion_vehiculo());
                ll_item.setBackgroundResource(R.color.colorCompletado);
            }catch (Exception e)
            {
                Log.e("Estado",e.toString());
            }
        }
        else if(ped.getEstado().equals("3")||ped.getEstado().equals("4"))
        {try {
            tv_estado.setText(String.valueOf("CANCELO"));
            tv_estado.setBackgroundResource(R.drawable.bk_cancelado);
            tv_estado.setTextColor(Color.parseColor("#fc0101"));
            tv_conductor.setText("("+ped.getNumero_movil()+")- ."+ped.getCalificacion_conductor());
            tv_vehiculo.setText(ped.getCalificacion_vehiculo());
            ll_item.setBackgroundResource(R.color.colorCompletado);
        }catch (Exception e)
        {
            Log.e("Estado cancelo",e.toString());
        }
        }
        else if(ped.getEstado().equals("5"))
        {
            tv_estado.setText(String.valueOf("CANCELE"));
            tv_estado.setBackgroundResource(R.drawable.bk_cancelado);
            tv_estado.setTextColor(Color.parseColor("#fc0101"));
            tv_conductor.setText("("+ped.getNumero_movil()+")- ."+ped.getCalificacion_conductor());
            tv_vehiculo.setText(ped.getCalificacion_vehiculo());
            ll_item.setBackgroundResource(R.color.colorCompletado);
        }


        String  url=  this.activity.getString(R.string.servidor)+"usuario/imagen/perfil/"+ped.getId_usuario()+"_perfil.png";

        Picasso.with(this.activity).load(url).placeholder(R.drawable.ic_perfil_plomo).into(im_perfil_pasajero);

        return v;
    }


}


