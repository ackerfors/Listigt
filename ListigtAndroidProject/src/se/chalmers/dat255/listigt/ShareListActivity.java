package se.chalmers.dat255.listigt;

import java.util.Arrays;
import java.util.List;
import javax.mail.SendFailedException;
import jon.simon.mail.Mail;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This activity will be launched when a user wants to share it's list with friends.
 * 
 * @author Ackerfors Crew.
 *
 */
public class ShareListActivity extends Activity{
	private EditText email, subject, message;
	private static Long LIST_ID;
	private Button sendButton;
	private Bundle extras;
	private String listTitle;
	private ItemsDbAdapter itemDbAdapter;
	private Cursor itemCursor;
	
	/** 
     * Called when the activity is first created. 
     * Sets the layout, open the database and fills the view up with 
     */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.share_list);
		email = (EditText) findViewById(R.id.emailField);	
		subject = (EditText) findViewById(R.id.subjectField);
		message = (EditText) findViewById(R.id.messageField);
		sendButton = (Button) findViewById(R.id.sendButton);
		//send button per default enabled
		sendButton.setEnabled(true);
		itemDbAdapter = new ItemsDbAdapter(this);
		extras = getIntent().getExtras();
		LIST_ID = extras.getLong(ListsDbAdapter.KEY_ROWID);
		listTitle = extras.getString(ListsDbAdapter.KEY_TITLE);
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
    	Mail m = new Mail("listigtapp@gmail.com", "1337_listigt"); 
    	String emailString = email.getText().toString();
    	String subjectString = subject.getText().toString();
    	String messageString = message.getText().toString();
        List<String> emailList = Arrays.asList(emailString.split("\\s*,\\s*"));
        String [] emailArr = new String[emailList.size()];
        emailList.toArray(emailArr);
        m.setTo(emailArr); 
        m.setFrom("listigtapp@gmail.com");//has to be an e-mail adress 
        m.setSubject("List from Listigt: " + subjectString);
        m.setBody("Message from the creator: \n" + messageString + "\n\nList title: " + listTitle + "\n\nItems in list:\n" + getItems(LIST_ID));
        System.out.println(getItems(LIST_ID));
        try {  
   
          if(m.send()) { 
            Toast.makeText(ShareListActivity.this, "Your list was shared successfully!", Toast.LENGTH_LONG).show();
            onBackPressed();//go back to the MainListActivity
          } else { 
            Toast.makeText(ShareListActivity.this, "Sorry, but the list was not shared. Please try again!", Toast.LENGTH_LONG).show();
          } 
        } catch(Exception e){
        	Toast.makeText(ShareListActivity.this, "The list could not be shared: " + e.getMessage(), Toast.LENGTH_LONG).show();
        	//Log.e("ShareListActivity", "Could not send email", e);
        }
      }
    
    /**
     * Get the list title and all its items from the database
     */
    private String getItems(long listid){
    	String theItems = "";
    	itemDbAdapter.open();
    	itemCursor = itemDbAdapter.fetchAllItemsFromList(LIST_ID);
        startManagingCursor(itemCursor);
        int cursorRows = itemCursor.getCount();
        
        for ( int i = 0; i < cursorRows; i++) {
        	itemCursor.moveToPosition(i);
        	String title = itemCursor.getString(itemCursor.getColumnIndexOrThrow(ItemsDbAdapter.KEY_TITLE));
        	theItems = theItems + title + "\n";
        }
    	itemDbAdapter.close();
    	return theItems;
    }
}
	
