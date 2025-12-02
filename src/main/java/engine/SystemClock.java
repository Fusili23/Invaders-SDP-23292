package engine;

/**
 * A Clock implementation that returns the real system time.
 */
public class SystemClock implements Clock {
    /**
     * {@inheritDoc}
     */
    @Override
    public long now() {
        return System.currentTimeMillis();
    }
}
