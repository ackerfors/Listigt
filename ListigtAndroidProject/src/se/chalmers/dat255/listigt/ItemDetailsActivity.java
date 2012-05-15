package se.chalmers.dat255.listigt;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
	private TextView itemTitle, itemDesc, itemStatus;
	private Long currentRowId;
	private Button editButton, bookButton, deleteButton;
	private Boolean status;
	private String title, desc, statusText;
	private static final int ACTIVITY_EDIT = 0;
	private Bundle extras;
	private Cursor itemCursor;
	
	
	/** 
     * Called when the activity is first created. 
     * Sets the layout, open the database and fills the view up with 
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details); 						//Sets the layout to the one we specified in res/layout/
        itemTitle = (TextView) findViewById(R.id.itemTitleField);	//instantiate the text fields	
		itemDesc = (TextView) findViewById(R.id.itemDescField);
		itemStatus = (TextView) findViewById(R.id.itemStatusField);
		editButton = (Button) findViewById(R.id.editButton);		//instantiate the buttons
		editButton.setEnabled(true);
		bookButton = (Button) findViewById(R.id.bookButton);
		deleteButton = (Button) findViewById(R.id.deleteButton);
		bookButton.setEnabled(true);
		deleteButton.setEnabled(true);
        itemDbAdapter = new ItemsDbAdapter(this);					//Instantiate the database-adapter
        itemDbAdapter.open();										//open or create the database
        extras = getIntent().getExtras(); 							//Take care of anything that was sent to us
        currentRowId = extras.getLong(ItemsDbAdapter.KEY_ROWID);	//Get the rowId for this item 
        fillData();													//calls internal method to fetch data from DB and load it onto our ListView
    }
    
    /**
     * Fills the title and description fields up with data from the database.
     */
    private void fillData() {
    	itemCursor = itemDbAdapter.fetchItem(currentRowId);
    	startManagingCursor(itemCursor);
    	title = itemCursor.getString(itemCursor.getColumnIndexOrThrow(ItemsDbAdapter.KEY_TITLE));
    	status = false;//Tillsvidare
		desc = itemCursor.getString(itemCursor.getColumnIndexOrThrow(ItemsDbAdapter.KEY_DESCRIPTION));
			if(status){
				statusText = "Booked";
				bookButton.setEnabled(false);
				deleteButton.setEnabled(false);
			}
			else{
				statusText ="Unbooked";
			}	
		 itemTitle.setText(title);								
		 itemDesc.setText(desc);								
		 itemStatus.setText(statusText);
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
     * When the Edit-button is clicked, this method runs (set in item_details.xml)
     */
    public void editItem(View v){
    	Intent i = new Intent(this, ItemEditor.class);
    	i.putExtra(ItemsDbAdapter.KEY_ROWID, currentRowId);
    	i.putExtra(ItemsDbAdapter.KEY_TITLE, title);
    	i.putExtra(ItemsDbAdapter.KEY_DESCRIPTION, desc);
    	startActivityForResult(i, ACTIVITY_EDIT);
    }
    
    /**
     * When the Book-button is clicked, this method runs (set in item_details.xml)
     */
    public void bookItem(View v){
    	bookButton.setBackgroundColor(Color.GREEN);
    }
    
    /**
     * When the Delete-button is clicked, this method runs (set in item_details.xml)
     */
    public void deleteItem(View v){
    	itemDbAdapter.deleteItem(currentRowId);
    	onBackPressed();//Brings us back to where we came from
    }
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
    	case ACTIVITY_EDIT:
    	    Long returnedRowId = extras.getLong(ItemsDbAdapter.KEY_ROWID);
    	    if (returnedRowId != null) {
    	        String updateTitle = extras.getString(ItemsDbAdapter.KEY_TITLE);
    	        String updateDescription = extras.getString(ItemsDbAdapter.KEY_DESCRIPTION);
    	        itemDbAdapter.updateItem(returnedRowId, updateTitle, updateDescription);
    	    }
    	    fillData();
    	    break;
    	}
    }
    
}
