package br.ufu.scheduling.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Arrays;


import br.ufu.scheduling.enums.AlgorithmType;
import br.ufu.scheduling.enums.MetricType;
import br.ufu.scheduling.enums.MutationType;
import br.ufu.scheduling.enums.SelectionType;
import br.ufu.scheduling.file.normalization.with.cost.LoaderNormalizationWithCost;
import br.ufu.scheduling.file.normalization.without.cost.LoaderNormalizationWithoutCost;

public class Configuration {
	public static final String USE_DEFAULT_GRAPH = "-1";
	public static final int    MAXIMIZATION_PROBLEM = 0;
	public final static String READ_ME_FILE_NAME = "README.conf";
	public final static String NORMALIZATION_BASE_FILE_NAME = "DAGBase-normalization.txt";
	public final static String SUFIX_NORMLIZATION_FILE_NAME = "-normalization";
	public final static String PACKAGE_BASE = "br" + File.separator + "ufu" + File.separator + "scheduling" + File.separator + "file";
	public final static String PACKAGE_NORMALIZATION_WITH_COST =  PACKAGE_BASE + File.separator + "normalization" + File.separator + "with" + File.separator + "cost" + File.separator;
	public final static String PACKAGE_NORMALIZATION_WITHOUT_COST = PACKAGE_BASE + File.separator + "normalization" + File.separator + "without" + File.separator + "cost" + File.separator;
    public final static String PACKAGE_DAG_WITH_COST =  PACKAGE_BASE + File.separator + "dag" + File.separator + "with" + File.separator + "cost" + File.separator;
    public final static String PACKAGE_DAG_WITHOUT_COST =  PACKAGE_BASE + File.separator + "dag" + File.separator + "without" + File.separator + "cost" + File.separator;
    public final static String PACKAGE_CSV = PACKAGE_BASE + File.separator + "csv" + File.separator;

	private Integer initialPopulation;
	private Double mutationRate;
	private Double crossoverRate;
	private Integer iterations;
	private Integer generations;
	private MetricType metricType;
	private MutationType mutationType;
	private SelectionType selectionType;
	private AlgorithmType algorithmType;
	private Integer tourForTournament;
	private Integer totalProcessors;
	private Boolean printIterations;
	private Boolean printGenerations;
	private Boolean testMode;
	private Integer maximizationConstant;
	private Integer attemptSelectParentNotRepeated;
	private Boolean stopGenerationIfFindBestSolution;
	private Boolean allowApplyingMutationOnRepeatedChild;
	private Boolean printBestChromosomeOfGeneration;
	private Boolean convergenceForTheBestSolution;
	private String taskGraphFileName;
	private Boolean graphWithCommunicationCost;
	private Boolean generateRandomCommunicationCostForNoCostDag;
	private Boolean generateCommunicationCostFromDAGWithoutCommunicationCost;
	private Boolean printGraphAtTheBeginningOfRun;
	private Boolean printBestResultsByObjectives;

	//AGMO
	private Boolean executeMultiObjectiveGA;
	private Integer totalObjectives;
	private Integer sizeOfTables;
	private Integer sizeOfNonDominatedTable;
	private Integer totalGenerations;
	private Integer totalGenerationsToResetTableScore;
	private Boolean printComparisonNonDominatedChromosomes;
	private Integer totalGenerationsToApplyMutation;
	private Boolean useWeightToCalculateAverageFunction;
	private Boolean calculateMaximusAndMinimusForNormalization;
	private Integer objective1;
	private Integer objective2;
	private Integer objective3;
	private Integer objective4;
	private Integer objective5;
	private Double weight1;
	private Double weight2;
	private Double weight3;
	private Double weight4;
	private Double weight5;

	//AGMO Normalization Data
	private Double maxObjectiveValue1;
	private Double minObjectiveValue1;
	private Double maxObjectiveValue2;
	private Double minObjectiveValue2;
	private Double maxObjectiveValue3;
	private Double minObjectiveValue3;
	private Double maxObjectiveValue4;
	private Double minObjectiveValue4;
	private Double maxObjectiveValue5;
	private Double minObjectiveValue5;
	
