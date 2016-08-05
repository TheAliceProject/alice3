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
package org.alice.stageide.icons;

/**
 * @author Dennis Cosgrove
 */
public class EnumConstantsIcon extends org.lgna.croquet.icon.AbstractIcon {
	private static final int OFFSET = 8;
	private final javax.swing.Icon[] icons;

	public EnumConstantsIcon( java.awt.Dimension size, java.util.List<org.lgna.croquet.icon.IconFactory> iconFactories ) {
		super( size );
		if( size.width >= 160 ) {
			final int N = iconFactories.size();
			this.icons = new javax.swing.Icon[ N ];
			int totalOffset = OFFSET * ( N - 1 );
			int subWidth = Math.max( size.width - totalOffset, size.width / 2 );
			int subHeight = Math.max( size.height - totalOffset, size.height / 2 );
			java.awt.Dimension subSize = new java.awt.Dimension( subWidth, subHeight );
			for( int i = 0; i < N; i++ ) {
				this.icons[ N - i - 1 ] = iconFactories.get( i ).getIcon( subSize );
			}
		} else {
			this.icons = new javax.swing.Icon[ 1 ];
			this.icons[ 0 ] = iconFactories.get( 0 ).getIcon( size );
		}
	}

	@Override
	protected void paintIcon( java.awt.Component c, java.awt.Graphics2D g2 ) {
		if( this.icons.length == 1 ) {
			this.icons[ 0 ].paintIcon( c, g2, 0, 0 );
		} else {
			int totalOffset = OFFSET * ( this.icons.length - 1 );
			int x = totalOffset;
			int y = 0;

			float alphaDelta = 0.1f;
			float alpha = 0.5f - ( ( this.icons.length - 1 ) * alphaDelta );
			java.awt.Composite prevComposite = g2.getComposite();
			for( javax.swing.Icon icon : this.icons ) {
				try {
					if( x > 0 ) {
						g2.setComposite( java.awt.AlphaComposite.getInstance( java.awt.AlphaComposite.SRC_OVER, alpha ) );
						//					g2.setPaint( c.getBackground() );
						//					g2.fillRect( x, y, icon.getIconWidth(), icon.getIconHeight() );
					}
					icon.paintIcon( c, g2, x, y );
					x -= OFFSET;
					y += OFFSET;
					alpha += alphaDelta;
				} finally {
					g2.setComposite( prevComposite );
				}
			}
		}
	}
}
