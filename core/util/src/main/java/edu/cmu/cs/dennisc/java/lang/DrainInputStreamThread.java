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
package edu.cmu.cs.dennisc.java.lang;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/class DrainInputStreamThread extends Thread {
	static interface LineAppender {
		void appendLine( String line );
	}

	static class PrintStreamLineAppender implements LineAppender {
		private final java.io.PrintWriter pw;

		public PrintStreamLineAppender( java.io.PrintStream ps ) {
			this.pw = ps != null ? new java.io.PrintWriter( ps ) : null;
		}

		@Override
		public void appendLine( String line ) {
			if( this.pw != null ) {
				this.pw.append( line );
				this.pw.append( '\n' );
				this.pw.flush();
			}
		}
	}

	static class StringListLineAppender implements LineAppender {
		private final java.util.List<String> list;

		public StringListLineAppender( java.util.List<String> list ) {
			this.list = list;
		}

		@Override
		public void appendLine( String line ) {
			this.list.add( line );
		}
	}

	private final java.io.InputStream is;
	private final LineAppender lineAppender;
	private final java.util.concurrent.CyclicBarrier barrier;

	public DrainInputStreamThread( java.io.InputStream is, LineAppender lineAppender, java.util.concurrent.CyclicBarrier barrier ) {
		this.is = is;
		this.lineAppender = lineAppender;
		this.barrier = barrier;
	}

	@Override
	public void run() {
		java.io.InputStreamReader isr = new java.io.InputStreamReader( this.is );
		java.io.BufferedReader br = new java.io.BufferedReader( isr );
		//java.io.PrintWriter pw = this.ps != null ? new java.io.PrintWriter( this.ps ) : null;
		while( true ) {
			try {
				String line = br.readLine();
				if( line != null ) {
					if( this.lineAppender != null ) {
						this.lineAppender.appendLine( line );
					}
				} else {
					break;
				}
			} catch( java.io.IOException ioe ) {
				throw new RuntimeException( ioe );
			}
		}
		if( barrier != null ) {
			try {
				barrier.await();
			} catch( java.util.concurrent.BrokenBarrierException bbe ) {
				throw new RuntimeException( bbe );
			} catch( InterruptedException ie ) {
				throw new RuntimeException( ie );
			}
		}
	}
}
