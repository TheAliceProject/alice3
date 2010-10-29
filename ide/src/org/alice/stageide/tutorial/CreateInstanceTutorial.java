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
package org.alice.stageide.tutorial;

import org.alice.ide.tutorial.IdeTutorial;

/**
 * @author Dennis Cosgrove
 */
public class CreateInstanceTutorial {
	public static void main( final String[] args ) {
		org.alice.stageide.tutorial.TutorialIDE ide = new org.alice.stageide.tutorial.TutorialIDE();
		ide.initialize(args);
		ide.loadProjectFrom( args[ 0 ] );
		ide.getFrame().maximize();
		
		final IdeTutorial tutorial = new IdeTutorial( ide, 0 );
		tutorial.addBooleanStateStep( 
				"Edit Scene", 
				"Press the <b>Edit Scene</b> button", 
				org.alice.ide.croquet.models.ui.IsSceneEditorExpandedState.getInstance(),
				true
		);
		tutorial.addMessageStep( 
				"Note Edit Scene",
				"Note you are now editing the scene." 
		);
		tutorial.addSpotlightStep( 
				"Gallery Browser", 
				"Note the gallery browser.", 
				ide.getGalleryBrowser() 
		);
//		final java.util.UUID CREATE_PERSON_ID = java.util.UUID.fromString( "3dba52e9-fe65-4fe2-9f51-fd428549ca3a" );
//		tutorial.addActionStep( 
//				"Create Person", 
//				"Press the <b>Create Person</b> button", 
//				CREATE_PERSON_ID 
//		);

		tutorial.addBooleanStateStep( 
				"Edit Code", 
				"Press the <b>Edit Code</b> button", 
				org.alice.ide.croquet.models.ui.IsSceneEditorExpandedState.getInstance(),
				false
		);
		
		
		tutorial.addMessageStep( 
				"Note Edit Code",
				"Note you are now editing the code." 
		);

		tutorial.setVisible( true );
		ide.getFrame().setVisible( true );
	}
}
