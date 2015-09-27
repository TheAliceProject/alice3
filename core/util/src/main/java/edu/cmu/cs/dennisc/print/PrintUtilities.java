/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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

package edu.cmu.cs.dennisc.print;

/**
 * @author Dennis Cosgrove
 */
public abstract class PrintUtilities {
	private static boolean s_isDumpStackDesired;

	private static java.util.Stack<java.io.PrintStream> s_printStreamStack = new java.util.Stack<java.io.PrintStream>();
	private static java.io.PrintStream s_printStream = System.out;
	private static java.util.Stack<java.text.DecimalFormat> s_decimalFormatStack = new java.util.Stack<java.text.DecimalFormat>();
	private static java.text.DecimalFormat s_decimalFormat;
	private static java.util.Stack<String> s_indentTextStack = new java.util.Stack<String>();
	private static String s_indentText;
	private static java.util.Stack<String> s_separatorTextStack = new java.util.Stack<String>();
	private static String s_separatorText;

	private static java.util.Map<Class<?>, java.lang.reflect.Method> s_classToAppendMethod;
	private static java.util.Map<Class<?>, java.lang.reflect.Method> s_classToAppendLinesMethod;

	private static java.lang.reflect.Method getMethod( String name, Class<?> cls ) {
		return edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getMethod( PrintUtilities.class, name, new Class[] { StringBuilder.class, cls } );
	}

	static {
		s_isDumpStackDesired = edu.cmu.cs.dennisc.java.lang.SystemUtilities.isPropertyTrue( "edu.cmu.cs.dennisc.print.PrintUtilities.isDumpStackDesired" );
		//s_isDumpStackDesired = true;

		s_classToAppendMethod = new java.util.HashMap<Class<?>, java.lang.reflect.Method>();
		s_classToAppendLinesMethod = new java.util.HashMap<Class<?>, java.lang.reflect.Method>();

		s_classToAppendMethod.put( Float.class, getMethod( "append", Float.class ) );
		s_classToAppendMethod.put( Double.class, getMethod( "append", Double.class ) );
		Class<?>[] classes = {
				int[].class,
				float[].class,
				double[].class,
				java.nio.IntBuffer.class,
				java.nio.FloatBuffer.class,
				java.nio.DoubleBuffer.class
		};
		for( Class<?> cls : classes ) {
			s_classToAppendMethod.put( cls, getMethod( "append", cls ) );
			s_classToAppendLinesMethod.put( cls, getMethod( "appendLines", cls ) );
		}
		s_decimalFormat = new java.text.DecimalFormat( "0.0000" );
		s_decimalFormat.setPositivePrefix( "+" );
		s_indentText = "    ";
		s_separatorText = " ";
	}

	private PrintUtilities() {
		throw new AssertionError();
	}

	public static void pushPrintStream() {
		s_printStreamStack.push( s_printStream );
	}

	public static java.io.PrintStream accessPrintStream() {
		return s_printStream;
	}

	//todo: add getDecimalFormat
	public static void setPrintStream( java.io.PrintStream printStream ) {
		s_printStream = printStream;
	}

	public static void popPrintStream() {
		s_printStream = s_printStreamStack.pop();
	}

	public static void pushDecimalFormat() {
		s_decimalFormatStack.push( s_decimalFormat );
	}

	public static java.text.DecimalFormat accessDecimalFormat() {
		return s_decimalFormat;
	}

	//todo: add getDecimalFormat
	public static void setDecimalFormat( java.text.DecimalFormat decimalFormat ) {
		s_decimalFormat = decimalFormat;
	}

	public static void popDecimalFormat() {
		s_decimalFormat = s_decimalFormatStack.pop();
	}

	public static void pushIndentText() {
		s_indentTextStack.push( s_indentText );
	}

	public static String getIndentText() {
		return s_indentText;
	}

	public static void setIndentText( String indentText ) {
		s_indentText = indentText;
	}

	public static void popIndentText() {
		s_indentText = s_indentTextStack.pop();
	}

	public static void pushSeparatorText() {
		s_separatorTextStack.push( s_separatorText );
	}

	public static String getSeparatorText() {
		return s_separatorText;
	}

	public static void setSeparatorText( String separatorText ) {
		s_separatorText = separatorText;
	}

	public static void popSeparatorText() {
		s_separatorText = s_separatorTextStack.pop();
	}

	public static final void printlns( java.io.PrintStream ps, int count ) {
		for( int i = 0; i < count; i++ ) {
			ps.println();
		}
	}

	public static final void println( java.io.PrintStream ps ) {
		printlns( ps, 1 );
	}

	public static final void printlns( int count ) {
		printlns( s_printStream, count );
	}

	public static final void println() {
		printlns( 1 );
	}

