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

import edu.cmu.cs.dennisc.java.lang.DoubleUtilities;
import edu.cmu.cs.dennisc.media.Player;
import edu.cmu.cs.dennisc.media.jmf.MediaFactory;
import org.alice.ide.custom.CustomExpressionCreatorComposite;
import org.alice.stageide.custom.components.AudioSourceCustomExpressionCreatorView;
import org.lgna.common.Resource;
import org.lgna.common.resources.AudioResource;
import org.lgna.croquet.BoundedIntegerState;
import org.lgna.croquet.CancelException;
import org.lgna.croquet.Operation;
import org.lgna.croquet.PlainStringValue;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.triggers.Trigger;
import org.lgna.project.ast.AbstractArgument;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.DoubleLiteral;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.InstanceCreation;
import org.lgna.project.ast.JavaConstructor;
import org.lgna.project.ast.NullLiteral;
import org.lgna.project.ast.ResourceExpression;
import org.lgna.project.ast.SimpleArgument;
import org.lgna.story.AudioSource;

import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public final class AudioSourceCustomExpressionCreatorComposite extends CustomExpressionCreatorComposite<AudioSourceCustomExpressionCreatorView> {
	private static class SingletonHolder {
		private static AudioSourceCustomExpressionCreatorComposite instance = new AudioSourceCustomExpressionCreatorComposite();
	}

	public static AudioSourceCustomExpressionCreatorComposite getInstance() {
		return SingletonHolder.instance;
	}

	private static final int MARKER_MAX = 1000;

	private final PlainStringValue resourceSidekickLabel = this.createStringValue( "resourceState.sidekickLabel" );
	private final BoundedIntegerState volumeState = this.createBoundedIntegerState( "volumeState", VolumeLevelUtilities.createDetails() );
	private final BoundedIntegerState startMarkerState = this.createBoundedIntegerState( "startMarkerState", new BoundedIntegerDetails().minimum( 0 ).maximum( MARKER_MAX ).initialValue( 0 ) );
	private final BoundedIntegerState stopMarkerState = this.createBoundedIntegerState( "stopMarkerState", new BoundedIntegerDetails().minimum( 0 ).maximum( MARKER_MAX ).initialValue( MARKER_MAX ) );

	private ValueListener<Integer> startValueListiner = new ValueListener<Integer>() {
		@Override
		public void valueChanged( ValueEvent<Integer> e ) {
			updateStopValueIfNecessary();
		}
	};
	private ValueListener<Integer> stopValueListiner = new ValueListener<Integer>() {
		@Override
		public void valueChanged( ValueEvent<Integer> e ) {
			updateStartValueIfNecessary();
		}
	};

	private double toDouble( int markerValue, double defaultValue ) {
		AudioResource audioResource = this.getAudioResourceExpressionState().getAudioResource();
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
			value = DoubleUtilities.round( value, 3 );
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

	private final Operation testOperation = this.createActionOperation( "test", new Action() {
		@Override
		public Edit perform( UserActivity userActivity, InternalActionOperation source ) throws CancelException {
			AudioResource audioResource = getAudioResourceExpressionState().getAudioResource();
			double volume = VolumeLevelUtilities.toDouble( getVolumeState().getValue() );
			double startTime = getStartMarkerTime();
			double stopTime = getStopMarkerTime();
			edu.cmu.cs.dennisc.media.MediaFactory mediaFactory = MediaFactory.getSingleton();
			Player player = mediaFactory.createPlayer( audioResource, volume, startTime, stopTime );
			player.test( null);
			return null;
		}
	} );

	private AudioSourceCustomExpressionCreatorComposite() {
		super( UUID.fromString( "786280be-fdba-4135-bcc4-b0548ded2e50" ) );
		this.startMarkerState.addNewSchoolValueListener( this.startValueListiner );
		this.stopMarkerState.addNewSchoolValueListener( this.stopValueListiner );
	}

	public AudioResourceExpressionState getAudioResourceExpressionState() {
		return AudioResourceExpressionState.getInstance();
	}

	public BoundedIntegerState getVolumeState() {
		return this.volumeState;
	}

	public BoundedIntegerState getStartMarkerState() {
		return this.startMarkerState;
	}

	public BoundedIntegerState getStopMarkerState() {
		return this.stopMarkerState;
	}

	public Operation getTestOperation() {
		return this.testOperation;
	}

	public PlainStringValue getResourceSidekickLabel() {
		return this.resourceSidekickLabel;
	}

	@Override
	protected Expression createValue() {
		ResourceExpression resourceExpression = (ResourceExpression)this.getAudioResourceExpressionState().getValue();
		if( resourceExpression != null ) {
			AudioResource audioResource = (AudioResource)resourceExpression.resource.getValue();
			Expression arg0Expression;
			if( audioResource != null ) {
				arg0Expression = new ResourceExpression( AudioResource.class, audioResource );
			} else {
				arg0Expression = new NullLiteral();
			}

			double volume = VolumeLevelUtilities.toDouble( this.getVolumeState().getValue() );
			double startTime = this.getStartMarkerTime();
			double stopTime = this.getStopMarkerTime();

			// apologies for the negative logic
			boolean isNotDefaultVolume = AudioSource.isWithinReasonableEpsilonOfDefaultVolume( volume ) == false;
			boolean isNotDefaultStartTime = AudioSource.isWithinReasonableEpsilonOfDefaultStartTime( startTime ) == false;
			boolean isNotDefaultStopTime = AudioSource.isDefaultStopTime_aka_NaN( stopTime ) == false;

			if( isNotDefaultVolume || isNotDefaultStartTime || isNotDefaultStopTime ) {
				DoubleLiteral volumeLiteral = new DoubleLiteral( volume );
				if( isNotDefaultStartTime || isNotDefaultStopTime ) {
					DoubleLiteral startTimeLiteral = new DoubleLiteral( startTime );
					if( isNotDefaultStopTime ) {
						DoubleLiteral stopTimeLiteral = new DoubleLiteral( stopTime );

						JavaConstructor constructor = JavaConstructor.getInstance(
								AudioSource.class,
								AudioResource.class,
								Number.class,
								Number.class,
								Number.class );
						return AstUtilities.createInstanceCreation( constructor, arg0Expression, volumeLiteral, startTimeLiteral, stopTimeLiteral );
					} else {
						JavaConstructor constructor = JavaConstructor.getInstance(
								AudioSource.class,
								AudioResource.class,
								Number.class,
								Number.class );
						return AstUtilities.createInstanceCreation( constructor, arg0Expression, volumeLiteral, startTimeLiteral );
					}
				} else {
					JavaConstructor constructor = JavaConstructor.getInstance(
							AudioSource.class,
							AudioResource.class,
							Number.class );
					return AstUtilities.createInstanceCreation( constructor, arg0Expression, volumeLiteral );
				}
			} else {
				JavaConstructor constructor = JavaConstructor.getInstance(
						AudioSource.class,
						AudioResource.class );
				return AstUtilities.createInstanceCreation( constructor, arg0Expression );
			}
		} else {
			return null;
		}
	}

	@Override
	protected Status getStatusPreRejectorCheck() {
		return null;
	}

	private static Expression getArgumentExpressionAt( InstanceCreation instanceCreation, int index ) {
		assert instanceCreation.requiredArguments.size() >= index;
		AbstractArgument arg = instanceCreation.requiredArguments.get( index );
		assert arg != null;
		assert arg instanceof SimpleArgument;
		return ( (SimpleArgument)arg ).expression.getValue();
	}

	@Override
	protected void initializeToPreviousExpression( Expression expression ) {
		ResourceExpression resourceExpression = null;
		AudioResource audioResource = null;
		double volumeLevel = 1.0;
		double startTime = 0.0;
		double stopTime = Double.NaN;

		if( expression instanceof InstanceCreation ) {
			InstanceCreation instanceCreation = (InstanceCreation)expression;
			int n = instanceCreation.requiredArguments.size();
			if( n > 0 ) {
				Expression expression0 = getArgumentExpressionAt( instanceCreation, 0 );
				if( expression0 instanceof ResourceExpression ) {
					resourceExpression = (ResourceExpression)expression0;
					Resource resource = resourceExpression.resource.getValue();
					if( resource instanceof AudioResource ) {
						audioResource = (AudioResource)resource;
					}
					if( n > 1 ) {
						if( n > 2 ) {
							if( n > 3 ) {
								Expression expression3 = getArgumentExpressionAt( instanceCreation, 3 );
								if( expression3 instanceof DoubleLiteral ) {
									DoubleLiteral stopTimeLiteral = (DoubleLiteral)expression3;
									stopTime = stopTimeLiteral.value.getValue();
								}
							}
							Expression expression2 = getArgumentExpressionAt( instanceCreation, 2 );
							if( expression2 instanceof DoubleLiteral ) {
								DoubleLiteral startTimeLiteral = (DoubleLiteral)expression2;
								startTime = startTimeLiteral.value.getValue();
							}
						}
						Expression expression1 = getArgumentExpressionAt( instanceCreation, 1 );
						if( expression1 instanceof DoubleLiteral ) {
							DoubleLiteral volumeLevelLiteral = (DoubleLiteral)expression1;
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
	protected AudioSourceCustomExpressionCreatorView createView() {
		return new AudioSourceCustomExpressionCreatorView( this );
	}
}
