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
package org.alice.ide.common;

import org.lgna.project.ast.ExpressionStatement;

/**
 * @author Dennis Cosgrove
 */
public class AddEventListenerStatementPanel extends StatementLikeSubstance {
	private final ExpressionStatement statement;

	public AddEventListenerStatementPanel( org.lgna.project.ast.ExpressionStatement statement ) {
		super(
				org.alice.ide.ast.draganddrop.statement.StatementDragModel.getInstance( statement ),
				org.lgna.project.ast.ExpressionStatement.class,
				javax.swing.BoxLayout.PAGE_AXIS );
		this.statement = statement;
		this.setPopupPrepModel( org.alice.ide.croquet.models.ast.StatementContextMenu.getInstance( statement ).getPopupPrepModel() );
	}

	@Override
	protected java.awt.Paint getBackgroundPaint( int x, int y, int width, int height ) {
		java.awt.Color color = org.alice.ide.ThemeUtilities.getActiveTheme().getEventColor();
		java.awt.Color colorA = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( color, 1.0, 1.0, 1.15 );
		java.awt.Color colorB = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB( color, 1.0, 0.9, 0.85 );
		return new java.awt.GradientPaint( x, y, colorA, x, y + 150, colorB );
	}

	@Override
	public boolean isMaximumSizeClampedToPreferredSize() {
		return false;
	}

	@Override
	protected void paintEpilogue( java.awt.Graphics2D g2, int x, int y, int width, int height ) {
		super.paintEpilogue( g2, x, y, width, height );
		if( this.statement.isEnabled.getValue() ) {
			//pass
		} else {
			g2.setPaint( org.lgna.croquet.views.PaintUtilities.getDisabledTexturePaint() );
			this.fillBounds( g2 );
		}
	}
}
