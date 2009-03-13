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
public class Perspective extends Component {
	private javax.swing.JSplitPane root = new javax.swing.JSplitPane( javax.swing.JSplitPane.HORIZONTAL_SPLIT );
	private javax.swing.JSplitPane left = new javax.swing.JSplitPane( javax.swing.JSplitPane.VERTICAL_SPLIT );
	private javax.swing.JSplitPane right = new javax.swing.JSplitPane( javax.swing.JSplitPane.VERTICAL_SPLIT );

	public Perspective() {
		this.root.setLeftComponent( this.left );
		this.root.setRightComponent( this.right );
		this.root.setDividerLocation( 320 );
		this.left.setDividerLocation( 240 );
		this.setLayout( new edu.cmu.cs.dennisc.awt.ExpandAllToBoundsLayoutManager() );
		this.add( this.root );
	}
	
	public void activate( java.awt.Component sceneEditor, java.awt.Component membersEditor, java.awt.Component listenersEditor, java.awt.Component tabbedPane ) {
		this.left.setTopComponent( sceneEditor );
		this.left.setBottomComponent( membersEditor );
		this.right.setTopComponent( listenersEditor );
		this.right.setBottomComponent( tabbedPane );
	}
	public void deactivate( java.awt.Container container ) {
		this.left.setTopComponent( null );
		this.left.setBottomComponent( null );
		this.right.setTopComponent( null );
		this.right.setBottomComponent( null );
	}
}
