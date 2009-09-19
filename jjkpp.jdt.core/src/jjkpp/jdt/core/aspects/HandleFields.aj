package jjkpp.jdt.core.aspects;

import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.lookup.LocalVariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.TagBits;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.flow.UnconditionalFlowInfo;
import org.eclipse.jdt.internal.compiler.ast.ThisReference;

@SuppressWarnings("restriction")
public aspect HandleFields {
	
	LocalVariableBinding around(SingleNameReference ref) : 
		call(LocalVariableBinding localVariableBinding()) && target(ref) {
		
		if (ref.binding instanceof FieldBinding) {
			FieldBinding fieldBinding=(FieldBinding) ref.binding;
			if (fieldBinding.original().declaringClass == ref.actualReceiverType.erasure())
				return fakeLocalVariableBinding(fieldBinding);
			return null; // CheckFieldSubclassTest
		}
		return proceed(ref);
	}	

	LocalVariableBinding around(FieldReference ref) : 
		call(LocalVariableBinding localVariableBinding()) && target(ref) {

		FieldBinding fieldBinding=(FieldBinding) ref.binding;
		if (ref.receiver instanceof ThisReference && fieldBinding.original().declaringClass == ref.actualReceiverType.erasure())
			return fakeLocalVariableBinding(fieldBinding);
		return null; // CheckFieldSubclassTest
	}	
	
	/**
	 * CheckFieldTest
	 * @param fieldBinding
	 * @return
	 */
	LocalVariableBinding fakeLocalVariableBinding(FieldBinding fieldBinding) {
		if ((fieldBinding.type.tagBits & TagBits.IsBaseType) == 0 && fieldBinding.id<UnconditionalFlowInfo.BitCacheSize) {
			LocalVariableBinding result = new LocalVariableBinding(fieldBinding.name,fieldBinding.type,fieldBinding.modifiers,(fieldBinding.tagBits & TagBits.IsArgument)!=0);
			result.id=-fieldBinding.id-1;
			result.setConstant(fieldBinding.constant());
			return result;	
		}
		return null;
	}
}
