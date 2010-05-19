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
package org.alice.stageide.personeditor;

/**
 * @author Dennis Cosgrove
 */
public class PersonEditor extends edu.cmu.cs.dennisc.croquet.BorderPanel {
	public static final java.util.UUID GROUP_ID = java.util.UUID.fromString( "2d7d725d-1806-40d1-ac2b-d9cd48cb0abb" );
	private edu.cmu.cs.dennisc.croquet.HorizontalSplitPane splitPane;
	private PersonViewer personViewer = PersonViewer.getSingleton();
	private IngredientsPane ingredientsPane = new IngredientsPane() {
		@Override
		protected void handleTabSelection( int tabIndex ) {
			personViewer.handleTabSelection( tabIndex, 0.5 );
		}
		@Override
		protected void handleLifeStageSelection( int tabIndex ) {
			personViewer.handleTabSelection( tabIndex, 0.0 );
		}
		@Override
		protected void handleGenderSelection( int tabIndex ) {
			personViewer.handleTabSelection( tabIndex, 0.0 );
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
		this.splitPane = new edu.cmu.cs.dennisc.croquet.HorizontalSplitPane( this.personViewer, this.ingredientsPane );
		this.splitPane.setDividerLocation( 400 );
		this.addComponent( this.splitPane, Constraint.CENTER );
	}
	public org.alice.apis.stage.Person getPerson() {
		org.alice.apis.stage.Person rv;
		
		if( this.personViewer.getLifeStage() == org.alice.apis.stage.LifeStage.ADULT ) {
			if( this.person != null ) {
				rv = this.person;
			} else {
				rv = new org.alice.apis.stage.Adult();
			}
		} else if( this.personViewer.getLifeStage() == org.alice.apis.stage.LifeStage.CHILD ) {
			rv = new org.alice.apis.stage.Child();
		} else {
			rv = null;
		}
		if( rv != null ) {
			rv.setGender( this.personViewer.getGender() );
			rv.setHair( this.personViewer.getHair() );
			rv.setSkinTone( this.personViewer.getBaseSkinTone() );
			rv.setEyeColor( this.personViewer.getBaseEyeColor() );
			rv.setFitnessLevel( this.personViewer.getFitnessLevel() );
			rv.setOutfit( this.personViewer.getFullBodyOutfit() );
		}
		
		return rv;
	}
//	@Override
//	public java.awt.Dimension getPreferredSize() {
//		return new java.awt.Dimension( 1024, 700 );
//	}
	
	public static void main( String[] args ) {
		org.alice.ide.IDE ide = new org.alice.ide.FauxIDE();
		edu.cmu.cs.dennisc.javax.swing.ApplicationFrame frame = new edu.cmu.cs.dennisc.javax.swing.ApplicationFrame() {
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
		frame.getContentPane().add( new PersonEditor( null ).getAwtComponent() );
		frame.setVisible( true );
	}
}
