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

package org.alice.netbeans;

import edu.cmu.cs.dennisc.java.lang.ThrowableUtilities;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.openide.windows.OutputWriter;

/**
 * @author Dennis Cosgrove
 */
public class IOLoggingHandler extends java.util.logging.Handler {
	private static java.util.logging.Level ERROR_LEVEL = java.util.logging.Level.SEVERE;
	private static InputOutput io = IOProvider.getDefault().getIO( "ClassInfo Generator", true );

	public static void printStackTrace( Throwable t ) {
		io.getErr().write(ThrowableUtilities.getStackTraceAsString(t));
	}

	private static void writeln( OutputWriter writer, Object o ) {
		writer.write( o != null ? o.toString() : "null" );
		writer.write( "\n" );
		writer.flush();
	}
	public static void errln( Object o ) {
		writeln(io.getErr(), o);
	}
	public static void outln( Object o ) {
		writeln(io.getOut(), o);
	}

	@Override
	public void publish( java.util.logging.LogRecord record ) {
		OutputWriter os;
		if( record.getLevel().intValue() >= ERROR_LEVEL.intValue() ) {
			os = io.getErr();
		} else {
			os = io.getOut();
		}
		os.print( this.getFormatter().format( record ) );
		os.flush();
	}
	@Override
	public void flush() {
	}
	@Override
	public void close() throws java.lang.SecurityException {
	}
}
