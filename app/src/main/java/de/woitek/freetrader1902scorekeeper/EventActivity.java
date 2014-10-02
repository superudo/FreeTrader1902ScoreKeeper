package de.woitek.freetrader1902scorekeeper;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import de.woitek.freetrader1902scorekeeper.types.GameData;
import de.woitek.libraries.styledradiogroup.StyledRadioGroup;

public class EventActivity extends Activity {
    GameData gameData;

    StyledRadioGroup rgAction;
    StyledRadioGroup rgTask;
    StyledRadioGroup yourStats;
    StyledRadioGroup theirStats;

    private ViewSwitcher switcher;
    private int switcherViewId = R.id.llBandit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        ActionBar b = getActionBar();
        if (b != null) {
            b.setDisplayUseLogoEnabled(false);
            b.setDisplayHomeAsUpEnabled(true);
        }
        gameData = GameData.CreateFromPreferences(getPreferences(MODE_PRIVATE));

        switcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);

        rgAction = (StyledRadioGroup) findViewById(R.id.rgAction);
        rgAction.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case 0:
                        fightBandits();
                        switchTo(R.id.llBandit);
                    case 1:
                        fightBear();
                        switchTo(R.id.llBandit);
                        break;
                    case 2:
                        policeCheck();
                        switchTo(R.id.llPolice);
                        break;
                }
                if ((i < 2) && (switcherViewId != R.id.llBandit)) {

                    switcherViewId = R.id.llBandit;
                } else if ((i == 2) && (switcherViewId != R.id.llPolice)) {
                    switcher.showNext();
                    switcherViewId = R.id.llPolice;
                }
            }

            private void switchTo(int viewId) {
                if (switcherViewId != viewId) {
                    switcher.showPrevious();
                    switcherViewId = viewId;
                }
            }
        });

        rgTask = (StyledRadioGroup) findViewById(R.id.rgTask);
        rgTask.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (rgTask.getSelectedIndex()) {
                    case 0: // Defense
                        ((TextView) findViewById(R.id.lblYourFinalStat)).setText("DEFENSE");
                        ((TextView) findViewById(R.id.lblTheirFinalStat)).setText("ATTACK");
                        ((TextView) findViewById(R.id.lblYourStateType)).setText("DEFENSE");
                        ((TextView) findViewById(R.id.lblTheirStateType)).setText("AGGRESSION");
                        ((TextView) findViewById(R.id.lblYourBonus)).setText(String.format("ARMOR: %d", gameData.getEquipment(GameData.ARMOR)));
                        ((TextView) findViewById(R.id.lblTheirBonus)).setText(String.format("MONTH BONUS: %d", gameData.getMonth() - 1));
                        ((StyledRadioGroup) findViewById(R.id.rgYourStat)).setSelectedIndex(0);
                        break;
                    case 1: // Attack
                        ((TextView) findViewById(R.id.lblYourFinalStat)).setText("ATTACK");
                        ((TextView) findViewById(R.id.lblTheirFinalStat)).setText("DEFENSE");
                        ((TextView) findViewById(R.id.lblYourStateType)).setText("AGGRESSION");
                        ((TextView) findViewById(R.id.lblTheirStateType)).setText("DEFENSE");
                        ((TextView) findViewById(R.id.lblYourBonus)).setText(String.format("SHOTGUNS: %d", gameData.getEquipment(GameData.SHOTGUNS)));
                        ((TextView) findViewById(R.id.lblTheirBonus)).setText(String.format("MONTH BONUS: %d", gameData.getMonth() - 1));
                        ((StyledRadioGroup) findViewById(R.id.rgYourStat)).setSelectedIndex(0);
                        break;
                    default:
                        break;
                }
            }
        });

        yourStats = (StyledRadioGroup) findViewById(R.id.rgYourStat);
        yourStats.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

            }
        });

        theirStats = (StyledRadioGroup) findViewById(R.id.rgTheirStat);
        theirStats.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

            }
        });

    }

    private void policeCheck() {

    }

    private void fightBear() {
        rgTask.setSelectedIndex(0);
    }

    private void fightBandits() {
        rgTask.setSelectedIndex(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                gameData.SaveState(getPreferences(MODE_PRIVATE));
                finish();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event, menu);
        return true;
    }
}
