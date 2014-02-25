package org.alice.netbeans.project;

import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.Maps;
import java.util.List;
import java.util.Map;
import org.alice.netbeans.options.Alice3OptionsPanelController;
import org.lgna.project.ast.JavaCodeGenerator;
import org.lgna.project.ast.ManagementLevel;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.ast.UserType;
import org.lgna.story.SScene;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/ class NetbeansJavaCodeGenerator extends JavaCodeGenerator {

	private static final Map<String, String> methodNamesToCollapse;
	private static final String INDENT = "    "; //todo: query indent from netbeans format
	private static final String BOILER_PLATE_CODE_TEXT = "boiler plate code";
	private static final String IMPORT_TEXT = "imports";

	static {
		methodNamesToCollapse = Maps.newHashMap();
		methodNamesToCollapse.put("performGeneratedSetUp", BOILER_PLATE_CODE_TEXT);
		methodNamesToCollapse.put("handleActiveChanged", BOILER_PLATE_CODE_TEXT);
	}

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

	private String getMethodCollapseText(UserMethod method) {
		if( Alice3OptionsPanelController.isBoilerPlateMethodCollapsingDesired() ) {
			if (method.getDeclaringType().isAssignableTo(SScene.class)) {
				return methodNamesToCollapse.get(method.name.getValue());
			}
		}
		return null;
	}

	@Override
	protected String getMethodPrefix(UserMethod method) {
		String collapseText = this.getMethodCollapseText(method);
		if (collapseText != null) {
			return "\n" + INDENT + "// <editor-fold defaultstate=\"collapsed\" desc=\"" + collapseText + ": " + method.name.getValue() + "\">\n";
		} else {
			return super.getMethodPrefix(method);
		}
	}

	@Override
	protected String getMethodPostfix(UserMethod method) {
		String collapseText = this.getMethodCollapseText(method);
		if (collapseText != null) {
			return "\n" + INDENT + "// </editor-fold>\n";
		} else {
			return super.getMethodPostfix(method);
		}
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
