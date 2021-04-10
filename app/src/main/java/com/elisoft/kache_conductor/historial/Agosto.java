package com.elisoft.kache_conductor.historial;

/**
 * Created by elisoft on 19-05-17.
 */

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.elisoft.kache_conductor.R;
import com.elisoft.kache_conductor.carreras.Carreras;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;

public class Agosto  extends Fragment {
    ArrayList<CPedido_taxi> historial;
    ListView lv_lista;
    TextView tv_error;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tabs_diciembre, container, false);
        tv_error = (TextView) rootView.findViewById(R.id.tv_error);
        lv_lista = (ListView) rootView.findViewById(R.id.lv_lista);
        Item_historial adaptador = new Item_historial(getActivity(),historial);
        lv_lista.setAdapter(adaptador);

        if(historial.size()>0)
        {
            tv_error.setText("");
        }
        else
        {
            tv_error.setText("No tienes ningun pedido.");
        }
        lv_lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(historial.get(i).getEstado_pedido()==2){
                    CPedido_taxi hi=new CPedido_taxi();
                    hi=historial.get(i);
                    mensaje(hi);
                }

            }
        });

        return rootView;
    }
    public void cargar_lista(ArrayList<CPedido_taxi> cPedido_taxis)
    {
        historial=cPedido_taxis;

    }
    public void mensaje(CPedido_taxi historial)
    {
        try{
            Intent i=new Intent(getActivity(),Carreras.class);

            i.putExtra("id_pedido",String.valueOf(historial.getId()));
            startActivity(i);
        }catch (Exception e)
        {
            Log.e("carrera",e.toString());
        }
    }
}