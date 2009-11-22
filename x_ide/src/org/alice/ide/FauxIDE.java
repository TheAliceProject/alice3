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
package org.alice.ide;

/**
 * @author Dennis Cosgrove
 */
public class FauxIDE extends IDE {
	@Override
	protected edu.cmu.cs.dennisc.zoot.ActionOperation createAboutOperation() {
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
	public java.io.File getGalleryRootDirectory() {
		return null;
	}
	@Override
	protected org.alice.ide.gallerybrowser.AbstractGalleryBrowser createGalleryBrowser( java.io.File galleryRootDirectory ) {
		return new org.alice.ide.gallerybrowser.FauxGalleryBrowser( galleryRootDirectory );
	}
	@Override
	public void handleRun( edu.cmu.cs.dennisc.zoot.ActionContext context, edu.cmu.cs.dennisc.alice.ast.AbstractType sceneType ) {
		javax.swing.JOptionPane.showMessageDialog( this, "imagine the program running here..." );
	}
	@Override
	public void handlePreviewMethod( edu.cmu.cs.dennisc.zoot.ActionContext actionContext, edu.cmu.cs.dennisc.alice.ast.MethodInvocation emptyExpressionMethodInvocation ) {
		javax.swing.JOptionPane.showMessageDialog( this, "imagine testing method here..." );
	}
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
	protected org.alice.ide.openprojectpane.TabContentPane createTemplatesPane() {
		return null;
	}
	public static void main( String[] args ) {
		org.alice.ide.LaunchUtilities.launch( FauxIDE.class, null, args );
	}
}
