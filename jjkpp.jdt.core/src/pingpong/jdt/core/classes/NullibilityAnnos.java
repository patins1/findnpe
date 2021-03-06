package pingpong.jdt.core.classes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import org.eclipse.jdt.core.IAnnotation;
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
import org.eclipse.jdt.internal.compiler.ast.MessageSend;
import org.eclipse.jdt.internal.compiler.ast.NameReference;
import org.eclipse.jdt.internal.compiler.ast.NullLiteral;
import org.eclipse.jdt.internal.compiler.ast.QualifiedNameReference;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;
import org.eclipse.jdt.internal.compiler.flow.ConditionalFlowInfo;
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
import org.eclipse.jdt.internal.compiler.lookup.MethodVerifier;
import org.eclipse.jdt.internal.compiler.lookup.PackageBinding;
import org.eclipse.jdt.internal.compiler.lookup.ParameterizedTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.ReferenceBinding;
import org.eclipse.jdt.internal.compiler.lookup.Scope;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TagBits;
import org.eclipse.jdt.internal.compiler.lookup.TypeBinding;
import org.eclipse.jdt.internal.compiler.lookup.TypeConstants;
import org.eclipse.jdt.internal.compiler.lookup.TypeIds;
import org.eclipse.jdt.internal.compiler.lookup.VariableBinding;
import org.eclipse.jdt.internal.compiler.problem.DefaultProblemFactory;
import org.eclipse.jdt.internal.compiler.problem.ProblemHandler;
import org.eclipse.jdt.internal.compiler.problem.ProblemReporter;
import org.eclipse.jdt.internal.compiler.problem.ProblemSeverities;

@SuppressWarnings("restriction")
public class NullibilityAnnos {

	public static boolean ATTACK = true;

	static public int RequireCanBeNull = IProblem.MethodRelated + 148;

	private static final boolean ALLOW_ASSIGN_NULL_TO_FIELD = true;

	private static final boolean ALLOW_ASSIGN_CANBENULL_TO_FIELD = ALLOW_ASSIGN_NULL_TO_FIELD && false;

	public static final boolean USE_PARAM_ANNOS = true;

	final static public String NPE_HAZARD = "NPE hazard";

	final static public String EXPECTED_NONNULL = "Expected NonNull value";
	
	final static public String EXPECTED_NONNULL_INSTEADOF_NULL = "Expected NonNull value, but value is always null";

	final static public String ANNOTATION_PROBLEM = "FindNPE annotation problem: ";

	/**
	 * Flag indicating the the null-status-method shall use only information
	 * from the flow info, but not from the ASTNode itself; see FlowInfo for
	 * free flags, tested by testDoubleCheckUsingOnlyOneExpressionAsBodyI
	 */
	public static final int FlowInfo_OnlyUseFlowInfo = 4;

	public static boolean checkOnNonNull(MethodBinding binding) {
		if (ATTACK) {
			return _getSolidityWithParent(binding) == FlowInfo.NON_NULL;
		}
		return getSolidityWithParent(binding);
	}

	public static boolean getSolidityWithParent(MethodBinding binding) {
		return _getSolidityWithParent(binding) != FlowInfo.NULL;
	}

	private static int _getSolidityWithParent(MethodBinding binding) {
		if (!enableAnnotations())
			return FlowInfo.UNKNOWN;
		int result = NullibilityAnnos.hasSolidAnnotation(binding.getAnnotations());
		if (result != FlowInfo.UNKNOWN) {
			return result;
		}
		MethodBinding superMethod = findSuperMethod(binding);
		if (superMethod != null && superMethod != binding) {
			result = _getSolidityWithParent(superMethod);
			if (result != FlowInfo.UNKNOWN) {
				return result;
			}
		}
		return NullibilityAnnos.hasSolidAnnotation(binding.declaringClass);
	}

	public static boolean checkOnNonNull(MethodBinding binding, int param) {
		if (ATTACK) {
			return _getSolidityWithParent(binding, param) == FlowInfo.NON_NULL;
		}
		return getSolidityWithParent(binding, param);
	}

