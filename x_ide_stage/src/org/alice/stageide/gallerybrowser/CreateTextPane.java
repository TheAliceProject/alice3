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
	private javax.swing.JTextField _textVC;
	private javax.swing.JTextField _instanceNameVC;
	private javax.swing.JTextField _classNameVC;
	private javax.swing.JCheckBox _constrainInstanceNameToTextVC;
	private javax.swing.JCheckBox _constrainClassNameToInstanceNameVC;
	private org.alice.stageide.fontchooser.FontChooser _fontChooser;
	private org.alice.ide.cascade.customfillin.CustomDoublePane _letterHeightChooser;
	public CreateTextPane() {
//		def __init__( this, selected, siblings ):
		final int inset = 16;
		this.setBorder( javax.swing.BorderFactory.createEmptyBorder(inset, inset, inset, inset ) );

		this._textVC = new javax.swing.JTextField();
		//this._textVC.getDocument().addDocumentListener( ecc.dennisc.swing.event.FilteredDocumentAdapter( this._handleTextChange ) );
		this._instanceNameVC = new javax.swing.JTextField();
		//this._instanceNameVC.getDocument().addDocumentListener( ecc.dennisc.swing.event.FilteredDocumentAdapter( this._handleInstanceNameChange ) );
		this._classNameVC = new javax.swing.JTextField();
		//this._classNameVC.getDocument().addDocumentListener( ecc.dennisc.swing.event.FilteredDocumentAdapter( this._handleClassNameChange ) );

		this._constrainInstanceNameToTextVC = new javax.swing.JCheckBox( "constrain to text" );
		//this._constrainInstanceNameToTextVC.addItemListener( ecc.dennisc.swing.event.ItemAdapter( this._handleInstanceNameContraintChange ) );
		this._constrainInstanceNameToTextVC.setSelected( true );
		this._constrainClassNameToInstanceNameVC = new javax.swing.JCheckBox( "constrain to instance" );
		//this._constrainClassNameToInstanceNameVC.addItemListener( ecc.dennisc.swing.event.ItemAdapter( this._handleClassNameContraintChange ) );
		this._constrainClassNameToInstanceNameVC.setSelected( true );

		this._letterHeightChooser = new org.alice.ide.cascade.customfillin.CustomDoublePane();
		this._letterHeightChooser.setLabelText( "letter height (in meters):" );
		this._letterHeightChooser.setAndSelectText( "1.0" );
		//this._letterHeightChooser.setInputPane( this );
		this.addOKButtonValidator( this._letterHeightChooser );

		this._fontChooser = new org.alice.stageide.fontchooser.FontChooser();

		javax.swing.JPanel fontPane = new javax.swing.JPanel();
		javax.swing.border.Border etchedBorder = javax.swing.BorderFactory.createEtchedBorder( javax.swing.border.EtchedBorder.LOWERED );
		javax.swing.border.Border titledBorder = javax.swing.BorderFactory.createTitledBorder( etchedBorder, "Font" );
		fontPane.setBorder( titledBorder );
		fontPane.setLayout( new java.awt.BorderLayout() );
		fontPane.add( this._letterHeightChooser, java.awt.BorderLayout.NORTH );
		fontPane.add( this._fontChooser, java.awt.BorderLayout.CENTER );

		this.setLayout( new java.awt.GridBagLayout() );
		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.fill = java.awt.GridBagConstraints.BOTH;
		gbc.insets.right = 16;
		
		gbc.gridy = 0;
		gbc.weightx = 0.0;
		this.add( new javax.swing.JLabel( "text:" ), gbc );
		gbc.weightx = 1.0;
		this.add( this._textVC, gbc );
		gbc.weightx = 0.0;
		this.add( new javax.swing.JPanel(), gbc );
		
		
		gbc.gridy = 1;
		gbc.weightx = 0.0;
		this.add( new javax.swing.JLabel( "instance:" ), gbc );
		gbc.weightx = 1.0;
		this.add( this._instanceNameVC, gbc );
		gbc.weightx = 0.0;
		this.add( this._constrainInstanceNameToTextVC, gbc );


		gbc.gridy = 2;
		gbc.weightx = 0.0;
		this.add( new javax.swing.JLabel( "class:" ), gbc );
		gbc.weightx = 1.0;
		this.add( this._classNameVC, gbc );
		gbc.weightx = 0.0;
		this.add( this._constrainClassNameToInstanceNameVC, gbc );

		gbc.weighty = 1.0;
		gbc.insets.top = 32;
		gbc.gridy = 3;
		gbc.gridwidth = 3;
		this.add( fontPane, gbc );

		gbc.insets.top = 0;
		gbc.gridy = 4;
		this.add( new javax.swing.JPanel(), gbc );

		//this.addOKButtonValidator( this );
	}
