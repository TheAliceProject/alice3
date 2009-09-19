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

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractOpenProjectOperation extends AbstractClearanceActionOperation {
	public static final Object FILE_KEY = "FILE_KEY";
	protected abstract boolean isNew();
	protected zoot.ActionOperation getSelectProjectToOpenOperation() {
		return this.getIDE().getSelectProjectToOpenOperation( this.isNew() );
	}
	public void perform( zoot.ActionContext actionContext ) {
		if( this.getIDE().isProjectUpToDateWithFile() ) {
			//pass
		} else {
			java.util.EventObject e = null;
			zoot.Context checkContext = actionContext.perform( this.getClearToProcedeWithChangedProjectOperation(), e, zoot.ZManager.CANCEL_IS_WORTHWHILE );
			if( checkContext.isCommitted() ) {
				//pass
			} else {
				actionContext.cancel();
			}
		}
		if( actionContext.isCancelled() ) {
			//pass
		} else {
			zoot.ActionContext selectProjectContext = actionContext.perform( this.getSelectProjectToOpenOperation(), null, zoot.ZManager.CANCEL_IS_WORTHWHILE );
			if( selectProjectContext.isCommitted() ) {
				java.io.File file = selectProjectContext.get( FILE_KEY, java.io.File.class );
				if( file != null ) {
					this.getIDE().loadProjectFrom( file );
				} else {
					this.getIDE().createProjectFromBootstrap();
				}
				actionContext.commitAndInvokeRedoIfAppropriate();
			} else {
				actionContext.cancel();
			}
		}
	}
	@Override
	public boolean canRedo() {
		return false;
	}
	@Override
	public boolean canUndo() {
		return false;
	}
	@Override
	public boolean isSignificant() {
		return false;
	}
}
