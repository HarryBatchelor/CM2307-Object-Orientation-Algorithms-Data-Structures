/* Put your student number here
 * C1816377
 * Optionally, if you have any comments regarding your submission, put them here.
 * For instance, specify here if your program does not generate the proper output or does not do it in the correct manner.
 */

import java.util.*;
import java.io.*;

class Vertex {

	// Constructor: set name, chargingStation and index according to given values,
	// initilaize incidentRoads as empty array
	public Vertex(String placeName, boolean chargingStationAvailable, int idx) {
		name = placeName;
		incidentRoads = new ArrayList<Edge>();
		index = idx;
		chargingStation = chargingStationAvailable;
		path = new ArrayList<Vertex>();
		working = -1;

	}

	public String getName() {
		return name;
	}

	public boolean hasChargingStation() {
		return chargingStation;
	}

	public ArrayList<Edge> getIncidentRoads() {
		return incidentRoads;
	}

	// Add a road to the array incidentRoads
	public void addIncidentRoad(Edge road) {
		incidentRoads.add(road);
	}

	public int getIndex() {
		return index;
	}

	private String name; // Name of the place
	private ArrayList<Edge> incidentRoads; // Incident edges
	private boolean chargingStation; // Availability of charging station
	private int index; // Index of this vertex in the vertex array of the map
	private ArrayList<Vertex> path;
	private int working;
}

class Edge {
	public Edge(int roadLength, Vertex firstPlace, Vertex secondPlace) {
		length = roadLength;
		incidentPlaces = new Vertex[] { firstPlace, secondPlace };
	}

	public Vertex getFirstVertex() {
		return incidentPlaces[0];
	}

	public Vertex getSecondVertex() {
		return incidentPlaces[1];
	}

	public int getLength() {
		return length;
	}

	private int length;
	private Vertex[] incidentPlaces;
}

// A class that represents a sparse matrix
public class RoadMap {

	// Default constructor
	public RoadMap() {
		places = new ArrayList<Vertex>();
		roads = new ArrayList<Edge>();
	}

	// Auxiliary function that prints out the command syntax
	public static void printCommandError() {
		System.err.println("ERROR: use one of the following commands");
		System.err.println(" - Read a map and print information: java RoadMap -i <MapFile>");
		System.err.println(
				" - Read a map and find shortest path between two vertices with charging stations: java RoadMap -s <MapFile> <StartVertexIndex> <EndVertexIndex>");
	}

	public static void main(String[] args) throws Exception {
		if (args.length == 2 && args[0].equals("-i")) {
			RoadMap map = new RoadMap();
			try {
				map.loadMap(args[1]);
			} catch (Exception e) {
				System.err.println("Error in reading map file");
				System.exit(-1);
			}

			System.out.println("Read road map from " + args[1] + ":");
			map.printMap();
		} else if (args.length == 4 && args[0].equals("-s")) {
			RoadMap map = new RoadMap();
			map.loadMap(args[1]);
			System.out.println("Read road map from " + args[1] + ":");
			map.printMap();

			int startVertexIdx = -1, endVertexIdx = -1;
			try {
				startVertexIdx = Integer.parseInt(args[2]);
				endVertexIdx = Integer.parseInt(args[3]);
			} catch (NumberFormatException e) {
				System.err.println("Error: start vertex and end vertex must be specified using their indices");
				System.exit(-1);
			}

			if (startVertexIdx < 0 || startVertexIdx >= map.numPlaces()) {
				System.err.println("Error: invalid index for start vertex");
				System.exit(-1);
			}

			if (endVertexIdx < 0 || endVertexIdx >= map.numPlaces()) {
				System.err.println("Error: invalid index for end vertex");
				System.exit(-1);
			}

			Vertex startVertex = map.getPlace(startVertexIdx);
			Vertex endVertex = map.getPlace(endVertexIdx);
			if (!map.isConnectedWithChargingStations(startVertex, endVertex)) {
				System.out.println();
				System.out.println("There is no path connecting " + map.getPlace(startVertexIdx).getName() + " and "
						+ map.getPlace(endVertexIdx).getName() + " with charging stations");
			} else {
				ArrayList<Vertex> path = map.shortestPathWithChargingStations(startVertex, endVertex);
				System.out.println();
				System.out.println("Shortest path with charging stations between " + startVertex.getName() + " and "
						+ endVertex.getName() + ":");
				map.printPath(path);
			}

		} else {
			printCommandError();
			System.exit(-1);
		}
	}

