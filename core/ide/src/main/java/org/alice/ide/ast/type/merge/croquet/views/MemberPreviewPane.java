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
package org.alice.ide.ast.type.merge.croquet.views;

/**
 * @author Dennis Cosgrove
 */
public abstract class MemberPreviewPane<M extends org.lgna.project.ast.Member> extends org.lgna.croquet.views.MigPanel {
	private static java.awt.Paint createOmitOrReplacePaint() {
		int size = 24;
		int width = size;
		int height = size;
		java.awt.image.BufferedImage image = new java.awt.image.BufferedImage( width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB );
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)image.getGraphics();
		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_OFF );
		g2.setColor( new java.awt.Color( 191, 191, 191, 150 ) );
		g2.fillRect( 0, 0, width, height );
		g2.setColor( new java.awt.Color( 63, 0, 0, 63 ) );
		g2.drawLine( 0, height, width, 0 );
		g2.drawLine( 0, 0, width, height );
		g2.dispose();
		return new java.awt.TexturePaint( image, new java.awt.Rectangle( 0, 0, width, height ) );
	}

	private static final java.awt.Paint OMIT_OR_REPLACE_PAINT = createOmitOrReplacePaint();

	public static org.lgna.croquet.views.AwtComponentView<?> createView( org.alice.ide.ast.type.merge.croquet.MemberHub<?> memberHub, boolean isAlphaDesiredWhenSelectionIsRequired ) {
		org.lgna.project.ast.Declaration member = memberHub.getMember();
		if( member instanceof org.lgna.project.ast.UserMethod ) {
			return new MethodPreviewPane( (org.alice.ide.ast.type.merge.croquet.MemberHub<org.lgna.project.ast.UserMethod>)memberHub, isAlphaDesiredWhenSelectionIsRequired );
		} else if( member instanceof org.lgna.project.ast.UserField ) {
			return new FieldPreviewPane( (org.alice.ide.ast.type.merge.croquet.MemberHub<org.lgna.project.ast.UserField>)memberHub, isAlphaDesiredWhenSelectionIsRequired );
		} else {
			return new org.lgna.croquet.views.Label( "todo" );
		}
	}

	private final org.alice.ide.ast.type.merge.croquet.MemberHub<M> memberHub;
	private final boolean isAlphaDesiredWhenSelectionIsRequired;

	public MemberPreviewPane( org.alice.ide.ast.type.merge.croquet.MemberHub<M> memberHub, boolean isAlphaDesiredWhenSelectionIsRequired ) {
		this.memberHub = memberHub;
		this.isAlphaDesiredWhenSelectionIsRequired = isAlphaDesiredWhenSelectionIsRequired;
	}

	@Override
	protected javax.swing.JPanel createJPanel() {
		return new DefaultJPanel() {
			@Override
			public void paint( java.awt.Graphics g ) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
				if( memberHub.getActionStatus() == org.alice.ide.ast.type.merge.croquet.ActionStatus.SELECTION_REQUIRED ) {
					if( isAlphaDesiredWhenSelectionIsRequired ) {
						java.awt.Composite prevComposite = g2.getComposite();
						g2.setComposite( java.awt.AlphaComposite.getInstance( java.awt.AlphaComposite.SRC_OVER, 0.4f ) );
						super.paint( g );
						g2.setComposite( prevComposite );
					} else {
						super.paint( g );
					}
				} else {
					super.paint( g );
					if( memberHub.getIsDesiredState().getValue() ) {
						//pass
					} else {
						g2.setPaint( OMIT_OR_REPLACE_PAINT );
						g2.fillRect( 0, 0, this.getWidth(), this.getHeight() );
					}
				}
			}
		};
	}
}
