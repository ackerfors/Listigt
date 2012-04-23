package se.chalmers.dat255.listigt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ItemEditCreate extends Activity {
	EditText editableItemTitle, editableItemDesc; //creates two editable textboxes
	Long currentRowId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_editcreate);//use the layout defined in item_editcreate.xml
		setTitle(R.string.itemEditCreateTitle);//set the title for this activity
		editableItemTitle = (EditText) findViewById(R.id.itemEditTitleField);//instantiate the Title-text field	
		editableItemDesc = (EditText) findViewById(R.id.itemEditDescField);
		Button confirmButton = (Button)	 findViewById(R.id.confirmButton);//instantiate the confirm button
		
		currentRowId = null;
		Bundle extras = getIntent().getExtras();
		//Check if the intent that started this activity brought any extra stuff
		if(extras!=null){
			 String title = extras.getString(ItemsDbAdapter.KEY_TITLE);
			 String desc = (String) extras.get(ItemsDbAdapter.KEY_DESCRIPTION);
			 currentRowId = extras.getLong(ItemsDbAdapter.KEY_ROWID);
			 
			 if (title != null) {
			        editableItemTitle.setText(title);//if we're editing an existing item, show its Title
			    }
			 
			 if (desc != null) {
			        editableItemDesc.setText(desc);//if we're editing an existing item, show its description
			    }
		}
		
		/**Begin listening to the confirmButton */
		confirmButton.setOnClickListener(new View.OnClickListener() {
			/** When the button is clicked, run this method*/
		    public void onClick(View view) {
		    	Bundle bundle = new Bundle();

		    	bundle.putString(ItemsDbAdapter.KEY_TITLE, editableItemTitle.getText().toString());
		    	bundle.putString(ItemsDbAdapter.KEY_DESCRIPTION, editableItemDesc.getText().toString());
		    	if (currentRowId != null) {
		    	    bundle.putLong(ItemsDbAdapter.KEY_ROWID, currentRowId);
		    	}
		    	
		    	Intent updateIntent = new Intent();
		    	updateIntent.putExtras(bundle);//ship all the data stored in the bundle with the intent
		    	setResult(RESULT_OK, updateIntent);//send the result back to the activity that started this activity
		    	finish();
		    }
		});
	}
}
