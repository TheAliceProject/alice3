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

package org.alice.stageide.properties;

import org.alice.ide.croquet.models.StandardExpressionState;
import org.alice.ide.properties.adapter.AbstractInstancePropertyAdapter;

import edu.cmu.cs.dennisc.math.Dimension3;
import edu.cmu.cs.dennisc.math.Point3;

public class ModelSizeAdapter extends AbstractInstancePropertyAdapter<Dimension3, org.lgna.story.implementation.ModelImp>
{
	public ModelSizeAdapter( org.lgna.story.implementation.ModelImp instance, StandardExpressionState expressionState )
	{
		super( "Size", instance, null, expressionState );
	}

	@Override
	public Dimension3 getValue()
	{
		if( this.instance != null )
		{
			Dimension3 size = this.instance.getSize();
			size = this.instance.getSize();
			return size;
		}
		return null;
	}

	@Override
	public void setValue( Dimension3 value )
	{
		Dimension3 currentValue = getValue();
		super.setValue( value );
		if( this.instance != null ) {
			double dist = Point3.calculateDistanceBetween( currentValue, value );
			double duration = 1;
			if( dist < .02 )
			{
				duration = 0;
			}
			else if( dist < .5 )
			{
				duration = ( dist - .02 ) / ( .5 - .02 );
			}

			this.instance.animateSetSize( value, duration, edu.cmu.cs.dennisc.animation.TraditionalStyle.BEGIN_AND_END_GENTLY );
		}
	}

	@Override
	protected void addPropertyListener( edu.cmu.cs.dennisc.property.event.PropertyListener propertyListener ) {
		if( this.instance != null ) {
			this.instance.addScaleListener( propertyListener );
		}
	}

	@Override
	protected void removePropertyListener( edu.cmu.cs.dennisc.property.event.PropertyListener propertyListener ) {
		if( this.instance != null ) {
			this.instance.removeScaleListener( propertyListener );
		}
	}

	@Override
	public Class<Dimension3> getPropertyType() {
		return Dimension3.class;
	}

	@Override
	public Dimension3 getValueCopyIfMutable() {
		return new Dimension3( this.getValue() );
	}
}
