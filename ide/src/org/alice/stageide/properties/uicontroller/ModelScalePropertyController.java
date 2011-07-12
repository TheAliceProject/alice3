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

package org.alice.stageide.properties.uicontroller;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.alice.apis.moveandturn.Model;
import org.alice.ide.properties.adapter.PropertyAdapter;
import org.alice.ide.properties.adapter.SetValueOperation;
import org.alice.ide.properties.uicontroller.AbstractAdapterController;
import org.alice.ide.properties.uicontroller.DoubleTextField;
import org.alice.stageide.properties.IsScaleLinkedState;
import org.alice.stageide.properties.LinkScaleButton;
import org.alice.stageide.utilities.BoundingBoxUtilities;
import org.lgna.croquet.components.BooleanStateButton;
import org.lgna.croquet.components.BoxUtilities;
import org.lgna.croquet.components.Button;
import org.lgna.croquet.components.Label;
import org.lgna.croquet.components.Panel;
import org.lgna.croquet.components.SwingAdapter;

import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Matrix3x3;
import edu.cmu.cs.dennisc.math.ScaleUtilities;
import edu.cmu.cs.dennisc.math.Vector3;

public class ModelScalePropertyController extends AbstractAdapterController<Matrix3x3>
{
	
	private ActionListener valueChangeListener;
	
	private DoubleTextField widthField;
	private DoubleTextField heightField;
	private DoubleTextField depthField;
	
	private Label widthLabel;
	private Label heightLabel;
	private Label depthLabel;
	
	private Button resetButton;
	
	private BooleanStateButton<javax.swing.AbstractButton> linkButton;
	
	private boolean doUpdateOnAdapter = true;
	
	
	public ModelScalePropertyController(PropertyAdapter<Matrix3x3, Model> propertyAdapter) 
	{
		super(propertyAdapter);
	}
	
	
	@Override
	public Class<?> getPropertyType() 
	{
		return org.alice.apis.moveandturn.Model.class;
	}

	@Override
	protected void initializeComponents() 
	{
		super.initializeComponents();
		this.valueChangeListener = new ActionListener() {
			
			public void actionPerformed(ActionEvent e) 
			{
				ModelScalePropertyController.this.updateAdapterFromUI(e);
				
			}
		};
		
		this.widthLabel = new Label("Width:");
		this.heightLabel = new Label("Height:");
		this.depthLabel = new Label("Depth:");
		
		this.widthField = new DoubleTextField(3);
		this.widthField.addActionListener(this.valueChangeListener);
		
		this.heightField = new DoubleTextField(3);
		this.heightField.addActionListener(this.valueChangeListener);
		
		this.depthField = new DoubleTextField(3);
		this.depthField.addActionListener(this.valueChangeListener);
	
		this.linkButton = new LinkScaleButton(IsScaleLinkedState.getInstance());
		
		this.mainPanel.addComponent( this.widthLabel, new GridBagConstraints( 
			0, //gridX
			0, //gridY
			1, //gridWidth
			1, //gridHeight
			0.0, //weightX
			0.0, //weightY
			GridBagConstraints.EAST, //anchor 
			GridBagConstraints.NONE, //fill
			new Insets(2,2,2,2), //insets
			0, //ipadX
			0 ) //ipadY
		);
		this.mainPanel.addComponent( new SwingAdapter(this.widthField), new GridBagConstraints( 
			1, //gridX
			0, //gridY
			1, //gridWidth
			1, //gridHeight
			0.0, //weightX
			0.0, //weightY
			GridBagConstraints.WEST, //anchor 
			GridBagConstraints.NONE, //fill
			new Insets(2,2,2,2), //insets
			0, //ipadX
			0 ) //ipadY
		);
		this.mainPanel.addComponent( this.heightLabel, new GridBagConstraints( 
			0, //gridX
			1, //gridY
			1, //gridWidth
			1, //gridHeight
			0.0, //weightX
			0.0, //weightY
			GridBagConstraints.EAST, //anchor 
			GridBagConstraints.NONE, //fill
			new Insets(2,2,2,2), //insets
			0, //ipadX
			0 ) //ipadY
		);
		this.mainPanel.addComponent( new SwingAdapter(this.heightField), new GridBagConstraints( 
			1, //gridX
			1, //gridY
			1, //gridWidth
			1, //gridHeight
			0.0, //weightX
			0.0, //weightY
			GridBagConstraints.WEST, //anchor 
			GridBagConstraints.NONE, //fill
			new Insets(2,2,2,2), //insets
			0, //ipadX
			0 ) //ipadY
		);
		this.mainPanel.addComponent( this.depthLabel, new GridBagConstraints( 
			0, //gridX
			2, //gridY
			1, //gridWidth
			1, //gridHeight
			0.0, //weightX
			0.0, //weightY
			GridBagConstraints.EAST, //anchor 
			GridBagConstraints.NONE, //fill
			new Insets(2,2,2,2), //insets
			0, //ipadX
			0 ) //ipadY
		);
		this.mainPanel.addComponent( new SwingAdapter(this.depthField), new GridBagConstraints( 
			1, //gridX
			2, //gridY
			1, //gridWidth
			1, //gridHeight
			0.0, //weightX
			0.0, //weightY
			GridBagConstraints.WEST, //anchor 
			GridBagConstraints.NONE, //fill
			new Insets(2,2,2,2), //insets
			0, //ipadX
			0 ) //ipadY
		);
		this.mainPanel.addComponent( this.linkButton, new GridBagConstraints( 
			2, //gridX
			0, //gridY
			1, //gridWidth
			3, //gridHeight
			0.0, //weightX
			0.0, //weightY
			GridBagConstraints.WEST, //anchor 
			GridBagConstraints.NONE, //fill
			new Insets(2,2,2,2), //insets
			0, //ipadX
			0 ) //ipadY
		);
		this.mainPanel.addComponent( BoxUtilities.createHorizontalGlue(), new GridBagConstraints( 
            4, //gridX
            0, //gridY
            1, //gridWidth
            3, //gridHeight
            1.0, //weightX
            1.0, //weightY
            GridBagConstraints.CENTER, //anchor 
            GridBagConstraints.BOTH, //fill
            new Insets(0,0,0,0), //insets
            0, //ipadX
            0 ) //ipadY
        );
		
		
	}
	
