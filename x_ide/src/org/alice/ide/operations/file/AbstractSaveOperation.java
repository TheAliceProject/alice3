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
public abstract class AbstractSaveOperation extends AbstractClearanceActionOperation {
	protected abstract boolean isPromptNecessary( java.io.File file );
	protected abstract java.io.File getDefaultDirectory();
	protected abstract String getExtension();
	protected abstract void save( java.io.File file ) throws java.io.IOException;
	protected abstract String getInitialFilename();
	
	private java.io.File fileNext;
	public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		java.io.File filePrevious = this.getIDE().getFile();
		boolean isExceptionRaised;
		do {
			if( this.isPromptNecessary( filePrevious ) ) {
				fileNext = edu.cmu.cs.dennisc.awt.FileDialogUtilities.showSaveFileDialog( this.getIDE(), this.getDefaultDirectory(), this.getInitialFilename(), this.getExtension(), true );
			} else {
				fileNext = filePrevious;
			}
			isExceptionRaised = false;
			if( this.fileNext != null ) {
				try {
					this.save( fileNext );
				} catch( java.io.IOException ioe ) {
					isExceptionRaised = true;
					javax.swing.JOptionPane.showMessageDialog( this.getIDE(), ioe.getMessage(), "Unable to save file", javax.swing.JOptionPane.ERROR_MESSAGE );
				}
				if( isExceptionRaised ) {
					//pass
				} else {
					actionContext.commit();
				}
			} else {
				actionContext.cancel();
			}
		} while( isExceptionRaised );
	}

}
