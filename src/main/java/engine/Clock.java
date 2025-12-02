package engine;

/**
 * An interface for providing the current time. This allows for swapping the
 * system clock with a mock clock for testing purposes.
 */
public interface Clock {
    /**
     * @return The current time in milliseconds, as a long.
     */
    long now();
}
