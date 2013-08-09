/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.alice.ide.ast.export.type;

/**
 * @author Dennis Cosgrove
 */
public final class TypeDetails {
	public static final double CURRENT_VERSION = 3.1;
	public static final double MINIMUM_ACCEPTABLE_VERSION = 3.1;

	private final double version;
	private final String typeName;
	private final String resourceClassName;
	private final java.util.List<String> procedureNames;
	private final java.util.List<String> functionNames;
	private final java.util.List<String> fieldNames;

	public TypeDetails( org.lgna.project.ast.NamedUserType type ) {
		this.version = CURRENT_VERSION;
		this.typeName = type.getName();

		org.lgna.project.ast.JavaType resourceType = org.alice.ide.typemanager.ResourceTypeUtilities.getResourceType( type );
		if( resourceType != null ) {
			this.resourceClassName = resourceType.getClassReflectionProxy().getName();
		} else {
			this.resourceClassName = null;
		}

		this.procedureNames = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		this.functionNames = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		for( org.lgna.project.ast.UserMethod method : type.methods ) {
			if( method.isProcedure() ) {
				this.procedureNames.add( method.getName() );
			} else {
				this.functionNames.add( method.getName() );
			}
		}
		this.fieldNames = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		for( org.lgna.project.ast.UserField field : type.fields ) {
			this.fieldNames.add( field.getName() );
		}
	}

	public TypeDetails( double version, String typeName, String resourceClassName, java.util.List<String> procedureNames, java.util.List<String> functionNames, java.util.List<String> fieldNames ) {
		this.version = version;
		this.typeName = typeName;
		this.resourceClassName = resourceClassName;
		this.procedureNames = procedureNames;
		this.functionNames = functionNames;
		this.fieldNames = fieldNames;
	}

	public double getVersion() {
		return this.version;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public String getResourceClassName() {
		return this.resourceClassName;
	}

	public java.util.List<String> getProcedureNames() {
		return this.procedureNames;
	}

	public java.util.List<String> getFunctionNames() {
		return this.functionNames;
	}

	public java.util.List<String> getFieldNames() {
		return this.fieldNames;
	}
}
