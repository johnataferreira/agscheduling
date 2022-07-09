package br.ufu.scheduling.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import br.ufu.scheduling.ag.AGScheduling;
import br.ufu.scheduling.enums.MetricType;
import br.ufu.scheduling.exceptions.BetterChromosomeFoundException;
import br.ufu.scheduling.utils.Configuration;
import br.ufu.scheduling.utils.Printer;

public class Metrics {
	private static final int MAXIMIZATION_PROBLEM = 0; 

	private double fitness;
	private double sLength; /* makespan */
	private double loadBalance;
	private double flowTime; /* sum of processor times */
	private double communicationCost;

	public Metrics() {
	}

	public double getFitness() {
		return fitness;
	}

	public double getSLength() {
		return sLength;
	}

	public double getLoadBalance() {
		return loadBalance;
	}

	public double getFlowTime() {
		return flowTime;
	}

	public double getCommunicationCost() {
		return communicationCost;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public void setsLength(double sLength) {
		this.sLength = sLength;
	}

	public void setLoadBalance(double loadBalance) {
		this.loadBalance = loadBalance;
	}

	public void setFlowTime(double flowTime) {
		this.flowTime = flowTime;
	}

	public void setCommunicationCost(double communicationCost) {
		this.communicationCost = communicationCost;
	}

	public int getFitnessAjusted() {
		return Long.valueOf(Math.round(Math.abs(fitness) * AGScheduling.ADJUST_VALUE_FOR_FITNESS_IN_ROULLETE)).intValue();
	}
	
	public void calculateMetrics(Graph graph, int[] mapping, int[] scheduling, Configuration config) {
		//To facilitate the calculation, we will not work with zero index for the auxiliary vectors created
		int [] startTimeTask = new int[graph.getNumberOfVertices() + 1]; 
		int [] finalTimeTask = new int[graph.getNumberOfVertices() + 1];
		int [] readinessTime = new int[config.getTotalProcessors() + 1]; 

		for (int taskIndex = 1; taskIndex <= graph.getNumberOfVertices(); taskIndex++) {
			//Need to subtract one because the scheduling/mapping vector starts from index 0
			int task = scheduling[taskIndex - 1];
			int processor = mapping[task - 1];

			startTimeTask[task] = Integer.max(readinessTime[processor], dat(graph, finalTimeTask, task, processor, mapping));
			finalTimeTask[task] = startTimeTask[task] + graph.getVertex(task).getComputationalCost();
			readinessTime[processor] = finalTimeTask[task];

			if (config.isTestMode()) {
				Printer.printExecutionOrder(startTimeTask, finalTimeTask, readinessTime, task, config.getTotalProcessors());
			}
		}

		calculateSLenght(finalTimeTask, config);
		calculateLoadBalance(readinessTime, config);
		calculateFlowTime(readinessTime, config);
		calculateCommunicationCost(0.0, config);
		calculateFitness(config);
	}

	private int dat(Graph graph, int [] finalTimeTask, int task, int processor, int[] mapping) {
		int max = 0;
		List<Integer> entries = graph.getVertex(task).getEntries();

		if (entries != null) {
			for (Integer entry : entries) {
				int resultCost = finalTimeTask[entry];

				//Need to subtract one because the mapping vector starts from index 0
				int parentTaskProcessor = mapping[entry - 1];

				if (processor != parentTaskProcessor) {
					resultCost += graph.getVertex(entry)
									.getAdjacency()
									.stream()
									.filter(edge -> edge.getSource().getTask() == entry && edge.getDestination().getTask() == task)
									.iterator()
									.next()
									.getComputationalCost();
				}

				if (max < resultCost) {
					max = resultCost;
				}
			}
		}

		return max;
	}

	private void calculateSLenght(int[] finalTimeTask, Configuration config) {
		sLength = maxValueFromVector(finalTimeTask);

		if (config.isConvergenceForTheBestSolution() && sLength < AGScheduling.BEST_SLENGTH) {
			throw new BetterChromosomeFoundException("We found a better chromosome than the last one found. SLength: " + sLength + ".");
		}
	}

	private int maxValueFromVector(int[] finalTimeTask) {
		return Arrays.stream(finalTimeTask)
				.max()
				.getAsInt();
	}

	private void calculateLoadBalance(int[] readinessTime, Configuration config) {
		double avg = (double) Arrays.stream(readinessTime)
								.boxed()
								.collect(Collectors.toList())
								.stream()
								.mapToInt(Integer::intValue)
								.sum() / config.getTotalProcessors(); 
		loadBalance = sLength / avg;

		if (config.isConvergenceForTheBestSolution() && loadBalance < AGScheduling.BEST_LOAD_BALANCE) {
			throw new BetterChromosomeFoundException("We found a better chromosome than the last one found. LoadBalance: " + loadBalance + ".");
		}
	}

	private void calculateFlowTime(int[] readinessTime, Configuration config) {
		flowTime = Arrays.stream(readinessTime)
					.boxed()
					.collect(Collectors.toList())
					.stream()
					.mapToInt(Integer::intValue)
					.sum();

		if (config.isConvergenceForTheBestSolution() && flowTime < AGScheduling.BEST_FLOW_TIME) {
			throw new BetterChromosomeFoundException("We found a better chromosome than the last one found. FlowTime: " + flowTime + ".");
		}		
	}

	private void calculateCommunicationCost(double accumulatedCommunicationCost, Configuration config) {
		communicationCost = accumulatedCommunicationCost;
		
		if (config.isConvergenceForTheBestSolution() && communicationCost < AGScheduling.BEST_COMMUNICATION_COST) {
			throw new BetterChromosomeFoundException("We found a better chromosome than the last one found. CommunicationCost: " + communicationCost + ".");
		}
	}

	private void calculateFitness(Configuration config) {
		if (config.getMaximizationConstant() == MAXIMIZATION_PROBLEM) {
			fitness = getMetricValue(config.getMetricType());
			return;
		}

		//Transform a minimization problem into a maximization problem 
		fitness = config.getMaximizationConstant() / getMetricValue(config.getMetricType());
	}

	private Double getMetricValue(MetricType metricType) {
		switch (metricType) {
		case MAKESPAN:
			return sLength;

		case LOAD_BALANCE:
			return loadBalance;

		case FLOW_TIME:
			return flowTime;

		default:
			throw new IllegalArgumentException("Metric type not implemented.");
		}
	}

	//FIXME: Not Finished!!!
	//idle = ocioso
	private void testSlots(Graph graph, int[] mapping, int[] scheduling, Integer totalProcessors, boolean testMode) {
		//To facilitate the calculation, we will not work with zero index for the auxiliary vectors created
		int [] startTimeTask = new int[graph.getNumberOfVertices() + 1]; 
		int [] finalTimeTask = new int[graph.getNumberOfVertices() + 1];
		int [] readinessTime = new int[totalProcessors + 1]; 

		int s_idle_time = 0;
		int e_idle_time = 0;

		int st_time = 0;
		List<Integer> idle_time = new ArrayList<>();

		for (int taskIndex = 1; taskIndex <= graph.getNumberOfVertices(); taskIndex++) {
			int min_ST = Integer.MAX_VALUE;
			int this_ST = 0;

			for (int processor = 1; processor <= totalProcessors; processor++) {
				//Need to subtract one because the mapping vector starts from index 0
				if (mapping[taskIndex - 1] == processor) {
					startTimeTask[taskIndex] = Integer.max(readinessTime[processor], dat(graph, finalTimeTask, taskIndex, processor, mapping));
					finalTimeTask[taskIndex] = startTimeTask[taskIndex] + graph.getVertex(taskIndex).getComputationalCost();
					readinessTime[processor] = finalTimeTask[taskIndex];

					this_ST = startTimeTask[taskIndex]; 

					if (this_ST < min_ST) {
						min_ST = this_ST; 
					}
				}

				//FIXME: I cannot understand when this list is fulfilled. The article is not clear about this.
				if (!idle_time.isEmpty()) {
					for (int time_slot : idle_time) {
						if ((e_idle_time - s_idle_time) >= graph.getVertex(taskIndex).getComputationalCost()
								&& dat(graph, finalTimeTask, taskIndex, processor, mapping) < s_idle_time) {
							
						}
					}
				}
			}

			if (st_time < min_ST) {
				min_ST = st_time;
			}
		}
	}

	public static void main(String[] args) {
		//For tests

		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("README.conf")));
			String linha = "";

			while ((linha = br.readLine()) != null) {
				System.out.println(linha);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}