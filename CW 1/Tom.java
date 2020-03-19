
/* C1769261
 *
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

	public ArrayList<Vertex> getConnecting() {
		ArrayList<Vertex> connections = new ArrayList<Vertex>();
		ArrayList<Edge> arcs = this.getIncidentRoads();
		for (Edge arc : arcs){
			if (this != arc.getSecondVertex()){
				connections.add(arc.getSecondVertex());
			}else{
				connections.add(arc.getFirstVertex());
			}
		}

		return connections;

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

class DijkstraBox{
	public DijkstraBox(String labelOfNode){
		label    = labelOfNode;
		working  = -1;
		route    = new ArrayList<Vertex>();
	}

	public void updateWorking(int newValue){
		if (newValue < working || working == -1){
			working = newValue;
		}
	}

	public void setRouteStart(ArrayList<Vertex> v){
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

	public String getAllInfo(){
		String w_str = String.valueOf(working);
		String outRoute = "";

		for (Vertex i : route){
			outRoute = (outRoute + i.getName());
		}

		if (w_str.length() < 2){
			w_str += " ";
		}

		return (label+"  "+w_str+"  "+outRoute);
	}

	private String  label;
	private int working;
	private ArrayList<Vertex> route;
}

public class Tom {

	public Tom() {
		places = new ArrayList<Vertex>();
		roads = new ArrayList<Edge>();
	}

	public static void printCommandError() {
		System.err.println("ERROR: use one of the following commands");
		System.err.println(" - Read a map and print information: java Tom -i <MapFile>");
		System.err.println(" - Read a map and find shortest path between two vertices with charging stations: java Tom -s <MapFile> <StartVertexIndex> <EndVertexIndex>");
	}

	public static void main(String[] args) throws Exception {
		if (args.length == 2 && args[0].equals("-i")) {
			Tom map = new Tom();
			try {
				map.loadMap(args[1]);
			} catch (Exception e) {
				System.err.println("Error in reading map file");
				System.exit(-1);
			}

			System.out.println("Read road map from " + args[1] + ":");
			map.printMap();
		} else if (args.length == 4 && args[0].equals("-s")) {
			Tom map = new Tom();
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
				int chargingStationFlag = sc.nextInt();
				boolean hasChargingStation = (chargingStationFlag == 1);

				// Add your code here to create a new vertex using the information above and add
				// it to places
				Vertex v = new Vertex(placeName,hasChargingStation,i);
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

				Edge e = new Edge(length,vtx1,vtx2);
				roads.add(e);

				// You should also set up incidentRoads for each vertex

				vtx1.addIncidentRoad(e);

				Edge e2 = new Edge(length,vtx2,vtx1);

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

	public ArrayList<Vertex> shortestPathWithChargingStations(Vertex startVertex, Vertex endVertex) {

		// Initialize an empty path
		ArrayList<Vertex> path = new ArrayList<Vertex>();

		// Sanity check for the case where the start vertex and the end vertex are the same
		if (startVertex.getIndex() == endVertex.getIndex()) {
			path.add(startVertex);

		}else{

			//Start of Q3

			ArrayList<DijkstraBox> dijs = new ArrayList<DijkstraBox>();  //initilises an array of dijkstra boxes

			for (Vertex i : places){
				DijkstraBox x = new DijkstraBox(i.getName());
				dijs.add(x);    //then fills that array with a dijkstra box for each node
			}

			dijs.get(startVertex.getIndex()).updateWorking(0);  //also being the first it has a cost of 0

			//Dijkstra's is now setup, box for every node and the starting point set.
			ArrayList<Vertex> nodes   = new ArrayList<Vertex>(); //then ititalises an array list for nodes,
			ArrayList<Vertex> visited = new ArrayList<Vertex>(); //to visit and nodes that have been visted
			nodes.add(startVertex); //adds the start vertex to the nodes to visit array
			dijs.get(startVertex.getIndex()).setRouteStart(nodes); //current sets the path of the start to just the start



			while (nodes.size() > 0){	//while there are still nodes in the nodes to visit array it will continue
				dij_print(dijs);
				int lowestIndex = findLowest(nodes,dijs);		//passes the list of nodes through the find lowest function
				Vertex lowestNode = nodes.remove(lowestIndex);  //which returns an index of the node next in the order.
				visited.add(lowestNode);						//this is then taken of the stack and becomes the node used this loop

				ArrayList<Edge> roads = lowestNode.getIncidentRoads();  //then will get the edges connected to the node.
				for (Edge r : roads){
					int length = r.getLength();						//then iterating along these roads and find the destination node
					Vertex destination = r.getSecondVertex();		//alongwith the cost of the edge to get to it.


					//Long line of code here but basically if the lowest node's cost + the cost of the arc is less
					//then the destinations current working value then it lowers the destinations working value.
					if (dijs.get(lowestNode.getIndex()).getWorking()+length < dijs.get(destination.getIndex()).getWorking() || dijs.get(destination.getIndex()).getWorking() == -1){
						dijs.get(destination.getIndex()).updateWorking(dijs.get(lowestNode.getIndex()).getWorking()+length);

						//This update the routes for the node, it takes the previous node that was of the lowest
						//working value and then changes the given nodes route to be the same as the route above it
						//plus then itself. This way it follows dijkstras.
						dijs.get(destination.getIndex()).setRouteStart(dijs.get(lowestNode.getIndex()).getRoute());
						dijs.get(destination.getIndex()).addToRoute(destination);
					}

					if ((nodes.contains(destination) == false && visited.contains(destination) == false) && destination.hasChargingStation() == true){
						nodes.add(destination);
					}
				}
			}

			dij_print(dijs);															//This is just for debugging
			int cost = dijs.get(endVertex.getIndex()).getWorking();					//but is also can been used to show
			System.out.println("Shortest path has a cost of "+String.valueOf(cost));	//all the infomation from the dijkstra

			path = dijs.get(endVertex.getIndex()).getRoute();
		}
		return path;
	}

	public boolean isConnectedWithChargingStations(Vertex startVertex, Vertex endVertex) {
		// Sanity check
		if (startVertex.getIndex() == endVertex.getIndex()) {
			return true;
		}

		ArrayList<Vertex> nodes      = new ArrayList<Vertex>();	//initalises a nodes and discovered array
		ArrayList<Vertex> discovered = new ArrayList<Vertex>(); //for the depth first tree search

		nodes.add(0,startVertex);	//uses an arrayList as a stack

		while (nodes.size() != 0){
			Vertex v = nodes.remove(0);					//gets the first element in the node array
			if (discovered.contains(v) == false){		//and adds it to the discovered array
				discovered.add(v);
			}
			for (Vertex x : v.getConnecting()){			//gets the nodes connected to the found node
				if (discovered.contains(x) == false){	//if it is an unseen node it adds it to discoverd
					discovered.add(x);					//and if it has a charging station it adds it to the nodes stack
					if (nodes.contains(x) == false && x.hasChargingStation()){
						nodes.add(x);
					}
				}
			}
			if (discovered.contains(endVertex) == true){ //finally it checks to see if the target vertex has been found.
				return true;
			}
		}

		return false;
	}

	private int findLowest(ArrayList<Vertex> nodes, ArrayList<DijkstraBox> dijs){
		//Used in the dijkstras it finds the node next in the visited order
		int min = -1;
		Vertex lowest = null;
		for (Vertex i : nodes){
			if (dijs.get(i.getIndex()).getWorking() < min || min == -1){  //it tests all of the nodes the one with the lowest
				lowest = i;												  //working value is found. The default value at the start is -1
				min = dijs.get(i.getIndex()).getWorking();				  //this way the first value checked will always become the lowest node
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

	private void dij_print(ArrayList<DijkstraBox> boxes){
		System.out.println("Current Boxes");
		for (DijkstraBox i : boxes){			//similar to debug_print this is for debugging
			System.out.println(i.getAllInfo()); //pass the array containing the dijkstra boxes
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
