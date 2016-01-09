package db.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import db.SQLiteHelper;
import db.DonDeSangContract.InterventionEntry;
import db.object.CIntervention;
import db.object.CSang;

/**
 * Created by Suzanne on 07.11.2015.
 */
public class InterventionDataSource {

    private SQLiteDatabase db;
    private Context context;
    private SangDataSource sa;


    public InterventionDataSource(Context context){
        SQLiteHelper sqliteHelper = SQLiteHelper.getInstance(context);
        db = sqliteHelper.getWritableDatabase();
        this.context = context;
        sa = new SangDataSource(context);
    }

    /**
     * Insert a new intervention
     */
    public long createIntervention(CIntervention intervention) throws ParseException {
        long id;
        ContentValues values = new ContentValues();
        values.put(InterventionEntry.KEY_QUANTITE, intervention.getQuantite());
        values.put(InterventionEntry.KEY_DATE, changeIntoString(intervention.getDate()));
        values.put(InterventionEntry.KEY_DESCRIPTION, intervention.getDescription());
        values.put(InterventionEntry.KEY_GROUPE, intervention.getGroupe());
        values.put(InterventionEntry.KEY_REGION, intervention.getRegion());


        id = this.db.insert(InterventionEntry.TABLE_INTERVENTION, null, values);

        return id;
    }

    /**
     *  Update a Intervention
     */
    public int updateIntervention(CIntervention intervention) throws ParseException {
        ContentValues values = new ContentValues();
        values.put(InterventionEntry.KEY_QUANTITE, intervention.getQuantite());
        values.put(InterventionEntry.KEY_DATE, changeIntoString(intervention.getDate()));
        values.put(InterventionEntry.KEY_DESCRIPTION, intervention.getDescription());
        values.put(InterventionEntry.KEY_GROUPE, intervention.getGroupe());
        values.put(InterventionEntry.KEY_REGION, intervention.getRegion());

        return this.db.update(InterventionEntry.TABLE_INTERVENTION, values, InterventionEntry.KEY_ID + " = ?",
                new String[] { String.valueOf(intervention.getId()) });
    }


    /**
     * Get all Interventions
     */
    public List<CIntervention> getAllInterventions() throws ParseException {
        List<CIntervention> interventions = new ArrayList<CIntervention>();
        String sql = "SELECT * FROM " + InterventionEntry.TABLE_INTERVENTION + " ORDER BY " + InterventionEntry.KEY_DATE + " ;";

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor.moveToFirst()){
            do{
                CIntervention i = new CIntervention();
                i.setId(cursor.getInt(cursor.getColumnIndex(InterventionEntry.KEY_ID)));
                i.setDate(changeIntoDate(cursor.getString(cursor.getColumnIndex(InterventionEntry.KEY_DATE))));
                i.setDescription(cursor.getString(cursor.getColumnIndex(InterventionEntry.KEY_DESCRIPTION)));
                i.setGroupe(cursor.getString(cursor.getColumnIndex(InterventionEntry.KEY_GROUPE)));
                i.setQuantite(cursor.getInt(cursor.getColumnIndex(InterventionEntry.KEY_QUANTITE)));
                i.setRegion(cursor.getString(cursor.getColumnIndex(InterventionEntry.KEY_REGION)));

                interventions.add(i);
            } while(cursor.moveToNext());
        }

        return interventions;
    }

    /**
     * Find one Intervention by Id
     */
    public CIntervention getInterventiononById(int id) throws ParseException {
        String sql = "SELECT * FROM " + InterventionEntry.TABLE_INTERVENTION +
                " WHERE " + InterventionEntry.KEY_ID + " = " + id;

        Cursor cursor = this.db.rawQuery(sql, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        CIntervention i = new CIntervention();
        i.setId(cursor.getInt(cursor.getColumnIndex(InterventionEntry.KEY_ID)));
        i.setDate(changeIntoDate(cursor.getString(cursor.getColumnIndex(InterventionEntry.KEY_DATE))));
        i.setDescription(cursor.getString(cursor.getColumnIndex(InterventionEntry.KEY_DESCRIPTION)));
        i.setQuantite(cursor.getInt(cursor.getColumnIndex(InterventionEntry.KEY_QUANTITE)));
        i.setGroupe(cursor.getString(cursor.getColumnIndex(InterventionEntry.KEY_GROUPE)));
        i.setRegion(cursor.getString(cursor.getColumnIndex(InterventionEntry.KEY_REGION)));

        return i;
    }

    /**
     * Delete a Intervention - this will also delete all records
     * for the intervention
     */
    public void deleteIntervention(long id) throws ParseException {

        List<CSang> liste = sa.getAllSangsByIntervention((int)id);

        for(int i = 0; i<liste.size(); i++){
            liste.get(i).setIntervention(-1);
            liste.get(i).setStatut("en stock");
            sa.updateSang(liste.get(i));
        }

        //delete the intervention
        this.db.delete(InterventionEntry.TABLE_INTERVENTION, InterventionEntry.KEY_ID + " = ?",
                new String[]{String.valueOf(id)});

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

