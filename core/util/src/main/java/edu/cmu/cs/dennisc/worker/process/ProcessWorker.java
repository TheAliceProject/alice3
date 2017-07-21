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
package edu.cmu.cs.dennisc.worker.process;

/**
 * @author Dennis Cosgrove
 */
public abstract class ProcessWorker extends edu.cmu.cs.dennisc.worker.WorkerWithProgress<Integer, String> {
	private static final String FIRST_CHUNK = "__PROCESS_WORKER_FIRST_CHUNK__acf167f6-5b8c-4ce1-a221-8eef7be26582";
	private static final String FIRST_CHUNK_FOR_PROCESS = "__PROCESS_WORKER_FIRST_CHUNK_FOR_PROCESS__";
	private final ProcessBuilder[] processBuilders;

	public ProcessWorker( ProcessBuilder... processBuilders ) {
		this.processBuilders = processBuilders;
	}

	public ProcessBuilder[] getProcessBuilders() {
		return this.processBuilders;
	}

	@Override
	protected Integer do_onBackgroundThread() throws Exception {
		int rv = 0; //?
		this.publish( FIRST_CHUNK );
		int i = 0;
		for( ProcessBuilder processBuilder : this.processBuilders ) {
			if( processBuilder.redirectErrorStream() ) {
				//pass
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( "NOTE: redirecting error stream", processBuilder );
				processBuilder.redirectErrorStream( true );
			}
			Process process = processBuilder.start();
			this.publish( FIRST_CHUNK_FOR_PROCESS + i );
			java.io.InputStream standardOutAndStandardError = process.getInputStream();
			byte[] buffer = new byte[ 256 ];
			while( true ) {
				if( this.isCancelled() ) {
					process.destroy();
				} else {
					int count = standardOutAndStandardError.read( buffer, 0, buffer.length );
					if( count != -1 ) {
						this.publish( new String( buffer, 0, count ) );
					} else {
						break;
					}
				}
			}
			rv = process.exitValue();
			i++;
		}
		return rv;
	}

	protected abstract void handleStart_onEventDispatchThread();

	protected abstract void handleStartProcess_onEventDispatchThread( int i );

	protected abstract void handleProcessStandardOutAndStandardError_onEventDispatchThread( String s );

	@Override
	protected final void handleProcess_onEventDispatchThread( java.util.List<String> chunks ) {
		StringBuilder sb = new StringBuilder();
		for( String chunk : chunks ) {
			if( chunk != null ) {
				if( FIRST_CHUNK.equals( chunk ) ) {
					this.handleStart_onEventDispatchThread();
				} else if( chunk.startsWith( FIRST_CHUNK_FOR_PROCESS ) ) {
					//todo
					String nText = chunk.substring( FIRST_CHUNK_FOR_PROCESS.length() );
					this.handleStartProcess_onEventDispatchThread( Integer.parseInt( nText ) );
				} else {
					sb.append( chunk );
				}
			}
		}
		this.handleProcessStandardOutAndStandardError_onEventDispatchThread( sb.toString() );
	}
}
