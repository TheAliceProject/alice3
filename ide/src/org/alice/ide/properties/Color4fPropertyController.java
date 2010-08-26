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

package org.alice.ide.properties;

import java.awt.Color;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.croquet.BorderPanel;
import edu.cmu.cs.dennisc.croquet.Label;
import edu.cmu.cs.dennisc.croquet.Panel;

public class Color4fPropertyController extends BorderPanel implements PropertyAdapterController
{
	private PropertyAdapter.ValueChangeObserver<edu.cmu.cs.dennisc.color.Color4f> colorChangeObserver = new PropertyAdapter.ValueChangeObserver<edu.cmu.cs.dennisc.color.Color4f>()
	{
		public void valueChanged(Color4f newValue) 
		{
			Color4fPropertyController.this.setColor(newValue);
		}
	};
	
	private PropertyAdapter<edu.cmu.cs.dennisc.color.Color4f, ?> propertyAdapter;
	private Label colorLabel;
	
	private static final String COLOR_STRING = "    COLOR    ";
	private static final String BLANK_STRING = "NO COLOR";
	
	public Color4fPropertyController(PropertyAdapter<edu.cmu.cs.dennisc.color.Color4f, ?> propertyAdapter)
	{
		super();
		this.colorLabel = new Label();
		this.colorLabel.getAwtComponent().setOpaque(true);
		this.addComponent(this.colorLabel, BorderPanel.Constraint.CENTER);
		this.setPropertyAdapter(propertyAdapter);
	}
	
	public void setPropertyAdapter(PropertyAdapter<?, ?> propertyAdapter)
	{
		if (this.propertyAdapter != null)
		{
			this.propertyAdapter.removeValueChangeObserver(this.colorChangeObserver);
		}
		if (propertyAdapter != null && edu.cmu.cs.dennisc.color.Color4f.class.isAssignableFrom(propertyAdapter.getPropertyType()))
		{
			this.propertyAdapter = (PropertyAdapter<edu.cmu.cs.dennisc.color.Color4f, ?>)propertyAdapter;
		}
		else
		{
			this.propertyAdapter = null;
		}
		if (this.propertyAdapter != null)
		{
			this.colorLabel.getAwtComponent().setOpaque(true);
			this.colorLabel.setText(COLOR_STRING);
			this.propertyAdapter.addAndInvokeValueChangeObserver(this.colorChangeObserver);
		}
		else
		{
			this.colorLabel.getAwtComponent().setOpaque(false);
			this.colorLabel.setText(BLANK_STRING);
			this.colorLabel.setForegroundColor(Color.BLACK);
		}
	}
	
	public Panel getPanel()
	{
		return this;
	}
	
	private void setColor(edu.cmu.cs.dennisc.color.Color4f color)
	{
		this.colorLabel.setForegroundColor(color.getAsAWTColor());
		this.colorLabel.setBackgroundColor(color.getAsAWTColor());
	}
}
