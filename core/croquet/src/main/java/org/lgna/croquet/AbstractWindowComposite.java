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
package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractWindowComposite<V extends org.lgna.croquet.views.CompositeView<?, ?>> extends AbstractSeverityStatusComposite<V> {
	public AbstractWindowComposite( java.util.UUID migrationId ) {
		super( migrationId );
	}

	protected static enum GoldenRatioPolicy {
		WIDTH_LONG_SIDE {
			@Override
			public java.awt.Dimension calculateWindowSize( org.lgna.croquet.views.AbstractWindow<?> window ) {
				java.awt.Dimension size = window.getAwtComponent().getPreferredSize();
				int phiHeight = edu.cmu.cs.dennisc.math.GoldenRatio.getShorterSideLength( size.width );
				if( phiHeight > size.height ) {
					return new java.awt.Dimension( size.width, phiHeight );
				} else {
					int phiWidth = edu.cmu.cs.dennisc.math.GoldenRatio.getLongerSideLength( size.height );
					if( phiWidth > size.width ) {
						return new java.awt.Dimension( phiWidth, size.height );
					} else {
						return size;
					}
				}
			}
		},
		HEIGHT_LONG_SIDE {
			@Override
			public java.awt.Dimension calculateWindowSize( org.lgna.croquet.views.AbstractWindow<?> window ) {
				java.awt.Dimension size = window.getAwtComponent().getPreferredSize();
				int phiHeight = edu.cmu.cs.dennisc.math.GoldenRatio.getLongerSideLength( size.width );
				if( phiHeight > size.height ) {
					return new java.awt.Dimension( size.width, phiHeight );
				} else {
					int phiWidth = edu.cmu.cs.dennisc.math.GoldenRatio.getShorterSideLength( size.height );
					if( phiWidth > size.width ) {
						return new java.awt.Dimension( phiWidth, size.height );
					} else {
						return size;
					}
				}
			}
		};
		public abstract java.awt.Dimension calculateWindowSize( org.lgna.croquet.views.AbstractWindow<?> window );

	}

	protected GoldenRatioPolicy getGoldenRatioPolicy() {
		return GoldenRatioPolicy.WIDTH_LONG_SIDE;
	}

	protected Integer getWiderGoldenRatioSizeFromWidth() {
		return null;
	}

	protected Integer getWiderGoldenRatioSizeFromHeight() {
		return null;
	}

	protected java.awt.Dimension calculateWindowSize( org.lgna.croquet.views.AbstractWindow<?> window ) {
		Integer width = this.getWiderGoldenRatioSizeFromWidth();
		if( width != null ) {
			return edu.cmu.cs.dennisc.java.awt.DimensionUtilities.createWiderGoldenRatioSizeFromWidth( width );
		} else {
			Integer height = this.getWiderGoldenRatioSizeFromHeight();
			if( height != null ) {
				return edu.cmu.cs.dennisc.java.awt.DimensionUtilities.createWiderGoldenRatioSizeFromHeight( height );
			} else {
				GoldenRatioPolicy goldenRatioPolicy = this.getGoldenRatioPolicy();
				if( goldenRatioPolicy != null ) {
					window.pack();
					return goldenRatioPolicy.calculateWindowSize( window );
				} else {
					window.pack();
					return window.getAwtComponent().getPreferredSize();
				}
			}
		}
	}

	public final void updateWindowSize( org.lgna.croquet.views.AbstractWindow<?> window ) {
		window.setSize( this.calculateWindowSize( window ) );
		window.getContentPane().revalidateAndRepaint();
	}

	protected java.awt.Point getDesiredWindowLocation() {
		return null;
	}
}
