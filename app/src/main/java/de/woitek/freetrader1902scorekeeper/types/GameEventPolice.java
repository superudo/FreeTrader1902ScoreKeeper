package de.woitek.freetrader1902scorekeeper.types;

import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

public class GameEventPolice extends GameEvent implements Parcelable {
    public static final Creator<GameEventPolice> CREATOR = new Creator<GameEventPolice>() {
        @Override
        public GameEventPolice createFromParcel(Parcel parcel) {
            return new GameEventPolice(parcel);
        }

        @Override
        public GameEventPolice[] newArray(int i) {
            return new GameEventPolice[i];
        }
    };
    private int mFine = 0;
    private int mLawLevel = 0;

    public GameEventPolice(GameData data) {
        gameData = data;
    }

    public GameEventPolice(Parcel parcel) {
        mFine = parcel.readInt();
        mLawLevel = parcel.readInt();
    }

    public GameEventPolice(SharedPreferences prefs) {
        mFine = prefs.getInt("EventPoliceFine", 0);
        mLawLevel = prefs.getInt("EventPoliceLaw", 0);
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

    @Override
    public void saveOnPause(SharedPreferences.Editor editor) {
        editor.putInt("EventPoliceFine", mFine);
        editor.putInt("EventPoliceLaw", mLawLevel);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mFine);
        parcel.writeInt(mLawLevel);
    }
}
