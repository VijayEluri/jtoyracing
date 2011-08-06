package net.juniorbl.jtoyracing.core.monitor;

/**
 * Chronometer that is being observed (Observer pattern).
 *
 * @version 1.0 Dec 22, 2007
 * @author Carlos Luz Junior
 */
public interface Chronometer {

	void addObserver(ChronometerObserver chronometerObserver);

	void removeObserver(ChronometerObserver chronometerObserver);

	void notifyObserversUpdateTime(int time);

	void notifyObserversTimeUP();
}
