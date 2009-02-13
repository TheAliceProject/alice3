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
package edu.cmu.cs.dennisc.apple;

/**
 * @author Dennis Cosgrove
 */
public class AppleUtilities {
	public static void addApplicationListener( edu.cmu.cs.dennisc.apple.event.ApplicationListener listener ) { 
		if( edu.cmu.cs.dennisc.lang.SystemUtilities.isMac() ) {
			//jython's class loader necessitates this reflection
			java.lang.reflect.Constructor< ? > constructor = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getConstructor( edu.cmu.cs.dennisc.apple.Adapter.class, edu.cmu.cs.dennisc.apple.event.ApplicationListener.class );
			com.apple.eawt.ApplicationListener adapter = (com.apple.eawt.ApplicationListener)edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.newInstance( constructor, listener );

			com.apple.eawt.Application application = com.apple.eawt.Application.getApplication();
			application.addApplicationListener( adapter );
		}
	}
}
