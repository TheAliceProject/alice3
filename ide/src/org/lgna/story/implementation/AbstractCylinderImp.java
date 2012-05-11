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
package org.lgna.story.implementation;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractCylinderImp extends ShapeImp {
	private final edu.cmu.cs.dennisc.scenegraph.Cylinder sgCylinder = new edu.cmu.cs.dennisc.scenegraph.Cylinder();
	public final DoubleProperty length = new DoubleProperty( AbstractCylinderImp.this ) {
		@Override
		public Double getValue() {
			return AbstractCylinderImp.this.sgCylinder.length.getValue();
		}
		@Override
		protected void handleSetValue( Double value ) {
			AbstractCylinderImp.this.sgCylinder.length.setValue( value );
		}
	};

	public AbstractCylinderImp() {
		this.getSgVisuals()[ 0 ].geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { this.sgCylinder } );
	}
	@Override
	protected edu.cmu.cs.dennisc.property.InstanceProperty[] getScaleProperties() {
		return new edu.cmu.cs.dennisc.property.InstanceProperty[] { this.sgCylinder.length, this.sgCylinder.bottomRadius };
	}
	
	protected abstract void setXZ( double xz );
	protected edu.cmu.cs.dennisc.scenegraph.Cylinder getSgCylinder() {
		return this.sgCylinder;
	}
	@Override
	public Resizer[] getResizers() {
		return new Resizer[] { Resizer.Y_AXIS, Resizer.XZ_PLANE, Resizer.UNIFORM };
	}
	@Override
	public double getValueForResizer( Resizer resizer ) {
		if( resizer == Resizer.Y_AXIS ) {
			return this.length.getValue();
		} else if( resizer == Resizer.XZ_PLANE ) {
			return this.sgCylinder.bottomRadius.getValue();
		} else if( resizer == Resizer.UNIFORM ) {
			return this.length.getValue();
		} else {
			assert false : resizer;
			return Double.NaN;
		}
	}
	@Override
	public void setValueForResizer( Resizer resizer, double value ) {
		assert value > 0.0 : value;
		if( resizer == Resizer.Y_AXIS ) {
			this.length.setValue( value );
		} else if( resizer == Resizer.XZ_PLANE ) {
			this.setXZ( value );
		} else if( resizer == Resizer.UNIFORM ) {
			double prevValue = this.sgCylinder.length.getValue();
			this.length.setValue( value );
			if( prevValue != 0.0 ) {
				double ratio = value / prevValue;
				this.setXZ( this.sgCylinder.bottomRadius.getValue() * ratio );
			}
		} else {
			assert false : resizer;
		}
	}
}
