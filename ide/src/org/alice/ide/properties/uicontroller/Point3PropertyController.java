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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import org.alice.ide.properties.adapter.PropertyAdapter;
import org.alice.ide.properties.adapter.SetValueOperation;

import edu.cmu.cs.dennisc.croquet.FlowPanel;
import edu.cmu.cs.dennisc.croquet.Label;
import edu.cmu.cs.dennisc.croquet.SwingAdapter;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.print.PrintUtilities;

public class Point3PropertyController extends AbstractAdapterController<Point3>
{
	private class DoubleTextField extends JTextField
	{
		protected boolean isDirty = false;	
		protected double trueValue = Double.NaN;
		
		protected DoubleTextField(int columns)
		{
			super(columns);
			this.getDocument().addDocumentListener( new edu.cmu.cs.dennisc.javax.swing.event.SimplifiedDocumentAdapter() {
				@Override
				protected void updated( javax.swing.event.DocumentEvent e ) 
				{
					try
					{
						DoubleTextField.this.setForeground(Color.BLACK);
						Double.parseDouble(DoubleTextField.this.getText());
						
					}
					catch (Exception exception)
					{
						DoubleTextField.this.setForeground(Color.RED);
					}
					finally
					{
						DoubleTextField.this.isDirty = true;
					}
				}
			} );
		}
		
		protected double getValue()
		{
			if (this.isDirty)
			{
				try
				{
					double value = Double.parseDouble(this.getText());
					this.trueValue = value;
				}
				catch (Exception e)
				{
					this.trueValue = Double.NaN;
				}
			}
			this.isDirty = false;
			return this.trueValue;
		}
		
	}
	
	
	private ActionListener valueChangeListener;
	
	private Label point3Label;
	
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
		this.point3Label = new Label();
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
		
		this.addComponent(uiPanel, Constraint.CENTER);
		
	}
	
	@Override
	public Class<?> getPropertyType() 
	{
		return Point3.class;
	}
	
	private static java.text.NumberFormat format = new java.text.DecimalFormat( "0.00" );
	
	@Override
	protected void setValueOnUI(Point3 point3Value)
	{
		this.doUpdateOnAdapter = false;
		if (point3Value != null)
		{
			this.xField.trueValue = point3Value.x;
			this.yField.trueValue = point3Value.y;
			this.zField.trueValue = point3Value.z;
			this.xField.setText(format.format(point3Value.x));
			this.yField.setText(format.format(point3Value.y));
			this.zField.setText(format.format(point3Value.z));
		}
		else
		{
			this.xField.trueValue = Double.NaN;
			this.yField.trueValue = Double.NaN;
			this.zField.trueValue = Double.NaN;
			this.xField.setText("none");
			this.yField.setText("none");
			this.zField.setText("none");
		}
		this.xField.isDirty = false;
		this.yField.isDirty = false;
		this.zField.isDirty = false;
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
	protected void updateUIFromNewAdapter() 
	{
		if (this.propertyAdapter != null)
		{
			Point3 currentValue = this.propertyAdapter.getValue();
			this.setValueOnUI(currentValue);
		}
	}
}
