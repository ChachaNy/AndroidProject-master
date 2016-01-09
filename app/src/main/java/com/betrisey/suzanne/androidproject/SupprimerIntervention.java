package com.betrisey.suzanne.androidproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TableLayout;
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

public class SupprimerIntervention extends AppCompatActivity {

    InterventionAdapter liste;
    int counter=0;
    int size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supprimer_intervention);

        //ajouter icone a la barre d'action
        getSupportActionBar().setHomeButtonEnabled(true);

        //pour le menu (marche pas sur toutes les versions d'android sinon)
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
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

        ListView lv = (ListView) findViewById(R.id.listView);
        lv.setAdapter(liste);

    }

    /*public void onSaveInstanceState(Bundle outState) {
        ArrayList<CIntervention> tests = new ArrayList<CIntervention>();
        CIntervention intervention;
        for(int i = 0; i < size; i++) {
            intervention = (CIntervention) liste.getItem(i);
            tests.add(intervention);
        }
        outState.putSerializable("tests", tests);
        super.onSaveInstanceState(outState);
    }*/

    public void buttonDelete(View view) throws ParseException {
        Intent intent = new Intent(this, Intervention.class);
        InterventionDataSource ia = new InterventionDataSource(getApplicationContext());
        for(int j = 0; j < size; j++){
            CIntervention intervention = (CIntervention)liste.getItem(j);
            if(intervention.isSelected()){
                ia.deleteIntervention(intervention.getId());
            }
        }

        startActivity(intent);
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

            size=listIntervention.size();

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

            final int pos;

            if(convertView == null) {
                LayoutInflater inflater = (LayoutInflater) SupprimerIntervention.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.liste_intervention_supp, parent,false);

                // On récupère notre checkBox
                final CheckBox cb = (CheckBox) convertView.findViewById (R.id.CheckBoxSelected);

                // On lui affecte un tag comportant la position de l'item afin de
                // pouvoir le récupérer au clic de la checkbox
                cb.setTag(position);


                cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        CIntervention icb = liste.get((Integer)cb.getTag());

                        if(isChecked){
                            counter++;
                            icb.setSelected(true);
                        }
                        else{
                            counter--;
                            icb.setSelected(false);
                        }
                        if(counter>1){
                            TextView tv = (TextView)findViewById(R.id.textView_Selection);
                            tv.setText(String.valueOf(counter)+" " + getResources().getString(R.string.selectionnes));
                        }
                        else{
                            TextView tv = (TextView)findViewById(R.id.textView_Selection);
                            tv.setText(String.valueOf(counter)+"  " + getResources().getString(R.string.selectionnes));
                        }
                    }
                });

            }

            TextView date = (TextView)convertView.findViewById(R.id.textViewDate);
            TextView description = (TextView)convertView.findViewById(R.id.textViewDescription);
            final CheckBox selected = (CheckBox) convertView.findViewById(R.id.CheckBoxSelected);


            CIntervention i = liste.get(position);

            try {
                date.setText(changeIntoString(i.getDate()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            description.setText(i.getDescription());

            return convertView;
        }


    }

    public String changeIntoString(Date d) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.FRENCH);
        String s = df.format(d);
        return s;
    }
}
