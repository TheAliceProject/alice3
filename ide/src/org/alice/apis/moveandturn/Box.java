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

/**
 * @author Dennis Cosgrove
 */
public class Box extends Model {
	private edu.cmu.cs.dennisc.scenegraph.Box m_sgBox = new edu.cmu.cs.dennisc.scenegraph.Box();
	@Override
	protected void createSGGeometryIfNecessary() {
	}
	@Override
	protected edu.cmu.cs.dennisc.scenegraph.Geometry getSGGeometry() {
		return m_sgBox;
	}
	public Double getLeft() {
		return m_sgBox.xMinimum.getValue();
	}
	public void setLeft( Number value, Number duration, Style style ) {
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			m_sgBox.xMinimum.setValue( value.doubleValue() );
		} else {
			perform( new edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation( adjustDurationIfNecessary( duration ), style, getLeft(), value.doubleValue() ) {
				@Override
				protected void updateValue( Double d ) {
					m_sgBox.xMinimum.setValue( d );
				}
			} );
		}
	}
	public void setLeft( Number value, Number duration ) {
		setLeft( value, duration, DEFAULT_STYLE );
	}
	public void setLeft( Number value ) {
		setLeft( value, DEFAULT_DURATION );
	}

	public Double getRight() {
		return m_sgBox.xMaximum.getValue();
	}
	public void setRight( Number value, Number duration, Style style ) {
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			m_sgBox.xMaximum.setValue( value.doubleValue() );
		} else {
			perform( new edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation( adjustDurationIfNecessary( duration ), style, getRight(), value.doubleValue() ) {
				@Override
				protected void updateValue( Double d ) {
					m_sgBox.xMaximum.setValue( d );
				}
			} );
		}
	}
	public void setRight( Number value, Number duration ) {
		setRight( value, duration, DEFAULT_STYLE );
	}
	public void setRight( Number value ) {
		setRight( value, DEFAULT_DURATION );
	}

	public Double getBottom() {
		return m_sgBox.yMinimum.getValue();
	}
	public void setBottom( Number value, Number duration, Style style ) {
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			m_sgBox.yMinimum.setValue( value.doubleValue() );
		} else {
			perform( new edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation( adjustDurationIfNecessary( duration ), style, getBottom(), value.doubleValue() ) {
				@Override
				protected void updateValue( Double d ) {
					m_sgBox.yMinimum.setValue( d );
				}
			} );
		}
	}
	public void setBottom( Number value, Number duration ) {
		setBottom( value, duration, DEFAULT_STYLE );
	}
	public void setBottom( Number value ) {
		setBottom( value, DEFAULT_DURATION );
	}

	public Double getTop() {
		return m_sgBox.yMaximum.getValue();
	}
	public void setTop( Number value, Number duration, Style style ) {
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			m_sgBox.yMaximum.setValue( value.doubleValue() );
		} else {
			perform( new edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation( adjustDurationIfNecessary( duration ), style, getTop(), value.doubleValue() ) {
				@Override
				protected void updateValue( Double d ) {
					m_sgBox.yMaximum.setValue( d );
				}
			} );
		}
	}
	public void setTop( Number value, Number duration ) {
		setTop( value, duration, DEFAULT_STYLE );
	}
	public void setTop( Number value ) {
		setTop( value, DEFAULT_DURATION );
	}

	public Double getFront() {
		return m_sgBox.zMinimum.getValue();
	}
	public void setFront( Number value, Number duration, Style style ) {
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			m_sgBox.zMinimum.setValue( value.doubleValue() );
		} else {
			perform( new edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation( adjustDurationIfNecessary( duration ), style, getFront(), value.doubleValue() ) {
				@Override
				protected void updateValue( Double d ) {
					m_sgBox.zMinimum.setValue( d );
				}
			} );
		}
	}
	public void setFront( Number value, Number duration ) {
		setFront( value, duration, DEFAULT_STYLE );
	}
	public void setFront( Number value ) {
		setFront( value, DEFAULT_DURATION );
	}

	public Double getBack() {
		return m_sgBox.zMaximum.getValue();
	}
	public void setBack( Number value, Number duration, Style style ) {
		if( edu.cmu.cs.dennisc.math.EpsilonUtilities.isWithinReasonableEpsilon( duration, RIGHT_NOW ) ) {
			m_sgBox.zMaximum.setValue( value.doubleValue() );
		} else {
			perform( new edu.cmu.cs.dennisc.animation.interpolation.DoubleAnimation( adjustDurationIfNecessary( duration ), style, getBack(), value.doubleValue() ) {
				@Override
				protected void updateValue( Double d ) {
					m_sgBox.zMaximum.setValue( d );
				}
			} );
		}
	}
	public void setBack( Number value, Number duration ) {
		setBack( value, duration, DEFAULT_STYLE );
	}
	public void setBack( Number value ) {
		setBack( value, DEFAULT_DURATION );
	}
}
