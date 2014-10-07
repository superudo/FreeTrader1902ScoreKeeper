package de.woitek.freetrader1902scorekeeper.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import de.woitek.freetrader1902scorekeeper.ConstableActivity;
import de.woitek.freetrader1902scorekeeper.R;
import de.woitek.freetrader1902scorekeeper.types.GameData;
import de.woitek.freetrader1902scorekeeper.types.GameEventPolice;
import de.woitek.libraries.styledradiogroup.StyledRadioGroup;

public class SellEquipmentDialog extends Dialog implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {
    private Activity activity;
    private GameData gameData;
    private GameEventPolice event;

    public SellEquipmentDialog(Activity a, GameData gameData) {
        super(a);
        this.activity = a;
        this.gameData = gameData;
        this.event = (GameEventPolice) gameData.getCurrentEvent();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_sell_equipment);
        init();
    }

    private void initRadioGroup(int id, String equipment) {
        StyledRadioGroup rg = (StyledRadioGroup) findViewById(id);
        rg.setOnCheckedChangeListener(this);
        final int NUM = gameData.getEquipment(equipment);
        for (int i = 0; i < 6; i++) {
            rg.enableIndex(i, i <= NUM);
        }
    }

    private void init() {
        initRadioGroup(R.id.rgCargo, GameData.CARGO);
        initRadioGroup(R.id.rgEngine, GameData.ENGINE);
        initRadioGroup(R.id.rgShotguns, GameData.SHOTGUNS);
        initRadioGroup(R.id.rgArmor, GameData.ARMOR);

        for (int id : new int[]{R.id.bnOk, R.id.bnCancel}) {
            findViewById(id).setOnClickListener(this);
        }

        ((TextView) findViewById(R.id.lblYouGet)).setText("$ 0");
        ((TextView) findViewById(R.id.lblYouNeed)).setText(String.format("$ %d", event.whatToPay() - gameData.getMoney() + 1));
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        int youGet = 0;
        for (int id : new int[]{R.id.rgCargo, R.id.rgArmor, R.id.rgShotguns}) {
            youGet += ((StyledRadioGroup) findViewById(id)).getSelectedIndex() * 2;
        }
        youGet += ((StyledRadioGroup) findViewById(R.id.rgEngine)).getSelectedIndex();
        ((TextView) findViewById(R.id.lblYouGet)).setText(String.format("$ %d", youGet));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bnOk:
                sellEquipmentBySelection(R.id.rgCargo, GameData.CARGO);
                sellEquipmentBySelection(R.id.rgEngine, GameData.ENGINE);
                sellEquipmentBySelection(R.id.rgShotguns, GameData.SHOTGUNS);
                sellEquipmentBySelection(R.id.rgArmor, GameData.ARMOR);
                Toast.makeText(activity, "Equipment sold.", Toast.LENGTH_SHORT).show();
                ((ConstableActivity) activity).enablePayButtons();
                dismiss();
                break;
            case R.id.bnCancel:
                Toast.makeText(activity, "No change.", Toast.LENGTH_SHORT).show();
                dismiss();
                break;
        }
    }

    private void sellEquipmentBySelection(int radioGroupId, String equipmentName) {
        final int N = ((StyledRadioGroup) findViewById(radioGroupId)).getSelectedIndex();
        for (int i = 0; i < N; i++) {
            gameData.sellEquipment(equipmentName);
        }
    }
}
