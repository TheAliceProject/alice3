package org.alice.netbeans.aliceprojectwizard.alicecomponentpalette.items;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ImportTree;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import org.netbeans.api.java.source.JavaSource;
import org.netbeans.api.java.source.Task;
import org.netbeans.api.java.source.TreeMaker;
import org.netbeans.api.java.source.WorkingCopy;
import org.openide.text.IndentEngine;
import org.openide.util.Exceptions;

public class AliceComponentPaletteUtilities {

	public static void insert(final String s, final String[] imports, final JTextComponent target) throws BadLocationException {
		JavaSource src = JavaSource.forDocument(target.getDocument());
		Task<WorkingCopy> task = new Task<WorkingCopy>() {

			@Override
			public void run(WorkingCopy workingCopy) throws IOException, BadLocationException {
				workingCopy.toPhase(JavaSource.Phase.RESOLVED);
				insertFormated(s, target, workingCopy.getDocument());
				if (imports != null) {
					addImportsAsNeeded(workingCopy, imports);
				}
			}
		};
		try {
			src.runModificationTask(task).commit();
		} catch (IOException ex) {
			Exceptions.printStackTrace(ex);
		}
	}

//    public static void insert() throws BadLocationException {
//
//        final StyledDocument doc = (StyledDocument) target.getDocument();
//        if (doc == null) {
//            return;
//        }
//
//        class InsertFormatedText implements Runnable {
//            public void run() {
//                try {
//                    if (imports != null)
//                    {
//                        addImportsAsNeeded(target, imports);
//                    }
//                    insertFormated(s, target, doc);
//                } catch (BadLocationException ex) {
//                    Exceptions.printStackTrace(ex);
//                }
//            }
//        }
//        InsertFormatedText insert = new InsertFormatedText();
//
//        //This starts the run() in the Runnable above:
//        NbDocument.runAtomicAsUser(doc, insert);
//
//    }
	private static int insertFormated(String s, JTextComponent target, Document doc) throws BadLocationException {

		int start = -1;

		try {

			//Find the location in the editor,
			//and if it is a selection, remove it,
			//to be replaced by the dropped item:
			Caret caret = target.getCaret();
			int p0 = Math.min(caret.getDot(), caret.getMark());
			int p1 = Math.max(caret.getDot(), caret.getMark());
			doc.remove(p0, p1 - p0);

			start = caret.getDot();

			//Insert the string in the document,
			//using the indentation engine
			//to create the correct indentation:
			IndentEngine engine = IndentEngine.find(doc);
			StringWriter textWriter = new StringWriter();
			Writer indentWriter = engine.createWriter(doc, start, textWriter);
			indentWriter.write(s);
			indentWriter.close();
			doc.insertString(start, textWriter.toString(), null);

		} catch (IOException ex) {
			Exceptions.printStackTrace(ex);
		} catch (BadLocationException ble) {
			Exceptions.printStackTrace(ble);
		}

		return start;
	}

	protected static boolean isImportRedundant(String existingImport, String newImport) {
		if (existingImport.equals(newImport)) {
			return true;
		}
		String[] splitExisting = existingImport.split("\\.");
		String[] splitNew = newImport.split("\\.");
		if (splitExisting.length == splitNew.length) {
			if (splitExisting[splitExisting.length - 1].startsWith("*")) {
				boolean matches = true;
				for (int i = 0; i < splitExisting.length - 1; i++) {
					if (!splitExisting[i].equals(splitNew[i])) {
						matches = false;
						break;
					}
				}
				return matches;
			}
		}
		return false;
	}

	public static void addImportsAsNeeded(WorkingCopy workingCopy, final String[] importStrings) {
		TreeMaker make = workingCopy.getTreeMaker();
		CompilationUnitTree cut = workingCopy.getCompilationUnit();
		List<ImportTree> allImports = new ArrayList<ImportTree>(cut.getImports());

		//Make a list of ImportTree imports from the strings passed in
		List<ImportTree> newImports = new ArrayList<ImportTree>();
		for (String importString : importStrings) {
			String trueImportString = importString;
			boolean isStatic = false;
			if (importString.startsWith("static")) {
				trueImportString = importString.substring(7).trim();
				isStatic = true;
			}
			newImports.add(make.Import(make.Identifier(trueImportString), isStatic));
		}

		//Check the imports for duplicates in the existing import list
		List<ImportTree> importsToAdd = new ArrayList<ImportTree>();
		for (ImportTree newImport : newImports) {
			boolean hasIt = false;
			for (ImportTree oldImport : allImports) {
				if (isImportRedundant(oldImport.toString(), newImport.toString())) {
					hasIt = true;
					break;
				}
			}
			if (!hasIt) {
				importsToAdd.add(newImport);
			}
		}

		//Add all the none duplicates and make the change
		allImports.addAll(importsToAdd);
		CompilationUnitTree unit = make.CompilationUnit(
				cut.getPackageName(),
				allImports,
				cut.getTypeDecls(),
				cut.getSourceFile());
		workingCopy.rewrite(cut, unit);
	}
}
