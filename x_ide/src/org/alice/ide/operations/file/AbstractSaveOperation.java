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
	protected abstract boolean isPromptNecessary();
	protected abstract java.io.File getDefaultDirectory();
	protected abstract String getExtension();
	protected abstract void save( java.io.File file );
	public void perform( zoot.ActionContext actionContext ) {
		if( this.isPromptNecessary() ) {
			java.io.File file = edu.cmu.cs.dennisc.awt.FileDialogUtilities.showSaveFileDialog( this.getIDE(), this.getDefaultDirectory(), this.getExtension(), true );
			if( file != null ) {
				this.save( file );
				actionContext.commit();
			} else {
				actionContext.cancel();
			}
		}
//		if self._isPromptNecessary():
//			#owner = e.getSource()
//			owner = self.getIDE()
//			#self._file = ecc.dennisc.swing.showFileSaveAsDialog( owner, self._getDefaultDirectory(), self._getExtension() )
//			self._file = edu.cmu.cs.dennisc.awt.FileDialogUtilities.showSaveFileDialog( owner, self._getDefaultDirectory(), self._getExtension(), True )
//			if self._file:
//				return alice.ide.Operation.PreparationResult.PERFORM
//			else:
//				return alice.ide.Operation.PreparationResult.CANCEL
//		else:
//			return alice.ide.Operation.PreparationResult.PERFORM
//
//		self.getIDE().saveProjectTo( self._file )
	}
}
