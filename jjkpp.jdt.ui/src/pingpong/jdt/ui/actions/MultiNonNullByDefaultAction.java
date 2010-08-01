package pingpong.jdt.ui.actions;

import java.util.Hashtable;
import java.util.Map;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.internal.ui.actions.CleanUpAction;
import org.eclipse.jdt.ui.cleanup.CleanUpOptions;
import org.eclipse.jdt.ui.cleanup.ICleanUp;
import org.eclipse.ui.IWorkbenchSite;

public class MultiNonNullByDefaultAction extends CleanUpAction {

	public MultiNonNullByDefaultAction(IWorkbenchSite site) {
		super(site);
	}

	protected ICleanUp[] getCleanUps(ICompilationUnit[] units) {
		Map settings = new Hashtable();
		settings.put(CleanUpConstants.ANNOTATE_NONNULLBYDEFAULT, CleanUpOptions.TRUE);

		return new ICleanUp[] { new NonNullByDefaultCleanUp(settings) };
	}

	protected String getActionName() {
		return ActionMessages.NonNullByDefaultAllAction_label;
	}

}