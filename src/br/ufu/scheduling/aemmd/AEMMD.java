package br.ufu.scheduling.aemmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import br.ufu.scheduling.agmo.Table;
import br.ufu.scheduling.model.Chromosome;
import br.ufu.scheduling.model.Graph;
import br.ufu.scheduling.utils.Configuration;
import br.ufu.scheduling.utils.Crossover;
import br.ufu.scheduling.utils.Printer;

public class AEMMD {
	public static final int DOUBLE_TOURNAMENT= 2;

	private Random generator = new Random();
	private Configuration config;
	private Graph graph;

	private List<Table> tables = new ArrayList<>();
	private List<Chromosome> chromosomeInitialList = new ArrayList<>();

	private Table table1ForDoubleTournament;
	private Table table2ForDoubleTournament;

	private int initialPopulation;
	private int generationAccumulated;
	private int generationAccumulatedForApplyMutation;

	public AEMMD() {
		//For tests
	}

	public AEMMD(Configuration config, Graph graph) throws Exception {
		this.config = config;
		this.graph = graph;

		createTables();

		//TODO: no AMMED n�o existe tamanho das tabelas, logo verificar o tamanho da popula��o inicial
		initialPopulation = tables.size() * config.getSizeOfTables();
	}

	private void createTables() {
		if (config.getTotalObjectives() < 2 || config.getTotalObjectives() > 5) {
			throw new IllegalArgumentException("Invalid value to work with multi-objective algorithm. Valid values between 2 and 5.");
		}

		switch (config.getTotalObjectives()) {
		case 2:
			createTable(Arrays.asList(1, 2));
			break;

		case 3:
			createTable(Arrays.asList(1, 2));
			createTable(Arrays.asList(1, 3));
			createTable(Arrays.asList(2, 3));

			createTable(Arrays.asList(1, 2, 3));
			break;

		case 4:
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
			throw new IllegalArgumentException("Invalid value to work with multi-objective algorithm. Valid values between 2 and 5.");
		}
	}

	private void createTable(List<Integer> objectives) {
		Table table = new TableAEMMD(Integer.MAX_VALUE);
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
		initialize();

		while (generationAccumulated < config.getTotalGenerations()) {
			if (config.isPrintIterationsAndGenerations()) {
				System.out.println("############################\n");
				System.out.println("####### GENERATION: " + (generationAccumulated + 1) + " #######\n");
				System.out.println("############################\n");
			}

			executeAG();

			finalizeGeneration();
		}

		showResult(initialTime);
	}

	private void initialize() throws Exception {
		generationAccumulated = 0;
		generationAccumulatedForApplyMutation = 0;

		generateInitialPopulation();
	}

	private void generateInitialPopulation() throws Exception {
		for (int i = 1; i <= initialPopulation; i++) {
			Chromosome chromosome = new Chromosome(generator, graph, config);

			if (!chromosomeInitialList.contains(chromosome)) {
				addChromosomeToTables(chromosome);
				chromosomeInitialList.add(chromosome);
			}
		}

		addChromosomesFromInitialPopulationToNonDominatedTable(chromosomeInitialList);
	}

	private boolean addChromosomeToTables(Chromosome chromosome) throws Exception {
		boolean addedInSometable = false;

		for (int i = 0; i < tables.size(); i++) {
			boolean added = tables.get(i).add(chromosome, config);

			if (!addedInSometable) {
				addedInSometable = added; 
			}
		}

		return addedInSometable;
	}

	private void addChromosomesFromInitialPopulationToNonDominatedTable(List<Chromosome> chromosomeInitialList) throws Exception {
		List<Chromosome> dominatedChromosomeList = new ArrayList<>();

		externalLoop:
		for (int chromosomeAIndex = 0; chromosomeAIndex < chromosomeInitialList.size(); chromosomeAIndex++) {
			Chromosome chromosomeA = chromosomeInitialList.get(chromosomeAIndex);

			if (dominatedChromosomeList.contains(chromosomeA)) {
				continue;
			}

			for (int chromosomeBIndex = 0; chromosomeBIndex < chromosomeInitialList.size(); chromosomeBIndex++) {
				if (chromosomeAIndex == chromosomeBIndex) {
					continue;
				}

				Chromosome chromosomeB = chromosomeInitialList.get(chromosomeBIndex);

				if (chromosomeB.isChromosomeDominated(config, chromosomeA)) {
					dominatedChromosomeList.add(chromosomeB);
				}

				if (chromosomeA.isChromosomeDominated(config, chromosomeB)) {
					continue externalLoop;
				}
			}

			addChromosomeToNonDominatedTable(chromosomeA);
		}
	}

