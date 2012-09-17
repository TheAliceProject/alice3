package org.lgna.story;

public class TimerFrequency implements AddTimeListener.Detail, AddWhileCollisionListener.Detail {

	public final static TimerFrequency ASAP = new TimerFrequency( Double.doubleToLongBits( 0 ) );
	private Long frequency;

	public TimerFrequency( double frequency ) {
		this.frequency = ( new Double( frequency ) ).longValue();
	}

	public static TimerFrequency getValue( Object[] details ) {
		for( Object detail : details ) {
			if( detail instanceof TimerFrequency ) {
				return (TimerFrequency)detail;
			}
		}
		return ASAP;
	}

	public Long getFrequency() {
		return this.frequency;
	}
}
