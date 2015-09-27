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
package org.alice.stageide.gallerybrowser.uri;

/**
 * @author Dennis Cosgrove
 */
public class ClassResourceKeyUriIteratingOperation extends ResourceKeyUriIteratingOperation {
	private static class SingletonHolder {
		private static ClassResourceKeyUriIteratingOperation instance = new ClassResourceKeyUriIteratingOperation();
	}

	/*package-private*/static ClassResourceKeyUriIteratingOperation getInstance() {
		return SingletonHolder.instance;
	}

	private ClassResourceKeyUriIteratingOperation() {
		super( java.util.UUID.fromString( "e0b90c6a-46be-4b26-ae19-43314f6271b8" ) );
	}

	@Override
	protected int getStepCount() {
		return 3;
	}

	@Override
	protected org.lgna.croquet.Model getNext( org.lgna.croquet.history.CompletionStep<?> step, java.util.List<org.lgna.croquet.history.Step<?>> subSteps, Object iteratingData ) {
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
	}
}
