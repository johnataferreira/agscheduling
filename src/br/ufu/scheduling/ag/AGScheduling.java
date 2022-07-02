package br.ufu.scheduling.ag;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import br.ufu.scheduling.model.Chromosome;
import br.ufu.scheduling.model.FinalResultModel;
import br.ufu.scheduling.model.Graph;
import br.ufu.scheduling.utils.Configuration;
import br.ufu.scheduling.utils.Crossover;

public class AGScheduling {
	public static final double 	RANDOM_NUMBER_FIXED_IN_ARTICLE 			= 0.5;
	public static final String 	QUEBRA_LINHA 							= "\n";
	public static final int 	INDEX_BEST_CHROMOSOME					= 0;
	public static final double 	ADJUST_VALUE_FOR_FITNESS_IN_ROULLETE	= 1000.0;

	//Best result of metrics from the graph used
	public static final double 	BEST_SLENGTH 							= 16.0;
	public static final double 	BEST_LOAD_BALANCE 						= 1.0851063829787235;
	public static final double 	BEST_FLOW_TIME 							= 30.0;
	public static final double 	COMPUTATIONAL_COST						= 0.1;

	private Random generator 				= new Random();
	private List<Chromosome> chromosomeList = new ArrayList<>();
	private List<Chromosome> parentList 	= new ArrayList<>();
	private List<Chromosome> childrenList 	= new ArrayList<>();

	private Configuration config;
	private Graph graph;
	private int[] roulette;
	private Chromosome bestChromosomeFound;

	private boolean firstGeneration = true;
	private boolean findBestChromosome = false;

	//Variables for the average calculation
	private int totalSuccess = 0;
	private double totalSLenghtOfBestChromosomes;
	private double totalLoadBalanceOfBestChromosomes;
	private double totalFlowTimeOfBestChromosomes;
	private double totalFitnessOfBestChromosomes;
	private int totalNumberOfChromosomes;


	public AGScheduling() throws Exception {
		config = new Configuration();
		graph = Graph.initializeGraph();
	}

	public void execute(long initialTime) throws Exception {
		int iteration = 0;

		while (iteration < config.getIterations()) {
			if (config.isPrintIterationsAndGenerations()) {
				System.out.println("############################\n");
				System.out.println("####### ITERATION: " + (iteration + 1) + " #######\n");
				System.out.println("############################\n");
			}

			resetIteration();

			int generation = 0;

			while (generation < config.getGenerations() && !(config.isStopGenerationIfFindBestSolution() && findBestChromosome)) {
				if (config.isPrintIterationsAndGenerations()) {
					System.out.println("##### GENERATION: " + (generation + 1) + " #####\n");
				}

				resetGeneration();
				executeAG();

				generation++;
			}

			iteration++;

			if (findBestChromosome) {
				totalSuccess++;
			}

			populateAverageDataOfBestChromosomes(chromosomeList.get(INDEX_BEST_CHROMOSOME));
		}

		showResult(initialTime);
	}

	private void resetIteration() {
		firstGeneration = true;
		findBestChromosome = false;
		chromosomeList.clear();
	}

	private void resetGeneration() {
		roulette = null;
		parentList.clear();
		childrenList.clear();
	}

	private void executeAG() throws Exception {
		if (firstGeneration) {
			for (int i = 0; i < config.getInitialPopulation(); i++) {
				Chromosome Chromosome = new Chromosome(generator, graph, config);
				addChromosomeInGeneralList(Chromosome);
			}

			firstGeneration = false;
		}

		executeSelection();
		selectBestChromosomesForReinsertion();

		processGenerationResult();
	}

	private void addChromosomeInGeneralList(Chromosome chromosome) {
		chromosomeList.add(chromosome);
	}

	private void addChromosomeInGeneralList(List<Chromosome> chromosomeList) {
		this.chromosomeList.addAll(chromosomeList);
	}

	private void executeSelection() {
		for (int pair = 0; pair < getNumberOfChromosomesForSelection(); pair++) {
			processPairSelection();
		}

		applyMutationOnChildren();
	}

	private int getNumberOfChromosomesForSelection() {
		return (int) (config.getInitialPopulation() * config.getCrossoverRate() / 100 / 2);
	}

	private void processPairSelection() {
		Chromosome parent1 = null;
		Chromosome parent2 = null;

		switch (config.getSelectionType()) {
		case TOURNAMENT:
			parent1 = raffleChromosomeByTournament(new ArrayList<>(chromosomeList), null);
			parent2 = raffleChromosomeByTournament(new ArrayList<>(chromosomeList), parent1);
			break;

		case ROULETTE:
			parent1 = raffleChromosomeByRoulette(null); 
			parent2 = raffleChromosomeByRoulette(parent1);
			break;

		default:
			throw new IllegalArgumentException("Selection type not implemented.");
		}

		parentList.add(parent1);
		parentList.add(parent2);

		selectChildren(parent1, parent2);
	}

