package pingpong.jdt.core.classes;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class NPContext {

	public ICompilationUnit cu;
	public CompilationUnit astRoot;

	public NPContext(ICompilationUnit cu, CompilationUnit astRoot) {
		this.cu = cu;
		this.astRoot = astRoot;
	}

	public NPContext duplicate() {
		return new NPContext(cu, astRoot);
	}

}
