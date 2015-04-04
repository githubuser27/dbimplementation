/**
 * 
 */
package de.dbimplementation.demo02;

/**
 * @author traenkle
 *
 */
public class Person {
	
	private String name;
	
	public Person (String name) {
		this.name = name;
	}
	
	public Person(){
		this.name = "unknown";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + "]";
	}
	

}
