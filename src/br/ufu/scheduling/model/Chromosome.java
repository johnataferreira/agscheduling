package br.ufu.scheduling.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.ufu.scheduling.exceptions.BetterChromosomeFoundException;
import br.ufu.scheduling.utils.Configuration;
import br.ufu.scheduling.utils.Mutation;
import br.ufu.scheduling.utils.Printer;

public class Chromosome implements Cloneable {
	private int[] mapping; 		//Processors execution order - starts on processor 1
	private int[] scheduling;	//Task execution order - starts at task 1
	private Metrics metrics = new Metrics(); 

	public Chromosome(Graph graph) {
		createVectors(graph);
	}

	public Chromosome(Random generator, Graph graph, Configuration config) {
		createVectors(graph);
		generateChromosome(generator, graph, config);
	}

	public Chromosome(int[] mapping, int[] scheduling, Graph graph, Configuration config) {
		this.mapping = mapping;
		this.scheduling = scheduling;

		calculateMetrics(graph, config);
	}

	private Chromosome() {
	}

	public int[] getMapping() {
		return mapping;
	}

	public int[] getScheduling() {
		return scheduling;
	}


	public double getSLength() {
		return metrics.getSLength();
	}

	public double getLoadBalance() {
		return metrics.getLoadBalance();
	}

	public double getFlowTime() {
		return metrics.getFlowTime();
	}

	public double getCommunicationCost() {
		return metrics.getCommunicationCost();
	}

	public double getWaitingTime() {
		return metrics.getWaitingTime();
	}

    public double getSLengthPlusWaitingTime() {
        return metrics.getSLengthPlusWaitingTime();
    }

	public double getFitness() {
		return metrics.getFitness();
	}

	public int getFitnessAdjusted() {
		return metrics.getFitnessAjusted();
	}

	public double getFitnessForSLength() {
		return metrics.getFitnessForSLength();
	}

	public double getFitnessForLoadBalance() {
		return metrics.getFitnessForLoadBalance();
	}

	public double getFitnessForFlowTime() {
		return metrics.getFitnessForFlowTime();
	}

	public double getFitnessForCommunicationCost() {
		return metrics.getFitnessForCommunicationCost();
	}

	public double getFitnessForWaitingTime() {
		return metrics.getFitnessForWaitingTime();
	}

	public double getAemmtValue() {
		return metrics.getAemmtValue();
	}

	public void setAemmtValue(double aemmtValue) {
		metrics.setAemmtValue(aemmtValue);
	}

	private void createVectors(Graph graph) {
		mapping = new int[graph.getNumberOfVertices()];
		scheduling = new int[graph.getNumberOfVertices()];
	}

	private void generateChromosome(Random generator, Graph graph, Configuration config) {
		populateMapping(generator, config);
		populateScheduling(generator, graph);
		calculateMetrics(graph, config);
	}

	private void populateMapping(Random generator, Configuration config) {
		for (int i = 0; i < mapping.length; i++) {
			//Plus 1, because the index of the first processor will be 1 e not 0 
			mapping[i] = generator.nextInt(config.getTotalProcessors()) + 1;
		}
	}

	private void populateScheduling(Random generator, Graph graph) {
		List<String> listOfRaffledEdges = new ArrayList<String>();
		List<Integer> listOfRaffledDestinationNodes = new ArrayList<Integer>();

		List<Integer> listOfSourceNodesForRaffle = new ArrayList<>();
		listOfSourceNodesForRaffle.add(graph.getFirstTask()); //We always begin for the first task

		for (int i = 0; i < scheduling.length; i++) {
			if (i == 0) { //First Task?
				int rootNode = listOfSourceNodesForRaffle.get(i);
				scheduling[i] = rootNode;

				addRaffledDestinationNode(listOfRaffledDestinationNodes, rootNode);

				continue;
			} 

			int raffledDestinationNode, raffledSourceNode;

			do {
				raffledSourceNode = listOfSourceNodesForRaffle.get(generator.nextInt(listOfSourceNodesForRaffle.size()));
				List<Edge> adjacency = graph.getVertex(raffledSourceNode).getAdjacency();
				raffledDestinationNode = adjacency.get(generator.nextInt(adjacency.size())).getDestination().getTask();

			} while(!isFinishedEntry(graph, listOfRaffledDestinationNodes, raffledDestinationNode) 
					|| isEdgeUsed(listOfRaffledEdges, raffledSourceNode, raffledDestinationNode) 
					|| isAlreadyRaffledDestinationNode(listOfRaffledDestinationNodes, raffledDestinationNode));

			scheduling[i] = raffledDestinationNode;

			addRaffledNode(listOfSourceNodesForRaffle, raffledDestinationNode); 
			addRaffledDestinationNode(listOfRaffledDestinationNodes, raffledDestinationNode);

			addRaffledEdges(listOfRaffledEdges, raffledSourceNode, raffledDestinationNode);
			handleInvalidData(graph, listOfRaffledEdges, listOfRaffledDestinationNodes, listOfSourceNodesForRaffle, raffledSourceNode, raffledDestinationNode);
		}
	}

