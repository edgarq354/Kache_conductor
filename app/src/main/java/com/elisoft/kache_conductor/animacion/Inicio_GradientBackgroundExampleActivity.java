package com.elisoft.kache_conductor.animacion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import com.elisoft.kache_conductor.animacion.login_inicio.login.InicioAnimacion;
import com.elisoft.kache_conductor.Menu_taxi;
import com.elisoft.kache_conductor.R;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;

public class Inicio_GradientBackgroundExampleActivity extends AhoyOnboarderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (verificar_login_taxi()) {
            startActivity(new Intent(this, Menu_taxi.class));
            finish();
        }

        AhoyOnboarderCard ahoyOnboarderCard1 = new AhoyOnboarderCard("Bienvenido a Kache", "Disfruta conduciendo con nosotros y genera buenos ingresos por ello.", R.drawable.onboarding1);
        AhoyOnboarderCard ahoyOnboarderCard2 = new AhoyOnboarderCard("Tu Personalidad", "Tu personalidad hara del viaje más placentero, trata de ser amable y cortes recuerda que una buena conversación hace el viaje mas corto y divertido.", R.drawable.onboarding2);
        AhoyOnboarderCard ahoyOnboarderCard3 = new AhoyOnboarderCard("Tu Herramienta", "Tu vehículo es tu herramienta de trabajo mantenlo en buen estado, eso te permitira tener mejor puntuación y te dara ventaja a la hora de aceptar viajes.", R.drawable.onboarding3);
        AhoyOnboarderCard ahoyOnboarderCard4 = new AhoyOnboarderCard("Tu guía", "Tu teléfono es tu guía mediante las ayudas de gps y red de datos te ayuda a dar con tu pasajero de la forma mas rapida y segura.", R.drawable.onboarding4);

        ahoyOnboarderCard1.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard2.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard3.setBackgroundColor(R.color.black_transparent);
        ahoyOnboarderCard4.setBackgroundColor(R.color.black_transparent);

        List<AhoyOnboarderCard> pages = new ArrayList<>();

        pages.add(ahoyOnboarderCard1);
        pages.add(ahoyOnboarderCard2);
        pages.add(ahoyOnboarderCard3);
        pages.add(ahoyOnboarderCard4);

        for (AhoyOnboarderCard page : pages) {
            page.setTitleColor(R.color.white);
            page.setDescriptionColor(R.color.grey_200);
            //page.setTitleTextSize(dpToPixels(12, this));
            //page.setDescriptionTextSize(dpToPixels(8, this));
            //page.setIconLayoutParams(width, height, marginTop, marginLeft, marginRight, marginBottom);
        }

        setFinishButtonTitle("Comenzamos");
        showNavigationControls(true);
        setGradientBackground();

        //set the button style you created
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setFinishButtonDrawableStyle(ContextCompat.getDrawable(this, R.drawable.rounded_button));
        }

        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        setFont(face);

        setOnboardPages(pages);
    }

    @Override
    public void onFinishButtonPressed() {
        startActivity(new Intent(getApplicationContext(), InicioAnimacion.class));
    }


    public boolean verificar_login_taxi()
    {
        SharedPreferences perfil=getSharedPreferences("perfil_conductor",MODE_PRIVATE);
        return (perfil.getString("login_taxi","").equals("1"));

    }
}
