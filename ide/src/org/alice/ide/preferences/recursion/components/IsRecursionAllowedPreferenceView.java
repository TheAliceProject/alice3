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
package org.alice.ide.preferences.recursion.components;

/**
 * @author Dennis Cosgrove
 */
public class IsRecursionAllowedPreferenceView extends org.lgna.croquet.components.BorderPanel {
	private static final int SPACING = 16;
	private static final int INDENT = 32;
	private static class RecursionAccessPanel extends org.lgna.croquet.components.PageAxisPanel {
		private final org.lgna.croquet.components.ImmutableTextField label;
		private final org.lgna.croquet.components.Button button;
		private final org.lgna.croquet.components.CheckBox checkBox = org.alice.ide.preferences.recursion.IsRecursionAllowedState.getInstance().createCheckBox();
		public RecursionAccessPanel( org.alice.ide.preferences.recursion.IsRecursionAllowedPreferenceDialogComposite composite ) {
			this.label = composite.getRecursiveButtonText().createImmutableTextField();
			this.label.changeFont( edu.cmu.cs.dennisc.java.awt.font.TextPosture.OBLIQUE );
			this.button = composite.getNext().getOperation().createButton();
			org.lgna.croquet.components.LineAxisPanel lineAxisPanel = new org.lgna.croquet.components.LineAxisPanel(
					label, button
			);
			this.addComponent( checkBox );
			this.addComponent( lineAxisPanel );
			this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8,INDENT,8,8 ) );
		}
		
		@Override
		protected javax.swing.JPanel createJPanel() {
			return new DefaultJPanel() {
				@Override
				public boolean contains( int x, int y ) {
					if( org.alice.ide.preferences.recursion.IsAccessToRecursionPreferenceAllowedState.getInstance().getValue() ) {
						return super.contains( x, y );
					} else {
						return false;
					}
				}
				@Override
				public void paint(java.awt.Graphics g) {
					super.paint(g);
					if( org.alice.ide.preferences.recursion.IsAccessToRecursionPreferenceAllowedState.getInstance().getValue() ) {
						//pass
					} else {
						java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
						java.awt.Paint prevPaint = g2.getPaint();
						try {
							g2.setPaint( org.lgna.croquet.components.PaintUtilities.getDisabledTexturePaint() );
							g2.fill( g2.getClipBounds() );
						} finally {
							g2.setPaint( prevPaint );
						}
						
					}
				}
			};
		}
		private org.lgna.croquet.State.ValueListener< Boolean > valueObserver = new org.lgna.croquet.State.ValueListener< Boolean >() {
			public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			}
			public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
				if( nextValue ) {
					//pass
				} else {
					org.alice.ide.preferences.recursion.IsRecursionAllowedState.getInstance().setValueTransactionlessly( false );
				}
				label.getAwtComponent().setEnabled( nextValue );
				button.getAwtComponent().setEnabled( nextValue );
				checkBox.getAwtComponent().setEnabled( nextValue );
				RecursionAccessPanel.this.repaint();
			}
		};
		@Override
		protected void handleDisplayable() {
			super.handleDisplayable();
			org.alice.ide.preferences.recursion.IsAccessToRecursionPreferenceAllowedState.getInstance().addAndInvokeValueListener( valueObserver );
		}
		@Override
		protected void handleUndisplayable() {
			org.alice.ide.preferences.recursion.IsAccessToRecursionPreferenceAllowedState.getInstance().removeValueListener( valueObserver );
			super.handleUndisplayable();
		}
	}

	public IsRecursionAllowedPreferenceView( org.alice.ide.preferences.recursion.IsRecursionAllowedPreferenceDialogComposite composite ) {
		super( composite );
		//todo
		org.alice.ide.browser.BrowserOperation browserOperation = new org.alice.ide.browser.BrowserOperation( java.util.UUID.fromString( "30e5e6e1-39ca-4c0f-a4a5-17e3f0e8212d" ) )  {
			@Override
			protected java.net.URL getUrl() {
				String path = "http://help.alice.org/recursion";
				try {
					return new java.net.URL( path );
				} catch( java.net.MalformedURLException murle ) {
					throw new RuntimeException( path, murle );
				}
			}
		};
		org.lgna.croquet.components.Hyperlink hyperlink = browserOperation.createHyperlink();
		hyperlink.setBorder( javax.swing.BorderFactory.createEmptyBorder( SPACING, INDENT, SPACING, 0 ) );
		
		org.lgna.croquet.components.CheckBox checkBox = org.alice.ide.preferences.recursion.IsAccessToRecursionPreferenceAllowedState.getInstance().createCheckBox(); 

		org.lgna.croquet.components.ImmutableTextArea descriptionLabel = composite.getDescriptionText().createImmutableTextArea();
		org.lgna.croquet.components.PageAxisPanel pageAxisPanel = new org.lgna.croquet.components.PageAxisPanel();
		pageAxisPanel.addComponent( descriptionLabel );
		pageAxisPanel.addComponent( hyperlink );
		pageAxisPanel.addComponent( checkBox );
		pageAxisPanel.addComponent( new RecursionAccessPanel( composite ) );
		pageAxisPanel.addComponent( org.lgna.croquet.components.BoxUtilities.createVerticalGlue() );
		
		this.addLineStartComponent( new org.lgna.croquet.components.Label( edu.cmu.cs.dennisc.javax.swing.IconUtilities.createImageIcon( IsRecursionAllowedPreferenceView.class.getResource( "images/AliceWithKeyAtDoor.png" ) ) ) );
		this.addCenterComponent( pageAxisPanel );

		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( 8,8,8,8 ) );
		this.setBackgroundColor( org.lgna.croquet.components.FolderTabbedPane.DEFAULT_BACKGROUND_COLOR );
	}
}