	private void addChromosomeToNonDominatedTable(Chromosome chromosome) throws Exception {
		Table nonDominatedTable = null;///tables.get(nonDominatedTableIndex);
		boolean isChromosomeDominated = false;

		int currentChromosomeIndex = 0;
		int totalChromosomes = nonDominatedTable.getTotalChromosomes();

		while (currentChromosomeIndex < totalChromosomes) {
			Chromosome chromosomeB = nonDominatedTable.getChromosomeFromIndex(currentChromosomeIndex);

			if (chromosomeB.isChromosomeDominated(config, chromosome)) {
				nonDominatedTable.remove(currentChromosomeIndex);
				totalChromosomes--;
			}

			if (chromosome.isChromosomeDominated(config, chromosomeB)) {
				isChromosomeDominated = true;
				break;
			}

			currentChromosomeIndex++;
		}

		if (!isChromosomeDominated) {
			nonDominatedTable.add(chromosome, config);
		}
	}

	private void executeAG() throws Exception {
		processTablesForDoubleTournament();

		Chromosome child = processPairSelection();
		applyMutation(child);

		if (addChromosomeToTables(child)) {
			processTableScore();
			addChromosomeChildToNonDominatedTable(child);
		}
	}

	private void processTablesForDoubleTournament() {
		table1ForDoubleTournament = raffleTableByDoubleTournament();
		table2ForDoubleTournament = raffleTableByDoubleTournament();
	}

	private Table raffleTableByDoubleTournament() {
		Table table = null;

		for (int tour = 0; tour < DOUBLE_TOURNAMENT; tour++) {
			int raffleIndex = raffleIndex(tables.size());

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
		return table.getChromosomeFromIndex(raffleIndex(table.getTotalChromosomes()));
	}

	private Chromosome getCrossoverChildren(Chromosome parent1, Chromosome parent2) {
		List<Chromosome> generatedChildren = Crossover.getCrossover(parent1, parent2, graph, generator, config);

		if (generatedChildren.size() == 0) {
			return generatedChildren.get(0);
		}

		return generatedChildren.get(raffleIndex(generatedChildren.size()));
	}

	private void applyMutation(Chromosome chromosome) {
		if (config.getTotalGenerationsToApplyMutation() > 0 && generationAccumulatedForApplyMutation > config.getTotalGenerationsToApplyMutation()) {
			chromosome.applyMutation(generator, graph, config);
			generationAccumulatedForApplyMutation = 1;
		}		
	}

	private void processTableScore() {
		table1ForDoubleTournament.addScore(1);
		table2ForDoubleTournament.addScore(1);
	}

	private void addChromosomeChildToNonDominatedTable(Chromosome child) throws Exception {
		boolean chromosomeChildIsDominated = false;

		//the non-dominated table will be processed only at the end
		externalLoop:
		for (int tableIndex = 0; tableIndex < tables.size() - 1; tableIndex++) {
			Table table = tables.get(tableIndex);

			for (int chromosomeIndex = 0; chromosomeIndex < table.getTotalChromosomes(); chromosomeIndex++) {
				Chromosome chromosomeB = table.getChromosomeFromIndex(chromosomeIndex);

				if (child.isChromosomeDominated(config, chromosomeB)) {
					chromosomeChildIsDominated = true;
					break externalLoop;
				}
			}
		}

		if (!chromosomeChildIsDominated) {
			addChromosomeToNonDominatedTable(child);
		}
	}

	private void finalizeGeneration() {
		generationAccumulatedForApplyMutation++;
		generationAccumulated++;
	}

	private void showResult(long initialTime) {
		if (config.isPrintComparisonNonDominatedChromosomes()) {
			Printer.printFinalResultForAEMMTWithComparedToNonDominated(config, tables, initialTime);
		} else {
			//Printer.printFinalResultForAEMMT(config, tables.get(nonDominatedTableIndex), initialTime);
		}
	}

	public static void main(String args[]) throws Exception {
		AEMMD a = new AEMMD();
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