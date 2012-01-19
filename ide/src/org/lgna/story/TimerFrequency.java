package org.lgna.story;

import org.lgna.story.AddTimerEventListener.Detail;

public class TimerFrequency implements AddTimerEventListener.Detail {

	public final static TimerFrequency ASAP = new TimerFrequency(Double.doubleToLongBits(0));
	public final static TimerFrequency AS_SOON_AS_FINISHED = new TimerFrequency(Double.doubleToLongBits(-1));
	private Long frequency;
	
	public TimerFrequency(double frequency) {
		this.frequency = (new Double(frequency)).longValue();
	}

	public static TimerFrequency getValue(Detail[] details) {
		for(Detail detail: details){
			if (detail instanceof TimerFrequency) {
				return (TimerFrequency) detail;
			}
		}
		return ASAP;
	}

	public Long getFrequency(){
		return this.frequency;
	}
}
