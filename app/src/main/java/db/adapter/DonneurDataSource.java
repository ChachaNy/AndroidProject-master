package db.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.betrisey.suzanne.dondesang.backend.cDonneurApi.model.CDonneurBack;
import com.google.api.client.util.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import db.DonDeSangContract;
import db.SQLiteHelper;
import db.object.CDonneur;

import db.DonDeSangContract.DonneurEntry;
import db.object.CIntervention;

/**
 * Created by Suzanne on 25.11.2015.
 */
public class DonneurDataSource {

    private SQLiteDatabase db;
    private Context context;

    public DonneurDataSource(Context context){
        SQLiteHelper sqliteHelper = SQLiteHelper.getInstance(context);
        db = sqliteHelper.getWritableDatabase();
        this.context = context;
    }

    /**
     * Insert a new donneur
     */
    public long createDonneur(CDonneur donneur) throws ParseException {
        long id;
        ContentValues values = new ContentValues();
        values.put(DonDeSangContract.DonneurEntry.KEY_NOM, donneur.getNom());
        values.put(DonDeSangContract.DonneurEntry.KEY_PRENOM, donneur.getPrenom());
        values.put(DonDeSangContract.DonneurEntry.KEY_GENRE, donneur.getSexe());
        values.put(DonDeSangContract.DonneurEntry.KEY_NAISSANCE, changeIntoString(donneur.getNaissance()));
        values.put(DonDeSangContract.DonneurEntry.KEY_RUE, donneur.getAdresse());
        values.put(DonDeSangContract.DonneurEntry.KEY_NPA, donneur.getNPA());
        values.put(DonDeSangContract.DonneurEntry.KEY_LIEU, donneur.getLieu());
        values.put(DonDeSangContract.DonneurEntry.KEY_REGION, donneur.getRegion());
        values.put(DonDeSangContract.DonneurEntry.KEY_TELEPHONE, donneur.getTelephone());
        values.put(DonDeSangContract.DonneurEntry.KEY_GROUPE, donneur.getGroupe());
        values.put(DonDeSangContract.DonneurEntry.KEY_DON, donneur.getDonsPossibles());
        values.put(DonDeSangContract.DonneurEntry.KEY_DISPONIBILITE, changeIntoString(donneur.getDisponibilite()));
        values.put(DonDeSangContract.DonneurEntry.KEY_UPDATE, 1);

        id = this.db.insert(DonDeSangContract.DonneurEntry.TABLE_DONNEUR, null, values);

        return id;
    }

