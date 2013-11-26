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
package edu.cmu.cs.dennisc.java.lang;

/**
 * @author Dennis Cosgrove
 */
public class ProcessUtilities {
	private static int startAndWaitFor( ProcessBuilder processBuilder, DrainInputStreamThread.LineAppender outLineAppender, DrainInputStreamThread.LineAppender errLineAppender ) throws java.io.IOException, InterruptedException {
		Process process = processBuilder.start();
		java.util.concurrent.CyclicBarrier barrier = new java.util.concurrent.CyclicBarrier( 3 );
		DrainInputStreamThread outputHandler = new DrainInputStreamThread( process.getInputStream(), outLineAppender, barrier );
		outputHandler.start();
		DrainInputStreamThread errorHandler = new DrainInputStreamThread( process.getErrorStream(), errLineAppender, barrier );
		errorHandler.start();
		int rv = process.waitFor();
		try {
			barrier.await();
		} catch( java.util.concurrent.BrokenBarrierException bbe ) {
			throw new RuntimeException( bbe );
		}
		return rv;
	}

	public static int startAndWaitFor( ProcessBuilder processBuilder, java.io.PrintStream out, java.io.PrintStream err ) throws java.io.IOException, InterruptedException {
		return startAndWaitFor( processBuilder, out != null ? new DrainInputStreamThread.PrintStreamLineAppender( out ) : null, err != null ? new DrainInputStreamThread.PrintStreamLineAppender( err ) : null );
	}

	public static int startAndWaitFor( ProcessBuilder processBuilder, java.util.List<String> out, java.util.List<String> err ) throws java.io.IOException, InterruptedException {
		return startAndWaitFor( processBuilder, out != null ? new DrainInputStreamThread.StringListLineAppender( out ) : null, err != null ? new DrainInputStreamThread.StringListLineAppender( err ) : null );
	}
}
