package jjkpp.jdt.core.classes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.util.IModifierConstants;
import org.eclipse.jdt.internal.compiler.ast.ASTNode;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Annotation;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.ast.Assignment;
import org.eclipse.jdt.internal.compiler.ast.CastExpression;
import org.eclipse.jdt.internal.compiler.ast.Expression;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.flow.FlowContext;
import org.eclipse.jdt.internal.compiler.flow.FlowInfo;
import org.eclipse.jdt.internal.compiler.flow.UnconditionalFlowInfo;
import org.eclipse.jdt.internal.compiler.lookup.AnnotationBinding;
import org.eclipse.jdt.internal.compiler.lookup.BaseTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.BinaryTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.Binding;
import org.eclipse.jdt.internal.compiler.lookup.BlockScope;
import org.eclipse.jdt.internal.compiler.lookup.ClassScope;
import org.eclipse.jdt.internal.compiler.lookup.FieldBinding;
import org.eclipse.jdt.internal.compiler.lookup.LocalVariableBinding;
import org.eclipse.jdt.internal.compiler.lookup.LookupEnvironment;
import org.eclipse.jdt.internal.compiler.lookup.MethodBinding;
import org.eclipse.jdt.internal.compiler.lookup.ParameterizedTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.VariableBinding;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.jdt.internal.compiler.problem.ProblemHandler;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.jdt.internal.compiler.problem.ProblemSeverities;

/**
 * TODO: QualifiedNameReference.otherBindings intermediate auf nullibility
 * prüfen SWT.error als alwaysThrow markieren
 * 
 * 
 * @author JoeKey
 * 
 */
@SuppressWarnings("restriction")
public class NullibilityAnnos {
	
	public static boolean ATTACK = !true;
	
	static public int RequireCanBeNull = IProblem.MethodRelated + 148;

	private static final boolean ALLOW_ASSIGN_NULL_TO_FIELD = true;

	private static final boolean ALLOW_ASSIGN_CANBENULL_TO_FIELD = ALLOW_ASSIGN_NULL_TO_FIELD && false;

	/**
	 * Flag indicating the the null-status-method shall use only information
	 * from the flow info, but not from the ASTNode itself; see FlowInfo for
	 * free flags, tested by testDoubleCheckUsingOnlyOneExpressionAsBodyI
	 */
	public static final int FlowInfo_OnlyUseFlowInfo = 4;

	public static boolean checkOnNonNull(MethodBinding binding) {
		if (ATTACK) {
			Boolean result=_getSolidityWithParent(binding);
			if (result!=null)
				return result;
			return false;			
		}
		return getSolidityWithParent(binding);
	}
	
	public static boolean getSolidityWithParent(MethodBinding binding) {
		Boolean result=_getSolidityWithParent(binding);
		if (result!=null)
			return result;
		return NullibilityAnnos.hasSolidAnnotation(binding.declaringClass) == FlowInfo.NON_NULL;
	}

	private static Boolean _getSolidityWithParent(MethodBinding binding) {
		MethodBinding highest=binding;
		MethodBinding _binding = binding;
		do {
			int result = NullibilityAnnos.hasSolidAnnotation(_binding.getAnnotations());
			if (result != FlowInfo.UNKNOWN) {
				return result == FlowInfo.NON_NULL;
			}
			MethodBinding superMethod = findSuperMethod(_binding);
			if (superMethod != null)
				highest = superMethod;
			if (superMethod == _binding)
				break;
			_binding= superMethod;
		} while (_binding != null);
		
		if (false && binding.declaringClass != null && highest != null && highest.declaringClass != null) {

			String methodName = new String(binding.selector);
			// Set<String> itfs = new HashSet<String>();
			// itfs.add(getClassName(declaringClass));
			// addSuperClasses(declaringClass, itfs);

			String className = getClassName(highest.declaringClass);
			if (className.equals("java.util.Arrays"))
				return true;
			if (className.equals("java.util.List"))
				return true;
			if (className.equals("java.util.Map") && !"put".equals(methodName) && !"remove".equals(methodName) && !"get".equals(methodName))
				return true;
			if (className.equals("java.util.Entry"))
				return true;
			if (className.equals("java.lang.Object"))
				return true;
			if (className.equals("java.lang.String"))
				return true;
			if (className.equals("java.lang.Class") && "".equals(methodName))
				return true;
			if (className.equals("java.lang.AbstractList"))
				return true;
			if (className.equals("org.eclipse.swt.graphics.GC"))
				return true;
			if (className.equals("java.util.Set") && "iterator".equals(methodName))
				return true;
			if ((className.equals("java.util.HashMap") || className.equals("java.util.Map")) && !"get".equals(methodName) && !"put".equals(methodName) && !"remove".equals(methodName))
				return true;
			if (className.equals("java.util.Iterator"))
				return true;
			if (className.equals("java.text.DateFormat") && !"parseObject".equals(methodName))
				return true;
			if (className.equals("java.sql.Connection") && "".equals(methodName) && !"".equals(methodName) && !"".equals(methodName))
				return true;
			if (className.equals("java.sql.Statement") && !"".equals(methodName) && !"".equals(methodName) && !"".equals(methodName))
				return true;
		}
		return null;
	}

