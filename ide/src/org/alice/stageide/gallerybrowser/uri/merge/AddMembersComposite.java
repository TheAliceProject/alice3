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
package org.alice.stageide.gallerybrowser.uri.merge;

import org.alice.stageide.gallerybrowser.uri.merge.data.DifferentImplementationDeclartions;
import org.alice.stageide.gallerybrowser.uri.merge.data.DifferentSignatureDeclarations;
import org.alice.stageide.gallerybrowser.uri.merge.data.IdenticalDeclarations;
import org.alice.stageide.gallerybrowser.uri.merge.data.ImportOnlyDeclaration;
import org.alice.stageide.gallerybrowser.uri.merge.data.ProjectOnlyDeclaration;

/**
 * @author Dennis Cosgrove
 */
public abstract class AddMembersComposite<V extends org.alice.stageide.gallerybrowser.uri.merge.views.AddMembersView, D extends org.lgna.project.ast.Declaration> extends org.lgna.croquet.ToolPaletteCoreComposite<V> {
	private final java.util.List<D> unusedProjectDeclarations;
	private final java.util.List<ImportOnlyDeclaration<D>> importOnlyDeclarations = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	private final java.util.List<DifferentSignatureDeclarations<D>> differentSignatureDeclarations = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	private final java.util.List<DifferentImplementationDeclartions<D>> differentImplementationDeclarations = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	private final java.util.List<IdenticalDeclarations<D>> identicalDeclarations = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	private java.util.List<ProjectOnlyDeclaration<D>> projectOnlyDeclarations;

	public AddMembersComposite( java.util.UUID migrationId, java.util.List<D> projectDeclarations ) {
		super( migrationId, org.lgna.croquet.Application.INHERIT_GROUP, true );
		this.unusedProjectDeclarations = projectDeclarations;
	}

	@Override
	protected org.lgna.croquet.components.ScrollPane createScrollPaneIfDesired() {
		return new org.lgna.croquet.components.ScrollPane();
	}

	public void addImportOnlyDeclaration( D method ) {
		this.importOnlyDeclarations.add( new ImportOnlyDeclaration( method ) );
	}

	public void addDifferentSignatureDeclarations( D projectDeclaration, D importDeclaration ) {
		this.differentSignatureDeclarations.add( new DifferentSignatureDeclarations( projectDeclaration, importDeclaration ) );
		this.unusedProjectDeclarations.remove( projectDeclaration );
	}

	public void addDifferentImplementationDeclarations( D projectDeclaration, D importDeclaration ) {
		this.differentImplementationDeclarations.add( new DifferentImplementationDeclartions( projectDeclaration, importDeclaration ) );
		this.unusedProjectDeclarations.remove( projectDeclaration );
	}

	public void addIdenticalDeclarations( D projectDeclaration, D importDeclaration ) {
		this.identicalDeclarations.add( new IdenticalDeclarations( projectDeclaration, importDeclaration ) );
		this.unusedProjectDeclarations.remove( projectDeclaration );
	}

	public void reifyProjectOnlyDeclarations() {
		this.projectOnlyDeclarations = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		for( D method : this.unusedProjectDeclarations ) {
			this.projectOnlyDeclarations.add( new ProjectOnlyDeclaration( method ) );
		}
	}

	public java.util.List<ImportOnlyDeclaration<D>> getImportOnlyDeclarations() {
		return this.importOnlyDeclarations;
	}

	public java.util.List<DifferentSignatureDeclarations<D>> getDifferentSignatureDeclarations() {
		return this.differentSignatureDeclarations;
	}

	public java.util.List<DifferentImplementationDeclartions<D>> getDifferentImplementationDeclarations() {
		return this.differentImplementationDeclarations;
	}

	public java.util.List<IdenticalDeclarations<D>> getIdenticalDeclarations() {
		return this.identicalDeclarations;
	}

	public java.util.List<ProjectOnlyDeclaration<D>> getProjectOnlyDeclarations() {
		return this.projectOnlyDeclarations;
	}

	public int getTotalCount() {
		assert this.importOnlyDeclarations != null : this;
		assert this.differentSignatureDeclarations != null : this;
		assert this.differentImplementationDeclarations != null : this;
		assert this.identicalDeclarations != null : this;
		assert this.projectOnlyDeclarations != null : this;
		return this.importOnlyDeclarations.size() + this.differentSignatureDeclarations.size() + this.differentImplementationDeclarations.size() + this.identicalDeclarations.size() + this.projectOnlyDeclarations.size();
	}
}
