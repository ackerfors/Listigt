package se.chalmers.dat255.listigt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * This activity will be launched when a user wants to share it's list with friends.
 * 
 * @author Ackerfors Crew.
 *
 */
public class ShareListActivity extends Activity{
	private EditText email, subject, message;
	private Long currentRowId;
	private AlertDialog.Builder builder;
	private Button sendButton;
	private Bundle extras;
	
	/** 
     * Called when the activity is first created. 
     * Sets the layout, open the database and fills the view up with 
     */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_list); 
		builder = new AlertDialog.Builder(this);
		email = (EditText) findViewById(R.id.emailField);	
		//email.addTextChangedListener(this); - might want to listen to make sure e-mail adress is correct
		subject = (EditText) findViewById(R.id.subjectField);
		message = (EditText) findViewById(R.id.messageField);
		
		sendButton = (Button) findViewById(R.id.sendButton);
		//confirm button per default disabled
		sendButton.setEnabled(true);//CHANGE TO FALSE!
		
		currentRowId = null;
		extras = getIntent().getExtras();
		
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
     * When the Send-button is clicked, this method runs (set in share_list.xml)
     */
    public void sendList(View v){
    	//send e-mail stuff. To be implemented by ACKERFORS
    	showDialog();//Show confirm dialog, and then go back to where we came from
    }
    
    /**
	 * Shows a confirm dialog when the list has been shared
	 */
	private void showDialog(){
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			// Setting Dialog Title
			alertDialog.setTitle("The list has succesfully been shared!");
			// Setting OK Button
			alertDialog.setButton("OK, got it!", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) {
			        // Write your code here to execute after dialog closed
			        dialog.dismiss();
			        onBackPressed();//go back to the MainListActivity
			        }
			});
			// Showing Alert Message
			alertDialog.show();
	}
}
	
