package de.woitek.freetrader1902scorekeeper.types;

import android.content.SharedPreferences;

public class GameEventNone extends GameEvent {
    @Override
    public EventType getEventType() {
        return EventType.NONE;
    }

    @Override
    public void saveOnPause(SharedPreferences.Editor editor) {

    }
}
