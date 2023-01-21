package br.ufu.scheduling.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.TreeMap;

public class TableOverleaf {
    public static final String LINE_BREAK = "\n";

    public static void main(String [] args) throws Exception {
        TableOverleaf t = new TableOverleaf();
        int totalObjectives = 5;
        Map<Integer, Line> lines = t.readFile();
        
        //t.generateIntervalValues(lines);
        t.generatePercentualByObjective(lines);
        t.generateBestObjectiveByAGMO(lines, totalObjectives);
        t.generateAverageBestObjectiveByAGMO(lines, totalObjectives);
        t.generateBestChromosomeByAGMOBySimpleAverage(lines);
        t.generateBestChromosomeByAGMOByHarmonicAverage(lines);
    }

    private void generateBestChromosomeByAGMOByHarmonicAverage(Map<Integer, Line> lines) {
        StringBuilder sb = new StringBuilder();

        for (Line line : lines.values()) {
            if ("90-100".equals(line.interval)) {
                if ("Makespan".equals(line.objective)) {
                    sb.append("\\begin{table}[htb]" + LINE_BREAK);
                    sb.append(" \\footnotesize" + LINE_BREAK);
                    sb.append(" \\centering" + LINE_BREAK);
                    sb.append(" \\caption[Melhores indivíduos encontrados por AGMO considerando média harmônica em um DAG de " + line.task + " tarefas com " + line.processors + " processadores]{Melhores indivíduos encontrados por AGMO considerando média harmônica em um DAG de " + line.task + " tarefas com " + line.processors + " processadores.}" + LINE_BREAK);
                    sb.append(" \\label{tabBestChromosomeByAgmoByHarmonicAverageForAGMO" + line.processors + "Proc" + line.task + "Task}" + LINE_BREAK);
                    sb.append(" \\begin{tabular}{c|c|c|c|c|c}" + LINE_BREAK);
                    sb.append("  \\textbf{Algoritmo} &" + LINE_BREAK); 
                    sb.append("  \\textbf{\\begin{tabular}{ccccc} Total \\\\ de \\\\ Indivíduos \\\\ Não \\\\ Dominados \\end{tabular}}  &" + LINE_BREAK);
                    sb.append("  \\textbf{\\begin{tabular}{cc} Tempo \\\\ (seg) \\end{tabular}}  &" + LINE_BREAK);
                    sb.append("  \\textbf{\\begin{tabular}{cccc} Média \\\\ do \\\\ Melhor \\\\ Indivíduos \\end{tabular}}  &" + LINE_BREAK);
                    sb.append("  \\textbf{\\begin{tabular}{cccc} Média \\\\ dos \\\\ Melhores \\\\ Indivíduos \\end{tabular}}  &" + LINE_BREAK);
                    sb.append("  \\textbf{\\begin{tabular}{cc} Desvio \\\\ Padrão \\end{tabular}} \\\\ \\hline" + LINE_BREAK);                                        

                    sb.append("  " + line.objectives.get(2).algorithm + " & " + line.objectives.get(2).averageTotalChromosomesNonDominated + " & " + line.objectives.get(2).averageRuntime + " & " + line.objectives.get(2).bestSolutionHarmonicAverage + " & " + line.objectives.get(2).averageBestSolutionHarmonicAverage + " & " + line.objectives.get(2).standardDeviationAverageBestSolutionHarmonicAverage + " \\\\ \\hline" + LINE_BREAK);
                    sb.append("  " + line.objectives.get(3).algorithm + " & " + line.objectives.get(3).averageTotalChromosomesNonDominated + " & " + line.objectives.get(3).averageRuntime + " & " + line.objectives.get(3).bestSolutionHarmonicAverage + " & " + line.objectives.get(3).averageBestSolutionHarmonicAverage + " & " + line.objectives.get(3).standardDeviationAverageBestSolutionHarmonicAverage + " \\\\ \\hline" + LINE_BREAK);
                    sb.append("  " + line.objectives.get(4).algorithm + " & " + line.objectives.get(4).averageTotalChromosomesNonDominated + " & " + line.objectives.get(4).averageRuntime + " & " + line.objectives.get(4).bestSolutionHarmonicAverage + " & " + line.objectives.get(4).averageBestSolutionHarmonicAverage + " & " + line.objectives.get(4).standardDeviationAverageBestSolutionHarmonicAverage + " \\\\" + LINE_BREAK);

                    sb.append(" \\end{tabular}" + LINE_BREAK);
                    sb.append("\\end{table}" + LINE_BREAK);

                    System.out.println(sb.toString() + LINE_BREAK);
                    sb.setLength(0);
                }
            }
        }
    }

