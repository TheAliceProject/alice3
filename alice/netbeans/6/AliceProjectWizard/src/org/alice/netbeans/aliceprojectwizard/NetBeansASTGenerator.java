/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.netbeans.aliceprojectwizard;

import com.sun.source.tree.Tree;
import com.sun.source.tree.BlockTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.StatementTree;
import com.sun.source.tree.TypeParameterTree;
import com.sun.source.tree.VariableTree;
import edu.cmu.cs.dennisc.java.util.Sets;
import javax.lang.model.element.Modifier;
import org.netbeans.api.java.source.TreeMaker;

import org.lgna.project.ast.*;

public class NetBeansASTGenerator {

	private static final java.util.Set<String> classesToImportPeriod;
	private static final java.util.Set<String> packagesToWildcardPeriod;
	private static final java.util.Set<String> packagesToWildcardIfReferenced;
	private static final String[] membersToStaticImportPeriod;

	static {
		classesToImportPeriod = new java.util.HashSet<String>();
		classesToImportPeriod.add("org.lgna.common.EachInTogetherRunnable");
		membersToStaticImportPeriod = new String[]{
					"org.lgna.common.ThreadUtilities.doTogether",
					"org.lgna.common.ThreadUtilities.eachInTogether",};
		packagesToWildcardPeriod = new java.util.HashSet<String>();
		packagesToWildcardPeriod.add("org.lgna.story");

		packagesToWildcardIfReferenced = new java.util.HashSet<String>();
	}

	private static java.util.Map<String, Boolean> createReferencedPackageMap() {
		java.util.Map<String, Boolean> rv = new java.util.HashMap<String, Boolean>();
		for (String specialPackageName : NetBeansASTGenerator.packagesToWildcardIfReferenced) {
			rv.put(specialPackageName, false);
		}
		return rv;
	}
	//Magician magician = new Magician();
	//static TopsyTheClown topsyTheClown = new TopsyTheClown();
	static java.util.Map<TreeMaker, NetBeansASTGenerator> mapTreeMakerToGenerator = new java.util.HashMap<TreeMaker, NetBeansASTGenerator>();
	private TreeMaker treeMaker;
	private java.util.Set<NamedUserType> referencedAliceTypes = new java.util.HashSet<NamedUserType>();
	private java.util.Map<AbstractType, java.util.Set<AbstractType>> mapOwnerTypeToReferencedTypes = new java.util.HashMap<AbstractType, java.util.Set<AbstractType>>();
	private java.util.Map<ClassTree, AbstractType> mapClassTreeToOwnerType = new java.util.HashMap<ClassTree, AbstractType>();
	//private java.util.Set<VariableDeclaredInAlice> wrappedVariables = new java.util.HashSet<VariableDeclaredInAlice>();
	private AbstractType currentOwnerType = null;
	private java.util.Stack<Boolean> isInnerClassStack = new java.util.Stack<Boolean>();
	private boolean isInnerClass = false;

	public static NetBeansASTGenerator get(TreeMaker treeMaker) {
		return NetBeansASTGenerator.mapTreeMakerToGenerator.get(treeMaker);
	}

	private void setTreeMaker(TreeMaker treeMaker) {
		this.treeMaker = treeMaker;
		NetBeansASTGenerator.mapTreeMakerToGenerator.put(this.treeMaker, this);
	}

	public void pushIsInnerClass(boolean isInnerClass) {
		this.isInnerClassStack.push(this.isInnerClass);
		this.isInnerClass = isInnerClass;
	}

	public void popIsInnerClass() {
		this.isInnerClass = this.isInnerClassStack.pop();
	}

	public void registerType(AbstractType type) {
		assert type != null;
		assert this.currentOwnerType != null;
		if (type.isArray()) {
			registerType(type.getComponentType());
		} else {
			mapOwnerTypeToReferencedTypes.get(this.currentOwnerType).add(type);
			if (type instanceof NamedUserType) {
				referencedAliceTypes.add((NamedUserType) type);
			}
		}
	}

	private String generateTypeName(AbstractType type) {
		if (type != null) {
			if (type.isArray()) {
				AbstractType componentType = type.getComponentType();
				assert componentType != null;
				return this.generateTypeName(type.getComponentType()) + "[]";
			} else {
				return type.getName();
			}
		} else {
			return null;
		}
	}