	public static boolean getSolidityWithParent(MethodBinding binding, int param) {
		return _getSolidityWithParent(binding, param) != FlowInfo.NULL;
	}

	private static int _getSolidityWithParent(MethodBinding binding, int param) {
		if (!enableAnnotations())
			return FlowInfo.UNKNOWN;
		int result = NullibilityAnnos.hasSolidAnnotation(binding, param);
		if (result != FlowInfo.UNKNOWN) {
			return result;
		}
		MethodBinding superMethod = findSuperMethod(binding);
		if (superMethod != null && superMethod != binding) {
			result = _getSolidityWithParent(superMethod, param);
			if (result != FlowInfo.UNKNOWN) {
				return result;
			}
		}
		return NullibilityAnnos.hasSolidAnnotation(binding.declaringClass);
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
			// Argument argument = (Argument) local.declaration;
			// AbstractMethodDeclaration decl = (AbstractMethodDeclaration)
			// local.declaringScope.referenceContext();
			// for (int param = 0; param < decl.arguments.length; param++)
			// if (decl.arguments[param] == argument) {
			// return getSolidityWithParent(decl.binding, param);
			// }
		} else {
			return getSolidityWithParent(local.getAnnotations()) == FlowInfo.NON_NULL;
		}
		return false;
	}

	public static boolean checkOnNonNull(FieldBinding binding) {
		if (ATTACK) {
			return _getSolidityWithParent(binding) == FlowInfo.NON_NULL;
		}
		return getSolidityWithParent(binding);
	}

	public static boolean getSolidityWithParent(FieldBinding binding) {
		return _getSolidityWithParent(binding) != FlowInfo.NULL;
	}

	private static int _getSolidityWithParent(FieldBinding binding) {
		return hasSolidAnnotation(binding.getAnnotations());
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
		return true;
	}

	public static boolean doubleCheck() {
		return enableNullibility() && true;
	}

	public static boolean enableNullibility() {
		return true;
	}

	public static boolean enableNullibilityFields() {
		return enableNullibility() && true;
	}

	public static boolean enableAnnotations() {
		return enableNullibility() && true;
	}

	public static boolean enableParameterAnnotations() {
		return enableAnnotations() && USE_PARAM_ANNOS;
	}

	public static boolean enableInterpretationNonNull() {
		return enableNullibility() && true;
	}

	public static boolean enableInterpretationAlwaysThrow() {
		return enableNullibility() && false;
	}

	/**
	 * 
	 * @param binding
	 * @param param
	 * @return {@link FlowInfo#NON_NULL}, {@link FlowInfo#NULL} or
	 *         {@link FlowInfo#UNKNOWN}
	 */
	public static int hasSolidAnnotation(MethodBinding binding, int param) {
		if (param == -1)
			return hasSolidAnnotation(binding.getAnnotations());
		if (!USE_PARAM_ANNOS) {
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
		}
		binding = binding.original(); // testAnnotatedParametrizedParameter()
		AnnotationBinding[][] annos = binding.getParameterAnnotations();
		if (annos == null)
			return FlowInfo.UNKNOWN;
		AnnotationBinding[] anno = param < annos.length ? annos[param] : annos[annos.length - 1];
		if (anno.length == 0) {
			// quick fix for: there may be annotations not returned!
			// testCallBuildClean()
			/**
			 * see MethodBinding#getParameterAnnotations()
			 */
			if (binding.declaringClass instanceof SourceTypeBinding) {
				SourceTypeBinding sourceType = (SourceTypeBinding) binding.declaringClass;
				if (sourceType.scope != null) {
					AbstractMethodDeclaration methodDecl = sourceType.scope.referenceType().declarationOf(binding);
					if (methodDecl.arguments != null && param < methodDecl.arguments.length) {
						Argument argument = methodDecl.arguments[param];
						if (argument.annotations != null) {
							return hasSolidAnnotation(argument.annotations);
						}
					}
				}
			}
		}
		return hasSolidAnnotation(anno);
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

	private static int hasSolidAnnotation(ReferenceBinding declaringClass) {
		ReferenceBinding current = declaringClass;
		do {
			int result = hasSolidAnnotation(current.getAnnotations());
			if (result != FlowInfo.UNKNOWN) {
				return result;
			}
			current = current.enclosingType();
		} while (current != null);

		PackageBinding pack = declaringClass.getPackage();
		while (pack != null) {
			Binding binding = pack.getTypeOrPackage(TypeConstants.PACKAGE_INFO_NAME);
			if (binding instanceof ReferenceBinding) {
				ReferenceBinding referenceBinding = (ReferenceBinding) binding;
				int result = hasSolidAnnotation(referenceBinding.getAnnotations());
				if (result != FlowInfo.UNKNOWN) {
					return result;
				}
			}
			pack = getParentPackage(pack);
		}
		return FlowInfo.UNKNOWN;
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

	public static int hasSolidAnnotation(AnnotationBinding[] annos) {
		if (enableAnnotations())
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
	public static int hasSolidAnnotation(Annotation[] annos) {
		if (enableAnnotations())
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
		if (enableAnnotations())
			if (annos != null)
				for (int i = 0; i < annos.length; i++) {
					IAnnotationBinding annotation = annos[i];
					if (comp.equals(annotation.getName())) {
						return true;
					}
				}
		return false;
	}

	public static int hasSolidAnnotation(IAnnotationBinding[] annos) {
		if (enableAnnotations())
			if (annos != null)
				for (int i = 0; i < annos.length; i++) {
					IAnnotationBinding annotation = annos[i];
					String annoName = annotation.getName();
					if (annoName.endsWith("NonNull")) {
						return FlowInfo.NON_NULL;
					}
					if (annoName.endsWith("CanBeNull")) {
						return FlowInfo.NULL;
					}
				}
		return FlowInfo.UNKNOWN;
	}

	public static int hasSolidAnnotation(IAnnotation[] annos) {
		if (enableAnnotations())
			if (annos != null)
				for (int i = 0; i < annos.length; i++) {
					IAnnotation annotation = annos[i];
					String annoName = annotation.getElementName();
					if (annoName.endsWith("NonNull")) {
						return FlowInfo.NON_NULL;
					}
					if (annoName.endsWith("CanBeNull")) {
						return FlowInfo.NULL;
					}
				}
		return FlowInfo.UNKNOWN;
	}

	private static int getSolidAnnotation(ReferenceBinding annotationType) {
		if (annotationType != null) {
			String annoName = new String(annotationType.sourceName);
			if (annoName.endsWith("NonNull") || annoName.endsWith("NonNullByDefault")) {
				return FlowInfo.NON_NULL;
			}
			if (annoName.endsWith("CanBeNull")) {
				return FlowInfo.NULL;
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
		if ((flowInfo.tagBits & currentFlow.tagBits & FlowInfo.NULL_FLAG_MASK) == 0)
			return true;
		if (((flowInfo.tagBits | currentFlow.tagBits) & FlowInfo.UNREACHABLE) != 0)
			return true; // testFinallyUnreachable()
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
					invalidNullibility(scope.problemReporter(), t, declaringClass, 0, EXPECTED_NONNULL_INSTEADOF_NULL); //$NON-NLS-1$
				} else {
					invalidNullibility(scope.problemReporter(), t, declaringClass, 0, EXPECTED_NONNULL); //$NON-NLS-1$
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
	 * Return the next higher method/constructor in supertype hierarchy with
	 * same selector and arguments; In contrast to
	 * {@link MethodBinding#getHighestOverridenMethod(LookupEnvironment), the
	 * highest method is not returned necessarily, by using the condition
	 * <code>bestMethod == t</code>.
	 */
	static public MethodBinding getHigherOverridenMethod(MethodBinding t, LookupEnvironment environment) {
		MethodBinding bestMethod = t;
		ReferenceBinding currentType = t.declaringClass;
		if (t.isConstructor()) {
			// walk superclasses - only
			do {
				MethodBinding superMethod = currentType.getExactConstructor(t.parameters);
				if (superMethod != null) {
					bestMethod = superMethod;
				}
			} while ((currentType = currentType.superclass()) != null && bestMethod == t);
			return bestMethod;
		}
		MethodVerifier verifier = environment.methodVerifier();
		// walk superclasses
		ReferenceBinding[] interfacesToVisit = null;
		int nextPosition = 0;
		do {
			MethodBinding[] superMethods = currentType.getMethods(t.selector);
			for (int i = 0, length = superMethods.length; i < length; i++) {
				if (verifier.doesMethodOverride(t, superMethods[i])) {
					bestMethod = superMethods[i];
					break;
				}
			}
			ReferenceBinding[] itsInterfaces = currentType.superInterfaces();
			if (itsInterfaces != null && itsInterfaces != Binding.NO_SUPERINTERFACES) {
				if (interfacesToVisit == null) {
					interfacesToVisit = itsInterfaces;
					nextPosition = interfacesToVisit.length;
				} else {
					int itsLength = itsInterfaces.length;
					if (nextPosition + itsLength >= interfacesToVisit.length)
						System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[nextPosition + itsLength + 5], 0, nextPosition);
					nextInterface: for (int a = 0; a < itsLength; a++) {
						ReferenceBinding next = itsInterfaces[a];
						for (int b = 0; b < nextPosition; b++)
							if (next == interfacesToVisit[b])
								continue nextInterface;
						interfacesToVisit[nextPosition++] = next;
					}
				}
			}
		} while ((currentType = currentType.superclass()) != null && bestMethod == t);
		if (bestMethod.declaringClass.id == TypeIds.T_JavaLangObject) {
			return bestMethod;
		}
		// walk superinterfaces
		for (int i = 0; i < nextPosition; i++) {
			currentType = interfacesToVisit[i];
			MethodBinding[] superMethods = currentType.getMethods(t.selector);
			for (int j = 0, length = superMethods.length; j < length; j++) {
				MethodBinding superMethod = superMethods[j];
				if (verifier.doesMethodOverride(t, superMethod)) {
					TypeBinding bestReturnType = bestMethod.returnType;
					if (bestReturnType == superMethod.returnType || bestMethod.returnType.findSuperTypeOriginatingFrom(superMethod.returnType) != null) {
						bestMethod = superMethod;
					}
					break;
				}
			}
			ReferenceBinding[] itsInterfaces = currentType.superInterfaces();
			if (itsInterfaces != null && itsInterfaces != Binding.NO_SUPERINTERFACES) {
				int itsLength = itsInterfaces.length;
				if (nextPosition + itsLength >= interfacesToVisit.length)
					System.arraycopy(interfacesToVisit, 0, interfacesToVisit = new ReferenceBinding[nextPosition + itsLength + 5], 0, nextPosition);
				nextInterface: for (int a = 0; a < itsLength; a++) {
					ReferenceBinding next = itsInterfaces[a];
					for (int b = 0; b < nextPosition; b++)
						if (next == interfacesToVisit[b])
							continue nextInterface;
					interfacesToVisit[nextPosition++] = next;
				}
			}
		}
		return bestMethod;
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
			return getHigherOverridenMethod(methodBinding, environment);
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

	private static PackageBinding getParentPackage(PackageBinding pack) {
		try {
			Field field = PackageBinding.class.getDeclaredField("parent");
			field.setAccessible(true);
			return (PackageBinding) field.get(pack);
		} catch (Exception e) {
			return null;
		}
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
		// if (declaringClass != null && declaringClass.isBinaryBinding()) {
		// severity = ProblemSeverities.Warning;
		// com.sun.jdi.VirtualMachine m;
		// }
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

	public static UnconditionalFlowInfo mergedWithPrepare(UnconditionalFlowInfo a, UnconditionalFlowInfo b) {
		if ((a.tagBits & b.tagBits & FlowInfo.NULL_FLAG_MASK) == 0)
			return b;
		if (((a.tagBits | b.tagBits) & FlowInfo.UNREACHABLE) != 0)
			return b;
		UnconditionalFlowInfo otherInits = b;
		long mask, a1 = a.nullBit1, a2 = a.nullBit2, a3 = a.nullBit3, a4 = a.nullBit4, b1 = b.nullBit1, b2 = b.nullBit2, b3 = b.nullBit3, b4 = b.nullBit4;
		mask = ~(a1 & a2 & a3 & ~a4 & b1 & ~b2 & b3 & ~b4);
		a.nullBit2 = a2 = a2 & mask; // testMergeProtNullAndDefNN
		mask = ~(b1 & b2 & b3 & ~b4 & a1 & ~a2 & a3 & ~a4);
		if ((b2 & mask) != b2) {
			b = (UnconditionalFlowInfo) otherInits.copy(); // testPreMerge
			b.nullBit2 = b2 & mask; // testMergeDefNNAndProtNull
		}
		return b;
		// TODO extra
	}

	/**
	 * InterpretationsTest
	 * 
	 * @param pack
	 * @param t
	 * @param flowInfo
	 * @param pos
	 * @return <code>true</code> if matched
	 */
	public static boolean interprete3rdParty(String pack, MessageSend t, FlowInfo flowInfo, int pos) {
		if (t.receiver instanceof NameReference) {
			NameReference nf = (NameReference) t.receiver;
			if (nf.binding instanceof ReferenceBinding) {
				ReferenceBinding nrf = (ReferenceBinding) nf.binding;
				if (pack.equals(new String(nrf.readableName()))) {
					if (pos == -1) {
						return true;
					}
					Expression last = t.arguments[pos];
					LocalVariableBinding local = last.localVariableBinding();
					if (local != null && (local.type.tagBits & TagBits.IsBaseType) == 0) {
						NullibilityAnnos.assureExtraLargeEnough(flowInfo, local);
						flowInfo.markAsDefinitelyNonNull(local);
						return true;
					}
				}

			}
		}
		return false;
	}

	/**
	 * testExtraLargeEnough()
	 */
	static public void assureExtraLargeEnough(FlowInfo flowInfo, LocalVariableBinding local) {
		if (!NullibilityAnnos.enableNullibilityFields())
			return;
		if (!(local.id < 0))
			return; // if no faked field, return
		if (flowInfo instanceof UnconditionalFlowInfo) {
			UnconditionalFlowInfo t = (UnconditionalFlowInfo) flowInfo;
			assureExtraLargeEnough(t, local.id + t.maxFieldCount);
		} else if (flowInfo instanceof ConditionalFlowInfo) {
			ConditionalFlowInfo c = (ConditionalFlowInfo) flowInfo;
			assureExtraLargeEnough(c.initsWhenTrue, local);
			assureExtraLargeEnough(c.initsWhenFalse, local);
		}
	}

	/**
	 * Assures that the given position is allocated in
	 * {@link UnconditionalFlowInfo#extra} if required, so that
	 * {@link UnconditionalFlowInfo#markAsDefinitelyNonNull(LocalVariableBinding)}
	 * /
	 * {@link UnconditionalFlowInfo#markAsDefinitelyNull(LocalVariableBinding)}
	 * /
	 * {@link UnconditionalFlowInfo#markAsDefinitelyUnknown(LocalVariableBinding)}
	 * work which assumes a correct allocation
	 * 
	 * @param t
	 * @param position
	 */
	static private void assureExtraLargeEnough(UnconditionalFlowInfo t, int position) {

		if (t != FlowInfo.DEAD_END) {
			// position is zero-based
			if (position < UnconditionalFlowInfo.BitCacheSize) {
				// nothing to do
			} else {
				// use extra vector
				int vectorIndex = (position / UnconditionalFlowInfo.BitCacheSize) - 1;
				if (t.extra == null) {
					int length = vectorIndex + 1;
					t.extra = new long[t.extraLength][];
					for (int j = 0; j < t.extraLength; j++) {
						t.extra[j] = new long[length];
					}
				} else {
					int oldLength; // might need to grow the arrays
					if (vectorIndex >= (oldLength = t.extra[0].length)) {
						for (int j = 0; j < t.extraLength; j++) {
							System.arraycopy(t.extra[j], 0, (t.extra[j] = new long[vectorIndex + 1]), 0, oldLength);
						}
					}
				}
			}
		}
	}

}
