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

import java.awt.Component;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.netbeans.api.project.ProjectManager;
import org.netbeans.spi.project.ui.support.ProjectChooser;
import org.netbeans.spi.project.ui.templates.support.Templates;
import org.openide.WizardDescriptor;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.xml.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import org.lgna.project.ast.*;

import org.netbeans.api.java.source.CancellableTask;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.java.source.ModificationResult;
import org.netbeans.api.java.source.TreeMaker;
import org.netbeans.api.java.source.WorkingCopy;
import com.sun.source.tree.Tree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.ExpressionTree;
import com.sun.source.tree.ModifiersTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.TypeParameterTree;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.tools.JavaFileObject;
import org.lgna.project.VersionNotSupportedException;
import org.openide.filesystems.FileAlreadyLockedException;
import org.openide.filesystems.FileLock;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

public class AliceProjectTemplateWizardIterator implements WizardDescriptor./*Progress*/InstantiatingIterator {

	private static final String CLASS_NAME = AliceProjectTemplateWizardIterator.class.getName();
	private int index;
	private WizardDescriptor.Panel[] panels;
	private WizardDescriptor wiz;
	private Logger logger = Logger.getLogger(CLASS_NAME);

	public AliceProjectTemplateWizardIterator() {
//	    System.out.println("Hello!");
	}

	public static AliceProjectTemplateWizardIterator createIterator() {
		return new AliceProjectTemplateWizardIterator();
	}

	private WizardDescriptor.Panel[] createPanels() {
		return new WizardDescriptor.Panel[]{new AliceProjectTemplateWizardPanel(),};
	}

	private String[] createSteps() {
		return new String[]{NbBundle.getMessage(AliceProjectTemplateWizardIterator.class, "LBL_CreateProjectStep")};
	}

	public void showMessageDialog(Object message, String title, int messageType, javax.swing.Icon icon) {
		javax.swing.JOptionPane.showMessageDialog(this.panels[0].getComponent(), message, title, messageType, icon);
	}

	private void showUnableToOpenFileDialog(java.io.File file, String message) {
		StringBuilder sb = new StringBuilder();
		sb.append("Unable to open file ");
		sb.append(edu.cmu.cs.dennisc.java.io.FileUtilities.getCanonicalPathIfPossible(file));
		sb.append(".\n\n");
		sb.append(message);
		this.showMessageDialog(sb.toString(), "Cannot read file", javax.swing.JOptionPane.ERROR_MESSAGE, null);
	}

	private void handleVersionNotSupported(java.io.File file, org.lgna.project.VersionNotSupportedException vnse) {
		StringBuilder sb = new StringBuilder();
		sb.append("This Alice project is not compatible with this plugin:");
		sb.append("\n    Project File Version: ");
		sb.append(vnse.getVersion());
		sb.append("\n    (Minimum Supported Version: ");
		sb.append(vnse.getMinimumSupportedVersion());
		sb.append(")");
		this.showUnableToOpenFileDialog(file, sb.toString());
	}

