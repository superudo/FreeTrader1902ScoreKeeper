package de.woitek.freetrader1902scorekeeper.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TextView;

import de.woitek.freetrader1902scorekeeper.R;
import de.woitek.freetrader1902scorekeeper.types.GameData;
import de.woitek.freetrader1902scorekeeper.views.StyledRadioGroupView;

public class CargoDialog extends Dialog implements View.OnClickListener {
	private GameData data;
	private String toSell;

	private StyledRadioGroupView rgAction;
	private StyledRadioGroupView rgAmount;
	private StyledRadioGroupView rgPrice;

	public CargoDialog(Activity a, GameData data, String sell) {
		super(a);
		this.data = data;
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

		rgAction = (StyledRadioGroupView) findViewById(R.id.rbAction);
		rgAmount = (StyledRadioGroupView) findViewById(R.id.rbAmount);
		rgPrice = (StyledRadioGroupView) findViewById(R.id.rbPrice);

		rgAction.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				StyledRadioGroupView radioGroup = (StyledRadioGroupView) group;
				String selected = radioGroup.getSelectedItemValue();
				if (selected.equals("Buy")) {
					rgPrice.enableAll(true);
				} else if (selected.equals("Sell")) {
					rgPrice.enableAll(true);
				} else {
					rgPrice.enableAll(false);
				}
			}
		});
		findViewById(R.id.bnOk).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		findViewById(R.id.bnCancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
/*
			case R.id.bnBuy:
				if (data.mayBuyCargo(pckAmount.getValue(), pckPrice.getValue(), toSell)) {
					data.buyCargo(pckAmount.getValue(), pckPrice.getValue(), toSell);
					Toast.makeText(activity, "Bought.", Toast.LENGTH_SHORT).show();
					dismiss();
				} else {
					Toast.makeText(activity, "You cannot buy this.", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.bnSell:
				if (data.maySellCargo(pckAmount.getValue(), toSell)) {
					data.sellCargo(pckAmount.getValue(), pckPrice.getValue(), toSell);
					Toast.makeText(activity, "Sold.", Toast.LENGTH_SHORT).show();
					dismiss();
				} else {
					Toast.makeText(activity, "You cannot sell this much.", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.bnDrop:
				if (data.maySellCargo(pckAmount.getValue(), toSell)) {
					data.dropCargo(pckAmount.getValue(), toSell);
					Toast.makeText(activity, "Dropped.", Toast.LENGTH_SHORT).show();
					dismiss();
				} else {
					Toast.makeText(activity, "You cannot drop this much.", Toast.LENGTH_SHORT).show();
				}
				break;
*/
			default:
				break;
		}
	}
}