	private Chromosome raffleChromosomeByRoulette(Chromosome chromosomeAlreadyChosen) {
		Chromosome chromosome = null;

		if (roulette == null) {
			roulette = populateRoulette();
		}

		//If it's the first individual of the pair to be chosen, I'll raffle anyone
		//For the second individual of the pair, we will try x times until an individual different from the first is drawn, or we will use a repeated one even
		if (chromosomeAlreadyChosen == null) {
			chromosome = raffleChromosomeByRoulette();
		} else {
			int currentAttemptSelectParentNotRepeated = 0;

			while (currentAttemptSelectParentNotRepeated < config.getAttemptSelectParentNotRepeated() && (chromosome == null || chromosomeAlreadyChosen.equals(chromosome))) {
				chromosome = raffleChromosomeByRoulette();

				currentAttemptSelectParentNotRepeated++;
			}
		}

		return chromosome;
	}

	//TODO: rewrite this method
	private int [] populateRoulette() {
		int [] roulette = new int[getTotalChromosomeScoreForSorting()];

		int initialIndex = 0;
		int finalIndex = 0;

		for (int chromsomeIndex = 0; chromsomeIndex < chromosomeList.size(); chromsomeIndex++) {
			Chromosome chromosome = chromosomeList.get(chromsomeIndex);

			initialIndex = finalIndex;
			finalIndex = initialIndex + chromosome.getFitnessAdjusted();

			for (int rouletteIndex = initialIndex; rouletteIndex < finalIndex; rouletteIndex++) {
				roulette[rouletteIndex] = chromsomeIndex;
			}
		}

		return roulette;
	}

	private int getTotalChromosomeScoreForSorting() {
		return chromosomeList
				.stream()
				.mapToInt(Chromosome::getFitnessAdjusted)
				.sum();
	}

	private Chromosome raffleChromosomeByRoulette() {
		return chromosomeList.get(roulette[raffleRouletteIndex(roulette.length)]);
	}

	private int raffleRouletteIndex(int rouletteLength) {
		return raffleIndex(rouletteLength);
	}

	private Chromosome raffleChromosomeByTournament(List<Chromosome> copyOfChromosomeList, Chromosome chromosomeAlreadyChosen) {
		Chromosome chromosome = null;

		//If it's the first individual of the pair to be chosen, I'll raffle anyone
		//For the second individual of the pair, we will try x times until an individual different from the first is drawn, or we will use a repeated one even
		if (chromosomeAlreadyChosen == null) {
			chromosome = raffleChromosomeByTournament(new ArrayList<Chromosome>(copyOfChromosomeList));

		} else {
			int currentAttemptSelectParentNotRepeated = 0;

			while (currentAttemptSelectParentNotRepeated < config.getAttemptSelectParentNotRepeated() && (chromosome == null || chromosomeAlreadyChosen.equals(chromosome))) {
				chromosome = raffleChromosomeByTournament(new ArrayList<Chromosome>(copyOfChromosomeList));

				currentAttemptSelectParentNotRepeated++;
			}
		}

		return chromosome;
	}

	private Chromosome raffleChromosomeByTournament(List<Chromosome> copyOfChromosomeList) {
		Chromosome chromosome = null;

		for (int tour = 0; tour < config.getTourForTournament(); tour++) {
			int chromosomeRaffledIndex = raffleChromosomeIndexByTournament(tour);

			if (chromosome == null || chromosome.getFitness() > copyOfChromosomeList.get(chromosomeRaffledIndex).getFitness()) { 
				chromosome = copyOfChromosomeList.get(chromosomeRaffledIndex);
			}

			removeRaffledChromosome(copyOfChromosomeList, chromosomeRaffledIndex);
		}

		return chromosome;
	}
	
	private int raffleChromosomeIndexByTournament(int currentIndex) {
		return raffleIndex(config.getInitialPopulation() - currentIndex);
	}

	private void removeRaffledChromosome(List<Chromosome> copyOfChromosomeList, int chromosomeRaffledIndex) {
		copyOfChromosomeList.remove(chromosomeRaffledIndex);
	}

	private int raffleIndex(int limit) {
		return generator.nextInt(limit);
	}

	private void selectChildren(Chromosome parent1, Chromosome parent2) {
		childrenList.addAll(getCrossoverChildren(parent1, parent2));
	}

	private List<Chromosome> getCrossoverChildren(Chromosome parent1, Chromosome parent2) {
		List<Chromosome> generatedChildren = Crossover.getCrossover(parent1, parent2, graph, generator, config);
		addChromosomeInGeneralList(generatedChildren);

		return generatedChildren;
	}

