package br.ufu.scheduling.model;

public class ObjectiveDataForSpreadsheet {
    private String algorithm;
    private String bestValueFounded;
    private String valuePercentageFounded;
    private String sortFunction;
    private int totalChromosomesNonDominated;
    private String runtime;
    private String bestSolutionSimpleAverage;
    private String bestSolutionHarmonicAverage;

    public ObjectiveDataForSpreadsheet() {
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public String getBestValueFounded() {
        return bestValueFounded;
    }

    public String getValuePercentageFounded() {
        return valuePercentageFounded;
    }

    public String getSortFunction() {
        return sortFunction;
    }

    public int getTotalChromosomesNonDominated() {
        return totalChromosomesNonDominated;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getBestSolutionSimpleAverage() {
        return bestSolutionSimpleAverage;
    }

    public String getBestSolutionHarmonicAverage() {
        return bestSolutionHarmonicAverage;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public void setBestValueFounded(String bestValueFounded) {
        this.bestValueFounded = bestValueFounded;
    }

    public void setValuePercentageFounded(String valuePercentageFounded) {
        this.valuePercentageFounded = valuePercentageFounded;
    }

    public void setSortFunction(String sortFunction) {
        this.sortFunction = sortFunction;
    }

    public void setTotalChromosomesNonDominated(int totalChromosomesNonDominated) {
        this.totalChromosomesNonDominated = totalChromosomesNonDominated;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public void setBestSolutionSimpleAverage(String bestSolutionSimpleAverage) {
        this.bestSolutionSimpleAverage = bestSolutionSimpleAverage;
    }

    public void setBestSolutionHarmonicAverage(String bestSolutionHarmonicAverage) {
        this.bestSolutionHarmonicAverage = bestSolutionHarmonicAverage;
    }

    @Override
    public String toString() {
        String dataBase = null;
        dataBase = algorithm + "\t" +
                bestValueFounded + "\t" +
                valuePercentageFounded + "\t" +
                (sortFunction != null ? sortFunction + "\t" : "") + 
                totalChromosomesNonDominated + "\t" + 
                runtime + "\t" +
                bestSolutionSimpleAverage + "\t" +
                (sortFunction != null ? "" : bestSolutionHarmonicAverage + "\t");

        return dataBase;
    }
}