    private Double realMaxObjectiveValue1;
    private Double realMinObjectiveValue1;
    private Double realMaxObjectiveValue2;
    private Double realMinObjectiveValue2;
    private Double realMaxObjectiveValue3;
    private Double realMinObjectiveValue3;
    private Double realMaxObjectiveValue4;
    private Double realMinObjectiveValue4;
    private Double realMaxObjectiveValue5;
    private Double realMinObjectiveValue5;

	//CSV File
	private Boolean generateCsvFile;
	private Integer totalDifferentChromosomes;
	private Integer maximumAttemptsGenerateDifferentChromosomes;

	//Auxiliaries
	private int metric;
	private int mutation;
	private int selection;
	private int algorithm;

	public Configuration() throws Exception {
		readConfiguration();
	}

	public Integer getInitialPopulation() {
		return initialPopulation;
	}

	public Double getMutationRate() {
		return mutationRate;
	}

	public Double getCrossoverRate() {
		return crossoverRate;
	}

	public Integer getIterations() {
		return iterations;
	}

	public Integer getGenerations() {
		return generations;
	}

	public MetricType getMetricType() {
		return metricType;
	}

	public MutationType getMutationType() {
		return mutationType;
	}

	public SelectionType getSelectionType() {
		return selectionType;
	}

	public AlgorithmType getAlgorithmType() {
	    return algorithmType;
	}

	public Integer getTourForTournament() {
		return tourForTournament;
	}

	public Integer getTotalProcessors() {
		return totalProcessors;
	}

	public Boolean isPrintIterations() {
		return printIterations;
	}

    public Boolean isPrintGenerations() {
        return printGenerations;
    }

	public Boolean isTestMode() {
		return testMode;
	}

	public Integer getMaximizationConstant() {
		return maximizationConstant;
	}

	public Integer getAttemptSelectParentNotRepeated() {
		return attemptSelectParentNotRepeated;
	}

	public Boolean isStopGenerationIfFindBestSolution() {
		return stopGenerationIfFindBestSolution;
	}

	public Boolean isAllowApplyingMutationOnRepeatedChild() {
		return allowApplyingMutationOnRepeatedChild;
	}

	public Boolean isPrintBestChromosomeOfGeneration() {
		return printBestChromosomeOfGeneration;
	}

	public Boolean isConvergenceForTheBestSolution() {
		return convergenceForTheBestSolution;
	}

	public String getTaskGraphFileName() {
		return taskGraphFileName;
	}

	public Boolean isGraphWithCommunicationCost() {
		return graphWithCommunicationCost;
	}

	public Boolean isGenerateRandomCommunicationCostForNoCostDag() {
        return generateRandomCommunicationCostForNoCostDag;
    }

    public Boolean isGenerateCommunicationCostFromDAGWithoutCommunicationCost() {
        return generateCommunicationCostFromDAGWithoutCommunicationCost;
    }

    public Boolean isPrintGraphAtTheBeginningOfRun() {
		return printGraphAtTheBeginningOfRun;
	}

	public Boolean isPrintBestResultsByObjectives() {
        return printBestResultsByObjectives;
    }

    public Boolean isExecuteMultiObjectiveGA() {
		return executeMultiObjectiveGA;
	}

	public Integer getTotalObjectives() {
		return totalObjectives;
	}

	public Integer getSizeOfTables() {
		return sizeOfTables;
	}

	public Integer getSizeOfNonDominatedTable() {
		return sizeOfNonDominatedTable;
	}

	public Integer getTotalGenerations() {
		return totalGenerations;
	}

	public Integer getTotalGenerationsToResetTableScore() {
		return totalGenerationsToResetTableScore;
	}

	public Boolean isPrintComparisonNonDominatedChromosomes() {
		return printComparisonNonDominatedChromosomes;
	}

	public Integer getTotalGenerationsToApplyMutation() {
		return totalGenerationsToApplyMutation;
	}

	public Boolean isUseWeightToCalculateAverageFunction() {
		return useWeightToCalculateAverageFunction;
	}

	public Boolean isCalculateMaximusAndMinimusForNormalization() {
		return calculateMaximusAndMinimusForNormalization;
	}

    public Integer getObjective1() {
		return objective1;
	}

	public Integer getObjective2() {
		return objective2;
	}

	public Integer getObjective3() {
		return objective3;
	}

	public Integer getObjective4() {
		return objective4;
	}

