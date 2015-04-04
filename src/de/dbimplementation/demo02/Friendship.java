/**
 * 
 */
package de.dbimplementation.demo02;

/**
 * @author traenkle
 *
 */
public class Friendship {
	// 0: bad 10: best
	int value;
	
	public Friendship (int value) {
		this.value = value;
	}
	
	public Friendship() {
		
	}

	@Override
	public String toString() {
		return "Friendship [value=" + value + "]";
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
