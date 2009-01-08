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
package edu.cmu.cs.dennisc.swing;

/**
 * @author Dennis Cosgrove
 */
public class UIManagerUtilities {
	public static void setDefaultFontResource( javax.swing.plaf.FontUIResource fontUIResource ) {
		for( Object key : javax.swing.UIManager.getDefaults().keySet() ) {
			edu.cmu.cs.dennisc.print.PrintUtilities.println( key );
			Object value = javax.swing.UIManager.get( key );
			if( value instanceof javax.swing.plaf.FontUIResource ) {
				javax.swing.UIManager.put( key, fontUIResource );
				edu.cmu.cs.dennisc.print.PrintUtilities.println( key, fontUIResource );
			}
		}
	}
	public static void setDefaultFont( java.awt.Font font ) {
		setDefaultFontResource( new javax.swing.plaf.FontUIResource( font ) );
	}
}
