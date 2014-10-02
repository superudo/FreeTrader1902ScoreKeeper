package de.woitek.freetrader1902scorekeeper.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import de.woitek.freetrader1902scorekeeper.R;
import de.woitek.freetrader1902scorekeeper.types.GameData;
import de.woitek.libraries.styledradiogroup.StyledRadioGroup;

public class CargoDialog extends Dialog {
	private Activity activity;
	private GameData data;
	private String toSell;

	private StyledRadioGroup rgAction;
	private StyledRadioGroup rgAmount;
	private StyledRadioGroup rgPrice;

	public CargoDialog(Activity a, GameData data, String sell) {
		super(a);
		this.data = data;
		this.activity = a;
		toSell = sell;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_cargo);
		init();
	}

	private void init() {
		((TextView) findViewById(R.id.tCargoTitle)).setText(toSell);

		rgAction = (StyledRadioGroup) findViewById(R.id.rbAction);
		rgAmount = (StyledRadioGroup) findViewById(R.id.rbAmount);
		rgPrice = (StyledRadioGroup) findViewById(R.id.rbPrice);

		rgAction.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				updateControlStates();
			}
		});
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

		updateControlStates();
	}

	private void updateControlStates() {
		String selected = rgAction.getSelectedItemValue();
		int numEnabled = 5;
		if (selected == null) {
			rgPrice.enableAll(false);
			rgAmount.enableAll(false);
		} else if (selected.equals("Buy")) {
			numEnabled = data.getFreeCargoAmount();
			rgPrice.enableAll(true);
		} else if (selected.equals("Sell")) {
			numEnabled = data.getCargo(toSell);
			rgPrice.enableAll(numEnabled > 0);
		} else {
			numEnabled = data.getCargo(toSell);
			rgPrice.enableAll(false);
		}

		int previousSelectedIndex = rgAmount.getSelectedIndex();
		final int MAXID = rgAmount.getMaxIndex();
		for (int i = 0; i < MAXID; ++i) {
			rgAmount.enableIndex(i, (i < numEnabled));
		}
		if ((previousSelectedIndex == -1) && (numEnabled > 0)) {
			rgAmount.setSelectedIndex(0);
		}

	}

	private boolean updateGameData() {
		int amount = rgAmount.getSelectedIndex() + 1;
		int price = rgPrice.getSelectedIndex() + 1;
		boolean ok = false;
		switch (rgAction.getSelectedIndex()) {
			case 0: // Buy
				ok = data.mayBuyCargo(amount, price, toSell);
				if (ok) {
					data.buyCargo(amount, price, toSell);
					Toast.makeText(activity, "Bought.", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(activity, "You cannot buy this.", Toast.LENGTH_SHORT).show();
				}
				break;
			case 1: // Sell
				ok = data.maySellCargo(amount, toSell);
				if (ok) {
					data.sellCargo(amount, price, toSell);
					Toast.makeText(activity, "Sold.", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(activity, "You cannot sell this much.", Toast.LENGTH_SHORT).show();
				}
				break;
			case 2: // Drop
				ok = data.maySellCargo(amount, toSell);
				if (ok) {
					data.dropCargo(amount, toSell);
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
