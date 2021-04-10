package com.elisoft.kache_conductor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class Informacion extends AppCompatActivity implements View.OnClickListener{
    Button pagina,terminos;
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pagina=(Button)findViewById(R.id.pagina);
        terminos=(Button)findViewById(R.id.terminos);


        pagina.setOnClickListener(this);
        terminos.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.pagina)
        {
            Uri uri = Uri.parse("http://www.radiotaxilacarroza.com/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        else if(v.getId()==R.id.terminos)
        {
            Uri uri = Uri.parse("http://www.radiotaxilacarroza.com/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    private void enviar(String[] to, String[] cc,
                        String asunto, String mensaje) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_CC, cc);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, asunto);
        emailIntent.putExtra(Intent.EXTRA_TEXT, mensaje);
        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent, "Email "));
    }
}
