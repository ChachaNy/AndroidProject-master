package com.betrisey.suzanne.androidproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import db.adapter.DonneurDataSource;
import db.adapter.InterventionDataSource;
import db.adapter.SangDataSource;
import db.object.CDonneur;
import db.object.CIntervention;
import db.object.CSang;

public class Home extends AppCompatActivity {

    List<CSang> listSang;
    List<CIntervention> listIntervention;
    SangDataSource sa;
    InterventionDataSource ia;
    DonneurDataSource da;
    Date now;

    // Ouvre la fenetre donneur
    public void openDonneur(View view) {
        Intent intent = new Intent(this, Donneur.class);
        startActivity(intent);
    }

    // Ouvre la fenetre intervention
    public void openIntervention(View view) {
        Intent intent = new Intent(this, Intervention.class);
        startActivity(intent);
    }

    // Ouvre la fenetre banque du sang
    public void openSang(View view) {
        Intent intent = new Intent(this, Sang.class);
        startActivity(intent);
    }

    // Ouvre la fenetre parametre
    public void openParametre(View view) {
        Intent intent = new Intent(this, Parametre.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        sa = new SangDataSource(this);
        ia = new InterventionDataSource(this);
        da = new DonneurDataSource(this);

        Date now = new Date();
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        if(day==1 && month==0){

            try {
                da.initializeDons();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        try {
            listSang = sa.getAllSangs();
            listIntervention = ia.getAllInterventions();
            now = changeIntoDate(year + "." + month + "." + day);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for(int k=0;k<listIntervention.size(); k++)
        {
            if(listIntervention.get(k).getDate().before(now)){
                try {
                    List<CSang> sangs = sa.getAllSangsByIntervention(listIntervention.get(k).getId());
                    for(int j=0; j<sangs.size(); j++)
                    {
                        sangs.get(j).setStatut("utilisé");
                        sa.updateSang(sangs.get(j));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        for(int i=0;i<listSang.size(); i++)
        {
            if(listSang.get(i).getPeremption().before(now)){
                try {
                    listSang.get(i).setStatut("inutilisable");
                    sa.updateSang(listSang.get(i));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }


          /* try {
                create();
            } catch (ParseException e) {
                e.printStackTrace();
            }*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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

    public Date changeIntoDate(String s) throws ParseException {

        DateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.FRENCH);
        Date date = format.parse(s);
        return date;
    }

    public void create() throws ParseException {

        CDonneur d1 = new CDonneur(1, "Balet", "Florence", "f", changeIntoDate("1992.12.03"), "Gebreitenweg 2", 3930, "Visp", "Brig", "+41 79 581 26 45", "A+", 1, changeIntoDate("2015.09.14"), 1);
        da.createDonneur(d1);
        CDonneur d2 = new CDonneur(2, "Pitteloud", "Vincent", "m", changeIntoDate("1964.04.01"), "Napoleonstrasse 32", 3900, "Brig", "Brig", "+41 79 337 45 66", "A-", 4, changeIntoDate("2012.02.04"), 1);
        da.createDonneur(d2);
        CDonneur d3 = new CDonneur(3, "Favre", "Véronique", "f", changeIntoDate("1978.05.10"), "Rathaustrasse 5", 3954, "Leukerbad", "Brig", "+41 76 815 65 65", "B+", 0, changeIntoDate("2016.01.01"), 1);
        da.createDonneur(d3);
        CDonneur d4 = new CDonneur(4, "Héritier", "Elodie", "f", changeIntoDate("1995.01.15"), "Neue Simplonstrasse 21", 3900, "Brig", "Brig", "+41 79 146 23 59", "B-", 1, changeIntoDate("2015.11.12"), 1);
        da.createDonneur(d4);
        CDonneur d5 = new CDonneur(5, "Dayer", "Philippe", "m", changeIntoDate("1984.08.03"), "Steinmattstrasse 13", 3920, "Zermatt", "Brig", "+41 79 165 56 78", "AB+", 0, changeIntoDate("2016.01.01"), 1);
        da.createDonneur(d5);
        CDonneur d6 = new CDonneur(6, "Rey", "Morgane", "f", changeIntoDate("1990.08.22"), "Rue du Bourg 18", 3960, "Sierre", "Sierre", "+41 76 899 23 47", "AB-", 2, changeIntoDate("2015.08.14"), 1);
        da.createDonneur(d6);
        CDonneur d7 = new CDonneur(7, "Sierre", "Elodie", "f", changeIntoDate("1965.02.23"), "Route de Miège 4", 3968, "Veyras", "Sierre", "+41 78 141 59 74", "O+", 1, changeIntoDate("2015.12.27"), 1);
        da.createDonneur(d7);
        CDonneur d8 = new CDonneur(8, "Roduit", "Damien", "m", changeIntoDate("1960.11.08"), "Rue de la Dent-Blanche 10", 1950, "Sion", "Sion", "+41 79 118 13 85", "O-", 2, changeIntoDate("2015.09.27"), 1);
        da.createDonneur(d8);

        CSang s1 = new CSang(1, 1, changeIntoDate("2015.12.01"), changeIntoDate("2016.01.11"), "Brig", "A+", "en stock", -1);
        sa.createSang(s1);
        CSang s2 = new CSang(2, 1, changeIntoDate("2015.12.01"), changeIntoDate("2016.01.11"), "Brig", "A+", "en stock", -1);
        sa.createSang(s2);
        CSang s3 = new CSang(3, 1, changeIntoDate("2015.12.01"), changeIntoDate("2016.01.11"), "Sion", "A+", "en stock", -1);
        sa.createSang(s3);
        CSang s4 = new CSang(4, 3, changeIntoDate("2015.12.01"), changeIntoDate("2016.01.11"), "Martigny", "B+", "commandé", 2);
        sa.createSang(s4);
        CSang s5 = new CSang(5, 3, changeIntoDate("2015.08.01"), changeIntoDate("2016.01.11"), "Martigny", "B+", "commandé", 2);
        sa.createSang(s5);
        CSang s6 = new CSang(6, 4, changeIntoDate("2015.12.01"), changeIntoDate("2015.09.11"), "Sierre", "B-", "en stock", -1);
        sa.createSang(s6);
        CSang s7 = new CSang(7, 5, changeIntoDate("2015.12.01"), changeIntoDate("2016.01.11"), "Brig", "AB+", "en stock", -1);
        sa.createSang(s7);
        CSang s8 = new CSang(8, 6, changeIntoDate("2015.08.01"), changeIntoDate("2015.09.11"), "Sierre", "AB-", "inutilisable", -1);
        sa.createSang(s8);
        CSang s9 = new CSang(9, 7, changeIntoDate("2015.12.01"), changeIntoDate("2016.01.11"), "Sierre", "O+", "utilisé", 1);
        sa.createSang(s9);
        CSang s10 = new CSang(10, 8, changeIntoDate("2015.12.01"), changeIntoDate("2016.01.11"), "Sion", "O-", "en stock", -1);
        sa.createSang(s10);

        CIntervention i1 = new CIntervention(1, changeIntoDate("2015.12.01"), "Anémie", 1, "O+", "Sierre", false);
        ia.createIntervention(i1);
        CIntervention i2 = new CIntervention(2, changeIntoDate("2015.12.05"), "Opération cardiaque", 2, "B+", "Martigny", false);
        ia.createIntervention(i2);

    }
}