	private boolean isFinishedEntry(Graph graph, List<Integer> listOfRaffledDestinationNodes, int raffledDestinationNode) {
		for (Integer entradaNecessariaParaExecucao : graph.getVertex(raffledDestinationNode).getEntries()) {
			if (!listOfRaffledDestinationNodes.contains(entradaNecessariaParaExecucao)) {
				return false;
			}
		}

		return true;
	}

	private boolean isEdgeUsed(List<String> listOfRaffledEdges, int raffledSourceNode, int raffledDestinationNode) {
		return listOfRaffledEdges.contains(getNameOfEdge(raffledSourceNode, raffledDestinationNode)); 
	}

	//A node can be the destination of only one origin, so if it is already in the tree, it cannot be selected again
	private boolean isAlreadyRaffledDestinationNode(List<Integer> listOfRaffledDestinationNodes, int raffledDestinationNode) {
		return listOfRaffledDestinationNodes.contains(raffledDestinationNode);
	}

	private void addRaffledNode(List<Integer> listOfSourceNodesForRaffle, int raffledDestinationNode) {
		listOfSourceNodesForRaffle.add(raffledDestinationNode);
	}

	private void addRaffledDestinationNode(List<Integer> listOfRaffledDestinationNodes, int raffledDestinationNode) {
		if (!listOfRaffledDestinationNodes.contains(raffledDestinationNode)) {
			listOfRaffledDestinationNodes.add(raffledDestinationNode);
		}
	}

	private void addRaffledEdges(List<String> listOfEdges, int sourceNode, int destinationNode) {
		listOfEdges.add(getNameOfEdge(sourceNode, destinationNode));
	}

	private String getNameOfEdge(int sourceNode, int destinationNode) {
		return sourceNode + "-" + destinationNode;
	}

	private void handleInvalidData(Graph graph, List<String> listOfRaffledEdges, List<Integer> listOfRaffledDestinationNodes, List<Integer> listOfSourceNodesForRaffle, int raffledSourceNode, int raffledDestinationNode) {
		List<Edge> adjagency = graph.getVertex(raffledDestinationNode).getAdjacency();

		for (Edge edje : adjagency) {
			int destinationAdjagencyNode = edje.getDestination().getTask();

			if (destinationAdjagencyNode == raffledSourceNode || (!listOfRaffledDestinationNodes.contains(destinationAdjagencyNode)) && destinationAdjagencyNode != graph.getFirstTask()) {
				continue;
			}

			addRaffledEdges(listOfRaffledEdges, raffledDestinationNode, destinationAdjagencyNode);

			//It could be that destinationAdjagencyNode becomes invalid when we add this new edge, so I should remove it from the raffle list
			handleInvalidNodeForRaffle(graph, listOfRaffledEdges, listOfSourceNodesForRaffle, destinationAdjagencyNode);
		}

		handleInvalidNodeForRaffle(graph, listOfRaffledEdges, listOfSourceNodesForRaffle, raffledSourceNode);
		handleInvalidNodeForRaffle(graph, listOfRaffledEdges, listOfSourceNodesForRaffle, raffledDestinationNode);
	}

	//If the node has already reached all its destinations, it cannot be raffle again, so we remove it from the list of source nodes suitable for raffle
	private void handleInvalidNodeForRaffle(Graph graph, List<String> listOfRaffledEdges, List<Integer> listOfSourceNodesForRaffle, int node) {
		if (isSourceNodeReachedAllDestinations(graph, listOfRaffledEdges, node)) {
			listOfSourceNodesForRaffle.remove(Integer.valueOf(node));
		}
	}

	private boolean isSourceNodeReachedAllDestinations(Graph graph, List<String> listOfRaffledEdges, int sourceNode) {
		return listOfRaffledEdges.stream().filter(nameOfEdge -> nameOfEdge.startsWith(sourceNode + "-")).count() == graph.getVertex(sourceNode).getAdjacency().size();
	}

