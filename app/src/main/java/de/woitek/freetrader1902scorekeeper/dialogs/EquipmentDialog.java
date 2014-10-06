package de.woitek.freetrader1902scorekeeper.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import de.woitek.freetrader1902scorekeeper.R;
import de.woitek.freetrader1902scorekeeper.types.GameData;
import de.woitek.libraries.styledradiogroup.StyledRadioGroup;

public class EquipmentDialog extends Dialog implements View.OnClickListener {
	private Activity activity;

	private GameData data;
	private String toSell;
	private StyledRadioGroup rgAmount;

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

		((TextView) findViewById(R.id.tEquipmentTitle)).setText(toSell);

	}

	@Override
	public void onClick(View v) {
	}
}
