package jjkpp.jdt.ui.classes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jjkpp.jdt.core.classes.NullibilityAnnos;
import jjkpp.jdt.core.classes.ProposalCollector;
import jjkpp.jdt.ui.classes.quickfix.NullibilityCodeCleanUp;
import jjkpp.jdt.ui.classes.quickfix.NullibilityCodeFix;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.internal.corext.fix.IProposableFix;
import org.eclipse.jdt.internal.ui.JavaPluginImages;
import org.eclipse.jdt.internal.ui.text.correction.ASTResolving;
import org.eclipse.jdt.internal.ui.text.correction.proposals.ASTRewriteCorrectionProposal;
import org.eclipse.jdt.internal.ui.text.correction.proposals.FixCorrectionProposal;
import org.eclipse.jdt.ui.cleanup.ICleanUp;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.swt.graphics.Image;

@SuppressWarnings("restriction")
public class NullibilityAnnosUI extends ProposalCollector {

	private static final boolean ENABLE_QUICKFIX = false;

	public static void getNullibilityProposals(IInvocationContext context, IProblemLocation problem, boolean isOnlyParameterMismatch, Collection<IJavaCompletionProposal> _proposals) throws CoreException {

		NullibilityAnnosUI nullibilityAnnosUI = new NullibilityAnnosUI();

		nullibilityAnnosUI.fetchProposalStructures(context.getCompilationUnit(), context.getASTRoot(), problem);
		List<NullibilityProposalStructure> proposals = nullibilityAnnosUI.proposals;

		if (proposals.isEmpty())
			return;

		if (ENABLE_QUICKFIX) {
			IProposableFix addMethodFix = NullibilityCodeFix.createMakeTypeAbstractFix(context.getASTRoot(), problem, proposals);
			if (addMethodFix != null) {
				Image image = JavaPluginImages.get(JavaPluginImages.IMG_CORRECTION_CHANGE);
				ICleanUp cleanUp = new NullibilityCodeCleanUp();
				_proposals.add(new FixCorrectionProposal(addMethodFix, cleanUp, 10, image, context));
				return;
			}
		}

		for (NullibilityProposalStructure proposal : proposals) {

			AST ast = proposal.getAst();
			ASTRewrite rewrite = ASTRewrite.create(ast);

			Annotation annot = ast.newMarkerAnnotation();
			annot.setTypeName(ast.newName(proposal.marker)); //$NON-NLS-1$
			rewrite.getListRewrite(proposal.decl, proposal.modifiers).insertFirst(annot, null);

			Image image = JavaPluginImages.get(JavaPluginImages.IMG_CORRECTION_CHANGE);
			ASTRewriteCorrectionProposal _proposal = new ASTRewriteCorrectionProposal(proposal.name, proposal.cu, rewrite, proposal.relevance, image);
			_proposals.add(_proposal);

			ImportRewrite imports = _proposal.createImportRewrite(proposal.getRoot());
			imports.addImport(proposal.getImport());
		}

	}

	public List<NullibilityProposalStructure> proposals = new ArrayList<NullibilityProposalStructure>();

