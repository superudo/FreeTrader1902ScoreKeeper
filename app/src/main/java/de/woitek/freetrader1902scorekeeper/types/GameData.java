package de.woitek.freetrader1902scorekeeper.types;

import android.os.Parcel;
import android.os.Parcelable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;

import static de.woitek.freetrader1902scorekeeper.types.GameEvent.EventType;

public class GameData implements Parcelable {
    public static final String CLASSNAME = "GAMEDATA";

    public static final String CARGO = "CARGO";
    public static final String ENGINE = "ENGINE";
    public static final String ARMOR = "ARMOR";
    public static final String SHOTGUNS = "SHOTGUNS";

    public static final String PRODUCE = "PRODUCE";
    public static final String MUNITIONS = "MUNITIONS";
    public static final String TEXTILES = "TEXTILES";
    public static final String MOONSHINE = "MOONSHINE";

    public static final String MONTH = "MONTH";
    public static final String MONEY = "MONEY";

    public static final String WINNER = "WINNER";

    public static final Creator<GameData> CREATOR
            = new Creator<GameData>() {
        @Override
        public GameData createFromParcel(Parcel parcel) {
            return new GameData(parcel);
        }

        @Override
        public GameData[] newArray(int size) {
            return new GameData[size];
        }
    };

    private HashMap<String, RangedInteger> mEquipment = null;
    private HashMap<String, RangedInteger> mCargo = null;
    private RangedInteger month;
    private RangedInteger money;

    private HashMap<String, Integer> equipmentPrice = null;
    private int[] monthlyRate;

    private int gameState;
    private GameEvent currentEvent;
    private GameEvent noEvent = new GameEventNone();

    private ArrayList<PropertyChangeListener> listener;

    public GameData() {
        init();
    }

    public GameData(Parcel parcel) {
        init();

        String eventName = parcel.readString();
        switch (EventType.valueOf(eventName)) {
            case FIGHT:
                currentEvent = (GameEventFight) parcel.readParcelable(GameEventFight.class.getClassLoader());
                break;
            case CARGOCHECK:
                currentEvent = (GameEventPolice) parcel.readParcelable(GameEventPolice.class.getClassLoader());
                break;
            case NONE:
                currentEvent = noEvent;
                break;
        }
        currentEvent.setGameData(this);

        month.setValue(parcel.readInt());
        money.setValue(parcel.readInt());
        for (String s : new String[]{CARGO, ENGINE, SHOTGUNS, ARMOR}) {
            setEquipment(s, parcel.readInt());
        }
        for (String s : new String[]{PRODUCE, MUNITIONS, TEXTILES, MOONSHINE}) {
            setCargo(s, parcel.readInt());
        }
    }

    private void init() {
        InitGameData();
        listener = new ArrayList<PropertyChangeListener>();
    }

    public void InitGameData() {
        if (mEquipment == null) {
            mEquipment = new HashMap<String, RangedInteger>(4);
        }
        if (equipmentPrice == null) {
            equipmentPrice = new HashMap<String, Integer>(4);
        }
        if (mCargo == null) {
            mCargo = new HashMap<String, RangedInteger>(4);
        }

        mEquipment.clear();
        mEquipment.put(CARGO, new RangedInteger(0, 5, 3));
        mEquipment.put(ENGINE, new RangedInteger(0, 5, 3));
        mEquipment.put(ARMOR, new RangedInteger(0, 5, 2));
        mEquipment.put(SHOTGUNS, new RangedInteger(0, 5, 2));

        mCargo.clear();
        mCargo.put(PRODUCE, new RangedInteger(0, 5, 0));
        mCargo.put(MUNITIONS, new RangedInteger(0, 5, 0));
        mCargo.put(TEXTILES, new RangedInteger(0, 5, 0));
        mCargo.put(MOONSHINE, new RangedInteger(0, 5, 0));

        equipmentPrice.clear();
        equipmentPrice.put(CARGO, 5);
        equipmentPrice.put(ENGINE, 3);
        equipmentPrice.put(SHOTGUNS, 4);
        equipmentPrice.put(ARMOR, 5);

        month = new RangedInteger(1, 4, 1);
        money = new RangedInteger(0, 20, 5);

        monthlyRate = new int[]{0, 5, 10, 10, 15};

        gameState = 0;
    }

    protected boolean CargoOk(int oldValue, int newValue) {
        return (getCurrentCargoAmount() - oldValue + newValue <= mEquipment.get(CARGO).getValue());
    }

    private boolean IsDifferent(int oldValue, int newValue) {
        return (oldValue != newValue);
    }

    public int getEquipment(String type) {
        if (mEquipment.containsKey(type)) {
            return mEquipment.get(type).getValue();
        }
        throw new IllegalArgumentException(type + " is no valid equipment key.");
    }

    protected void setEquipment(String type, int value) {
        if (mEquipment.containsKey(type)) {
            int oldValue = mEquipment.get(type).getValue();
            if (IsDifferent(oldValue, value)) {
                mEquipment.get(type).setValue(value);
                notifyListeners(this, type, oldValue, value);
            }
        } else {
            throw new IllegalArgumentException(type + " is no valid equipment key.");
        }
    }

    public Integer getCargo(String type) {
        if (mCargo.containsKey(type)) {
            return mCargo.get(type).getValue();
        }
        throw new IllegalArgumentException(type + " is no valid cargo key.");
    }

