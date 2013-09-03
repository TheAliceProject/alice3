/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
 */
package org.alice.ide.ast.type.merge.views;

/**
 * @author Dennis Cosgrove
 */
@Deprecated
public class ActionRequiredView extends org.lgna.croquet.components.JComponent<javax.swing.JComponent> {
	private static javax.swing.Icon ICON =
			new edu.cmu.cs.dennisc.javax.swing.icons.ScaledIcon(
					edu.cmu.cs.dennisc.javax.swing.IconUtilities.getErrorIcon(),
					0.5f
			);

	private class JActionRequiredView extends javax.swing.JComponent {
		@Override
		protected void paintComponent( java.awt.Graphics g ) {
			super.paintComponent( g );

			int w = this.getWidth();
			int h = this.getHeight();

			int x0 = w / 2;
			int x1;
			int y0 = 0;
			int y1 = h - 1;

			if( isLeading ) {
				x1 = w - 1;
			} else {
				x1 = 0;
			}

			java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
			path.moveTo( x1, y0 );
			path.lineTo( x0, y0 );
			path.lineTo( x0, y1 );
			path.lineTo( x1, y1 );

			java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
			g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
			g2.draw( path );

			if( isActionRequiredCriterion.accept( null ) ) {
				ICON.paintIcon( this, g2, 0, ( h - ICON.getIconHeight() ) / 2 );
			}
		}
	}

	private final edu.cmu.cs.dennisc.pattern.Criterion<Void> isActionRequiredCriterion;
	private final boolean isLeading;

	public ActionRequiredView( edu.cmu.cs.dennisc.pattern.Criterion<Void> isActionRequiredCriterion, boolean isLeading ) {
		this.isActionRequiredCriterion = isActionRequiredCriterion;
		this.isLeading = isLeading;
	}

	@Override
	protected javax.swing.JComponent createAwtComponent() {
		return new JActionRequiredView();
	}
}
