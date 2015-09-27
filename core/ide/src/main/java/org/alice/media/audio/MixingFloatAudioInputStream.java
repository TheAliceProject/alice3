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
/*
 *	MixingFloatAudioInputStream
 *
 *	This file is part of jsresources.org
 */

package org.alice.media.audio;

/*
 |<---            this code is formatted to fit into 80 columns             --->|
 */

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

/**
 * Mixing of multiple AudioInputStreams to one AudioInputStream. This class
 * takes a collection of AudioInputStreams and mixes them together. Being a
 * subclass of AudioInputStream itself, reading from instances of this class
 * behaves as if the mixdown result of the input streams is read.
 * <p>
 * This class uses the FloatSampleBuffer for easy conversion using normalized
 * samples in the buffers.
 *
 * @author Florian Bomers
 * @author Matthias Pfisterer
 */
public class MixingFloatAudioInputStream extends AudioInputStream {
	private List<ScheduledAudioStream> audioInputStreamList;

	/**
	 * Attenuate the stream by how many dB per mixed stream. For example, if
	 * attenuationPerStream is 2dB, and 3 streams are mixed together, the mixed
	 * stream will be attenuated by 6dB. Set to 0 to not attenuate the signal,
	 * which will usually give good results if only 2 streams are mixed
	 * together.
	 */
	private float attenuationPerStream = 0.1f;

	/**
	 * The linear factor to apply to all samples (derived from
	 * attenuationPerStream). This is a factor in the range of 0..1 (depending
	 * on attenuationPerStream and the number of streams).
	 */
	private float attenuationFactor = 1.0f;
	private double length = 0;
	private long totalBytes = 0;
	private FloatSampleBuffer mixBuffer;
	private FloatSampleBuffer readBuffer;

	private long bytesRead = 0;

	private float bytesPerSecond;

	/**
	 * A buffer for byte to float conversion.
	 */
	private byte[] tempBuffer;

	public MixingFloatAudioInputStream( AudioFormat audioFormat,
			Collection<ScheduledAudioStream> audioInputStreams, double length ) {
		super( new ByteArrayInputStream( new byte[ 0 ] ), audioFormat,
				AudioSystem.NOT_SPECIFIED );

		this.length = length;
		audioInputStreamList = new ArrayList<ScheduledAudioStream>( audioInputStreams );

		// set up the static mix buffer with initially no samples. Note that
		// using a static mix buffer prevents that this class can be used at
		// once from different threads, but that wouldn't be useful anyway. But
		// by re-using this buffer we save a lot of garbage collection.
		mixBuffer = new FloatSampleBuffer( audioFormat.getChannels(), 0,
				audioFormat.getSampleRate() );

		// ditto for the read buffer. It is used for reading samples from the
		// underlying streams.
		readBuffer = new FloatSampleBuffer();

		// calculate the linear attenuation factor
		attenuationFactor = decibel2linear( -1.0f * attenuationPerStream
				* audioInputStreamList.size() );

		bytesRead = 0;

		float frameRate = this.format.getFrameRate(); //frames per second
		int frameSize = this.format.getFrameSize(); //bytes per frame
		this.bytesPerSecond = frameRate * frameSize;
		this.totalBytes = (long)( this.bytesPerSecond * this.length );
	}

	public float secondsProcessed() {
		return this.bytesRead / this.bytesPerSecond;
	}

	/**
	 * The maximum of the frame length of the input stream is calculated and
	 * returned. If at least one of the input streams has length
	 * <code>AudioInputStream.NOT_SPECIFIED</code>, this value is returned.
	 */
	@Override
	public long getFrameLength() {
		return this.totalBytes / this.format.getFrameSize();
	}

	@Override
	public int read() throws IOException {
		byte[] samples = new byte[ 1 ];
		int ret = read( samples );
		if( ret != 1 ) {
			return -1;
		}
		return samples[ 0 ];
	}

