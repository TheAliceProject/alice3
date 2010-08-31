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

import org.alice.ide.properties.adapter.PropertyAdapter;
import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.croquet.BorderPanel;
import edu.cmu.cs.dennisc.croquet.Label;
import edu.cmu.cs.dennisc.croquet.Model;
import edu.cmu.cs.dennisc.croquet.Panel;
import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;

public class DoublePropertyController extends AbstractAdapterController<Double>
{
	private Label doubleLabel;
	
	protected static final String BLANK_STRING = "NO VALUE";
	
	private class SelectDoubleOperation extends edu.cmu.cs.dennisc.croquet.ActionOperation {
		protected Double doubleValue;
		
		public SelectDoubleOperation( Double doubleValue, String name) {
			super( edu.cmu.cs.dennisc.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "34c9f0d6-62e1-4c13-b773-9a7dc3b6b7f1" ) );
			this.doubleValue = doubleValue;
			this.setName( name );
		}
		@Override
		protected void perform(edu.cmu.cs.dennisc.croquet.ActionOperationContext context) {
			DoublePropertyController.this.setValueOnData(this.doubleValue);
			context.finish();
		}
		
	}
	
	private static java.text.NumberFormat format = new java.text.DecimalFormat( "0.0" );
	private edu.cmu.cs.dennisc.croquet.PopupMenuOperation popupMenuOperation;
	protected java.util.List< SelectDoubleOperation > defaultDoubleOperationModels;
	
	public DoublePropertyController(PropertyAdapter<Double, ?> propertyAdapter)
	{
		super(propertyAdapter);
	}
	
	@Override
	protected void initializeComponents() 
	{
		this.doubleLabel = new Label();
		this.addComponent(this.doubleLabel, BorderPanel.Constraint.CENTER);
		
		this.defaultDoubleOperationModels = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		
		ReflectionUtilities.getPublicStaticFinalInstances(Color4f.class, Color4f.class);
		
		for (double d : this.getDefaultValues())
		{
			this.defaultDoubleOperationModels.add(new SelectDoubleOperation(new Double(d), format.format(d)));
		}
			
		this.popupMenuOperation = new edu.cmu.cs.dennisc.croquet.MenuModel( java.util.UUID.fromString( "66435390-e900-44c7-b440-0789c31e5a7a" ) ) {
			@Override
			protected void handlePopupMenuPrologue(edu.cmu.cs.dennisc.croquet.PopupMenu popupMenu, edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext context ) {
				super.handlePopupMenuPrologue( popupMenu, context );
				
				Double currentDouble = DoublePropertyController.this.propertyAdapter.getValue();
				String currentDoubleName = format.format(currentDouble)+" (current value)";
				
				SelectDoubleOperation currentDoubleOperation = new SelectDoubleOperation(currentDouble, currentDoubleName);
				
				java.util.List<Model> models = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
				models.add(currentDoubleOperation);
				models.add(edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR);
				models.addAll(DoublePropertyController.this.defaultDoubleOperationModels);
				
				edu.cmu.cs.dennisc.croquet.MenuItemContainerUtilities.addMenuElements( popupMenu, models );
			}
		}.getPopupMenuOperation();
		
		this.addComponent(this.popupMenuOperation.createButton(), BorderPanel.Constraint.EAST);
	}
	
	@Override
	public Panel getPanel()
	{
		return this;
	}
	
	@Override
	public Class<?> getPropertyType() 
	{
		return Double.class;
	}
	
	@Override
	protected void setValueOnUI(Double value)
	{
		if (value != null)
		{
			this.doubleLabel.setText(format.format(value));
		}
		else
		{
			this.doubleLabel.setText(BLANK_STRING);
		}
	}
	
	protected double[] getDefaultValues()
	{
		double[] defaultValues = {0.0, .5, 1.0, 2.0, 5.0};
		return defaultValues;
	}
}
