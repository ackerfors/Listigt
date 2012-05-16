package se.chalmers.dat255.listigt;

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
 * This class adds functionality to the class SimpleCursorAdapter
 * It will get information about the current state of the checkboxes
 * provided by the cursor parameter, and adds an OnClickListener
 * on each checkbox that will trigger an update of the clicked
 * checkbox state in the local SQLite database.
 * 
 * @author Ackerfors Crew
 *
 */
public class ItemsCursorAdapter extends SimpleCursorAdapter {
	private Cursor cursor;
	private Context context;
	private ItemsDbAdapter itemDbAdapter;

	public ItemsCursorAdapter(Context context, int layout, Cursor c, String[] from,
			int[] to) {
		super(context, layout, c, from, to);
		cursor = c;
		this.context = context;
		itemDbAdapter = new ItemsDbAdapter(context);
	}

	public View getView(final int pos, View inView, ViewGroup parent) {
		if (inView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			inView = inflater.inflate(R.layout.items_row, null);
		}
		cursor.moveToPosition(pos);
		CheckBox checkBox = (CheckBox) inView.findViewById(R.id.itemCheckBox);
		checkBox.setTag(cursor.getInt(cursor.getColumnIndex(ItemsDbAdapter.KEY_ROWID)));

		int checked = cursor.getInt(cursor.getColumnIndex(ItemsDbAdapter.KEY_BOOKED));

		//Log.i("selectBooked", "Checked value = " + checked);
		if (checked == 1) {
			checkBox.setChecked(true);          
		} else {
			checkBox.setChecked(false);
		}

		checkBox.setOnClickListener(new OnClickListener() {  
			public void onClick(View v) {
				itemDbAdapter.open();
				CheckBox cBox = (CheckBox) v.findViewById(R.id.itemCheckBox);
				if (cBox.isChecked()) {
					// Update the database for each checked item
					int id = (Integer) cBox.getTag();
					itemDbAdapter.updateBooking( id, 1);
					cursor.requery();
					Log.i("checked _id", "id= " + id + " " + cursor.getString(cursor.getColumnIndex(ItemsDbAdapter.KEY_ROWID))); 
					Log.i("checked checked", "" + cursor.getString(cursor.getColumnIndex(ItemsDbAdapter.KEY_BOOKED)));

				} else if (!cBox.isChecked()) {
					int id = (Integer) cBox.getTag();
					itemDbAdapter.updateBooking( id, 0);
					cursor.requery();
				}
				itemDbAdapter.close();
			}
		});

		//Set the title on each row.
		TextView itemTitle = (TextView) inView.findViewById(R.id.itemRowTitle);
		itemTitle.setText(cursor.getString(cursor.getColumnIndex(ItemsDbAdapter.KEY_TITLE)));

		return inView;
	}
}
