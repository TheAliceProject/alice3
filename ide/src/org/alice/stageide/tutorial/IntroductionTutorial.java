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

/**
 * @author Dennis Cosgrove
 */
public class IntroductionTutorial {
	public static void main( final String[] args ) {
		org.alice.stageide.StageIDE ide = new org.alice.stageide.StageIDE();
		ide.initialize(args);
		ide.loadProjectFrom( args[ 0 ] );
		ide.getFrame().maximize();
		
		final edu.cmu.cs.dennisc.tutorial.Tutorial tutorial = new edu.cmu.cs.dennisc.tutorial.Tutorial();
		tutorial.addMessageStep( 
				"Welcome", 
				"<html><b><center>Welcome To The Tutorial</center></b><p>First we'll show you around a bit.</html>" 
		);
		tutorial.addSpotlightStep( 
				"Scene Editor", 
				"<html>This is the scene editor.</html>", 
				ide.getSceneEditor() 
		);
		tutorial.addSpotlightStep( 
				"Constructs",
				"<html>This where loops and locals live.</html>", 
				ide.getUbiquitousPane() 
		);
		tutorial.addDialogOpenStep( 
				"Run", 
				"<html>Press the <b>Run</b> button</html>", 
				ide.getRunOperation() 
		);
		tutorial.addDialogCloseStep( 
				"Run", 
				"<html>Press the <b>Close</b> button</html>", 
				ide.getRunOperation() 
		);
		
		org.alice.ide.memberseditor.MembersEditor membersEditor = ide.getMembersEditor();
		
		
		edu.cmu.cs.dennisc.alice.ast.AbstractField sceneField = ide.getSceneField();
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice sceneType = ide.getSceneType();
		edu.cmu.cs.dennisc.alice.ast.AbstractField sunLightField = sceneType.fields.get( 0 );
		
		
		tutorial.addSpotlightStep( 
				"Instance Details", 
				"<html>This is the currently selected instance methods and fields pane.</html>", 
				membersEditor 
		);

		tutorial.addSpotlightStep( 
				"Curent Instance", 
				"<html>The current instance is show here.</html>", 
				ide.getFieldSelectionState()
		);
		
		tutorial.addItemSelectionStep(
				"Select Scene",
				"<html>Select the <b>scene</b> instance.</html>",
				ide.getFieldSelectionState(),
				sceneField
		);
		
		tutorial.addSpotlightStep( 
				"Note Scene Instance Details", 
				"<html>Now the scene instance details are now displayed.</html>", 
				membersEditor 
		);

		tutorial.addItemSelectionStep(
				"Select Scene",
				"<html>Select the <b>sunLight</b> instance.</html>",
				ide.getFieldSelectionState(),
				sunLightField
		);
		
		tutorial.addSpotlightStep( 
				"Note sunLight Instance Details", 
				"<html>Now the sunLight instance details are now displayed.</html>", 
				membersEditor 
		);

		tutorial.addSelectTabStep( 
				"Select Functions Tab", 
				"<html>Select the <b>Functions</b> tab.</html>", 
				membersEditor.getTabbedPaneSelectionState(),
				membersEditor.getFunctionsTab()
		);
		tutorial.addSpotlightTabScrollPaneStep( 
				"Note Functions Tab", 
				"<html>Now the functions are now displayed.</html>", 
				membersEditor.getTabbedPaneSelectionState(),
				membersEditor.getFunctionsTab()
		);
		tutorial.addSelectTabStep( 
				"Properies Tab", 
				"<html>Select the <b>Properies</b> tab.</html>", 
				membersEditor.getTabbedPaneSelectionState(),
				membersEditor.getFieldsTab()
		);
		tutorial.addSpotlightTabScrollPaneStep( 
				"Note Properies Tab", 
				"<html>Now the properties are now displayed.</html>", 
				membersEditor.getTabbedPaneSelectionState(),
				membersEditor.getFieldsTab()
		);
		tutorial.addSelectTabStep( 
				"Procedures Tab", 
				"<html>Select the <b>Procedures</b> tab.</html>", 
				membersEditor.getTabbedPaneSelectionState(),
				membersEditor.getProceduresTab()
		);
		tutorial.addSpotlightTabScrollPaneStep( 
				"Note Procedures Tab", 
				"<html>Now the procedures are now displayed.</html>", 
				membersEditor.getTabbedPaneSelectionState(),
				membersEditor.getProceduresTab()
		);

		tutorial.addBooleanStateStep( 
				"Edit Scene", 
				"<html>Press the <b>Edit Scene</b> button</html>", 
				ide.getIsSceneEditorExpandedState(),
				true
		);
		tutorial.addMessageStep( 
				"Note Edit Scene",
				"<html>Note you are now editing the scene.</html>" 
		);
		tutorial.addBooleanStateStep( 
				"Edit Code", 
				"<html>Press the <b>Edit Code</b> button</html>", 
				ide.getIsSceneEditorExpandedState(),
				false
		);
		tutorial.addMessageStep( 
				"Note Edit Code",
				"<html>Note you are now editing the code.</html>" 
		);

		//membersEditor.getTabbedPaneSelectionState().setValue( membersEditor.getFunctionsTab() );
		tutorial.setSelectedIndex( 1 );
		
		ide.getFrame().addWindowListener( new java.awt.event.WindowAdapter() {
			@Override
			public void windowOpened(java.awt.event.WindowEvent e) {
//				javax.swing.SwingUtilities.invokeLater( new Runnable() {
//					public void run() {
						tutorial.setVisible( true );
//					}
//				} );
			}
		} );

		ide.getFrame().setVisible( true );
	}
}
