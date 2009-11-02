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
package org.alice.stageide.personeditor;

/**
 * @author Dennis Cosgrove
 */
public class PersonEditor extends org.alice.ide.Editor< org.alice.apis.stage.Person > {
//public class PersonEditor extends org.alice.ide.Editor< edu.cmu.cs.dennisc.alice.ast.AbstractType > {
	private javax.swing.JSplitPane splitPane;
	private PersonViewer personViewer = PersonViewer.getSingleton();
	private IngredientsPane ingredientsPane = new IngredientsPane() {
		@Override
		protected void handleTabSelection( int index ) {
			personViewer.handleTabSelection( index );
		}
	};
	
	private org.alice.apis.stage.Person person;
	public PersonEditor( org.alice.apis.stage.Person person ) {
		this.person = person;
		this.personViewer.setIngredientsPane( this.ingredientsPane );
		javax.swing.SwingUtilities.invokeLater( new Runnable() {
			public void run() {
				PersonEditor.this.personViewer.initializeValues( PersonEditor.this.person );
			}			
		} );
//		this.personViewer.initializeValues( this.person );
		javax.swing.JPanel container = new javax.swing.JPanel();
		this.splitPane = new javax.swing.JSplitPane( javax.swing.JSplitPane.HORIZONTAL_SPLIT, container, this.ingredientsPane );
		this.splitPane.setDividerLocation( 400 );
		this.setLayout( new java.awt.GridLayout( 1, 1 ) );
		this.add( this.splitPane );
		personViewer.showInAWTContainer( container, new String[] {} );
	}
	
	public org.alice.apis.stage.Person getPerson() {
		org.alice.apis.stage.Person rv;
		
		if( this.personViewer.getLifeStage() == org.alice.apis.stage.LifeStage.ADULT ) {
			if( this.person != null ) {
				rv = this.person;
			} else {
				rv = new org.alice.apis.stage.Adult();
			}
			rv.setGender( this.personViewer.getGender() );
			rv.setHair( this.personViewer.getHair() );
			rv.setSkinTone( this.personViewer.getBaseSkinTone() );
			rv.setEyeColor( this.personViewer.getBaseEyeColor() );
			rv.setFitnessLevel( this.personViewer.getFitnessLevel() );
			rv.setOutfit( this.personViewer.getFullBodyOutfit() );
		} else {
			rv = null;
		}
		
		return rv;
	}
	@Override
	public java.awt.Dimension getPreferredSize() {
		return new java.awt.Dimension( 1024, 700 );
	}
	
	public static void main( String[] args ) {
		edu.cmu.cs.dennisc.zoot.ZFrame frame = new edu.cmu.cs.dennisc.zoot.ZFrame() {
			@Override
			protected void handleWindowOpened( java.awt.event.WindowEvent e ) {
			}
			@Override
			protected void handleAbout( java.util.EventObject e ) {
			}
			@Override
			protected void handlePreferences( java.util.EventObject e ) {
			}

			@Override
			protected void handleQuit( java.util.EventObject e ) {
				System.exit( 0 );
			}
		};
		frame.setSize( new java.awt.Dimension( 1024, 768 ) );
		frame.getContentPane().add( new PersonEditor( null ) );
		frame.setVisible( true );
	}
}
