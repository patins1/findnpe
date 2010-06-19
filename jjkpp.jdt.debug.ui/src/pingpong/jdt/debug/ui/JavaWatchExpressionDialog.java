package pingpong.jdt.debug.ui;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.IHandler;
import org.eclipse.debug.core.model.IDebugElement;
import org.eclipse.debug.core.model.IWatchExpression;
import org.eclipse.debug.internal.ui.actions.ActionMessages;
import org.eclipse.debug.internal.ui.actions.StatusInfo;
import org.eclipse.debug.internal.ui.actions.expressions.WatchExpressionDialog;
import org.eclipse.jdt.internal.debug.ui.JDIDebugUIPlugin;
import org.eclipse.jdt.internal.debug.ui.JDISourceViewer;
import org.eclipse.jdt.internal.debug.ui.actions.ExpressionInputDialog;
import org.eclipse.jdt.internal.debug.ui.contentassist.CurrentFrameContext;
import org.eclipse.jdt.internal.debug.ui.contentassist.JavaDebugContentAssistProcessor;
import org.eclipse.jdt.internal.debug.ui.display.DisplayViewerConfiguration;
import org.eclipse.jdt.internal.debug.ui.propertypages.BreakpointConditionEditor;
import org.eclipse.jdt.ui.text.IJavaPartitions;
import org.eclipse.jdt.ui.text.JavaTextTools;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.actions.TextViewerAction;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;

public class JavaWatchExpressionDialog extends WatchExpressionDialog {

	/**
	 * The detail formatter to edit.
	 */
	private IWatchExpression fWatchExpression;

	// widgets
	private SourceViewer fSnippetViewer;
	private Button fCheckBox;

	public JavaWatchExpressionDialog(Shell parent, IWatchExpression watchExpression, boolean editDialog) {
		super(parent, watchExpression, editDialog);
		fWatchExpression = watchExpression;
	}

	public JavaWatchExpressionDialog(Shell parent, IWatchExpression watchExpression, boolean editDialog, IDebugElement context) {
		this(parent, watchExpression, editDialog);
		this.context = context;
	}

	/**
	 * See {@link BreakpointConditionEditor} and {@link ExpressionInputDialog}
	 * to see where this is taken from!
	 */
	private IDebugElement context;// !!

	private IContentAssistProcessor fCompletionProcessor;// !!

	private IHandlerService fHandlerService;// !!

	private IHandlerActivation fActivation;// !!

	protected Control createDialogArea(Composite parent) {
		Font font = parent.getFont();

		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_BOTH);
		container.setLayoutData(gd);

		// snippet label
		Label label = new Label(container, SWT.NONE);
		label.setText(ActionMessages.WatchExpressionDialog_2);
		gd = new GridData(GridData.BEGINNING);
		label.setLayoutData(gd);
		label.setFont(font);

		fSnippetViewer = new JDISourceViewer(container, null, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.LEFT_TO_RIGHT);
		fSnippetViewer.setInput(this);

		IDocument document = new Document();
		configureSourceViewer(document); // !! add content assist
		// fSnippetViewer.configure(new SourceViewerConfiguration());
		fSnippetViewer.setEditable(true);
		fSnippetViewer.setDocument(document);
		document.addDocumentListener(new IDocumentListener() {
			public void documentAboutToBeChanged(DocumentEvent event) {
			}

			public void documentChanged(DocumentEvent event) {
				checkValues();
			}
		});

		fSnippetViewer.getTextWidget().setFont(JFaceResources.getTextFont());

		Control control = fSnippetViewer.getControl();
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = convertHeightInCharsToPixels(10);
		gd.widthHint = convertWidthInCharsToPixels(80);
		control.setLayoutData(gd);
		fSnippetViewer.getDocument().set(fWatchExpression.getExpressionText());

