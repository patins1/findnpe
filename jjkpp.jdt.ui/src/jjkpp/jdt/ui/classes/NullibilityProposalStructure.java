package jjkpp.jdt.ui.classes;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class NullibilityProposalStructure {

	public final String name;

	public final ICompilationUnit cu;
	public final int relevance;
	public final ASTNode decl;
	public final String marker;

	public final ChildListPropertyDescriptor modifiers;

	public NullibilityProposalStructure(String name, ICompilationUnit cu, ASTNode rewrite, int relevance, String marker, ChildListPropertyDescriptor modifiers) {
		this.name = name;
		this.cu = cu;
		this.decl = rewrite;
		this.relevance = relevance;
		this.marker = marker;
		this.modifiers = modifiers;
	}

	public String getImport() {
		return "jjkpp.jdt.annotations." + marker;
	}

	public AST getAst() {
		return decl.getAST();
	}

	public CompilationUnit getRoot() {
		return (CompilationUnit) decl.getRoot();
	}

}