	private void convertAliceWorldToSourceFolder(File aliceWorldFile, FileObject srcFolder) throws IOException, VersionNotSupportedException {
		if (aliceWorldFile != null && aliceWorldFile.canRead() && srcFolder != null) {
			java.io.File srcFile = aliceWorldFile;
			org.lgna.project.Project aliceProject = org.lgna.project.io.IoUtilities.readProject(srcFile);
			final NamedUserType type = (NamedUserType) aliceProject.getProgramType();

			final FileObject srcDirectory = srcFolder;

			final java.util.Set<org.lgna.common.Resource> resources = aliceProject.getResources();
			if (resources.isEmpty()) {
				//pass
			} else {
				final CharSequence resourcesClassName = "Resources";
				String resourcesFilename = resourcesClassName + ".java";
				FileObject resourcesFile;
				try {
					resourcesFile = srcDirectory.createData(resourcesFilename);
				} catch (Exception createException) {
					resourcesFile = srcDirectory.getFileObject(resourcesFilename);
				}
				final JavaSource resourcesSource = JavaSource.forFileObject(resourcesFile);
				CancellableTask<WorkingCopy> task = new CancellableTask<WorkingCopy>() {

					public void run(WorkingCopy workingCopy) throws java.io.IOException {
						workingCopy.toPhase(JavaSource.Phase.RESOLVED);
						CompilationUnitTree cut = workingCopy.getCompilationUnit();
						TreeMaker treeMaker = workingCopy.getTreeMaker();

						ModifiersTree classModifiersTree = ModifierUtilities.generateModifiersTree(treeMaker, javax.lang.model.element.Modifier.PUBLIC);
						java.util.List<TypeParameterTree> typeParameters = new java.util.LinkedList<TypeParameterTree>();
						Tree extendsClause = null;
						java.util.List<Tree> implementsClauses = new java.util.LinkedList<Tree>();
						java.util.List<Tree> memberDecls = new java.util.LinkedList<Tree>();
						for (org.lgna.common.Resource resource : resources) {
							ModifiersTree fieldModifiersTree = ModifierUtilities.generateModifiersTree(treeMaker, javax.lang.model.element.Modifier.PUBLIC, javax.lang.model.element.Modifier.STATIC, javax.lang.model.element.Modifier.FINAL);
							String fieldName = NetBeansASTGenerator.getResourceName(resource);
							ExpressionTree fieldType = treeMaker.Identifier(resource.getClass().getName());

							List<ExpressionTree> typeArguments = new LinkedList<ExpressionTree>();
							List<ExpressionTree> arguments = new LinkedList<ExpressionTree>();
							arguments.add(treeMaker.MemberSelect(treeMaker.Identifier(resourcesClassName), "class"));
							arguments.add(treeMaker.Literal("resources/" + resource.getName()));
							arguments.add(treeMaker.Literal(resource.getContentType()));

							NewClassTree fieldInitializer = treeMaker.NewClass(null, typeArguments, fieldType, arguments, null);
							memberDecls.add(treeMaker.Variable(fieldModifiersTree, fieldName, fieldType, fieldInitializer));
						}
						ClassTree classTree = treeMaker.Class(classModifiersTree, resourcesClassName, typeParameters, extendsClause, implementsClauses, memberDecls);

						JavaFileObject jfo = null;
						ExpressionTree packageName = null;
						java.util.ArrayList<ClassTree> classList = new java.util.ArrayList<ClassTree>();
						classList.add(classTree);

						java.util.List<ImportTree> imports = new java.util.LinkedList<ImportTree>();
						CompilationUnitTree cutPrime = treeMaker.CompilationUnit(packageName, imports, classList, jfo);

						workingCopy.rewrite(cut, cutPrime);

					}

					public void cancel() {
					}
				};
				ModificationResult resourcesSourceResult = resourcesSource.runModificationTask(task);
				resourcesSourceResult.commit();

				FileObject resourcesDirectory = srcDirectory.createFolder("resources");
				for (org.lgna.common.Resource resource : resources) {
					final String dstPath = resource.getName();
					FileObject f;
					try {
						f = resourcesDirectory.createData(dstPath);
					} catch (Exception createException) {
						f = resourcesDirectory.getFileObject(dstPath);
					}

					FileLock lock;
					try {
						lock = f.lock();
					} catch (FileAlreadyLockedException fale) {
						throw new RuntimeException(fale);
					}
					try {
						OutputStream os = f.getOutputStream(lock);
						os.write(resource.getData());
						os.flush();
						os.close();
					} finally {
						lock.releaseLock();
					}
				}
			}

			final String classname = "DummySource";
			final String dstPath = classname + ".java";
			FileObject f;
			try {
				f = srcDirectory.createData(dstPath);
			} catch (Exception createException) {
				f = srcDirectory.getFileObject(dstPath);
			}

			final FileObject file = f;

			final JavaSource source = JavaSource.forFileObject(file);
			final java.util.ArrayList<ClassTree> classTrees = new java.util.ArrayList<ClassTree>();

			final NetBeansASTGenerator netBeansASTGenerator = new NetBeansASTGenerator();
			CancellableTask<WorkingCopy> task = new CancellableTask<WorkingCopy>() {

				public void run(WorkingCopy workingCopy) throws java.io.IOException {
					workingCopy.toPhase(JavaSource.Phase.RESOLVED);
					TreeMaker treeMaker = workingCopy.getTreeMaker();
					java.util.List<ClassTree> typeDeclarations = netBeansASTGenerator.generateClassTrees(treeMaker, type);
					classTrees.addAll(typeDeclarations);
					//							throw new RuntimeException( "test" );
				}

				public void cancel() {
				}
			};
			ModificationResult tempResult = source.runModificationTask(task);
			f.delete();
			//            for (int i=0; i<classTrees.size(); ++i)
			//            {
			//                final ClassTree currentTree = (ClassTree) classTrees.get(i);
			for (final ClassTree currentTree : classTrees) {
				final String subClassName = currentTree.getSimpleName().toString();
				final String dstClassPath = subClassName + ".java";
				FileObject newClassFile;
				try {
					newClassFile = srcDirectory.createData(dstClassPath);
				} catch (Exception createException) {
					newClassFile = srcDirectory.getFileObject(dstClassPath);
				}
				final FileObject newClassFileObject = newClassFile;
				final JavaSource newClassSource = JavaSource.forFileObject(newClassFileObject);
				CancellableTask<WorkingCopy> makeJavaSourceTask = new CancellableTask<WorkingCopy>() {

					public void run(WorkingCopy subWorkingCopy) throws java.io.IOException {
						subWorkingCopy.toPhase(JavaSource.Phase.RESOLVED);
						TreeMaker treeMaker = subWorkingCopy.getTreeMaker();
						//NetBeansASTGenerator netBeansASTGenerator = new NetBeansASTGenerator(treeMaker);
						CompilationUnitTree subCUT = subWorkingCopy.getCompilationUnit();

						JavaFileObject jfo = null;
						ExpressionTree packageName = null;
						java.util.ArrayList<ClassTree> classList = new java.util.ArrayList<ClassTree>();
						classList.add(currentTree);

						java.util.List<ImportTree> imports = netBeansASTGenerator.generateImportTrees(currentTree);
						CompilationUnitTree subCUTPrime = treeMaker.CompilationUnit(packageName, imports, classList, jfo);
						subWorkingCopy.rewrite(subCUT, subCUTPrime);
//							throw new RuntimeException( "delete me" );
					}

					public void cancel() {
					}
				};
				ModificationResult result = newClassSource.runModificationTask(makeJavaSourceTask);
				result.commit();
			}
		}
	}

