package de.woitek.freetrader1902scorekeeper;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;

import de.woitek.libraries.styledradiogroup.StyledRadioGroup;


public class EventActivity extends Activity {
	StyledRadioGroup rgAction;
	StyledRadioGroup rgTask;
	StyledRadioGroup yourStats;
	StyledRadioGroup theirStats;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);
		ActionBar b = getActionBar();
		if (b != null) {
			b.setDisplayHomeAsUpEnabled(true);
		}
		rgAction = (StyledRadioGroup) findViewById(R.id.rgAction);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event, menu);
		return true;
	}
}
