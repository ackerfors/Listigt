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
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ListigtAndroidProjectActivity extends ListActivity {
    private ListsDbAdapter listsDbAdapter;//Creates a new NotesDbAdapter-object used to access the database
    public static final int INSERT_LIST_ID = Menu.FIRST;
    private Cursor listCursor;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main); //Sets the layout to the one we specified in res/layout/notepad_list.xlm
        listsDbAdapter = new ListsDbAdapter(this);//Construct the database-adapter
        listsDbAdapter.open();//open or create the database
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
    
    private void fillData(){
    	//TODO Create this method that is used to fetch data from DB and load it onto our ListView
    	listCursor = dbadapter.fetchAllNotes();
    }
    private void createList(){
    	String hej = "Hej!";
    }
}
