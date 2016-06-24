import org.alice.stageide.modelresource.ClassResourceKey;
import org.alice.stageide.modelresource.ResourceKey;

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

/**
 * @author Dennis Cosgrove
 */
public class GenerateJointIdProject {
	private static void addAllJointIdFields( java.util.List<java.lang.reflect.Field> fields, org.alice.stageide.modelresource.ResourceNode node ) {
		for( org.alice.stageide.modelresource.ResourceNode child : node.getNodeChildren() ) {
			ResourceKey key = child.getResourceKey();
			if( key instanceof ClassResourceKey ) {
				ClassResourceKey classKey = (ClassResourceKey)key;
				Class<? extends org.lgna.story.resources.ModelResource> modelResourceCls = classKey.getModelResourceCls();
				for( java.lang.reflect.Field jField : edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getPublicFinalDeclaredFields( modelResourceCls, org.lgna.story.resources.JointId.class ) ) {
					String name = jField.getName();
					for( char c : name.toCharArray() ) {
						if( Character.isLowerCase( c ) ) {
							edu.cmu.cs.dennisc.java.util.logging.Logger.errln( modelResourceCls.getName(), name );
							break;
						}
					}
					fields.add( jField );
				}
			}
			addAllJointIdFields( fields, child );
		}
	}

	public static void main( String[] args ) throws Exception {
		edu.cmu.cs.dennisc.java.util.logging.Logger.setLevel( java.util.logging.Level.INFO );
		org.alice.stageide.StageIDE usedOnlyForSideEffect = new org.alice.ide.story.AliceIde( null );
		org.alice.stageide.modelresource.ResourceNode rootGalleryNode = org.alice.stageide.modelresource.TreeUtilities.getTreeBasedOnClassHierarchy();

		java.util.List<java.lang.reflect.Field> jFields = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
		addAllJointIdFields( jFields, rootGalleryNode );

		org.alice.stageide.openprojectpane.models.TemplateUriState.Template template = org.alice.stageide.openprojectpane.models.TemplateUriState.Template.GRASS;
		org.lgna.project.ast.NamedUserType programType = org.alice.stageide.ast.BootstrapUtilties.createProgramType( template.getSurfaceAppearance(), template.getAtmospherColor(), template.getFogDensity(), template.getAboveLightColor(), template.getBelowLightColor() );

		org.lgna.project.ast.UserMethod mainMethod = programType.getDeclaredMethod( "main", String[].class );

		org.lgna.project.ast.BlockStatement body = mainMethod.body.getValue();
		for( java.lang.reflect.Field jField : jFields ) {
			body.statements.add( new org.lgna.project.ast.ExpressionStatement(
					org.lgna.project.ast.AstUtilities.createStaticFieldAccess( jField ) ) );
		}

		org.lgna.project.Project project = new org.lgna.project.Project( programType );
		String path = edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory() + "/JointIdTest/" + org.lgna.project.ProjectVersion.getCurrentVersionText() + ".a3p";

		org.lgna.project.io.IoUtilities.writeProject( path, project );
	}
}
