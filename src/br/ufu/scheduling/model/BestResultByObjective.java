package br.ufu.scheduling.model;

import java.util.ArrayList;
import java.util.List;

import br.ufu.scheduling.utils.Configuration;

public class BestResultByObjective {
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

    private Configuration config;

    public BestResultByObjective(Configuration config) {
        this.config = config;
    }

    public void verifyBestAndWorstSolutions(Chromosome chromosome) {
        if (config.isPrintBestResultsByObjectives()) {
            List<Chromosome> chromosomeList = new ArrayList<>();
            chromosomeList.add(chromosome);

            verifyBestAndWorstSolutions(chromosomeList);
        }
    }

    public void verifyBestAndWorstSolutions(List<Chromosome> chromosomeList) {
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

    public void showResult() {
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
