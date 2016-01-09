package com.betrisey.suzanne.androidproject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.betrisey.suzanne.dondesang.backend.cDonneurApi.model.CDonneurBack;

import db.adapter.DonneurDataSource;
import db.object.CDonneur;

public class Parametre extends AppCompatActivity {

    Activity activity;
    Locale myLocale;
    DonneurDataSource da;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametre);
        //ajouter icone a la barre d'action
        getSupportActionBar().setHomeButtonEnabled(true);

        activity = this;

        Resources res = getResources();
        Configuration conf = res.getConfiguration();

        String lang = conf.locale.getLanguage();
        lang = lang.substring(0,2);



        RadioButton radioButton;

        if(lang.equals("de"))
        {
            radioButton = (RadioButton) findViewById(R.id.radioAllemand);
            radioButton.setChecked(true);
        }
        else
        {
            radioButton = (RadioButton) findViewById(R.id.radioFrancais);
            radioButton.setChecked(true);
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
        String region = preferences.getString("region", "Brig");

        Spinner spinner = (Spinner) findViewById(R.id.spinnerRegions);
        String[] regions = getResources().getStringArray(R.array.region);

        //Récupère la bonne région par défaut
        for(int i = 0; i<regions.length; i++) {
            if (region.equals(regions[i].toString())) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_parametre, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

   /* public void sync (View view) {

       // new EndpointsAsyncTask().execute(new Pair<Context, String>(this, "Manfred"));
    }*/

    public void sync (View v) {
        new EndpointsAsyncTaskHi().execute(new Pair<Context, String>(this, "Manfred"));

        CDonneurBack donneur = new CDonneurBack();
        da = new DonneurDataSource(getApplicationContext());
        try {
            donneur = da.getDonneurByIdBack(1);
        } catch (ParseException e) {
            e.printStackTrace();
        /*Phone phoneWork = new Phone();
        phoneWork.setType("Work");
        phoneWork.setNumber("079 123 45 67");

        Phone phonePrivate = new Phone();
        phonePrivate.setType("Private");
        phonePrivate.setNumber("079 876 54 32");

        List<Phone> list = new ArrayList<Phone>();

        list.add(phonePrivate);
        list.add(phoneWork);

        employee.setPhones(list);*/

            new EndpointsAsyncTask(donneur).execute();
        }
    }

    public void buttonOk (View view){

        RadioButton rb;

        rb = (RadioButton) findViewById(R.id.radioFrancais);

        if(rb.isChecked())
        {
            setLocale("fr");
        }
        else
        {
            setLocale("de");
        }

        //Enregistre la préférence pour la région
        Spinner spinner = (Spinner) findViewById(R.id.spinnerRegions);
        String region = spinner.getSelectedItem().toString();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("region", region);
        editor.commit();

        activity.finish();

    }


    public void buttonAnnuler (View view){
        activity.finish();
    }

    public void setLocale(String lang) {

        myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
}
