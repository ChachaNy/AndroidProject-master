package com.betrisey.suzanne.androidproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import db.SQLiteHelper;
import db.adapter.DonneurDataSource;
import db.adapter.SangDataSource;
import db.object.CDonneur;
import db.object.CSang;

public class DonDeSang extends AppCompatActivity {
    private boolean utilOui;
    private boolean utilNon;
    private boolean malOui;
    private boolean malNon;
    CDonneur d;
    DonneurDataSource da;
    SangDataSource sa;
    int id;
    Date now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_de_sang);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        utilOui = false;
        utilNon = false;
        malOui = false;
        malNon = false;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
        }

        da = new DonneurDataSource(getApplicationContext());
        sa = new SangDataSource(getApplicationContext());
        try {
            d = da.getDonneurById(id);
            remplirTableau();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        //spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinnerRegion);
        String[] groupe = getResources().getStringArray(R.array.region);
        ArrayAdapter<String> ad = new ArrayAdapter<String>(DonDeSang.this, android.R.layout.simple_spinner_dropdown_item,groupe);
        spinner.setAdapter(ad);
        spinner.setSelection(ad.getPosition(d.getRegion()));
    }

    public void buttonUtilOui(View view){
        ImageButton imgBtnOui = (ImageButton)findViewById(R.id.btnUtilOui);
        ImageButton imgBtnNon = (ImageButton)findViewById(R.id.btnUtilNon);
        changeImageOui(imgBtnOui, imgBtnNon, utilOui);
        utilOui=true;
        utilNon=false;
    }

    public void buttonUtilNon(View view){
        ImageButton imgBtnNon = (ImageButton)findViewById(R.id.btnUtilNon);
        ImageButton imgBtnOui = (ImageButton)findViewById(R.id.btnUtilOui);
        changeImageNon(imgBtnNon, imgBtnOui, utilNon);
        utilNon=true;
        utilOui=false;
    }

    public void buttonMalOui(View view){
        ImageButton imgBtnOui = (ImageButton)findViewById(R.id.btnMalOui);
        ImageButton imgBtnNon = (ImageButton)findViewById(R.id.btnMalNon);
        changeImageOui(imgBtnOui, imgBtnNon, malOui);
        malOui=true;
        malNon=false;
    }

    public void buttonMalNon(View view){
        ImageButton imgBtnNon = (ImageButton)findViewById(R.id.btnMalNon);
        ImageButton imgBtnOui = (ImageButton)findViewById(R.id.btnMalOui);
        changeImageNon(imgBtnNon, imgBtnOui, malNon);
        malNon=true;
        malOui=false;
    }

    public void changeImageOui(ImageButton imgBtn,ImageButton imgBtn2, boolean bool){
        if(bool == false){
            imgBtn.setImageDrawable(getResources().getDrawable(R.drawable.btn_oui_act));
            imgBtn2.setImageDrawable(getResources().getDrawable(R.drawable.btn_non_des));
        }
    }

    public void changeImageNon(ImageButton imgBtn, ImageButton imgBtn2, boolean bool){
        if(bool == false){
            imgBtn.setImageDrawable(getResources().getDrawable(R.drawable.btn_non_act));
            imgBtn2.setImageDrawable(getResources().getDrawable(R.drawable.btn_oui_des));
            }
    }

    public void remplirTableau() throws ParseException {
        ListView vueData;
        ListView vueInfo;

        now = new Date();
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        try {
            now = changeIntoDate(year + "." + month + "." + day);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String[] donneur = {String.valueOf(d.getId()), d.getPrenom() + " "+d.getNom(), changeIntoString(d.getNaissance()), d.getGroupe(), changeIntoString(now)};
        String[] info = getResources().getStringArray(R.array.infoDon);

        TableLayout table=(TableLayout) findViewById(R.id.tableLayout);
        TableRow row;
        TextView tv1,tv2;

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.width=0;
        params.bottomMargin = 20;
        params.weight = 0.5f;

        for(int i = 0;i<info.length;i++) {
            row = new TableRow(this); //Création d'une nouvelle ligne
            row.setWeightSum(1f);

            tv1 = new TextView(this); // création cellule
            tv1.setText(info[i]);
            tv1.setTextSize(18);
            tv1.setTextColor(Color.parseColor("#000000"));// ajout du texte
            tv1.setLayoutParams(params);


            tv2 = new TextView(this); // création cellule
            tv2.setText(donneur[i]);
            tv2.setTextSize(18);
            tv2.setTextColor(Color.parseColor("#000000"));// ajout du texte;
            tv2.setLayoutParams(params);

            //ajout des cellules à la ligne
            row.addView(tv1);
            row.addView(tv2);

            //ajout de la ligne au tableau
            table.addView(row);
        }


    }

    public void buttonOk (View view) throws ParseException {
        if(test()==true){

            d.setDonsPossibles(d.getDonsPossibles()-1);

          GregorianCalendar calendar = new GregorianCalendar();
            Date date = now;
            calendar.setTime(date);
            if(malOui)
            {
                calendar.add(Calendar.MONTH, +6);
            }
            else
            {
                calendar.add(Calendar.MONTH, +3);
            }
            d.setDisponibilite(calendar.getTime());

            if(d.getDonsPossibles().equals(0)){
                GregorianCalendar calendarCompare = new GregorianCalendar();
                int year = calendarCompare.get(Calendar.YEAR) + 1;
                Date dateCompare2=changeIntoDate(year + ".1.1");
                if(dateCompare2.after(d.getDisponibilite())){
                    d.setDisponibilite(dateCompare2);
                }

            }

            da.updateDonneur(d);

            CSang s = new CSang();
            s.setDateDon(now);
            s.setDonneur(d.getId());

            GregorianCalendar calendarPochette = new GregorianCalendar();
            calendarPochette.setTime(now);
            calendarPochette.add(Calendar.DAY_OF_MONTH, +42);
            s.setPeremption(calendarPochette.getTime());

            Spinner spin = (Spinner) findViewById(R.id.spinnerRegion);
            s.setRegion(spin.getSelectedItem().toString());

            s.setGroupe(d.getGroupe());

            if(utilOui)
            {
                s.setStatut("en stock");
            }
            else
            {
                s.setStatut("inutilisable");
            }

            s.setIntervention(-1);

            sa.createSang(s);

            Toast.makeText(getApplicationContext(), getResources().getString(R.string.pochette_cree), Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, Donneur.class);
            startActivity(intent);
        }
        else
        {
            ContextThemeWrapper themedContext;
            themedContext = new ContextThemeWrapper( this, android.R.style.Theme_DeviceDefault_Light_Dialog);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(themedContext);
            alertDialog.setMessage(getResources().getString(R.string.alertePochette));
            alertDialog.show();
        }
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

    public boolean test(){

        if((utilOui!=utilNon)&&(malOui!=malNon)){
            return true;
        }
        else
        {
            return false;
        }
    }

}
