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
