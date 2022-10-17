package br.ufu.scheduling.aemmd;

import java.util.Comparator;

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
	    
	    
	    
	    
//	    if (chromosomeList.size() < size) {
//			//clone.setAemmtValue(calculateAverage(clone, config));
//			chromosomeList.add(clone);
//
//			return true;
//		}
//
//		// Ascending sort
//		chromosomeList.sort(new Comparator<Chromosome>() {
//			@Override
//			public int compare(Chromosome o1, Chromosome o2) {
//				return o1.getAemmtValue() < o2.getAemmtValue() ? -1 : o1.getAemmtValue() == o2.getAemmtValue() ? 0 : 1;
//			}
//		});
//
//		double weightedAverage = 0.0;//calculateAverage(chromosome, config);
//
//		if (weightedAverage > chromosomeList.get(0).getAemmtValue()) {
//			chromosomeList.remove(0);
//
//			Chromosome clone = buildChromosomeClone(chromosome);
//			clone.setAemmtValue(weightedAverage);
//			chromosomeList.add(clone);
//
//			return true;
//		}

		return false;
	}
}