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
package alice.ide;

/**
 * @author Dennis Cosgrove
 */
public class Perspective extends Component {
	private javax.swing.JSplitPane root = new javax.swing.JSplitPane( javax.swing.JSplitPane.HORIZONTAL_SPLIT );
	private javax.swing.JSplitPane left = new javax.swing.JSplitPane( javax.swing.JSplitPane.VERTICAL_SPLIT );
	private javax.swing.JSplitPane right = new javax.swing.JSplitPane( javax.swing.JSplitPane.VERTICAL_SPLIT );
	private alice.ide.listenerseditor.ListenersEditor listenersEditor = this.createListenersEditor();
	private alice.ide.sceneeditor.SceneEditor sceneEditor = this.createSceneEditor(); 
	private alice.ide.classmemberseditor.ClassMembersEditor classMembersEditor = this.createClassMembersEditor(); 
	private zoot.ZTabbedPane tabbedPane = new zoot.ZTabbedPane( null );
	private zoot.ZLabel feedback = new zoot.ZLabel();

	protected alice.ide.sceneeditor.SceneEditor createSceneEditor() {
		return new alice.ide.sceneeditor.SceneEditor();
	}
	protected alice.ide.listenerseditor.ListenersEditor createListenersEditor() {
		return new alice.ide.listenerseditor.ListenersEditor();
	}
	protected alice.ide.classmemberseditor.ClassMembersEditor createClassMembersEditor() {
		return new alice.ide.classmemberseditor.ClassMembersEditor();
	}
	
	public Perspective() {
		this.root.setLeftComponent( this.left );
		this.root.setRightComponent( this.right );
		this.left.setTopComponent( this.sceneEditor );
		this.left.setBottomComponent( this.classMembersEditor );
		this.right.setTopComponent( this.listenersEditor );
		this.right.setBottomComponent( this.tabbedPane );
		this.setLayout( new java.awt.BorderLayout() );
		this.add( this.root, java.awt.BorderLayout.CENTER );
		this.add( this.feedback, java.awt.BorderLayout.SOUTH );
	}
	
	public void activate( java.awt.Container container ) {
		assert container.getComponentCount() == 0;
		container.setLayout( new edu.cmu.cs.dennisc.awt.ExpandAllToBoundsLayoutManager() );
		container.add( this );
	}
	public void deactivate( java.awt.Container container ) {
		container.remove( this );
	}
}
