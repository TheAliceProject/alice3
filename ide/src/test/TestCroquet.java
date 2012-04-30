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
	public static void main( String[] args ) {
		TestCroquet testCroquet = new TestCroquet();
		testCroquet.initialize( args );
		
		class IntegerState extends org.lgna.croquet.BoundedIntegerState {
			public IntegerState() {
				super( new Details( org.lgna.croquet.Application.DOCUMENT_UI_GROUP, java.util.UUID.fromString( "82e0bdc9-92af-4c8d-92c5-68ea3d9d2457" ) ).maximum( 80 ).initialValue( 50 ) );
			}
		}
		class DoubleState extends org.lgna.croquet.BoundedDoubleState {
			public DoubleState() {
				super( new Details( org.lgna.croquet.Application.DOCUMENT_UI_GROUP, java.util.UUID.fromString( "82e0bdc9-92af-4c8d-92c5-68ea3d9d2457" ) ).maximum( 80 ).initialValue( 50 ).stepSize( 10 ).minimum( 20 ) );
			}
		}
		
		IntegerState integerState = new IntegerState();
		DoubleState doubleState = new DoubleState();
		
		org.lgna.croquet.components.GridPanel gridPanel = org.lgna.croquet.components.GridPanel.createGridPane( 
				4,  1, 
				integerState.createSlider(), integerState.createSpinner(),
				doubleState.createSlider(), doubleState.createSpinner()
		);
		
		testCroquet.getFrame().getContentPanel().addComponent( gridPanel, org.lgna.croquet.components.BorderPanel.Constraint.CENTER );
		testCroquet.getFrame().setDefaultCloseOperation( org.lgna.croquet.components.Frame.DefaultCloseOperation.EXIT );
		testCroquet.getFrame().pack();
		testCroquet.getFrame().setVisible( true );
	}
}
