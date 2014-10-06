package de.woitek.freetrader1902scorekeeper;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import de.woitek.freetrader1902scorekeeper.types.GameData;


public class FightActivity extends Activity {
	GameData gameData;

	@Override
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
    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.fight, menu);
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
