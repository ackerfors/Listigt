package se.chalmers.dat255.listigt;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ItemDetailsActivity extends Activity {
	private ItemsDbAdapter itemDbAdapter; 
	private TextView itemTitle, itemDesc;
	Long currentRowId;
	
	
	/** Called when the activity is first created. 
     * 
     * */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details); //Sets the layout to the one we specified in res/layout/
        itemTitle = (TextView) findViewById(R.id.itemTitleField);//instantiate the Title-text field	
		itemDesc = (TextView) findViewById(R.id.itemDescField);
        itemDbAdapter = new ItemsDbAdapter(this);//Construct the database-adapter
        itemDbAdapter.open();//open or create the database
        fillData();//calls internal method to fetch data from DB and load it onto our ListView
    }
    
    private void fillData(){
    	currentRowId = null;
		Bundle extras = getIntent().getExtras();
		//Check if the intent that started this activity brought any extra stuff
		if(extras!=null){
			 String title = extras.getString(ItemsDbAdapter.KEY_TITLE);
			 String desc = (String) extras.get(ItemsDbAdapter.KEY_DESCRIPTION);
			 currentRowId = extras.getLong(ItemsDbAdapter.KEY_ROWID);
			 itemTitle.setText(title);//if we're editing an existing item, show its Title
			 itemDesc.setText(desc);//if we're editing an existing item, show its description   
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
