/**
 * 
 */
package se.chalmers.dat255.listigt;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

/**
 * Extends the abstract class AbstractListigtDbAdapter.
 * Gets, inserts, deletes or updates items in the database.
 * 
 * @author Ackerfors Crew
 *
 */
public class ItemsDbAdapter extends AbstractListigtDbAdapter {
	public static final String KEY_TITLE = "itemTitle";
    public static final String KEY_DESCRIPTION = "description";
    //public static final String KEY_WEIGHT = "weight";
    public static final String KEY_BOOKED = "booked";
    public static final String KEY_PARENT = "parent";
    public static final String KEY_ROWID = "_id";
    private static final String DATABASE_TABLE = "items";

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx the Context within which to work
     */
    public ItemsDbAdapter(Context ctx) {
    	super(ctx);
    }
    
    /**
     * Create a new item with a title, description and it's parent list.
     * 
     * @param title The items Title
     * @param description The items description
     * @param parent The items parent list id.
     * @return rowId or -1 if failed
     */
    public long createItem(String title, String description, int parent) {
    	ContentValues argumentValues = new ContentValues();
    	argumentValues.put(KEY_TITLE, title);
    	argumentValues.put(KEY_DESCRIPTION, description);
    	//argumentValues.put(KEY_WEIGHT, weight);
    	argumentValues.put(KEY_BOOKED, 0);
    	argumentValues.put(KEY_PARENT, parent);
    	
    	return sqlLiteDb.insert(DATABASE_TABLE, null, argumentValues);
    }
    

    /**
     * Delete an item with the given rowId
     * 
     * @param rowId
     * @return true if deleted, false otherwise
     */
    public boolean deleteItem(long rowId) {
    	return sqlLiteDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all items in the database
     * 
     * @return Cursor over all items
     */
    public Cursor fetchAllItems() {
    	String[] table = new String[] {KEY_ROWID, KEY_TITLE, KEY_DESCRIPTION, KEY_BOOKED, KEY_PARENT};
    	
    	return sqlLiteDb.query(DATABASE_TABLE, table, null, null, null, null, null);
    }
    
    /**
     * Return a Cursor over all items that matches the parent id.
     * 
     * @param listId id of route to retrieve
     * @return Cursor positioned to matching route, if found
     * @throws SQLException if route could not be found/retrieved
     */
    public Cursor fetchAllItemsFromList(long listId) throws SQLException {
        String[] table = new String[] {KEY_ROWID, KEY_TITLE, KEY_DESCRIPTION, KEY_BOOKED, KEY_PARENT};
    	String whereString = KEY_PARENT + "=" + listId;
        Cursor myCursor = sqlLiteDb.query(DATABASE_TABLE, table, whereString , null, null, null, null, null);
        
        return myCursor;
    }

    /**
     * Return a Cursor positioned at the item that matches the given rowId
     * 
     * @param rowId id of route to retrieve
     * @return Cursor positioned to matching route, if found
     * @throws SQLException if route could not be found/retrieved
     */
    public Cursor fetchItem(long rowId) throws SQLException {
        Cursor myCursor =
        	sqlLiteDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID,
                    KEY_TITLE, KEY_DESCRIPTION, KEY_BOOKED, KEY_PARENT}, KEY_ROWID + "=" + rowId, null,
                    null, null, null, null);
        if (myCursor != null) {
        	myCursor.moveToFirst();
        }
        return myCursor;
    }
    
    /**
     * Update the item using the provided title and description. The item to be updated is
     * specified using the rowId.
     * 
     * @param rowId id of note to update
     * @param title value to set note title to
     * @param description value to set note body to
     * @return true if the note was successfully updated, false otherwise
     */
    public boolean updateItem(long rowId, String title, String description) {
        ContentValues argumentValues = new ContentValues();
        argumentValues.put(KEY_TITLE, title);
        argumentValues.put(KEY_DESCRIPTION, description);

        return sqlLiteDb.update(DATABASE_TABLE, argumentValues, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
