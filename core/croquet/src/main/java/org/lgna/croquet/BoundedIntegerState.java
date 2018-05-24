/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.lgna.croquet;

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.SpinnerNumberModel;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class BoundedIntegerState extends BoundedNumberState<Integer> {
	public static class Details {
		private static class IntegerSwingModel implements SwingModel<Integer> {
			private boolean isInTheMidstOfStateChanged = false;

			private class CustomSpinnerNumberModel extends SpinnerNumberModel {
				public CustomSpinnerNumberModel( Details details ) {
					super( details.initialValue, details.minimum, details.maximum, details.stepSize );
				}

				@Override
				protected void fireStateChanged() {
					super.fireStateChanged();
					if( isInTheMidstOfStateChanged ) {
						//pass
					} else {
						isInTheMidstOfStateChanged = true;
						try {
							boolean isAdjusting = false;
							boundedRangeModel.setRangeProperties( (Integer)this.getValue(), boundedRangeModel.getExtent(), (Integer)this.getMinimum(), (Integer)this.getMaximum(), isAdjusting );
						} finally {
							isInTheMidstOfStateChanged = false;
						}
					}
				}
			}

			private class CustomBoundedRangeModel extends DefaultBoundedRangeModel {
				public CustomBoundedRangeModel( Details details ) {
					super(
							Math.min( Math.max( details.initialValue, details.minimum ), details.maximum - details.extent ),
							details.extent,
							details.minimum,
							details.maximum );
				}

				@Override
				protected void fireStateChanged() {
					super.fireStateChanged();
					if( isInTheMidstOfStateChanged ) {
						//pass
					} else {
						isInTheMidstOfStateChanged = true;
						try {
							spinnerModel.setMinimum( this.getMinimum() );
							spinnerModel.setMaximum( this.getMaximum() );
							spinnerModel.setValue( this.getValue() );
						} finally {
							isInTheMidstOfStateChanged = false;
						}
					}
				}
			}

			private final CustomBoundedRangeModel boundedRangeModel;
			private final CustomSpinnerNumberModel spinnerModel;

			public IntegerSwingModel( Details details ) {
				this.spinnerModel = new CustomSpinnerNumberModel( details );
				this.boundedRangeModel = new CustomBoundedRangeModel( details );
			}

			@Override
			public BoundedRangeModel getBoundedRangeModel() {
				return this.boundedRangeModel;
			}

			@Override
			public SpinnerNumberModel getSpinnerModel() {
				return this.spinnerModel;
			}

			@Override
			public void setValue( Integer value ) {
				this.boundedRangeModel.setValue( value );
			}

			@Override
			public void setAll( Integer value, Integer minimum, Integer maximum, Integer stepSize, Integer extent, boolean isAdjusting ) {
				if( value != null ) {
					//pass
				} else {
					value = this.boundedRangeModel.getValue();
				}
				if( extent != null ) {
					//pass
				} else {
					extent = this.boundedRangeModel.getExtent();
				}
				if( minimum != null ) {
					//pass
				} else {
					minimum = this.boundedRangeModel.getMinimum();
				}
				if( maximum != null ) {
					//pass
				} else {
					maximum = this.boundedRangeModel.getMaximum();
				}
				this.boundedRangeModel.setRangeProperties( value, extent, minimum, maximum, isAdjusting );
				if( stepSize != null ) {
					Number prevStepSize = this.spinnerModel.getStepSize();
					if( stepSize.doubleValue() == prevStepSize.doubleValue() ) {
						//pass
					} else {
						this.spinnerModel.setStepSize( stepSize );
					}
				}
			}
		}

		private final Group group;
		private final UUID id;
		private int minimum = 0;
		private int maximum = 100;
		private int extent = 0;
		private int stepSize = 1;
		private int initialValue = 50;

		public Details( Group group, UUID id ) {
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

		public Details stepSize( int stepSize ) {
			this.stepSize = stepSize;
			return this;
		}

		public Details initialValue( int initialValue ) {
			this.initialValue = initialValue;
			return this;
		}

		private synchronized IntegerSwingModel createSwingModel() {
			return new IntegerSwingModel( this );
		}
	}

	public BoundedIntegerState( Details details ) {
		super( details.group, details.id, details.initialValue, details.createSwingModel() );
	}

	@Override
	public Class<Integer> getItemClass() {
		return Integer.class;
	}

	@Override
	public Integer decodeValue( BinaryDecoder binaryDecoder ) {
		return binaryDecoder.decodeInt();
	}

	@Override
	public void encodeValue( BinaryEncoder binaryEncoder, Integer value ) {
		binaryEncoder.encode( value );
	}

	@Override
	public Integer getMinimum() {
		return this.getSwingModel().getBoundedRangeModel().getMinimum();
	}

	@Override
	public void setMinimum( Integer minimum ) {
		this.getSwingModel().getBoundedRangeModel().setMinimum( minimum );
	}

	@Override
	public Integer getMaximum() {
		return this.getSwingModel().getBoundedRangeModel().getMaximum();
	}

	@Override
	public void setMaximum( Integer maximum ) {
		this.getSwingModel().getBoundedRangeModel().setMaximum( maximum );
	}

	@Override
	protected Integer getSwingValue() {
		return this.getSwingModel().getBoundedRangeModel().getValue();
	}
}
