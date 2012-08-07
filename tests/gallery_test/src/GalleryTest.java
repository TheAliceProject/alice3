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
public class GalleryTest {
	private static void test( java.util.List<Throwable> brokenModels,  org.alice.ide.croquet.models.gallerybrowser.GalleryNode node, Class<? extends org.lgna.story.SJointedModel> instanceCls, Class<?>... parameterClses ) throws IllegalAccessException {
		if( node instanceof org.alice.ide.croquet.models.gallerybrowser.FieldGalleryNode ) {
			org.alice.ide.croquet.models.gallerybrowser.FieldGalleryNode fieldGalleryNode = (org.alice.ide.croquet.models.gallerybrowser.FieldGalleryNode)node;
			org.lgna.project.ast.JavaField field = (org.lgna.project.ast.JavaField)fieldGalleryNode.getDeclaration();
			java.lang.reflect.Field fld = field.getFieldReflectionProxy().getReification();
			edu.cmu.cs.dennisc.java.util.logging.Logger.outln("TESTING:", field.getName(), field.getDeclaringType().getName() );
			Object resource = fld.get( null );
			assert parameterClses[ 0 ].isInstance( resource ) : parameterClses[ 0 ] + " " + resource;
			try {
				org.lgna.story.SJointedModel jointedModel = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( instanceCls, parameterClses, resource );
				jointedModel.straightenOutJoints();
			} catch( Throwable t ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.throwable( t );
				brokenModels.add( t );
			}
//			if (jointedModel instanceof org.lgna.story.Swimmer) {
//				jointedModel.straightenOutJoints();
//			}
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