	public void fetchProposalStructures(ICompilationUnit cu, CompilationUnit astRoot, ASTNode selectedNode) throws CoreException {

		annotate(cu, astRoot, selectedNode, "NonNull");

		ASTNode invocationNode = selectedNode.getParent();
		if (invocationNode instanceof VariableDeclarationFragment) {
			VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) invocationNode;
			annotate(cu, astRoot, variableDeclarationFragment, "CanBeNull");
		} else if (invocationNode instanceof Assignment) {
			Assignment assignment = (Assignment) invocationNode;
			annotate(cu, astRoot, assignment.getLeftHandSide(), "CanBeNull");
		} else if (invocationNode instanceof MethodInvocation) {
			MethodInvocation methodImpl = (MethodInvocation) invocationNode;
			IMethodBinding binding = methodImpl.resolveMethodBinding();
			int param = methodImpl.arguments().indexOf(selectedNode);
			if (param >= 0)
				addNullibilityProposals(cu, astRoot, binding, binding.getDeclaringClass(), param, invocationNode, "CanBeNull");
		} else if (invocationNode instanceof ClassInstanceCreation) {
			ClassInstanceCreation methodImpl = (ClassInstanceCreation) invocationNode;
			IMethodBinding binding = methodImpl.resolveConstructorBinding();
			if (binding.getDeclaringClass().isAnonymous()) {
				// use super constructor instead of its anonymous duplicate
				ITypeBinding superclass = binding.getDeclaringClass().getSuperclass();
				if (superclass != null) {
					for (IMethodBinding method : superclass.getDeclaredMethods()) {
						if (method.isConstructor()) {
							if (method.isSubsignature(binding)) {
								binding = method;
								break;
							}
						}
					}
				}
			}
			int param = methodImpl.arguments().indexOf(selectedNode);
			if (param >= 0)
				addNullibilityProposals(cu, astRoot, binding, binding.getDeclaringClass(), param, invocationNode, "CanBeNull");
		} else if (invocationNode instanceof SuperMethodInvocation) {
			SuperMethodInvocation methodImpl = (SuperMethodInvocation) invocationNode;
			IMethodBinding binding = methodImpl.resolveMethodBinding();
			int param = methodImpl.arguments().indexOf(selectedNode);
			if (param >= 0)
				addNullibilityProposals(cu, astRoot, binding, binding.getDeclaringClass(), param, invocationNode, "CanBeNull");
		} else if (invocationNode instanceof ReturnStatement) {
			ReturnStatement returnStatement = (ReturnStatement) invocationNode;
			MethodDeclaration methodDeclaration = getMethodDeclaration(returnStatement);
			addMarker2(cu, methodDeclaration, "CanBeNull");
		}
	}

	protected void annotate(ICompilationUnit cu, CompilationUnit astRoot, ASTNode selectedNode, String marker) throws CoreException {
		if (selectedNode instanceof SimpleName) {
			SimpleName name = (SimpleName) selectedNode;
			IBinding nameBinding = name.resolveBinding();
			addNullibilityProposals2(cu, astRoot, nameBinding, marker);
		}
		if (selectedNode instanceof MethodInvocation) {
			MethodInvocation methodInvocation = (MethodInvocation) selectedNode;
			IMethodBinding methodBinding = methodInvocation.resolveMethodBinding();

			addNullibilityProposals(cu, astRoot, methodBinding, methodBinding.getDeclaringClass(), -1, selectedNode, marker);

		}
		if (selectedNode instanceof FieldAccess) {
			FieldAccess name = (FieldAccess) selectedNode;
			IVariableBinding nameBinding = name.resolveFieldBinding();
			addNullibilityProposals(cu, astRoot, nameBinding, nameBinding.getDeclaringClass(), -1, selectedNode, marker);
		}
		if (selectedNode instanceof VariableDeclarationFragment) {
			VariableDeclarationFragment name = (VariableDeclarationFragment) selectedNode;
			IVariableBinding nameBinding = name.resolveBinding();
			addNullibilityProposals(cu, astRoot, nameBinding, nameBinding.getDeclaringClass(), -1, selectedNode, marker);
		}
		if (selectedNode instanceof QualifiedName) {
			QualifiedName name = (QualifiedName) selectedNode;
			IBinding nameBinding = name.resolveBinding();
			if (nameBinding instanceof IVariableBinding) {
				IVariableBinding varBinding = (IVariableBinding) nameBinding;
				addNullibilityProposals(cu, astRoot, varBinding, varBinding.getDeclaringClass(), -1, selectedNode, marker);
			}
		}
	}

	protected MethodDeclaration getMethodDeclaration(ASTNode node) {
		while (node != null) {
			if (node instanceof MethodDeclaration) {
				return (MethodDeclaration) node;
			} else {
				node = node.getParent();
			}
		}
		return null;
	}

	protected void addNullibilityProposals2(ICompilationUnit cu, CompilationUnit astRoot, IBinding binding, String marker) throws CoreException {
		ASTNode node = astRoot.findDeclaringNode(binding);
		if (node != null)
			addMarker2(cu, node, marker);

	}

	public void fetchProposalStructures(ICompilationUnit cu, CompilationUnit astRoot, IProblemLocation problem) throws CoreException {
		fetchProposalStructures(cu, astRoot, problem.getCoveringNode(astRoot));
	}

	protected void addNullibilityProposals(ICompilationUnit cu, CompilationUnit astRoot, IBinding binding, ITypeBinding declaringClass, int param, ASTNode invocationNode, String marker) throws CoreException {
		if (declaringClass == null)
			return;
		ITypeBinding declaringTypeDecl = declaringClass.getTypeDeclaration();
		if (declaringTypeDecl != null && declaringTypeDecl.isFromSource()) {
			ICompilationUnit targetCU = ASTResolving.findCompilationUnitForBinding(cu, astRoot, declaringTypeDecl);
			if (targetCU != null) {

				ASTNode newDecl = astRoot.findDeclaringNode(binding);
				if (newDecl == null) {
					astRoot = ASTResolving.createQuickFixAST(targetCU, null);
					newDecl = astRoot.findDeclaringNode(binding.getKey());
				}

				if (newDecl != null)
					if (param >= 0) {
						MethodDeclaration mdecl = (MethodDeclaration) newDecl;
						List params = mdecl.parameters();
						if (params != null && param < params.size()) {
							addMarker2(targetCU, (VariableDeclaration) params.get(param), marker);
						}
					} else {
						addMarker2(targetCU, newDecl, marker);
					}
			}
		}

	}

	protected void addMarker2(ICompilationUnit cu, ASTNode variableDeclaration, String marker) {
		if (variableDeclaration == null)
			return;

		String oppositeMarker = marker.equals("NonNull") ? "CanBeNull" : "NonNull";
		String varType = "variable";
		ChildListPropertyDescriptor modifiers;
		ASTNode decl;
		IAnnotationBinding[] existingAnnots = null;
		if (variableDeclaration.getParent() instanceof VariableDeclarationStatement && variableDeclaration instanceof VariableDeclaration) {
			VariableDeclaration varDecl = (VariableDeclaration) variableDeclaration;
			decl = variableDeclaration.getParent();
			modifiers = VariableDeclarationStatement.MODIFIERS2_PROPERTY;
			varType = "variable " + varDecl.getName().getIdentifier();
			existingAnnots = varDecl.resolveBinding().getAnnotations();
		} else if (variableDeclaration.getParent() instanceof SingleVariableDeclaration && variableDeclaration instanceof VariableDeclaration) {
			VariableDeclaration varDecl = (VariableDeclaration) variableDeclaration;
			decl = (SingleVariableDeclaration) variableDeclaration.getParent();
			modifiers = VariableDeclarationStatement.MODIFIERS2_PROPERTY;
			varType = "variable " + varDecl.getName().getIdentifier();
			existingAnnots = varDecl.resolveBinding().getAnnotations();
		} else if (variableDeclaration.getParent() instanceof FieldDeclaration && variableDeclaration instanceof VariableDeclaration) {
			VariableDeclaration varDecl = (VariableDeclaration) variableDeclaration;
			decl = (FieldDeclaration) variableDeclaration.getParent();
			modifiers = FieldDeclaration.MODIFIERS2_PROPERTY;
			varType = "field " + varDecl.getName().getIdentifier();
			existingAnnots = varDecl.resolveBinding().getAnnotations();
		} else if (variableDeclaration.getParent() instanceof MethodDeclaration && variableDeclaration instanceof SingleVariableDeclaration) {
			SingleVariableDeclaration varDecl = (SingleVariableDeclaration) variableDeclaration;
			decl = (MethodDeclaration) variableDeclaration.getParent();
			MethodDeclaration mdecl = (MethodDeclaration) variableDeclaration.getParent();
			modifiers = mdecl.getModifiersProperty();
			int param = mdecl.parameters().indexOf(varDecl);
			String suffix = "Param" + (param + 1);
			existingAnnots = mdecl.resolveBinding().getAnnotations();
			marker = marker + suffix;
			oppositeMarker = oppositeMarker + suffix;
			varType = "parameter " + mdecl.getName().getIdentifier() + "(" + varDecl.getName().getIdentifier() + ")";
		} else if (variableDeclaration instanceof MethodDeclaration) {
			MethodDeclaration mdecl = (MethodDeclaration) variableDeclaration;
			decl = mdecl;
			modifiers = mdecl.getModifiersProperty();
			varType = "method " + mdecl.getName().getIdentifier();
			existingAnnots = mdecl.resolveBinding().getAnnotations();
		} else {
			return;
		}

		if (existingAnnots != null) {
			if (NullibilityAnnos.hasSolidAnnotation(existingAnnots, oppositeMarker)) {
				return;
			}
			if (NullibilityAnnos.hasSolidAnnotation(existingAnnots, marker)) {
				return;
			}
		}

		// AST ast = decl.getAST();
		// ASTRewrite rewrite = ASTRewrite.create(ast);
		//
		// Annotation annot = ast.newMarkerAnnotation();
		//		annot.setTypeName(ast.newName(marker)); //$NON-NLS-1$
		// rewrite.getListRewrite(decl, modifiers).insertFirst(annot, null);

		String label = "Mark " + varType + " " + " as " + marker;
		// Image image = JavaPluginImages
		// .get(JavaPluginImages.IMG_CORRECTION_CHANGE);
		NullibilityProposalStructure proposal = new NullibilityProposalStructure(label, cu, decl, 15, marker, modifiers);
		// ASTRewriteCorrectionProposal proposal = new
		// ASTRewriteCorrectionProposal(
		// label, cu, rewrite, 15, image);
		proposals.add(proposal);

		// ImportRewrite imports = proposal
		// .createImportRewrite((CompilationUnit) decl.getRoot());
		// imports.addImport("jjkpp.jdt.annotations." + marker);
	}

}
