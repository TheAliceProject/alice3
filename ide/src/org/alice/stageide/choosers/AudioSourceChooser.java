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
package org.alice.stageide.choosers;

class TimeSlider extends javax.swing.JSlider {
	private javax.swing.event.ChangeListener changeAdapter = new javax.swing.event.ChangeListener() {
		public void stateChanged(javax.swing.event.ChangeEvent e) {
			TimeSlider.this.updateTime();
		}
	};
	private double duration;
	private double time;
	public TimeSlider() {
		this.setMaximum( 1000 );
		this.setDuration( Double.NaN );
	}
	
	@Override
	public void addNotify() {
		super.addNotify();
		this.addChangeListener( this.changeAdapter );
	}
	@Override
	public void removeNotify() {
		this.removeChangeListener( this.changeAdapter );
		super.removeNotify();
	}
	private void updateValue() {
		if( Double.isNaN( this.time ) || Double.isNaN( this.duration ) ) {
			this.setValue( this.getMaximum() );
		} else {
			this.setValue( (int)((this.time/this.duration)*this.getMaximum()) );
		}
	}
	private void updateTime() {
		if( Double.isNaN( this.duration ) ) {
			//pass
		} else {
			this.time = this.getValue()*this.duration/this.getMaximum();
		}
	}
	
	public double getDuration() {
		return this.duration;
	}
	public void setDuration( double duration ) {
		this.duration = duration;
		this.setEnabled( Double.isNaN( this.duration )==false );
		this.updateValue();
	}
	private static java.text.NumberFormat format = new java.text.DecimalFormat( "0.00" );
	public double getTime() {
		if( Double.isNaN( this.time ) ) {
			return this.time;
		} else {
			return Double.parseDouble( format.format( this.time ) );
		}
	}
	public void setTime( double time ) {
		this.time = time;
		this.updateValue();
	}
}

/**
 * @author Dennis Cosgrove
 */
