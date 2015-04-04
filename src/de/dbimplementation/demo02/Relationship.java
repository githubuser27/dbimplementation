/**
 * 
 */
package de.dbimplementation.demo02;

/**
 * @author traenkle
 *
 */
public class Relationship {

	private Relation relation;
	
	@Override
	public String toString() {
		return "Relationship [relation=" + relation + "]";
	}

	public Relation getRelation() {
		return relation;
	}

	public void setRelation(Relation relation) {
		this.relation = relation;
	}

	public Relationship (Relation relation) {
		this.relation = relation;
	}
	
	public Relationship () {
		
	}

	public enum Relation{
		related,
		married,
		none
	}
}
