package de.woitek.freetrader1902scorekeeper.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TextView;

import de.woitek.freetrader1902scorekeeper.FightActivity;
import de.woitek.freetrader1902scorekeeper.R;
import de.woitek.freetrader1902scorekeeper.types.GameData;
import de.woitek.libraries.styledradiogroup.StyledRadioGroup;

public class LooseEquipmentDialog extends Dialog implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
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
				"Too bad. You lost the fight. The attacker destroyed one part of your pretty " +
						"truck, but you can choose which one... What do you think you will miss the least?"
		);

		((StyledRadioGroup) findViewById(R.id.rgEquipment1)).setOnCheckedChangeListener(this);
		((StyledRadioGroup) findViewById(R.id.rgEquipment2)).setOnCheckedChangeListener(this);

		findViewById(R.id.bnOk).setOnClickListener(this);
		findViewById(R.id.bnCancel).setOnClickListener(this);

		updateUI();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (group.getId()) {
			case R.id.rgEquipment1:
				if (checkedId > -1) {
					((StyledRadioGroup) findViewById(R.id.rgEquipment2)).setSelectedIndex(-1);
				}
				break;
			case R.id.rgEquipment2:
				if (checkedId > -1) {
					((StyledRadioGroup) findViewById(R.id.rgEquipment1)).setSelectedIndex(-1);
				}
				break;
		}
		updateUI();
	}

	private void updateUI() {
		String what = null;
		String more = null;
		String explain;
		switch (getCombinedSelectedIndex()) {
			case 0: // Cargo
				what = GameData.CARGO;
				if (gameData.getEquipment(GameData.CARGO) - 1 < gameData.getCurrentCargoAmount()) {
					more = "Unfortunately your cargo space will not carry all your goods anymore. So you have to drop some goods too.";
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
			explain = String.format("The attacker destroyed one part of your <b>%s</b>.", what.toLowerCase());
		} else {
			explain = "Please select, which truck part will be destroyed.";
		}
		((TextView) findViewById(R.id.tExplain)).setText(Html.fromHtml(explain));
		TextView moreView = (TextView) findViewById(R.id.tMore);
		if (more == null) {
			moreView.setVisibility(View.GONE);
		} else {
			moreView.setVisibility(View.VISIBLE);
			moreView.setText(more);
		}
	}

	private int getCombinedSelectedIndex() {
		int equ1 = ((StyledRadioGroup) findViewById(R.id.rgEquipment1)).getSelectedIndex();
		int equ2 = ((StyledRadioGroup) findViewById(R.id.rgEquipment2)).getSelectedIndex();
		int selectedIndex = -1;
		if (equ1 > -1) {
			selectedIndex = equ1;
		} else if (equ2 > -1) {
			selectedIndex = equ2 + 2;
		}
		return selectedIndex;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.bnOk:
				break;
			default:
				dismiss();
				break;
		}
	}
}
