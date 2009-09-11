package jjkpp.jdt.test;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.AbstractTextEditor;

public class TestErrors extends TestCase {

	protected IProject project;

	int totalChecks = 0;

	public void testErrors() throws CoreException {
		final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		IProjectDescription description = ResourcesPlugin.getWorkspace().loadProjectDescription(new Path("C:/dev/jjkpp/jjkpp.javaproject.test/.project"));
		project = ResourcesPlugin.getWorkspace().getRoot().getProject(description.getName());
		project.create(description, new NullProgressMonitor());
		this.project.open(new NullProgressMonitor());
		this.project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		IFolder src = project.getFolder("src");
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
					Set<IMarker> markers = findMarkers(file.findMarkers("org.eclipse.jdt.core.problem", true, IResource.DEPTH_ZERO));
					int lineNumber = 1;
					for (String line : content.split("\n")) {
						int errorIndex = line.indexOf("/*error");
						if (errorIndex != -1) {
							totalChecks++;
							int errorIndexTo = line.indexOf("*/", errorIndex) + 2;
							String errorContent = line.substring(errorIndex, errorIndexTo);
							boolean easyError = errorContent.contains("easy");
							IMarker marker = findMarker(markers, lineNumber);
							if (marker == null || easyError != marker.getAttribute("message", "").contains("Easy")) {
								reveal(ed, doc, lineNumber, errorIndex, errorIndexTo);
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
								reveal(ed, doc, lineNumber, errorIndex, errorIndexTo);
								try {
									Assert.assertEquals("Proposal count problem: resource=" + resource.getProjectRelativePath() + " lineNumber=" + lineNumber, proposalCount, resolutions.length);
								} catch (AssertionFailedError e) {
									// System.err.println(e.getMessage());
									throw e;
								}
							}
							markers.remove(marker);
						}
						lineNumber++;
					}
					for (IMarker marker : markers) {
						Assert.fail("Too many errors: resource=" + resource.getProjectRelativePath() + " message=" + marker.getAttribute("message") + " line=" + marker.getAttribute("lineNumber", -1));
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

			private Set<IMarker> findMarkers(IMarker[] markers) throws CoreException {
				Set<IMarker> result = new HashSet<IMarker>();
				for (IMarker marker : markers) {
					if (((String) marker.getAttribute("message")).contains("Nullibility"))
						result.add(marker);
				}
				return result;
			}

			private IMarker findMarker(Set<IMarker> markers, int lineNumber) throws CoreException {
				for (IMarker marker : markers) {
					if (lineNumber == marker.getAttribute("lineNumber", -1)) {
						if (((String) marker.getAttribute("message")).contains("Nullibility"))
							return marker;
					}
				}
				return null;
			}
		});
		System.out.println("TOTAL CHECKS = " + totalChecks);
	}

}
