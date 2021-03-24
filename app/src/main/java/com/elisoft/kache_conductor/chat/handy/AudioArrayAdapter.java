package com.elisoft.kache_conductor.chat.handy;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.elisoft.valle_grande_conductor.R;
import com.elisoft.kache_conductor.Suceso;
import java.util.ArrayList;
import java.util.List;

class AudioArrayAdapter extends ArrayAdapter<CAudio> {

    private TextView chatText;
    private List<CAudio> CMensajeList = new ArrayList<CAudio>();
    private Context context;
    Suceso suceso;
    int posicion=0;




    @Override
    public void add(CAudio object) {
        CMensajeList.add(object);
        super.add(object);
    }

    public AudioArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    public int getCount() {
        return this.CMensajeList.size();
    }

    public CAudio getItem(int index) {
        return this.CMensajeList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        CAudio CMensajeObj = getItem(position);
        posicion=position;

        View row = convertView;

        TextView fecha;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (CMensajeObj.left) {
            row = inflater.inflate(R.layout.derecha_audio, parent, false);
        }else{
            row = inflater.inflate(R.layout.derecha_audio, parent, false);
        }
        chatText = (TextView) row.findViewById(R.id.msgr);
        fecha = (TextView) row.findViewById(R.id.fecha);
        chatText.setText(CMensajeObj.titulo);
        fecha.setText(CMensajeObj.hora);


        return row;
    }


}