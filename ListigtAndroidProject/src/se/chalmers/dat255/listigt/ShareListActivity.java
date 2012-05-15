package se.chalmers.dat255.listigt;

import android.app.Activity;
import android.os.Bundle;

/**
 * This activity will be launched when a user wants to share it's list with friends.
 * 
 * NOT YET IMPLEMENTED
 * 
 * @author Ackerfors Crew.
 *
 */
public class ShareListActivity extends Activity{
	
	/** 
     * Called when the activity is first created. 
     * Sets the layout, open the database and fills the view up with 
     */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_list); 
		
	}
}
	