	public static boolean checkOnNonNull(MethodBinding binding, int param) {
		if (ATTACK) {
			Boolean result=_getSolidityWithParent(binding,param);
			if (result!=null)
				return result;
			return false;			
		}
		return getSolidityWithParent(binding,param);
	}

	public static boolean getSolidityWithParent(MethodBinding binding, int param) {
		Boolean result=_getSolidityWithParent(binding,param);
		if (result!=null)
			return result;
		return NullibilityAnnos.hasSolidAnnotation(binding.declaringClass) == FlowInfo.NON_NULL;
	}

	public static Boolean _getSolidityWithParent(MethodBinding binding, int param) {
		MethodBinding _binding = binding;
		do {
			int result = hasSolidAnnotation(_binding, param);
			if (result != FlowInfo.UNKNOWN)
				return result == FlowInfo.NON_NULL;
			MethodBinding superMethod = findSuperMethod(_binding);
			if (superMethod == _binding)
				break;
			_binding = superMethod;
		} while (_binding != null);
		return null;
	}
	
	public static boolean checkOnNonNull(VariableBinding var) {
		if (var instanceof FieldBinding) {
			return checkOnNonNull((FieldBinding) var);
		}
		if (var instanceof LocalVariableBinding) {
			return checkOnNonNull((LocalVariableBinding) var);
		}
		return false;
	}	

	public static boolean getSolidityWithParent(VariableBinding var) {
		if (var instanceof FieldBinding) {
			return getSolidityWithParent((FieldBinding) var);
		}
		if (var instanceof LocalVariableBinding) {
			return getSolidityWithParent((LocalVariableBinding) var);
		}
		return false;
	}

	public static boolean checkOnNonNull(LocalVariableBinding local) {
		return getSolidityWithParent(local);
	}

	public static boolean getSolidityWithParent(LocalVariableBinding local) {
		if (local.declaration instanceof Argument && local.declaringScope != null && local.declaringScope.referenceContext() instanceof AbstractMethodDeclaration) {
			Argument argument = (Argument) local.declaration;
			AbstractMethodDeclaration decl = (AbstractMethodDeclaration) local.declaringScope.referenceContext();
			for (int param = 0; param < decl.arguments.length; param++)
				if (decl.arguments[param] == argument) {
					return getSolidityWithParent(decl.binding, param);
				}
		} else {
			return getSolidityWithParent(local.getAnnotations()) == FlowInfo.NON_NULL;
		}
		return false;
	}


	public static boolean checkOnNonNull(FieldBinding binding) {
		if (ATTACK) {
			Boolean result=_getSolidityWithParent(binding);
			if (result!=null)
				return result;
			return false;			
		}
		return getSolidityWithParent(binding);
	}
	
	public static boolean getSolidityWithParent(FieldBinding binding) {
		Boolean result=_getSolidityWithParent(binding);
		if (result!=null)
			return result;
		if (NullibilityAnnos.hasSolidAnnotation(binding.declaringClass) == FlowInfo.NON_NULL)
			return true;
		String className = getClassName(binding.declaringClass);
		if ("".equals(className))
			return true;
		return false;
	}	
	
	private static Boolean _getSolidityWithParent(FieldBinding binding) {
		int result = hasSolidAnnotation(binding.getAnnotations());
		if (result != FlowInfo.UNKNOWN) {
			return result == FlowInfo.NON_NULL;
		}
		return null;
	}

