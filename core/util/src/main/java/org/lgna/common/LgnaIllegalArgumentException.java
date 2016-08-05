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
package org.lgna.common;

/**
 * @author Dennis Cosgrove
 */
public class LgnaIllegalArgumentException extends LgnaRuntimeException {
	//todo: remove return value?
	public static <T> T checkArgumentIsNotNull( T value, int index ) {
		if( value != null ) {
			return value;
		} else {
			throw new LgnaIllegalArgumentException( "argument must not be null", index, value );
		}
	}

	public static Number checkArgumentIsNumber( Number value, int index ) {
		checkArgumentIsNotNull( value, index );
		if( Double.isNaN( value.doubleValue() ) == false ) {
			return value;
		} else {
			throw new LgnaIllegalArgumentException( "argument must be a number", index, value );
		}
	}

	public static Number checkArgumentIsPositive( Number value, int index ) {
		checkArgumentIsNumber( value, index );
		if( value.doubleValue() > 0.0 ) {
			return value;
		} else {
			throw new LgnaIllegalArgumentException( "argument must be positive", index, value );
		}
	}

	public static Number checkArgumentIsPositiveOrZero( Number value, int index ) {
		checkArgumentIsNumber( value, index );
		if( value.doubleValue() >= 0.0 ) {
			return value;
		} else {
			throw new LgnaIllegalArgumentException( "argument must be positive or zero", index, value );
		}
	}

	public static Number checkArgumentIsBetween0and1( Number value, int index ) {
		checkArgumentIsNumber( value, index );
		double d = value.doubleValue();
		if( ( 0.0 <= d ) && ( d <= 1.0 ) ) {
			return value;
		} else {
			throw new LgnaIllegalArgumentException( "argument must be positive or zero", index, value );
		}
	}

	private final int index;
	private final Object value;

	public LgnaIllegalArgumentException( String message, int index, Object value ) {
		super( message );
		this.index = index;
		this.value = value;
	}

	public int getIndex() {
		return this.index;
	}

	public Object getValue() {
		return this.value;
	}

	@Override
	protected void appendFormattedString( StringBuilder sb ) {
		sb.append( "<html>" );
		sb.append( "<h1>" );
		sb.append( this.getClass().getSimpleName() );
		sb.append( "</h1>" );
		sb.append( "<h2>" );
		sb.append( this.getMessage() );
		sb.append( "</h2>" );
		sb.append( "</html>" );
	}
}
