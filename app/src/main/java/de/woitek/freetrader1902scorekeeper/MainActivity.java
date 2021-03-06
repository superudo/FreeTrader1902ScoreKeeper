package de.woitek.freetrader1902scorekeeper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import de.woitek.freetrader1902scorekeeper.dialogs.CargoDialog;
import de.woitek.freetrader1902scorekeeper.dialogs.EquipmentDialog;
import de.woitek.freetrader1902scorekeeper.dialogs.EventDialog;
import de.woitek.freetrader1902scorekeeper.types.GameData;
import de.woitek.freetrader1902scorekeeper.views.BoxUIView;

public class MainActivity extends Activity
		implements PropertyChangeListener, View.OnClickListener {

	private GameData gameData;

	private TextView vGold;

	private BoxUIView bCargo;
	private BoxUIView bEngine;
	private BoxUIView bShotguns;
	private BoxUIView bArmor;

	private BoxUIView bProduce;
	private BoxUIView bMunitions;
	private BoxUIView bTextiles;
	private BoxUIView bMoonshine;

	private BoxUIView bMonth;

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("de.woitek.freetrader1902scorekeeper.MainActivity", "onResume");
        gameData = new GameData();
        gameData.loadOnResume(getPreferences(MODE_PRIVATE));

        if (getIntent().hasExtra(GameData.CLASSNAME)) {
            gameData = getIntent().getParcelableExtra(GameData.CLASSNAME);
        }

        if (gameData == null) {
            gameData = new GameData();
        }

        gameData.addChangeListener(this);
        updateViewByGameData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("de.woitek.freetrader1902scorekeeper.MainActivity", "onPause");
        gameData.saveOnPause(getPreferences(MODE_PRIVATE));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		gameData = getIntent().getParcelableExtra(GameData.CLASSNAME);
		if (gameData == null) {
			gameData = new GameData();
		}

		setContentView(R.layout.activity_main);

		vGold = (TextView) findViewById(R.id.tGold);

		bCargo = (BoxUIView) findViewById(R.id.boxCargo);
		bEngine = (BoxUIView) findViewById(R.id.boxEngine);
		bShotguns = (BoxUIView) findViewById(R.id.boxShotguns);
		bArmor = (BoxUIView) findViewById(R.id.boxArmor);

		bProduce = (BoxUIView) findViewById(R.id.boxProduce);
		bMunitions = (BoxUIView) findViewById(R.id.boxMunitions);
		bTextiles = (BoxUIView) findViewById(R.id.boxTextiles);
		bMoonshine = (BoxUIView) findViewById(R.id.boxMoonshine);

		bMonth = (BoxUIView) findViewById(R.id.boxMonth);

		findViewById(R.id.rowProduce).setOnClickListener(this);
		findViewById(R.id.rowMunition).setOnClickListener(this);
		findViewById(R.id.rowTextiles).setOnClickListener(this);
		findViewById(R.id.rowMoonshine).setOnClickListener(this);

		findViewById(R.id.rowCargo).setOnClickListener(this);
		findViewById(R.id.rowEngine).setOnClickListener(this);
		findViewById(R.id.rowShotguns).setOnClickListener(this);
		findViewById(R.id.rowArmor).setOnClickListener(this);

		findViewById(R.id.llGold).setOnClickListener(this);
		findViewById(R.id.tMove).setOnClickListener(this);
		findViewById(R.id.tEvent).setOnClickListener(this);
		findViewById(R.id.rowMonth).setOnClickListener(this);

		gameData.addChangeListener(this);
		updateViewByGameData();
	}

	private void updateViewByGameData() {
		findViewById(R.id.rowMonth).setBackgroundColor(getResources().getColor(R.color.month_row_color));
		((TextView) findViewById(R.id.lblMonth)).setText(GameData.MONTH);
		bMonth.setOwned(gameData.getMonth());

		vGold.setText(String.format("$ %d", gameData.getMoney()));

		bProduce.setOwned(gameData.getCargo(GameData.PRODUCE));
		bMunitions.setOwned(gameData.getCargo(GameData.MUNITIONS));
		bTextiles.setOwned(gameData.getCargo(GameData.TEXTILES));
		bMoonshine.setOwned(gameData.getCargo(GameData.MOONSHINE));

		bCargo.setOwned(gameData.getEquipment(GameData.CARGO));
		bCargo.setFilled(gameData.getCurrentCargoAmount());

		bEngine.setOwned(gameData.getEquipment(GameData.ENGINE));
		bShotguns.setOwned(gameData.getEquipment(GameData.SHOTGUNS));
		bArmor.setOwned(gameData.getEquipment(GameData.ARMOR));

		setCargoColorAndCheckOverload();
	}

	private void setCargoColorAndCheckOverload() {
		int c = getResources().getColor(R.color.cargo_color);
		if (gameData.isOverload()) {
			c = getResources().getColor(R.color.cargo_red);
			warnAboutOverload();
		}
		((TextView) findViewById(R.id.lblCargo)).setTextColor(c);
	}

	private boolean warnAboutOverload() {
		if (!gameData.isOverload()) {
			return false;
		}
		new AlertDialog.Builder(this)
				.setTitle("Overload!")
				.setMessage("You need to drop some cargo!")
				.setPositiveButton("Ok", null)
				.create()
				.show();
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
			case R.id.action_restart:
				RestartGame();
				return true;
			case R.id.action_quit: // http://stackoverflow.com/questions/20853087/finish-activity-when-back-button-twice-pressed
				finish();
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void RestartGame() {
		gameData.InitGameData();
		updateViewByGameData();
	}

	@Override
	public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
		String propName = propertyChangeEvent.getPropertyName();

		if (propName.equals(GameData.MONTH)) {
			bMonth.setOwned(gameData.getMonth());
		}

		if (propName.equals(GameData.MONEY)) {
			vGold.setText(String.format("$ %d", gameData.getMoney()));
		}

		if (propName.equals(GameData.CARGO) || gameData.isCargo(propName)) {
			bCargo.setOwned(gameData.getEquipment(GameData.CARGO));
			bCargo.setFilled(gameData.getCurrentCargoAmount());
		}

		if (propName.equals(GameData.ENGINE)) {
			bEngine.setOwned(gameData.getEquipment(GameData.ENGINE));
		}

		if (propName.equals(GameData.SHOTGUNS)) {
			bShotguns.setOwned(gameData.getEquipment(GameData.SHOTGUNS));
		}

		if (propName.equals(GameData.ARMOR)) {
			bArmor.setOwned(gameData.getEquipment(GameData.ARMOR));
		}

		if (propName.equals(GameData.PRODUCE)) {
			bProduce.setOwned(gameData.getCargo(GameData.PRODUCE));
		}

		if (propName.equals(GameData.MUNITIONS)) {
			bMunitions.setOwned(gameData.getCargo(GameData.MUNITIONS));
		}

		if (propName.equals(GameData.TEXTILES)) {
			bTextiles.setOwned(gameData.getCargo(GameData.TEXTILES));
		}

		if (propName.equals(GameData.MOONSHINE)) {
			bMoonshine.setOwned(gameData.getCargo(GameData.MOONSHINE));
		}

		setCargoColorAndCheckOverload();

		if (propName.equals(GameData.WINNER)) {
			LinearLayout row = (LinearLayout) findViewById(R.id.rowMonth);
			if (gameData.wonGame() || gameData.lostGame()) { // WINNER!
				Intent intent = new Intent(this, GameEndActivity.class);
				intent.putExtra(GameData.CLASSNAME, gameData);
				startActivity(intent);
				finish();
			} else { // Game start
				row.setBackgroundColor(getResources().getColor(R.color.month_row_color));
				((TextView) findViewById(R.id.lblMonth)).setText(GameData.MONTH);
				bMonth.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void onClick(View view) {
		if (!gameData.gameFinished()) {
			switch (view.getId()) {
				case R.id.rowProduce:
					new CargoDialog(this, gameData, GameData.PRODUCE).show();
					break;
				case R.id.rowMunition:
					new CargoDialog(this, gameData, GameData.MUNITIONS).show();
					break;
				case R.id.rowTextiles:
					new CargoDialog(this, gameData, GameData.TEXTILES).show();
					break;
				case R.id.rowMoonshine:
					new CargoDialog(this, gameData, GameData.MOONSHINE).show();
					break;
				case R.id.rowCargo:
					if (!warnAboutOverload()) {
						new EquipmentDialog(this, gameData, GameData.CARGO).show();
					}
					break;
				case R.id.rowEngine:
					if (!warnAboutOverload()) {
						new EquipmentDialog(this, gameData, GameData.ENGINE).show();
					}
					break;
				case R.id.rowShotguns:
					if (!warnAboutOverload()) {
						new EquipmentDialog(this, gameData, GameData.SHOTGUNS).show();
					}
					break;
				case R.id.rowArmor:
					if (!warnAboutOverload()) {
						new EquipmentDialog(this, gameData, GameData.ARMOR).show();
					}
					break;
				case R.id.rowMonth:
					if (!warnAboutOverload()) {
						FinishCurrentMonth();
					}
					break;
				case R.id.tMove:
					if (!warnAboutOverload()) {
						MoveToNextCity();
					}
					break;
				case R.id.tEvent:
					if (!warnAboutOverload()) {
						EventDialog dlg = new EventDialog(this, gameData);
						dlg.show();
					}
					break;
				default:
					break;
			}
		}
	}

	private void MoveToNextCity() {
		if (gameData.mayMove()) {
			new AlertDialog.Builder(this)
					.setTitle("Move to next city")
					.setMessage("Ready to go?")
					.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogInterface, int i) {
							gameData.setMoney(gameData.getMoney() - 1);
						}
					})
					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialogInterface, int i) {
								}
							}
					)
					.create()
					.show();
		} else {
			new AlertDialog.Builder(this)
					.setTitle("Move to next city")
					.setMessage("Seems that you're broke.")
					.setPositiveButton("Ok", null)
					.create()
					.show();
		}
	}

	private void FinishCurrentMonth() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("New Month")
				.setMessage("End current month?")
				.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						gameData.finishMonth();
					}
				})
				.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).create().show();
	}
}
