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
public class GenerateGalleryAst {
	private static org.lgna.project.ast.Statement createFieldAccessStatement( java.lang.reflect.Field fld ) {
		org.lgna.project.ast.JavaField field = org.lgna.project.ast.JavaField.getInstance( fld );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( field.getDeclaringType(), field );
		org.lgna.project.ast.FieldAccess fieldAccess = new org.lgna.project.ast.FieldAccess(
				new org.lgna.project.ast.TypeLiteral( field.getDeclaringType() ),
				field
				);
		return new org.lgna.project.ast.ExpressionStatement( fieldAccess );

	}

	public static void main( String[] args ) throws Exception {
		org.lgna.project.ast.BlockStatement blockStatement = new org.lgna.project.ast.BlockStatement();

		org.lgna.story.resources.sims2.IngredientManager<?>[] managers = {
				org.lgna.story.resources.sims2.FullBodyOutfitManager.getSingleton(),
				org.lgna.story.resources.sims2.HairManager.getSingleton(),
		};
		for( org.lgna.story.resources.sims2.LifeStage lifeStage : org.lgna.story.resources.sims2.LifeStage.values() ) {
			for( org.lgna.story.resources.sims2.Gender gender : org.lgna.story.resources.sims2.Gender.values() ) {
				for( org.lgna.story.resources.sims2.IngredientManager<?> manager : managers ) {
					Class<?>[] clses = manager.getImplementingClasses( lifeStage, gender );
					for( Class<?> cls : clses ) {
						for( Enum e : (Enum[])cls.getEnumConstants() ) {
							java.lang.reflect.Field fld = cls.getField( e.name() );
							blockStatement.statements.add( createFieldAccessStatement( fld ) );
						}
					}
				}
			}
		}

		org.w3c.dom.Document xmlDocument = blockStatement.encode();
		java.io.File file = new java.io.File( edu.cmu.cs.dennisc.java.io.FileUtilities.getDefaultDirectory(), org.lgna.project.ProjectVersion.getCurrentVersionText() + ".xml" );
		edu.cmu.cs.dennisc.xml.XMLUtilities.write( xmlDocument, file );
		edu.cmu.cs.dennisc.java.util.logging.Logger.outln( file );
	}
}
