package se.chalmers.dat255.listigt.test;

import se.chalmers.dat255.listigt.ItemsDbAdapter;
import se.chalmers.dat255.listigt.ListsDbAdapter;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public final class DatabaseTest extends AndroidTestCase {

	private static final String TEST_FILE_PREFIX = "test_";
	private ItemsDbAdapter itemsAdapter;
	private ListsDbAdapter listAdapter;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		RenamingDelegatingContext context 
		= new RenamingDelegatingContext(getContext(), TEST_FILE_PREFIX);

		itemsAdapter = new ItemsDbAdapter(context);
		itemsAdapter.open();
		listAdapter = new ListsDbAdapter(context);
		listAdapter.open();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		itemsAdapter.close();
		itemsAdapter = null;
		listAdapter.close();
		listAdapter = null;
	}

	public void testPreConditions() {
		assertNotNull(itemsAdapter);
		assertNotNull(listAdapter);
	}

}