	public IdentifierTree generateTypeTree(AbstractType type) {
		if (type instanceof JavaType) {
			JavaType typeJ = (JavaType) type;
			ClassReflectionProxy classReflectionProxy = typeJ.getClassReflectionProxy();
			ClassReflectionProxy declaringClassReflectionProxy = classReflectionProxy.getDeclaringClassReflectionProxy();
			if (declaringClassReflectionProxy != null) {
				JavaType declaringType = JavaType.getInstance(declaringClassReflectionProxy);
				registerType(declaringType);
				return this.treeMaker.Identifier(declaringType.getName() + "." + generateTypeName(type));
			}
		}
		registerType(type);
		if (type instanceof AnonymousUserType) {
			AnonymousUserType anonymousInnerTypeDeclaredInAlice = (AnonymousUserType) type;
			IdentifierTree identifierTree = this.generateTypeTree(anonymousInnerTypeDeclaredInAlice.superType.getValue());
			return identifierTree;
		} else {
			String name = this.generateTypeName(type);
			if (name != null) {
				return this.treeMaker.Identifier(name);
			} else {
				return null;
			}
		}
	}

	public String generateThisIdentifier() {
		String rv;
		if (this.isInnerClass) {
			StringBuilder sb = new StringBuilder();
			sb.append(this.currentOwnerType.getName());
			sb.append(".this");
			rv = sb.toString();
		} else {
			rv = "this";
		}
		return rv;
	}

	public String generateVariableAccessIdentifier(LocalAccess localAccess) {
		UserLocal local = localAccess.local.getValue();
		String rv = local.getValidName(localAccess);
		return rv;
	}

//	private java.util.List<ExpressionTree> generateTypeArguments(AbstractType... types) {
//		java.util.List<ExpressionTree> rv = new java.util.LinkedList<ExpressionTree>();
//		for (AbstractType type : types) {
//			rv.add(generateTypeTree(type));
//		}
//		return rv;
//	}
//	private ParameterizedTypeTree generateParameterizedType(AbstractType type, AbstractType... argumentTypes) {
//		return treeMaker.ParameterizedType(this.generateTypeTree(type), generateTypeArguments(argumentTypes));
//	}
	private Tree generateLocalTypeTree(UserLocal local/*, boolean isGenericRequired*/) {
		return generateTypeTree(local.valueType.getValue());
	}

	private VariableTree generateVariableTree(Tree typeTree, UserLocal local, ExpressionTree initializerTree, javax.lang.model.element.Modifier... modifiers) {
		//ModifiersTree modifiersTree = ModifierUtilities.generateModifiersTree(treeMaker, javax.lang.model.element.Modifier.FINAL);
		ModifiersTree modifiersTree = ModifierUtilities.generateModifiersTree(treeMaker, modifiers);
		//todo
		Node context = null;

		String name = this.getValidName(local, context);

		return treeMaker.Variable(modifiersTree, name, typeTree, initializerTree);
	}

	public VariableTree generateLocalDeclarationStatement(UserLocal local, ExpressionTree initializerTree) {
		Tree treeType = generateLocalTypeTree(local);
		Modifier[] modifiers;
		if (local.isFinal.getValue()) {
			modifiers = new Modifier[]{javax.lang.model.element.Modifier.FINAL};
		} else {
			modifiers = new Modifier[]{};
		}
		return generateVariableTree(treeType, local, initializerTree, modifiers);
	}

	public VariableTree generateEachVariableDeclaration(UserLocal local) {
		Tree treeType = generateLocalTypeTree(local);
		return generateVariableTree(treeType, local, null, javax.lang.model.element.Modifier.FINAL);
	}

	private VariableTree generateParameter(AbstractParameter parameter) {
		//todo
		//MosdifiersTree modifiersTree = this.generateModifiersTree(parameter.getAccess());
		ModifiersTree modifiersTree = ModifierUtilities.generateModifiersTree(this.treeMaker, javax.lang.model.element.Modifier.FINAL);
		CharSequence name = parameter.getName();
		Tree type = this.generateTypeTree(parameter.getValueType());
		ExpressionTree initializer = null;
		return this.treeMaker.Variable(modifiersTree, name, type, initializer);
	}
	private LocalNameGenerator localNameGenerator = null;
	private static int unnamedResourceCount = 0;

	public static String getResourceName(org.lgna.common.Resource resource) {
		StringBuilder sb = new StringBuilder();

		//todo: move this code to getName()
		String name = resource.getName();
		if (name != null) {
			for (char c : name.toCharArray()) {
				if (Character.isLetterOrDigit(c)) {
					sb.append(c);
				} else {
					sb.append('_');
				}
			}
		}

		String rv;
		if (sb.length() > 0) {
			rv = sb.toString();
			if (Character.isLetter(name.charAt(0))) {
				//pass
			} else {
				rv = "_" + rv;
			}
		} else {
			rv = "UNNAMED_" + (unnamedResourceCount++);
		}
		return rv;
	}

