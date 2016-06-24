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

/**
 * @author Kyle J. Harms
 */
public class FFmpegProcess {
	public static boolean isArchitectureSpecificCommandAbsolute() {
		return edu.cmu.cs.dennisc.java.lang.SystemUtilities.isLinux() == false;
	}

	public static String getArchitectureSpecificCommand() {
		final String FFMPEG_COMMAND = "ffmpeg";
		if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isLinux() ) {
			return FFMPEG_COMMAND;
		} else {
			java.io.File archDirectory = edu.cmu.cs.dennisc.app.ApplicationRoot.getArchitectureSpecificDirectory();
			StringBuilder sb = new StringBuilder();
			sb.append( "ffmpeg/" );
			sb.append( FFMPEG_COMMAND );
			if( edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
				sb.append( ".exe" );
			}
			java.io.File commandFile = new java.io.File( archDirectory, sb.toString() );
			if( commandFile.exists() ) {
				return commandFile.getAbsolutePath();
			} else {
				//todo: find on path
				throw new RuntimeException( commandFile.getAbsolutePath() );
			}
		}
	}

	private final String[] commandArgs;
	private final ProcessBuilder processBuilder;

	private Process process;
	private java.io.OutputStream outputStream;
	private java.io.BufferedReader errorStream;
	private java.io.BufferedReader inputStream;
	private StringBuilder processInput;
	private StringBuilder processError;

	public FFmpegProcess( String... args ) {
		this.commandArgs = edu.cmu.cs.dennisc.java.lang.ArrayUtilities.concat( String.class, getArchitectureSpecificCommand(), args );
		this.processBuilder = new ProcessBuilder( this.commandArgs );
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
			final boolean IS_LOCKING_A_PROBLEM_ON_WINDOWS = true;
			if( IS_LOCKING_A_PROBLEM_ON_WINDOWS && edu.cmu.cs.dennisc.java.lang.SystemUtilities.isWindows() ) {
				this.process.getInputStream().close();
				this.process.getErrorStream().close();

				this.errorStream = null;
				this.inputStream = null;
			}
		} catch( Exception e ) {
			this.process = null;
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "failed to create ffmpeg process for encoding", this.commandArgs );
			handleProcessError( e );
		}
		return this.process;
	}

	//	public Process getProcess() {
	//		return this.process;
	//	}

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
		readStream( this.inputStream, this.processInput );
		return ( this.processInput == null ) ? null : this.processInput.toString();
	}

	public String getProcessError() {
		readStream( this.errorStream, this.processError );
		return ( this.processError == null ) ? null : this.processError.toString();
	}

	private void handleProcessError( Exception e ) {
		throw new FFmpegProcessException( e, getProcessInput(), getProcessError() );
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
