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

		//Log.i("position", "pos = " + pos);
		cursor.moveToPosition(pos);
		CheckBox cBox = (CheckBox) inView.findViewById(R.id.itemCheckBox);
		cBox.setTag(cursor.getInt(cursor.getColumnIndex(ItemsDbAdapter.KEY_ROWID)));

		int idTag = (Integer) cBox.getTag();
		//Log.i("Checkbox ID","Checkbox ID tag = " + idTag);

		int checked = cursor.getInt(cursor.getColumnIndex(ItemsDbAdapter.KEY_BOOKED));

		//Log.i("selectBooked", "Checked value = " + checked);
		if (checked == 1) {
			cBox.setChecked(true);          
		} else {
			cBox.setChecked(false);
		}

		cBox.setOnClickListener(new OnClickListener() {  
			public void onClick(View v) {
				
				itemDbAdapter = new ItemsDbAdapter(context);
				itemDbAdapter.open();
				CheckBox cBox = (CheckBox) v.findViewById(R.id.itemCheckBox);
				if (cBox.isChecked()) {
					//cBox.setChecked(false);
					
					// Update the database for each checked item
					//Long rowID = cursor.getLong(cursor.getColumnIndex(ItemsDbAdapter.KEY_ROWID));
					int intId = (Integer) cBox.getTag();
					long longId = (long) intId;
					Long booked = (long) 1;
					itemDbAdapter.updateBooking( intId, booked);
					cursor.requery();

					// Verify that the db was updated for debugging purposes
					//String event = c.getString(c.getColumnIndex("event"));                  
					

					Log.i("checked _id", "id= " + longId + " " + cursor.getString(cursor.getColumnIndex(ItemsDbAdapter.KEY_ROWID))); 
					Log.i("checked checked", "" + cursor.getString(cursor.getColumnIndex(ItemsDbAdapter.KEY_BOOKED)));

				} else if (!cBox.isChecked()) {
					//cBox.setChecked(true);
					
					// checkList.remove(cBox.getTag());
					//checkList.add((Integer) cBox.getTag());
					//String event = cursor.getString(cursor.getColumnIndex("event"));
					//Long rowID = cursor.getLong(cursor.getColumnIndex(ItemsDbAdapter.KEY_ROWID));
					int intId = (Integer) cBox.getTag();
					long longId = (long) intId;
					Long unBooked = (long) 0;
					itemDbAdapter.updateBooking( intId, unBooked);
					cursor.requery();
					//int sqlresult = mDbHelper.selectChk(id, event);                   
					//Log.i("sqlresult checked value after update...........", ""+ sqlresult);                  
					//Log.i("unchecked _id...........", ""+c.getString(c.getColumnIndex("_id"))); 
					//Log.i("unchecked checked...........", ""+c.getString(c.getColumnIndex("checked")));
				}
				itemDbAdapter.close();
			}
		});

		TextView itemTitle = (TextView) inView.findViewById(R.id.itemRowTitle);
		itemTitle.setText(cursor.getString(cursor.getColumnIndex(ItemsDbAdapter.KEY_TITLE)));

		return inView;
	}
}
