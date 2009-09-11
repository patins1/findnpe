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
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.statushandlers.StatusAdapter;
import org.eclipse.ui.statushandlers.StatusManager;
import org.eclipse.ui.views.markers.WorkbenchMarkerResolution;

public class Action1 implements IObjectActionDelegate {

	private ISelection fSelection;

	public Action1() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// TODO Auto-generated method stub

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

	@Override
	public void run(IAction action) {
		long millis = System.currentTimeMillis();
		if (fSelection instanceof IStructuredSelection) {
			IStructuredSelection ss = (IStructuredSelection) fSelection;
			Object[] ssa = ss.toArray();
			List ssl = new ArrayList();
			List<IMarkerResolution> ssres = new ArrayList<IMarkerResolution>();
			for (Object o : ssa)
				ssl.add(o);
			count = ssl.size();
			index = 0;
			for (Object o : ssl) {
				if (o instanceof IAdaptable) {
					IAdaptable adaptable = (IAdaptable) o;
					Object markeradaptable = adaptable.getAdapter(IMarker.class);
					if (markeradaptable instanceof IMarker) {
						final IMarker marker = (IMarker) markeradaptable;
						startANew: if (marker != null) {
							IMarkerResolution[] resolutions = IDE.getMarkerHelpRegistry().getResolutions(marker);

							// to clear ast for following markers
							callActiveJavaEditorChanged(ASTProvider.getASTProvider());

							if (resolutions.length == 0) {
								System.out.println("invalid:length=0 lineNumer=" + marker.getAttribute("lineNumber", -1));
								continue;
							}
							IMarkerResolution resolution = resolutions[0];
							ASTRewriteCorrectionProposal proposal = getProposal(resolution);
							if (proposal == null) {
								System.out.println("invalid:proposal=null lineNumer=" + marker.getAttribute("lineNumber", -1));
								continue;
							}
							ASTRewrite rewrite = getRewrite(proposal);
							ASTNode rootNode = getRootNode(rewrite);
							/*
							 * TextEdit edit; try { if (proposal.getChange()
							 * instanceof CompilationUnitChange) {
							 * CompilationUnitChange compilationUnitChange =
							 * (CompilationUnitChange) proposal .getChange();
							 * edit = compilationUnitChange.getEdit(); if (edit
							 * == null) continue; edit =
							 * compilationUnitChange.getEdit(); } else {
							 * continue; } } catch (CoreException e) { continue;
							 * }
							 */
							int ind = 0;
							if (!ssres.isEmpty()) {
								ASTRewriteCorrectionProposal todoProposal = getProposal(ssres.get(0));
								if (!todoProposal.getCompilationUnit().equals(proposal.getCompilationUnit())) {
									doWork(ssres);
									break startANew;
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
									 * CompilationUnitChange rChange; try {
									 * rChange = (CompilationUnitChange)
									 * proposal .getChange(); TextEdit rEdit =
									 * rChange.getEdit(); if
									 * (rEdit.covers(edit)) { ind = -1; break; }
									 * if (rEdit.getOffset() > edit
									 * .getOffset()) break; } catch
									 * (CoreException e) { e.printStackTrace();
									 * }
									 */
									ind++;
								}
							}
							if (ind != -1) {
								// changesAreEqual(rewrite, rewrite);
								ssres.add(ind, resolution);
								index++;
							}
							System.out.println("ind=" + ind + " lineNumer=" + marker.getAttribute("lineNumber", -1));
							doWork(ssres);
						}
					}
				}
			}
			doWork(ssres);
		}

		long millis2 = System.currentTimeMillis();
		System.out.println("millis=" + (millis2 - millis));
	}

	private void callActiveJavaEditorChanged(ASTProvider astProvider) {
		try {
			Method method = ASTProvider.class.getDeclaredMethod("activeJavaEditorChanged", IWorkbenchPart.class);
			method.setAccessible(true);
			method.invoke(astProvider, new Object[] { null });
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private void doWork(List<IMarkerResolution> ssres) {
		for (final IMarkerResolution r : ssres) {
			try {
				IMarker marker = getMarker(r);
				System.out.println(marker.getResource() + " line=" + marker.getAttribute("lineNumber") + " count=" + ssres.size() + " total=" + index + "/" + count);
			} catch (CoreException e1) {
				e1.printStackTrace();
			}
			break;
		}
		for (final IMarkerResolution r : revert(ssres)) {
			System.out.println("doWork");
			doWork(null, r);
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
		} catch (SecurityException e) {
			return null;
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
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
		} catch (SecurityException e) {
			return null;
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private ASTRewrite getRewrite(ASTRewriteCorrectionProposal proposal) {
		Field field;
		try {
			field = proposal.getClass().getDeclaredField("fRewrite");
		} catch (SecurityException e) {
			return null;
		} catch (NoSuchFieldException e) {
			return null;
		}
		try {
			field.setAccessible(true);
			return (ASTRewrite) field.get(proposal);
		} catch (IllegalArgumentException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		}
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
		} catch (SecurityException e) {
			return null;
		} catch (NoSuchFieldException e) {
			return null;
		}
		try {
			field.setAccessible(true);
			return (ASTRewriteCorrectionProposal) field.get(todoResolution);
		} catch (IllegalArgumentException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		}
	}

	private void doWork(final IMarker marker, final IMarkerResolution resolution) {
		// try {
		// System.out.println(marker.getResource()+" line="+marker.getAttribute("lineNumber")+" count="+count+"/"+ssl.size()+" resolutions.length="+
		// resolutions.length);
		// } catch (CoreException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
		try {
			getWizardRun(false, true, new IRunnableWithProgress() {
				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * org.eclipse.jface.operation.IRunnableWithProgress#run(org
				 * .eclipse.core.runtime.IProgressMonitor)
				 */
				public void run(IProgressMonitor monitor) {
					IMarker[] markers = new IMarker[1];
					markers[0] = getMarker(resolution);
					((WorkbenchMarkerResolution) resolution).run(markers, monitor);
					// try {
					// Thread.sleep(1000);
					// } catch (InterruptedException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
				}

			});
		} catch (InvocationTargetException e) {
			StatusManager.getManager().handle(MarkerSupportInternalUtilitiesErrorFor(e));
		} catch (InterruptedException e) {
			StatusManager.getManager().handle(MarkerSupportInternalUtilitiesErrorFor(e));
		}
	}

	protected IMarker getMarker(IMarkerResolution resolution) {
		Field field;
		try {
			field = resolution.getClass().getDeclaredField("fMarker");
		} catch (SecurityException e) {
			return null;
		} catch (NoSuchFieldException e) {
			return null;
		}
		try {
			field.setAccessible(true);
			return (IMarker) field.get(resolution);
		} catch (IllegalArgumentException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection.isEmpty()) {
			return;
		}
		fSelection = selection;
	}

}