	public static FieldBinding getLastFieldBinding(QualifiedNameReference qualifiedNameReference) {
		if (qualifiedNameReference.otherBindings != null && qualifiedNameReference.otherBindings.length > 0) {
			return qualifiedNameReference.otherBindings[qualifiedNameReference.otherBindings.length - 1];
		}
		if (qualifiedNameReference.binding instanceof FieldBinding) {
			return (FieldBinding) qualifiedNameReference.binding;
		}
		return null;
	}

	public static boolean calcNullStatus(QualifiedNameReference qualifiedNameReference) {
		FieldBinding lastFieldBinding = getLastFieldBinding(qualifiedNameReference);
		if (lastFieldBinding != null) {
			return calcNullStatus(lastFieldBinding);
		}
		return false;
	}

	public static boolean checkScope(Scope scope) {
		return interestingScope(scope);
	}

	public static boolean doubleCheck() {
		return enableNullibility() && true;
	}

	public static boolean enableNullibility() {
		return true;
	}

	/**
	 * 
	 * @param binding
	 * @param param
	 * @return {@link FlowInfo#NON_NULL}, {@link FlowInfo#NULL} or
	 *         {@link FlowInfo#UNKNOWN}
	 */
	private static int hasSolidAnnotation(MethodBinding binding, int param) {
		AnnotationBinding[] annos = binding.getAnnotations();
		if (annos != null)
			for (int i = 0; i < annos.length; i++) {
				AnnotationBinding annotation = annos[i];
				ReferenceBinding annotationType = annotation.getAnnotationType();
				if (annotationType != null) {
					String annoName = new String(annotationType.sourceName);
					if (getParamIndex(annoName, "NonNullParam") == param) {
						return FlowInfo.NON_NULL;
					}
					if (getParamIndex(annoName, "CanBeNullParam") == param) {
						return FlowInfo.NULL;
					}
				}
			}
		return FlowInfo.UNKNOWN;

		// AnnotationBinding[][] annos=binding.getParameterAnnotations();
		// if (annos==null)
		// return FlowInfo.UNKNOWN;
		// AnnotationBinding[] anno =
		// param<annos.length?annos[param]:annos[annos.length-1];
		// return hasSolidAnnotation(anno);
	}

	private static boolean calcNullStatus(FieldBinding fieldBinding) {
		boolean result = getSolidityWithParent(fieldBinding);
		if (result) {
			return result;
		}
		if (fieldBinding.isFinal()) {
			return true;
		}
		return false;
	}

	private static int getSolidityWithParent(AnnotationBinding[] annotations) {
		if (hasSolidAnnotation(annotations) == FlowInfo.NON_NULL)
			return FlowInfo.NON_NULL;
		return FlowInfo.UNKNOWN;
	}

	public static int getParamIndex(String annoName, String prefix) {
		if (annoName.startsWith(prefix)) {
			try {
				return Integer.parseInt(annoName.substring(prefix.length())) - 1;
			} catch (java.lang.NumberFormatException e) {
			}
		}
		return -1;
	}

	private static int getSolidityWithParent(AnnotationBinding[] annotationBindings, ReferenceBinding declaringClass) {
		int result = hasSolidAnnotation(annotationBindings);
		if (result == FlowInfo.UNKNOWN && declaringClass != null)
			result = hasSolidAnnotation(declaringClass);
		return result;
	}

	private static int hasSolidAnnotation(ReferenceBinding declaringClass) {
		// return hasSolidAnnotation(declaringClass.getAnnotations());
		return FlowInfo.NON_NULL;
	}

	@SuppressWarnings("unused")
	static private int getSolidity(AnnotationBinding[] annotations, Scope scope) {
		int result = hasSolidAnnotation(annotations);
		if (result != FlowInfo.UNKNOWN) {
			if (result == FlowInfo.NON_NULL)
				return FlowInfo.NON_NULL;
		} else if (interestingScope(scope)) {
			return FlowInfo.NON_NULL;
		}
		return FlowInfo.UNKNOWN;
	}

	private static boolean interestingScope(Scope declaringScope) {
		while (declaringScope != null) {
			if (declaringScope instanceof ClassScope) {
				ClassScope classScope = (ClassScope) declaringScope;
				if (classScope.referenceContext != null)
					if (hasSolidAnnotation(classScope.referenceContext.binding) != FlowInfo.UNKNOWN) {
						return true;
					}
			}
			declaringScope = declaringScope.parent;
		}
		return false;
	}

