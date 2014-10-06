package de.woitek.freetrader1902scorekeeper.types;

public class GameEventNone extends GameEvent {
    @Override
    public EventType getEventType() {
        return EventType.NONE;
    }
}
