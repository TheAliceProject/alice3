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
package org.alice.ide.ast.export;

/**
 * @author Dennis Cosgrove
 */
public class MemberInfo<D extends org.lgna.project.ast.Member> extends DeclarationInfo<D> {
	private class Dependencies implements edu.cmu.cs.dennisc.pattern.Crawler {
		private final java.util.List<TypeInfo> typeInfos = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		private final java.util.List<FieldInfo> fieldInfos = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		private final java.util.List<MethodInfo> methodInfos = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		private final java.util.List<ConstructorInfo> constructorInfos = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

		@Override
		public void visit( edu.cmu.cs.dennisc.pattern.Crawlable crawlable ) {
			if( crawlable == MemberInfo.this.getDeclaration() ) {
				//pass
			} else {
				if( crawlable instanceof org.lgna.project.ast.NamedUserType ) {
					org.lgna.project.ast.NamedUserType type = (org.lgna.project.ast.NamedUserType)crawlable;
					TypeInfo typeInfo = getProjectInfo().getInfoForType( type );
					this.typeInfos.add( typeInfo );
				} else if( crawlable instanceof org.lgna.project.ast.UserConstructor ) {
					org.lgna.project.ast.UserConstructor constructor = (org.lgna.project.ast.UserConstructor)crawlable;
					TypeInfo typeInfo = getProjectInfo().getInfoForType( constructor.getDeclaringType() );
					ConstructorInfo constructorInfo = typeInfo.getInfoForConstructor( constructor );
					this.constructorInfos.add( constructorInfo );
				} else if( crawlable instanceof org.lgna.project.ast.UserMethod ) {
					org.lgna.project.ast.UserMethod method = (org.lgna.project.ast.UserMethod)crawlable;
					TypeInfo typeInfo = getProjectInfo().getInfoForType( method.getDeclaringType() );
					MethodInfo methodInfo = typeInfo.getInfoForMethod( method );
					this.methodInfos.add( methodInfo );
				} else if( crawlable instanceof org.lgna.project.ast.UserField ) {
					org.lgna.project.ast.UserField field = (org.lgna.project.ast.UserField)crawlable;
					TypeInfo typeInfo = getProjectInfo().getInfoForType( field.getDeclaringType() );
					FieldInfo fieldInfo = typeInfo.getInfoForField( field );
					this.fieldInfos.add( fieldInfo );
				}
			}
		}

		public String getToolTipText() {
			StringBuilder sb = new StringBuilder();
			for( TypeInfo info : this.typeInfos ) {
				sb.append( info.getDeclaration().getName() );
				sb.append( ", " );
			}
			for( FieldInfo info : this.fieldInfos ) {
				sb.append( info.getDeclaration().getName() );
				sb.append( ", " );
			}
			for( MethodInfo info : this.methodInfos ) {
				sb.append( info.getDeclaration().getName() );
				sb.append( ", " );
			}
			for( ConstructorInfo info : this.constructorInfos ) {
				sb.append( info.getDeclaration().getName() );
				sb.append( ", " );
			}
			return sb.toString();
		}
	}

	private TypeInfo declaringTypeInfo;
	private Dependencies dependencies;

	public MemberInfo( ProjectInfo projectInfo, D declaration ) {
		super( projectInfo, declaration );
	}

	/* package-private */void updateDependencies() {
		this.dependencies = new Dependencies();
		this.declaringTypeInfo = this.getProjectInfo().getInfoForType( (org.lgna.project.ast.UserType<?>)this.getDeclaration().getDeclaringType() );
		this.getDeclaration().crawl( this.dependencies, org.lgna.project.ast.CrawlPolicy.INCLUDE_REFERENCES_BUT_DO_NOT_TUNNEL, null );
		this.getCheckBox().setToolTipText( this.dependencies.getToolTipText() );
	}

	@Override
	protected void addRequired( java.util.Set<org.alice.ide.ast.export.DeclarationInfo<?>> visited ) {
		super.addRequired( visited );
		this.declaringTypeInfo.updateRequired( visited );
		for( TypeInfo info : this.dependencies.typeInfos ) {
			info.updateRequired( visited );
		}
		for( FieldInfo info : this.dependencies.fieldInfos ) {
			info.updateRequired( visited );
		}
		for( MethodInfo info : this.dependencies.methodInfos ) {
			info.updateRequired( visited );
		}
		for( ConstructorInfo info : this.dependencies.constructorInfos ) {
			info.updateRequired( visited );
		}
	}
}
