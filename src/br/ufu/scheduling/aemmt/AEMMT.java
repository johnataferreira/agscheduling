package br.ufu.scheduling.aemmt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import br.ufu.scheduling.model.Chromosome;
import br.ufu.scheduling.model.Graph;
import br.ufu.scheduling.utils.Configuration;
import br.ufu.scheduling.utils.Crossover;

public class AEMMT {
	public static final int DOUBLE_TOURNAMENT= 2;

	private Random generator 				= new Random();
	private Configuration config;
	private Graph graph;
	private int initialPopulation;
	private List<Table> tables = new ArrayList<>();
	private List<Chromosome> chromosomeInitialList = new ArrayList<>();
	private Table table1ForDoubleTournament;
	private Table table2ForDoubleTournament;

	public AEMMT() {
		//For tests
	}

	public AEMMT(Configuration config, Graph graph) throws Exception {
		this.config = config;
		this.graph = graph;

		createTables();
		initialPopulation = tables.size() * config.getSizeOfTables();
	}

	private void createTables() {
		if (config.getTotalObjectives() < 2 || config.getTotalObjectives() > 5) {
			throw new IllegalArgumentException(
					"Invalid value to work with multi-objective algorithm. Valid values between 2 and 5.");
		}

		switch (config.getTotalObjectives()) {
		case 2:
			createTable(1);
			createTable(2);

			createTable(Arrays.asList(1, 2));
			break;

		case 3:
			createTable(1);
			createTable(2);
			createTable(3);

			createTable(Arrays.asList(1, 2));
			createTable(Arrays.asList(1, 3));
			createTable(Arrays.asList(2, 3));

			createTable(Arrays.asList(1, 2, 3));
			break;

		case 4:
			createTable(1);
			createTable(2);
			createTable(3);
			createTable(4);

			createTable(Arrays.asList(1, 2));
			createTable(Arrays.asList(1, 3));
			createTable(Arrays.asList(1, 4));
			createTable(Arrays.asList(2, 3));
			createTable(Arrays.asList(2, 4));
			createTable(Arrays.asList(3, 4));

			createTable(Arrays.asList(1, 2, 3));
			createTable(Arrays.asList(1, 2, 4));
			createTable(Arrays.asList(1, 3, 4));
			createTable(Arrays.asList(2, 3, 4));

			createTable(Arrays.asList(1, 2, 3, 4));
			break;

		case 5:
			createTable(1);
			createTable(2);
			createTable(3);
			createTable(4);
			createTable(5);

			createTable(Arrays.asList(1, 2));
			createTable(Arrays.asList(1, 3));
			createTable(Arrays.asList(1, 4));
			createTable(Arrays.asList(1, 5));
			createTable(Arrays.asList(2, 3));
			createTable(Arrays.asList(2, 4));
			createTable(Arrays.asList(2, 5));
			createTable(Arrays.asList(3, 4));
			createTable(Arrays.asList(3, 5));
			createTable(Arrays.asList(4, 5));

			createTable(Arrays.asList(1, 2, 3));
			createTable(Arrays.asList(1, 2, 4));
			createTable(Arrays.asList(1, 2, 5));
			createTable(Arrays.asList(1, 3, 4));
			createTable(Arrays.asList(1, 3, 5));
			createTable(Arrays.asList(1, 4, 5));
			createTable(Arrays.asList(2, 3, 4));
			createTable(Arrays.asList(2, 3, 5));
			createTable(Arrays.asList(2, 4, 5));
			createTable(Arrays.asList(3, 4, 5));

			createTable(Arrays.asList(1, 2, 3, 4));
			createTable(Arrays.asList(1, 2, 3, 5));
			createTable(Arrays.asList(1, 2, 4, 5));
			createTable(Arrays.asList(1, 3, 4, 5));
			createTable(Arrays.asList(2, 3, 4, 5));

			createTable(Arrays.asList(1, 2, 3, 4, 5));
			break;

		default:
			break;
		}

		createNonDominatedTable();
	}

	private void createNonDominatedTable() {
		Table table = new Table(config.getSizeOfNonDominatedTable());
		tables.add(table);
	}

	private void createTable(Integer objective) {
		Table table = new Table(config.getSizeOfTables());
		table.addObjective(objective);
		tables.add(table);
	}

	private void createTable(List<Integer> objectives) {
		Table table = new Table(config.getSizeOfTables());
		table.addObjectives(objectives);
		tables.add(table);
	}

	private int simpleCombination(int numberOfElements, int numberInTheSet) {
		return fat(numberOfElements) / (fat(numberInTheSet) * fat(numberOfElements - numberInTheSet));
	}

	private int fat(int value) {
		if (value == 0 || value == 1) {
			return 1;
		}

		int accumulatedValue = 1;
		for (int i = 1; i <= value; i++) {
			accumulatedValue *= i;
		}

		return accumulatedValue;
	}

	public void execute(long initialTime) throws Exception {
		generateInitialPopulation();

		int generation = 0;
		int generationAccumulatedForResetTableScore = 0;
		while (generation < config.getTotalGenerations()) {
			if (config.isPrintIterationsAndGenerations()) {
				System.out.println("############################\n");
				System.out.println("####### GENERATION: " + (generation + 1) + " #######\n");
				System.out.println("############################\n");
			}

			executeAG();

			generationAccumulatedForResetTableScore++;
			generation++;

			if (resetTableScore(generationAccumulatedForResetTableScore)) { 
				generationAccumulatedForResetTableScore = 0;
			}
		}

		showResult(initialTime);
	}

