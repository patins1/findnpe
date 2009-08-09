package jjkpp.firstfix;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickFixProcessor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

public class QuickFixProcessor1 implements IQuickFixProcessor {

	@Override
	public IJavaCompletionProposal[] getCorrections(IInvocationContext context,
			IProblemLocation[] locations) throws CoreException {
		// TODO Auto-generated method stub
		return new IJavaCompletionProposal[]{
				
				new IJavaCompletionProposal() {

					@Override
					public int getRelevance() {
						// TODO Auto-generated method stub
						return 0;
					}

					@Override
					public void apply(IDocument document) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public String getAdditionalProposalInfo() {
						return "getAdditionalProposalInfo";
					}

					@Override
					public IContextInformation getContextInformation() {
						return null;
					}

					@Override
					public String getDisplayString() {
						// TODO Auto-generated method stub
						return "getDisplayString";
					}

					@Override
					public Image getImage() {
						// TODO Auto-generated method stub
						return null;
					}

					@Override
					public Point getSelection(IDocument document) {
						// TODO Auto-generated method stub
						return null;
					}}
				
		};
	}

	@Override
	public boolean hasCorrections(ICompilationUnit unit, int problemId) {
		return true;
	}

}
