/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package pingpong.jdt.ui.classes.quickfix;

import java.util.ArrayList;
import java.util.List;

import jjkpp.jdt.ui.classes.NullibilityAnnosUI;
import jjkpp.jdt.ui.classes.NullibilityProposalStructure;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.internal.corext.fix.CompilationUnitRewriteOperationsFix;
import org.eclipse.jdt.internal.corext.fix.LinkedProposalModel;
import org.eclipse.jdt.internal.corext.refactoring.structure.CompilationUnitRewrite;
import org.eclipse.jdt.internal.ui.text.correction.CorrectionMessages;
import org.eclipse.jdt.ui.cleanup.ICleanUpFix;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.text.edits.TextEditGroup;

import pingpong.jdt.core.classes.NPContext;

@SuppressWarnings("restriction")
public class NullibilityCodeFix extends CompilationUnitRewriteOperationsFix {

	public static final class MakeTypeAbstractOperation extends CompilationUnitRewriteOperation {

		public final NullibilityProposalStructure proposal;

		public MakeTypeAbstractOperation(NullibilityProposalStructure proposal) {
			this.proposal = proposal;
		}

		/**
		 * {@inheritDoc}
		 */
		public void rewriteAST(CompilationUnitRewrite cuRewrite, LinkedProposalModel linkedProposalPositions) throws CoreException {

			AST ast = cuRewrite.getAST();
			ASTRewrite rewrite = cuRewrite.getASTRewrite();
			// AST ast = proposal.getAst();
			// ASTRewrite rewrite = ASTRewrite.create(ast);

			Annotation annot = ast.newMarkerAnnotation();
			annot.setTypeName(ast.newName(proposal.marker));
			TextEditGroup textEditGroup = createTextEditGroup(CorrectionMessages.UnimplementedCodeFix_TextEditGroup_label, cuRewrite);
			rewrite.getListRewrite(proposal.decl, proposal.modifiers).insertFirst(annot, textEditGroup);

			ImportRewrite imports = cuRewrite.getImportRewrite();
			imports.addImport(proposal.getImport());

		}
	}

	public static NullibilityCodeFix createMakeTypeAbstractFix(CompilationUnit root, IProblemLocation problem, List<NullibilityProposalStructure> proposals) {

		for (int i = 0; i < proposals.size();) {
			NullibilityProposalStructure proposal = (NullibilityProposalStructure) proposals.get(i);

			MakeTypeAbstractOperation operation = new MakeTypeAbstractOperation(proposal);

			return new NullibilityCodeFix(proposal.name, proposal.getRoot(), new CompilationUnitRewriteOperation[] { operation });
		}
		return null;
	}

	public NullibilityCodeFix(String name, CompilationUnit compilationUnit, CompilationUnitRewriteOperation[] fixRewriteOperations) {
		super(name, compilationUnit, fixRewriteOperations);
	}

	public static ICleanUpFix createCleanUp(CompilationUnit root, IProblemLocation[] problems) throws CoreException {

		if (problems.length == 0)
			return null;

		ArrayList<MakeTypeAbstractOperation> operations = new ArrayList<MakeTypeAbstractOperation>();

		for (IProblemLocation problem : problems) {
			NullibilityAnnosUI nullibilityAnnosUI = new NullibilityAnnosUI();
			nullibilityAnnosUI.fetchProposalStructures(new NPContext((ICompilationUnit) root.getJavaElement(), root), problem);
			if (!nullibilityAnnosUI.proposals.isEmpty()) {
				NullibilityProposalStructure proposal = (NullibilityProposalStructure) nullibilityAnnosUI.proposals.iterator().next();
				if (proposal.getRoot() != root)
					continue;
				for (MakeTypeAbstractOperation operation : operations) {
					if (operation.proposal.decl == proposal.decl) {
						if (operation.proposal.marker.equals(proposal.marker)) {
							proposal = null;
							break;
						} else {
						}
					}
				}
				if (proposal != null) {
					MakeTypeAbstractOperation operation = new MakeTypeAbstractOperation(proposal);
					operations.add(operation);
				}
			}
		}

		if (operations.size() == 0)
			return null;

		String label = "Solve Nullibility";
		return new NullibilityCodeFix(label, root, (CompilationUnitRewriteOperation[]) operations.toArray(new CompilationUnitRewriteOperation[operations.size()]));
	}
}