	private void generateInitialPopulation() throws Exception {
		for (int i = 1; i <= initialPopulation; i++) {
			Chromosome chromosome = new Chromosome(generator, graph, config);

			if (!chromosomeInitialList.contains(chromosome)) {
				addChromosomeToTables(chromosome);
				chromosomeInitialList.add(chromosome);
			}
		}

		addChromosomesToNonDominatedTable(chromosomeInitialList);
	}

	private boolean addChromosomeToTables(Chromosome chromosome) throws Exception {
		boolean addedInSometable = false;

		//the non-dominated table will be processed only at the end
		for (int i = 0; i < tables.size() - 1; i++) {
			boolean added = tables.get(i).add(chromosome, config);

			if (!addedInSometable) {
				addedInSometable = added; 
			}
		}

		return addedInSometable;
	}

	private void addChromosomesToNonDominatedTable(List<Chromosome> chromosomeInitialList) {
		// TODO Auto-generated method stub
		
	}

	private void executeAG() throws Exception {
		processTablesForDoubleTournament();

		Chromosome children = processPairSelection();
		boolean added = addChromosomeToTables(children);
		processTableScore(added);

		processNonDominatedTable(children);
	}

	private void processTablesForDoubleTournament() {
		table1ForDoubleTournament = raffleTableByDoubleTournament();
		table2ForDoubleTournament = raffleTableByDoubleTournament();
	}

	private Table raffleTableByDoubleTournament() {
		Table table = null;

		for (int tour = 0; tour < DOUBLE_TOURNAMENT; tour++) {
			//Disregarding the non-dominated table
			int raffleIndex = raffleIndex(tables.size() - 1);

			if (table == null || table.getScore() > tables.get(raffleIndex).getScore()) { 
				table = tables.get(raffleIndex);
			}
		}

		return table;
	}

	private int raffleIndex(int limit) {
		return generator.nextInt(limit);
	}

	private Chromosome processPairSelection() {
		Chromosome parent1 = raflleChromosomeFromTable(table1ForDoubleTournament);
		Chromosome parent2 = raflleChromosomeFromTable(table2ForDoubleTournament);

		return getCrossoverChildren(parent1, parent2);
	}

	private Chromosome raflleChromosomeFromTable(Table table) {
		return table.getChromosomeFromIndex(raffleIndex(table.getSize()));
	}

	private Chromosome getCrossoverChildren(Chromosome parent1, Chromosome parent2) {
		List<Chromosome> generatedChildren = Crossover.getCrossover(parent1, parent2, graph, generator, config);

		if (generatedChildren.size() == 0) {
			return generatedChildren.get(0);
		}

		return generatedChildren.get(raffleIndex(generatedChildren.size()));
	}

	private void processTableScore(boolean chromosomeGeneratedSurvived) {
		if (chromosomeGeneratedSurvived) {
			table1ForDoubleTournament.addScore(1);
			table2ForDoubleTournament.addScore(1);
		}
	}

	//TODO: criar o preenchimento da tabela de não dominância
	private void processNonDominatedTable(Chromosome children) {
		// TODO Auto-generated method stub
		
	}

	private boolean resetTableScore(int totalGenerations) {
		if (config.getTotalGenerationsToResetTableScore() > 0 && totalGenerations > config.getTotalGenerationsToResetTableScore()) {
			tables.forEach(x -> System.out.println(x.getScore()));
			tables.forEach(table -> table.resetScore());
			return true;
		}
		
		return false;
	}

	private void showResult(long initialTime) {
//		FinalResultModel result = new FinalResultModel();
//		result.setInitialTime(initialTime);
//		result.setTotalSuccess(totalSuccess);
//		result.setTotalSLenght(totalSLenghtOfBestChromosomes);
//		result.setTotalLoadBalance(totalLoadBalanceOfBestChromosomes);
//		result.setTotalFlowTime(totalFlowTimeOfBestChromosomes);
//		result.setTotalCommunicationCost(totalCommunicationCostOfBestChromosomes);
//		result.setTotalWaitingTime(totalWaitingTimeOfBestChromosomes);
//		result.setTotalFitness(totalFitnessOfBestChromosomes);
//		result.setTotalNumberOfChromosomes(totalNumberOfChromosomes);
//
//		result.showResult(bestChromosomeFound, config);		
	}

	public static void main(String args[]) throws Exception {
		AEMMT a = new AEMMT();
		//System.out.println(a.simpleCombination(2, 1) + a.simpleCombination(2, 2));
		//System.out.println(a.simpleCombination(3, 1) + a.simpleCombination(3, 2) + a.simpleCombination(3, 3));
		//System.out.println(a.simpleCombination(4, 1) + a.simpleCombination(4, 2) + a.simpleCombination(4, 3) + a.simpleCombination(4, 4));
		//System.out.println(a.simpleCombination(5, 1) + a.simpleCombination(5, 2) + a.simpleCombination(5, 3) + a.simpleCombination(5, 4) + a.simpleCombination(5, 5));
		System.out.println(a.simpleCombination(5, 1));
		System.out.println(a.simpleCombination(5, 2));
		System.out.println(a.simpleCombination(5, 3));
		System.out.println(a.simpleCombination(5, 4));
		System.out.println(a.simpleCombination(5, 5));
	}
}