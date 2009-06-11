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
package org.alice.stageide.gallerybrowser;

public class CreateTextPane extends zoot.ZInputPane< org.alice.apis.moveandturn.Text > {
	class FamilyList extends javax.swing.JList {
		public FamilyList() {
			this.setListData( new String[] { "Serif", "SansSerif" } );
			this.setSelectedIndex( 0 );
		}
		public org.alice.apis.moveandturn.font.FamilyAttribute getFamilyAttribute() {
			Object value = this.getSelectedValue();
			org.alice.apis.moveandturn.font.FamilyAttribute rv;
			if( value.equals( "Serif" ) ) {
				rv = org.alice.apis.moveandturn.font.FamilyConstant.SERIF;
			} else {
				rv = org.alice.apis.moveandturn.font.FamilyConstant.SANS_SERIF;
			}
			return rv;
		}
		public void setFamilyAttribute( org.alice.apis.moveandturn.font.FamilyAttribute familyAttribute ) {
			if( familyAttribute == org.alice.apis.moveandturn.font.FamilyConstant.SERIF ) {
				this.setSelectedValue( "Serif", true );
			} else {
				this.setSelectedValue( "SansSerif", true );
			}
		}
	}
	class StyleList extends javax.swing.JList {
		public StyleList() {
			this.setListData( new String[] { "Regular", "Bold", "Italic", "Bold Italic" } );
			this.setSelectedIndex( 0 );
		}
		public org.alice.apis.moveandturn.font.WeightAttribute getWeightAttribute() {
			Object value = this.getSelectedValue();
			org.alice.apis.moveandturn.font.WeightAttribute rv;
			if( value != null && ( value.equals( "Bold" ) || value.equals( "Bold Italic" ) ) ) {
				rv = org.alice.apis.moveandturn.font.WeightConstant.BOLD;
			} else {
				rv = org.alice.apis.moveandturn.font.WeightConstant.REGULAR;
			}
			return rv;
		}
		public org.alice.apis.moveandturn.font.PostureAttribute getPostureAttribute() {
			Object value = this.getSelectedValue();
			org.alice.apis.moveandturn.font.PostureAttribute rv;
			if( value != null && ( value.equals( "Italic" ) || value.equals( "Bold Italic" ) ) ) {
				rv = org.alice.apis.moveandturn.font.PostureConstant.OBLIQUE;
			} else {
				rv = org.alice.apis.moveandturn.font.PostureConstant.REGULAR;
			}
			return rv;
		}
		public void setStyleAttributes(  org.alice.apis.moveandturn.font.WeightAttribute weight, org.alice.apis.moveandturn.font.PostureAttribute posture ) {
			boolean isBold = ( weight == org.alice.apis.moveandturn.font.WeightConstant.BOLD );
			boolean isItalic = ( posture == org.alice.apis.moveandturn.font.PostureConstant.OBLIQUE );
			Object selectedValue;
			if( isBold ) {
				if( isItalic ) {
					selectedValue = "Bold Italic";
				} else {
					selectedValue = "Bold";
				}
			} else {
				if( isItalic ) {
					selectedValue = "Italic";
				} else {
					selectedValue = "Regular";
				}
			}
			this.setSelectedValue( selectedValue, true );
		}
	}

	private javax.swing.JTextField textVC;
	private javax.swing.JTextField instanceNameVC;
	//private javax.swing.JTextField classNameVC;
	private zoot.ZCheckBox constrainInstanceNameToTextVC;
	//private javax.swing.JCheckBox constrainClassNameToInstanceNameVC;
	
	private javax.swing.JTextField heightTextField;
	private FamilyList familyList;
	private StyleList styleList;

