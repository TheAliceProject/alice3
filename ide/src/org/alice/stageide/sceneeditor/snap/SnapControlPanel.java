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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JSpinner;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.alice.stageide.properties.ModelOpacityAdapter;
import org.alice.stageide.properties.ModelScaleAdapter;
import org.alice.stageide.properties.TransformableTranslationAdapter;
import org.alice.stageide.properties.TransformableVehicleAdapter;
import org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor;
import org.lgna.croquet.BooleanState;
import org.lgna.croquet.components.GridBagPanel;
import org.lgna.croquet.components.Label;
import org.lgna.croquet.components.LineAxisPanel;
import org.lgna.croquet.components.SwingAdapter;
import org.lgna.croquet.components.ToolPalette;

import edu.cmu.cs.dennisc.math.AngleInDegrees;

public class SnapControlPanel extends GridBagPanel implements ChangeListener, ActionListener
{
	private SnapState snapState;
	private MoveAndTurnSceneEditor sceneEditor;
	
	private JSpinner gridSizeSpinner;
	private Label gridSizeLabel;
	private SpinnerNumberModel gridSizeModel;
	
	private JSpinner snapAngleSpinner;
	private Label snapAngleLabel;
	private SpinnerListModel snapAngleModel;
	
	private GridBagPanel detailsPanel;
	private LineAxisPanel snapToGridPanel;
	private LineAxisPanel rotationSnapPanel;
	private ToolPalette detailsPalette;
	
	private boolean isInitializing = false;
	
	private org.lgna.croquet.State.ValueObserver< Boolean > snapStateValueObserver = new org.lgna.croquet.State.ValueObserver< Boolean >() {
		public void changing( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
		}
		public void changed( org.lgna.croquet.State< Boolean > state, Boolean prevValue, Boolean nextValue, boolean isAdjusting ) {
			SnapControlPanel.this.updateUIFromSnapState();
		}
	};
	
	public SnapControlPanel(SnapState snapState, MoveAndTurnSceneEditor sceneEditor)
	{
		this.sceneEditor = sceneEditor;
		this.snapState = snapState;
		this.detailsPanel = new GridBagPanel();
		this.detailsPalette = AreSnapDetailsExpandedState.getInstance().createToolPalette(detailsPanel);
		createUI();
		initializeUI();
		this.snapState.getIsSnapEnabledState().addAndInvokeValueObserver(this.snapStateValueObserver);
		updateUIFromSnapState();
	}
	
	@Override
    public void setBackgroundColor( java.awt.Color color )
    {
        super.setBackgroundColor(color);
        this.detailsPalette.setBackgroundColor(color);
    }
	
	private void createUI()
	{
	    Dimension spinnerSize = new Dimension(50, 26);
        this.gridSizeModel = new SpinnerNumberModel(this.snapState.getGridSpacing(), .01d, 10d, .05d);
        this.gridSizeSpinner = new JSpinner(this.gridSizeModel);
        this.gridSizeSpinner.setPreferredSize(spinnerSize);
        this.gridSizeSpinner.setMinimumSize(spinnerSize);
        this.gridSizeSpinner.setMaximumSize(spinnerSize);
        this.gridSizeSpinner.addChangeListener(this);
        this.gridSizeModel.addChangeListener(this);
        
        this.snapAngleModel = new SpinnerListModel(SnapState.ANGLE_SNAP_OPTIONS);
        this.snapAngleModel.setValue(SnapState.getAngleOptionForAngle((int)this.snapState.getRotationSnapAngle().getAsDegrees()));
        this.snapAngleSpinner = new JSpinner(this.snapAngleModel);
        this.snapAngleSpinner.addChangeListener(this);
        this.snapAngleSpinner.setPreferredSize(spinnerSize);
        this.snapAngleSpinner.setMinimumSize(spinnerSize);
        this.snapAngleSpinner.setMaximumSize(spinnerSize);
        
        java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( SnapControlPanel.class.getPackage().getName() + ".snap" );
        
        this.snapToGridPanel = new LineAxisPanel();
        snapToGridPanel.setBorder(null);
        snapToGridPanel.addComponent(this.snapState.getShowSnapGridState().createCheckBox());
        this.gridSizeLabel = new Label("   "+resourceBundle.getString("gridSpacing")+" ");
        snapToGridPanel.addComponent(this.gridSizeLabel);
        snapToGridPanel.addComponent(new SwingAdapter(this.gridSizeSpinner));
        
        this.rotationSnapPanel = new LineAxisPanel();
        rotationSnapPanel.setBorder(null);
        rotationSnapPanel.addComponent(this.snapState.getIsRotationSnapEnabledState().createCheckBox());
        this.snapAngleLabel = new Label("   "+resourceBundle.getString("angleSnap")+"  ");
        rotationSnapPanel.addComponent(this.snapAngleLabel);
        rotationSnapPanel.addComponent(new SwingAdapter(this.snapAngleSpinner));
	}
	
