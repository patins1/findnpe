/**
 * TODO
 * org.eclipse.jdt.internal.ui.javaeditor.ASTProvider#getAST() :
 * isActiveElement auf FALSE setzen in zeile 220
 */
package jjkpp.firstfix;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.internal.core.dom.rewrite.RewriteEvent;
import org.eclipse.jdt.internal.core.dom.rewrite.RewriteEventStore;
import org.eclipse.jdt.internal.ui.javaeditor.ASTProvider;
import org.eclipse.jdt.internal.ui.text.correction.proposals.ASTRewriteCorrectionProposal;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.statushandlers.StatusAdapter;
import org.eclipse.ui.views.markers.WorkbenchMarkerResolution;

@SuppressWarnings("restriction")
public class Action1 implements IObjectActionDelegate {

	private ISelection fSelection;

	public Action1() {
		// nothing to do
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// nothing to do
	}

	public void getWizardRun(boolean fork, boolean cancelable, IRunnableWithProgress runnable) throws InvocationTargetException, InterruptedException {
		ModalContext.run(runnable, false, new NullProgressMonitor(), Workbench.getInstance().getDisplay());

	}

	static final StatusAdapter MarkerSupportInternalUtilitiesErrorFor(Throwable exception) {
		IStatus status = new Status(IStatus.ERROR, IDEWorkbenchPlugin.IDE_WORKBENCH, IStatus.ERROR, exception.getLocalizedMessage(), exception);
		return new StatusAdapter(status);
	}

	int index = 0;
	private int count;
	private ProgressMonitorDialog progressMonitorDialog;

	@Override
	public void run(IAction action) {
		try {

			// doWork(new NullProgressMonitor());

			// class DecathlonJob extends Job {
			// public DecathlonJob() {
			// super("Athens decathlon 2004");
			// }
			//
			// public IStatus run(IProgressMonitor monitor) {
			// doWork(monitor);
			// return Status.OK_STATUS;
			// }
			// }
			// ;
			// new DecathlonJob().schedule();

			IRunnableWithProgress op = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) {
					doWork(monitor);
				}
			};
			IWorkbench wb = PlatformUI.getWorkbench();
			IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
			Shell shell = win != null ? win.getShell() : null;
			progressMonitorDialog = new ProgressMonitorDialog(shell);
			progressMonitorDialog.run(false, true, op);

			// ResourcesPlugin.getWorkspace().run(new IWorkspaceRunnable() {
			// @Override
			// public void run(IProgressMonitor monitor) throws CoreException {
			// doWork(monitor);
			// }
			// }, new NullProgressMonitor());

			// WorkspaceJob cleanJob = new WorkspaceJob("First Fix Job") {
			//
			// public IStatus runInWorkspace(IProgressMonitor monitor) throws
			// CoreException {
			// doWork(monitor);
			// return Status.OK_STATUS;
			// }
			// };
			// //
			// cleanJob.setRule(ResourcesPlugin.getWorkspace().getRuleFactory().buildRule());
			// cleanJob.setUser(true);
			// cleanJob.schedule();

			// new UIJob("First Fix") {
			//
			// @Override
			// public IStatus runInUIThread(IProgressMonitor monitor) {
			// doWork(monitor);
			// // try {
			// // ResourcesPlugin.getWorkspace().run(new
			// // IWorkspaceRunnable() {
			// // @Override
			// // public void run(IProgressMonitor monitor) throws
			// // CoreException {
			// // doWork(monitor);
			// // }
			// // }, new NullProgressMonitor());
			// // } catch (CoreException e) {
			// // e.printStackTrace();
			// // }
			// return Status.OK_STATUS;
			// }
			//
			// }.schedule();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void doWork(IProgressMonitor monitor) {

		// try {
		// Thread.sleep(2000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		long millis = System.currentTimeMillis();
		if (fSelection instanceof IStructuredSelection) {
			IStructuredSelection ss = (IStructuredSelection) fSelection;
			Object[] ssa = ss.toArray();
			List<IMarker> ssl = new ArrayList<IMarker>();
			List<IMarkerResolution> ssres = new ArrayList<IMarkerResolution>();
			for (Object o : ssa) {
				if (o instanceof IAdaptable) {
					IAdaptable adaptable = (IAdaptable) o;
					Object markeradaptable = adaptable.getAdapter(IMarker.class);
					if (markeradaptable instanceof IMarker) {
						ssl.add((IMarker) markeradaptable);
					}
				}
			}
			count = ssl.size();
			index = 0;
			monitor.beginTask("First Fix", count);
			for (IMarker marker : ssl) {
				handleMarker(marker, monitor, ssres);
				monitor.worked(1);
				if (monitor.isCanceled())
					return;
			}
			doWork(ssres, monitor);
		}