	private static int hasSolidAnnotation(AnnotationBinding[] annos) {
		if (annos != null)
			for (int i = 0; i < annos.length; i++) {
				AnnotationBinding annotation = annos[i];
				int result = getSolidAnnotation(annotation.getAnnotationType());
				if (result != FlowInfo.UNKNOWN) {
					return result;
				}
			}
		return FlowInfo.UNKNOWN;
	}

	@SuppressWarnings("unused")
	private static int hasSolidAnnotation(Annotation[] annos) {
		if (annos != null)
			for (int i = 0; i < annos.length; i++) {
				Annotation annotation = annos[i];
				if (annotation.getCompilerAnnotation() != null) {
					int result = getSolidAnnotation(annotation.getCompilerAnnotation().getAnnotationType());
					if (result != FlowInfo.UNKNOWN)
						return result;
				}
			}
		return FlowInfo.UNKNOWN;
	}

	public static boolean hasSolidAnnotation(IAnnotationBinding[] annos, String comp) {
		if (annos != null)
			for (int i = 0; i < annos.length; i++) {
				IAnnotationBinding annotation = annos[i];
				if (comp.equals(annotation.getName())) {
					return true;
				}
			}
		return false;
	}

	private static int getSolidAnnotation(ReferenceBinding annotationType) {
		if (annotationType != null) {
			String annoName = new String(annotationType.sourceName);
			if (annoName.endsWith("NonNull")) {
				return FlowInfo.NON_NULL;
			}
			if (annoName.endsWith("CanBeNull")) {
				return FlowInfo.NULL;
			}
			if (annoName.endsWith("NonNull") || annoName.endsWith("Solid")) {
				return FlowInfo.NON_NULL;
			}
		}
		return FlowInfo.UNKNOWN;
	}

	@SuppressWarnings("unused")
	private boolean isSolidFinal(SimpleName simpleName) {
		IBinding typeBinding = simpleName.resolveBinding();
		if (typeBinding instanceof IVariableBinding) {
			IVariableBinding var = (IVariableBinding) typeBinding;
			if (var.isField() && (var.getModifiers() & IModifierConstants.ACC_FINAL) != 0) {
				return true; // assume that a solid field is assigned
			}
		} else if (typeBinding instanceof ITypeBinding) {
			return true;
		}
		return false;
	}

	public static boolean retainCannotBeNull(FlowInfo flowInfo, FlowInfo currentFlow, BlockScope currentScope) {
		UnconditionalFlowInfo unconditionalFlowInfo = flowInfo.unconditionalInits();
		UnconditionalFlowInfo unconditionalCurrentFlow = currentFlow.unconditionalInits();
		if ((getCannotBeNull(unconditionalFlowInfo) & ~getCannotBeNull(unconditionalCurrentFlow)) != 0) {
			// testDoubleCheckWithField*()
			return false;
		}
		for (int i = currentScope.localIndex - 1; i >= 0; i--) {
			LocalVariableBinding local = currentScope.locals[i];
			int position = local.id + unconditionalFlowInfo.maxFieldCount;
			if (position < UnconditionalFlowInfo.BitCacheSize)
				break;
			if (unconditionalFlowInfo.cannotBeNull(local) && !unconditionalCurrentFlow.cannotBeNull(local))
				return false;
		}
		if (currentScope.parent instanceof BlockScope) {
			return retainCannotBeNull(unconditionalFlowInfo, unconditionalCurrentFlow, (BlockScope) currentScope.parent);
		}
		return true;
	}

	private static long getCannotBeNull(UnconditionalFlowInfo flowInfo) {
		return flowInfo.nullBit1 & flowInfo.nullBit3 & (~flowInfo.nullBit2 | flowInfo.nullBit4);
	}

	static public Long getProblemKey(int problemStartPosition, int problemId) {
		return (((long) problemStartPosition) << 32) | problemId;
	}

