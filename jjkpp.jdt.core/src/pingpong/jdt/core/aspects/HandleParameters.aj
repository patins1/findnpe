package pingpong.jdt.core.aspects;


import java.util.Arrays;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.jdt.internal.compiler.CompilationResult;
import org.eclipse.jdt.internal.compiler.ISourceElementRequestor.MethodInfo;
import org.eclipse.jdt.internal.compiler.ast.AbstractMethodDeclaration;
import org.eclipse.jdt.internal.compiler.ast.Argument;
import org.eclipse.jdt.internal.compiler.classfmt.AnnotationInfo;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileReader;
import org.eclipse.jdt.internal.compiler.env.IBinaryAnnotation;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;
import org.eclipse.jdt.internal.compiler.parser.SourceTypeConverter;
import org.eclipse.jdt.internal.core.CompilationUnitStructureRequestor;
import org.eclipse.jdt.internal.core.JavaElementDeltaBuilder;
import org.eclipse.jdt.internal.core.JavaElementInfo;
import org.eclipse.jdt.internal.core.SourceMethod;
import org.eclipse.jdt.internal.core.SourceMethodElementInfo;

import pingpong.jdt.core.classes.NullibilityAnnos;

@SuppressWarnings("restriction")
privileged public aspect HandleParameters {

	private Argument[] SourceMethodElementInfo.originalArguments;

	boolean around(ClassFileReader t, org.eclipse.jdt.internal.compiler.classfmt.MethodInfo currentMethodInfo, org.eclipse.jdt.internal.compiler.classfmt.MethodInfo otherMethodInfo) : 
		call(boolean hasStructuralMethodChanges(org.eclipse.jdt.internal.compiler.classfmt.MethodInfo, org.eclipse.jdt.internal.compiler.classfmt.MethodInfo)) && args(currentMethodInfo, otherMethodInfo) && target(t) {

		boolean result=proceed(t,currentMethodInfo,otherMethodInfo);
		if (!NullibilityAnnos.enableParameterAnnotations()) return result; 	
		if (result)
			return true;
		AnnotationInfo[][] parameterAnnotations1 = null;
		if (currentMethodInfo instanceof org.eclipse.jdt.internal.compiler.classfmt.MethodInfoWithParameterAnnotations) {
			parameterAnnotations1=((org.eclipse.jdt.internal.compiler.classfmt.MethodInfoWithParameterAnnotations)currentMethodInfo).parameterAnnotations;
		}
		AnnotationInfo[][] parameterAnnotations2 = null;
		if (otherMethodInfo instanceof org.eclipse.jdt.internal.compiler.classfmt.MethodInfoWithParameterAnnotations) {
			parameterAnnotations2=((org.eclipse.jdt.internal.compiler.classfmt.MethodInfoWithParameterAnnotations)otherMethodInfo).parameterAnnotations;
		}
//		org.eclipse.jdt.internal.compiler.classfmt.MethodInfoWithAnnotations x=(org.eclipse.jdt.internal.compiler.classfmt.MethodInfoWithParameterAnnotations)currentMethodInfo;
		
		if ((parameterAnnotations1==null) != (parameterAnnotations2==null)) {
			// if one version has parameter annotations, the other not, report a change;
			// note that MethodInfoWithParameterAnnotations is exactly instantiated if there is at least one parameter annotation
			// testAbstractParameterOverrideProposal2()
			return true;
		}
		if (parameterAnnotations1==null) {
			// if both have no parameter annotations, report no change
			return false;
		}
		if (parameterAnnotations1.length != parameterAnnotations2.length) {
			// this case should not happen
			return false;
		}
		
		for (int index=0; index<parameterAnnotations1.length; index++) {
			IBinaryAnnotation[] annos1=parameterAnnotations1[index];
			IBinaryAnnotation[] annos2=parameterAnnotations2[index];
			if (!Arrays.equals(annos1, annos2)) {
				return true;
			}			
		}
		
		return false;
	}

	after(CompilationUnitStructureRequestor t, MethodInfo methodInfo, SourceMethod handle) returning (SourceMethodElementInfo result) : 
		call(SourceMethodElementInfo createMethodInfo(MethodInfo, SourceMethod)) && args(methodInfo,handle) && target(t) {

		// AttactTest.testNonNullParam()
		if (NullibilityAnnos.enableParameterAnnotations())
			if (methodInfo.node != null) {
				result.originalArguments = methodInfo.node.arguments;
			}
	}

	after(SourceTypeConverter t, SourceMethod methodHandle, SourceMethodElementInfo methodInfo, CompilationResult compilationResult) returning (AbstractMethodDeclaration result) : 
		call(AbstractMethodDeclaration convert(SourceMethod, SourceMethodElementInfo, CompilationResult)) && args(methodHandle,methodInfo,compilationResult) && target(t) {

		// AttactTest.testNonNullParam()
		if (NullibilityAnnos.enableParameterAnnotations())
			if (methodInfo.originalArguments != null && result.arguments != null && methodInfo.originalArguments.length == result.arguments.length) {
				for (int i=result.arguments.length-1; i>=0; i--) {
					Argument argument = result.arguments[i];
					Argument originalArgument = methodInfo.originalArguments[i];
					if (argument.annotations == null)
						argument.annotations = originalArgument.annotations;
				}
			}
	}
	
	after(JavaElementDeltaBuilder t, JavaElementInfo oldInfo, JavaElementInfo newInfo, IJavaElement newElement) returning: 
		call(void findContentChange(JavaElementInfo, JavaElementInfo, IJavaElement)) && args(oldInfo, newInfo, newElement) && target(t) {

		if (!NullibilityAnnos.enableParameterAnnotations()) return;
		
		if (oldInfo instanceof SourceMethodElementInfo && newInfo instanceof SourceMethodElementInfo) {
			Argument[] oldArguments = ((SourceMethodElementInfo)oldInfo).originalArguments;
			Argument[] newArguments = ((SourceMethodElementInfo)newInfo).originalArguments;
			if (oldArguments!=null && newArguments!=null && oldArguments.length==newArguments.length && oldArguments!=newArguments) {
				for (int i = 0; i < oldArguments.length; i++) {
					if (NullibilityAnnos.hasSolidAnnotation(oldArguments[i].annotations)!=NullibilityAnnos.hasSolidAnnotation(newArguments[i].annotations)) {
						t.delta.changed(newElement, IJavaElementDelta.F_CONTENT);
						return;
					}
				}
			}
		}
	}

	after(CompilerOptions t, boolean newval): 
		set(public boolean CompilerOptions.storeAnnotations) && args(newval) && target(t) {
		
		if (NullibilityAnnos.enableAnnotations()) 	
		if (!t.storeAnnotations)
			t.storeAnnotations=true; 
	 }

}
