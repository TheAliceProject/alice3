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

package org.alice.flite;

import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

import org.lgna.common.resources.AudioResource;

public class TextToSpeech
{

	public final static String MALE_VOICE = "MALE";
	public final static String FEMALE_VOICE = "FEMALE";

	private String type;
	private int numSamples;
	private int numChannels;
	private int sampleRate;
	private short[] samples;

	native void initWithTextToSpeech( String text, String voice );

	static
	{
		try
		{
			edu.cmu.cs.dennisc.java.lang.SystemUtilities.loadLibrary( "", "jni_flite", edu.cmu.cs.dennisc.java.lang.LoadLibraryReportStyle.EXCEPTION );
		} catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	public TextToSpeech()
	{
		this.type = null;
		this.numSamples = 0;
		this.numChannels = 0;
		this.sampleRate = 0;
		this.samples = null;
	}

	public void processText( String text, String voice )
	{
		this.initWithTextToSpeech( text, voice );
	}

	public double getDuration()
	{
		return ( this.numSamples / (double)this.sampleRate );
	}

	public byte[] saveToByteArray()
	{
		if( this.samples != null )
		{
			ShortArrayInputStream shortStream = new ShortArrayInputStream( this.samples );
			AudioFormat wavFormat = new AudioFormat( this.sampleRate, 16, this.numChannels, true, true );
			AudioInputStream audioStream = new AudioInputStream( shortStream, wavFormat, this.numSamples );
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try
			{
				AudioSystem.write( audioStream, AudioFileFormat.Type.WAVE, out );
			} catch( Exception e )
			{
				return null;
			}
			return out.toByteArray();
		}
		return null;
	}

	static void doTextToSpeech( String toSaveTo, String text, String voice )
	{
		try
		{
			//System.out.println("Doing text to speech on: \""+text+"\"");
			long startTime = System.currentTimeMillis();
			TextToSpeech tts = new TextToSpeech();
			tts.initWithTextToSpeech( text, voice );
			long initTime = System.currentTimeMillis();
			ShortArrayInputStream shortStream = new ShortArrayInputStream( tts.samples );
			AudioFormat wavFormat = new AudioFormat( tts.sampleRate, 16, tts.numChannels, true, true );
			AudioInputStream audioStream = new AudioInputStream( shortStream, wavFormat, tts.numSamples );

			File audioFile = new File( "C:/" + toSaveTo + ".wav" );

			long dumpToFileTime = System.currentTimeMillis();
			AudioResource ttsAudioResource = new AudioResource( audioFile );
			long doneLoadingTime = System.currentTimeMillis();

			//			org.lgna.story.AudioSource ttsAudioSource = new org.lgna.story.AudioSource(ttsAudioResource);

			//			edu.cmu.cs.dennisc.media.MediaFactory mediaFactory = edu.cmu.cs.dennisc.media.jmf.MediaFactory.getSingleton();
			//			edu.cmu.cs.dennisc.media.Player player = mediaFactory.createPlayer( ttsAudioSource.getAudioResource(), ttsAudioSource.getVolume(), ttsAudioSource.getStartTime(), ttsAudioSource.getStopTime() );
			long endTime = System.currentTimeMillis();
			int wordCount = text.split( " " ).length;
			System.out.print( text + " | " );
			System.out.print( audioFile.getAbsolutePath() + " | " );
			System.out.print( wordCount + " | " );
			System.out.print( ( initTime - startTime ) + " | " );
			System.out.print( ( dumpToFileTime - initTime ) + " | " );
			System.out.print( ( doneLoadingTime - dumpToFileTime ) + " | " );
			System.out.print( ( endTime - startTime ) );
			System.out.println();
			//player.playUntilStop();
		} catch( Exception e )
		{
			e.printStackTrace();
		}
	}

}
