package br.ufu.scheduling.nsga2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.ufu.scheduling.enums.SelectionType;
import br.ufu.scheduling.model.BestResultByObjective;
import br.ufu.scheduling.model.Chromosome;
import br.ufu.scheduling.model.Graph;
import br.ufu.scheduling.utils.Configuration;
import br.ufu.scheduling.utils.Constants;
import br.ufu.scheduling.utils.Crossover;

public class NSGA2 {
    private Random generator;
    private Configuration config;
    private Graph graph;

    private List<Chromosome> chromosomeList = new ArrayList<>();
    private List<Chromosome> parentList     = new ArrayList<>();
    private List<Chromosome> childrenList   = new ArrayList<>();

    private List<List<Chromosome>> borders = new ArrayList<>();

    private int generationAccumulated;

    public NSGA2(Configuration config, Graph graph, Random generator) throws Exception {
        this.config = config;
        this.graph = graph;
        this.generator = generator;
    }

    public void execute(long initialTime) throws Exception {
        initialize();

        while (generationAccumulated < config.getTotalGenerations()) {
            if (config.isPrintIterations()) {
                System.out.println("############################\n");
                System.out.println("####### GENERATION: " + (generationAccumulated + 1) + " #######\n");
                System.out.println("############################\n");
            }

            executeAG();

            //finalizeGeneration();
        }

        //showResult(initialTime);
    }

    private void initialize() throws Exception {
        generationAccumulated = 0;
        generateInitialPopulation();
    }

    private void generateInitialPopulation() throws Exception {
        for (int i = 0; i < config.getInitialPopulation(); i++) {
            Chromosome chromosome = new Chromosome(generator, graph, config);
            addChromosomeInGeneralList(chromosome);
        }
    }

    private void addChromosomeInGeneralList(Chromosome chromosome) {
        chromosomeList.add(chromosome);
    }

    private void addChromosomeInGeneralList(List<Chromosome> chromosomeList) {
        this.chromosomeList.addAll(chromosomeList);
    }

    private void executeAG() throws Exception {
        executeSelection();
        calculateRank();
        selectBestChromosomesForReinsertion();        
    }

    private void executeSelection() {
        for (int pair = 0; pair < config.getInitialPopulation() / 2; pair++) {
            processPairSelection();
        }

        applyMutationOnChildren();
    }

    private void processPairSelection() {
        Chromosome parent1 = raffleChromosomeByTournament(new ArrayList<>(chromosomeList), null);
        Chromosome parent2 = raffleChromosomeByTournament(new ArrayList<>(chromosomeList), parent1);

        parentList.add(parent1);
        parentList.add(parent2);

        selectChildren(parent1, parent2);
    }

    private Chromosome raffleChromosomeByTournament(List<Chromosome> copyOfChromosomeList, Chromosome chromosomeAlreadyChosen) {
        Chromosome chromosome = null;

        //If it's the first individual of the pair to be chosen, I'll raffle anyone
        //For the second individual of the pair, we will try x times until an individual different from the first is drawn, or we will use a repeated one even
        if (chromosomeAlreadyChosen == null) {
            chromosome = raffleChromosomeByTournament(new ArrayList<Chromosome>(copyOfChromosomeList));

        } else {
            int currentAttemptSelectParentNotRepeated = 0;

            while (currentAttemptSelectParentNotRepeated < config.getAttemptSelectParentNotRepeated() && (chromosome == null || chromosomeAlreadyChosen.equals(chromosome))) {
                chromosome = raffleChromosomeByTournament(new ArrayList<Chromosome>(copyOfChromosomeList));

                currentAttemptSelectParentNotRepeated++;
            }
        }

        return chromosome;
    }

    private Chromosome raffleChromosomeByTournament(List<Chromosome> copyOfChromosomeList) {
        Chromosome chromosome = null;

        for (int tour = 0; tour < Constants.DOUBLE_TOURNAMENT; tour++) {
            int chromosomeRaffledIndex = raffleChromosomeIndexByTournament();

            //TODO: como pedir a avaliação para selecionar o individuo. Não pode ser pelo objetivo.
            //Talvez seja primeiro pelo rank e depois pelo crowding distance
            if (chromosome == null || chromosome.getFitness() > copyOfChromosomeList.get(chromosomeRaffledIndex).getFitness()) { 
                chromosome = copyOfChromosomeList.get(chromosomeRaffledIndex);
            }
        }

        return chromosome;
    }

