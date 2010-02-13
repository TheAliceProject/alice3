/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.stageide.choosers;

/**
 * @author Dennis Cosgrove
 */
public class AudioSourceChooser extends org.alice.ide.choosers.AbstractChooser< org.alice.apis.moveandturn.AudioSource > {
	private static java.text.NumberFormat tFormat = new java.text.DecimalFormat( "0.00" );
	private static double getT( javax.swing.JSlider slider, double duration ) {
		double t = duration*(slider.getValue()/100.0);
		return Double.parseDouble( tFormat.format( t ) );
	}

	class BogusNode extends edu.cmu.cs.dennisc.alice.ast.Node {
		private edu.cmu.cs.dennisc.alice.ast.ExpressionProperty bogusProperty = new edu.cmu.cs.dennisc.alice.ast.ExpressionProperty( this ) {
			@Override
			public edu.cmu.cs.dennisc.alice.ast.AbstractType getExpressionType() {
				return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.virtualmachine.resources.AudioResource.class );
			}
		};
		@Override
		public void firePropertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			super.firePropertyChanged( e );
			edu.cmu.cs.dennisc.zoot.ZInputPane< ? > inputPane = AudioSourceChooser.this.getInputPane();
			if( inputPane != null ) {
				inputPane.updateOKButton();
			}
		}
	}
	
	private BogusNode bogusNode = new BogusNode();
	private java.awt.Component dropDown;
	
	private javax.swing.JSlider volumeSlider = new javax.swing.JSlider();
	private javax.swing.JSlider startSlider = new javax.swing.JSlider();
	private javax.swing.JSlider stopSlider = new javax.swing.JSlider();
	
	class TestOperation extends edu.cmu.cs.dennisc.zoot.InconsequentialActionOperation {
		public TestOperation() {
			this.putValue( javax.swing.Action.NAME, "test" );
		}
		@Override
		protected void performInternal( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
			org.alice.apis.moveandturn.AudioSource audioSource = getValue();
			edu.cmu.cs.dennisc.media.Player player = edu.cmu.cs.dennisc.media.MediaFactory.createPlayer( audioSource.getAudioResource(), audioSource.getVolume(), audioSource.getStartTime(), audioSource.getStopTime() );
			player.test( AudioSourceChooser.this.getInputPane() );
		}
	};
	private TestOperation testOperation = new TestOperation();
	private edu.cmu.cs.dennisc.zoot.ZButton test = new edu.cmu.cs.dennisc.zoot.ZButton( testOperation );

	public AudioSourceChooser() {
		this.volumeSlider.setValue( 100 );
		this.volumeSlider.setMaximum( 200 );
		this.startSlider.setValue( 0 );
		this.stopSlider.setValue( 100 );
		
		java.util.Dictionary<Integer, javax.swing.JComponent> labels = new java.util.Hashtable<Integer, javax.swing.JComponent>();
		labels.put( 0, edu.cmu.cs.dennisc.zoot.ZLabel.acquire( "0.0" ) );
		labels.put( 100, edu.cmu.cs.dennisc.zoot.ZLabel.acquire( "1.0" ) );
		labels.put( 200, edu.cmu.cs.dennisc.zoot.ZLabel.acquire( "2.0" ) );
		this.volumeSlider.setLabelTable( labels );
		this.volumeSlider.setPaintLabels( true );

		this.volumeSlider.setSnapToTicks( true );
		this.volumeSlider.setMinorTickSpacing( 10 );
		this.volumeSlider.setMajorTickSpacing( 100 );
		this.volumeSlider.setPaintTicks( true );
		
		this.volumeSlider.addChangeListener( new javax.swing.event.ChangeListener() {
			public void stateChanged( javax.swing.event.ChangeEvent e ) {
				AudioSourceChooser.this.getInputPane().updateOKButton();
			}
		} );
		this.startSlider.addChangeListener( new javax.swing.event.ChangeListener() {
			public void stateChanged( javax.swing.event.ChangeEvent e ) {
				if( startSlider.getValue() > stopSlider.getValue() ) {
					stopSlider.setValue( startSlider.getValue() );
				}
				AudioSourceChooser.this.getInputPane().updateOKButton();
			}
		} );
		this.stopSlider.addChangeListener( new javax.swing.event.ChangeListener() {
			public void stateChanged( javax.swing.event.ChangeEvent e ) {
				if( startSlider.getValue() > stopSlider.getValue() ) {
					startSlider.setValue( stopSlider.getValue() );
				}
				AudioSourceChooser.this.getInputPane().updateOKButton();
			}
		} );
		edu.cmu.cs.dennisc.alice.ast.Expression previousExpression = this.getPreviousExpression();
		if( previousExpression instanceof edu.cmu.cs.dennisc.alice.ast.InstanceCreation ) {
			edu.cmu.cs.dennisc.alice.ast.InstanceCreation instanceCreation = (edu.cmu.cs.dennisc.alice.ast.InstanceCreation)previousExpression;
			
			int n = instanceCreation.arguments.size();
			
			edu.cmu.cs.dennisc.alice.ast.Argument arg0 =  instanceCreation.arguments.get( 0 );
			edu.cmu.cs.dennisc.alice.ast.Expression exp0 = arg0.expression.getValue();
			bogusNode.bogusProperty.setValue( exp0 );
		}
	}
	public String getTitleDefault() {
		return "Enter Custom Audio Source";
	}
	public boolean isInputValid() {
		edu.cmu.cs.dennisc.alice.ast.ResourceExpression resourceExpression = (edu.cmu.cs.dennisc.alice.ast.ResourceExpression)bogusNode.bogusProperty.getValue();
		return resourceExpression != null;
	}
	@Override
	public String[] getLabelTexts() {
		return new String[] { "resource:", "volume:", "", "start marker:", "stop marker:" };
	}
	@Override
	public java.awt.Component[] getComponents() {
		org.alice.ide.common.Factory factory = org.alice.ide.IDE.getSingleton().getCodeFactory();
//		this.dropDown = new org.alice.ide.codeeditor.ExpressionPropertyDropDownPane( null, factory.createExpressionPropertyPane( bogusNode.bogusProperty, null ), bogusNode.bogusProperty );
		this.dropDown = factory.createExpressionPropertyPane( bogusNode.bogusProperty, null );
		return new java.awt.Component[] { 
				this.dropDown, 
				new edu.cmu.cs.dennisc.croquet.swing.PageAxisPane( javax.swing.Box.createVerticalStrut( 16 ), this.volumeSlider ), 
				javax.swing.Box.createVerticalStrut( 16 ), 
				this.startSlider, 
				this.stopSlider };
	}
	@Override
	public java.util.List< java.awt.Component[] > updateRows( java.util.List< java.awt.Component[] > rv ) {
		super.updateRows( rv );
		rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( javax.swing.Box.createRigidArea( new java.awt.Dimension( 0, 32 ) ), null ) );
		rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( this.test, null ) );
		return rv;
	}
		
	public org.alice.apis.moveandturn.AudioSource getValue() {
		edu.cmu.cs.dennisc.alice.ast.ResourceExpression resourceExpression = (edu.cmu.cs.dennisc.alice.ast.ResourceExpression)bogusNode.bogusProperty.getValue();
		org.alice.virtualmachine.resources.AudioResource audioResource = (org.alice.virtualmachine.resources.AudioResource)resourceExpression.resource.getValue();
		double volume = this.volumeSlider.getValue() / 100.0;
		double start;
		double stop;
		double duration = audioResource.getDuration();
		if( Double.isNaN( duration ) ) {
			//todo
			start = 0.0;
			stop = Double.NaN;
		} else {
			if( this.startSlider.getValue() == this.startSlider.getMinimum() ) {
				start = 0.0;
			} else {
				start = getT( this.startSlider, duration );
			}
			if( this.stopSlider.getValue() == this.stopSlider.getMaximum() ) {
				stop = Double.NaN;
			} else {
				stop = getT( this.stopSlider, duration );
			}
		}
		return new org.alice.apis.moveandturn.AudioSource( audioResource, volume, start, stop );
	}
	public static void main( String[] args ) {
		org.alice.ide.IDE ide = new org.alice.ide.FauxIDE();
		try {
			org.alice.stageide.cascade.customfillin.CustomizeAudioSourceFillIn fillIn = new org.alice.stageide.cascade.customfillin.CustomizeAudioSourceFillIn();
			if( fillIn != null ) {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( fillIn.getValue() );
			}
		} catch( edu.cmu.cs.dennisc.cascade.CancelException ce ) {
		}
	}
	
}
