package opt4j;

import java.util.Random;

import org.opt4j.core.Genotype;
import org.opt4j.core.Individual;
import org.opt4j.core.IndividualFactory;
import org.opt4j.core.Objectives;
import org.opt4j.core.common.random.Rand;
import org.opt4j.core.optimizer.Archive;
import org.opt4j.core.optimizer.IndividualCompleter;
import org.opt4j.core.optimizer.IterativeOptimizer;
import org.opt4j.core.optimizer.Population;
import org.opt4j.core.optimizer.TerminationException;
import org.opt4j.operators.copy.Copy;
import org.opt4j.operators.neighbor.Neighbor;

import com.google.inject.Inject;

/**
 * Adaptation of simulated annealing algorithm from Opt4J.
 */
public class HillClimber implements IterativeOptimizer {

    protected final Random random;

    protected final Neighbor<Genotype> neighbor;

    protected final Copy<Genotype> copy;


    private final IndividualFactory individualFactory;

    private final IndividualCompleter completer;

    private final Population population;

    private final Archive archive;

    /**
     * Constructs a new {@link HillClimber}.
     *
     * @param population the population
     * @param individualFactory the individual factory
     * @param completer the completer
     * @param random the random number generator
     * @param neighbor the neighbor operator
     * @param copy the copy operator
     */
    @Inject
    public HillClimber(Population population, Archive archive,
            IndividualFactory individualFactory, IndividualCompleter completer,
            Rand random, Neighbor<Genotype> neighbor, Copy<Genotype> copy) {
        this.random = random;
        this.neighbor = neighbor;
        this.copy = copy;
        this.individualFactory = individualFactory;
        this.population = population;
        this.completer = completer;
        this.archive = archive;
    }
    
    @Override
    public void initialize() throws TerminationException {

    }

    private double fold;
    private Individual old;

    @Override
    public void next() throws TerminationException {
        
        if (population.isEmpty()) {
            // the first iteration

            old = individualFactory.create();

            population.add(old);
            completer.complete(population);

            fold = f(old);
        } else {
            // all iterations > 1

            Genotype g = copy.copy(old.getGenotype());
            neighbor.neighbor(g);

            Individual y = individualFactory.create(g);

            completer.complete(y);
            archive.update(y);

            double fy = f(y);

            if (fy <= fold) {
                population.remove(old);
                population.add(y);
                fold = fy;
                old = y;
            }

        }
    }

    /**
     * Calculates the sum of the {@link Objectives} of one {@link Individual}.
     *
     * @param individual the individual
     * @return the sum of the objective values
     */
    protected double f(Individual individual) {
        Objectives objectives = individual.getObjectives();

        double sum = 0;

        for (double d : objectives.array()) {
            sum += d;
        }

        return sum;
    }

}
