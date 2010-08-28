package net.juniorbl.jtoyracing.core;

/**
 * Define methods to the chronometer observers (Observer pattern).
 *
 * @version 1.0 Dec 22, 2007
 * @author Carlos Luz Junior
 */
public interface ChronometerObserver {

    /**
     * Notification to update time.
     *
     * @param time time's value to update.
     */
	void updateTime(int time);

    /**
     * Notification when time's up.
     */
	void timeUP();
}
