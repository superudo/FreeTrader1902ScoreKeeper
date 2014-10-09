package de.woitek.freetrader1902scorekeeper.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import de.woitek.freetrader1902scorekeeper.FightActivity;
import de.woitek.freetrader1902scorekeeper.R;
import de.woitek.freetrader1902scorekeeper.types.GameData;

public class GetawayDialog extends Dialog {
	private FightActivity activity;
	private GameData gameData;

	public GetawayDialog(FightActivity a, GameData data) {
		super(a);
		this.gameData = data;
		this.activity = a;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_getaway);
		init();
	}

	private void init() {
		String s = "You try to get away from a fight. So you have to draw another event card. " +
				"The Crank field tells the outcome of your try. If it says \"Start\" " +
				"you go to you former target city. If it says \"Misfire\" then the worst " +
				"has happened. You've lost your maps! You need to reshuffle all city cards and " +
				"draw a first city for a new map and a new event card for the arrival at this city.";
		((TextView) findViewById(R.id.tRules)).setText(s);

		findViewById(R.id.bnCrankStart).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.gotoMainActivity();
			}
		});

		findViewById(R.id.bnCrankFailed).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				gameData.setMoney(gameData.getMoney() - 1);
				activity.gotoMainActivity();
			}
		});

		findViewById(R.id.bnCancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}
}