	public static boolean callNeedValueStore(ReturnStatement rewrite) {
		try {
			Method method = ReturnStatement.class.getDeclaredMethod("needValueStore");
			method.setAccessible(true);
			return (Boolean) method.invoke(rewrite);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isAlwaysNull(Expression t) {
		if (t instanceof NullLiteral)
			return true;
		if (t instanceof CastExpression)
			return isAlwaysNull(((CastExpression) t).expression);
		return false;
	}

	static public void checkEasyNPE(Expression t, BlockScope scope, FlowContext flowContext, FlowInfo flowInfo, ReferenceBinding declaringClass) {
		if (enableNullibility())
			if (isNotNonNull(t, scope, flowInfo)) {
				if (NullibilityAnnos.isAlwaysNull(t)) {
					invalidNullibility(scope.problemReporter(), t, declaringClass, 0, "Easy Nullibility problem"); //$NON-NLS-1$
				} else {
					invalidNullibility(scope.problemReporter(), t, declaringClass, 0, "Nullibility problem"); //$NON-NLS-1$
				}
			}
	}

	public static boolean isNotNonNull(Expression t, BlockScope scope, FlowInfo flowInfo) {
		if ((flowInfo.tagBits & FlowInfo.UNREACHABLE) == 0 && checkScope(scope)) {
			flowInfo.tagBits |= FlowInfo_OnlyUseFlowInfo;
			int status = t.nullStatus(flowInfo);
			flowInfo.tagBits &= ~FlowInfo_OnlyUseFlowInfo;
			return status != FlowInfo.NON_NULL && !(t.resolvedType instanceof BaseTypeBinding && !"null".equals(new String(t.resolvedType.sourceName())));
		}
		return false;
	}

	private static String getClassName(ReferenceBinding declaringClass) {
		if (declaringClass == null)
			return null;
		return new String(declaringClass.fPackage.readableName()) + "." + new String(declaringClass.sourceName);
	}

	/**
	 * Finds the highest super method
	 * 
	 * @param methodBinding
	 * @return the highest super method, which may by the passed method itself,
	 *         or <code>null</code> if search failed
	 */
	static public MethodBinding findSuperMethod(MethodBinding methodBinding) {
		ReferenceBinding decl = methodBinding.declaringClass;
		LookupEnvironment environment = null;
		if (decl instanceof SourceTypeBinding) {
			SourceTypeBinding sourceTypeBinding = (SourceTypeBinding) decl;
			if (sourceTypeBinding.scope != null) {
				environment = sourceTypeBinding.scope.environment();
				if (environment == null)
					return null;
			} else if (sourceTypeBinding.fPackage != null) {
				environment = sourceTypeBinding.fPackage.environment;
				if (environment == null)
					return null;
			} else
				return null;
		} else if (decl instanceof BinaryTypeBinding) {
			BinaryTypeBinding binaryTypeBinding = (BinaryTypeBinding) decl;
			environment = getEnvironment(binaryTypeBinding);
			if (environment == null)
				return null;
		} else if (decl instanceof ParameterizedTypeBinding) {
			ParameterizedTypeBinding parameterizedTypeBinding = (ParameterizedTypeBinding) decl;
			environment = parameterizedTypeBinding.environment;
			if (environment == null)
				return null;
		}
		if (environment != null) {
			return methodBinding.getHighestOverridenMethod(environment);
		}
		return null;
		/*
		 * ReferenceBinding superClass = decl.superclass(); if (superClass !=
		 * null) { MethodBinding superMethod = getSuperMethod(superClass,
		 * methodBinding); if (superMethod != null) { return superMethod; }
		 * superMethod = findSuperMethod(superClass, methodBinding); if
		 * (superMethod != null) { return superMethod; } } ReferenceBinding[]
		 * interfaces = decl.superInterfaces(); for (int i=0;
		 * i<interfaces.length; i++) { ReferenceBinding binding = interfaces[i];
		 * MethodBinding superMethod = getSuperMethod(binding, methodBinding);
		 * if (superMethod != null) { return superMethod; } superMethod =
		 * findSuperMethod(binding, methodBinding); if (superMethod != null) {
		 * return superMethod; } } return null;
		 */
	}

	private static LookupEnvironment getEnvironment(BinaryTypeBinding binaryTypeBinding) {
		Field field;
		try {
			field = binaryTypeBinding.getClass().getDeclaredField("environment");
		} catch (SecurityException e) {
			return null;
		} catch (NoSuchFieldException e) {
			return null;
		}
		try {
			field.setAccessible(true);
			return (LookupEnvironment) field.get(binaryTypeBinding);
		} catch (IllegalArgumentException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		}
	}

	/*
	 * private MethodBinding getSuperMethod(ReferenceBinding binding,
	 * MethodBinding methodBinding) { for (MethodBinding superMethod :
	 * binding.getMethods(null)) { if (!hasOverrideAnnotation(superMethod) &&
	 * methodBinding.overrides(superMethod)) { return superMethod; } } return
	 * null; }
	 */

	static public void invalidNullibility(ProblemReporter _this, ASTNode causingExpr, ReferenceBinding declaringClass, int param, String caption) {
		// DefaultProblemFactory..messageTemplates; new DefaultProblemFactory()
		if (_this.problemFactory instanceof DefaultProblemFactory)
			((DefaultProblemFactory) _this.problemFactory).messageTemplates.put((RequireCanBeNull & IProblem.IgnoreCategoriesMask) + 1, caption);
		handle(_this, RequireCanBeNull, ProblemHandler.NoArgument, ProblemHandler.NoArgument, causingExpr.sourceStart, causingExpr.sourceEnd, declaringClass);

	}

	static public void invalidNullibility(ProblemReporter _this, Binding local, ASTNode location, ReferenceBinding declaringClass, int param, String caption) {
		// DefaultProblemFactory..messageTemplates; new DefaultProblemFactory()
		if (_this.problemFactory instanceof DefaultProblemFactory)
			((DefaultProblemFactory) _this.problemFactory).messageTemplates.put((RequireCanBeNull & IProblem.IgnoreCategoriesMask) + 1, caption);
		handle(_this, RequireCanBeNull, ProblemHandler.NoArgument, ProblemHandler.NoArgument, callNodeSourceStart(_this, local, location), callNodeSourceEnd(_this, local, location), declaringClass);

	}

	private static int callNodeSourceStart(ProblemReporter this1, Binding local, ASTNode location) {
		try {
			Method method = ProblemReporter.class.getDeclaredMethod("nodeSourceStart", Binding.class, ASTNode.class);
			method.setAccessible(true);
			return (Integer) method.invoke(this1, local, location);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	private static int callNodeSourceEnd(ProblemReporter this1, Binding local, ASTNode location) {
		try {
			Method method = ProblemReporter.class.getDeclaredMethod("nodeSourceEnd", Binding.class, ASTNode.class);
			method.setAccessible(true);
			return (Integer) method.invoke(this1, local, location);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	static private void handle(ProblemReporter _this, int problemId, String[] problemArguments, String[] messageArguments, int problemStartPosition, int problemEndPosition, ReferenceBinding declaringClass) {
		if (_this.referenceContext == null)
			return;
		int severity = ProblemSeverities.Error;
//		if (declaringClass != null && declaringClass.isBinaryBinding()) {
//			severity = ProblemSeverities.Warning;
//			com.sun.jdi.VirtualMachine m;
//		}
		_this.handle(problemId, problemArguments, 0, // no message elaboration
				messageArguments, severity, problemStartPosition, problemEndPosition, _this.referenceContext, _this.referenceContext == null ? null : _this.referenceContext.compilationResult());
		_this.referenceContext = null;
	}

	static private void addSuperInterface(ReferenceBinding decl, Set<String> result) {
		ReferenceBinding[] superInterfaces = decl.superInterfaces();
		for (int i = 0; i < superInterfaces.length; i++) {
			if (result.add(getClassName(superInterfaces[i])))
				addSuperInterface(superInterfaces[i], result);
		}
	}

	@SuppressWarnings("unused")
	static private void addSuperClasses(ReferenceBinding decl, Set<String> result) {
		addSuperInterface(decl, result);
		while ((decl = decl.superclass()) != null) {
			if (!result.add(getClassName(decl))) {
				return;
			}
			addSuperInterface(decl, result);
		}
	}

	public static boolean checkFieldAssignment(Assignment assignment) {
		if (ALLOW_ASSIGN_CANBENULL_TO_FIELD)
			return false;
		if (ALLOW_ASSIGN_NULL_TO_FIELD) {
			if (assignment.expression instanceof NullLiteral)
				return false;
			if (assignment.expression instanceof Assignment)
				return checkFieldAssignment((Assignment) assignment.expression);
		}
		return true;
	}

}
