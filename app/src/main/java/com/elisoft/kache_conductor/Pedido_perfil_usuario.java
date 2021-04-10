package com.elisoft.kache_conductor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;


public class Pedido_perfil_usuario extends AppCompatActivity implements View.OnClickListener {
    EditText nombre, indicacion;

    TextView celular;
    ImageView perfil;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_perfil_usuario);
        nombre = (EditText) findViewById(R.id.nombre);
        indicacion = (EditText) findViewById(R.id.indicacion);
        celular = (TextView) findViewById(R.id.celular);
        perfil = (ImageView) findViewById(R.id.perfil);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences preferencias = getSharedPreferences("ultimo_pedido_conductor", Context.MODE_PRIVATE);

        nombre.setText(preferencias.getString("nombre_usuario", ""));
        celular.setText(preferencias.getString("celular", ""));
        indicacion.setText(preferencias.getString("indicacion", ""));
        getImage(preferencias.getString("id_usuario", "0"));

        celular.setOnClickListener(this);
    }

    private void getImage(String id)//
    {
        class GetImage extends AsyncTask<String, Void, Bitmap> {
            ImageView bmImage;
            ProgressDialog loading;

            public GetImage(ImageView bmImage) {
                this.bmImage = bmImage;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                loading.dismiss();

                Drawable dw = new BitmapDrawable(getResources(), bitmap);
                //se edita la imagen para ponerlo en circulo.

                if (bitmap == null) {
                    dw = getResources().getDrawable(R.mipmap.ic_perfil);
                }

                imagen_circulo(dw, bmImage);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Pedido_perfil_usuario.this, "Descargando Imagen", "Espere Porfavor...", true, true);
            }

            @Override
            protected Bitmap doInBackground(String... strings) {
                String url = getString(R.string.servidor) + "frmTaxi.php?opcion=get_imagen_usuario&id_usuario=" + strings[0];//hace consulta ala Bd para recurar la imagen
                Drawable d = getResources().getDrawable(R.mipmap.ic_perfil);
                Bitmap mIcon = drawableToBitmap(d);
                try {
                    InputStream in = new java.net.URL(url).openStream();
                    mIcon = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                }
                return mIcon;
            }
        }

        GetImage gi = new GetImage(perfil);
        gi.execute(id);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public void imagen_circulo(Drawable id_imagen, ImageView imagen) {
        Bitmap originalBitmap = ((BitmapDrawable) id_imagen).getBitmap();
        if (originalBitmap.getWidth() > originalBitmap.getHeight()) {
            originalBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getHeight(), originalBitmap.getHeight());
        } else if (originalBitmap.getWidth() < originalBitmap.getHeight()) {
            originalBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getWidth());
        }

//creamos el drawable redondeado
        RoundedBitmapDrawable roundedDrawable =
                RoundedBitmapDrawableFactory.create(getResources(), originalBitmap);

//asignamos el CornerRadius
        roundedDrawable.setCornerRadius(originalBitmap.getWidth());

        imagen.setImageDrawable(roundedDrawable);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.celular) {
            try {
                SharedPreferences preferencias = getSharedPreferences("ultimo_pedido_conductor", Context.MODE_PRIVATE);
                Intent llamada = new Intent(Intent.ACTION_CALL);
                llamada.setData(Uri.parse("tel:" + preferencias.getString("celular", "")));
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(llamada);
            }catch (Exception e)
            {

            }
        }
    }

}
