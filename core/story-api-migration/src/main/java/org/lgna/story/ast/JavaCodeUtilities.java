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
package org.lgna.story.ast;

import org.lgna.project.code.CodeOrganizer;

/**
 * @author Dennis Cosgrove
 */
public class JavaCodeUtilities {

	private static CodeOrganizer.CodeOrganizerDefinition s_defaultCodeOrganizer = new CodeOrganizer.CodeOrganizerDefinition();
	private static CodeOrganizer.CodeOrganizerDefinition s_sceneClassCodeOrganizer = new CodeOrganizer.CodeOrganizerDefinition();
	private static CodeOrganizer.CodeOrganizerDefinition s_programClassCodeOrganizer = new CodeOrganizer.CodeOrganizerDefinition();
	static {
		s_defaultCodeOrganizer.addSection( "ConstructorSection", CodeOrganizer.CONSTRUCTORS );
		s_defaultCodeOrganizer.addSection( "MethodsAndFunctionsSection", CodeOrganizer.NON_STATIC_METHODS );
		s_defaultCodeOrganizer.addSection( "GettersAndSettersSection", CodeOrganizer.GETTERS_AND_SETTERS );
		s_defaultCodeOrganizer.addSection( "FieldsSection", CodeOrganizer.FIELDS );
		s_defaultCodeOrganizer.addSection( "StaticMethodsSection", CodeOrganizer.STATIC_METHODS );

		s_sceneClassCodeOrganizer.addSection( "ConstructorSection", CodeOrganizer.CONSTRUCTORS );
		s_sceneClassCodeOrganizer.addSection( "EventListenersSection", "initializeEventListeners" );
		s_sceneClassCodeOrganizer.addSection( "MethodsAndFunctionsSection", CodeOrganizer.NON_STATIC_METHODS );
		s_sceneClassCodeOrganizer.addSection( "FieldsSection", true, CodeOrganizer.FIELDS );
		s_sceneClassCodeOrganizer.addSection( "SceneSetupSection", true, "performCustomSetup", "performGeneratedSetUp" );
		s_sceneClassCodeOrganizer.addSection( "MultipleSceneSection", true, "handleActiveChanged", CodeOrganizer.GETTERS_AND_SETTERS );
		s_sceneClassCodeOrganizer.addSection( "StaticMethodsSection", CodeOrganizer.STATIC_METHODS );

		s_programClassCodeOrganizer.addSection( "ConstructorSection", CodeOrganizer.CONSTRUCTORS );
		s_programClassCodeOrganizer.addSection( "FieldsSection", "myScene", CodeOrganizer.FIELDS );
		s_programClassCodeOrganizer.addSection( "MainFunction", "main" );
		s_programClassCodeOrganizer.addSection( "GettersAndSettersSection", CodeOrganizer.GETTERS_AND_SETTERS );
	}

	private JavaCodeUtilities() {
		throw new AssertionError();
	}

	public static org.lgna.project.ast.JavaCodeGenerator.Builder createJavaCodeGeneratorBuilder() {
		return new org.lgna.project.ast.JavaCodeGenerator.Builder()
				.isLambdaSupported( true )
				.isPublicStaticFinalFieldGetterDesired( false )
				.addCommentsLocalizationBundleName( "org.lgna.story.CodeComments" )
				.addImportOnDemandPackage( Package.getPackage( "org.lgna.story" ) )
				.addDefaultCodeOrganizerDefinition( s_defaultCodeOrganizer )
				.addCodeOrganizerDefinition( "Scene", s_sceneClassCodeOrganizer )
				.addCodeOrganizerDefinition( "Program", s_programClassCodeOrganizer )
				.addImportStaticMethod(
						edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getMethod(
								org.lgna.common.ThreadUtilities.class,
								"doTogether",
								Runnable[].class ) )
				.addImportStaticMethod(
						edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getMethod(
								org.lgna.common.ThreadUtilities.class,
								"eachInTogether",
								org.lgna.common.EachInTogetherRunnable.class,
								Object[].class ) );
	}

	public static org.lgna.project.ast.JavaCodeGenerator createJavaCodeGenerator() {
		return createJavaCodeGeneratorBuilder().build();
	}
}
