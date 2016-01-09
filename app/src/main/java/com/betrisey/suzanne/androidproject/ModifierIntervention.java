package com.betrisey.suzanne.androidproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class ModifierIntervention extends AppCompatActivity {
    private int id;
    public CIntervention i;
    public InterventionDataSource ia;
    public SangDataSource sa;
    public List<CSang> ListeS;
    TextView mDateDisplay1;
    Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_intervention);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // Get the message from the intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getInt("id");
        }

        activity = this;

        //spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinnerGroupe);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.groupe_sanguin, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        ia = new InterventionDataSource(getApplicationContext());
        sa = new SangDataSource(this);
        try {
            i = ia.getInterventiononById(id);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Create the text view

        EditText tw = (EditText) findViewById(R.id.textViewQuantite);
        tw.setText(String.valueOf(i.getQuantite()));
        final Spinner spin = (Spinner) findViewById (R.id.spinnerGroupe);
        String[] groupe = getResources().getStringArray(R.array.groupe_sanguin);
        ArrayAdapter<String> ad = new ArrayAdapter<String>(ModifierIntervention.this, android.R.layout.simple_spinner_dropdown_item,groupe);
        spin.setAdapter(ad);
        spin.setSelection(ad.getPosition(i.getGroupe()));

        tw = (EditText) findViewById(R.id.textViewDescription);
        tw.setText(i.getDescription());

        mDateDisplay1 = (TextView) findViewById(R.id.textViewDate);

        try {
            mDateDisplay1.setText(changeIntoString(i.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mDateDisplay1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            public void onClick(View v) {
                // showDialog(DATE_DIALOG_ID);
                Bundle b = new Bundle();
                DialogFragment newFragment = new DateDialog();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });


    }

    public void buttonAnnuler(View view) {
        Intent intent = new Intent(this, AfficherIntervention.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void buttonOk(View view) throws ParseException {

        if(testQuantite() == true)
        {
            List<CSang> liste = sa.getAllSangsByIntervention((int)id);

            for(int i = 0; i<liste.size(); i++){
                liste.get(i).setIntervention(-1);
                liste.get(i).setStatut("en stock");
                sa.updateSang(liste.get(i));
            }

        TextView tw = (TextView) findViewById (R.id.textViewDate);
        i.setDate(changeIntoDate(tw.getText().toString()));
        EditText et = (EditText) findViewById (R.id.textViewDescription);
        i.setDescription(et.getText().toString());
        et = (EditText) findViewById (R.id.textViewQuantite);
        i.setQuantite(Integer.parseInt(et.getText().toString()));
        Spinner spin = (Spinner) findViewById (R.id.spinnerGroupe);
        i.setGroupe(spin.getSelectedItem().toString());

        ia.updateIntervention(i);

            int quantite = i.getQuantite();

            Date now;
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH)+1;
            int day = c.get(Calendar.DAY_OF_MONTH);

            now = changeIntoDate(day + "." + month + "." + year);


            if(i.getDate().equals(now) || i.getDate().after(now))
            {
                List<CSang> listeS = sa.getAllSangs();

                for (int k = 0; k < listeS.size(); k++) {
                    if (listeS.get(k).getPeremption().after(i.getDate()) && quantite > 0 && i.getGroupe().equals(listeS.get(k).getGroupe()) && i.getRegion().equals(listeS.get(k).getRegion()) && listeS.get(k).getStatut().equals("en stock")) {
                        listeS.get(k).setIntervention(i.getId());
                        listeS.get(k).setStatut("commandé");
                        sa.updateSang(listeS.get(k));
                        quantite = quantite - 1;
                    }
                }

                if (quantite > 0) {
                    for (int j = 0; j < listeS.size(); j++) {
                        if (listeS.get(j).getPeremption().after(i.getDate()) && quantite > 0 && i.getGroupe().equals(listeS.get(j).getGroupe()) && listeS.get(j).getStatut().equals("en stock")) {
                            listeS.get(j).setIntervention(i.getId());
                            listeS.get(j).setStatut("transfert");
                            listeS.get(j).setRegion(i.getRegion());
                            sa.updateSang(listeS.get(j));
                            quantite = quantite - 1;
                        }
                    }
                }
            }

        Intent intent = new Intent(this, AfficherIntervention.class);
        intent.putExtra("id", id);
        startActivity(intent);
        }
        else
        {
            AlertDialog alertDialog = new AlertDialog.Builder(ModifierIntervention.this).create();
            alertDialog.setTitle(getResources().getString(R.string.alerteErreur));
            alertDialog.setMessage(getResources().getString(R.string.alerteQuPochette));

            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
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

    public Date changeIntoDate(String s) throws ParseException {
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.FRENCH);
        Date date = format.parse(s);
        return date;
    }

    public String changeIntoString(Date d) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.FRENCH);
        String s = df.format(d);
        return s;
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
            mDateDisplay1.setText(String.valueOf(day) + "."
                    + String.valueOf(month + 1) + "." + String.valueOf(year));
        }


    }
}
