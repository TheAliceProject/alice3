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