    private void generateBestChromosomeByAGMOBySimpleAverage(Map<Integer, Line> lines) {
        StringBuilder sb = new StringBuilder();

        for (Line line : lines.values()) {
            if ("90-100".equals(line.interval)) {
                if ("Makespan".equals(line.objective)) {
                    sb.append("\\begin{table}[htb]" + LINE_BREAK);
                    sb.append(" \\footnotesize" + LINE_BREAK);
                    sb.append(" \\centering" + LINE_BREAK);
                    sb.append(" \\caption[Melhores indivíduos encontrados por AGMO considerando média simples em um DAG de " + line.task + " tarefas com " + line.processors + " processadores]{Melhores indivíduos encontrados por AGMO considerando média simples em um DAG de " + line.task + " tarefas com " + line.processors + " processadores.}" + LINE_BREAK);
                    sb.append(" \\label{tabBestChromosomeByAgmoBySimpleAverageForAGMO" + line.processors + "Proc" + line.task + "Task}" + LINE_BREAK);
                    sb.append(" \\begin{tabular}{c|c|c|c|c|c}" + LINE_BREAK);
                    sb.append("  \\textbf{Algoritmo} &" + LINE_BREAK); 
                    sb.append("  \\textbf{\\begin{tabular}{ccccc} Total \\\\ de \\\\ Indivíduos \\\\ Não \\\\ Dominados \\end{tabular}}  &" + LINE_BREAK);
                    sb.append("  \\textbf{\\begin{tabular}{cc} Tempo \\\\ (seg) \\end{tabular}}  &" + LINE_BREAK);
                    sb.append("  \\textbf{\\begin{tabular}{cccc} Média \\\\ do \\\\ Melhor \\\\ Indivíduo \\end{tabular}}  &" + LINE_BREAK);
                    sb.append("  \\textbf{\\begin{tabular}{cccc} Média \\\\ dos \\\\ Melhores \\\\ Indivíduos \\end{tabular}}  &" + LINE_BREAK);
                    sb.append("  \\textbf{\\begin{tabular}{cc} Desvio \\\\ Padrão \\end{tabular}} \\\\ \\hline" + LINE_BREAK);                                        

                    sb.append("  " + line.objectives.get(1).algorithm + " & " + line.objectives.get(1).averageTotalChromosomesNonDominated + " & " + line.objectives.get(1).averageRuntime + " & " + line.objectives.get(1).bestSolutionSimpleAverage + " & " + line.objectives.get(1).averageBestSolutionSimpleAverage + " & " + line.objectives.get(1).standardDeviationAverageBestSolutionSimpleAverage + " \\\\ \\hline" + LINE_BREAK);
                    sb.append("  " + line.objectives.get(3).algorithm + " & " + line.objectives.get(3).averageTotalChromosomesNonDominated + " & " + line.objectives.get(3).averageRuntime + " & " + line.objectives.get(3).bestSolutionSimpleAverage + " & " + line.objectives.get(3).averageBestSolutionSimpleAverage + " & " + line.objectives.get(3).standardDeviationAverageBestSolutionSimpleAverage + " \\\\ \\hline" + LINE_BREAK);
                    sb.append("  " + line.objectives.get(4).algorithm + " & " + line.objectives.get(4).averageTotalChromosomesNonDominated + " & " + line.objectives.get(4).averageRuntime + " & " + line.objectives.get(4).bestSolutionSimpleAverage + " & " + line.objectives.get(4).averageBestSolutionSimpleAverage + " & " + line.objectives.get(4).standardDeviationAverageBestSolutionSimpleAverage + " \\\\" + LINE_BREAK);

                    sb.append(" \\end{tabular}" + LINE_BREAK);
                    sb.append("\\end{table}" + LINE_BREAK);

                    System.out.println(sb.toString() + LINE_BREAK);
                    sb.setLength(0);
                }
            }
        }
    }

