package br.ufu.scheduling.aemmt;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import br.ufu.scheduling.model.Chromosome;
import br.ufu.scheduling.utils.Configuration;

public class Table {
	private int score = 0;
	private int size;
	private List<Integer> objectives = new ArrayList<>();
	private List<Chromosome> chromosomeList = new ArrayList<>();

	public Table(int size) {
		this.size = size;
	}

	public int getScore() {
		return score;
	}

	public int getSize() {
		return size;
	}

	public Chromosome getChromosomeFromIndex(int index) {
		if (index < 0 || index >= chromosomeList.size()) {
			throw new IllegalArgumentException("Invalid chromosome index. Valid values between 0 and " + chromosomeList.size() + ". Value Informed: " + index + ".");
		}

		return chromosomeList.get(index);
	}

	public void addScore(int score) {
		this.score += score;
	}

	public void resetScore() {
		this.score = 0;
	}

	public void addObjective(Integer objective) {
		objectives.add(objective);
	}

	public void addObjectives(List<Integer> objectives) {
		this.objectives.addAll(objectives);
	}

	public int getTotalChromosomes() {
		return chromosomeList.size();
	}

	public void remove(int index) {
		if (index < 0 || index >= chromosomeList.size()) {
			throw new IllegalArgumentException("Invalid chromosome index. Valid values between 0 and " + chromosomeList.size() + ".");
		}

		chromosomeList.remove(index);
	}

	public boolean add(Chromosome chromosome, Configuration config) throws Exception {
		if (chromosomeList.size() < this.size) {
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

	private Chromosome buildChromosomeClone(Chromosome chromosome) throws Exception {
		Chromosome clone = null;

		try {
			clone = (Chromosome) chromosome.clone();
		} catch (CloneNotSupportedException e) {
			Exception e2 = new Exception("Error making base chromosome clone: " + e);
			e2.initCause(e);

			throw e;
		}

		return clone;
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
				accumulatedValue += chromosome.getNormalizedObjectiveValue(config, config.getObjective1(), config.getMaxObjectiveValue1(), config.getMinObjectiveValue1());
				break;

			case 2:
				accumulatedValue += chromosome.getNormalizedObjectiveValue(config, config.getObjective2(), config.getMaxObjectiveValue2(), config.getMinObjectiveValue2());
				break;

			case 3:
				accumulatedValue += chromosome.getNormalizedObjectiveValue(config, config.getObjective3(), config.getMaxObjectiveValue3(), config.getMinObjectiveValue3());
				break;

			case 4:
				accumulatedValue += chromosome.getNormalizedObjectiveValue(config, config.getObjective4(), config.getMaxObjectiveValue4(), config.getMinObjectiveValue4());
				break;

			case 5:
				accumulatedValue += chromosome.getNormalizedObjectiveValue(config, config.getObjective5(), config.getMaxObjectiveValue5(), config.getMinObjectiveValue5());
				break;

			default:
				throw new IllegalArgumentException("Objective invalid. Value: " + objective + ".");
			}
		}

		return accumulatedValue /= objectives.size();
	}
}