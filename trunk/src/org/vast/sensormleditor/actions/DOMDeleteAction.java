package org.vast.sensormleditor.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMDeleteAction extends Action implements IShellProvider {

	private Element selectedElement, chosenOne;
	protected boolean stop = false;
	private DOMHelper dom;
	private DOMHelperAddOn domUtil;
	private TreeViewer viewer;

	public DOMDeleteAction(TreeViewer viewer, DOMHelper dom) {

		this.viewer = viewer;
		this.dom = dom;
		domUtil = new DOMHelperAddOn(dom);
	}

	public void run() {

		IStructuredSelection ssel = (IStructuredSelection) viewer
				.getSelection();
		if (!ssel.isEmpty() && ssel instanceof IStructuredSelection) {
			Object input = ((IStructuredSelection) ssel).getFirstElement();
			if (input instanceof Element) {
				this.selectedElement = (Element) input;
				String selectedElementString = selectedElement.getNodeName();
				if (selectedElement.getNodeName().equals("swe:field")
						|| selectedElement.getNodeName().equals("sml:input")
						|| selectedElement.getNodeName().equals("sml:output")
						|| selectedElement.getNodeName().equals("sml:member")
						|| selectedElement.getNodeName()
								.equals("sml:parameter")) {
					stop = false;
					Element retNode = null;
					chosenOne = domUtil
							.getChosenField(selectedElement, retNode);
					if (chosenOne != null) {
						selectedElementString = chosenOne.getNodeName();
					}
				}
				if (selectedElement.getParentNode() instanceof Element) {
					Node parent = selectedElement.getParentNode();
					if (!dom.hasQName(parent, "xng:item")
							&& (!dom.hasQName(parent, "rng:optional"))) {
						if (dom.existElement(selectedElement, "rng:choice")) {
							domUtil.deleteChildren(selectedElement);
							this.firePropertyChange("INSERT", null, null);
							return;
						}
						MessageDialog.openError(getShell(), "Delete Element",
								"Element is required and cannot be deleted.");
						return;
					}

					if (!MessageDialog
							.openQuestion(
									getShell(),
									"Delete Element",
									"Deleting this element will delete the element's children and associated properties.  Do you still want to delete: "
											+ selectedElementString + "?"))
						return;
					else {

						Node rulingElement = findRulingElement(selectedElement);
						if (rulingElement != null) {
							if (dom.hasQName(rulingElement, "rng:oneOrMore")) {

								NodeList items = dom.getElements(
										(Element) rulingElement, "xng:item");
								if (items.getLength() == 1) {
									MessageDialog.openError(getShell(),
											"Delete Element",
											"One element is required.");
									return;
								}
								for (int i = 0; i < items.getLength(); i++) {
									if (dom.existAttribute((Element) items
											.item(i), "@id")) {
										String attValue = dom
												.getAttributeValue(
														(Element) parent, "@id");
										String newValue = dom
												.getAttributeValue(
														(Element) items.item(i),
														"@id");
										if (attValue.equals(newValue)) {
											rulingElement.removeChild(items
													.item(i));

											this.firePropertyChange("INSERT",
													null, null);
											return;
										}
									}
								}

							} else if (dom.hasQName(rulingElement,
									"rng:zeroOrMore")) {

								rulingElement.removeChild(parent);
								this.firePropertyChange("INSERT", null, null);

							} else if (dom.hasQName(rulingElement,
									"rng:optional")) {

								if (dom.existAttribute((Element) rulingElement,
										"@xng:selected")) {
									((Element) rulingElement)
											.removeAttribute("xng:selected");
									domUtil.deleteChildren(selectedElement);

								}

								stop = false;
								Element retNode = null;
								chosenOne = domUtil.getChosenField(
										selectedElement, retNode);
								if (chosenOne != null) {

									if (dom.existAttribute((Element) chosenOne,
											"@xng:selected")) {
										((Element) chosenOne)
												.removeAttribute("xng:selected");
									}

									String path = chosenOne.getNodeName()
											+ "/rng:oneOrMore/xng:item";

									NodeList xngChildren = dom
											.getElements((Element) chosenOne
													.getParentNode(), path);
									for (int i = 0; i < xngChildren.getLength(); i++) {
										Node xngParent = xngChildren.item(i)
												.getParentNode();
										xngParent.removeChild(xngChildren
												.item(i));
									}
									domUtil.deleteChildren(chosenOne);
								}

								this.firePropertyChange("INSERT", null, null);

							}
						}
					}
				}
			}
		}
	}

	public Element findRulingElement(Element el) {

		if (el.getNodeName().equals("rng:oneOrMore")
				|| el.getNodeName().equals("rng:zeroOrMore")
				|| el.getNodeName().equals("rng:optional")) {
			return el;
		} else {
			if (el.getParentNode() != null
					&& el.getParentNode() instanceof Element) {
				return findRulingElement((Element) el.getParentNode());
			}
		}
		return null;

	}

	@Override
	public Shell getShell() {
		Shell currentShell = PlatformUI.getWorkbench().getDisplay()
				.getActiveShell();
		return currentShell;
	}

}
