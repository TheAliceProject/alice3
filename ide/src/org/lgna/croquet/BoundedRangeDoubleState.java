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
public abstract class BoundedRangeDoubleState extends BoundedRangeNumberState< Double > {
	public static class Details {
		private final Group group;
		private final java.util.UUID id;
		private double minimum = 0.0;
		private double maximum = 1.0;
		private double extent = 0.01;
		private double initialValue = 0.5;
		public Details( Group group, java.util.UUID id ) {
			this.group = group;
			this.id = id;
		}
		public Details minimum( double minimum ) {
			this.minimum = minimum;
			return this;
		}
		public Details maximum( double maximum ) {
			this.maximum = maximum;
			return this;
		}
		public Details extent( double extent ) {
			this.extent = extent;
			return this;
		}
		public Details initialValue( double initialValue ) {
			this.initialValue = initialValue;
			return this;
		}
		private javax.swing.BoundedRangeModel boundedRangeModel;
		private javax.swing.SpinnerModel spinnerModel;
		private synchronized javax.swing.BoundedRangeModel getBoundedRangeModel() {
			if( this.boundedRangeModel != null ) {
				//pass
			} else {
				this.boundedRangeModel = new javax.swing.DefaultBoundedRangeModel();
				int min = 0;
				int max = (int)((this.maximum-this.minimum)/this.extent);
				int ext = 1;
				int val = (int)((this.initialValue-this.minimum)/this.extent);
				this.boundedRangeModel.setRangeProperties( val, ext, min, max, false );
			}
			return this.boundedRangeModel;
		}
		private synchronized javax.swing.SpinnerModel getSpinnerModel() {
			if( this.spinnerModel != null ) {
				//pass
			} else {
				this.spinnerModel = new javax.swing.AbstractSpinnerModel() {
					public Double getNextValue() {
						throw new RuntimeException( "todo" );
					}
					public Double getPreviousValue() {
						throw new RuntimeException( "todo" );
					}
					public Double getValue() {
						throw new RuntimeException( "todo" );
					}
					public void setValue( Object value ) {
						throw new RuntimeException( "todo" );
					}
				};
			}
			return this.spinnerModel;
		}
	}
	public BoundedRangeDoubleState( Details details ) {
		super( details.group, details.id, details.getBoundedRangeModel(), details.getSpinnerModel() );
	}
	@Override
	protected void commitStateEdit( Double prevValue, Double nextValue, boolean isAdjusting, org.lgna.croquet.triggers.Trigger trigger ) {
		org.lgna.croquet.history.TransactionManager.handleBoundedRangeDoubleStateChanged( BoundedRangeDoubleState.this, nextValue, isAdjusting, trigger );
	}
	@Override
	protected Double fromInt( int value ) {
//		java.math.BigDecimal bigDecimal = new java.math.BigDecimal( value );
//		bigDecimal = bigDecimal.movePointLeft( 2 );
		return value/100.0;
	}
	@Override
	protected int toInt( Double value ) {

		return (int)(value*100);
	}
}
