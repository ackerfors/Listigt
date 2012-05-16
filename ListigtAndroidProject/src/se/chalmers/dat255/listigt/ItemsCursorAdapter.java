package se.chalmers.dat255.listigt;

import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * @author paac
 *
 */
public class ItemsCursorAdapter extends SimpleCursorAdapter {
	private Cursor cursor;
	private Context context;
	private ArrayList<String> list = new ArrayList<String>();
	private ArrayList<Boolean> itemChecked = new ArrayList<Boolean>();
	private ItemsDbAdapter itemDbAdapter;

	// itemChecked will store the position of the checked items.

	public ItemsCursorAdapter(Context context, int layout, Cursor c, String[] from,
			int[] to) {
		super(context, layout, c, from, to);
		cursor = c;
		this.context = context;
		itemDbAdapter = new ItemsDbAdapter(context);
		
		for (int i = 0; i < this.getCount(); i++) {
			itemChecked.add(i, false); // Initialises all items value with false
		}
	}
	
	public View getView(final int pos, View inView, ViewGroup parent) {
		if (inView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inView = inflater.inflate(R.layout.items_row, null);
		}
		
		Log.i("position", "pos = " + pos);
	    cursor.moveToPosition(pos);
	    CheckBox cBox = (CheckBox) inView.findViewById(R.id.itemCheckBox);
	    cBox.setTag(cursor.getInt(cursor.getColumnIndex(ItemsDbAdapter.KEY_ROWID)));

	    int idTag = (Integer) cBox.getTag();
	    Log.i("Checkbox ID","Checkbox ID tag = " + idTag);
	    //int checked = mDbHelper.selectBooked(idTag);
	    
//	    Log.i("results from selectChk.....................", ""+checked);
//	    if (checked == 1) {
//	        cBox.setChecked(true);          
//	    } else {
//	        cBox.setChecked(false);
//	    }
	    
	    TextView itemTitle = (TextView) inView.findViewById(R.id.itemRowTitle);
	    itemTitle.setText(cursor.getString(cursor.getColumnIndex(ItemsDbAdapter.KEY_TITLE)));
	    
		return inView;
	}
}
