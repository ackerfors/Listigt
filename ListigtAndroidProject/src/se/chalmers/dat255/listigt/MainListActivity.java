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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

/**
 * This class extends ListActivity and is the class that handles
 * the activity of populating the initial screen of this application
 * with a list of lists.
 * 
 * @author Patrik Ackerfors
 *
 */
public class MainListActivity extends ListActivity {
    private ListsDbAdapter listsDbAdapter;//Creates a new Adapter-object used to access the database
    private static final int INSERT_LIST_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int EDIT_ID = Menu.FIRST + 2;
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int ACTIVITY_GOTOITEMS = 2;

	private Cursor listCursor;
	
	
    /** 
     * Called when the activity is first created. 
     * Sets the layout and opens the database adapter. Then fills the views with data
     * from the database.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list); //Sets the layout to the one we specified in res/layout
        listsDbAdapter = new ListsDbAdapter(this);//Construct the database-adapter
        listsDbAdapter.open();//open or create the database
        registerForContextMenu(getListView());
        fillData();//calls internal method to fetch data from DB and load it onto our ListView
    }
    
    /**
     * The menu that is accessed by clicking the menu-button
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_LIST_ID, 0, R.string.menu_insert_list);
        return result;
    }
    
    /**
     * The menu that is accessed by long-clicking a lists title.
     * Gives the options of editing a  and Delete.
     * 
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.delete);
        menu.add(0, EDIT_ID, 0, R.string.edit);
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
        listCursor = listsDbAdapter.fetchAllLists();
        startManagingCursor(listCursor);

        String[] from = new String[] { ListsDbAdapter.KEY_TITLE };
        int[] to = new int[] { R.id.listRowTitle };
        
        // Creates an array adapter and set it to display using our row
        SimpleCursorAdapter lists =
            new SimpleCursorAdapter(this, R.layout.list_row, listCursor, from, to);
        setListAdapter(lists);
        //listsDbAdapter.close();
    }
    
    /** Called to create a new list */
    private void createList(){
    	Intent i = new Intent(this, ListEditor.class);
    	startActivityForResult(i, ACTIVITY_CREATE);	
    }
    
    /**
     * Called when someone clicks on a list. Puts what id the clicked list has
     * and sends it with an extra and launches a new activity that displays
     * that particular list's items.
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, MainItemActivity.class);
        i.putExtra(ItemsDbAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_GOTOITEMS);
    }
    
    /**
     * Called when an option from the context-menu has been clicked.
     * If the user wanted to delete a list, that list will be deleted from
     * the database and this activity will be re-populated. If the user wants
     * to edit a list a new activity, ListEditor, will be launched and the
     * title will be passed as an extra in the intent.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        case DELETE_ID:
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
            listsDbAdapter.deleteList(info.id);
            fillData();
            return true;
        case EDIT_ID:
        	AdapterContextMenuInfo info2 = (AdapterContextMenuInfo) item.getMenuInfo();
        	Cursor c = listCursor;
        	c.moveToPosition(info2.position);
        	Intent i = new Intent(this, ListEditor.class);
        	i.putExtra(ListsDbAdapter.KEY_ROWID, info2.id);
        	i.putExtra(ListsDbAdapter.KEY_TITLE, c.getString(
        	        c.getColumnIndexOrThrow(ListsDbAdapter.KEY_TITLE)));
        	startActivityForResult(i, ACTIVITY_EDIT);
            return true;
        }
        return super.onContextItemSelected(item);
    }
    
   
    /**This method runs when an activity that we started finishes and returns information
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
	    		String title = extras.getString(ListsDbAdapter.KEY_TITLE);
	    	    listsDbAdapter.createList(title);
    	    }
    	    fillData();
    	    break;
    	case ACTIVITY_EDIT:
    	    Long currentRowId = extras.getLong(ListsDbAdapter.KEY_ROWID);
    	    if (currentRowId != null) {
    	        String updateTitle = extras.getString(ListsDbAdapter.KEY_TITLE);
    	        listsDbAdapter.updateList(currentRowId, updateTitle);
    	    }
    	    fillData();
    	    break;
    	case ACTIVITY_GOTOITEMS:
    		fillData();
    		break;
    	}
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listsDbAdapter != null) {
        	listsDbAdapter.close();
        }
    }
}