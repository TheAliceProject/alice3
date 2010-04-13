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

package edu.cmu.cs.dennisc.java.lang;

/**
 * @author Dennis Cosgrove
 */
class StreamHandler extends Thread {
	private java.io.InputStream m_is;
	private java.io.PrintStream m_ps;

	public StreamHandler( java.io.InputStream is, java.io.PrintStream ps ) {
		m_is = is;
		m_ps = ps;
	}
	@Override
	public void run() {
		java.io.InputStreamReader isr = new java.io.InputStreamReader( m_is );
		java.io.BufferedReader br = new java.io.BufferedReader( isr );
		java.io.PrintWriter pw = new java.io.PrintWriter( m_ps );
		while( true ) {
			try {
				String line = br.readLine();
				if( line != null ) {
					pw.println( line );
					pw.flush();
				} else {
					break;
				}
			} catch( java.io.IOException ioe ) {
				throw new RuntimeException( ioe );
			}
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
public class RuntimeUtilities {
	public static int exec( java.io.File workingDirectory, java.util.Map< String, String > environment, String... cmds ) {
		ProcessBuilder processBuilder = new ProcessBuilder( cmds );
		if( workingDirectory != null ) {
			processBuilder.directory( workingDirectory );
		}
		if( environment != null ) {
			java.util.Map< String, String > map = processBuilder.environment();
			for( String key : environment.keySet() ) {
				map.put( key, environment.get( key ) );
			}
		}
		try {
			Process process = processBuilder.start();
			StreamHandler outputHandler = new StreamHandler( process.getInputStream(), System.out );
			StreamHandler errorHandler = new StreamHandler( process.getErrorStream(), System.err );
			outputHandler.start();
			errorHandler.start();
			return process.waitFor();
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		} catch( InterruptedException ie ) {
			throw new RuntimeException( ie );
		}
	}
	public static int exec( java.io.File workingDirectory, String... cmds ) {
		return exec( workingDirectory, null, cmds );
	}
	public static int exec( String... cmds ) {
		return exec( null, cmds );
	}
}
