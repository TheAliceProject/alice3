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
import java.io.File;
import java.io.IOException;

import javax.media.ControllerClosedEvent;
import javax.media.ControllerErrorEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.DataSink;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Processor;
import javax.media.control.TrackControl;
import javax.media.datasink.DataSinkErrorEvent;
import javax.media.datasink.DataSinkEvent;
import javax.media.datasink.DataSinkListener;
import javax.media.datasink.EndOfStreamEvent;
import javax.media.protocol.FileTypeDescriptor;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import com.sun.media.renderer.audio.device.JavaSoundOutput;

import edu.cmu.cs.dennisc.media.jmf.MediaFactory;

/**
 * @author Dave Culyba
 */
public class AudioResourceConverter implements ControllerListener, DataSinkListener {

	private org.lgna.common.resources.AudioResource audioResource;
	private Processor processor = null;
	private DataSink dataSink = null;
	private boolean boolSaving = false;

	boolean stateFailed = false;
	boolean isWaiting = false;
	Object stateLock = new Object();
	Object waitLock = new Object();

	class StateListener implements ControllerListener {
		@Override
		public void controllerUpdate( ControllerEvent ce ) {
			if( ce instanceof ControllerClosedEvent ) {
				stateFailed = true;
			}
			synchronized( stateLock ) {
				stateLock.notifyAll();
			}
		}
	}

	public AudioResourceConverter( org.lgna.common.resources.AudioResource audioResource ) {
		this.audioResource = audioResource;
	}

	private static File createTempFile() throws IOException {
		final File temp;

		temp = File.createTempFile( Long.toString( System.nanoTime() ), ".wav" );
		temp.deleteOnExit();

		if( !( temp.delete() ) ) {
			throw new IOException( "Could not delete temp file: "
					+ temp.getAbsolutePath() );
		}

		if( !( temp.mkdir() ) ) {
			throw new IOException( "Could not create temp directory: "
					+ temp.getAbsolutePath() );
		}

		return ( temp );
	}

	private synchronized boolean waitForState( Processor p, int state ) {
		p.addControllerListener( new StateListener() );
		stateFailed = false;
		if( state == Processor.Configured ) {
			p.configure();
		} else if( state == Processor.Realized ) {
			p.realize();
		}
		while( ( p.getState() < state ) && !stateFailed ) {
			synchronized( stateLock ) {
				try {
					stateLock.wait();
				} catch( InterruptedException ie ) {
					return false;
				}
			}
		}
		return ( !stateFailed );
	}

	@Override
	public void dataSinkUpdate( DataSinkEvent event ) {
		if( event instanceof EndOfStreamEvent ) {
			closeDataSink();
		} else if( event instanceof DataSinkErrorEvent ) {
			stopSaving();
		}
	}

	/**
	 * This method cleans up after the completion of the file save procedure.
	 */
	private void stopSaving() {
		boolSaving = false;
		if( processor != null ) {
			processor.stop();
			processor.close();
			processor = null;
			if( dataSink == null ) {
				onDone();
			}
		}
	}

	private void closeDataSink() {
		synchronized( this ) {
			if( dataSink != null ) {
				dataSink.close();
			}
			dataSink = null;
			if( processor == null ) {
				onDone();
			}
		}
	}

	@Override
	public void controllerUpdate( ControllerEvent event ) {
		if( event instanceof ControllerErrorEvent ) {
			if( boolSaving == true ) {
				stopSaving();
			}
		} else if( event instanceof EndOfMediaEvent ) {
			if( boolSaving == true ) {
				stopSaving();
			}
		}
	}

	private void onDone() {
		long endTime = System.currentTimeMillis();
		long dif = endTime - startTime;
		System.out.println( "Took: " + ( dif * .001 ) + " seconds" );
		synchronized( waitLock ) {
			isWaiting = false;
		}

	}

	private boolean waitForFileDone() {
		synchronized( waitLock ) {
			try {
				while( isWaiting ) {
					waitLock.wait( 100 );
				}
			} catch( Exception e ) {
				return false;
			}
		}
		return true;
	}

	private long startTime;

	public org.lgna.common.resources.AudioResource convertTo( AudioFormat destFormat ) {
		javax.media.format.AudioFormat convertToFormat = JavaSoundOutput.convertFormat( destFormat );
		return convertTo( convertToFormat );
	}

	public org.lgna.common.resources.AudioResource convertTo( javax.media.format.AudioFormat destFormat ) {
		//		System.out.println( "Converting " + this.audioResource.getOriginalFileName() );

		startTime = System.currentTimeMillis();
		try {
			javax.media.protocol.DataSource dataSource = new edu.cmu.cs.dennisc.javax.media.protocol.ByteArrayDataSource(
					this.audioResource.getData(),
					this.audioResource.getContentType() );
			this.processor = Manager.createProcessor( dataSource );

			processor.addControllerListener( this );

			if( !waitForState( processor, Processor.Configured ) ) {
				return null;
			}

			TrackControl[] trackControls = processor.getTrackControls();
			assert trackControls.length == 1;
			TrackControl control = trackControls[ 0 ];

			processor.setContentDescriptor( new FileTypeDescriptor(
					FileTypeDescriptor.WAVE ) );

			control.setEnabled( true );
			control.setFormat( destFormat );

			if( !waitForState( processor, Processor.Realized ) ) {
				return null;
			}
			boolSaving = true;

			dataSource = processor.getDataOutput();

			File tempFile = createTempFile();

			MediaLocator mediaDest = new MediaLocator( "file:"
					+ tempFile.getAbsolutePath() );

			this.dataSink = Manager.createDataSink( dataSource, mediaDest );
			dataSink.addDataSinkListener( this );
			dataSink.open();
			dataSink.start();
			processor.start();
			isWaiting = true;
			boolean success = waitForFileDone();

			org.lgna.common.resources.AudioResource ar = MediaFactory.getSingleton().createAudioResource( tempFile );
			tempFile.delete();

			return ar;
		} catch( Exception e ) {
			e.printStackTrace();
		}
		return null;
	}

	public static org.lgna.common.resources.AudioResource convert( org.lgna.common.resources.AudioResource resource, javax.media.format.AudioFormat destFormat )
	{
		AudioResourceConverter converter = new AudioResourceConverter( resource );
		return converter.convertTo( destFormat );
	}

	public static org.lgna.common.resources.AudioResource convert( org.lgna.common.resources.AudioResource resource, javax.sound.sampled.AudioFormat destFormat )
	{
		AudioResourceConverter converter = new AudioResourceConverter( resource );
		return converter.convertTo( destFormat );
	}

	public static boolean needsConverting( org.lgna.common.resources.AudioResource resource, javax.sound.sampled.AudioFormat destFormat )
	{
		AudioInputStream audioStream = null;
		ByteArrayInputStream dataStream = new ByteArrayInputStream( resource.getData() );

		try
		{
			audioStream = AudioSystem.getAudioInputStream( dataStream );
		} catch( Exception e )
		{
			e.printStackTrace();
			return false;
		}
		AudioFormat currentFormat = audioStream.getFormat();

		return needsConverting( currentFormat, destFormat );
	}

	public static boolean needsConverting( javax.sound.sampled.AudioFormat currentFormat, javax.sound.sampled.AudioFormat destFormat )
	{
		if( currentFormat.getSampleRate() != destFormat.getSampleRate() )
		{
			return true;
		}
		if( currentFormat.getEncoding() != destFormat.getEncoding() )
		{
			return true;
		}
		return false;
	}

}
