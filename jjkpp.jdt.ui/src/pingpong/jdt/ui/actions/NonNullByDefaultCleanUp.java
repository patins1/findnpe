package pingpong.jdt.ui.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.internal.ui.fix.AbstractCleanUp;
import org.eclipse.jdt.ui.cleanup.CleanUpContext;
import org.eclipse.jdt.ui.cleanup.CleanUpRequirements;
import org.eclipse.jdt.ui.cleanup.ICleanUpFix;
import org.eclipse.jdt.ui.text.java.IProblemLocation;

import pingpong.jdt.ui.classes.quickfix.NullibilityCodeFix;

public class NonNullByDefaultCleanUp extends AbstractCleanUp {

	public NonNullByDefaultCleanUp() {
		super();
	}

	public NonNullByDefaultCleanUp(Map options) {
		super(options);
	}

	public CleanUpRequirements getRequirements() {
		return new CleanUpRequirements(true, true, false, null);
	}

	public ICleanUpFix createFix(CleanUpContext context) throws CoreException {
		CompilationUnit compilationUnit = context.getAST();
		if (compilationUnit == null)
			return null;
		List<AbstractTypeDeclaration> tlts = compilationUnit.types();
		List<IProblemLocation> problems = new ArrayList<IProblemLocation>();
		for (final AbstractTypeDeclaration tlt : tlts) {
			IProblemLocation problem = new IProblemLocation() {

				@Override
				public ASTNode getCoveredNode(CompilationUnit astRoot) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public ASTNode getCoveringNode(CompilationUnit astRoot) {
					return tlt;
				}

				@Override
				public int getLength() {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public String getMarkerType() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public int getOffset() {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public String[] getProblemArguments() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public int getProblemId() {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public boolean isError() {
					// TODO Auto-generated method stub
					return false;
				}
			};
			problems.add(problem);
		}
		return NullibilityCodeFix.createCleanUp(compilationUnit, problems.toArray(new IProblemLocation[] {}));
	}

	public String[] getStepDescriptions() {
		ArrayList result = new ArrayList();
		if (isEnabled(CleanUpConstants.ANNOTATE_NONNULLBYDEFAULT))
			result.add(MultiFixMessages.NonNullByDefaultCleanUp_description);
		return (String[]) result.toArray(new String[result.size()]);
	}

	public String getPreview() {
		StringBuffer buf = new StringBuffer();
		buf.append("/**\n"); //$NON-NLS-1$
		buf.append(" *A Javadoc comment\n"); //$NON-NLS-1$
		buf.append("* @since 2007\n"); //$NON-NLS-1$
		buf.append(" */\n"); //$NON-NLS-1$
		buf.append("@NonNullByDefault\n"); //$NON-NLS-1$
		buf.append("public class Engine {\n"); //$NON-NLS-1$
		buf.append("  public void start() {}\n"); //$NON-NLS-1$		
		buf.append("        void stop() {\n"); //$NON-NLS-1$
		buf.append("    }\n"); //$NON-NLS-1$
		buf.append("}\n"); //$NON-NLS-1$

		return buf.toString();
	}
}