	public void calculateMetrics(Graph graph, Configuration config) {
		try {
			metrics.calculateMetrics(graph, mapping, scheduling, config);
		} catch (BetterChromosomeFoundException e) {
			System.out.println("Better Chromosome Found:");
			Printer.printChromosomeVectors(mapping, scheduling);

			BetterChromosomeFoundException e2 = new BetterChromosomeFoundException(e.getMessage());
			e2.initCause(e);
			throw e2;
		}
	}

	public void applyMutation(Random generator, Graph graph, Configuration config) {
		mapping = Mutation.applyMutation(generator, mapping, config);
		calculateMetrics(graph, config);
	}

	public void printChromosome() {
		Printer.printChromosome(this);
	}

	public Object clone() throws CloneNotSupportedException {
		Chromosome clone = new Chromosome();
		clone.mapping = this.mapping.clone();
		clone.scheduling = this.scheduling.clone();
		clone.metrics = (Metrics) this.metrics.clone();
		return  clone;
	}

	public double getObjectiveValue(Integer objectiveIndex) {
		double valueObjective = 0.0;

		switch (objectiveIndex) {
		case 0:
			valueObjective = getFitnessForSLength();
			break;

		case 1:
			valueObjective = getFitnessForLoadBalance();
			break;

		case 2:
			valueObjective = getFitnessForFlowTime();
			break;

		case 3:
			valueObjective = getFitnessForCommunicationCost();
			break;

		case 4:
			valueObjective = getFitnessForWaitingTime();
			break;

		default:
			throw new IllegalArgumentException("Type of objective invalid. Value: " + objectiveIndex + ".");
		}

		return valueObjective;
	}

	/**
	 * Rule:
	 *   B dominates A or A is dominated by B if:
	 *     1: B >= A in all objectives;
	 *     2: B > A in at least one objective
	 * 
	 * */
	public boolean isChromosomeDominated(Configuration config, Chromosome chromosomeB) {
		boolean isChromosomeDominated = false;

		for (int objectiveIndex = 0; objectiveIndex < config.getTotalObjectives(); objectiveIndex++) {
			int result = Double.compare(chromosomeB.getObjectiveValue(objectiveIndex), this.getObjectiveValue(objectiveIndex));

			if (result > 0) {
				isChromosomeDominated = true;
			} else {
				return false;
			}
		}

		return isChromosomeDominated;
	}

	   /**
     * Rule:
     *   B dominates A or A is dominated by B if:
     *     1: B >= A in all objectives;
     *     2: B > A in at least one objective
     * 
     * */
    public boolean isChromosomeDominated(List<Integer> objectives, Chromosome chromosomeB) {
        boolean isChromosomeDominated = false;

        for (int objective : objectives) {
            //Internally, the objectives start from zero, so the subtraction
            int realObjective = objective - 1;
            int result = Double.compare(chromosomeB.getObjectiveValue(realObjective), this.getObjectiveValue(realObjective));

            if (result > 0) {
                isChromosomeDominated = true;
            } else {
                return false;
            }
        }

        return isChromosomeDominated;
    }

	@Override
	public boolean equals(Object obj) {
		Chromosome other = (Chromosome) obj;
		boolean equals = true;

		for (int i = 0; i < other.getMapping().length; i++) {
			equals = mapping[i] == other.mapping[i] && scheduling[i] == other.scheduling[i];

			if (!equals) {
				return equals;
			}
		}

		return equals;
	}

	public static void main(String args[]) throws Exception {
		Configuration config = new Configuration();

		Graph graph = Graph.initializeGraph();
		Chromosome chromosome = new Chromosome(graph);

		chromosome.mapping[0] = 2; 
		chromosome.mapping[1] = 2;
		chromosome.mapping[2] = 3;
		chromosome.mapping[3] = 3;
		chromosome.mapping[4] = 1;
		chromosome.mapping[5] = 1;
		chromosome.mapping[6] = 2;
		chromosome.mapping[7] = 3;
		chromosome.mapping[8] = 3;

		chromosome.scheduling[0] = 1; 
		chromosome.scheduling[1] = 4;
		chromosome.scheduling[2] = 3;
		chromosome.scheduling[3] = 2;
		chromosome.scheduling[4] = 8;
		chromosome.scheduling[5] = 7;
		chromosome.scheduling[6] = 6;
		chromosome.scheduling[7] = 9;
		chromosome.scheduling[8] = 5;

		chromosome.calculateMetrics(graph, config);
		chromosome.printChromosome();
	}
}