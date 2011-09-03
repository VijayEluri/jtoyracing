package net.juniorbl.jtoyracing.core.monitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Chronometer used when a vehicle stops because of lack of health.
 *
 * TODO use chronometer at the beginning of the race.
 *
 * @version 1.0 Dec 22, 2007
 * @author Carlos Luz Junior
 */
public final class HealthChronometer extends Thread implements Chronometer {
	private static final int ONE_SECOND = 1000;
	private boolean stop;
	private int waitSeconds;
	private List<ChronometerObserver> chronometerObservers = new ArrayList<ChronometerObserver>();

	public HealthChronometer(int seconds) {
		this.waitSeconds = seconds;
	}

	@Override
	public void run() {
		while (!stop) {
			try {
				if (waitSeconds >= 0) {
					notifyObserversUpdateTime(waitSeconds);
					sleep(ONE_SECOND);
					--waitSeconds;
				} else {
					stopChronometer();
					notifyObserversTimeUP();
				}
			} catch (InterruptedException exception) {
				stopChronometer();
			}
		}
	}

	public void stopChronometer() {
		stop = true;
	}

	public void addObserver(ChronometerObserver observadorCronometro) {
		chronometerObservers.add(observadorCronometro);
	}

	public void removeObserver(ChronometerObserver observadorCronometro) {
		chronometerObservers.remove(observadorCronometro);
	}

	public void notifyObserversUpdateTime(int seconds) {
		for (ChronometerObserver chronometerObserver : chronometerObservers) {
			chronometerObserver.updateTime(seconds);
		}
	}

	public void notifyObserversTimeUP() {
		for (ChronometerObserver chronometerObserver : chronometerObservers) {
			chronometerObserver.timeUP();
		}
	}
}
