package navigation;

import manhattan.Intersection;

public class CachedNode {
	
	public final Intersection intersection;
	public final Graph graph;
	DijkstraAlgorithm dijkstra;
	
	
	public CachedNode(Intersection intersection, Graph graph, Vertex node) {
		super();
		this.intersection = intersection;
		this.graph = graph;
		dijkstra = new DijkstraAlgorithm(graph);
		dijkstra.execute(node);
	}
	
	

}
