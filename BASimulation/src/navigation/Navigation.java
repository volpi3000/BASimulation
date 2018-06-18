package navigation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import main.Enums.oType;
import main.Enums.orientation;
import main.Coordinate;
import main.Enums.direction;
import manhattan.Entrance;
import manhattan.Intersection;
import manhattan.Manhattan;
import manhattan.MapObject;
import manhattan.Street;

public class Navigation {

	List<Vertex> vertexes = new ArrayList<Vertex>();
	List<Edge> edges = new ArrayList<Edge>();
	Manhattan matrix;
	Map<String, Vertex> map = new HashMap<String, Vertex>();
	ArrayList<CachedNode> cache = new ArrayList<CachedNode>();

	public ArrayList<CachedNode> getCache() {
		return cache;
	}

	public Navigation(Manhattan matrix) {
		this.matrix = matrix;

		// fill Vertexes from Intersections
		int counter = 1;

		for (Intersection item : matrix.getIntersections()) {
			vertexes.add(new Vertex(counter + "", "Intersection" + counter, item));
			counter++;
		}
		// fill HashMap
		for (Vertex item2 : vertexes) {

			map.put(item2.getMapObject().getX() + ":" + item2.getMapObject().getY(), item2);
		}
		// fillEdges
		counter = 1;
		for (Street[] item : matrix.getStreets()) {
			Street s1 = item[0];
			Street s2 = item[item.length - 1];
			if (s1.getType() == oType.ENTRANCE || s2.getType() == oType.ENTRANCE) {
				continue;
			}
			// dirty aber schneller //für die suche muss hier noch in die richtige achse
			// verschoben werden
			int x1 = 0;
			int x2 = 0;
			int y1 = 0;
			int y2 = 0;
			if (s1.getOrientation() == orientation.H) {
				x1 = -1;
				x2 = 1;
			} else {
				y1 = -1;
				y2 = 1;
			}

			Vertex v1 = map.get((s1.getX() + x1) + ":" + (s1.getY() + y1));

			Vertex v2 = map.get((s2.getX() + x2) + ":" + (s2.getY() + y2));

			// find nulls

			if (v1 == null || v2 == null) {
				System.out.println("Fehler: ");
				System.out.println("v1: " + (s1.getX() + x1) + ":" + (s1.getY() + y1));
				System.out.println(v1.toString());
				System.out.println("v2: " + (s2.getX() + x2) + ":" + (s2.getY() + y2));
				System.out.println(v2.toString());
			}

			edges.add(new Edge(counter + "", v1, v2, 1));

			// beide richtungen
			edges.add(new Edge(counter + "", v2, v1, 1));
			counter++;

		}

	}

	public void preCache(Manhattan matrix) {
		// get List with all starting nodes
		ArrayList<Entrance> entranceList = matrix.getEntranceList();
		ArrayList<Intersection> intersectionsEntrance = new ArrayList<Intersection>();
		for (Entrance item : entranceList) {
			int rl = matrix.getRoadlength();
			int x = 0;
			int y = 0;
			switch (item.getDir()) {
			case NORTH:
				y = rl;
				break;
			case EAST:
				x = -(rl);
				break;
			case SOUTH:
				y = -(rl);
				break;
			case WEST:
				x = rl;
				break;
			}
			// wir nehmen hier einfach mal das das funktioniert, sonst cast böse
			intersectionsEntrance.add((Intersection) matrix.map[item.getX() + x][item.getY() + y]);
		}

		System.out.println("Started Caching ...");
		// calculate cache
		for (Intersection item : intersectionsEntrance) {
			Vertex x = map.get(item.getX() + ":" + item.getY());
			Graph graph = new Graph(vertexes, edges);
			cache.add(new CachedNode(x.getMapObject(), graph, x));
		}
		System.out.println("Caching Done");

	}

	public LinkedList<Vertex> getInstructions(Intersection from, Intersection to) {
		CachedNode node = getCachedNode(from);

		// HIER MUSS GEARBEITET WERDEN

		if (from == to) {
			LinkedList<Vertex> path = new LinkedList<Vertex>();
			path.add(map.get(from.getLoc().toString()));
			return path;
		}

		return node.dijkstra.getPath(map.get(to.getX() + ":" + to.getY()));
	}

	private CachedNode getCachedNode(Intersection currentTarget) {

		for (CachedNode item : cache) {
			if (item.intersection == currentTarget) {
				return item;
			}
		}
		System.out.println("Cached Node Error");
		return null;
	}

	public direction getNewDirection(Coordinate position, Coordinate nextLoc) {
		Coordinate diff = position.difference(nextLoc);

		if (diff.getX() == 0) {
			if (diff.getY() > 0) {
				return direction.NORTH;
			}
			return direction.SOUTH;
		}
		if (diff.getY() == 0) {
			if (diff.getX() > 0) {
				return direction.WEST;
			}
			return direction.EAST;
		}

		return null;
	}

}
