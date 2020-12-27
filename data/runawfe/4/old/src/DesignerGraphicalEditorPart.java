package org.jbpm.ui.editor;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.properties.UndoablePropertySheetEntry;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.jbpm.ui.DesignerLogger;
import org.jbpm.ui.command.TransitionCreateCommand;
import org.jbpm.ui.factory.ElementCreationFactory;
import org.jbpm.ui.model.GraphElement;
import org.jbpm.ui.model.Node;
import org.jbpm.ui.model.Transition;
import org.jbpm.ui.outline.OutlineViewer;
import org.jbpm.ui.part.graph.NodeGraphicalEditPart;
import org.w3c.dom.NodeList;

public class DesignerGraphicalEditorPart extends GraphicalEditorWithFlyoutPalette implements RequestConstants {
	private KeyHandler commonKeyHandler;

	private DesignerEditor editor;

	private OutlineViewer outlineViewer;

	private boolean isDirty;

	private PropertySheetPage undoablePropertySheetPage;

	private IStructuredSelection selection;

	public DesignerGraphicalEditorPart(DesignerEditor editor) {
		this.editor = editor;
		DefaultEditDomain defaultEditDomain = new DefaultEditDomain(this);
		setEditDomain(defaultEditDomain);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (editor.equals(getSite().getPage().getActiveEditor()))
			updateActions(getSelectionActions());
		if (selection instanceof IStructuredSelection) {
			this.selection = (IStructuredSelection) selection;
		}
		if (part instanceof ContentOutline) {
			if (!(selection instanceof IStructuredSelection))
				return;
			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
			Object selectedObject = structuredSelection.getFirstElement();
			if (!(selectedObject instanceof EditPart))
				return;
			EditPart source = (EditPart) selectedObject;
			EditPart target = (EditPart) getGraphicalViewer().getEditPartRegistry().get(source.getModel());
			if (target != null) {
				getGraphicalViewer().select(target);
			}
		}
	}

	@Override
	public void commandStackChanged(EventObject event) {
		super.commandStackChanged(event);
		setDirty(getCommandStack().isDirty());
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == EditDomain.class)
			return getEditDomain();
		else if (adapter == IPropertySheetPage.class)
			return getPropertySheetPage();
		else if (adapter == IContentOutlinePage.class)
			return getOutlineViewer();

