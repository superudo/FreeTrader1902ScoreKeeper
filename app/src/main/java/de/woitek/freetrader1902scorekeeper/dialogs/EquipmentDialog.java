package de.woitek.freetrader1902scorekeeper.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import de.woitek.freetrader1902scorekeeper.R;
import de.woitek.freetrader1902scorekeeper.types.GameData;
import de.woitek.libraries.styledradiogroup.StyledRadioGroup;

public class EquipmentDialog extends Dialog {
	private Activity activity;

	private GameData gameData;
	private String toSell;

	private StyledRadioGroup rgAction;

	public EquipmentDialog(Activity a, GameData data, String sell) {
		super(a);
		this.activity = a;
		this.gameData = data;
		toSell = sell;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_equipment);
		init();
	}

	private void init() {
		((TextView) findViewById(R.id.tEquipmentTitle)).setText(toSell);

		rgAction = (StyledRadioGroup) findViewById(R.id.rbAction);
		findViewById(R.id.bnOk).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (updateGameData()) {
					dismiss();
				}
			}
		});
		findViewById(R.id.bnCancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	private boolean updateGameData() {
		boolean ok = false;
		switch (rgAction.getSelectedIndex()) {
			case 0: // Buy
				ok = gameData.mayBuyEquipment(toSell);
				if (ok) {
					gameData.buyEquipment(toSell);
					Toast.makeText(activity, "Bought.", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(activity, "You cannot buy this.", Toast.LENGTH_SHORT).show();
				}
				break;
			case 1: // Sell
				ok = gameData.maySellEquipment(toSell);
				if (ok) {
					gameData.sellEquipment(toSell);
					Toast.makeText(activity, "Sold.", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(activity, "You cannot sell this much.", Toast.LENGTH_SHORT).show();
				}
				break;
			case 2: // Drop
				ok = gameData.maySellEquipment(toSell);
				if (ok) {
					gameData.dropEquipment(toSell);
					Toast.makeText(activity, "Dropped.", Toast.LENGTH_SHORT).show();
					dismiss();
				} else {
					Toast.makeText(activity, "You cannot drop this much.", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
		}

		return ok;
	}
}
