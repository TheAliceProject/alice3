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

package org.alice.ide.name.validators;

public class ResourceNameValidator extends org.alice.ide.name.NameValidator {
	private org.alice.virtualmachine.Resource resource;
	public ResourceNameValidator( org.alice.virtualmachine.Resource resource ) {
		this.resource = resource;
	}
	public org.alice.virtualmachine.Resource getResource() {
		return this.resource;
	}
	@Override
	protected boolean isNameAvailable( String name ) {
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		if( ide != null ) {
			edu.cmu.cs.dennisc.alice.Project project = ide.getProject();
			if( project != null ) {
				java.util.Set< org.alice.virtualmachine.Resource > resources = project.getResources();
				if( resources != null ) {
					for( org.alice.virtualmachine.Resource resource : resources ) {
						if( resource != null && resource != this.resource ) {
							if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( name, resource.getName(), edu.cmu.cs.dennisc.equivalence.CaseSensitivityPolicy.INSENSITIVE ) ) {
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}
	@Override
	public boolean isNameValid( String name ) {
		//todo?
		return true;
	}
}