	private Set<FileObject> loadAndConvertAliceWorld(File aliceWorldFile, File projectFolder) throws IOException, VersionNotSupportedException {
		Set<FileObject> resultSet = new LinkedHashSet<FileObject>();
		projectFolder.mkdirs();
		FileObject template = Templates.getTemplate(wiz);
		FileObject dir = FileUtil.toFileObject(projectFolder);
		unZipFile(template.getInputStream(), dir);

		// Always open top dir as a project:
		resultSet.add(dir);
		FileObject srcFolder = null;
		// Look for nested projects to open as well:
		Enumeration<? extends FileObject> e = dir.getFolders(true);
		while (e.hasMoreElements()) {
			FileObject subfolder = e.nextElement();
			if (subfolder.isFolder() && subfolder.getPath().contains("src")) {
				srcFolder = subfolder;
				//                File myAddedFile = new File(subfolder.getPath(), "DaveRocks.java");
				//                myAddedFile.createNewFile();
			}
			if (ProjectManager.getDefault().isProject(subfolder)) {
				resultSet.add(subfolder);
			}
		}

		convertAliceWorldToSourceFolder(aliceWorldFile, srcFolder);

		return resultSet;
	}

	private static boolean isFolderValid(String folderString) {
		File projLoc = FileUtil.normalizeFile(new File(folderString).getAbsoluteFile());
		if (projLoc == null) {
			return false;
		}
		File[] kids = projLoc.listFiles();
		if (projLoc.exists() && kids != null && kids.length > 0) {
			return false;
		}
		return true;
	}

	private File getProjectDirForWorld(File aliceWorldFile, File baseProjectDir) {
		String baseProjectName = AliceProjectTemplatePanelVisual.getProjectNameForFile(aliceWorldFile.getName());
		File baseDir = new File(baseProjectDir, baseProjectName);
		baseProjectName = baseDir.getAbsolutePath();
		String projectName = baseProjectName;
		int count = 1;
		while (!isFolderValid(projectName)) {
			projectName = baseProjectName + "_" + count;
			count++;
		}
		return new File(projectName);
	}

	private String getRelativePathForFile(File file, File basePath) {
		if (file.isFile()) {
			file = file.getParentFile();
		}
		String fileName = file.getAbsolutePath();
		String baseName = basePath.getAbsolutePath();
		if (fileName.startsWith(baseName)) {
			return fileName.substring(baseName.length());
		}
		return "";
	}

