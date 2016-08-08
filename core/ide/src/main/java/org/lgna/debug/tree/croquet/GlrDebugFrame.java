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
package org.lgna.debug.tree.croquet;

/**
 * @author Dennis Cosgrove
 */
public class GlrDebugFrame extends DebugFrame<edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrComponent<?>> {
	public static org.lgna.debug.tree.core.ZTreeNode.Builder<edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrComponent<?>> createBuilder( edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrComponent<?> glrComponent ) {
		org.lgna.debug.tree.core.ZTreeNode.Builder<edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrComponent<?>> rv = new org.lgna.debug.tree.core.ZTreeNode.Builder<edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrComponent<?>>( glrComponent, glrComponent instanceof edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrLeaf<?> );
		if( glrComponent instanceof edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrComposite<?> ) {
			edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrComposite<?> glrComposite = (edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrComposite<?>)glrComponent;
			for( edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrComponent<?> glrChild : glrComposite.accessChildren() ) {
				rv.addChildBuilder( createBuilder( glrChild ) );
			}
		}
		return rv;
	}

	public GlrDebugFrame() {
		super( java.util.UUID.fromString( "502e2f7e-cd4a-4ae5-a06d-20890d8e1eb6" ) );
	}

	@Override
	protected org.lgna.debug.tree.core.ZTreeNode.Builder<edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrComponent<?>> capture() {
		org.lgna.project.virtualmachine.UserInstance sceneUserInstance = org.alice.stageide.sceneeditor.StorytellingSceneEditor.getInstance().getActiveSceneInstance();
		org.lgna.story.SScene scene = sceneUserInstance.getJavaInstance( org.lgna.story.SScene.class );
		org.lgna.story.implementation.SceneImp sceneImp = org.lgna.story.EmployeesOnly.getImplementation( scene );
		edu.cmu.cs.dennisc.scenegraph.Scene sgScene = sceneImp.getSgComposite();
		edu.cmu.cs.dennisc.render.gl.imp.adapters.GlrScene glrScene = edu.cmu.cs.dennisc.render.gl.imp.adapters.AdapterFactory.getAdapterFor( sgScene );
		return createBuilder( glrScene );
	}
}
