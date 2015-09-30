/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alice.netbeans;

import edu.cmu.cs.dennisc.java.util.logging.ConsoleFormatter;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import java.util.logging.Level;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;

import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import org.netbeans.api.java.source.CompilationController;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.java.source.Task;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

import org.lgna.project.reflect.ClassInfo;
import edu.cmu.cs.dennisc.java.io.FileUtilities;
import edu.cmu.cs.dennisc.java.lang.ClassUtilities;
import edu.cmu.cs.dennisc.java.util.zip.ZipUtilities;
//import edu.cmu.cs.dennisc.print.PrintUtilities;

public final class GenerateClassInfo implements ActionListener {

	private final static String JAVA_EXTENSION = "java";
	private final static String CONSTRUCTOR_NAME = "<init>";
	private static Map<String, Set<String>> map = new HashMap<String, Set<String>>();

	private static void add(String packageName, String className) {
		Set<String> classNames = map.get(packageName);
		if (classNames != null) {
			//pass
		} else {
			classNames = new HashSet<String>();
			map.put(packageName, classNames);
		}
		classNames.add(className);
	}

	private static File[] getJavaFiles(String path, boolean isRecursionDesired) {
		File root = new File(path);
		assert root.exists() : path;
		if (root.isDirectory()) {
			if (isRecursionDesired) {
				return FileUtilities.listDescendants(root, JAVA_EXTENSION);
			} else {
				return FileUtilities.listFiles(root, JAVA_EXTENSION);
			}
		} else {
			return new File[]{root};
		}
	}

	private static void fillMap(String path, boolean isRecursionDesired) throws Exception {
		final boolean shared = false;
		for (File file : getJavaFiles(path, isRecursionDesired)) {
			assert file.exists();
			final FileObject fileObject = FileUtil.toFileObject(file);
			final JavaSource source = JavaSource.forFileObject(fileObject);
			source.runUserActionTask(new Task<CompilationController>() {

				@Override
				public void run(CompilationController compilationController) throws Exception {
					try {
						compilationController.toPhase(JavaSource.Phase.PARSED);
						//compilationController.toPhase(JavaSource.Phase.RESOLVED);
						CompilationUnitTree compilationUnitTree = compilationController.getCompilationUnit();
						String packageName = compilationUnitTree.getPackageName().toString();
						for (Tree typeTree : compilationUnitTree.getTypeDecls()) {
							ClassTree classTree = ClassUtilities.getInstance(typeTree, ClassTree.class);
							if (classTree != null) {
								ModifiersTree classModifiersTree = classTree.getModifiers();
								Set<Modifier> classModifierFlags = classModifiersTree.getFlags();
								if (classModifierFlags.contains(Modifier.PUBLIC)) {
									add(packageName, classTree.getSimpleName().toString());
								}
							}
						}
					} catch (Exception e) {
						IOLoggingHandler.printStackTrace(e);
					}
				}
			}, shared);
		}
	}

