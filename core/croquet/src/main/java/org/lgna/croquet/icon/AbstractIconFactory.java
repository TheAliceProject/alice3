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
package org.lgna.croquet.icon;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractIconFactory implements IconFactory {
	protected static enum IsCachingDesired {
		TRUE,
		FALSE;
	}

	private final java.util.Map<java.awt.Dimension, javax.swing.Icon> map;

	public AbstractIconFactory( IsCachingDesired isCachingDesired ) {
		if( isCachingDesired == IsCachingDesired.TRUE ) {
			this.map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
		} else {
			this.map = null;
		}
	}

	//	protected java.util.Map<java.awt.Dimension, javax.swing.Icon> getMap() {
	//		return this.map;
	//	}

	protected java.util.Collection<javax.swing.Icon> getMapValues() {
		return this.map.values();
	}

	protected abstract javax.swing.Icon createIcon( java.awt.Dimension size );

	@Override
	public final javax.swing.Icon getIcon( java.awt.Dimension size ) {
		if( this.map != null ) {
			javax.swing.Icon rv = this.map.get( size );
			if( rv != null ) {
				//pass
			} else {
				rv = this.createIcon( size );
				this.map.put( size, rv );
			}
			return rv;
		} else {
			return this.createIcon( size );
		}
	}

	protected double getDefaultWidthToHeightAspectRatio() {
		java.awt.Dimension defaultSize = this.getDefaultSize( null );
		if( defaultSize != null ) {
			return defaultSize.width / (double)defaultSize.height;
		} else {
			return 4.0 / 3.0;
		}
	}

	protected double getTrimmedWidthToHeightAspectRatio() {
		return this.getDefaultWidthToHeightAspectRatio();
	}

	protected java.awt.Dimension createDimensionForWidth( int width, double widthToHeigthAspectRatio ) {
		int height = (int)Math.round( width / widthToHeigthAspectRatio );
		return new java.awt.Dimension( width, height );
	}

	protected java.awt.Dimension createDimensionForHeight( int height, double widthToHeigthAspectRatio ) {
		int width = (int)Math.round( height * widthToHeigthAspectRatio );
		return new java.awt.Dimension( width, height );
	}

	@Override
	public final java.awt.Dimension getDefaultSizeForWidth( int width ) {
		return this.createDimensionForWidth( width, this.getDefaultWidthToHeightAspectRatio() );
	}

	@Override
	public final java.awt.Dimension getDefaultSizeForHeight( int height ) {
		return this.createDimensionForHeight( height, this.getDefaultWidthToHeightAspectRatio() );
	}

	@Override
	public java.awt.Dimension getTrimmedSizeForWidth( int width ) {
		return this.createDimensionForWidth( width, this.getTrimmedWidthToHeightAspectRatio() );
	}

	@Override
	public java.awt.Dimension getTrimmedSizeForHeight( int height ) {
		return this.createDimensionForHeight( height, this.getTrimmedWidthToHeightAspectRatio() );
	}
}
