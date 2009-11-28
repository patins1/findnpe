package jjkpp.jdt.core.aspects;


import jjkpp.jdt.core.classes.NullibilityAnnos;
import jjkpp.jdt.core.classes.ProposalCollector;

import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration; 
import org.eclipse.jdt.internal.compiler.ast.ConstructorDeclaration; 
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
import org.eclipse.jdt.internal.compiler.ast.TryStatement;
import org.eclipse.jdt.internal.compiler.flow.ExceptionHandlingFlowContext;
import org.eclipse.jdt.internal.compiler.flow.FinallyFlowContext;
import org.eclipse.jdt.internal.compiler.flow.FlowContext;
import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.flow.InitializationFlowContext;
import org.eclipse.jdt.internal.compiler.flow.InsideSubRoutineFlowContext;
import org.eclipse.jdt.internal.compiler.flow.NullInfoRegistry;
import org.eclipse.jdt.internal.compiler.flow.UnconditionalFlowInfo;
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
privileged public aspect CheckNPE {

	// in the following precedence list, each aspect has a replace-advice 
	// which would hide an advice of any aspect to its left, so the precedence is required; 
	// replace-advices for analyseCode() have comments with "custom code" text in their body
	declare precedence : HandleIterations, CheckNPE, HandleNullStatusMethod;


	before(MethodDeclaration t, ClassScope classScope, InitializationFlowContext initializationContext, FlowInfo flowInfo) : 
		call(void analyseCode(ClassScope, InitializationFlowContext, FlowInfo)) && target(t) && args(classScope,initializationContext,flowInfo) {
					
			if (NullibilityAnnos.enableNullibility()) 	
			if (!t.ignoreFurtherInvestigation && t.binding!=null && t.arguments!=null) {
				MethodBinding methodBinding = t.binding;
				for (int i = 0, count = t.arguments.length; i < count; i++) {
					if (NullibilityAnnos.getSolidityWithParent(methodBinding,i)) {
						flowInfo.markAsDefinitelyAssigned(t.arguments[i].binding); // assure initialization of UnconditionalFlowInfo.extra
						flowInfo.markAsDefinitelyNonNull(t.arguments[i].binding);
					}
				}
			}		
	}

	before(ConstructorDeclaration t, ClassScope classScope, InitializationFlowContext initializationContext, FlowInfo flowInfo, int initialReachMode) : 
		call(void analyseCode(ClassScope, InitializationFlowContext, FlowInfo, int)) && target(t) && args(classScope,initializationContext,flowInfo,initialReachMode) {
					
			if (NullibilityAnnos.enableNullibility()) 	
			if (!t.ignoreFurtherInvestigation && t.binding!=null && t.arguments!=null) {
				MethodBinding methodBinding = t.binding;
				for (int i = 0, count = t.arguments.length; i < count; i++) {
					if (NullibilityAnnos.getSolidityWithParent(methodBinding,i)) {
						flowInfo.markAsDefinitelyAssigned(t.arguments[i].binding); // assure initialization of UnconditionalFlowInfo.extra
						flowInfo.markAsDefinitelyNonNull(t.arguments[i].binding);
					}
				}
			}		
	}
	
	before(Assignment t,BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) : 
		call(FlowInfo analyseCode(BlockScope, FlowContext, FlowInfo)) && args(currentScope,flowContext,flowInfo) && target(t) {
		
		if (NullibilityAnnos.enableNullibility()) {			
			if (t.lhs instanceof SingleNameReference) {
				SingleNameReference singleNameReference=(SingleNameReference) t.lhs;
				if (singleNameReference.binding instanceof FieldBinding) {
					FieldBinding fieldBinding=(FieldBinding) singleNameReference.binding;
					if (NullibilityAnnos.checkOnNonNull(fieldBinding) && NullibilityAnnos.checkFieldAssignment(t)) {
						t.expression.checkNPE(currentScope, flowContext, flowInfo);
					}
				} else
				if (singleNameReference.binding instanceof VariableBinding) {
					VariableBinding variableBinding=(VariableBinding) singleNameReference.binding;
					if (NullibilityAnnos.checkOnNonNull(variableBinding)) {
						t.expression.checkNPE(currentScope, flowContext, flowInfo);
					} 
				}
			} else
			if (t.lhs instanceof FieldReference) {
				FieldBinding fieldBinding=((FieldReference) t.lhs).binding;
				if (NullibilityAnnos.checkOnNonNull(fieldBinding) && NullibilityAnnos.checkFieldAssignment(t)) {
					t.expression.checkNPE(currentScope, flowContext, flowInfo);			
				}
			}
			 else
			if (t.lhs instanceof QualifiedNameReference) {
				FieldBinding fieldBinding=NullibilityAnnos.getLastFieldBinding((QualifiedNameReference)t.lhs);
				if (fieldBinding!=null && NullibilityAnnos.checkOnNonNull(fieldBinding) && NullibilityAnnos.checkFieldAssignment(t)) {
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

		if (!NullibilityAnnos.enableNullibility()) return proceed(t,currentScope,flowContext,flowInfo); 	
		
		boolean nonStatic = !t.binding.isStatic();
		flowInfo = t.receiver.analyseCode(currentScope, flowContext, flowInfo, nonStatic).unconditionalInits();
		if (nonStatic) {
			t.receiver.checkNPE(currentScope, flowContext, flowInfo);
		}

		if (t.arguments != null) {
			int length = t.arguments.length;
			for (int i = 0; i < length; i++) {
				flowInfo = t.arguments[i].analyseCode(currentScope, flowContext, flowInfo).unconditionalInits();
				// custom code: check parameters
				if (NullibilityAnnos.enableNullibility()) 			
					if (NullibilityAnnos.checkOnNonNull(t.binding,i)) { 
						NullibilityAnnos.checkEasyNPE(t.arguments[i], currentScope, flowContext, flowInfo, t.binding.declaringClass); 
					}
			}
		}
		ReferenceBinding[] thrownExceptions;
		if ((thrownExceptions = t.binding.thrownExceptions) != Binding.NO_EXCEPTIONS) {
			if ((t.bits & ASTNode.Unchecked) != 0 && t.genericTypeArguments == null) {
				thrownExceptions = currentScope.environment().convertToRawTypes(t.binding.original().thrownExceptions, true, true);
			}			
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
		
		if (!NullibilityAnnos.enableNullibility()) return proceed(t,currentScope,flowContext,flowInfo); 	
		
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
					// custom code: use super constructor instead of its anonymous duplicate
					MethodBinding superBinding=NullibilityAnnos.findSuperMethod(binding);
					if (superBinding!=null)
						binding=superBinding;
				}
				for (int i = 0, count = t.arguments.length; i < count; i++) {
					flowInfo = t.arguments[i].analyseCode(currentScope, flowContext, flowInfo);
					// custom code: check parameters
					if (NullibilityAnnos.enableNullibility()) 			
						if (NullibilityAnnos.checkOnNonNull(binding,i)) {
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
				// custom code: use super constructor instead of its anonymous duplicate
				MethodBinding superBinding=NullibilityAnnos.findSuperMethod(binding);
				if (superBinding!=null)
					binding=superBinding;
			}
			for (int i = 0, count = t.arguments.length; i < count; i++) {
				flowInfo =
					t.arguments[i]
						.analyseCode(currentScope, flowContext, flowInfo)
						.unconditionalInits();
				// custom code: check parameters
				if (NullibilityAnnos.enableNullibility()) 			
					if (NullibilityAnnos.checkOnNonNull(binding,i)) {
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
	
	FlowInfo around(TryStatement t, BlockScope currentScope, FlowContext flowContext, FlowInfo flowInfo) : 
		call(FlowInfo analyseCode(BlockScope, FlowContext, FlowInfo)) && target(t) && args(currentScope,flowContext,flowInfo) {

		if (!NullibilityAnnos.enableNullibility()) return proceed(t,currentScope,flowContext,flowInfo); 	

		/*custom code: exchange nullInfoLessUnconditionalCopy by unconditionalCopy, since our advice for this not advices this method */
		
		// Consider the try block and catch block so as to compute the intersection of initializations and
		// the minimum exit relative depth amongst all of them. Then consider the subroutine, and append its
		// initialization to the try/catch ones, if the subroutine completes normally. If the subroutine does not
		// complete, then only keep t result for the rest of the analysis

		// process the finally block (subroutine) - create a context for the subroutine

		t.preTryInitStateIndex =
			currentScope.methodScope().recordInitializationStates(flowInfo);

		if (t.anyExceptionVariable != null) {
			t.anyExceptionVariable.useFlag = LocalVariableBinding.USED;
		}
		if (t.returnAddressVariable != null) { // TODO (philippe) if subroutine is escaping, unused
			t.returnAddressVariable.useFlag = LocalVariableBinding.USED;
		}
		if (t.subRoutineStartLabel == null) {
			// no finally block -- t is a simplified copy of the else part
			// process the try block in a context handling the local exceptions.
			ExceptionHandlingFlowContext handlingContext =
				new ExceptionHandlingFlowContext(
					flowContext,
					t,
					t.caughtExceptionTypes,
					null,
					t.scope,
					flowInfo.unconditionalInits());
			handlingContext.initsOnFinally =
				new NullInfoRegistry(flowInfo.unconditionalInits());
			// only try blocks initialize that member - may consider creating a
			// separate class if needed

			FlowInfo tryInfo;
			if (t.tryBlock.isEmptyBlock()) {
				tryInfo = flowInfo;
			} else {
				tryInfo = t.tryBlock.analyseCode(currentScope, handlingContext, flowInfo.copy());
				if ((tryInfo.tagBits & FlowInfo.UNREACHABLE) != 0)
					t.bits |= ASTNode.IsTryBlockExiting;
			}

			// check unreachable catch blocks
			handlingContext.complainIfUnusedExceptionHandlers(t.scope, t);

			// process the catch blocks - computing the minimal exit depth amongst try/catch
			if (t.catchArguments != null) {
				int catchCount;
				t.catchExits = new boolean[catchCount = t.catchBlocks.length];
				t.catchExitInitStateIndexes = new int[catchCount];
				for (int i = 0; i < catchCount; i++) {
					// keep track of the inits that could potentially have led to t exception handler (for final assignments diagnosis)
					FlowInfo catchInfo;
					if (t.caughtExceptionTypes[i].isUncheckedException(true)) {
						catchInfo =
							handlingContext.initsOnFinally.mitigateNullInfoOf(
								flowInfo.unconditionalCopy().
									addPotentialInitializationsFrom(
										handlingContext.initsOnException(
											t.caughtExceptionTypes[i])).
									addPotentialInitializationsFrom(tryInfo).
									addPotentialInitializationsFrom(
										handlingContext.initsOnReturn));
					} else {
						catchInfo =
							flowInfo.unconditionalCopy().
								addPotentialInitializationsFrom(
									handlingContext.initsOnException(
										t.caughtExceptionTypes[i]))
								.addPotentialInitializationsFrom(
									tryInfo.unconditionalCopy())
									// remove null info to protect point of
									// exception null info
								.addPotentialInitializationsFrom(
									handlingContext.initsOnReturn.
									unconditionalCopy());
					}

					// catch var is always set
					LocalVariableBinding catchArg = t.catchArguments[i].binding;
					catchInfo.markAsDefinitelyAssigned(catchArg);
					catchInfo.markAsDefinitelyNonNull(catchArg);
					/*
					"If we are about to consider an unchecked exception handler, potential inits may have occured inside
					the try block that need to be detected , e.g.
					try { x = 1; throwSomething();} catch(Exception e){ x = 2} "
					"(uncheckedExceptionTypes notNil and: [uncheckedExceptionTypes at: index])
					ifTrue: [catchInits addPotentialInitializationsFrom: tryInits]."
					*/
					if (t.tryBlock.statements == null) {
						catchInfo.setReachMode(FlowInfo.UNREACHABLE);
					}
					catchInfo =
						t.catchBlocks[i].analyseCode(
							currentScope,
							flowContext,
							catchInfo);
					t.catchExitInitStateIndexes[i] = currentScope.methodScope().recordInitializationStates(catchInfo);
					t.catchExits[i] =
						(catchInfo.tagBits & FlowInfo.UNREACHABLE) != 0;
					tryInfo = tryInfo.mergedWith(catchInfo.unconditionalInits());
				}
			}
			t.mergedInitStateIndex =
				currentScope.methodScope().recordInitializationStates(tryInfo);

			// chain up null info registry
			if (flowContext.initsOnFinally != null) {
				flowContext.initsOnFinally.add(handlingContext.initsOnFinally);
			}

			return tryInfo;
		} else {
			InsideSubRoutineFlowContext insideSubContext;
			FinallyFlowContext finallyContext;
			UnconditionalFlowInfo subInfo;
			// analyse finally block first
			insideSubContext = new InsideSubRoutineFlowContext(flowContext, t);

			subInfo =
				t.finallyBlock
					.analyseCode(
						currentScope,
						finallyContext = new FinallyFlowContext(flowContext, t.finallyBlock),
						flowInfo.unconditionalCopy())
					.unconditionalInits();
			if (subInfo == FlowInfo.DEAD_END) {
				t.bits |= ASTNode.IsSubRoutineEscaping;
				t.scope.problemReporter().finallyMustCompleteNormally(t.finallyBlock);
			}
			t.subRoutineInits = subInfo;
			// process the try block in a context handling the local exceptions.
			ExceptionHandlingFlowContext handlingContext =
				new ExceptionHandlingFlowContext(
					insideSubContext,
					t,
					t.caughtExceptionTypes,
					null,
					t.scope,
					flowInfo.unconditionalInits());
			handlingContext.initsOnFinally =
				new NullInfoRegistry(flowInfo.unconditionalInits());
			// only try blocks initialize that member - may consider creating a
			// separate class if needed

			FlowInfo tryInfo;
			if (t.tryBlock.isEmptyBlock()) {
				tryInfo = flowInfo;
			} else {
				tryInfo = t.tryBlock.analyseCode(currentScope, handlingContext, flowInfo.copy());
				if ((tryInfo.tagBits & FlowInfo.UNREACHABLE) != 0)
					t.bits |= ASTNode.IsTryBlockExiting;
			}

			// check unreachable catch blocks
			handlingContext.complainIfUnusedExceptionHandlers(t.scope, t);

			// process the catch blocks - computing the minimal exit depth amongst try/catch
			if (t.catchArguments != null) {
				int catchCount;
				t.catchExits = new boolean[catchCount = t.catchBlocks.length];
				t.catchExitInitStateIndexes = new int[catchCount];
				for (int i = 0; i < catchCount; i++) {
					// keep track of the inits that could potentially have led to t exception handler (for final assignments diagnosis)
					FlowInfo catchInfo;
					if (t.caughtExceptionTypes[i].isUncheckedException(true)) {
						catchInfo =
							handlingContext.initsOnFinally.mitigateNullInfoOf(
								flowInfo.unconditionalCopy().
									addPotentialInitializationsFrom(
										handlingContext.initsOnException(
											t.caughtExceptionTypes[i])).
									addPotentialInitializationsFrom(tryInfo).
									addPotentialInitializationsFrom(
										handlingContext.initsOnReturn));
					}else {
						catchInfo =
							flowInfo.unconditionalCopy()
								.addPotentialInitializationsFrom(
									handlingContext.initsOnException(
										t.caughtExceptionTypes[i]))
										.addPotentialInitializationsFrom(
									tryInfo.unconditionalCopy())
									// remove null info to protect point of
									// exception null info
								.addPotentialInitializationsFrom(
										handlingContext.initsOnReturn.
										unconditionalCopy());
					}

					// catch var is always set
					LocalVariableBinding catchArg = t.catchArguments[i].binding;
					catchInfo.markAsDefinitelyAssigned(catchArg);
					catchInfo.markAsDefinitelyNonNull(catchArg);
					/*
					"If we are about to consider an unchecked exception handler, potential inits may have occured inside
					the try block that need to be detected , e.g.
					try { x = 1; throwSomething();} catch(Exception e){ x = 2} "
					"(uncheckedExceptionTypes notNil and: [uncheckedExceptionTypes at: index])
					ifTrue: [catchInits addPotentialInitializationsFrom: tryInits]."
					*/
					if (t.tryBlock.statements == null) {
						catchInfo.setReachMode(FlowInfo.UNREACHABLE);
					}
					catchInfo =
						t.catchBlocks[i].analyseCode(
							currentScope,
							insideSubContext,
							catchInfo);
					t.catchExitInitStateIndexes[i] = currentScope.methodScope().recordInitializationStates(catchInfo);
					t.catchExits[i] =
						(catchInfo.tagBits & FlowInfo.UNREACHABLE) != 0;
					tryInfo = tryInfo.mergedWith(catchInfo.unconditionalInits());
				}
			}
			// we also need to check potential multiple assignments of final variables inside the finally block
			// need to include potential inits from returns inside the try/catch parts - 1GK2AOF
			finallyContext.complainOnDeferredChecks(
				handlingContext.initsOnFinally.mitigateNullInfoOf(
					(tryInfo.tagBits & FlowInfo.UNREACHABLE) == 0 ?
						flowInfo.unconditionalCopy().
						addPotentialInitializationsFrom(tryInfo).
							// lighten the influence of the try block, which may have
							// exited at any point
						addPotentialInitializationsFrom(insideSubContext.initsOnReturn) :
						insideSubContext.initsOnReturn),
				currentScope);

			// chain up null info registry
			if (flowContext.initsOnFinally != null) {
				flowContext.initsOnFinally.add(handlingContext.initsOnFinally);
			}

			t.naturalExitMergeInitStateIndex =
				currentScope.methodScope().recordInitializationStates(tryInfo);
			
			/*custom code: second pass if required*/			
			if (NullibilityAnnos.doubleCheck())
			if (!NullibilityAnnos.retainCannotBeNull(flowInfo,tryInfo,currentScope)) {
				t.finallyBlock
					.analyseCode(
						currentScope,
						finallyContext = new FinallyFlowContext(flowContext, t.finallyBlock),
						tryInfo.unconditionalCopy())
					.unconditionalInits();
			}			
			
			if (subInfo == FlowInfo.DEAD_END) {
				t.mergedInitStateIndex =
					currentScope.methodScope().recordInitializationStates(subInfo);
				return subInfo;
			} else {
				FlowInfo mergedInfo = tryInfo.addInitializationsFrom(subInfo);
				t.mergedInitStateIndex =
					currentScope.methodScope().recordInitializationStates(mergedInfo);
				return mergedInfo;
			}
		}
	}	
	
	after(FieldDeclaration t, MethodScope initializationScope, FlowContext flowContext, FlowInfo flowInfo) 
		returning(FlowInfo result) : 
		call(FlowInfo analyseCode(MethodScope, FlowContext, FlowInfo)) && target(t) && args(initializationScope,flowContext,flowInfo) {
			
		if (NullibilityAnnos.enableNullibility()) 
		if (t.initialization != null) { 
			if (NullibilityAnnos.checkOnNonNull(t.binding))
				t.initialization.checkNPE(initializationScope, flowContext, result);
		}
	}

	after(MethodDeclaration t, ClassScope classScope, InitializationFlowContext initializationContext, FlowInfo info) returning: 
		call(void analyseCode(ClassScope, InitializationFlowContext, FlowInfo)) && target(t) && args(classScope,initializationContext,info) {
				
		if (NullibilityAnnos.enableNullibility()) 	
		if (t.binding!=null && t.annotations!=null && t.annotations.length!=0) {
			MethodBinding methodBinding = t.binding;
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
									if (NullibilityAnnos.getSolidityWithParent(methodBinding)) {
										NullibilityAnnos.invalidNullibility(classScope.problemReporter(),t.annotations[i],null,0,"Nullibility problem: Defined already as NonNull"); //$NON-NLS-1$
									} else
									if ((higher!=null || (higher=NullibilityAnnos.findSuperMethod(methodBinding))!=null) && higher!=methodBinding && NullibilityAnnos.getSolidityWithParent(higher)) {
										NullibilityAnnos.invalidNullibility(classScope.problemReporter(),t.annotations[i],higher.declaringClass,0,"Nullibility problem: Defined already as NonNull in super method"); //$NON-NLS-1$
									}
								} else
								if (annoName.equals("NonNull")) {
									if (!NullibilityAnnos.getSolidityWithParent(methodBinding)) {
										NullibilityAnnos.invalidNullibility(classScope.problemReporter(),t.annotations[i],null,0,"Nullibility problem: Defined already as CanBeNull"); //$NON-NLS-1$
									}
								}									
							}							
						}
					}
				}
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
				if (NullibilityAnnos.checkOnNonNull(methodDeclaration.binding)) {
					NullibilityAnnos.checkEasyNPE(t, currentScope, flowContext, result, null);
				}
			}
	}
	
	after(CompilerOptions t, boolean newval): 
		set(public boolean CompilerOptions.storeAnnotations) && args(newval) && target(t) {
		
		if (NullibilityAnnos.enableNullibility()) 	
		if (!t.storeAnnotations)
			t.storeAnnotations=true; 
	 }
	
	before(Expression t, BlockScope scope, FlowContext flowContext, FlowInfo flowInfo):
		call(void checkNPE(BlockScope, FlowContext, FlowInfo)) && target(t) && args(scope,flowContext,flowInfo) {
		
		if (NullibilityAnnos.enableNullibility()) 
		if (NullibilityAnnos.isNotNonNull(t, scope, flowInfo)) 
		{
			NullibilityAnnos.invalidNullibility(scope.problemReporter(),t,null,0,"Nullibility problem"); //$NON-NLS-1$
		}
	}
	
}
