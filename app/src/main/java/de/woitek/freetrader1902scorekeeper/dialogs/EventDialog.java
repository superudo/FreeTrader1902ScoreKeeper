package de.woitek.freetrader1902scorekeeper.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.RadioGroup;

import de.woitek.freetrader1902scorekeeper.ConstableActivity;
import de.woitek.freetrader1902scorekeeper.FightActivity;
import de.woitek.freetrader1902scorekeeper.R;
import de.woitek.freetrader1902scorekeeper.types.GameData;
import de.woitek.freetrader1902scorekeeper.types.GameEvent;
import de.woitek.freetrader1902scorekeeper.types.GameEventFight;
import de.woitek.freetrader1902scorekeeper.types.GameEventNone;
import de.woitek.freetrader1902scorekeeper.types.GameEventPolice;
import de.woitek.libraries.styledradiogroup.StyledRadioGroup;

public class EventDialog extends Dialog {
	private Activity activity;
	private GameData gameData;

	private StyledRadioGroup rgEnemy;

	public EventDialog(Activity a, GameData gameData) {
		super(a);
		this.gameData = gameData;
		this.activity = a;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_event);
		init();
	}

	private void init() {
		gameData.clearCurrentEvent();
		rgEnemy = (StyledRadioGroup) findViewById(R.id.rgEnemy);
		rgEnemy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				GameEvent event;
				Intent eventIntent = null;
				switch (rgEnemy.getSelectedIndex()) {
					case 0:
						event = new GameEventFight(gameData, GameEventFight.Enemy.HIGHWAYMAN, 1);
						eventIntent = new Intent(getContext(), GameEventFight.class);
						break;
					case 1:
						event = new GameEventFight(gameData, GameEventFight.Enemy.HIGHWAYMAN, 2);
						eventIntent = new Intent(getContext(), FightActivity.class);
						break;
					case 2:
						event = new GameEventFight(gameData, GameEventFight.Enemy.HIGHWAYMAN, 3);
						eventIntent = new Intent(getContext(), FightActivity.class);
						break;
					case 3:
						event = new GameEventFight(gameData, GameEventFight.Enemy.BEAR, 2);
						eventIntent = new Intent(getContext(), FightActivity.class);
						break;
					case 4:
						event = new GameEventPolice(gameData);
						eventIntent = new Intent(getContext(), ConstableActivity.class);
						break;
					default:
						event = new GameEventNone();
						break;
				}

				gameData.setCurrentEvent(event);
				if (eventIntent != null) {
					eventIntent.putExtra(GameData.CLASSNAME, gameData);
					activity.startActivity(eventIntent);
					activity.finish();
				}
				dismiss();
			}
		});
	}
}