	public Integer getObjective5() {
		return objective5;
	}

	public Double getWeight1() {
		return weight1;
	}

	public Double getWeight2() {
		return weight2;
	}

	public Double getWeight3() {
		return weight3;
	}

	public Double getWeight4() {
		return weight4;
	}

	public Double getWeight5() {
		return weight5;
	}

	public Double getMaxObjectiveValue1() {
		return maxObjectiveValue1;
	}

	public Double getMinObjectiveValue1() {
		return minObjectiveValue1;
	}

	public Double getMaxObjectiveValue2() {
		return maxObjectiveValue2;
	}

	public Double getMinObjectiveValue2() {
		return minObjectiveValue2;
	}

	public Double getMaxObjectiveValue3() {
		return maxObjectiveValue3;
	}

	public Double getMinObjectiveValue3() {
		return minObjectiveValue3;
	}

	public Double getMaxObjectiveValue4() {
		return maxObjectiveValue4;
	}

	public Double getMinObjectiveValue4() {
		return minObjectiveValue4;
	}

	public Double getMaxObjectiveValue5() {
		return maxObjectiveValue5;
	}

	public Double getMinObjectiveValue5() {
		return minObjectiveValue5;
	}

    public Double getRealMaxObjectiveValue1() {
        return realMaxObjectiveValue1;
    }

    public Double getRealMinObjectiveValue1() {
        return realMinObjectiveValue1;
    }

    public Double getRealMaxObjectiveValue2() {
        return realMaxObjectiveValue2;
    }

    public Double getRealMinObjectiveValue2() {
        return realMinObjectiveValue2;
    }

    public Double getRealMaxObjectiveValue3() {
        return realMaxObjectiveValue3;
    }

    public Double getRealMinObjectiveValue3() {
        return realMinObjectiveValue3;
    }

    public Double getRealMaxObjectiveValue4() {
        return realMaxObjectiveValue4;
    }

    public Double getRealMinObjectiveValue4() {
        return realMinObjectiveValue4;
    }

    public Double getRealMaxObjectiveValue5() {
        return realMaxObjectiveValue5;
    }

    public Double getRealMinObjectiveValue5() {
        return realMinObjectiveValue5;
    }

	public Boolean isGenerateCsvFile() {
		return generateCsvFile;
	}

	public Integer getTotalDifferentChromosomes() {
		return totalDifferentChromosomes;
	}

	public Integer getMaximumAttemptsGenerateDifferentChromosomes() {
		return maximumAttemptsGenerateDifferentChromosomes;
	}

	//The sets methods are used by the Java Reflection lib to populate the configuration
	private void setInitialPopulation(Integer initialPopulation) {
		this.initialPopulation = initialPopulation;
	}

	private void setMutationRate(Double mutationRate) {
		this.mutationRate = mutationRate;
	}

	private void setCrossoverRate(Double crossoverRate) {
		this.crossoverRate = crossoverRate;
	}
	
	private void setIterations(Integer iterations) {
		this.iterations = iterations;
	}
	
	private void setGenerations(Integer generations) {
		this.generations = generations;
	}

	private void setMetric(Integer metric) {
		this.metric = metric;
		setMetricType(metric);
	}

	private void setMetricType(Integer metric) {
		switch (metric) {
		case 0:
			metricType = MetricType.MAKESPAN;
			break;

		case 1:
			metricType = MetricType.LOAD_BALANCE;
			break;

		case 2:
			metricType = MetricType.FLOW_TIME;
			break;

		case 3:
			metricType = MetricType.COMMUNICATION_COST;
			break;

		case 4:
			metricType = MetricType.WAITING_TIME;
			break;

        case 5:
            metricType = MetricType.MAKESPAN_PLUS_WAITING_TIME;
            break;

		default:
			throw new IllegalArgumentException("Invalid value of metric: " + metric + ". Valid values: " + Arrays.asList(0, 1, 2, 3, 4, 5).toString());
		}
	}

	private void setMutation(Integer mutation) {
		this.mutation = mutation;
		setMutationType(mutation);
	}

	private void setMutationType(Integer mutation) {
		switch (mutation) {
		case 0:
			mutationType = MutationType.ONE_POINT;
			break;

		case 1:
			mutationType = MutationType.TWO_POINTS;
			break;

		default:
			throw new IllegalArgumentException("Invalid value of mutation: " + mutation + ". Valid values: " + Arrays.asList(0, 1).toString());	
		}		
	}

