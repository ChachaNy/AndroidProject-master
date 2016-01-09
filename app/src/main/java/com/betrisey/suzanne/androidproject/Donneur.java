package com.betrisey.suzanne.androidproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.prefs.Preferences;

import db.adapter.DonneurDataSource;
import db.object.CDonneur;
import db.object.CIntervention;

public class Donneur extends AppCompatActivity {

    DonneurAdapter liste;
    String region;
    String filtre = "nom";
    int quantite=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donneur);

        //ajouter icone a la barre d'action
        getSupportActionBar().setHomeButtonEnabled(true);

        // récupère le filtre passé dans l'intent (choix fait par le menu)
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            filtre = extras.getString("filtre");
        }

        //afficher le bon titre de filtre
        setFiltreTitle();

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

        afficher();

    }

    private class PreferenceChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener{

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            restartActivity();
        }

    }

    private void restartActivity(){
        Intent intent = new Intent(this, Donneur.class);
        intent.putExtra("filtre", filtre);
        startActivity(intent);
    }


    private int compareRegion(String regionPref){
        List<String> listRegions = Arrays.asList(getResources().getStringArray(R.array.region));

        for(int i = 0; i<listRegions.size(); i ++) {

            if (listRegions.get(i).equals(regionPref)) {
                return i;
            }
        }

        return 0;
    }


    public void afficher(){

        //Récupérer région dans les préférences
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
        String regionPref = preferences.getString("region", "Brig");
        PreferenceChangeListener mPreferenceListener = new PreferenceChangeListener();
        preferences.registerOnSharedPreferenceChangeListener(mPreferenceListener);


        try {
            //Récupère la liste des régions dans les ressources: spinners.xml
            List<String> listRegions = Arrays.asList(getResources().getStringArray(R.array.region));

            Collections.swap(listRegions, 0,  compareRegion(regionPref));

            for(int i = 0; i < listRegions.size(); i++){
                region = listRegions.get(i);
                afficherParRegion(region);
            }

        } catch (ParseException e) {
            e.printStackTrace();
            }
    }

    private void setFiltreTitle(){
        TextView text = (TextView) findViewById(R.id.textView_filtre);

        switch(filtre){
            case("naissance"):
                text.setText(getResources().getString(R.string.filtreNaissance));
                break;
            case("dispo"):
                text.setText(getResources().getString(R.string.filtreDispo));
                break;
            case("nom"):
                text.setText(getResources().getString(R.string.filtreNom));
                break;
            case("prénom"):
                text.setText("Filtre: prénom");
                break;
        }
    }

    public void afficherParRegion(String region) throws ParseException {

        final ArrayList<CDonneur> listeDonneurs = new ArrayList<CDonneur>();

        //listview
        liste = new DonneurAdapter(this.getApplicationContext());

        //Ne doit pas afficher s'il n'y a pas de donneur pour une région
        if(liste.getCount()!=0){
            //Pour récupérer tous les donneurs de chaque région
            for(int i = 0; i<liste.getCount(); i++){
                listeDonneurs.add(liste.getDonneur(i));
            }

           LinearLayout ll = (LinearLayout)findViewById(R.id.linearLayout);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(25, 25, 25, 25);

            //Crée texteView Region
            TextView tv = new TextView(this);
            tv.setLayoutParams(params);
            tv.setPadding(15, 0, 0, 0);
            tv.setTextSize(20);
            tv.setTextColor(getResources().getColor(R.color.black));

            tv.setTypeface(null, Typeface.BOLD);
            tv.setText(region);

            ll.addView(tv);
            ListView lv = new ListView(this);
            lv.setVerticalScrollBarEnabled(true);
            lv.setAdapter(liste);
            if (lv.getCount()>1) {
                lv.setFastScrollAlwaysVisible(true);
            }
            ll.addView(lv);


            //Click sur la listView
            lv.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CDonneur d = listeDonneurs.get(position);
                    sendDonneur(d.getId());
                }
            });
        }

    }


    public void sendDonneur(int id){
        Intent intent = new Intent(this, AfficherDonneur.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_donneur, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case R.id.action_nouveau_donneur:
                Intent intent = new Intent(this, NouveauDonneur.class);
                startActivity(intent);
                return true;
            case R.id.action_filtre_disponibilite:
                filtre = "dispo";
                intent = new Intent(this, Donneur.class);
                intent.putExtra("filtre", filtre);
                startActivity(intent);
                return true;
            case R.id.action_filtre_nom:
                filtre="nom";
                intent = new Intent(this, Donneur.class);
                intent.putExtra("filtre", filtre);
                startActivity(intent);
                return true;
            case R.id.action_filtre_prenom:
                filtre = "prénom";
                intent = new Intent(this, Donneur.class);
                intent.putExtra("filtre", filtre);
                startActivity(intent);
                return true;
            case R.id.action_filtre_naissance:
                filtre = "naissance";
                intent = new Intent(this, Donneur.class);
                intent.putExtra("filtre", filtre);
                startActivity(intent);
                return true;
            case R.id.action_parametre:
                Intent intent2 = new Intent(this, Parametre.class);
                startActivity(intent2);
        }
        return (super.onOptionsItemSelected(item));


    }



    public class DonneurAdapter extends BaseAdapter {

        DonneurDataSource da;
        List<CDonneur> liste;

        public DonneurAdapter (Context context) throws ParseException {
            da = new DonneurDataSource(context);
            liste = getDataForListView();
        }

        public List<CDonneur> getDataForListView() throws ParseException {
            List<CDonneur> listDonneur;
            listDonneur = da.getAllDonneurByNom(region);

            //Tri en fonction du filtre
            switch (filtre){
                case("naissance"):
                    listDonneur = da.getAllDonneurByNaissance(region);
                    break;
                case("dispo"):
                    listDonneur = da.getAllDonneurByDispo(region);
                    break;
                case("nom"):
                    listDonneur = da.getAllDonneurByNom(region);
                    break;
                case("prénom"):
                    listDonneur = da.getAllDonneurByPrenom(region);
                    break;

            }

            quantite = listDonneur.size();
            return listDonneur;
        }

        @Override
        public int getCount() {
            return liste.size();
        }

        @Override
        public Object getItem(int position) {
            return liste.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater) Donneur.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.liste_donneur, parent,false);
            }
            ImageView dispo = (ImageView) convertView.findViewById(R.id.imageViewDispo);
            TextView nom = (TextView)convertView.findViewById(R.id.textViewNom);
            TextView naissance = (TextView)convertView.findViewById(R.id.textViewNaissance);

            CDonneur i = liste.get(position);

            Date now = new Date();
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH)+1;
            int day = c.get(Calendar.DAY_OF_MONTH);
            try {
                now = changeIntoDate(year + "." + month + "." + day);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //Test si le donneur est dispo pour l'affichage de l'image
            if(i.getDisponibilite().after(now)){
                dispo.setImageDrawable(getResources().getDrawable(R.drawable.dispo_false));
            }else{
                dispo.setImageDrawable(getResources().getDrawable(R.drawable.dispo_true));
            }

            nom.setText(i.getNom() + " " +i.getPrenom());
            try {
                naissance.setText(changeIntoString(i.getNaissance()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return convertView;
        }

        public CDonneur getDonneur(int position)
        {
            return liste.get(position);
        }

    }
    public Date changeIntoDate(String s) throws ParseException {

        DateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.FRENCH);
        Date date = format.parse(s);
        return date;
    }

    public String changeIntoString(Date d) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.FRENCH);
        String s = df.format(d);
        return s;
    }

}
