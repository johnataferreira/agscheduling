package br.ufu.scheduling.aemmt;

import java.util.Comparator;

import br.ufu.scheduling.agmo.Table;
import br.ufu.scheduling.model.Chromosome;
import br.ufu.scheduling.utils.Configuration;

public class TableAEMMT extends Table {
	public TableAEMMT(int size) {
		super(size);
	}

    public TableAEMMT(int size, boolean isSolutionTable) {
        super(size, isSolutionTable);
    }

	public boolean add(Chromosome chromosome, Configuration config) throws Exception {
		if (chromosomeList.size() < size) {
			Chromosome clone = buildChromosomeClone(chromosome);
			clone.setAemmtValue(calculateAverage(clone, config));
			chromosomeList.add(clone);

			return true;
		}

		// Ascending sort
		chromosomeList.sort(new Comparator<Chromosome>() {
			@Override
			public int compare(Chromosome o1, Chromosome o2) {
				return o1.getAemmtValue() < o2.getAemmtValue() ? -1 : o1.getAemmtValue() == o2.getAemmtValue() ? 0 : 1;
			}
		});

		double weightedAverage = calculateAverage(chromosome, config);

		if (weightedAverage > chromosomeList.get(0).getAemmtValue()) {
			chromosomeList.remove(0);

			Chromosome clone = buildChromosomeClone(chromosome);
			clone.setAemmtValue(weightedAverage);
			chromosomeList.add(clone);

			return true;
		}

		return false;
	}

	private double calculateAverage(Chromosome chromosome, Configuration config) {
		if (config.isUseWeightToCalculateAverageFunction()) {
			return calculateAverageByWeight(chromosome, config);
		} else {
			return calculateAverageByNormalization(chromosome, config);
		}
	}
	
	private double calculateAverageByWeight(Chromosome chromosome, Configuration config) {
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

	private double calculateAverageByNormalization(Chromosome chromosome, Configuration config) {
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

		return accumulatedValue /= objectives.size();
	}

    private double getNormalizedObjectiveValue(Configuration config, Integer objectiveIndex, double maxObjectiveValue, double minObjectiveValue, Chromosome chromosome) {
        double objectiveValue = 0.0;

        switch (objectiveIndex) {
        case 0:
            objectiveValue = calculateNormalizedObjetiveValue(config, chromosome.getFitnessForSLength(), maxObjectiveValue, minObjectiveValue);
            break;

        case 1:
            objectiveValue = calculateNormalizedObjetiveValue(config, chromosome.getFitnessForLoadBalance(), maxObjectiveValue, minObjectiveValue);
            break;

        case 2:
            objectiveValue = calculateNormalizedObjetiveValue(config, chromosome.getFitnessForFlowTime(), maxObjectiveValue, minObjectiveValue);
            break;

        case 3:
            objectiveValue = calculateNormalizedObjetiveValue(config, chromosome.getFitnessForCommunicationCost(), maxObjectiveValue, minObjectiveValue);
            break;

        case 4:
            objectiveValue = calculateNormalizedObjetiveValue(config, chromosome.getFitnessForWaitingTime(), maxObjectiveValue, minObjectiveValue);
            break;

        default:
            throw new IllegalArgumentException("Type of objective invalid. Value: " + objectiveIndex + ".");
        }

        return objectiveValue;
    }

    private double calculateNormalizedObjetiveValue(Configuration config, double objectiveValue, double maxObjectiveValue, double minObjectiveValue) {
        double differenceToMinimum = objectiveValue - minObjectiveValue;
        double differenceBetweenExtremes = maxObjectiveValue - minObjectiveValue;

        return differenceBetweenExtremes > 0.0 ? differenceToMinimum / differenceBetweenExtremes : 0.0;
    }
}