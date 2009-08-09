package jjkpp.jdt.core.classes;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.internal.compiler.flow.FlowContext;
import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;

public class ProposalCollector {

	private boolean canFind = false;
	
//	static public boolean hasProposals(org.eclipse.jdt.internal.compiler.ast.Expression t, BlockScope scope, FlowContext flowContext, FlowInfo flowInfo) {
//		return true;
//		ProposalCollector pc = new ProposalCollector();
//		pc.fetchProposalStructures(null,null,t);
//		return pc.canFind;		
//	}

	protected void addNullibilityProposals(ICompilationUnit cu,
			CompilationUnit astRoot, IBinding binding,
			ITypeBinding declaringTypeDecl, int param, ASTNode invocationNode,
			 String marker) throws CoreException {

		if (declaringTypeDecl.isFromSource()) {
			canFind=true;
		}

	}

	protected void addMarker2(ICompilationUnit cu,
			ASTNode variableDeclaration, String marker) {
		canFind=true;
	}

}