	@Override
	protected void updateUIFromNewAdapter() 
	{
		super.updateUIFromNewAdapter();
		setResetButton();
	}

	private void setResetButton()
	{
		if (this.resetButton != null && this.resetButton.getAwtComponent().getParent() != null)
		{
			this.mainPanel.removeComponent(this.resetButton);
		}
		if (this.propertyAdapter != null)
		{
		    Matrix3x3 originalScale = Matrix3x3.createIdentity();
		    if (this.propertyAdapter.getInstance() instanceof Model)
		    {
		        originalScale = ((Model)this.propertyAdapter.getInstance()).getOriginalScale();
		    }
			SetValueOperation<Matrix3x3> setScale = this.propertyAdapter.getSetValueOperation(originalScale);
			setScale.setName("Reset");
			this.resetButton = setScale.createButton();
			
		}
		this.mainPanel.addComponent( this.resetButton, new GridBagConstraints( 
				3, //gridX
				0, //gridY
				1, //gridWidth
				3, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.WEST, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(2,2,2,2), //insets
				0, //ipadX
				0 ) //ipadY
		);
	}
	
	private AxisAlignedBox getUnscaledBBox()
	{
		Model baseModel = (Model)this.propertyAdapter.getInstance();
		AxisAlignedBox bbox = BoundingBoxUtilities.getTransformableUnscaledBBox(baseModel);
		return bbox;
	}
	
	@Override
	protected void setValueOnUI(Matrix3x3 value) 
	{
		if (value != null)
		{
			Vector3 scaleVector = ScaleUtilities.newScaleVector3(value);
			AxisAlignedBox bbox = this.getUnscaledBBox();
			if (bbox != null)
			{
				this.doUpdateOnAdapter = false;
				double width = bbox.getWidth() * scaleVector.x;
				double height = bbox.getHeight() * scaleVector.y;
				double depth = bbox.getDepth() * scaleVector.z;
				this.widthField.setValue(width);
				this.heightField.setValue(height);
				this.depthField.setValue(depth);
				this.doUpdateOnAdapter = true;
				return;
			}
		}
		//If we haven't set the scale value, set it to null
		this.widthField.setValue(null);
		this.heightField.setValue(null);
		this.depthField.setValue(null);
	}
	
	private Matrix3x3 getScaleFromUI(Object source)
	{
		double desiredWidth = widthField.getValue();
		double desiredHeight = heightField.getValue();
		double desiredDepth = depthField.getValue();
		if (Double.isNaN(desiredWidth) || Double.isNaN(desiredHeight) || Double.isNaN(desiredDepth))
		{
			return null;
		}
		AxisAlignedBox bbox = this.getUnscaledBBox();
		if (bbox == null)
		{
			return null;
		}
		
		Matrix3x3 currentScale = this.propertyAdapter.getValue();
		double currentXScale = currentScale.right.x;
		double currentYScale = currentScale.up.y;
		double currentZScale = currentScale.backward.z;
		
		double xScale = desiredWidth / bbox.getWidth();
		double yScale = desiredHeight / bbox.getHeight();
		double zScale = desiredDepth / bbox.getDepth();
		if (source != null && IsScaleLinkedState.getInstance().getValue())
		{
			if (source == widthField)
			{
				double relativeXScale = xScale / currentXScale;
				yScale = relativeXScale * currentYScale;
				zScale = relativeXScale * currentZScale;
			}
			else if (source == heightField)
			{
				double relativeYScale = yScale / currentYScale;
				xScale = relativeYScale * currentXScale;
				zScale = relativeYScale * currentZScale;
			}
			else if (source == depthField)
			{
				double relativeZScale = zScale / currentZScale;
				xScale = relativeZScale * currentXScale;
				yScale = relativeZScale * currentYScale;
			}
			
		}
		Vector3 scaleVector = new Vector3(xScale, yScale, zScale);
		Matrix3x3 newScale = ScaleUtilities.newScaleMatrix3d(scaleVector);
		return newScale;
	}
	
	protected void updateAdapterFromUI(ActionEvent e)
	{ 
		if (this.doUpdateOnAdapter)
		{
			Matrix3x3 newScale = getScaleFromUI(e.getSource());
			if (newScale != null)
			{
				if (!newScale.equals(this.propertyAdapter.getValue()))
				{
					if (this.propertyAdapter.getLastSetValue() == null || !this.propertyAdapter.getLastSetValue().equals(newScale))
					{
						SetValueOperation<Matrix3x3> operation = this.propertyAdapter.getSetValueOperation(newScale);
						operation.setName(newScale.toString());
						operation.fire(e);
					}
				}
			}
		}
	}


	@Override
	public Panel getPanel() 
	{
		return this.mainPanel;
	}
}
