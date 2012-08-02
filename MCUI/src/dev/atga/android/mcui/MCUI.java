package dev.atga.android.mcui;

import java.util.Arrays;
import java.util.LinkedList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MCUI extends Activity
{
	//instantiate data members.
	private String[] mItems={"lorem", "ipsum", "dolor",
			"consectetuer", "adipiscing", "elit", "morbi", "vel",
			"ligula", "vitae", "arcu", "aliquet", "mollis",
			"etiam", "vel", "erat", "placerat", "ante",
			"porttitor", "sodales", "pellentesque", "augue", "purus",
			"sit", "amet",};
	private ArrayAdapter<String> mAdapter;
	private PullToRefreshListView mPullRefreshListView;
	private LinkedList<String> mListItems;
	
	LinearLayout mHead;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	
    	Log.d("ATGA", "onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.ptrLst);

		//set a listener to be invoked when the list should be refreshed.
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				mPullRefreshListView.setLastUpdatedLabel(DateUtils.formatDateTime(getApplicationContext(),
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL));

				//do work to refresh the list here.
				new GetDataTask().execute();
			}
		});
		
		
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		
		//Setup clickListener to display Toasts when items in ListView are clicked.
        actualListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
            	String item;
            	if (position == 1) {
            		item = ((TextView)((LinearLayout)view).getChildAt(1)).getText().toString();
            		Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
            	}
            	else {
            		item = ((TextView)((LinearLayout)view).getChildAt(3)).getText().toString();
                    Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
            	}
            }
        });

		//inflate header from xml and add to top of ListView.
        mHead = (LinearLayout) getLayoutInflater().inflate(R.layout.header, null); 
        actualListView.addHeaderView(mHead);
		
		
		//create list items and arrayAdapter.
		mListItems = new LinkedList<String>();
		mListItems.addAll(Arrays.asList(mItems));
		mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.label, mListItems);

		//set the ListView to use the arrayAdapter.
		actualListView.setAdapter(mAdapter);
    }
    
    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
			return mItems;
		}

		@Override
		protected void onPostExecute(String[] result) {
			mListItems.addFirst("Added after refresh...");
			mAdapter.notifyDataSetChanged();

			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshListView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}
}
