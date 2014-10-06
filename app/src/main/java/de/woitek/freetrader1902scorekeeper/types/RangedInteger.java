package de.woitek.freetrader1902scorekeeper.types;

public class RangedInteger {
	private Integer low;
	private Integer high;
	private Integer value;

	public RangedInteger(Integer low, Integer high, Integer value) {
		this.low = low;
		this.high = high;
		this.value = value;
	}

	public Integer setValue(Integer v) {
		Integer oldValue = value;
		v = Math.max(low, v);
		v = Math.min(v, high);
		value = v;
		return oldValue;
	}

	public Integer getValue() {
		return value;
	}

	public Integer incValue() {
		return setValue(getValue() + 1);
	}

	public int getMax() {
		return high;
	}
}
