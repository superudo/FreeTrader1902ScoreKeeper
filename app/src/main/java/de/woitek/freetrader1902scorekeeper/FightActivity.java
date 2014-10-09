package de.woitek.freetrader1902scorekeeper;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

import de.woitek.freetrader1902scorekeeper.dialogs.GetawayDialog;
import de.woitek.freetrader1902scorekeeper.dialogs.LooseEquipmentDialog;
import de.woitek.freetrader1902scorekeeper.types.GameData;
import de.woitek.freetrader1902scorekeeper.types.GameEventFight;
import de.woitek.libraries.styledradiogroup.StyledRadioGroup;


public class FightActivity extends Activity implements RadioGroup.OnCheckedChangeListener, Observer {
    private static final String CLASSID = "FightActivity";
    GameData gameData;
    GameEventFight event;

    private Button bnButton1;
    private Button bnButton2;

    private View.OnClickListener fightBackListener;
    private View.OnClickListener defendAgainListener;
    private View.OnClickListener loosePartListener;
    private View.OnClickListener fleeListener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight);

        ActionBar b = getActionBar();
        if (b != null) {
            b.setDisplayUseLogoEnabled(false);
            b.setDisplayHomeAsUpEnabled(true);
        }

        gameData = getIntent().getParcelableExtra(GameData.CLASSNAME);
        if (gameData == null) {
            gameData = new GameData();
        }

        event = (GameEventFight) gameData.getCurrentEvent();

        ((RadioGroup) findViewById(R.id.rgTask)).setOnCheckedChangeListener(this);
        ((RadioGroup) findViewById(R.id.rgYourStat)).setOnCheckedChangeListener(this);
        ((RadioGroup) findViewById(R.id.rgTheirStat)).setOnCheckedChangeListener(this);

        bnButton1 = (Button) findViewById(R.id.bnButton1);
        bnButton2 = (Button) findViewById(R.id.bnButton2);

        createButtonListeners();

        event.addObserver(this);
    }

	public void gotoMainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(GameData.CLASSNAME, gameData);
        startActivity(intent);
        finish();
    }

    private void createButtonListeners() {
	    final FightActivity a = this;
	    fleeListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dialog explaining Crank, with good or bad result
	            new GetawayDialog(a, gameData).show();
            }
        };

        fightBackListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((StyledRadioGroup) findViewById(R.id.rgTask)).setSelectedIndex(1);
            }
        };

        defendAgainListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((StyledRadioGroup) findViewById(R.id.rgTask)).setSelectedIndex(0);
            }
        };

        loosePartListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
	            new LooseEquipmentDialog(a, gameData).show();
            }
        };
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
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            case R.id.rgTask:
                switchTask(checkedId);
                break;
            case R.id.rgYourStat:
                switchYourStat(checkedId);
                break;
            case R.id.rgTheirStat:
                switchTheirStat(checkedId);
                break;
            default:
                Log.d(CLASSID, String.format("Unknown RadioGroupId: %d", group.getId()));
                break;
        }
    }

    private void switchTheirStat(int checkedId) {
        event.setEnemyModifier(checkedId + 1);
    }

    private void switchYourStat(int checkedId) {
        event.setPlayerModifier(checkedId + 1);
    }

    private void switchTask(int checkedId) {
        ((RadioGroup) findViewById(R.id.rgYourStat)).clearCheck();
        ((RadioGroup) findViewById(R.id.rgTheirStat)).clearCheck();
        GameEventFight.Fight fight;
        switch (checkedId) {
            case 0:
                fight = GameEventFight.Fight.DEFENSE;
                break;
            case 1:
                fight = GameEventFight.Fight.OFFENSE;
                break;
            default:
                fight = GameEventFight.Fight.NOFIGHT;
                break;
        }
        updateTextViews(fight);
        event.setFight(fight);
    }

    private void updateTextViews(GameEventFight.Fight fight) {
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

    private void updateButtons(String text1, View.OnClickListener listener1, String text2, View.OnClickListener listener2) {
        if (text1 == null) {
            bnButton1.setVisibility(View.GONE);
        } else {
            bnButton1.setVisibility(View.VISIBLE);
            bnButton1.setText(text1);
            bnButton1.setOnClickListener(listener1);
        }
        if (text2 == null) {
            bnButton2.setVisibility(View.GONE);
        } else {
            bnButton2.setVisibility(View.VISIBLE);
            bnButton2.setText(text2);
            bnButton2.setOnClickListener(listener2);
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        SparseArray<String> a = new SparseArray<String>();
        if (event.canFight()) { // Fight result
            a.append(R.id.tYourFinalStat, String.format("%d", event.getPlayerFightValue()));
            a.append(R.id.tTheirFinalStat, String.format("%d", event.getEnemyFightValue()));
            if (event.hasPlayerWon()) {
                a.append(R.id.lblOutcome, "YOU HAVE WON THE FIGHT");
                if (event.getFight() == GameEventFight.Fight.DEFENSE) {
                    a.append(R.id.lblRules, "You may now fight back or try to flee by drawing another event card.");
                    updateButtons("Fight back!", fightBackListener, "Flee...", fleeListener);
                } else { // OFFENSE
                    if (event.getEnemy() == GameEventFight.Enemy.HIGHWAYMAN) {
                        a.append(R.id.lblRules,
                                String.format("You get $ %d as a bonus for killing these bandits. You may now proceed to the next city. Use the sell modifiers at the bandit card to calculate prices.",
                                        event.getEnemyBonusValue()));
                    } else {
                        a.append(R.id.lblRules, "You may now proceed to the next city. Use the sell modifiers at the bear card to calculate prices.");
                    }
                    updateButtons("Go to city", fleeListener, null, null);
                }
            } else {
                a.append(R.id.lblOutcome, "YOU LOST THE FIGHT");
                if (event.getFight() == GameEventFight.Fight.DEFENSE) {
                    a.append(R.id.lblRules, "You loose one part of your truck."); // You may now fight back or try to flee by drawing another event card.");
                    updateButtons("Loose truck part", loosePartListener, null, null);
                } else {
                    a.append(R.id.lblRules, "The enemy will attack again. To the next fight...");
                    updateButtons("Defend again", defendAgainListener, null, null);
                }
            }
        } else { // No fight
            a.append(R.id.tYourFinalStat, "0");
            a.append(R.id.tTheirFinalStat, "0");
            a.append(R.id.lblRules, "");
            a.append(R.id.lblOutcome, "");
            updateButtons(null, null, null, null);
        }

        final int N = a.size();
        for (int i = 0; i < N; i++) {
            ((TextView) findViewById(a.keyAt(i))).setText(a.valueAt(i));
        }
    }
}

