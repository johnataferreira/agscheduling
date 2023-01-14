package br.ufu.scheduling.utils;

import java.util.List;

import br.ufu.scheduling.model.Chromosome;

public class CalculateValueForSort {
    public static double calculate(Chromosome chromosome, Configuration config, List<Integer> objectives) {
        switch (config.getSortFunctionType()) {
        case WEIGHT:
            return calculateAverageByWeight(chromosome, config, objectives);

        case SIMPLE_AVERAGE:
            return calculateAverageBySimpleAverage(chromosome, config, objectives);

        case HARMONIC_AVERAGE:
            return calculateAverageByHarmonicAverage(chromosome, config, objectives);

        default:
            throw new IllegalArgumentException("Sort Function type not implemented.");
        }
    }

    public static double calculateAverageBySimpleAverage(Chromosome chromosome, Configuration config) {
        double accumulatedValue = 0.0;

        for (int objective = 1; objective <= config.getTotalObjectives(); objective++) {
            accumulatedValue += getValueForObjectiveBySimpleAverage(chromosome, config, objective); 
        }

        return accumulatedValue / config.getTotalObjectives();
    }

    public static double calculateAverageByHarmonicAverage(Chromosome chromosome, Configuration config) {
        double accumulatedValue = 0.0;

        for (int objective = 1; objective <= config.getTotalObjectives(); objective++) {
            accumulatedValue += getValueForObjectiveByHarmonicAverage(chromosome, config, objective); 
        }

        return config.getTotalObjectives() / accumulatedValue;
    }

    private static double calculateAverageByWeight(Chromosome chromosome, Configuration config, List<Integer> objectives) {
        double accumulatedValue = 0.0;

        for (Integer objective : objectives) {
            switch (objective) {
            case 1:
                accumulatedValue += chromosome.getObjectiveValue(config.getObjective1()) * config.getWeight1();
                break;

            case 2:
                accumulatedValue += chromosome.getObjectiveValue(config.getObjective2()) * config.getWeight2();
                break;

            case 3:
                accumulatedValue += chromosome.getObjectiveValue(config.getObjective3()) * config.getWeight3();
                break;

            case 4:
                accumulatedValue += chromosome.getObjectiveValue(config.getObjective4()) * config.getWeight4();
                break;

            case 5:
                accumulatedValue += chromosome.getObjectiveValue(config.getObjective5()) * config.getWeight5();
                break;

            default:
                throw new IllegalArgumentException("Objective invalid. Value: " + objective + ".");
            }
        }

        return accumulatedValue /= objectives.size();
    }

    private static double calculateAverageBySimpleAverage(Chromosome chromosome, Configuration config, List<Integer> objectives) {
        double accumulatedValue = 0.0;

        for (Integer objective : objectives) {
            accumulatedValue += getValueForObjectiveBySimpleAverage(chromosome, config, objective);
        }

        return accumulatedValue / objectives.size();
    }

    private static double getValueForObjectiveBySimpleAverage(Chromosome chromosome, Configuration config, int objective) {
        switch (objective) {
        case 1:
            return getNormalizedObjectiveValue(config, config.getObjective1(), config.getMaxObjectiveValue1(), config.getMinObjectiveValue1(), chromosome);

        case 2:
            return getNormalizedObjectiveValue(config, config.getObjective2(), config.getMaxObjectiveValue2(), config.getMinObjectiveValue2(), chromosome);

        case 3:
            return getNormalizedObjectiveValue(config, config.getObjective3(), config.getMaxObjectiveValue3(), config.getMinObjectiveValue3(), chromosome);

        case 4:
            return getNormalizedObjectiveValue(config, config.getObjective4(), config.getMaxObjectiveValue4(), config.getMinObjectiveValue4(), chromosome);

        case 5:
            return getNormalizedObjectiveValue(config, config.getObjective5(), config.getMaxObjectiveValue5(), config.getMinObjectiveValue5(), chromosome);

        default:
            throw new IllegalArgumentException("Objective invalid. Value: " + objective + ".");
        }
    }

    private static double calculateAverageByHarmonicAverage(Chromosome chromosome, Configuration config, List<Integer> objectives) {
        double accumulatedValue = 0.0;

        for (Integer objective : objectives) {
            accumulatedValue += getValueForObjectiveByHarmonicAverage(chromosome, config, objective);
        }

        return objectives.size() / accumulatedValue;
    }

    private static double getValueForObjectiveByHarmonicAverage(Chromosome chromosome, Configuration config, int objective) {
        switch (objective) {
        case 1:
            return (1 / getNormalizedObjectiveValue(config, config.getObjective1(), config.getMaxObjectiveValue1(), config.getMinObjectiveValue1(), chromosome));

        case 2:
            return (1 / getNormalizedObjectiveValue(config, config.getObjective2(), config.getMaxObjectiveValue2(), config.getMinObjectiveValue2(), chromosome));

        case 3:
            return (1 / getNormalizedObjectiveValue(config, config.getObjective3(), config.getMaxObjectiveValue3(), config.getMinObjectiveValue3(), chromosome));

        case 4:
            return (1 / getNormalizedObjectiveValue(config, config.getObjective4(), config.getMaxObjectiveValue4(), config.getMinObjectiveValue4(), chromosome));

        case 5:
            return (1 / getNormalizedObjectiveValue(config, config.getObjective5(), config.getMaxObjectiveValue5(), config.getMinObjectiveValue5(), chromosome));

        default:
            throw new IllegalArgumentException("Objective invalid. Value: " + objective + ".");
        }
    }

    private static double getNormalizedObjectiveValue(Configuration config, Integer objectiveIndex, double maxObjectiveValue, double minObjectiveValue, Chromosome chromosome) {
        double objectiveValue = 0.0;

        switch (objectiveIndex) {
            case Constants.MAKESPAN:
                objectiveValue = calculateNormalizedObjectiveValue(config, chromosome.getFitnessForSLength(), maxObjectiveValue, minObjectiveValue);
                break;

            case Constants.LOAD_BALANCE:
                objectiveValue = calculateNormalizedObjectiveValue(config, chromosome.getFitnessForLoadBalance(), maxObjectiveValue, minObjectiveValue);
                break;

            case Constants.FLOW_TIME:
                objectiveValue = calculateNormalizedObjectiveValue(config, chromosome.getFitnessForFlowTime(), maxObjectiveValue, minObjectiveValue);
                break;

            case Constants.COMMUNICATION_COST:
                objectiveValue = calculateNormalizedObjectiveValue(config, chromosome.getFitnessForCommunicationCost(), maxObjectiveValue, minObjectiveValue);
                break;

            case Constants.WAITING_TIME:
                objectiveValue = calculateNormalizedObjectiveValue(config, chromosome.getFitnessForWaitingTime(), maxObjectiveValue, minObjectiveValue);
                break;

            default:
                throw new IllegalArgumentException("Type of objective invalid. Value: " + objectiveIndex + ".");
        }

        return objectiveValue;
    }

    private static double calculateNormalizedObjectiveValue(Configuration config, double objectiveValue, double maxObjectiveValue, double minObjectiveValue) {
        double differenceToMinimum = objectiveValue - minObjectiveValue;
        double differenceBetweenExtremes = Math.abs(maxObjectiveValue - minObjectiveValue);

        return differenceBetweenExtremes > 0.0 ? differenceToMinimum / differenceBetweenExtremes : 0.0;
    }
}
