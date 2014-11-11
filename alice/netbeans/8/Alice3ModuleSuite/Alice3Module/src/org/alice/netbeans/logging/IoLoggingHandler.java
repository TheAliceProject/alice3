/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.alice.netbeans.logging;

import edu.cmu.cs.dennisc.java.util.logging.ConsoleFormatter;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import java.util.logging.Level;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.openide.windows.OutputWriter;

/**
 * @author Dennis Cosgrove
 */
public class IoLoggingHandler extends java.util.logging.Handler {
	private static final java.util.logging.Level ERROR_LEVEL = java.util.logging.Level.SEVERE;
	private static final InputOutput io = IOProvider.getDefault().getIO( "Alice3 Plugin", true );

	private static IoLoggingHandler ioLoggingHandler;
	public static void initialize() {
		if( ioLoggingHandler != null ) {
			//pass
		} else {
			ioLoggingHandler = new IoLoggingHandler();
			ioLoggingHandler.setFormatter( new ConsoleFormatter() );
		}
		Logger.getInstance().addHandler( ioLoggingHandler );
		Logger.setLevel(Level.INFO);
	}
	
	public static void uninitialize() {
		Logger.getInstance().removeHandler( ioLoggingHandler );
	}
	
	public static void printStackTrace( Throwable t ) {
		for( StackTraceElement stackTraceElement : t.getStackTrace() ) {
			io.getErr().println( stackTraceElement );
		}
	}
	
	public static void errln( Object object ) {
		io.getErr().println( object );
	}

	public static void errln( Object... objects ) {
		StringBuilder sb = new StringBuilder();
		String separator = "";
		for( Object o : objects ) {
			sb.append( separator );
			sb.append( edu.cmu.cs.dennisc.java.lang.ArrayUtilities.toString( o ) );
			separator = " ";
		}
		io.getErr().println( sb.toString() );
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