	private void setSelection(Integer selection) {
		this.selection = selection;
		setSelectionType(selection);
	}

	private void setSelectionType(Integer selection) {
		switch (selection) {
		case 0:
			selectionType = SelectionType.ROULETTE;
			break;

		case 1:
			selectionType = SelectionType.SIMPLE_TOURNAMENT;
			break;

		case 2:
			selectionType = SelectionType.STOCHASTIC_TOURNAMENT;
			break;

		case 3:
			selectionType = SelectionType.LINEAR_RANKING;
			break;

		case 4:
			selectionType = SelectionType.NON_LINEAR_RANKING;
			break;

		default:
			throw new IllegalArgumentException("Invalid value of selection: " + selection + ". Valid values: " + Arrays.asList(0, 1, 2, 3, 4).toString());	
		}
	}

    private void setAlgorithm(Integer algorithm) {
        this.algorithm = algorithm;
        setAlgorithmType(algorithm);
    }

	private void setAlgorithmType(Integer algorithm) {
        switch (algorithm) {
        case 0:
            algorithmType = AlgorithmType.SINGLE_OBJECTIVE;
            break;

        case 1:
            algorithmType = AlgorithmType.NSGAII;
            break;

        case 2:
            algorithmType = AlgorithmType.SPEA2;
            break;

        case 3:
            algorithmType = AlgorithmType.AEMMT;
            break;

        case 4:
            algorithmType = AlgorithmType.AEMMD;
            break;

        default:
            throw new IllegalArgumentException("Invalid value of selection: " + algorithm + ". Valid values: " + Arrays.asList(0, 1, 2, 3, 4).toString());   
        }        
    }

    private void setTourForTournament(Integer tourForTournament) {
		this.tourForTournament = tourForTournament;
	}

	private void setPrintIterations(Boolean printIterations) {
		this.printIterations = printIterations;
	}

    private void setPrintGenerations(Boolean printGenerations) {
        this.printGenerations = printGenerations;
    }

	private void setTotalProcessors(Integer totalProcessors) {
		this.totalProcessors = totalProcessors;
	}

	private void setTestMode(Boolean testMode) {
		this.testMode = testMode;
	}

	private void setMaximizationConstant(Integer maximizationConstant) {
		this.maximizationConstant = maximizationConstant;
	}

	private void setAttemptSelectParentNotRepeated(Integer attemptSelectParentNotRepeated) {
		this.attemptSelectParentNotRepeated = attemptSelectParentNotRepeated;
	}

	private void setStopGenerationIfFindBestSolution(Boolean stopGenerationIfFindBestSolution) {
		this.stopGenerationIfFindBestSolution = stopGenerationIfFindBestSolution;
	}

	private void setAllowApplyingMutationOnRepeatedChild(Boolean allowApplyingMutationOnRepeatedChild) {
		this.allowApplyingMutationOnRepeatedChild = allowApplyingMutationOnRepeatedChild;
	}

	private void setPrintBestChromosomeOfGeneration(Boolean printBestChromosomeOfGeneration) {
		this.printBestChromosomeOfGeneration = printBestChromosomeOfGeneration;
	}

	private void setConvergenceForTheBestSolution(Boolean convergenceForTheBestSolution) {
		this.convergenceForTheBestSolution = convergenceForTheBestSolution;
	}

	private void setTaskGraphFileName(String taskGraphFileName) {
		this.taskGraphFileName = taskGraphFileName;
	}

	private void setGraphWithCommunicationCost(Boolean graphWithCommunicationCost) {
		this.graphWithCommunicationCost = graphWithCommunicationCost;
	}

    private void setGenerateRandomCommunicationCostForNoCostDag(Boolean generateRandomCommunicationCostForNoCostDag) {
        this.generateRandomCommunicationCostForNoCostDag = generateRandomCommunicationCostForNoCostDag;
    }

    private void setGenerateCommunicationCostFromDAGWithoutCommunicationCost(Boolean generateCommunicationCostFromDAGWithoutCommunicationCost) {
        this.generateCommunicationCostFromDAGWithoutCommunicationCost = generateCommunicationCostFromDAGWithoutCommunicationCost;
    }

