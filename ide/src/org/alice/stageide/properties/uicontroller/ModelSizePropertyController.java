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

import org.alice.ide.properties.adapter.AbstractPropertyAdapter;
import org.alice.ide.properties.adapter.SetValueOperation;
import org.alice.ide.properties.uicontroller.AbstractAdapterController;
import org.alice.ide.properties.uicontroller.DoubleTextField;
import org.alice.stageide.properties.IsScaleLinkedState;
import org.alice.stageide.properties.LinkScaleButton;
import org.alice.stageide.properties.ModelSizeAdapter;
import org.lgna.croquet.components.BooleanStateButton;
import org.lgna.croquet.components.BoxUtilities;
import org.lgna.croquet.components.Button;
import org.lgna.croquet.components.Label;
import org.lgna.croquet.components.Panel;
import org.lgna.croquet.components.SwingAdapter;
import org.lgna.story.implementation.ModelImp;

import edu.cmu.cs.dennisc.math.Dimension3;

public class ModelSizePropertyController extends AbstractAdapterController<Dimension3>
{
	
	protected class SetSizeOperation extends SetValueOperation<Dimension3> {
		public SetSizeOperation( AbstractPropertyAdapter <Dimension3, ?> propertyAdapter, Dimension3 value ) {
			super( propertyAdapter, value, null, java.util.UUID.fromString( "c742ea2e-cafe-41a0-9b76-38cb51921823" ) );
		}
	}
	
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
	
	
	public ModelSizePropertyController(ModelSizeAdapter propertyAdapter) 
	{
		super(propertyAdapter);
	}
	
	
	@Override
	public Class<?> getPropertyType() 
	{
		return org.lgna.story.Model.class;
	}

	@Override
	protected void initializeComponents() 
	{
		super.initializeComponents();
		this.valueChangeListener = new ActionListener() {
			
			public void actionPerformed(ActionEvent e) 
			{
				ModelSizePropertyController.this.updateAdapterFromUI(e);
				
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
		
		this.addComponent( this.widthLabel, new GridBagConstraints( 
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
		this.addComponent( new SwingAdapter(this.widthField), new GridBagConstraints( 
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
		this.addComponent( this.heightLabel, new GridBagConstraints( 
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
		this.addComponent( new SwingAdapter(this.heightField), new GridBagConstraints( 
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
		this.addComponent( this.depthLabel, new GridBagConstraints( 
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
		this.addComponent( new SwingAdapter(this.depthField), new GridBagConstraints( 
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
		this.addComponent( this.linkButton, new GridBagConstraints( 
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
		this.addComponent( BoxUtilities.createHorizontalGlue(), new GridBagConstraints( 
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
			this.removeComponent(this.resetButton);
		}
		if (this.propertyAdapter != null)
		{
			
			SetValueOperation<Dimension3> setSize = new SetSizeOperation(this.propertyAdapter, getOriginalSize());
			setSize.setName("Reset");
			this.resetButton = setSize.createButton();
			
		}
		if( this.resetButton != null ) {
			this.addComponent( this.resetButton, new GridBagConstraints( 
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
	}
	
	private Dimension3 getOriginalSize()
	{
		ModelImp baseModel = (ModelImp)this.propertyAdapter.getInstance();
		Dimension3 scale = baseModel.getScale();
		Dimension3 size = baseModel.getSize();
		return new Dimension3(size.x / scale.x, size.y / scale.y, size.z / scale.z);
	}
	
	private Dimension3 getSize()
	{
		ModelImp baseModel = (ModelImp)this.propertyAdapter.getInstance();
		return baseModel.getSize();
	}
	
	@Override
	protected void setValueOnUI(Dimension3 value) 
	{
		if (value != null)
		{
			this.doUpdateOnAdapter = false;
			this.widthField.setValue(value.x);
			this.heightField.setValue(value.y);
			this.depthField.setValue(value.x);
			this.doUpdateOnAdapter = true;
			return;
		}
		//If we haven't set the scale value, set it to null
		this.widthField.setValue(null);
		this.heightField.setValue(null);
		this.depthField.setValue(null);
	}
	
	private Dimension3 getSizeFromUI(Object source)
	{
		double desiredWidth = widthField.getValue();
		double desiredHeight = heightField.getValue();
		double desiredDepth = depthField.getValue();
		if (Double.isNaN(desiredWidth) || Double.isNaN(desiredHeight) || Double.isNaN(desiredDepth))
		{
			return null;
		}
		double width = desiredWidth;
		double height = desiredHeight;
		double depth = desiredDepth;
		if (source != null && IsScaleLinkedState.getInstance().getValue())
		{
			Dimension3 size = this.getSize();
			if (source == widthField)
			{
				double relativeXScale = width / size.x;
				height = relativeXScale * size.y;
				depth = relativeXScale * size.z;
			}
			else if (source == heightField)
			{
				double relativeYScale = height / size.y;
				width = relativeYScale * size.x;
				depth = relativeYScale * size.z;
			}
			else if (source == depthField)
			{
				double relativeZScale = depth / size.z;
				width = relativeZScale * size.x;
				height = relativeZScale * size.y;
			}
		}
		Dimension3 newSize = new Dimension3(width, height, depth);
		return newSize;
	}
	
	protected void updateAdapterFromUI(ActionEvent e)
	{ 
		if (this.doUpdateOnAdapter)
		{
			Dimension3 newScale = getSizeFromUI(e.getSource());
			if (newScale != null)
			{
				if (!newScale.equals(this.propertyAdapter.getValue()))
				{
					if (this.propertyAdapter.getLastSetValue() == null || !this.propertyAdapter.getLastSetValue().equals(newScale))
					{
						SetValueOperation<Dimension3> operation = new SetSizeOperation(this.propertyAdapter, newScale);
						operation.setName(newScale.toString());
						operation.fire(e);
					}
				}
			}
		}
	}
}