public class AudioSourceChooser extends org.alice.ide.choosers.AbstractRowsPaneChooser< org.lgna.project.ast.InstanceCreation > {
	class BogusNode extends org.lgna.project.ast.AbstractNode {
		private org.lgna.project.ast.ExpressionProperty bogusProperty = new org.lgna.project.ast.ExpressionProperty( this ) {
			@Override
			public org.lgna.project.ast.AbstractType<?,?,?> getExpressionType() {
				return org.lgna.project.ast.JavaType.getInstance( org.lgna.common.resources.AudioResource.class );
			}
		};
		@Override
		public void firePropertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			super.firePropertyChanged( e );
			AudioSourceChooser.this.startTimeSlider.setTime( 0.0 );
			AudioSourceChooser.this.stopTimeSlider.setTime( Double.NaN );
			
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: update OK button?" );
//			edu.cmu.cs.dennisc.croquet.InputPanel< ? > inputPanel = AudioSourceChooser.this.getInputPanel();
//			if( inputPanel != null ) {
//				inputPanel.updateOKButton();
//			}
		}
	}
	
	private BogusNode bogusNode = new BogusNode();
	private org.lgna.croquet.components.Component< ? > dropDown;
	
	private org.alice.stageide.controls.VolumeLevelControl volumeLevelControl = new org.alice.stageide.controls.VolumeLevelControl();
	private TimeSlider startTimeSlider = new TimeSlider();
	private TimeSlider stopTimeSlider = new TimeSlider();
	
	class TestOperation extends org.alice.ide.operations.InconsequentialActionOperation {
		public TestOperation() {
			super( java.util.UUID.fromString( "6d1c60d4-bf5d-43ff-9b65-797eebf21a90" ) );
			this.setName( "test" );
		}
		@Override
		protected void performInternal( org.lgna.croquet.history.CompletionStep<?> step ) {
			org.lgna.story.AudioSource audioSource = getAudioSource();
			edu.cmu.cs.dennisc.media.MediaFactory mediaFactory = edu.cmu.cs.dennisc.media.jmf.MediaFactory.getSingleton();
			edu.cmu.cs.dennisc.media.Player player = mediaFactory.createPlayer( audioSource.getAudioResource(), audioSource.getVolume(), audioSource.getStartTime(), audioSource.getStopTime() );
			org.lgna.croquet.triggers.Trigger trigger = step.getTrigger();
			player.test( trigger.getViewController().getAwtComponent() );
		}
	};
	private TestOperation testOperation = new TestOperation();

	private static org.lgna.project.ast.Expression getArgumentExpressionAt( org.lgna.project.ast.InstanceCreation instanceCreation, int index ) {
		assert instanceCreation.requiredArguments.size() >= index;
		org.lgna.project.ast.AbstractArgument arg = instanceCreation.requiredArguments.get( index );
		assert arg != null;
		assert arg instanceof org.lgna.project.ast.SimpleArgument;
		return ((org.lgna.project.ast.SimpleArgument)arg).expression.getValue();
	}
	public AudioSourceChooser() {
		org.lgna.project.ast.ResourceExpression resourceExpression = null;
		org.lgna.common.resources.AudioResource audioResource = null;
		double volumeLevel = 1.0;
		double startTime = 0.0;
		double stopTime = Double.NaN;

		org.lgna.project.ast.Expression previousExpression = this.getPreviousExpression();
		if( previousExpression instanceof org.lgna.project.ast.InstanceCreation ) {
			org.lgna.project.ast.InstanceCreation instanceCreation = (org.lgna.project.ast.InstanceCreation)previousExpression;
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
		this.bogusNode.bogusProperty.setValue( resourceExpression );
		this.volumeLevelControl.setVolumeLevel( volumeLevel );
		this.startTimeSlider.setTime( startTime );
		this.stopTimeSlider.setTime( stopTime );

		this.dropDown = org.alice.ide.x.DialogAstI18nFactory.getInstance().createExpressionPropertyPane( bogusNode.bogusProperty, null );
		
		if( audioResource != null ) {
			double duration = audioResource.getDuration();
			if( Double.isNaN( duration ) ) {
				//pass
			} else {
				this.startTimeSlider.setDuration( duration );
				this.stopTimeSlider.setDuration( duration );
			}
		}

//		this.volumeLevelControl.addChangeListener( new javax.swing.event.ChangeListener() {
//			public void stateChanged( javax.swing.event.ChangeEvent e ) {
////				AudioSourceChooser.this.getInputPanel().updateOKButton();
//			}
//		} );
		this.startTimeSlider.addChangeListener( new javax.swing.event.ChangeListener() {
			public void stateChanged( javax.swing.event.ChangeEvent e ) {
				if( startTimeSlider.getValue() > stopTimeSlider.getValue() ) {
					stopTimeSlider.setValue( startTimeSlider.getValue() );
				}
			}
		} );
		this.stopTimeSlider.addChangeListener( new javax.swing.event.ChangeListener() {
			public void stateChanged( javax.swing.event.ChangeEvent e ) {
				if( startTimeSlider.getValue() > stopTimeSlider.getValue() ) {
					startTimeSlider.setValue( stopTimeSlider.getValue() );
				}
			}
		} );
	}
	@Override
	public String getExplanationIfOkButtonShouldBeDisabled() {
		org.lgna.project.ast.ResourceExpression resourceExpression = (org.lgna.project.ast.ResourceExpression)bogusNode.bogusProperty.getValue();
		if( resourceExpression != null ) {
			if( resourceExpression.resource.getValue() instanceof org.lgna.common.resources.AudioResource ) {
				return null;
			} else {
				return "resource is not audio";
			}
		} else {
			return "resource is not set";
		}
	}
	@Override
	public String[] getLabelTexts() {
		return new String[] { 
				"resource:", 
				"volume:", 
				"", 
				"start marker:", 
				"stop marker:" };
	}
	@Override
	public org.lgna.croquet.components.Component< ? >[] getRowComponents() {
		return new org.lgna.croquet.components.Component< ? >[] { 
				this.dropDown, 
				new org.lgna.croquet.components.LineAxisPanel( 
						new org.lgna.croquet.components.SwingAdapter( this.volumeLevelControl ), 
						org.lgna.croquet.components.BoxUtilities.createHorizontalGlue() 
				), 
				org.lgna.croquet.components.BoxUtilities.createVerticalSliver( 16 ), 
				new org.lgna.croquet.components.SwingAdapter( this.startTimeSlider ), 
				new org.lgna.croquet.components.SwingAdapter( this.stopTimeSlider ) };
	}
	@Override
	public java.util.List< org.lgna.croquet.components.Component< ? >[] > updateRows( java.util.List< org.lgna.croquet.components.Component< ? >[] > rv ) {
		super.updateRows( rv );
		rv.add( org.lgna.croquet.components.SpringUtilities.createRow( org.lgna.croquet.components.BoxUtilities.createVerticalSliver( 32 ), null ) );
		rv.add( org.lgna.croquet.components.SpringUtilities.createRow( testOperation.createButton(), null ) );
		return rv;
	}

	@Override
	public org.lgna.project.ast.InstanceCreation getValue() {
		org.lgna.story.AudioSource audioSource = this.getAudioSource();
		if( audioSource != null ) {
			org.lgna.common.resources.AudioResource audioResource = audioSource.getAudioResource();

			org.alice.ide.IDE ide = org.alice.ide.IDE.getActiveInstance();
			org.lgna.project.Project project = ide.getProject();
			if( project != null ) {
				project.addResource( audioResource );
			}

			org.lgna.project.ast.Expression arg0Expression;
			if( audioResource != null ) {
				arg0Expression = new org.lgna.project.ast.ResourceExpression( org.lgna.common.resources.AudioResource.class, audioResource );				
			} else {
				arg0Expression = new org.lgna.project.ast.NullLiteral();
			}

			double volume = audioSource.getVolume();
			double startTime = audioSource.getStartTime();
			double stopTime = audioSource.getStopTime();

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
	
	private org.lgna.story.AudioSource getAudioSource() {
		org.lgna.common.resources.AudioResource audioResource;
		double volume = this.volumeLevelControl.getVolumeLevel();
		double start;
		double stop;
		double duration;
		org.lgna.project.ast.ResourceExpression resourceExpression = (org.lgna.project.ast.ResourceExpression)bogusNode.bogusProperty.getValue();
		if( resourceExpression != null ) {
			org.lgna.common.Resource resource = resourceExpression.resource.getValue();
			if( resource instanceof org.lgna.common.resources.AudioResource ) {
				audioResource = (org.lgna.common.resources.AudioResource)resource;
				duration = audioResource.getDuration();
			} else {
				audioResource = null;
				duration = Double.NaN;
			}
		} else {
			audioResource = null;
			duration = Double.NaN;
		}
		if( Double.isNaN( duration ) ) {
			//todo
			start = 0.0;
			stop = Double.NaN;
		} else {
			if( this.startTimeSlider.getValue() == this.startTimeSlider.getMinimum() ) {
				start = 0.0;
			} else {
				start = this.startTimeSlider.getTime();
			}
			if( this.stopTimeSlider.getValue() == this.stopTimeSlider.getMaximum() ) {
				stop = Double.NaN;
			} else {
				stop = this.stopTimeSlider.getTime();
			}
		}
		return new org.lgna.story.AudioSource( audioResource, volume, start, stop );
	}
}
