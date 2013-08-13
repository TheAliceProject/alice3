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
package org.alice.stageide.gallerybrowser.uri.merge;

/**
 * @author Dennis Cosgrove
 */
public class MergeUtilities {
	private MergeUtilities() {
		throw new AssertionError();
	}

	public static org.lgna.project.ast.NamedUserType findMatchingTypeInExistingTypes( org.lgna.project.ast.NamedUserType type, java.util.Collection<org.lgna.project.ast.NamedUserType> existingTypes ) {
		for( org.lgna.project.ast.NamedUserType existingType : existingTypes ) {
			//todo
			if( existingType.getName().contentEquals( type.getName() ) ) {
				return existingType;
			}
		}
		return null;
	}

	public static org.lgna.project.ast.NamedUserType findMatchingTypeInExistingTypes( org.lgna.project.ast.NamedUserType type ) {
		org.lgna.project.Project project = org.alice.ide.ProjectStack.peekProject();
		if( project != null ) {
			java.util.Set<org.lgna.project.ast.NamedUserType> existingTypes = project.getNamedUserTypes();
			return findMatchingTypeInExistingTypes( type, existingTypes );
		}
		return null;
	}

	public static org.lgna.project.ast.UserMethod findMethodWithMatchingName( org.lgna.project.ast.UserMethod importedMethod, org.lgna.project.ast.NamedUserType existingType ) {
		for( org.lgna.project.ast.UserMethod existingMethod : existingType.methods ) {
			if( importedMethod.getName().contentEquals( existingMethod.getName() ) ) {
				return existingMethod;
			}
		}
		return null;
	}

	public static boolean isHeaderEquivalent( org.lgna.project.ast.UserMethod a, org.lgna.project.ast.UserMethod b ) {
		boolean isLambdaSupported = true; //don't care
		return a.generateHeaderJavaCode( isLambdaSupported ).contentEquals( b.generateHeaderJavaCode( isLambdaSupported ) );
	}

	public static boolean isEquivalent( org.lgna.project.ast.UserMethod a, org.lgna.project.ast.UserMethod b ) {
		boolean isLambdaSupported = true; //don't care
		return a.generateJavaCode( isLambdaSupported ).contentEquals( b.generateJavaCode( isLambdaSupported ) );
	}

	public static boolean isValueTypeEquivalent( org.lgna.project.ast.UserField a, org.lgna.project.ast.UserField b ) {
		return a.getValueType().getName().contentEquals( b.getValueType().getName() ); //todo
	}

	public static boolean isEquivalent( org.lgna.project.ast.UserField a, org.lgna.project.ast.UserField b ) {
		boolean isLambdaSupported = true; //don't care
		return a.generateJavaCode( isLambdaSupported ).contentEquals( b.generateJavaCode( isLambdaSupported ) );
	}
}
