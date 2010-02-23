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
package org.alice.stageide.operations.ast;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractFieldTileActionOperation extends org.alice.ide.operations.ast.AbstractFieldActionOperation {
	public AbstractFieldTileActionOperation( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		super( org.alice.ide.IDE.PROJECT_GROUP, field );
	}
	
	//todo: remove
	protected org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor getMoveAndTurnSceneEditor() {
		return edu.cmu.cs.dennisc.lang.ClassUtilities.getInstance( getIDE().getSceneEditor(), org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor.class );
	}
	
	
	@Override
	public boolean isSignificant() {
		return true;
	}
}
