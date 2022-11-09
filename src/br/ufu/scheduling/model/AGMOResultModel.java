package br.ufu.scheduling.model;

import br.ufu.scheduling.utils.Configuration;

public class AGMOResultModel {
    private static final int MAKESPAN = 0;
    private static final int LOAD_BALANCE = 1;
    private static final int FLOW_TIME = 2;
    private static final int COMMUNICATION_COST = 3;
    private static final int WAITING_TIME = 4;
    
    private static final int SOLUTION_RANGE = 4;

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

    private BestResultByObjective bestResult;

	public AGMOResultModel(Configuration config, int totalChromossomes) {
	    this.totalChromossomes = totalChromossomes;
	    this.bestResult = new BestResultByObjective(config);

	    handleValueFromObjective(config, MAKESPAN);
	    handleValueFromObjective(config, LOAD_BALANCE);
	    handleValueFromObjective(config, FLOW_TIME);
	    handleValueFromObjective(config, COMMUNICATION_COST);
	    handleValueFromObjective(config, WAITING_TIME);
	}

	private void handleValueFromObjective(Configuration config, int objective) {
	    double bestObjectiveValue = 0.0;
	    double worstObjectiveValue = 0.0;

	    //The lower the values, the better the chromosome, so best has the best value.
	    if (config.getObjective1() == objective) {
	        bestObjectiveValue = config.getRealMinObjectiveValue1();
	        worstObjectiveValue = config.getRealMaxObjectiveValue1();

	    } else if (config.getObjective2() == objective) {
            bestObjectiveValue = config.getRealMinObjectiveValue2();
            worstObjectiveValue = config.getRealMaxObjectiveValue2();

	    } else if (config.getObjective3() == objective) {
            bestObjectiveValue = config.getRealMinObjectiveValue3();
            worstObjectiveValue = config.getRealMaxObjectiveValue3();

	    } else if (config.getObjective4() == objective) {
            bestObjectiveValue = config.getRealMinObjectiveValue4();
            worstObjectiveValue = config.getRealMaxObjectiveValue4();

	    } else if (config.getObjective5() == objective) {
            bestObjectiveValue = config.getRealMinObjectiveValue5();
            worstObjectiveValue = config.getRealMaxObjectiveValue5();	        
	    }

	    switch (objective) {
            case MAKESPAN:
                bestSLenght = bestObjectiveValue;
                worstSLenght = worstObjectiveValue;
                diffBetweenMaxAndMinSLenght = Math.abs(bestSLenght - worstSLenght) / SOLUTION_RANGE;
                break;

            case LOAD_BALANCE:
                bestLoadBalance = bestObjectiveValue;
                worstLoadBalance = worstObjectiveValue;
                diffBetweenMaxAndMinLoadBalance = Math.abs(bestLoadBalance - worstLoadBalance) / SOLUTION_RANGE;
                break;

            case FLOW_TIME:
                bestFlowTime = bestObjectiveValue;
                worstFlowTime = worstObjectiveValue;
                diffBetweenMaxAndMinFlowTime = Math.abs(bestFlowTime - worstFlowTime);
                break;

            case COMMUNICATION_COST:
                bestCommunicationCost = bestObjectiveValue;
                worstCommunicationCost = worstObjectiveValue;
                diffBetweenMaxAndMinCommunicationCost = Math.abs(bestCommunicationCost - worstCommunicationCost) / SOLUTION_RANGE;
                break;

            case WAITING_TIME:
                bestWaitingTime = bestObjectiveValue;
                worstWaitingTime = worstObjectiveValue;
                diffBetweenMaxAndMinWaitingTime = Math.abs(bestWaitingTime - worstWaitingTime) / SOLUTION_RANGE;
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

        bestResult.verifyBestAndWorstSolutions(chromosome);
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

        totalBestFlowTime += calculation.totalBest;
        totalFlowTimeValueBetween0_25 += calculation.totalValueBetween0_25;
        totalFlowTimeValueBetween25_50 += calculation.totalValueBetween25_50;
        totalFlowTimeValueBetween50_75 += calculation.totalValueBetween50_75;
        totalFlowTimeValueBetween75_100 += calculation.totalValueBetween75_100;
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

        TotalCalculation totalCalculation = new TotalCalculation();

        totalCalculation.totalBest = totalBestSLenght;
        totalCalculation.totalValueBetween75_100 = totalSLenghtValueBetween75_100;
        totalCalculation.totalValueBetween50_75 = totalSLenghtValueBetween50_75;
        totalCalculation.totalValueBetween25_50 = totalSLenghtValueBetween25_50;
        totalCalculation.totalValueBetween0_25 = totalSLenghtValueBetween0_25;
        printObjectiveFormatted("SLenght", totalCalculation, bestSLenght, worstSLenght, diffBetweenMaxAndMinSLenght);

        totalCalculation.totalBest = totalBestLoadBalance;
        totalCalculation.totalValueBetween75_100 = totalLoadBalanceValueBetween75_100;
        totalCalculation.totalValueBetween50_75 = totalLoadBalanceValueBetween50_75;
        totalCalculation.totalValueBetween25_50 = totalLoadBalanceValueBetween25_50;
        totalCalculation.totalValueBetween0_25 = totalLoadBalanceValueBetween0_25;
        printObjectiveFormatted("LoadBalance", totalCalculation, bestLoadBalance, worstLoadBalance, diffBetweenMaxAndMinLoadBalance);

        totalCalculation.totalBest = totalBestFlowTime;
        totalCalculation.totalValueBetween75_100 = totalFlowTimeValueBetween75_100;
        totalCalculation.totalValueBetween50_75 = totalFlowTimeValueBetween50_75;
        totalCalculation.totalValueBetween25_50 = totalFlowTimeValueBetween25_50;
        totalCalculation.totalValueBetween0_25 = totalFlowTimeValueBetween0_25;
        printObjectiveFormatted("FlowTime", totalCalculation, bestFlowTime, worstFlowTime, diffBetweenMaxAndMinFlowTime);

        totalCalculation.totalBest = totalBestCommunicationCost;
        totalCalculation.totalValueBetween75_100 = totalCommunicationCostValueBetween75_100;
        totalCalculation.totalValueBetween50_75 = totalCommunicationCostValueBetween50_75;
        totalCalculation.totalValueBetween25_50 = totalCommunicationCostValueBetween25_50;
        totalCalculation.totalValueBetween0_25 = totalCommunicationCostValueBetween0_25;
        printObjectiveFormatted("CommunicationCost", totalCalculation, bestCommunicationCost, worstCommunicationCost, diffBetweenMaxAndMinCommunicationCost);

        totalCalculation.totalBest = totalBestWaitingTime;
        totalCalculation.totalValueBetween75_100 = totalWaitingTimeValueBetween75_100;
        totalCalculation.totalValueBetween50_75 = totalWaitingTimeValueBetween50_75;
        totalCalculation.totalValueBetween25_50 = totalWaitingTimeValueBetween25_50;
        totalCalculation.totalValueBetween0_25 = totalWaitingTimeValueBetween0_25;
        printObjectiveFormatted("WaitingTime", totalCalculation, bestWaitingTime, worstWaitingTime, diffBetweenMaxAndMinWaitingTime);

        bestResult.showResult();
    }

    private void printObjectiveFormatted(String objectiveName, TotalCalculation totalCalculation, double bestValue, double worstValue, double diffBetweenMaxAndMin) {
        System.out.println("");
        System.out.println("## " + objectiveName + "##");

        double percTotalBestObjective = Math.round((totalCalculation.totalBest * 100.0 / totalChromossomes) * 100.0) / 100.0;
        System.out.println("Total Best " + objectiveName + " Founded: " + totalCalculation.totalBest + " -> " + percTotalBestObjective + "%");
        System.out.println("");

        double percTotalValueBetween75_100 = Math.round((totalCalculation.totalValueBetween75_100 * 100.0 / totalChromossomes) * 100.0) / 100.0;
        double percTotalValueBetween50_75 = Math.round((totalCalculation.totalValueBetween50_75 * 100.0 / totalChromossomes) * 100.0) / 100.0;
        double percTotalValueBetween25_50 = Math.round((totalCalculation.totalValueBetween25_50 * 100.0 / totalChromossomes) * 100.0) / 100.0;
        double percTotalValueBetween0_25 = Math.round((totalCalculation.totalValueBetween0_25 * 100.0 / totalChromossomes) * 100.0) / 100.0;

        double bottomLimit75_100 = bestValue;
        double topLimit75_100 = bestValue + diffBetweenMaxAndMin;

        double bottomLimit50_75 = topLimit75_100;
        double topLimit50_75 = bestValue + 2 * diffBetweenMaxAndMin;

        double bottomLimit25_50 = topLimit50_75;
        double topLimit25_50 = bestValue + 3 * diffBetweenMaxAndMin;

        double bottomLimit0_25 = topLimit25_50;
        double topLimit0_25 = bestValue + 4 * diffBetweenMaxAndMin;

        System.out.println(objectiveName + " Between Min and Max: [" + bestValue + ", " + worstValue + "]");
        System.out.println("75-100 [" + bottomLimit75_100 + ", " + topLimit75_100 + "): " + totalCalculation.totalValueBetween75_100 + " -> " + percTotalValueBetween75_100 + "%");
        System.out.println("50-75 [" + bottomLimit50_75 + ", " + topLimit50_75 + "): " + totalCalculation.totalValueBetween50_75 + " -> " + percTotalValueBetween50_75 + "%");
        System.out.println("25-50 [" + bottomLimit25_50 + ", " + topLimit25_50 + "): " + totalCalculation.totalValueBetween25_50 + " -> " + percTotalValueBetween25_50 + "%");
        System.out.println("0-25 [" + bottomLimit0_25 + ", " + topLimit0_25 + "]: " + totalCalculation.totalValueBetween0_25 + " -> " + percTotalValueBetween0_25 + "%");
    }

    class TotalCalculation {
        int totalBest;
        int totalValueBetween75_100;
        int totalValueBetween50_75;
        int totalValueBetween25_50;
        int totalValueBetween0_25;
    }
}
