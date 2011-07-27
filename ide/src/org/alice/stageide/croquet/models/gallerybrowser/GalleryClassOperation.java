/**
 * Copyright (c) 2006-2011, Carnegie Mellon University. All rights reserved.
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
package org.alice.stageide.croquet.models.gallerybrowser;

import org.alice.ide.IDE;
import org.alice.stageide.sceneeditor.SetUpMethodGenerator;
import org.lgna.croquet.history.InputDialogOperationStep;
import org.lookingglassandalice.storytelling.Entity;
import org.lookingglassandalice.storytelling.ImplementationAccessor;
import org.lookingglassandalice.storytelling.implementation.EntityImplementation;
import org.lookingglassandalice.storytelling.implementation.TransformableImplementation;
import org.lookingglassandalice.storytelling.resources.ModelResource;
import org.lookingglassandalice.storytelling.resourceutilities.ModelResourceTreeNode;
import org.lookingglassandalice.storytelling.resourceutilities.ModelResourceUtilities;

import edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.scenegraph.AbstractTransformable;

/**
 * @author dculyba
 *
 */
public class GalleryClassOperation extends AbstractGalleryDeclareFieldOperation {
	private edu.cmu.cs.dennisc.math.AffineMatrix4x4 desiredTransformation = null;
	
	private static java.util.Map<edu.cmu.cs.dennisc.javax.swing.models.TreeNode<TypeDeclaredInAlice>, GalleryClassOperation> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static GalleryClassOperation getInstance( edu.cmu.cs.dennisc.javax.swing.models.TreeNode<TypeDeclaredInAlice> treeNode ) {
		GalleryClassOperation rv = map.get( treeNode );
		if( rv != null ) {
			//pass
		} else {
			rv = new GalleryClassOperation( treeNode );
			map.put( treeNode, rv );
		}
		return rv;
	}

	private edu.cmu.cs.dennisc.javax.swing.models.TreeNode<TypeDeclaredInAlice> treeNode;
	private GalleryClassOperation(edu.cmu.cs.dennisc.javax.swing.models.TreeNode<TypeDeclaredInAlice> treeNode) {
		super( java.util.UUID.fromString( "98886117-99d3-4c9a-b08d-78a2c95acb2d" ) );
		this.treeNode = treeNode;
	}
	@Override
	protected org.alice.ide.declarationpanes.CreateFieldFromGalleryPane prologue( org.lgna.croquet.history.InputDialogOperationStep step ) {
		return new org.alice.ide.declarationpanes.CreateFieldFromGalleryPane(this.getDeclaringType(), this.treeNode.getValue());
	}
	
	public void setDesiredTransformation( edu.cmu.cs.dennisc.math.AffineMatrix4x4 desiredTransformation )
	{
		this.desiredTransformation = desiredTransformation;
	}

