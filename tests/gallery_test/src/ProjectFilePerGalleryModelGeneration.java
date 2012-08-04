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
 * @author dennisc
 */
public class ProjectFilePerGalleryModelGeneration {
	private static void test( java.util.List<Throwable> brokenModels,  org.alice.ide.croquet.models.gallerybrowser.GalleryNode node, Class<? extends org.lgna.story.SJointedModel> instanceCls, Class<?>... parameterClses ) throws IllegalAccessException, java.io.IOException {
		if( node instanceof org.alice.ide.croquet.models.gallerybrowser.FieldGalleryNode ) {
			org.alice.ide.croquet.models.gallerybrowser.FieldGalleryNode fieldGalleryNode = (org.alice.ide.croquet.models.gallerybrowser.FieldGalleryNode)node;
			org.lgna.project.ast.JavaField argumentField = (org.lgna.project.ast.JavaField)fieldGalleryNode.getDeclaration();

			org.lgna.project.ast.AbstractType< ?, ?, ? > valueType = argumentField.getValueType();
			org.lgna.project.ast.AbstractConstructor bogusConstructor = org.alice.ide.croquet.models.gallerybrowser.RootGalleryNode.getInstance().getConstructorForArgumentType( valueType );
			org.lgna.project.ast.NamedUserType namedUserType = org.alice.ide.typemanager.TypeManager.getNamedUserTypeFromArgumentField( bogusConstructor.getDeclaringType().getFirstEncounteredJavaType(), (org.lgna.project.ast.JavaField)argumentField );
			org.lgna.project.ast.AbstractConstructor constructor = namedUserType.constructors.get( 0 );
			org.lgna.project.ast.InstanceCreation instanceCreation = org.lgna.project.ast.AstUtilities.createInstanceCreation( constructor, org.lgna.project.ast.AstUtilities.createStaticFieldAccess( argumentField ) );
			
			org.lgna.project.ast.UserField field = new org.lgna.project.ast.UserField( argumentField.getName(), namedUserType, instanceCreation );
			field.managementLevel.setValue( org.lgna.project.ast.ManagementLevel.MANAGED );
			field.finalVolatileOrNeither.setValue( org.lgna.project.ast.FieldModifierFinalVolatileOrNeither.FINAL );
			
			org.alice.stageide.openprojectpane.models.TemplateUriSelectionState.Template template = org.alice.stageide.openprojectpane.models.TemplateUriSelectionState.Template.GRASS;
			org.lgna.project.ast.NamedUserType programType = org.alice.stageide.ast.BootstrapUtilties.createProgramType( template.getSurfaceAppearance(), template.getAtmospherColor(), template.getFogDensity(), template.getAboveLightColor(), template.getBelowLightColor() );
			org.lgna.project.ast.NamedUserType sceneType = (org.lgna.project.ast.NamedUserType)programType.fields.get( 0 ).getValueType();
			sceneType.fields.add( field );

			org.lgna.project.ast.UserMethod performGeneratedSetupMethod = (org.lgna.project.ast.UserMethod)sceneType.findMethod( org.alice.stageide.StageIDE.PERFORM_GENERATED_SET_UP_METHOD_NAME );

			org.lgna.project.ast.JavaMethod setVehicleMethod = org.lgna.project.ast.JavaMethod.getInstance( org.lgna.story.SModel.class, "setVehicle", org.lgna.story.SThing.class );
			
			performGeneratedSetupMethod.body.getValue().statements.add( 
					org.lgna.project.ast.AstUtilities.createMethodInvocationStatement(  
							new org.lgna.project.ast.FieldAccess(new org.lgna.project.ast.ThisExpression(), field), 
							setVehicleMethod, 
							new org.lgna.project.ast.ThisExpression()
					) 
			);
			
			org.lgna.project.Project project = new org.lgna.project.Project( programType );
			
			
			String path = edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory() + "/GalleryTest/" + org.lgna.project.Version.getCurrentVersionText() + "/" + valueType.getName() + "/" + argumentField.getName() + ".a3p";
			
			org.lgna.project.io.IoUtilities.writeProject( path, project );
			
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln( path );
		}
		final int N = node.getChildCount();
		for( int i=0; i<N; i++ ) {
			test( brokenModels, node.getChild( i ), instanceCls, parameterClses );
		}
	}
	public static void main( String[] args ) throws Exception {
		edu.cmu.cs.dennisc.java.util.logging.Logger.setLevel( java.util.logging.Level.INFO );
		org.alice.stageide.StageIDE usedOnlyForSideEffect = new org.alice.stageide.StageIDE();
		org.alice.ide.croquet.models.gallerybrowser.RootGalleryNode rootGalleryNode = org.alice.ide.croquet.models.gallerybrowser.RootGalleryNode.getInstance();
		
		java.util.List< Throwable > brokenModels = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		
		int i = 0;
		test( brokenModels, rootGalleryNode.getChild( i++ ), org.lgna.story.SBiped.class, org.lgna.story.resources.BipedResource.class );
		test( brokenModels, rootGalleryNode.getChild( i++ ), org.lgna.story.SFlyer.class, org.lgna.story.resources.FlyerResource.class );
		test( brokenModels, rootGalleryNode.getChild( i++ ), org.lgna.story.SProp.class, org.lgna.story.resources.PropResource.class );
		test( brokenModels, rootGalleryNode.getChild( i++ ), org.lgna.story.SQuadruped.class, org.lgna.story.resources.QuadrupedResource.class );
		test( brokenModels, rootGalleryNode.getChild( i++ ), org.lgna.story.SSwimmer.class, org.lgna.story.resources.SwimmerResource.class );
		
		if( brokenModels.size() > 0 ) {
//			System.err.println();
//			System.err.println();
//			System.err.println();
//			for( Throwable t : brokenModels ) {
//				t.printStackTrace();
//			}
			javax.swing.JOptionPane.showMessageDialog( null, brokenModels.size() + " broken models." );
		}
		
	}
}