		long millis2 = System.currentTimeMillis();
		System.out.println("millis=" + (millis2 - millis));
	}

	private void handleMarker(IMarker marker, IProgressMonitor monitor, List<IMarkerResolution> ssres) {
		if (!subTaskForMarker(marker, "Fetch proposal", monitor))
			return;
		IMarkerResolution[] resolutions = IDE.getMarkerHelpRegistry().getResolutions(marker);

		// to clear ast for following markers
		callActiveJavaEditorChanged(ASTProvider.getASTProvider());

		if (resolutions.length == 0) {
			// System.out.println("invalid:length=0 lineNumer=" +
			// marker.getAttribute("lineNumber", -1));
			return;
		}
		IMarkerResolution resolution = resolutions[0];
		ASTRewriteCorrectionProposal proposal = getProposal(resolution);
		if (proposal == null) {
			// System.out.println("invalid:proposal=null lineNumer=" +
			// marker.getAttribute("lineNumber", -1));
			return;
		}
		ASTRewrite rewrite = getRewrite(proposal);
		ASTNode rootNode = getRootNode(rewrite);
		/*
		 * TextEdit edit; try { if (proposal.getChange() instanceof
		 * CompilationUnitChange) { CompilationUnitChange compilationUnitChange
		 * = (CompilationUnitChange) proposal .getChange(); edit =
		 * compilationUnitChange.getEdit(); if (edit == null) continue; edit =
		 * compilationUnitChange.getEdit(); } else { continue; } } catch
		 * (CoreException e) { continue; }
		 */
		int ind = 0;
		if (!ssres.isEmpty()) {
			ASTRewriteCorrectionProposal todoProposal = getProposal(ssres.get(0));
			if (!todoProposal.getCompilationUnit().equals(proposal.getCompilationUnit())) {
				doWork(ssres, monitor);
				handleMarker(marker, monitor, ssres);
				return;
			}
			for (final IMarkerResolution r : ssres) {
				ASTRewriteCorrectionProposal rProposal = getProposal(r);

				ASTRewrite rRewrite = getRewrite(rProposal);
				ASTNode rRootNode = getRootNode(rRewrite);
				if (rRootNode.getStartPosition() == rootNode.getStartPosition()) {
					if (changesAreEqual(rewrite, rRewrite)) {
						ind = -1;
						break;
					}
				}
				if (rRootNode.getStartPosition() > rootNode.getStartPosition()) {
					break;
				}

				/*
				 * CompilationUnitChange rChange; try { rChange =
				 * (CompilationUnitChange) proposal .getChange(); TextEdit rEdit
				 * = rChange.getEdit(); if (rEdit.covers(edit)) { ind = -1;
				 * break; } if (rEdit.getOffset() > edit .getOffset()) break; }
				 * catch (CoreException e) { e.printStackTrace(); }
				 */
				ind++;
			}
		}
		if (ind != -1) {
			// changesAreEqual(rewrite, rewrite);
			ssres.add(ind, resolution);
			index++;
		}
		// System.out.println("ind=" + ind + " lineNumer=" +
		// marker.getAttribute("lineNumber", -1));
		doWork(ssres, monitor);
	}

	private boolean subTaskForMarker(IMarker marker, String name, IProgressMonitor monitor) {
		while (Display.getCurrent().readAndDispatch())
			;
		if (progressMonitorDialog != null && progressMonitorDialog.getShell() != null)
			while (progressMonitorDialog.getShell().getDisplay().readAndDispatch())
				;
		if (monitor.isCanceled())
			return false;
		String lineInfo = "";
		try {
			Object att = marker.getAttribute("lineNumber");
			if (att instanceof Integer)
				lineInfo = " (line " + att + ")";
		} catch (CoreException e) {
			// don't print line info
		}
		monitor.subTask(name + " for marker " + marker.getResource().getName() + lineInfo);
		return true;
	}

	private void callActiveJavaEditorChanged(ASTProvider astProvider) {
		try {
			Method method = ASTProvider.class.getDeclaredMethod("activeJavaEditorChanged", IWorkbenchPart.class);
			method.setAccessible(true);
			method.invoke(astProvider, new Object[] { null });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void doWork(List<IMarkerResolution> ssres, IProgressMonitor monitor) {
		// for (final IMarkerResolution r : ssres) {
		// try {
		// IMarker marker = getMarker(r);
		// System.out.println(marker.getResource() + " line=" +
		// marker.getAttribute("lineNumber") + " count=" + ssres.size() +
		// " total=" + index + "/" + count);
		// } catch (CoreException e1) {
		// e1.printStackTrace();
		// }
		// break;
		// }
		for (final IMarkerResolution r : revert(ssres)) {
			// System.out.println("doWork");
			doWork(r, monitor);
			// try {
			// Thread.sleep(10000);
			// } catch (InterruptedException e) {
			// e.printStackTrace();
			// }
		}
		ssres.clear();
	}

	private boolean changesAreEqual(ASTRewrite rewrite1, ASTRewrite rewrite2) {
		RewriteEventStore rewriteEventStore1 = getRewriteEventStore(rewrite1);
		RewriteEventStore rewriteEventStore2 = getRewriteEventStore(rewrite2);

		Iterator iter1 = rewriteEventStore1.getChangeRootIterator();
		Iterator iter2 = rewriteEventStore2.getChangeRootIterator();
		while (iter1.hasNext()) {
			if (!iter2.hasNext())
				return false;
			ASTNode curr1 = (ASTNode) iter1.next();
			ASTNode curr2 = (ASTNode) iter2.next();
			if (curr1 != curr2)
				return false;
			List<RewriteEvent> events1 = rewriteEventStore1.getChangedPropertieEvents(curr1);
			List<RewriteEvent> events2 = rewriteEventStore2.getChangedPropertieEvents(curr2);
			if (events1.size() != events2.size())
				return false;
			for (int eventIndex = 0; eventIndex < events1.size(); eventIndex++) {
				RewriteEvent event1 = events1.get(eventIndex);
				RewriteEvent event2 = events2.get(eventIndex);
				if (!event1.toString().equals(event2.toString()))
					return false;
			}

			// getChangedPropertieEvents
			// if (!RewriteEventStore.isNewNode(curr1) ||
			// !RewriteEventStore.isNewNode(curr1))
			// return false;
			// if (curr1.getStartPosition()!=curr2.getStartPosition())
			// return false;
			// if (curr1.getLength()!=curr2.getLength())
			// return false;
			// if (!curr1.toString().equals(curr2.toString()))
			// return false;
		}
		if (iter2.hasNext())
			return false;
		return true;
	}

	private RewriteEventStore getRewriteEventStore(ASTRewrite rRewrite) {
		try {
			Method method = ASTRewrite.class.getDeclaredMethod("getRewriteEventStore");
			method.setAccessible(true);
			return (RewriteEventStore) method.invoke(rRewrite);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private ASTNode getRootNode(ASTRewrite rewrite) {
		try {
			Method method = ASTRewrite.class.getDeclaredMethod("getRootNode");
			method.setAccessible(true);
			return (ASTNode) method.invoke(rewrite);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private ASTRewrite getRewrite(ASTRewriteCorrectionProposal proposal) {
		Field field;
		try {
			field = proposal.getClass().getDeclaredField("fRewrite");
			field.setAccessible(true);
			return (ASTRewrite) field.get(proposal);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<IMarkerResolution> revert(List<IMarkerResolution> ssres) {
		List<IMarkerResolution> result = new ArrayList<IMarkerResolution>();
		for (IMarkerResolution r : ssres)
			result.add(0, r);
		return result;
	}

	private ASTRewriteCorrectionProposal getProposal(IMarkerResolution todoResolution) {
		Field field;
		try {
			field = todoResolution.getClass().getDeclaredField("fProposal");
			field.setAccessible(true);
			return (ASTRewriteCorrectionProposal) field.get(todoResolution);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void doWork(final IMarkerResolution resolution, IProgressMonitor monitor) {
		// try {
		// System.out.println(marker.getResource()+" line="+marker.getAttribute("lineNumber")+" count="+count+"/"+ssl.size()+" resolutions.length="+
		// resolutions.length);
		// } catch (CoreException e1) {
		// e1.printStackTrace();
		// }

		IMarker marker = getMarker(resolution);
		if (!subTaskForMarker(marker, "Apply proposal", monitor))
			return;
		final IMarker[] markers = new IMarker[] { marker };
		((WorkbenchMarkerResolution) resolution).run(markers, monitor);

		// try {
		// getWizardRun(false, true, new IRunnableWithProgress() {
		//
		// public void run(IProgressMonitor monitor) {
		// ((WorkbenchMarkerResolution) resolution).run(markers, monitor);
		// }
		//
		// });
		// } catch (InvocationTargetException e) {
		// StatusManager.getManager().handle(MarkerSupportInternalUtilitiesErrorFor(e));
		// } catch (InterruptedException e) {
		// StatusManager.getManager().handle(MarkerSupportInternalUtilitiesErrorFor(e));
		// }
	}

	protected IMarker getMarker(IMarkerResolution resolution) {
		Field field;
		try {
			field = resolution.getClass().getDeclaredField("fMarker");
			field.setAccessible(true);
			return (IMarker) field.get(resolution);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection.isEmpty()) {
			return;
		}
		fSelection = selection;
	}

}
