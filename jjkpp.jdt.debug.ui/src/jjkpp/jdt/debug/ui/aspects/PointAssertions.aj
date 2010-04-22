package jjkpp.jdt.debug.ui.aspects;

import org.eclipse.debug.core.model.IWatchExpression;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.debug.internal.ui.actions.expressions.WatchExpressionDialog;
import org.eclipse.debug.ui.DebugUITools;

import jjkpp.jdt.debug.ui.JavaWatchExpressionDialog;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jdt.internal.debug.core.model.JDIStackFrame;
public aspect PointAssertions {

    @SuppressWarnings("restriction")
	WatchExpressionDialog around(Shell parent, IWatchExpression watchExpression, boolean editDialog) : 
		call(WatchExpressionDialog.new(Shell,IWatchExpression, boolean)) && args(parent, watchExpression, editDialog)  {
		System.out.println("sdf");
    	if (DebugUITools.getDebugContext() instanceof JDIStackFrame)
        	return new JavaWatchExpressionDialog(parent,watchExpression,editDialog);
    	return proceed(parent,watchExpression,editDialog);
	}	

}
