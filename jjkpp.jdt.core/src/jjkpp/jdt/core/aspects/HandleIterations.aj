package jjkpp.jdt.core.aspects;

import jjkpp.jdt.core.classes.FakeFlowInfo;
import jjkpp.jdt.core.classes.NullibilityAnnos;

import org.eclipse.jdt.internal.compiler.ast.Statement;
import org.eclipse.jdt.internal.compiler.ast.WhileStatement;
import org.eclipse.jdt.internal.compiler.ast.DoStatement;
import org.eclipse.jdt.internal.compiler.ast.ForeachStatement;
import org.eclipse.jdt.internal.compiler.ast.ForStatement;
import org.eclipse.jdt.internal.compiler.ast.TryStatement;
import org.eclipse.jdt.internal.compiler.flow.FlowContext;
import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.flow.UnconditionalFlowInfo;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.LocalVariableBinding;

@SuppressWarnings("restriction")
public aspect HandleIterations {
	
	/**
	 * testUseNullInfo()
	 */
	UnconditionalFlowInfo around(FlowInfo flowInfo) : 
		withincode(FlowInfo analyseCode(BlockScope, FlowContext, FlowInfo)) && (this(TryStatement) || this(WhileStatement) || this(DoStatement) || this(ForeachStatement) || this(ForStatement)) && 
		call(UnconditionalFlowInfo nullInfoLessUnconditionalCopy()) && target(flowInfo) {
 
		return flowInfo.unconditionalCopy();	
	}	

	/**
	 * testDoubleCheck(), WhileFixedBugsTest
	 */
	FlowInfo around(Statement action, BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo, Statement t) : 
		withincode(FlowInfo analyseCode(BlockScope, FlowContext, FlowInfo)) && (this(WhileStatement) || this(DoStatement) || this(ForeachStatement) || this(ForStatement)) && this(t) && 
		call(FlowInfo analyseCode(BlockScope, FlowContext, FlowInfo)) && args(currentScope,flowContext,flowInfo) && target(action) && target(Statement) {
		
		if (NullibilityAnnos.doubleCheck()) {
			if (t instanceof WhileStatement && ((WhileStatement)t).action==action || t instanceof DoStatement && ((DoStatement)t).action==action || t instanceof ForeachStatement && ((ForeachStatement)t).action==action || t instanceof ForStatement && ((ForStatement)t).action==action) {
				FlowInfo firstPassFlowInfo = action.analyseCode(currentScope, flowContext, flowInfo.copy());
				if ((firstPassFlowInfo.tagBits & FlowInfo.UNREACHABLE) == 0) {
					flowInfo=flowInfo.mergedWith(firstPassFlowInfo.unconditionalInits());
					// merging is especially necessary because FirstAssignmentToLocal flag would be cleared at second pass
					FlowInfo secondPassFlowInfo=action.analyseCode(currentScope, flowContext, flowInfo);
					return secondPassFlowInfo;
				}  else {
					return firstPassFlowInfo;
				}
			}
		} 

		return action.analyseCode(currentScope,flowContext,flowInfo);
	}	

	/**
	 * testEnduringNullInfoChange()
	 */
	FlowInfo around(FlowInfo exitBranch, FlowInfo actionInfo_unconditionalInits) : 
		withincode(FlowInfo analyseCode(BlockScope, FlowContext, FlowInfo)) && (this(WhileStatement) || this(ForStatement)) && 
		call(FlowInfo addPotentialInitializationsFrom(FlowInfo)) && args(actionInfo_unconditionalInits) && target(exitBranch) {
		
		return new FakeFlowInfo(exitBranch,actionInfo_unconditionalInits);
	}	

	/**
	 * testElementVariableIsNotNull()
	 */
	void around(UnconditionalFlowInfo actionInfo, LocalVariableBinding local) : 
		withincode(FlowInfo analyseCode(BlockScope, FlowContext, FlowInfo)) && this(ForeachStatement) && 
		call(void markAsDefinitelyUnknown(LocalVariableBinding)) && args(local) && target(actionInfo) {
		
		actionInfo.markAsDefinitelyNonNull(local);
	}	
	
}
