package se.chalmers.dat255.listigt;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ListEditCreate extends Activity {
	EditText editableListTitle; //creates an editable textbox
	Long currentRowId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_editcreate);//use the layout defined in list_editcreate.xml
		setTitle(R.string.listEditCreateTitle);//set the title for this activity
		editableListTitle = (EditText) findViewById(R.id.listEditTitleField);//instantiate the Title-text field		
		Button confirmButton = (Button)	 findViewById(R.id.confirmButton);//instantiate the confirm button
		
		currentRowId = null;
		Bundle extras = getIntent().getExtras();
		//Check if the intent that started this activity brought any extra stuff
		if(extras!=null){
			 String title = extras.getString(ListsDbAdapter.KEY_TITLE);
			 currentRowId = extras.getLong(ListsDbAdapter.KEY_ROWID);
			 
			 if (title != null) {
			        editableListTitle.setText(title);//if we're editing an existing list, show its Title
			    }
		}
		
		/**Begin listening to the confirmButton */
		confirmButton.setOnClickListener(new View.OnClickListener() {
			/** When the button is clicked, run this method*/
		    public void onClick(View view) {
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
		});
	}
}
