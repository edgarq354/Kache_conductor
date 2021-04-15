package com.elisoft.kache_conductor.animacion.login_inicio.login.login;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import com.elisoft.kache_conductor.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

public class SignUpFragment extends Fragment implements OnSignUpListener{

    private static final String TAG = "SignUpFragment";
    public  EditText et_usuario,et_contrasenia;




    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_signup, container, false);
        et_usuario=inflate.findViewById(R.id.et_usuario);
        et_contrasenia=inflate.findViewById(R.id.et_contrasenia);

        SharedPreferences perfil=getContext().getSharedPreferences("login",MODE_PRIVATE);
        et_usuario.setText(perfil.getString("usuario",""));
        et_contrasenia.setText(perfil.getString("contrasenia",""));

        et_usuario.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences perfil=getContext().getSharedPreferences("login",MODE_PRIVATE);
                SharedPreferences.Editor editor=perfil.edit();
                editor.putString("usuario",s.toString());
                editor.commit();
            }
        });

        et_contrasenia.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences perfil=getContext().getSharedPreferences("login",MODE_PRIVATE);
                SharedPreferences.Editor editor=perfil.edit();
                editor.putString("contrasenia",s.toString());
                editor.commit();
            }
        });










        return inflate;
    }

    @Override
    public void signUp() {
        Toast.makeText(getContext(), "Sign up"+et_usuario.getText(), Toast.LENGTH_SHORT).show();
    }



}