    private void generateAverageBestObjectiveByAGMO(Map<Integer, Line> lines, int totalObjectives) {
        StringBuilder sb = new StringBuilder();

        for (Line line : lines.values()) {
            if ("90-100".equals(line.interval)) {
                if ("Makespan".equals(line.objective)) {
                    sb.append("\\begin{table}[htb]" + LINE_BREAK);
                    sb.append(" \\footnotesize" + LINE_BREAK);
                    sb.append(" \\centering" + LINE_BREAK);
                    sb.append(" \\caption[Média dos Melhores objetivos encontrados por AGMO em um DAG de " + line.task + " tarefas com " + line.processors + " processadores]{Média dos melhores objetivos encontrados por AGMO em um DAG de " + line.task + " tarefas com " + line.processors + " processadores.}" + LINE_BREAK);
                    sb.append(" \\label{tabAvgBestObjectiveForAGMO" + line.processors + "Proc" + line.task + "Task}" + LINE_BREAK);
                    sb.append(" \\begin{tabular}{c|c|c|c|c}" + LINE_BREAK);
                    sb.append("  \\textbf{Objetivo} &" + LINE_BREAK); 
                    sb.append("  \\textbf{\\begin{tabular}{cc} AEMMT \\\\ Média Simples \\end{tabular}}  &" + LINE_BREAK);
                    sb.append("  \\textbf{\\begin{tabular}{cc} AEMMT \\\\ Média Harmônica \\end{tabular}}  &" + LINE_BREAK);
                    sb.append("  \\textbf{AEMMD}  &" + LINE_BREAK);
                    sb.append("  \\textbf{NSGA-II} \\\\ \\hline" + LINE_BREAK);
                }

                if (getMetricByNumberOfObjectives(totalObjectives).equals(line.objective)) {
                    sb.append("  " + line.objective + " & " + line.objectives.get(1).averageBestValueFounded + " & " + line.objectives.get(2).averageBestValueFounded + " & " + line.objectives.get(3).averageBestValueFounded + " & " + line.objectives.get(4).averageBestValueFounded + " \\\\" + LINE_BREAK);
                    sb.append(" \\end{tabular}" + LINE_BREAK);
                    sb.append("\\end{table}" + LINE_BREAK);
                    System.out.println(sb.toString() + LINE_BREAK);
                    sb.setLength(0);
                } else {
                    sb.append("  " + line.objective + " & " + line.objectives.get(1).averageBestValueFounded + " & " + line.objectives.get(2).averageBestValueFounded + " & " + line.objectives.get(3).averageBestValueFounded + " & " + line.objectives.get(4).averageBestValueFounded + " \\\\ \\hline" + LINE_BREAK);
                }
            }
        }
    }

    private void generateBestObjectiveByAGMO(Map<Integer, Line> lines, int totalObjectives) {
        StringBuilder sb = new StringBuilder();

        for (Line line : lines.values()) {
            if ("90-100".equals(line.interval)) {
                if ("Makespan".equals(line.objective)) {
                    sb.append("\\begin{table}[htb]" + LINE_BREAK);
                    sb.append(" \\footnotesize" + LINE_BREAK);
                    sb.append(" \\centering" + LINE_BREAK);
                    sb.append(" \\caption[Melhores objetivos encontrados por AGMO em um DAG de " + line.task + " tarefas com " + line.processors + " processadores]{Melhores objetivos encontrados por AGMO em um DAG de " + line.task + " tarefas com " + line.processors + " processadores.}" + LINE_BREAK);
                    sb.append(" \\label{tabBestObjectiveForAGMO" + line.processors + "Proc" + line.task + "Task}" + LINE_BREAK);
                    sb.append(" \\begin{tabular}{c|c|c|c|c}" + LINE_BREAK);
                    sb.append("  \\textbf{Objetivo} &" + LINE_BREAK); 
                    sb.append("  \\textbf{\\begin{tabular}{cc} AEMMT \\\\ Média Simples \\end{tabular}}  &" + LINE_BREAK);
                    sb.append("  \\textbf{\\begin{tabular}{cc} AEMMT \\\\ Média Harmônica \\end{tabular}}  &" + LINE_BREAK);
                    sb.append("  \\textbf{AEMMD}  &" + LINE_BREAK);
                    sb.append("  \\textbf{NSGA-II} \\\\ \\hline" + LINE_BREAK);
                }

                if (getMetricByNumberOfObjectives(totalObjectives).equals(line.objective)) {
                    sb.append("  " + line.objective + " & " + line.objectives.get(1).bestValueFounded + " & " + line.objectives.get(2).bestValueFounded + " & " + line.objectives.get(3).bestValueFounded + " & " + line.objectives.get(4).bestValueFounded + " \\\\" + LINE_BREAK);
                    sb.append(" \\end{tabular}" + LINE_BREAK);
                    sb.append("\\end{table}" + LINE_BREAK);
                    System.out.println(sb.toString() + LINE_BREAK);
                    sb.setLength(0);
                } else {
                    sb.append("  " + line.objective + " & " + line.objectives.get(1).bestValueFounded + " & " + line.objectives.get(2).bestValueFounded + " & " + line.objectives.get(3).bestValueFounded + " & " + line.objectives.get(4).bestValueFounded + " \\\\ \\hline" + LINE_BREAK);
                }
            }
        }
    }

