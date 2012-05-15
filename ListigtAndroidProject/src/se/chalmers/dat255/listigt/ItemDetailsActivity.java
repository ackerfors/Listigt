package se.chalmers.dat255.listigt;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


/**
 * This is the activity class that will handle the display of an item.
 * It will instantiate the item database adapter and use it display the items title and
 * description from the database. This class is called from MainItemActivity and that class
 * will pass the clicked item ID to this class so that the correct item will show.
 * 
 * @author Ackerfors Crew
 *
 */
public class ItemDetailsActivity extends Activity {
	private ItemsDbAdapter itemDbAdapter; 
	private TextView itemTitle, itemDesc;
	Long currentRowId;
	
	/** 
     * Called when the activity is first created. 
     * Sets the layout, open the database and fills the view up with 
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details); 						//Sets the layout to the one we specified in res/layout/
        itemTitle = (TextView) findViewById(R.id.itemTitleField);	//instantiate the Title-text field	
		itemDesc = (TextView) findViewById(R.id.itemDescField);
        itemDbAdapter = new ItemsDbAdapter(this);					//Instantiate the database-adapter
        itemDbAdapter.open();										//open or create the database
        fillData();													//calls internal method to fetch data from DB and load it onto our ListView
    }
    
    /**
     * Fills the title and description fields up with data from the database.
     */
    private void fillData() {
    	currentRowId = null;
		Bundle extras = getIntent().getExtras(); 					// Returns any possible extras from the intent that might have been sent back to us.
		if(extras!=null) {
			 String title = extras.getString(ItemsDbAdapter.KEY_TITLE);
			 String desc = extras.getString(ItemsDbAdapter.KEY_DESCRIPTION);
			 currentRowId = extras.getLong(ItemsDbAdapter.KEY_ROWID);
			 itemTitle.setText(title);								//if we're editing an existing item, show its Title
			 itemDesc.setText(desc);								//if we're editing an existing item, show its description   
		}	
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
}
