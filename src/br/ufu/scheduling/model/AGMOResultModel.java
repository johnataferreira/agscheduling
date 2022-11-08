package br.ufu.scheduling.model;

import br.ufu.scheduling.utils.Configuration;

public class AGMOResultModel {
    private static final int MAKESPAN = 0;
    private static final int LOAD_BALANCE = 1;
    private static final int FLOW_TIME = 2;
    private static final int COMMUNICATION_COST = 3;
    private static final int WAITING_TIME = 4;

    private int totalChromossomes;

    private int totalBestSLenght;
    private int totalBestLoadBalance;
    private int totalBestFlowTime;
    private int totalBestCommunicationCost;
    private int totalBestWaitingTime;

    private double bestSLenght;
    private double bestLoadBalance;
    private double bestFlowTime;
    private double bestCommunicationCost;
    private double bestWaitingTime;

    private double worstSLenght;
    private double worstLoadBalance;
    private double worstFlowTime;
    private double worstCommunicationCost;
    private double worstWaitingTime;

    private double diffBetweenMaxAndMinSLenght;
    private double diffBetweenMaxAndMinLoadBalance;
    private double diffBetweenMaxAndMinFlowTime;
    private double diffBetweenMaxAndMinCommunicationCost;
    private double diffBetweenMaxAndMinWaitingTime;

    private int totalSLenghtValueBetween0_25;
    private int totalSLenghtValueBetween25_50;
    private int totalSLenghtValueBetween50_75;
    private int totalSLenghtValueBetween75_100;

    private int totalLoadBalanceValueBetween0_25;
    private int totalLoadBalanceValueBetween25_50;
    private int totalLoadBalanceValueBetween50_75;
    private int totalLoadBalanceValueBetween75_100;

    private int totalFlowTimeValueBetween0_25;
    private int totalFlowTimeValueBetween25_50;
    private int totalFlowTimeValueBetween50_75;
    private int totalFlowTimeValueBetween75_100;

    private int totalCommunicationCostValueBetween0_25;
    private int totalCommunicationCostValueBetween25_50;
    private int totalCommunicationCostValueBetween50_75;
    private int totalCommunicationCostValueBetween75_100;

    private int totalWaitingTimeValueBetween0_25;
    private int totalWaitingTimeValueBetween25_50;
    private int totalWaitingTimeValueBetween50_75;
    private int totalWaitingTimeValueBetween75_100;

	public AGMOResultModel(Configuration config, int totalChromossomes) {
	    this.totalChromossomes = totalChromossomes;

	    handleValueFromObjective(config, MAKESPAN);
	    handleValueFromObjective(config, LOAD_BALANCE);
	    handleValueFromObjective(config, FLOW_TIME);
	    handleValueFromObjective(config, COMMUNICATION_COST);
	    handleValueFromObjective(config, WAITING_TIME);
	}

	private void handleValueFromObjective(Configuration config, int objective) {
	    double bestObjectiveValue = 0.0;
	    double worstObjectiveValue = 0.0;

	    if (config.getObjective1() == objective) {
	        bestObjectiveValue = config.getRealObjectiveValue(config.getMaxObjectiveValue1());
	        worstObjectiveValue = config.getRealObjectiveValue(config.getMinObjectiveValue1());

	    } else if (config.getObjective2() == objective) {
            bestObjectiveValue = config.getRealObjectiveValue(config.getMaxObjectiveValue2());
            worstObjectiveValue = config.getRealObjectiveValue(config.getMinObjectiveValue2());

	    } else if (config.getObjective3() == objective) {
            bestObjectiveValue = config.getRealObjectiveValue(config.getMaxObjectiveValue3());
            worstObjectiveValue = config.getRealObjectiveValue(config.getMinObjectiveValue3());

	    } else if (config.getObjective4() == objective) {
            bestObjectiveValue = config.getRealObjectiveValue(config.getMaxObjectiveValue4());
            worstObjectiveValue = config.getRealObjectiveValue(config.getMinObjectiveValue4());

	    } else if (config.getObjective5() == objective) {
            bestObjectiveValue = config.getRealObjectiveValue(config.getMaxObjectiveValue5());
            worstObjectiveValue = config.getRealObjectiveValue(config.getMinObjectiveValue5());	        
	    }

	    switch (objective) {
            case MAKESPAN:
                bestSLenght = bestObjectiveValue;
                worstSLenght = worstObjectiveValue;
                diffBetweenMaxAndMinSLenght = Math.abs(bestSLenght - worstSLenght);
                break;

            case LOAD_BALANCE:
                bestLoadBalance = bestObjectiveValue;
                worstLoadBalance = worstObjectiveValue;
                diffBetweenMaxAndMinLoadBalance = Math.abs(bestLoadBalance - worstLoadBalance);
                break;

            case FLOW_TIME:
                bestFlowTime = bestObjectiveValue;
                worstFlowTime = worstObjectiveValue;
                diffBetweenMaxAndMinFlowTime = Math.abs(bestFlowTime - worstFlowTime);
                break;

            case COMMUNICATION_COST:
                bestCommunicationCost = bestObjectiveValue;
                worstCommunicationCost = worstObjectiveValue;
                diffBetweenMaxAndMinCommunicationCost = Math.abs(bestCommunicationCost - worstCommunicationCost);
                break;

            case WAITING_TIME:
                bestWaitingTime = bestObjectiveValue;
                worstWaitingTime = worstObjectiveValue;
                diffBetweenMaxAndMinWaitingTime = Math.abs(bestWaitingTime - worstWaitingTime);
                break;

            default:
                throw new IllegalArgumentException("Objective invalid.");
        } 
	}
	
