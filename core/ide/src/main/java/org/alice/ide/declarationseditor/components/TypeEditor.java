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

package org.alice.ide.declarationseditor.components;

class DeclarationMenuIcon extends edu.cmu.cs.dennisc.javax.swing.icons.DropDownArrowIcon {
	private final org.alice.ide.common.TypeBorder border = org.alice.ide.common.TypeBorder.getSingletonForUserType();
	private final java.awt.Font typeFont;

	private final int PAD = 4;

	public DeclarationMenuIcon() {
		super( 10, java.awt.Color.DARK_GRAY );
		this.typeFont = new java.awt.Font( null, 0, 12 );
	}

	private static java.awt.geom.Rectangle2D getTextBounds( String text, java.awt.Font font ) {
		if( text != null ) {
			java.awt.Graphics g = edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.getGraphics();
			java.awt.FontMetrics fm;
			if( font != null ) {
				fm = g.getFontMetrics( font );
			} else {
				fm = g.getFontMetrics();
			}
			return fm.getStringBounds( text, g );
		} else {
			return new java.awt.geom.Rectangle2D.Float( 0, 0, 0, 0 );
		}
	}

	protected java.awt.Font getTypeFont() {
		return this.typeFont;
	}

	private String getTypeText() {
		return "    ";
	}

	private java.awt.geom.Rectangle2D getTypeTextBounds() {
		return getTextBounds( this.getTypeText(), this.getTypeFont() );
	}

	private int getBorderWidth() {
		java.awt.Insets insets = this.border.getBorderInsets( null );
		java.awt.geom.Rectangle2D typeTextBounds = this.getTypeTextBounds();
		return insets.left + insets.right + (int)typeTextBounds.getWidth() + PAD;
	}

	private int getBorderHeight() {
		java.awt.Insets insets = this.border.getBorderInsets( null );
		java.awt.geom.Rectangle2D bounds = this.getTypeTextBounds();
		return insets.top + insets.bottom + (int)bounds.getHeight();
	}

	@Override
	public int getIconWidth() {
		return super.getIconWidth() + this.getBorderWidth();
	}

	@Override
	public int getIconHeight() {
		return Math.max( super.getIconHeight(), this.getBorderHeight() );
	}

	@Override
	public void paintIcon( java.awt.Component c, java.awt.Graphics g, int x, int y ) {
		java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
		g2.setRenderingHint( java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON );
		java.awt.geom.AffineTransform prevTransform = g2.getTransform();

		int w = this.getBorderWidth();
		int h = this.getBorderHeight();

		org.alice.ide.common.TypeBorder.getSingletonForUserType().paintBorder( c, g, x, y, w, h );
		g.setColor( c.getForeground() );

		java.awt.Font prevFont = g.getFont();
		g.setFont( this.getTypeFont() );
		edu.cmu.cs.dennisc.java.awt.GraphicsUtilities.drawCenteredText( g, this.getTypeText(), x, y, w, h );

		g.setFont( prevFont );
		g2.setTransform( prevTransform );

		int superHeight = super.getIconHeight();
		int yOffset = ( h - superHeight ) / 2;
		super.paintIcon( c, g, x + w + PAD, y + yOffset );
	}
}

/**
 * @author Dennis Cosgrove
 */
public class TypeEditor extends org.lgna.croquet.views.BorderPanel {
	private final org.lgna.croquet.views.FolderTabbedPane<org.alice.ide.declarationseditor.DeclarationComposite<?, ?>> tabbedPane;
	private final org.lgna.croquet.views.AbstractPopupButton<?> startButton;

	public TypeEditor( org.alice.ide.declarationseditor.DeclarationsEditorComposite composite ) {
		super( composite );
		//		org.lgna.croquet.components.FlowPanel headerTrailingComponent = new org.lgna.croquet.components.FlowPanel(
		//				composite.getControlsComposite().getView(),
		//				org.lgna.croquet.components.BoxUtilities.createHorizontalSliver( 12 ),
		//				org.alice.ide.clipboard.Clipboard.SINGLETON.getDragComponent()
		//				);

		org.lgna.croquet.views.SwingComponentView<?> headerTrailingComponent = composite.getControlsComposite().getView();

		//		final boolean IS_RECYCLE_BIN_READY_FOR_PRIME_TIME = false;
		//		if( IS_RECYCLE_BIN_READY_FOR_PRIME_TIME ) {
		//			headerTrailingComponent.addComponent( new org.alice.ide.recyclebin.RecycleBinView() );
		//		}
		headerTrailingComponent.setBorder( javax.swing.BorderFactory.createEmptyBorder( 2, 2, 0, 2 ) );

		final boolean IS_CUSTOM_DRAWING_DESIRED = false;
		if( IS_CUSTOM_DRAWING_DESIRED ) {
			this.tabbedPane = new org.lgna.croquet.views.FolderTabbedPane<org.alice.ide.declarationseditor.DeclarationComposite<?, ?>>( composite.getTabState() ) {
				@Override
				protected TitlesPanel createTitlesPanel() {
					return new TitlesPanel() {
						@Override
						protected javax.swing.JPanel createJPanel() {
							return new JTitlesPanel() {
								@Override
								public void paint( java.awt.Graphics g ) {
									super.paint( g );
									g.setColor( java.awt.Color.RED );
									g.drawString( "possibilities abound", 100, 10 );
								}
							};
						}
					};
				}
			};
		} else {
			this.tabbedPane = composite.getTabState().createFolderTabbedPane();
		}
		this.tabbedPane.setHeaderTrailingComponent( headerTrailingComponent );
		this.startButton = composite.getDeclarationMenu().getPopupPrepModel().createPopupButton();

		this.startButton.setClobberIcon( new DeclarationMenuIcon() );
		this.addCenterComponent( tabbedPane );
		org.lgna.croquet.views.SwingComponentView<?> component;
		if( org.alice.ide.croquet.models.ui.preferences.IsEmphasizingClassesState.getInstance().getValue() ) {
			component = this.startButton;
		} else {
			component = null;
		}
		this.tabbedPane.setHeaderLeadingComponent( component );
	}

	public org.alice.ide.codedrop.CodePanelWithDropReceptor getCodeDropReceptorInFocus() {
		org.alice.ide.declarationseditor.DeclarationsEditorComposite composite = (org.alice.ide.declarationseditor.DeclarationsEditorComposite)this.getComposite();
		org.alice.ide.declarationseditor.DeclarationComposite<?, ?> item = composite.getTabState().getValue();
		if( item != null ) {
			org.lgna.croquet.views.SwingComponentView<?> component = this.tabbedPane.getMainComponentFor( item );
			if( component instanceof org.alice.ide.declarationseditor.code.components.CodeDeclarationView ) {
				return ( (org.alice.ide.declarationseditor.code.components.CodeDeclarationView)component ).getCodePanelWithDropReceptor();
			}
		}
		return null;
	}
}
