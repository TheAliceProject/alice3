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

package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class BoundedNumberState< N extends Number > extends State< N > {
	public class SwingModel {
		private final javax.swing.BoundedRangeModel boundedRangeModel;
		private final javax.swing.SpinnerModel spinnerModel;
		private SwingModel( javax.swing.BoundedRangeModel boundedRangeModel, javax.swing.SpinnerModel spinnerModel ) {
			this.boundedRangeModel = boundedRangeModel;
			this.spinnerModel = spinnerModel;
		}
		public javax.swing.BoundedRangeModel getBoundedRangeModel() {
			return this.boundedRangeModel;
		}
		public javax.swing.SpinnerModel getSpinnerModel() {
			return this.spinnerModel;
		}
	}

	private final SwingModel swingModel;
	private final javax.swing.event.ChangeListener changeListener = new javax.swing.event.ChangeListener() {
		//private boolean previousValueIsAdjusting = false;
		public void stateChanged( javax.swing.event.ChangeEvent e ) {
			BoundedNumberState.this.handleStateChanged( e );
		}
	};

	public BoundedNumberState( Group group, java.util.UUID id, N initialValue, javax.swing.BoundedRangeModel boundedRangeModel, javax.swing.SpinnerModel spinnerModel ) {
		super( group, id, initialValue );
		this.swingModel = new SwingModel( boundedRangeModel, spinnerModel );
		this.swingModel.boundedRangeModel.addChangeListener( this.changeListener );
	}
	
	@Override
	public Iterable< ? extends PrepModel > getPotentialRootPrepModels() {
		return java.util.Collections.emptyList();
	}

	@Override
	public StringBuilder appendRepresentation( StringBuilder rv, N value, java.util.Locale locale ) {
		rv.append( value );
		return rv;
	}

	public SwingModel getSwingModel() {
		return this.swingModel;
	}

	private void handleStateChanged( javax.swing.event.ChangeEvent e ) {
		N nextValue = this.getValue();
		this.changeValue( nextValue, this.swingModel.boundedRangeModel.getValueIsAdjusting(), new org.lgna.croquet.triggers.ChangeEventTrigger( e ) );
	}
	@Override
	protected void localize() {
	}
	protected abstract N fromInt( int value );
	protected abstract int toInt( N value );
	public N getMinimum() {
		return this.fromInt( this.swingModel.boundedRangeModel.getMinimum() );
	}
	public N getMaximum() {
		return this.fromInt( this.swingModel.boundedRangeModel.getMaximum() );
	}
	public N getExtent() {
		return this.fromInt( this.swingModel.boundedRangeModel.getExtent() );
	}
	
	public class AtomicChange {
		private N minimum;
		private N maximum;
		private N extent;
		private N value;
		private boolean isAdjusting;
		AtomicChange minimum( N minimum ) {
			this.minimum = minimum;
			return this;
		}
		AtomicChange maximum( N maximum ) {
			this.maximum = maximum;
			return this;
		}
		AtomicChange extent( N extent ) {
			this.extent = extent;
			return this;
		}
		AtomicChange value( N value ) {
			this.value = value;
			return this;
		}
		AtomicChange isAdjusting( boolean isAdjusting ) {
			this.isAdjusting = isAdjusting;
			return this;
		}
		
		private void updateBoundedRangeModel( javax.swing.BoundedRangeModel boundedRangeModel ) {
			int minimum; 
			if( this.minimum != null ) {
				minimum = toInt( this.minimum );
			} else {
				minimum = boundedRangeModel.getMinimum();
			}
			int maximum; 
			if( this.maximum != null ) {
				maximum = toInt( this.maximum );
			} else {
				maximum = boundedRangeModel.getMaximum();
			}
			int extent; 
			if( this.extent != null ) {
				extent = toInt( this.extent );
			} else {
				extent = boundedRangeModel.getExtent();
			}
			int value; 
			if( this.value != null ) {
				value = toInt( this.value );
			} else {
				value = boundedRangeModel.getValue();
			}
			boundedRangeModel.setRangeProperties( value, extent, minimum, maximum, this.isAdjusting );
		}
	}
	
	public void setMinimum( N value ) {
		this.swingModel.boundedRangeModel.setMinimum( this.toInt( value ) );
	}
	public void setMaximum( N value ) {
		this.swingModel.boundedRangeModel.setMaximum( this.toInt( value ) );
	}
	public void setExtent( N value ) {
		this.swingModel.boundedRangeModel.setExtent( this.toInt( value ) );
	}
	
	public void setAll( AtomicChange atomicChange ) {	
		atomicChange.updateBoundedRangeModel( this.swingModel.boundedRangeModel );
	}
	@Override
	protected N getActualValue() {
		return this.fromInt( this.swingModel.boundedRangeModel.getValue() );
	}
	
	@Override
	protected void updateSwingModel(N nextValue) {
		this.swingModel.boundedRangeModel.setValue( this.toInt( nextValue ) );
	}
//	@Override
//	protected void handleValueChange(N nextValue) {
//		if( nextValue != this.previousValue ) {
//			N prevValue = this.previousValue;
//			boolean isAdjusting = false;
//			this.fireChanging( prevValue, nextValue, isAdjusting );
//			this.swingModel.boundedRangeModel.setValue( this.toInt( nextValue ) );
//			this.previousValue = nextValue;
//			this.fireChanged( prevValue, nextValue, isAdjusting );
//		}
//	}

	public org.lgna.croquet.components.Slider createSlider() {
		return new org.lgna.croquet.components.Slider( this );
	}
	public org.lgna.croquet.components.Spinner createSpinner() {
		return new org.lgna.croquet.components.Spinner( this );
	}
}