    private int raffleChromosomeIndexByTournament() {
        return raffleIndex(config.getInitialPopulation());
    }

    private int raffleIndex(int limit) {
        return generator.nextInt(limit);
    }

    private void selectChildren(Chromosome parent1, Chromosome parent2) {
        childrenList.addAll(getCrossoverChildren(parent1, parent2));
    }

    private List<Chromosome> getCrossoverChildren(Chromosome parent1, Chromosome parent2) {
        //TODO: aqui será preciso implementar o crossover correto, de acordo com o paper
        List<Chromosome> generatedChildren = Crossover.getCrossover(parent1, parent2, graph, generator, config);
        addChromosomeInGeneralList(generatedChildren);

        return generatedChildren;
    }

    private void applyMutationOnChildren() {
        List<Integer> raffledIndexList = new ArrayList<>();

        for (int mutatedChromosomeIndex = 0; mutatedChromosomeIndex < getNumberOfChromosomesMutated(); mutatedChromosomeIndex++) {
            int raffledIndex = -1;

            if (!config.isAllowApplyingMutationOnRepeatedChild()) {
                do {
                    raffledIndex = raffleIndex(childrenList.size());
                } while(raffledIndexList.contains(raffledIndex));

                raffledIndexList.add(raffledIndex);
            } else {
                raffledIndex = raffleIndex(childrenList.size());
            }

            applyMutation(childrenList.get(raffledIndex));
        }
    }

    private int getNumberOfChromosomesMutated() {
        return (int) Math.ceil(config.getInitialPopulation() * config.getMutationRate() / 100);
    }

    private void applyMutation(Chromosome chromosome) {
        chromosome.applyMutation(generator, graph, config);
    }

    private void calculateRank() {
        //chromosomeList.addAll(childrenList);

        while (chromosomeList.size() > 0) {
            List<Chromosome> borderChromosomeList = new ArrayList<>();
            borders.add(borderChromosomeList);

            List<Chromosome> dominatedChromosomeList = new ArrayList<>();
            int totalChromosomes = chromosomeList.size();

            externalLoop:
            for (int chromosomeAIndex = 0; chromosomeAIndex < totalChromosomes; chromosomeAIndex++) {
                Chromosome chromosomeA = chromosomeList.get(chromosomeAIndex);

                if (dominatedChromosomeList.contains(chromosomeA)) {
                    continue;
                }

                for (int chromosomeBIndex = 0; chromosomeBIndex < totalChromosomes; chromosomeBIndex++) {
                    if (chromosomeAIndex == chromosomeBIndex) {
                        continue;
                    }

                    Chromosome chromosomeB = chromosomeList.get(chromosomeBIndex);

                    if (chromosomeB.isChromosomeDominated(config, chromosomeA)) {
                        dominatedChromosomeList.add(chromosomeB);
                    }

                    if (chromosomeA.isChromosomeDominated(config, chromosomeB)) {
                        continue externalLoop;
                    }
                }

                borderChromosomeList.add(chromosomeA);

                chromosomeList.remove(chromosomeAIndex);
                totalChromosomes--;
            }
        }

        int acumula = 0;
        for (int i = 0; i < borders.size(); i++) {
            System.out.println(borders.get(i).size());
            acumula += borders.get(i).size();
        }

        System.out.println(acumula);

        System.exit(0);
    }

    private void selectBestChromosomesForReinsertion() throws Exception {
//        sort(parentList);
//        sort(childrenList);
//
//        //ELITISM -> It will depend on how many children were generated, because depending on the crossover used, one or two children can be generated
//        int totalChildrenGenerated = childrenList.size();
//        int elitismParents = config.getInitialPopulation() - totalChildrenGenerated;
//
//        List<Chromosome> chromosomeListForReinsertion = new ArrayList<>(parentList.subList(0, elitismParents));
//        chromosomeListForReinsertion.addAll(new ArrayList<>(childrenList));
//
//        chromosomeList = new ArrayList<>(chromosomeListForReinsertion);
//        sort(chromosomeList);

        if (chromosomeList.size() != config.getInitialPopulation()) {
            throw new Exception("Invalid population size.");
        }
    }
}