package com.elisoft.kache_conductor.historial;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.elisoft.valle_grande_conductor.R;

import java.util.List;

/**
 * Created by elisoft on 19-05-17.
 */


public class Item_historial extends ArrayAdapter<CPedido_taxi> {
    public Item_historial(Context context, List<CPedido_taxi> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener inflater.
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ¿Existe el view actual?
        if (null == convertView) {
            convertView = inflater.inflate(
                    R.layout.item_pedido,
                    parent,
                    false);
        }


        CPedido_taxi ped =  getItem(position);

        TextView tv_nombre= (TextView) convertView.findViewById(R.id.tv_nombre);
        TextView tv_direccion = (TextView) convertView.findViewById(R.id.tv_direccion);
        TextView tv_direccion_inicio = (TextView) convertView.findViewById(R.id.tv_direccion_pedido);
        TextView tv_fecha = (TextView) convertView.findViewById(R.id.tv_fecha);
        TextView tv_estado = (TextView) convertView.findViewById(R.id.tv_estado);
        TextView tv_clase_vehiculo = (TextView) convertView.findViewById(R.id.tv_clase_vehiculo);
        RatingBar rb_conductor = (RatingBar) convertView.findViewById(R.id.rb_conductor);
        RatingBar rb_vehiculo = (RatingBar) convertView.findViewById(R.id.rb_vehiculo);


/*
* 2=pedido finalizado correctamente.
* 3=pedido cancelado por el usuario
* 4=pedido cancelado por el usuario.
* 5=pedido cancelado por el taxista por alguna razon.
* */

        tv_nombre.setText(ped.getNombre());
        tv_direccion.setText(ped.getIndicacion());
        tv_direccion_inicio.setText(String.valueOf(ped.getDescripcion()));
        tv_fecha.setText(ped.getFecha_pedido());




        int clase_vehiculo=ped.getClase_vehiculo();
        switch (clase_vehiculo){
            case 1: tv_clase_vehiculo.setText(getContext().getString(R.string.taxi_normal));break;
            case 2: tv_clase_vehiculo.setText(getContext().getString(R.string.taxi_lujo));break;
            case 3: tv_clase_vehiculo.setText(getContext().getString(R.string.taxi_con_aire));break;
            case 4: tv_clase_vehiculo.setText(getContext().getString(R.string.taxi_con_maletero));break;
            case 5: tv_clase_vehiculo.setText(getContext().getString(R.string.taxi_delivery));break;
            case 6: tv_clase_vehiculo.setText("RESERVA DE UN MOVIL");break;
            case 7: tv_clase_vehiculo.setText("MOTO");break;
            case 8: tv_clase_vehiculo.setText("MOTO PARA PEDIDOS");break;
            case 11: tv_clase_vehiculo.setText(getContext().getString(R.string.taxi_con_parrilla));break;
            case 15: tv_clase_vehiculo.setText(getContext().getString(R.string.taxi_camioneta));break;
        }



        if(ped.getEstado_pedido()==2)
        {
            try {
                tv_estado.setText(ped.getMonto_total()+" Bs");
                tv_estado.setBackgroundResource(R.drawable.bk_completado);
                tv_estado.setTextColor(Color.parseColor("#536DFE"));
                rb_conductor.setRating(ped.getCalificacion_conductor());
                rb_vehiculo.setRating(ped.getCalificacion_vehiculo());
            }catch (Exception e)
            {
                Log.e("Estado",e.toString());
            }
        }
        else if(ped.getEstado_pedido()==3||ped.getEstado_pedido()==4)
        {try {
            tv_estado.setText(String.valueOf("CANCELO"));
            tv_estado.setBackgroundResource(R.drawable.bk_cancelado);
            tv_estado.setTextColor(Color.parseColor("#fc0101"));
        }catch (Exception e)
        {
            Log.e("Estado cancelo",e.toString());
        }
        }
        else if(ped.getEstado_pedido()==5)
        {
            tv_estado.setText(String.valueOf("CANCELE"));
            tv_estado.setBackgroundResource(R.drawable.bk_cancelado);
            tv_estado.setTextColor(Color.parseColor("#fc0101"));
        }




        return convertView;
    }
}