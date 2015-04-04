/**
 * 
 */
package de.dbimplementation.demo01;
import java.util.List;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPlainLink;
import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.atom.*;


/**
 * @author traenkle
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		HyperGraph graph = new HyperGraph("/location");
		
		
		/*
		String someObject = "Lorem ipsum";
		HGHandle handle1 = graph.add(someObject);
		HGHandle handle2 = graph.add(someObject);
		
		//System.out.println(graph.get(handle1));
		//System.out.println(((String) graph.get(handle1)).toUpperCase());
		System.out.println(hg.getOne(graph, hg.type(String.class)));
		System.out.println(hg.getAll(graph, hg.type(String.class)));
		
		String object2 = "dolor sit amet";
		HGHandle noDup1 = hg.assertAtom(graph, object2);
		HGHandle noDup2 = hg.assertAtom(graph, object2);  //trying hard to duplicate
		System.out.println("Are those two handles duplicates, i.e. two distinct handles? : " + (!noDup1.equals(noDup2)));
	
		String object3 = "dolor sit amet bla";
		HGHandle noDup3 = graph.add(object3);
		HGHandle noDup4 = graph.add(object3);  //trying hard to duplicate
		System.out.println("Are those two handles duplicates, i.e. two distinct handles? : " + (!noDup3.equals(noDup4)));
	
		HGHandle duplicateLink = graph.add(new HGPlainLink(handle1, handle2));
        List<HGHandle> dupsList = hg.findAll(graph, hg.link(handle1, handle2));
        System.out.println("querying for link returned that duplicate Link? :" + dupsList.contains(duplicateLink));
        */
	}

}
