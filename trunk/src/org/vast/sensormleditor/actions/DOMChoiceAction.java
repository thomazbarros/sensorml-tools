package org.vast.sensormleditor.actions;

import java.io.IOException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.vast.sensormleditor.config.Configuration;
import org.vast.sensormleditor.relaxNG.Relax2Hybrid;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.vast.xml.transform.DOMTransformException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMChoiceAction extends Action {

	private Element ele, selectedElement;
	private DOMHelper dom, srcDoc;
	boolean stop = false;
	private DOMHelperAddOn domUtil;
	private String coreSchema;
	private Node chosenObject;
	boolean found = false;
	String xngURI = "http://xng.org/1.0";
	String rngaURI = "http://relaxng.org/ns/compatibility/annotations/1.0";

	// private IStructuredSelection selection;

	// selected = the highlighted item in the tree
	// element = the descendant element of selected that is selected choice
	public DOMChoiceAction(DOMHelper dom, Element element, Element selected,
			Node chosenObject) {

		super(element.getLocalName(), IAction.AS_CHECK_BOX);
		coreSchema = Configuration.getSchema();
		try {
			srcDoc = new DOMHelper(coreSchema, false);
		} catch (DOMHelperException e) {
			e.printStackTrace();
		}

		this.dom = dom;
		this.ele = element;
		this.selectedElement = selected;
		this.chosenObject = chosenObject;

		domUtil = new DOMHelperAddOn(dom);
	}

	public void run() {

		if (chosenObject.getParentNode().getNodeName().equals("rng:optional")) {
			handleOptionalSelection();
		}

		else if ((chosenObject.getParentNode().getNodeName().equals(
				"rng:zeroOrMore") || chosenObject.getParentNode().getNodeName()
				.equals("rng:oneOrMore"))) {
			handleAddXNGItem((Element) chosenObject);
		}

		else if (!chosenObject.getParentNode().getNamespaceURI()
				.equals(rngaURI)
				&& !chosenObject.getParentNode().getNamespaceURI().equals(
						xngURI)) {

			handleRequiredElement();

		}

		else if (chosenObject.getParentNode().getNodeName().equals("xng:item")) {
			stop = false;
			Element retNode = null;
			selectedElement = domUtil.getChosenField((Element) chosenObject,
					retNode);
			if (selectedElement != null)
				handleNestedXNGItem((Element) chosenObject);
			// return;
		} else {

			Node parent = ele.getParentNode();

			// Find the current path to this chosen element
			String path = domUtil.findPath(dom.getBaseElement(), parent);

			// Examine this path and create an "xng:item" element after each
			// "rng:oneOrMore" or "rng:zeroOrMore"
			String newpath = domUtil.createXNGItemPath(dom.getBaseElement(),
					path);
			// if the path is different, we need to move our pointer to the one
			// with
			// the new "xng:item"

			if (!path.equals(newpath)) {
				NodeList nodeCandidates = dom.getElements(newpath
						+ ele.getNodeName());
				for (int j = nodeCandidates.getLength(); j > 0; j--) {
					Node oneNode = nodeCandidates.item(j - 1);
					if (oneNode.getNodeName().equals("rng:ref")) {
						if (dom.getAttributeValue((Element) oneNode, "name")
								.equals(ele.getAttribute("name"))) {
							handleTransform(oneNode);
							if (chosenObject.getNodeName().equals("swe:field")
									|| (chosenObject.getNodeName().equals(
											"sml:input") || (chosenObject
											.getNodeName().equals(
													"sml:parameter") || (chosenObject
											.getNodeName().equals("sml:output")))))
								this.handleEnterName();
							break;
						}
					} else if (!dom.existAttribute((Element) oneNode,
							"xng:selected")) {
						((Element) oneNode)
								.setAttribute("xng:selected", "true");
						break;
					}
				}

			}
		}
		this.firePropertyChange("INSERT", null, null);
		try {
			dom.serialize(dom.getBaseElement(), System.out, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// this method adds the name for only the first xng:item created - it does
	// not
	// search for one of many xng:items
	public void handleEnterName() {
		Element oneOrMore = dom.getElement((Element) chosenObject
				.getParentNode(), "");
		Element xngNode = dom.getElement((Element) oneOrMore, "xng:item");
		Element fieldEle = dom.getElement(xngNode, chosenObject.getNodeName());
		Element dataNode = dom.getElement((Element) fieldEle,
				"rng:attribute/rng:data");
		dom.setElementValue((Element) dataNode, "rng:value", "Enter name...");
		if (!dom.existAttribute(dataNode, "rng:value/@selected")) {
			dom.setAttributeValue((Element) dataNode, "rng:value/@selected",
					"true");
			String newID = domUtil.findUniqueID();
			dataNode.removeAttribute("id");
			dom.setAttributeValue((Element) dataNode, "id", newID);
		}
	}

	public void handleRequiredElement() {

		if (dom.hasQName(ele, "rng:ref")) {
			handleTransform(ele);

		} else {
			((Element) ele).setAttribute("xng:selected", "true");
		}

	}

	// This method adds an xng item instance to the chosen object - for example
	// add a second and subsequent input to InputList
	public void handleNestedXNGItem(Element object) {
		// find the chosen element of this xng:item and begin at that element.
		Element oneOrMore = null;
		if (dom.existElement((Element) selectedElement, "rng:oneOrMore")) {
			oneOrMore = dom.getElement(selectedElement, "rng:oneOrMore");
		} else if (dom
				.existElement((Element) selectedElement, "rng:zeroOrMore")) {
			oneOrMore = dom.getElement(selectedElement, "rng:zeroOrMore");
		}
		Element fieldEle = null;
		if (oneOrMore != null) {
			if (dom.existElement((Element) oneOrMore, "swe:field")) {
				fieldEle = dom.getElement(oneOrMore, "swe:field");
			} else if (dom.existElement((Element) oneOrMore, "sml:input")) {
				fieldEle = dom.getElement(oneOrMore, "sml:input");
			} else if (dom.existElement((Element) oneOrMore, "sml:output")) {
				fieldEle = dom.getElement(oneOrMore, "sml:output");
			}
		}

		Node xngNode = domUtil.createXNGItem(oneOrMore);
		Node copyNode = dom.getElement((Element) oneOrMore, fieldEle
				.getNodeName());
		Node nNode = dom.getDocument().importNode(copyNode, true);
		xngNode.appendChild(nNode);
		oneOrMore.appendChild(xngNode);
		fieldEle = dom.getElement((Element) xngNode, fieldEle.getNodeName());
		if (fieldEle.getNodeName().equals("swe:field")
				|| (fieldEle.getNodeName().equals("sml:input") || (fieldEle
						.getNodeName().equals("sml:parameter") || (fieldEle
						.getNodeName().equals("sml:output"))))) {
			Element dataNode = dom.getElement((Element) fieldEle,
					"rng:attribute/rng:data");
			dom.setElementValue((Element) dataNode, "rng:value",
					"Enter name...");
			if (!dom.existAttribute(dataNode, "rng:value/@selected")) {
				dom.setAttributeValue((Element) dataNode,
						"rng:value/@selected", "true");
				String newID = domUtil.findUniqueID();
				dataNode.removeAttribute("id");
				dom.setAttributeValue((Element) dataNode, "id", newID);
			}
		}

		Element retNode = null;
		String str = ele.getAttribute("name");
		if (str.equals(""))
			str = ele.getNodeName();

		if (str.contains(".")) {
			int startIndex = str.lastIndexOf(".");
			str = str.substring(startIndex + 1);
		} else if (str.contains(":")) {
			int startIndex = str.lastIndexOf(":");
			str = str.substring(startIndex + 1);
		}

		Node selectedNode = domUtil.findNode(fieldEle, str, retNode);
		if (selectedNode != null) {
			// Node baseElement = dom.getBaseElement();
			Node endElement = selectedNode.getParentNode();

			Node resolvedandSelected = null;
			String saveName = ((Element) selectedNode).getAttribute("name");
			if (dom.hasQName(selectedNode, "rng:ref")) {
				handleTransform(selectedNode);
				/*
				 * Relax2Hybrid transform1 = new Relax2Hybrid(); try {
				 * transform1.transform(srcDoc, dom, selectedNode); // now
				 * resolve the rng:ref transform1.retrieveRelaxNGRef((Element)
				 * selectedNode); } catch (DOMTransformException e1) { // TODO
				 * Auto-generated catch block e1.printStackTrace(); }
				 * 
				 * // saveName = saveName.replace(".", ":"); if
				 * (saveName.contains(".")) { int startIndex =
				 * saveName.lastIndexOf("."); saveName =
				 * saveName.substring(startIndex + 1); } else if
				 * (saveName.contains(":")) { int startIndex =
				 * saveName.lastIndexOf(":"); saveName =
				 * saveName.substring(startIndex + 1); } retNode = null;
				 * resolvedandSelected = domUtil.findNode(endElement, saveName,
				 * retNode); if (resolvedandSelected != null) {
				 * dom.setAttributeValue((Element) resolvedandSelected,
				 * "xng:selected", "true");
				 * 
				 * }
				 */
			} else {
				dom.setAttributeValue((Element) selectedNode, "xng:selected",
						"true");
			}

		}

	}

	public void handleAddXNGItem(Element object) {
		chosenObject = object;
		Element oneOrMore = dom.getElement((Element) chosenObject
				.getParentNode(), "");
		Node xngNode = domUtil.createXNGItem(oneOrMore);
		Node copyNode = dom.getElement((Element) oneOrMore, chosenObject
				.getNodeName());
		Node nNode = dom.getDocument().importNode(copyNode, true);
		xngNode.appendChild(nNode);
		oneOrMore.appendChild(xngNode);
		Element fieldEle = dom.getElement((Element) xngNode, chosenObject
				.getNodeName());
		if (chosenObject.getNodeName().equals("swe:field")
				|| (chosenObject.getNodeName().equals("sml:input") || (chosenObject
						.getNodeName().equals("sml:parameter") || (chosenObject
						.getNodeName().equals("sml:output"))))) {
			Element dataNode = dom.getElement((Element) fieldEle,
					"rng:attribute/rng:data");
			dom.setElementValue((Element) dataNode, "rng:value",
					"Enter name...");
			if (!dom.existAttribute(dataNode, "rng:value/@selected")) {
				dom.setAttributeValue((Element) dataNode,
						"rng:value/@selected", "true");
				String newID = domUtil.findUniqueID();
				dataNode.removeAttribute("id");
				dom.setAttributeValue((Element) dataNode, "id", newID);
			}
		}

		Element retNode = null;
		String str = ele.getAttribute("name");
		if (str.equals(""))
			str = ele.getNodeName();

		if (str.contains(".")) {
			int startIndex = str.lastIndexOf(".");
			str = str.substring(startIndex + 1);
		} else if (str.contains(":")) {
			int startIndex = str.lastIndexOf(":");
			str = str.substring(startIndex + 1);
		}

		Element beginsearch = dom.getElement(fieldEle, "");
		Node selectedNode = domUtil.findNode(beginsearch, str, retNode);
		if (selectedNode != null) {
			// Node baseElement = dom.getBaseElement();
			Node endElement = selectedNode.getParentNode();

			Node resolvedandSelected = null;
			String saveName = ((Element) selectedNode).getAttribute("name");
			if (dom.hasQName(selectedNode, "rng:ref")) {
				handleTransform(selectedNode);
				/*
				 * Relax2Hybrid transform1 = new Relax2Hybrid(); try {
				 * transform1.transform(srcDoc, dom, selectedNode); // now
				 * resolve the rng:ref transform1.retrieveRelaxNGRef((Element)
				 * selectedNode); } catch (DOMTransformException e1) { // TODO
				 * Auto-generated catch block e1.printStackTrace(); }
				 * 
				 * if (saveName.contains(".")) { int startIndex =
				 * saveName.lastIndexOf("."); saveName =
				 * saveName.substring(startIndex + 1); } else if
				 * (saveName.contains(":")) { int startIndex =
				 * saveName.lastIndexOf(":"); saveName =
				 * saveName.substring(startIndex + 1); } retNode = null;
				 * resolvedandSelected = domUtil.findNode(endElement, saveName,
				 * retNode); if (resolvedandSelected != null) {
				 * dom.setAttributeValue((Element) resolvedandSelected,
				 * "xng:selected", "true");
				 * 
				 * }
				 */
			} else {
				dom.setAttributeValue((Element) selectedNode, "xng:selected",
						"true");
			}

		}
	}

	public void handleTransform(Node node) {
		Node transformNode = node;
		Node parent = transformNode.getParentNode();
		String saveName = ele.getAttribute("name");

		Relax2Hybrid transform1 = new Relax2Hybrid();
		try {
			transform1.transform(srcDoc, dom, transformNode);
			transform1.retrieveRelaxNGRef((Element) transformNode);
		} catch (DOMTransformException e1) {
			e1.printStackTrace();
		}
		saveName = saveName.replace(".", ":");
		if (saveName.equals("xlink:href")) {
			Element selected = dom
					.getElement((Element) parent, "rng:attribute");
			selected.setAttribute("xng:selected", "true");
		} else {
			Element selected = dom.getElement((Element) parent, saveName);
			selected.setAttribute("xng:selected", "true");
		}

	}

	/*
	 * public Node findNode(Node n, String str, Node retNode) { NodeList nodes =
	 * dom.getChildElements(n); for (int i = 0; i < nodes.getLength(); i++) {
	 * Node oneNode = nodes.item(i); if
	 * (!oneNode.getNamespaceURI().equals(rngaURI) // &&
	 * !oneNode.getNodeName().equals("rng:optional") &&
	 * !oneNode.getNamespaceURI().equals(xngURI)) { if
	 * (str.equals(oneNode.getLocalName())) { return oneNode;
	 * 
	 * } else if (dom.hasQName(oneNode, "rng:ref")) { String name =
	 * dom.getAttributeValue((Element) oneNode, "name"); int startIndex =
	 * name.lastIndexOf("."); name = name.substring(startIndex + 1); if
	 * (name.equals(str)) return oneNode;
	 * 
	 * } else if (dom.hasQName(oneNode, "rng:attribute")) { String name =
	 * dom.getAttributeValue((Element) oneNode, "name"); int startIndex =
	 * name.lastIndexOf(":"); name = name.substring(startIndex + 1); if
	 * (name.equals(str)) return oneNode;
	 * 
	 * } else { // (dom.hasQName(oneNode, "rng:choice") // ||
	 * (dom.hasQName(oneNode, "rng:group"))) { retNode = findNode(oneNode, str,
	 * retNode); if (retNode != null) return retNode; } } } return retNode; }
	 */

	public void handleOptionalSelection() {
		// get path up to optional
		// Find the current path to this chosen element
		Element optionalNode = dom.getElement((Element) chosenObject
				.getParentNode(), "");

		if (optionalNode != null) {
			Node retNode = null;
			String str = "";
			if (dom.hasQName(ele, "rng:ref")) {
				str = ele.getAttribute("name");
			} else {
				str = ele.getNodeName();
			}

			if (str.contains(".")) {
				int startIndex = str.lastIndexOf(".");
				str = str.substring(startIndex + 1);
			} else if (str.contains(":")) {
				int startIndex = str.lastIndexOf(":");
				str = str.substring(startIndex + 1);
			}
			Node selectedNode = domUtil.findNode(optionalNode, str, retNode);
			String pathforme = domUtil.findPath(optionalNode, selectedNode);
			String myxngpath = domUtil.createXNGItemPath3(optionalNode,
					pathforme);

			NodeList xngEles = dom.getElements(optionalNode, myxngpath);

			for (int i = xngEles.getLength(); i > 0; i--) {
				if (xngEles.item(i - 1).getNodeName().equals("rng:ref")) {
					if (dom.getAttributeValue((Element) xngEles.item(i - 1),
							"name").equals(ele.getAttribute("name"))) {
						handleTransform(xngEles.item(i - 1));
						break;
					}
				} else if (!dom.existAttribute((Element) xngEles.item(i - 1),
						"xng:selected")) {
					((Element) xngEles.item(i - 1)).setAttribute(
							"xng:selected", "true");
					this.setChecked(true);
				}
			}
			if (!dom.existAttribute((Element) optionalNode, "xng:selected")) {
				optionalNode.setAttribute("xng:selected", "true");
			}

		}
		return;
	}

}
