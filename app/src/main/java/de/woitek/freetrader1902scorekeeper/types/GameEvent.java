package de.woitek.freetrader1902scorekeeper.types;

/**
 * Created by Udo.Woitek on 06.10.2014.
 */
public interface GameEvent {
	public enum EventType {
		FIGHT,
		CARGOCHECK
	}

	public abstract EventType getEventType();
}
