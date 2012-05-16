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
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


/**
 * This is the activity class that will handle the display of items.
 * It will instantiate the item database adapter and use it to fill up lines with
 * items from the database. This class is called from MainListActivity and that class
 * will pass the clicked lists ID to this class so that the correct items will show.
 * 
 * @author Ackerfors Crew
 *
 */
public class MainItemActivity extends ListActivity {
    private ItemsDbAdapter itemDbAdapter;				//Creates a new Adapter-object used to access the database
    public static final int INSERT_ITEM_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int EDIT_ID = Menu.FIRST + 2;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int ACTIVITY_DETAILS = 2;
	private static long LIST_ID;
    private Cursor itemCursor;
    private Button addItemButton;

    /**  
     * Called when the activity is first created.
     * Gets the lists id from the intent that activated this activity and sets it to LIST_ID.
     * 
     * */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();		//take care of the extras the activity may have sent back to us
    	LIST_ID = extras.getLong("_id");
        setContentView(R.layout.main_items); 			//Sets the layout to the one we specified in res/layout/
        itemDbAdapter = new ItemsDbAdapter(this);		//Construct the database-adapter
        itemDbAdapter.open();							//open or create the database
        registerForContextMenu(getListView());
        addItemButton = (Button) findViewById(R.id.addItem);
		addItemButton.setEnabled(true);
        fillData();										//calls internal method to fetch data from DB and load it onto our ListView
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ITEM_ID, 0, R.string.menu_insert_item);
        return result;
    }
    
    /**
     * The menu that is accessed by long-clicking a list.
     * The options Delete and Edit will be present.
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.delete);
        menu.add(0, EDIT_ID, 0, R.string.edit);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()){		//checks which MenuItem that was selected
    	case INSERT_ITEM_ID:			//if the "Add Item"-button was clicked
    		createItem();				//then call internal method to create a new item
    		return true;				//and return true because the operation was successful
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    /**
     * Fetch data from the database adapter class and load it to a Cursor. 
     * Gets the items from the list that started this activity.
     * Then use it to load it onto our ListView.
     */
    private void fillData(){
        itemCursor = itemDbAdapter.fetchAllItemsFromList(LIST_ID);
        startManagingCursor(itemCursor);

        String[] from = new String[] { ItemsDbAdapter.KEY_TITLE };
        int[] to = new int[] { R.id.itemRowTitle };
        
        // Creates an array adapter and set it to display using our row
        ItemsCursorAdapter items =
            new ItemsCursorAdapter(this, R.layout.items_row, itemCursor, from, to);
        setListAdapter(items);
    }
    
    /** 
     * Called to create a new item.
     * 
     * */
    private void createItem(){
    	Intent i = new Intent(this, ItemEditor.class);
    	startActivityForResult(i, ACTIVITY_CREATE);	
    }
    
//    public void clickHandler(View view){
//    	final int checkBoxPosition = getListView().getPositionForView(view)+1;
//    		System.out.println(checkBoxPosition);
//    }
    
    /**
     * This method runs when an activity that we started finishes and returns information
     * 
     * @param requestCode 
     * @param resultCode
     * @param intent 
   	*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    	super.onActivityResult(requestCode, resultCode, intent);
    	Bundle extras = intent.getExtras();//take care of the extras the activity may have sent back to us
    	switch(requestCode) {
    	case ACTIVITY_CREATE:
    	    if(extras != null){
	    		String title = extras.getString(ItemsDbAdapter.KEY_TITLE);
	    	    String description = extras.getString(ItemsDbAdapter.KEY_DESCRIPTION);
	    	    itemDbAdapter.createItem(title, description, (int) LIST_ID);
    	    }
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
    	case ACTIVITY_DETAILS:
    		fillData();
    		break;
    	}
    }
    
    /**
     * Runs when the users clicks on an item. Bundles the relevant information
     * into the extras of the intent and starts the activity that shows the items
     * details.
     */
   @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    	Cursor c = itemCursor;
    	c.moveToPosition(position);
    	Intent i = new Intent(this, ItemDetailsActivity.class);
    	i.putExtra(ItemsDbAdapter.KEY_ROWID, id);//it's enough to send only the ID of the item to ItemDetailsActivity
    	startActivityForResult(i, ACTIVITY_DETAILS);
    }
    
    /**
     * Called when an option from the context-menu has been clicked.
     * Gives the options of editing or deleting an item. If editing is clicked
     * a new activity, ItemEditor, will be launched.
     * 
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        case DELETE_ID:
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
            itemDbAdapter.deleteItem(info.id);
            fillData();
            return true;
        case EDIT_ID:
        	AdapterContextMenuInfo info2 = (AdapterContextMenuInfo) item.getMenuInfo();
        	Cursor c = itemCursor;
        	c.moveToPosition(info2.position);
        	Intent i = new Intent(this, ItemEditor.class);
        	//When editing an item we must proved the ItemEditor-class
        	//with ID, Title and Description for the Item
        	i.putExtra(ItemsDbAdapter.KEY_ROWID, info2.id);
        	i.putExtra(ItemsDbAdapter.KEY_TITLE, c.getString(
        	        c.getColumnIndexOrThrow(ItemsDbAdapter.KEY_TITLE)));
        	i.putExtra(ItemsDbAdapter.KEY_DESCRIPTION, c.getString(
        	        c.getColumnIndexOrThrow(ItemsDbAdapter.KEY_DESCRIPTION)));
        	startActivityForResult(i, ACTIVITY_EDIT);
            return true;
        }
        return super.onContextItemSelected(item);
    }
    
    public void addItemButtonPressed(View v){
    	createItem();
    }
    
    /**
     * When the back-key is pressed we simply return to the previous activity
     * with the same Intent (no data changed)
     */
    @Override
    public void onBackPressed() {
    	setResult(RESULT_OK, getIntent());
    	finish();
    }
    
    /**
     * Closes the database adapter when the activity seize to exist.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (itemDbAdapter != null) {
        	itemDbAdapter.close();
        }
    }
}