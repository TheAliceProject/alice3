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

package org.alice.ide.properties.uicontroller;

import java.awt.GridBagConstraints;
import java.awt.Insets;

import org.alice.ide.properties.adapter.PropertyAdapter;

import edu.cmu.cs.dennisc.croquet.GridBagPanel;
import edu.cmu.cs.dennisc.croquet.Label;
import edu.cmu.cs.dennisc.math.Point3;

public class PropertyAdapterUIController extends GridBagPanel
{
	private PropertyAdapterController valuePanel; 
	private Label nameLabel;
	
	public PropertyAdapterUIController(PropertyAdapter<?, ?> propertyAdapter)
	{
		super();
		this.nameLabel = new Label();
		if (propertyAdapter != null)
		{
			this.nameLabel.setText(propertyAdapter.getRepr());
		}
		else
		{
			this.nameLabel.setText("NO PROPERTY, NO NAME");
		}
		Label middleLabel = new Label(" = ");
		this.valuePanel = getValuePanelForPropertyAdapter(propertyAdapter);
		
		this.addComponent(this.nameLabel, new GridBagConstraints( 
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
		this.addComponent(middleLabel, new GridBagConstraints( 
				1, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.CENTER, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(2,2,2,2), //insets
				0, //ipadX
				0 ) //ipadY
		);
		this.addComponent(this.valuePanel.getPanel(), new GridBagConstraints( 
				2, //gridX
				0, //gridY
				1, //gridWidth
				1, //gridHeight
				0.0, //weightX
				0.0, //weightY
				GridBagConstraints.CENTER, //anchor 
				GridBagConstraints.NONE, //fill
				new Insets(2,2,2,2), //insets
				0, //ipadX
				0 ) //ipadY
		);
		
	}
	
	public void setPropertyAdapter(PropertyAdapter<?, ?> propertyAdapter)
	{
		if (propertyAdapter != null)
		{
			this.nameLabel.setText(propertyAdapter.getRepr());
		}
		else
		{
			this.nameLabel.setText("NO PROPERTY, NO NAME");
		}
		if (this.valuePanel.getPropertyType() == propertyAdapter.getPropertyType())
		{
			this.valuePanel.setPropertyAdapter(propertyAdapter);
		}
		else
		{
			PropertyAdapterController<?> newValuePanel = getValuePanelForPropertyAdapter(propertyAdapter);
			if (!(newValuePanel instanceof BlankPropertyController))
			{
				this.removeComponent(this.valuePanel.getPanel());
				this.valuePanel.setPropertyAdapter(null);
				this.valuePanel = newValuePanel;
				this.addComponent(this.valuePanel.getPanel(), new GridBagConstraints( 
						2, //gridX
						0, //gridY
						1, //gridWidth
						1, //gridHeight
						0.0, //weightX
						0.0, //weightY
						GridBagConstraints.CENTER, //anchor 
						GridBagConstraints.NONE, //fill
						new Insets(2,2,2,2), //insets
						0, //ipadX
						0 ) //ipadY
				);
			}
		}
	}
	
	public PropertyAdapterController getValueController()
	{
		return this.valuePanel;
	}
	
	public static PropertyAdapterController getValuePanelForPropertyAdapter(PropertyAdapter propertyAdapter)
	{
		Class<?> propertyType = propertyAdapter != null?  propertyAdapter.getPropertyType() : null;
		if (propertyType == null)
		{
			return new BlankPropertyController(propertyAdapter);
		}
		else if (edu.cmu.cs.dennisc.color.Color4f.class.isAssignableFrom(propertyType))
		{
			return new Color4fPropertyController(propertyAdapter);
		}
		else if (String.class.isAssignableFrom(propertyType))
		{
			return new StringPropertyController(propertyAdapter);
		}
		else if (Double.class.isAssignableFrom(propertyType))
		{
			return new DoublePropertyController(propertyAdapter);
		}
		else if (Point3.class.isAssignableFrom(propertyType))
		{
			return new Point3PropertyController(propertyAdapter);
		}
		else
		{
			return new BlankPropertyController(propertyAdapter);
		}
	}
	
}
