package org.lgna.story.event;

public class TimeEvent extends AbstractEvent {

	private final Double timeSinceLastFire;

	public TimeEvent( Double timeElapsed ) {
		this.timeSinceLastFire = timeElapsed;
	}

	public Double getTimeSinceLastFire() {
		return this.timeSinceLastFire;
	}

}