	public Set/*<FileObject>*/ instantiate(/*ProgressHandle handle*/) throws IOException {
		Set<FileObject> resultSet = new LinkedHashSet<FileObject>();
		final String METHOD_NAME = "instantiate";
		logger.entering(CLASS_NAME, METHOD_NAME);

		boolean isSingle = (Boolean) wiz.getProperty("singleWorld");
		boolean isMulti = (Boolean) wiz.getProperty("multiWorld");
		boolean preserveDirs = (Boolean) wiz.getProperty("preserveDirs");
		String baseDirString = (String) wiz.getProperty("baseDir");
		File baseDir = null;
		if (baseDirString == null || baseDirString.length() == 0) {
			baseDir = null;
		} else {
			baseDir = new File(baseDirString);
		}

		List<File> aliceWorldFiles = new LinkedList<File>();
		List<File> aliceProjectDirs = new LinkedList<File>();
		if (isMulti) {
			String batchString = ((String) wiz.getProperty("batchString")).trim();
			aliceWorldFiles.addAll(AliceProjectTemplatePanelVisual.getWorldsFromBatchString(batchString));
			File baseProjectDir = (File) wiz.getProperty("baseProjDir");
			List<File> validAliceWorldFiles = new LinkedList<File>();
			for (File aliceWorldFile : aliceWorldFiles) {
				File newProjectDir = baseProjectDir;
				if (preserveDirs) {
					String relativeDir = getRelativePathForFile(aliceWorldFile, baseDir);
					newProjectDir = new File(baseProjectDir, relativeDir);
				}
				File projectDir = getProjectDirForWorld(aliceWorldFile, newProjectDir);
				if (projectDir != null) {
					validAliceWorldFiles.add(aliceWorldFile);
					aliceProjectDirs.add(projectDir);
				}
			}
			aliceWorldFiles = validAliceWorldFiles;
		} else if (isSingle && aliceWorldFiles.size() == 0) {
			File aliceWorldFile = (File) wiz.getProperty("aliceWorld");
			aliceWorldFiles.add(aliceWorldFile);
			aliceProjectDirs.add(FileUtil.normalizeFile((File) wiz.getProperty("projdir")));
		}
		for (int i = 0; i < aliceWorldFiles.size(); i++) {
			File aliceWorldFile = aliceWorldFiles.get(i);
			File aliceProjectDir = aliceProjectDirs.get(i);
			logger.log(Level.ALL, "Attempting to convert " + aliceWorldFile + " to " + aliceProjectDir);
			try {
				Set<FileObject> projectResultSet = loadAndConvertAliceWorld(aliceWorldFile, aliceProjectDir);
				resultSet.addAll(projectResultSet);
			} catch (VersionNotSupportedException vnse) {
				handleVersionNotSupported(aliceWorldFile, vnse);
				edu.cmu.cs.dennisc.java.io.FileUtilities.delete(aliceProjectDir);
			} catch (RuntimeException re) {
				IOLoggingHandler.printStackTrace(re);
				edu.cmu.cs.dennisc.java.io.FileUtilities.delete(aliceProjectDir);
			}
		}
		logger.exiting(CLASS_NAME, METHOD_NAME);
		return resultSet;
	}