		// actions
		final TextViewerAction cutAction = new TextViewerAction(fSnippetViewer, ITextOperationTarget.CUT);
		cutAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_CUT));
		cutAction.setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_CUT_DISABLED));
		cutAction.setText(ActionMessages.WatchExpressionDialogMenu_0);
		final TextViewerAction copyAction = new TextViewerAction(fSnippetViewer, ITextOperationTarget.COPY);
		copyAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_COPY));
		copyAction.setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_COPY_DISABLED));
		copyAction.setText(ActionMessages.WatchExpressionDialogMenu_1);
		final TextViewerAction pasteAction = new TextViewerAction(fSnippetViewer, ITextOperationTarget.PASTE);
		pasteAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		pasteAction.setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
		pasteAction.setText(ActionMessages.WatchExpressionDialogMenu_2);
		// !!
		final TextViewerAction undoAction = new TextViewerAction(fSnippetViewer, ITextOperationTarget.UNDO);
		undoAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_UNDO));
		undoAction.setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_UNDO_DISABLED));
		undoAction.setText(WorkbenchMessages.Workbench_undo);
		// !!
		final TextViewerAction redoAction = new TextViewerAction(fSnippetViewer, ITextOperationTarget.REDO);
		redoAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_REDO));
		redoAction.setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_REDO_DISABLED));
		redoAction.setText(WorkbenchMessages.Workbench_redo);

		// context menu
		MenuManager menuManager = new MenuManager();
		menuManager.add(cutAction);
		menuManager.add(copyAction);
		menuManager.add(pasteAction);
		menuManager.add(undoAction);
		menuManager.add(redoAction);
		menuManager.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				cutAction.update();
				copyAction.update();
				pasteAction.update();
				undoAction.update();
				redoAction.update();
			}
		});
		Menu menu = menuManager.createContextMenu(fSnippetViewer.getTextWidget());
		fSnippetViewer.getTextWidget().setMenu(menu);

		// enable checkbox
		fCheckBox = new Button(container, SWT.CHECK | SWT.LEFT);
		fCheckBox.setText(ActionMessages.WatchExpressionDialog_3);
		fCheckBox.setSelection(fWatchExpression.isEnabled());
		fCheckBox.setFont(font);

		applyDialogFont(container);
		fSnippetViewer.getControl().setFocus();

		activateHandler();// !! install content assist
		return container;
	}

	/**
	 * Initializes the source viewer. This method is based on code in
	 * BreakpointConditionEditor.
	 */
	private void configureSourceViewer(IDocument document) {
		JavaTextTools tools = JDIDebugUIPlugin.getDefault().getJavaTextTools();
		tools.setupJavaDocumentPartitioner(document, IJavaPartitions.JAVA_PARTITIONING);
		fSnippetViewer.configure(new DisplayViewerConfiguration() {
			public IContentAssistProcessor getContentAssistantProcessor() {
				return getCompletionProcessor();
			}
		});
	}

	/**
	 * Activates the content assist handler.
	 */
	private void activateHandler() {
		IHandler handler = new AbstractHandler() {
			public Object execute(ExecutionEvent event) throws org.eclipse.core.commands.ExecutionException {
				fSnippetViewer.doOperation(ISourceViewer.CONTENTASSIST_PROPOSALS);
				return null;
			}
		};
		IWorkbench workbench = PlatformUI.getWorkbench();
		fHandlerService = (IHandlerService) workbench.getAdapter(IHandlerService.class);
		fActivation = fHandlerService.activateHandler(ITextEditorActionDefinitionIds.CONTENT_ASSIST_PROPOSALS, handler);
	}

	/**
	 * Return the completion processor associated with this viewer.
	 * 
	 * @return DisplayConditionCompletionProcessor
	 */
	protected IContentAssistProcessor getCompletionProcessor() {
		if (fCompletionProcessor == null) {
			fCompletionProcessor = new JavaDebugContentAssistProcessor(new CurrentFrameContext());
		}
		return fCompletionProcessor;
	}

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed() {
		fWatchExpression.setEnabled(fCheckBox.getSelection());
		fWatchExpression.setExpressionText(fSnippetViewer.getDocument().get());
		// super.okPressed();
		setReturnCode(OK);
		close();
	}

	/**
	 * Check the field values and display a message in the status if needed.
	 */
	private void checkValues() {
		StatusInfo status = new StatusInfo();
		if (fSnippetViewer.getDocument().get().trim().length() == 0) {
			status.setError(ActionMessages.WatchExpressionDialog_4);
		}
		updateStatus(status);
	}

	@Override
	public boolean close() {
		this.fHandlerService.deactivateHandler(this.fActivation);
		return super.close();
	}
}
