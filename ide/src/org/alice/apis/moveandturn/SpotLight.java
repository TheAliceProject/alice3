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

package org.alice.apis.moveandturn;

import edu.cmu.cs.dennisc.alice.annotations.PropertyGetterTemplate;
import edu.cmu.cs.dennisc.alice.annotations.Visibility;

/**
 * @author Dennis Cosgrove
 */

public class SpotLight extends PointLight {
//	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< Double > INNER_BEAM_ANGLE_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< Double >( SpotLight.class, "InnerBeamAngle" );
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< Double > OUTER_BEAM_ANGLE_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< Double >( SpotLight.class, "OuterBeamAngle" );
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< Double > FALLOFF_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< Double >( SpotLight.class, "Falloff" );

	private edu.cmu.cs.dennisc.scenegraph.SpotLight m_sgSpotLight;
	@Override
	protected edu.cmu.cs.dennisc.scenegraph.Light getSGLight() {
		if( m_sgSpotLight == null ) {
			m_sgSpotLight = new edu.cmu.cs.dennisc.scenegraph.SpotLight();
		}
		return m_sgSpotLight;
	}
	
	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
	public Double getOuterBeamAngle() {
		return m_sgSpotLight.outerBeamAngle.getValue().getAsRevolutions();
	}
	public void setOuterBeamAngle( 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
			Number outerBeamAngle, 
			Number duration, 
			Style style 
		) {
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			m_sgSpotLight.outerBeamAngle.setValue( new edu.cmu.cs.dennisc.math.AngleInRevolutions( outerBeamAngle.doubleValue() ) );
		} else {
			perform( new edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation( duration, style, getOuterBeamAngle().doubleValue(), outerBeamAngle.doubleValue() ) {
				@Override
				protected void updateValue( Double a ) {
					m_sgSpotLight.outerBeamAngle.setValue( new edu.cmu.cs.dennisc.math.AngleInRevolutions( a.doubleValue() ) );
				}
			} );
		}
	}
	public void setOuterBeamAngle( 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
			Number outerBeamAngle, 
			Number duration 
		) {
		setOuterBeamAngle( outerBeamAngle, duration, DEFAULT_STYLE );
	}
	public void setOuterBeamAngle( 
			@edu.cmu.cs.dennisc.alice.annotations.ParameterTemplate( preferredArgumentClass=AngleInRevolutions.class )
			Number outerBeamAngle 
		) {
		setOuterBeamAngle( outerBeamAngle, DEFAULT_DURATION );
	}
	
//	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
//	public Number getInnerBeamAngle() {
//		return m_sgSpotLight.innerBeamAngle.getValue();
//	}
//	public void setInnerBeamAngle( Number innerBeamAngle, Number duration, Style style ) {
//		duration = adjustDurationIfNecessary( duration );
//		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
//			m_sgSpotLight.innerBeamAngle.setValue( innerBeamAngle );
//		} else {
//			perform( new edu.cmu.cs.dennisc.math.animation.AngleAnimation( duration, style, getInnerBeamAngle(), innerBeamAngle ) {
//				@Override
//				protected void updateValue( Number a ) {
//					m_sgSpotLight.innerBeamAngle.setValue( a );
//				}
//			} );
//		}
//	}
//	public void setInnerBeamAngle( Number innerBeamAngle, Number duration ) {
//		setInnerBeamAngle( innerBeamAngle, duration, DEFAULT_STYLE );
//	}
//	public void setInnerBeamAngle( Number innerBeamAngle ) {
//		setInnerBeamAngle( innerBeamAngle, DEFAULT_DURATION );
//	}
	
	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
	public Double getFalloff() {
		return m_sgSpotLight.falloff.getValue();
	}
	public void setFalloff( Number falloff, Number duration, Style style ) {
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			m_sgSpotLight.falloff.setValue( falloff.doubleValue() );
		} else {
			perform( new edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation( duration, style, getFalloff(), falloff.doubleValue() ) {
				@Override
				protected void updateValue( Double v ) {
					m_sgSpotLight.falloff.setValue( v );
				}
			} );
		}
	}
	public void setFalloff( Number falloff, Number duration ) {
		setFalloff( falloff, duration, DEFAULT_STYLE );
	}
	public void setFalloff( Number falloff ) {
		setFalloff( falloff, DEFAULT_DURATION );
	}
	
}
