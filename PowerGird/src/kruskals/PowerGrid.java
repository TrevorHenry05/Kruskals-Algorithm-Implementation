package kruskals;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;
/**
 * A class that performs the Kruskal's Algorithm on a SimpleGrpah
 * 
 * @author thenry04
 * @version 1.0
 */
public class PowerGrid {
	//class for uptrees that stores the parent of the vertex and its rank
	static class Uptree {
		Vertex parent;
		int rank;
	};
	//Class for edges that implement Comaparable so we can use a minHeap
	static class Edges implements Comparable<Edges> {
		Edge edge;
		
		public int compareTo(Edges compareEdge) {
			return (int)((double)this.edge.getData() - (double)compareEdge.edge.getData());
		}
	};
	
	//main method
	public static void main(String[] args) {
		SimpleGraph sg = new SimpleGraph();
		
		Set<Edge> result = kruskal(sg);
		double weight = 0;
		
		System.out.println("List of Edges in the Minimum Spanning Tree: ");
		for(Edge e : result) {
			weight += (double) e.getData();
			System.out.println((String)e.getFirstEndpoint().getName() + "---" + (String)e.getSecondEndpoint().getName());
		}
		System.out.println("Weight of the Minimum Spanning Tree: " + weight);
	}
	
	public static  Set<Edge> kruskal(SimpleGraph theGraph) {
		Hashtable<String, Vertex> uptrees =  GraphInput.LoadSimpleGraph(theGraph);
		Set<Edge> result = new HashSet<Edge>(theGraph.numVertices() - 1);
		PriorityQueue<Edges> minHeap = new PriorityQueue<Edges>();
		//make HashTable of singleton Uptrees for each node
		Iterator<Vertex> vertices = theGraph.vertices();
		while(vertices.hasNext()) {
			Uptree ut = new Uptree();
			ut.parent = vertices.next();
			ut.rank = 0;
			uptrees.get(ut.parent.getName()).setData(ut);
		}
		//sort edges in minHeap from smallest to largest weight
		Iterator<Edge> edges = theGraph.edges();
		while(edges.hasNext()) {
			Edges edge = new Edges();
			edge.edge = edges.next();
			//edges.remove();
			minHeap.add(edge);
		}
		
		int eNum = 0;
		Edges edge;
		Vertex start, end;
		while(!minHeap.isEmpty()) {
			edge = minHeap.poll();
			//stop once |V| - 1 edges have been added to the spanning tree
			if(eNum == (theGraph.numVertices() - 1)) {
				break;
			}
			//get both vertex of the edge
			start = uptrees.get(edge.edge.getFirstEndpoint().getName());
			end = uptrees.get(edge.edge.getSecondEndpoint().getName());
			//if edge vertices don't have the same root vertex then add the edge to the result and do a union of the two Uptrees of each edge vertex
			if(find(uptrees, start) != find(uptrees, end)) {
				result.add(edge.edge);
				union(start , end, uptrees);
				eNum++;
			}
		}
		
		return result;
	}
	
	private static Vertex find(Hashtable<String, Vertex> uptrees, Vertex x) {
		if(((Uptree) uptrees.get(x.getName()).getData()).parent != x) {
			((Uptree) (uptrees.get(x.getName()).getData())).parent = find(uptrees, ((Uptree) (uptrees.get(x.getName()).getData())).parent);
		}
		return ((Uptree) (uptrees.get(x.getName()).getData())).parent;
	}
	
	private static void union(Vertex x, Vertex y, Hashtable<String, Vertex> uptrees) {
		Vertex xRoot = find(uptrees, x);
		Vertex yRoot = find(uptrees, y);
		if(xRoot == yRoot) {
			return;
		}
		
		if(((Uptree) (uptrees.get(xRoot.getName()).getData())).rank > ((Uptree) (uptrees.get(yRoot.getName()).getData())).rank)  {
			((Uptree) uptrees.get(yRoot.getName()).getData()).parent = xRoot;
		} else if (((Uptree) (uptrees.get(xRoot.getName()).getData())).rank < ((Uptree) (uptrees.get(yRoot.getName()).getData())).rank) {
			((Uptree) (uptrees.get(xRoot.getName()).getData())).parent = yRoot;
		} else {
			((Uptree) (uptrees.get(xRoot.getName()).getData())).parent = yRoot;
			((Uptree) (uptrees.get(yRoot.getName()).getData())).rank += 1;
		}
	}
}
