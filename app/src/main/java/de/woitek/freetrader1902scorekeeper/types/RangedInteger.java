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

	protected Integer setValue(Integer v) {
		Integer oldValue = value;
		v = Math.max(low, v);
		v = Math.min(v, high);
		value = v;
		return oldValue;
	}

	protected Integer getValue() {
		return value;
	}

	protected Integer incValue() {
		return setValue(getValue() + 1);
	}

	/*
		protected Integer incBy(Integer inc) {
			return setValue(getValue() + inc);
		}

		protected Integer decValue() {
			return setValue(getValue() - 1);
		}

		public Integer decBy(Integer dec) {
			return setValue(getValue() + dec);
		}
	*/
	public int getMax() {
		return high;
	}
}
