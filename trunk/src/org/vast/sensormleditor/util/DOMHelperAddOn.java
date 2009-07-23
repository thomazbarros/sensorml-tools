package org.vast.sensormleditor.util;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.vast.sensormleditor.config.Configuration;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMHelperAddOn {

	private String rngURI = "http://relaxng.org/ns/structure/1.0";
	private String rngaURI = "http://relaxng.org/ns/compatibility/annotations/1.0";
	private String smlURI = "http://www.opengis.net/sensorML/1.0.1";
	private String sweURI = "http://www.opengis.net/swe/1.0.1";
	private String gmlURI = "http://www.opengis.net/gml";
	private String xlinkURI = "http://www.w3.org/1999/xlink";
	private String xngURI = "http://xng.org/1.0";
	private String coreSchema;
	private DOMHelper dom;
	private DOMHelper srcDoc;
	private boolean stop=false;

	public DOMHelperAddOn(DOMHelper dom) {
		this.dom = dom;

		try {
			coreSchema = Configuration.getSchema();
			srcDoc = new DOMHelper(coreSchema, false);
			srcDoc.addUserPrefix("rng", rngURI);
			srcDoc.addUserPrefix("sml", smlURI);
			srcDoc.addUserPrefix("swe", sweURI);
			srcDoc.addUserPrefix("gml", gmlURI);
			srcDoc.addUserPrefix("xlink", xlinkURI);

			srcDoc.getXmlDocument().addNS("sml", smlURI);
			srcDoc.getXmlDocument().addNS("swe", sweURI);
			srcDoc.getXmlDocument().addNS("gml", gmlURI);
			srcDoc.getXmlDocument().addNS("xlink", xlinkURI);
			srcDoc.getXmlDocument().addNS("rng", rngURI);
			srcDoc.getXmlDocument().addNS("a", rngaURI);

		} catch (DOMHelperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
/*	public Element getChosenOne(Element element,Element returnEle){
		stop = false;
		Element retElement = null;
		return this.getChosenField(element,retElement);
	}*/

	// This method returns the chosen field when element is swe:field, sml:input, 
	// sml:output, etc.
	public Element getChosenField(Element element, Element returnEle) {
		NodeList children = dom.getChildElements(element);
		for (int i = 0; i < children.getLength(); i++) {
			Element oneEle = (Element) children.item(i);
			if (!dom.hasQName(oneEle, "a:documentation")
					&& !dom.hasQName(oneEle, "rng:ref")
					&& !dom.hasQName(oneEle, "rng:optional")) {

				if (dom.hasQName(oneEle, "rng:choice")
						|| dom.hasQName(oneEle, "rng:group")) {
					returnEle = getChosenField(oneEle, returnEle);
					if (stop)
						return returnEle;
					
				} else if (dom.hasQName(oneEle, "rng:attribute") &&
					dom.getAttributeValue(oneEle,"name").equals("xlink:href")) {
					if (dom.existAttribute(oneEle,"xng:selected")){
						stop = true;
						return oneEle;
					}

				} else if (dom.existAttribute(oneEle, "xng:selected")) {
					stop = true;
					return oneEle;
				}
			}
		}
		return returnEle;
	}
	
	public void deleteChildren(Element element) {
		NodeList children = dom.getChildElements(element);
		for (int i = 0; i < children.getLength(); i++) {
			Element oneEle = (Element) children.item(i);
			if (!dom.hasQName(oneEle, "a:documentation")
					&& !dom.hasQName(oneEle, "rng:ref")) {
				if (dom.existAttribute(oneEle, "@xng:selected")){
					Node parent = oneEle.getParentNode();
					//oneEle.removeAttribute("xng:selected");	
					Element rngEle = dom.createElement("rng:ref");
					String name = oneEle.getNodeName();
					if (name.contains(":"))
						name = name.replace(":", ".");
					parent.removeChild(oneEle);
					rngEle.setAttribute("name", name);
					parent.appendChild(rngEle);
				/*	if (oneEle.getNodeName().equals("rng:data") &&
						dom.existElement(oneEle, "rng:value")){
						Element removeEle = dom.getElement(oneEle,"rng:value");
						oneEle.removeChild(removeEle);
						continue;
					} else if (oneEle.getNodeName().equals("rng:attribute") &&
						dom.existElement(oneEle, "rng:data/rng:value")){
						Element dataEle = dom.getElement(oneEle,"rng:data");
						Element removeEle = dom.getElement(dataEle,"rng:value");
						dataEle.removeChild(removeEle);
						continue;
					} else if (oneEle.getNodeName().equals("rng:value")) {
						oneEle.setTextContent("");
					} else if (dom.existElement(oneEle,"rng:attribute/rng:data/rng:value")){
						Element dataEle = dom.getElement(oneEle,"rng:attribute/rng:data");
						Element removeEle = dom.getElement(dataEle,"rng:value");
						dataEle.removeChild(removeEle);
						continue;
					}*/
				}
				if (oneEle.hasChildNodes())
					deleteChildren(oneEle);
			}
		}
		
	}
	
	// This method finds the matching node beginning with Node n and matching str
	public Node findNode(Node n, String str, Node retNode) {
		NodeList nodes = dom.getChildElements(n);
		for (int i = 0; i < nodes.getLength(); i++) {
			Node oneNode = nodes.item(i);
			if (!oneNode.getNamespaceURI().equals(rngaURI)
			// && !oneNode.getNodeName().equals("rng:optional")
					&& !oneNode.getNamespaceURI().equals(xngURI)) {
				if (str.equals(oneNode.getLocalName())) {
					return oneNode;

				} else if (dom.hasQName(oneNode, "rng:ref")) {
					String name = dom.getAttributeValue((Element) oneNode,
							"name");
					int startIndex = name.lastIndexOf(".");
					name = name.substring(startIndex + 1);
					if (name.equals(str))
						return oneNode;
					
				} else if (dom.hasQName(oneNode, "rng:attribute")) {
					String name = dom.getAttributeValue((Element) oneNode,
							"name");
					int startIndex = name.lastIndexOf(":");
					name = name.substring(startIndex + 1);
					if (name.equals(str))
						return oneNode;

				} else { // (dom.hasQName(oneNode, "rng:choice")
					// || (dom.hasQName(oneNode, "rng:group"))) {
					retNode = findNode(oneNode, str, retNode);
					if (retNode != null)
						return retNode;
				}
			}
		}
		return retNode;
	}
	
	public Node findPreviousNode(Node n, String str, Node retNode) {
		
		Node parent = n.getParentNode();
		
		if (parent.getLocalName().equals(str)){
			return parent;
		} else {
			retNode = findPreviousNode(parent,str,retNode);
			if (retNode != null)
				return retNode;
		}
		
		return retNode;
	}
	
	

	public String findPath(Node startNode, Node endNode) {
		ArrayList<String> currentPath = new ArrayList<String>();
		String path = "";
		currentPath.clear();

		boolean found = findPathHelper(currentPath, startNode, endNode);
		if (found) {
			for (int i = 0; i < currentPath.size(); i++) {
				path += currentPath.get(i) + "/";
			}
			return path;
		}
		return null;
	}

	public boolean findPathHelper(ArrayList<String> currentPath,
			Node startNode, Node endNode) {

		NodeList children = dom.getChildElements(startNode);

		for (int i = 0; i < children.getLength(); i++) {
			currentPath.add(children.item(i).getNodeName());
			if (children.item(i).getNodeName().equals(endNode.getNodeName())) {

				if (children.item(i).getParentNode() == endNode.getParentNode()) {
					return true;
				}
			}
			boolean done = findPathHelper(currentPath, children.item(i),
					endNode);
			if (done == true)
				return true;
			currentPath.remove(currentPath.size() - 1);
		}
		return false;
	}

	/*
	 * public void retrieveRelaxNGRef(Element refElt, boolean setSelected)
	 * throws DOMTransformException {
	 * 
	 * String refName = dom.getAttributeValue(refElt, "@name"); Element
	 * defineElt = findDefineElt(srcDoc.getXmlDocument(), refName);
	 * 
	 * if (defineElt != null) { Node parentNode = refElt.getParentNode();
	 * parentNode.removeChild(refElt); resolveRef(defineElt, parentNode,
	 * setSelected);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * public void resolveRef(Element defineElt, Node parentNode, boolean
	 * setSelected) { NodeList nl = dom.getChildElements(defineElt); for (int i
	 * = 0; i < nl.getLength(); i++) { Node child = nl.item(i); String paramPath
	 * = "swe:param/@name"; if (dom.hasQName(child, "rng:element")) { String
	 * newEleName = dom.getAttributeValue((Element) child, "@name"); Element
	 * newEle = dom.createElement(newEleName); parentNode.appendChild((Node)
	 * newEle); if (setSelected) selectOptionalChild(newEle);
	 * resolveRef((Element) child, newEle, false);
	 * 
	 * } else if (dom.hasQName(child, "rng:attribute") &&
	 * dom.hasQName(child.getParentNode(), "rng:element") &&
	 * dom.existElement((Element) child, "rng:value")) { Node newNode =
	 * dom.getDocument().importNode(child, false); String attValue =
	 * dom.getElementValue((Element) child, "rng:value"); String attName = dom
	 * .getAttributeValue((Element) child, "@name"); QName qName =
	 * getResultQName(attName); ((Element)
	 * newNode).setAttribute(qName.getFullName(), attValue);
	 * resolveRef((Element) child, newNode, false);
	 * 
	 * } else if (dom.hasQName(child, "rng:ref") && !dom.hasQName(parentNode,
	 * "rng:optional") && !dom.hasQName(parentNode, "rng:choice") &&
	 * !dom.hasQName(parentNode, "rng:zeroOrMore")) { String refName = dom
	 * .getAttributeValue((Element) child, "@name"); Element rngRefElt; try {
	 * rngRefElt = findDefineElt(srcDoc.getXmlDocument(), refName);
	 * resolveRef((Element) rngRefElt, parentNode, false); } catch
	 * (DOMTransformException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); } } else if (isElementQName(child, "rng:data") &&
	 * (dom.getAttributeValue((Element) child, paramPath) != null) &&
	 * (dom.getAttributeValue((Element) child, paramPath)
	 * .equalsIgnoreCase("dictionary"))) { String dicoPath =
	 * dom.getAttributeValue((Element) child, "swe:param/@value"); try {
	 * XMLFragment dicoFragment = dom.getLinkedFragment(dom .getXmlDocument(),
	 * dicoPath); NodeList entries = dom.getElements(dicoFragment
	 * .getBaseElement(), "dictionaryEntry");
	 * 
	 * Element choiceElt = dom.addElement(child, "rng:choice"); for (int k = 0;
	 * k < entries.getLength(); k++) { Element entry = (Element)
	 * entries.item(k); String defName = dom.getElementValue(entry, "name");
	 * Element newElt = dom .addElement(choiceElt, "+rng:value");
	 * newElt.setTextContent(defName); } } catch (Exception e) { //
	 * e.("Dictionary " + dicoPath + " cannot be found"); } } else if
	 * (child.getNamespaceURI() != null &&
	 * child.getNamespaceURI().equals(rngURI)) { Node newNode =
	 * dom.getDocument().importNode(child, false); newNode.setPrefix("rng"); //
	 * parentNode.insertBefore(newNode, parentNode.getFirstChild());
	 * parentNode.appendChild(newNode);
	 * 
	 * if (newNode.getNodeName().equals("rng:optional") ||
	 * (newNode.getNodeName().equals("rng:group")) ||
	 * (newNode.getNodeName().equals("rng:zeroOrMore") || (newNode
	 * .getNodeName().equals("rng:oneOrMore") || (newNode
	 * .getNodeName().equals("rng:choice") || (newNode
	 * .getNodeName().equals("rng:data")))))) { String newID = findUniqueID();
	 * if (!dom.existAttribute((Element) newNode, "@id")) {
	 * dom.setAttributeValue((Element) newNode, "@id", newID); } } if
	 * (isElement(child)) { String text = dom.getElementValue((Element) child,
	 * ""); if (text != null && !text.equals("")) dom.setElementValue((Element)
	 * newNode, text); resolveRef((Element) child, newNode, true); } //
	 * resolveRef((Element)child,newNode,false);
	 * 
	 * } else if (child.getNamespaceURI() != null &&
	 * child.getNamespaceURI().equals(rngaURI)) { // Node newNode = //
	 * parentNode.getOwnerDocument().importNode(child, // true); Node newNode =
	 * dom.getDocument().importNode(child, true);
	 * parentNode.appendChild(newNode); }
	 * 
	 * }
	 * 
	 * }
	 */
	/*
	 * public void insertElement(Node node) throws DOMTransformException {
	 * 
	 * String eleName = node.getNodeName();
	 * 
	 * eleName = eleName.replace(":", "."); Element defineElt =
	 * findDefineElt(srcDoc.getXmlDocument(), eleName);
	 * 
	 * if (defineElt != null) { Node parentNode = node.getParentNode(); //
	 * parentNode.removeChild(node); resolveRef(defineElt, parentNode, false); }
	 * }
	 */

	/*
	 * protected QName getResultQName(String name) { QName qname = new
	 * QName(name); String nsUri =
	 * dom.getXmlDocument().getNSUri(qname.getPrefix()); String resPrefix =
	 * dom.getXmlDocument().getNSPrefix(nsUri); qname.setPrefix(resPrefix);
	 * return qname; }
	 * 
	 * public boolean isElementQName(Node node, String qName) { if
	 * (node.getNodeType() == Node.ELEMENT_NODE && dom.hasQName(node, qName))
	 * return true; else return false; }
	 */

	/*
	 * protected Element findDefineElt(XMLDocument currentDoc, String refName)
	 * throws DOMTransformException { Element docElt =
	 * currentDoc.getDocumentElement();
	 * 
	 * // look through local define elements NodeList defineElts =
	 * srcDoc.getElements(docElt, "define"); for (int i = 0; i <
	 * defineElts.getLength(); i++) { Element defineElt = (Element)
	 * defineElts.item(i); String defName = defineElt.getAttribute("name"); if
	 * (refName.equals(defName)) return defineElt; }
	 * 
	 * // recursively look in included documents NodeList includeElts =
	 * srcDoc.getElements(docElt, "include"); for (int i = 0; i <
	 * includeElts.getLength(); i++) { Element includeElt = (Element)
	 * includeElts.item(i); String filePath =
	 * srcDoc.getAttributeValue(includeElt, "href");
	 * 
	 * try { XMLFragment includedDoc = srcDoc.getLinkedFragment(currentDoc,
	 * filePath); Element defElt = findDefineElt(includedDoc.getXmlDocument(),
	 * refName); if (defElt != null) return defElt; } catch (DOMHelperException
	 * e) { throw new DOMTransformException("Included schema " + filePath +
	 * " cannot be found"); } }
	 * 
	 * return null; }
	 */
	public synchronized String findUniqueID() {

		String uniqueID = "";
		int maxID = 0;

		int id = findUniqueIDHelper(dom.getBaseElement(), maxID);
		id++;
		uniqueID = Integer.toString(id);
		return uniqueID;
	}

	public synchronized int findUniqueIDHelper(Node node, int maxID) {

		NodeList children = dom.getChildElements(node);
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (dom.existAttribute((Element) node, "id")) {
				String tempID = dom.getAttributeValue((Element) node, "id");
				if (!tempID.equals("")) {
					int id = Integer.parseInt(tempID);
					int mm = Math.max(id, maxID);
					maxID = mm;
				}
			}
			if (child.hasChildNodes()) {
				// check child nodes for another number
				int id = findUniqueIDHelper(child, maxID);
				maxID = Math.max(id, maxID);
			}

		}

		return maxID;
	}

	public void handleIDCreation(Node node, int intID) {

		NodeList children = dom.getChildElements(node);
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getPrefix().equals("rng")
					&& !child.getNodeName().equals("rng:ref")) {
				String newID = new Integer(++intID).toString();
				((Element) child).setAttribute("id", newID);
			}
			handleIDCreation(child, intID);
		}

	}

	public boolean isElement(Node node) {
		return (node.getNodeType() == Node.ELEMENT_NODE);
	}



	public Node createXNGItem(Node node) {

		Element xngItemElt = dom.createElement("xng:item");
		String newID = findUniqueID();
		xngItemElt.setAttribute("id", newID);
		node.appendChild(xngItemElt);
		return xngItemElt;
	}


	public String createXNGItemPath(Node startNode, String oldPath) {

		StringTokenizer tokens = new StringTokenizer(oldPath, "/");

		String lastToken = tokens.nextToken();
		String newpath = lastToken;
		while (tokens.hasMoreElements()) {
			String currentToken = tokens.nextToken();
			if ((lastToken.equals("rng:oneOrMore") || lastToken
					.equals("rng:zeroOrMore"))
					&& (!currentToken.equals("xng:item"))) {
				NodeList nodeCandidates = dom.getElements(newpath);
				for (int j = 0; j < nodeCandidates.getLength(); j++) {

					Node n = nodeCandidates.item(j);
					if (dom.existElement((Element) n, currentToken)) {
						Node xngNode = createXNGItem(n);
						String n2path = newpath + "/" + currentToken;
						Node n2 = dom.getElement(n2path);
						Node newNode = dom.getDocument().importNode(n2, true);
						xngNode.appendChild(newNode);
						newpath += "/xng:item";
						break;
					}
				}
			}

			if ((lastToken.equals("rng:choice") && (!currentToken
					.equals("rng:choice")))) {
				NodeList choiceCandidates = dom.getElements(newpath + "/"
						+ currentToken);
				for (int k = 0; k < choiceCandidates.getLength(); k++) {
					Element choiceNode = (Element) choiceCandidates.item(k);
					if (!dom.existAttribute(choiceNode, "xng:selected")) {
						((Element) choiceNode).setAttribute("xng:selected",
								"true");
					}
					// String newID = findUniqueID();
					// choiceNode.removeAttribute("id");
					// dom.setAttributeValue((Element) choiceNode, "id", newID);

				}

			} else if (lastToken.equals("rng:optional")) {
				NodeList optionalCandidates = dom.getElements(newpath + "/"
						+ currentToken);
				for (int k = 0; k < optionalCandidates.getLength(); k++) {
					Node optionalNode = (Element) optionalCandidates.item(k);
					if (!dom.existAttribute((Element) optionalNode
							.getParentNode(), "xng:selected")) {
						((Element) optionalNode.getParentNode()).setAttribute(
								"xng:selected", "true");
						String newselectionID = findUniqueID();
						((Element) optionalNode.getParentNode())
								.removeAttribute("id");
						dom.setAttributeValue((Element) optionalNode
								.getParentNode(), "id", newselectionID);
					}
				}
				// ((Element) optionalNode.getParentNode()).setAttribute(
				// "xng:selected", "true");
			}
			newpath += "/" + currentToken;
			lastToken = currentToken;
		}

		return newpath + "/";
	}

	
	public String createXNGItemPath3(Node startNode, String oldPath) {

		StringTokenizer tokens = new StringTokenizer(oldPath, "/");

		String lastToken = tokens.nextToken();
		String newpath = lastToken;

		while (tokens.hasMoreElements()) {
			String currentToken = tokens.nextToken();
			if ((lastToken.equals("rng:oneOrMore") || lastToken
					.equals("rng:zeroOrMore"))
					&& (!currentToken.equals("xng:item"))) {
				NodeList nodeCandidates = dom.getElements((Element) startNode,
						newpath);
				for (int j = 0; j < nodeCandidates.getLength(); j++) {

					Node n = nodeCandidates.item(j);
					if (dom.existElement((Element) n, currentToken)) {
						Node xngNode = createXNGItem(n);

						String n2path = newpath + "/" + currentToken;
						Node n2 = dom.getElement((Element) startNode, n2path);
						Node newNode = dom.getDocument().importNode(n2, true);
						xngNode.appendChild(newNode);
						newpath += "/xng:item";
						break;
					}
				}
			}
			/*
			 * if ((lastToken.equals("rng:choice") &&
			 * (!currentToken.equals("rng:choice")))) { NodeList
			 * choiceCandidates = dom.getElements((Element)startNode,newpath +
			 * "/" + currentToken); for (int k = 0; k <
			 * choiceCandidates.getLength(); k++) { Element choiceNode =
			 * (Element) choiceCandidates.item(k); if
			 * (!dom.existAttribute(choiceNode, "xng:selected")) { ((Element)
			 * choiceNode).setAttribute("xng:selected", "true"); } }
			 * 
			 * } else
			 */if (lastToken.equals("rng:optional")) {
				NodeList optionalCandidates = dom.getElements(
						(Element) startNode, newpath + "/" + currentToken);
				for (int k = 0; k < optionalCandidates.getLength(); k++) {
					Node optionalNode = (Element) optionalCandidates.item(k);
					if (!dom.existAttribute((Element) optionalNode
							.getParentNode(), "xng:selected")) {
						((Element) optionalNode.getParentNode()).setAttribute(
								"xng:selected", "true");
						String newselectionID = findUniqueID();
						((Element) optionalNode.getParentNode())
								.removeAttribute("id");
						dom.setAttributeValue((Element) optionalNode
								.getParentNode(), "id", newselectionID);
					}
				}
				// ((Element) optionalNode.getParentNode()).setAttribute(
				// "xng:selected", "true");
			}
			newpath += "/" + currentToken;
			lastToken = currentToken;

		}

		return newpath + "/";
	}
	/*
	 * public void setElementValue(Node node, String text) { String value =
	 * text; Node changeNode = node; Node baseNode = dom.getBaseElement();
	 * 
	 * NodeList children = dom.getChildElements(changeNode); for (int k = 0; k <
	 * children.getLength(); k++) { if
	 * (!children.item(k).getNamespaceURI().equals(rngaURI)) { if
	 * (dom.hasQName(children.item(k), "rng:attribute")) { Node topNode =
	 * children.item(k); String nodepath = findPath(baseNode, changeNode);
	 * NodeList nodeCandidates = dom.getElements(nodepath); for (int j = 0; j <
	 * nodeCandidates.getLength(); j++) { Element oneNode = (Element)
	 * nodeCandidates.item(j);
	 * 
	 * if (dom.existAttribute(oneNode, "id")) { String thisID = dom
	 * .getAttributeValue(oneNode, "id"); if (dom.existAttribute((Element)
	 * changeNode, "id")) { String endID = dom.getAttributeValue( (Element)
	 * changeNode, "id"); if (endID.equals(thisID)) { if
	 * (!dom.existAttribute((Element) topNode, "rng:value/@selected")) { //
	 * ((Element)topNode).removeAttribute("rng:value/@selected");
	 * dom.setAttributeValue( (Element) topNode, "rng:value/@selected", "true");
	 * } dom.setElementValue((Element) topNode, "rng:value", value.toString());
	 * 
	 * dom.setElementValue((Element) topNode, "rng:value", value.toString());
	 * dom.setAttributeValue((Element) topNode, "rng:value/@selected", "true");
	 * 
	 * 
	 * } } }
	 * 
	 * } }
	 * 
	 * else { Node topNode = null;
	 * 
	 * topNode = locateNode(changeNode, "rng:data", topNode); if (topNode !=
	 * null) { String nodepath = findPath(baseNode, topNode); NodeList
	 * nodeCandidates = dom.getElements(nodepath); for (int j = 0; j <
	 * nodeCandidates.getLength(); j++) { Element oneNode = (Element)
	 * nodeCandidates.item(j);
	 * 
	 * if (dom.existAttribute(oneNode, "id")) { String thisID =
	 * dom.getAttributeValue(oneNode, "id"); if (dom.existAttribute((Element)
	 * topNode, "id")) { String endID = dom.getAttributeValue( (Element)
	 * topNode, "id"); if (endID.equals(thisID)) { if (!dom.existAttribute(
	 * (Element) topNode, "rng:value/@selected")) { dom.setAttributeValue(
	 * (Element) topNode, "rng:value/@selected", "true"); }
	 * dom.setElementValue((Element) topNode, "rng:value", value.toString()); }
	 * } }
	 * 
	 * } } } }
	 * 
	 * }
	 * 
	 * }
	 */

	/*
	 * public void setTextContent(Node node, String value) {
	 * node.setTextContent(value); }
	 */

}
