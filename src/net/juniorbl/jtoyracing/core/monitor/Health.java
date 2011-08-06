package net.juniorbl.jtoyracing.core.monitor;

/**
 * Health of the vehicle that is being observed (Observer pattern).
 *
 * @version 1.0 Dec 23, 2007
 * @author Carlos Luz Junior
 */
public interface Health {

	void addObserver(HealthObserver healthObserver);

	void removeObserver(HealthObserver healthObserver);

	void notifyObserversHealthEnded();
}