	private static void generate(String path, boolean isRecursionDesired) throws Exception {
		final boolean shared = false;
		for (File file : getJavaFiles(path, isRecursionDesired)) {
			final FileObject fileObject = FileUtil.toFileObject(file);
			final JavaSource source = JavaSource.forFileObject(fileObject);
			source.runUserActionTask(new Task<CompilationController>() {

				@Override
				public void run(CompilationController compilationController) throws Exception {
					try {
						compilationController.toPhase(JavaSource.Phase.RESOLVED);
						Trees trees = compilationController.getTrees();
						CompilationUnitTree compilationUnitTree = compilationController.getCompilationUnit();
						String packageName = compilationUnitTree.getPackageName().toString();
						for (Tree typeTree : compilationUnitTree.getTypeDecls()) {
							ClassTree classTree = ClassUtilities.getInstance(typeTree, ClassTree.class);
							if (classTree != null) {
								ModifiersTree classModifiersTree = classTree.getModifiers();
								Set<Modifier> classModifierFlags = classModifiersTree.getFlags();
								if (classModifierFlags.contains(Modifier.PUBLIC)) {
									String className = packageName + "." + classTree.getSimpleName().toString();
									IOLoggingHandler.outln(className);
									ClassInfo classInfo = ClassInfo.forName(className);
									for (Tree treeMember : classTree.getMembers()) {
										MethodTree methodTree = ClassUtilities.getInstance(treeMember, MethodTree.class);
										if (methodTree != null) {
											ModifiersTree methodModifiersTree = methodTree.getModifiers();
											Set<Modifier> methodModifierFlags = methodModifiersTree.getFlags();
											if (methodModifierFlags.contains(Modifier.PUBLIC)) {
												if (methodModifierFlags.contains(Modifier.STATIC)) {
													//pass
												} else {
													String methodName = methodTree.getName().toString();
													List<? extends VariableTree> parameters = methodTree.getParameters();
													final int N = parameters.size();
													String[] parameterClassNames = new String[N];
													String[] parameterNames = new String[N];
													Logger.outln(methodName);
													for (int i = 0; i < N; i++) {
														VariableTree variableTree = parameters.get(i);
														Tree parameterTypeTree = variableTree.getType();
														TreePath path = TreePath.getPath(compilationUnitTree, parameterTypeTree);
														TypeMirror typeMirror = trees.getTypeMirror(path);
														String parameterClassName = typeMirror.toString();
														TypeKind kind = typeMirror.getKind();
														if (kind.isPrimitive()) {
															//pass
														} else {
															if (kind == TypeKind.ARRAY) {
																if (parameterClassName.endsWith("[]")) {
																	parameterClassName = parameterClassName.substring(0, parameterClassName.length() - 2);
																} else {
																	Logger.severe(parameterClassName);
																}
															}
															if (parameterClassName.indexOf('.') != -1) {
																//pass
															} else {
																Set<String> set = map.get(packageName);
																if (set != null) {
																	if (set.contains(parameterClassName)) {
																		parameterClassName = packageName + "." + parameterClassName;
																		//PrintUtilities.println( "FOUND", paramterClassName);
																	} else {
																		Logger.severe(className, methodName, "could not find", parameterClassName, "in", packageName, set);
																	}
																} else {
																	throw new RuntimeException(packageName);
																}
															}
															if (kind == TypeKind.ARRAY) {
																//todo: only on innerclasses
																if( Character.isUpperCase( parameterClassName.charAt(0) ) ) {
																	int index = parameterClassName.lastIndexOf('.');
																	if( index != -1 ) {
																		parameterClassName = packageName + "." + parameterClassName.substring(0, index) + "$" +  parameterClassName.substring(index+1);
																	}
																}
																parameterClassName = "[L" + parameterClassName + ";";
															}
														}
														//PrintUtilities.println( "pst", paramterClassName );
														parameterClassNames[i] = parameterClassName;
														parameterNames[i] = variableTree.getName().toString();
													}
													if (CONSTRUCTOR_NAME.equals(methodName)) {
														classInfo.addConstructorInfo(parameterClassNames, parameterNames);
													} else {
														classInfo.addMethodInfo(methodName, parameterClassNames, parameterNames);
													}
												}
											}
										}
										edu.cmu.cs.dennisc.codec.CodecUtilities.encodeBinary(classInfo, new File( OUT_TEMP, classInfo.getClsName() + ".bin" ));
									}
								}
							}
						}
					} catch (Exception e) {
						IOLoggingHandler.printStackTrace(e);
					}
				}
			}, shared);
		}
	}

	private static final File GIT_CORE_ROOT = new File( FileUtilities.getDefaultDirectory(), "/Code/alice/core" );
	private static final File GIT_ALICE_ROOT = new File( FileUtilities.getDefaultDirectory(), "/Code/alice/alice" );
	private static final File STORY_API_SRC_ROOT = new File( GIT_CORE_ROOT, "/story-api/src/main/java" );
	private static final File ALICE_IDE_RESOURCES_ROOT = new File( GIT_ALICE_ROOT, "/alice-ide/src/main/resources" );
	private static final File OUT_TEMP = new File( FileUtilities.getDefaultDirectory(), "ClassInfoGenerationTemp" );
	private static final File API_ROOT = new File( STORY_API_SRC_ROOT, "/org/lgna/story" );
	private static final File OUT = new File( ALICE_IDE_RESOURCES_ROOT, "/org/alice/stageide/apis/org/lgna/story/classinfos.zip" );
	private static boolean isNecessary = true;

	private static void fillMapIfNecessary() {
		if (isNecessary) {
			isNecessary = false;
			try {
				fillMap(API_ROOT.getAbsolutePath(), false);
				fillMap(API_ROOT.getAbsolutePath()+"/event", false);
			} catch (Exception e) {
				IOLoggingHandler.printStackTrace(e);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		IOLoggingHandler ioLoggingHandler = new IOLoggingHandler();
		ioLoggingHandler.setFormatter(new ConsoleFormatter());
		Logger.getInstance().addHandler(ioLoggingHandler);
		Logger.setLevel(Level.ALL);
		try {
			FileUtilities.delete(OUT_TEMP);
			FileUtilities.delete(OUT);
			IOLoggingHandler.outln("delete");
			fillMapIfNecessary();
			generate(API_ROOT.getAbsolutePath(), false);
			generate(API_ROOT.getAbsolutePath()+"/event", false);


			FileUtilities.createParentDirectoriesIfNecessary(OUT);
			ZipUtilities.zip(OUT_TEMP, OUT );
			IOLoggingHandler.outln(OUT);
		} catch (Throwable t) {
			Logger.throwable(t);
		}
	}
}
