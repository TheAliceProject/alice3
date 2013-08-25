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

import org.alice.stageide.gallerybrowser.uri.merge.data.DifferentImplementationMembers;
import org.alice.stageide.gallerybrowser.uri.merge.data.DifferentSignatureMembers;
import org.alice.stageide.gallerybrowser.uri.merge.data.IdenticalMembers;
import org.alice.stageide.gallerybrowser.uri.merge.data.ImportOnlyMember;
import org.alice.stageide.gallerybrowser.uri.merge.data.ProjectOnlyMember;

/**
 * @author Dennis Cosgrove
 */
public abstract class AddMembersComposite<V extends org.alice.stageide.gallerybrowser.uri.merge.views.AddMembersView, M extends org.lgna.project.ast.Member> extends org.lgna.croquet.ToolPaletteCoreComposite<V> {
	private final org.lgna.croquet.PlainStringValue addLabel = this.createStringValue( this.createKey( "addLabel" ) );
	private final org.lgna.croquet.PlainStringValue keepLabel = this.createStringValue( this.createKey( "keepLabel" ) );

	private final java.net.URI uriForDescriptionPurposesOnly;
	private final java.util.List<M> unusedProjectMembers;
	private final java.util.List<ImportOnlyMember<M>> importOnlyMembers = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	private final java.util.List<DifferentSignatureMembers<M>> differentSignatureMembers = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	private final java.util.List<DifferentImplementationMembers<M>> differentImplementationMembers = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	private final java.util.List<IdenticalMembers<M>> identicalMembers = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	private java.util.List<ProjectOnlyMember<M>> projectOnlyMembers;

	private final edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap<M, MemberPopupCoreComposite> mapMemberToPopupComposite = edu.cmu.cs.dennisc.java.util.Collections.newInitializingIfAbsentHashMap();

	public AddMembersComposite( java.util.UUID migrationId, java.net.URI uriForDescriptionPurposesOnly, java.util.List<M> projectMembers ) {
		super( migrationId, org.lgna.croquet.Application.INHERIT_GROUP, true );
		this.uriForDescriptionPurposesOnly = uriForDescriptionPurposesOnly;
		this.unusedProjectMembers = projectMembers;
	}

	@Override
	protected String modifyLocalizedText( org.lgna.croquet.Element element, String localizedText ) {
		String rv = super.modifyLocalizedText( element, localizedText );
		if( element == this.addLabel ) {
			java.io.File file = new java.io.File( this.uriForDescriptionPurposesOnly );
			rv = rv.replaceAll( "</filename/>", file.getName() );
		}
		return rv;
	}

	public MemberPopupCoreComposite getPopupMemberFor( M member ) {
		return this.mapMemberToPopupComposite.getInitializingIfAbsent( member, new edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap.Initializer<M, MemberPopupCoreComposite>() {
			public org.alice.stageide.gallerybrowser.uri.merge.MemberPopupCoreComposite initialize( M key ) {
				return new MemberPopupCoreComposite( key );
			}
		} );
	}

	@Override
	protected org.lgna.croquet.components.ScrollPane createScrollPaneIfDesired() {
		return new org.lgna.croquet.components.ScrollPane();
	}

	public void addImportOnlyMember( M method ) {
		this.importOnlyMembers.add( new ImportOnlyMember<M>( method ) );
	}

	public void addDifferentSignatureMembers( M projectMember, M importMember ) {
		this.differentSignatureMembers.add( new DifferentSignatureMembers<M>( projectMember, importMember ) );
		this.unusedProjectMembers.remove( projectMember );
	}

	public void addDifferentImplementationMembers( M projectMember, M importMember ) {
		this.differentImplementationMembers.add( new DifferentImplementationMembers<M>( projectMember, importMember ) );
		this.unusedProjectMembers.remove( projectMember );
	}

	public void addIdenticalMembers( M projectMember, M importMember ) {
		this.identicalMembers.add( new IdenticalMembers<M>( projectMember, importMember ) );
		this.unusedProjectMembers.remove( projectMember );
	}

	public void reifyProjectOnlyMembers() {
		this.projectOnlyMembers = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		for( M method : this.unusedProjectMembers ) {
			this.projectOnlyMembers.add( new ProjectOnlyMember( method ) );
		}
	}

	public java.util.List<ImportOnlyMember<M>> getImportOnlyMembers() {
		return this.importOnlyMembers;
	}

	public java.util.List<DifferentSignatureMembers<M>> getDifferentSignatureMembers() {
		return this.differentSignatureMembers;
	}

	public java.util.List<DifferentImplementationMembers<M>> getDifferentImplementationMembers() {
		return this.differentImplementationMembers;
	}

	public java.util.List<IdenticalMembers<M>> getIdenticalMembers() {
		return this.identicalMembers;
	}

	public java.util.List<ProjectOnlyMember<M>> getProjectOnlyMembers() {
		return this.projectOnlyMembers;
	}

	public org.lgna.croquet.PlainStringValue getAddLabel() {
		return this.addLabel;
	}

	public org.lgna.croquet.PlainStringValue getKeepLabel() {
		return this.keepLabel;
	}

	public int getActionItemCount() {
		return this.differentSignatureMembers.size() + this.differentImplementationMembers.size();
	}

	public int getAddCount() {
		return this.importOnlyMembers.size() + this.differentSignatureMembers.size() + this.differentImplementationMembers.size();
	}

	public int getKeepCount() {
		return this.differentImplementationMembers.size() + this.differentImplementationMembers.size() + this.identicalMembers.size() + this.projectOnlyMembers.size();
	}

	public int getTotalCount() {
		assert this.projectOnlyMembers != null : this;
		return this.importOnlyMembers.size() + this.differentSignatureMembers.size() + this.differentImplementationMembers.size() + this.identicalMembers.size() + this.projectOnlyMembers.size();
	}

	public void appendStatusPreRejectorCheck( StringBuffer sb, org.lgna.croquet.history.CompletionStep<?> step ) {
		for( DifferentSignatureMembers<M> differentSignatureMember : this.differentSignatureMembers ) {
			differentSignatureMember.appendStatusPreRejectorCheck( sb, step );
		}
		for( DifferentImplementationMembers<M> differentImplementationMember : this.differentImplementationMembers ) {
			differentImplementationMember.appendStatusPreRejectorCheck( sb, step );
		}
	}
}
