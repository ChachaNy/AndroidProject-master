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
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import db.adapter.DonneurDataSource;
import db.object.CDonneur;

public class ModifierDonneur extends AppCompatActivity {
    int id;
    CDonneur d;
    DonneurDataSource da;
    TextView mDateDisplay1;
    TextView mDateDisplay2;
    Activity activity;
    ArrayList<String> donneur;
    String npa;
    int IntCheckButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_donneur);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        activity = this;
        donneur = new ArrayList<String>();

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

        if (savedInstanceState == null) {
            // Get the message from the intent

            donneur.add(d.getPrenom());
            donneur.add(d.getNom());
            donneur.add(d.getSexe());
           try {
                donneur.add(changeIntoString(d.getNaissance()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            donneur.add(d.getAdresse());
            donneur.add(String.valueOf(d.getNPA()));
            donneur.add(d.getLieu());
            donneur.add(d.getRegion());
            donneur.add(d.getTelephone());
            donneur.add(d.getGroupe());
            donneur.add(String.valueOf(d.getDonsPossibles()));
            try {
                donneur.add(changeIntoString(d.getDisponibilite()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            remplirTableau(donneur);
        } else {

            ArrayList<String> currentData = savedInstanceState.getStringArrayList("liste");
            donneur = currentData;
            remplirTableau(donneur);
        }


    }


    public void onSaveInstanceState(Bundle outState){
        //Récupération des données -> rotation écran
        for(int i= 0; i<donneur.size();i++) {
            Object obj = findViewById(1000 + i);
            if (obj instanceof RadioButton) {
                if (((RadioButton) obj).isChecked() == true){
                    donneur.set(i, "f");
                }
                else{
                    donneur.set(i, "m");
                }
            } else if (obj instanceof Spinner) {
                donneur.set(i, ((Spinner) obj).getSelectedItem().toString());
            } else if (obj instanceof TextView){
                donneur.set(i, ((TextView) obj).getText().toString());
            }else if(obj instanceof EditText){
                donneur.set(i, ((EditText) obj).getText().toString());
            }



        }

        outState.putStringArrayList("liste", donneur);
        super.onSaveInstanceState(outState);
    }

    private void saveData() throws ParseException {
        ArrayList<String> donnees = new ArrayList<String>();
        String text= "";

        for(int i= 0; i<donneur.size();i++) {
            Object obj = findViewById(1000 + i);
            if (obj instanceof RadioButton) {
                if (((RadioButton) obj).isChecked() == true){
                    text = "f";
                }
                else {
                    text = "m";
                }
            } else if (obj instanceof Spinner) {
                text = ((Spinner) obj).getSelectedItem().toString();
            } else if (obj instanceof TextView){
                text = ((TextView) obj).getText().toString();
            }else if(obj instanceof EditText){
                text = ((EditText) obj).getText().toString();
            }
            donnees.add(text);
        }

        npa = donnees.get(5);
        //teste la valeur de npa
        if(testNPA()){
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            d.setNom(donnees.get(0));
            d.setPrenom(donnees.get(1));
            d.setSexe(donnees.get(2));
            d.setNaissance(formatter.parse(donnees.get(3)));
            d.setAdresse(donnees.get(4));
            d.setNPA(Integer.parseInt(donnees.get(5)));
            d.setLieu(donnees.get(6));
            d.setRegion(donnees.get(7));
            d.setTelephone(donnees.get(8));
            d.setGroupe(donnees.get(9));
            d.setDonsPossibles(Integer.parseInt(donnees.get(10)));
            d.setDisponibilite(formatter.parse(donnees.get(11)));
        }else {
            ContextThemeWrapper themedContext;
            themedContext = new ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(themedContext);
            alertDialog.setMessage(getResources().getString(R.string.alerteNPA));
            alertDialog.show();
        }

    }

    //UpdateDonneur
    public void updateData() throws ParseException {
        saveData();
        da.updateDonneur(d);
    }


    public void remplirTableau(ArrayList<String> donneur){

        ListView vueData;
        ListView vueInfo;
        String[] info = getResources().getStringArray(R.array.infoModifDonneur);

        //Tableau
        TableLayout table=(TableLayout) findViewById(R.id.tableLayoutDonnees);
        TableRow row;
        TextView tvNaissance, tvDispo, tv1,tv2;
        Spinner spinner;
        String[] regions = getResources().getStringArray(R.array.region);
        String[] groupe = getResources().getStringArray(R.array.groupe_sanguin);

        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        params.width=0;
        params.weight = 0.5f;


        for(int i = 0;i<info.length;i++){
            row = new TableRow(this); //Création d'une nouvelle ligne
            row.setWeightSum(1f);

            tv1 = new TextView(this); // création cellule
            tv1.setText(info[i]); // ajout du texte
            tv1.setLayoutParams(params);

            //ajout des cellules à la ligne
            row.addView(tv1);

            if(i==2) {
                RadioGroup radioGroup = new RadioGroup(this);
                RadioButton fem = new RadioButton(this);
                fem.setText(getResources().getString(R.string.feminin));
                RadioButton masc = new RadioButton(this);
                masc.setText(getString(R.string.masculin));


                if(donneur.get(i).equals("f")){
                    radioGroup.clearCheck();
                    fem.setChecked(true);
                }else{
                    radioGroup.clearCheck();
                    masc.setChecked(true);
                }

                fem.setId(1000 + i);
                masc.setId(3000 + 1);

                radioGroup.setLayoutParams(params);
                //Rajouter le test pour cocher la bonne sélection par défaut
                radioGroup.addView(fem);
                radioGroup.addView(masc);
                row.addView(radioGroup);

            }else if( i == 3){
                tvNaissance = new TextView(this);
                tvNaissance.setId(1000 + i);
                mDateDisplay1 = tvNaissance;
                mDateDisplay1.setText(donneur.get(i));
                mDateDisplay1.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("NewApi")
                    public void onClick(View v) {
                        // showDialog(DATE_DIALOG_ID);
                        Bundle b = new Bundle();
                        DialogFragment newFragment = new DateDialog1();
                        newFragment.show(getFragmentManager(), "datePicker");
                    }
                });
                mDateDisplay1.setLayoutParams(params);
                row.addView(mDateDisplay1);

            }else if (i == 7){
                spinner = new Spinner(this);
                ArrayAdapter<Integer> stringArrayAdapter=
                        new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, regions);
                spinner.setAdapter(stringArrayAdapter);

                //Récupère la bonne région par défaut
                for(int ind = 0; ind<stringArrayAdapter.getCount(); ind++) {
                    if(donneur.get(i).trim().equals(stringArrayAdapter.getItem(ind))){
                        spinner.setSelection(ind);
                        break;
                    }
                }

                spinner.setId(1000+i);

                spinner.setLayoutParams(params);
                row.addView(spinner);
            }else if(i == 9){
                spinner = new Spinner(this);
                ArrayAdapter<Integer> stringArrayAdapter=
                        new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, groupe);
                spinner.setAdapter(stringArrayAdapter);
                spinner.setId(1000+i);

                //Récupère le bon groupe paer défaut
                for(int ind = 0; ind<stringArrayAdapter.getCount(); ind++) {
                    if(donneur.get(i).trim().equals(stringArrayAdapter.getItem(ind))){
                        spinner.setSelection(ind);
                        break;
                    }
                }

                spinner.setLayoutParams(params);
                row.addView(spinner);

            }else if(i == 11) {
                tvDispo = new TextView(this);
                tvDispo.setId(1000 + i);
                mDateDisplay2 = tvDispo;
                mDateDisplay2.setText(donneur.get(i));
                mDateDisplay2.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("NewApi")
                    public void onClick(View v) {
                        // showDialog(DATE_DIALOG_ID);
                        DialogFragment newFragment = new DateDialog2();
                        newFragment.show(getFragmentManager(), "datePicker");
                    }
                });
                mDateDisplay2.setLayoutParams(params);
                row.addView(mDateDisplay2);
            }else{
                tv2 = new EditText(this); // création cellule
                tv2.setText(donneur.get(i)); // ajout du texte;
                tv2.setLayoutParams(params);

                tv2.setId(1000+i);
                row.addView(tv2);
            }
            //ajout de la ligne au tableau
            table.addView(row);
        }
    }

    public void buttonAnnuler(View view) {
        Intent intent = new Intent(this, AfficherDonneur.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    public void buttonOk(View view) throws ParseException {

        //Récupère les données des champs dans CDonneur
        saveData();

        //Ne pas pour suivre (retour à l'affiche du donneur) si npa contient des lettres ou s'il est null
        if(testNPA()==false){
            return;
        }

        //Pas de champs vide
        if (d.getNom().equals("") == false && d.getPrenom().equals("") == false && d.getNaissance()!=null && d.getDisponibilite()!=null && d.getLieu().equals("") == false && d.getAdresse().equals("") == false
                && d.getTelephone().equals("") == false){

            Intent intent = new Intent(this, AfficherDonneur.class);
            intent.putExtra("id", id);
            startActivity(intent);
            updateData();

        } else {
            ContextThemeWrapper themedContext;
            themedContext = new ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(themedContext);
            alertDialog.setMessage(getResources().getString(R.string.alerteChamps));
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
            themedContext = new ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(themedContext);
            alertDialog.setMessage(getResources().getString(R.string.alerteDate));
            alertDialog.show();
            return null;
        }

    }

    public boolean testNPA(){
        try {
            Integer.parseInt(npa);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }

    @SuppressLint("ValidFragment")
    public class DateDialog1 extends DialogFragment
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

        public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
            mDateDisplay1.setText(String.valueOf(day) + "."
                    + String.valueOf(month + 1) + "." + String.valueOf(year));
        }
    }

    @SuppressLint("ValidFragment")
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

        public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
            mDateDisplay2.setText(String.valueOf(day) + "."
                    + String.valueOf(month + 1) + "." + String.valueOf(year));
        }
    }


    public String changeIntoString(Date d) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.FRENCH);
        String s = df.format(d);
        return s;
    }

}