	public IdentifierTree getIdentifier(TreeMaker treeMaker, ResourceExpression resourceExpression) {
		org.lgna.common.Resource resource = resourceExpression.resource.getValue();
		if (resource != null) {
			return treeMaker.Identifier("Resources." + getResourceName(resource));
		} else {
			return treeMaker.Identifier("null");
		}
	}

	public String getValidName(UserLocal local, Node context) {
		if (this.localNameGenerator != null) {
			return this.localNameGenerator.getNameFor(local, context);
		} else {
			return local.getValidName(context);
		}
	}

	private MethodTree generateConstructor(NamedUserConstructor constructor, CharSequence name) {
		java.util.Set<javax.lang.model.element.Modifier> modifiers = Sets.newHashSet();
		constructor.addModifiers(modifiers);
		ModifiersTree modifiersTree = ModifierUtilities.generateModifiersTree(this.treeMaker, modifiers);
		java.util.List<TypeParameterTree> typeParameters = new java.util.LinkedList<TypeParameterTree>();
		java.util.List<VariableTree> parameters = new java.util.LinkedList<VariableTree>();
		for (AbstractParameter parameter : constructor.requiredParameters) {
			parameters.add(generateParameter(parameter));
		}
		java.util.List<ExpressionTree> throwsList = new java.util.LinkedList<ExpressionTree>();
		localNameGenerator = new LocalNameGenerator(constructor);
		BlockTree body = StatementUtilities.generateBlockStatement(this.treeMaker, constructor.body.getValue());
		localNameGenerator = null;
//        return this.treeMaker.Constructor(modifiersTree, typeParameters, parameters, throwsList, body);

//		CharSequence name = "<init>";
		//Tree returnType = this.treeMaker.PrimitiveType(TypeKind.VOID);
		Tree returnType = null;
		ExpressionTree defaultValue = null; //used by annotations
		return this.treeMaker.Method(modifiersTree, name, returnType, typeParameters, parameters, throwsList, body, defaultValue);
	}

	//todo: reduce visibility?
	public MethodTree generateMethod(UserMethod method) {
		java.util.Set<javax.lang.model.element.Modifier> modifiers = Sets.newHashSet();
		method.addModifiers(modifiers);
		ModifiersTree modifiersTree = ModifierUtilities.generateModifiersTree(this.treeMaker, modifiers);
		CharSequence name = method.getName();

		Tree returnType = this.generateTypeTree(method.getReturnType());

		java.util.List<TypeParameterTree> typeParameters = new java.util.LinkedList<TypeParameterTree>();
		java.util.List<VariableTree> parameters = new java.util.LinkedList<VariableTree>();
		for (AbstractParameter parameter : method.requiredParameters) {
			parameters.add(generateParameter(parameter));
		}

		java.util.List<ExpressionTree> throwsList = new java.util.LinkedList<ExpressionTree>();

		localNameGenerator = new LocalNameGenerator(method);
		BlockTree body = StatementUtilities.generateBlockStatement(this.treeMaker, method.body.getValue());
		localNameGenerator = null;

		ExpressionTree defaultValue = null; //used by annotations

		return this.treeMaker.Method(modifiersTree, name, returnType, typeParameters, parameters, throwsList, body, defaultValue);
	}

	public MethodTree generateMethodForLambda(UserLambda lambda, CharSequence name) {
		java.util.Set<javax.lang.model.element.Modifier> modifiers = Sets.newHashSet();
		lambda.addModifiers(modifiers);
		ModifiersTree modifiersTree = ModifierUtilities.generateModifiersTree(this.treeMaker, modifiers);

		Tree returnType = this.generateTypeTree(lambda.getReturnType());

		java.util.List<TypeParameterTree> typeParameters = new java.util.LinkedList<TypeParameterTree>();
		java.util.List<VariableTree> parameters = new java.util.LinkedList<VariableTree>();
		for (AbstractParameter parameter : lambda.requiredParameters) {
			parameters.add(generateParameter(parameter));
		}

		java.util.List<ExpressionTree> throwsList = new java.util.LinkedList<ExpressionTree>();

		localNameGenerator = new LocalNameGenerator(lambda);
		BlockTree body = StatementUtilities.generateBlockStatement(this.treeMaker, lambda.body.getValue());
		localNameGenerator = null;

		ExpressionTree defaultValue = null; //used by annotations

		return this.treeMaker.Method(modifiersTree, name, returnType, typeParameters, parameters, throwsList, body, defaultValue);
	}

