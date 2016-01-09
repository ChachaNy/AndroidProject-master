package com.betrisey.suzanne.androidproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import db.adapter.InterventionDataSource;
import db.adapter.SangDataSource;
import db.object.CIntervention;
import db.object.CSang;

public class AfficherIntervention extends AppCompatActivity {

    private int id;
    InterventionDataSource ia;
    SangDataSource sa;
    List<CSang> listSang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_intervention);

        //ajouter icone a la barre d'action
        getSupportActionBar().setHomeButtonEnabled(true);


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

        ia = new InterventionDataSource(getApplicationContext());
        CIntervention i = null;
        try {
            i = ia.getInterventiononById(id);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Create the text view
        TextView tw = (TextView) findViewById(R.id.textViewDate);
        try {
            tw.setText(changeIntoString(i.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tw = (TextView) findViewById(R.id.textViewQuantite);
        tw.setText(String.valueOf(i.getQuantite()));
        tw = (TextView) findViewById(R.id.textViewGroupe);
        tw.setText(i.getGroupe());
        tw = (TextView) findViewById(R.id.textViewDescription);
        tw.setText(i.getDescription());
        tw = (TextView) findViewById(R.id.textViewRegion);
        tw.setText(i.getRegion());

        LinearLayout layout = (LinearLayout) findViewById(R.id.pochettes);
        sa = new SangDataSource(this);
        try {
            listSang = sa.getAllSangsByIntervention(i.getId());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for(int j = 0; j<listSang.size(); j++){
            TextView text = new TextView(getApplicationContext());
            text.setText(getResources().getString(R.string.nrpochette) + String.valueOf(listSang.get(j).getId()));
            text.setTextSize(18);
            text.setTextColor(getResources().getColor(R.color.black));
            layout.addView(text);
        }



    }

    public void buttonDelete(View view) throws ParseException {
        Intent intent = new Intent(this, Intervention.class);
        ia.deleteIntervention(id);
        startActivity(intent);
    }

    public void buttonEdit(View view) {
        Intent intent = new Intent(this, ModifierIntervention.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public String changeIntoString(Date d) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.FRENCH);
        String s = df.format(d);
        return s;
    }
}
