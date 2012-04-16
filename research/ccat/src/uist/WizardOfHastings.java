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
package uist;

/**
 * @author Dennis Cosgrove
 */
class WizardOfHastings {
	public static void castPart( org.lgna.croquet.Retargeter retargeter, org.lgna.project.Project originalProject, String originalFieldName, org.lgna.project.Project replacementProject, String replacementFieldName ) {
		org.lgna.project.ast.AbstractType<?,?,?> orginalSceneType = originalProject.getProgramType().getDeclaredFields().get( 0 ).getValueType();		
		org.lgna.project.ast.AbstractField originalField = orginalSceneType.getDeclaredField( originalFieldName );
		org.lgna.project.ast.AbstractType<?,?,?> replacementSceneType = replacementProject.getProgramType().getDeclaredFields().get( 0 ).getValueType();
		org.lgna.project.ast.AbstractField replacementField = replacementSceneType.getDeclaredField( replacementFieldName );
		
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "original:", originalField.getName(), originalField.getValueType().getName(), originalField.getValueType().getId() );
		edu.cmu.cs.dennisc.print.PrintUtilities.println( "replacement:", replacementField.getName(), replacementField.getValueType().getName(), replacementField.getValueType().getId() );
		retargeter.addKeyValuePair( originalField, replacementField );
		retargeter.addKeyValuePair( originalField.getValueType(), replacementField.getValueType() );
	}
	public static void castType( org.lgna.croquet.Retargeter retargeter, org.lgna.project.Project originalProject, String originalTypeName, org.lgna.project.Project replacementProject, String replacementTypeName ) {
		for( org.lgna.project.ast.NamedUserType originalType : originalProject.getNamedUserTypes() ) {
			if( originalType.getName().equals( originalTypeName ) ) {
				for( org.lgna.project.ast.NamedUserType replacementType : replacementProject.getNamedUserTypes() ) {
					if( replacementType.getName().equals( replacementTypeName ) ) {
						retargeter.addKeyValuePair( originalType, replacementType );
						return;
					}
				}
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe( originalTypeName, replacementTypeName );
			}
		}
		edu.cmu.cs.dennisc.java.util.logging.Logger.severe( originalTypeName );
	}
}
