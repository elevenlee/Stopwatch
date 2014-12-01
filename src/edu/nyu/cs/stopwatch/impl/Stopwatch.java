package edu.nyu.cs.stopwatch.impl;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import edu.nyu.cs.stopwatch.api.IStopwatch;

/**
 * @author shenli
 * <p>
 * The {@code Stopwatch} is a thread-safe class that can be used for timing laps. The {@code Stopwatch} objects 
 * are created in the {@link edu.nyu.cs.stopwatch.impl.StopwatchFactory}. Different threads can share a single 
 * {@code Stopwatch} object and safely call any of the stopwatch methods.
 */
public class Stopwatch implements IStopwatch {
    private static final long MSEC_TO_HOUR = 3600000;
    private static final long MSEC_TO_MINUTE = 60000;
    private static final long MSEC_TO_SECOND = 1000;
    
    private final String id;
    
    private long startTime = -1;
    private long stopTime = -1;
    private long totalTime = 0;
    private boolean running = false;
    private final List<Long> lapTimes = 
            Collections.synchronizedList(new CopyOnWriteArrayList<>());
    
    /**
     * Creates a new {@code Stopwatch} object with specified id. The constructor is package-private, only the 
     * object in the same package could instantiate this object.
     * <p>
     * @param id the specified id for this {@code Stopwatch} object
     */
    Stopwatch(String id) {
        this.id = id;
    }
    
    /**
     * Returns the id of this {@code Stopwatch} object.
     * <p>
     * @return the id of this stopwatch
     */
    public String getId() {
        return id;
    }
    
    /**
     * Starts the stopwatch.
     * <p>
     * @throws IllegalStateException if called when the stopwatch is already running
     */
    public synchronized void start() {
        if (running) {
            throw new IllegalStateException("Stopwatch " + id + " is already running!");
        }
        startTime = System.currentTimeMillis();
        running = true;
    }
    
    /**
     * Stores the time elapsed since the last time {@code lap()} was called or since {@code start()} was called 
     * if this is the first lap.
     * <p>
     * @throws IllegalStateException if called when the stopwatch isn't running
     */
    public synchronized void lap() {
        if (! running) {
            throw new IllegalStateException("Stopwatch " + id + " is not running!");
        }
        stopTime = System.currentTimeMillis();
        final long lapTime = stopTime - startTime;
        lapTimes.add(lapTime);
        totalTime += lapTime;
        startTime = stopTime;
    }
    
    /**
     * Stops the stopwatch (and records one final lap).
     * <p>
     * @throws IllegalStateException if called when the stopwatch isn't running
     */
    public synchronized void stop() {
        if (! running) {
            throw new IllegalStateException("Stopwatch " + id + " is not running!");
        }
        stopTime = System.currentTimeMillis();
        final long lapTime = stopTime - startTime;
        lapTimes.add(lapTime);
        totalTime += lapTime;
        running = false;
    }
    
    /**
     * Resets the stopwatch. If the stopwatch is running, this method stops the watch and resets it. This also 
     * clears all recorded laps.
     */
    public synchronized void reset() {
        startTime = -1;
        stopTime = -1;
        totalTime = 0;
        lapTimes.clear();
        running = false;
    }
    
    /**
     * Returns a list of lap times (in milliseconds). This method can be called at any time and will not throw 
     * an exception.
     * <p>
     * @return a list of recorded lap times or an empty list if no times are recorded.
     */
    public synchronized List<Long> getLapTimes() {      
        return Collections.unmodifiableList(lapTimes);
    }
    
