package de.woitek.freetrader1902scorekeeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import de.woitek.freetrader1902scorekeeper.types.GameData;
import de.woitek.freetrader1902scorekeeper.types.GameEvent;
import de.woitek.freetrader1902scorekeeper.types.GameEventPolice;
import de.woitek.libraries.styledradiogroup.StyledRadioGroup;


public class ConstableActivity extends Activity {
    private GameData gameData;

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

        rgFine = (StyledRadioGroup) findViewById(R.id.rgConstableFine);
        rgFine.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                GameEvent event = gameData.getCurrentEvent();
                if (event.getEventType() == GameEvent.EventType.CARGOCHECK) {
                    ((GameEventPolice) event).setFine(checkedId + 1);
                    updateUI();
                }
            }
        });

        rgLawLevel = (StyledRadioGroup) findViewById(R.id.rgLawLevel);
        rgLawLevel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                GameEvent event = gameData.getCurrentEvent();
                if (event.getEventType() == GameEvent.EventType.CARGOCHECK) {
                    ((GameEventPolice) event).setLawLevel(checkedId);
                    updateUI();
                }
            }
        });

        rgFine.setSelectedIndex(0);
        ((GameEventPolice) gameData.getCurrentEvent()).setFine(1);

        rgLawLevel.setSelectedIndex(0);
        ((GameEventPolice) gameData.getCurrentEvent()).setLawLevel(0);

        updateUI();
    }

    private void updateUI() {
        GameEventPolice event = (GameEventPolice) gameData.getCurrentEvent();
        String outcome, title;
        if (event.caughtInTheAct()) {
            outcome = getResources().getString(R.string.police_outcome_rule);
            ((TextView) findViewById(R.id.tYouPay)).setText(String.format("$ %d", event.whatToPay()));
            title = "Caught with illegal goods.";
        } else {
            title = "Ok. You are clean.";
            outcome = "You may go now. But I'll keep an eye on you, pal...";
            for (int i : new int[]{R.id.lblConstableFine, R.id.lblLawLevel, R.id.lblPoliceRule, R.id.rgLawLevel, R.id.rgConstableFine, R.id.lblYouPay, R.id.tYouPay, R.id.llYouPay}) {
                findViewById(i).setVisibility(View.GONE);
            }
        }
        ((TextView) findViewById(R.id.lblPoliceOutcome)).setText(outcome);
        ((TextView) findViewById(R.id.lblPoliceTitle)).setText(title);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.constable, menu);
        return true;
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
}
