/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.apis.moveandturn;

import edu.cmu.cs.dennisc.alice.annotations.*;

/**
 * @author Dennis Cosgrove
 */
public class AudioSource /*implements edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable*/ {
	public static boolean isWithinReasonableEpsilonOfDefaultVolume( double volume ) {
		return edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( edu.cmu.cs.dennisc.media.MediaFactory.DEFAULT_VOLUME, volume );
	}
	public static boolean isWithinReasonableEpsilonOfDefaultStartTime( double startTime ) {
		return edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( edu.cmu.cs.dennisc.media.MediaFactory.DEFAULT_START_TIME, startTime );
	}
	public static boolean isDefaultStopTime_aka_NaN( double stopTime ) {
		return Double.isNaN( stopTime );
	}
	
	private org.alice.virtualmachine.resources.AudioResource audioResource;
	private Double volume;
	private Double startTime;
	private Double stopTime;
	@ConstructorTemplate( visibility=Visibility.PRIME_TIME )
	public AudioSource( 
			org.alice.virtualmachine.resources.AudioResource audioResource, 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=VolumeLevel.class )
			Number volume, 
			Number startTime, 
			Number stopTime ) {
		this.audioResource = audioResource;
		this.setVolume( volume );
		this.setStartTime( startTime );
		this.setStopTime( stopTime );
	}
	@ConstructorTemplate( visibility=Visibility.CHAINED )
	public AudioSource( org.alice.virtualmachine.resources.AudioResource audioResource, 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=VolumeLevel.class )
			Number volume, 
			Number startTime ) {
		this( audioResource, volume, startTime, edu.cmu.cs.dennisc.media.MediaFactory.DEFAULT_STOP_TIME );
	}
	@ConstructorTemplate( visibility=Visibility.CHAINED )
	public AudioSource( org.alice.virtualmachine.resources.AudioResource audioResource, 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=VolumeLevel.class )
			Number volume ) {
		this( audioResource, volume, edu.cmu.cs.dennisc.media.MediaFactory.DEFAULT_START_TIME );
	}
	@ConstructorTemplate( visibility=Visibility.CHAINED )
	public AudioSource( org.alice.virtualmachine.resources.AudioResource audioResource ) {
		this( audioResource, edu.cmu.cs.dennisc.media.MediaFactory.DEFAULT_VOLUME );
	}
	
	@MethodTemplate( visibility=Visibility.COMPLETELY_HIDDEN )
	public org.alice.virtualmachine.resources.AudioResource getAudioResource() {
		return this.audioResource;
	}

	public Double getVolume() {
		return this.volume;
	}
	private void setVolume( Number volume ) {
		this.volume = volume.doubleValue();
	}
	public Double getStartTime() {
		return this.startTime;
	}
	private void setStartTime( Number startTime) {
		if( startTime != null ) {
			this.startTime = startTime.doubleValue();
		} else {
			this.startTime = Double.NaN;
		}
	}
	public Double getStopTime() {
		return this.stopTime;
	}
	private void setStopTime( Number stopTime) {
		if( stopTime != null ) {
			this.stopTime = stopTime.doubleValue();
		} else {
			this.stopTime = Double.NaN;
		}
	}
}
