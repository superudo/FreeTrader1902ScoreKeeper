package de.woitek.freetrader1902scorekeeper.types;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;

public class GameData {
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

	private HashMap<String, RangedInteger> equipment = null;
	private HashMap<String, RangedInteger> cargo = null;
	private HashMap<String, Integer> equipmentPrice = null;
	private int[] monthlyRate;
	private int gameState;

	private RangedInteger month;
	private RangedInteger money;

	private ArrayList<PropertyChangeListener> listener;

	public GameData() {
		InitGameData();
		listener = new ArrayList<PropertyChangeListener>();
	}

	public void InitGameData() {
		if (equipment == null) {
			equipment = new HashMap<String, RangedInteger>(4);
		}
		if (equipmentPrice == null) {
			equipmentPrice = new HashMap<String, Integer>(4);
		}
		if (cargo == null) {
			cargo = new HashMap<String, RangedInteger>(4);
		}

		equipment.clear();
		equipment.put(CARGO, new RangedInteger(0, 5, 3));
		equipment.put(ENGINE, new RangedInteger(0, 5, 3));
		equipment.put(ARMOR, new RangedInteger(0, 5, 2));
		equipment.put(SHOTGUNS, new RangedInteger(0, 5, 2));

		cargo.clear();
		cargo.put(PRODUCE, new RangedInteger(0, 5, 0));
		cargo.put(MUNITIONS, new RangedInteger(0, 5, 0));
		cargo.put(TEXTILES, new RangedInteger(0, 5, 0));
		cargo.put(MOONSHINE, new RangedInteger(0, 5, 0));

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
		return (getCurrentCargoAmount() - oldValue + newValue <= equipment.get(CARGO).getValue());
	}

	private boolean IsDifferent(int oldValue, int newValue) {
		return (oldValue != newValue);
	}

	public int getEquipment(String type) {
		if (equipment.containsKey(type)) {
			return equipment.get(type).getValue();
		}
		throw new IllegalArgumentException(type + " is no valid equipment key.");
	}

	protected void setEquipment(String type, int value) {
		if (equipment.containsKey(type)) {
			int oldValue = equipment.get(type).getValue();
			if (IsDifferent(oldValue, value)) {
				equipment.get(type).setValue(value);
				notifyListeners(this, type, oldValue, value);
			}
		} else {
			throw new IllegalArgumentException(type + " is no valid equipment key.");
		}
	}

	public Integer getCargo(String type) {
		if (cargo.containsKey(type)) {
			return cargo.get(type).getValue();
		}
		throw new IllegalArgumentException(type + " is no valid cargo key.");
	}

	protected void setCargo(String type, int value) {
		if (cargo.containsKey(type)) {
			int oldValue = cargo.get(type).getValue();
			if (IsDifferent(oldValue, value) && CargoOk(oldValue, value)) {
				cargo.get(type).setValue(value);
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
		for (RangedInteger i : cargo.values()) {
			sum += i.getValue();
		}
		return sum;
	}

	/*
		protected int getPossibleCargoAmount() {
			return equipment.get(CARGO).getValue() - getCurrentCargoAmount();
		}
	*/
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
		return (amount > 0) && ((money.getValue() - amount * price) > 0) && CargoOk(cargo.get(toBuy).getValue(), amount);
	}

	public boolean maySellCargo(int amount, String toSell) {
		return (amount <= cargo.get(toSell).getValue());
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

	public boolean mayBuyEquipment(int amount, String toBuy) {
		return (((amount * equipmentPrice.get(toBuy)) <= getMoney()) && ((equipment.get(toBuy).getValue() + amount) <= equipment.get(toBuy).getMax()));
	}

	public void buyEquipment(int amount, String toBuy) {
		if (mayBuyEquipment(amount, toBuy)) {
			setEquipment(toBuy, equipment.get(toBuy).getValue() + amount);
			setMoney(getMoney() - amount * equipmentPrice.get(toBuy));
		}
	}

	public boolean maySellEquipment(int amount, String toSell) {
		return (amount <= equipment.get(toSell).getValue());
	}


	public void sellEquipment(int amount, String toSell) {
		if (maySellEquipment(amount, toSell)) {
			setEquipment(toSell, equipment.get(toSell).getValue() - amount);
			setMoney(getMoney() + amount * equipmentPrice.get(toSell));
		}
	}

	public boolean mayDropEquipment(int amount, String toDrop) {
		return maySellEquipment(amount, toDrop);
	}

	public void dropEquipment(int amount, String toDrop) {
		if (mayDropEquipment(amount, toDrop)) {
			setEquipment(toDrop, equipment.get(toDrop).getValue() - amount);
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
/*
    public void SaveState(Bundle outState) {
		outState.putInt(MONTH, getMonth());
		outState.putInt(MONEY, getMoney());
		for (String s: new String[] {CARGO, ENGINE, SHOTGUNS, ARMOR}) {
			outState.putInt(s, getEquipment(s));
		}
		for (String s: new String[] {PRODUCE, MUNITIONS, TEXTILES, MOONSHINE}) {
			outState.putInt(s, getCargo(s));
		}
	}

	public void RestoreState(Bundle savedInstanceState) {
		month.setValue(savedInstanceState.getInt(MONTH));
		setMoney(savedInstanceState.getInt(MONEY));
		for (String s: new String[] {CARGO, ENGINE, SHOTGUNS, ARMOR}) {
			setEquipment(s, savedInstanceState.getInt(s));
		}
		for (String s: new String[] {PRODUCE, MUNITIONS, TEXTILES, MOONSHINE}) {
			setCargo(s, savedInstanceState.getInt(s));
		}
	}
*/
}
