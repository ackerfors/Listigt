package se.chalmers.dat255.listigt;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Creates the outer class of a database adapter that will handle all of
 * the creates, reads, updates and deletes in the local SQLite database.
 * This is an abstract class and an implementation for each table must be
 * implemented. The two tables are items and lists.
 * 
 *@author Ackerfors Crew
 */
public abstract class AbstractListigtDbAdapter {
    protected InnerSQLOpenHelper innerDbHelpder;
    protected SQLiteDatabase sqlLiteDb;
    
    protected static final String TAG = "AbstractDBAdapter";
    protected static final String CREATE_ITEMS_TABLE = 
		"CREATE TABLE items (_id INTEGER PRIMARY KEY AUTOINCREMENT, itemTitle TEXT NOT NULL, " +
		"description TEXT, booked INTEGER, parent INTEGER);";
    protected static final String CREATE_LISTS_TABLE = 
		"CREATE TABLE lists (_id INTEGER PRIMARY KEY AUTOINCREMENT, listTitle TEXT NOT NULL);";
    protected static final String DATABASE_NAME = "Listigt";
    protected static final int DATABASE_VERSION = 2;
    protected static final String DATABASE_ITEMS_TABLE = "items";
    protected static final String DATABASE_LISTS_TABLE = "lists";
    protected final Context myContext;
	
	/**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param listigtAnadroidProjectActivity the Context within which to work
     */
	public AbstractListigtDbAdapter(Context listigtAnadroidProjectActivity) {
		this.myContext = listigtAnadroidProjectActivity;
	}
	
	/**
     * Instantiates a new Object of the static innerclass InnerSQLOpenHelper.
     * It opens the database if it exists, else it creates it or upgrades it.
     * 
     * @return this (self reference, allowing this to be chained in an
     *         Initialisation call)
     * @throws SQLException if the database could be neither opened or created
     */
    public AbstractListigtDbAdapter open() throws SQLException {
    	innerDbHelpder = new InnerSQLOpenHelper(myContext); 	//instantiate a new DatabaseHelper object.
        sqlLiteDb = innerDbHelpder.getWritableDatabase();	//Open, create or upgrade SQLite database.
        return this;
    }
    
    /**
     * Closes the InnerSQLOpenHelper and database connection.
     */
    public void close() {
    	innerDbHelpder.close();
    }
	
    
    /**
     * The inner class of the database adapter. This will extend SQLiteOpenHelper and 
     * will create the SQL database if it does not exist and upgrade it if necessary.
     * 
     * @author Ackerfors Crew
     */
	private static class InnerSQLOpenHelper extends SQLiteOpenHelper {
		
		
		public InnerSQLOpenHelper (Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		/**
		 * Creates two tables, items and lists, it they don't exist.
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_ITEMS_TABLE);
			db.execSQL(CREATE_LISTS_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS items");
            db.execSQL("DROP TABLE IF EXISTS lists");
            onCreate(db);
		}	
	} //End inner class
} //End package
