package com.iescamas.settings;

import android.app.AlertDialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.google.android.material.appbar.MaterialToolbar;

import java.util.Arrays;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

     TextView lbl_descanso,lbl_alarma,lbl_pantalla,lbl_fuente,lbl_oscuro,lbl_datosMoviles,
                    lbl_descarga,lbl_restrictivo,lbl_estadisticas;

     MaterialToolbar materialToolbar;
     SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponent();
        limpiarPreferenciasAntiguas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mostrarInfo(sharedPreferences);
    }



    private void initComponent(){
        materialToolbar = findViewById(R.id.materialToolbar);
        setSupportActionBar(materialToolbar);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        lbl_descanso = findViewById(R.id.lbl_descanso);
        lbl_alarma = findViewById(R.id.lbl_alarma);
        lbl_pantalla = findViewById(R.id.lbl_pantalla);
        lbl_fuente = findViewById(R.id.lbl_fuente);
        lbl_oscuro = findViewById(R.id.lbl_oscuro);
        lbl_datosMoviles = findViewById(R.id.lbl_datosMoviles);
        lbl_descarga = findViewById(R.id.lbl_descarga);
        lbl_restrictivo = findViewById(R.id.lbl_restrictivo);
        lbl_estadisticas = findViewById(R.id.lbl_estadisticas);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.item_Borrar){
            reseteaConfig();
        }
        if (item.getItemId()==R.id.item_Config){
            cambiaIntent();
        }
        return true;
    }

    private void reseteaConfig(){

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_AD))
                .setMessage(getString(R.string.mesage_AD))
                .setPositiveButton(getString(R.string.positive_AD), (dialog, which) -> {
                    valoresDefault();
                })
                .setNegativeButton(getString(R.string.negative_AD), null)
                .create()
                .show();
    }

    private void valoresDefault() {
       SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
       SharedPreferences.Editor editor = sharedPreferences.edit();
       editor.putString("descanso","1 hora");
       editor.putString("alarma","¡A descansar!");
       editor.putString("pantalla","Nunca");
       editor.putFloat("fuente",5.0f);
       editor.putBoolean("oscuro",false);
       editor.putStringSet("datos_Moviles",new HashSet<>(Arrays.asList("En cualquier red")));
       editor.putStringSet("descarga",new HashSet<>(Arrays.asList("360p","original")));
       editor.putBoolean("restrictivo",true);
       editor.putBoolean("estadisticas",true);
       editor.apply();

       mostrarInfo(sharedPreferences);

    }

    private void cambiaIntent(){
        Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
        startActivity(intent);
    }

    private void mostrarInfo(SharedPreferences sharedPreferences) {
        lbl_descanso.setText(sharedPreferences.getString("descanso", "1 hora"));
        lbl_alarma.setText(sharedPreferences.getString("alarma", "¡A descansar!"));
        lbl_pantalla.setText(sharedPreferences.getString("pantalla", "Nunca"));

        try {
            lbl_fuente.setText(String.valueOf(sharedPreferences.getFloat("fuente", 5.0f)));
        } catch (ClassCastException e) {
            int fuenteInt = sharedPreferences.getInt("fuente", 5);
            float fuenteFloat = (float) fuenteInt;

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putFloat("fuente", fuenteFloat);
            editor.apply();

            lbl_fuente.setText(String.valueOf(fuenteFloat));
        }
        lbl_oscuro.setText(sharedPreferences.getBoolean("oscuro", false) ? "Activado" : "Desactivado");
        lbl_restrictivo.setText(sharedPreferences.getBoolean("restrictivo", true) ? "Habilitado" : "Deshabilitado");
        lbl_estadisticas.setText(sharedPreferences.getBoolean("estadisticas", true) ? "Habilitado" : "Deshabilitado");
        lbl_datosMoviles.setText(String.join(", ", sharedPreferences.getStringSet("datos_Moviles", new HashSet<>())));
        lbl_descarga.setText(String.join(", ", sharedPreferences.getStringSet("descarga", new HashSet<>())));
    }

    private void limpiarPreferenciasAntiguas() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }






}
