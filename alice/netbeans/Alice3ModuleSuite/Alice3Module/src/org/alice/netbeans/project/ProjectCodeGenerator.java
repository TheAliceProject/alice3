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
import java.util.List;
import java.util.logging.Level;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import org.lgna.project.Project;
import org.lgna.project.VersionNotSupportedException;
import org.lgna.project.ast.JavaCodeGenerator;
import org.lgna.project.code.CodeConverter;
import org.lgna.project.code.PathCodePair;
import org.lgna.project.io.IoUtilities;
import org.netbeans.modules.editor.indent.api.Reformat;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.text.NbDocument;

/**
 *
 * @author Dennis Cosgrove
 */
public class ProjectCodeGenerator {
	public static void generateCode( File aliceProjectFile, File javaSrcDirectory ) throws IOException, VersionNotSupportedException {
		//FileUtil.toFileObject( javaSrcDirectory );
		
		Project aliceProject = IoUtilities.readProject(aliceProjectFile);
		JavaCodeGenerator.Builder javaCodeGeneratorBuilder = new JavaCodeGenerator.Builder().isLambdaSupported(true);
		CodeConverter codeConverter = new CodeConverter(javaCodeGeneratorBuilder);
		Iterable<PathCodePair> pathCodePairs = codeConverter.convert(aliceProject);
		
		List<File> filesToFormat = Lists.newLinkedList();
		for( PathCodePair pathCodePair : pathCodePairs ) {
			File file = new File( javaSrcDirectory, pathCodePair.getPath() );
			TextFileUtilities.write(file, pathCodePair.getCode());
			
			filesToFormat.add( file );
			//Logger.getInstance().log(Level.SEVERE, file.getAbsolutePath());
			//FileObject fileObject = javaSrcDirectoryObject.createData(null)
		}
		
		for( File fileToFormat : filesToFormat ) {
           try {
                FileObject fo = FileUtil.toFileObject(fileToFormat);
                DataObject dobj = DataObject.find(fo);
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
	}
}
