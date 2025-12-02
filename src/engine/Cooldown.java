package engine;

import java.util.Random;

/**
 * Imposes a cooldown period between two actions.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class Cooldown {

	/** Cooldown duration. */
	private int milliseconds;
	/** Maximum difference between durations. */
	private int variance;
	/** Duration of this run, varies between runs if variance > 0. */
	private int duration;
	/** Beginning time. */
	private long time;
	/** Clock provider for getting current time. */
	private final Clock clock;
	/** Random number generator for variance. */
	private final Random random;

	/**
	 * Constructor, established the time until the action can be performed
	 * again.
	 *
	 * @param milliseconds
	 *            Time until cooldown period is finished.
	 * @param clock
	 *            The clock to use for time checking.
	 */
	public Cooldown(final int milliseconds, final Clock clock) {
		this(milliseconds, 0, clock, new Random());
	}

	/**
	 * Constructor, established the time until the action can be performed
	 * again, with a variation of +/- variance.
	 *
	 * @param milliseconds
	 *            Time until cooldown period is finished.
	 * @param variance
	 *            Variance in the cooldown period.
	 * @param clock
	 *            The clock to use for time checking.
	 * @param random
	 *            The random number generator for variance.
	 */
	public Cooldown(final int milliseconds, final int variance, final Clock clock, final Random random) {
		this.milliseconds = milliseconds;
		this.variance = variance;
		this.duration = milliseconds;
		this.time = 0;
		this.clock = clock;
		this.random = random;
	}

	/**
	 * Checks if the cooldown is finished.
	 *
	 * @return Cooldown state.
	 */
	public final boolean checkFinished() {
		if ((this.time == 0)
				|| this.time + this.duration < this.clock.now())
			return true;
		return false;
	}

	/**
	 * Restarts the cooldown.
	 */
	public final void reset() {
		this.time = this.clock.now();
		if (this.variance != 0) {
			// Calculate the range for the random value.
			int min = this.milliseconds - this.variance;
			int max = this.milliseconds + this.variance;
			// Ensure min is not negative.
			if (min < 0) {
				min = 0;
			}
			// Get a random duration within the range [min, max].
			this.duration = min + this.random.nextInt(max - min + 1);
		}
	}


	/**
	 * Sets the cooldown duration.
	 *
	 * @param milliseconds
	 *            New cooldown duration.
	 */
	public final void setMilliseconds(final int milliseconds) {
		this.milliseconds = milliseconds;
		this.duration = milliseconds;
	}
}

