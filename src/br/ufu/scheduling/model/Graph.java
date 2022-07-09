package br.ufu.scheduling.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
	private static final int TASK_NUMBER				= 0;
	private static final int COMPUTATIONAL_COST			= 1;
	private static final int TOTAL_PREDECESSORS			= 2;

    private Map<Integer, Vertex> vertices;
    private List<Edge> edges;
    private int firstTask;

    private Graph() {
        vertices = new HashMap<Integer, Vertex>();
        edges = new ArrayList<Edge>();
    }

    public Map<Integer, Vertex> getVertices() {
    	return vertices;
    }

    public Vertex getVertex(int task) {
    	if (!vertices.containsKey(task)) {
    		throw new IllegalArgumentException("There is no vertex with the task " + task + ".");
    	}
    	
    	return vertices.get(task);
    }

    public int getFirstTask() {
    	return firstTask;
    }

    public Vertex addVertex(int task, int computationalCost) {
        Vertex vertice = new Vertex(task, computationalCost);
        vertices.put(task, vertice);
        return vertice;
    }

    public Edge addEdge(Vertex source, Vertex destination, int computationalCost) {
        Edge edge = new Edge(source, destination, computationalCost);
        source.addAdjacency(edge);
        edges.add(edge);
        return edge;
    }

    public int getNumberOfVertices() {
    	return vertices.size();
    }

    //TODO
    public static Graph initializeGraph(String fileName) throws Exception {
    	Graph graph = new Graph();

		try (BufferedReader buffer = new BufferedReader(new FileReader(new File(fileName)))) {
			String line = null;

			int totalTasks = -1;
			int counter = 0;

			while ((line = buffer.readLine()) != null && counter != totalTasks) {
				if (totalTasks == -1) { //First Line
					totalTasks = Integer.parseInt(line.trim()) + 1; //We need to add 1 because the stg graph does not consider task 0 in the total tasks
					continue;
				}

				String[] vector = line.split(" ");

				int taskNumber = 0;
				int computationalCost = 0;
				int totalPredecessors = 0;

				int totalPositionsAnalyzed = 0;
				int totalPredecessorsAnalyzed = 0;
				List<Integer> listPredecessors = new ArrayList<>();

				for (int position = 0; position < vector.length; position++) {
					String value = vector[position];
					if (value == null || Arrays.asList("", " ").contains(value)) {
						continue;	
					}

					switch (totalPositionsAnalyzed) {
					case TASK_NUMBER:
						taskNumber = Integer.parseInt(value) + 1; //FIXME: verificar se vamos tratar assim, para não deixar começar uma tarefa em zero 
						break;

					case COMPUTATIONAL_COST:
						computationalCost = Integer.parseInt(value);
						break;

					case TOTAL_PREDECESSORS:
						totalPredecessors = Integer.parseInt(value);
						break;

					default:
						listPredecessors.add(Integer.parseInt(value) + 1); //FIXME: verificar se vamos tratar assim, para não deixar começar uma tarefa em zero
						totalPredecessorsAnalyzed++;
						break;
					}

					totalPositionsAnalyzed++;

					if (totalPredecessors == totalPredecessorsAnalyzed && totalPredecessors > 0) {
						break;
					}
				}

				Vertex vertex = graph.addVertex(taskNumber, computationalCost);

				if (listPredecessors.isEmpty()) {
					vertex.setEntries(null);
					graph.firstTask = vertex.getTask();
				} else {
					vertex.setEntries(listPredecessors);
					listPredecessors.forEach(predecessor -> graph.addEdge(graph.getVertex(predecessor), vertex, 0)); //Without communication cost
				}


				counter++;
			}
		} catch (Exception e) {
			Exception e2 = new Exception("Error loading " + fileName + "  task graph file: " + e.getMessage());
			e2.initCause(e);
			throw e2;
		}

		//FIXME: rancar esse cara depois
		boolean debug = false;
		if (debug) {
			System.out.println(graph.toString());
		}

    	return graph;
    }

    public static Graph initializeGraph() {
    	Graph graph = new Graph();

        Vertex vertex1 = graph.addVertex(1, 2);
        vertex1.setEntries(null);
        graph.firstTask = vertex1.getTask();

        Vertex vertex2 = graph.addVertex(2, 3);
        vertex2.setEntries(Arrays.asList(1));

        Vertex vertex3 = graph.addVertex(3, 3);
        vertex3.setEntries(Arrays.asList(1));

        Vertex vertex4 = graph.addVertex(4, 4);
        vertex4.setEntries(Arrays.asList(1));

        Vertex vertex5 = graph.addVertex(5, 5);
        vertex5.setEntries(Arrays.asList(1));

        Vertex vertex6 = graph.addVertex(6, 4);
        vertex6.setEntries(Arrays.asList(2));

        Vertex vertex7 = graph.addVertex(7, 4);
        vertex7.setEntries(Arrays.asList(1, 2));

        Vertex vertex8 = graph.addVertex(8, 4);
        vertex8.setEntries(Arrays.asList(3, 4));

        Vertex vertex9 = graph.addVertex(9, 1);
        //Another way to add entries
        vertex9.addEntry(6);
        vertex9.addEntry(7);
        vertex9.addEntry(8);

        //Building Edges: sources and destinations
        //Vertex 1
        graph.addEdge(vertex1, vertex2, 4);
        graph.addEdge(vertex1, vertex3, 1);
        graph.addEdge(vertex1, vertex4, 1);
        graph.addEdge(vertex1, vertex5, 1);
        graph.addEdge(vertex1, vertex7, 10);

        //Vertex 2
        graph.addEdge(vertex2, vertex6, 1);
        graph.addEdge(vertex2, vertex7, 1);

        //Vertex 3
        graph.addEdge(vertex3, vertex8, 1);

        //Vertex 4
        graph.addEdge(vertex4, vertex8, 1);

        //Vertex 6
        graph.addEdge(vertex6, vertex9, 5);

        //Vertex 7
        graph.addEdge(vertex7, vertex9, 6);

        //Vertex 8
        graph.addEdge(vertex8, vertex9, 5);

        return graph;
    }

    public String toString() {
        String formattedText = "";

        for (Map.Entry<Integer, Vertex> mapVertice : vertices.entrySet()) {
        	Vertex vertex = mapVertice.getValue();

            formattedText += vertex.getTask() + " -> ";

            for (Edge e : vertex.getAdjacency()) {
                Vertex v = e.getDestination();
                formattedText += v.getTask() + ", ";
            }

            formattedText += "\n";
        }

        return formattedText;
    }

    public static void main(String[] args) {
        Graph graph = Graph.initializeGraph();
        System.out.println(graph);
    }
}