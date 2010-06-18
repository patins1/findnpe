package jjkpp.jdt.core.aspects;


import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.ArrayAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.ArrayReference;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.Reference;
import org.eclipse.jdt.internal.compiler.ast.NameReference;
import org.eclipse.jdt.internal.compiler.flow.FlowContext;
import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.flow.InitializationFlowContext;
import org.eclipse.jdt.internal.compiler.flow.UnconditionalFlowInfo;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.LocalVariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.TagBits;
import org.eclipse.jdt.internal.compiler.lookup.VariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.ast.Assignment;
import org.eclipse.jdt.internal.compiler.ast.CompoundAssignment;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;

import pingpong.jdt.core.classes.NullibilityAnnos;

@SuppressWarnings("restriction")
public aspect HandleNullStatusMethod {

	public static final int ASTNode_IsNonNull = ASTNode.IsNonNull;
	public static final int ASTNode_IsNull = ASTNode.Bit20;
	public static final int ASTNode_CheckedNullOrNonNull = ASTNode.Bit21;

	UnconditionalFlowInfo around(UnconditionalFlowInfo t, UnconditionalFlowInfo otherInits) : 
		call(UnconditionalFlowInfo mergedWith(UnconditionalFlowInfo)) && target(t) && args(otherInits) {
					
		if (NullibilityAnnos.enableNullibility()) {
			otherInits = NullibilityAnnos.mergedWithPrepare(t,otherInits);
		}
		return proceed(t,otherInits);
	}

	int around(ArrayAllocationExpression t,FlowInfo flowInfo) : 
		call(int nullStatus(FlowInfo)) && args(flowInfo) && target(t) {
		
		if (NullibilityAnnos.enableNullibility()) {
			if (t.initializer!=null && t.initializer.expressions!=null)
			for (Expression init:t.initializer.expressions) {
				if (init.nullStatus(flowInfo)!=FlowInfo.NON_NULL) {
					return FlowInfo.UNKNOWN;
				}
			} 
			return FlowInfo.NON_NULL;		
		}
		return proceed(t,flowInfo);
	}	

	int around(ArrayReference t,FlowInfo flowInfo) : 
		call(int nullStatus(FlowInfo)) && args(flowInfo) && target(t) {
		
		if (NullibilityAnnos.enableNullibility()) {
			return t.receiver.nullStatus(flowInfo);
		}
		return proceed(t,flowInfo);
	}	

	int around(FieldReference t,FlowInfo flowInfo) : 
		call(int nullStatus(FlowInfo)) && args(flowInfo) && target(t) {

		if (NullibilityAnnos.enableNullibility()) {
			return getNullStatus(t,t.binding,flowInfo);
		}
		return proceed(t,flowInfo);
	}	

	int around(MessageSend t,FlowInfo flowInfo) : 
		call(int nullStatus(FlowInfo)) && args(flowInfo) && target(t) {

		if (NullibilityAnnos.enableNullibility()) {
			return NullibilityAnnos.getSolidityWithParent(t.binding)?FlowInfo.NON_NULL:FlowInfo.UNKNOWN;
		}
		return proceed(t,flowInfo);
	}	

	int around(QualifiedNameReference t,FlowInfo flowInfo) : 
		call(int nullStatus(FlowInfo)) && args(flowInfo) && target(t) {
		
		if (NullibilityAnnos.enableNullibility()) { 
			return NullibilityAnnos.calcNullStatus(t)?FlowInfo.NON_NULL:FlowInfo.UNKNOWN;
		}
		return proceed(t,flowInfo);
	}

	int around(SingleNameReference t,FlowInfo flowInfo) : 
		call(int nullStatus(FlowInfo)) && args(flowInfo) && target(t) {

		if (NullibilityAnnos.enableNullibility()) { 
			return getNullStatus(t,t.binding,flowInfo);
		}
		return proceed(t,flowInfo);
	}

	/**
	 * testConditionalExpression1UsingAssignment()
	 */
	FlowInfo around(Assignment t, BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) : 
		call(FlowInfo analyseCode(BlockScope, FlowContext, FlowInfo)) && target(t) && (!target(CompoundAssignment)) && args(currentScope,flowContext,flowInfo) {
		
			if (!NullibilityAnnos.enableNullibility()) return proceed(t,currentScope,flowContext,flowInfo);
			
			// custom code: execute analyseCode() (called by analyseAssignment()) before nullStatus()
			flowInfo = ((Reference) t.lhs)
				.analyseAssignment(currentScope, flowContext, flowInfo, t, false)
				.unconditionalInits();
			// record setting a variable: various scenarii are possible, setting an array reference,
			// a field reference, a blank final field reference, a field of an enclosing instance or
			// just a local variable.
			LocalVariableBinding local = t.lhs.localVariableBinding();
			int nullStatus = t.expression.nullStatus(flowInfo);
			if (local != null && (local.type.tagBits & TagBits.IsBaseType) == 0) {
				if (nullStatus == FlowInfo.NULL) {
					flowContext.recordUsingNullReference(currentScope, local, t.lhs,
						FlowContext.CAN_ONLY_NULL | FlowContext.IN_ASSIGNMENT, flowInfo);
				}
			}
			if (local != null && (local.type.tagBits & TagBits.IsBaseType) == 0) {
				switch(nullStatus) {
					case FlowInfo.NULL :
						flowInfo.markAsDefinitelyNull(local);
						break;
					case FlowInfo.NON_NULL :
						flowInfo.markAsDefinitelyNonNull(local);
						break;
					default:
						flowInfo.markAsDefinitelyUnknown(local);
				}
				if (flowContext.initsOnFinally != null) {
					switch(nullStatus) {
						case FlowInfo.NULL :
							flowContext.initsOnFinally.markAsDefinitelyNull(local);
							break;
						case FlowInfo.NON_NULL :
							flowContext.initsOnFinally.markAsDefinitelyNonNull(local);
							break;
						default:
							flowContext.initsOnFinally.markAsDefinitelyUnknown(local);
					}
				}
			}
			return flowInfo;
	}
	
	/**
	 * testConditionalExpression1UsingLocalDeclaration()
	 */
	FlowInfo around(LocalDeclaration t, BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) : 
		call(FlowInfo analyseCode(BlockScope, FlowContext, FlowInfo)) && target(t) && args(currentScope,flowContext,flowInfo) {

		if (!NullibilityAnnos.enableNullibility()) return proceed(t,currentScope,flowContext,flowInfo);

		// record variable initialization if any
		if ((flowInfo.tagBits & FlowInfo.UNREACHABLE) == 0) {
			t.bits |= ASTNode.IsLocalDeclarationReachable; // only set if actually reached
		}
		if (t.initialization == null) {
			return flowInfo;
		}
		// custom code: execute analyseCode() before nullStatus()
		flowInfo =
			t.initialization
				.analyseCode(currentScope, flowContext, flowInfo)
				.unconditionalInits();
		int nullStatus = t.initialization.nullStatus(flowInfo);
		if (!flowInfo.isDefinitelyAssigned(t.binding)){// for local variable debug attributes
			t.bits |= ASTNode.FirstAssignmentToLocal;
		} else {
			t.bits &= ~ASTNode.FirstAssignmentToLocal;  // int i = (i = 0);
		}
		flowInfo.markAsDefinitelyAssigned(t.binding);
		if ((t.binding.type.tagBits & TagBits.IsBaseType) == 0) {
			switch(nullStatus) {
				case FlowInfo.NULL :
					flowInfo.markAsDefinitelyNull(t.binding);
					break;
				case FlowInfo.NON_NULL :
					flowInfo.markAsDefinitelyNonNull(t.binding);
					break;
				default:
					flowInfo.markAsDefinitelyUnknown(t.binding);
			}
			// no need to inform enclosing try block since its locals won't get
			// known by the finally block
		}
		return flowInfo;
	}

	before(Reference t, BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) : 
		call(FlowInfo analyseCode(BlockScope, FlowContext, FlowInfo)) && target(t) && args(currentScope,flowContext,flowInfo) && (target(SingleNameReference) || target(FieldReference)) {

		if (NullibilityAnnos.enableNullibility()) {
			t.bits = (t.bits | ASTNode_CheckedNullOrNonNull) & ~(ASTNode_IsNonNull | ASTNode_IsNull) ;
			LocalVariableBinding local = t.localVariableBinding();
			if (local != null) {
				// NullStatusEnhancedTest
				if (flowInfo.isDefinitelyNull(local))
					t.bits |= ASTNode_IsNull;
				if (flowInfo.isDefinitelyNonNull(local))
					t.bits |= ASTNode_IsNonNull;//see t.markAsNonNull()
			}
		}
	}
	
	int getNullStatus(Reference t, Binding binding, FlowInfo flowInfo) {
		if ((t.bits & ASTNode_CheckedNullOrNonNull) != 0 && (flowInfo.tagBits & NullibilityAnnos.FlowInfo_OnlyUseFlowInfo) == 0) {
			// NullStatusEnhancedTest
			if ((t.bits & ASTNode_IsNonNull) != 0)
				return FlowInfo.NON_NULL;
			if ((t.bits & ASTNode_IsNull) != 0)
				return FlowInfo.NULL;
		} else {
			LocalVariableBinding local = t.localVariableBinding();
			if (local != null) {
				if (flowInfo.isDefinitelyNull(local))
					return FlowInfo.NULL;
				if (flowInfo.isDefinitelyNonNull(local))
					return FlowInfo.NON_NULL;
			}
		}
		if (binding instanceof VariableBinding)
			return NullibilityAnnos.getSolidityWithParent((VariableBinding)binding)?FlowInfo.NON_NULL:FlowInfo.UNKNOWN;
		return FlowInfo.UNKNOWN;
	}


}
