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

import org.alice.ide.properties.adapter.AbstractPropertyAdapter;
import org.alice.stageide.properties.ModelSizeAdapter;
import org.alice.stageide.properties.MutableRiderVehicleAdapter;
import org.alice.stageide.properties.SelectedInstanceAdapter;
import org.alice.stageide.properties.uicontroller.CompositePropertyController;
import org.alice.stageide.properties.uicontroller.ModelSizePropertyController;
import org.alice.stageide.properties.uicontroller.SelectedInstancePropertyController;

import edu.cmu.cs.dennisc.math.Point3;

public class AdapterControllerUtilities
{
	//TODO: base this lookup on a (type -> property controller) registration that happens in the IDE
	public static PropertyAdapterController getValuePanelForPropertyAdapter( AbstractPropertyAdapter<?, ?> propertyAdapter )
	{
		Class<?> propertyType = propertyAdapter != null ? propertyAdapter.getPropertyType() : null;
		if( propertyAdapter instanceof SelectedInstanceAdapter )
		{
			return new SelectedInstancePropertyController( (SelectedInstanceAdapter)propertyAdapter );
		}
		if( propertyType == null )
		{
			return new BlankPropertyController( propertyAdapter );
		}
		if( propertyAdapter.getExpressionState() != null )
		{
			return new ExpressionBasedPropertyController( propertyAdapter );
		}
		//Now check based on desired type
		if( edu.cmu.cs.dennisc.color.Color4f.class.isAssignableFrom( propertyType ) )
		{
			return new Color4fPropertyController( (AbstractPropertyAdapter<edu.cmu.cs.dennisc.color.Color4f, ?>)propertyAdapter );
		}
		else if( String.class.isAssignableFrom( propertyType ) )
		{
			return new StringPropertyController( (AbstractPropertyAdapter<String, ?>)propertyAdapter );
		}
		else if( Float.class.isAssignableFrom( propertyType ) )
		{
			return new FloatPropertyController( (AbstractPropertyAdapter<Float, ?>)propertyAdapter );
		}
		else if( Point3.class.isAssignableFrom( propertyType ) )
		{
			return new Point3PropertyController( (AbstractPropertyAdapter<Point3, ?>)propertyAdapter );
		}
		else if( propertyAdapter instanceof MutableRiderVehicleAdapter )
		{
			return new CompositePropertyController( (MutableRiderVehicleAdapter)propertyAdapter );
		}
		else if( propertyAdapter instanceof ModelSizeAdapter )
		{
			return new ModelSizePropertyController( (ModelSizeAdapter)propertyAdapter );
		} else
		{
			return new BlankPropertyController( propertyAdapter );
		}
	}
}
