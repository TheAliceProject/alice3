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

import org.alice.ide.croquet.models.StandardExpressionState;
import org.alice.ide.croquet.models.ui.formatter.FormatterSelectionState;
import org.alice.ide.swing.icons.ColorIcon;
import edu.cmu.cs.dennisc.color.Color4f;

public abstract class AbstractColorPropertyAdapter<O> extends AbstractInstancePropertyAdapter<edu.cmu.cs.dennisc.color.Color4f, O> 
{
	protected class SetColorOperation extends SetValueOperation<edu.cmu.cs.dennisc.color.Color4f> {
		public SetColorOperation( Color4f value, String name) {
			super( AbstractColorPropertyAdapter.this, value, name, java.util.UUID.fromString( "828f978a-ce77-45a0-83c6-dbac238b4210" ) );
			if (this.value != null)
			{
				this.setSmallIcon(new ColorIcon(this.value.getAsAWTColor()));
			}
		}
		
		@Override
		public void setValue(edu.cmu.cs.dennisc.color.Color4f value)
		{
			super.setValue(value);
			if (value != null)
			{
				this.setSmallIcon(new ColorIcon(this.value.getAsAWTColor()));
			}
		}
	}
	
	private org.lgna.croquet.MenuModel.InternalPopupPrepModel popupMenuOperation;
	protected java.util.List< SetColorOperation > defaultColorOperationModels;
	private static java.text.NumberFormat format = new java.text.DecimalFormat( "0.00" );
	
	@Override
	public org.lgna.croquet.PopupPrepModel getEditModel() 
	{
		if (this.popupMenuOperation == null)
		{
			this.defaultColorOperationModels = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
			
			Class< ? > colorClass = Color4f.class;
			for( java.lang.reflect.Field fld : edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getPublicStaticFinalFields( colorClass, colorClass ) ) 
			{
				try
				{
					defaultColorOperationModels.add(new SetColorOperation((Color4f)fld.get(colorClass), FormatterSelectionState.getInstance().getSelectedItem().getNameForField(fld)));
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			this.popupMenuOperation = new org.lgna.croquet.MenuModel( java.util.UUID.fromString( "9aa93f57-87cc-412b-b166-beb73bcd1fe8" ) ) {
				@Override
				public void handlePopupMenuPrologue(org.lgna.croquet.components.PopupMenu popupMenu, org.lgna.croquet.history.StandardPopupPrepStep context ) {
					super.handlePopupMenuPrologue( popupMenu, context );
					
					Color4f currentColor = AbstractColorPropertyAdapter.this.getValue();
					String currentColorName = null;
					for (SetColorOperation defaultColor : AbstractColorPropertyAdapter.this.defaultColorOperationModels)
					{
						if (currentColor.equals(defaultColor.value))
						{
							currentColorName = defaultColor.getName();
						}
					}
					if (currentColorName ==  null)
					{
						currentColorName = "r="+format.format(currentColor.red)+", g="+format.format(currentColor.green)+", b="+format.format(currentColor.blue);
					}
					currentColorName += AbstractColorPropertyAdapter.this.getCurrentValueLabelString();
					
					SetColorOperation currentColorOperation = new SetColorOperation(currentColor, currentColorName);
					
					java.util.List<org.lgna.croquet.StandardMenuItemPrepModel> models = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
					models.add(currentColorOperation.getMenuItemPrepModel());
					models.add(org.lgna.croquet.MenuModel.SEPARATOR);
					for( SetColorOperation operation : AbstractColorPropertyAdapter.this.defaultColorOperationModels ) {
						models.add(operation.getMenuItemPrepModel());
					}
					
					org.lgna.croquet.components.MenuItemContainerUtilities.addMenuElements( popupMenu, models );
				}
			}.getPopupPrepModel();
		}
		return this.popupMenuOperation;
	}
	
	@Override
	public SetValueOperation<Color4f> getSetValueOperation(Color4f value) 
	{
		return new SetColorOperation(value, null);
	}
	
	public AbstractColorPropertyAdapter(O instance, StandardExpressionState expressionState)
	{
		this("Color", instance, expressionState);
	}
	
	public AbstractColorPropertyAdapter(String repr, O instance, StandardExpressionState expressionState )
	{
		super(repr, instance, expressionState);
	}
	
	public Color4f getValueCopy() 
	{
		return new Color4f(this.getValue());
	}
	
	@Override
	public String getUndoRedoDescription(Locale locale) 
	{
		return "Color";
	}
	
	public Class<edu.cmu.cs.dennisc.color.Color4f> getPropertyType()
	{
		return edu.cmu.cs.dennisc.color.Color4f.class;
	}

}
