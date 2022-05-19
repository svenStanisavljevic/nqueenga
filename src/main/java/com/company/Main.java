package com.company;

import java.util.Random;

public class Main {

    public static Integer POPULATION_LENGTH = 100;
    public static Integer CHROMOSOME_LENGTH = 20;
    public static Double MUTATION_PROBABILITY = 0.3;
    public static Integer MULTIPLICATOR_COEFICIENT = 5;

    public static void main(String[] args) {

        Long startTime = System.nanoTime();

        Population population = new Population(POPULATION_LENGTH).initializePopulation();

        Integer noGen = 0;
        while (noGen < 10000) {

            System.out.println("Generacija #" + (noGen + 1) + ": ");
            System.out.println("AVG FF: " + population.avgFF());
            System.out.println("MIN FF: " + population.bestFF());
            for (int i = 0; i < population.getChromosomes().length; i++) {
                if (population.getChromosomes()[i].getFitness() == 0) {
                    System.out.println("Rjesenje:");
                    for (int j = 0; j < population.getChromosomes()[i].getGenes().length; j++) {
                        System.out.print(population.getChromosomes()[i].getGenes()[j] + " ");
                    }
                    System.out.println();
                    population.getChromosomes()[i].drawChromosome();
                    Long endTime = System.nanoTime();
                    endTime = (endTime - startTime) / 1000000;
                    System.out.println(endTime);
                    return;
                }
            }
            System.out.println();


            Population nextGen;
            population.fitnessSort();

            nextGen = rouletteWheelSelect(population);
            nextGen = mutate(nextGen);
            population = nextGen;
            population.fitnessSort();

            noGen++;

        }

    }

    public static Population rouletteWheelSelect(Population p1) {
        Random rand = new Random();
        Population p2 = new Population(p1.getChromosomes().length);
        Integer maxFF = 0;
        for (int i = 0; i < p1.getChromosomes().length; i++) {
            if (p1.getChromosomes()[i].getFitness() > maxFF) {
                maxFF = p1.getChromosomes()[i].getFitness();
            }
        }
        Integer sumFF = 0;
        for (int i = 0; i < p1.getChromosomes().length; i++) {
            p1.getChromosomes()[i].setFitness(maxFF - p1.getChromosomes()[i].getFitness());
            sumFF += p1.getChromosomes()[i].getFitness();
            p1.getChromosomes()[i].setSelectHelp(sumFF);
        }
        Chromosome[] parentsBuff = new Chromosome[2];
        Chromosome[] childrenBuff = new Chromosome[2];
        for (int i = 0; i < p1.getChromosomes().length; i+=2) {
            Integer select1 = rand.nextInt(sumFF) + 1;
            Integer select2 = rand.nextInt(sumFF) + 1;
            Integer select3 = 0;
            if (select1 > select2) {
                select3 = select2;
                select2 = select1;
                select1 = select3;
            }

            Integer flag1 = 0;
            Integer flag2 = 0;
            for (int j = 0; j < p1.getChromosomes().length; j++) {
                if (select1 >= p1.getChromosomes()[j].getSelectHelp()) {
                    parentsBuff[0] = p1.getChromosomes()[j];
                    flag1 = 1;
                }
                if (select2 >= p1.getChromosomes()[j].getSelectHelp()) {
                    parentsBuff[1] = p1.getChromosomes()[j];
                    flag2 = 1;
                }
            }
            if (flag1 == 0) {
                parentsBuff[0] = p1.getChromosomes()[0];
            }
            if (flag2 == 0) {
                parentsBuff[1] = p1.getChromosomes()[1];
            }


            childrenBuff = breed(parentsBuff[0], parentsBuff[1]);
            p2.setChromosome(childrenBuff[0], i);
            p2.setChromosome(childrenBuff[1], i + 1);
        }


        return p2;
    }

    public static Chromosome[] breed(Chromosome c1, Chromosome c2) {
        Chromosome r1 = new Chromosome(CHROMOSOME_LENGTH);
        Chromosome r2 = new Chromosome(CHROMOSOME_LENGTH);
        Chromosome r3 = new Chromosome(CHROMOSOME_LENGTH);
        Chromosome r4 = new Chromosome(CHROMOSOME_LENGTH);
        Random rand = new Random();
        Integer rp = rand.nextInt(CHROMOSOME_LENGTH - 2) + 1;
        int i;
        for (i = 0; i < c2.getGenes().length; i++) {
            r2.setGene(c2.getGenes()[i], i);
            r4.setGene(c1.getGenes()[i], i);
        }

        for (i = 0; i < rp; i++) {
            r1.setGene(c1.getGenes()[i], i);
        }

        for (int j = 0; j < r1.getGenes().length; j++) {
            for (int k = 0; k < r2.getGenes().length; k++) {
                if (r1.getGenes()[j] == r2.getGenes()[k]) {
                    r2.setGene(-1, k);
                }
            }
        }
        Integer[] filtered = new Integer[c1.getGenes().length];
        Integer filteredIndex = 0;
        for (int j = 0; j < r2.getGenes().length; j++) {
            if (r2.getGenes()[j] != -1) {
                filtered[filteredIndex] = r2.getGenes()[j];
                filteredIndex++;
            }
        }
        filteredIndex = 0;
        for (i = rp; i < c1.getGenes().length; i++) {
            r1.setGene(filtered[filteredIndex], i);
            filteredIndex++;
        }

        for (i = 0; i < rp; i++) {
            r3.setGene(c2.getGenes()[i], i);
        }
        for (int j = 0; j < r3.getGenes().length; j++) {
            for (int k = 0; k < r4.getGenes().length; k++) {
                if (r3.getGenes()[j] == r4.getGenes()[k]) {
                    r4.setGene(-1, k);
                }
            }
        }
        filteredIndex = 0;
        for (int j = 0; j < r4.getGenes().length; j++) {
            if (r4.getGenes()[j] != -1) {
                filtered[filteredIndex] = r4.getGenes()[j];
                filteredIndex++;
            }
        }
        filteredIndex = 0;
        for (i = rp; i < c1.getGenes().length; i++) {
            r3.setGene(filtered[filteredIndex], i);
            filteredIndex++;
        }
        Chromosome[] returnChromosome = new Chromosome[2];
        returnChromosome[0] = r1;
        returnChromosome[1] = r3;


        return returnChromosome;
    }

    public static Population mutate(Population p1) {
        Double mutationsNumber = (POPULATION_LENGTH * MUTATION_PROBABILITY);
        Integer x = mutationsNumber.intValue();
        Random rand = new Random();
        Integer[] list1 = new Integer[CHROMOSOME_LENGTH];
        Integer[] list2 = new Integer[CHROMOSOME_LENGTH];
        Integer l1 = 0;
        Integer l2 = 0;
        for (int i = 0; i < x; i++) {
            l2 = 0;
            int r = rand.nextInt(POPULATION_LENGTH);
            for (int j = 0; j < list1.length; j++) {
                list1[j] = p1.getChromosomes()[r].getGenes()[j];
            }
            for (int j = list1.length; j > 0; j--) {
                list2[l2] = list1[j - 1];
                l2++;
            }

            Chromosome c = new Chromosome(CHROMOSOME_LENGTH);
            c.setGenes(list2);
            p1.setChromosome(c, r);
        }
        return p1;
    }

}