	public MethodTree generateGetter(Getter getter) {
		java.util.Set<javax.lang.model.element.Modifier> modifiers = Sets.newHashSet();
		getter.addModifiers(modifiers);
		ModifiersTree modifiersTree = ModifierUtilities.generateModifiersTree(this.treeMaker, modifiers);
		CharSequence name = getter.getName();
		Tree returnType = this.generateTypeTree(getter.getReturnType());

		java.util.List<TypeParameterTree> typeParameters = new java.util.LinkedList<TypeParameterTree>();
		java.util.List<VariableTree> parameters = new java.util.LinkedList<VariableTree>();
		java.util.List<ExpressionTree> throwsList = new java.util.LinkedList<ExpressionTree>();

		java.util.List<StatementTree> statements = new java.util.LinkedList<StatementTree>();
		statements.add(StatementUtilities.generateGetterStatement(treeMaker, getter));
		boolean isStatic = false;
		BlockTree body = treeMaker.Block(statements, isStatic);

		ExpressionTree defaultValue = null; //used by annotations

		return this.treeMaker.Method(modifiersTree, name, returnType, typeParameters, parameters, throwsList, body, defaultValue);
	}

	public MethodTree generateSetter(Setter setter) {
		java.util.Set<javax.lang.model.element.Modifier> modifiers = Sets.newHashSet();
		setter.addModifiers(modifiers);
		ModifiersTree modifiersTree = ModifierUtilities.generateModifiersTree(this.treeMaker, modifiers);
		CharSequence name = setter.getName();
		Tree returnType = this.generateTypeTree(setter.getReturnType());

		java.util.List<TypeParameterTree> typeParameters = new java.util.LinkedList<TypeParameterTree>();
		java.util.List<VariableTree> parameters = new java.util.LinkedList<VariableTree>();

		for (AbstractParameter parameter : setter.getRequiredParameters()) {
			parameters.add(generateParameter(parameter));
		}

		java.util.List<ExpressionTree> throwsList = new java.util.LinkedList<ExpressionTree>();

		java.util.List<StatementTree> statements = new java.util.LinkedList<StatementTree>();
		statements.add(StatementUtilities.generateSetterStatement(treeMaker, setter));

		boolean isStatic = false;
		BlockTree body = treeMaker.Block(statements, isStatic);

		ExpressionTree defaultValue = null; //used by annotations

		return this.treeMaker.Method(modifiersTree, name, returnType, typeParameters, parameters, throwsList, body, defaultValue);
	}

	public VariableTree generateField(UserField field) {
		java.util.Set<javax.lang.model.element.Modifier> modifiers = Sets.newHashSet();
		field.addModifiers(modifiers);
		ModifiersTree modifiersTree = ModifierUtilities.generateModifiersTree(this.treeMaker, modifiers);
		CharSequence name = field.name.getValue();
		Tree typeTree = this.generateTypeTree(field.valueType.getValue());
		ExpressionTree initializer = ExpressionUtilities.generate(this.treeMaker, field.initializer.getValue());
		return this.treeMaker.Variable(modifiersTree, name, typeTree, initializer);
	}

	private void generateConstructors(java.util.List<Tree> memberDecls, NamedUserType type) {
		CharSequence name = type.getName();
		for (NamedUserConstructor constructor : type.constructors) {
			memberDecls.add(generateConstructor(constructor, name));
		}
	}

	/*package-private*/ void generateMethods(java.util.List<Tree> memberDecls, UserType<?> type) {
		for (UserMethod method : type.methods) {
			memberDecls.add(generateMethod(method));
		}
	}

	/*package-private*/ void generateGettersAndSetters(java.util.List<Tree> memberDecls, UserType<?> type) {
		for (UserField field : type.fields) {
			memberDecls.add(generateGetter(field.getGetter()));
			if (field.isFinal()) {
				//pass
			} else {
				memberDecls.add(generateSetter(field.getSetter()));
			}
		}
	}

	/*package-private*/ void generateFields(java.util.List<Tree> memberDecls, UserType<?> type) {
		for (UserField field : type.fields) {
			memberDecls.add(generateField(field));
		}
	}

