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

import org.alice.ide.ast.type.merge.croquet.ActionStatus;
import org.alice.ide.ast.type.merge.croquet.MemberHub;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.MigPanel;
import org.lgna.project.ast.Declaration;
import org.lgna.project.ast.Member;
import org.lgna.project.ast.UserField;
import org.lgna.project.ast.UserMethod;

import javax.swing.JPanel;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

/**
 * @author Dennis Cosgrove
 */
public abstract class MemberPreviewPane<M extends Member> extends MigPanel {
	private static Paint createOmitOrReplacePaint() {
		int size = 24;
		int width = size;
		int height = size;
		BufferedImage image = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
		Graphics2D g2 = (Graphics2D)image.getGraphics();
		g2.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF );
		g2.setColor( new Color( 191, 191, 191, 150 ) );
		g2.fillRect( 0, 0, width, height );
		g2.setColor( new Color( 63, 0, 0, 63 ) );
		g2.drawLine( 0, height, width, 0 );
		g2.drawLine( 0, 0, width, height );
		g2.dispose();
		return new TexturePaint( image, new Rectangle( 0, 0, width, height ) );
	}

	private static final Paint OMIT_OR_REPLACE_PAINT = createOmitOrReplacePaint();

	public static AwtComponentView<?> createView( MemberHub<?> memberHub, boolean isAlphaDesiredWhenSelectionIsRequired ) {
		Declaration member = memberHub.getMember();
		if( member instanceof UserMethod ) {
			return new MethodPreviewPane( (MemberHub<UserMethod>)memberHub, isAlphaDesiredWhenSelectionIsRequired );
		} else if( member instanceof UserField ) {
			return new FieldPreviewPane( (MemberHub<UserField>)memberHub, isAlphaDesiredWhenSelectionIsRequired );
		} else {
			return new Label( "todo" );
		}
	}

	private final MemberHub<M> memberHub;
	private final boolean isAlphaDesiredWhenSelectionIsRequired;

	public MemberPreviewPane( MemberHub<M> memberHub, boolean isAlphaDesiredWhenSelectionIsRequired ) {
		this.memberHub = memberHub;
		this.isAlphaDesiredWhenSelectionIsRequired = isAlphaDesiredWhenSelectionIsRequired;
	}

	@Override
	protected JPanel createJPanel() {
		return new DefaultJPanel() {
			@Override
			public void paint( Graphics g ) {
				Graphics2D g2 = (Graphics2D)g;
				if( memberHub.getActionStatus() == ActionStatus.SELECTION_REQUIRED ) {
					if( isAlphaDesiredWhenSelectionIsRequired ) {
						Composite prevComposite = g2.getComposite();
						g2.setComposite( AlphaComposite.getInstance( AlphaComposite.SRC_OVER, 0.4f ) );
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
