package jjkpp.jdt.ui.classes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
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
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;
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

import pingpong.jdt.core.classes.NPContext;
import pingpong.jdt.core.classes.NullibilityAnnos;
import pingpong.jdt.core.classes.ProposalCollector;
import pingpong.jdt.ui.classes.quickfix.NullibilityCodeCleanUp;
import pingpong.jdt.ui.classes.quickfix.NullibilityCodeFix;

@SuppressWarnings("restriction")
public class NullibilityAnnosUI extends ProposalCollector {

	private static final boolean ENABLE_QUICKFIX = false;

	public static void getNullibilityProposals(IInvocationContext context, IProblemLocation problem, boolean isOnlyParameterMismatch, Collection<IJavaCompletionProposal> _proposals) throws CoreException {

		NullibilityAnnosUI nullibilityAnnosUI = new NullibilityAnnosUI();

		NPContext npContext = new NPContext(context.getCompilationUnit(), context.getASTRoot());
		nullibilityAnnosUI.fetchProposalStructures(npContext, problem);
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

	public void fetchProposalStructures(NPContext context, ASTNode selectedNode) throws CoreException {

		annotate(context.duplicate(), selectedNode, "NonNull");

		ASTNode invocationNode = selectedNode.getParent();
		if (invocationNode instanceof VariableDeclarationFragment) {
			VariableDeclarationFragment variableDeclarationFragment = (VariableDeclarationFragment) invocationNode;
			annotate(context, variableDeclarationFragment, "CanBeNull");
		} else if (invocationNode instanceof Assignment) {
			Assignment assignment = (Assignment) invocationNode;
			annotate(context, assignment.getLeftHandSide(), "CanBeNull");
		} else if (invocationNode instanceof MethodInvocation) {
			MethodInvocation methodImpl = (MethodInvocation) invocationNode;
			IMethodBinding binding = methodImpl.resolveMethodBinding();
			int param = methodImpl.arguments().indexOf(selectedNode);
			if (param >= 0)
				addNullibilityProposals(context, binding, binding.getDeclaringClass(), param, invocationNode, "CanBeNull");
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
				addNullibilityProposals(context, binding, binding.getDeclaringClass(), param, invocationNode, "CanBeNull");
		} else if (invocationNode instanceof SuperMethodInvocation) {
			SuperMethodInvocation methodImpl = (SuperMethodInvocation) invocationNode;
			IMethodBinding binding = methodImpl.resolveMethodBinding();
			int param = methodImpl.arguments().indexOf(selectedNode);
			if (param >= 0)
				addNullibilityProposals(context, binding, binding.getDeclaringClass(), param, invocationNode, "CanBeNull");
		} else if (invocationNode instanceof SuperConstructorInvocation) {
			SuperConstructorInvocation methodImpl = (SuperConstructorInvocation) invocationNode;
			IMethodBinding binding = methodImpl.resolveConstructorBinding();
			int param = methodImpl.arguments().indexOf(selectedNode);
			if (param >= 0)
				addNullibilityProposals(context, binding, binding.getDeclaringClass(), param, invocationNode, "CanBeNull");
		} else if (invocationNode instanceof ReturnStatement) {
			ReturnStatement returnStatement = (ReturnStatement) invocationNode;
			MethodDeclaration methodDeclaration = getMethodDeclaration(returnStatement);
			addMarker2(context, methodDeclaration, "CanBeNull");
		}
	}

	protected void annotate(NPContext context, ASTNode selectedNode, String marker) throws CoreException {
		if (selectedNode instanceof SimpleName) {
			SimpleName name = (SimpleName) selectedNode;
			IBinding nameBinding = name.resolveBinding();
			addNullibilityProposals2(context, nameBinding, marker);
		}
		if (selectedNode instanceof MethodInvocation) {
			MethodInvocation methodInvocation = (MethodInvocation) selectedNode;
			IMethodBinding methodBinding = methodInvocation.resolveMethodBinding();

			addNullibilityProposals(context, methodBinding, methodBinding.getDeclaringClass(), -1, selectedNode, marker);

		}
		if (selectedNode instanceof FieldAccess) {
			FieldAccess name = (FieldAccess) selectedNode;
			IVariableBinding nameBinding = name.resolveFieldBinding();
			addNullibilityProposals(context, nameBinding, nameBinding.getDeclaringClass(), -1, selectedNode, marker);
		}
		if (selectedNode instanceof VariableDeclarationFragment) {
			VariableDeclarationFragment name = (VariableDeclarationFragment) selectedNode;
			IVariableBinding nameBinding = name.resolveBinding();
			addNullibilityProposals(context, nameBinding, nameBinding.getDeclaringClass(), -1, selectedNode, marker);
		}
		if (selectedNode instanceof QualifiedName) {
			QualifiedName name = (QualifiedName) selectedNode;
			IBinding nameBinding = name.resolveBinding();
			if (nameBinding instanceof IVariableBinding) {
				IVariableBinding varBinding = (IVariableBinding) nameBinding;
				addNullibilityProposals(context, varBinding, varBinding.getDeclaringClass(), -1, selectedNode, marker);
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

	protected void addNullibilityProposals2(NPContext context, IBinding binding, String marker) throws CoreException {
		ASTNode node = context.astRoot.findDeclaringNode(binding);
		if (node != null)
			addMarker2(context, node, marker);

	}

	public void fetchProposalStructures(NPContext context, IProblemLocation problem) throws CoreException {
		fetchProposalStructures(context, problem.getCoveringNode(context.astRoot));
	}

	/**
	 * @param context
	 *            if this method returns <code>null</code>, this object is
	 *            unspecified
	 * @param binding
	 * @param declaringClass
	 *            where the binding is declared
	 * @return the declaring node of the given binding, or <code>null</code>
	 * @throws JavaModelException
	 */
	private ASTNode findDeclaringNode(NPContext context, IBinding binding, ITypeBinding declaringClass) throws JavaModelException {
		if (declaringClass == null)
			return null;
		ITypeBinding declaringTypeDecl = declaringClass.getTypeDeclaration();
		if (declaringTypeDecl != null && declaringTypeDecl.isFromSource()) {
			context.cu = ASTResolving.findCompilationUnitForBinding(context.cu, context.astRoot, declaringTypeDecl);
			if (context.cu != null) {
				if (binding instanceof IMethodBinding) {
					// testNotAnnotatedParametrizedParameter()
					IMethodBinding methodBinding = (IMethodBinding) binding;
					methodBinding = methodBinding.getMethodDeclaration();
					binding = methodBinding;
				}
				ASTNode newDecl = context.astRoot.findDeclaringNode(binding);
				if (newDecl == null) {
					context.astRoot = ASTResolving.createQuickFixAST(context.cu, null);
					newDecl = context.astRoot.findDeclaringNode(binding.getKey());
				}
				return newDecl;
			}
		}
		return null;
	}

	@Override
	protected void addNullibilityProposals(NPContext context, IBinding binding, ITypeBinding declaringClass, int param, ASTNode invocationNode, String marker) throws CoreException {
		ASTNode newDecl = findDeclaringNode(context, binding, declaringClass);
		if (newDecl != null) {
			if (param >= 0) {
				MethodDeclaration mdecl = (MethodDeclaration) newDecl;
				List<?> params = mdecl.parameters();
				if (param < params.size()) {
					addMarker2(context, (VariableDeclaration) params.get(param), marker);
				}
			} else {
				addMarker2(context, newDecl, marker);
			}
		}
	}

	@Override
	protected void addMarker2(NPContext context, ASTNode variableDeclaration, String marker) throws JavaModelException {
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
			MethodDeclaration mdecl = (MethodDeclaration) variableDeclaration.getParent();
			int param = mdecl.parameters().indexOf(varDecl);
			if (param == -1) {
				return;
			}
			SimpleName paramName = varDecl.getName();
			MethodDeclaration betterMethod = findBetterMethod(context, mdecl, param);
			if (betterMethod != null) {
				mdecl = betterMethod;
				if (param >= mdecl.parameters().size()) {
					return;
				}
				Object superParam = mdecl.parameters().get(param);
				if (!(superParam instanceof SingleVariableDeclaration)) {
					return;
				}
				varDecl = (SingleVariableDeclaration) superParam;
			}
			if (NullibilityAnnos.USE_PARAM_ANNOS) {
				decl = varDecl;
				modifiers = SingleVariableDeclaration.MODIFIERS2_PROPERTY;
				varType = "parameter " + paramName.getIdentifier();
				existingAnnots = varDecl.resolveBinding().getAnnotations();
			} else {
				decl = (MethodDeclaration) varDecl.getParent();
				modifiers = mdecl.getModifiersProperty();
				String suffix = "Param" + (param + 1);
				existingAnnots = mdecl.resolveBinding().getAnnotations();
				marker = marker + suffix;
				oppositeMarker = oppositeMarker + suffix;
				varType = "parameter " + mdecl.getName().getIdentifier() + "(" + paramName.getIdentifier() + ")";
			}
		} else if (variableDeclaration instanceof MethodDeclaration) {
			MethodDeclaration mdecl = (MethodDeclaration) variableDeclaration;
			String methodName = mdecl.getName().getIdentifier();
			MethodDeclaration betterMethod = findBetterMethod(context, mdecl, -1);
			if (betterMethod != null) {
				mdecl = betterMethod;
				methodName = betterMethod.resolveBinding().getDeclaringClass().getName() + "." + methodName;
			}
			decl = mdecl;
			modifiers = mdecl.getModifiersProperty();
			varType = "method " + methodName;
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
		NullibilityProposalStructure proposal = new NullibilityProposalStructure(label, context.cu, decl, 15, marker, modifiers);
		// ASTRewriteCorrectionProposal proposal = new
		// ASTRewriteCorrectionProposal(
		// label, cu, rewrite, 15, image);
		proposals.add(proposal);

		// ImportRewrite imports = proposal
		// .createImportRewrite((CompilationUnit) decl.getRoot());
		// imports.addImport("jjkpp.jdt.annotations." + marker);
	}

	private MethodDeclaration findBetterMethod(NPContext context, MethodDeclaration mdecl, int param) throws JavaModelException {
		IMethodBinding method = mdecl.resolveBinding();
		if (NullibilityAnnos.hasSolidAnnotation(getBinding(method), param) == FlowInfo.UNKNOWN) {
			IMethodBinding bestMethod = method;
			do {
				IMethodBinding superMethod = getHigherOverridenMethod(bestMethod);
				if (superMethod != null && superMethod != bestMethod) {
					int result = NullibilityAnnos.hasSolidAnnotation(getBinding(superMethod), param);
					if (result == FlowInfo.UNKNOWN) {
						bestMethod = superMethod;
					} else {
						bestMethod = superMethod;
						break;
					}
				} else {
					break;
				}
			} while (true);
			if (bestMethod != null && bestMethod != method) {
				ASTNode newDecl = findDeclaringNode(context, bestMethod, bestMethod.getDeclaringClass());
				return (MethodDeclaration) newDecl;
			}
		}
		return null;
	}

	static protected MethodBinding getBinding(IMethodBinding method) {
		try {
			Field field = method.getClass().getDeclaredField("binding");
			field.setAccessible(true);
			return (MethodBinding) field.get(method);
		} catch (Exception e) {
			return null;
		}
	}

	static protected TypeBinding getBinding(ITypeBinding type) {
		try {
			Field field = type.getClass().getDeclaredField("binding");
			field.setAccessible(true);
			return (TypeBinding) field.get(type);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Return the next higher method/constructor in supertype hierarchy with
	 * same selector and arguments as
	 * {@link NullibilityAnnos#getHigherOverridenMethod(MethodBinding, org.eclipse.jdt.internal.compiler.lookup.LookupEnvironment)}
	 * , however should be called only for non-construtor methods
	 */
	static public IMethodBinding getHigherOverridenMethod(IMethodBinding t) {
		IMethodBinding bestMethod = t;
		ITypeBinding currentType = t.getDeclaringClass();
		// walk superclasses
		ITypeBinding[] interfacesToVisit = null;
		int nextPosition = 0;
		do {
			IMethodBinding[] superMethods = currentType.getDeclaredMethods();
			for (int i = 0, length = superMethods.length; i < length; i++) {
				if (t.overrides(superMethods[i])) {
					bestMethod = superMethods[i];
					break;
				}
			}
			ITypeBinding[] itsInterfaces = currentType.getInterfaces();
			if (itsInterfaces != null && itsInterfaces != Binding.NO_SUPERINTERFACES) {
				if (interfacesToVisit == null) {
					interfacesToVisit = itsInterfaces;
					nextPosition = interfacesToVisit.length;
				} else {
					int itsLength = itsInterfaces.length;
					if (nextPosition + itsLength >= interfacesToVisit.length)
						System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ITypeBinding[nextPosition + itsLength + 5], 0, nextPosition);
					nextInterface: for (int a = 0; a < itsLength; a++) {
						ITypeBinding next = itsInterfaces[a];
						for (int b = 0; b < nextPosition; b++)
							if (next == interfacesToVisit[b])
								continue nextInterface;
						interfacesToVisit[nextPosition++] = next;
					}
				}
			}
		} while ((currentType = currentType.getSuperclass()) != null && bestMethod == t);
		if (getBinding(bestMethod).declaringClass.id == TypeIds.T_JavaLangObject) {
			return bestMethod;
		}
		// walk superinterfaces
		for (int i = 0; i < nextPosition; i++) {
			currentType = interfacesToVisit[i];
			IMethodBinding[] superMethods = currentType.getDeclaredMethods();
			for (int j = 0, length = superMethods.length; j < length; j++) {
				IMethodBinding superMethod = superMethods[j];
				if (t.overrides(superMethod)) {
					ITypeBinding bestReturnType = bestMethod.getReturnType();
					if (bestReturnType == superMethod.getReturnType() || getBinding(bestMethod.getReturnType()).findSuperTypeOriginatingFrom(getBinding(superMethod.getReturnType())) != null) {
						bestMethod = superMethod;
					}
					break;
				}
			}
			ITypeBinding[] itsInterfaces = currentType.getInterfaces();
			if (itsInterfaces != null && itsInterfaces != Binding.NO_SUPERINTERFACES) {
				int itsLength = itsInterfaces.length;
				if (nextPosition + itsLength >= interfacesToVisit.length)
					System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ITypeBinding[nextPosition + itsLength + 5], 0, nextPosition);
				nextInterface: for (int a = 0; a < itsLength; a++) {
					ITypeBinding next = itsInterfaces[a];
					for (int b = 0; b < nextPosition; b++)
						if (next == interfacesToVisit[b])
							continue nextInterface;
					interfacesToVisit[nextPosition++] = next;
				}
			}
		}
		return bestMethod;
	}

}
