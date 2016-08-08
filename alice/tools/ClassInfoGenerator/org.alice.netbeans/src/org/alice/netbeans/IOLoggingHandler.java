/*******************************************************************************
 * Copyright (c) 2006, 2016, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/

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
