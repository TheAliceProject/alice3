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

package org.alice.stageide.custom;

/**
 * @author Dennis Cosgrove
 */
public final class AudioSourceCustomExpressionCreatorComposite extends org.alice.ide.custom.CustomExpressionCreatorComposite<org.alice.stageide.custom.components.AudioSourceCustomExpressionCreatorView> {
	private static class SingletonHolder {
		private static AudioSourceCustomExpressionCreatorComposite instance = new AudioSourceCustomExpressionCreatorComposite();
	}

	public static AudioSourceCustomExpressionCreatorComposite getInstance() {
		return SingletonHolder.instance;
	}

	private static final int MARKER_MAX = 1000;

	private final org.lgna.croquet.PlainStringValue resourceSidekickLabel = this.createStringValue( "resourceState.sidekickLabel" );
	private final org.lgna.croquet.BoundedIntegerState volumeState = this.createBoundedIntegerState( "volumeState", VolumeLevelUtilities.createDetails() );
	private final org.lgna.croquet.BoundedIntegerState startMarkerState = this.createBoundedIntegerState( "startMarkerState", new BoundedIntegerDetails().minimum( 0 ).maximum( MARKER_MAX ).initialValue( 0 ) );
	private final org.lgna.croquet.BoundedIntegerState stopMarkerState = this.createBoundedIntegerState( "stopMarkerState", new BoundedIntegerDetails().minimum( 0 ).maximum( MARKER_MAX ).initialValue( MARKER_MAX ) );

