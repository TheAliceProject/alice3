/**
 * Copyright (c) 2008-2015, Washington University in St. Louis. All rights reserved.
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
 * 3. Products derived from the software may not be called "Looking Glass", nor
 *    may "Looking Glass" appear in their name, without prior written permission
 *    of Washington University in St. Louis.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software
 *    developed by Washington University in St. Louis"
 *
 * 5. The gallery of art assets and animations provided with this software is
 *    contributed by Electronic Arts Inc. and may be used for personal,
 *    non-commercial, and academic use only. Redistributions of any program
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in
 *    The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  ANY AND ALL
 * EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A PARTICULAR PURPOSE,
 * TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHORS,
 * COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.wustl.lookingglass.media;

import edu.cmu.cs.dennisc.java.util.logging.Logger;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

/**
 * @author Kyle J. Harms
 */
public class FFmpegImageExtractor {

	public static void getFrameAt( final String videoPath, final float frameTimeSeconds, final File snapshotFile ) {
		// https://trac.ffmpeg.org/wiki/Seeking%20with%20FFmpeg
		final Float GOP_SEEK = 10.0f * 2.0f; // GOP is typically 10 seconds, so let's double it.
		Float fastSeek = frameTimeSeconds - GOP_SEEK;
		Float accurateSeek = GOP_SEEK;
		if( fastSeek < 0.0f ) {
			fastSeek = 0.0f;
			accurateSeek = frameTimeSeconds;
		}
		// Sometimes VLCJ returns negative values for position due to being stopped. Just correct for it.
		if( accurateSeek < 0.0f ) {
			accurateSeek = 0.0f;
		}

		FFmpegProcess ffmpegProcess = new FFmpegProcess( "-y", "-ss", fastSeek.toString(), "-i", videoPath, "-ss", accurateSeek.toString(), "-f", "image2", "-vframes", "1", snapshotFile.getAbsolutePath() );
		ffmpegProcess.start();
		int status = ffmpegProcess.stop();
		if( status != 0 ) {
			Logger.severe( "encoding failed; status != 0", status );
			throw new FFmpegProcessException( ffmpegProcess.getProcessInput(), ffmpegProcess.getProcessError() );
		}
	}

	public static Image getFrameAt( final String mrl, final float seconds ) {
		Image snapshot = null;
		try {
			File snapshotFile = File.createTempFile( "snapshot", ".png" );
			snapshotFile.deleteOnExit();
			getFrameAt( mrl, seconds, snapshotFile );
			snapshot = ImageIO.read( snapshotFile );
			snapshotFile.delete();
		} catch( IOException e ) {
			Logger.severe( e );
		}
		return snapshot;
	}
}
