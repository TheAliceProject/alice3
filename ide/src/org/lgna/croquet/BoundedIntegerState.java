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
public abstract class BoundedIntegerState extends BoundedNumberState< Integer > {
	public static class Details {
		private final Group group;
		private final java.util.UUID id;
		private int minimum = 0;
		private int maximum = 100;
		private int extent = 0;
		private int initialValue = 50;
		public Details( Group group, java.util.UUID id ) {
			this.group = group;
			this.id = id;
		}
		public Details minimum( int minimum ) {
			this.minimum = minimum;
			return this;
		}
		public Details maximum( int maximum ) {
			this.maximum = maximum;
			return this;
		}
		public Details extent( int extent ) {
			this.extent = extent;
			return this;
		}
		public Details initialValue( int initialValue ) {
			this.initialValue = initialValue;
			return this;
		}
		private javax.swing.BoundedRangeModel boundedRangeModel;
		private javax.swing.SpinnerModel spinnerModel;
		private synchronized javax.swing.BoundedRangeModel getBoundedRangeModel() {
			if( this.boundedRangeModel != null ) {
				//pass
			} else {
				int cappedInitialValue = Math.min( Math.max( this.initialValue, this.minimum ), this.maximum-this.extent );
				this.boundedRangeModel = new javax.swing.DefaultBoundedRangeModel( cappedInitialValue, this.extent, this.minimum, this.maximum );
			}
			return this.boundedRangeModel;
		}
		private synchronized javax.swing.SpinnerModel getSpinnerModel() {
			if( this.spinnerModel != null ) {
				//pass
			} else {
				this.spinnerModel = new javax.swing.AbstractSpinnerModel() {
					public Integer getNextValue() {
						return this.getValue() + Details.this.boundedRangeModel.getExtent();
					}
					public Integer getPreviousValue() {
						return this.getValue() - Details.this.boundedRangeModel.getExtent();
					}
					public Integer getValue() {
						return Details.this.boundedRangeModel.getValue();
					}
					public void setValue( Object value ) {
						Details.this.boundedRangeModel.setValue( (Integer)value );
					}
				};
			}
			return this.spinnerModel;
		}
	}
	public BoundedIntegerState( Details details ) {
		super( details.group, details.id, details.getBoundedRangeModel().getValue(), details.getBoundedRangeModel(), details.getSpinnerModel() );
	}
	@Override
	public Class< Integer > getItemClass() {
		return Integer.class;
	}
	@Override
	public Integer decodeValue( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		return binaryDecoder.decodeInt();
	}
	@Override
	public void encodeValue( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, Integer value ) {
		binaryEncoder.encode( value );
	}
//	@Override
//	protected void commitStateEdit( Integer prevValue, Integer nextValue, boolean isAdjusting, org.lgna.croquet.triggers.Trigger trigger ) {
//		org.lgna.croquet.history.TransactionManager.handleBoundedIntegerStateChanged( BoundedIntegerState.this, nextValue, isAdjusting, trigger );
//	}
	@Override
	protected Integer fromInt( int value ) {
		return value;
	}
	@Override
	protected int toInt( Integer value ) {
		return value;
	}
}
