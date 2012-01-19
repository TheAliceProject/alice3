package org.lgna.story;


public class AddTimerEventListener {

	public interface Detail{}

	public static Detail timerFrequency(double d) {
		return new TimerFrequency(d);
	}
}