    private void generatePercentualByObjective(Map<Integer, Line> lines) {
        StringBuilder sb = new StringBuilder();

        for (Line line : lines.values()) {
            if ("90-100".equals(line.interval)) {
                sb.append("\\begin{table}[htb]" + LINE_BREAK);
                sb.append(" \\footnotesize" + LINE_BREAK);
                sb.append(" \\centering" + LINE_BREAK);
                sb.append(" \\caption[Percentuais obtidos por AGMO nos intervalos de valores do \\textit{" + line.objective + "} em um DAG de " + line.task + " tarefas com " + line.processors + " processadores]{Percentuais obtidos por AGMO nos intervalos de valores do \\textit{" + line.objective + "} em um DAG de " + line.task + " tarefas com " + line.processors + " processadores.}" + LINE_BREAK);
                sb.append(" \\label{tabPercInterval" + line.objective + line.processors + "Proc" + line.task + "Task}" + LINE_BREAK);
                sb.append(" \\begin{tabular}{c|c|c|c|c}" + LINE_BREAK);
                sb.append("  \\textbf{Intervalo} &" + LINE_BREAK); 
                sb.append("  \\textbf{\\begin{tabular}{cc} AEMMT \\\\ Média Simples (\\%) \\end{tabular}}  &" + LINE_BREAK);
                sb.append("  \\textbf{\\begin{tabular}{cc} AEMMT \\\\ Média Harmônica (\\%) \\end{tabular}}  &" + LINE_BREAK);
                sb.append("  \\textbf{AEMMD (\\%)}  &" + LINE_BREAK);
                sb.append("  \\textbf{NSGA-II (\\%)} \\\\ \\hline" + LINE_BREAK);
            }

            if ("0-10".equals(line.interval)) {
                sb.append("  " + line.interval + " & " + line.objectives.get(1).averageValueFoundedPerc + " & " + line.objectives.get(2).averageValueFoundedPerc + " & " + line.objectives.get(3).averageValueFoundedPerc + " & " + line.objectives.get(4).averageValueFoundedPerc + " \\\\" + LINE_BREAK);
                sb.append(" \\end{tabular}" + LINE_BREAK);
                sb.append("\\end{table}" + LINE_BREAK);
                System.out.println(sb.toString() + LINE_BREAK);
                sb.setLength(0);
            } else {
                sb.append("  " + line.interval + " & " + line.objectives.get(1).averageValueFoundedPerc + " & " + line.objectives.get(2).averageValueFoundedPerc + " & " + line.objectives.get(3).averageValueFoundedPerc + " & " + line.objectives.get(4).averageValueFoundedPerc + " \\\\ \\hline" + LINE_BREAK);                
            }
        }
    }

