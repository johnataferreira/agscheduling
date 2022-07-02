package br.ufu.scheduling.model;

public class Edge {
	private Vertex source;
	private Vertex destination;

	private int computationalCost = 0;

	public Edge(Vertex source, Vertex destination, int computationalCost) {
		this.source = source;
		this.destination = destination;
		this.computationalCost = computationalCost;
	}

	public Vertex getSource() {
		return source;
	}

	public Vertex getDestination() {
		return destination;
	}

	public int getComputationalCost() {
		return computationalCost;
	}

	public void setSource(Vertex source) {
		this.source = source;
	}

	public void setDestination(Vertex destination) {
		this.destination = destination;
	}

	public void setComputationalCost(int computationalCost) {
		this.computationalCost= computationalCost;
	}
}