	//Object...
	private static StringBuilder append( StringBuilder rv, Object[] values, boolean isSingleLine ) {
		if( s_isDumpStackDesired ) {
			Thread.dumpStack();
		}
		for( Object value : values ) {
			if( value != null ) {
				if( value instanceof Printable ) {
					Printable printable = (Printable)value;
					try {
						printable.append( rv, s_decimalFormat, isSingleLine == false );
					} catch( java.io.IOException ioe ) {
						throw new RuntimeException( ioe );
					}
				} else {
					java.util.Map<Class<?>, java.lang.reflect.Method> map;
					if( isSingleLine ) {
						map = s_classToAppendMethod;
					} else {
						map = s_classToAppendLinesMethod;
					}
					Class<?> cls = value.getClass();
					java.lang.reflect.Method method = map.get( cls );
					if( method != null ) {
						Object[] args = { rv, value };
						edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.invoke( null, method, args );
					} else {
						if( value instanceof Object[] ) {
							Object[] array = (Object[])value;
							rv.append( array.getClass().getComponentType().getName() );
							rv.append( "[]: " );
							rv.append( "length=" );
							rv.append( array.length );
							rv.append( "; values=[ " );
							for( Object element : array ) {
								if( isSingleLine ) {
									append( rv, element );
									rv.append( " " );
								} else {
									appendLines( rv, element );
									rv.append( "\n" );
								}
							}
							rv.append( "]" );
						} else {
							rv.append( value );
						}
					}
				}
			} else {
				rv.append( "null" );
			}
			rv.append( s_separatorText );
		}
		return rv;
	}

	public static StringBuilder append( StringBuilder rv, Object... values ) {
		return append( rv, values, true );
	}

	public static StringBuilder appendLines( StringBuilder rv, Object... values ) {
		return append( rv, values, false );
	}

	public static String toString( Object... value ) {
		return append( new StringBuilder(), value ).toString();
	}

	public static String toStringLines( Object... value ) {
		return appendLines( new StringBuilder(), value ).toString();
	}

	public static void print( java.io.PrintStream ps, Object... value ) {
		ps.print( toString( value ) );
	}

	public static void println( java.io.PrintStream ps, Object... value ) {
		ps.println( toString( value ) );
	}

	public static void printlns( java.io.PrintStream ps, Object... value ) {
		ps.println( toStringLines( value ) );
	}

	public static void print( Object... value ) {
		print( s_printStream, value );
	}

	public static void println( Object... value ) {
		println( s_printStream, value );
	}

	public static void printlns( Object... value ) {
		printlns( s_printStream, value );
	}

	//todo: handle null

	//Float

	public static StringBuilder append( StringBuilder rv, Float value ) {
		return rv.append( s_decimalFormat.format( value ) );
	}

	//Double

	public static StringBuilder append( StringBuilder rv, Double value ) {
		return rv.append( s_decimalFormat.format( value ) );
	}

	//java.nio.IntBuffer

	public static StringBuilder append( StringBuilder rv, java.nio.IntBuffer value ) {
		rv.append( value );
		rv.append( ' ' );
		while( value.position() < value.limit() ) {
			rv.append( value.get() );
			rv.append( ' ' );
		}
		value.rewind();
		return rv;
	}

	public static StringBuilder appendLines( StringBuilder rv, java.nio.IntBuffer value ) {
		//todo:
		return append( rv, value );
	}

	//java.nio.FloatBuffer

	public static StringBuilder append( StringBuilder rv, java.nio.FloatBuffer value ) {
		rv.append( value );
		rv.append( ' ' );
		while( value.position() < value.limit() ) {
			append( rv, value.get() );
			rv.append( ' ' );
		}
		value.rewind();
		return rv;
	}

	public static StringBuilder appendLines( StringBuilder rv, java.nio.FloatBuffer value ) {
		//todo:
		return append( rv, value );
	}

	//java.nio.DoubleBuffer

	public static StringBuilder append( StringBuilder rv, java.nio.DoubleBuffer value ) {
		rv.append( value );
		rv.append( ' ' );
		while( value.position() < value.limit() ) {
			append( rv, value.get() );
			rv.append( ' ' );
		}
		value.rewind();
		return rv;
	}

	public static StringBuilder appendLines( StringBuilder rv, java.nio.DoubleBuffer value ) {
		//todo:
		return append( rv, value );
	}

	//int[]

	public static StringBuilder append( StringBuilder rv, int[] value ) {
		rv.append( "int[]: " );
		if( value != null ) {
			rv.append( "length=" );
			rv.append( value.length );
			rv.append( "; values=[ " );
			for( int element : value ) {
				rv.append( element );
				rv.append( " " );
			}
			rv.append( "]" );
		} else {
			rv.append( "null" );
		}
		return rv;
	}

	public static StringBuilder appendLines( StringBuilder rv, int[] value ) {
		//todo:
		return append( rv, value );
	}

	//float[]

	public static StringBuilder append( StringBuilder rv, float[] value ) {
		rv.append( "float[]: " );
		if( value != null ) {
			rv.append( "length=" );
			rv.append( value.length );
			rv.append( "; values=[ " );
			for( float element : value ) {
				append( rv, element );
				rv.append( " " );
			}
			rv.append( "]" );
		} else {
			rv.append( "null" );
		}
		return rv;
	}

	public static StringBuilder appendLines( StringBuilder rv, float[] value ) {
		//todo:
		return append( rv, value );
	}

	//double[]

	public static StringBuilder append( StringBuilder rv, double[] value ) {
		rv.append( "double[]: " );
		if( value != null ) {
			rv.append( "length=" );
			rv.append( value.length );
			rv.append( "; values=[ " );
			for( double element : value ) {
				append( rv, element );
				rv.append( " " );
			}
			rv.append( "]" );
		} else {
			rv.append( "null" );
		}
		return rv;
	}

	public static StringBuilder appendLines( StringBuilder rv, double[] value ) {
		//todo:
		return append( rv, value );
	}

	public static Appendable append( Appendable rv, Printable value ) {
		try {
			return value.append( rv, s_decimalFormat, false );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}

	public static Appendable appendLines( Appendable rv, Printable value ) {
		try {
			return value.append( rv, s_decimalFormat, true );
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
}
