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
package org.alice.stageide;

public class MoveAndTurnRuntimeProgram extends org.alice.apis.moveandturn.Program {
	private static java.awt.Dimension preferredSize = new java.awt.Dimension( 400, 300 );
	private edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> sceneType;
	private edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice sceneInstance;
	private edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vm;
	public MoveAndTurnRuntimeProgram( edu.cmu.cs.dennisc.alice.ast.AbstractType<?,?,?> sceneType, edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vm ) {
		this.sceneType = sceneType;
		this.vm = vm;
		this.addComponentListener( new java.awt.event.ComponentListener() {
			public void componentShown( java.awt.event.ComponentEvent e ) {
			}
			public void componentHidden( java.awt.event.ComponentEvent e ) {
			}
			public void componentMoved( java.awt.event.ComponentEvent e ) {
			}
			public void componentResized( java.awt.event.ComponentEvent e ) {
				e.getComponent().getSize( MoveAndTurnRuntimeProgram.preferredSize );
			}
		} );
	}
	
	public edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine getVM() {
		return this.vm;
	}
	public edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice getSceneInstance() {
		return this.sceneInstance;
	}
	@Override
	protected void initialize() {
		//this.vm.setResources( org.alice.ide.IDE.getSingleton().getResources() );
		this.sceneInstance = (edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice)this.vm.createInstanceEntryPoint( this.sceneType );
		org.alice.apis.moveandturn.Scene scene = (org.alice.apis.moveandturn.Scene)this.sceneInstance.getInstanceInJava();
		this.setScene( scene );
	}
	@Override
	protected void run() {
		this.getOnscreenLookingGlass().getAWTComponent().requestFocus();
		this.vm.invokeEntryPoint( this.sceneType.getDeclaredMethod( "run" ), this.sceneInstance );
	}
	
	private org.lgna.croquet.Operation getRestartOperation() {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		if( ide != null ) {
			return ide.getRestartOperation();
		} else {
			return null;
		}
	}
	@Override
	protected boolean isRestartSupported() {
		return getRestartOperation() != null;
	}
	@Override
	protected void restart( java.awt.event.ActionEvent e ) {
		super.restart( e );
		javax.swing.SwingUtilities.getRoot( this ).setVisible( false );
		this.getRestartOperation().fire( e );
	}
	
	@Override
	public java.awt.Dimension getPreferredSize() {
		return MoveAndTurnRuntimeProgram.preferredSize;
	}
}
