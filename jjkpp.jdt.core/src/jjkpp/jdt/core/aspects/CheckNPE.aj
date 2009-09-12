package jjkpp.jdt.core.aspects;


import jjkpp.jdt.core.classes.NullibilityAnnos;
import jjkpp.jdt.core.classes.ProposalCollector;

import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration; 
import org.eclipse.jdt.internal.compiler.ast.AllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.Assignment;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.FieldDeclaration;
import org.eclipse.jdt.internal.compiler.ast.FieldReference;
import org.eclipse.jdt.internal.compiler.ast.LocalDeclaration;
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.MethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.QualifiedAllocationExpression;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.ast.SingleNameReference;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.flow.FlowContext;
import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.flow.InitializationFlowContext;
import org.eclipse.jdt.internal.compiler.lookup.AnnotationBinding;
import org.eclipse.jdt.internal.compiler.lookup.BaseTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.LocalVariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.MethodScope;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.VariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.TagBits;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;

@SuppressWarnings("restriction")
public aspect CheckNPE {
 
	before(Assignment t,BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) : 
		call(FlowInfo analyseCode(BlockScope, FlowContext, FlowInfo)) && args(currentScope,flowContext,flowInfo) && target(t) {
		
		if (NullibilityAnnos.enableNullibility()) {			
			if (t.lhs instanceof SingleNameReference) {
				SingleNameReference singleNameReference=(SingleNameReference) t.lhs;
				if (singleNameReference.binding instanceof FieldBinding) {
					FieldBinding fieldBinding=(FieldBinding) singleNameReference.binding;
					if (NullibilityAnnos.getSolidityWithParent(fieldBinding) && NullibilityAnnos.checkFieldAssignment(t)) {
						t.expression.checkNPE(currentScope, flowContext, flowInfo);
					}
				} else
				if (singleNameReference.binding instanceof VariableBinding) {
					VariableBinding variableBinding=(VariableBinding) singleNameReference.binding;
					if (NullibilityAnnos.getSolidityWithParent(variableBinding)) {
						t.expression.checkNPE(currentScope, flowContext, flowInfo);
					} 
				}
			} else
			if (t.lhs instanceof FieldReference) {
				FieldBinding fieldBinding=((FieldReference) t.lhs).binding;
				if (NullibilityAnnos.getSolidityWithParent(fieldBinding) && NullibilityAnnos.checkFieldAssignment(t)) {
					t.expression.checkNPE(currentScope, flowContext, flowInfo);			
				}
			}
			 else
			if (t.lhs instanceof QualifiedNameReference) {
				FieldBinding fieldBinding=NullibilityAnnos.getLastFieldBinding((QualifiedNameReference)t.lhs);
				if (fieldBinding!=null && NullibilityAnnos.getSolidityWithParent(fieldBinding) && NullibilityAnnos.checkFieldAssignment(t)) {
					t.expression.checkNPE(currentScope, flowContext, flowInfo);			
				}
			}
		}
	}	

	before(QualifiedNameReference t, BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo, boolean valueRequired) : 
		call(FlowInfo analyseCode(BlockScope, FlowContext, FlowInfo, boolean)) && target(t) && args(currentScope,flowContext,flowInfo,valueRequired) {

		if (NullibilityAnnos.enableNullibility()) 
		if ((flowInfo.tagBits & FlowInfo.UNREACHABLE) == 0 && NullibilityAnnos.checkScope(currentScope)) 
		{
				
			if (t.binding instanceof LocalVariableBinding)
			{
				LocalVariableBinding local = (LocalVariableBinding) t.binding;
				if ((local.type.tagBits & TagBits.IsBaseType) == 0) {
					if ((t.bits & ASTNode.IsNonNull) == 0) {
						if (flowInfo.isPotentiallyNull(local))
							NullibilityAnnos.invalidNullibility(currentScope.problemReporter(),local,t,null,0,"Nullibility problem"); //$NON-NLS-1$
					}
				}
			} else
			if (t.otherBindings!=null && t.otherBindings.length!=0) {
				FieldBinding fieldBinding = (FieldBinding) t.binding;
				if (!NullibilityAnnos.getSolidityWithParent(fieldBinding)) 
				{
					NullibilityAnnos.invalidNullibility(currentScope.problemReporter(),fieldBinding,t,null,0,"Nullibility problem"); //$NON-NLS-1$
				}
			}
			
			if (t.otherBindings!=null)
			for (int i = 0; i < t.otherBindings.length-1; i++) {
				FieldBinding fieldBinding = t.otherBindings[i];
				if (!NullibilityAnnos.getSolidityWithParent(fieldBinding)) 
				{
					NullibilityAnnos.invalidNullibility(currentScope.problemReporter(),fieldBinding,t,null,0,"Nullibility problem"); //$NON-NLS-1$
				}
			}
			
		}
	}
		
	FlowInfo around(MessageSend t, BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) : 
		call(FlowInfo analyseCode(BlockScope, FlowContext, FlowInfo)) && target(t) && args(currentScope,flowContext,flowInfo) {
	
		boolean nonStatic = !t.binding.isStatic();
		flowInfo = t.receiver.analyseCode(currentScope, flowContext, flowInfo, nonStatic).unconditionalInits();
		if (nonStatic) {
			t.receiver.checkNPE(currentScope, flowContext, flowInfo);
		}

		if (t.arguments != null) {
			int length = t.arguments.length;
			for (int i = 0; i < length; i++) {
				flowInfo = t.arguments[i].analyseCode(currentScope, flowContext, flowInfo).unconditionalInits();
				if (NullibilityAnnos.enableNullibility()) 			
					if (NullibilityAnnos.getSolidityWithParent(t.binding,i)) { 
						NullibilityAnnos.checkEasyNPE(t.arguments[i], currentScope, flowContext, flowInfo, t.binding.declaringClass); 
					}
			}
		}
		ReferenceBinding[] thrownExceptions;
		if ((thrownExceptions = t.binding.thrownExceptions) != Binding.NO_EXCEPTIONS) {
			// must verify that exceptions potentially thrown by t expression are caught in the method
			flowContext.checkExceptionHandlers(thrownExceptions, t, flowInfo.copy(), currentScope);
			// TODO (maxime) the copy above is needed because of a side effect into 
			//               checkExceptionHandlers; consider protecting there instead of here;
			//               NullReferenceTest#test0510
		}
		t.manageSyntheticAccessIfNecessary(currentScope, flowInfo);	
		return flowInfo;			
	}

	FlowInfo around(AllocationExpression t, BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) : 
		call(FlowInfo analyseCode(BlockScope, FlowContext, FlowInfo)) && target(t) && args(currentScope,flowContext,flowInfo) {
		
		if (t instanceof QualifiedAllocationExpression) {
			
			QualifiedAllocationExpression qt=(QualifiedAllocationExpression) t;
			
			// analyse the enclosing instance
			if (qt.enclosingInstance != null) {
				flowInfo = qt.enclosingInstance.analyseCode(currentScope, flowContext, flowInfo);
			}

			// check captured variables are initialized in current context (26134)
			t.checkCapturedLocalInitializationIfNecessary(
				(ReferenceBinding)(qt.anonymousType == null
					? t.binding.declaringClass.erasure()
					: t.binding.declaringClass.superclass().erasure()),
				currentScope,
				flowInfo);

			// process arguments
			if (t.arguments != null) {
				MethodBinding binding=t.binding;
				if (binding.declaringClass.isAnonymousType()) {
					MethodBinding superBinding=NullibilityAnnos.findSuperMethod(binding);
					if (superBinding!=null)
						binding=superBinding;
				}
				for (int i = 0, count = t.arguments.length; i < count; i++) {
					flowInfo = t.arguments[i].analyseCode(currentScope, flowContext, flowInfo);
					if (NullibilityAnnos.enableNullibility()) 			
						if (NullibilityAnnos.getSolidityWithParent(binding,i)) {
							NullibilityAnnos.checkEasyNPE(t.arguments[i], currentScope, flowContext, flowInfo, binding.declaringClass); 
						}
				}
			}

			// analyse the anonymous nested type
			if (qt.anonymousType != null) {
				flowInfo = qt.anonymousType.analyseCode(currentScope, flowContext, flowInfo);
			}

			// record some dependency information for exception types
			ReferenceBinding[] thrownExceptions;
			if (((thrownExceptions = t.binding.thrownExceptions).length) != 0) {
				if ((t.bits & ASTNode.Unchecked) != 0 && t.genericTypeArguments == null) {
					thrownExceptions = currentScope.environment().convertToRawTypes(t.binding.original().thrownExceptions, true, true);
				}			
				// check exception handling
				flowContext.checkExceptionHandlers(
					thrownExceptions,
					t,
					flowInfo.unconditionalCopy(),
					currentScope);
			}
			t.manageEnclosingInstanceAccessIfNecessary(currentScope, flowInfo);
			t.manageSyntheticAccessIfNecessary(currentScope, flowInfo);
			return flowInfo;			
			
		}
		
		// check captured variables are initialized in current context (26134)
		t.checkCapturedLocalInitializationIfNecessary((ReferenceBinding)t.binding.declaringClass.erasure(), currentScope, flowInfo);

		// process arguments
		if (t.arguments != null) {
			MethodBinding binding=t.binding;
			if (binding.declaringClass.isAnonymousType()) {
				MethodBinding superBinding=NullibilityAnnos.findSuperMethod(binding);
				if (superBinding!=null)
					binding=superBinding;
			}
			for (int i = 0, count = t.arguments.length; i < count; i++) {
				flowInfo =
					t.arguments[i]
						.analyseCode(currentScope, flowContext, flowInfo)
						.unconditionalInits();
				if (NullibilityAnnos.enableNullibility()) 			
					if (NullibilityAnnos.getSolidityWithParent(binding,i)) {
						NullibilityAnnos.checkEasyNPE(t.arguments[i], currentScope, flowContext, flowInfo, binding.declaringClass); 
					}
			}
		}
		// record some dependency information for exception types
		ReferenceBinding[] thrownExceptions;
		if (((thrownExceptions = t.binding.thrownExceptions).length) != 0) {
			if ((t.bits & ASTNode.Unchecked) != 0 && t.genericTypeArguments == null) {
				thrownExceptions = currentScope.environment().convertToRawTypes(t.binding.original().thrownExceptions, true, true);
			}		
			// check exception handling
			flowContext.checkExceptionHandlers(
				thrownExceptions,
				t,
				flowInfo.unconditionalCopy(),
				currentScope);
		}
		t.manageEnclosingInstanceAccessIfNecessary(currentScope, flowInfo);
		t.manageSyntheticAccessIfNecessary(currentScope, flowInfo);

		return flowInfo;
	}
	
	after(FieldDeclaration t, MethodScope initializationScope, FlowContext flowContext, FlowInfo flowInfo) 
		returning(FlowInfo result) : 
		call(FlowInfo analyseCode(MethodScope, FlowContext, FlowInfo)) && target(t) && args(initializationScope,flowContext,flowInfo) {
			
		if (NullibilityAnnos.enableNullibility()) 
		if (t.initialization != null) { 
			if (NullibilityAnnos.getSolidityWithParent(t.binding))
				t.initialization.checkNPE(initializationScope, flowContext, result);
		}
	}

	after(MethodDeclaration t, ClassScope classScope, InitializationFlowContext initializationContext, FlowInfo info): 
		call(void analyseCode(ClassScope, InitializationFlowContext, FlowInfo)) && target(t) && args(classScope,initializationContext,info) {
				
		if (NullibilityAnnos.enableNullibility()) 	
		if (t.binding!=null && t.annotations!=null && t.annotations.length!=0) {
			MethodBinding methodBinding = (MethodBinding)t.binding;
			AnnotationBinding[] annos = methodBinding.getAnnotations();			
			MethodBinding higher = null;
			int argumentsCount = t.arguments==null ? 0 : t.arguments.length;
			if (annos!=null) {

				for (int i = 0; i < annos.length; i++) {
					AnnotationBinding annotation = annos[i];
					ReferenceBinding annotationType = annotation
							.getAnnotationType();
					if (annotationType != null) {
						String annoName = new String(annotationType.sourceName);
						int param=NullibilityAnnos.getParamIndex(annoName, "NonNullParam");
						if (param!=-1) {
							if (param>=argumentsCount) {
								NullibilityAnnos.invalidNullibility(classScope.problemReporter(),t.annotations[i],null,0,"Nullibility problem: The "+(param+1)+"-th parameter does not exist"); //$NON-NLS-1$
							} else
							if (!NullibilityAnnos.getSolidityWithParent(methodBinding,param)) {
								NullibilityAnnos.invalidNullibility(classScope.problemReporter(),t.annotations[i],null,0,"Nullibility problem: Defined already as CanBeNull"); //$NON-NLS-1$
							} else
							if ((higher!=null || (higher=NullibilityAnnos.findSuperMethod(methodBinding))!=null) && higher!=methodBinding && !NullibilityAnnos.getSolidityWithParent(higher,param)) {
								NullibilityAnnos.invalidNullibility(classScope.problemReporter(),t.annotations[i],higher.declaringClass,0,"Nullibility problem: Defined already as CanBeNull in super method"); //$NON-NLS-1$
							}
						} else {
							param=NullibilityAnnos.getParamIndex(annoName, "CanBeNullParam");
							if (param!=-1) {
								if (param>=argumentsCount) {
									NullibilityAnnos.invalidNullibility(classScope.problemReporter(),t.annotations[i],null,0,"Nullibility problem: The "+(param+1)+"-th parameter does not exist"); //$NON-NLS-1$
								} else
								if (NullibilityAnnos.getSolidityWithParent(methodBinding,param)) {
									NullibilityAnnos.invalidNullibility(classScope.problemReporter(),t.annotations[i],null,0,"Nullibility problem: Defined already as NonNull"); //$NON-NLS-1$
								}
							} else {
								if (annoName.equals("CanBeNull")) {
									if (methodBinding.returnType == TypeBinding.VOID) {
										NullibilityAnnos.invalidNullibility(classScope.problemReporter(),t.annotations[i],null,0,"Nullibility problem: Method has no return value"); //$NON-NLS-1$
									} else
									if (NullibilityAnnos.getSolidityWithParent(methodBinding)) {
										NullibilityAnnos.invalidNullibility(classScope.problemReporter(),t.annotations[i],null,0,"Nullibility problem: Defined already as NonNull"); //$NON-NLS-1$
									} else
									if ((higher!=null || (higher=NullibilityAnnos.findSuperMethod(methodBinding))!=null) && higher!=methodBinding && NullibilityAnnos.getSolidityWithParent(higher)) {
										NullibilityAnnos.invalidNullibility(classScope.problemReporter(),t.annotations[i],higher.declaringClass,0,"Nullibility problem: Defined already as NonNull in super method"); //$NON-NLS-1$
									}
								} else
								if (annoName.equals("NonNull")) {
									if (methodBinding.returnType == TypeBinding.VOID) {
										NullibilityAnnos.invalidNullibility(classScope.problemReporter(),t.annotations[i],null,0,"Nullibility problem: Method has no return value"); //$NON-NLS-1$
									} else
									if (!NullibilityAnnos.getSolidityWithParent(methodBinding)) {
										NullibilityAnnos.invalidNullibility(classScope.problemReporter(),t.annotations[i],null,0,"Nullibility problem: Defined already as CanBeNull"); //$NON-NLS-1$
									}
								}									
							}							
						}
					}
				}
				
//				if (NullibilityAnnos.getSolidityWithParent(higher) && !NullibilityAnnos.getSolidityWithParent(methodBinding)) {
//					if (t.annotations.length>NullibilityAnnos.DECIDING_ANNO_INDEX) 
//						NullibilityAnnos.invalidNullibility(classScope.problemReporter(),t.annotations[NullibilityAnnos.DECIDING_ANNO_INDEX],null,0,"Easy Nullibility problem"); //$NON-NLS-1$
//				}
//				if (t.arguments != null) {
//					for (int i = 0, count = t.arguments.length; i < count; i++) {
////						LocalVariableBinding arg=t.arguments[i].binding;
//						if (!NullibilityAnnos.getSolidityWithParent(higher,i) && NullibilityAnnos.getSolidityWithParent(methodBinding,i)) 
//							if (t.annotations.length>NullibilityAnnos.DECIDING_ANNO_INDEX) 						
//								NullibilityAnnos.invalidNullibility(classScope.problemReporter(),t.annotations[NullibilityAnnos.DECIDING_ANNO_INDEX],null,0,"Easy Nullibility problem"); //$NON-NLS-1$
//					}
//				}
				
				
			}
		}
	}		

	after(LocalDeclaration t, BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) 
		returning(FlowInfo result) : 
		call(FlowInfo analyseCode(BlockScope, FlowContext, FlowInfo)) && target(t) && args(currentScope,flowContext,flowInfo) {
			
		if (NullibilityAnnos.enableNullibility()) 	
		if (t.initialization != null) {
			if (NullibilityAnnos.getSolidityWithParent(t.binding))
				t.initialization.checkNPE(currentScope, flowContext, result);
		}
	}

	after(ReturnStatement r, Expression t, BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) returning (FlowInfo result) : 
		call(FlowInfo analyseCode(BlockScope, FlowContext, FlowInfo)) && args(currentScope,flowContext,flowInfo) && target(t) && this(r) {

		if (NullibilityAnnos.enableNullibility())
			if (currentScope.referenceContext() instanceof MethodDeclaration) {
				MethodDeclaration methodDeclaration = (MethodDeclaration)currentScope.referenceContext();
				if (NullibilityAnnos.getSolidityWithParent(methodDeclaration.binding)) {
					NullibilityAnnos.checkEasyNPE(t, currentScope, flowContext, result, null);
				}
			}
	}
	
	after(CompilerOptions t, boolean newval): 
		set(public boolean CompilerOptions.storeAnnotations) && args(newval) && target(t) {
		
		if (!t.storeAnnotations)
			t.storeAnnotations=true; 
	 }
	
	before(Expression t, BlockScope scope, FlowContext flowContext, FlowInfo flowInfo):
		call(void checkNPE(BlockScope, FlowContext, FlowInfo)) && target(t) && args(scope,flowContext,flowInfo) {
		
		if (NullibilityAnnos.enableNullibility()) 
		if ((flowInfo.tagBits & FlowInfo.UNREACHABLE) == 0 && NullibilityAnnos.checkScope(scope) && t.nullStatus(flowInfo)!=FlowInfo.NON_NULL && !(t.resolvedType instanceof BaseTypeBinding && !"null".equals(new String(t.resolvedType.sourceName())))) 
		{
			NullibilityAnnos.invalidNullibility(scope.problemReporter(),t,null,0,"Nullibility problem"); //$NON-NLS-1$
		}
	}
	
}
