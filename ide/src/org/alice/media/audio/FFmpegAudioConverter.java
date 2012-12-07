/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.media.audio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.sound.sampled.AudioFormat;

import org.lgna.common.resources.AudioResource;

import edu.cmu.cs.dennisc.java.lang.RuntimeUtilities;

/**
 * @author Matt May
 */
public class FFmpegAudioConverter {

	private static float RATE_22 = 22050f;
	private static float RATE_44 = 44100f;
	private static AudioFormat QUICKTIME_AUDIO_FORMAT_PCM = new AudioFormat( AudioFormat.Encoding.PCM_SIGNED, RATE_44, 16, 1, 2, RATE_44, false );
	public static AudioFormat desiredFormat = QUICKTIME_AUDIO_FORMAT_PCM;
	private static String ffmpegCommand;
	private static AudioTrackMixer mixer = new AudioTrackMixer( QUICKTIME_AUDIO_FORMAT_PCM, 0 );

	static {
		// Find the ffmpeg process
		//<alice>
		//String nativePath = edu.wustl.cse.lookingglass.utilities.NativeLibLoader.getOsPath( "ffmpeg" );
		String nativePath = getFfmpegPath();
		//</alice>
		if( nativePath == null ) {
			// Hope it's on the system path
			ffmpegCommand = "ffmpeg";
		} else {
			String ext = "";
			if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
				ext = ".exe";
			}

			nativePath = nativePath + "/bin/ffmpeg" + ext;
			if( !( new java.io.File( nativePath ) ).exists() ) {
				ffmpegCommand = "ffmpeg";
			} else {
				ffmpegCommand = nativePath;
			}
		}
	}

	//<alice>
	private static String getFfmpegPath() {
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isLinux() ) {
			return null;
		} else {
			String installPath = System.getProperty( "org.alice.ide.IDE.install.dir" );
			java.io.File installDir = new java.io.File( installPath );
			java.io.File ffmpegFile = new java.io.File( installDir.getParent(), "lib/ffmpeg" );
			StringBuilder sb = new StringBuilder();
			sb.append( ffmpegFile.getAbsolutePath() );
			if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
				sb.append( "/windows" );
			} else if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isMac() ) {
				sb.append( "/macosx" );
			} else {
				throw new RuntimeException();
			}
			return sb.toString();
		}
	}

	public static File convertAudioIfNecessary( AudioResource resource ) {
		try {
			File file = createAudioFileToConvert( resource );
			File outputFile;
			outputFile = File.createTempFile( "outputFile", ".wav" );
			String path = outputFile.getAbsolutePath();
			outputFile.delete();
			RuntimeUtilities.execSilent( ffmpegCommand, "-i", file.getAbsolutePath(), "-acodec", "pcm_s16le",
					"-ar", String.valueOf( RATE_44 ), "-ac", "1", path );
			File convertedFile = new File( path );
			return convertedFile;
		} catch( IOException e ) {
			e.printStackTrace();
		}
		return null;
	}

	private static File createAudioFileToConvert( AudioResource resource ) {
		resource.getData();
		try {
			File createTempFile = File.createTempFile( "sound", ".wav" );
			@SuppressWarnings( "resource" )
			OutputStream oStream = new FileOutputStream( createTempFile );
			oStream.write( resource.getData() );
			return createTempFile;
		} catch( FileNotFoundException e ) {
			e.printStackTrace();
		} catch( IOException e ) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main( String[] args ) {
		try {
			File outputFile = File.createTempFile( "outputFile", ".wav" );
			String path = outputFile.getAbsolutePath();
			outputFile.delete();
			RuntimeUtilities.exec( ffmpegCommand, "-i", "C:/Users/Matt/Downloads/blorp.wav", "-acodec", "pcm_s16le",
					"-ar", String.valueOf( RATE_44 ), "-ac", "1", path );

		} catch( IOException e ) {
			e.printStackTrace();
		}
	}

}