	private zoot.ZLabel sample;
	
	
	public CreateTextPane() {
		final int INSET = 16;
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder( INSET, INSET, INSET, INSET ) );
		this.setBackground( org.alice.ide.IDE.getSingleton().getFieldColor() );
		this.setOpaque( true );
		class TextField extends zoot.ZSuggestiveTextField {
			public TextField( String text, String textForBlankCondition ) {
				super( text, textForBlankCondition );
			}
			@Override
			public java.awt.Dimension getPreferredSize() {
				return edu.cmu.cs.dennisc.awt.DimensionUtilties.constrainToMinimumWidth( super.getPreferredSize(), 160 );
			}
			@Override
			public java.awt.Dimension getMaximumSize() {
				java.awt.Dimension rv = super.getMaximumSize();
				rv.height = this.getPreferredSize().height;
				return rv;
			}
		}
		this.textVC = new TextField( "", " enter text here" );
		this.textVC.getDocument().addDocumentListener( new edu.cmu.cs.dennisc.swing.event.SimplifiedDocumentAdapter() {
			@Override
			protected void updated( javax.swing.event.DocumentEvent e ) {
				CreateTextPane.this.handleTextChange(e);
			}
		} );
		//this.textVC.getDocument().addDocumentListener( ecc.dennisc.swing.event.FilteredDocumentAdapter( this.handleTextChange ) );
		this.instanceNameVC = new TextField( "", " enter instance name here");
		this.instanceNameVC.getDocument().addDocumentListener( new edu.cmu.cs.dennisc.swing.event.SimplifiedDocumentAdapter() {
			@Override
			protected void updated( javax.swing.event.DocumentEvent e ) {
				CreateTextPane.this.updateOKButton();
			}
		} );
		//this.instanceNameVC.getDocument().addDocumentListener( ecc.dennisc.swing.event.FilteredDocumentAdapter( this.handleInstanceNameChange ) );

		this.constrainInstanceNameToTextVC = new zoot.ZCheckBox( new ConstrainInstanceNameToTextBooleanStateOperation() );
		this.constrainInstanceNameToTextVC.setSelected( true );
		this.constrainInstanceNameToTextVC.setOpaque( false );
		this.heightTextField = new TextField( "1.0", " enter height in meters here" );
		this.heightTextField.getDocument().addDocumentListener( new edu.cmu.cs.dennisc.swing.event.SimplifiedDocumentAdapter() {
			@Override
			protected void updated( javax.swing.event.DocumentEvent e ) {
				CreateTextPane.this.updateOKButton();
			}
		} );
		this.familyList = new FamilyList();
		this.styleList = new StyleList();
	
		this.sample = zoot.ZLabel.acquire( "AaBbYyZz" );
		this.sample.setFontToScaledFont( 1.2f );
		this.updateSample();
		
		class ListSelectionAdapter implements javax.swing.event.ListSelectionListener {
			public void valueChanged( javax.swing.event.ListSelectionEvent e ) {
				if( e.getValueIsAdjusting() ) {
					//pass
				} else {
					CreateTextPane.this.updateSample();
				}
			}
		}
		ListSelectionAdapter listSelectionAdapter = new ListSelectionAdapter();
		
		this.familyList.addListSelectionListener( listSelectionAdapter );
		this.styleList.addListSelectionListener( listSelectionAdapter );
		
