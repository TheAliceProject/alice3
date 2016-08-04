/*******************************************************************************
 * Copyright (c) 2006, 2016, Carnegie Mellon University. All rights reserved.
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

package org.alice.netbeans.project;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import java.util.List;
import java.util.Map;
import org.alice.netbeans.options.Alice3OptionsPanelController;
import org.lgna.project.ast.AbstractType;
import org.lgna.project.ast.JavaCodeGenerator;
import org.lgna.project.ast.ManagementLevel;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserType;
import org.lgna.story.SScene;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/ class NetbeansJavaCodeGenerator extends JavaCodeGenerator {

	private static final String INDENT = "    "; //todo: query indent from netbeans format
	private static final String IMPORT_TEXT = "imports";

	public NetbeansJavaCodeGenerator(JavaCodeGenerator.Builder javaCodeGeneratorBuilder) {
		super(javaCodeGeneratorBuilder);
	}

	@Override
	protected String getImportsPrefix() {
		if( Alice3OptionsPanelController.isImportCollapsingDesired() ) {
			return "// <editor-fold defaultstate=\"collapsed\" desc=\"" + IMPORT_TEXT + "\">\n";
		} else {
			return super.getImportsPostfix();
		}
	}

	@Override
	protected String getImportsPostfix() {
		if( Alice3OptionsPanelController.isImportCollapsingDesired() ) {
			return "\n// </editor-fold>\n";
		} else {
			return super.getImportsPostfix();
		}
	}
        
        @Override
        protected String getSectionPrefix( AbstractType<?, ?, ?> declaringType, String sectionName, boolean shouldCollapse ) {
            String sectionComment = this.getLocalizedCommentForSection( declaringType, sectionName, java.util.Locale.getDefault() );
            String rv = null;
            if (shouldCollapse && Alice3OptionsPanelController.isBoilerPlateMethodCollapsingDesired()) {
                String desc = sectionComment != null ? "desc=\""+ sectionComment + "\"" : "";
                rv = "\n" + INDENT + "// <editor-fold defaultstate=\"collapsed\" "+desc+">\n";  
            }
            else if( sectionComment != null ) {
                rv = "\n\n" + INDENT + sectionComment + "\n";
            }
            else {
                rv = super.getSectionPrefix(declaringType, sectionName, shouldCollapse);
            }
            return rv;
	}

        @Override
	protected String getSectionPostfix( AbstractType<?, ?, ?> declaringType, String sectionName, boolean shouldCollapse ) {
            String rv = null;
            if (shouldCollapse && Alice3OptionsPanelController.isBoilerPlateMethodCollapsingDesired()) {
                rv = "\n" + INDENT + "// </editor-fold>\n";
            }
            else {
                rv = super.getSectionPostfix(declaringType, sectionName, shouldCollapse);
            }
            return rv;
	}

	private void appendMethodsWithManagementLevel(List<UserMethod> list, UserType<?> type, ManagementLevel managementLevel) {
		for (UserMethod method : type.methods) {
			if (method.managementLevel.getValue() == managementLevel) {
				list.add(method);
			}
		}
	}

	@Override
	public Iterable<UserMethod> getMethods(UserType<?> type) {
		List<UserMethod> methods = Lists.newArrayListWithInitialCapacity(type.methods.size());
		this.appendMethodsWithManagementLevel(methods, type, null);
		this.appendMethodsWithManagementLevel(methods, type, ManagementLevel.NONE);
		this.appendMethodsWithManagementLevel(methods, type, ManagementLevel.GENERATED);
		this.appendMethodsWithManagementLevel(methods, type, ManagementLevel.MANAGED);
		return methods;
	}
}
