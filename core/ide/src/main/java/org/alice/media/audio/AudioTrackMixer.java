/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may
 *    "Alice" appear in their name, without prior written permission of
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************/
package org.alice.media.audio;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 * @author Dave Culyba
 */
public class AudioTrackMixer {

	private double trackLength;
	private List<ScheduledAudioStream> scheduledStreams;
	private Map<org.lgna.common.resources.AudioResource, org.lgna.common.resources.AudioResource> convertedResourceMap;
	private javax.sound.sampled.AudioFormat targetFormat;

	public AudioTrackMixer( javax.sound.sampled.AudioFormat targetFormat, double trackLength ) {
		this.trackLength = trackLength;
		this.scheduledStreams = new LinkedList<ScheduledAudioStream>();
		this.targetFormat = targetFormat;
		this.convertedResourceMap = new HashMap<org.lgna.common.resources.AudioResource, org.lgna.common.resources.AudioResource>();
	}

	private org.lgna.common.resources.AudioResource convertResourceIfNecessary( org.lgna.common.resources.AudioResource resource ) {
		org.lgna.common.resources.AudioResource convertedResource = null;
		if( this.convertedResourceMap.containsKey( resource ) ) {
			convertedResource = this.convertedResourceMap.get( resource );
		} else {
			if( AudioResourceConverter.needsConverting( resource, this.targetFormat ) ) {
				convertedResource = AudioResourceConverter.convert( resource, this.targetFormat );
			} else {
				convertedResource = resource;
			}
			this.convertedResourceMap.put( resource, convertedResource );
		}
		return convertedResource;
	}

	public void addAudioResource( org.lgna.common.resources.AudioResource resource, double startTime, double entryPoint, double endPoint, double volume ) {
		org.lgna.common.resources.AudioResource convertedResource = convertResourceIfNecessary( resource );
		ScheduledAudioStream scheduledStream = new ScheduledAudioStream( convertedResource, startTime, entryPoint, endPoint, volume );
		addScheduledStream( scheduledStream );
	}

	public void addScheduledStream( ScheduledAudioStream scheduledStream ) {
		org.lgna.common.resources.AudioResource convertedResource = convertResourceIfNecessary( scheduledStream.getAudioResource() );
		if( convertedResource != scheduledStream.getAudioResource() ) {
			scheduledStream.setAudioResource( convertedResource );
		}
		int index = scheduledStreams.size();
		for( int i = 0; i < scheduledStreams.size(); i++ ) {
			if( scheduledStreams.get( i ).compareTo( scheduledStream ) > 0 ) {
				index = i;
				break;
			}
		}
		scheduledStreams.add( index, scheduledStream );
	}

	public AudioInputStream createAudioStream() {
		AudioInputStream audioInputStream = new MixingFloatAudioInputStream( this.targetFormat, this.scheduledStreams, this.trackLength );
		return audioInputStream;
	}

	public void write( OutputStream out ) throws IOException {
		AudioInputStream audioInputStream = createAudioStream();
		AudioSystem.write( audioInputStream, AudioFileFormat.Type.WAVE, out );
	}

}
