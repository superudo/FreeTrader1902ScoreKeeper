package de.woitek.freetrader1902scorekeeper;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.Observable;
import java.util.Observer;

import de.woitek.freetrader1902scorekeeper.types.GameData;
import de.woitek.freetrader1902scorekeeper.types.GameFight;
import de.woitek.libraries.styledradiogroup.StyledRadioGroup;

public class EventActivity extends Activity implements RadioGroup.OnCheckedChangeListener, Observer {
    public final String CLASSID = "EventActivity";

    GameData gameData;
    GameFight gameFight;
    private ViewSwitcher switcher;
    private StyledRadioGroup rgAction;

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getId()) {
            case R.id.rgAction:
                switchAction(i);
                break;
            case R.id.rgTask:
                switchTask(i);
                break;
            case R.id.rgMultiplier:
                switchMultiplier(i);
                break;
            case R.id.rgYourStat:
                switchYourStat(i);
                break;
            case R.id.rgTheirStat:
                switchTheirStat(i);
                break;
            case R.id.rgConstableFine:
                switchConstableFine(i);
                break;
            case R.id.rgLawLevel:
                switchLawLevel(i);
            default:
                Log.d(CLASSID, String.format("Unknown RadioGroupId: %d", radioGroup.getId()));
                break;
        }
    }

    private void switchLawLevel(int lawLevel) {

    }

    private void switchConstableFine(int fine) {

    }

    private void switchTheirStat(int selectedButtonIndex) {
        gameFight.setEnemyModifier(selectedButtonIndex + 1);
    }

    private void switchYourStat(int selectedButtonIndex) {
        gameFight.setPlayerModifier(selectedButtonIndex + 1);
    }

    private void switchMultiplier(int selectedButtonIndex) {
        gameFight.setEnemyMight(selectedButtonIndex + 1);
    }

    private void switchTask(int selectedButtonIndex) {
        ((RadioGroup) findViewById(R.id.rgYourStat)).clearCheck();
        ((RadioGroup) findViewById(R.id.rgTheirStat)).clearCheck();
        GameFight.Fight fight;
        switch (selectedButtonIndex) {
            case 0:
                fight = GameFight.Fight.DEFENSE;
                break;
            case 1:
                fight = GameFight.Fight.OFFENSE;
                break;
            default:
                fight = GameFight.Fight.NOFIGHT;
                break;
        }
        updateTextViews(fight);
        gameFight.setFight(fight);
    }

    private void updateTextViews(GameFight.Fight fight) {
        SparseArray<String> a = new SparseArray<String>();
        switch (fight) {
            case OFFENSE:
                a.append(R.id.lblYourBonus, String.format("%s: %d", GameData.SHOTGUNS, gameData.getEquipment(GameData.SHOTGUNS)));
                a.append(R.id.lblTheirBonus, String.format("MONTH BONUS: %d", gameData.getMonth() - 1));
                a.append(R.id.lblYourStateType, "AGGRESSION");
                a.append(R.id.lblTheirStateType, "DEFENSE");
                a.append(R.id.lblYourFinalStat, "ATTACK");
                a.append(R.id.lblTheirFinalStat, "DEFENSE");
                break;
            case DEFENSE:
                a.append(R.id.lblYourBonus, String.format("%s: %d", GameData.ARMOR, gameData.getEquipment(GameData.ARMOR)));
                a.append(R.id.lblTheirBonus, String.format("MONTH BONUS: %d", gameData.getMonth() - 1));
                a.append(R.id.lblYourStateType, "DEFENSE");
                a.append(R.id.lblTheirStateType, "AGGRESSION");
                a.append(R.id.lblYourFinalStat, "DEFENSE");
                a.append(R.id.lblTheirFinalStat, "ATTACK");
                break;
            case NOFIGHT:
                a.append(R.id.lblYourBonus, "ARMOR / SHOTGUNS");
                a.append(R.id.lblTheirBonus, "MONTH BONUS");
                a.append(R.id.lblYourStateType, "MODIFIER");
                a.append(R.id.lblTheirStateType, "MODIFIER");
                a.append(R.id.lblYourFinalStat, "YOURS");
                a.append(R.id.lblTheirFinalStat, "THEIRS");
                a.append(R.id.tYourFinalStat, "0");
                a.append(R.id.tTheirFinalStat, "0");
                a.append(R.id.lblRules, "");
                a.append(R.id.lblOutcome, "SELECT TASK AND MODIFIERS");
                break;
        }
        final int N = a.size();
        for (int i = 0; i < N; i++) {
            ((TextView) findViewById(a.keyAt(i))).setText(a.valueAt(i));
        }
    }

    private void switchAction(int selectedButtonIndex) {
        switch (selectedButtonIndex) {
            case 0: // Bandit
                gameFight.setEnemy(GameFight.Enemy.HIGHWAYMAN);
                findViewById(R.id.rgMultiplier).setVisibility(View.VISIBLE);
                switcher.setDisplayedChild(1);
                break;
            case 1: // Bear
                gameFight.setEnemy(GameFight.Enemy.BEAR);
                findViewById(R.id.rgMultiplier).setVisibility(View.INVISIBLE);
                switcher.setDisplayedChild(1);
                break;
            case 2: // Police
                switcher.setDisplayedChild(0);
                break;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        gameData = getIntent().getParcelableExtra(GameData.CLASSNAME);
        if (gameData == null) {
            gameData = new GameData();
        }
        gameFight = new GameFight(gameData);

        ActionBar b = getActionBar();
        if (b != null) {
            b.setDisplayUseLogoEnabled(false);
            b.setDisplayHomeAsUpEnabled(true);
        }

        switcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        switcher.reset();

        rgAction = (StyledRadioGroup) findViewById(R.id.rgAction);
        rgAction.setOnCheckedChangeListener(this);

        ((RadioGroup) findViewById(R.id.rgMultiplier)).setOnCheckedChangeListener(this);
        ((RadioGroup) findViewById(R.id.rgTask)).setOnCheckedChangeListener(this);
        ((RadioGroup) findViewById(R.id.rgYourStat)).setOnCheckedChangeListener(this);
        ((RadioGroup) findViewById(R.id.rgTheirStat)).setOnCheckedChangeListener(this);
        ((RadioGroup) findViewById(R.id.rgLawLevel)).setOnCheckedChangeListener(this);
        ((RadioGroup) findViewById(R.id.rgConstableFine)).setOnCheckedChangeListener(this);

        gameFight.addObserver(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent mainIntent = new Intent(this, MainActivity.class);
                mainIntent.putExtra("GameData", gameData);
                startActivity(mainIntent);
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

    @Override
    public void update(Observable observable, Object o) {
        SparseArray<String> a = new SparseArray<String>();
        if (gameFight.canFight()) { // Fight result
            a.append(R.id.tYourFinalStat, String.format("%d", gameFight.getPlayerFightValue()));
            a.append(R.id.tTheirFinalStat, String.format("%d", gameFight.getEnemyFightValue()));
            if (gameFight.hasPlayerWon()) {
                a.append(R.id.lblOutcome, "YOU HAVE WON THE FIGHT");
                if (gameFight.getFight() == GameFight.Fight.DEFENSE) {
                    a.append(R.id.lblRules, "You may now fight back or try to flee by drawing another event card.");
                } else { // OFFENSE
                    if (gameFight.getEnemy() == GameFight.Enemy.HIGHWAYMAN) {
                        a.append(R.id.lblRules,
                                String.format("You get $ %d as a bonus for killing these bandits. You may now proceed to the next city. Use the sell modifiers at the bandit card to calculate prices.",
                                        gameFight.getEnemyBonusValue()));
                    } else {
                        a.append(R.id.lblRules, "You may now proceed to the next city. Use the sell modifiers at the bear card to calculate prices.");
                    }
                }
            } else {
                a.append(R.id.lblOutcome, "YOU LOST THE FIGHT");
                a.append(R.id.lblRules, "You loose one part of your truck. You may now fight back or try to flee by drawing another event card.");
            }
        } else { // No fight
            a.append(R.id.tYourFinalStat, "0");
            a.append(R.id.tTheirFinalStat, "0");
            a.append(R.id.lblRules, "");
            a.append(R.id.lblOutcome, "");
        }

        final int N = a.size();
        for (int i = 0; i < N; i++) {
            ((TextView) findViewById(a.keyAt(i))).setText(a.valueAt(i));
        }
    }
}