	private void applyMutationOnChildren() {
		List<Integer> raffledIndexList = new ArrayList<>();

		for (int mutatedChromosomeIndex = 0; mutatedChromosomeIndex < getNumberOfChromosomesMutated(); mutatedChromosomeIndex++) {
			int raffledIndex = -1;

			if (!config.isAllowApplyingMutationOnRepeatedChild()) {
				do {
					raffledIndex = raffleIndex(childrenList.size());
				} while(raffledIndexList.contains(raffledIndex));

				raffledIndexList.add(raffledIndex);
			} else {
				raffledIndex = raffleIndex(childrenList.size());
			}

			applyMutation(childrenList.get(raffledIndex));
		}
	}

	private int getNumberOfChromosomesMutated() {
		return (int) Math.ceil(config.getInitialPopulation() * config.getMutationRate() / 100);
	}

	private void applyMutation(Chromosome chromosome) {
		chromosome.applyMutation(generator, graph, config);
	}

	private void selectBestChromosomesForReinsertion() throws Exception {
		sort(parentList);
		sort(childrenList);

		//ELITISM -> It will depend on how many children were generated, because depending on the crossover used, one or two children can be generated
		int totalChildrenGenerated = childrenList.size();
		int elitismParents = config.getInitialPopulation() - totalChildrenGenerated;

		List<Chromosome> chromosomeListForReinsertion = new ArrayList<>(parentList.subList(0, elitismParents));
		chromosomeListForReinsertion.addAll(new ArrayList<>(childrenList));

		chromosomeList = new ArrayList<>(chromosomeListForReinsertion);
		sort(chromosomeList);

		if (chromosomeList.size() != config.getInitialPopulation()) {
			throw new Exception("Invalid population size.");
		}
	}

	private void sort(List<Chromosome> chromosomeList) {
		//Descending ordering (higher fitness = better chromosome)
		chromosomeList.sort(new Comparator<Chromosome>() {
			@Override
			public int compare(Chromosome chromosome1, Chromosome chromosome2) {
				double fitnessChromosome1 = chromosome1.getFitness();
				double fitnessChromosome2 = chromosome2.getFitness();

				return fitnessChromosome1 > fitnessChromosome2 ? -1 
											 				   : fitnessChromosome1 == fitnessChromosome2 ? 0 
											 						   									  : 1;
			}
		});
	}

	private void populateAverageDataOfBestChromosomes(Chromosome chromosome) {
		totalSLenghtOfBestChromosomes += chromosome.getSLength();
		totalLoadBalanceOfBestChromosomes += chromosome.getLoadBalance();
		totalFlowTimeOfBestChromosomes += chromosome.getFlowTime();
		totalFitnessOfBestChromosomes += chromosome.getFitness();
		totalNumberOfChromosomes += 1;
	}

	private void processGenerationResult() throws Exception {
		Chromosome bestChromosomeOfGeneration = chromosomeList.get(INDEX_BEST_CHROMOSOME);

		//If this flag is true, it has to reach the optimal solution
		if (config.isConvergenceForTheBestSolution()) {
			switch (config.getMetricType()) {
			case MAKESPAN:
				if (BEST_SLENGTH < bestChromosomeOfGeneration.getSLength()) {
					return;
				}
				break;

			case LOAD_BALANCE:
				if (BEST_LOAD_BALANCE < bestChromosomeOfGeneration.getLoadBalance()) {
					return;
				}
				break;

			case FLOW_TIME:
				if (BEST_FLOW_TIME < bestChromosomeOfGeneration.getFlowTime()) {
					return;
				}
				break;

			default:
				throw new IllegalArgumentException("Metric type not implemented.");
			}

			findBestChromosome = true;
		}

		if (config.isPrintBestChromosomeOfGeneration()) {
			System.out.println("Best Chromosome of Generation: ");
			bestChromosomeOfGeneration.printChromosome();
		}

		if (bestChromosomeFound == null || bestChromosomeOfGeneration.getFitness() > bestChromosomeFound.getFitness()) {
			bestChromosomeFound = bestChromosomeOfGeneration;
		}
	}

	private void showResult(long initialTime) {
		FinalResultModel result = new FinalResultModel();
		result.setInitialTime(initialTime);
		result.setTotalSuccess(totalSuccess);
		result.setTotalSLenght(totalSLenghtOfBestChromosomes);
		result.setTotalLoadBalance(totalLoadBalanceOfBestChromosomes);
		result.setTotalFlowTime(totalFlowTimeOfBestChromosomes);
		result.setTotalFitness(totalFitnessOfBestChromosomes);
		result.setTotalNumberOfChromosomes(totalNumberOfChromosomes);

		result.showResult(bestChromosomeFound, config);		
	}
}