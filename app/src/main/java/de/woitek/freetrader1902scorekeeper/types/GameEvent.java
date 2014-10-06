package de.woitek.freetrader1902scorekeeper.types;

import java.util.Observable;

public abstract class GameEvent extends Observable {
    protected GameData gameData = null;

    void setGameData(GameData gameData) {
        this.gameData = gameData;
    }

    public EventType getEventType() {
        return EventType.NONE;
    }

    public enum EventType {
        FIGHT,
        CARGOCHECK,
        NONE
    }
}
