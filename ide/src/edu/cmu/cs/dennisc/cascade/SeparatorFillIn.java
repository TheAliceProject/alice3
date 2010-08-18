/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
package edu.cmu.cs.dennisc.cascade;

/**
 * @author Dennis Cosgrove
 */
public class SeparatorFillIn extends FillIn< Object > {
//	private edu.cmu.cs.dennisc.croquet.LineAxisPanel panel;
//	public SeparatorFillIn() {
//		this( null );
//	}
//	public SeparatorFillIn( String text ) {
//		if( text != null ) {
//			edu.cmu.cs.dennisc.croquet.Label label = new edu.cmu.cs.dennisc.croquet.Label( text );
//			label.setHorizontalAlignment( edu.cmu.cs.dennisc.croquet.HorizontalAlignment.LEADING );
//			this.panel = new edu.cmu.cs.dennisc.croquet.LineAxisPanel( label );
//		} else {
//			this.panel = null;
//		}
//	}
	
	private javax.swing.Icon icon;
	public SeparatorFillIn( final String text ) {
		
		if( text != null ) {
			final java.awt.Graphics g = edu.cmu.cs.dennisc.javax.swing.SwingUtilities.getGraphics();
			final java.awt.Font font = edu.cmu.cs.dennisc.java.awt.FontUtilities.deriveFont( g.getFont(), edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE );
			this.icon = new javax.swing.Icon() {
				public int getIconWidth() {
					java.awt.FontMetrics fm = g.getFontMetrics( font );
					return fm.stringWidth( text );
				}
				public int getIconHeight() {
					java.awt.FontMetrics fm = g.getFontMetrics( font );
					return fm.getMaxAscent() +  fm.getMaxDescent();
				}
				public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
					g.setFont( font );
					java.awt.FontMetrics fm = g.getFontMetrics( font );
				    int ascent = fm.getMaxAscent();
				    g.setColor( java.awt.Color.GRAY );
					g.drawString( text, x, y+ascent );
				}
			};
		}
	}
	public SeparatorFillIn() {
		this( null );
	}
	/*package-private*/ String getName() {
		return null;
	}
	/*package-private*/ javax.swing.Icon getIcon() {
		return this.icon;
	}
	
//	private edu.cmu.cs.dennisc.croquet.MenuSeparatorModel menuSeparatorModel;
//	public SeparatorFillIn( String name ) {
//		if( name != null ) {
//			this.menuSeparatorModel = new edu.cmu.cs.dennisc.croquet.MenuSeparatorModel( name );
//		} else {
//			this.menuSeparatorModel = null;
//		}
//	}
//	public SeparatorFillIn() {
//		this( null );
//	}
//	@Override
//	public edu.cmu.cs.dennisc.croquet.MenuSeparatorModel getCroquetModel() {
//		return this.menuSeparatorModel;
//	}
//	/*package-private*/ String getName() {
//		if( this.menuSeparatorModel != null ) {
//			return this.menuSeparatorModel.getName();
//		} else {
//			return null;
//		}
//	}
	@Override
	protected void addChildren() {
	}
	@Override
	public final Object getValue() {
		return null;
	}
	@Override
	public final Object getTransientValue() {
		return null;
	}
}
