package net.juniorbl.jtoyracing.core.monitor;

/**
 * Define methods to the chronometer observers (Observer pattern).
 *
 * @version 1.0 Dec 22, 2007
 * @author Carlos Luz Junior
 */
public interface ChronometerObserver {

	void updateTime(int time);

	void timeUP();
}
