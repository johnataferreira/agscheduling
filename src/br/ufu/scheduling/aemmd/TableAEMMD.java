package br.ufu.scheduling.aemmd;

import br.ufu.scheduling.agmo.Table;
import br.ufu.scheduling.model.Chromosome;
import br.ufu.scheduling.utils.Configuration;

public class TableAEMMD extends Table {
	public TableAEMMD(int size) {
		super(size);
	}

	public boolean add(Chromosome chromosome, Configuration config) throws Exception {
	    if (chromosomeList.size() == 0) {
	        Chromosome clone = buildChromosomeClone(chromosome);
	        chromosomeList.add(clone);
	        return true;
	    }

	    if (isChromosomeDominated(chromosome)) {
	        return false;
	    }

        removeChromosomeDominatedFromTable(chromosome);

        Chromosome clone = buildChromosomeClone(chromosome);
        chromosomeList.add(clone);
        return true;
	}

	private boolean isChromosomeDominated(Chromosome chromosome) {
        for (int chromosomeIndex = 0; chromosomeIndex < chromosomeList.size(); chromosomeIndex++) {
            Chromosome chromosomeB = chromosomeList.get(chromosomeIndex);

            if (chromosome.isChromosomeDominated(objectives, chromosomeB)) {
                return true;
            }
        }

        return false;
	}

    private void removeChromosomeDominatedFromTable(Chromosome chromosome) {
        int totalChromosomes = chromosomeList.size();
        int chromosomeIndex = 0;

        while (chromosomeIndex < totalChromosomes) {
            Chromosome chromosomeB = chromosomeList.get(chromosomeIndex);

            if (chromosomeB.isChromosomeDominated(objectives, chromosome)) {
                chromosomeList.remove(chromosomeIndex);
                totalChromosomes--;
            }

            chromosomeIndex++;
        }
    }
}