	private ClassTree generateType(NamedUserType type) {
		java.util.Set<javax.lang.model.element.Modifier> modifiers = Sets.newHashSet();
		type.addModifiers(modifiers);
		ModifiersTree modifiersTree = ModifierUtilities.generateModifiersTree(this.treeMaker, modifiers);
		java.util.List<TypeParameterTree> typeParameters = new java.util.LinkedList<TypeParameterTree>();
		Tree extendsClause = this.generateTypeTree(type.getSuperType());
		java.util.List<Tree> implementsClauses = new java.util.LinkedList<Tree>();
		java.util.List<Tree> memberDecls = new java.util.LinkedList<Tree>();

		generateConstructors(memberDecls, type);
		generateMethods(memberDecls, type);
		generateGettersAndSetters(memberDecls, type);
		generateFields(memberDecls, type);

		CharSequence name = type.getName();
		ClassTree rv = this.treeMaker.Class(modifiersTree, name, typeParameters, extendsClause, implementsClauses, memberDecls);
		return rv;
	}

	private java.util.List<NamedUserType> getUnhandledTypes(java.util.Set<NamedUserType> handledTypes) {
		java.util.List<NamedUserType> rv = new java.util.LinkedList<NamedUserType>();
		rv.addAll(this.referencedAliceTypes);
		rv.removeAll(handledTypes);
		return rv;
	}

	public java.util.List<ClassTree> generateClassTrees(TreeMaker treeMaker, NamedUserType type) {
		this.setTreeMaker(treeMaker);
		//todo
		this.referencedAliceTypes.add(type);
		java.util.Set<NamedUserType> handled = new java.util.HashSet<NamedUserType>();
		java.util.List<ClassTree> rv = new java.util.LinkedList<ClassTree>();
		while (true) {
			java.util.List<NamedUserType> unhandled = getUnhandledTypes(handled);
			if (unhandled.size() > 0) {
				for (NamedUserType unhandledType : unhandled) {
					this.currentOwnerType = unhandledType;
					this.mapOwnerTypeToReferencedTypes.put(unhandledType, new java.util.HashSet<AbstractType>());
					ClassTree classTree = generateType(unhandledType);
					this.mapClassTreeToOwnerType.put(classTree, unhandledType);
					rv.add(classTree);
					this.currentOwnerType = null;
					handled.add(unhandledType);
				}
			} else {
				break;
			}
		}

		return rv;
	}

	public java.util.List<ImportTree> generateImportTrees(ClassTree classTree) {
		assert classTree != null;
		//JOptionPane.showMessageDialog(null, classTree.toString(), "generateImportTrees: "  + classTree.hashCode(), JOptionPane.INFORMATION_MESSAGE );
		AbstractType ownerType = this.mapClassTreeToOwnerType.get(classTree);
		assert ownerType != null;
		java.util.List<ImportTree> rv = new java.util.LinkedList<ImportTree>();
		boolean isStatic = false;
		java.util.Map<String, Boolean> map = NetBeansASTGenerator.createReferencedPackageMap();
		for (AbstractType type : this.mapOwnerTypeToReferencedTypes.get(ownerType)) {
			AbstractPackage pack = type.getPackage();
			if (pack != null) {
				String packageName = pack.getName();
				if (packageName.equals("java.lang")) {
					//pass
				} else {
					if (NetBeansASTGenerator.packagesToWildcardPeriod.contains(packageName)) {
						//pass
					} else if (NetBeansASTGenerator.packagesToWildcardIfReferenced.contains(packageName)) {
						map.put(packageName, true);
					} else {
						String clsName = pack.getName() + "." + type.getName();
						if (NetBeansASTGenerator.classesToImportPeriod.contains(clsName)) {
							//pass
						} else {
							rv.add(treeMaker.Import(this.treeMaker.Identifier(clsName), isStatic));
						}
					}
				}
			}
		}
		for (String packageName : map.keySet()) {
			boolean isReferenced = map.get(packageName);
			if (isReferenced) {
				rv.add(treeMaker.Import(this.treeMaker.Identifier(packageName + ".*"), isStatic));
			}
		}
		for (String packageName : NetBeansASTGenerator.packagesToWildcardPeriod) {
			rv.add(treeMaker.Import(this.treeMaker.Identifier(packageName + ".*"), isStatic));
		}
		for (String clsName : NetBeansASTGenerator.classesToImportPeriod) {
			rv.add(treeMaker.Import(this.treeMaker.Identifier(clsName), isStatic));
		}

		for (String memberName : NetBeansASTGenerator.membersToStaticImportPeriod) {
			rv.add(treeMaker.Import(this.treeMaker.Identifier(memberName), true));
		}
		return rv;
	}
}