		return super.getAdapter(adapter);
	}

	@Override
	public CommandStack getCommandStack() {
		return super.getCommandStack();
	}

	public OutlineViewer getOutlineViewer() {
		if (outlineViewer == null && getGraphicalViewer() != null) {
			RootEditPart rootEditPart = getGraphicalViewer().getRootEditPart();
			if (rootEditPart instanceof ScalableFreeformRootEditPart) {
				outlineViewer = new OutlineViewer(this);
			}
		}
		return outlineViewer;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		SWTGraphics g = null;
		GC gc = null;
		Image image = null;

		LayerManager lm = (LayerManager) getGraphicalViewer().getEditPartRegistry().get(LayerManager.ID);
		IFigure figure = lm.getLayer(LayerConstants.PRINTABLE_LAYERS);

		try {
			Rectangle r = figure.getBounds();
			editor.getProcessDefinition().setDimension(new Dimension(r.width, r.height));
			image = new Image(Display.getDefault(), r.width, r.height);
			gc = new GC(image);
			g = new SWTGraphics(gc);
			g.translate(r.x * -1, r.y * -1);
			figure.paint(g);
			ImageLoader imageLoader = new ImageLoader();
			imageLoader.data = new ImageData[] { ImageHelper.downSample(image) };
			imageLoader.save(getImageSavePath(), SWT.IMAGE_GIF);
			refreshProcessFolder(monitor);
		} finally {
			if (g != null) {
				g.dispose();
			}
			if (gc != null) {
				gc.dispose();
			}
			if (image != null) {
				image.dispose();
			}
		}
	}

	private void refreshProcessFolder(IProgressMonitor monitor) {
		try {
			IFile file = ((FileEditorInput) getEditorInput()).getFile();
			file.getParent().refreshLocal(IResource.DEPTH_ONE, monitor);
		} catch (CoreException e) {
			DesignerLogger.logError(e);
		}
	}

	private String getImageSavePath() {
		IFile file = ((FileEditorInput) getEditorInput()).getFile();
		IPath path = file.getRawLocation().removeLastSegments(1).append(DesignerContentProvider.PROCESS_IMAGE_FILE_NAME);
		return path.toOSString();
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		IEditorInput gpdInput = DesignerContentProvider.INSTANCE.getGpdEditorInput(input);
		DesignerContentProvider.INSTANCE.addAuxInfo(editor.getProcessDefinition(), gpdInput);
		super.init(site, gpdInput);
		getSite().setSelectionProvider(editor.getSite().getSelectionProvider());
	}

	public DesignerEditor getEditor() {
		return editor;
	}

	private PaletteRoot root;

	@Override
	protected PaletteRoot getPaletteRoot() {
		if (root == null) {
			root = new DesignerPaletteRoot(editor);
		}
		return root;
	}

	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		getGraphicalViewer().setContents(editor.getProcessDefinition());
	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		getEditDomain().addViewer(getGraphicalViewer());

		getGraphicalViewer().setRootEditPart(new ScalableFreeformRootEditPart());
		getGraphicalViewer().setEditPartFactory(new EditPartFactory() {
			public EditPart createEditPart(EditPart context, Object object) {
				if (!(object instanceof GraphElement))
					return null;
				GraphElement element = (GraphElement) object;
				return element.getElementType().getContributor().createGraphicalEditPart(element);
			}
		});

		KeyHandler keyHandler = new GraphicalViewerKeyHandler(getGraphicalViewer());
		keyHandler.setParent(getCommonKeyHandler());
		getGraphicalViewer().setKeyHandler(keyHandler);
		getGraphicalViewer().setContextMenu(createContextMenu());
		getSite().setSelectionProvider(getGraphicalViewer());
	}

	protected KeyHandler getCommonKeyHandler() {
		if (commonKeyHandler == null) {
			commonKeyHandler = new KeyHandler();
			commonKeyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0), getActionRegistry().getAction(ActionFactory.DELETE.getId()));
			commonKeyHandler.put(KeyStroke.getPressed(SWT.F2, 0), getActionRegistry().getAction(GEFActionConstants.DIRECT_EDIT));
		}
		return commonKeyHandler;
	}

	private MenuManager createContextMenu() {
		MenuManager menuManager = new EditorContextMenuProvider(getGraphicalViewer());
		getSite().registerContextMenu("org.jbpm.ui.graph.contextmenu", menuManager, getSite().getSelectionProvider());
		return menuManager;
	}

	private class EditorContextMenuProvider extends ContextMenuProvider {

		public EditorContextMenuProvider(EditPartViewer viewer) {
			super(viewer);
		}

		@Override
		public void buildContextMenu(IMenuManager menu) {
			GEFActionConstants.addStandardActionGroups(menu);

			IAction action;

			action = new Action("copy") {

				@Override
				public void run() {
					Clipboard clipboard = Clipboard.getDefault();
					if (selection != null) {
						clipboard.setContents(selection);
					}
				}

			};
			menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

			action = new Action("paste") {

				@Override
				public void run() {
					try {
						Clipboard clipboard = Clipboard.getDefault();
						IStructuredSelection selection = (IStructuredSelection) clipboard.getContents();
						ArrayList<Node> nodeList = new ArrayList<Node>();
						ArrayList<Node> newNodeList = new ArrayList<Node>();
						for (Iterator iter = selection.iterator(); iter.hasNext();) {
							boolean add = true;
							if (!(iter.next() instanceof NodeGraphicalEditPart)) {
								continue;
							}
							NodeGraphicalEditPart editPart = (NodeGraphicalEditPart) iter.next();
							Node node = editPart.getModel();
							ElementCreationFactory factory;
							if ("start".equals(node.getNamePrefix())) {
								factory = new ElementCreationFactory("start-state", editor);
								for (Iterator iterator = editor.getProcessDefinition().getNodes().iterator(); iterator.hasNext();) {
									Node element = (Node) iterator.next();
									if (element.getNamePrefix().equals("start")) {
										add = false;
									}
								}
							} else if ("end".equals(node.getNamePrefix())) {
								factory = new ElementCreationFactory("end-state", editor);
								for (Iterator iterator = editor.getProcessDefinition().getNodes().iterator(); iterator.hasNext();) {
									Node element = (Node) iterator.next();
									if (element.getNamePrefix().equals("end")) {
										add = false;
									}
								}
							} else
								factory = new ElementCreationFactory(node.getNamePrefix(), editor);
							if (add) {
								Node copy = (Node) factory.getNewObject();
								copy.setName(node.getName());
								if (copy.getName() == null)
									copy.setName(editor.getProcessDefinition().getNextNodeName(copy));
								copy.setConstraint(node.getConstraint());
								editor.getProcessDefinition().addNode(copy);
								nodeList.add(node);
								newNodeList.add(copy);
							}
						}
						if (nodeList.size() > 0) {
							Node node = nodeList.get(0);
							NodeList transitionList = node.getNode().getOwnerDocument().getElementsByTagName("transition");
							for (int i = 0; i < transitionList.getLength(); i++) {
								Transition transition = (Transition) ((IDOMNode) transitionList.item(i)).getAdapterFor(GraphElement.class);
								if (nodeList.contains(transition.getSource()) && nodeList.contains(transition.getTarget())) {
									TransitionCreateCommand command = new TransitionCreateCommand();
									int indexOfSource = nodeList.indexOf(transition.getSource());
									int indexOfTarget = nodeList.indexOf(transition.getTarget());
									command.setSource(newNodeList.get(indexOfSource));
									command.setTarget(newNodeList.get(indexOfTarget));
									editor.getCommandStack().execute(command);
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			};
			menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

			action = getActionRegistry().getAction(ActionFactory.UNDO.getId());
			menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

			action = getActionRegistry().getAction(ActionFactory.REDO.getId());
			menu.appendToGroup(GEFActionConstants.GROUP_UNDO, action);

			action = getActionRegistry().getAction(ActionFactory.DELETE.getId());
			if (action.isEnabled())
				menu.appendToGroup(GEFActionConstants.GROUP_EDIT, action);

		}
	}

	@Override
	public boolean isSaveOnCloseNeeded() {
		return getCommandStack().isDirty();
	}

	@Override
	public boolean isDirty() {
		return isDirty;
	}

	protected void setDirty(boolean dirty) {
		if (isDirty != dirty) {
			isDirty = dirty;
			firePropertyChange(IEditorPart.PROP_DIRTY);
		}
	}

	protected PropertySheetPage getPropertySheetPage() {
		if (undoablePropertySheetPage == null) {
			undoablePropertySheetPage = new PropertySheetPage();
			undoablePropertySheetPage.setRootEntry(new UndoablePropertySheetEntry(getCommandStack()));
		}
		return undoablePropertySheetPage;
	}

	@Override
	protected FlyoutPreferences getPalettePreferences() {
		return new PaletteFlyoutPreferences();
	}

	@Override
	public ActionRegistry getActionRegistry() {
		return super.getActionRegistry();
	}

	public RootEditPart getRootEditPart() {
		return getGraphicalViewer().getRootEditPart();
	}

	@Override
	public DefaultEditDomain getEditDomain() {
		return super.getEditDomain();
	}
}
