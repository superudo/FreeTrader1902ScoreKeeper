package de.woitek.freetrader1902scorekeeper.types;

/**
 * Created by Udo.Woitek on 06.10.2014.
 */
public class GamePolice implements GameEvent {
	private GameData gameData;
	private int mFine = 0;
	private int mLawLevel = 0;

	public GamePolice(GameData data) {
		gameData = data;
	}

	private GamePolice() {
	}

	public void setLawLevel(int lawLevel) {
		mLawLevel = lawLevel;
	}

	public void setFine(int fine) {
		mFine = fine;
	}

	public boolean caughtInTheAct() {
		return gameData.getCargo(GameData.MOONSHINE) > 0;
	}

	public int whatToPay() {
		return (caughtInTheAct()) ? mFine * mLawLevel : 0;
	}

	@Override
	public EventType getEventType() {
		return EventType.CARGOCHECK;
	}
}
