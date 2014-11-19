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
public abstract class BoundedNumberState<N extends Number> extends SimpleValueState<N> {
	public static class AtomicChange<N extends Number> {
		private N minimum = null;
		private N maximum = null;
		private N stepSize = null;
		private N extent = null;
		private N value = null;
		private boolean isAdjusting;

		public AtomicChange<N> minimum( N minimum ) {
			this.minimum = minimum;
			return this;
		}

		public AtomicChange<N> maximum( N maximum ) {
			this.maximum = maximum;
			return this;
		}

		public AtomicChange<N> stepSize( N stepSize ) {
			this.stepSize = stepSize;
			return this;
		}

		public AtomicChange<N> extent( N extent ) {
			this.extent = extent;
			return this;
		}

		public AtomicChange<N> value( N value ) {
			this.value = value;
			return this;
		}

		public AtomicChange<N> isAdjusting( boolean isAdjusting ) {
			this.isAdjusting = isAdjusting;
			return this;
		}

		private void updateSwingModel( SwingModel<N> swingModel ) {
			swingModel.setAll( this.value, this.minimum, this.maximum, this.stepSize, this.extent, this.isAdjusting );
		}
	}

	public static interface SwingModel<N extends Number> {
		public javax.swing.BoundedRangeModel getBoundedRangeModel();

		public javax.swing.SpinnerNumberModel getSpinnerModel();

		public void setValue( N value );

		public void setAll( N value, N minimum, N maximum, N stepSize, N extent, boolean isAdjusting );
	}

	private final SwingModel<N> swingModel;
	private final javax.swing.event.ChangeListener changeListener = new javax.swing.event.ChangeListener() {
		//private boolean previousValueIsAdjusting = false;
		@Override
		public void stateChanged( javax.swing.event.ChangeEvent e ) {
			BoundedNumberState.this.handleStateChanged( e );
		}
	};

	public BoundedNumberState( Group group, java.util.UUID id, N initialValue, SwingModel<N> swingModel ) {
		super( group, id, initialValue );
		this.swingModel = swingModel;
		this.swingModel.getSpinnerModel().addChangeListener( this.changeListener );
	}

	@Override
	public java.util.List<? extends java.util.List<? extends PrepModel>> getPotentialPrepModelPaths( org.lgna.croquet.edits.Edit edit ) {
		return java.util.Collections.emptyList();
	}

	@Override
	public void appendRepresentation( StringBuilder sb, N value ) {
		sb.append( value );
	}

	public SwingModel<N> getSwingModel() {
		return this.swingModel;
	}

	@Override
	protected boolean isAdjustingIgnored() {
		return false;
	}

	private void handleStateChanged( javax.swing.event.ChangeEvent e ) {
		N nextValue = this.getSwingValue();
		this.changeValueFromSwing( nextValue, IsAdjusting.valueOf( this.swingModel.getBoundedRangeModel().getValueIsAdjusting() ), org.lgna.croquet.triggers.ChangeEventTrigger.createUserInstance( e ) );
	}

	@Override
	protected void localize() {
	}

	public abstract N getMinimum();

	public abstract void setMinimum( N minimum );

	public abstract N getMaximum();

	public abstract void setMaximum( N maximum );

	public N getStepSize() {
		return (N)this.getSwingModel().getSpinnerModel().getStepSize();
	}

	public void setStepSize( N stepSize ) {
		this.getSwingModel().getSpinnerModel().setStepSize( stepSize );
	}

	public void setAll( AtomicChange<N> atomicChange ) {
		atomicChange.updateSwingModel( this.swingModel );
		if( atomicChange.value != null ) {
			this.setCurrentTruthAndBeautyValue( atomicChange.value );
		}
	}

	public void setAllTransactionlessly( AtomicChange<N> atomicChange ) {
		this.pushIsInTheMidstOfAtomicChange();
		try {
			this.setAll( atomicChange );
		} finally {
			this.popIsInTheMidstOfAtomicChange();
		}
	}

	@Override
	protected void setSwingValue( N nextValue ) {
		this.swingModel.setValue( nextValue );
	}

	public org.lgna.croquet.views.Slider createSlider() {
		return new org.lgna.croquet.views.Slider( this );
	}

	public org.lgna.croquet.views.Spinner createSpinner() {
		return new org.lgna.croquet.views.Spinner( this );
	}
}
