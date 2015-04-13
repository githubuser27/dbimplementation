/**
 * 
 */
package de.dbimplementation.demo03;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPlainLink;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HGSearchResult;
import org.hypergraphdb.HGValueLink;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.query.And;
import org.hypergraphdb.query.AtomTypeCondition;
import org.hypergraphdb.query.HGQueryCondition;
import org.hypergraphdb.query.IncidentCondition;
import org.omg.CORBA.portable.IndirectionException;

import de.dbimplementation.demo02.Person;

/**
 * @author sascha
 *
 */
/**
 * @author sascha
 *
 */
public class Main {

	static HyperGraph graph;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long rand = new Date().getTime();
		File dir = new File("/location/" + rand);
		dir.mkdir();
		graph = new HyperGraph("/location/" + rand);
		
		// Family A
		HGHandle handleAV = graph.add(new Person("AV"));
		HGHandle handleAM = graph.add(new Person("AM"));
		HGHandle handleAT1 = graph.add(new Person("AT1"));
		HGHandle handleAT2 = graph.add(new Person("AT2"));
		HGHandle handleAS1 = graph.add(new Person("AS1"));
		
		// Family B
		HGHandle handleBV = graph.add(new Person("BV"));
		HGHandle handleBM = graph.add(new Person("BM"));
		HGHandle handleBT1 = graph.add(new Person("BT1"));
		HGHandle handleBT2 = graph.add(new Person("BT2"));
		
		// Family C
		HGHandle handleCV = graph.add(new Person("CV"));
		HGHandle handleCM = graph.add(new Person("CM"));
		HGHandle handleCS1 = graph.add(new Person("CS1"));
		HGHandle handleCS2 = graph.add(new Person("CS2"));
		HGHandle handleCS3 = graph.add(new Person("CS3"));
		HGHandle handleCT1 = graph.add(new Person("CT1"));
		
		// Family D
		HGHandle handleDM = graph.add(new Person("DM"));
		HGHandle handleDT1 = graph.add(new Person("DT1"));

		// Link families with HGPlainLink
		graph.add(new HGPlainLink(handleAV, handleAM, handleAT1, handleAT2, handleAS1));
		graph.add(new HGPlainLink(handleBV, handleBM, handleBT1, handleBT2));
		graph.add(new HGPlainLink(handleCV, handleCM, handleCS1, handleCS2, handleCS3, handleCT1));
		graph.add(new HGPlainLink(handleDM, handleDT1));
		/*
		graph.add(new HGValueLink("FamilyA", handleAV, handleAM, handleAT1, handleAT2, handleAS1));
		graph.add(new HGValueLink("FamilyB", handleBV, handleBM, handleBT1, handleBT2));
		graph.add(new HGValueLink("FamilyC", handleCV, handleCM, handleCS1, handleCS2, handleCS3, handleCT1));
		graph.add(new HGValueLink("FamilyD", handleDM, handleDT1));
		*/
		
		// Link friendships with HGValueLink, value: int as years of friendship
		HGHandle th = graph.add(new HGValueLink(29, handleAV, handleBV));
		System.out.println(graph.get(th));
		graph.add(new HGValueLink(24, handleAV, handleCV));
		graph.add(new HGValueLink(11, handleAM, handleBM));
		graph.add(new HGValueLink(1, handleAT1, handleCS2));
		graph.add(new HGValueLink(8, handleAT2, handleCS2));
		graph.add(new HGValueLink(10, handleAT2, handleBT2));
		graph.add(new HGValueLink(4, handleAS1, handleBT1));
		graph.add(new HGValueLink(12, handleAS1, handleCS3));
		graph.add(new HGValueLink(0, handleBT1, handleDT1));
		graph.add(new HGValueLink(25, handleBM, handleDM));
		graph.add(new HGValueLink(6, handleCS1, handleDT1));
		graph.add(new HGValueLink(6, handleCS3, handleDT1));
		graph.add(new HGValueLink(5, handleCT1, handleDT1));
		