    //The range of generated values does not depend on the number of objectives worked
    private void generateIntervalValues(Map<Integer, Line> lines) throws Exception {
        StringBuilder sb = new StringBuilder();

        for (Line line : lines.values()) {
            if ("90-100".equals(line.interval)) {
                sb.append("\\begin{table}[htb]" + LINE_BREAK);
                sb.append(" \\footnotesize" + LINE_BREAK);
                sb.append(" \\centering" + LINE_BREAK);
                sb.append(" \\caption[Intervalo de valores obtidos para o \\textit{" + line.objective + "} em um DAG de " + line.task + " tarefas com " + line.processors + " processadores]{Intervalo de valores obtidos para o \\textit{" + line.objective + "} em um DAG de " + line.task + " tarefas com " + line.processors + " processadores.}" + LINE_BREAK);
                sb.append(" \\label{tabInterval" + line.objective + line.processors + "Proc" + line.task + "Task}" + LINE_BREAK);
                sb.append(" \\begin{tabular}{c|c|c|c|c}" + LINE_BREAK);
                sb.append("  \\textbf{Melhor Valor} & \\textbf{Pior Valor}  & \\textbf{Intervalo}  & \\textbf{Limite Inferior}  & \\textbf{Limite Superior} \\\\ \\hline" + LINE_BREAK);
            }

            if ("0-10".equals(line.interval)) {
                sb.append("  " + line.bestValue + " & " + line.worstValue + " & " + line.interval + " & " + line.bottomLimit + " & " + line.topLimit + " \\\\" + LINE_BREAK);
                sb.append(" \\end{tabular}" + LINE_BREAK);
                sb.append("\\end{table}" + LINE_BREAK);
                System.out.println(sb.toString() + LINE_BREAK);
                sb.setLength(0);
            } else {
                sb.append("  " + line.bestValue + " & " + line.worstValue + " & " + line.interval + " & " + line.bottomLimit + " & " + line.topLimit + " \\\\ \\hline" + LINE_BREAK);                
            }
        }
    }
    
    
    private Map<Integer, Line> readFile() throws Exception {
        Map<Integer, Line> lines = new TreeMap<>();

        try (BufferedReader buffer = new BufferedReader(new FileReader(new File("tableOverleaf.txt")))) {
            String register = null;

            int cont = 1;
            while ((register = buffer.readLine()) != null) {
                String[] vector = register.split("\t");
                Line line = new Line();

                line.task = vector[0].split("-")[4];
                line.processors = vector[1];
                line.seed = vector[2];
                line.bestValue = vector[3];
                line.worstValue = vector[4];
                line.objective = handleObjective(vector[5]);
                line.interval = vector[6];
                line.bottomLimit = vector[7];
                line.topLimit = vector[8];

                Objective o1 = new Objective();
                o1.algorithm = handleAlgorithm(vector[9]);
                o1.bestValueFounded = vector[10];
                o1.averageBestValueFounded = vector[11];
                o1.standardDeviationAverageBestValueFounded = vector[12];
                o1.averageValueFoundedPerc = vector[13];
                o1.sortFunction = vector[14];
                o1.averageTotalChromosomesNonDominated = vector[15];
                o1.averageRuntime = vector[16];
                o1.bestSolutionSimpleAverage = vector[17];
                o1.averageBestSolutionSimpleAverage = vector[18];
                o1.standardDeviationAverageBestSolutionSimpleAverage = vector[19];
                line.objectives.put(1, o1);

                Objective o2 = new Objective();
                o2.algorithm = handleAlgorithm(vector[20]);
                o2.bestValueFounded = vector[21];
                o2.averageBestValueFounded = vector[22];
                o2.standardDeviationAverageBestValueFounded = vector[23]; 
                o2.averageValueFoundedPerc = vector[24];
                o2.sortFunction = vector[25];
                o2.averageTotalChromosomesNonDominated = vector[26];
                o2.averageRuntime = vector[27];
                o2.bestSolutionHarmonicAverage = vector[28];
                o2.averageBestSolutionHarmonicAverage = vector[29];
                o2.standardDeviationAverageBestSolutionHarmonicAverage = vector[30];
                line.objectives.put(2, o2);

                Objective o3 = new Objective();
                o3.algorithm = handleAlgorithm(vector[31]);
                o3.bestValueFounded = vector[32];
                o3.averageBestValueFounded = vector[33];
                o3.standardDeviationAverageBestValueFounded = vector[34];
                o3.averageValueFoundedPerc = vector[35];
                o3.averageTotalChromosomesNonDominated = vector[36];
                o3.averageRuntime = vector[37];
                o3.bestSolutionSimpleAverage = vector[38];
                o3.averageBestSolutionSimpleAverage = vector[39];
                o3.standardDeviationAverageBestSolutionSimpleAverage = vector[40];
                o3.bestSolutionHarmonicAverage = vector[41];
                o3.averageBestSolutionHarmonicAverage = vector[42];
                o3.standardDeviationAverageBestSolutionHarmonicAverage = vector[43];
                line.objectives.put(3, o3);

                Objective o4 = new Objective();
                o4.algorithm = handleAlgorithm(vector[44]);
                o4.bestValueFounded = vector[45];
                o4.averageBestValueFounded = vector[46];
                o4.standardDeviationAverageBestValueFounded = vector[47];
                o4.averageValueFoundedPerc = vector[48];
                o4.averageTotalChromosomesNonDominated = vector[49];
                o4.averageRuntime = vector[50];
                o4.bestSolutionSimpleAverage = vector[51];
                o4.averageBestSolutionSimpleAverage = vector[52];
                o4.standardDeviationAverageBestSolutionSimpleAverage = vector[53];
                o4.bestSolutionHarmonicAverage = vector[54];
                o4.averageBestSolutionHarmonicAverage = vector[55];
                o4.standardDeviationAverageBestSolutionHarmonicAverage = vector[56];
                line.objectives.put(4, o4);

                lines.put(cont, line);

                cont++;
            }
        } catch (Exception e) {
            Exception e2 = new Exception("Error loading tableOverleaf.txt file: " + e.getMessage());
            e2.initCause(e);
            throw e2;
        }

//        for (Line line : lines.values()) {
//            System.out.println(line.toString());
//        }
//        
//        System.exit(0);
        
        return lines;
    }

