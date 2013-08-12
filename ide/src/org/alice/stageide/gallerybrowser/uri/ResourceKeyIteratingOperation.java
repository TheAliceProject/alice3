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
package org.alice.stageide.gallerybrowser.uri;

/**
 * @author Dennis Cosgrove
 */
public final class ResourceKeyIteratingOperation extends org.lgna.croquet.SingleThreadIteratingOperation {
	private static class SingletonHolder {
		private static ResourceKeyIteratingOperation instance = new ResourceKeyIteratingOperation();
	}

	public static ResourceKeyIteratingOperation getInstance() {
		return SingletonHolder.instance;
	}

	private org.alice.stageide.modelresource.ResourceKey resourceKey;

	private ResourceKeyIteratingOperation() {
		super( org.alice.ide.IDE.PROJECT_GROUP, java.util.UUID.fromString( "0f54aa06-c973-47dd-8a6f-3f608a075853" ) );
	}

	public org.alice.stageide.modelresource.ResourceKey getResourceKey() {
		return this.resourceKey;
	}

	public void setResourceKey( org.alice.stageide.modelresource.ResourceKey resourceKey ) {
		this.resourceKey = resourceKey;
	}

	private int getStepCount() {
		if( this.resourceKey instanceof org.alice.stageide.modelresource.ClassResourceKey ) {
			return 3;
		} else if( this.resourceKey instanceof org.alice.stageide.modelresource.EnumConstantResourceKey ) {
			return 2;
		} else {
			return 0;
		}
	}

	@Override
	protected boolean hasNext( org.lgna.croquet.history.CompletionStep<?> step, java.util.List<org.lgna.croquet.history.Step<?>> subSteps, java.lang.Object iteratingData ) {
		return subSteps.size() < this.getStepCount();
	}

	private org.lgna.croquet.Operation getAddResourceKeyManagedFieldCompositeOperation( org.alice.stageide.modelresource.EnumConstantResourceKey enumConstantResourceKey ) {
		org.alice.stageide.ast.declaration.AddResourceKeyManagedFieldComposite addResourceKeyManagedFieldComposite = org.alice.stageide.ast.declaration.AddResourceKeyManagedFieldComposite.getInstance();
		addResourceKeyManagedFieldComposite.setResourceKeyToBeUsedByGetInitializerInitialValue( enumConstantResourceKey, false );
		return addResourceKeyManagedFieldComposite.getOperation();
	}

	private org.lgna.croquet.Operation getMergeTypeOperation() {
		org.alice.stageide.gallerybrowser.uri.merge.MergeTypeComposite mergeTypeComposite = new org.alice.stageide.gallerybrowser.uri.merge.MergeTypeComposite();
		return mergeTypeComposite.getOperation();
	}

	@Override
	protected org.lgna.croquet.Model getNext( org.lgna.croquet.history.CompletionStep<?> step, java.util.List<org.lgna.croquet.history.Step<?>> subSteps, java.lang.Object iteratingData ) {
		if( this.resourceKey instanceof org.alice.stageide.modelresource.ClassResourceKey ) {
			org.alice.stageide.modelresource.ClassResourceKey classResourceKey = (org.alice.stageide.modelresource.ClassResourceKey)this.resourceKey;
			switch( subSteps.size() ) {
			case 0:
				org.alice.stageide.gallerybrowser.enumconstant.EnumConstantResourceKeySelectionComposite composite = org.alice.stageide.gallerybrowser.enumconstant.EnumConstantResourceKeySelectionComposite.getInstance();
				composite.setClassResourceKey( classResourceKey );
				return composite.getValueCreator();
			case 1:
				org.lgna.croquet.history.Step<?> prevSubStep = subSteps.get( 0 );
				if( prevSubStep.containsEphemeralDataFor( org.lgna.croquet.ValueCreator.VALUE_KEY ) ) {
					org.alice.stageide.modelresource.EnumConstantResourceKey enumConstantResourceKey = (org.alice.stageide.modelresource.EnumConstantResourceKey)prevSubStep.getEphemeralDataFor( org.lgna.croquet.ValueCreator.VALUE_KEY );
					return this.getAddResourceKeyManagedFieldCompositeOperation( enumConstantResourceKey );
				} else {
					return null;
				}
			case 2:
				return this.getMergeTypeOperation();
			default:
				return null;
			}
		} else if( this.resourceKey instanceof org.alice.stageide.modelresource.EnumConstantResourceKey ) {
			org.alice.stageide.modelresource.EnumConstantResourceKey enumConstantResourceKey = (org.alice.stageide.modelresource.EnumConstantResourceKey)this.resourceKey;
			switch( subSteps.size() ) {
			case 0:
				return this.getAddResourceKeyManagedFieldCompositeOperation( enumConstantResourceKey );
			case 1:
				return this.getMergeTypeOperation();
			default:
				return null;
			}
		} else {
			return null;
		}
	}

	@Override
	protected void handleSuccessfulCompletionOfSubModels( org.lgna.croquet.history.CompletionStep<?> step, java.util.List<org.lgna.croquet.history.Step<?>> subSteps ) {
		step.finish();
		this.resourceKey = null;
	}

}
