package br.ufu.scheduling.utils;

import br.ufu.scheduling.model.Chromosome;
import br.ufu.scheduling.model.FinalResultModel;

public class Printer {
	public static final String LINE_BREAK = "\n";

	public static void printExecutionOrder(int[] startTimeTask, int[] finalTimeTask, int[] readinessTime, int task, Integer totalProcessors) {
		StringBuilder sbExecutionOrder = new StringBuilder();

		for (int processor = 1; processor <= totalProcessors; processor++) {
			sbExecutionOrder.append("RT[" + processor + "] = " + readinessTime[processor]);

			if (processor != totalProcessors) {
				sbExecutionOrder.append(", ");
			}
		}

		sbExecutionOrder.append("\n");

		for (int taskIndex = 1; taskIndex <= totalProcessors; taskIndex++) {
			sbExecutionOrder.append("ST[" + taskIndex + "] = " + startTimeTask[taskIndex] + ", ");
			sbExecutionOrder.append("FT[" + taskIndex + "] = " + finalTimeTask[taskIndex] + (taskIndex == task ? " [Running task now " + task + "]" : ""));
			sbExecutionOrder.append("\n");
		}

		System.out.println(sbExecutionOrder);
	}

	public static void printChromosome(Chromosome chromosome) {
		System.out.println("Mapping (Processors) : " + getFormattedVector(chromosome.getMapping()));
		System.out.println("Scheduling (Tasks) : " + getFormattedVector(chromosome.getScheduling()));
		System.out.println("SLenght : " + chromosome.getSLength());
		System.out.println("LoadBalance: " + chromosome.getLoadBalance());
		System.out.println("FlowTime: " + chromosome.getFlowTime());
		System.out.println("Fitness : " + chromosome.getFitness());
	}

	public static void printChromosomeVectors(int[] mapping, int[] scheduling) {
		System.out.println("Mapping (Processors) : " + getFormattedVector(mapping));
		System.out.println("Scheduling (Tasks) : " + getFormattedVector(scheduling));
	}

	private static String getFormattedVector(int[] vector) {
		StringBuilder sbFormattedVector = new StringBuilder();
		sbFormattedVector.append("[ ");

		for (int i = 0; i < vector.length; i++) {
			sbFormattedVector.append(vector[i]);

			if (i != vector.length - 1) {
				sbFormattedVector.append(", ");
			}
		}

		sbFormattedVector.append(" ]");

		return sbFormattedVector.toString();
	}

	public static void printFinalResult(FinalResultModel result, Configuration config) {
		StringBuilder builder = new StringBuilder();

		append(builder, "######################################");
		append(builder, "############ Final Result ############");
		append(builder, "######################################");
		append(builder, "");

		if (config.isConvergenceForTheBestSolution()) {
			double sucessPercentage = result.getTotalSuccess() * 100.0 / result.getTotalNumberOfChromosomes();
			append(builder, String.format("Successful run number: " + result.getTotalSuccess() + " -> %.2f", sucessPercentage) + "%");
		}

		append(builder, "Runtime for " + result.getTotalNumberOfChromosomes() + " iterations: " + ((double) (System.currentTimeMillis() - result.getInitialTime()) / 1000) + " segundos.\n");

		append(builder, "## Averages of the best chromosomes of the iterations ##");
		append(builder, "Average S_Length: " + ( ((double) result.getTotalSLenght()) / result.getTotalNumberOfChromosomes()));
		append(builder, "Average Load_Balance: " + (result.getTotalLoadBalance() / result.getTotalNumberOfChromosomes()));
		append(builder, "Average FlowTime: " + (result.getTotalFlowTime() / result.getTotalNumberOfChromosomes()));
		append(builder, "Average Fitness: " + (result.getTotalFitness() / result.getTotalNumberOfChromosomes()));

		System.out.println(builder.toString());
	}

	private static void append(StringBuilder builder, String message) {
		builder.append(message + LINE_BREAK);
	}
}
