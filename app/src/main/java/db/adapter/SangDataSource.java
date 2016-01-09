package db.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.betrisey.suzanne.androidproject.Sang;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import db.DonDeSangContract;
import db.SQLiteHelper;
import db.object.CIntervention;
import db.object.CSang;

/**
 * Created by Suzanne on 25.11.2015.
 */
public class SangDataSource {

    private SQLiteDatabase db;
    private Context context;

    public SangDataSource(Context context){
        SQLiteHelper sqliteHelper = SQLiteHelper.getInstance(context);
        db = sqliteHelper.getWritableDatabase();
        this.context = context;
    }

    /**
     * Insert a new pochette
     */
    public long createSang(CSang sang) throws ParseException {
        long id;
        ContentValues values = new ContentValues();
        values.put(DonDeSangContract.SangEntry.KEY_ID_DONNEUR, sang.getDonneur());
        values.put(DonDeSangContract.SangEntry.KEY_DATE_DON, changeIntoString(sang.getDateDon()));
        values.put(DonDeSangContract.SangEntry.KEY_DATE_PEREMPTION, changeIntoString(sang.getPeremption()));
        values.put(DonDeSangContract.SangEntry.KEY_REGION, sang.getRegion());
        values.put(DonDeSangContract.SangEntry.KEY_GROUPE, sang.getGroupe());
        values.put(DonDeSangContract.SangEntry.KEY_STATUT, sang.getStatut());
        values.put(DonDeSangContract.SangEntry.KEY_ID_INTERVENTION, sang.getIntervention());

        id = this.db.insert(DonDeSangContract.SangEntry.TABLE_SANG, null, values);

        return id;
    }

