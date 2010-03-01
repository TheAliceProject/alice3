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
package org.alice.stageide.cascade.customfillin;

/**
 * @author Dennis Cosgrove
 */
public abstract class ImportNewSourceFillIn< E, F extends org.alice.virtualmachine.Resource > extends edu.cmu.cs.dennisc.cascade.FillIn<edu.cmu.cs.dennisc.alice.ast.InstanceCreation> {
	@Override
	public edu.cmu.cs.dennisc.alice.ast.InstanceCreation getTransientValue() {
		return null;
	}
	
	protected abstract Class< E > getSourceClass();
	protected abstract Class< F > getResourceClass();
	protected abstract org.alice.ide.resource.prompter.ResourcePrompter<F> getResourcePrompter();
	protected abstract String getMenuText();
	@Override
	public edu.cmu.cs.dennisc.alice.ast.InstanceCreation getValue() {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		try {
			F resource = this.getResourcePrompter().promptUserForResource( ide );
			if( resource != null ) {
				edu.cmu.cs.dennisc.alice.Project project = ide.getProject();
				if( project != null ) {
					project.addResource( resource );
				}
				edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava constructor = edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava.get( getSourceClass(), getResourceClass() );
				edu.cmu.cs.dennisc.alice.ast.ResourceExpression resourceExpression = new edu.cmu.cs.dennisc.alice.ast.ResourceExpression( getResourceClass(), resource );
				edu.cmu.cs.dennisc.alice.ast.AbstractParameter parameter0 = constructor.getParameters().get( 0 );
				edu.cmu.cs.dennisc.alice.ast.Argument argument0 = new edu.cmu.cs.dennisc.alice.ast.Argument( parameter0, resourceExpression );
				return new edu.cmu.cs.dennisc.alice.ast.InstanceCreation( constructor, argument0 );
			} else {
				throw new edu.cmu.cs.dennisc.cascade.CancelException( "" );
			}
		} catch( java.io.IOException ioe ) {
			//todo
			throw new edu.cmu.cs.dennisc.cascade.CancelException( "" );
		}
	}
	@Override
	protected void addChildren() {
	}
	@Override
	protected javax.swing.JComponent createMenuProxy() {
		return edu.cmu.cs.dennisc.croquet.LabelUtilities.createLabel( this.getMenuText() );
	}
}
