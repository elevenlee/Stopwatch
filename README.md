For this assignment you will implement a thread-safe stopwatch library. Your library implementation will provide stopwatch objects for timing tasks. The stopwatch objects support the tpical operations of a physical stopwatch: start, stop, restart, and the recording of laps (times intervals). Also, a stopwatch can be asked for a list of all the lap times that have been recorded using that stopwatch (if the stopwatch has just been started and stopped once, then the list is of size one and contains the elapsed time).

Both the stopwatch objects and the stopwatch factory need to be thread-safe. That means they might be shared by a number of different threads. 

You've also been provided with a small demo application that uses some of the stopwatch functionality (this application does not prove that our code is threadsafe, it's just a small app that calls some stopwatch methods)
