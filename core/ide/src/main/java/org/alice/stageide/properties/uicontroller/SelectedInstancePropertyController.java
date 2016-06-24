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

package org.alice.stageide.properties.uicontroller;

import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import org.alice.ide.properties.uicontroller.BasicPropertyController;
import org.alice.stageide.properties.SelectedInstanceAdapter;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.GridBagPanel;
import org.lgna.croquet.views.Label;

public class SelectedInstancePropertyController extends BasicPropertyController<org.alice.ide.instancefactory.InstanceFactory>
{

	private Label label;

	public SelectedInstancePropertyController( SelectedInstanceAdapter propertyAdapter )
	{
		super( propertyAdapter );
	}

	@Override
	protected AwtComponentView<?> createPropertyComponent()
	{
		this.label = new Label( "", 1.2f, edu.cmu.cs.dennisc.java.awt.font.TextWeight.BOLD )
		{

			@Override
			protected JLabel createAwtComponent()
			{
				return new javax.swing.JLabel() {
					@Override
					protected void paintComponent( java.awt.Graphics g ) {
						g.setColor( this.getBackground() );
						if( g instanceof Graphics2D )
						{
							( (Graphics2D)g ).setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
						}
						g.fillRoundRect( 0, 0, this.getWidth(), this.getHeight(), 12, 12 );
						super.paintComponent( g );
					}
				};
			}
		};
		this.label.setBackgroundColor( org.alice.ide.ThemeUtilities.getActiveTheme().getSelectedColor() );
		this.label.setBorder( BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );

		AwtComponentView itemSelector = new org.alice.ide.croquet.components.InstanceFactoryPopupButton( org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getInstanceFactoryState() );
		GridBagPanel componentPanel = new GridBagPanel();
		int xIndex = 0;
		componentPanel.addComponent( itemSelector, new GridBagConstraints(
				xIndex++, // gridX
				0, // gridY
				1, // gridWidth
				1, // gridHeight
				0.0, // weightX
				0.0, // weightY
				GridBagConstraints.WEST, // anchor
				GridBagConstraints.HORIZONTAL, // fill
				new Insets( 0, 0, 0, 0 ), // insets (top, left, bottom, right)
				0, // ipadX
				0 ) // ipadY
		);

		org.alice.ide.instancefactory.InstanceFactory instanceFactory = org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getInstanceFactoryState().getValue();
		org.lgna.croquet.views.SwingComponentView<?> oneShotComponent = org.alice.stageide.oneshot.OneShotMenuModel.getInstance( instanceFactory ).getPopupPrepModel().createPopupButton();
		componentPanel.addComponent( oneShotComponent, new GridBagConstraints(
				xIndex++, // gridX
				0, // gridY
				0, // gridWidth
				1, // gridHeight
				0.0, // weightX
				0.0, // weightY
				GridBagConstraints.WEST, // anchor
				GridBagConstraints.HORIZONTAL, // fill
				new Insets( 0, 0, 0, 0 ), // insets (top, left, bottom, right)
				0, // ipadX
				0 ) // ipadY
		);

		return componentPanel;
	}

	@Override
	public Class<?> getPropertyType()
	{
		return org.alice.ide.instancefactory.InstanceFactory.class;
	}

	@Override
	protected void setValueOnUI( org.alice.ide.instancefactory.InstanceFactory value )
	{
		edu.cmu.cs.dennisc.java.util.logging.Logger.warning( "NOT SUPPORTED", this, value );

	}

}
