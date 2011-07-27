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

package org.alice.stageide.properties;

import java.util.Locale;

import org.alice.ide.IDE;
import org.alice.ide.properties.adapter.AbstractScaleAdapter;
import org.alice.interact.handle.ManipulationHandle3D;
import org.lgna.croquet.Operation;

import edu.cmu.cs.dennisc.math.Dimension3;
import edu.cmu.cs.dennisc.math.Matrix3x3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.property.event.PropertyListener;

public class ModelScaleAdapter extends AbstractScaleAdapter<org.lgna.story.implementation.ModelImplementation>
{
	public ModelScaleAdapter(org.lgna.story.implementation.ModelImplementation instance)
	{
		super("Size", instance);
	}
	
	@Override
	public String getUndoRedoDescription(Locale locale) 
	{
		return "Resize";
	}

	public Dimension3 getValue() 
	{
		if (this.instance != null)
		{
			this.instance.getScale();
		}
		return null;
	}
	
	@Override
	public void setValue(Dimension3 value) 
	{
		super.setValue(value);
		if (this.instance != null)
		{
//			Vector3 scaleVector = edu.cmu.cs.dennisc.math.ScaleUtilities.newScaleVector3(value);
//			Matrix3x3 oldScale = edu.cmu.cs.dennisc.scenegraph.scale.ScaleUtilities.getTransformableScale(this.instance.getSgComposite());
//			Dimension3 oldScaleVector = this.inst
//			final Dimension3 scaleToApply = new Dimension3(value.x / oldScaleVector.x, value.y / oldScaleVector.y, value.z / oldScaleVector.z);
			this.instance.setScale(value);
			
//			if( ((MoveAndTurnSceneEditor)(IDE.getActiveInstance().getSceneEditor())).getAnimator() != null ) {
//				class ScaleAnimation extends edu.cmu.cs.dennisc.math.animation.Vector3Animation {
//					private edu.cmu.cs.dennisc.math.Vector3 m_vPrev = new edu.cmu.cs.dennisc.math.Vector3( 1, 1, 1 );
//					private edu.cmu.cs.dennisc.math.Vector3 m_vBuffer = new edu.cmu.cs.dennisc.math.Vector3();
//					public ScaleAnimation() {
//						super( 0.5, edu.cmu.cs.dennisc.animation.TraditionalStyle.BEGIN_AND_END_GENTLY, new edu.cmu.cs.dennisc.math.Vector3( 1, 1, 1 ), scaleToApply );
//					}
//					@Override
//					protected void updateValue( edu.cmu.cs.dennisc.math.Vector3 v ) {
//						edu.cmu.cs.dennisc.math.Vector3.setReturnValueToDivision( m_vBuffer, v, m_vPrev );
//						edu.cmu.cs.dennisc.scenegraph.scale.ScaleUtilities.applyScale( ModelScaleAdapter.this.instance.getSgComposite(), m_vBuffer, ManipulationHandle3D.NOT_3D_HANDLE_CRITERION );
//						m_vPrev.set( v );
//					}
//				}
//				((MoveAndTurnSceneEditor)(IDE.getActiveInstance().getSceneEditor())).getAnimator().invokeLater( new ScaleAnimation(), null );
//			} else {
//				edu.cmu.cs.dennisc.scenegraph.scale.ScaleUtilities.applyScale( ModelScaleAdapter.this.instance.getSgComposite(), scaleToApply, ManipulationHandle3D.NOT_3D_HANDLE_CRITERION );
//			}
		}
	}

	@Override
	public Operation getEditModel() 
	{
		return null;
	}

	@Override
	protected void addPropertyListener(PropertyListener propertyListener) {
		if (this.instance != null){
			this.instance.addScaleListener(propertyListener);
		}	
	}

	@Override
	protected void removePropertyListener(PropertyListener propertyListener) {
		if (this.instance != null){
			this.instance.removeScaleListener(propertyListener);
		}		
	}
}
