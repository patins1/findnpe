package jjkpp.jdt.debug.ui;

import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.IWatchExpression;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.internal.ui.actions.expressions.AddWatchExpressionAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.window.Window;

public class AddJavaWatchExpressionAction extends AddWatchExpressionAction {

	@Override
	public void run(IAction action) {
		// create a watch expression
		IWatchExpression watchExpression = DebugPlugin.getDefault().getExpressionManager().newWatchExpression(""); //$NON-NLS-1$
		// open the watch expression dialog
		if (new JavaWatchExpressionDialog(DebugUIPlugin.getShell(), watchExpression, false, this.getContext()).open() == Window.OK) {
			// if OK is selected, add the expression to the expression view and
			// try to evaluate the
			// expression.
			DebugPlugin.getDefault().getExpressionManager().addExpression(watchExpression);
			watchExpression.setExpressionContext(this.getContext());
		}
	}

}