	// Load matrix entries from a text file
	public void loadMap(String filename) {
		File file = new File(filename);
		places.clear();
		roads.clear();

		try {
			Scanner sc = new Scanner(file);

			// Read the first line: number of vertices and number of edges
			int numVertices = sc.nextInt();
			int numEdges = sc.nextInt();

			for (int i = 0; i < numVertices; ++i) {
				// Read the vertex name and its charing station flag
				String placeName = sc.next();
				int charginStationFlag = sc.nextInt();
				boolean hasChargingStataion = (charginStationFlag == 1);

				// Add your code here to create a new vertex using the information above and add
				// it to places
				Vertex v = new Vertex(placeName,hasChargingStataion,i);
				places.add(v);
			}

			for (int j = 0; j < numEdges; ++j) {
				// Read the edge length and the indices for its two vertices
				int vtxIndex1 = sc.nextInt();
				int vtxIndex2 = sc.nextInt();
				int length = sc.nextInt();
				Vertex vtx1 = places.get(vtxIndex1);
				Vertex vtx2 = places.get(vtxIndex2);

				// Add your code here to create a new edge using the information above and add
				// it to roads
				// You should also set up incidentRoads for each vertex
				Edge e = new Edge(length, vtx1, vtx2);
				roads.add(e);
				vtx1.addIncidentRoad(e);
				Edge e2 = new Edge(length, vtx2, vtx1);
				vtx2.addIncidentRoad(e2);

			}

			sc.close();

			// Add your code here if approparite
		} catch (Exception e) {
			e.printStackTrace();
			places.clear();
			roads.clear();
		}
	}

	// Return the shortest path between two given vertex, with charging stations on
	// each itermediate vertex.
	public ArrayList<Vertex> shortestPathWithChargingStations(Vertex startVertex, Vertex endVertex) {

		// Initialize an empty path
		ArrayList<Vertex> path = new ArrayList<Vertex>();

		// Sanity check for the case where the start vertex and the end vertex are the
		// same
		if (startVertex.getIndex() == endVertex.getIndex()) {
			path.add(startVertex);
			return path;
		}

		// Add your code here
		int[] distance = new int[places.size()]; //List to store distances of places from start vertex
		ArrayList<Integer> visited = new ArrayList<Integer>();
		PriorityQ<VertexDistance> priorityQ = new PriorityQ<VertexDistance>(places.size(), new VertexDistance());

		for (int i=0; i < places.size(); i++){ //For each index, set the value to infinite as a placeholder
		distance[i] = Integer.MAX_VALUE; //We can check if new distance is less than old, should be less tan infinite
	}
		priorityQ.add(new VertexDistance(startVertex.getIndex(),0)); //add the start vertex to the priority queue always in position 0
		distance[startVertex.getIndex()] = 0; //distance from the start vertex to itself is always 0

		while(visited.size() != places.size()){ //while there are still places left to vist
			int minimumDistanceVertex = priorityQ.remove().VertexIndex; //Remove the minimum distance vertex from the front of the priority queue
			visited.add(minimumDistanceVertex); //Add the minium distance to the list of vertexes

			int edgeDistance = -1; //initialise distance between vertexrs as null values
			int newDistance = -1;

			ArrayList<Edge> adjRoads = places.get(minimumDistanceVertex).getIncidentRoads(); //get a list of all the roads to conected to current vertex
			ArrayList<Integer[]> adjVertex = new ArrayList<Integer[]>();

			for (Edge eachRoad: adjRoads){
				//checking for points with charging stations with raods going each way
				//if the road goes from out current vertex to another, add the index to the list to get to the vertex
				if(eachRoad.getFirstVertex().getIndex() == minimumDistanceVertex){
					Integer[]tempArray = {eachRoad.getSecondVertex().getIndex(), eachRoad.getLength()};
					adjVertex.add(tempArray); //store vertex index and distance to it from current vertex
				}
				//if the road does not go to the vertex it must already be at the vertex
				else{
					Integerp[]tempArray = {eachRoad.getFirstVertex().getIndex(), eachRoad.getLength()};
					adjVertex.add(tempArray);
				}

			}
			for(int i = 0; i<adjVertex.size(); i++){
				Integer[]currentV = adjVertex.get(i); //get the list of the neighbouring vertexes and loop through them

				if(!visited.contains(currentV[0])){ //if the current vertex has not been visited
					edgeDistance = currentV[1];
					newDistance = distance[minimumDistanceVertex] + edgeDistance; //add the distance to that vertex to that total distance
				}
				if(newDistance < distance[minimumDistanceVertex]){ //if the new distance is cheaper in cost
					distance[minimumDistanceVertex] = newDistance; //change for the distnace
				}
				priorityQ.add(new VertexDistance(currentV[0], distance[currentV[0]])); //save the current vertex and its distance from the start vertex
			}
		}

	return path;
}

