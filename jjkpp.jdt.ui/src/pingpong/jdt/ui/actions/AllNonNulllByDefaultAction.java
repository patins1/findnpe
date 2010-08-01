package pingpong.jdt.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.internal.Workbench;

public class AllNonNulllByDefaultAction implements IObjectActionDelegate {

	private MultiNonNullByDefaultAction fCleanUpDelegate;
	private IStructuredSelection selection;

	public AllNonNulllByDefaultAction() {
		super();
		fCleanUpDelegate = new MultiNonNullByDefaultAction(Workbench.getInstance().getActiveWorkbenchWindow().getActivePage().getActivePart().getSite());
	}

	public void selectionChanged(IStructuredSelection selection) {
		fCleanUpDelegate.selectionChanged(selection);
		// setEnabled(fCleanUpDelegate.isEnabled());
	}

	public void run(IAction arg0) {
		fCleanUpDelegate.run(selection);
	}

	public void selectionChanged(IAction action, ISelection sel) {
		if (sel instanceof IStructuredSelection) {
			selection = (IStructuredSelection) sel;
			selectionChanged(selection);
		}
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

}
