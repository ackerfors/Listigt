package se.chalmers.dat255.listigt;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class ListEditor extends Activity implements TextWatcher {
	EditText editableListTitle; //creates an editable textbox
	Long currentRowId;
	AlertDialog.Builder builder;
	Button confirmButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_editcreate);//use the layout defined in list_editcreate.xml
		setTitle(R.string.listEditCreateTitle);//set the title for this activity
		editableListTitle = (EditText) findViewById(R.id.listEditTitleField);//instantiate the Title-text field	
		editableListTitle.addTextChangedListener(this);
		confirmButton = (Button) findViewById(R.id.confirmButton);//instantiate the confirm button
		confirmButton.setEnabled(false);//per default disabled
		builder = new AlertDialog.Builder(this);
		
		currentRowId = null;
		Bundle extras = getIntent().getExtras();
		//Check if the intent that started this activity brought any extra stuff
		if(extras!=null){
			 String title = extras.getString(ListsDbAdapter.KEY_TITLE);
			 currentRowId = extras.getLong(ListsDbAdapter.KEY_ROWID);
			 
			 if (title != null) {
			        editableListTitle.setText(title);//if we're editing an existing list, show its Title
			        confirmButton.setEnabled(true);
			    }
		}
		
		/**Begin listening to the confirmButton */
		confirmButton.setOnClickListener(new View.OnClickListener() {
			/** When the button is clicked, run this method*/
		    public void onClick(View view) {
		    	if(TextUtils.isEmpty(editableListTitle.getText().toString())){
		    		//POP-UP
		    		showDialog();
		    	} else {
			    	Bundle bundle = new Bundle();
	
			    	bundle.putString(ListsDbAdapter.KEY_TITLE, editableListTitle.getText().toString());
			    	if (currentRowId != null) {
			    	    bundle.putLong(ListsDbAdapter.KEY_ROWID, currentRowId);
			    	}
			    	
			    	Intent updateIntent = new Intent();
			    	updateIntent.putExtras(bundle);//ship all the data stored in the bundle with the intent
			    	setResult(RESULT_OK, updateIntent);//send the result back to the activity that started this activity
			    	finish();
		    	}
		    }
		});
	}
	
	private void showDialog(){
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			// Setting Dialog Title
			alertDialog.setTitle("Your list needs a title buddy!");
			
			// Setting OK Button
			alertDialog.setButton("OK, got it!", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) {
			        // Write your code here to execute after dialog closed
			        dialog.dismiss();
			        }
			});
			
			// Showing Alert Message
			alertDialog.show();
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
     * Methods that run when the title is being edited
     */

	public void afterTextChanged(Editable s) {
		// No need to implement this
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// No need to implement this
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
			confirmButton.setEnabled(!TextUtils.isEmpty(editableListTitle.getText().toString()));
    	
	}
}
