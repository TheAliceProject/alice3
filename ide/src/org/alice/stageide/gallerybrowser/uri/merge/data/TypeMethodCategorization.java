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
package org.alice.stageide.gallerybrowser.uri.merge.data;

/**
 * @author Dennis Cosgrove
 */
public class TypeMethodCategorization {
	private final java.util.List<org.lgna.project.ast.UserMethod> unusedProjectMethods;
	private final java.util.List<ImportOnlyMethod> importOnlyMethods = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	private final java.util.List<DifferentSignatureMethods> differentSignatureMethods = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	private final java.util.List<DifferentImplementationMethods> differentImplementationMethods = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	private final java.util.List<IdenticalMethods> identicalMethods = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
	private java.util.List<ProjectOnlyMethod> projectOnlyMethods;

	public TypeMethodCategorization( java.util.List<org.lgna.project.ast.UserMethod> projectMethods ) {
		this.unusedProjectMethods = projectMethods;
	}

	public void addImportOnlyMethod( org.lgna.project.ast.UserMethod method ) {
		this.importOnlyMethods.add( new ImportOnlyMethod( method ) );
	}

	public void addDifferentSignatureMethods( org.lgna.project.ast.UserMethod projectMethod, org.lgna.project.ast.UserMethod importMethod ) {
		this.differentSignatureMethods.add( new DifferentSignatureMethods( projectMethod, importMethod ) );
		this.unusedProjectMethods.remove( projectMethod );
	}

	public void addDifferentImplementationMethods( org.lgna.project.ast.UserMethod projectMethod, org.lgna.project.ast.UserMethod importMethod ) {
		this.differentImplementationMethods.add( new DifferentImplementationMethods( projectMethod, importMethod ) );
		this.unusedProjectMethods.remove( projectMethod );
	}

	public void addIdenticalMethods( org.lgna.project.ast.UserMethod projectMethod, org.lgna.project.ast.UserMethod importMethod ) {
		this.identicalMethods.add( new IdenticalMethods( projectMethod, importMethod ) );
		this.unusedProjectMethods.remove( projectMethod );
	}

	public void reifyProjectOnlyMethods() {
		this.projectOnlyMethods = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
		for( org.lgna.project.ast.UserMethod method : this.unusedProjectMethods ) {
			this.projectOnlyMethods.add( new ProjectOnlyMethod( method ) );
		}
	}

	public java.util.List<ImportOnlyMethod> getImportOnlyMethods() {
		return this.importOnlyMethods;
	}

	public java.util.List<DifferentSignatureMethods> getDifferentSignatureMethods() {
		return this.differentSignatureMethods;
	}

	public java.util.List<DifferentImplementationMethods> getDifferentImplementationMethods() {
		return this.differentImplementationMethods;
	}

	public java.util.List<IdenticalMethods> getIdenticalMethods() {
		return this.identicalMethods;
	}

	public java.util.List<ProjectOnlyMethod> getProjectOnlyMethods() {
		return this.projectOnlyMethods;
	}

	public int getTotalCount() {
		return this.importOnlyMethods.size() + this.differentSignatureMethods.size() + this.differentImplementationMethods.size() + this.identicalMethods.size() + this.projectOnlyMethods.size();
	}
}