    public void processChromosome(Chromosome chromosome) {
        processSLenght(chromosome.getSLength());
        processLoadBalance(chromosome.getLoadBalance());
        processFlowTime(chromosome.getFlowTime());
        processCommunicationCost(chromosome.getCommunicationCost());
        processWaitingTime(chromosome.getWaitingTime());
    }

    private void processSLenght(double sLenght) {
        TotalCalculation calculation = processTotalCalculation(sLenght, bestSLenght, diffBetweenMaxAndMinSLenght);

        totalBestSLenght += calculation.totalBest;
        totalSLenghtValueBetween0_25 += calculation.totalValueBetween0_25;
        totalSLenghtValueBetween25_50 += calculation.totalValueBetween25_50;
        totalSLenghtValueBetween50_75 += calculation.totalValueBetween50_75;
        totalSLenghtValueBetween75_100 += calculation.totalValueBetween75_100;
    }

    private void processLoadBalance(double loadBalance) {
        TotalCalculation calculation = processTotalCalculation(loadBalance, bestLoadBalance, diffBetweenMaxAndMinLoadBalance);

        totalBestLoadBalance += calculation.totalBest;
        totalLoadBalanceValueBetween0_25 += calculation.totalValueBetween0_25;
        totalLoadBalanceValueBetween25_50 += calculation.totalValueBetween25_50;
        totalLoadBalanceValueBetween50_75 += calculation.totalValueBetween50_75;
        totalLoadBalanceValueBetween75_100 += calculation.totalValueBetween75_100;
    }

    private void processFlowTime(double flowTime) {
        TotalCalculation calculation = processTotalCalculation(flowTime, bestFlowTime, diffBetweenMaxAndMinFlowTime);

        totalBestFlowTime = calculation.totalBest;
        totalFlowTimeValueBetween0_25 = +calculation.totalValueBetween0_25;
        totalFlowTimeValueBetween25_50 = +calculation.totalValueBetween25_50;
        totalFlowTimeValueBetween50_75 = +calculation.totalValueBetween50_75;
        totalFlowTimeValueBetween75_100 = +calculation.totalValueBetween75_100;
    }

    private void processCommunicationCost(double communicationCost) {
        TotalCalculation calculation = processTotalCalculation(communicationCost, bestCommunicationCost, diffBetweenMaxAndMinCommunicationCost);

        totalBestCommunicationCost += calculation.totalBest;
        totalCommunicationCostValueBetween0_25 += calculation.totalValueBetween0_25;
        totalCommunicationCostValueBetween25_50 += calculation.totalValueBetween25_50;
        totalCommunicationCostValueBetween50_75 += calculation.totalValueBetween50_75;
        totalCommunicationCostValueBetween75_100 += calculation.totalValueBetween75_100;
    }

    private void processWaitingTime(double waitingTime) {
        TotalCalculation calculation = processTotalCalculation(waitingTime, bestWaitingTime, diffBetweenMaxAndMinWaitingTime);

        totalBestWaitingTime += calculation.totalBest;
        totalWaitingTimeValueBetween0_25 += calculation.totalValueBetween0_25;
        totalWaitingTimeValueBetween25_50 += calculation.totalValueBetween25_50;
        totalWaitingTimeValueBetween50_75 += calculation.totalValueBetween50_75;
        totalWaitingTimeValueBetween75_100 += calculation.totalValueBetween75_100;
    }

    private TotalCalculation processTotalCalculation(double objectiveValue, double best, double diffBetweenMaxAndMin) {
        TotalCalculation calculation = new TotalCalculation();

        if (objectiveValue <= best) {
            calculation.totalBest++;
        }

        if (objectiveValue < (best + diffBetweenMaxAndMin)) {
            calculation.totalValueBetween75_100++;

        } else if (objectiveValue < (best + (2 * diffBetweenMaxAndMin))) {
            calculation.totalValueBetween50_75++;

        } else if (objectiveValue < (best + (3 * diffBetweenMaxAndMin))) {
            calculation.totalValueBetween25_50++;

        } else {
            calculation.totalValueBetween0_25++;            
        }

        return calculation;
    }

    public void showResult() {
        System.out.println("##################################");
        System.out.println("######## General Analysis ########");
        System.out.println("");

        System.out.println("## SLenght ##");
        System.out.println("Total Best SLenght Founded: " + totalBestSLenght + " | " + (totalBestSLenght * 100 / totalChromossomes) + "%");
        System.out.println("");

        System.out.println("SLenght Between Min and Max: [" + bestSLenght + " - " + worstSLenght + "]");
        System.out.println("0-25: " + totalSLenghtValueBetween0_25 + " | " + (totalSLenghtValueBetween0_25 * 100 / totalChromossomes) + "%");
        System.out.println("25-50: " + totalSLenghtValueBetween25_50 + " | " + (totalSLenghtValueBetween25_50 * 100 / totalChromossomes) + "%");
        System.out.println("50-75: " + totalSLenghtValueBetween50_75 + " | " + (totalSLenghtValueBetween50_75 * 100 / totalChromossomes) + "%");
        System.out.println("75-100: " + totalSLenghtValueBetween75_100 + " | " + (totalSLenghtValueBetween75_100 * 100 / totalChromossomes) + "%");
    }

    class TotalCalculation {
        int totalBest;
        int totalValueBetween0_25;
        int totalValueBetween25_50;
        int totalValueBetween50_75;
        int totalValueBetween75_100;
    }
}
