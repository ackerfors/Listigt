package se.chalmers.dat255.listigt.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;
import se.chalmers.dat255.listigt.MainListActivity;

public class TestMainListActivity extends
		ActivityInstrumentationTestCase2<MainListActivity> {

	private MainListActivity mActivity;  // the activity under test
    private TextView mView;          // the activity's TextView (the only view)
    private String resourceString;
	
	public TestMainListActivity() {
		super("se.chalmers.dat255.listigt Testing", MainListActivity.class);
	}
	
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = this.getActivity();
        mView = (TextView) mActivity.findViewById(se.chalmers.dat255.listigt.R.id.listRowTitle);
        //resourceString = mActivity.getString(se.chalmers.dat255.listigt.R.string.title);
    }
    public void testPreconditions() {
      assertNotNull(mView);
    }
    /*public void testText() {
      assertEquals(resourceString,(String)mView.getText());
    }*/
}