package pingpong.jdt.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import pingpong.firstfix.FirstFixHandler;
import pingpong.jdt.core.classes.NullibilityAnnos;

public class TestErrors extends TestCase {

	protected IProject project;

	int totalChecks = 0;

	public void testErrors() throws CoreException, InterruptedException, IOException {
		Display.getCurrent().update();
		PlatformUI.getWorkbench().showPerspective("org.eclipse.jdt.ui.JavaPerspective", PlatformUI.getWorkbench().getActiveWorkbenchWindow());
		Display.getCurrent().update();
		final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		URL url = FileLocator.toFileURL(FileLocator.find(Platform.getBundle("pingpong.javaproject.test"), new Path(""), Collections.EMPTY_MAP));
		File sour = new File(url.getPath()).getAbsoluteFile();
		File dest = new File(ResourcesPlugin.getWorkspace().getRoot().getRawLocationURI().toURL().getPath(),"pingpong.javaproject.test");
		copyDirectory(sour,dest);
		IProjectDescription description = ResourcesPlugin.getWorkspace().loadProjectDescription(new FileInputStream(new File(dest,".project")));
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());
		project.create(description, new NullProgressMonitor());
		this.project.open(new NullProgressMonitor());
		this.project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		project.build(IncrementalProjectBuilder.CLEAN_BUILD, new NullProgressMonitor());
		waitForBuild();
		Display.getCurrent().update();
		IFolder src = project.getFolder("src");
		try {
			src.accept(new IResourceVisitor() {

				@Override
				public boolean visit(IResource resource) throws CoreException {
					if (resource instanceof IFile && "java".equals(resource.getFileExtension())) {
						IFile file = (IFile) resource;
						System.out.println(file.getProjectRelativePath());

						// get the preferred editor id
						IEditorRegistry editorReg = PlatformUI.getWorkbench().getEditorRegistry();
						String editorID = "org.eclipse.jdt.ui.CompilationUnitEditor";
						IEditorDescriptor editorDesc = editorReg.findEditor(editorID);

						// open the editor on the file
						IEditorPart editor = page.openEditor(new FileEditorInput(file), editorID, true);
						AbstractTextEditor ed = (AbstractTextEditor) editor;
						IDocument doc = ed.getDocumentProvider().getDocument(ed.getEditorInput());
						String content = doc.get();
						Display.getCurrent().update();
						Collection<IMarker> firstfixes = new ArrayList<IMarker>();
						Set<IMarker> markers = findMarkers(file.findMarkers("org.eclipse.jdt.core.problem", true, IResource.DEPTH_ZERO));
						int commentLineNumber = 0;
						for (String line : content.split("\n")) {
							commentLineNumber++;
							int errorIndex = line.indexOf("/* error");
							if (errorIndex != -1) {
								int lineNumber = commentLineNumber;
								if (line.substring(0, errorIndex).trim().equals("")) {
									lineNumber--;
								}
								totalChecks++;
								int errorIndexTo = line.indexOf("*/", errorIndex) + 2;
								String errorContent = line.substring(errorIndex, errorIndexTo);
								boolean easyError = errorContent.contains("easy");
								boolean cancel = errorContent.contains("ATTACK") && !errorContent.contains("NOATTACK") && !NullibilityAnnos.ATTACK || errorContent.contains("NOATTACK") && NullibilityAnnos.ATTACK;
								if (cancel)
									continue;
								IMarker marker = findMarker(markers, lineNumber);
								if (marker == null || easyError != marker.getAttribute("message", "").equals(NullibilityAnnos.EXPECTED_NONNULL_INSTEADOF_NULL)) {
									reveal(ed, doc, commentLineNumber, errorIndex, errorIndexTo);
									for (IMarker marker2 : markers) {
										System.out.println("Marker " + marker2.getAttribute("message") + " line=" + marker2.getAttribute("lineNumber", -1));
									}
									if (marker == null) {
										Assert.fail("Cannot find marker: resource=" + resource.getProjectRelativePath() + " lineNumber=" + lineNumber + " marker=" + marker + " count=" + markers.size());
									} else {
										Assert.fail("Easy problem problem: resource=" + resource.getProjectRelativePath() + " lineNumber=" + lineNumber + " marker=" + marker + " count=" + markers.size());
									}
								}

								int proposalCount = -1;
								if (errorContent.contains("0"))
									proposalCount = 0;
								if (errorContent.contains("1"))
									proposalCount = 1;
								if (errorContent.contains("2"))
									proposalCount = 2;
								IMarkerResolution[] resolutions = IDE.getMarkerHelpRegistry().getResolutions(marker);
								if (proposalCount != resolutions.length) {
									reveal(ed, doc, commentLineNumber, errorIndex, errorIndexTo);
									try {
										Assert.assertEquals("Proposal count problem: resource=" + resource.getProjectRelativePath() + " lineNumber=" + lineNumber, proposalCount, resolutions.length);
									} catch (AssertionFailedError e) {
										// System.err.println(e.getMessage());
										throw e;
									}
								}  else if (errorContent.contains("FIRSTFIX")) {
									firstfixes.add(marker);	
								}
								markers.remove(marker);
							}
						}
						for (IMarker marker : markers) {
							int lineNumber = marker.getAttribute("lineNumber", -1);
							try {
								reveal(ed, doc, lineNumber, 0, doc.getLineLength(lineNumber - 1) - 1);
							} catch (BadLocationException e) {
								e.printStackTrace();
							}
							Assert.fail("Too many errors: resource=" + resource.getProjectRelativePath() + " message=" + marker.getAttribute("message") + " line=" + marker.getAttribute("lineNumber", -1));
						}
						for (IMarker marker:firstfixes) {
							int lineNumber = marker.getAttribute("lineNumber", -1);
							FirstFixHandler action1=new FirstFixHandler();
							try {
								action1.execute(new IMarker[]{marker});
							} catch (ExecutionException e1) {
								e1.printStackTrace();
							}
							SaveOpenFilesHandler handler=new SaveOpenFilesHandler();
							handler.showSaveDialog(project);
							try {
								waitForBuild();
							} catch (InterruptedException e) {
							}
							Set<IMarker> markers2 = findMarkers(file.findMarkers("org.eclipse.jdt.core.problem", true, IResource.DEPTH_ZERO));
							IMarker marker2 = findMarker(markers2, lineNumber);
							Assert.assertNull("Expected problem to be solved",marker2);
						}
					}
					return true;
				}

				private void reveal(AbstractTextEditor ed, IDocument doc, int lineNumber, int errorIndex, int errorIndexTo) {
					try {
						int offs1 = doc.getLineOffset(lineNumber - 1) + errorIndex;
						ed.selectAndReveal(offs1, errorIndexTo - errorIndex);
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				}
				
				boolean isFindNPEMessage(IMarker marker) throws CoreException {
					String message = (String) marker.getAttribute("message");
					if (message != null && (message.equals(NullibilityAnnos.EXPECTED_NONNULL) || message.equals(NullibilityAnnos.EXPECTED_NONNULL_INSTEADOF_NULL) || message.equals(NullibilityAnnos.NPE_HAZARD) || message.startsWith(NullibilityAnnos.ANNOTATION_PROBLEM)))
						return true;	
					return false;
				}

				private Set<IMarker> findMarkers(IMarker[] markers) throws CoreException {
					Set<IMarker> result = new HashSet<IMarker>();
					for (IMarker marker : markers) {						
						if (isFindNPEMessage(marker))
							result.add(marker);
					}
					return result;
				}

				private IMarker findMarker(Set<IMarker> markers, int lineNumber) throws CoreException {
					for (IMarker marker : markers) {
						if (lineNumber == marker.getAttribute("lineNumber", -1)) {			
							if (isFindNPEMessage(marker))
								return marker;
						}
					}
					return null;
				}
			});
		} catch (AssertionFailedError e) {
			e.printStackTrace();
			loop();
			throw e;
		}
		System.out.println("TOTAL CHECKS = " + totalChecks);
	}

	protected void loop() {
		Display current;
		long start = new Date().getTime();
		while ((current = Display.getCurrent()) != null && !current.isDisposed()) {
			if (!current.readAndDispatch())
				current.sleep();
			long end = new Date().getTime();
			if (end - start > 10000000)
				break;
		}
	}

	private void waitForBuild() throws InterruptedException {
		Job[] build;
		build = Job.getJobManager().find(ResourcesPlugin.FAMILY_AUTO_BUILD);
		if (build.length == 1) {
			build[0].join();
			waitForBuild();
		}
		build = Job.getJobManager().find(ResourcesPlugin.FAMILY_MANUAL_BUILD);
		if (build.length == 1) {
			build[0].join();
			waitForBuild();
		}
	}
    public void copyDirectory(File sourceLocation , File targetLocation) throws IOException {
        
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            
            String[] children = sourceLocation.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {
            
            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);
            
            // Copy the bits from instream to outstream
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }
}
