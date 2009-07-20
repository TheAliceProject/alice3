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
package org.alice.ide.operations.file;

import java.io.File;

import org.alice.media.VideoCapturePane;

import edu.cmu.cs.dennisc.alice.Project;
import edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice;
import edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine;
import edu.cmu.cs.dennisc.animation.Program;

/**
 * @author Dennis Cosgrove
 */
public class ExportVideoUploadToYouTubeOperation extends org.alice.ide.operations.AbstractActionOperation {
	public ExportVideoUploadToYouTubeOperation() {
		this.putValue( javax.swing.Action.NAME, "Export Video / Upload To YouTube\u2122..." );
	}
	public void perform( zoot.ActionContext actionContext ) {
		final int frameRate = 24;
		
		edu.cmu.cs.dennisc.alice.Project project = this.getIDE().getProject();
		
		//this.rtProgram = new RecordableRuntimeProgram( sceneType, vm );
		VideoCapturePane videoCapturePane = new VideoCapturePane(project, frameRate){

			@Override
			protected edu.cmu.cs.dennisc.animation.Program createProgram( Project project )
			{
				edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vm = new edu.cmu.cs.dennisc.alice.virtualmachine.ReleaseVirtualMachine();
				edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice sceneType = (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)project.getProgramType().getDeclaredFields().get( 0 ).getValueType();
				return ExportVideoUploadToYouTubeOperation.this.getIDE().createRuntimeProgram( vm, sceneType, frameRate );
			}
			
			@Override
			protected void onClose()
			{
				javax.swing.SwingUtilities.getRoot( this ).setVisible( false );
			}
		};

		javax.swing.JDialog dialog = edu.cmu.cs.dennisc.swing.JDialogUtilities.createPackedJDialog( videoCapturePane, this.getIDE(), "Export Video", true, javax.swing.WindowConstants.DISPOSE_ON_CLOSE );
		dialog.setVisible( true );
		//todo
		//actionContext.commit();
	}
}
