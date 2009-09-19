package jjkpp.jdt.core.aspects;

import jjkpp.jdt.core.classes.NullibilityAnnos;

import org.eclipse.jdt.internal.compiler.ast.ArrayAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.ArrayReference;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.Reference;
import org.eclipse.jdt.internal.compiler.ast.NameReference;
import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.lookup.LocalVariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.VariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.Binding;

@SuppressWarnings("restriction")
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

		return getNullStatus(t,t.binding,flowInfo);
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

		return getNullStatus(t,t.binding,flowInfo);
	}
	
	int getNullStatus(Reference t, Binding binding, FlowInfo flowInfo) {
		LocalVariableBinding local = t.localVariableBinding();
		if (local != null) {
			if (flowInfo.isDefinitelyNull(local))
				return FlowInfo.NULL;
			if (flowInfo.isDefinitelyNonNull(local))
				return FlowInfo.NON_NULL;
		}
		if (binding instanceof VariableBinding)
			return NullibilityAnnos.getSolidityWithParent((VariableBinding)binding)?FlowInfo.NON_NULL:FlowInfo.UNKNOWN;
		return FlowInfo.UNKNOWN;
	}


}
