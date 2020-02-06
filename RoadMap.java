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

class Box{
	public Box(String labelOfNode){
		label = labelOfNode;
		working = -1;
		route = new ArrayList<Vertex>();
	}
	public void updateWorking(int newValue){
		if (newValue < working || working  == -1){
			working = newValue;
		}
	}
	public void setStart(ArrayList<Vertex> v){
		route.clear();
		for (Vertex i : v){
			route.add(i);
		}
	}
	public void addToRoute(Vertex v){
		route.add(v);
	}
	public ArrayList<Vertex> getRoute(){
		return route;
	}
	public int getWorking(){
		return working;
	}
	public String getInfo(){
		String w_str = String.valueOf(working);
		String outRoute = "";

		for (Vertex i : route){
			outRoute = (outRoute + i.getName());
		}
		if (w_str.length() < 2){
			w_str += " ";
		}
		return(label+"  "+w_str+"  "+outRoute);
	}
	private String label;
	private int working;
	private ArrayList<Vertex> route;
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

		}else{
		// Add your code here
			ArrayList<Box> box = new ArrayList<Box>(); //initalise an array of dijkstra boxes

			for(Vertex i : places){
				Box x = new Box(i.getName());
				box.add(x); //fills array woith a differnt box for each node

			}
			box.get(startVertex.getIndex()).updateWorking(0); //also being the first it has a cost of 0
			//Dijkstras is now set u, box for each node and the starting point set
			ArrayList<Vertex> nodes = new ArrayList<Vertex>(); //then itialises an array list for nodes
			ArrayList<Vertex> visited = new ArrayList<Vertex>(); //to vist and nodes that have been visted
			nodes.add(startVertex); //adds the start vertex to the nodes to vist array
			box.get(startVertex.getIndex()).setStart(nodes); //current sets the path of the start to just the start

			while(nodes.size()> 0){ //while there are still nodes in the nodes to vist array it will continue
				box_print(box);
				int lowestIndex = findLowest(nodes,box); //passes the list of nodes through to find lowest function
				Vertex lowestNode = nodes.remove(lowestIndex); //which returns an index of the node next in the order
				visited.add(lowestNode); //this is then taken of the stack and becomes the node used this loop

				ArrayList<Edge> Roads = lowestNode.getIncidentRoads(); //then will get the edges conected to the node
				 for (Edge r : roads){
					 int length = r.getLength(); //then iterating along these roads and find the destionation node
					 Vertex destination = r.getSecondVertex(); //alongwith the cost of the edge to get to it

					 //Long line of code here but basically if the lowest node's cost + the cost of the arc is less
 					//then the destinations current working value then it lowers the destinations working value.
					if (box.get(lowestNode.getIndex()).getWorking()+length <  box.get(destination.getIndex()).getWorking() || box.get(destination.getIndex()).getWorking() == -1){
						box.get(destination.getIndex()).updateWorking(box.get(lowestNode.getIndex()).getWorking()+length);
					//This update the routes for the node, it takes the previous node that was of the lowest
					//working value and then changes the given nodes route to be the same as the route above it
					//plus then itself. This way it follows dijkstras.
					box.get(destination.getIndex()).setStart(box.get(lowestNode.getIndex()).getRoute());
					box.get(destination.getIndex()).addToRoute(destination);
					}
					if((nodes.contains(destination) == false && visited.contains(destination) == false) && destination.hasChargingStation() == true){
						nodes.add(destination);
					}
				 }
			}
			box_print(box);															//This is just for debugging
			int cost = box.get(endVertex.getIndex()).getWorking();					//but is also can been used to show
			System.out.println("Shortest path has a cost of "+String.valueOf(cost));	//all the infomation from the dijkstra

			path = box.get(endVertex.getIndex()).getRoute();
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

		private int findLowest(ArrayList<Vertex> nodes, ArrayList<Box> box){
			//Used in the dijkstras it finds the node next in the visited order
			int min = -1;
			Vertex lowest = null;
			for (Vertex i : nodes){
				if (box.get(i.getIndex()).getWorking() < min || min == -1){  //it tests all of the nodes the one with the lowest
					lowest = i;												  //working value is found. The default value at the start is -1
					min = box.get(i.getIndex()).getWorking();				  //this way the first value checked will always become the lowest node
				}															  //after that its only updated then a node of lower cost is found.
			}
			return nodes.indexOf(lowest); //then it returns the index of that node.
		}

		private void debug_print(String label, ArrayList<Vertex> nodes){
			System.out.print(label+" ");
			for(Vertex V : nodes){					//When passed a list of nodes just shows their label
				System.out.print(V.getName()+" ");	//Used for debugging when designing the algorithm.
			}
			System.out.println(" ");
		}

		private void box_print(ArrayList<Box> NumbBoxes){
			System.out.println("Current Boxes");
			for (Box i : NumbBoxes){			//similar to debug_print this is for debugging
				System.out.println(i.getInfo()); //pass the array containing the dijkstra boxes
			}										//and it will print all the infomation about the nodes.
			System.out.println("");
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
