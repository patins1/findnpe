package pingpong.jdt.debug.ui.aspects;

import java.util.List;
import org.eclipse.swt.SWT;
import org.eclipse.jdi.internal.StackFrameImpl;
import org.eclipse.jdi.internal.LocalVariableImpl;

import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IWatchExpression;
import org.eclipse.debug.internal.ui.actions.expressions.WatchExpressionDialog;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jdi.internal.MethodImpl;
import org.eclipse.jdi.internal.event.StepEventImpl;
import org.eclipse.jdt.internal.debug.core.model.JDIDebugTarget;
import org.eclipse.jdt.internal.debug.core.model.JDIStackFrame;
import org.eclipse.jdt.internal.debug.core.model.JDIThread;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.Shell;

import pingpong.jdt.debug.ui.JavaWatchExpressionDialog;

import com.sun.jdi.AbsentInformationException;
import com.sun.jdi.IncompatibleThreadStateException;
import com.sun.jdi.Location;
import com.sun.jdi.ThreadReference;
import com.sun.jdi.event.Event;
import com.sun.jdi.event.EventSet;
import com.sun.jdi.request.StepRequest;

privileged public aspect PointAssertions {
	
	public static boolean ENABLE_OUTER_STEPIN = false;

	@SuppressWarnings("restriction")
	WatchExpressionDialog around(Shell parent, IWatchExpression watchExpression, boolean editDialog) : 
		call(WatchExpressionDialog.new(Shell,IWatchExpression, boolean)) && args(parent, watchExpression, editDialog)  {
		System.out.println("sdf");
		if (DebugUITools.getDebugContext() instanceof JDIStackFrame)
			return new JavaWatchExpressionDialog(parent, watchExpression, editDialog);
		return proceed(parent, watchExpression, editDialog);
	}

	@SuppressWarnings("restriction")
	boolean around(JDIThread.StepHandler t, Event event, JDIDebugTarget target, boolean suspendVote, EventSet eventSet) : 
		call(boolean handleEvent(Event, JDIDebugTarget, boolean, EventSet)) && target(t) && args(event, target, suspendVote, eventSet) {
		
		if (ENABLE_OUTER_STEPIN && event instanceof StepEventImpl) {
			System.out.println();
			System.out.println("handleEvent");
			StepEventImpl stepEvent = (StepEventImpl) event;
			ThreadReference thread = stepEvent.thread();
			Location loc = stepEvent.location();
		
			// ThreadReference thread = ((StepEventImpl) event).thread();
			for (IThread iThread : target.getThreads()) {
				if (iThread instanceof JDIThread) {
					JDIThread jdiThread = (JDIThread) iThread;
					if (jdiThread.getUnderlyingThread() == thread) {
						System.out.println("isInvokingMethod:"+jdiThread.isInvokingMethod()+" isPerformingEvaluation:"+jdiThread.isPerformingEvaluation());
						Location ori = jdiThread.getOriginalStepLocation();
						try {
							List frames = thread.frames();
							System.out.println("Ori loc: " + ori + " ori index:" + ori.codeIndex() + " frame count:" + thread.frameCount() + "/" + frames.size());
							for (Object oframe:frames) {
								if (oframe instanceof StackFrameImpl) {
									StackFrameImpl frame=(StackFrameImpl) oframe;
									System.out.println("frame in:"+frame.location());
									List vars = frame.location().method().variables();
									for (Object ovar:vars) {
										if (ovar instanceof LocalVariableImpl) {
											LocalVariableImpl var=(LocalVariableImpl) ovar;
											System.out.println("		var:"+var.name());
										}
									}
								}
							}
							
						} catch (IncompatibleThreadStateException e) {
							e.printStackTrace();
						}catch (AbsentInformationException e) {
							e.printStackTrace();
						}
					}
				}
			}

			System.out.println("Current loc: " + loc + " current index:" + loc.codeIndex());
			
			MethodImpl m = ((MethodImpl)loc.method());
			try {
				List codeIndexes = m.javaStratumLineToCodeIndexes(loc.lineNumber());
				System.out.println("method:"+m.returnTypeName()+" "+m.name()+" codeIndexes of line " + loc.lineNumber() + ": " + codeIndexes);
			} catch (AbsentInformationException e) {
				e.printStackTrace();
			}

		}

		
		if (ENABLE_OUTER_STEPIN && event instanceof StepEventImpl && OS.GetAsyncKeyState(OS.VK_SHIFT) < 0) {
			StepEventImpl stepEvent = (StepEventImpl) event;
			ThreadReference thread = stepEvent.thread();
			Location loc = stepEvent.location();
		
			JDIThread jdiThread=null;
			for (IThread iThread : target.getThreads()) {
				if (iThread instanceof JDIThread) {
					jdiThread = (JDIThread) iThread;
					if (jdiThread.getUnderlyingThread() == thread) {
						if (jdiThread.getOriginalStepKind() != StepRequest.STEP_INTO) break;
						MethodImpl m = ((MethodImpl)loc.method());
						if (!"void".equals( m.returnTypeName())) {
							t.deleteStepRequest();
							try {
								t.createSecondaryStepRequest(StepRequest.STEP_OUT);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							System.out.println("stepped out of "+m.name());
							return true;
						}
						break;
					}
				}
			}

		
		}
		
		boolean result = proceed(t, event, target, suspendVote, eventSet);
		return result;
	}

}
