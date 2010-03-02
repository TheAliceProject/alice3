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

package edu.cmu.cs.dennisc.print;

/**
 * @author Dennis Cosgrove
 */
public abstract class PrintUtilities {
	private static boolean s_isDumpStackDesired;	
	
	private static java.util.Stack< java.io.PrintStream > s_printStreamStack = new java.util.Stack< java.io.PrintStream >();
	private static java.io.PrintStream s_printStream = System.out;
	private static java.util.Stack< java.text.DecimalFormat > s_decimalFormatStack = new java.util.Stack< java.text.DecimalFormat >();
	private static java.text.DecimalFormat s_decimalFormat;
	private static java.util.Stack< String > s_indentTextStack = new java.util.Stack< String >();
	private static String s_indentText;
	private static java.util.Stack< String > s_separatorTextStack = new java.util.Stack< String >();
	private static String s_separatorText;

	private static java.util.Map< Class<?>, java.lang.reflect.Method > s_classToAppendMethod;
	private static java.util.Map< Class<?>, java.lang.reflect.Method > s_classToAppendLinesMethod;

	private static java.lang.reflect.Method getMethod( String name, Class<?> cls ) {
		return edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getMethod( PrintUtilities.class, name, new Class[] { StringBuffer.class, cls } );
	}
	

