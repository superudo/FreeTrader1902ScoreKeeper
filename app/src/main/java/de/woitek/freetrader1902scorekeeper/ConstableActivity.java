package de.woitek.freetrader1902scorekeeper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import de.woitek.freetrader1902scorekeeper.dialogs.SellEquipmentDialog;
import de.woitek.freetrader1902scorekeeper.types.GameData;
import de.woitek.freetrader1902scorekeeper.types.GameEventPolice;
import de.woitek.libraries.styledradiogroup.StyledRadioGroup;


public class ConstableActivity extends Activity implements View.OnClickListener {
    private GameData gameData;
	private GameEventPolice event;

    private StyledRadioGroup rgFine;
    private StyledRadioGroup rgLawLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constable);

        gameData = getIntent().getParcelableExtra(GameData.CLASSNAME);
        if (gameData == null) {
            gameData = new GameData();
        }

	    event = (GameEventPolice) gameData.getCurrentEvent();

        rgFine = (StyledRadioGroup) findViewById(R.id.rgConstableFine);
        rgFine.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
	            event.setFine(checkedId + 1);
	            updateUI();
            }
        });

        rgLawLevel = (StyledRadioGroup) findViewById(R.id.rgLawLevel);
        rgLawLevel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
	            event.setLawLevel(checkedId);
	            updateUI();
            }
        });

        rgFine.setSelectedIndex(0);
	    event.setFine(1);

        rgLawLevel.setSelectedIndex(0);
	    event.setLawLevel(0);

	    findViewById(R.id.bnProceed).setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			    gotoMainActivity();
		    }
	    });

	    findViewById(R.id.bnPay).setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			    v.setEnabled(false);
			    gameData.setMoney(gameData.getMoney() - event.whatToPay());
			    gameData.dropAllCargo(GameData.MOONSHINE);
			    gotoMainActivity();
		    }
	    });

	    final Activity activity = this;
	    findViewById(R.id.bnBroke).setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
			    new AlertDialog.Builder(activity).setTitle("Really broke?")
					    .setMessage("Do you really can't sell any truck parts? Remember: If you don't pay you're arrested and the game is over!")
					    .setNegativeButton("Yes. I'm done.", new DialogInterface.OnClickListener() {
						    @Override
						    public void onClick(DialogInterface dialog, int which) {
							    looseByArrest();
						    }
					    })
					    .setPositiveButton("Stop. I can pay.", new DialogInterface.OnClickListener() {
						    @Override
						    public void onClick(DialogInterface dialog, int which) {
							    dialog.cancel();
						    }
					    }).show();
		    }
	    });

        findViewById(R.id.bnSell).setOnClickListener(this);

        updateUI();
    }

	private void looseByArrest() {
		gameData.setArrested();
		Intent intent = new Intent(this, GameEndActivity.class);
		intent.putExtra(GameData.CLASSNAME, gameData);
		startActivity(intent);
		finish();
	}

	private void gotoMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra(GameData.CLASSNAME, gameData);
		startActivity(intent);
		finish();
	}

	private void updateUI() {
        String outcome, title;
        if (event.caughtInTheAct()) {
            outcome = getResources().getString(R.string.police_outcome_rule);
	        ((TextView) findViewById(R.id.lblYouHaveToPay)).setText(String.format("You have to pay $ %d", event.whatToPay()));
	        title = "Caught with illegal goods.";
	        findViewById(R.id.bnProceed).setVisibility(View.GONE);
	        enablePayButtons();
        } else {
            title = "Ok. You are clean.";
            outcome = "You may go now. But I'll keep an eye on you, pal...";
	        for (int i : new int[]{R.id.lblConstableFine, R.id.lblLawLevel,
			        R.id.lblPoliceRule, R.id.rgLawLevel, R.id.rgConstableFine,
			        R.id.lblYouHaveToPay, R.id.llPayButtons, R.id.bnBroke}) {
		        findViewById(i).setVisibility(View.GONE);
            }
        }
        ((TextView) findViewById(R.id.lblPoliceOutcome)).setText(outcome);
        ((TextView) findViewById(R.id.lblPoliceTitle)).setText(title);
    }

    public void enablePayButtons() {
	    boolean enoughMoney = gameData.getMoney() - 1 >= event.whatToPay();
		findViewById(R.id.bnPay).setEnabled(enoughMoney);
		findViewById(R.id.bnSell).setEnabled(!enoughMoney);
	}


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                Intent mainIntent = new Intent(this, MainActivity.class);
                mainIntent.putExtra(GameData.CLASSNAME, gameData);
                startActivity(mainIntent);
                finish();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        new SellEquipmentDialog(this, gameData).show();
    }
}
