package net.juniorbl.jtoyracing.core.monitor;

import net.juniorbl.jtoyracing.core.JToyRacing;

/**
 * Thread responsible for monitor the car's health.
 *
 * @version 1.0 Jun 18, 2008
 * @author Carlos Luz Junior
 */
public final class HealthMonitor extends Thread {
	private static final int ONE_SECOND = 1000;
	private JToyRacing jToyRacing;

	public HealthMonitor(JToyRacing jToyRacing) {
		this.jToyRacing = jToyRacing;
	}

	@Override
	public void run() {
		try {
			while (true) {
				sleep(ONE_SECOND);
				jToyRacing.updateVehiclesHealth();
			}
		} catch (InterruptedException exception) {
			//FIXME handle this correctly
			throw new Error(exception);
		}
	}
}
