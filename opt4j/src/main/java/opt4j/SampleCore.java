
package opt4j;

import org.opt4j.core.Individual;
import org.opt4j.core.optimizer.Archive;
import org.opt4j.core.start.Opt4JTask;
import org.opt4j.viewer.ViewerModule;

public class SampleCore {

    /**
     * 0: file
     * 1: size
     * 2: steps
     */
    public static void main(String[] args) {

        run(args);
        System.gc();

    }
    
    private static void run(String[] args) {
        
        // parse arguments
        String file = args[0];
        int coreSize = Integer.parseInt(args[1]);
        int steps = Integer.parseInt(args[2]);
        
        // read distance matrix
        double[][] dist = FileReader.read(file);
        int n = dist.length;
                
        // create search
        HillClimberModule hc = new HillClimberModule();
        hc.setIterations(steps);
        
        // configure operators
        CoreNeighborModule om = new CoreNeighborModule();
                
        // create and configure problem
        CoreCreator.n = n;
        CoreCreator.coreSize = coreSize;
        CoreEvaluator.dist = dist;
        CoreModule cm = new CoreModule();
                   
        // run search
        ViewerModule viewer = new ViewerModule();
        viewer.setCloseOnStop(true);
        Opt4JTask task = new Opt4JTask(false);
        task.init(hc, cm, viewer);
        try {
                        
            long startTime = System.currentTimeMillis();
            task.execute();
            long stopTime = System.currentTimeMillis();
            
            long time = stopTime - startTime;
            
            Archive archive = task.getInstance(Archive.class);
            Individual solInd = archive.iterator().next();
            CoreSolution sol = (CoreSolution) solInd.getPhenotype();
            
            System.out.println("Best solution: " + sol);
            System.out.println("Best score: " + -solInd.getObjectives().array()[0]);
            System.out.println("Runtime (ms): " + time);
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            task.close();
        }
        
    }

}
