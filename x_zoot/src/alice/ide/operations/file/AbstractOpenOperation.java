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
package alice.ide.operations.file;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractOpenOperation extends AbstractOperation {
	protected abstract boolean isNew();
	protected zoot.Operation getSelectProjectToOpenOperation() {
		throw new RuntimeException( "todo" );
	}
	
	public void perform( zoot.Context context ) {
		if( this.getIDE().isProjectUpToDateWithFile() ) {
			//pass
		} else {
			java.util.EventObject e = null;
			zoot.Context checkContext = context.perform( this.getClearToProcedeWithChangedProjectOperation(), e );
			if( checkContext.isCommitted() ) {
				//pass
			} else {
				context.cancel();
			}
		}
		if( context.isCancelled() ) {
			//pass
		} else {
			zoot.Context selectContext = context.perform( this.getSelectProjectToOpenOperation(), null );
			if( selectContext.isCommitted() ) {
				final Object FILE_KEY = "FILE_KEY";
				java.io.File file = selectContext.get( FILE_KEY, java.io.File.class );
				if( file != null ) {
					this.getIDE().loadProjectFrom( file );
				} else {
					this.getIDE().createProjectFromBootstrap();
				}
				context.commit();
			} else {
				context.cancel();
			}
		}
	}
}
