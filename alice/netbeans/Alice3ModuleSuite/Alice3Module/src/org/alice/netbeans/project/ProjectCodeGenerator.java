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
import org.lgna.story.SProgram;
import org.lgna.story.SScene;
import org.netbeans.modules.editor.indent.api.Reformat;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.text.NbDocument;

/**
 * @author Dennis Cosgrove
 */
public class ProjectCodeGenerator {
	public static Collection<FileObject> generateCode(File aliceProjectFile, File javaSrcDirectory) throws IOException, VersionNotSupportedException {
		Project aliceProject = IoUtilities.readProject(aliceProjectFile);
		JavaCodeGenerator.Builder javaCodeGeneratorBuilder = new JavaCodeGenerator.Builder().isLambdaSupported(true);

		List<FileObject> filesToOpen = Lists.newLinkedList();
		List<FileObject> filesToFormat = Lists.newLinkedList();
		java.util.Set<org.lgna.project.ast.NamedUserType> set = aliceProject.getNamedUserTypes();
		for (org.lgna.project.ast.NamedUserType type : set) {
			String path = type.getName() + ".java";
			String code = type.generateJavaCode(new NetbeansJavaCodeGenerator( javaCodeGeneratorBuilder ));
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
			FileObject fo = FileUtil.toFileObject(file);
			filesToFormat.add(fo);

			if (isMarkedForOpen) {
				filesToOpen.add(fo);
			}
		}

		for (FileObject fileToFormat : filesToFormat) {
			try {
				DataObject dobj = DataObject.find(fileToFormat);
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
			} catch (BadLocationException ble) {
				Logger.throwable(ble);
			} catch (IOException ioe) {
				Logger.throwable(ioe);
			}
		}
		return filesToOpen;
	}
}
