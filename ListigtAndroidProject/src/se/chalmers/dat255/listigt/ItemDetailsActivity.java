package se.chalmers.dat255.listigt;

import android.app.Activity;
import android.os.Bundle;

public class ItemDetailsActivity extends Activity {
	private ItemsDbAdapter itemDbAdapter; 
	
	
	/** Called when the activity is first created. 
     * 
     * */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_list); //Sets the layout to the one we specified in res/layout/
        itemDbAdapter = new ItemsDbAdapter(this);//Construct the database-adapter
        itemDbAdapter.open();//open or create the database
        fillData();//calls internal method to fetch data from DB and load it onto our ListView
    }
    
    private void fillData(){
    	
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
