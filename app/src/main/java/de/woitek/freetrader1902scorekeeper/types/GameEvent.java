package de.woitek.freetrader1902scorekeeper.types;

import android.content.SharedPreferences;

import java.util.Observable;

public abstract class GameEvent extends Observable {
    protected GameData gameData = null;

    void setGameData(GameData gameData) {
        this.gameData = gameData;
    }

    public EventType getEventType() {
        return EventType.NONE;
    }

    public abstract void saveOnPause(SharedPreferences.Editor editor);

    public enum EventType {
        FIGHT,
        CARGOCHECK,
        NONE
    }
}
