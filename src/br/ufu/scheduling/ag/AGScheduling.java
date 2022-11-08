package br.ufu.scheduling.ag;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import br.ufu.scheduling.aemmd.AEMMD;
import br.ufu.scheduling.aemmt.AEMMT;
import br.ufu.scheduling.enums.SelectionType;
import br.ufu.scheduling.file.csv.GeneratorDifferentChromosome;
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
	public static final double 	BEST_LOAD_BALANCE 						= 1.085106383;
	public static final double 	BEST_FLOW_TIME 							= 80.0;
	public static final double 	BEST_COMMUNICATION_COST					= 0.0;
	public static final double  BEST_WAITING_TIME						= 9.0;
	public static final double  BEST_SLENGTH_PLUS_WAITING_TIME          = 25.0;

	private Random generator 				= new Random();
	private List<Chromosome> chromosomeList = new ArrayList<>();
	private List<Chromosome> parentList 	= new ArrayList<>();
	private List<Chromosome> childrenList 	= new ArrayList<>();

	private Configuration config;
	private Graph graph;
	private int[] roulette;
	private int totalChromosomeScoreForSorting;
	private Chromosome bestChromosomeFound;

	private boolean firstGeneration = true;
	private boolean findBestChromosomeInGeneration = false;

	//Variables for the average calculation
	private int totalSuccess;
	private double totalSLengthOfBestChromosomes;
	private double totalLoadBalanceOfBestChromosomes;
	private double totalFlowTimeOfBestChromosomes;
	private double totalCommunicationCostOfBestChromosomes;
	private double totalWaitingTimeOfBestChromosomes;
	private double totalSLengthPlusWaitingTime;
	private double totalFitnessOfBestChromosomes;
	private int totalNumberOfChromosomes;
	
	//Variables for the best and the worst solutions
	private double bestSlength = Double.MAX_VALUE;
	private double bestLoadBalance = Double.MAX_VALUE;
	private double bestFlowTime = Double.MAX_VALUE;
	private double bestCommunicationCost = Double.MAX_VALUE;
	private double bestWaitingTime = Double.MAX_VALUE;
	private double bestSLengthPlusWaitingTime = Double.MAX_VALUE;

    private double worstSlength = Double.MIN_VALUE;
    private double worstLoadBalance = Double.MIN_VALUE;
    private double worstFlowTime = Double.MIN_VALUE;
    private double worstCommunicationCost = Double.MIN_VALUE;
    private double worstWaitingTime = Double.MIN_VALUE;
    private double worstSLengthPlusWaitingTime = Double.MIN_VALUE;

	public AGScheduling() throws Exception {
		config = new Configuration();
		
		if (Configuration.USE_DEFAULT_GRAPH.equals(config.getTaskGraphFileName())) {
			graph = Graph.initializeGraph();
		} else {
			graph = Graph.initializeGraph(config);
		}
	}

	public void execute(long initialTime) throws Exception {
		if (config.isGenerateCsvFile()) {
			executeAGAndGenerateCsvFile();
			return;
		}

        switch (config.getAlgorithmType()) {
        case SINGLE_OBJECTIVE:
            executeStandarGeneticAlgorithm(initialTime);
            break;

        case NSGAII:
        case SPEA2:
            throw new IllegalArgumentException("Algoritm type not implemented.");

        case AEMMT:
            executeMultiObjectiveGeneticAlgorithmAEMMT(initialTime);
            break;

        case AEMMD:
            executeMultiObjectiveGeneticAlgorithmAEMMD(initialTime);
            break;

        default:
            throw new IllegalArgumentException("Algoritm type not implemented.");
        }
	}

	private void executeAGAndGenerateCsvFile() throws Exception {
		GeneratorDifferentChromosome generator = new GeneratorDifferentChromosome(this.generator, config, graph);
		generator.execute();
	}

	private void executeMultiObjectiveGeneticAlgorithmAEMMT(long initialTime) throws Exception {
		AEMMT aemmt = new AEMMT(config, graph);
		aemmt.execute(initialTime);
	}

    private void executeMultiObjectiveGeneticAlgorithmAEMMD(long initialTime) throws Exception {
        AEMMD aemmd = new AEMMD(config, graph);
        aemmd.execute(initialTime);
    }	

	private void executeStandarGeneticAlgorithm(long initialTime) throws Exception {
		int iteration = 0;

		while (iteration < config.getIterations()) {
			if (config.isPrintIterations()) {
				System.out.println("############################\n");
				System.out.println("####### ITERATION: " + (iteration + 1) + " #######\n");
				System.out.println("############################\n");
			}

			resetIteration();

			int generation = 0;

			while (generation < config.getGenerations() && !(config.isStopGenerationIfFindBestSolution() && findBestChromosomeInGeneration)) {
				if (config.isPrintGenerations()) {
					System.out.println("##### GENERATION: " + (generation + 1) + " #####\n");
				}

				resetGeneration();
				executeAG();
				processGenerationResult();

				generation++;
			}

			processIterationResult();
			populateAverageDataOfBestChromosomes(chromosomeList.get(INDEX_BEST_CHROMOSOME));

			iteration++;
		}

		showResult(initialTime);
		showBestResultsByObjectives();
	}

    private void resetIteration() {
		firstGeneration = true;
		chromosomeList.clear();
	}

	private void resetGeneration() {
		roulette = null;
		parentList.clear();
		childrenList.clear();
		findBestChromosomeInGeneration = false;
	}

	private void executeAG() throws Exception {
		if (firstGeneration) {
			for (int i = 0; i < config.getInitialPopulation(); i++) {
				Chromosome chromosome = new Chromosome(generator, graph, config);
				addChromosomeInGeneralList(chromosome);
			}

			firstGeneration = false;
		}

		executeSelection();
		selectBestChromosomesForReinsertion();
	}

	private void addChromosomeInGeneralList(Chromosome chromosome) {
		chromosomeList.add(chromosome);
		verifyBestAndWorstSolutions(chromosome);
	}

    private void addChromosomeInGeneralList(List<Chromosome> chromosomeList) {
		this.chromosomeList.addAll(chromosomeList);
		verifyBestAndWorstSolutions(chromosomeList);
	}

    private void verifyBestAndWorstSolutions(Chromosome chromosome) {
        if (config.isPrintBestResultsByObjectives()) {
            List<Chromosome> chromosomeList = new ArrayList<>();
            chromosomeList.add(chromosome);

            verifyBestAndWorstSolutions(chromosomeList);
        }
    }

    private void verifyBestAndWorstSolutions(List<Chromosome> chromosomeList) {
        if (config.isPrintBestResultsByObjectives()) {
            for (Chromosome chromosome : chromosomeList) {
                //Best Solutions
                if (chromosome.getSLength() < bestSlength) {
                    bestSlength = chromosome.getSLength();
                }

                if (chromosome.getLoadBalance() < bestLoadBalance) {
                    bestLoadBalance = chromosome.getLoadBalance();
                }

                if (chromosome.getFlowTime() < bestFlowTime) {
                    bestFlowTime = chromosome.getFlowTime();
                }

                if (chromosome.getCommunicationCost() < bestCommunicationCost) {
                    bestCommunicationCost = chromosome.getCommunicationCost();
                }

                if (chromosome.getWaitingTime() < bestWaitingTime) {
                    bestWaitingTime = chromosome.getWaitingTime();
                }

                if (chromosome.getSLengthPlusWaitingTime() < bestSLengthPlusWaitingTime) {
                    bestSLengthPlusWaitingTime = chromosome.getSLengthPlusWaitingTime();
                }

                //Worst Solutions
                if (chromosome.getSLength() > worstSlength) {
                    worstSlength = chromosome.getSLength();
                }

                if (chromosome.getLoadBalance() > worstLoadBalance) {
                    worstLoadBalance = chromosome.getLoadBalance();
                }

                if (chromosome.getFlowTime() > worstFlowTime) {
                    worstFlowTime = chromosome.getFlowTime();
                }

                if (chromosome.getCommunicationCost() > worstCommunicationCost) {
                    worstCommunicationCost = chromosome.getCommunicationCost();
                }

                if (chromosome.getWaitingTime() > worstWaitingTime) {
                    worstWaitingTime = chromosome.getWaitingTime();
                }

                if (chromosome.getSLengthPlusWaitingTime() > worstSLengthPlusWaitingTime) {
                    worstSLengthPlusWaitingTime = chromosome.getSLengthPlusWaitingTime();
                }
            }
        }
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
		case ROULETTE:
		case LINEAR_RANKING:
		case NON_LINEAR_RANKING:
			parent1 = raffleChromosomeByRoulette(null);
			parent2 = raffleChromosomeByRoulette(parent1);
			break;

		case SIMPLE_TOURNAMENT:
		case STOCHASTIC_TOURNAMENT:
			parent1 = raffleChromosomeByTournament(new ArrayList<>(chromosomeList), null);
			parent2 = raffleChromosomeByTournament(new ArrayList<>(chromosomeList), parent1);
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
			switch (config.getSelectionType()) {
			case LINEAR_RANKING:
				totalChromosomeScoreForSorting = getTotalPositionsForLinearRanking();
				roulette = populateRouletteForRanking(false);
				break;

			case NON_LINEAR_RANKING:
				totalChromosomeScoreForSorting = getTotalPositionsForNonLinearRanking();
				roulette = populateRouletteForRanking(true);
				break;

			default:
				totalChromosomeScoreForSorting = getTotalChromosomeScoreForSorting();
				roulette = populateRoulette();
				break;
			}
		}

		//If it's the first individual of the pair to be chosen, I'll raffle anyone
		//For the second individual of the pair, we will try x times until an individual different from the first is drawn, or we will use a repeated one even
		if (chromosomeAlreadyChosen == null) {
			chromosome = raffleChromosomeByRoulette(totalChromosomeScoreForSorting);
		} else {
			int currentAttemptSelectParentNotRepeated = 0;

			while (currentAttemptSelectParentNotRepeated < config.getAttemptSelectParentNotRepeated() && (chromosome == null || chromosomeAlreadyChosen.equals(chromosome))) {
				chromosome = raffleChromosomeByRoulette(totalChromosomeScoreForSorting);

				currentAttemptSelectParentNotRepeated++;
			}
		}

		return chromosome;
	}

	private int getTotalPositionsForLinearRanking() {
		int totalPositions = 0;
		int totalChromosomes = chromosomeList.size();

		// Sum of Gauss
		if (totalChromosomes % 2 == 0) {
			totalPositions = (totalChromosomes + 1) * (totalChromosomes / 2);
		} else {
			totalPositions = (totalChromosomes * (totalChromosomes / 2)) + totalChromosomes;
		}

		return totalPositions;
	}

	private int getTotalPositionsForNonLinearRanking() {
		int totalPositions = 0;

		for (int i = 1; i <= chromosomeList.size(); i++) {
			totalPositions += (int) Math.pow(i, 2);
		}

		return totalPositions;
	}

	private int getTotalChromosomeScoreForSorting() {
		return chromosomeList
				.stream()
				.mapToInt(Chromosome::getFitnessAdjusted)
				.sum();
	}

	private int [] populateRouletteForRanking(boolean isNonLinearRanking) {
		int [] roulette = new int[chromosomeList.size()];
		int amountPerChromosome = config.getInitialPopulation();
		int accumulatedValue = 0;

		chromosomeList.sort(new Comparator<Chromosome>() {
			@Override
			public int compare(Chromosome o1, Chromosome o2) {
				double o1Fitness = o1.getFitness();
				double o2Fitness = o2.getFitness();

				return o1Fitness > o2Fitness ? 1 : o1Fitness == o2Fitness ? 0 : -1;
			}
		});

		for (int chromsomeIndex = 0; chromsomeIndex < chromosomeList.size(); chromsomeIndex++) {
			accumulatedValue += (isNonLinearRanking ? (int) Math.pow(amountPerChromosome, 2) : amountPerChromosome);
			roulette[chromsomeIndex] = accumulatedValue; 
			amountPerChromosome--;
		}

		return roulette;
	}

	private int [] populateRoulette() {
		int [] roulette = new int[chromosomeList.size()];
		int accumulatedValue = 0;

		for (int chromsomeIndex = 0; chromsomeIndex < chromosomeList.size(); chromsomeIndex++) {
			Chromosome chromosome = chromosomeList.get(chromsomeIndex);
			accumulatedValue += chromosome.getFitnessAdjusted();
			roulette[chromsomeIndex] = accumulatedValue;
		}

		return roulette;
	}

	private Chromosome raffleChromosomeByRoulette(int totalChromosomeScoreForSorting) {
		int indexForSearch = raffleRouletteIndex(totalChromosomeScoreForSorting);
		return chromosomeList.get(getRealRouletteIndex(indexForSearch));
	}

	private int getRealRouletteIndex(int indexForSearch) {
		int begin = 0;
		int end = roulette.length - 1;
		int firstIndice = 0;
		

		while (begin <= end) {
			int middle = (begin + end) / 2;
			
			if (roulette[middle] == indexForSearch 
					|| (middle == firstIndice && roulette[middle] > indexForSearch) 
					|| (roulette[middle - 1] < indexForSearch && roulette[middle] > indexForSearch )) {
				return middle;
			}

			if (roulette[middle] < indexForSearch) { /* Item is in the sub-vector on the right */
				begin = middle + 1;
			} else { /* Item is in the sub-vector on the right */
				end = middle;
			}
		}

		throw new IllegalStateException("It was not possible to fetch the value found in Roulette: " + indexForSearch + ".");
	}

	private int raffleRouletteIndex(int rouletteLength) {
		return raffleIndex(rouletteLength);
	}

	private Chromosome raffleChromosomeByTournament(List<Chromosome> copyOfChromosomeList, Chromosome chromosomeAlreadyChosen) {
		Chromosome chromosome = null;

		if (config.getSelectionType() == SelectionType.STOCHASTIC_TOURNAMENT && roulette == null) {
			totalChromosomeScoreForSorting = getTotalChromosomeScoreForSorting();
			roulette = populateRoulette();
		}

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
			int chromosomeRaffledIndex = 0;

			if (config.getSelectionType() == SelectionType.STOCHASTIC_TOURNAMENT) {
				int indexForSearch = raffleRouletteIndex(totalChromosomeScoreForSorting);
				chromosomeRaffledIndex = getRealRouletteIndex(indexForSearch);
			} else {
				chromosomeRaffledIndex = raffleChromosomeIndexByTournament();
			}

			if (chromosome == null || chromosome.getFitness() > copyOfChromosomeList.get(chromosomeRaffledIndex).getFitness()) { 
				chromosome = copyOfChromosomeList.get(chromosomeRaffledIndex);
			}
		}

		return chromosome;
	}

	private int raffleChromosomeIndexByTournament() {
		return raffleIndex(config.getInitialPopulation());
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

	private void processGenerationResult() throws Exception {
		Chromosome bestChromosomeOfGeneration = chromosomeList.get(INDEX_BEST_CHROMOSOME);
		findBestChromosomeInGeneration = findBestChromosome(bestChromosomeOfGeneration);

		if (config.isPrintBestChromosomeOfGeneration()) {
			System.out.println("Best Chromosome of Generation: ");
			bestChromosomeOfGeneration.printChromosome();
		}

		updateBestChromosome(bestChromosomeOfGeneration);
	}

	private boolean findBestChromosome(Chromosome bestChromosome) {
		//If this flag is true, it has to reach the optimal solution
		if (config.isConvergenceForTheBestSolution()) {
			switch (config.getMetricType()) {
			case MAKESPAN:
				if (BEST_SLENGTH < bestChromosome.getSLength()) {
					return false;
				}
				break;

			case LOAD_BALANCE:
				if (BEST_LOAD_BALANCE < bestChromosome.getLoadBalance()) {
					return false;
				}
				break;

			case FLOW_TIME:
				if (BEST_FLOW_TIME < bestChromosome.getFlowTime()) {
					return false;
				}
				break;

			case COMMUNICATION_COST:
				if (BEST_COMMUNICATION_COST < bestChromosome.getCommunicationCost()) {
					return false;
				}
				break;

			case WAITING_TIME:
				if (BEST_WAITING_TIME < bestChromosome.getWaitingTime()) {
					return false;
				}
				break;

            case MAKESPAN_PLUS_WAITING_TIME:
                if (BEST_SLENGTH_PLUS_WAITING_TIME < bestChromosome.getSLengthPlusWaitingTime()) {
                    return false;
                }
                break;

			default:
				throw new IllegalArgumentException("Metric type not implemented.");
			}

			return true;
		}

		return false;
	}

	private void updateBestChromosome(Chromosome bestChromosome) {
		if (bestChromosomeFound == null || bestChromosome.getFitness() > bestChromosomeFound.getFitness()) {
			bestChromosomeFound = bestChromosome;
		}
	}

	private void populateAverageDataOfBestChromosomes(Chromosome chromosome) {
		totalSLengthOfBestChromosomes += chromosome.getSLength();
		totalLoadBalanceOfBestChromosomes += chromosome.getLoadBalance();
		totalFlowTimeOfBestChromosomes += chromosome.getFlowTime();
		totalCommunicationCostOfBestChromosomes += chromosome.getCommunicationCost();
		totalWaitingTimeOfBestChromosomes += chromosome.getWaitingTime();
		totalSLengthPlusWaitingTime += chromosome.getSLengthPlusWaitingTime();
		totalFitnessOfBestChromosomes += chromosome.getFitness();
		totalNumberOfChromosomes += 1;
	}

	private void processIterationResult() throws Exception {
		Chromosome bestChromosomeOfIteration = chromosomeList.get(INDEX_BEST_CHROMOSOME);

		if (findBestChromosome(bestChromosomeOfIteration)) {
			totalSuccess++;
		}

		updateBestChromosome(bestChromosomeOfIteration);
	}

	private void showResult(long initialTime) {
		FinalResultModel result = new FinalResultModel();
		result.setInitialTime(initialTime);
		result.setTotalSuccess(totalSuccess);
		result.setTotalSLength(totalSLengthOfBestChromosomes);
		result.setTotalLoadBalance(totalLoadBalanceOfBestChromosomes);
		result.setTotalFlowTime(totalFlowTimeOfBestChromosomes);
		result.setTotalCommunicationCost(totalCommunicationCostOfBestChromosomes);
		result.setTotalWaitingTime(totalWaitingTimeOfBestChromosomes);
		result.setTotalSLengthPlusWaitingTime(totalSLengthPlusWaitingTime);
		result.setTotalFitness(totalFitnessOfBestChromosomes);
		result.setTotalNumberOfChromosomes(totalNumberOfChromosomes);

		result.showResult(bestChromosomeFound, config);		
	}

    private void showBestResultsByObjectives() {
        if (config.isPrintBestResultsByObjectives()) {
            System.out.println("");
            System.out.println("##################################");
            System.out.println("## Best And Worst Results By Objectives ##");
            System.out.println("");
            System.out.println("Best SLength: " + bestSlength);
            System.out.println("Best LoadBalance: " + bestLoadBalance);
            System.out.println("Best FlowTime: " + bestFlowTime);
            System.out.println("Best CommunicationCost: " + bestCommunicationCost);
            System.out.println("Best WaitingTime: " + bestWaitingTime);
            System.out.println("Best SLengthPlusWaitingTime: " + bestSLengthPlusWaitingTime);
            System.out.println("");
            System.out.println("Worst SLength: " + worstSlength);
            System.out.println("Worst LoadBalance: " + worstLoadBalance);
            System.out.println("Worst FlowTime: " + worstFlowTime);
            System.out.println("Worst CommunicationCost: " + worstCommunicationCost);
            System.out.println("Worst WaitingTime: " + worstWaitingTime);
            System.out.println("Worst SLengthPlusWaitingTime: " + worstSLengthPlusWaitingTime);
            System.out.println("");
            System.out.println("Graph: " + (Configuration.USE_DEFAULT_GRAPH.equals(config.getTaskGraphFileName()) ? "Graph_Omara_Arafa" : config.getTaskGraphFileName()));
            System.out.println("Processors: " + config.getTotalProcessors());
            System.out.println("With CommunicationCost: " + (Configuration.USE_DEFAULT_GRAPH.equals(config.getTaskGraphFileName()) ? true : config.isGraphWithCommunicationCost()));
        }
    }
}