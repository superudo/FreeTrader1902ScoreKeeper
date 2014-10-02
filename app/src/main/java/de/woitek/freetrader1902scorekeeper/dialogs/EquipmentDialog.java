package de.woitek.freetrader1902scorekeeper.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import de.woitek.freetrader1902scorekeeper.R;
import de.woitek.freetrader1902scorekeeper.types.GameData;

public class EquipmentDialog extends Dialog implements View.OnClickListener {
	private Activity activity;
	private Button buy;
	private Button sell;
	private Button drop;

	private NumberPicker pckAmount;

	private GameData data;
	private String toSell;

	public EquipmentDialog(Activity a, GameData data, String sell) {
		super(a);
		this.activity = a;
		this.data = data;
		toSell = sell;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_equipment);
		buy = (Button) findViewById(R.id.bnBuy);
		sell = (Button) findViewById(R.id.bnSell);
		drop = (Button) findViewById(R.id.bnDrop);

		pckAmount = (NumberPicker) findViewById(R.id.pckAmount);

		buy.setOnClickListener(this);
		sell.setOnClickListener(this);
		drop.setOnClickListener(this);

		((TextView) findViewById(R.id.tEquipmentTitle)).setText(toSell);

		pckAmount.setMinValue(1);
		pckAmount.setMaxValue(5);
		pckAmount.setValue(1);
		pckAmount.setWrapSelectorWheel(false);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.bnBuy:
				if (data.mayBuyEquipment(pckAmount.getValue(), toSell)) {
					data.buyEquipment(pckAmount.getValue(), toSell);
					Toast.makeText(activity, "Bought.", Toast.LENGTH_SHORT).show();
					dismiss();
				} else {
					Toast.makeText(activity, "You cannot buy this.", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.bnSell:
				if (data.maySellEquipment(pckAmount.getValue(), toSell)) {
					data.sellEquipment(pckAmount.getValue(), toSell);
					Toast.makeText(activity, "Sold.", Toast.LENGTH_SHORT).show();
					dismiss();
				} else {
					Toast.makeText(activity, "You cannot sell this much.", Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.bnDrop:
				if (data.mayDropEquipment(pckAmount.getValue(), toSell)) {
					data.dropEquipment(pckAmount.getValue(), toSell);
					Toast.makeText(activity, "Dropped.", Toast.LENGTH_SHORT).show();
					dismiss();
				} else {
					Toast.makeText(activity, "You cannot drop this much.", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
		}
	}
}