//
//	def isValid( this ):
//		instanceName = this._instanceNameVC.getText()
//		className = this._classNameVC.getText()
//		#todo: check siblings
//		return ecc.dennisc.alice.vm.isValidIdentifier( instanceName ) and ecc.dennisc.alice.vm.isValidIdentifier( className )
//	
//	def _handleInstanceNameContraintChange( this, e ):
//		this._instanceNameVC.setEditable( not e.getSource().isSelected() )
//
//	def _handleClassNameContraintChange( this, e ):
//		this._classNameVC.setEditable( not e.getSource().isSelected() )
//
//	def _handleTextChange( this, text ):
//		text = this._textVC.getText()
//		if this._constrainInstanceNameToTextVC.isSelected():
//			instanceName = ecc.dennisc.alice.vm.getConventionalInstanceName( text )
//			this._instanceNameVC.setText( instanceName )
//		this._fontChooser.setSampleText( text )
//		this.updateOKButton()
//	def _handleInstanceNameChange( this, text ):
//		if this._constrainClassNameToInstanceNameVC.isSelected():
//			instanceName = this._instanceNameVC.getText()
//			className = ecc.dennisc.alice.vm.getConventionalClassName( instanceName )
//			this._classNameVC.setText( className )
//		this.updateOKButton()
//	def _handleClassNameChange( this, text ):
//		this.updateOKButton()
//		
//	def getActualInputValue( this ):
//		typeDeclaredInJava = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Text )
//		className = this._classNameVC.getText();
//		constructors = [ alice.ast.ConstructorDeclaredInAlice( [], alice.ast.BlockStatement( [] ) ) ]
//		type = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice( className, None, typeDeclaredInJava, constructors, [], [] )
//		rv = getSceneEditor().createInstance( type )
//		instanceInJava = ecc.dennisc.alice.vm.getInstanceInJava( rv )
//		instanceInJava.setName( this._instanceNameVC.getText() )
//		instanceInJava.setValue( this._textVC.getText() )
//		instanceInJava.setFont( org.alice.apis.moveandturn.Font( this._fontChooser.getFont() ) )
//		instanceInJava.setLetterHeight( this._letterHeightChooser.getActualInputValue() )
//		return rv}

	@Override
	protected org.alice.apis.moveandturn.Text getActualInputValue() {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava typeDeclaredInJava = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Text.class );
//		className = this._classNameVC.getText();
//		constructors = [ alice.ast.ConstructorDeclaredInAlice( [], alice.ast.BlockStatement( [] ) ) ];
//		type = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice( className, None, typeDeclaredInJava, constructors, [], [] );
//		rv = getSceneEditor().createInstance( type );
//		instanceInJava = ecc.dennisc.alice.vm.getInstanceInJava( rv );
//		instanceInJava.setName( this._instanceNameVC.getText() );
//		instanceInJava.setValue( this._textVC.getText() );
//		instanceInJava.setFont( org.alice.apis.moveandturn.Font( this._fontChooser.getFont() ) );
//		instanceInJava.setLetterHeight( this._letterHeightChooser.getActualInputValue() );
//		return rv;
		return null;
	}
}	