	protected void initializeUI()
	{
	    this.isInitializing = true;
		this.removeAllComponents();
		
		this.gridSizeModel.setValue(this.snapState.getGridSpacing());
		this.snapAngleModel.setValue(SnapState.getAngleOptionForAngle((int)this.snapState.getRotationSnapAngle().getAsDegrees()));
		
		java.util.ResourceBundle resourceBundle = java.util.ResourceBundle.getBundle( SnapControlPanel.class.getPackage().getName() + ".snap" );
		
		this.snapToGridPanel.removeAllComponents();
		this.snapToGridPanel.addComponent(this.snapState.getShowSnapGridState().createCheckBox());
		snapToGridPanel.addComponent(this.gridSizeLabel);
		snapToGridPanel.addComponent(new SwingAdapter(this.gridSizeSpinner));
		
		this.rotationSnapPanel.removeAllComponents();
		rotationSnapPanel.addComponent(this.snapState.getIsRotationSnapEnabledState().createCheckBox());
		rotationSnapPanel.addComponent(this.snapAngleLabel);
		rotationSnapPanel.addComponent(new SwingAdapter(this.snapAngleSpinner));

		detailsPanel.addComponent(snapToGridPanel , new GridBagConstraints( 
				0, //gridX
				1, //gridY
				2, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(0,10,0,0), //insets (top, left, bottom, right)
				0, //ipadX
				0 ) //ipadY
		);
		detailsPanel.addComponent(rotationSnapPanel , new GridBagConstraints( 
				0, //gridX
				2, //gridY
				2, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(0,10,0,0), //insets (top, left, bottom, right)
				0, //ipadX
				0 ) //ipadY
		);
		detailsPanel.addComponent(this.snapState.getIsSnapToGroundEnabledState().createCheckBox() , new GridBagConstraints( 
				0, //gridX
				3, //gridY
				2, //gridWidth
				1, //gridHeight
				1.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(0,10,0,0), //insets (top, left, bottom, right)
				0, //ipadX
				0 ) //ipadY
		);

		
		this.addComponent(this.snapState.getIsSnapEnabledState().createCheckBox() , new GridBagConstraints( 
                0, //gridX
                0, //gridY
                1, //gridWidth
                1, //gridHeight
                0.0, //weightX
                0.0, //weightY
                GridBagConstraints.WEST, //anchor 
                GridBagConstraints.NONE, //fill
                new Insets(0,0,0,16), //insets (top, left, bottom, right)
                0, //ipadX
                0 ) //ipadY
        );
		this.addComponent(detailsPalette , new GridBagConstraints( 
                1, //gridX
                0, //gridY
                1, //gridWidth
                1, //gridHeight
                1.0, //weightX
                0.0, //weightY
                GridBagConstraints.EAST, //anchor 
                GridBagConstraints.HORIZONTAL, //fill
                new Insets(0,0,0,0), //insets (top, left, bottom, right)
                0, //ipadX
                0 ) //ipadY
        );
		this.addComponent(detailsPalette.getMainComponent() , new GridBagConstraints( 
                0, //gridX
                1, //gridY
                2, //gridWidth
                1, //gridHeight
                1.0, //weightX
                0.0, //weightY
                GridBagConstraints.EAST, //anchor 
                GridBagConstraints.HORIZONTAL, //fill
                new Insets(0,0,0,0), //insets (top, left, bottom, right)
                0, //ipadX
                0 ) //ipadY
        );
		this.isInitializing = false;
	}
	
	protected void updateUIFromSnapState()
	{
		this.snapState.getIsSnapToGroundEnabledState().setEnabled(this.snapState.isSnapEnabled());
		this.snapState.getShowSnapGridState().setEnabled(this.snapState.isSnapEnabled());
		
		this.gridSizeModel.setValue(new Double(this.snapState.getGridSpacing()));
		this.snapState.getIsSnapToGridEnabledState().setEnabled(this.snapState.isSnapEnabled());
		this.gridSizeSpinner.setEnabled(this.snapState.isSnapEnabled());
		this.gridSizeLabel.getAwtComponent().setEnabled(this.snapState.isSnapEnabled());
		
		this.snapAngleModel.setValue(SnapState.getAngleOptionForAngle((int)this.snapState.getRotationSnapAngle().getAsDegrees()));
		this.snapState.getIsRotationSnapEnabledState().setEnabled(this.snapState.isSnapEnabled());
		this.snapAngleSpinner.setEnabled(this.snapState.isSnapEnabled());
		this.snapAngleLabel.getAwtComponent().setEnabled(this.snapState.isSnapEnabled());
	}
	
	protected void updateSnapStateFromUI()
	{
		this.snapState.setGridSpacing(this.gridSizeModel.getNumber().doubleValue());
		this.snapState.setRotationSnapAngle(new AngleInDegrees(((Integer)this.snapAngleModel.getValue()).doubleValue()));
		if (this.sceneEditor != null)
		{
			this.sceneEditor.setSnapGridSpacing(this.snapState.getGridSpacing());
		}
	}
	
	public void setSnapState(SnapState snapState)
	{
		if (this.snapState != null)
		{
			this.snapState.getIsSnapEnabledState().removeValueObserver(this.snapStateValueObserver);
		}
		this.snapState = snapState;
		initializeUI();
		this.snapState.getIsSnapEnabledState().addAndInvokeValueObserver(this.snapStateValueObserver);
		updateUIFromSnapState();
	}

	public void stateChanged(ChangeEvent e) {
	    if (!this.isInitializing)
	    {
	        updateSnapStateFromUI();
	        updateUIFromSnapState();
	    }
	}

	public void actionPerformed(ActionEvent e) {
	    if (!this.isInitializing)
        {
	        updateSnapStateFromUI();
	        updateUIFromSnapState();
        }
	}
	
}