    /**
     * Get all Interventions
     */
    public List<CDonneur> getAllDonneur() throws ParseException {
        List<CDonneur> donneurs = new ArrayList<CDonneur>();
        String sql = "SELECT * FROM " + DonDeSangContract.DonneurEntry.TABLE_DONNEUR + " ORDER BY " + DonDeSangContract.DonneurEntry.KEY_NOM + " ;";

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                CDonneur d = new CDonneur();
                d.setId(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_ID)));
                d.setNom(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_NOM))));
                d.setPrenom(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_PRENOM))));
                d.setSexe(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_GENRE))));
                d.setNaissance(changeIntoDate(cursor.getString(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_NAISSANCE))));
                d.setAdresse(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_RUE))));
                d.setNPA(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_NPA)));
                d.setLieu(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_LIEU))));
                d.setRegion(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_REGION))));
                d.setTelephone(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_TELEPHONE))));
                d.setGroupe(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_GROUPE))));
                d.setDonsPossibles(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_DON)));
                d.setDisponibilite(changeIntoDate(cursor.getString(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_DISPONIBILITE))));
                d.setUpdate(cursor.getInt(cursor.getColumnIndex(DonneurEntry.KEY_UPDATE)));

                donneurs.add(d);
            } while(cursor.moveToNext());
        }

        return donneurs;
    }




    public List<CDonneur> getAllDonneurByNom(String region) throws ParseException {
        List<CDonneur> donneurs = new ArrayList<CDonneur>();
        String sql = "SELECT * FROM " + DonDeSangContract.DonneurEntry.TABLE_DONNEUR +
                " WHERE " + DonneurEntry.KEY_REGION + " = '" + region + "'" + " ORDER BY lower(" + DonneurEntry.KEY_NOM  + ");";

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                CDonneur d = new CDonneur();
                d.setId(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_ID)));
                d.setNom(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_NOM))));
                d.setPrenom(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_PRENOM))));
                d.setSexe(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_GENRE))));
                d.setNaissance(changeIntoDate(cursor.getString(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_NAISSANCE))));
                d.setAdresse(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_RUE))));
                d.setNPA(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_NPA)));
                d.setLieu(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_LIEU))));
                d.setRegion(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_REGION))));
                d.setTelephone(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_TELEPHONE))));
                d.setGroupe(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_GROUPE))));
                d.setDonsPossibles(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_DON)));
                d.setDisponibilite(changeIntoDate(cursor.getString(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_DISPONIBILITE))));
                d.setUpdate(cursor.getInt(cursor.getColumnIndex(DonneurEntry.KEY_UPDATE)));

                donneurs.add(d);
            } while(cursor.moveToNext());
        }

        return donneurs;
    }

    public List<CDonneur> getAllDonneurByPrenom(String region) throws ParseException {
        List<CDonneur> donneurs = new ArrayList<CDonneur>();
        String sql = "SELECT * FROM " + DonDeSangContract.DonneurEntry.TABLE_DONNEUR + " WHERE " + DonneurEntry.KEY_REGION + " = '" + region + "'" + " ORDER BY lower(" + DonneurEntry.KEY_PRENOM  + ");";

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                CDonneur d = new CDonneur();
                d.setId(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_ID)));
                d.setNom(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_NOM))));
                d.setPrenom(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_PRENOM))));
                d.setSexe(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_GENRE))));
                d.setNaissance(changeIntoDate(cursor.getString(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_NAISSANCE))));
                d.setAdresse(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_RUE))));
                d.setNPA(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_NPA)));
                d.setLieu(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_LIEU))));
                d.setRegion(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_REGION))));
                d.setTelephone(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_TELEPHONE))));
                d.setGroupe(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_GROUPE))));
                d.setDonsPossibles(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_DON)));
                d.setDisponibilite(changeIntoDate(cursor.getString(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_DISPONIBILITE))));
                d.setUpdate(cursor.getInt(cursor.getColumnIndex(DonneurEntry.KEY_UPDATE)));

                donneurs.add(d);
            } while(cursor.moveToNext());
        }

        return donneurs;
    }

    public List<CDonneur> getAllDonneurByNaissance(String region) throws ParseException {
        List<CDonneur> donneurs = new ArrayList<CDonneur>();
        String sql = "SELECT * FROM " + DonDeSangContract.DonneurEntry.TABLE_DONNEUR + " WHERE " + DonneurEntry.KEY_REGION + " = '" + region + "'" + " ORDER BY " + DonneurEntry.KEY_NAISSANCE  + " ASC ;";

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                CDonneur d = new CDonneur();
                d.setId(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_ID)));
                d.setNom(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_NOM))));
                d.setPrenom(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_PRENOM))));
                d.setSexe(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_GENRE))));
                d.setNaissance(changeIntoDate(cursor.getString(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_NAISSANCE))));
                d.setAdresse(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_RUE))));
                d.setNPA(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_NPA)));
                d.setLieu(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_LIEU))));
                d.setRegion(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_REGION))));
                d.setTelephone(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_TELEPHONE))));
                d.setGroupe(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_GROUPE))));
                d.setDonsPossibles(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_DON)));
                d.setDisponibilite(changeIntoDate(cursor.getString(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_DISPONIBILITE))));
                d.setUpdate(cursor.getInt(cursor.getColumnIndex(DonneurEntry.KEY_UPDATE)));

                donneurs.add(d);
            } while(cursor.moveToNext());
        }

        return donneurs;
    }

    public List<CDonneur> getAllDonneurByDispo(String region) throws ParseException {
        List<CDonneur> donneurs = new ArrayList<CDonneur>();
        String sql = "SELECT * FROM " + DonDeSangContract.DonneurEntry.TABLE_DONNEUR + " WHERE " + DonneurEntry.KEY_REGION + " = '" + region + "'" + " ORDER BY " + DonneurEntry.KEY_DISPONIBILITE  + " ASC ;";

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                CDonneur d = new CDonneur();
                d.setId(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_ID)));
                d.setNom(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_NOM))));
                d.setPrenom(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_PRENOM))));
                d.setSexe(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_GENRE))));
                d.setNaissance(changeIntoDate(cursor.getString(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_NAISSANCE))));
                d.setAdresse(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_RUE))));
                d.setNPA(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_NPA)));
                d.setLieu(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_LIEU))));
                d.setRegion(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_REGION))));
                d.setTelephone(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_TELEPHONE))));
                d.setGroupe(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_GROUPE))));
                d.setDonsPossibles(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_DON)));
                d.setDisponibilite(changeIntoDate(cursor.getString(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_DISPONIBILITE))));
                d.setUpdate(cursor.getInt(cursor.getColumnIndex(DonneurEntry.KEY_UPDATE)));

                donneurs.add(d);
            } while(cursor.moveToNext());
        }

        return donneurs;
    }


    /**
     * Initalize nombre de don
     */
    public void initializeDons() throws ParseException {
        List<CDonneur> donneurs = new ArrayList<CDonneur>();
        donneurs = getAllDonneur();
        for(int i=0; i<donneurs.size();i++){
            if(donneurs.get(i).getSexe().equals("f")){
                donneurs.get(i).setDonsPossibles(3);
            }
            else{
                donneurs.get(i).setDonsPossibles(4);
            }
             updateDonneur(donneurs.get(i));
        }
    }

    /**
     *  Update a Donneur
     */
    public int updateDonneur(CDonneur donneur) throws ParseException {
        ContentValues values = new ContentValues();
        values.put(DonneurEntry.KEY_NOM, donneur.getNom());
        values.put(DonneurEntry.KEY_PRENOM, donneur.getPrenom());
        values.put(DonneurEntry.KEY_GENRE, donneur.getSexe());
        values.put(DonneurEntry.KEY_NAISSANCE, changeIntoString(donneur.getNaissance()));
        values.put(DonneurEntry.KEY_RUE, donneur.getAdresse());
        values.put(DonneurEntry.KEY_NPA, donneur.getNPA());
        values.put(DonneurEntry.KEY_LIEU, donneur.getLieu());
        values.put(DonneurEntry.KEY_REGION, donneur.getRegion());
        values.put(DonneurEntry.KEY_TELEPHONE, donneur.getTelephone());
        values.put(DonneurEntry.KEY_GROUPE, donneur.getGroupe());
        values.put(DonneurEntry.KEY_DON, donneur.getDonsPossibles());
        values.put(DonneurEntry.KEY_DISPONIBILITE, changeIntoString(donneur.getDisponibilite()));
        values.put(DonneurEntry.KEY_UPDATE, 1);

        return this.db.update(DonneurEntry.TABLE_DONNEUR, values, DonneurEntry.KEY_ID + " = ?",
                new String[] { String.valueOf(donneur.getId()) });
    }

    /**
     * Find one Donneur by Id
     */
    public CDonneur getDonneurById(int id) throws ParseException {
        String sql = "SELECT * FROM " + DonneurEntry.TABLE_DONNEUR +
                " WHERE " + DonneurEntry.KEY_ID + " = " + id;

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        CDonneur d = new CDonneur();
        d.setId(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_ID)));
        d.setNom(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_NOM))));
        d.setPrenom(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_PRENOM))));
        d.setSexe(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_GENRE))));
        d.setNaissance(changeIntoDate(cursor.getString(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_NAISSANCE))));
        d.setAdresse(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_RUE))));
        d.setNPA(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_NPA)));
        d.setLieu(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_LIEU))));
        d.setRegion(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_REGION))));
        d.setTelephone(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_TELEPHONE))));
        d.setGroupe(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_GROUPE))));
        d.setDonsPossibles(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_DON)));
        d.setDisponibilite(changeIntoDate(cursor.getString(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_DISPONIBILITE))));
        d.setUpdate(cursor.getInt(cursor.getColumnIndex(DonneurEntry.KEY_UPDATE)));

        return d;
    }

    public CDonneurBack getDonneurByIdBack(int id) throws ParseException {
        String sql = "SELECT * FROM " + DonneurEntry.TABLE_DONNEUR +
                " WHERE " + DonneurEntry.KEY_ID + " = " + id;

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        CDonneurBack d = new CDonneurBack();
        d.setId(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_ID)));
        d.setNom(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_NOM))));
        d.setPrenom(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_PRENOM))));
        d.setSexe(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_GENRE))));
        d.setNaissance(changeIntoDateTime(cursor.getString(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_NAISSANCE))));
        d.setAdresse(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_RUE))));
        d.setNpa(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_NPA)));
        d.setLieu(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_LIEU))));
        d.setRegion(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_REGION))));
        d.setTelephone(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_TELEPHONE))));
        d.setGroupe(cursor.getString(cursor.getColumnIndex((DonDeSangContract.DonneurEntry.KEY_GROUPE))));
        d.setDonsPossibles(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_DON)));
        d.setDisponibilite(changeIntoDateTime(cursor.getString(cursor.getColumnIndex(DonDeSangContract.DonneurEntry.KEY_DISPONIBILITE))));
        d.setUpdate(cursor.getInt(cursor.getColumnIndex(DonneurEntry.KEY_UPDATE)));

        return d;
    }

    public DateTime changeIntoDateTime(String s) throws ParseException {

        DateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.FRENCH);
        Date date = format.parse(s);
        DateTime dateTime = new DateTime(date);
        return dateTime;
    }

    public Date changeIntoDate(String s) throws ParseException {

        DateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.FRENCH);
        Date date = format.parse(s);
        return date;
    }

    public String changeIntoString(Date d) throws ParseException {

        DateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.FRENCH);
        String str = format.format(d);
        return str;
    }
}
