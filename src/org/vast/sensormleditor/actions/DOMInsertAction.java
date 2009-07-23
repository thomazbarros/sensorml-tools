package org.vast.sensormleditor.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMInsertAction extends Action {

	private Element ele;
	private DOMHelper dom;
	private DOMHelperAddOn domUtil;

	public DOMInsertAction(DOMHelper dom, Element element) {

		this.dom = dom;
		this.ele = element;
		domUtil = new DOMHelperAddOn(dom);
	}

	public void run() {

		if ((ele.getParentNode().getNodeName().equals("rng:zeroOrMore") || ele
				.getParentNode().getNodeName().equals("rng:oneOrMore"))) {
			handleAddXNGItem();

		} else if ((ele.getParentNode().getNodeName().equals("rng:optional"))) {
			dom.setAttributeValue((Element) ele.getParentNode(),
					"xng:selected", "true");

		} else {

			Node baseNode = dom.getBaseElement();
			String nodepath = domUtil.findPath(baseNode, ele);
			String newpath = domUtil.createXNGItemPath(baseNode, nodepath);
			NodeList nodeCandidates = dom.getElements(newpath);
			if (nodeCandidates.getLength() > 0) {
				Element oneNode = (Element) nodeCandidates.item(nodeCandidates
						.getLength() - 1);

				if (dom.existElement(oneNode,"rng:attribute/rng:data")){
					dom.setElementValue((Element) oneNode,
						"rng:attribute/rng:data/rng:value", ele.getLocalName());

				/*
				 * String newID = domUtil.findUniqueID();
				 * oneNode.removeAttribute("id");
				 * dom.setAttributeValue((Element) oneNode, "id", newID);
				 */
					dom.setAttributeValue((Element) oneNode,
						"rng:attribute/rng:data/rng:value/@selected", "true");
				}

			}
		}
		this.firePropertyChange("INSERT", null, this.getText());
	}

	public void setSelection(ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection select = (IStructuredSelection) selection;
		}

	}

	public void handleAddXNGItem() {

		Element oneOrMore = dom.getElement((Element) ele.getParentNode(), "");
		Node xngNode = domUtil.createXNGItem(oneOrMore);

		Node copyNode = dom.getElement((Element) oneOrMore, ele.getNodeName());
		Node nNode = dom.getDocument().importNode(copyNode, true);
		xngNode.appendChild(nNode);
		oneOrMore.appendChild(xngNode);
		if (dom.existElement((Element) nNode,"rng:attribute/rng:data")){
			dom.setElementValue((Element) nNode,
				"rng:attribute/rng:data/rng:value", ele.getLocalName());

			dom.setAttributeValue((Element) nNode,
				"rng:attribute/rng:data/rng:value/@selected", "true");
		}
		/*
		 * this.firePropertyChange("INSERT", null, null); try {
		 * dom.serialize(dom.getBaseElement(), System.out, true); } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

	}

}
