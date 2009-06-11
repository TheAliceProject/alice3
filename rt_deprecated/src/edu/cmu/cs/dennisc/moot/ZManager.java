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
package edu.cmu.cs.dennisc.moot;

/**
 * @author Dennis Cosgrove
 */
public class ZManager {
	static {
	}
//	public static void setHandler( Handler handler ) {
//	}
	public static void handleTabSelection( ZTabbedPane tabbedPane, int index ) {
	}
//	private static void addToHistory( Operation operation ) {
//	}
	private static void handlePreparedOperation( CancellableOperation operation, java.util.EventObject e, java.util.List< java.util.EventObject > preparationUpdates, CancellableOperation.PreparationResult preparationResult ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "ZManager.handlePreparedOperation", operation, preparationResult );
		if( preparationResult != null ) {
			if( preparationResult == CancellableOperation.PreparationResult.CANCEL ) {
				//pass
			} else {
				operation.perform();
				if( preparationResult == CancellableOperation.PreparationResult.PERFORM_AND_ADD_TO_HISTORY ) {
					//addToHistory( operation );
				}
			}
		}
	}
	public static void performIfAppropriate( Operation operation, java.util.EventObject e ) {
		if( operation instanceof CancellableOperation ) {
			CancellableOperation cancellableOperation = (CancellableOperation)operation;
			final java.util.List< java.util.EventObject > preparationUpdates = new java.util.LinkedList< java.util.EventObject >();
			CancellableOperation.PreparationResult preparationResult = cancellableOperation.prepare( e, new CancellableOperation.PreparationObserver() {
				public void update( java.util.EventObject e ) {
					preparationUpdates.add( e );
				}
			} );
			handlePreparedOperation( cancellableOperation, e, preparationUpdates, preparationResult );
		} else {
			if( operation instanceof ResponseOperation ) {
				((ResponseOperation)operation).respond( e );
			}
			operation.perform();
		}
	}
}
