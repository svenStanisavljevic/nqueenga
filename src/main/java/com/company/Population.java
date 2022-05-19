package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.company.Main.CHROMOSOME_LENGTH;

public class Population {

    private Chromosome[] chromosomes;
    public Population (Integer length) {
        chromosomes = new Chromosome[length];
    }
    public Population initializePopulation (){
        for (int i = 0; i < chromosomes.length; i++) {
            chromosomes[i] = new Chromosome(CHROMOSOME_LENGTH).initialiseChromosome();
        }
        fitnessSort();
        return this;
    }

    public Double avgFF() {
        Double solution = new Double(0);
        for (int i = 0; i < chromosomes.length; i++) {
            solution += chromosomes[i].fitnessfunction();
        }
        return solution / (double) chromosomes.length;
    }

    public Integer bestFF() {
        Integer solution = chromosomes[0].fitnessfunction();
        for (int i = 0; i < chromosomes.length; i++) {
            if (chromosomes[i].fitnessfunction() < solution) {
                solution = chromosomes[i].fitnessfunction();
            }
        }
        return solution;
    }

    public void fitnessSort() {
        List<Chromosome> c = new ArrayList<>();
        for (int i = 0; i < chromosomes.length; i++) {
            c.add(chromosomes[i]);
            chromosomes[i].setFitness(chromosomes[i].fitnessfunction());
        }
        Collections.sort(c);
        for (int i = 0; i < chromosomes.length; i++) {
            chromosomes[i] = c.get(i);
        }
    }

    public void setChromosome(Chromosome chromosome, Integer index) {
        this.chromosomes[index] = chromosome;
    }

    public Chromosome[] getChromosomes() {
        return chromosomes;
    }

    public void setChromosomes(Chromosome[] chromosomes) {
        this.chromosomes = chromosomes;
    }

}