/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
package org.lgna.project.migration;

/**
 * @author Dennis Cosgrove
 */
public class MigrationManager {
	public static final String NO_REPLACEMENT  = null;
	
	private static final TextMigration[] textMigrations = {
		new org.lgna.project.migration.TextMigration(
				new org.lgna.project.Version( "3.1.7.0.0" ),
				new org.lgna.project.Version( "3.1.8.0.0" )
			),

			new org.lgna.project.migration.TextMigration(
				new org.lgna.project.Version( "3.1.8.0.0" ),
				new org.lgna.project.Version( "3.1.9.0.0" ),

				"ARMOIRE_CLOTHING",
				NO_REPLACEMENT,
				
				"org.lgna.story.resources.armoire.ArmoireArtNouveau",
				NO_REPLACEMENT,
				
				"PINK_POODLE",
				NO_REPLACEMENT,
				
				"org.lgna.story.resources.quadruped.Poodle",
				NO_REPLACEMENT 
			),

			new org.lgna.project.migration.TextMigration(
				new org.lgna.project.Version( "3.1.9.0.0" ),
				new org.lgna.project.Version( "3.1.10.0.0" )
			),

			new org.lgna.project.migration.TextMigration(
				new org.lgna.project.Version( "3.1.10.0.0" ),
				new org.lgna.project.Version( "3.1.11.0.0" )
			),

			new org.lgna.project.migration.TextMigration(
				new org.lgna.project.Version( "3.1.11.0.0" ),
				new org.lgna.project.Version( "3.1.14.0.0" ),

				"CAMEL",
				NO_REPLACEMENT ,
				
				"org.lgna.story.resources.quadruped.Camel",
				NO_REPLACEMENT ,

				"FALCON",
				NO_REPLACEMENT ,

				"org.lgna.story.resources.flyer.Falcon",
				NO_REPLACEMENT ,

				"LION",
				NO_REPLACEMENT ,

				"org.lgna.story.resources.quadruped.Lion",
				NO_REPLACEMENT ,

				"WOLF",
				NO_REPLACEMENT ,

				"org.lgna.story.resources.quadruped.Wolf",
				NO_REPLACEMENT
			),

			new org.lgna.project.migration.TextMigration(
				new org.lgna.project.Version( "3.1.14.0.0" ),
				new org.lgna.project.Version( "3.1.15.1.0" )
			),

			new org.lgna.project.migration.TextMigration(
				new org.lgna.project.Version( "3.1.15.1.0" ),
				new org.lgna.project.Version( "3.1.20.0.0" ),

				"org.lgna.story.resources.dresser.DresserCentralAsian",
				"org.lgna.story.resources.prop.DresserCentralAsian",

				"org.lgna.story.resources.dresser.DresserColonial",
				"org.lgna.story.resources.prop.DresserColonial",

				"org.lgna.story.resources.dresser.DresserDesigner",
				"org.lgna.story.resources.prop.DresserDesigner"
			),

			new org.lgna.project.migration.TextMigration(
				new org.lgna.project.Version( "3.1.20.0.0" ),
				new org.lgna.project.Version( "3.1.23.0.0" )
			),

			new org.lgna.project.migration.TextMigration(
				new org.lgna.project.Version( "3.1.23.0.0" ),
				new org.lgna.project.Version( "3.1.24.0.0" )
			),

			new org.lgna.project.migration.TextMigration(
				new org.lgna.project.Version( "3.1.24.0.0" ),
				new org.lgna.project.Version( "3.1.25.0.0" )
			)
	};
	private static final java.util.List< Migration > versionIndependentMigrations = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	
	private MigrationManager() {
		throw new AssertionError();
	}
	
	public static boolean isDevoidOfVersionIndependentMigrations() {
		return versionIndependentMigrations.size() == 0;
	}
	
	public static String migrate( String source, org.lgna.project.Version version ) {
		String rv = source;
		for( TextMigration textMigration : textMigrations ) {
			if( textMigration.isApplicable( version ) ) {
				rv = textMigration.migrate( rv );
				version = textMigration.getResultVersion();
			}
		}
		
		for( Migration versionIndependentMigration : versionIndependentMigrations ) {
			rv = versionIndependentMigration.migrate( rv );
		}
		
		return rv;
	}
	
	public static void addVersionIndependentMigration( Migration migration ) {
		versionIndependentMigrations.add( migration );
	}
	public static void removeVersionIndependentMigration( Migration migration ) {
		versionIndependentMigrations.remove( migration );
	}
}