	@Override
	public int read( byte[] abData, int nOffset, int nLength ) throws IOException {
		float secondsElapsed = this.secondsProcessed();

		if( this.bytesRead >= this.totalBytes ) {
			return -1;
		}

		// set up the mix buffer with the requested size
		mixBuffer.changeSampleCount( nLength / getFormat().getFrameSize(), false );

		// initialize the mixBuffer with silence
		mixBuffer.makeSilence();

		// remember the maximum number of samples actually mixed
		int maxMixed = 0;

		Iterator<ScheduledAudioStream> streamIterator = audioInputStreamList.iterator();
		while( streamIterator.hasNext() ) {
			ScheduledAudioStream scheduledStream = (ScheduledAudioStream)streamIterator.next();
			//skip streams that haven't started yet
			if( scheduledStream.getStartTime() > secondsElapsed ) {
				continue;
			}
			AudioInputStream stream = scheduledStream.getAudioStream();

			// calculate how many bytes we need to read from this stream

			int sampleCount = mixBuffer.getSampleCount();

			int needRead = sampleCount * stream.getFormat().getFrameSize();

			// set up the temporary byte buffer
			if( ( tempBuffer == null ) || ( tempBuffer.length < needRead ) ) {
				tempBuffer = new byte[ needRead ];
			}

			// read from the source stream
			int bytesRead = scheduledStream.read( tempBuffer, 0, needRead );
			if( bytesRead == -1 ) {
				// end of stream: remove it from the list of streams.
				streamIterator.remove();
				continue;
			}
			// now convert this buffer to float samples
			readBuffer.initFromByteArray( tempBuffer, 0, bytesRead,
					stream.getFormat() );
			if( maxMixed < readBuffer.getSampleCount() ) {
				maxMixed = readBuffer.getSampleCount();
			}

			// the actual mixing routine: add readBuffer to mixBuffer
			// can only mix together as many channels as available
			int maxChannels = Math.min( mixBuffer.getChannelCount(),
					readBuffer.getChannelCount() );
			for( int channel = 0; channel < maxChannels; channel++ ) {
				// get the arrays of the normalized float samples
				float[] readSamples = readBuffer.getChannel( channel );
				float[] mixSamples = mixBuffer.getChannel( channel );
				// Never use readSamples.length or mixSamples.length: the length
				// of the array may be longer than the actual buffer ("lazy"
				// deletion).
				int maxSamples = Math.min( mixBuffer.getSampleCount(),
						readBuffer.getSampleCount() );
						// in a loop, add each "read" sample to the mix buffer
						// can only mix as many samples as available. Also apply the
						// attenuation factor.

				// Note1: the attenuation factor could also be applied only once
				// in a separate loop after mixing all the streams together,
				// saving processor time in case of many mixed streams.

				// Note2: adding everything together here will not cause
				// clipping, because all samples are in float format.
				for( int sample = 0; sample < maxSamples; sample++ ) {
					mixSamples[ sample ] += attenuationFactor * readSamples[ sample ] * scheduledStream.getVolume();
				}
			}

		} // loop over streams

		//No streams processed, so uses the silence
		if( maxMixed == 0 ) {
			maxMixed = mixBuffer.getSampleCount();
		}
		// finally convert the mix Buffer to the requested byte array.
		// This routine will handle clipping, i.e. if there are samples > 1.0f
		// in the mix buffer, they will be clipped to 1.0f and converted to the
		// specified audioFormat's sample format.
		mixBuffer.convertToByteArray( abData, nOffset, getFormat() );

		long bytesProcessed = maxMixed * getFormat().getFrameSize();

		if( ( bytesProcessed + this.bytesRead ) > this.totalBytes ) {
			bytesProcessed = this.totalBytes - this.bytesRead;
		}

		this.bytesRead += bytesProcessed;

		return (int)bytesProcessed;
	}

	/**
	 * calls skip() on all input streams. There is no way to assure that the
	 * number of bytes really skipped is the same for all input streams. Due to
	 * that, this method always returns the passed value. In other words: the
	 * return value is useless (better ideas appreciated).
	 */
	@Override
	public long skip( long lLength ) throws IOException {
		Iterator<ScheduledAudioStream> streamIterator = audioInputStreamList.iterator();
		while( streamIterator.hasNext() ) {
			ScheduledAudioStream scheduledStream = (ScheduledAudioStream)streamIterator.next();
			AudioInputStream stream = scheduledStream.getAudioStream();
			stream.skip( lLength );
		}
		return lLength;
	}

	/**
	 * The minimum of available() of all input stream is calculated and
	 * returned.
	 */
	@Override
	public int available() throws IOException {
		int avail = (int)( this.totalBytes - this.bytesRead );
		return avail;
	}

	@Override
	public void close() throws IOException {
		// TODO: should we close all streams in the list?
	}

	/**
	 * Calls mark() on all input streams.
	 */
	@Override
	public void mark( int nReadLimit ) {
		Iterator<ScheduledAudioStream> streamIterator = audioInputStreamList.iterator();
		while( streamIterator.hasNext() ) {
			ScheduledAudioStream scheduledStream = (ScheduledAudioStream)streamIterator.next();
			AudioInputStream stream = scheduledStream.getAudioStream();
			stream.mark( nReadLimit );
		}
	}

	/**
	 * Calls reset() on all input streams.
	 */
	@Override
	public void reset() throws IOException {
		this.bytesRead = 0;
		Iterator<ScheduledAudioStream> streamIterator = audioInputStreamList.iterator();
		while( streamIterator.hasNext() ) {
			ScheduledAudioStream scheduledStream = (ScheduledAudioStream)streamIterator.next();
			AudioInputStream stream = scheduledStream.getAudioStream();
			stream.reset();
		}
	}

	/**
	 * returns true if all input stream return true for markSupported().
	 */
	@Override
	public boolean markSupported() {
		Iterator<ScheduledAudioStream> streamIterator = audioInputStreamList.iterator();
		while( streamIterator.hasNext() ) {
			ScheduledAudioStream scheduledStream = (ScheduledAudioStream)streamIterator.next();
			AudioInputStream stream = scheduledStream.getAudioStream();
			if( !stream.markSupported() ) {
				return false;
			}
		}
		return true;
	}

	public static float decibel2linear( float decibels ) {
		return (float)Math.pow( 10.0, decibels / 20.0 );
	}

}

/** * MixingFloatAudioInputStream.java ** */
