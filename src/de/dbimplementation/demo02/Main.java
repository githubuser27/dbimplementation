/**
 * 
 */
package de.dbimplementation.demo02;

import java.awt.print.Book;
import java.io.File;
import java.util.Date;
import java.util.List;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HGSearchResult;
import org.hypergraphdb.HGValueLink;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.query.And;
import org.hypergraphdb.query.AtomPartCondition;
import org.hypergraphdb.query.AtomTypeCondition;
import org.hypergraphdb.query.ComparisonOperator;
import org.hypergraphdb.query.HGQueryCondition;
import org.hypergraphdb.query.IncidentCondition;

import de.dbimplementation.demo02.Relationship.Relation;

/**
 * @author traenkle
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long rand = new Date().getTime();
		File dir = new File("/location/" + rand);
		dir.mkdir();
		HyperGraph graph = new HyperGraph("/location/" + rand);
		
		HGHandle handleLu = graph.add(new Person("Lukas"));
		HGHandle handleTo = graph.add(new Person("Tobi"));
		HGHandle handleSch = graph.add(new Person("Schwester"));
		HGHandle handleVa = graph.add(new Person("Vater"));
		HGHandle handleMu = graph.add(new Person("Mutter"));
		HGHandle handleTBr = graph.add(new Person("Tobi_Bruder"));
		HGHandle handleMa = graph.add(new Person("Matze"));
		HGHandle handleFr = graph.add(new Person("Fremder"));
		
		HGValueLink link1 = new HGValueLink(new Relationship(Relation.related), handleLu, handleVa);
		HGValueLink link2 = new HGValueLink(new Relationship(Relation.related), handleLu, handleMu);
		HGValueLink link3 = new HGValueLink(new Relationship(Relation.married), handleVa, handleMu);
		HGValueLink link4 = new HGValueLink(new Relationship(Relation.related), handleLu, handleSch);
		HGValueLink link5 = new HGValueLink(new Relationship(Relation.related), handleTo, handleTBr);
		HGValueLink link6 = new HGValueLink(new Friendship(5), handleLu, handleMa);
		HGValueLink link7 = new HGValueLink(new Friendship(7), handleLu, handleTo);
		HGValueLink link8 = new HGValueLink(new Friendship(9), handleLu, handleSch);
		HGValueLink link9 = new HGValueLink(new Friendship(9), handleTBr, handleFr);
		HGValueLink link10 = new HGValueLink(new Friendship(2), handleTo, handleFr);
		HGValueLink link11 = new HGValueLink(new Friendship(4), handleSch, handleFr);
		HGValueLink link12 = new HGValueLink(new Relationship(Relation.related), handleSch, handleVa);
		HGValueLink link13 = new HGValueLink(new Relationship(Relation.related), handleSch, handleMu);
		HGValueLink link14 = new HGValueLink(new Friendship(2), handleVa, handleMa);
		graph.add(link1);
		graph.add(link2);
		graph.add(link3);
		graph.add(link4);
		graph.add(link5);
		graph.add(link6);
		graph.add(link7);
		graph.add(link8);
		graph.add(link9);
		graph.add(link10);
		graph.add(link11);
		graph.add(link12);
		graph.add(link13);
		
		List<Person> pers_list;
		List<HGValueLink> rs_list;
		List<HGValueLink> fs_list;
		
		System.out.println("\nAlle Personen:");
		pers_list = graph.getAll(
				hg.type(Person.class)
				);
		for (Person p : pers_list) {
			System.out.println(p);
		}
		
		System.out.println("\nAlle Verwandtschaften:");
		rs_list = graph.getAll(
				hg.type(Relationship.class)
				);
		for (HGValueLink r : rs_list) {
			System.out.println(r);
			System.out.println("\t" + graph.get(r.getTargetAt(0)) + " <--> " + graph.get(r.getTargetAt(1)));
			System.out.println("\tStatus: " + r.getValue());
		}
		
		System.out.println("\nAlle Freundschaften:");
		fs_list = graph.getAll(
				hg.type(Friendship.class)
				);
		for (HGValueLink f : fs_list) {
			System.out.println(f);
			System.out.println("\t" + graph.get(f.getTargetAt(0)) + " <--> " + graph.get(f.getTargetAt(1)));
			System.out.println("\tStatus: " + f.getValue());
		}
		
		System.out.println("\nAlle Verheirateten:");
		rs_list = graph.getAll(
				hg.and(
						hg.type(Relationship.class),
						hg.eq("relation", Relation.married)
						)
				);
		for (HGValueLink r : rs_list) {
			System.out.println(r);
			System.out.println("\t" + graph.get(r.getTargetAt(0)) + " <--> " + graph.get(r.getTargetAt(1)));
			System.out.println("\tStatus: " + r.getValue());
		}
		
		System.out.println("\nAlle, die mit Lukas verwandt sind:");
		HGQueryCondition condition = new And(
	              new AtomTypeCondition(Person.class), 
	              new AtomPartCondition(new String[]{"name"}, new String("Lukas"), ComparisonOperator.EQ)
	              );
	    HGSearchResult<HGHandle> rs = graph.find(condition);
	    HGQueryCondition condition2 = new And(
	              new AtomTypeCondition(Relationship.class), 
	              new AtomPartCondition(new String[]{"relation"}, Relation.related, ComparisonOperator.EQ),
	              new IncidentCondition(rs.next())
	              );
	    HGSearchResult<HGHandle> rs2 = graph.find(condition2);
	    while(rs2.hasNext()) {
	    	HGHandle match = rs2.next();
	    	System.out.println(graph.get(match));
	    	HGValueLink vl = graph.get(match);
	    	Relationship match_rs1 = graph.get(vl.getTargetAt(0));
	    	Relationship match_rs2 = graph.get(vl.getTargetAt(1));
	    	if match_rs1.
			//System.out.println("\t" + graph.get(r.getTargetAt(0)) + " <--> " + graph.get(r.getTargetAt(1)));
			//System.out.println("\tStatus: " + r.getValue());
			//if (r.getTargetAt(0).equals(obj))
	    }
		/*rs_list = graph.getAll(
				hg.and(
						hg.type(Relationship.class),
						hg.eq("relation", Relation.related),
						hg.incident(rs.next())
						)
				);
		for (HGValueLink r : rs_list) {
			System.out.println(r);
			System.out.println("\t" + graph.get(r.getTargetAt(0)) + " <--> " + graph.get(r.getTargetAt(1)));
			System.out.println("\tStatus: " + r.getValue());
			if (r.getTargetAt(0).equals(obj))
		}*/
		rs.close();
		rs2.close();
		
		System.out.println("\nAlle Freundschaften zwischen Personen die mit Lukas verwandt oder befreundet sind:");
		condition = new And(
	              new AtomTypeCondition(Person.class), 
	              new AtomPartCondition(new String[]{"name"}, new String("Lukas"), ComparisonOperator.EQ)
	              );
	    rs = graph.find(condition);
		rs_list = graph.getAll(
				hg.and(
						hg.incident(rs.next())
						)
				);
		for (HGValueLink r : rs_list) {
			System.out.println(r);
			System.out.println("\t" + graph.get(r.getTargetAt(0)) + " <--> " + graph.get(r.getTargetAt(1)));
			System.out.println("\tStatus: " + r.getValue());
		}
		rs.close();
		
		
		graph.close();
	}

}
