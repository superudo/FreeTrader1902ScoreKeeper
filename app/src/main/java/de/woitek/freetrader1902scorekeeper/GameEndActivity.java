package de.woitek.freetrader1902scorekeeper;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import de.woitek.freetrader1902scorekeeper.types.GameData;


public class GameEndActivity extends Activity {

	private GameData gameData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_end);

		gameData = getIntent().getParcelableExtra(GameData.CLASSNAME);
		if (gameData == null) {
			gameData = new GameData();
		}

		ActionBar b = getActionBar();
		if (b != null) {
			b.setTitle(gameData.wonGame() ? "You won!" : "You lost!");
		}

		((ImageView) findViewById(R.id.imgTruck)).setImageResource(gameData.wonGame() ? R.drawable.truck : R.drawable.carwreck);
		((TextView) findViewById(R.id.tHeadline)).setText(getHeadlineText());
		((TextView) findViewById(R.id.tExplanation)).setText(getExplanationText() + getGameReport());
	}

	private String getHeadlineText() {
		return (gameData.wonGame())
				? "Congratulations."
				: "You lost the game";
	}

	private String getExplanationText() {
		if (gameData.wonGame()) {
			return "You did it. You paid all your dues building a wealthy business. ";
		} else if (gameData.isInJail()) {
			return "You are arrested. ";
		} else {
			return "You are broke. ";
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_end, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_restart) {
			gameData = new GameData();
			Intent intent = new Intent(this, MainActivity.class);
			intent.putExtra(GameData.CLASSNAME, gameData);
			startActivity(intent);
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public String getGameReport() {
		if (gameData.wonGame()) {
			StringBuilder sb = new StringBuilder();
			sb.append(String.format(
					" You now own your truck with %d cargo places and a %d horse power engine. " +
							"Enemies are scared by your %d shotgun%s, protected by %d armor.",
					gameData.getEquipment(GameData.CARGO),
					gameData.getEquipment(GameData.ENGINE),
					gameData.getEquipment(GameData.SHOTGUNS),
					gameData.getEquipment(GameData.SHOTGUNS) == 1 ? "" : "s",
					gameData.getEquipment(GameData.ARMOR)
			));

			if (gameData.getMoney() > 0) {
				sb.append(String.format(" You still have %d golden dollars left. ", gameData.getMoney()));
			} else {
				sb.append(" You did it with your last golden dollar. Phew. ");
			}

			if (gameData.getCurrentCargoAmount() > 0) {
				sb.append(" Your truck currently holds");
				boolean needsComma = false;
				for (String id : new String[]{GameData.PRODUCE, GameData.TEXTILES, GameData.MUNITIONS, GameData.MOONSHINE}) {
					if (gameData.getCargo(id) > 0) {
						sb.append(String.format("%s %d %s",
								(needsComma) ? " and" : "",
								gameData.getCargo(id),
								id
						));
						needsComma = true;
					}
				}
				sb.append(". ");
			} else {
				sb.append(" Your trucks cargo bays are completely empty.");
			}

			sb.append(" You are very keen to see what challenges lie ahead...");

			return sb.toString();
		} else {
			return "Because you are not able to pay all your dues the bank took away " +
					"the truck and sold it. You are left in your misery. Beaten but not " +
					"broken you start thinking about your next big business. Well then...";
		}
	}
}
