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

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 * @author dculyba
 * 
 */
public class ScheduledAudioStream implements Comparable<ScheduledAudioStream>
{
	private AudioInputStream audioStream;
	private org.lgna.common.resources.AudioResource audioResource;
	private double startTime;
	private double volume;
	private double entryPoint;
	private double endPoint;

	private double bytesPerSecond;
	private long bytesRead = 0;
	private boolean initialized = false;

	public ScheduledAudioStream( org.lgna.common.resources.AudioResource audioResource, double startTime, double entryPoint, double endPoint, double volume )
	{
		this.startTime = startTime;
		this.volume = volume;
		this.entryPoint = entryPoint;
		this.endPoint = endPoint;
		this.setAudioResource( audioResource );
	}

	public void setAudioResource( org.lgna.common.resources.AudioResource resource )
	{
		this.audioResource = resource;
	}

	private void initializeIfNecessary()
	{
		if( !this.initialized )
		{
			ByteArrayInputStream dataStream = new ByteArrayInputStream( this.audioResource.getData() );

			try
			{
				this.audioStream = AudioSystem.getAudioInputStream( dataStream );
			} catch( Exception e )
			{
				e.printStackTrace();
			}

			AudioFormat af = this.audioStream.getFormat();
			double frameRate = af.getFrameRate(); //frames per second
			int frameSize = af.getFrameSize(); //bytes per frame
			this.bytesPerSecond = frameRate * frameSize;
			try
			{
				this.audioStream.skip( (long)( this.bytesPerSecond * this.entryPoint ) );
			} catch( Exception e )
			{
				e.printStackTrace();
			}
			this.initialized = true;
		}
	}

	public ScheduledAudioStream( org.lgna.common.resources.AudioResource audioResource, double startTime, double entryPoint, double endPoint )
	{
		this( audioResource, startTime, entryPoint, endPoint, 1 );
	}

	public ScheduledAudioStream( org.lgna.common.resources.AudioResource audioResource, double startTime, double entryPoint )
	{
		this( audioResource, startTime, entryPoint, -1, 1 );
	}

	public ScheduledAudioStream( org.lgna.common.resources.AudioResource audioResource, double startTime )
	{
		this( audioResource, startTime, 0 );
	}

	public org.lgna.common.resources.AudioResource getAudioResource()
	{
		return this.audioResource;
	}

	public AudioInputStream getAudioStream()
	{
		this.initializeIfNecessary();
		return this.audioStream;
	}

	public double getStartTime()
	{
		return this.startTime;
	}

	public double getVolume()
	{
		return this.volume;
	}

	public double secondsRead()
	{
		return this.bytesRead / this.bytesPerSecond;
	}

	public int read( byte[] buffer, int offset, int toRead ) throws IOException
	{
		if( this.endPoint != -1 )
		{
			double totalSecondsToRead = this.endPoint - this.entryPoint;
			double secondsRead = this.secondsRead();
			double secondsLeft = totalSecondsToRead - secondsRead;
			if( secondsLeft <= 0 )
			{
				return -1;
			}
			double secondsToRead = toRead / this.bytesPerSecond;
			if( secondsToRead > secondsLeft )
			{
				toRead = (int)( secondsLeft * this.bytesPerSecond );
			}
		}
		int read = this.audioStream.read( buffer, offset, toRead );
		this.bytesRead += read;
		return read;
	}

	@Override
	public int compareTo( ScheduledAudioStream arg0 )
	{
		return Double.compare( this.startTime, arg0.startTime );
	}

}
