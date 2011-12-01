/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
 */

package edu.cmu.cs.dennisc.java.util.logging;

/**
 * @author Dennis Cosgrove
 */
public class GlobalLogger {
//	private static final String NAME = GlobalLogger.class.getName();
//	private static class InstanceHolder {
//		private static java.util.logging.Logger instance = java.util.logging.Logger.getLogger( NAME );
//	}
//	public static java.util.logging.Logger getInstance() {
//		return InstanceHolder.instance;
//	}
	private static java.util.logging.Logger getInstance() {
		return java.util.logging.Logger.global;
	}
	static {
		java.util.logging.LogManager.getLogManager().reset();
		//java.util.logging.Logger.global.setUseParentHandlers( false );
		getInstance().setLevel( java.util.logging.Level.INFO );
		java.util.logging.ConsoleHandler consoleHandler = new java.util.logging.ConsoleHandler();
		consoleHandler.setFormatter( new ConsoleFormatter() );
		getInstance().addHandler( consoleHandler );
		
	}
	private GlobalLogger() {
		throw new AssertionError();
	}
	
	private static String buildMessage( Object object ) {
		return object != null ? object.toString() : null;
	}
	private static String buildMessage( Object[] objects ) {
		StringBuilder sb = new StringBuilder();
		String separator = "";
		for( Object o : objects ) {
			sb.append( separator );
			sb.append( o.toString() );
			separator = " ";
		}
		return sb.toString();
	}

	private static boolean isLoggable( java.util.logging.Level level ) {
		return getInstance().isLoggable( level );
	}
	
	private static java.util.logging.Level TODO_LEVEL = new java.util.logging.Level( "TODO", java.util.logging.Level.INFO.intValue() + 1 ) {};
	private static java.util.logging.Level THROWABLE_LEVEL = new java.util.logging.Level( "THROWABLE", java.util.logging.Level.SEVERE.intValue() + 1 ) {};
	
	private static void log( java.util.logging.Level level, Object object, Throwable throwable ) {
		if( isLoggable( level ) ) {
			String message = buildMessage( object );
			if( throwable != null ) {
				getInstance().log( level, message, throwable );
			} else {
				getInstance().log( level, message );
			}
		}
	}
	private static void log( java.util.logging.Level level, Object[] objects, Throwable throwable ) {
		if( isLoggable( level ) ) {
			String message = buildMessage( objects );
			if( throwable != null ) {
				getInstance().log( level, message, throwable );
			} else {
				getInstance().log( level, message );
			}
		}
	}
	private static void log( java.util.logging.Level level, Object object ) {
		log( level, object, null );
	}
	private static void log( java.util.logging.Level level, Object[] objects ) {
		log( level, objects, null );
	}
	
	public static void throwable( Throwable t, Object object ) {
		log( THROWABLE_LEVEL, object );
	}
	public static void throwable( Throwable t, Object... objects ) {
		log( THROWABLE_LEVEL, objects );
	}
	
	public static void todo( Object object ) {
		log( TODO_LEVEL, object );
	}
	public static void todo( Object... objects ) {
		log( TODO_LEVEL, objects );
	}

	public static void severe( Object object ) {
		log( java.util.logging.Level.SEVERE, object );
	}
	public static void severe( Object... objects ) {
		log( java.util.logging.Level.SEVERE, objects );
	}
	public static void warning( Object object ) {
		log( java.util.logging.Level.WARNING, object );
	}
	public static void warning( Object... objects ) {
		log( java.util.logging.Level.WARNING, objects );
	}
	public static void info( Object object ) {
		log( java.util.logging.Level.INFO, object );
	}
	public static void info( Object... objects ) {
		log( java.util.logging.Level.INFO, objects );
	}
	public static void config( Object object ) {
		log( java.util.logging.Level.CONFIG, object );
	}
	public static void config( Object... objects ) {
		log( java.util.logging.Level.CONFIG, objects );
	}
	public static void fine( Object object ) {
		log( java.util.logging.Level.FINE, object );
	}
	public static void fine( Object... objects ) {
		log( java.util.logging.Level.FINE, objects );
	}
	public static void finer( Object object ) {
		log( java.util.logging.Level.FINER, object );
	}
	public static void finer( Object... objects ) {
		log( java.util.logging.Level.FINER, objects );
	}
	public static void finest( Object object ) {
		log( java.util.logging.Level.FINEST, object );
	}
	public static void finest( Object... objects ) {
		log( java.util.logging.Level.FINEST, objects );
	}
}
