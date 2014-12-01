package edu.nyu.cs.stopwatch.impl;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import edu.nyu.cs.stopwatch.api.IStopwatch;

/**
 * @author shenli
 * <p>
 * The {@code StopwatchFactory} is a thread-safe factory class for {@link edu.nyu.cs.stopwatch.api.IStopwatch} 
 * objects. It maintains references to all created {@link edu.nyu.cs.stopwatch.api.IStopwatch} objects and 
 * provides a convenient method for getting a list of those objects.
 */
public class StopwatchFactory {
    private static final Set<String> idSet = Collections.synchronizedSet(new CopyOnWriteArraySet<>());
    private static final List<IStopwatch> stopwatches = Collections.synchronizedList(new CopyOnWriteArrayList<>());
    
    /**
     * Suppress default constructor for noninstantiable
     */
    private StopwatchFactory() {
        throw new AssertionError();
    }
    
    /**
     * Creates and returns a new {@link edu.nyu.cs.stopwatch.api.IStopwatch} object.
     * <p>
     * @param id the identifier of the new object
     * @return The new {@link edu.nyu.cs.stopwatch.api.IStopwatch} object
     */
    public static IStopwatch getStopwatch(String id) {
        if (id == null) {
            throw new IllegalArgumentException("StopwatchFactory id: null!");
        } else if (id.equals("")) {
            throw new IllegalArgumentException("StopwatchFactory id: empty!");
        } else if (! idSet.add(id)) {
            throw new IllegalArgumentException("StopwatchFactory id: " + id + "already taken!");
        }
        
        IStopwatch sw = new Stopwatch(id);
        stopwatches.add(sw);
        return sw;
    }

    /**
     * Returns a List of all creates {@link edu.nyu.cs.stopwatch.api.IStopwatch} objects. Returns an empty 
     * list for no {@link edu.nyu.cs.stopwatch.api.IStopwatch} have been created.
     * <p>
     * @return a List of all creates {@link edu.nyu.cs.stopwatch.api.IStopwatch} objects. Returns an empty 
     * list for no {@link edu.nyu.cs.stopwatch.api.IStopwatch} have been created
     */
    public static List<IStopwatch> getStopwatches() {
        return Collections.unmodifiableList(stopwatches);
    }
    
}
