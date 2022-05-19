package com.company;

import java.util.*;

import static com.company.Main.MULTIPLICATOR_COEFICIENT;
import static com.company.Main.POPULATION_LENGTH;

public class Chromosome implements Comparable<Chromosome>{
    private Integer fitness;
    private Integer[] genes;
    private Integer selectHelp;
    public Chromosome(Integer length) {
        genes = new Integer[length];
    }
    public Chromosome initialiseChromosome() {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < genes.length; i++) {
            genes[i] = i;
        }
        for (int i : genes) {
            list.add(genes[i]);
        }
        Collections.shuffle(list);
        for (int i = 0; i < list.size(); i++) {
            genes[i] = list.get(i);
            System.out.print(genes[i] + " ");
        }
        System.out.println(" ");
        return this;
    }

    public void drawChromosome() {
        for (int i = 0; i < genes.length; i++) {
            int j = 0;
            for (j = 0; j < genes[i]; j++) {
                System.out.print("-");
            }
            System.out.print("x");
            for (j = j; j < genes.length; j++) {
                System.out.print("-");
            }
            System.out.println();
        }
        System.out.println("ff: " + fitnessfunction());
    }

    public void setGene (Integer gene, Integer index) {
        this.genes[index] = gene;
    }

    public Integer fitnessfunction() {

        Integer returnValue = 0;

        //gornji lijevi sweep
        for (int i = 1; i < genes.length; i++) {
            int j = 0, k = i; //j za stupce, k za redove
            Integer queens = 0;
            while (k >= 0) {
                if (genes[k] == j) {
                    queens++;
                }
                j++;
                k--;
            }
            if (queens >= 2) {
                returnValue += (queens - 1);
            }
        }

        //donji lijevi sweep
        for (int i = 1; i < genes.length - 1; i++) {
            int j = genes.length - 1, k = i;
            Integer queens = 0;
            for (int x = 0; x < genes.length - i; x++) {
                if (genes[k + x] == j) {
                    queens++;
                }
                j--;
            }
            if (queens >= 2) {
                returnValue += (queens - 1);
            }
        }

        //gornji desni sweep
        for (int i = 0; i < genes.length - 1; i++) {
            Integer queens = 0;
            for (int j = 0; j < genes.length - i; j++) {
                if (genes[j] == j + i) {
                    queens++;
                }
            }
            if (queens >= 2) {
                returnValue += (queens - 1);
            }
        }

        //donji desni sweep
        for (int i = 1; i < genes.length - 1; i++) {
            Integer queens = 0;
            for (int j = 0; j < genes.length - i; j++) {
                if (genes[j] == j - i) {
                    queens++;
                }
                if (queens >= 2) {
                    returnValue += (queens - 1);
                }
            }
        }

        return returnValue * MULTIPLICATOR_COEFICIENT;
    }

    public Integer[] getGenes() {
        return this.genes;
    }

    public void setGenes(Integer[] genes) {
        this.genes = genes;
    }

    public Integer getFitness() {
        return fitness;
    }

    public void setFitness(Integer fitness) {
        this.fitness = fitness;
    }

    public Integer getSelectHelp() {
        return selectHelp;
    }

    public void setSelectHelp(Integer selectHelp) {
        this.selectHelp = selectHelp;
    }

    @Override
    public int compareTo(Chromosome o) {
        return this.fitness - o.fitness.intValue();
    }
}