	// Check if two vertices are connected by a path with charging stations on each itermediate vertex.
	// Return true if such a path exists; return false otherwise.
	// The worst-case time complexity of your algorithm should be no worse than O(v + e),
	// where v and e are the number of vertices and the number of edges in the graph.
	public boolean isConnectedWithChargingStations(Vertex startVertex, Vertex endVertex) {
		// Sanity check
		if (startVertex.getIndex() == endVertex.getIndex()) {
			return true;
		}

		// Add your code here

			if (startVertex.getIndex() == endVertex.getIndex()){
				return true;
			}
			boolean visited[] = new boolean[places.size()];

			LinkedList<Integer> queue = new LinkedList<Integer>();
			Vertex currentVertex = startVertex;
			visited[currentVertex.getIndex()] = true;
			queue.add(currentVertex.getIndex());

			while(queue.size()!=0){
				// dequeue a vertex from the queue
				currentVertex = places.get(queue.poll());
				ArrayList<Edge> currentIncedentRoads = currentVertex.getIncidentRoads();

				ArrayList<Integer> tempInterger = new ArrayList<Integer>();
				for(Edge eachRoad: currentIncedentRoads){
					//checking if adjacent road is a destination
						if(eachRoad.getFirstVertex() == endVertex || eachRoad.getSecondVertex() == endVertex){
							return true;
						}
						//checking points with charging stations
						else if(eachRoad.getFirstVertex() == currentVertex && (eachRoad.getSecondVertex()).hasChargingStation()){
							tempInterger.add(eachRoad.getSecondVertex().getIndex());
						}
						else if (eachRoad.getSecondVertex() == currentVertex && (eachRoad.getFirstVertex()).hasChargingStation()) {
							tempInterger.add(eachRoad.getFirstVertex().getIndex());
						}

				}

				Iterator<Integer> i = tempInterger.iterator();
				//n is the current vertex being checked
				while(i.hasNext()){
					int n = i.next();
					if(!visited[n]){
						visited[n] = true;
						queue.add(n);
					}
				}
			}
			return false;
		}




	public void printMap() {
		System.out.println("The map contains " + this.numPlaces() + " places and " + this.numRoads() + " roads");
		System.out.println();

		System.out.println("Places:");

		for (Vertex v : places) {
			System.out.println("- name: " + v.getName() + ", charging station: " + v.hasChargingStation());
		}

		System.out.println();
		System.out.println("Roads:");

		for (Edge e : roads) {
			System.out.println("- (" + e.getFirstVertex().getName() + ", " + e.getSecondVertex().getName()
					+ "), length: " + e.getLength());
		}
	}

	public void printPath(ArrayList<Vertex> path) {
		System.out.print("(  ");

		for (Vertex v : path) {
			System.out.print(v.getName() + "  ");
		}

		System.out.println(")");
	}

	public int numPlaces() {
		return places.size();
	}

	public int numRoads() {
		return roads.size();
	}

	public Vertex getPlace(int idx) {
		return places.get(idx);
	}

	private ArrayList<Vertex> places;
	private ArrayList<Edge> roads;
}
