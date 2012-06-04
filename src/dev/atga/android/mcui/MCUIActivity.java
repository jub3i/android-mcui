package dev.atga.android.mcui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MCUIActivity extends Activity {
	
	private static final String[] items={"lorem", "ipsum", "dolor",
		"sit", "amet",
		"consectetuer", "adipiscing", "elit", "morbi", "vel",
		"ligula", "vitae", "arcu", "aliquet", "mollis",
		"etiam", "vel", "erat", "placerat", "ante",
		"porttitor", "sodales", "pellentesque", "augue", "purus"};
	private ArrayAdapter<String> mAdapter;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        ListView lv = (ListView) findViewById(R.id.vLst);
        mAdapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.label, items);
		lv.setAdapter(mAdapter);
    }
}