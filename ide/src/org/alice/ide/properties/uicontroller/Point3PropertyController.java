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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.alice.ide.properties.adapter.PropertyAdapter;
import org.alice.ide.properties.adapter.SetValueOperation;

import edu.cmu.cs.dennisc.croquet.BorderPanel;
import edu.cmu.cs.dennisc.croquet.FlowPanel;
import edu.cmu.cs.dennisc.croquet.Label;
import edu.cmu.cs.dennisc.croquet.Panel;
import edu.cmu.cs.dennisc.croquet.SwingAdapter;
import edu.cmu.cs.dennisc.croquet.BorderPanel.Constraint;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.print.PrintUtilities;

public class Point3PropertyController extends AbstractAdapterController<Point3>
{	
	private ActionListener valueChangeListener;
	
	private BorderPanel mainPanel;
	
	private Label xLabel;
	private Label yLabel;
	private Label zLabel;
	private Label endLabel;
	
	private DoubleTextField xField;
	private DoubleTextField yField;
	private DoubleTextField zField;

	private boolean doUpdateOnAdapter = true;
	
	public Point3PropertyController(PropertyAdapter<Point3, ?> propertyAdapter)
	{
		super(propertyAdapter);
	}
	
	@Override
	protected void initializeComponents() 
	{
		this.mainPanel = new BorderPanel();
		
		this.valueChangeListener = new ActionListener() {
			
			public void actionPerformed(ActionEvent e) 
			{
				Point3PropertyController.this.updateAdapterFromUI(e);
				
			}
		};
		this.xLabel = new Label("( x:");
		this.yLabel = new Label(", y:");
		this.zLabel = new Label(", z:");
		this.endLabel = new Label(")");
		
		this.xField = new DoubleTextField(4);
		this.xField.addActionListener(this.valueChangeListener);
		
		this.yField = new DoubleTextField(4);
		this.yField.addActionListener(this.valueChangeListener);
		
		this.zField = new DoubleTextField(4);
		this.zField.addActionListener(this.valueChangeListener);
		
		FlowPanel uiPanel = new FlowPanel(FlowPanel.Alignment.LEFT);
		uiPanel.addComponent(this.xLabel);
		uiPanel.addComponent(new SwingAdapter(this.xField));
		uiPanel.addComponent(this.yLabel);
		uiPanel.addComponent(new SwingAdapter(this.yField));
		uiPanel.addComponent(this.zLabel);
		uiPanel.addComponent(new SwingAdapter(this.zField));
		uiPanel.addComponent(this.endLabel);
		
		this.mainPanel.addComponent(uiPanel, Constraint.CENTER);
		
	}
	
	@Override
	public Class<?> getPropertyType() 
	{
		return Point3.class;
	}
	
	
	
	@Override
	protected void setValueOnUI(Point3 point3Value)
	{
		this.doUpdateOnAdapter = false;
		if (point3Value != null)
		{
			this.xField.setValue(point3Value.x);
			this.yField.setValue(point3Value.y);
			this.zField.setValue(point3Value.z);
		}
		else
		{
			this.xField.setValue(null);
			this.yField.setValue(null);
			this.zField.setValue(null);
		}
		this.doUpdateOnAdapter = true;
	}
	
	private Point3 getPointFromUI()
	{
		double xVal = xField.getValue();
		double yVal = yField.getValue();
		double zVal = zField.getValue();
		if (Double.isNaN(xVal) || Double.isNaN(yVal) || Double.isNaN(zVal))
		{
			return null;
		}
		return new Point3(xVal, yVal, zVal);
	}
	
	
	protected void updateAdapterFromUI(ActionEvent e)
	{ 
		if (this.doUpdateOnAdapter)
		{
			Point3 newPoint = getPointFromUI();
			if (newPoint != null)
			{
				if (!newPoint.equals(this.propertyAdapter.getValue()))
				{
					if (this.propertyAdapter.getLastSetValue() == null || !this.propertyAdapter.getLastSetValue().equals(newPoint))
					{
						SetValueOperation<Point3> operation = this.propertyAdapter.getSetValueOperation(newPoint);
						operation.setName(newPoint.toString());
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
