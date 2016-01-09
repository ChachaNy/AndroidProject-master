package com.betrisey.suzanne.androidproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import db.adapter.DonneurDataSource;
import db.object.CDonneur;

public class AfficherDonneur extends AppCompatActivity {

    private int id;
    DonneurDataSource da;
    CDonneur d;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficher_donneur);

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

        da = new DonneurDataSource(getApplicationContext());
        try {
            d = da.getDonneurById(id);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String genre;

        if(d.getSexe().equals("f"))
            genre= getResources().getString(R.string.feminin);
        else
            genre= getResources().getString(R.string.masculin);

        TextView tv = (TextView) findViewById(R.id.textPrenom);
        tv.setText(d.getPrenom() + " " + d.getNom());

        ListView vueData;
        ListView vueInfo;
        String[] donneur = new String[0];
        try {
            donneur = new String[]{genre, changeIntoString(d.getNaissance()), d.getAdresse(), String.valueOf(d.getNPA()), d.getLieu(), d.getRegion(),d.getTelephone(), d.getGroupe(), String.valueOf(d.getDonsPossibles()), changeIntoString(d.getDisponibilite())};
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] info = getResources().getStringArray(R.array.infoDonneur);

        //Tableau
        TableLayout table=(TableLayout) findViewById(R.id.tableLayout);
        TableRow row;
        TextView tv1,tv2;

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.width=0;
        params.bottomMargin = 20;
        params.weight = 0.5f;

        for(int i = 0;i<info.length;i++){
            row = new TableRow(this); //Création d'une nouvelle ligne
            row.setWeightSum(1f);

            tv1 = new TextView(this); // création cellule
            tv1.setText(info[i]);// ajout du texte
            tv1.setTextSize(18);
            tv1.setTextColor(Color.parseColor("#000000"));
            tv1.setLayoutParams(params);

            tv2 = new TextView(this); // création cellule
            tv2.setText(donneur[i]);
            tv2.setTextSize(18);// ajout du texte;
            tv2.setTextColor(Color.parseColor("#515151"));
            tv2.setLayoutParams(params);


            //ajout des cellules à la ligne
            row.addView(tv1);
            row.addView(tv2);

            //ajout de la ligne au tableau
            table.addView(row);

        }


    }


    public void buttonDonDeSang(View view) throws ParseException {
        Date now = new Date();
        final GregorianCalendar c = new GregorianCalendar();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH)+1;
        int day = c.get(Calendar.DAY_OF_MONTH);
        try {
            now = changeIntoDate(year + "." + month + "." + day);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(d.getDisponibilite().before(now) || d.getDonsPossibles()>0 || d.getDisponibilite().equals(now))
        {
            Intent intent = new Intent(this, DonDeSang.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }
        else
        {
            ContextThemeWrapper themedContext;
            themedContext = new ContextThemeWrapper( this, android.R.style.Theme_DeviceDefault_Light_Dialog);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(themedContext);
            alertDialog.setMessage("\n"+ getResources().getString(R.string.alerteSang1) + "\n\n" +
                    getResources().getString(R.string.alerteSang2) +  " " +changeIntoString(d.getDisponibilite()) +"\n");
            alertDialog.show();
        }


    }

    public void buttonEdit(View view) {
        Intent intent = new Intent(this, ModifierDonneur.class);
        startActivity(intent);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public String changeIntoString(Date d) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.FRENCH);
        String s = df.format(d);
        return s;
    }

    public Date changeIntoDate(String s) throws ParseException {

        DateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.FRENCH);
        Date date = format.parse(s);
        return date;
    }
}
