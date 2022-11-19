package br.ufu.scheduling.utils;

import java.util.List;

import br.ufu.scheduling.model.Chromosome;

public class CalculateValueForSort {
    public static double calculate(Chromosome chromosome, Configuration config, List<Integer> objectives) {
        switch (config.getSortFunctionType()) {
        case WEIGHT:
            return calculateAverageByWeight(chromosome, config, objectives);

        case SINGLE_AVERAGE:
            return calculateAverageBySingleAverage(chromosome, config, objectives);

        case HARMONIC_AVERAGE:
            return calculateAverageByHarmonicAverage(chromosome, config, objectives);

        default:
            throw new IllegalArgumentException("Sort Function type not implemented.");
        }
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

    private static double calculateAverageBySingleAverage(Chromosome chromosome, Configuration config, List<Integer> objectives) {
        double accumulatedValue = 0.0;

        for (Integer objective : objectives) {
            switch (objective) {
            case 1:
                accumulatedValue += getNormalizedObjectiveValue(config, config.getObjective1(), config.getMaxObjectiveValue1(), config.getMinObjectiveValue1(), chromosome);
                break;

            case 2:
                accumulatedValue += getNormalizedObjectiveValue(config, config.getObjective2(), config.getMaxObjectiveValue2(), config.getMinObjectiveValue2(), chromosome);
                break;

            case 3:
                accumulatedValue += getNormalizedObjectiveValue(config, config.getObjective3(), config.getMaxObjectiveValue3(), config.getMinObjectiveValue3(), chromosome);
                break;

            case 4:
                accumulatedValue += getNormalizedObjectiveValue(config, config.getObjective4(), config.getMaxObjectiveValue4(), config.getMinObjectiveValue4(), chromosome);
                break;

            case 5:
                accumulatedValue += getNormalizedObjectiveValue(config, config.getObjective5(), config.getMaxObjectiveValue5(), config.getMinObjectiveValue5(), chromosome);
                break;

            default:
                throw new IllegalArgumentException("Objective invalid. Value: " + objective + ".");
            }
        }

        return accumulatedValue / objectives.size();
    }

    private static double calculateAverageByHarmonicAverage(Chromosome chromosome, Configuration config, List<Integer> objectives) {
        double accumulatedValue = 0.0;

        for (Integer objective : objectives) {
            switch (objective) {
            case 1:
                accumulatedValue += (1 / getNormalizedObjectiveValue(config, config.getObjective1(), config.getMaxObjectiveValue1(), config.getMinObjectiveValue1(), chromosome));
                break;

            case 2:
                accumulatedValue += (1 / getNormalizedObjectiveValue(config, config.getObjective2(), config.getMaxObjectiveValue2(), config.getMinObjectiveValue2(), chromosome));
                break;

            case 3:
                accumulatedValue += (1 / getNormalizedObjectiveValue(config, config.getObjective3(), config.getMaxObjectiveValue3(), config.getMinObjectiveValue3(), chromosome));
                break;

            case 4:
                accumulatedValue += (1 / getNormalizedObjectiveValue(config, config.getObjective4(), config.getMaxObjectiveValue4(), config.getMinObjectiveValue4(), chromosome));
                break;

            case 5:
                accumulatedValue += (1 / getNormalizedObjectiveValue(config, config.getObjective5(), config.getMaxObjectiveValue5(), config.getMinObjectiveValue5(), chromosome));
                break;

            default:
                throw new IllegalArgumentException("Objective invalid. Value: " + objective + ".");
            }
        }

        return objectives.size() / accumulatedValue;
    }

    private static double getNormalizedObjectiveValue(Configuration config, Integer objectiveIndex, double maxObjectiveValue, double minObjectiveValue, Chromosome chromosome) {
        double objectiveValue = 0.0;

        switch (objectiveIndex) {
        case 0:
            objectiveValue = calculateNormalizedObjectiveValue(config, chromosome.getFitnessForSLength(), maxObjectiveValue, minObjectiveValue);
            break;

        case 1:
            objectiveValue = calculateNormalizedObjectiveValue(config, chromosome.getFitnessForLoadBalance(), maxObjectiveValue, minObjectiveValue);
            break;

        case 2:
            objectiveValue = calculateNormalizedObjectiveValue(config, chromosome.getFitnessForFlowTime(), maxObjectiveValue, minObjectiveValue);
            break;

        case 3:
            objectiveValue = calculateNormalizedObjectiveValue(config, chromosome.getFitnessForCommunicationCost(), maxObjectiveValue, minObjectiveValue);
            break;

        case 4:
            objectiveValue = calculateNormalizedObjectiveValue(config, chromosome.getFitnessForWaitingTime(), maxObjectiveValue, minObjectiveValue);
            break;

        default:
            throw new IllegalArgumentException("Type of objective invalid. Value: " + objectiveIndex + ".");
        }

        return objectiveValue;
    }

    private static double calculateNormalizedObjectiveValue(Configuration config, double objectiveValue, double maxObjectiveValue, double minObjectiveValue) {
        double differenceToMinimum = objectiveValue - minObjectiveValue;
        double differenceBetweenExtremes = maxObjectiveValue - minObjectiveValue;

        return differenceBetweenExtremes > 0.0 ? differenceToMinimum / differenceBetweenExtremes : 0.0;
    }
}
