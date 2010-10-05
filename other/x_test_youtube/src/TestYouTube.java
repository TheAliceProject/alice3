import java.io.File;

import org.alice.media.VideoCapturePane;
import org.alice.stageide.MoveAndTurnRuntimeProgram;

import edu.cmu.cs.dennisc.alice.Project;

/**
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

/**
 * @author David Culyba
 */
public class TestYouTube
{
	public static void main( String[] args ) {
		zoot.ZFrame frame = new zoot.ZFrame() {
			@Override
			protected void handleWindowOpened( java.awt.event.WindowEvent e ) {
			}
			@Override
			public void handleQuit( java.util.EventObject e ) {
				System.exit( 0 );
			}
		};
		final int frameRate = 24;
		
		File projectFile = new File( edu.cmu.cs.dennisc.alice.io.FileUtilities.getMyProjectsDirectory(), "movieTest.a3p" );
		edu.cmu.cs.dennisc.alice.Project project = edu.cmu.cs.dennisc.alice.io.FileUtilities.readProject(projectFile);
		
		//this.rtProgram = new RecordableRuntimeProgram( sceneType, vm );
		frame.getContentPane().add( new VideoCapturePane(project, frameRate){

			@Override
			protected edu.cmu.cs.dennisc.animation.Program createProgram( Project project )
			{
				edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vm = new edu.cmu.cs.dennisc.alice.virtualmachine.ReleaseVirtualMachine();
				edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice sceneType = (edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice)project.getProgramType().getDeclaredFields().get( 0 ).getValueType();
				
				return new MoveAndTurnRuntimeProgram(sceneType, vm)
				{
					@Override
					protected java.awt.Component createSpeedMultiplierControlPanel() 
					{
						return null;
					}
					@Override
					protected edu.cmu.cs.dennisc.animation.Animator createAnimator() 
					{
						return new edu.cmu.cs.dennisc.animation.FrameBasedAnimator( frameRate );
					}

					@Override
					protected void postRun() 
					{
						super.postRun();
						this.setMovieEncoder( null );
					}
				};
			}
			
			@Override
			protected void onClose()
			{
				this.setVisible( false );
				System.exit( 0 );
			}
		});
		
		frame.pack();
		frame.setVisible( true );

	}

}
