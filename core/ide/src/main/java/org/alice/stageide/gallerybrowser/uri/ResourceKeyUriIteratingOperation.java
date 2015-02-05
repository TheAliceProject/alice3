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
public abstract class ResourceKeyUriIteratingOperation extends org.lgna.croquet.SingleThreadIteratingOperation {
	public static ResourceKeyUriIteratingOperation getInstance( org.alice.stageide.modelresource.ResourceKey resourceKey, Class<?> thingCls, java.net.URI uri ) {
		ResourceKeyUriIteratingOperation rv;
		if( resourceKey != null ) {
			if( resourceKey instanceof org.alice.stageide.modelresource.ClassResourceKey ) {
				//				org.alice.stageide.modelresource.ClassResourceKey classResourceKey = (org.alice.stageide.modelresource.ClassResourceKey)resourceKey;
				rv = ClassResourceKeyUriIteratingOperation.getInstance();
			} else if( resourceKey instanceof org.alice.stageide.modelresource.EnumConstantResourceKey ) {
				rv = EnumConstantResourceKeyUriIteratingOperation.getInstance();
			} else if( resourceKey instanceof org.alice.stageide.modelresource.PersonResourceKey ) {
				rv = PersonResourceKeyUriIteratingOperation.getInstance();
			} else {
				rv = null;
			}
		} else {
			rv = ThingClsUriIteratingOperation.getInstance();
		}
		if( rv != null ) {
			rv.setResourceKeyThingClsAndUri( resourceKey, thingCls, uri );
		}
		return rv;
	}

	public ResourceKeyUriIteratingOperation( java.util.UUID migrationId ) {
		super( org.lgna.croquet.Application.PROJECT_GROUP, migrationId );
	}

	public org.alice.stageide.modelresource.ResourceKey getResourceKey() {
		return this.resourceKey;
	}

	public Class<?> getThingCls() {
		return this.thingCls;
	}

	public java.net.URI getUri() {
		return this.uri;
	}

	private void setResourceKeyThingClsAndUri( org.alice.stageide.modelresource.ResourceKey resourceKey, Class<?> thingCls, java.net.URI uri ) {
		this.resourceKey = resourceKey;
		this.thingCls = thingCls;
		this.uri = uri;
	}

	protected abstract int getStepCount();

	@Override
	protected boolean hasNext( org.lgna.croquet.history.CompletionStep<?> step, java.util.List<org.lgna.croquet.history.Step<?>> subSteps, Object iteratingData ) {
		return subSteps.size() < this.getStepCount();
	}

	protected org.lgna.croquet.Operation getAddResourceKeyManagedFieldCompositeOperation( org.alice.stageide.modelresource.EnumConstantResourceKey enumConstantResourceKey ) {
		org.alice.stageide.ast.declaration.AddResourceKeyManagedFieldComposite addResourceKeyManagedFieldComposite = org.alice.stageide.ast.declaration.AddResourceKeyManagedFieldComposite.getInstance();
		addResourceKeyManagedFieldComposite.setResourceKeyToBeUsedByGetInitializerInitialValue( enumConstantResourceKey, false );
		return addResourceKeyManagedFieldComposite.getLaunchOperation();
	}

	protected org.lgna.croquet.Operation getMergeTypeOperation() {
		edu.cmu.cs.dennisc.pattern.Tuple2<org.lgna.project.ast.NamedUserType, java.util.Set<org.lgna.common.Resource>> tuple;
		try {
			tuple = org.lgna.project.io.IoUtilities.readType( new java.io.File( this.uri ) );
		} catch( java.io.IOException ioe ) {
			tuple = null;
			edu.cmu.cs.dennisc.java.util.logging.Logger.throwable( ioe, this.uri );
		} catch( org.lgna.project.VersionNotSupportedException vnse ) {
			tuple = null;
			edu.cmu.cs.dennisc.java.util.logging.Logger.throwable( vnse, this.uri );
		}
		if( tuple != null ) {
			org.lgna.project.ast.NamedUserType importedRootType = tuple.getA();
			java.util.Set<org.lgna.common.Resource> importedResources = tuple.getB();
			org.lgna.project.ast.NamedUserType srcType = importedRootType;
			org.lgna.project.ast.NamedUserType dstType = org.alice.ide.ast.type.merge.core.MergeUtilities.findMatchingTypeInExistingTypes( srcType );
			org.alice.ide.ast.type.croquet.ImportTypeWizard wizard = new org.alice.ide.ast.type.croquet.ImportTypeWizard( this.uri, importedRootType, importedResources, srcType, dstType );
			return wizard.getLaunchOperation();
		} else {
			return null;
		}
	}

	@Override
	protected abstract org.lgna.croquet.Model getNext( org.lgna.croquet.history.CompletionStep<?> step, java.util.List<org.lgna.croquet.history.Step<?>> subSteps, Object iteratingData );

	@Override
	protected void handleSuccessfulCompletionOfSubModels( org.lgna.croquet.history.CompletionStep<?> step, java.util.List<org.lgna.croquet.history.Step<?>> subSteps ) {
		step.finish();
		this.resourceKey = null;
	}

	protected org.alice.stageide.modelresource.ResourceKey resourceKey;
	protected Class<?> thingCls;
	private java.net.URI uri;
}
