/*
 * Copyright (C) 2012 Ackerfors Crew.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * Base on the "Notepad Tutorial" (C) Google 2008 at developer.android.com
 */

package se.chalmers.dat255.listigt;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ItemView extends ListActivity {
    private ItemsDbAdapter itemDbAdapter;//Creates a new Adapter-object used to access the database
    public static final int INSERT_LIST_ID = Menu.FIRST;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
    private Cursor listCursor;

    /** Called when the activity is first created. 
     * 
     * */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_items); //Sets the layout to the one we specified in res/layout/
        itemDbAdapter = new ItemsDbAdapter(this);//Construct the database-adapter
        itemDbAdapter.open();//open or create the database
        fillData();//calls internal method to fetch data from DB and load it onto our ListView
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_LIST_ID, 0, R.string.menu_insert_list);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){//checks which MenuItem that was selected
    	case INSERT_LIST_ID://if the "Add List"-button was clicked
    		createList();//then call internal method to create a new list
    		return true;//and return true because the operation was successful
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    /**
     * Fetch data from the database adapter class and load it to a Cursor. Then use it to
     * load it onto our ListView.
     */
    private void fillData(){
    	// Get all of the notes from the database and create the item list
        Cursor c = itemDbAdapter.fetchAllItems();
        startManagingCursor(c);

        String[] from = new String[] { ItemsDbAdapter.KEY_TITLE };
        int[] to = new int[] { R.id.listRowTitle };
        
        // Creates an array adapter and set it to display using our row
        SimpleCursorAdapter notes =
            new SimpleCursorAdapter(this, R.layout.items_row, c, from, to);
        setListAdapter(notes);
    }
    
    /** Called to create a new list 
     * Adding more comments
     * 
     * */
    private void createList(){
    	Intent i = new Intent(this, ListEditCreate.class);
    	startActivityForResult(i, ACTIVITY_CREATE);	
    }
    
    @Override
    /**This method runs when an activity that we started finishes and returns information
     * 
     * @param requestCode 
     * @param resultCode
     * @param intent 
   	*/
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	super.onActivityResult(requestCode, resultCode, intent);
    	Bundle extras = intent.getExtras();//take care of the extras the activity may have sent back to us

    	switch(requestCode) {
    	case ACTIVITY_CREATE:
    	    String title = extras.getString(ItemsDbAdapter.KEY_TITLE);
    	    String description = extras.getString(ItemsDbAdapter.KEY_DESCRIPTION);
    	    int parent = extras.getInt(ItemsDbAdapter.KEY_PARENT);
    	    itemDbAdapter.createItem(title, description, parent);
    	    fillData();
    	    break;
    	case ACTIVITY_EDIT:
    	    Long currentRowId = extras.getLong(ItemsDbAdapter.KEY_ROWID);
    	    if (currentRowId != null) {
    	        String updateTitle = extras.getString(ItemsDbAdapter.KEY_TITLE);
    	        String updateDescription = extras.getString(ItemsDbAdapter.KEY_DESCRIPTION);
    	        itemDbAdapter.updateItem(currentRowId, updateTitle, updateDescription);
    	    }
    	    fillData();
    	    break;
    	}
    }
    
   /*@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, ItemEditCreate.class);
        i.putExtra(ItemsDbAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_EDIT);
    }*/
    
    /**
     * When the back-key is pressed we must return something to the activity
     * that started this activity
     */
    @Override
    public void onBackPressed() {
    	setResult(RESULT_OK, new Intent()); //send the result back to the activity that started this activity
    	finish();
    }
}