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
public abstract class BoundedRangeNumberState< N extends Number > extends State< N > {
	private final javax.swing.BoundedRangeModel boundedRangeModel;
	private final javax.swing.SpinnerModel spinnerModel;
	private N previousValue;
	private final javax.swing.event.ChangeListener changeListener = new javax.swing.event.ChangeListener() {
		//private boolean previousValueIsAdjusting = false;
		public void stateChanged( javax.swing.event.ChangeEvent e ) {
			BoundedRangeNumberState.this.handleStateChanged( e );
		}
	};

	public BoundedRangeNumberState( Group group, java.util.UUID id, javax.swing.BoundedRangeModel boundedRangeModel, javax.swing.SpinnerModel spinnerModel ) {
		super( group, id );
		this.boundedRangeModel = boundedRangeModel;
		this.spinnerModel = spinnerModel;
		this.previousValue = this.getValue();
		this.boundedRangeModel.addChangeListener( this.changeListener );
	}
	protected abstract N fromInt( int value );
	protected abstract int toInt( N value );
	public N getMinimum() {
		return this.fromInt( this.boundedRangeModel.getMinimum() );
	}
	public N getMaximum() {
		return this.fromInt( this.boundedRangeModel.getMaximum() );
	}
	public N getExtent() {
		return this.fromInt( this.boundedRangeModel.getExtent() );
	}
	@Override
	protected void localize() {
	}
	private void handleStateChanged( javax.swing.event.ChangeEvent e ) {
		javax.swing.BoundedRangeModel boundedRangeModel = this.getBoundedRangeModel();
		if( this.isAppropriateToComplete() ) {
			this.commitStateEdit( this.previousValue, this.getValue(), boundedRangeModel.getValueIsAdjusting(), new org.lgna.croquet.triggers.ChangeEventTrigger( e ) );
		}
		this.fireChanged( this.previousValue, this.getValue(), boundedRangeModel.getValueIsAdjusting() );
	}
	@Override
	public N getValue() {
		return this.fromInt( this.boundedRangeModel.getValue() );
	}
	public void setValue( N value ) {
		if( value != this.previousValue ) {
			N prevValue = this.previousValue;
			boolean isAdjusting = false;
			this.fireChanging( prevValue, value, isAdjusting );
			this.getBoundedRangeModel().setValue( this.toInt( value ) );
			this.previousValue = value;
			this.fireChanged( prevValue, value, isAdjusting );
		}
	}

	public javax.swing.BoundedRangeModel getBoundedRangeModel() {
		return this.boundedRangeModel;
	}
	public javax.swing.SpinnerModel getSpinnerModel() {
		return this.spinnerModel;
	}
	public org.lgna.croquet.components.Slider createSlider() {
		return new org.lgna.croquet.components.Slider( this );
	}
	public org.lgna.croquet.components.Spinner createSpinner() {
		return new org.lgna.croquet.components.Spinner( this );
	}
}
