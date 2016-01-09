package com.betrisey.suzanne.androidproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import db.adapter.DonneurDataSource;
import db.object.CDonneur;

public class NouveauDonneur extends AppCompatActivity {

    DonneurDataSource da;
    TextView mDateDisplay;
    TextView mDateDisplay2;
    Activity activity;
    boolean stop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nouveau_donneur);

        activity = this;
        da = new DonneurDataSource(this);

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

        //spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinnerRegion);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.region, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        //spinner
        Spinner spinner2 = (Spinner) findViewById(R.id.spinnerGroupes);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.groupe_sanguin, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);

        //DatePicker
        mDateDisplay = (TextView) findViewById(R.id.textViewDate);
        mDateDisplay.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            public void onClick(View v) {
                // showDialog(DATE_DIALOG_ID);
                DialogFragment newFragment = new DateDialog();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        mDateDisplay2 = (TextView) findViewById(R.id.textViewDateDispo);
        mDateDisplay2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            public void onClick(View v) {
                // showDialog(DATE_DIALOG_ID);
                DialogFragment newFragment = new DateDialog2();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

    }


    public void buttonAnnuler(View view) {
        Intent intent = new Intent(this, Donneur.class);
        startActivity(intent);
    }

    public void buttonOk(View view) throws ParseException {

        CDonneur d = new CDonneur();

        TextView et = (EditText) findViewById(R.id.editNom);
        d.setNom(et.getText().toString());
        et = (EditText) findViewById(R.id.editPrenom);
        d.setPrenom(et.getText().toString());
        RadioButton rb = (RadioButton) findViewById(R.id.radioFeminin);
        if (rb.isChecked()) {
            d.setSexe("f");
            d.setDonsPossibles(3);
        } else {
            d.setSexe("m");
            d.setDonsPossibles(4);
        }
        TextView tv = (TextView) findViewById(R.id.textViewDate);
        d.setNaissance(changeIntoDate(tv.getText().toString()));
        tv = (TextView) findViewById(R.id.textViewDateDispo);
        d.setDisponibilite(changeIntoDate(tv.getText().toString()));

        et = (EditText) findViewById(R.id.editTelephone);
        d.setTelephone(et.getText().toString());

        et = (EditText) findViewById(R.id.editAdresse);
        d.setAdresse(et.getText().toString());
        et = (EditText) findViewById(R.id.editAdresse);
        d.setAdresse(et.getText().toString());
        et = (EditText) findViewById(R.id.editLieu);
        d.setLieu(et.getText().toString());

        if (d.getNom().equals("") == false && d.getPrenom().equals("") == false && d.getNaissance()!=null && d.getDisponibilite()!=null && d.getLieu().equals("") == false && d.getAdresse().equals("") == false
                && d.getTelephone().equals("") == false){

            if(testNPA()){
                et = (EditText) findViewById(R.id.ediNpa);
                d.setNPA(Integer.parseInt(et.getText().toString()));
                Spinner spin = (Spinner) findViewById(R.id.spinnerRegion);
                d.setRegion(spin.getSelectedItem().toString());
                Spinner spin2 = (Spinner) findViewById(R.id.spinnerGroupes);
                d.setGroupe(spin2.getSelectedItem().toString());

                da.createDonneur(d);

                Intent intent = new Intent(this, Donneur.class);
                startActivity(intent);
            }else{
                ContextThemeWrapper themedContext;
                themedContext = new ContextThemeWrapper( this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(themedContext);
                alertDialog.setMessage(getResources().getString(R.string.alerteNPA));
                alertDialog.show();
            }

        } else {
            ContextThemeWrapper themedContext;
            themedContext = new ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(themedContext);
            alertDialog.setMessage(getResources().getString(R.string.alerteChamps));
            alertDialog.show();
        }
    }

    public boolean testNPA(){
        try {
            EditText text = (EditText) findViewById (R.id.ediNpa);
            Integer.parseInt(text.getText().toString());
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }

    public Date changeIntoDate(String s) {
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.FRENCH);
        Date date = null;
        try {
            date = format.parse(s);
            return date;
        } catch (ParseException e) {
            ContextThemeWrapper themedContext;
            themedContext = new ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(themedContext);
            alertDialog.setMessage(getResources().getString(R.string.alerteDate));
            alertDialog.show();
            return null;
        }

    }

    @SuppressLint({"NewApi", "ValidFragment"})
    public class DateDialog extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dpd = new DatePickerDialog(activity, this, year, month, day);
            return dpd;
        }


        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
            mDateDisplay.setText(String.valueOf(day) + "."
                    + String.valueOf(month + 1) + "." + String.valueOf(year));
        }
    }

    @SuppressLint({"NewApi", "ValidFragment"})
    public class DateDialog2 extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dpd = new DatePickerDialog(activity, this, year, month, day);
            return dpd;
        }

        @Override
        public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
            mDateDisplay2.setText(String.valueOf(day) + "."
                    + String.valueOf(month + 1) + "." + String.valueOf(year));
        }

    }
}



