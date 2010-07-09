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
package org.alice.stageide.gallerybrowser;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author Dennis Cosgrove
 */
public class GalleryFileActionOperation extends AbstractGalleryDeclareFieldOperation<org.alice.ide.declarationpanes.CreateFieldFromGalleryPane> {
	private AffineMatrix4x4 desiredTransformation = null;
	
	private static java.util.Map<edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String>, GalleryFileActionOperation> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static GalleryFileActionOperation getInstance( edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> treeNode ) {
		GalleryFileActionOperation rv = map.get( treeNode );
		if( rv != null ) {
			//pass
		} else {
			rv = new GalleryFileActionOperation( treeNode );
			map.put( treeNode, rv );
		}
		return rv;
	}

	private edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> treeNode;
	private GalleryFileActionOperation(edu.cmu.cs.dennisc.javax.swing.models.TreeNode<String> treeNode) {
		super( java.util.UUID.fromString( "19e8291e-3b0b-48f5-8bc9-1d02b754f9d4" ) );
		this.treeNode = treeNode;
	}
	@Override
	protected org.alice.ide.declarationpanes.CreateFieldFromGalleryPane prologue( edu.cmu.cs.dennisc.croquet.InputDialogOperationContext< org.alice.ide.declarationpanes.CreateFieldFromGalleryPane > context ) {
		return new org.alice.ide.declarationpanes.CreateFieldFromGalleryPane(this.getOwnerType(), this.treeNode);
	}
	
	public void setDesiredTransformation( AffineMatrix4x4 desiredTransformation )
	{
		this.desiredTransformation = desiredTransformation;
	}

	
	//"Create New Instance"
	@Override
	protected edu.cmu.cs.dennisc.pattern.Tuple2<edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, java.lang.Object> createFieldAndInstance(edu.cmu.cs.dennisc.croquet.InputDialogOperationContext< org.alice.ide.declarationpanes.CreateFieldFromGalleryPane > context) {
		org.alice.ide.declarationpanes.CreateFieldFromGalleryPane createFieldFromGalleryPane = context.getMainPanel();
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field = createFieldFromGalleryPane.getActualInputValue();
		if (field != null) {
			Object fieldObject = createFieldFromGalleryPane.createInstanceInJava();
			if (fieldObject instanceof org.alice.apis.moveandturn.Transformable)
			{
				if (this.desiredTransformation != null)
				{
//					PrintUtilities.println("setting drop item "+ ((org.alice.apis.moveandturn.Transformable)fieldObject).getSGTransformable().hashCode()+" to "+this.desiredTransformation.translation);
					((org.alice.apis.moveandturn.Transformable)fieldObject).setLocalTransformation(new AffineMatrix4x4(this.desiredTransformation));
//					((org.alice.apis.moveandturn.Transformable)fieldObject).getSGTransformable().localTransformation.setValue();
				}
			}
			return new edu.cmu.cs.dennisc.pattern.Tuple2<edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice, Object>( field, fieldObject );
		} else {
			return null;
		}
	}
}
