/**
 * 
 */
package se.chalmers.dat255.listigt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

/**
 * Implementation of the abstract class AbstractListigtDbAdapter.
 * Fill handle the table specific inserts, selects, deletes etc.
 * 
 * @author paac
 *
 */
public class ListsDbAdapter extends AbstractListigtDbAdapter {
	public static final String KEY_TITLE = "listTitle";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_WEIGHT = "weight";
    public static final String KEY_ROWID = "_id";

    private static final String DATABASE_TABLE = "lists";

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public ListsDbAdapter(Context ctx) {
    	super(ctx);
    }
    
    /**
     * Create a new list with a title, description and a weight.
     * 
     * @param title
     * @param description
     * @param weight
     * @return rowId or -1 if failed
     */
    public long createList(String title, String description, int weight) {
    	ContentValues argumentValues = new ContentValues();
    	argumentValues.put(KEY_TITLE,title);
    	argumentValues.put(KEY_DESCRIPTION,description);
    	argumentValues.put(KEY_WEIGHT, weight);
    	
    	return sqlLiteDb.insert(DATABASE_TABLE, null, argumentValues);
    }
    

    /**
     * Delete a list with the given rowId
     * 
     * @param rowId
     * @return true if deleted, false otherwise
     */
    public boolean deleteList(long rowId) {
    	return sqlLiteDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all lists in the database
     * 
     * @return Cursor over all lists
     */
    public Cursor fetchAllLists() {
    	return sqlLiteDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE,
    			KEY_DESCRIPTION, KEY_WEIGHT}, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the list that matches the given rowId
     * 
     * @param rowId id of route to retrieve
     * @return Cursor positioned to matching route, if found
     * @throws SQLException if route could not be found/retrieved
     */
    public Cursor fetchList(long rowId) throws SQLException {
        Cursor myCursor =
        	sqlLiteDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                    KEY_TITLE, KEY_DESCRIPTION, KEY_WEIGHT}, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (myCursor != null) {
        	myCursor.moveToFirst();
        }
        return myCursor;
    }
}