	private void setPrintGraphAtTheBeginningOfRun(Boolean printGraphAtTheBeginningOfRun) {
		this.printGraphAtTheBeginningOfRun = printGraphAtTheBeginningOfRun;
	}

    private void setPrintBestResultsByObjectives(Boolean printBestResultsByObjectives) {
        this.printBestResultsByObjectives = printBestResultsByObjectives;
    }

	public void setExecuteMultiObjectiveGA(Boolean executeMultiObjectiveGA) {
		this.executeMultiObjectiveGA = executeMultiObjectiveGA;
	}

	private void setTotalObjectives(Integer totalObjectives) {
		this.totalObjectives = totalObjectives;
	}

	private void setSizeOfTables(Integer sizeOfTables) {
		this.sizeOfTables= sizeOfTables;
	}

	private void setSizeOfNonDominatedTable(Integer sizeOfNonDominatedTable) {
		this.sizeOfNonDominatedTable= sizeOfNonDominatedTable;
	}

	private void setTotalGenerations(Integer totalGenerations) {
		this.totalGenerations = totalGenerations;
	}

	private void setTotalGenerationsToResetTableScore(Integer totalGenerationsToResetTableScore) {
		this.totalGenerationsToResetTableScore = totalGenerationsToResetTableScore;
	}

	private void setPrintComparisonNonDominatedChromosomes(Boolean printComparisonNonDominatedChromosomes) {
		this.printComparisonNonDominatedChromosomes = printComparisonNonDominatedChromosomes;
	}

	private void setTotalGenerationsToApplyMutation(Integer totalGenerationsToApplyMutation) {
		this.totalGenerationsToApplyMutation = totalGenerationsToApplyMutation;
	}

	private void setUseWeightToCalculateAverageFunction(Boolean useWeightToCalculateAverageFunction) {
		this.useWeightToCalculateAverageFunction = useWeightToCalculateAverageFunction;
	}

	private void setCalculateMaximusAndMinimusForNormalization(Boolean calculateMaximusAndMinimusForNormalization) {
		this.calculateMaximusAndMinimusForNormalization = calculateMaximusAndMinimusForNormalization;
	}

	private void setObjective1(Integer objective1) {
		this.objective1 = objective1;
	}

	private void setObjective2(Integer objective2) {
		this.objective2 = objective2;
	}

	private void setObjective3(Integer objective3) {
		this.objective3 = objective3;
	}

	private void setObjective4(Integer objective4) {
		this.objective4 = objective4;
	}

	private void setObjective5(Integer objective5) {
		this.objective5 = objective5;
	}

	private void setWeight1(Double weight1) {
		this.weight1 = weight1;
	}

	private void setWeight2(Double weight2) {
		this.weight2 = weight2;
	}

	private void setWeight3(Double weight3) {
		this.weight3 = weight3;
	}

	private void setWeight4(Double weight4) {
		this.weight4 = weight4;
	}

	private void setWeight5(Double weight5) {
		this.weight5 = weight5;
	}

	private void setMaxObjectiveValue1(Double maxObjectiveValue1) {
		this.maxObjectiveValue1 = getTransformedObjectiveValue(maxObjectiveValue1);
		setRealMaxObjectiveValue1(maxObjectiveValue1);
	}

    private void setMinObjectiveValue1(Double minObjectiveValue1) {
		this.minObjectiveValue1 = getTransformedObjectiveValue(minObjectiveValue1);
		setRealMinObjectiveValue1(minObjectiveValue1);
	}

	private void setMaxObjectiveValue2(Double maxObjectiveValue2) {
		this.maxObjectiveValue2 = getTransformedObjectiveValue(maxObjectiveValue2);
		setRealMaxObjectiveValue2(maxObjectiveValue2);
	}

	private void setMinObjectiveValue2(Double minObjectiveValue2) {
		this.minObjectiveValue2 = getTransformedObjectiveValue(minObjectiveValue2);
		setRealMinObjectiveValue2(minObjectiveValue2);
	}

	private void setMaxObjectiveValue3(Double maxObjectiveValue3) {
		this.maxObjectiveValue3 = getTransformedObjectiveValue(maxObjectiveValue3);
		setRealMaxObjectiveValue3(maxObjectiveValue3);
	}

