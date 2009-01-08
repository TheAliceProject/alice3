/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package edu.cmu.cs.dennisc.task;

/**
 * @author Dennis Cosgrove
 */
public abstract class BlockingTaskObserver<E> implements TaskObserver< E >, Runnable  {
	private java.util.concurrent.CyclicBarrier barrier;
	private E result;
	private void await() {
		//todo?
		try {
			barrier.await();
		} catch( InterruptedException ie ) {
			throw new RuntimeException( ie );
		} catch( java.util.concurrent.BrokenBarrierException bbe ) {
			throw new RuntimeException( bbe );
		}
	}
	
	public BlockingTaskObserver() {
		this.barrier = new java.util.concurrent.CyclicBarrier( 2 );
	}
	
	public abstract void run();
	
	public E getResult() {
		RuntimeException runtimeException;
		try {
			this.run();
			runtimeException = null;
		} catch( RuntimeException re ) {
			runtimeException = re;
		}
		if( runtimeException != null ) {
			throw runtimeException;
		} else {
			this.await();
			return this.result;
		}
	}

	public void handleCompletion( E result ) {
		this.result = result;
		this.await();
	}
	public void handleCancelation() {
		this.result = null;
		this.await();
	}
}
