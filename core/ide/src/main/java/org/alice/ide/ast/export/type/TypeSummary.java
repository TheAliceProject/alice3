/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.ide.ast.export.type;

/**
 * @author Dennis Cosgrove
 */
public final class TypeSummary {
	public static final double CURRENT_VERSION = 3.1;
	public static final double MINIMUM_ACCEPTABLE_VERSION = 3.1;

	private static void addHierarchyClassNames( java.util.List<String> hierarchyClassNames, org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		if( type != null ) {
			if( type instanceof org.lgna.project.ast.JavaType ) {
				org.lgna.project.ast.JavaType javaType = (org.lgna.project.ast.JavaType)type;
				hierarchyClassNames.add( javaType.getClassReflectionProxy().getReification().getName() );
			} else {
				hierarchyClassNames.add( type.getName() );
				addHierarchyClassNames( hierarchyClassNames, type.getSuperType() );
			}
		} else {
			//pass
		}
	}

	private final double version;
	private final String typeName;
	private final java.util.List<String> hierarchyClassNames;
	private final ResourceInfo resourceInfo;
	private final java.util.List<String> procedureNames;
	private final java.util.List<FunctionInfo> functionInfos;
	private final java.util.List<FieldInfo> fieldInfos;

	public TypeSummary( org.lgna.project.ast.NamedUserType type ) {
		this.version = CURRENT_VERSION;
		this.typeName = type.getName();

		this.hierarchyClassNames = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		addHierarchyClassNames( this.hierarchyClassNames, type.getSuperType() );

		org.lgna.project.ast.Declaration declaration = org.alice.ide.typemanager.ResourceTypeUtilities.getResourceFieldOrType( type );
		if( declaration instanceof org.lgna.project.ast.JavaType ) {
			org.lgna.project.ast.JavaType resourceType = (org.lgna.project.ast.JavaType)declaration;
			this.resourceInfo = new ResourceInfo( resourceType.getClassReflectionProxy().getName(), null );
		} else if( declaration instanceof org.lgna.project.ast.JavaField ) {
			org.lgna.project.ast.JavaField resourceField = (org.lgna.project.ast.JavaField)declaration;
			this.resourceInfo = new ResourceInfo( resourceField.getDeclaringType().getClassReflectionProxy().getName(), resourceField.getName() );
		} else {
			this.resourceInfo = null;
		}

		this.procedureNames = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		this.functionInfos = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		for( org.lgna.project.ast.UserMethod method : type.methods ) {
			org.lgna.project.ast.ManagementLevel managementLevel = method.getManagementLevel();
			if( ( managementLevel == null ) || ( managementLevel == org.lgna.project.ast.ManagementLevel.NONE ) ) {
				if( method.isProcedure() ) {
					this.procedureNames.add( method.getName() );
				} else {
					this.functionInfos.add( new FunctionInfo( method.getReturnType().getName(), method.getName() ) );
				}
			}
		}
		this.fieldInfos = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		for( org.lgna.project.ast.UserField field : type.fields ) {
			this.fieldInfos.add( new FieldInfo( field.getValueType().getName(), field.getName() ) );
		}
	}

	public TypeSummary( double version, String typeName, java.util.List<String> hierarchyClassNames, ResourceInfo resourceInfo, java.util.List<String> procedureNames, java.util.List<FunctionInfo> functionInfos, java.util.List<FieldInfo> fieldInfos ) {
		this.version = version;
		this.typeName = typeName;
		this.resourceInfo = resourceInfo;
		this.hierarchyClassNames = hierarchyClassNames;
		this.procedureNames = procedureNames;
		this.functionInfos = functionInfos;
		this.fieldInfos = fieldInfos;
	}

	public double getVersion() {
		return this.version;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public java.util.List<String> getHierarchyClassNames() {
		return this.hierarchyClassNames;
	}

	public ResourceInfo getResourceInfo() {
		return this.resourceInfo;
	}

	public java.util.List<String> getProcedureNames() {
		return this.procedureNames;
	}

	public java.util.List<FunctionInfo> getFunctionInfos() {
		return this.functionInfos;
	}

	public java.util.List<FieldInfo> getFieldInfos() {
		return this.fieldInfos;
	}
}
