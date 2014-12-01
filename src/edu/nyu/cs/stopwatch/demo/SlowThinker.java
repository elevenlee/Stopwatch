package edu.nyu.cs.stopwatch.demo;

import java.util.List;
import java.util.logging.Logger;

import edu.nyu.cs.stopwatch.api.IStopwatch;
import edu.nyu.cs.stopwatch.impl.StopwatchFactory;

/**
 * This is a simple program that demonstrates just some of the functionality of the 
 * {@link edu.nyu.cs.stopwatch.api.IStopwatch} interface and {@link edu.nyu.cs.stopwatch.impl.StopwatchFactory} 
 * class.
 * <p>
 * Just because this class runs successfully does not mean that the assignment is complete. It is up to you to 
 * implement the methods of {@link edu.nyu.cs.stopwatch.api.IStopwatch} and {@link edu.nyu.cs.stopwatch.impl.StopwatchFactory}.
 */
public class SlowThinker {

    /** use a logger instead of System.out.println */
    private static final Logger LOGGER =  Logger.getLogger("edu.nyu.cs.stopwatch.demo.SlowThinker");

    /**
     * Run the {@code SlowThinker} demo application.
     * <p>
     * @param args a single argument specifying the number of threads
     */
    public static void main(String[] args) {
        SlowThinker thinker = new SlowThinker();
        thinker.go();
    }

    /**
     * Starts the {@code SlowThinker} object It will get a stopwatch, set a number of lap times, stop the watch
     * and then print out all the lap times.
     */
    private void go() {
        Runnable runnable = new Runnable() {
            public void run() {
                IStopwatch stopwatch = StopwatchFactory.getStopwatch(
                        "ID " + Thread.currentThread().getId());
                stopwatch.start();
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ie) {
                        /* safely ignore this */
                    }
                    stopwatch.lap();
                }
                stopwatch.stop();
                List<Long> times = stopwatch.getLapTimes();
                LOGGER.info(times.toString());
            }
        };
        Thread thinkerThread = new Thread(runnable);
        thinkerThread.start();
    }
    
}
