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
public abstract class BoundedRangeIntegerState extends State< Integer > {
	private int previousValue;
	private final javax.swing.BoundedRangeModel boundedRangeModel = new javax.swing.DefaultBoundedRangeModel();
	private final javax.swing.event.ChangeListener changeListener = new javax.swing.event.ChangeListener() {
		//private boolean previousValueIsAdjusting = false;
		public void stateChanged( javax.swing.event.ChangeEvent e ) {
			org.lgna.croquet.history.TransactionManager.handleBoundedRangeIntegerStateChanged( BoundedRangeIntegerState.this, e, boundedRangeModel.getValue(), boundedRangeModel.getValueIsAdjusting() );
			fireChanged( previousValue, boundedRangeModel.getValue(), boundedRangeModel.getValueIsAdjusting() );
		}
	};
	javax.swing.SpinnerModel spinnerModel = new javax.swing.AbstractSpinnerModel() {
		public Integer getNextValue() {
			return this.getValue()+1;
		}
		public Integer getPreviousValue() {
			return this.getValue()-1;
		}
		public Integer getValue() {
			return BoundedRangeIntegerState.this.boundedRangeModel.getValue();
		}
		public void setValue( Object value ) {
			BoundedRangeIntegerState.this.boundedRangeModel.setValue( (Integer)value );
		}
	};

	public BoundedRangeIntegerState( Group group, java.util.UUID id, int minimum, int value, int maximum ) {
		super( group, id );
		this.boundedRangeModel.setMinimum( minimum );
		this.boundedRangeModel.setMaximum( maximum );
		this.boundedRangeModel.setValue( value );
		this.previousValue = this.boundedRangeModel.getValue();
		this.boundedRangeModel.addChangeListener( this.changeListener );
	}

	@Override
	protected void localize() {
	}
	public javax.swing.BoundedRangeModel getBoundedRangeModel() {
		return this.boundedRangeModel;
	}
	public javax.swing.SpinnerModel getSpinnerModel() {
		return this.spinnerModel;
	}

	public int getMinimum() {
		return this.boundedRangeModel.getMinimum();
	}
	public int getMaximum() {
		return this.boundedRangeModel.getMaximum();
	}
	@Override
	public Integer getValue() {
		return this.boundedRangeModel.getValue();
	}
	public void setValue( Integer value ) {
		if( value != this.previousValue ) {
			Integer prevValue = this.previousValue;
			boolean isAdjusting = false;
			this.fireChanging( prevValue, value, isAdjusting );
			this.boundedRangeModel.setValue( value );
			this.previousValue = value;
			this.fireChanged( prevValue, value, isAdjusting );
		}
	}

	@Override
	protected StringBuilder updateTutorialStepText( StringBuilder rv, org.lgna.croquet.history.Step< ? > step, org.lgna.croquet.edits.Edit< ? > edit, org.lgna.croquet.UserInformation userInformation ) {
		return rv;
	}
	
	public org.lgna.croquet.components.Slider createSlider() {
		return new org.lgna.croquet.components.Slider( this );
	}
	public org.lgna.croquet.components.Spinner createSpinner() {
		return new org.lgna.croquet.components.Spinner( this );
	}
}
