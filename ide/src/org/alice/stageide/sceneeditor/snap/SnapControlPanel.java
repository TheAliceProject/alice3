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

package org.alice.stageide.sceneeditor.snap;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.lgna.croquet.components.GridBagPanel;
import org.lgna.croquet.components.Label;
import org.lgna.croquet.components.LineAxisPanel;
import org.lgna.croquet.components.Spinner;
import org.lgna.croquet.components.ToolPalette;

public class SnapControlPanel extends GridBagPanel implements ChangeListener, ActionListener
{
	private Spinner gridSizeSpinner;
	private Label gridSizeLabel;

	private Spinner snapAngleSpinner;
	private Label snapAngleLabel;

	private GridBagPanel detailsPanel;
	private LineAxisPanel snapToGridPanel;
	private LineAxisPanel rotationSnapPanel;
	private ToolPalette detailsPalette;

	private boolean isInitializing = false;

	private org.lgna.croquet.State.ValueListener<Boolean> snapStateValueObserver = new org.lgna.croquet.State.ValueListener<Boolean>() {
		public void changing( org.lgna.croquet.State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}

		public void changed( org.lgna.croquet.State<Boolean> state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			SnapControlPanel.this.updateUIFromSnapState();
		}
	};

	public SnapControlPanel()
	{
		this.detailsPanel = new GridBagPanel();
		this.detailsPalette = AreSnapDetailsExpandedState.getInstance().createToolPalette( detailsPanel );
		initializeUI();
		SnapState.getInstance().getIsSnapEnabledState().addAndInvokeValueListener( this.snapStateValueObserver );
		updateUIFromSnapState();
	}

	@Override
	public void setBackgroundColor( java.awt.Color color )
	{
		super.setBackgroundColor( color );
		this.detailsPalette.setBackgroundColor( color );
	}

	protected void initializeUI()
	{
		this.isInitializing = true;
		this.removeAllComponents();
		java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( SnapControlPanel.class.getPackage().getName() + ".snap" );
		Dimension spinnerSize = new Dimension( 50, 26 );

		this.snapToGridPanel = new LineAxisPanel();
		snapToGridPanel.setBorder( null );
		snapToGridPanel.addComponent( SnapState.getInstance().getShowSnapGridState().createCheckBox() );
		this.gridSizeLabel = new Label( "   " + resourceBundle.getString( "gridSpacing" ) + " " );
		snapToGridPanel.addComponent( this.gridSizeLabel );
		this.gridSizeSpinner = SnapState.getInstance().getSnapGridSpacingState().createSpinner();
		this.gridSizeSpinner.getAwtComponent().setPreferredSize( spinnerSize );
		this.gridSizeSpinner.getAwtComponent().setMinimumSize( spinnerSize );
		this.gridSizeSpinner.getAwtComponent().setMaximumSize( spinnerSize );
		snapToGridPanel.addComponent( this.gridSizeSpinner );

		this.rotationSnapPanel = new LineAxisPanel();
		rotationSnapPanel.setBorder( null );
		rotationSnapPanel.addComponent( SnapState.getInstance().getIsRotationSnapEnabledState().createCheckBox() );
		this.snapAngleLabel = new Label( "   " + resourceBundle.getString( "angleSnap" ) + "  " );
		rotationSnapPanel.addComponent( this.snapAngleLabel );
		this.snapAngleSpinner = SnapState.getInstance().getSnapAngleInDegreesState().createSpinner();
		this.snapAngleSpinner.getAwtComponent().setPreferredSize( spinnerSize );
		this.snapAngleSpinner.getAwtComponent().setMinimumSize( spinnerSize );
		this.snapAngleSpinner.getAwtComponent().setMaximumSize( spinnerSize );
		rotationSnapPanel.addComponent( this.snapAngleSpinner );

		detailsPanel.addComponent( snapToGridPanel, new GridBagConstraints(
				0, //gridX
				1, //gridY
				2, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 0, 10, 0, 0 ), //insets (top, left, bottom, right)
				0, //ipadX
				0 ) //ipadY
		);
		detailsPanel.addComponent( rotationSnapPanel, new GridBagConstraints(
				0, //gridX
				2, //gridY
				2, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 0, 10, 0, 0 ), //insets (top, left, bottom, right)
				0, //ipadX
				0 ) //ipadY
		);
		detailsPanel.addComponent( SnapState.getInstance().getIsSnapToGroundEnabledState().createCheckBox(), new GridBagConstraints(
				0, //gridX
				3, //gridY
				2, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 0, 10, 0, 0 ), //insets (top, left, bottom, right)
				0, //ipadX
				0 ) //ipadY
		);

		this.addComponent( SnapState.getInstance().getIsSnapEnabledState().createCheckBox(), new GridBagConstraints(
				0, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets( 0, 0, 0, 16 ), //insets (top, left, bottom, right)
				0, //ipadX
				0 ) //ipadY
		);
		this.addComponent( detailsPalette, new GridBagConstraints(
				1, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.EAST, //anchor 
				GridBagConstraints.HORIZONTAL, //fill
				new Insets( 0, 0, 0, 0 ), //insets (top, left, bottom, right)
				0, //ipadX
				0 ) //ipadY
		);
		this.addComponent( detailsPalette.getMainComponent(), new GridBagConstraints(
				0, //gridX
				1, //gridY
				2, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.EAST, //anchor 
				GridBagConstraints.HORIZONTAL, //fill
				new Insets( 0, 0, 0, 0 ), //insets (top, left, bottom, right)
				0, //ipadX
				0 ) //ipadY
		);
		this.isInitializing = false;
	}

	protected void updateUIFromSnapState()
	{
		SnapState.getInstance().getIsSnapToGroundEnabledState().setEnabled( SnapState.getInstance().isSnapEnabled() );
		SnapState.getInstance().getShowSnapGridState().setEnabled( SnapState.getInstance().isSnapEnabled() );

		SnapState.getInstance().getIsSnapToGridEnabledState().setEnabled( SnapState.getInstance().isSnapEnabled() );
		this.gridSizeSpinner.getAwtComponent().setEnabled( SnapState.getInstance().isSnapEnabled() );
		this.gridSizeLabel.getAwtComponent().setEnabled( SnapState.getInstance().isSnapEnabled() );

		SnapState.getInstance().getIsRotationSnapEnabledState().setEnabled( SnapState.getInstance().isSnapEnabled() );
		this.snapAngleSpinner.getAwtComponent().setEnabled( SnapState.getInstance().isSnapEnabled() );
		this.snapAngleLabel.getAwtComponent().setEnabled( SnapState.getInstance().isSnapEnabled() );
	}

	public void setSnapState( SnapState snapState )
	{
		if( SnapState.getInstance() != null )
		{
			SnapState.getInstance().getIsSnapEnabledState().removeValueListener( this.snapStateValueObserver );
		}
		initializeUI();

		updateUIFromSnapState();
	}

	public void stateChanged( ChangeEvent e ) {
		if( !this.isInitializing )
		{
			updateUIFromSnapState();
		}
	}

	public void actionPerformed( ActionEvent e ) {
		if( !this.isInitializing )
		{
			updateUIFromSnapState();
		}
	}

}
