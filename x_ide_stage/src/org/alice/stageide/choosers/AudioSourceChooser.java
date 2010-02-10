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
	class BogusNode extends edu.cmu.cs.dennisc.alice.ast.Node {
		private edu.cmu.cs.dennisc.alice.ast.ExpressionProperty bogusProperty = new edu.cmu.cs.dennisc.alice.ast.ExpressionProperty( this ) {
			public edu.cmu.cs.dennisc.alice.ast.AbstractType getExpressionType() {
				return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.virtualmachine.Resource.class );
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
	
	
	class IsMarkedExpandPane extends edu.cmu.cs.dennisc.swing.ExpandPane {
		@Override
		protected String getCollapsedLabelText() {
			return "play all      ";
		}
		@Override
		protected String getExpandedLabelText() {
			return "";
		}
		@Override
		protected String getCollapsedButtonText() {
			return "set markers >>>";
		}
		@Override
		protected javax.swing.JComponent createCenterPane() {
			javax.swing.JComponent rv = new edu.cmu.cs.dennisc.croquet.swing.FormPane() {
				@Override
				protected java.util.List< java.awt.Component[] > addComponentRows( java.util.List< java.awt.Component[] > rv ) {
					rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createColumn0Label( "from marker:" ), getSliderFrom() ) );
					rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createColumn0Label( "to marker:" ), getSliderTo() ) );
					return rv;
				}
			};
			rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 16, 4, 16, 4 ) );
			return rv;
		}
	}
	
	private IsMarkedExpandPane isMarkedExpandPane = new IsMarkedExpandPane();
	private javax.swing.JSlider sliderFrom;
	private javax.swing.JSlider sliderTo;
	private javax.swing.JButton preview = new javax.swing.JButton( "preview" );

	private javax.swing.JSlider getSliderFrom() {
		if( this.sliderFrom != null ) {
			//pass
		} else {
			this.sliderFrom = new javax.swing.JSlider();
		}
		return this.sliderFrom;
	}
	private javax.swing.JSlider getSliderTo() {
		if( this.sliderTo != null ) {
			//pass
		} else {
			this.sliderTo = new javax.swing.JSlider();
		}
		return this.sliderTo;
	}
	public AudioSourceChooser() {
		this.getSliderFrom().setValue( 0 );
		this.getSliderTo().setValue( 100 );
		this.isMarkedExpandPane.addItemListener( new java.awt.event.ItemListener() {
			public void itemStateChanged( java.awt.event.ItemEvent e ) {
				AudioSourceChooser.this.getInputPane().updateOKButton();
			}
		} );
		edu.cmu.cs.dennisc.alice.ast.Expression previousExpression = this.getPreviousExpression();
		if( previousExpression instanceof edu.cmu.cs.dennisc.alice.ast.InstanceCreation ) {
			edu.cmu.cs.dennisc.alice.ast.InstanceCreation instanceCreation = (edu.cmu.cs.dennisc.alice.ast.InstanceCreation)previousExpression;
			
			int n = instanceCreation.arguments.size();
			
			assert n==1 || n==3;
			
			
			this.isMarkedExpandPane.setSelected( n == 3 );
			
			
			edu.cmu.cs.dennisc.alice.ast.Argument arg0 =  instanceCreation.arguments.get( 0 );
			edu.cmu.cs.dennisc.alice.ast.Expression exp0 = arg0.expression.getValue();
			//edu.cmu.cs.dennisc.alice.ast.ResourceExpression re0 = (edu.cmu.cs.dennisc.alice.ast.ResourceExpression)exp0;
			bogusNode.bogusProperty.setValue( exp0 );
			
			if( n==3 ) {
				//todo
			}
			
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
		return new String[] { "resource:", "", "" };
	}
	public java.awt.Component[] getComponents() {
		this.dropDown = new org.alice.ide.codeeditor.ExpressionPropertyDropDownPane( null, new org.alice.ide.common.ExpressionPropertyPane( org.alice.ide.IDE.getSingleton().getCodeFactory(), bogusNode.bogusProperty ), bogusNode.bogusProperty );
		return new java.awt.Component[] { this.dropDown, this.isMarkedExpandPane, this.preview };
	}
	public org.alice.apis.moveandturn.AudioSource getValue() {
		edu.cmu.cs.dennisc.alice.ast.ResourceExpression resourceExpression = (edu.cmu.cs.dennisc.alice.ast.ResourceExpression)bogusNode.bogusProperty.getValue();
		return new org.alice.apis.moveandturn.AudioSource( resourceExpression.resource.getValue() );
	}
	public static void main( String[] args ) {
		org.alice.ide.IDE ide = new org.alice.ide.FauxIDE();
		org.alice.stageide.cascade.customfillin.CustomAudioSourceFillIn fillIn = new org.alice.stageide.cascade.customfillin.CustomAudioSourceFillIn();
		edu.cmu.cs.dennisc.print.PrintUtilities.println( fillIn.getValue() );
	}
	
}
