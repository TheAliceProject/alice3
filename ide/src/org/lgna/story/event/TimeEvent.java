package org.lgna.story.event;

public class TimeEvent extends AbstractEvent {

	private Long timeSinceLastFire;

	public TimeEvent( Long timeElapsed ) {
		this.timeSinceLastFire = timeElapsed / 1000;
	}

	public Long getTimeSinceLastFire() {
		return this.timeSinceLastFire;
	}

}
