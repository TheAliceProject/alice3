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

package org.alice.ide.properties.uicontroller;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.alice.ide.properties.adapter.AbstractPropertyAdapter;
import org.lgna.croquet.views.BoxUtilities;
import org.lgna.croquet.views.GridBagPanel;
import org.lgna.croquet.views.Label;
import org.lgna.croquet.views.SwingAdapter;

import edu.cmu.cs.dennisc.math.Point3;

public class Point3PropertyController extends AbstractAdapterController<Point3>
{

	private ActionListener valueChangeListener;

	private Label xLabel;
	private Label yLabel;
	private Label zLabel;
	private Label endLabel;

	private DoubleTextField xField;
	private DoubleTextField yField;
	private DoubleTextField zField;

	private boolean doUpdateOnAdapter = true;

	public Point3PropertyController( AbstractPropertyAdapter<Point3, ?> propertyAdapter )
	{
		super( propertyAdapter );
	}

	@Override
	protected void initializeComponents()
	{
		super.initializeComponents();

		this.valueChangeListener = new ActionListener() {

			@Override
			public void actionPerformed( ActionEvent e )
			{
				Point3PropertyController.this.updateAdapterFromUI( e );

			}
		};
		this.xLabel = new Label( "( x:" );
		this.yLabel = new Label( ", y:" );
		this.zLabel = new Label( ", z:" );
		this.endLabel = new Label( ")" );

		this.xField = new DoubleTextField( 3 );
		Dimension d = new Dimension( this.xField.getMinimumSize() );
		d.width = 36;
		this.xField.setMinimumSize( d );
		this.xField.addActionListener( this.valueChangeListener );

		this.yField = new DoubleTextField( 3 );
		this.yField.setMinimumSize( d );
		this.yField.addActionListener( this.valueChangeListener );

		this.zField = new DoubleTextField( 3 );
		this.zField.setMinimumSize( d );
		this.zField.addActionListener( this.valueChangeListener );

		GridBagPanel uiPanel = new GridBagPanel();
		uiPanel.addComponent( this.xLabel, new GridBagConstraints(
				0, // gridX
				0, // gridY
				1, // gridWidth
				1, // gridHeight
				0.0, // weightX
				0.0, // weightY
				GridBagConstraints.WEST, // anchor
				GridBagConstraints.NONE, // fill
				new Insets( 0, 0, 0, 0 ), // insets (top, left, bottom, right)
				0, // ipadX
				0 ) // ipadY
		);
		uiPanel.addComponent( new SwingAdapter( this.xField ), new GridBagConstraints(
				1, // gridX
				0, // gridY
				1, // gridWidth
				1, // gridHeight
				0.5, // weightX
				0.0, // weightY
				GridBagConstraints.WEST, // anchor
				GridBagConstraints.NONE, // fill
				new Insets( 0, 0, 0, 0 ), // insets (top, left, bottom, right)
				0, // ipadX
				0 ) // ipadY
		);
		uiPanel.addComponent( this.yLabel, new GridBagConstraints(
				2, // gridX
				0, // gridY
				1, // gridWidth
				1, // gridHeight
				0.0, // weightX
				0.0, // weightY
				GridBagConstraints.WEST, // anchor
				GridBagConstraints.NONE, // fill
				new Insets( 0, 0, 0, 0 ), // insets (top, left, bottom, right)
				0, // ipadX
				0 ) // ipadY
		);
		uiPanel.addComponent( new SwingAdapter( this.yField ), new GridBagConstraints(
				3, // gridX
				0, // gridY
				1, // gridWidth
				1, // gridHeight
				0.5, // weightX
				0.0, // weightY
				GridBagConstraints.WEST, // anchor
				GridBagConstraints.NONE, // fill
				new Insets( 0, 0, 0, 0 ), // insets (top, left, bottom, right)
				0, // ipadX
				0 ) // ipadY
		);
		uiPanel.addComponent( this.zLabel, new GridBagConstraints(
				4, // gridX
				0, // gridY
				1, // gridWidth
				1, // gridHeight
				0.0, // weightX
				0.0, // weightY
				GridBagConstraints.WEST, // anchor
				GridBagConstraints.NONE, // fill
				new Insets( 0, 0, 0, 0 ), // insets (top, left, bottom, right)
				0, // ipadX
				0 ) // ipadY
		);
		uiPanel.addComponent( new SwingAdapter( this.zField ), new GridBagConstraints(
				5, // gridX
				0, // gridY
				1, // gridWidth
				1, // gridHeight
				0.5, // weightX
				0.0, // weightY
				GridBagConstraints.WEST, // anchor
				GridBagConstraints.NONE, // fill
				new Insets( 0, 0, 0, 0 ), // insets (top, left, bottom, right)
				0, // ipadX
				0 ) // ipadY
		);
		uiPanel.addComponent( this.endLabel, new GridBagConstraints(
				6, // gridX
				0, // gridY
				1, // gridWidth
				1, // gridHeight
				0.0, // weightX
				0.0, // weightY
				GridBagConstraints.WEST, // anchor
				GridBagConstraints.NONE, // fill
				new Insets( 0, 0, 0, 0 ), // insets (top, left, bottom, right)
				0, // ipadX
				0 ) // ipadY
		);

		this.addComponent( uiPanel, new GridBagConstraints(
				0, // gridX
				0, // gridY
				1, // gridWidth
				1, // gridHeight
				0.0, // weightX
				0.0, // weightY
				GridBagConstraints.WEST, // anchor
				GridBagConstraints.NONE, // fill
				new Insets( 0, 0, 0, 0 ), // insets (top, left, bottom, right)
				0, // ipadX
				0 ) // ipadY
		);
		this.addComponent( BoxUtilities.createHorizontalGlue(), new GridBagConstraints(
				0, // gridX
				0, // gridY
				1, // gridWidth
				1, // gridHeight
				1.0, // weightX
				0.0, // weightY
				GridBagConstraints.WEST, // anchor
				GridBagConstraints.HORIZONTAL, // fill
				new Insets( 0, 0, 0, 0 ), // insets (top, left, bottom, right)
				0, // ipadX
				0 ) // ipadY
		);
		//		this.mainPanel.addComponent(uiPanel, Constraint.CENTER);

	}