		// Print all persons in graph
		List<Person> pers_list;
		System.out.println("\nAll persons in the graph:");
		pers_list = graph.getAll(
				hg.type(Person.class)
				);
		for (Person p : pers_list) {
			System.out.println(p);
		}
		
		// Print all families
		System.out.println("\nAll families:");
		List<HGPlainLink> fam_list = graph.getAll(
				hg.type(HGPlainLink.class)
				);
		for (HGPlainLink pl : fam_list) {
			printFamily(pl);
		}
		
		// Print all friendships
		System.out.println("\nAll friendships:");
		List<HGValueLink> fs_list = graph.getAll(
				hg.type(Integer.class)
				);
		for (HGValueLink vl: fs_list) {
			printFriendship(vl);
		}
		
		// Print all 2nd-level contacts of CS3 from other families
		System.out.println("\nAll 2nd-lvl contacts of CS3 from other families:");
		HGHandle testHandle = handleCS3;
		HGHandle family = getFamily(testHandle);
		for(HGHandle lvl1: getFriendships(testHandle)) {
			System.out.println(graph.get(lvl1));
			HGHandle lvl1_p = getOtherSideOfFriendship(lvl1, testHandle);
			for(HGHandle lvl2: getFriendships(lvl1_p)) {
				HGHandle lvl2_p = getOtherSideOfFriendship(lvl2, lvl1_p);
				if(!lvl2_p.equals(testHandle)){
					for(HGHandle fam: getFamilyMembers(lvl2_p)) {
						if(!fam.equals(lvl2_p)) {
							System.out.println(graph.get(lvl2_p));
						}
					}
				}
			}
		}
	}
	
	/** Returns the other side of a friendship
	 * @param fs Friendship
	 * @param me The known one in the friendship
	 * @return The other (unknown) one in the friendship
	 */
	static HGHandle getOtherSideOfFriendship(HGHandle fs, HGHandle me) {
		HGValueLink fsl = graph.get(fs);
		if(me.equals(fsl.getTargetAt(0)))
				return fsl.getTargetAt(1);
		else
			return fsl.getTargetAt(0);
	}
	
	
	/** Returns the HGHandle to the HGPlainLink (=family) h belongs to
	 * @param h Person
	 * @return
	 */
	static HGHandle getFamily(HGHandle h) {
		HGQueryCondition condition = new And(
				new AtomTypeCondition(HGPlainLink.class), 
				new IncidentCondition(h)
				);
		HGSearchResult<HGHandle> rs = graph.find(condition);
		return rs.next();
	}
	
	/** Returns a list of HGHandles to the members of the family at h
	 * @param h Family
	 * @return
	 */
	static List<HGHandle> getFamilyMembers(HGHandle h) {
		List<HGHandle> fam = new ArrayList<HGHandle>();
		HGPlainLink famLink = graph.get(getFamily(h));
		for (int i = 0; i<famLink.getArity(); i++) {
			fam.add(graph.get(famLink.getTargetAt(i)));
		}
		return fam;
	}
	
	/** Returns a list of HGHandles to the HGValueLinks (=friendships) which contain h
	 * @param h Person
	 * @return
	 */
	static List<HGHandle> getFriendships(HGHandle h) {
		List<HGHandle> fs = new ArrayList<HGHandle>();
		HGQueryCondition condition = new And(
				new AtomTypeCondition(Integer.class), 
				new IncidentCondition(h)
				);
		HGSearchResult<HGHandle> rs = graph.find(condition);
		while(rs.hasNext())
			fs.add(rs.next());
		rs.close();
		return fs;
	}
	
	/** Prints all members of the family of pl
	 * @param pl Family link
	 */
	static void printFamily(HGPlainLink pl) {
		for (int i = 0; i<pl.getArity(); i++) {
			System.out.print(" - " + graph.get(pl.getTargetAt(i)));
		}
		System.out.println();
	}
	
	/** Prints all members of the friendship of vl
	 * @param vl Friendship link
	 */
	static void printFriendship(HGValueLink vl) {
		for (int i = 0; i<vl.getArity(); i++) {
			System.out.print(" - " + graph.get(vl.getTargetAt(i)));
		}
		System.out.println();
	}

}
