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
package org.alice.ide;

/**
 * @author Dennis Cosgrove
 */
public class FauxIDE extends IDE {
	@Override
	protected edu.cmu.cs.dennisc.croquet.Operation createAboutOperation() {
		return null;
	}
	@Override
	protected void promptForLicenseAgreements() {
	}
	@Override
	protected org.alice.ide.sceneeditor.AbstractSceneEditor createSceneEditor() {
		return new org.alice.ide.sceneeditor.FauxSceneEditor();
	}
	@Override
	public javax.swing.tree.TreeNode getGalleryRoot() {
		return null;
	}
	@Override
	protected org.alice.ide.gallerybrowser.AbstractGalleryBrowser createGalleryBrowser( javax.swing.tree.TreeNode root ) {
		return new org.alice.ide.gallerybrowser.FauxGalleryBrowser( root );
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.DialogOperation createRunOperation() {
		return new edu.cmu.cs.dennisc.croquet.DialogOperation( null, java.util.UUID.fromString( "158967ad-956c-4870-8f3c-fb1f790523a6" ) ) {
			@Override
			protected edu.cmu.cs.dennisc.croquet.Container< ? > createContentPane( edu.cmu.cs.dennisc.croquet.DialogOperationContext context, edu.cmu.cs.dennisc.croquet.Dialog dialog ) {
				return null;
			}
			@Override
			protected void releaseContentPane( edu.cmu.cs.dennisc.croquet.DialogOperationContext context, edu.cmu.cs.dennisc.croquet.Dialog dialog, edu.cmu.cs.dennisc.croquet.Container< ? > contentPane ) {
			}
		};
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.Operation createRestartOperation() {
		return null;
	}
	@Override
	public edu.cmu.cs.dennisc.croquet.Operation createPreviewOperation( org.alice.ide.memberseditor.templates.ProcedureInvocationTemplate procedureInvocationTemplate ) {
		return null;
	}
//	@Override
//	public void handleRun( edu.cmu.cs.dennisc.croquet.ModelContext context, edu.cmu.cs.dennisc.alice.ast.AbstractType sceneType ) {
//		this.showMessageDialog( "imagine the program running here..." );
//	}
//	@Override
//	public void handleRestart( edu.cmu.cs.dennisc.croquet.ModelContext context ) {
//	}
//	@Override
//	public void handlePreviewMethod( edu.cmu.cs.dennisc.croquet.ModelContext context, edu.cmu.cs.dennisc.alice.ast.MethodInvocation emptyExpressionMethodInvocation ) {
//		this.showMessageDialog( "imagine testing method here..." );
//	}
	@Override
	public boolean isInstanceCreationAllowableFor( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice typeInAlice ) {
		return true;
	}
	@Override
	public edu.cmu.cs.dennisc.animation.Program createRuntimeProgram( edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vm, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice sceneType, int frameRate ) {
		return null;
	}
	@Override
	protected java.awt.image.BufferedImage createThumbnail() throws java.lang.Throwable {
		return null;
	}
	@Override
	protected org.alice.app.openprojectpane.TabContentPanel createTemplatesTabContentPane() {
		return null;
	}
	public static void main( String[] args ) {
		org.alice.ide.LaunchUtilities.launch( FauxIDE.class, null, args );
	}
}
