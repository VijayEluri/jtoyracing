package net.juniorbl.jtoyracing.core;

/**
 * Health of the vehicle that is being observed (Observer pattern).
 *
 * @version 1.0 Dec 23, 2007
 * @author Carlos Luz Junior
 */
public interface Health {

    /**
     * Adds an observer to the set of observers of the car's health.
     *
     * @param healthObserver an observer to be added.
     */
	void addObserver(HealthObserver healthObserver);

	/**
	 * Deletes an observer from the set of observers of the car's health.
	 *
	 * @param healthObserver an observer to be removed.
	 */
	void removeObserver(HealthObserver healthObserver);

	/**
	 * Notifies the observers when the health ends.
	 */
	void notifyObserversHealthEnded();
}
