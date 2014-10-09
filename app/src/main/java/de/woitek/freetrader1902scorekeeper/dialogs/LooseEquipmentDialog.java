package de.woitek.freetrader1902scorekeeper.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TextView;

import de.woitek.freetrader1902scorekeeper.FightActivity;
import de.woitek.freetrader1902scorekeeper.R;
import de.woitek.freetrader1902scorekeeper.types.GameData;
import de.woitek.libraries.styledradiogroup.StyledRadioGroup;

public class LooseEquipmentDialog extends Dialog implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
	public final String CLASSNAME = "LooseEquipmentDialog";

	private FightActivity activity;
	private GameData gameData;

	public LooseEquipmentDialog(FightActivity a, GameData data) {
		super(a);
		this.gameData = data;
		this.activity = a;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_loose_equipment);
		init();
	}

	private void init() {
		((TextView) findViewById(R.id.tRules)).setText(
				"The attacker destroyed one part of your pretty " +
						"truck, but you can choose which one...");

		((StyledRadioGroup) findViewById(R.id.rgEquipment)).setOnCheckedChangeListener(this);

		findViewById(R.id.bnOk).setOnClickListener(this);
		findViewById(R.id.bnCancel).setOnClickListener(this);

		updateUI();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		updateUI();
	}

	private void updateUI() {
		String what = null;
		String more = "\n";
		String explain;
		switch (((StyledRadioGroup) findViewById(R.id.rgEquipment)).getSelectedIndex()) {
			case 0: // Cargo
				what = GameData.CARGO;
				if (gameData.getEquipment(GameData.CARGO) - 1 < gameData.getCurrentCargoAmount()) {
					more = " You have to drop some goods too.";
				}
				break;
			case 1:
				what = GameData.ENGINE;
				break;
			case 2:
				what = GameData.SHOTGUNS;
				break;
			case 3:
				what = GameData.ARMOR;
				break;
			default:
				break;
		}
		if (what != null) {
			if (gameData.getEquipment(what) == 1) {
				explain = String.format("The last of your <b>%s%s</b> will be destroyed.%s",
						what.toLowerCase(), what.equals(GameData.SHOTGUNS) ? "" : "s", more);
			} else {
				explain = String.format("One of your <b>%d %s%s</b> will be destroyed.%s",
						gameData.getEquipment(what), what.toLowerCase(),
						what.equals(GameData.SHOTGUNS) ? "" : "s", more
				);
			}
		} else {
			explain = "Please select, which truck part will be destroyed.";
		}
		((TextView) findViewById(R.id.tExplain)).setText(Html.fromHtml(explain));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.bnOk:
				int part = ((StyledRadioGroup) findViewById(R.id.rgEquipment)).getSelectedIndex();
				if (part > -1) {
					dropPartAndGotoMainActivity(part);
				}
				dismiss();
				break;
			default:
				dismiss();
				break;
		}
	}

	private void dropPartAndGotoMainActivity(int part) {
		Log.d(CLASSNAME, String.format(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> dropPart Start: Produce = %d, Cargo = %d", gameData.getCargo(GameData.PRODUCE), gameData.getEquipment(GameData.CARGO)));
		String[] parts = new String[]{GameData.CARGO, GameData.ENGINE, GameData.SHOTGUNS, GameData.ARMOR};
		if ((part >= 0) && (part < parts.length)) {
			gameData.dropEquipment(parts[part]);
		}
		activity.gotoMainActivity();
		Log.d(CLASSNAME, String.format(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> dropPart Finish: Produce = %d, Cargo = %d", gameData.getCargo(GameData.PRODUCE), gameData.getEquipment(GameData.CARGO)));
	}
}
