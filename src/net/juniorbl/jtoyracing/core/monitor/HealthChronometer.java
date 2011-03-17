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

	/**
     * One second in milliseconds.
     */
	private static final int ONE_SECOND = 1000;

	/**
     * Indication when the chronometer should stop.
     */
	private boolean stop;

	/**
	 * Amount of time in seconds which a car should wait when his health ends.
	 */
	private int waitSeconds;

	/**
	 * List of observers.
	 */
	private List<ChronometerObserver> chronometerObservers = new ArrayList<ChronometerObserver>();

	/**
	 * Constructor used when the chronometer is monitoring the lack of health.
	 *
	 * @param seconds seconds a car should wait.
	 */
	public HealthChronometer(int seconds) {
		this.waitSeconds = seconds;
	}

	/**
	 * {@inheritDoc}
	 */
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

    /**
     * Stops the chronometer.
     */
	public void stopChronometer() {
		stop = true;
	}

    /**
	 * {@inheritDoc}
	 */
	public void addObserver(ChronometerObserver observadorCronometro) {
		chronometerObservers.add(observadorCronometro);
	}

    /**
     * {@inheritDoc}
     */
	public void removeObserver(ChronometerObserver observadorCronometro) {
		chronometerObservers.remove(observadorCronometro);
	}

    /**
     * {@inheritDoc}
     */
	public void notifyObserversUpdateTime(int tempo) {
		for (ChronometerObserver observadorCronometro : chronometerObservers) {
			observadorCronometro.updateTime(tempo);
		}
	}

    /**
     * {@inheritDoc}
     */
	public void notifyObserversTimeUP() {
		for (ChronometerObserver observadorCronometro : chronometerObservers) {
			observadorCronometro.timeUP();
		}
	}
}
