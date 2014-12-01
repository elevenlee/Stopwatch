package edu.nyu.cs.stopwatch.api;

import java.util.List;

/**
 * A thread-safe object that can be used for timing laps. The {@link edu.nyu.cs.stopwatch.impl.Stopwatch} 
 * objects are created in the {@link edu.nyu.cs.stopwatch.impl.StopwatchFactory}. Different threads can share 
 * a single {@link edu.nyu.cs.stopwatch.impl.Stopwatch} object and safely call any of the
 * {@link edu.nyu.cs.stopwatch.impl.Stopwatch} methods.
 */
public interface IStopwatch {

    /**
     * Returns the id of this {@link edu.nyu.cs.stopwatch.impl.Stopwatch} object.
     * <p>
     * @return the id of this {@link edu.nyu.cs.stopwatch.impl.Stopwatch} object
     */
    public String getId();

    /**
     * Starts the stopwatch.
     * <p>
     * @throws IllegalStateException if called when the {@link edu.nyu.cs.stopwatch.impl.Stopwatch} is 
     * already running
     */
    public void start();

    /**
     * Stores the time elapsed since the last time {@code lap()} was called or since {@code start()} was 
     * called if this is the first lap.
     * <p>
     * @throws IllegalStateException if called when the {@link edu.nyu.cs.stopwatch.impl.Stopwatch} isn't 
     * running
     */
    public void lap();

    /**
     * Stops the {@link edu.nyu.cs.stopwatch.impl.Stopwatch} (and records one final lap).
     * <p>
     * @throws IllegalStateException if called when the {@link edu.nyu.cs.stopwatch.impl.Stopwatch} isn't 
     * running
     */
    public void stop();

    /**
     * Resets the {@link edu.nyu.cs.stopwatch.impl.Stopwatch}. If the {@link edu.nyu.cs.stopwatch.impl.Stopwatch} 
     * is running, this method stops the watch and resets it. This also clears all recorded laps.
     */
    public void reset();

    /**
     * Returns a list of lap times (in milliseconds). This method can be called at any time and will not throw 
     * an exception.
     * <p>
     * @return a list of recorded lap times or an empty list if no times are recorded.
     */
    public List<Long> getLapTimes();
    
}
