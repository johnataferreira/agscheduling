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

	public boolean add(Chromosome chromosome, Configuration config) {
		return add(chromosome, config, false);
	}

	public boolean add(Chromosome chromosome, Configuration config, boolean validateInsertion) {
		if (!validateInsertion) {
			//FIXME: aqui temos o problema de ter um valor no chromosome, mas esse cara pode ter métricas diferentes de acordo com a tabela, portanto não posso usar o chromossomo original
			//preciso fazer uma copia dele
			chromosome.setAemmtValue(calculateWeightedAverage(chromosome, config));
			chromosomeList.add(chromosome);
			return true;
		}

		// Ascending sort
		chromosomeList.sort(new Comparator<Chromosome>() {
			@Override
			public int compare(Chromosome o1, Chromosome o2) {
				return o1.getAemmtValue() < o2.getAemmtValue() ? -1 : o1.getAemmtValue() == o2.getAemmtValue() ? 0 : 1;
			}
		});

		double weightedAverage = calculateWeightedAverage(chromosome, config);

		if (weightedAverage > chromosomeList.get(0).getAemmtValue()) {
			chromosomeList.remove(0);
			chromosomeList.add(chromosome);
			return true;
		}

		return false;
	}

	private double calculateWeightedAverage(Chromosome chromosome, Configuration config) {
		double accumulatedValue = 0.0;

		for (Integer objective : objectives) {
			switch (objective) {
			case 1:
				accumulatedValue += getValueObjective(chromosome, config.getObjective1()) * config.getWeight1();
				break;

			case 2:
				accumulatedValue += getValueObjective(chromosome, config.getObjective2()) * config.getWeight2();
				break;

			case 3:
				accumulatedValue += getValueObjective(chromosome, config.getObjective3()) * config.getWeight3();
				break;

			case 4:
				accumulatedValue += getValueObjective(chromosome, config.getObjective4()) * config.getWeight4();
				break;

			case 5:
				accumulatedValue += getValueObjective(chromosome, config.getObjective5()) * config.getWeight5();
				break;

			default:
				throw new IllegalArgumentException("Objective invalid. Value: " + objective + ".");
			}
		}

		return accumulatedValue /= objectives.size();
	}
	
	private double getValueObjective(Chromosome chromosome, Integer objective) {
		double valueObjective = 0.0;

		switch (objective) {
		case 0:
			valueObjective = chromosome.getSLength();
			break;

		case 1:
			valueObjective = chromosome.getLoadBalance();
			break;

		case 2:
			valueObjective = chromosome.getFlowTime();
			break;

		case 3:
			valueObjective = chromosome.getCommunicationCost();
			break;

		case 4:
			valueObjective = chromosome.getWaitingTime();
			break;

		default:
			throw new IllegalArgumentException("Type of objective invalid. Value: " + objective + ".");
		}

		return valueObjective;
	}
}