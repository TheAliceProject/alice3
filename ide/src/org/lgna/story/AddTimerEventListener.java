package org.lgna.story;


public class AddTimerEventListener {

	@org.lgna.project.annotations.ClassTemplate( keywordFactoryCls=AddTimerEventListener.class )
	public interface Detail{}

	public static Detail timerFrequency(double d) {
		return new TimerFrequency(d);
	}
}
