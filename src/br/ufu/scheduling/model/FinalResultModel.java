package br.ufu.scheduling.model;

import br.ufu.scheduling.utils.Configuration;
import br.ufu.scheduling.utils.Printer;

public class FinalResultModel {
	private long initialTime;
	private int totalSuccess;
	private double totalSLenght;
	private double totalLoadBalance;
	private double totalFlowTime;
	private double totalCommunicationCost;
	private double totalWaitingTime;
	private double totalSLengthPlusWaitingTime;
	private double totalFitness;
	private int totalNumberOfChromosomes;

	public FinalResultModel() {
	}

	public long getInitialTime() {
		return initialTime;
	}

	public int getTotalSuccess() {
		return totalSuccess;
	}

	public double getTotalSLenght() {
		return totalSLenght;
	}

	public double getTotalLoadBalance() {
		return totalLoadBalance;
	}

	public double getTotalFlowTime() {
		return totalFlowTime;
	}

	public double getTotalCommunicationCost() {
		return totalCommunicationCost;
	}

	public double getTotalWaitingTime() {
		return totalWaitingTime;
	}

	public double getTotalSLengthPlusWaitingTime() {
	    return totalSLengthPlusWaitingTime;
	}

	public double getTotalFitness() {
		return totalFitness;
	}

	public int getTotalNumberOfChromosomes() {
		return totalNumberOfChromosomes;
	}

	public void setInitialTime(long initialTime) {
		this.initialTime = initialTime;
	}

	public void setTotalSuccess(int totalSuccess) {
		this.totalSuccess = totalSuccess;
	}

	public void setTotalSLength(double totalSLenght) {
		this.totalSLenght = totalSLenght;
	}

	public void setTotalLoadBalance(double totalLoadBalance) {
		this.totalLoadBalance = totalLoadBalance;
	}

	public void setTotalFlowTime(double totalFlowTime) {
		this.totalFlowTime = totalFlowTime;
	}

	public void setTotalCommunicationCost(double totalCommunicationCost) {
		this.totalCommunicationCost = totalCommunicationCost;
	}

	public void setTotalWaitingTime(double totalWaitingTime) {
		this.totalWaitingTime = totalWaitingTime;
	}

	public void setTotalSLengthPlusWaitingTime(double totalSLengthPlusWaitingTime) {
	    this.totalSLengthPlusWaitingTime = totalSLengthPlusWaitingTime;
	}

	public void setTotalFitness(double totalFitness) {
		this.totalFitness = totalFitness;
	}

	public void setTotalNumberOfChromosomes(int totalNumberOfChromosomes) {
		this.totalNumberOfChromosomes = totalNumberOfChromosomes;
	}

	public void showResult(Chromosome bestChromosomeFound, Configuration config) {
		Printer.printFinalResult(this, config);

		System.out.println("## Best Chromosome ##");

		if (bestChromosomeFound != null) {
			bestChromosomeFound.printChromosome();
		} else {
			System.out.println("[ There was no convergence for the best chromosome! ]");
		}
	}
}
