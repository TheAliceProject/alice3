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
package edu.wustl.lookingglass.media;

/**
 * @author Dennis Cosgrove
 */
public class NewSchoolImagesToWebmEncoder {
	private static final String WEBM_EXTENSION = "webm";

	private final int frameRate;
	private final java.awt.Dimension size;
	private final boolean isUpsideDown;

	private java.io.File encodedVideoFile = null;
	private ProcessBuilder processBuilder;
	private Process process;

	private final java.io.ByteArrayOutputStream stdout = new java.io.ByteArrayOutputStream();
	private final java.io.ByteArrayOutputStream stderr = new java.io.ByteArrayOutputStream();

	public NewSchoolImagesToWebmEncoder( int frameRate, java.awt.Dimension size, boolean isUpsideDown ) {
		this.frameRate = frameRate;
		this.size = size;
		this.isUpsideDown = isUpsideDown;

		assert ( this.size.width % 2 ) == 0 : "ffmpeg requires dimensions that are divisble by two " + this.size.width;
		assert ( this.size.height % 2 ) == 0 : "ffmpeg requires dimensions that are divisble by two " + this.size.height;
	}

	public void start() throws Exception {
		javax.imageio.ImageIO.setUseCache( false );

		this.encodedVideoFile = java.io.File.createTempFile( "project", "." + WEBM_EXTENSION );
		this.encodedVideoFile.deleteOnExit();

		java.util.List<String> args = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

		// overwrite output files without asking
		args.add( "-y" );

		// set frame rate
		args.add( "-r" );
		args.add( Integer.toString( this.frameRate ) );

		//
		args.add( "-f" );
		args.add( "image2pipe" );

		// set video codec for input (ppm)
		args.add( "-vcodec" );
		args.add( "ppm" );

		// use stdin
		args.add( "-i" );
		args.add( "-" );

		if( this.isUpsideDown ) {
			// flip vertically
			args.add( "-vf" );
			args.add( "vflip" );
		}

		//set video codec output (webm)
		args.add( "-vcodec" );
		args.add( "libvpx" );

		// libvpx option: quality (best, good, realtime) 
		args.add( "-quality" );
		args.add( "good" );

		// libvpx option: (0-5) lower values use more cpu
		args.add( "-cpu-used" );
		args.add( "0" );

		// libvpx option: variable bit rate 
		args.add( "-b:v" );
		args.add( "500k" );

		// libvpx option: minimum quantizer (0-63)
		args.add( "-qmin" );
		args.add( "10" );

		// libvpx option: maximum quantizer (0-63)
		args.add( "-qmax" );
		args.add( "42" );

		// libvpx option: maxrate
		args.add( "-maxrate" );
		args.add( "500k" );

		// libvpx option: buffer size
		args.add( "-bufsize" );
		args.add( "1000k" );

		// pixel format
		args.add( "-pix_fmt" );
		args.add( "yuv420p" );

		// output file
		args.add( this.encodedVideoFile.getAbsolutePath() );

		this.processBuilder = FFmpegProcessBuilderUtilities.createFFmpegProcessBuilder( args );
		this.process = this.processBuilder.start();
	}

	public java.io.File getEncodedVideoFile() {
		return this.encodedVideoFile;
	}

	private void drainInputStreams() throws Exception {
		java.io.InputStream isOut = this.process.getInputStream();
		java.io.InputStream isErr = this.process.getErrorStream();
		edu.cmu.cs.dennisc.java.io.InputStreamUtilities.drain( isOut, this.stdout );
		edu.cmu.cs.dennisc.java.io.InputStreamUtilities.drain( isErr, this.stderr );
	}

	public void addBufferedImage( java.awt.image.BufferedImage image, boolean isUpsideDown ) throws Exception {
		assert image != null;
		assert image.getWidth() == this.size.getWidth();
		assert image.getHeight() == this.size.getHeight();
		assert isUpsideDown == this.isUpsideDown;

		this.drainInputStreams();
		java.io.OutputStream os = this.process.getOutputStream();
		javax.imageio.ImageIO.write( image, "ppm", os );
	}

	public int stop() throws Exception {
		this.drainInputStreams();
		java.io.OutputStream os = this.process.getOutputStream();
		os.flush();
		os.close();
		this.drainInputStreams();
		int status = this.process.waitFor();
		this.drainInputStreams();
		javax.imageio.ImageIO.setUseCache( true );
		return status;
	}

	public java.io.ByteArrayOutputStream getStdout() {
		return this.stdout;
	}

	public java.io.ByteArrayOutputStream getStderr() {
		return this.stderr;
	}
}
