package de.woitek.freetrader1902scorekeeper;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.TextView;

import de.woitek.freetrader1902scorekeeper.types.GameData;
import de.woitek.freetrader1902scorekeeper.types.GameEvent;
import de.woitek.freetrader1902scorekeeper.types.GamePolice;
import de.woitek.libraries.styledradiogroup.StyledRadioGroup;


public class ConstableActivity extends Activity {
	GameData gameData;

	StyledRadioGroup rgFine;
	StyledRadioGroup rgLawLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_constable);

		gameData = getIntent().getParcelableExtra(GameData.CLASSNAME);
		if (gameData == null) {
			gameData = new GameData();
		}

		rgFine = (StyledRadioGroup) findViewById(R.id.rgConstableFine);
		rgFine.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				GameEvent event = gameData.getCurrentEvent();
				if (event.getEventType() == GameEvent.EventType.CARGOCHECK) {
					((GamePolice) event).setFine(checkedId + 1);
					updateUI();
				}
			}
		});

		rgLawLevel = (StyledRadioGroup) findViewById(R.id.rgLawLevel);
		rgLawLevel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				GameEvent event = gameData.getCurrentEvent();
				if (event.getEventType() == GameEvent.EventType.CARGOCHECK) {
					((GamePolice) event).setLawLevel(checkedId);
					updateUI();
				}
			}
		});

		rgFine.setSelectedIndex(0);
		rgLawLevel.setSelectedIndex(0);

		updateUI();
	}

	private void updateUI() {
		GamePolice event = (GamePolice) gameData.getCurrentEvent();

		if (event == null) {
			return;
		}

		if (event.caughtInTheAct()) {
			((TextView) findViewById(R.id.lblPoliceOutcome)).setText(getResources().getString(R.string.police_outcome_rule, event.whatToPay()));
		} else {
			((TextView) findViewById(R.id.lblPoliceOutcome)).setText("You may go now. But I'll keep an eye on you, pal...");
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.constable, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
