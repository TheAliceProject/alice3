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
import org.alice.ide.properties.adapter.AbstractPropertyAdapter;
import org.lgna.story.EmployeesOnly;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationEvent;
import edu.cmu.cs.dennisc.scenegraph.event.AbsoluteTransformationListener;

public class MoveableTurnableTranslationAdapter extends AbstractPropertyAdapter<Point3, org.lgna.story.SMovableTurnable>
{
	private AbsoluteTransformationListener absoluteTransformationListener;

	public MoveableTurnableTranslationAdapter( org.lgna.story.SMovableTurnable instance, StandardExpressionState expressionState ) {
		super( "Position", instance, expressionState );
	}

	private void initializeTransformationListenersIfNecessary()
	{
		if( this.absoluteTransformationListener == null )
		{
			this.absoluteTransformationListener = new AbsoluteTransformationListener() {
				@Override
				public void absoluteTransformationChanged( AbsoluteTransformationEvent absoluteTransformationEvent )
				{
					MoveableTurnableTranslationAdapter.this.handleInternalValueChanged();
				}
			};
		}
	}

	@Override
	public void startListening()
	{
		if( this.instance != null )
		{
			this.initializeTransformationListenersIfNecessary();
			org.lgna.story.implementation.AbstractTransformableImp implementation = EmployeesOnly.getImplementation( this.instance );
			implementation.getSgComposite().addAbsoluteTransformationListener( this.absoluteTransformationListener );
		}
	}

	@Override
	public void stopListening()
	{
		if( this.instance != null )
		{
			org.lgna.story.implementation.AbstractTransformableImp implementation = EmployeesOnly.getImplementation( this.instance );
			implementation.getSgComposite().removeAbsoluteTransformationListener( this.absoluteTransformationListener );
		}
	}

	@Override
	public Class<Point3> getPropertyType()
	{
		return Point3.class;
	}

	@Override
	public Point3 getValueCopyIfMutable()
	{
		return new Point3( this.getValue() );
	}

	@Override
	public Point3 getValue()
	{
		if( this.instance != null )
		{
			return EmployeesOnly.getImplementation( this.instance ).getAbsoluteTransformation().translation;
		}
		return null;
	}

	@Override
	public void setValue( Point3 newValue )
	{
		super.setValue( newValue );
		if( this.instance != null )
		{
			AffineMatrix4x4 currentTrans = EmployeesOnly.getImplementation( this.instance ).getAbsoluteTransformation();
			double dist = Point3.calculateDistanceBetween( currentTrans.translation, newValue );
			double duration = 1;
			if( dist < .02 )
			{
				duration = 0;
			}
			else if( dist < .5 )
			{
				duration = ( dist - .02 ) / ( .5 - .02 );
			}

			org.lgna.story.implementation.AbstractTransformableImp implementation = EmployeesOnly.getImplementation( this.instance );
			implementation.animatePositionOnly( org.lgna.story.implementation.AsSeenBy.SCENE.getActualEntityImplementation( implementation ), newValue, false, duration, edu.cmu.cs.dennisc.animation.TraditionalStyle.BEGIN_AND_END_GENTLY );
		}
	}

	protected void handleInternalValueChanged()
	{
		this.notifyValueObservers( this.getValue() );
	}

}
