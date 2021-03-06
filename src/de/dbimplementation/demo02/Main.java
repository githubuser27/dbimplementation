/**
 * 
 */
package de.dbimplementation.demo02;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HGHandleHolder;
import org.hypergraphdb.HGSearchResult;
import org.hypergraphdb.HGValueLink;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.query.And;
import org.hypergraphdb.query.AtomPartCondition;
import org.hypergraphdb.query.AtomTypeCondition;
import org.hypergraphdb.query.ComparisonOperator;
import org.hypergraphdb.query.HGQueryCondition;

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
		HyperGraph graph = setup();

		queryAllPersons(graph);

		queryAllRelationships(graph);

		queryAllFriendships(graph);

		queryAllMarriages(graph);

		queryAllRelativesOfName(graph, "Lukas");

		queryAllFriendshipsBetweenPersonsKnowingName(graph, "Lukas");

		graph.close();
	}

	/**
	 * @return
	 */
	private static HyperGraph setup() {
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
		graph.add(link14);
		return graph;
	}

	/**
	 * @param graph
	 * @param name
	 */
	private static void queryAllFriendshipsBetweenPersonsKnowingName(
			HyperGraph graph, String name) {
		System.out.println("\nAll Friendships between Persons knowing " + name + ":");

		// Finde Person
		HGQueryCondition condition = new And(
				new AtomTypeCondition(Person.class), 
				new AtomPartCondition(new String[]{"name"}, new String(name), ComparisonOperator.EQ)
				);
		HGSearchResult<HGHandle> rs = graph.find(condition);
		HGHandle handle = rs.next();

		// Finde alle Links zu Personen die diese Person kennen (= Friendship oder Relationship)
		List<HGValueLink> rs_list;
		rs_list = graph.getAll(
			hg.incident(handle)
			);
		
		// Sammle aus diesen Links die anderen Personen
		List<HGHandle> pers_handles = new ArrayList<HGHandle>();
		List<HGHandle> pers_handles2 = new ArrayList<HGHandle>();
		for (HGValueLink l: rs_list) {
			if (!l.getTargetAt(0).equals(handle)  && !pers_handles.contains(l.getTargetAt(0))) {
				pers_handles.add(l.getTargetAt(0));
				pers_handles2.add(l.getTargetAt(0));
			}
			else
				if (!pers_handles.contains(l.getTargetAt(1))) {
					pers_handles.add(l.getTargetAt(1));
					pers_handles2.add(l.getTargetAt(1));
				}
		}
		// Finde alle Friendships zwischen diesen Personen
		for (HGHandle h: pers_handles) {
			//System.out.println(graph.get(h));
			for (HGHandle h2: pers_handles2) {
				if (!h.equals(h2)) {
					List<HGValueLink> matches;
					matches = graph.getAll(
						hg.and(
							hg.incident(h),
							hg.incident(h2),
							hg.type(Friendship.class)
							)
						);
					for (HGValueLink m : matches) {
						System.out.println(m);
						System.out.println("\t" + graph.get(m.getTargetAt(0)) + " <--> " + graph.get(m.getTargetAt(1)));
						System.out.println("\tStatus: " + m.getValue());
					}
				}
			}
			pers_handles2.remove(h);
		}
		rs.close();
	}

	/**
	 * @param graph
	 * @param name
	 */
	private static void queryAllRelativesOfName(HyperGraph graph, String name) {
		System.out.println("\nAll relatives of " + name + ":");
		// Finde Person
		HGQueryCondition condition = new And(
				new AtomTypeCondition(Person.class), 
				new AtomPartCondition(new String[]{"name"}, new String(name), ComparisonOperator.EQ)
				);
		HGSearchResult<HGHandle> rs = graph.find(condition);
		// Finde alle Verwandtschaften-Links, an denen diese Person beteiligt ist
		List<HGValueLink> rs_list;
		rs_list = graph.getAll(
				hg.and(
						hg.type(Relationship.class),
						hg.eq("relation", Relation.related),
						hg.incident(rs.next())
						)
				);
		// Andere an Verwandtschaft beteiligte Person ausgeben
		for (HGValueLink r : rs_list) {
			Person pers = graph.get(r.getTargetAt(0));
			if (!pers.getName().equals(name))
				System.out.println(pers);
			else
				System.out.println((Person)graph.get(r.getTargetAt(1)));

		}
		rs.close();
	}

	/**
	 * @param graph
	 */
	private static void queryAllMarriages(HyperGraph graph) {
		List<HGValueLink> rs_list;
		System.out.println("\nAll marriages:");
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
	}

	/**
	 * @param graph
	 */
	private static void queryAllFriendships(HyperGraph graph) {
		List<HGValueLink> fs_list;
		System.out.println("\nAll Friendships:");
		fs_list = graph.getAll(
				hg.type(Friendship.class)
				);
		for (HGValueLink f : fs_list) {
			System.out.println(f);
			System.out.println("\t" + graph.get(f.getTargetAt(0)) + " <--> " + graph.get(f.getTargetAt(1)));
			System.out.println("\tStatus: " + f.getValue());
		}
	}

	/**
	 * @param graph
	 */
	private static void queryAllRelationships(HyperGraph graph) {
		List<HGValueLink> rs_list;
		System.out.println("\nAll Relationships in the graph:");
		rs_list = graph.getAll(
				hg.type(Relationship.class)
				);
		for (HGValueLink r : rs_list) {
			System.out.println(r);
			System.out.println("\t" + graph.get(r.getTargetAt(0)) + " <--> " + graph.get(r.getTargetAt(1)));
			System.out.println("\tStatus: " + r.getValue());
		}
	}

	/**
	 * @param graph
	 */
	private static void queryAllPersons(HyperGraph graph) {
		List<Person> pers_list;
		System.out.println("\nAll Persons in the graph:");
		pers_list = graph.getAll(
				hg.type(Person.class)
				);
		for (Person p : pers_list) {
			System.out.println(p);
		}
	}

}
