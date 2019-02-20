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
public abstract class BoundedDoubleState extends BoundedNumberState<Double> {
	public static class Details {
		private static int toInt( double d, double minimum, double maximum, double stepSize ) {
			double total = maximum - minimum;
			double portion = d - minimum;
			double numberOfSteps = Math.ceil( total / stepSize );
			return (int)( ( portion / total ) * numberOfSteps );
		}

		private static double toDouble( int i, double minimum, double stepSize ) {
			return minimum + ( i * stepSize );
		}

		private static final int MINIMUM = 0;
		private static final int EXTENT = 0;

		private static class DoubleSwingModel implements SwingModel<Double> {
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
							double value = (Double)this.getValue();
							double minimum = (Double)this.getMinimum();
							double maximum = (Double)this.getMaximum();
							double stepSize = this.getStepSize().doubleValue();
							int v = toInt( value, minimum, maximum, stepSize );
							int max = toInt( maximum, minimum, maximum, stepSize );

							boundedRangeModel.setRangeProperties( v, EXTENT, MINIMUM, max, isAdjusting );
						} finally {
							isInTheMidstOfStateChanged = false;
						}
					}
				}
			}

			private class CustomBoundedRangeModel extends DefaultBoundedRangeModel {
				public CustomBoundedRangeModel( Details details ) {
					super( details.toIntInitialValue(), EXTENT, MINIMUM, details.toIntMaximum() );
				}

				@Override
				protected void fireStateChanged() {
					super.fireStateChanged();
					if( isInTheMidstOfStateChanged ) {
						//pass
					} else {
						isInTheMidstOfStateChanged = true;
						try {
							int v = this.getValue();
							//spinnerModel.setMaximum( this.getMaximum() );
							spinnerModel.setValue( toDouble( v, ( (Number)spinnerModel.getMinimum() ).doubleValue(), spinnerModel.getStepSize().doubleValue() ) );
						} finally {
							isInTheMidstOfStateChanged = false;
						}
					}
				}
			}

			private final CustomBoundedRangeModel boundedRangeModel;
			private final CustomSpinnerNumberModel spinnerModel;

			public DoubleSwingModel( Details details ) {
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
			public void setValue( Double value ) {
				this.spinnerModel.setValue( value );
			}

			@Override
			public void setAll( Double value, Double minimum, Double maximum, Double stepSize, Double extent, boolean isAdjusting ) {
				if( minimum != null ) {
					Number prevMinimum = (Number)this.spinnerModel.getMinimum();
					if( minimum.doubleValue() == prevMinimum.doubleValue() ) {
						//pass
					} else {
						this.spinnerModel.setMinimum( minimum );
					}
				}
				if( maximum != null ) {
					Number prevMaximum = (Number)this.spinnerModel.getMaximum();
					if( maximum.doubleValue() == prevMaximum.doubleValue() ) {
						//pass
					} else {
						this.spinnerModel.setMaximum( maximum );
					}
				}
				if( stepSize != null ) {
					Number prevStepSize = this.spinnerModel.getStepSize();
					if( stepSize.doubleValue() == prevStepSize.doubleValue() ) {
						//pass
					} else {
						this.spinnerModel.setStepSize( stepSize );
					}
				}
				if( value != null ) {
					Number prevValue = (Number)this.spinnerModel.getValue();
					if( value.doubleValue() == prevValue.doubleValue() ) {
						//pass
					} else {
						this.spinnerModel.setValue( stepSize );
					}
				}
			}
		}

		private final Group group;
		private final UUID id;
		private double minimum = 0.0;
		private double maximum = 1.0;
		private double stepSize = 0.01;
		private double initialValue = 0.5;

		public Details( Group group, UUID id ) {
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

		public Details stepSize( double stepSize ) {
			this.stepSize = stepSize;
			return this;
		}

		public Details initialValue( double initialValue ) {
			this.initialValue = initialValue;
			return this;
		}

		private synchronized DoubleSwingModel createSwingModel() {
			return new DoubleSwingModel( this );
		}

		private int toIntInitialValue() {
			return toInt( this.initialValue, this.minimum, this.maximum, this.stepSize );
		}

		private int toIntMaximum() {
			return toInt( this.maximum, this.minimum, this.maximum, this.stepSize );
		}
	}

	public BoundedDoubleState( Details details ) {
		super( details.group, details.id, details.initialValue, details.createSwingModel() );
	}

	@Override
	public Double decodeValue( BinaryDecoder binaryDecoder ) {
		return binaryDecoder.decodeDouble();
	}

	@Override
	public void encodeValue( BinaryEncoder binaryEncoder, Double value ) {
		binaryEncoder.encode( value );
	}

	@Override
	public Double getMinimum() {
		return (Double)this.getSwingModel().getSpinnerModel().getMinimum();
	}

	@Override
	public void setMinimum( Double minimum ) {
		this.getSwingModel().getSpinnerModel().setMinimum( minimum );
	}

	@Override
	public Double getMaximum() {
		return (Double)this.getSwingModel().getSpinnerModel().getMaximum();
	}

	@Override
	public void setMaximum( Double maximum ) {
		this.getSwingModel().getSpinnerModel().setMaximum( maximum );
	}

	@Override
	protected Double getSwingValue() {
		Number value = (Number)this.getSwingModel().getSpinnerModel().getValue();
		return value.doubleValue();
	}
}