    private Object getMetricByNumberOfObjectives(int totalObjectives) {
        switch (totalObjectives) {
            case 5:
                return "Waiting Time";

            case 4:
                return "Communication Cost";

            case 3:
                return "Flow Time";

            case 2:
                return "Load Balance";

            default:
                throw new IllegalArgumentException("Total Objetives invalid: " + totalObjectives + ".");
        }
    }

    private String handleAlgorithm(String algorithm) {
        if ("NSGAII".equals(algorithm)) {
            return "NSGA-II"; 
        }

        return algorithm;
    }

    private String handleObjective(String objective) {
        switch (objective) {
            case "LoadBalance":
                return "Load Balance";

            case "FlowTime":
                return "Flow Time";

            case "CommunicationCost":
                return "Communication Cost";

            case "WaitingTime":
                return "Waiting Time";

            default:
                return objective;
        }
    }

    class Line {
        String task;
        String processors;  
        String seed;
        String bestValue;  
        String worstValue; 
        String objective;   
        String interval;    
        String bottomLimit; 
        String topLimit;
        Map<Integer, Objective> objectives = new TreeMap<>();

        @Override
        public String toString() {
            String result = task + "\t" + 
                processors + "\t" + 
                seed + "\t" + 
                bestValue + "\t" + 
                worstValue + "\t" + 
                objective + "\t" + 
                interval + "\t" + 
                bottomLimit + "\t" + 
                topLimit + "\t";

            for (Objective o : objectives.values()) {
                result += o.toString();
            }

            return result;
        }
    }

    class Objective {
        String algorithm;
        String bestValueFounded;
        String averageBestValueFounded;
        String standardDeviationAverageBestValueFounded;
        String averageValueFoundedPerc;
        String sortFunction;
        String averageTotalChromosomesNonDominated;
        String averageRuntime;
        String bestSolutionSimpleAverage;
        String averageBestSolutionSimpleAverage;
        String standardDeviationAverageBestSolutionSimpleAverage;
        String bestSolutionHarmonicAverage;
        String averageBestSolutionHarmonicAverage;
        String standardDeviationAverageBestSolutionHarmonicAverage;

        @Override
        public String toString() {
            String result = algorithm + "\t" +
                bestValueFounded + "\t" +
                averageBestValueFounded + "\t";

            if (standardDeviationAverageBestValueFounded != null) {
                result += standardDeviationAverageBestValueFounded + "\t";
            }

            result += averageValueFoundedPerc + "\t";

            if (sortFunction != null) {
                result += sortFunction + "\t";
            }

            result += averageTotalChromosomesNonDominated + "\t" +
                averageRuntime + "\t";

            if (bestSolutionSimpleAverage != null) {
                result += bestSolutionSimpleAverage + "\t" +
                    averageBestSolutionSimpleAverage + "\t" +
                    standardDeviationAverageBestSolutionSimpleAverage + "\t";
            }

            if (bestSolutionHarmonicAverage != null) {
                result += bestSolutionHarmonicAverage + "\t" +
                    averageBestSolutionHarmonicAverage + "\t" +
                    standardDeviationAverageBestSolutionHarmonicAverage + "\t";
            }

            return result;
        }
    }
}



