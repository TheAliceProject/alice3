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
package org.alice.stageide.gallerybrowser.uri.merge.views;

/**
 * @author Dennis Cosgrove
 */
public class DifferentImplementationCourseOfActionListener implements org.lgna.croquet.State.ValueListener<org.alice.stageide.gallerybrowser.uri.merge.DifferentImplementationCourseOfAction> {
	private final org.lgna.croquet.components.Component<?>[] replaceComponents;
	private final org.lgna.croquet.components.Component<?>[] keepComponents;
	private final org.lgna.croquet.components.Component<?>[] renameComponents;

	public DifferentImplementationCourseOfActionListener( org.lgna.croquet.components.Component<?>[] replaceComponents, org.lgna.croquet.components.Component<?>[] keepComponents, org.lgna.croquet.components.Component<?>[] renameComponents ) {
		this.replaceComponents = replaceComponents;
		this.keepComponents = keepComponents;
		this.renameComponents = renameComponents;
	}

	public void changing( org.lgna.croquet.State<org.alice.stageide.gallerybrowser.uri.merge.DifferentImplementationCourseOfAction> state, org.alice.stageide.gallerybrowser.uri.merge.DifferentImplementationCourseOfAction prevValue, org.alice.stageide.gallerybrowser.uri.merge.DifferentImplementationCourseOfAction nextValue, boolean isAdjusting ) {
	}

	public void changed( org.lgna.croquet.State<org.alice.stageide.gallerybrowser.uri.merge.DifferentImplementationCourseOfAction> state, org.alice.stageide.gallerybrowser.uri.merge.DifferentImplementationCourseOfAction prevValue, org.alice.stageide.gallerybrowser.uri.merge.DifferentImplementationCourseOfAction nextValue, boolean isAdjusting ) {
		for( org.lgna.croquet.components.Component<?> component : this.replaceComponents ) {
			synchronized( component.getTreeLock() ) {
				component.setVisible( nextValue == org.alice.stageide.gallerybrowser.uri.merge.DifferentImplementationCourseOfAction.REPLACE );
			}
		}
		for( org.lgna.croquet.components.Component<?> component : this.keepComponents ) {
			synchronized( component.getTreeLock() ) {
				component.setVisible( nextValue == org.alice.stageide.gallerybrowser.uri.merge.DifferentImplementationCourseOfAction.KEEP );
			}
		}
		for( org.lgna.croquet.components.Component<?> component : this.renameComponents ) {
			synchronized( component.getTreeLock() ) {
				component.setVisible( nextValue == org.alice.stageide.gallerybrowser.uri.merge.DifferentImplementationCourseOfAction.ADD_AND_KEEP_WITH_RENAME );
			}
		}
	}
}