	private void setMinObjectiveValue3(Double minObjectiveValue3) {
		this.minObjectiveValue3 = getTransformedObjectiveValue(minObjectiveValue3);
		setRealMinObjectiveValue3(minObjectiveValue3);
	}

	private void setMaxObjectiveValue4(Double maxObjectiveValue4) {
		this.maxObjectiveValue4 = getTransformedObjectiveValue(maxObjectiveValue4);
		setRealMaxObjectiveValue4(maxObjectiveValue4);
	}

	private void setMinObjectiveValue4(Double minObjectiveValue4) {
		this.minObjectiveValue4 = getTransformedObjectiveValue(minObjectiveValue4);
		setRealMinObjectiveValue4(minObjectiveValue4);
	}

	private void setMaxObjectiveValue5(Double maxObjectiveValue5) {
		this.maxObjectiveValue5 = getTransformedObjectiveValue(maxObjectiveValue5);
		setRealMaxObjectiveValue5(maxObjectiveValue5);
	}

	private void setMinObjectiveValue5(Double minObjectiveValue5) {
		this.minObjectiveValue5 = getTransformedObjectiveValue(minObjectiveValue5);
		setRealMinObjectiveValue5(minObjectiveValue5);
	}

    private void setRealMaxObjectiveValue1(Double realMaxObjectiveValue1) {
        this.realMaxObjectiveValue1 = realMaxObjectiveValue1;
    }

    private void setRealMinObjectiveValue1(Double realMinObjectiveValue1) {
        this.realMinObjectiveValue1 = realMinObjectiveValue1;
    }

    private void setRealMaxObjectiveValue2(Double realMaxObjectiveValue2) {
        this.realMaxObjectiveValue2 = realMaxObjectiveValue2;
    }

    private void setRealMinObjectiveValue2(Double realMinObjectiveValue2) {
        this.realMinObjectiveValue2 = realMinObjectiveValue2;
    }

    private void setRealMaxObjectiveValue3(Double realMaxObjectiveValue3) {
        this.realMaxObjectiveValue3 = realMaxObjectiveValue3;
    }

    private void setRealMinObjectiveValue3(Double realMinObjectiveValue3) {
        this.realMinObjectiveValue3 = realMinObjectiveValue3;
    }

    private void setRealMaxObjectiveValue4(Double realMaxObjectiveValue4) {
        this.realMaxObjectiveValue4 = realMaxObjectiveValue4;
    }

    private void setRealMinObjectiveValue4(Double realMinObjectiveValue4) {
        this.realMinObjectiveValue4 = realMinObjectiveValue4;
    }

    private void setRealMaxObjectiveValue5(Double realMaxObjectiveValue5) {
        this.realMaxObjectiveValue5 = realMaxObjectiveValue5;
    }

    private void setRealMinObjectiveValue5(Double realMinObjectiveValue5) {
        this.realMinObjectiveValue5 = realMinObjectiveValue5;
    }

	private void setGenerateCsvFile(Boolean generateCsvFile) {
		this.generateCsvFile = generateCsvFile;
	}

	private void setTotalDifferentChromosomes(Integer totalDifferentChromosomes) {
		this.totalDifferentChromosomes = totalDifferentChromosomes;
	}

	private void setMaximumAttemptsGenerateDifferentChromosomes(Integer maximumAttemptsGenerateDifferentChromosomes) {
		this.maximumAttemptsGenerateDifferentChromosomes = maximumAttemptsGenerateDifferentChromosomes;
	}

	private void readConfiguration() throws Exception {
		try (BufferedReader buffer = new BufferedReader(new FileReader(new File(READ_ME_FILE_NAME)))) {
			String line = null;

			while ((line = buffer.readLine()) != null) {
				if (line.startsWith("#")) continue;

				//0 - FieldName
				//1 - Type (int, double, String)
				//2 - Value
				String[] vector = line.split(":");
				Method method = this.getClass().getDeclaredMethod(getMethodName(vector[0]), getParameterType(vector[1]));
				method.invoke(this, getConvertedValue(vector[2], vector[1]));
			}

			if (getAlgorithmType() != AlgorithmType.SINGLE_OBJECTIVE && !isUseWeightToCalculateAverageFunction()) {
				readConfigurationForDataNormalization();
				
				if (getMaximizationConstant() != Configuration.MAXIMIZATION_PROBLEM) {
					invertMaximumAndMinimumObjectiveValues();
				}
			}
		} catch (Exception e) {
			Exception e2 = new Exception("Error loading README.conf configuration file: " + e.getMessage());
			e2.initCause(e);
			throw e2;
		}
	}

