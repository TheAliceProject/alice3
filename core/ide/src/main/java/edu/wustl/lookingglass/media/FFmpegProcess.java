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

import edu.cmu.cs.dennisc.app.ApplicationRoot;
import edu.cmu.cs.dennisc.java.lang.ArrayUtilities;
import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.java.util.logging.Logger;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * @author Kyle J. Harms
 */
public class FFmpegProcess {
	public static boolean isArchitectureSpecificCommandAbsolute() {
		return SystemUtilities.isLinux() == false;
	}

	public static String getArchitectureSpecificCommand() {
		final String FFMPEG_COMMAND = "ffmpeg";
		if( SystemUtilities.isLinux() ) {
			return FFMPEG_COMMAND;
		} else {
			File archDirectory = ApplicationRoot.getArchitectureSpecificDirectory();
			StringBuilder sb = new StringBuilder();
			sb.append( "ffmpeg/" );
			sb.append( FFMPEG_COMMAND );
			if( SystemUtilities.isWindows() ) {
				sb.append( ".exe" );
			}
			File commandFile = new File( archDirectory, sb.toString() );
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
	private OutputStream outputStream;
	private BufferedReader errorStream;
	private BufferedReader inputStream;
	private StringBuilder processInput;
	private StringBuilder processError;

	public FFmpegProcess( String... args ) {
		this.commandArgs = ArrayUtilities.concat( String.class, getArchitectureSpecificCommand(), args );
		this.processBuilder = new ProcessBuilder( this.commandArgs );
	}

	public synchronized Process start() throws FFmpegProcessException {
		try {
			this.process = this.processBuilder.start();

			this.outputStream = new BufferedOutputStream( this.process.getOutputStream() );
			this.outputStream.flush();
			this.errorStream = new BufferedReader( new InputStreamReader( this.process.getErrorStream() ) );
			this.inputStream = new BufferedReader( new InputStreamReader( this.process.getInputStream() ) );

			this.processInput = new StringBuilder();
			this.processError = new StringBuilder();

			// Windows requires that we close all other streams, otherwise the output stream for ffmpeg will lock.
			final boolean IS_LOCKING_A_PROBLEM_ON_WINDOWS = true;
			if( IS_LOCKING_A_PROBLEM_ON_WINDOWS && SystemUtilities.isWindows() ) {
				this.process.getInputStream().close();
				this.process.getErrorStream().close();

				this.errorStream = null;
				this.inputStream = null;
			}
		} catch( Exception e ) {
			this.process = null;
			Logger.severe( "failed to create ffmpeg process for encoding", this.commandArgs );
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

	public OutputStream getProcessOutputStream() {
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

	private void readStream( BufferedReader reader, StringBuilder string ) {
		if( ( reader == null ) || ( string == null ) ) {
			return;
		}
		try {
			while( reader.ready() ) {
				string.append( reader.readLine() );
				string.append( "\n" );
			}
		} catch( IOException e ) {
			Logger.severe( "unable to read ffmpeg error", e );
		}
	}
}
