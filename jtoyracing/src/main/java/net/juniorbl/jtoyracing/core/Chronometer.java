package net.juniorbl.jtoyracing.core;

/**
 * Chronometer that is being observed (Observer pattern).
 *
 * @version 1.0 Dec 22, 2007
 * @author Fco. Carlos L. Barros Junior
 */
public interface Chronometer {

    /**
     * Adds an observer to the set of observers of the chronometer.
     *
     * @param chronometerObserver a observer to be added.
     */
	void addObserver(ChronometerObserver chronometerObserver);

    /**
     * Deletes an observer from the set of observers of the chronometer.
     *
     * @param chronometerObserver a observer to be removed.
     */
	void removeObserver(ChronometerObserver chronometerObserver);

	/**
	 * Notifies the observers to update time.
	 *
	 * @param time time's value to update.
	 */
	void notifyObserversUpdateTime(int time);

	/**
	 * Notifies the observers that time's up.
	 */
	void notifyObserversTimeUP();
}