	private void readConfigurationForDataNormalization() throws Exception {
		String fileName;

		if (USE_DEFAULT_GRAPH.equals(getTaskGraphFileName())) {
			fileName = NORMALIZATION_BASE_FILE_NAME;
		} else {
			fileName = getTaskGraphFileName().split(".stg")[0] + SUFIX_NORMLIZATION_FILE_NAME + ".txt";
		} 

		Class cls = null;
		String packagePath = null;

		if (USE_DEFAULT_GRAPH.equals(getTaskGraphFileName()) || isGraphWithCommunicationCost()) {
		    cls = LoaderNormalizationWithCost.class;
		    packagePath = PACKAGE_NORMALIZATION_WITH_COST;
		} else {
		    cls = LoaderNormalizationWithoutCost.class;
		    packagePath = PACKAGE_NORMALIZATION_WITHOUT_COST;
		}

		try (BufferedReader buffer = new BufferedReader(new InputStreamReader(cls.getClassLoader().getResourceAsStream(packagePath + fileName)))) {
			String line = null;

			while ((line = buffer.readLine()) != null) {
				if (line.startsWith("#")) continue;

				//0 - FieldName
				//1 - Type (int, double, String)
				//2 - Value
				String[] vector = line.split(":");
				Method method = this.getClass().getDeclaredMethod(getMethodName(vector[0]), getParameterType(vector[1]));
				method.invoke(this, getConvertedValue(vector[2], vector[1]));
			}
		} catch (Exception e) {
			Exception e2 = new Exception("Error loading " + fileName + " file: " + e.getMessage());
			e2.initCause(e);
			throw e2;
		}
	}

	private void invertMaximumAndMinimumObjectiveValues() {
		double aux = maxObjectiveValue1;
		this.maxObjectiveValue1 = minObjectiveValue1;
		this.minObjectiveValue1 = aux;

		aux = maxObjectiveValue2;
		this.maxObjectiveValue2 = minObjectiveValue2;
		this.minObjectiveValue2 = aux;

		aux = maxObjectiveValue3;
		this.maxObjectiveValue3 = minObjectiveValue3;
		this.minObjectiveValue3 = aux;

		aux = maxObjectiveValue4;
		this.maxObjectiveValue4 = minObjectiveValue4;
		this.minObjectiveValue4 = aux;

		aux = maxObjectiveValue5;
		this.maxObjectiveValue5 = minObjectiveValue5;
		this.minObjectiveValue5 = aux;
	}

	private String getMethodName(String fieldName) {
		return "set" + fieldName.toUpperCase().charAt(0) + fieldName.substring(1); 
	}

	private Class<?> getParameterType(String type) {
		switch (type) {
		case "int":
			return Integer.class;

		case "double":
			return Double.class;

		case "boolean":
			return Boolean.class;

		case "string":
			return String.class;

		default:
			throw new IllegalArgumentException("Invalid type of field: " + type + "!");
		}
	}

	private Object getConvertedValue(String value, String type) {
		switch (type) {
		case "int":
			return Integer.parseInt(value);

		case "double":
			return Double.parseDouble(value);

		case "boolean":
			return Boolean.parseBoolean(value);

		case "string":
			return value; 

		default:
			throw new IllegalArgumentException("Invalid type of field: " + type + "!");
		}
	}

	public double getTransformedObjectiveValue(double objectiveValue) {
		if (getMaximizationConstant() == Configuration.MAXIMIZATION_PROBLEM) {
			return objectiveValue;
		}

		//Transform a minimization problem into a maximization problem 
		return objectiveValue > 0.0 ? getMaximizationConstant() / (double) objectiveValue : 0.0;
	}

	public static void main(String args[]) throws Exception {
		Configuration config = new Configuration();
		System.out.println("Teste");
		
		System.out.println(Arrays.asList(0, 1, 2).toString());
	}
}