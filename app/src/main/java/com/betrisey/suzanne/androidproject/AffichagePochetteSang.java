package com.betrisey.suzanne.androidproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import db.adapter.SangDataSource;
import db.object.CSang;

public class AffichagePochetteSang extends AppCompatActivity {

   CSang sang;
    SangDataSource sa;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage_pochette_sang);

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

        sa = new SangDataSource(getApplicationContext());
        try {
            sang = sa.getSangById(id);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextView textView = (TextView)findViewById(R.id.textNum);
        textView.setText("N° " + sang.getId());
        try {
            remplirTableau();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void remplirTableau() throws ParseException {
        ListView vueData;
        ListView vueInfo;

        String statut = "";


        switch(sang.getStatut())
        {
            case "utilisé":
                statut =getResources().getString(R.string.utilise);
                break;
            case "en stock":
                statut = getResources().getString(R.string.enStock);
                break;
            case "transfert":
                statut = getResources().getString(R.string.transfert);
                break;
            case "commandé":
                statut = getResources().getString(R.string.commande);
                break;
            case "inutilisable":
                statut = getResources().getString(R.string.inutilisable);
                break;
        }

        String[] data = {String.valueOf(sang.getDonneur()), changeIntoString(sang.getDateDon()), changeIntoString(sang.getPeremption()), sang.getRegion(), sang.getGroupe(), statut};
        String[] info = getResources().getStringArray(R.array.infoPochette);

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
            tv1.setText(info[i]);
            tv1.setTextSize(18);
            tv1.setTextColor(Color.parseColor("#000000"));// ajout du texte
            tv1.setLayoutParams(params);

            tv2 = new TextView(this); // création cellule
            tv2.setText(data[i]);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pochette, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_statut:
                Intent intent2 = new Intent(this, ChoixStatut.class);
                intent2.putExtra("id", id);
                startActivity(intent2);
                return true;
            case R.id.action_transfert:
                Intent intent = new Intent(this, Transfert.class);
                intent.putExtra("id", id);
                startActivity(intent);
                return true;
            }
        return (super.onOptionsItemSelected(item));
    }

    public void buttonDonneur (View view){
        Intent intent = new Intent(this, AfficherDonneur.class);
        intent.putExtra("id", sang.getDonneur());
        startActivity(intent);
    }

    public void buttonIntervention (View view){

        if(sang.getIntervention()<0)
        {
            ContextThemeWrapper themedContext;
            themedContext = new ContextThemeWrapper( this, android.R.style.Theme_DeviceDefault_Light_Dialog);
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(themedContext);
            alertDialog.setMessage(R.string.alerteIntervention);
            alertDialog.show();
        }
        else{
            Intent intent = new Intent(this, AfficherIntervention.class);
            intent.putExtra("id", sang.getIntervention());
            startActivity(intent);
        }
    }

    public String changeIntoString(Date d) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.FRENCH);
        String s = df.format(d);
        return s;
    }
}
