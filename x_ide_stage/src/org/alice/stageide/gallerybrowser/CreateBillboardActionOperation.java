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
class CreateBillboardActionOperation extends AbstractDeclareFieldOperation {
	public CreateBillboardActionOperation() {
		this.putValue(javax.swing.Action.NAME, "Create Billboard...");
	}
	@Override
	protected edu.cmu.cs.dennisc.pattern.Tuple2<edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, Object> createFieldAndInstance(edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ownerType) {
		org.alice.ide.resource.ImageResourcePrompter imageResourcePrompter = org.alice.ide.resource.ImageResourcePrompter.getSingleton();
		try {
			org.alice.virtualmachine.resources.ImageResource frontImageResource = imageResourcePrompter.promptUserForResource( this.getIDE() );
			if( frontImageResource != null ) {
				edu.cmu.cs.dennisc.alice.Project project = this.getIDE().getProject();
				if( project != null ) {
					project.addResource( frontImageResource );
				}
				org.alice.apis.moveandturn.ImageSource frontImageSource = new org.alice.apis.moveandturn.ImageSource( frontImageResource );
				org.alice.apis.moveandturn.Billboard billboard = new org.alice.apis.moveandturn.Billboard();
				billboard.setFrontImageSource( frontImageSource );
				
				String name = "billboard";
				edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice type = this.getIDE().getTypeDeclaredInAliceFor(org.alice.apis.moveandturn.Billboard.class);
				edu.cmu.cs.dennisc.alice.ast.Expression initializer = org.alice.ide.ast.NodeUtilities.createInstanceCreation(type);
				edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field = new edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice(name, type, initializer);
				field.finalVolatileOrNeither.setValue(edu.cmu.cs.dennisc.alice.ast.FieldModifierFinalVolatileOrNeither.FINAL);
				field.access.setValue(edu.cmu.cs.dennisc.alice.ast.Access.PRIVATE);
				return new edu.cmu.cs.dennisc.pattern.Tuple2<edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, Object>( field, billboard );
			} else {
				return null;
			}
		} catch( java.io.IOException ioe ) {
			throw new RuntimeException( ioe );
		}
	}
}