	static {
		s_isDumpStackDesired = "true".equals( System.getProperty( "edu.cmu.cs.dennisc.print.PrintUtilities.isDumpStackDesired" ) );
		
		s_classToAppendMethod = new java.util.HashMap< Class<?>, java.lang.reflect.Method >();
		s_classToAppendLinesMethod = new java.util.HashMap< Class<?>, java.lang.reflect.Method >();

		s_classToAppendMethod.put( Float.class, getMethod( "append", Float.class  ) );
		s_classToAppendMethod.put( Double.class, getMethod( "append", Double.class  ) );
		Class<?>[] classes = { 
				int[].class, 
				float[].class, 
				double[].class, 
				java.nio.IntBuffer.class, 
				java.nio.FloatBuffer.class, 
				java.nio.DoubleBuffer.class,
				Printable.class
		};
		for( Class<?> cls : classes ) {
			s_classToAppendMethod.put( cls, getMethod( "append", cls  ) );
			s_classToAppendLinesMethod.put( cls, getMethod( "appendLines", cls  ) );
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
		for( int i=0; i<count; i++ ) {
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
		printlns(1);
	}

	//Object...
	private static StringBuffer append( StringBuffer rv, Object[] values, boolean isSingleLine ) {
		if( s_isDumpStackDesired ) {
			Thread.dumpStack();
		}
		for( Object value : values ) {
			if( value != null ) {
				java.util.Map< Class< ? >, java.lang.reflect.Method > map;
				if( isSingleLine ) {
					map = s_classToAppendMethod;
				} else {
					map = s_classToAppendLinesMethod;
				}
				java.lang.reflect.Method method = map.get( value.getClass() );
				if( method != null ) {
					Object[] args = { rv, value };
					edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.invoke( null, method, args );
				} else {
					if( value instanceof Object[] ) {
						Object[] array = (Object[])value;
						rv.append( array.getClass().getComponentType().getName() );
						rv.append( "[]: " );
						rv.append( "length=" );
						rv.append( array.length );
						rv.append( "; values=[ " );
						for( int i = 0; i < array.length; i++ ) {
							if( isSingleLine ) {
								append( rv, array[ i ] );
								rv.append( " " );
							} else {
								appendLines( rv, array[ i ] );
								rv.append( "\n" );
							}
						}
						rv.append( "]" );
					} else {
						rv.append( value );
					}
				}
			} else {
				rv.append( "null" );
			}
			rv.append( s_separatorText );
		}
		return rv;
	}

	public static StringBuffer append( StringBuffer rv, Object... values ) {
		return append( rv, values, true );
	}
	public static StringBuffer appendLines( StringBuffer rv, Object... values ) {
		return append( rv, values, false );
	}
	public static String toString( Object... value ) {
		return append( new StringBuffer(), value ).toString();
	}
	public static String toStringLines( Object... value ) {
		return appendLines( new StringBuffer(), value ).toString();
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

	public static StringBuffer append( StringBuffer rv, Float value ) {
		return rv.append( s_decimalFormat.format( value ) );
	}

	//Double

	public static StringBuffer append( StringBuffer rv, Double value ) {
		return rv.append( s_decimalFormat.format( value ) );
	}

	//java.nio.IntBuffer

	public static StringBuffer append( StringBuffer rv, java.nio.IntBuffer value ) {
		rv.append( value );
		rv.append( ' ' );
		while( value.position() < value.limit() ) {
			rv.append( value.get() );
			rv.append( ' ' );
		}
		value.rewind();
		return rv;
	}

	public static StringBuffer appendLines( StringBuffer rv, java.nio.IntBuffer value ) {
		//todo:
		return append( rv, value );
	}

	//java.nio.FloatBuffer

	public static StringBuffer append( StringBuffer rv, java.nio.FloatBuffer value ) {
		rv.append( value );
		rv.append( ' ' );
		while( value.position() < value.limit() ) {
			append( rv, value.get() );
			rv.append( ' ' );
		}
		value.rewind();
		return rv;
	}

	public static StringBuffer appendLines( StringBuffer rv, java.nio.FloatBuffer value ) {
		//todo:
		return append( rv, value );
	}

	//java.nio.DoubleBuffer

	public static StringBuffer append( StringBuffer rv, java.nio.DoubleBuffer value ) {
		rv.append( value );
		rv.append( ' ' );
		while( value.position() < value.limit() ) {
			append( rv, value.get() );
			rv.append( ' ' );
		}
		value.rewind();
		return rv;
	}

	public static StringBuffer appendLines( StringBuffer rv, java.nio.DoubleBuffer value ) {
		//todo:
		return append( rv, value );
	}

	//int[]

	public static StringBuffer append( StringBuffer rv, int[] value ) {
		rv.append( "int[]: " );
		if( value != null ) {
			rv.append( "length=" );
			rv.append( value.length );
			rv.append( "; values=[ " );
			for( int i = 0; i < value.length; i++ ) {
				rv.append( value[ i ] );
				rv.append( " " );
			}
			rv.append( "]" );
		} else {
			rv.append( "null" );
		}
		return rv;
	}

	public static StringBuffer appendLines( StringBuffer rv, int[] value ) {
		//todo:
		return append( rv, value );
	}

	//float[]

	public static StringBuffer append( StringBuffer rv, float[] value ) {
		rv.append( "float[]: " );
		if( value != null ) {
			rv.append( "length=" );
			rv.append( value.length );
			rv.append( "; values=[ " );
			for( int i = 0; i < value.length; i++ ) {
				append( rv, value[ i ] );
				rv.append( " " );
			}
			rv.append( "]" );
		} else {
			rv.append( "null" );
		}
		return rv;
	}

	public static StringBuffer appendLines( StringBuffer rv, float[] value ) {
		//todo:
		return append( rv, value );
	}

	//double[]

	public static StringBuffer append( StringBuffer rv, double[] value ) {
		rv.append( "double[]: " );
		if( value != null ) {
			rv.append( "length=" );
			rv.append( value.length );
			rv.append( "; values=[ " );
			for( int i = 0; i < value.length; i++ ) {
				append( rv, value[ i ] );
				rv.append( " " );
			}
			rv.append( "]" );
		} else {
			rv.append( "null" );
		}
		return rv;
	}

	public static StringBuffer appendLines( StringBuffer rv, double[] value ) {
		//todo:
		return append( rv, value );
	}

	public static StringBuffer append( StringBuffer rv, Printable value ) {
		return value.append( rv, s_decimalFormat, false );
	}

	public static StringBuffer appendLines( StringBuffer rv, Printable value ) {
		return value.append( rv, s_decimalFormat, true );
	}
}
