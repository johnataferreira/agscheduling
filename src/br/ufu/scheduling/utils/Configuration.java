package br.ufu.scheduling.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.Arrays;

import br.ufu.scheduling.enums.MetricType;
import br.ufu.scheduling.enums.MutationType;
import br.ufu.scheduling.enums.SelectionType;

public class Configuration {
	public final static String READ_ME_FILE_NAME = "README.conf"; 

	private Integer initialPopulation;
	private Double mutationRate;
	private Double crossoverRate;
	private Integer iterations;
	private Integer generations;
	private MetricType metricType;
	private MutationType mutationType;
	private SelectionType selectionType;
	private Integer tourForTournament;
	private Integer totalProcessors;
	private Boolean printIterationsAndGenerations;
	private Boolean testMode;
	private Integer maximizationConstant;
	private Integer attemptSelectParentNotRepeated;
	private Boolean stopGenerationIfFindBestSolution;
	private Boolean allowApplyingMutationOnRepeatedChild;
	private Boolean printBestChromosomeOfGeneration;
	private Boolean convergenceForTheBestSolution;
	private String taskGraphFileName;
	private Boolean graphWithCommunicationCost;
	private Boolean printGraphAtTheBeginningOfRun;

	//AGMO
	private Boolean executeMultiObjectiveGA;
	private Integer totalObjectives;
	private Integer sizeOfTables;
	private Integer sizeOfNonDominatedTable;
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

	//CSV File
	private Boolean generateCsvFile;
	private Integer totalDifferentChromosomes;
	private Integer maximumAttemptsGenerateDifferentChromosomes;

	//Auxiliaries
	private int metric;
	private int mutation;
	private int selection;

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

	public Integer getTourForTournament() {
		return tourForTournament;
	}

	public Integer getTotalProcessors() {
		return totalProcessors;
	}

	public Boolean isPrintIterationsAndGenerations() {
		return printIterationsAndGenerations;
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

	public Boolean isPrintGraphAtTheBeginningOfRun() {
		return printGraphAtTheBeginningOfRun;
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

		default:
			throw new IllegalArgumentException("Invalid value of metric: " + metric + ". Valid values: " + Arrays.asList(0, 1, 2, 3).toString());
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
			throw new IllegalArgumentException("Invalid value of selection: " + selection + ". Valid values: " + Arrays.asList(0, 1).toString());	
		}
	}

	private void setTourForTournament(Integer tourForTournament) {
		this.tourForTournament = tourForTournament;
	}

	private void setPrintIterationsAndGenerations(Boolean printIterationsAndGenerations) {
		this.printIterationsAndGenerations = printIterationsAndGenerations;
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

	private void setPrintGraphAtTheBeginningOfRun(Boolean printGraphAtTheBeginningOfRun) {
		this.printGraphAtTheBeginningOfRun = printGraphAtTheBeginningOfRun;
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

	public void setObjective1(Integer objective1) {
		this.objective1 = objective1;
	}

	public void setObjective2(Integer objective2) {
		this.objective2 = objective2;
	}

	public void setObjective3(Integer objective3) {
		this.objective3 = objective3;
	}

	public void setObjective4(Integer objective4) {
		this.objective4 = objective4;
	}

	public void setObjective5(Integer objective5) {
		this.objective5 = objective5;
	}

	public void setWeight1(Double weight1) {
		this.weight1 = weight1;
	}

	public void setWeight2(Double weight2) {
		this.weight2 = weight2;
	}

	public void setWeight3(Double weight3) {
		this.weight3 = weight3;
	}

	public void setWeight4(Double weight4) {
		this.weight4 = weight4;
	}

	public void setWeight5(Double weight5) {
		this.weight5 = weight5;
	}

	public void setGenerateCsvFile(Boolean generateCsvFile) {
		this.generateCsvFile = generateCsvFile;
	}

	public void setTotalDifferentChromosomes(Integer totalDifferentChromosomes) {
		this.totalDifferentChromosomes = totalDifferentChromosomes;
	}

	public void setMaximumAttemptsGenerateDifferentChromosomes(Integer maximumAttemptsGenerateDifferentChromosomes) {
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
		} catch (Exception e) {
			Exception e2 = new Exception("Error loading README.conf configuration file: " + e.getMessage());
			e2.initCause(e);
			throw e2;
		}
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

	public static void main(String args[]) throws Exception {
		Configuration config = new Configuration();
		System.out.println("Teste");
		
		System.out.println(Arrays.asList(0, 1, 2).toString());
	}
}
