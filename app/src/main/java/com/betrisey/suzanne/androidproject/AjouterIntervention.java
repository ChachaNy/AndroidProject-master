package com.betrisey.suzanne.androidproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import db.adapter.InterventionDataSource;
import db.adapter.SangDataSource;
import db.object.CIntervention;
import db.object.CSang;

import static android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;

public class AjouterIntervention extends AppCompatActivity {

    InterventionDataSource ia;
    SangDataSource sa;
    TextView mDateDisplay;
    Activity activity;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_intervention);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

       activity = this;


       ia = new InterventionDataSource(this);
       sa = new SangDataSource(this);

       //spinner
       Spinner spinner = (Spinner) findViewById(R.id.spinnerGroupe);
// Create an ArrayAdapter using the string array and a default spinner layout
       ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
               R.array.groupe_sanguin, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
       adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
       spinner.setAdapter(adapter);

       //spinner
       Spinner spinner2 = (Spinner) findViewById(R.id.spinnerRegion);
// Create an ArrayAdapter using the string array and a default spinner layout
       ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
               R.array.region, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
       adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
       spinner2.setAdapter(adapter2);

       mDateDisplay = (TextView) findViewById(R.id.textViewDate);
       mDateDisplay.setOnClickListener(new View.OnClickListener() {
           @SuppressLint("NewApi")
           public void onClick(View v) {
               // showDialog(DATE_DIALOG_ID);
               DialogFragment newFragment = new DateDialog();
               newFragment.show(getFragmentManager(), "datePicker");
           }
       });
    }

    public void buttonAnnuler(View view) {
        Intent intent = new Intent(this, Intervention.class);
        startActivity(intent);
    }

    public void buttonOk(View view) throws ParseException {

        if(testQuantite() == true)
        {
            CIntervention i = new CIntervention();

            TextView et = (TextView) findViewById (R.id.textViewDate);
            i.setDate(changeIntoDate(et.getText().toString()));
            if(i.getDate()!= null)
            {
                et = (EditText) findViewById (R.id.textViewDescription);
                i.setDescription(et.getText().toString());
                et = (EditText) findViewById (R.id.textViewQuantite);
                i.setQuantite(Integer.parseInt(et.getText().toString()));
                Spinner spin = (Spinner) findViewById (R.id.spinnerGroupe);
                i.setGroupe(spin.getSelectedItem().toString());
                spin = (Spinner) findViewById (R.id.spinnerRegion);
                i.setRegion(spin.getSelectedItem().toString());;

                long id = ia.createIntervention(i);
                int quantite = i.getQuantite();

                Date now = new Date();
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH)+1;
                int day = c.get(Calendar.DAY_OF_MONTH);

                now = changeIntoDate(day + "." + month + "." + year);


                if(i.getDate().equals(now) || i.getDate().after(now))
                {
                    List<CSang> listeS = sa.getAllSangs();

                    for (int j = 0; j < listeS.size(); j++) {
                        if (listeS.get(j).getPeremption().after(i.getDate()) && quantite > 0 && i.getGroupe().equals(listeS.get(j).getGroupe()) && i.getRegion().equals(listeS.get(j).getRegion()) && listeS.get(j).getStatut().equals("en stock")) {
                            listeS.get(j).setIntervention((int) id);
                            listeS.get(j).setStatut("commandé");
                            sa.updateSang(listeS.get(j));
                            quantite = quantite - 1;
                        }
                    }

                    if (quantite > 0) {
                        for (int j = 0; j < listeS.size(); j++) {
                            if (listeS.get(j).getPeremption().after(i.getDate()) && quantite > 0 && i.getGroupe().equals(listeS.get(j).getGroupe()) && listeS.get(j).getStatut().equals("en stock")) {
                                listeS.get(j).setIntervention((int) id);
                                listeS.get(j).setStatut("transfert");
                                listeS.get(j).setRegion(i.getRegion());
                                sa.updateSang(listeS.get(j));
                                quantite = quantite - 1;
                            }
                        }
                    }
                }


                Intent intent = new Intent(this, Intervention.class);
                startActivity(intent);

                }

        } else
        {
            ContextThemeWrapper themedContext;
            themedContext = new ContextThemeWrapper( this, android.R.style.Theme_DeviceDefault_Light_Dialog);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(themedContext);
            alertDialog.setMessage(getResources().getString(R.string.alerteQuPochette));
            alertDialog.show();
        }



    }

    public Date changeIntoDate(String s) {
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.FRENCH);
        Date date = null;
        try {
            date = format.parse(s);
            return date;
        } catch (ParseException e) {
            ContextThemeWrapper themedContext;
            themedContext = new ContextThemeWrapper( this, android.R.style.Theme_DeviceDefault_Light_Dialog);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(themedContext);
            alertDialog.setMessage(getResources().getString(R.string.alerteDate));
            alertDialog.show();
            return null;
        }

    }

    public boolean testQuantite(){
            try {
                EditText text = (EditText) findViewById (R.id.textViewQuantite);
                Integer.parseInt(text.getText().toString());
            } catch(NumberFormatException e) {
                return false;
            } catch(NullPointerException e) {
                return false;
            }
            return true;
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

}
