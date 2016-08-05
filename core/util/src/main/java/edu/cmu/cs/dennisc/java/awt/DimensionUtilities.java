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
package edu.cmu.cs.dennisc.java.awt;

/**
 * @author Dennis Cosgrove
 */
public class DimensionUtilities {
	public static java.awt.Dimension constrainToMinimumWidth( java.awt.Dimension rv, int minimumWidth ) {
		if( rv != null ) {
			rv.width = Math.max( rv.width, minimumWidth );
		} else {
			rv = new java.awt.Dimension( minimumWidth, 0 );
		}
		return rv;
	}

	public static java.awt.Dimension constrainToMinimumHeight( java.awt.Dimension rv, int minimumHeight ) {
		if( rv != null ) {
			rv.height = Math.max( rv.height, minimumHeight );
		} else {
			rv = new java.awt.Dimension( 0, minimumHeight );
		}
		return rv;
	}

	public static java.awt.Dimension constrainToMinimumSize( java.awt.Dimension rv, int minimumWidth, int minimumHeight ) {
		rv = constrainToMinimumWidth( rv, minimumWidth );
		rv = constrainToMinimumHeight( rv, minimumHeight );
		return rv;
	}

	public static java.awt.Dimension constrainToMaximumWidth( java.awt.Dimension rv, int maximumWidth ) {
		rv.width = Math.min( rv.width, maximumWidth );
		return rv;
	}

	public static java.awt.Dimension constrainToMaximumHeight( java.awt.Dimension rv, int maximumHeight ) {
		rv.height = Math.min( rv.height, maximumHeight );
		return rv;
	}

	public static java.awt.Dimension constrainToMaximumSize( java.awt.Dimension rv, int maximumWidth, int maximumHeight ) {
		rv = constrainToMaximumWidth( rv, maximumWidth );
		rv = constrainToMaximumHeight( rv, maximumHeight );
		return rv;
	}

	public static java.awt.Dimension constrainToWidth( java.awt.Dimension rv, int width ) {
		rv.width = width;
		return rv;
	}

	public static java.awt.Dimension constrainToHeight( java.awt.Dimension rv, int height ) {
		rv.height = height;
		return rv;
	}

	private static java.awt.Dimension calculateWidthBasedSize( java.awt.Dimension size, double widthToHeightAspectRatio ) {
		return new java.awt.Dimension( size.width, (int)( size.width / widthToHeightAspectRatio ) );
	}

	private static java.awt.Dimension calculateHeightBasedSize( java.awt.Dimension size, double widthToHeightAspectRatio ) {
		return new java.awt.Dimension( (int)( size.height * widthToHeightAspectRatio ), size.height );
	}

	public static java.awt.Dimension calculateBestFittingSize( java.awt.Dimension size, double widthToHeightAspectRatio ) {
		java.awt.Dimension widthBasedSize = calculateWidthBasedSize( size, widthToHeightAspectRatio );
		java.awt.Dimension heightBasedSize = calculateHeightBasedSize( size, widthToHeightAspectRatio );
		if( widthBasedSize.height > size.height ) {
			return heightBasedSize;
		} else if( heightBasedSize.width > size.width ) {
			return widthBasedSize;
		} else {
			if( ( widthBasedSize.width * widthBasedSize.height ) > ( heightBasedSize.width * heightBasedSize.height ) ) {
				return widthBasedSize;
			} else {
				return heightBasedSize;
			}
		}
	}

	public static java.awt.Dimension createWiderGoldenRatioSizeFromWidth( int width ) {
		return new java.awt.Dimension( width, edu.cmu.cs.dennisc.math.GoldenRatio.getShorterSideLength( width ) );
	}

	public static java.awt.Dimension createWiderGoldenRatioSizeFromHeight( int height ) {
		return new java.awt.Dimension( edu.cmu.cs.dennisc.math.GoldenRatio.getLongerSideLength( height ), height );
	}

	public static java.awt.Dimension createTallerGoldenRatioSizeFromWidth( int width ) {
		return new java.awt.Dimension( width, edu.cmu.cs.dennisc.math.GoldenRatio.getLongerSideLength( width ) );
	}

	public static java.awt.Dimension createTallerGoldenRatioSizeFromHeight( int height ) {
		return new java.awt.Dimension( edu.cmu.cs.dennisc.math.GoldenRatio.getShorterSideLength( height ), height );
	}

}
