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
package org.alice.stageide.gallerybrowser;

/**
 * @author Dennis Cosgrove
 */
class GalleryFileActionOperation extends AbstractDeclareFieldOperation {
	private java.io.File file;
	public GalleryFileActionOperation(java.io.File file) {
		this.file = file;
	}
	@Override
	protected edu.cmu.cs.dennisc.pattern.Tuple2<edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, java.lang.Object> createFieldAndInstance(edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ownerType) {
		org.alice.ide.createdeclarationpanes.CreateFieldFromGalleryPane createFieldPane = new org.alice.ide.createdeclarationpanes.CreateFieldFromGalleryPane(ownerType, this.file);
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field = createFieldPane.showInJDialog(this.getIDE(), "Create New Instance", true);
		if (field != null) {
			return new edu.cmu.cs.dennisc.pattern.Tuple2<edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, Object>( field, createFieldPane.createInstanceInJava() );
		} else {
			return null;
		}
	}
}
