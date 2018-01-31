/**
 * Copyright (c) 2017, Carnegie Mellon University. All rights reserved.
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

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.alice.ide.identifier.IdentifierNameGenerator;
import org.lgna.project.annotations.FieldTemplate;
import org.lgna.project.annotations.Visibility;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Developer tool to scan the alice-model-source jar and identify all joints that are marked as PRIME_TIME.
 * Then lists them out to the console as properties file entries for English and the developer Canadian.
 */
public class GenerateJointI18nProperties {

	// The specific jar hardcoded with location and version. Another developer or another version will require an update.
	private static final String ALICE_MODEL_SOURCE_JAR =
					"/Users/daniel/.m2/repository/org/alice/alice-model-source/2016.08.19/alice-model-source-2016.08.19.jar";

	private static void listJoints() {
		java.util.Set<String> methodNames = edu.cmu.cs.dennisc.java.util.Sets.newHashSet();

		for( String className : getJointedSubclasses() ) {
			try {
				Class<?> cls = Class.forName(className);
				for( Field field : cls.getDeclaredFields() ) {
					for ( Annotation anno : field.getAnnotations()) {
						if (anno instanceof FieldTemplate && ((FieldTemplate) anno).visibility().equals(Visibility.PRIME_TIME)) {
							// Prefix with x to capitalize the first letter
							String methodName = IdentifierNameGenerator.SINGLETON.convertConstantNameToMethodName( field.getName(), "x" );
							// Remove x to get Joint to start with Capital
							methodNames.add( methodName.substring(1) );
						}
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		List<String> list = edu.cmu.cs.dennisc.java.util.Lists.newArrayList( methodNames );
		java.util.Collections.sort( list );
		StringBuilder sb = new StringBuilder();
		for( String methodName : list ) {
			sb.append( methodName );
			sb.append( " = " );
			sb.append( methodName );
			sb.append( "\n" );
		}
		sb.append("\nCanadian\n");
		for( String methodName : list ) {
			sb.append( methodName );
			sb.append( " = |" );
			sb.append( methodName );
			sb.append( "|\n" );
		}
		Logger.outln( sb );
	}

	private static List<String> getJointedSubclasses() {
		List<String> classNames = new ArrayList<>();
		try {
			ZipInputStream zip = new ZipInputStream(new FileInputStream(ALICE_MODEL_SOURCE_JAR));
			for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
				if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
					// This ZipEntry represents a class
					String className = entry.getName().replace('/', '.'); // including ".class"
					classNames.add(className.substring(0, className.length() - ".class".length()));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classNames;
	}

	public static void main( String[] args ) throws Exception {
		listJoints();
	}
}