	private org.lgna.croquet.event.ValueListener<Integer> startValueListiner = new org.lgna.croquet.event.ValueListener<Integer>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<Integer> e ) {
			updateStopValueIfNecessary();
		}
	};
	private org.lgna.croquet.event.ValueListener<Integer> stopValueListiner = new org.lgna.croquet.event.ValueListener<Integer>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<Integer> e ) {
			updateStartValueIfNecessary();
		}
	};

	private double toDouble( int markerValue, double defaultValue ) {
		org.lgna.common.resources.AudioResource audioResource = this.getAudioResourceExpressionState().getAudioResource();
		double duration;
		if( audioResource != null ) {
			duration = audioResource.getDuration();
		} else {
			duration = Double.NaN;
		}
		if( Double.isNaN( duration ) ) {
			return defaultValue;
		} else {
			double value = markerValue * 0.001 * duration;
			value = edu.cmu.cs.dennisc.java.lang.DoubleUtilities.round( value, 3 );
			return value;
		}
	}

	private boolean isIgnoringValueChanges;

	private void updateStartValueIfNecessary() {
		if( this.isIgnoringValueChanges ) {
			//pass
		} else {
			int start = this.startMarkerState.getValue();
			int stop = this.stopMarkerState.getValue();
			if( start > stop ) {
				this.isIgnoringValueChanges = true;
				try {
					this.startMarkerState.setValueTransactionlessly( stop );
				} finally {
					this.isIgnoringValueChanges = false;
				}
				this.getView().updatePreview();
			}
		}
	}

	private void updateStopValueIfNecessary() {
		if( this.isIgnoringValueChanges ) {
			//pass
		} else {
			int start = this.startMarkerState.getValue();
			int stop = this.stopMarkerState.getValue();
			if( start > stop ) {
				this.isIgnoringValueChanges = true;
				try {
					this.stopMarkerState.setValueTransactionlessly( start );
				} finally {
					this.isIgnoringValueChanges = false;
				}
				this.getView().updatePreview();
			}
		}
	}

	private double getStartMarkerTime() {
		int value = this.startMarkerState.getValue();
		return toDouble( value, 0.0 );
	}

	private double getStopMarkerTime() {
		int value = this.stopMarkerState.getValue();
		if( value == MARKER_MAX ) {
			return Double.NaN;
		} else {
			return toDouble( value, Double.NaN );
		}
	}

	private final org.lgna.croquet.Operation testOperation = this.createActionOperation( "test", new Action() {
		@Override
		public org.lgna.croquet.edits.Edit perform( org.lgna.croquet.history.CompletionStep<?> step, org.lgna.croquet.AbstractComposite.InternalActionOperation source ) throws org.lgna.croquet.CancelException {
			org.lgna.common.resources.AudioResource audioResource = getAudioResourceExpressionState().getAudioResource();
			double volume = VolumeLevelUtilities.toDouble( getVolumeState().getValue() );
			double startTime = getStartMarkerTime();
			double stopTime = getStopMarkerTime();
			edu.cmu.cs.dennisc.media.MediaFactory mediaFactory = edu.cmu.cs.dennisc.media.jmf.MediaFactory.getSingleton();
			edu.cmu.cs.dennisc.media.Player player = mediaFactory.createPlayer( audioResource, volume, startTime, stopTime );
			org.lgna.croquet.triggers.Trigger trigger = step.getTrigger();
			player.test( trigger.getViewController().getAwtComponent() );
			return null;
		}
	} );

	private AudioSourceCustomExpressionCreatorComposite() {
		super( java.util.UUID.fromString( "786280be-fdba-4135-bcc4-b0548ded2e50" ) );
		this.startMarkerState.addNewSchoolValueListener( this.startValueListiner );
		this.stopMarkerState.addNewSchoolValueListener( this.stopValueListiner );
	}

	public AudioResourceExpressionState getAudioResourceExpressionState() {
		return AudioResourceExpressionState.getInstance();
	}

	public org.lgna.croquet.BoundedIntegerState getVolumeState() {
		return this.volumeState;
	}

	public org.lgna.croquet.BoundedIntegerState getStartMarkerState() {
		return this.startMarkerState;
	}

	public org.lgna.croquet.BoundedIntegerState getStopMarkerState() {
		return this.stopMarkerState;
	}

	public org.lgna.croquet.Operation getTestOperation() {
		return this.testOperation;
	}

	public org.lgna.croquet.PlainStringValue getResourceSidekickLabel() {
		return this.resourceSidekickLabel;
	}

	@Override
	protected org.lgna.project.ast.Expression createValue() {
		org.lgna.project.ast.ResourceExpression resourceExpression = (org.lgna.project.ast.ResourceExpression)this.getAudioResourceExpressionState().getValue();
		if( resourceExpression != null ) {
			org.lgna.common.resources.AudioResource audioResource = (org.lgna.common.resources.AudioResource)resourceExpression.resource.getValue();
			org.lgna.project.ast.Expression arg0Expression;
			if( audioResource != null ) {
				arg0Expression = new org.lgna.project.ast.ResourceExpression( org.lgna.common.resources.AudioResource.class, audioResource );
			} else {
				arg0Expression = new org.lgna.project.ast.NullLiteral();
			}

			double volume = VolumeLevelUtilities.toDouble( this.getVolumeState().getValue() );
			double startTime = this.getStartMarkerTime();
			double stopTime = this.getStopMarkerTime();

			// apologies for the negative logic
			boolean isNotDefaultVolume = org.lgna.story.AudioSource.isWithinReasonableEpsilonOfDefaultVolume( volume ) == false;
			boolean isNotDefaultStartTime = org.lgna.story.AudioSource.isWithinReasonableEpsilonOfDefaultStartTime( startTime ) == false;
			boolean isNotDefaultStopTime = org.lgna.story.AudioSource.isDefaultStopTime_aka_NaN( stopTime ) == false;

			if( isNotDefaultVolume || isNotDefaultStartTime || isNotDefaultStopTime ) {
				org.lgna.project.ast.DoubleLiteral volumeLiteral = new org.lgna.project.ast.DoubleLiteral( volume );
				if( isNotDefaultStartTime || isNotDefaultStopTime ) {
					org.lgna.project.ast.DoubleLiteral startTimeLiteral = new org.lgna.project.ast.DoubleLiteral( startTime );
					if( isNotDefaultStopTime ) {
						org.lgna.project.ast.DoubleLiteral stopTimeLiteral = new org.lgna.project.ast.DoubleLiteral( stopTime );

						org.lgna.project.ast.JavaConstructor constructor = org.lgna.project.ast.JavaConstructor.getInstance(
								org.lgna.story.AudioSource.class,
								org.lgna.common.resources.AudioResource.class,
								Number.class,
								Number.class,
								Number.class );
						return org.lgna.project.ast.AstUtilities.createInstanceCreation( constructor, arg0Expression, volumeLiteral, startTimeLiteral, stopTimeLiteral );
					} else {
						org.lgna.project.ast.JavaConstructor constructor = org.lgna.project.ast.JavaConstructor.getInstance(
								org.lgna.story.AudioSource.class,
								org.lgna.common.resources.AudioResource.class,
								Number.class,
								Number.class );
						return org.lgna.project.ast.AstUtilities.createInstanceCreation( constructor, arg0Expression, volumeLiteral, startTimeLiteral );
					}
				} else {
					org.lgna.project.ast.JavaConstructor constructor = org.lgna.project.ast.JavaConstructor.getInstance(
							org.lgna.story.AudioSource.class,
							org.lgna.common.resources.AudioResource.class,
							Number.class );
					return org.lgna.project.ast.AstUtilities.createInstanceCreation( constructor, arg0Expression, volumeLiteral );
				}
			} else {
				org.lgna.project.ast.JavaConstructor constructor = org.lgna.project.ast.JavaConstructor.getInstance(
						org.lgna.story.AudioSource.class,
						org.lgna.common.resources.AudioResource.class );
				return org.lgna.project.ast.AstUtilities.createInstanceCreation( constructor, arg0Expression );
			}
		} else {
			return null;
		}
	}

	@Override
	protected Status getStatusPreRejectorCheck( org.lgna.croquet.history.CompletionStep<?> step ) {
		return null;
	}

	private static org.lgna.project.ast.Expression getArgumentExpressionAt( org.lgna.project.ast.InstanceCreation instanceCreation, int index ) {
		assert instanceCreation.requiredArguments.size() >= index;
		org.lgna.project.ast.AbstractArgument arg = instanceCreation.requiredArguments.get( index );
		assert arg != null;
		assert arg instanceof org.lgna.project.ast.SimpleArgument;
		return ( (org.lgna.project.ast.SimpleArgument)arg ).expression.getValue();
	}

	@Override
	protected void initializeToPreviousExpression( org.lgna.project.ast.Expression expression ) {
		org.lgna.project.ast.ResourceExpression resourceExpression = null;
		org.lgna.common.resources.AudioResource audioResource = null;
		double volumeLevel = 1.0;
		double startTime = 0.0;
		double stopTime = Double.NaN;

		if( expression instanceof org.lgna.project.ast.InstanceCreation ) {
			org.lgna.project.ast.InstanceCreation instanceCreation = (org.lgna.project.ast.InstanceCreation)expression;
			int n = instanceCreation.requiredArguments.size();
			if( n > 0 ) {
				org.lgna.project.ast.Expression expression0 = getArgumentExpressionAt( instanceCreation, 0 );
				if( expression0 instanceof org.lgna.project.ast.ResourceExpression ) {
					resourceExpression = (org.lgna.project.ast.ResourceExpression)expression0;
					org.lgna.common.Resource resource = resourceExpression.resource.getValue();
					if( resource instanceof org.lgna.common.resources.AudioResource ) {
						audioResource = (org.lgna.common.resources.AudioResource)resource;
					}
					if( n > 1 ) {
						if( n > 2 ) {
							if( n > 3 ) {
								org.lgna.project.ast.Expression expression3 = getArgumentExpressionAt( instanceCreation, 3 );
								if( expression3 instanceof org.lgna.project.ast.DoubleLiteral ) {
									org.lgna.project.ast.DoubleLiteral stopTimeLiteral = (org.lgna.project.ast.DoubleLiteral)expression3;
									stopTime = stopTimeLiteral.value.getValue();
								}
							}
							org.lgna.project.ast.Expression expression2 = getArgumentExpressionAt( instanceCreation, 2 );
							if( expression2 instanceof org.lgna.project.ast.DoubleLiteral ) {
								org.lgna.project.ast.DoubleLiteral startTimeLiteral = (org.lgna.project.ast.DoubleLiteral)expression2;
								startTime = startTimeLiteral.value.getValue();
							}
						}
						org.lgna.project.ast.Expression expression1 = getArgumentExpressionAt( instanceCreation, 1 );
						if( expression1 instanceof org.lgna.project.ast.DoubleLiteral ) {
							org.lgna.project.ast.DoubleLiteral volumeLevelLiteral = (org.lgna.project.ast.DoubleLiteral)expression1;
							volumeLevel = volumeLevelLiteral.value.getValue();
						}
					}
				}
			}
		}
		this.getVolumeState().setValueTransactionlessly( VolumeLevelUtilities.toInt( volumeLevel ) );
		this.getAudioResourceExpressionState().setValueTransactionlessly( resourceExpression );
	}

	@Override
	protected org.alice.stageide.custom.components.AudioSourceCustomExpressionCreatorView createView() {
		return new org.alice.stageide.custom.components.AudioSourceCustomExpressionCreatorView( this );
	}
}
