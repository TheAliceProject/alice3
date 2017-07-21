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
package org.lgna.croquet.views;

import java.awt.Dimension;

/**
 * @author Matt May
 */
public class PreserveAspectRatioPanel extends Panel {

	private Dimension dimension;

	public PreserveAspectRatioPanel( AwtComponentView<?> centerPanel, Dimension dimension ) {
		this.dimension = dimension;
		this.internalAddComponent( centerPanel );
	}

	@Override
	protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		return new java.awt.LayoutManager() {
			@Override
			public void addLayoutComponent( String name, java.awt.Component comp ) {
			}

			@Override
			public void removeLayoutComponent( java.awt.Component comp ) {
			}

			private java.awt.Dimension layoutSize( java.awt.Container parent ) {
				if( parent.getComponentCount() > 0 ) {
					return parent.getComponent( 0 ).getPreferredSize();
				} else {
					return new java.awt.Dimension();
				}
			}

			@Override
			public java.awt.Dimension minimumLayoutSize( java.awt.Container parent ) {
				return this.layoutSize( parent );
			}

			@Override
			public java.awt.Dimension preferredLayoutSize( java.awt.Container parent ) {
				return this.layoutSize( parent );
			}

			@Override
			public void layoutContainer( java.awt.Container parent ) {
				if( parent.getComponentCount() > 0 ) {
					java.awt.Dimension parentSize = parent.getSize();
					java.awt.Component component = parent.getComponent( 0 );
					Dimension properSize = getProperSize();
					component.setSize( properSize );
					component.setLocation( ( parentSize.width - properSize.width ) / 2, ( parentSize.height - properSize.height ) / 2 );
				}
			}

			private Dimension getProperSize() {
				Dimension rv = new Dimension();
				if( isTooFat() ) {
					rv.width = (int)( ( getHeight() / dimension.getHeight() ) * dimension.getWidth() );
					rv.height = getHeight();
				} else {
					rv.width = getWidth();
					rv.height = (int)( ( getWidth() / dimension.getWidth() ) * dimension.getHeight() );
				}
				return rv;
			}

			private boolean isTooFat() {
				return ( getWidth() / dimension.getWidth() ) > ( getHeight() / dimension.getHeight() );
			}
		};
	}
}
