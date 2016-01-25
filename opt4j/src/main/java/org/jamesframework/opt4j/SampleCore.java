
package org.jamesframework.opt4j;

import org.jamesframework.james.asc.FileReader;
import org.opt4j.core.Individual;
import org.opt4j.core.optimizer.Archive;
import org.opt4j.core.start.Opt4JTask;
import org.opt4j.optimizers.sa.SimulatedAnnealingModule;
import org.opt4j.viewer.ViewerModule;

public class SampleCore {

    /**
     * 0: file
     * 1: size
     * 2: runtime (sec)
     */
    public static void main(String[] args) {

        // parse arguments
        String file = args[0];
        int coreSize = Integer.parseInt(args[1]);
        int runtime = Integer.parseInt(args[2]);
        
        // read distance matrix
        double[][] dist = FileReader.read(file).getDistanceMatrix();
        int n = dist.length;
                
        // create search (TODO: implement hill climber based on simulated annealing code)
        SimulatedAnnealingModule sa = new SimulatedAnnealingModule();
        sa.setIterations(1000);
        
        // create and configure problem
        CoreModule cm = new CoreModule();
        
        // run search
        ViewerModule viewer = new ViewerModule();
        viewer.setCloseOnStop(true);
        Opt4JTask task = new Opt4JTask(false);
        task.init(sa, cm, viewer);
        try {
            task.execute();
            Archive archive = task.getInstance(Archive.class);
            System.out.println("Archive size: " + archive.size());
            for (Individual individual : archive) {
                // obtain the phenotype and objective, etc. of each individual
                // ...
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            task.close();
        }
        
        
//        // create problem
//        CoreSelectionProblem problem = new CoreSelectionProblem(n, coreSize, dist);
//
//        // create search
//        OptimizationParameters optParams = OptimizerFactory.hillClimbing(problem, 1);
//        MaximumTimeTerminator sc = new MaximumTimeTerminator();
//        sc.setMaximumTime(runtime);
//        optParams.setTerminator(sc);
//        HillClimbing hc = (HillClimbing) optParams.getOptimizer();
//        hc.setMutationOperator(new SubsetSwapMutation(n));
//
//        // run search
//        BitSet sol = OptimizerFactory.optimizeToBinary(optParams, "eva2.out");
//        
//        // output results
//        Set<Integer> selectedIds = sol.stream().boxed().collect(Collectors.toSet());
//        System.out.println("Best solution: " + selectedIds);
//        System.out.println("Best score: " + -problem.evaluate(sol)[0]);
//        System.out.println("Performed evaluations: " + OptimizerFactory.lastEvalsPerformed());

    }

}
