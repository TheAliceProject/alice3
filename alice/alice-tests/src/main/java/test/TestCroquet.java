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
package test;

/**
 * @author Dennis Cosgrove
 */
public class TestCroquet extends org.lgna.croquet.simple.SimpleApplication {
	private static javax.swing.filechooser.FileFilter createFileFilter( boolean areDirectoriesAccepted ) {
		return new edu.cmu.cs.dennisc.javax.swing.filechooser.ExtensionFileFilter( "audio file", areDirectoriesAccepted, "mp3", "wav", "au" );
	}

	public static void main( String[] args ) throws Exception {
		edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities.setLookAndFeel( "Nimbus" );

		TestCroquet testCroquet = new TestCroquet();
		testCroquet.initialize( args );

		class TestIntegerState extends org.lgna.croquet.BoundedIntegerState {
			public TestIntegerState() {
				super( new Details( org.lgna.croquet.Application.DOCUMENT_UI_GROUP, java.util.UUID.fromString( "82e0bdc9-92af-4c8d-92c5-68ea3d9d2457" ) ).maximum( 80 ).initialValue( 50 ) );
			}
		}
		class TestDoubleState extends org.lgna.croquet.BoundedDoubleState {
			public TestDoubleState() {
				super( new Details( org.lgna.croquet.Application.DOCUMENT_UI_GROUP, java.util.UUID.fromString( "397994c3-daf3-4db9-ad5b-feaccfb2aa56" ) ).maximum( 80 ).initialValue( 50 ).stepSize( 10 ).minimum( 20 ) );
			}
		}

		class TestBooleanState extends org.lgna.croquet.BooleanState {
			public TestBooleanState() {
				super( org.lgna.croquet.Application.DOCUMENT_UI_GROUP, java.util.UUID.fromString( "1d7a975b-58cb-4ebb-b1cb-e0b456584820" ), false );
			}

			@Override
			protected void localize() {
				super.localize();
				this.setTextForTrueAndTextForFalse( "true", "false" );
			}
		}

		TestIntegerState integerState = new TestIntegerState();
		TestDoubleState doubleState = new TestDoubleState();
		final TestBooleanState booleanState = new TestBooleanState();

		integerState.addNewSchoolValueListener( new org.lgna.croquet.event.ValueListener<Integer>() {
			@Override
			public void valueChanged( org.lgna.croquet.event.ValueEvent<Integer> e ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( e );
			}
		} );
		booleanState.addNewSchoolValueListener( new org.lgna.croquet.event.ValueListener<Boolean>() {
			@Override
			public void valueChanged( org.lgna.croquet.event.ValueEvent<Boolean> e ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( e );
			}
		} );

		class TestOperation extends org.lgna.croquet.ActionOperation {
			public TestOperation() {
				super( org.lgna.croquet.Application.DOCUMENT_UI_GROUP, java.util.UUID.fromString( "bc3e9e3a-0219-4ed7-9fa7-c4e7ff5c7360" ) );
			}

			@Override
			protected void perform( org.lgna.croquet.history.CompletionStep<?> step ) {
				booleanState.setValueTransactionlessly( !booleanState.getValue() );
			}
		}

		TestOperation operation = new TestOperation();

		class TestFileSelectionState extends org.lgna.croquet.FileSelectionState {
			public TestFileSelectionState() {
				super( org.lgna.croquet.Application.DOCUMENT_UI_GROUP, java.util.UUID.fromString( "7b183ea9-914f-4296-a96b-93d95af9c98f" ), null );
			}
		}

		javax.swing.filechooser.FileFilter fileFilter = createFileFilter( true );
		TestFileSelectionState fileSelectionState = new TestFileSelectionState();
		fileSelectionState.addChoosableFileFilter( fileFilter );
		fileSelectionState.setFileFilter( fileFilter );
		fileSelectionState.addNewSchoolValueListener( new org.lgna.croquet.event.ValueListener<java.io.File>() {
			@Override
			public void valueChanged( org.lgna.croquet.event.ValueEvent<java.io.File> e ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.outln( e.getNextValue() );
			}
		} );

		org.lgna.croquet.views.GridPanel gridPanel = org.lgna.croquet.views.GridPanel.createGridPane(
				0, 2,
				integerState.createSlider(), integerState.createSpinner(),
				doubleState.createSlider(), doubleState.createSpinner(),
				booleanState.createToggleButton(), booleanState.createCheckBox(),
				operation.createButton(), new org.lgna.croquet.views.Label()
				);

		org.lgna.croquet.views.BorderPanel borderPanel = new org.lgna.croquet.views.BorderPanel.Builder()
				.center( fileSelectionState.getOneAndOnlyOneFileChooser() )
				.pageEnd( gridPanel )
				.build();

		org.lgna.croquet.DocumentFrame documentFrame = testCroquet.getDocumentFrame();
		org.lgna.croquet.views.Frame frame = documentFrame.getFrame();
		frame.getContentPane().addCenterComponent( borderPanel );
		frame.pack();
		frame.setVisible( true );
	}
}
