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
package edu.cmu.cs.dennisc.util;

/**
 * @author Dennis Cosgrove
 */
public abstract class ResourceBundleUtilities {
	public static String getStringFromSimpleNames( Class<?> cls, String baseName, java.util.Locale locale ) {
		java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( baseName, locale );
		String key;
		Class<?> c = cls;
		do {
			if( c != null ) {
				key = c.getSimpleName();
				c = c.getSuperclass();
			} else {
				throw new RuntimeException( "cannot find resource for " + cls );
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "cannot find resource for", cls );
				//return null;
			}
			try {
				String unused = resourceBundle.getString( key );
				break;
			} catch( RuntimeException re ) {
				//pass;
			}
		} while( true );
		return resourceBundle.getString( key );
	}
	public static String getStringFromSimpleNames( Class<?> cls, String baseName ) {
		return getStringFromSimpleNames( cls, baseName, javax.swing.JComponent.getDefaultLocale() );
	}
}