    /**
     * Tests for equality between the specified object and this {@code Stopwatch} object. Two {@code Stopwatch} 
     * are considered equal if the id, running flag, lap times, total time, start time and stop time of their 
     * stopwatch's are equal.
     * <p>
     * @param o the object to test for equality with this {@code Stopwatch} object
     * @return true if the stopwatch are considered equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (! (o instanceof Stopwatch)) {
            return false;
        }
        Stopwatch sw = (Stopwatch) o;
        synchronized (this) {
            return id.equals(sw.id)
                    && running == sw.running
                    && lapTimes.equals(sw.lapTimes)
                    && totalTime == sw.totalTime
                    && startTime == sw.startTime
                    && stopTime == sw.stopTime;
        }
    }
    
    /**
     * Returns the hash code value for this {@code Stopwatch} object. The hash code is generated using the id, 
     * running flag, lap times, total time, start time and stop time.
     * <p>
     * @return a hash code value for this {@code Stopwatch} object
     */
    @Override
    public int hashCode() {
        int result = 0;
        synchronized (this) {
            if (result == 0) {
                final int prime = 31;
                result = 17;
                result = result * prime + id.hashCode();
                result = result * prime + (running ? 1 : 0);
                result = result * prime + lapTimes.hashCode();
                result = result * prime + (int) (totalTime ^ (totalTime >>> 32));
                result = result * prime + (int) (startTime ^ (startTime >>> 32));
                result = result * prime + (int) (stopTime ^ (stopTime >>> 32));
            }
        }
        return result;
    }
    
    /**
     * Returns the string representation of this {@code Stopwatch} object. The string consists of four parts 
     * whose format is "HH:MM:SS.MSE", where HH is the hours, MM is the minutes, SS is the seconds, and MSE is
     * the milliseconds. (Each of the capital letters represents a single decimal digit.)
     * <p>
     * If any of the four parts of this stopwatch is too small to fill up its field, the field is padded with 
     * leading zeros. For example, if the value of the milliseconds is 25, the last three characters of the 
     * string representation will be "025".
     * <p>
     * Note that there is a single colon character separating the first three parts and a dot character 
     * separating seconds and milliseconds.
     * <p>
     * @return a string comprising the hours, minutes, seconds, and milliseconds
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        long elapsedTime;
        synchronized (this) {
            elapsedTime = getTotalTime();
        }
        formatElapsedTime(sb, elapsedTime);
        return sb.toString();
    }
    
    /**
     * Returns the total time of this {@code Stopwatch} object. This method can be called at any time and will 
     * not throw an exception.
     * <p>
     * @return the stopwatch's total time
     */
    private synchronized long getTotalTime() {
        if (startTime == -1) {
            return 0;
        } else if (running) {
            return System.currentTimeMillis() + totalTime - startTime;
        } else {
            return totalTime;
        }
    }
    
    /**
     * Format the string representation of elapsed time. The string consists of four parts whose format is 
     * "HH:MM:SS.MSE", where HH is the hours, MM is the minutes, SS is the seconds, and MSE is the milliseconds. 
     * (Each of the capital letters represents a single decimal digit.)
     * <p>
     * If any of the four parts of elapsed time is too small to fill up its field, the field is padded with 
     * leading zeros. For example, if the value of the milliseconds is 25, the last three characters of the 
     * string representation will be "025".
     * <p>
     * Note that there is a single colon character separating the first three parts and a dot character 
     * separating seconds and milliseconds.
     * <p>
     * @param sb the string representation of formatted elapsed time
     * @param elapsedTime the specified elapsed time to be format
     */
    private static void formatElapsedTime(final StringBuffer sb, final long elapsedTime) {
        long hour = elapsedTime / MSEC_TO_HOUR;
        if (hour < 10) {
            sb.append("0" + hour);
        } else {
            sb.append(hour);
        }
        sb.append(":");
        
        long minute = (elapsedTime - hour * MSEC_TO_HOUR) / MSEC_TO_MINUTE;
        if (minute < 10) {
            sb.append("0" + minute);
        } else {
            sb.append(hour);
        }
        sb.append(":");

        long second = (elapsedTime - hour * MSEC_TO_HOUR - minute * MSEC_TO_MINUTE) / MSEC_TO_SECOND;
        if (second < 10) {
            sb.append("0" + second);
        } else {
            sb.append(second);
        }
        sb.append(".");
        
        long msec = elapsedTime % MSEC_TO_SECOND;
        if (msec < 10) {
            sb.append("00" + msec);
        } else if (msec < 100) {
            sb.append("0" + msec);
        } else {
            sb.append(msec);
        }
    }
    
}
