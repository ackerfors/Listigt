package se.chalmers.dat255.listigt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * This class is called from the MainItemActivity if the user wants to add or edit an item
 * from a specific list. 
 * 
 * @author Ackerfors Crew
 *
 */
public class ItemEditor extends Activity implements TextWatcher {
	EditText editableItemTitle, editableItemDesc; 								//creates two editable textboxes
	Long currentRowId;
	Button confirmButton;
	
	/**
	 * This method runs when this activity is started. Sets the layout and gets
	 * the title and description from the extras. 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_editcreate);								//use the layout defined in item_editcreate.xml
		setTitle(R.string.itemEditCreateTitle);									//set the title for this activity
		editableItemTitle = (EditText) findViewById(R.id.itemEditTitleField);	//instantiate the Title-text field	
		editableItemTitle.addTextChangedListener(this);
		editableItemDesc = (EditText) findViewById(R.id.itemEditDescField);
		confirmButton = (Button) findViewById(R.id.confirmButton);				//instantiate the confirm button
		confirmButton.setEnabled(false);
		currentRowId = null;
		Bundle extras = getIntent().getExtras();								//Gets extras (if any) that the class might have sent back to us.
		if(extras!=null){
			String title = extras.getString(ItemsDbAdapter.KEY_TITLE);
			String desc = extras.getString(ItemsDbAdapter.KEY_DESCRIPTION);
			currentRowId = extras.getLong(ItemsDbAdapter.KEY_ROWID);
			if (title != null) {
				editableItemTitle.setText(title);								//if we're editing an existing item, show its Title
				confirmButton.setEnabled(true);
			}
			if (desc != null) {
				editableItemDesc.setText(desc);									//if we're editing an existing item, show its description
			}
		}
		
		/**
		 *  Begin listening to the confirmButton
		 */
		confirmButton.setOnClickListener(new View.OnClickListener() {
			/** When the button is clicked, run this method */
			public void onClick(View view) {
				Bundle bundle = new Bundle();

				bundle.putString(ItemsDbAdapter.KEY_TITLE, editableItemTitle.getText().toString());
				bundle.putString(ItemsDbAdapter.KEY_DESCRIPTION, editableItemDesc.getText().toString());
				if (currentRowId != null) {
					bundle.putLong(ItemsDbAdapter.KEY_ROWID, currentRowId);
				}

				Intent updateIntent = new Intent();
				updateIntent.putExtras(bundle);								//ship all the data stored in the bundle with the intent
				setResult(RESULT_OK, updateIntent);							//send the result back to the activity that started this activity
				finish();
			}
		});
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

	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// No need to implement this
	}

	/**
	 * Enables the confirm button when the user has written text in the Title-field
	 */
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		confirmButton.setEnabled(!TextUtils.isEmpty(editableItemTitle.getText().toString()));
	}
}
