package com.betrisey.suzanne.androidproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import db.adapter.InterventionDataSource;
import db.object.CIntervention;
import db.object.CSang;

public class Intervention extends AppCompatActivity {

    InterventionAdapter liste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intervention);

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

        //listview
        try {
            liste = new InterventionAdapter(this.getApplicationContext());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ListView lv = (ListView)findViewById(R.id.listView);
        lv.setAdapter(liste);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CIntervention i = liste.getIntervention(position);
                sendIntervention(i.getId());
            }
        });


    }

    public void sendIntervention(int id){
        Intent intent = new Intent(this, AfficherIntervention.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_intervention, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_ajouter_intervention:
                Intent intent = new Intent(this, AjouterIntervention.class);
                startActivity(intent);
                return true;
            case R.id.action_supprimer_intervention:
                Intent intent3 = new Intent(this, SupprimerIntervention.class);
                startActivity(intent3);
                return true;
            case R.id.action_parametre:
                Intent intent2 = new Intent(this, Parametre.class);
                startActivity(intent2);
        }
        return (super.onOptionsItemSelected(item));
    }



    public class InterventionAdapter extends BaseAdapter {

        InterventionDataSource ia;
        List<CIntervention> liste;

        public InterventionAdapter (Context context) throws ParseException {
            ia = new InterventionDataSource(context);
            liste = getDataForListView();
        }

        public List<CIntervention> getDataForListView() throws ParseException {
            List<CIntervention> listIntervention;
            listIntervention = ia.getAllInterventions();

            return listIntervention;

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
                LayoutInflater inflater = (LayoutInflater) Intervention.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.liste_intervention, parent,false);
            }

            TextView date = (TextView)convertView.findViewById(R.id.textViewDate);
            TextView description = (TextView)convertView.findViewById(R.id.textViewDescription);

            CIntervention i = liste.get(position);

            try {
                date.setText(changeIntoString(i.getDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            description.setText(i.getDescription());

            return convertView;
        }

        public CIntervention getIntervention(int position)
        {
            return liste.get(position);
        }
    }

    public String changeIntoString(Date d) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.FRENCH);
        String s = df.format(d);
        return s;
    }


}
