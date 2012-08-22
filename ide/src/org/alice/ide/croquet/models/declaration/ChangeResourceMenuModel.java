/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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

package org.alice.ide.croquet.models.declaration;

/**
 * @author Dennis Cosgrove
 */
public class ChangeResourceMenuModel extends org.lgna.croquet.CascadeMenuModel<org.lgna.project.ast.InstanceCreation> {
	private static class SingletonHolder {
		private static ChangeResourceMenuModel instance = new ChangeResourceMenuModel();
	}

	public static ChangeResourceMenuModel getInstance() {
		return SingletonHolder.instance;
	}

	private ChangeResourceMenuModel() {
		super( java.util.UUID.fromString( "11f6a90d-0d39-4893-a092-21a68205aaf1" ) );
	}

	@Override
	protected java.util.List<org.lgna.croquet.CascadeBlankChild> updateBlankChildren( java.util.List<org.lgna.croquet.CascadeBlankChild> rv, org.lgna.croquet.cascade.BlankNode<org.lgna.project.ast.InstanceCreation> blankNode ) {
		java.util.List<org.lgna.project.ast.JavaType> topLevelTypes = org.alice.ide.IDE.getActiveInstance().getApiConfigurationManager().getTopLevelGalleryTypes();
		for( org.lgna.project.ast.JavaType type : topLevelTypes ) {
			rv.add( GalleryResourceTypeFillIn.getInstance( type ) );
			//rv.add( GalleryResourceMenu.getInstance( type.getDeclaredConstructors().get( 0 ).getRequiredParameters().get( 0 ).getValueType() ) );
		}
		return rv;
	}
	//	@Override
	//	protected org.lgna.project.ast.InstanceCreation createValue( org.lgna.project.ast.Expression[] expressions ) {
	//		org.alice.ide.typemanager.TypeManager.getNamedUserTypeFor( ancestorType, argumentField );
	//		return null;
	//	}
	//	@Override
	//	public org.lgna.project.ast.InstanceCreation getTransientValue( org.lgna.croquet.cascade.ItemNode< ? super org.lgna.project.ast.InstanceCreation, org.lgna.project.ast.Expression > step ) {
	//		return null;
	//	}
	//	
	//	@Override
	//	protected java.util.List< org.lgna.croquet.CascadeBlankChild > updateBlankChildren( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, org.lgna.croquet.cascade.BlankNode< org.lgna.project.ast.Expression > blankNode ) {
	//		java.util.List< org.lgna.project.ast.JavaType > topLevelTypes = org.alice.ide.IDE.getActiveInstance().getApiConfigurationManager().getTopLevelGalleryTypes();
	//		for( org.lgna.project.ast.AbstractType< ?,?,? > type : topLevelTypes ) {
	////			if( type instanceof org.lgna.project.ast.JavaType ) {
	////				org.lgna.project.ast.JavaType javaType = (org.lgna.project.ast.JavaType)type;
	////				type = org.alice.ide.typemanager.TypeManager.getNamedUserTypeFor( javaType );
	////			}
	//			rv.add( GalleryResourceMenu.getInstance( type.getDeclaredConstructors().get( 0 ).getRequiredParameters().get( 0 ).getValueType() ) );
	//		}
	//		return rv;
	//	}
}

//public class ChangeResourceMenuModel extends org.alice.ide.croquet.models.cascade.ExpressionFillInWithBlanks< org.lgna.project.ast.InstanceCreation, org.lgna.project.ast.Expression > {
//	private static class SingletonHolder {
//		private static ChangeResourceMenuModel instance = new ChangeResourceMenuModel();
//	}
//	public static ChangeResourceMenuModel getInstance() {
//		return SingletonHolder.instance;
//	}
//	private ChangeResourceMenuModel() {
//		super( java.util.UUID.fromString( "90b990bf-8303-414d-bd40-67b6f68cf3ad" ), org.lgna.project.ast.Expression.class );
//		this.addBlank( GalleryRootBlank.getInstance() );
//	}
//	@Override
//	protected org.lgna.project.ast.InstanceCreation createValue( org.lgna.project.ast.Expression[] expressions ) {
//		org.alice.ide.typemanager.TypeManager.getNamedUserTypeFor( ancestorType, argumentField );
//		return null;
//	}
//	@Override
//	public org.lgna.project.ast.InstanceCreation getTransientValue( org.lgna.croquet.cascade.ItemNode< ? super org.lgna.project.ast.InstanceCreation, org.lgna.project.ast.Expression > step ) {
//		return null;
//	}
////	
////	@Override
////	protected java.util.List< org.lgna.croquet.CascadeBlankChild > updateBlankChildren( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, org.lgna.croquet.cascade.BlankNode< org.lgna.project.ast.Expression > blankNode ) {
////		java.util.List< org.lgna.project.ast.JavaType > topLevelTypes = org.alice.ide.IDE.getActiveInstance().getApiConfigurationManager().getTopLevelGalleryTypes();
////		for( org.lgna.project.ast.AbstractType< ?,?,? > type : topLevelTypes ) {
//////			if( type instanceof org.lgna.project.ast.JavaType ) {
//////				org.lgna.project.ast.JavaType javaType = (org.lgna.project.ast.JavaType)type;
//////				type = org.alice.ide.typemanager.TypeManager.getNamedUserTypeFor( javaType );
//////			}
////			rv.add( GalleryResourceMenu.getInstance( type.getDeclaredConstructors().get( 0 ).getRequiredParameters().get( 0 ).getValueType() ) );
////		}
////		return rv;
////	}
//}
