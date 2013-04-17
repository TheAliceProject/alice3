/**
 * Copyright (c) 2008-2013, Washington University in St. Louis. All rights reserved.
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
package edu.wustl.cse.lookingglass.media;

/**
 * @author Kyle J. Harms
 */
public class FFmpegProcess {

	private static final String FFMPEG_COMMAND = "ffmpeg";

	private ProcessBuilder processBuilder;
	private Process process;
	private java.io.OutputStream outputStream;
	private java.io.BufferedReader errorStream;
	private java.io.BufferedReader inputStream;
	private StringBuilder processInput;
	private StringBuilder processError;

	public FFmpegProcess( String... args ) {
		String[] commandArgs = new String[ args.length + 1 ];
		commandArgs[ 0 ] = getFFmpegCommand();
		for( int i = 1; i < commandArgs.length; i++ ) {
			commandArgs[ i ] = args[ i - 1 ];
		}
		this.processBuilder = new ProcessBuilder( commandArgs );
	}

	// <alice/>
	private static String getFFmpegPath() {
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

	public static String getFFmpegCommand() {
		// Find the ffmpeg process
		//<alice>
		//String nativePath = edu.wustl.cse.lookingglass.utilities.NativeLibLoader.getOsPath( "ffmpeg" );
		String nativePath = getFFmpegPath();
		//</alice>
		if( nativePath == null ) {
			// Hope it's on the system path
			// TODO: give a warning to these users that they need to have ffmpeg installed.
			return FFMPEG_COMMAND;
		} else {
			String ext = "";
			if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
				ext = ".exe";
			}
			nativePath = nativePath + "/bin/ffmpeg" + ext;
			if( !( new java.io.File( nativePath ) ).exists() ) {
				return FFMPEG_COMMAND;
			} else {
				return nativePath;
			}
		}
	}

	public synchronized Process start() throws FFmpegProcessException {
		try {
			this.process = this.processBuilder.start();

			this.outputStream = new java.io.BufferedOutputStream( this.process.getOutputStream() );
			this.outputStream.flush();
			this.errorStream = new java.io.BufferedReader( new java.io.InputStreamReader( this.process.getErrorStream() ) );
			this.inputStream = new java.io.BufferedReader( new java.io.InputStreamReader( this.process.getInputStream() ) );

			this.processInput = new StringBuilder();
			this.processError = new StringBuilder();

			// Windows requires that we close all other streams, otherwise the output stream for ffmpeg will lock.
			if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
				this.process.getInputStream().close();
				this.process.getErrorStream().close();

				this.errorStream = null;
				this.inputStream = null;
			}
		} catch( Exception e ) {
			this.process = null;
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "failed to create ffmpeg process for encoding", FFmpegProcess.getFFmpegCommand() );
			handleProcessError( e );
		}
		return this.process;
	}

	public synchronized int stop() throws FFmpegProcessException {
		try {
			synchronized( this.outputStream ) {
				this.outputStream.close();
			}
		} catch( Exception e ) {
			handleProcessError( e );
		}

		int status = -1;
		try {
			status = this.process.waitFor();
		} catch( InterruptedException e ) {
			handleProcessError( e );
		}
		return status;
	}

	public java.io.OutputStream getProcessOutputStream() {
		return this.outputStream;
	}

	public String getProcessInput() {
		return this.processInput.toString();
	}

	public String getProcessError() {
		return this.processError.toString();
	}

	private void handleProcessError( Exception e ) {
		readStream( this.inputStream, this.processInput );
		readStream( this.errorStream, this.processError );
		throw new FFmpegProcessException( e, ( this.processInput == null ) ? null : this.processInput.toString(), ( this.processError == null ) ? null : this.processError.toString() );
	}

	private void readStream( java.io.BufferedReader reader, StringBuilder string ) {
		if( ( reader == null ) || ( string == null ) ) {
			return;
		}
		try {
			while( reader.ready() ) {
				string.append( reader.readLine() );
				string.append( "\n" );
			}
		} catch( java.io.IOException e ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "unable to read ffmpeg error", e );
		}
	}
}
