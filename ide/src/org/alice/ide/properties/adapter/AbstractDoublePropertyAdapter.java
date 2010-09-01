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

package org.alice.ide.properties.adapter;

import java.util.Locale;

import org.alice.ide.properties.uicontroller.DoublePropertyController;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.croquet.Model;
import edu.cmu.cs.dennisc.croquet.Operation;
import edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities;

public abstract class AbstractDoublePropertyAdapter<O> extends AbstractInstancePropertyAdapter<Double, O> 
{

	protected class SetDoubleOperation extends SetValueOperation 
	{
		public SetDoubleOperation( Double value, String name) {
			super( value, name, java.util.UUID.fromString( "34c9f0d6-62e1-4c13-b773-9a7dc3b6b7f1" ) );
		}
	}
	
	public static java.text.NumberFormat format = new java.text.DecimalFormat( "0.0" );
	protected edu.cmu.cs.dennisc.croquet.PopupMenuOperation popupMenuOperation;
	protected java.util.List< SetDoubleOperation > defaultDoubleOperationModels;
	
	public AbstractDoublePropertyAdapter(String repr, O instance )
	{
		super(repr, instance);
	}
	
	public Class<Double> getPropertyType()
	{
		return Double.class;
	}
	
	@Override
	protected String getUndoRedoDescription(Locale locale) 
	{
		return "Double";
	}
	
	@Override
	public Operation getEditOperation() 
	{
		if (this.popupMenuOperation == null)
		{
			this.defaultDoubleOperationModels = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			
			ReflectionUtilities.getPublicStaticFinalInstances(Color4f.class, Color4f.class);
			
			for (double d : this.getDefaultValues())
			{
				this.defaultDoubleOperationModels.add(new SetDoubleOperation(new Double(d), format.format(d)));
			}
				
			this.popupMenuOperation = new edu.cmu.cs.dennisc.croquet.MenuModel( java.util.UUID.fromString( "66435390-e900-44c7-b440-0789c31e5a7a" ) ) {
				@Override
				protected void handlePopupMenuPrologue(edu.cmu.cs.dennisc.croquet.PopupMenu popupMenu, edu.cmu.cs.dennisc.croquet.PopupMenuOperationContext context ) {
					super.handlePopupMenuPrologue( popupMenu, context );
					
					Double currentDouble = AbstractDoublePropertyAdapter.this.getValue();
					String currentDoubleName = format.format(currentDouble)+AbstractDoublePropertyAdapter.this.getCurrentValueLabelString();
					
					SetDoubleOperation currentDoubleOperation = new SetDoubleOperation(currentDouble, currentDoubleName);
					
					java.util.List<Model> models = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
					models.add(currentDoubleOperation);
					models.add(edu.cmu.cs.dennisc.croquet.MenuModel.SEPARATOR);
					models.addAll(AbstractDoublePropertyAdapter.this.defaultDoubleOperationModels);
					
					edu.cmu.cs.dennisc.croquet.MenuItemContainerUtilities.addMenuElements( popupMenu, models );
				}
			}.getPopupMenuOperation();
		}
		return this.popupMenuOperation;
	}
	
	protected double[] getDefaultValues()
	{
		double[] defaultValues = {0.0, .5, 1.0, 2.0, 5.0};
		return defaultValues;
	}

}
