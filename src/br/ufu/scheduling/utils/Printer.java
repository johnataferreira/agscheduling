package br.ufu.scheduling.utils;

import java.util.List;

import br.ufu.scheduling.aemmt.Table;
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
		printChromosome(chromosome, true);
	}

	public static void printChromosome(Chromosome chromosome, boolean printFitness) {
		System.out.print(getObjectivesFromChromosomeFormatted(chromosome, printFitness));
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
		append(builder, "Average CommunicationCost: " + (result.getTotalCommunicationCost() / result.getTotalNumberOfChromosomes()));
		append(builder, "Average WaitingTime: " + (result.getTotalWaitingTime() / result.getTotalNumberOfChromosomes()));
		append(builder, "Average Fitness: " + (result.getTotalFitness() / result.getTotalNumberOfChromosomes()));

		System.out.println(builder.toString());
	}

	private static void append(StringBuilder builder, String message) {
		builder.append(message + LINE_BREAK);
	}

	public static void printFinalResultForAEMMT(Configuration config, Table nonDominatedTable, long initialTime) {
		StringBuilder builder = new StringBuilder();

		append(builder, "######################################");
		append(builder, "############ Final Result ############");
		append(builder, "######################################");
		append(builder, "");

		append(builder, "############ AEMMT ############");
		append(builder, "## Chromosomes Non-Dominated ##");
		append(builder, "");

		for (int i = 0; i < nonDominatedTable.getTotalChromosomes(); i++) {
			append(builder, getObjectivesFromChromosomeFormatted(nonDominatedTable.getChromosomeFromIndex(i), false));
		}

		append(builder, "##################################");
		append(builder, "");
		append(builder, getGeneralData(config, nonDominatedTable, initialTime));

		System.out.println(builder.toString());
	}

	private static String getObjectivesFromChromosomeFormatted(Chromosome chromosome, boolean showFitness) {
		StringBuilder builder = new StringBuilder();

		append(builder, "Mapping (Processors) : " + getFormattedVector(chromosome.getMapping()));
		append(builder, "Scheduling (Tasks) : " + getFormattedVector(chromosome.getScheduling()));
		append(builder, "SLenght : " + chromosome.getSLength());
		append(builder, "LoadBalance: " + chromosome.getLoadBalance());
		append(builder, "FlowTime: " + chromosome.getFlowTime());
		append(builder, "CommunicationCost: " + chromosome.getCommunicationCost());
		append(builder, "WaitingTime: " + chromosome.getWaitingTime());

		if (showFitness) {
			append(builder, "Fitness : " + chromosome.getFitness());
		}

		return builder.toString();
	}

	public static String getGeneralData(Configuration config, Table tableNonDominated, long initialTime) {
		StringBuilder builder = new StringBuilder();

		append(builder, "Total Chromosomes Non-Dominated: " + tableNonDominated.getTotalChromosomes() + ".");
		append(builder, "Runtime for " + config.getTotalGenerations() + " generations: " + ((double) (System.currentTimeMillis() - initialTime) / 1000) + " segundos.\n");

		return builder.toString();
	}

	public static void printFinalResultForAEMMTWithComparedToNonDominated(Configuration config, List<Table> tables, long initialTime) {
		StringBuilder builder = new StringBuilder();

		append(builder, "######################################");
		append(builder, "############ Final Result ############");
		append(builder, "######################################");
		append(builder, "");

		append(builder, "############ AEMMT ############");
		append(builder, "## Chromosomes Non-Dominated ##");
		append(builder, "");
		

		Table nonDominatedTable = tables.get(tables.size() - 1);

		for (int chromosomeNonDominatedIndex = 0; chromosomeNonDominatedIndex < nonDominatedTable.getTotalChromosomes(); chromosomeNonDominatedIndex++) {
			append(builder, "##################################");
			append(builder, "Chromosome A: - " + (chromosomeNonDominatedIndex + 1));
			append(builder, "");

			Chromosome chromosomeA = nonDominatedTable.getChromosomeFromIndex(chromosomeNonDominatedIndex);

			append(builder, getObjectivesFromChromosomeFormatted(chromosomeA, false));
			append(builder, "");

			int totalCromossomesAnalised = 0;

			for (int tableIndex = 0; tableIndex < tables.size(); tableIndex++) {
				Table table = tables.get(tableIndex);

				for (int chromosomeIndex = 0; chromosomeIndex < table.getTotalChromosomes(); chromosomeIndex++) {
					Chromosome chromosomeB = table.getChromosomeFromIndex(chromosomeIndex);

					append(builder, "----------------------------------");
					append(builder, "Chromosome B: - " + (totalCromossomesAnalised + 1));
					append(builder, "");

					append(builder, getObjectivesFromChromosomeFormatted(chromosomeB, false));
					append(builder, "");

					append(builder, "SLenght : ");
					append(builder, "	B: " + chromosomeB.getFitnessForSLength() + " | A: " + chromosomeA.getFitnessForSLength());
					append(builder, "	Result: " + getResolvedComparison(chromosomeB.getFitnessForSLength(), chromosomeA.getFitnessForSLength()));

					append(builder, "LoadBalance : ");
					append(builder, "	B: " + chromosomeB.getFitnessForLoadBalance() + " | A: " + chromosomeA.getFitnessForLoadBalance());
					append(builder, "	Result: " + getResolvedComparison(chromosomeB.getFitnessForLoadBalance(), chromosomeA.getFitnessForLoadBalance()));

					append(builder, "FlowTime : ");
					append(builder, "	B: " + chromosomeB.getFitnessForFlowTime() + " | A: " + chromosomeA.getFitnessForFlowTime());
					append(builder, "	Result: " + getResolvedComparison(chromosomeB.getFitnessForFlowTime(), chromosomeA.getFitnessForFlowTime()));

					append(builder, "CommunicationCost : ");
					append(builder, "	B: " + chromosomeB.getFitnessForCommunicationCost() + " | A: " + chromosomeA.getFitnessForCommunicationCost());
					append(builder, "	Result: " + getResolvedComparison(chromosomeB.getFitnessForCommunicationCost(), chromosomeA.getFitnessForCommunicationCost()));

					append(builder, "WaitingTime : ");
					append(builder, "	B: " + chromosomeB.getFitnessForWaitingTime() + " | A: " + chromosomeA.getFitnessForWaitingTime());
					append(builder, "	Result: " + getResolvedComparison(chromosomeB.getFitnessForWaitingTime(), chromosomeA.getFitnessForWaitingTime()));

					append(builder, "");

					append(builder, "B domina A: " + chromosomeA.isChromosomeDominated(config, chromosomeB));
					append(builder, "");
					
					totalCromossomesAnalised++;
				}
			}
		}

		append(builder, "##################################");
		append(builder, "");
		append(builder, getGeneralData(config, nonDominatedTable, initialTime));

		System.out.println(builder.toString());
	}

	private static String getResolvedComparison(double valueA, double valueB) {
		if (valueA > valueB) {
			return "Greater or Equal";

		} else if (valueA == valueB) {
			return "Equal";

		} else {
			return "Less";
		}
	}
}
