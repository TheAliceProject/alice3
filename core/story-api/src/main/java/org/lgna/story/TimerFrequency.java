package org.lgna.story;

import org.lgna.story.AddTimeListener.Detail;

public class TimerFrequency implements
		AddTimeListener.Detail {

	public final static TimerFrequency ASAP = new TimerFrequency( Double.doubleToLongBits( 0 ) );
	private Double frequency;

	public TimerFrequency( double frequency ) {
		this.frequency = new Double( frequency );
	}

	public static TimerFrequency getValue( Detail[] details ) {
		for( Detail detail : details ) {
			if( detail instanceof TimerFrequency ) {
				return (TimerFrequency)detail;
			}
		}
		return ASAP;
	}

	public Double getFrequency() {
		return this.frequency;
	}

	//Testing a different way to make keyword parameters on the java side
	public static TimerFrequency make( double value ) {
		return new TimerFrequency( value );
	}
}