    /**
     * Find one Sang by Id
     */
    public CSang getSangById(int id) throws ParseException {
        String sql = "SELECT * FROM " + DonDeSangContract.SangEntry.TABLE_SANG +
                " WHERE " + DonDeSangContract.SangEntry.KEY_ID + " = " + id;

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        CSang s = new CSang();
        s.setId(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_ID)));
        s.setDonneur(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_ID_DONNEUR)));
        s.setDateDon(changeIntoDate(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_DATE_DON))));
        s.setPeremption(changeIntoDate(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_DATE_PEREMPTION))));
        s.setRegion(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_REGION)));
        s.setGroupe(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_GROUPE)));
        s.setStatut(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_STATUT)));
        s.setIntervention(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_ID_INTERVENTION)));

        return s;
    }

    /**
     * Get all Sangs
     */
    public List<CSang> getAllSangs() throws ParseException {
        List<CSang> sangs = new ArrayList<CSang>();
        String sql = "SELECT * FROM " + DonDeSangContract.SangEntry.TABLE_SANG + " ORDER BY " + DonDeSangContract.SangEntry.KEY_GROUPE + " ;";

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                CSang s = new CSang();
                s.setId(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_ID)));
                s.setDonneur(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_ID_DONNEUR)));
                s.setDateDon(changeIntoDate(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_DATE_DON))));
                s.setPeremption(changeIntoDate(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_DATE_PEREMPTION))));
                s.setRegion(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_REGION)));
                s.setGroupe(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_GROUPE)));
                s.setStatut(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_STATUT)));
                s.setIntervention(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_ID_INTERVENTION)));

                sangs.add(s);
            } while(cursor.moveToNext());
        }

        return sangs;
    }

    public List<CSang> getAllSangsByGroupe() throws ParseException {
        List<CSang> sangs = new ArrayList<CSang>();
        String sql = "SELECT * FROM " + DonDeSangContract.SangEntry.TABLE_SANG + " ORDER BY " + DonDeSangContract.SangEntry.KEY_GROUPE + " ;";

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                CSang s = new CSang();
                s.setId(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_ID)));
                s.setDonneur(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_ID_DONNEUR)));
                s.setDateDon(changeIntoDate(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_DATE_DON))));
                s.setPeremption(changeIntoDate(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_DATE_PEREMPTION))));
                s.setRegion(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_REGION)));
                s.setGroupe(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_GROUPE)));
                s.setStatut(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_STATUT)));
                s.setIntervention(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_ID_INTERVENTION)));

                sangs.add(s);
            } while(cursor.moveToNext());
        }

        return sangs;
    }

    public List<CSang> getAllSangsByIntervention(int intervention) throws ParseException {
        List<CSang> sangs = new ArrayList<CSang>();
        String sql = "SELECT * FROM " + DonDeSangContract.SangEntry.TABLE_SANG + " WHERE " + DonDeSangContract.SangEntry.KEY_ID_INTERVENTION + " = '" + intervention + "'" + " ORDER BY " + DonDeSangContract.SangEntry.KEY_GROUPE + " ;";

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                CSang s = new CSang();
                s.setId(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_ID)));
                s.setDonneur(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_ID_DONNEUR)));
                s.setDateDon(changeIntoDate(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_DATE_DON))));
                s.setPeremption(changeIntoDate(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_DATE_PEREMPTION))));
                s.setRegion(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_REGION)));
                s.setGroupe(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_GROUPE)));
                s.setStatut(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_STATUT)));
                s.setIntervention(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_ID_INTERVENTION)));

                sangs.add(s);
            } while(cursor.moveToNext());
        }

        return sangs;
    }

    public List<CSang> getAllSangsByStatut() throws ParseException {
        List<CSang> sangs = new ArrayList<CSang>();
        String sql = "SELECT * FROM " + DonDeSangContract.SangEntry.TABLE_SANG +  " ORDER BY lower(" + DonDeSangContract.SangEntry.KEY_STATUT + ");";

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                CSang s = new CSang();
                s.setId(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_ID)));
                s.setDonneur(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_ID_DONNEUR)));
                s.setDateDon(changeIntoDate(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_DATE_DON))));
                s.setPeremption(changeIntoDate(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_DATE_PEREMPTION))));
                s.setRegion(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_REGION)));
                s.setGroupe(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_GROUPE)));
                s.setStatut(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_STATUT)));
                s.setIntervention(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_ID_INTERVENTION)));

                sangs.add(s);
            } while(cursor.moveToNext());
        }

        return sangs;
    }

    public List<CSang> getAllSangsByDate() throws ParseException {
        List<CSang> sangs = new ArrayList<CSang>();
        String sql = "SELECT * FROM " + DonDeSangContract.SangEntry.TABLE_SANG + " ORDER BY " + DonDeSangContract.SangEntry.KEY_DATE_PEREMPTION+ " ;";

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                CSang s = new CSang();
                s.setId(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_ID)));
                s.setDonneur(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_ID_DONNEUR)));
                s.setDateDon(changeIntoDate(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_DATE_DON))));
                s.setPeremption(changeIntoDate(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_DATE_PEREMPTION))));
                s.setRegion(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_REGION)));
                s.setGroupe(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_GROUPE)));
                s.setStatut(cursor.getString(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_STATUT)));
                s.setIntervention(cursor.getInt(cursor.getColumnIndex(DonDeSangContract.SangEntry.KEY_ID_INTERVENTION)));

                sangs.add(s);
            } while(cursor.moveToNext());
        }

        return sangs;
    }

    /**
     *  Update a Sang
     */
    public int updateSang(CSang sang) throws ParseException {
        ContentValues values = new ContentValues();
        values.put(DonDeSangContract.SangEntry.KEY_REGION, sang.getRegion());
        values.put(DonDeSangContract.SangEntry.KEY_STATUT, sang.getStatut());
        values.put(DonDeSangContract.SangEntry.KEY_ID_INTERVENTION, sang.getIntervention());

        return this.db.update(DonDeSangContract.SangEntry.TABLE_SANG, values, DonDeSangContract.SangEntry.KEY_ID + " = ?",
                new String[] { String.valueOf(sang.getId()) });
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