		swing.RowsSpringPane pane = new swing.RowsSpringPane( 16, 4 ) {
			@Override
			protected java.util.List< java.awt.Component[] > addComponentRows( java.util.List< java.awt.Component[] > rv ) {
				CreateTextPane.this.updateRows( rv );
				return rv;
			}
		};
		
		
		this.setLayout( new java.awt.BorderLayout() );
		this.add( pane, java.awt.BorderLayout.CENTER );
	}
	
	class ConstrainInstanceNameToTextBooleanStateOperation extends org.alice.ide.operations.AbstractBooleanStateOperation {
		public ConstrainInstanceNameToTextBooleanStateOperation() {
			super( false );
			this.putValue( javax.swing.Action.NAME, "constrain to text" );
		}
		public void performStateChange( zoot.BooleanStateContext booleanStateContext ) {
			CreateTextPane.this.instanceNameVC.setEditable( booleanStateContext.getNextValue() == false );
			booleanStateContext.put( org.alice.ide.IDE.IS_PROJECT_CHANGED_KEY, false );
			booleanStateContext.commit();
		}
	}
	private static java.awt.Component createLabel( String text ) {
		//javax.swing.JLabel rv = new javax.swing.JLabel( text );
		zoot.ZLabel rv = zoot.ZLabel.acquire( text );
		rv.setHorizontalAlignment( javax.swing.SwingConstants.TRAILING );
		rv.setVerticalAlignment( javax.swing.SwingConstants.TOP );
		rv.setOpaque( false );
		return rv;
	}
	
	protected java.util.List< java.awt.Component[] > updateRows( java.util.List< java.awt.Component[] > rv ) {
		rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "text:" ), this.textVC, null ) );
		rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "instance:" ), this.instanceNameVC, this.constrainInstanceNameToTextVC ) );
		rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( javax.swing.Box.createVerticalStrut( 24 ), null, null ) );
		rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "letter height:" ), this.heightTextField, zoot.ZLabel.acquire( "(meters)" ) ) );
		rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( javax.swing.Box.createVerticalStrut( 4 ), null, null ) );
		rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "family:" ), this.familyList, null ) );
		rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "style:" ), this.styleList, null ) );
		rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( createLabel( "sample:" ), this.sample, null ) );
		rv.add( edu.cmu.cs.dennisc.swing.SpringUtilities.createRow( null, null, null ) );

		return rv;
	}
	
	private void updateSample() {
		org.alice.apis.moveandturn.font.FamilyAttribute familyAttribute = this.familyList.getFamilyAttribute();
		org.alice.apis.moveandturn.font.WeightAttribute weightAttribute = this.styleList.getWeightAttribute();
		org.alice.apis.moveandturn.font.PostureAttribute postureAttribute = this.styleList.getPostureAttribute();
		
		java.util.Map< java.awt.font.TextAttribute, Object > map = new java.util.HashMap< java.awt.font.TextAttribute, Object >();
		map.put( familyAttribute.getKey(), familyAttribute.getValue() );
		map.put( weightAttribute.getKey(), weightAttribute.getValue() );
		map.put( postureAttribute.getKey(), postureAttribute.getValue() );
		this.sample.setFontToDerivedFont( map );
	}
	@Override
	public boolean isOKButtonValid() {
		try {
			Double.parseDouble( this.heightTextField.getText() );
		} catch( NumberFormatException nfe ) {
			return false;
		}
		boolean isInstanceNameValid = edu.cmu.cs.dennisc.alice.ast.IdentifierUtilities.isValidIdentifier( this.instanceNameVC.getText() );
		return super.isOKButtonValid() && isInstanceNameValid;
	}
	private void handleTextChange( javax.swing.event.DocumentEvent e ) {
		if( this.constrainInstanceNameToTextVC.isSelected() ) {
			String text = this.textVC.getText();
			String instanceName = edu.cmu.cs.dennisc.alice.ast.IdentifierUtilities.getConventionalInstanceName( text );
			this.instanceNameVC.setText( instanceName );
		}
	}
	@Override
	protected org.alice.apis.moveandturn.Text getActualInputValue() {
		org.alice.apis.moveandturn.Text rv = new org.alice.apis.moveandturn.Text();
		org.alice.apis.moveandturn.font.FamilyAttribute familyAttribute = this.familyList.getFamilyAttribute();
		org.alice.apis.moveandturn.font.WeightAttribute weightAttribute = this.styleList.getWeightAttribute();
		org.alice.apis.moveandturn.font.PostureAttribute postureAttribute = this.styleList.getPostureAttribute();

		rv.setName( this.instanceNameVC.getText() );
		rv.setValue( this.textVC.getText() );
		rv.setFont( new org.alice.apis.moveandturn.Font( familyAttribute, weightAttribute, postureAttribute ) );
		rv.setLetterHeight( Double.parseDouble( this.heightTextField.getText() ) );
		return rv;
	}
	public static void main( String[] args ) {
		org.alice.ide.IDE ide = new org.alice.ide.FauxIDE();
		CreateTextPane createTextPane = new CreateTextPane();
		org.alice.apis.moveandturn.Text text = createTextPane.showInJDialog( ide );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( text );
		System.exit( 0 );
	}
}	