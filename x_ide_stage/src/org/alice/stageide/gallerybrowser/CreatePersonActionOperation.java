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
class CreateFieldFromPersonPane extends org.alice.ide.declarationpanes.CreateLargelyPredeterminedFieldPane {
	public CreateFieldFromPersonPane( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType, org.alice.apis.stage.Person person ) {
		super( declaringType, person.getClass(), null );
	}
}

/**
 * @author Dennis Cosgrove
 */
class CreatePersonActionOperation extends AbstractGalleryDeclareFieldOperation {
	private org.alice.apis.stage.Person person;
	public CreatePersonActionOperation( org.alice.apis.stage.Person person ) {
		this.person = person;
	}
	@Override
	protected edu.cmu.cs.dennisc.pattern.Tuple2< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, Object > createFieldAndInstance( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ownerType ) {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice declaringType = ide.getSceneType();
		CreateFieldFromPersonPane createFieldFromPersonPane = new CreateFieldFromPersonPane( declaringType, person );
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field = createFieldFromPersonPane.showInJDialog( ide );
		if( field != null ) {
			//ide.getSceneEditor().handleFieldCreation( declaringType, field, person );
			return new edu.cmu.cs.dennisc.pattern.Tuple2< edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, Object >( field, this.person );
		} else {
			return null;
		}
	}
}
