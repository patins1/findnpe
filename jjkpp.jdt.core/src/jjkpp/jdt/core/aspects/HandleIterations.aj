package jjkpp.jdt.core.aspects;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jjkpp.jdt.core.classes.FakeFlowInfo;
import jjkpp.jdt.core.classes.NullibilityAnnos;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblem;

import org.eclipse.jdt.core.compiler.CategorizedProblem;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ast.DoStatement;
import org.eclipse.jdt.internal.compiler.ast.ForStatement;
import org.eclipse.jdt.internal.compiler.ast.ForeachStatement;
import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.TryStatement;
import org.eclipse.jdt.internal.compiler.ast.WhileStatement;
import org.eclipse.jdt.internal.compiler.flow.FlowContext;
import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.flow.LoopingFlowContext;
import org.eclipse.jdt.internal.compiler.flow.UnconditionalFlowInfo;
import org.eclipse.jdt.internal.compiler.impl.ReferenceContext;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.LocalVariableBinding;
import org.eclipse.jdt.internal.compiler.problem.ProblemHandler;

@SuppressWarnings("restriction")
public aspect HandleIterations {
	
	private Set CompilationResult.alreadyReported=new HashSet();
	
	/**
	 * testUseNullInfo()
	 */
	UnconditionalFlowInfo around(FlowInfo flowInfo) : 
		withincode(FlowInfo analyseCode(BlockScope, FlowContext, FlowInfo)) && (this(TryStatement) || this(WhileStatement) || this(DoStatement) || this(ForeachStatement) || this(ForStatement)) && 
		call(UnconditionalFlowInfo nullInfoLessUnconditionalCopy()) && target(flowInfo) {

		if (NullibilityAnnos.enableNullibility()) {
			return flowInfo.unconditionalCopy();
		}
		return proceed(flowInfo);
	}	

	/**
	 * testDoubleCheck(), WhileFixedBugsTest
	 */
	FlowInfo around(Statement action, BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo, Statement t) : 
		withincode(FlowInfo analyseCode(BlockScope, FlowContext, FlowInfo)) && (this(WhileStatement) || this(DoStatement) || this(ForeachStatement) || this(ForStatement)) && this(t) && 
		call(FlowInfo analyseCode(BlockScope, FlowContext, FlowInfo)) && args(currentScope,flowContext,flowInfo) && target(action) && target(Statement) {
		
		if (NullibilityAnnos.doubleCheck()) {
			if (t instanceof WhileStatement && ((WhileStatement)t).action==action || t instanceof DoStatement && ((DoStatement)t).action==action || t instanceof ForeachStatement && ((ForeachStatement)t).action==action || t instanceof ForStatement && ((ForStatement)t).action==action) {
				ReferenceContext referenceContext;
				CompilationResult unitResult;
				if ((referenceContext=currentScope.problemReporter().referenceContext)!=null && (unitResult=referenceContext.compilationResult())!=null) {
					int problemCount = unitResult.problemCount;
					
					// first pass
					FlowInfo firstPassFlowInfo = action.analyseCode(currentScope, flowContext, flowInfo.copy());
										
					// if the iteration occurs at most once, second pass is useless
					if ((firstPassFlowInfo.tagBits & FlowInfo.UNREACHABLE) != 0) {
						return firstPassFlowInfo;
					}
					
					if (NullibilityAnnos.retainCannotBeNull(flowInfo,firstPassFlowInfo,currentScope)) {
						return firstPassFlowInfo;
					}
						
					// merging is especially necessary because FirstAssignmentToLocal flag would be cleared at second pass
					flowInfo=firstPassFlowInfo.mergedWith(flowInfo.unconditionalInits());
					
					// testDoubleCheckWithCondition()
					if (t instanceof WhileStatement) {
						flowInfo = ((WhileStatement)t).condition.analyseCode(currentScope, flowContext, flowInfo);
						flowInfo = flowInfo.initsWhenTrue().copy();
					} else
					if (t instanceof DoStatement) {
						flowInfo = ((DoStatement)t).condition.analyseCode(currentScope, flowContext, flowInfo);
						flowInfo = flowInfo.initsWhenTrue().copy();
					} else
					if (t instanceof ForStatement) {
						flowInfo = ((ForStatement)t).condition.analyseCode(currentScope, flowContext, flowInfo);
						flowInfo = flowInfo.initsWhenTrue().copy();
						//TODO analyze the "increments" expressions twice
					}
					
					// second pass
					return action.analyseCode(currentScope, flowContext, flowInfo);
				}		
			}
		} 

		return action.analyseCode(currentScope,flowContext,flowInfo);
	}	

	/**
	 * testNoDoubledProblems()
	 */
	void around(ProblemHandler problemHandler, int problemId, String[] problemArguments, int elaborationId, String[] messageArguments, int severity, int problemStartPosition, int problemEndPosition, ReferenceContext referenceContext, CompilationResult unitResult) :
		call(void handle(int, String[], int, String[], int, int, int, ReferenceContext, CompilationResult )) && 
		args(problemId,	problemArguments, elaborationId, messageArguments, severity, problemStartPosition, problemEndPosition, referenceContext, unitResult) && target(problemHandler) {

		if (NullibilityAnnos.doubleCheck())
		if (unitResult != null) {
			Object key = NullibilityAnnos.getProblemKey(problemStartPosition,problemId);
			if (!unitResult.alreadyReported.add(key)) return;
		}
		proceed(problemHandler,problemId,problemArguments,elaborationId,messageArguments,severity,problemStartPosition,problemEndPosition,referenceContext,unitResult);
	}	

	/**
	 * testEnduringNullInfoChange()
	 */
	FlowInfo around(FlowInfo exitBranch, FlowInfo actionInfo_unconditionalInits) : 
		withincode(FlowInfo analyseCode(BlockScope, FlowContext, FlowInfo)) && (this(WhileStatement) || this(ForStatement)) && 
		call(FlowInfo addPotentialInitializationsFrom(FlowInfo)) && args(actionInfo_unconditionalInits) && target(exitBranch) {

		if (NullibilityAnnos.enableNullibility()) {
			return new FakeFlowInfo(exitBranch,actionInfo_unconditionalInits);
		}		
		return proceed(exitBranch,actionInfo_unconditionalInits);
	}	

	/**
	 * testElementVariableIsNotNull()
	 */
	void around(UnconditionalFlowInfo actionInfo, LocalVariableBinding local) : 
		withincode(FlowInfo analyseCode(BlockScope, FlowContext, FlowInfo)) && this(ForeachStatement) && 
		call(void markAsDefinitelyUnknown(LocalVariableBinding)) && args(local) && target(actionInfo) {

		if (NullibilityAnnos.enableNullibility()) {
			actionInfo.markAsDefinitelyNonNull(local);
			return;
		}		
		proceed(actionInfo,local);
	}	
	
}
