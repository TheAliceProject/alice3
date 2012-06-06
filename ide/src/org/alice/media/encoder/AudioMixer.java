/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */
package org.alice.media.encoder;

//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.Map;
//import java.util.Vector;
//
//import javax.media.CaptureDeviceInfo;
//import javax.media.CaptureDeviceManager;
//import javax.media.DataSink;
//import javax.media.Manager;
//import javax.media.MediaLocator;
//import javax.media.NoDataSinkException;
//import javax.media.NoProcessorException;
//import javax.media.Processor;
//import javax.media.format.AudioFormat;
//import javax.media.protocol.DataSource;
//
//import org.alice.media.audio.AudioResourceConverter;
//import org.alice.media.audio.ScheduledAudioStream;

/**
 * @author Matt May
 */
public class AudioMixer {
//
//	private static final AudioFormat targetFormat = new AudioFormat( "linear", 44100, 16, 2 );
//	private Vector<ScheduledAudioStream> scheduledStreams;
//	private FileOutputStream out;
//	private Map<org.lgna.common.resources.AudioResource,org.lgna.common.resources.AudioResource> convertedResourceMap;
//
//	public AudioMixer( int i ) {
//		CaptureDeviceInfo di = null;
//		Vector deviceList = CaptureDeviceManager.getDeviceList( targetFormat );
//		if( deviceList.size() > 0 )
//			di = (CaptureDeviceInfo)deviceList.firstElement();
//		Processor p;
//		try {
//			p = Manager.createProcessor( di.getLocator() );
//			DataSource source = p.getDataOutput();
//			//	The source object returned from the Processor can then be turned into a Player object by calling Manager.createPlayer().
//			//	To capture it to an audio file instead, a DataSink can take the data instead:
//			DataSink sink;
//			MediaLocator dest = new MediaLocator( "file://output.wav" );
//			sink = Manager.createDataSink( source, dest );
//			sink.open();
//			sink.start();
//		} catch( SecurityException e ) {
//			e.printStackTrace();
//		} catch( IOException e ) {
//			e.printStackTrace();
//		} catch( NoDataSinkException e ) {
//			e.printStackTrace();
//		} catch( NoProcessorException e ) {
//			e.printStackTrace();
//		}
//	}
//
//	public AudioMixer( double movieLength, Vector<ScheduledAudioStream> audioStreams, File tempFile ) throws FileNotFoundException {
//		out = new FileOutputStream( tempFile );
//		this.scheduledStreams = audioStreams;
//	}
//
//	public void addAudioResource( org.lgna.common.resources.AudioResource resource, double startTime, double entryPoint, double endPoint, double volume ) {
//		org.lgna.common.resources.AudioResource convertedResource = convertResourceIfNecessary( resource );
//		ScheduledAudioStream scheduledStream = new ScheduledAudioStream( convertedResource, startTime, entryPoint, endPoint, volume );
//		addScheduledStream( scheduledStream );
//	}
//
//	public void addScheduledStream( ScheduledAudioStream scheduledStream ) {
//		org.lgna.common.resources.AudioResource convertedResource = convertResourceIfNecessary( scheduledStream.getAudioResource() );
//		if( convertedResource != scheduledStream.getAudioResource() ) {
//			scheduledStream.setAudioResource( convertedResource );
//		}
//		int index = scheduledStreams.size();
//		for( int i = 0; i < scheduledStreams.size(); i++ ) {
//			if( scheduledStreams.get( i ).compareTo( scheduledStream ) > 0 ) {
//				index = i;
//				break;
//			}
//		}
//		scheduledStreams.add( index, scheduledStream );
//	}
//
//	private org.lgna.common.resources.AudioResource convertResourceIfNecessary( org.lgna.common.resources.AudioResource resource ) {
//		org.lgna.common.resources.AudioResource convertedResource = null;
//		if( this.convertedResourceMap.containsKey( resource ) ) {
//			convertedResource = this.convertedResourceMap.get( resource );
//		} else {
//			if( AudioResourceConverter.needsConverting( resource, this.targetFormat ) ) {
//				convertedResource = AudioResourceConverter.convert( resource, this.targetFormat );
//			} else {
//				convertedResource = resource;
//			}
//			this.convertedResourceMap.put( resource, convertedResource );
//		}
//		return convertedResource;
//	}
}
