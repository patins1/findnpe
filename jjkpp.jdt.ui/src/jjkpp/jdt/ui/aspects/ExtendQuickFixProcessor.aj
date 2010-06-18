package jjkpp.jdt.ui.aspects;


import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.lookup.VariableBinding;
import org.eclipse.jdt.internal.ui.text.correction.QuickFixProcessor;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import java.util.Collection;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;

import pingpong.jdt.core.classes.NullibilityAnnos;
import pingpong.jdt.ui.classes.NullibilityAnnosUI;

@SuppressWarnings("restriction")
public aspect ExtendQuickFixProcessor {

	boolean around(QuickFixProcessor t, ICompilationUnit cu, int problemId) : 
		call(boolean hasCorrections(ICompilationUnit, int)) && args(cu,problemId) && target(t) {

		if (problemId==NullibilityAnnos.RequireCanBeNull) {
			return true;
		}
		return proceed(t,cu,problemId);
	}

	void around(QuickFixProcessor t, IInvocationContext context, IProblemLocation problem, Collection proposals) throws CoreException : 
		call(void process(IInvocationContext, IProblemLocation, Collection)) && args(context,problem,proposals) && target(t) {

		if (problem.getProblemId()==NullibilityAnnos.RequireCanBeNull) {
			NullibilityAnnosUI.getNullibilityProposals(context, problem, true, proposals);
		}
		proceed(t,context,problem,proposals);
	}
}
  