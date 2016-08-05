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
package edu.cmu.cs.dennisc.worker;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractWorker<T, V> {
	protected class InternalSwingWorker extends javax.swing.SwingWorker<T, V> {
		@Override
		protected final T doInBackground() throws Exception {
			return AbstractWorker.this.do_onBackgroundThread();
		}

		@Override
		protected final void done() {
			super.done();
			if( this.isCancelled() ) {
				//pass
			} else {
				T value;
				try {
					value = this.get();
				} catch( InterruptedException ie ) {
					throw new RuntimeException( ie );
				} catch( java.util.concurrent.ExecutionException ee ) {
					throw new RuntimeException( ee );
				}
				AbstractWorker.this.handleDone_onEventDispatchThread( value );
			}
		}
	}

	protected abstract javax.swing.SwingWorker<T, V> getSwingWorker();

	public final void addPropertyChangeListener( java.beans.PropertyChangeListener listener ) {
		this.getSwingWorker().addPropertyChangeListener( listener );
	}

	public final void removePropertyChangeListener( java.beans.PropertyChangeListener listener ) {
		this.getSwingWorker().removePropertyChangeListener( listener );
	}

	protected abstract T do_onBackgroundThread() throws Exception;

	protected abstract void handleDone_onEventDispatchThread( T value );

	public final T get_obviouslyLockingCurrentThreadUntilDone() throws InterruptedException, java.util.concurrent.ExecutionException {
		return this.getSwingWorker().get();
	}

	public final T get_obviouslyLockingCurrentThreadUntilDoneOrTimedOut( int timeout, java.util.concurrent.TimeUnit timeUnit ) throws InterruptedException, java.util.concurrent.ExecutionException, java.util.concurrent.TimeoutException {
		return this.getSwingWorker().get( timeout, timeUnit );
	}

	public final javax.swing.SwingWorker.StateValue getState() {
		return this.getSwingWorker().getState();
	}

	public final boolean isDone() {
		return this.getSwingWorker().isDone();
	}

	public final boolean isCancelled() {
		return this.getSwingWorker().isCancelled();
	}

	public final boolean cancel( boolean mayInterruptIfRunning ) {
		return this.getSwingWorker().cancel( mayInterruptIfRunning );
	}

	public final void execute() {
		this.getSwingWorker().execute();
	}
}