	@Override
	protected EpilogueData fillInEpilogueData(EpilogueData rv, InputDialogOperationStep step) {
		super.fillInEpilogueData(rv, step);
		
		org.alice.ide.declarationpanes.CreateFieldFromGalleryPane createFieldFromGalleryPane = step.getMainPanel();
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field = createFieldFromGalleryPane.getInputValue();
		if (field != null) {
			
			rv.addDoStatement(SetUpMethodGenerator.createSetVehicleStatement(field, null, true));
			
//			ModelResource resource = ((ModelResourceTreeNode)this.treeNode).getModelResource();
//			Object fieldObject = createFieldFromGalleryPane.createInstanceInJavaForArguments(resource);
			AffineMatrix4x4 objectTransform = null;
//			if (this.desiredTransformation != null)
//			{
//				objectTransform = new edu.cmu.cs.dennisc.math.AffineMatrix4x4(this.desiredTransformation);
//				//Reset the desired transform after using it
//				this.desiredTransformation = null;
//			}
//			else
//			{
//				if (this.treeNode instanceof ModelResourceTreeNode)
//				{
//					Class<?> resourceClass = ((ModelResourceTreeNode)this.treeNode).getResourceClass();
//					edu.cmu.cs.dennisc.math.AxisAlignedBox box = ModelResourceUtilities.getBoundingBox(resourceClass);
//					if (box.isNaN())
//                    {
//                        System.err.println("TODO: fix broken bounding box for "+this.treeNode.getValue());
//                    }
//					objectTransform = ((MoveAndTurnSceneEditor)(IDE.getActiveInstance().getSceneEditor())).getGoodPointOfViewInSceneForObject(box);
//					
//				}
//			}
			if (objectTransform != null)
			{
//				if (fieldObject instanceof Entity)
//				{
//					EntityImplementation impl = ImplementationAccessor.getImplementation((Entity)fieldObject);
//					if (impl.getSgComposite() instanceof AbstractTransformable)
//					{
//						AbstractTransformable trans = (AbstractTransformable)impl.getSgComposite();
//						trans.setLocalTransformation(objectTransform);
//					}
//					
//				}
//				if (fieldObject instanceof org.lookingglassandalice.storytelling.MovableTurnable)
//				{
//					 TransformableImplementation tranformable = ImplementationAccessor.getImplementation((org.lookingglassandalice.storytelling.MovableTurnable)fieldObject);
//					 tranformable.setLocalTransformation(objectTransform);
//				}
			}
		}
		
		rv.setField(field);
		return rv;
	}
	

	protected edu.cmu.cs.dennisc.pattern.Tuple2<edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, java.lang.Object> createFieldAndInstance(org.lgna.croquet.history.InputDialogOperationStep step) {
		org.alice.ide.declarationpanes.CreateFieldFromGalleryPane createFieldFromGalleryPane = step.getMainPanel();
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field = createFieldFromGalleryPane.getInputValue();
		if (field != null) {
			ModelResource resource = ((ModelResourceTreeNode)this.treeNode).getModelResource();
			Object fieldObject = createFieldFromGalleryPane.createInstanceInJavaForArguments(resource);
			AffineMatrix4x4 objectTransform = null;
			if (this.desiredTransformation != null)
			{
				objectTransform = new edu.cmu.cs.dennisc.math.AffineMatrix4x4(this.desiredTransformation);
				//Reset the desired transform after using it
				this.desiredTransformation = null;
			}
			else
			{
				if (this.treeNode instanceof ModelResourceTreeNode)
				{
					Class<?> resourceClass = ((ModelResourceTreeNode)this.treeNode).getResourceClass();
					edu.cmu.cs.dennisc.math.AxisAlignedBox box = ModelResourceUtilities.getBoundingBox(resourceClass);
					if (box.isNaN())
                    {
                        System.err.println("TODO: fix broken bounding box for "+this.treeNode.getValue());
                    }
					objectTransform = org.alice.stageide.StageIDE.getActiveInstance().getSceneEditor().getGoodPointOfViewInSceneForObject(box);
					
				}
			}
			if (objectTransform != null)
			{
				if (fieldObject instanceof Entity)
				{
					EntityImplementation impl = ImplementationAccessor.getImplementation((Entity)fieldObject);
					if (impl.getSgComposite() instanceof AbstractTransformable)
					{
						AbstractTransformable trans = (AbstractTransformable)impl.getSgComposite();
						trans.setLocalTransformation(objectTransform);
					}
					
				}
				if (fieldObject instanceof org.lookingglassandalice.storytelling.MovableTurnable)
				{
					 TransformableImplementation tranformable = ImplementationAccessor.getImplementation((org.lookingglassandalice.storytelling.MovableTurnable)fieldObject);
					 tranformable.setLocalTransformation(objectTransform);
				}
			}
			return edu.cmu.cs.dennisc.pattern.Tuple2.createInstance( field, fieldObject );
		} else {
			return null;
		}
	}
}
