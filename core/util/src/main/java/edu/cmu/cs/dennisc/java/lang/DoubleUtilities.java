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
package edu.cmu.cs.dennisc.java.lang;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParsePosition;

/**
 * @author Dennis Cosgrove
 */
public class DoubleUtilities {
	public static Double toDouble( Number n ) {
		return n.doubleValue();
	}

	public static Double divide( Number numerator, Number denominator ) {
		return numerator.doubleValue() / denominator.doubleValue();
	}

	public static double parseDoubleInCurrentDefaultLocale( String text ) {
		ParsePosition parsePosition = new ParsePosition( 0 );
		Number number = NumberFormat.getNumberInstance().parse( text, parsePosition );
		if( ( number != null ) && ( parsePosition.getIndex() == text.length() ) ) {
			return number.doubleValue();
		} else {
			return Double.NaN;
		}
	}

	public static String format( double d, NumberFormat format ) {
		synchronized( format ) {
			return format.format( d );
		}
	}

	public static String formatInCurrentDefaultLocale( double d ) {
		return format( d, NumberFormat.getNumberInstance() );
	}

	//	public static double formatAndParse( double d, java.text.NumberFormat format, double valueInCaseOfThrowable ) {
	//		synchronized( format ) {
	//			try {
	//				String text = format.format( d );
	//				java.text.ParsePosition parsePosition = new java.text.ParsePosition( 0 );
	//				Number number = format.parse( text, parsePosition );
	//				if( number != null && parsePosition.getIndex() == text.length() ) {
	//					return number.doubleValue();
	//				} else {
	//					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( text );
	//					return valueInCaseOfThrowable;
	//				}
	//			} catch( Throwable t ) {
	//				return valueInCaseOfThrowable;
	//			}
	//		}
	//	}
	public static double round( double value, int decimalPlaces ) {
		if (Double.isFinite( value )) {
			BigDecimal bigDecimal = new BigDecimal( value );
			bigDecimal = bigDecimal.round( new MathContext( decimalPlaces, RoundingMode.HALF_DOWN ) );
			return bigDecimal.doubleValue();
		}
		else {
			return value;
		}
	}
}