    protected void setCargo(String type, int value) {
        if (mCargo.containsKey(type)) {
            int oldValue = mCargo.get(type).getValue();
            if (IsDifferent(oldValue, value) && CargoOk(oldValue, value)) {
                mCargo.get(type).setValue(value);
                notifyListeners(this, type, oldValue, value);
            }
        } else {
            throw new IllegalArgumentException(type + " is no valid cargo key.");
        }
    }

    protected void nextMonth() {
        int oldValue = month.incValue();
        notifyListeners(this, MONTH, oldValue, month.getValue());
    }

    /*
        protected int getHenchmenBaseValue() {
            return month.getValue() - 1;
        }
    */
    public int getCurrentCargoAmount() {
        int sum = 0;
        for (RangedInteger i : mCargo.values()) {
            sum += i.getValue();
        }
        return sum;
    }

    private void notifyListeners(Object object, String property, int oldValue, int newValue) {
        for (PropertyChangeListener name : listener) {
            name.propertyChange(new PropertyChangeEvent(object, property, oldValue, newValue));
        }
    }

    public void addChangeListener(PropertyChangeListener newListener) {
        listener.add(newListener);
    }

    public int getMonth() {
        return month.getValue();
    }

    public int getMoney() {
        return money.getValue();
    }

    public void setMoney(int value) {
        if (money.getValue() != value) {
            notifyListeners(this, MONEY, money.setValue(value), money.getValue());
        }
    }

    public boolean mayBuyCargo(int amount, int price, String toBuy) {
        return (amount > 0) && ((money.getValue() - amount * price) > 0) && CargoOk(mCargo.get(toBuy).getValue(), amount);
    }

    public boolean maySellCargo(int amount, String toSell) {
        return (amount <= mCargo.get(toSell).getValue());
    }

    public void buyCargo(int amount, int price, String toBuy) {
        if (mayBuyCargo(amount, price, toBuy)) {
            setCargo(toBuy, getCargo(toBuy) + amount);
            setMoney(money.getValue() - amount * price);
        }
    }

    public void sellCargo(int amount, int price, String toSell) {
        if (maySellCargo(amount, toSell)) {
            setCargo(toSell, getCargo(toSell) - amount);
            setMoney(money.getValue() + amount * price);
        }
    }

    public void dropCargo(int amount, String toSell) {
        setCargo(toSell, getCargo(toSell) - amount);
    }

    public boolean mayBuyEquipment(String toBuy) {
        return (((equipmentPrice.get(toBuy)) <= getMoney()) && ((mEquipment.get(toBuy).getValue() + 1) <= mEquipment.get(toBuy).getMax()));
    }

    public void buyEquipment(String toBuy) {
        if (mayBuyEquipment(toBuy)) {
            setEquipment(toBuy, mEquipment.get(toBuy).getValue() + 1);
            setMoney(getMoney() - equipmentPrice.get(toBuy));
        }
    }

    public boolean maySellEquipment(String toSell) {
        return (1 <= mEquipment.get(toSell).getValue());
    }

    public void sellEquipment(String toSell) {
        if (maySellEquipment(toSell)) {
            setEquipment(toSell, mEquipment.get(toSell).getValue() - 1);
            setMoney(getMoney() + equipmentPrice.get(toSell) / 2);
        }
    }

    public boolean mayDropEquipment(String toDrop) {
        return maySellEquipment(toDrop);
    }

    public void dropEquipment(String toDrop) {
        if (mayDropEquipment(toDrop)) {
            setEquipment(toDrop, mEquipment.get(toDrop).getValue() - 1);
        }
    }

    public boolean gameFinished() {
        return (gameState != 0);
    }

    public boolean wonGame() {
        return (gameState == 1);
    }

    public boolean lostGame() {
        return (gameState == -1);
    }

    protected void setGameState(int value) {
        if (gameState != value) {
            gameState = value;
            notifyListeners(this, WINNER, 0, gameState);
        }
    }

    public void finishMonth() {
        if (getMoney() < monthlyRate[getMonth()]) {
            // loser
            setGameState(-1);
        } else {
            setMoney(getMoney() - monthlyRate[getMonth()]);
            if (getMonth() < month.getMax() - 1) {
                nextMonth();
            } else {
                // winner!!!
                setGameState(1);
            }
        }
    }

    public boolean isCargo(String propName) {
        return propName.equals(PRODUCE) || propName.equals(MUNITIONS) || propName.equals(TEXTILES) || propName.equals(MOONSHINE);
    }

    public boolean mayMove() {
        return (getMoney() > 0);
    }

    public int getFreeCargoAmount() {
        return mEquipment.get(CARGO).getValue() - getCurrentCargoAmount();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(currentEvent.getEventType().toString());
        switch (currentEvent.getEventType()) {
            case FIGHT:
                parcel.writeParcelable((GameEventFight) currentEvent, i);
                break;
            case CARGOCHECK:
                parcel.writeParcelable((GameEventPolice) currentEvent, i);
                break;
            case NONE:
                break;
        }

        parcel.writeInt(getMonth());
        parcel.writeInt(getMoney());
        for (String s : new String[]{CARGO, ENGINE, SHOTGUNS, ARMOR}) {
            parcel.writeInt(getEquipment(s));
        }
        for (String s : new String[]{PRODUCE, MUNITIONS, TEXTILES, MOONSHINE}) {
            parcel.writeInt(getCargo(s));
        }
    }

    public void clearCurrentEvent() {
        currentEvent = noEvent;
    }

    public GameEvent getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(GameEvent event) {
        currentEvent = event;
    }
}