	public void initialize(WizardDescriptor wiz) {
		this.wiz = wiz;
		index = 0;
		panels = createPanels();
		// Make sure list of steps is accurate.
		String[] steps = createSteps();
		for (int i = 0; i < panels.length; i++) {
			Component c = panels[i].getComponent();
			if (steps[i] == null) {
				// Default step name to component name of panel.
				// Mainly useful for getting the name of the target
				// chooser to appear in the list of steps.
				steps[i] = c.getName();
			}
			if (c instanceof JComponent) { // assume Swing components

				JComponent jc = (JComponent) c;
				// Step #.
				jc.putClientProperty("WizardPanel_contentSelectedIndex", new Integer(i));
				// Step name (actually the whole list for reference).
				jc.putClientProperty("WizardPanel_contentData", steps);
			}
		}
		final boolean IS_CLASS_INFO_REQUIRED = false;
		if( IS_CLASS_INFO_REQUIRED ) {
			String path = "/org/alice/stageide/apis/org/lgna/story/classinfos.zip";
			java.io.InputStream is = AliceProjectTemplateWizardIterator.class.getResourceAsStream(path);
			if (is != null) {
				try {
					org.lgna.project.reflect.ClassInfoManager.addClassInfosFrom(is);
				} catch (java.io.IOException ioe) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.throwable(ioe);
				}
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.severe(path);
			}
		}
	}

	public void uninitialize(WizardDescriptor wiz) {
		this.wiz.putProperty("projdir", null);
		this.wiz.putProperty("name", null);
		this.wiz = null;
		panels = null;
	}

	public String name() {
		return MessageFormat.format("{0} of {1}", new Object[]{new Integer(index + 1), new Integer(panels.length)});
	}

	public boolean hasNext() {
		return index < panels.length - 1;
	}

	public boolean hasPrevious() {
		return index > 0;
	}

	public void nextPanel() {
		if (!hasNext()) {
			throw new NoSuchElementException();
		}
		index++;
	}

	public void previousPanel() {
		if (!hasPrevious()) {
			throw new NoSuchElementException();
		}
		index--;
	}

	public WizardDescriptor.Panel current() {
		return panels[index];
	}

	// If nothing unusual changes in the middle of the wizard, simply:
	public final void addChangeListener(ChangeListener l) {
	}

	public final void removeChangeListener(ChangeListener l) {
	}

	private static void unZipFile(InputStream source, FileObject projectRoot) throws IOException {
		try {
			ZipInputStream str = new ZipInputStream(source);
			ZipEntry entry;
			while ((entry = str.getNextEntry()) != null) {
				if (entry.isDirectory()) {
					FileUtil.createFolder(projectRoot, entry.getName());
				} else {
					FileObject fo = FileUtil.createData(projectRoot, entry.getName());
					if ("nbproject/project.xml".equals(entry.getName())) {
						// Special handling for setting name of Ant-based projects; customize as needed:
						filterProjectXML(fo, str, projectRoot.getName());
					} else if ("nbproject/project.properties".equals(entry.getName())) {
						filterPropertiesFile(fo, str);
					} else {
						writeFile(str, fo);
					}
				}
			}
		} finally {
			source.close();
		}
	}

	private static void writeFile(ZipInputStream str, FileObject fo) throws IOException {
		OutputStream out = fo.getOutputStream();
		try {
			FileUtil.copy(str, out);
		} finally {
			out.close();
		}
	}

	private static void filterPropertiesFile(FileObject fo, ZipInputStream str) throws IOException {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			FileUtil.copy(str, baos);
			String propertiesString = baos.toString();

			String platformProp = System.getProperty("os.name").toLowerCase();
			String archProp = System.getProperty("sun.arch.data.model").toLowerCase();
			boolean isMac = platformProp.startsWith("mac os x");
			boolean isWindows = platformProp.contains("windows");
			boolean isLinux = platformProp.contains("linux");
			boolean is64Bit = archProp.contains("64");
			final String TO_REPLACE = "    <PATHS>";
			String platform = "";
			String bits = "";
			if (is64Bit) {
				bits = "amd64";
			} else {
				bits = "i586";
			}
			if (isMac) {
				platform = "macosx";
				bits = "universal";
			} else if (isWindows) {
				platform = "windows";
			} else if (isLinux) {
				platform = "linux";
			}
			String platformSpecific = "-natives-" + platform + "-" + bits + "${path.separator}";
			String replacement = "    ${libs.JOGL.classpath}" + platformSpecific + "\\\n";
			replacement += "    ${libs.GLUEGEN-RT.classpath}" + platformSpecific + "\\\n";
			replacement += "    ${libs.Alice3Library.classpath}" + platformSpecific + "\n";
			propertiesString = propertiesString.replace(TO_REPLACE, replacement);
			OutputStreamWriter osw = new OutputStreamWriter(fo.getOutputStream());
			try {
				osw.write(propertiesString, 0, propertiesString.length());
			} finally {
				osw.close();
			}
		} catch (Exception ex) {
			Exceptions.printStackTrace(ex);
			writeFile(str, fo);
		}
	}

	private static void filterProjectXML(FileObject fo, ZipInputStream str, String name) throws IOException {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			FileUtil.copy(str, baos);
			Document doc = XMLUtil.parse(new InputSource(new ByteArrayInputStream(baos.toByteArray())), false, false, null, null);
			NodeList nl = doc.getDocumentElement().getElementsByTagName("name");
			if (nl != null) {
				for (int i = 0; i < nl.getLength(); i++) {
					Element el = (Element) nl.item(i);
					if (el.getParentNode() != null && "data".equals(el.getParentNode().getNodeName())) {
						NodeList nl2 = el.getChildNodes();
						if (nl2.getLength() > 0) {
							nl2.item(0).setNodeValue(name);
						}
						break;
					}
				}
			}
			OutputStream out = fo.getOutputStream();
			try {
				XMLUtil.write(doc, out, "UTF-8");
			} finally {
				out.close();
			}
		} catch (Exception ex) {
			Exceptions.printStackTrace(ex);
			writeFile(str, fo);
		}

	}
}