	@Override
	public Class<?> getPropertyType()
	{
		return Point3.class;
	}

	@Override
	protected void setValueOnUI( Point3 point3Value )
	{
		this.doUpdateOnAdapter = false;
		if( point3Value != null )
		{
			this.xField.setValue( point3Value.x );
			this.yField.setValue( point3Value.y );
			this.zField.setValue( point3Value.z );
		}
		else
		{
			this.xField.setValue( null );
			this.yField.setValue( null );
			this.zField.setValue( null );
		}
		this.doUpdateOnAdapter = true;
	}

	private Point3 getPointFromUI()
	{
		double xVal = xField.getValue();
		double yVal = yField.getValue();
		double zVal = zField.getValue();
		if( Double.isNaN( xVal ) || Double.isNaN( yVal ) || Double.isNaN( zVal ) )
		{
			return null;
		}
		return new Point3( xVal, yVal, zVal );
	}

	protected void updateAdapterFromUI( ActionEvent e )
	{
		if( this.doUpdateOnAdapter )
		{
			Point3 newPoint = getPointFromUI();
			if( newPoint != null )
			{
				if( !newPoint.equals( this.propertyAdapter.getValue() ) )
				{
					if( ( this.propertyAdapter.getLastSetValue() == null ) || !this.propertyAdapter.getLastSetValue().equals( newPoint ) )
					{
						org.lgna.croquet.Operation operation = new org.alice.ide.properties.adapter.croquet.ModelPositionPropertyValueOperation( this.propertyAdapter, newPoint );
						operation.fire( org.lgna.croquet.triggers.ActionEventTrigger.createUserInstance( e ) );
					}
				}
			}
		}
	}

}
