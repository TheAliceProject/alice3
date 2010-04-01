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
package edu.cmu.cs.dennisc.alice.ide;

/**
 * @author Dennis Cosgrove
 */
public class MenuUtilities {
	private static void setHeavyWeight( javax.swing.JPopupMenu popup ) {
		popup.setLightWeightPopupEnabled( false );
	}
	public static javax.swing.JMenu createJMenu( String name, Operation... operations ) {
		javax.swing.JMenu rv = new javax.swing.JMenu( name );
		for( Operation operation : operations ) {
			if( operation != null ) {
				rv.add( new MenuItem( operation ) );
			} else {
				rv.addSeparator();
			}
		}
		//todo?
		setHeavyWeight( rv.getPopupMenu() );
		return rv;
	}
	public static javax.swing.JPopupMenu createJPopupMenu( Operation... operations ) {
		javax.swing.JPopupMenu rv = new javax.swing.JPopupMenu();
		for( Operation operation : operations ) {
			if( operation != null ) {
				rv.add( new MenuItem( operation ) );
			} else {
				rv.addSeparator();
			}
		}
		//todo?
		setHeavyWeight( rv );
		return rv;
	}
	public static javax.swing.JPopupMenu createJPopupMenu( java.util.Collection< Operation > operations ) {
		Operation[] array = new Operation[ operations.size() ];
		operations.toArray( array );
		return createJPopupMenu( array );
	}
}
