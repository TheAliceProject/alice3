/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.alice.netbeans.project;

import edu.cmu.cs.dennisc.java.io.TextFileUtilities;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.lgna.project.Project;
import org.lgna.project.VersionNotSupportedException;
import org.lgna.project.ast.JavaCodeGenerator;
import org.lgna.project.ast.ManagementLevel;
import org.lgna.project.ast.UserMethod;
import org.lgna.project.io.IoUtilities;
import org.lgna.project.resource.ResourcesTypeWrapper;
import org.lgna.story.SProgram;
import org.lgna.story.SScene;
import org.lgna.story.ast.JavaCodeUtilities;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.modules.editor.indent.api.Reformat;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileAlreadyLockedException;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.text.NbDocument;

/**
 * @author Dennis Cosgrove
 */
public class ProjectCodeGenerator {

	private static void progress(ProgressHandle progressHandle, String prefix, FileObject fileObject, int workUnit) {
		if (progressHandle != null) {
			progressHandle.progress(prefix + fileObject.getNameExt(), workUnit);
			//for testing progress
			//ThreadUtilities.sleep(1000);
		}
	}

	public static Collection<FileObject> generateCode(File aliceProjectFile, File javaSrcDirectory, ProgressHandle progressHandle) throws IOException, VersionNotSupportedException {
		Project aliceProject = IoUtilities.readProject(aliceProjectFile);
		JavaCodeGenerator.Builder javaCodeGeneratorBuilder = JavaCodeUtilities.createJavaCodeGeneratorBuilder();
		//JavaCodeGenerator.Builder javaCodeGeneratorBuilder = new JavaCodeGenerator.Builder().isLambdaSupported(true);

		List<FileObject> filesToOpen = Lists.newLinkedList();
		List<FileObject> fileObjectsToFormat = Lists.newLinkedList();
		java.util.Set<org.lgna.project.ast.NamedUserType> namedUserTypes = aliceProject.getNamedUserTypes();
		final java.util.Set<org.lgna.common.Resource> resources = aliceProject.getResources();
		if (resources.isEmpty()) {
			//pass
		} else {
			ResourcesTypeWrapper resourcesTypeWrapper = new ResourcesTypeWrapper(aliceProject);
			namedUserTypes.add( resourcesTypeWrapper.getType() );

			FileObject javaSrcDirectoryFileObject = (FileUtil.toFileObject(javaSrcDirectory));
			FileObject resourcesDirectory = javaSrcDirectoryFileObject.createFolder("resources");
			for (org.lgna.common.Resource resource : resources) {
				final String dstPath = resource.getName();
				FileObject f;
				try {
					f = resourcesDirectory.createData(dstPath);
				} catch (Exception e) {
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

		if (progressHandle != null) {
			progressHandle.switchToDeterminate(namedUserTypes.size());
		}
		int createWorkUnit = 0;
		for (org.lgna.project.ast.NamedUserType type : namedUserTypes) {
			String path = type.getName() + ".java";
			String code = type.generateJavaCode(new NetbeansJavaCodeGenerator(javaCodeGeneratorBuilder));
			File file = new File(javaSrcDirectory, path);
			boolean isMarkedForOpen = false;
			if (type.isAssignableTo(SProgram.class)) {
				//pass
			} else {
				if (type.isAssignableTo(SScene.class)) {
					isMarkedForOpen = true;
				} else {
					for (UserMethod method : type.methods) {
						if (method.managementLevel.getValue() == ManagementLevel.NONE) {
							isMarkedForOpen = true;
							break;
						}
					}
				}
			}

			TextFileUtilities.write(file, code);
			FileObject fileObject = FileUtil.toFileObject(file);
			fileObjectsToFormat.add(fileObject);

			if (isMarkedForOpen) {
				filesToOpen.add(fileObject);
			}
			progress(progressHandle, "create: ", fileObject, createWorkUnit);
			createWorkUnit++;
		}

		if (progressHandle != null) {
			progressHandle.switchToDeterminate(fileObjectsToFormat.size());
		}
		int formatWorkUnit = 0;
		for (FileObject fileObjectToFormat : fileObjectsToFormat) {
			try {
				DataObject dobj = DataObject.find(fileObjectToFormat);
				EditorCookie ec = dobj.getCookie(EditorCookie.class);
				final StyledDocument doc = ec.openDocument();
				final Reformat rf = Reformat.get(doc);
				rf.lock();
				try {
					NbDocument.runAtomicAsUser(doc, new Runnable() {
						@Override
						public void run() {
							try {
								rf.reformat(0, doc.getLength());
							} catch (BadLocationException ble) {
								Logger.throwable(ble);
							}
						}
					});
				} finally {
					rf.unlock();
				}
				ec.saveDocument();
				progress(progressHandle, "format: ", fileObjectToFormat, formatWorkUnit);
			} catch (BadLocationException ble) {
				Logger.throwable(ble);
			} catch (IOException ioe) {
				Logger.throwable(ioe);
			}
			formatWorkUnit++;
		}
		return filesToOpen;
	}
}
