package jjkpp.jdt.core.aspects;

import jjkpp.jdt.core.classes.NullibilityAnnos;


import org.eclipse.jdt.internal.compiler.ast.ArrayAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.ArrayReference;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.lookup.VariableBinding;

public aspect HandleNullStatusMethod {

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
		
		if (NullibilityAnnos.enableNullibility()) 		
			return t.receiver.nullStatus(flowInfo);
		return proceed(t,flowInfo);
	}	

	int around(FieldReference t,FlowInfo flowInfo) : 
		call(int nullStatus(FlowInfo)) && args(flowInfo) && target(t) {

		if (NullibilityAnnos.enableNullibility())
			return NullibilityAnnos.getSolidityWithParent(t.binding)?FlowInfo.NON_NULL:FlowInfo.UNKNOWN;
		return proceed(t,flowInfo);
	}	

	int around(MessageSend t,FlowInfo flowInfo) : 
		call(int nullStatus(FlowInfo)) && args(flowInfo) && target(t) {

		if (NullibilityAnnos.enableNullibility())
			return NullibilityAnnos.getSolidityWithParent(t.binding)?FlowInfo.NON_NULL:FlowInfo.UNKNOWN;
		return proceed(t,flowInfo);
	}	

	int around(QualifiedNameReference t,FlowInfo flowInfo) : 
		call(int nullStatus(FlowInfo)) && args(flowInfo) && target(t) {
		
		if (NullibilityAnnos.enableNullibility()) 
			return NullibilityAnnos.nullStatus(t)?FlowInfo.NON_NULL:FlowInfo.UNKNOWN;
		return proceed(t,flowInfo);
	}

	int around(SingleNameReference t,FlowInfo flowInfo) : 
		call(int nullStatus(FlowInfo)) && args(flowInfo) && target(t) {

		int result= proceed(t,flowInfo);
		if (NullibilityAnnos.enableNullibility())
			if (result==FlowInfo.UNKNOWN) {
				if (t.binding instanceof VariableBinding)
					return NullibilityAnnos.getSolidityWithParent((VariableBinding)t.binding)?FlowInfo.NON_NULL:FlowInfo.UNKNOWN;
			}
		return result;
	}


}
