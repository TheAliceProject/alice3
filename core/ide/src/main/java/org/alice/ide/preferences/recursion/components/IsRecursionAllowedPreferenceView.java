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
package org.alice.ide.preferences.recursion.components;

import edu.cmu.cs.dennisc.java.awt.font.TextPosture;
import edu.cmu.cs.dennisc.javax.swing.IconUtilities;
import org.alice.ide.browser.ImmutableBrowserOperation;
import org.alice.ide.help.HelpBrowserOperation;
import org.alice.ide.preferences.recursion.IsAccessToRecursionPreferenceAllowedState;
import org.alice.ide.preferences.recursion.IsRecursionAllowedPreferenceDialogComposite;
import org.alice.ide.preferences.recursion.IsRecursionAllowedState;
import org.lgna.croquet.Operation;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.views.AbstractLabel;
import org.lgna.croquet.views.BorderPanel;
import org.lgna.croquet.views.BoxUtilities;
import org.lgna.croquet.views.Button;
import org.lgna.croquet.views.CheckBox;
import org.lgna.croquet.views.FolderTabbedPane;
import org.lgna.croquet.views.Hyperlink;
import org.lgna.croquet.views.ImmutableTextArea;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.LineAxisPanel;
import org.lgna.croquet.views.PageAxisPanel;
import org.lgna.croquet.views.PaintUtilities;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public class IsRecursionAllowedPreferenceView extends BorderPanel {
	private static final int SPACING = 16;
	private static final int INDENT = 32;

	private static class RecursionAccessPanel extends PageAxisPanel {
		private final AbstractLabel label;
		private final Button button;
		private final CheckBox checkBox = IsRecursionAllowedState.getInstance().createCheckBox();

		public RecursionAccessPanel( IsRecursionAllowedPreferenceDialogComposite composite ) {
			this.label = composite.getRecursiveButtonText().createLabel( TextPosture.OBLIQUE );
			this.button = composite.getNext().createButton();
			LineAxisPanel lineAxisPanel = new LineAxisPanel(
					this.label, this.button
					);
			this.addComponent( checkBox );
			this.addComponent( lineAxisPanel );
			this.setBorder( BorderFactory.createEmptyBorder( 8, INDENT, 8, 8 ) );
		}

		@Override
		protected JPanel createJPanel() {
			return new DefaultJPanel() {
				@Override
				public boolean contains( int x, int y ) {
					if( IsAccessToRecursionPreferenceAllowedState.getInstance().getValue() ) {
						return super.contains( x, y );
					} else {
						return false;
					}
				}

				@Override
				public void paint( Graphics g ) {
					super.paint( g );
					if( IsAccessToRecursionPreferenceAllowedState.getInstance().getValue() ) {
						//pass
					} else {
						Graphics2D g2 = (Graphics2D)g;
						Paint prevPaint = g2.getPaint();
						try {
							g2.setPaint( PaintUtilities.getDisabledTexturePaint() );
							g2.fill( g2.getClipBounds() );
						} finally {
							g2.setPaint( prevPaint );
						}

					}
				}
			};
		}

		private ValueListener<Boolean> valueObserver = new ValueListener<Boolean>() {
			@Override
			public void valueChanged( ValueEvent<Boolean> e ) {
				Boolean nextValue = e.getNextValue();
				if( nextValue ) {
					//pass
				} else {
					IsRecursionAllowedState.getInstance().setValueTransactionlessly( false );
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
			IsAccessToRecursionPreferenceAllowedState.getInstance().addAndInvokeNewSchoolValueListener( valueObserver );
		}

		@Override
		protected void handleUndisplayable() {
			IsAccessToRecursionPreferenceAllowedState.getInstance().removeNewSchoolValueListener( valueObserver );
			super.handleUndisplayable();
		}
	}

	public IsRecursionAllowedPreferenceView( IsRecursionAllowedPreferenceDialogComposite composite ) {
		super( composite );
		//todo
		Operation browserOperation = new ImmutableBrowserOperation( UUID.fromString( "30e5e6e1-39ca-4c0f-a4a5-17e3f0e8212d" ), HelpBrowserOperation.HELP_URL_SPEC + "recursion" );
		Hyperlink hyperlink = browserOperation.createHyperlink();
		hyperlink.setBorder( BorderFactory.createEmptyBorder( SPACING, INDENT, SPACING, 0 ) );

		CheckBox checkBox = IsAccessToRecursionPreferenceAllowedState.getInstance().createCheckBox();

		ImmutableTextArea descriptionLabel = composite.getDescriptionText().createImmutableTextArea();
		PageAxisPanel pageAxisPanel = new PageAxisPanel();
		pageAxisPanel.addComponent( descriptionLabel );
		pageAxisPanel.addComponent( hyperlink );
		pageAxisPanel.addComponent( checkBox );
		pageAxisPanel.addComponent( new RecursionAccessPanel( composite ) );
		pageAxisPanel.addComponent( BoxUtilities.createVerticalGlue() );

		this.addLineStartComponent( new Label( IconUtilities.createImageIcon( IsRecursionAllowedPreferenceView.class.getResource( "images/AliceWithKeyAtDoor.png" ) ) ) );
		this.addCenterComponent( pageAxisPanel );

		this.setBorder( BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
		this.setBackgroundColor( FolderTabbedPane.DEFAULT_BACKGROUND_COLOR );
	}
}
