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
package org.alice.apis.moveandturn;

/**
 * @author Dennis Cosgrove
 */
public class DoTogether {
	//todo 
	public static void invokeAndWait( 
			@edu.cmu.cs.dennisc.lang.ParameterAnnotation( isVariable=true )
			Runnable... runnables 
	) {
		switch( runnables.length ) {
		case 0:
			break;
		case 1:
			runnables[ 0 ].run();
			break;
		default:
			final java.util.List< RuntimeException > runtimeExceptions = new java.util.LinkedList< RuntimeException >();
			final java.util.concurrent.CyclicBarrier barrier = new java.util.concurrent.CyclicBarrier( runnables.length + 1 );
	    	for( final Runnable runnable : runnables ) {
	            new edu.cmu.cs.dennisc.lang.ThreadWithRevealingToString() {
	            	@Override
	                public void run() {
	            		try {
		            		runnable.run();
	            		} catch( RuntimeException re ) {
	            			synchronized( runtimeExceptions ) {
		            			runtimeExceptions.add( re );
							}
	            		} finally {
	            			try {
            					barrier.await();
	            			} catch( InterruptedException ie ) {
	            				throw new RuntimeException( ie );
	            			} catch( java.util.concurrent.BrokenBarrierException bbe ) {
	            				throw new RuntimeException( bbe );
	            			}
	            		}
	    	        }
	            	@Override
	            	protected StringBuffer updateRepr(StringBuffer rv) {
	            		rv = super.updateRepr(rv);
	            		rv.append( ";runnable=" );
	            		rv.append( runnable );
	            		return rv;
	            	}
	            	
	    	    }.start();
	    	}
			try {
    			barrier.await();
			} catch( InterruptedException ie ) {
				throw new RuntimeException( ie );
			} catch( java.util.concurrent.BrokenBarrierException bbe ) {
				throw new RuntimeException( bbe );
			}
			synchronized( runtimeExceptions ) {
		        if( runtimeExceptions.isEmpty() ) {
		        	//pass
		        } else {
		        	//todo:
		        	throw runtimeExceptions.get( 0 );
		        }
			}
		}
	}
}
