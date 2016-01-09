package db;


import db.DonDeSangContract.DonneurEntry;
import db.DonDeSangContract.InterventionEntry;
import db.DonDeSangContract.SangEntry;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Suzanne on 07.11.2015.
 */
public class SQLiteHelper extends SQLiteOpenHelper {
    public SQLiteDatabase db;

    //Infos about database
    public static final String DATABASE_NAME = "DonDeSang.db";
    public static final int DATABASE_VERSION = 1;
    public static SQLiteHelper instance;


    //use a singleton
    //we want always just one instance of the database
    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
   }

   public static SQLiteHelper getInstance(Context context){
        if(instance == null){
            instance = new SQLiteHelper(context.getApplicationContext());
        }

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DonneurEntry.CREATE_TABLE_PERSONNE);
        db.execSQL(InterventionEntry.CREATE_TABLE_INTERVENTION);
        db.execSQL(SangEntry.CREATE_TABLE_SANG);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop old tables
        db.execSQL("DROP TABLE IF EXISTS " + DonneurEntry.TABLE_DONNEUR);
        db.execSQL("DROP TABLE IF EXISTS " + InterventionEntry.TABLE_INTERVENTION);
        db.execSQL("DROP TABLE IF EXISTS " + SangEntry.TABLE_SANG);

        //create new tables
        onCreate(db);
    }

}