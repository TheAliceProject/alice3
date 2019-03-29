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
package test.gallery;

/**
 * @author Dennis Cosgrove
 */
public class ReadGalleryAst {
	public static void main( String[] args ) throws Exception {
		org.lgna.project.Version version = new org.lgna.project.Version( args[ 0 ] );
		java.io.File file = new java.io.File( edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory(), version + ".xml" );
		java.io.FileInputStream fis = new java.io.FileInputStream( file );
		org.lgna.project.io.MigrationManagerDecodedVersionPair[] migrationManagerDecodedVersionPairs = {
				new org.lgna.project.io.MigrationManagerDecodedVersionPair( org.lgna.project.migration.ProjectMigrationManager.getInstance(), version )
		};
		org.w3c.dom.Document xmlDocument = org.lgna.project.io.IoUtilities.readXML( fis, migrationManagerDecodedVersionPairs );
		org.lgna.project.ast.BlockStatement blockStatement = (org.lgna.project.ast.BlockStatement)org.lgna.project.ast.BlockStatement.decode( xmlDocument, org.lgna.project.ProjectVersion.getCurrentVersion() );

		org.lgna.project.Project project = null;
		org.lgna.project.migration.ProjectMigrationManager projectMigrationManager = org.lgna.project.migration.ProjectMigrationManager.getInstance();
		projectMigrationManager.migrate( blockStatement, project, version );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( blockStatement.statements.size() );
		for( org.lgna.project.ast.Statement statement : blockStatement.statements ) {
			org.lgna.project.ast.ExpressionStatement expressionStatement = (org.lgna.project.ast.ExpressionStatement)statement;
			org.lgna.project.ast.FieldAccess fieldAccess = (org.lgna.project.ast.FieldAccess)expressionStatement.expression.getValue();
			org.lgna.project.ast.JavaField field = (org.lgna.project.ast.JavaField)fieldAccess.field.getValue();
			java.lang.reflect.Field fld = field.getFieldReflectionProxy().getReification();
			if( fld != null ) {
				//pass
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.errln( "FAILURE:", field.getDeclaringType(), field );
			}
		}
	}
}
