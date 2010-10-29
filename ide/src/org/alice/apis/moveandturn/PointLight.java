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

public class PointLight extends Light {
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< Double > CONSTANT_ATTENUATION_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< Double >( PointLight.class, "ConstantAttenuation" );
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< Double > LINEAR_ATTENUATION_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< Double >( PointLight.class, "LinearAttenuation" );
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< Double > QUADRATIC_ATTENUATION_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< Double >( PointLight.class, "QuadraticAttenuation" );
	private edu.cmu.cs.dennisc.scenegraph.PointLight m_sgPointLight;
	@Override
	protected edu.cmu.cs.dennisc.scenegraph.Light getSGLight() {
		if( m_sgPointLight == null ) {
			m_sgPointLight = new edu.cmu.cs.dennisc.scenegraph.PointLight();
		}
		return m_sgPointLight;
	}

	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
	public Double getConstantAttenuation() {
		return m_sgPointLight.constantAttenuation.getValue();
	}
	public void setConstantAttenuation( Number constantAttenuation, Number duration, Style style ) {
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			m_sgPointLight.constantAttenuation.setValue( constantAttenuation.doubleValue() );
		} else {
			perform( new edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation( duration, style, getConstantAttenuation(), constantAttenuation.doubleValue() ) {
				@Override
				protected void updateValue( Double v ) {
					m_sgPointLight.constantAttenuation.setValue( v );
				}
			} );
		}
	}
	public void setConstantAttenuation( Number constantAttenuation, Number duration ) {
		setConstantAttenuation( constantAttenuation, duration, DEFAULT_STYLE );
	}
	public void setConstantAttenuation( Number constantAttenuation ) {
		setConstantAttenuation( constantAttenuation, DEFAULT_DURATION );
	}
	
	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
	public Double getLinearAttenuation() {
		return m_sgPointLight.linearAttenuation.getValue();
	}
	public void setLinearAttenuation( Number linearAttenuation, Number duration, Style style ) {
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			m_sgPointLight.linearAttenuation.setValue( linearAttenuation.doubleValue() );
		} else {
			perform( new edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation( duration, style, getLinearAttenuation(), linearAttenuation.doubleValue() ) {
				@Override
				protected void updateValue( Double v ) {
					m_sgPointLight.linearAttenuation.setValue( v );
				}
			} );
		}
	}
	public void setLinearAttenuation( Number linearAttenuation, Number duration ) {
		setLinearAttenuation( linearAttenuation, duration, DEFAULT_STYLE );
	}
	public void setLinearAttenuation( Number linearAttenuation ) {
		setLinearAttenuation( linearAttenuation, DEFAULT_DURATION );
	}

	
	@PropertyGetterTemplate( visibility=Visibility.PRIME_TIME )
	public Double getQuadraticAttenuation() {
		return m_sgPointLight.quadraticAttenuation.getValue();
	}
	public void setQuadraticAttenuation( Number quadraticAttenuation, Number duration, Style style ) {
		duration = adjustDurationIfNecessary( duration );
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			m_sgPointLight.quadraticAttenuation.setValue( quadraticAttenuation.doubleValue() );
		} else {
			perform( new edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation( duration, style, getQuadraticAttenuation(), quadraticAttenuation.doubleValue() ) {
				@Override
				protected void updateValue( Double v ) {
					m_sgPointLight.quadraticAttenuation.setValue( v );
				}
			} );
		}
	}
	public void setQuadraticAttenuation( Number quadraticAttenuation, Number duration ) {
		setQuadraticAttenuation( quadraticAttenuation, duration, DEFAULT_STYLE );
	}
	public void setQuadraticAttenuation( Number quadraticAttenuation ) {
		setQuadraticAttenuation( quadraticAttenuation, DEFAULT_DURATION );
	}

}
