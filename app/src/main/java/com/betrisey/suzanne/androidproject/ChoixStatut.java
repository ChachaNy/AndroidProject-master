package com.betrisey.suzanne.androidproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.lang.reflect.Field;
import java.text.ParseException;

import db.adapter.SangDataSource;
import db.object.CSang;

public class ChoixStatut extends AppCompatActivity {

    CSang sang;
    SangDataSource sa;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_statut);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //pour le menu (marche pas sur toutes les versions d'android sinon)
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception ex) {
            // Ignore
        }

        // Get the message from the intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
        }

        sa = new SangDataSource(getApplicationContext());
        try {
            sang = sa.getSangById(id);
        } catch (ParseException e) {
            e.printStackTrace();
        }

            switch (sang.getStatut()) {

            case ("en stock"):
                RadioButton radioBtnStock = (RadioButton) findViewById(R.id.radioBtnStock);
                radioBtnStock.setChecked(true);
                break;
            case ("transfert"):
                RadioButton radioBtnTransfert = (RadioButton) findViewById(R.id.radioBtnTransfert);
                radioBtnTransfert.setChecked(true);
                break;
            case ("inutilisable"):
                RadioButton radioBtnUtilisable = (RadioButton) findViewById(R.id.radioBtnUtilisable);
                radioBtnUtilisable.setChecked(true);
                break;
            case ("commandé"):
                RadioButton radioBtnCommande = (RadioButton) findViewById(R.id.radioBtnCommande);
                radioBtnCommande.setChecked(true);
                break;
            case ("utilisé"):
                RadioButton radioBtnUtilise = (RadioButton) findViewById(R.id.radioBtnUtilise);
                radioBtnUtilise.setChecked(true);
                break;
        }

    }

    public void buttonAnnuler(View view) {
        Intent intent = new Intent(this, AffichagePochetteSang.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void buttonOk(View view) throws ParseException {

        RadioButton radioBtnStock = (RadioButton) findViewById(R.id.radioBtnStock);
        RadioButton radioBtnTransfert = (RadioButton) findViewById(R.id.radioBtnTransfert);
        RadioButton radioBtnUtilisable = (RadioButton) findViewById(R.id.radioBtnUtilisable);
        RadioButton radioBtnCommande = (RadioButton) findViewById(R.id.radioBtnCommande);
        RadioButton radioBtnUtilise = (RadioButton) findViewById(R.id.radioBtnUtilise);

        if(radioBtnUtilise.isChecked()){
            sang.setStatut("utilisé");
        }else if(radioBtnStock.isChecked()){
            sang.setStatut("en stock");
        }else if(radioBtnTransfert.isChecked()){
            sang.setStatut("transfert");
        }else if(radioBtnCommande.isChecked()){
            sang.setStatut("commandé");
        }else if(radioBtnUtilisable.isChecked()){
            sang.setStatut("inutilisable");
        }

        sa.updateSang(sang);

        Intent intent = new Intent(this, AffichagePochetteSang.class);
        intent.putExtra("id", id);
        startActivity(intent);

    }
}
