/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
 Alexandre Robin <robin@nsstc.uah.edu>
 Mike Botts <mike.botts@uah.edu>
 
 ******************************* END LICENSE BLOCK ***************************/

package org.vast.sensormleditor.editors;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * <p>
 * <b>Title:</b> SensorML Tree Content Provider
 * </p>
 * 
 * <p>
 * <b>Description:</b><br/>
 * TODO SMLTreeContentProvider type description
 * </p>
 * 
 * <p>
 * Copyright (c) 2005
 * </p>
 * 
 * @author Mike Botts
 * @author Alexandre Robin
 * @date May 22, 2006
 * @version 1.0
 */
public class SMLTreeContentProvider implements ITreeContentProvider,
		IPropertySourceProvider {
	protected TreeViewer viewer;

	protected DOMHelper dom;
	protected boolean selected = false;

	String xngURI = "http://xng.org/1.0";
	String rngURI = "http://relaxng.org/ns/structure/1.0";
	String rngaURI = "http://relaxng.org/ns/compatibility/annotations/1.0";
	String smlURI = "http://www.opengis.net/sensorML/1.0.1";
	String sweURI = "http://www.opengis.net/swe/1.0.1";
	String gmlURI = "http://www.opengis.net/gml";

	protected boolean stop = false;

	public SMLTreeContentProvider(TreeViewer viewer, DOMHelper dom) {
		this.viewer = viewer;
		this.dom = dom;

		dom.addUserPrefix("rng", rngURI);
		dom.addUserPrefix("xng", xngURI);
		dom.addUserPrefix("a", rngaURI);
		dom.addUserPrefix("sml", smlURI);
		dom.addUserPrefix("swe", sweURI);
		dom.addUserPrefix("gml", gmlURI);
	}

	/**
	 * Retrieves all objects to be displayed as children of the parent object in
	 * the tree
	 */
	public Object[] getChildren(Object parent) {

		if (parent instanceof Element) {

			ArrayList<Object> childArray = new ArrayList<Object>();

			traverseTree((Node) parent, childArray);

			return childArray.toArray();
		}
		return null;
	}

	public void traverseTree(Node node, ArrayList childArray) {
		NodeList c = dom.getChildElements(node);
		for (int i = 0; i < c.getLength(); i++) {
			Node child = c.item(i);
			if (!child.getNamespaceURI().equals("rngaURI")) {
				if (child.getNodeName().equals("rng:choice")
						|| child.getNodeName().equals("rng:ref")
						|| child.getNodeName().equals("rng:group")
						|| child.getNodeName().equals("rng:attribute")
						|| child.getNodeName().equals("a:documentation")) {
					traverseTree(child, childArray);

				} else if (child.getNodeName().equals("rng:oneOrMore")
						|| child.getNodeName().equals("rng:zeroOrMore")) {
					handleXNGItem(child, childArray);

				} else if (child.getNodeName().equals("rng:optional")) {
					if (dom.existAttribute((Element) child, "selected"))
						traverseTree(child, childArray);

				} else if (dom.hasQName(child, "sml:identifier")
						&& (dom.hasQName(node, "xng:item"))) {
					childArray.add(child);

				} else if (dom.hasQName(child, "sml:classifier")
						&& (dom.hasQName(node, "xng:item"))) {
					childArray.add(child);
					
				} else if (dom.hasQName(child, "swe:field")
						&& (dom.hasQName(node, "xng:item"))) {
					stop = false;
					Element retNode = null;
					Element chosenOne = getChosenField((Element) child,
							retNode);
					if (chosenOne != null)
						childArray.add(child);
					
				} else if (dom.hasQName(child, "swe:data")
						&& (dom.hasQName(node, "swe:ConditionalValue"))) {
				/*	stop = false;
					Element retNode = null;
					Element chosenOne = getChosenField((Element) child,
							retNode);
					if (chosenOne != null)*/
						childArray.add(child);
					
				} else if (dom.hasQName(child, "sml:input")
						&& (dom.hasQName(node, "xng:item"))) {
					childArray.add(child);

				} else if (dom.hasQName(child, "sml:output")
						&& (dom.hasQName(node, "xng:item"))) {
					childArray.add(child);
					
				} else if (dom.hasQName(child, "swe:quality")
						&& (dom.hasQName(node, "xng:item"))) {
					childArray.add(child);

				} else if (dom.hasQName(child, "sml:parameter")
						&& (dom.hasQName(node, "xng:item"))) {
					childArray.add(child);

				} else if (dom.hasQName(child, "sml:component")
						&& (dom.hasQName(node, "xng:item"))) {
					childArray.add(child);
					
				} else if (dom.hasQName(child, "sml:connection")
						&& (dom.hasQName(node, "xng:item"))) {
					childArray.add(child);
					
				} else if (dom.hasQName(child, "swe:coordinate")
						&& (dom.hasQName(node, "xng:item"))) {
					childArray.add(child);
					
				} else if (dom.hasQName(child, "swe:case")
						&& (dom.hasQName(node, "xng:item"))) {
					childArray.add(child);
					
				} else if (dom.hasQName(child, "swe:condition")
						&& (dom.hasQName(node, "xng:item"))) {
					childArray.add(child);
					
				} else if (dom.hasQName(child, "swe:member")
						&& (dom.hasQName(node, "xng:item"))) {
					childArray.add(child);
					
				} else if (dom.hasQName(child, "swe:elementCount")
						&& (dom.hasQName(node, "swe:DataArray"))) {
					childArray.add(child);
					
				} else if (dom.hasQName(child, "swe:elementType")
						&& (dom.hasQName(node, "swe:DataArray"))) {
					childArray.add(child);
					
				} else if ((dom.hasQName(child, "gml:timePosition")
						|| dom.hasQName(child, "gml:beginPosition")
						|| dom.hasQName(child, "gml:endPosition")) &&
						(dom.existAttribute((Element) node,"selected"))){
					childArray.add(child);

				} else if (dom.hasQName(child, "sml:member")
						|| dom.hasQName(child, "sml:identification")
						|| dom.hasQName(child, "sml:classification")
						|| dom.hasQName(child, "sml:characteristics")
						|| dom.hasQName(child, "sml:capabilities")
						|| dom.hasQName(child, "sml:contact")
						|| dom.hasQName(child, "sml:method")
						|| dom.hasQName(child, "sml:documentation")
						|| dom.hasQName(child, "sml:validTime")
						|| dom.hasQName(child, "sml:securityConstraint")
						|| dom.hasQName(child, "swe:StandardFormat")
						|| dom.hasQName(child, "swe:TextBlock")
						|| dom.hasQName(child, "swe:BinaryBlock")
						|| dom.hasQName(child, "sml:inputs")
						|| dom.hasQName(child, "sml:outputs")
						|| dom.hasQName(child, "swe:quality")
						|| dom.hasQName(child, "swe:constraint")
						|| dom.hasQName(child, "sml:parameters")
						|| dom.hasQName(child, "sml:components")
						|| dom.hasQName(child, "sml:connections")
						|| dom.hasQName(child, "sml:keywords")) {
					childArray.add(child);

				} else {
					traverseTree(child, childArray);
				}

			}
		}
	}

	/*
	 * public void traverseTree(Node node, ArrayList childArray) { NodeList c =
	 * dom.getChildElements(node); for (int i = 0; i < c.getLength(); i++) { if
	 * (!c.item(i).getNamespaceURI().equals(rngaURI)){ if
	 * (c.item(i).getNodeName().equals("rng:choice")) {
	 * //handleChoice(c.item(i), childArray); traverseTree(c.item(i),
	 * childArray); } else if (c.item(i).getNodeName().equals("rng:oneOrMore"))
	 * { //traverseTree(c.item(i), childArray); handleXNGItem
	 * (c.item(i),childArray);
	 * 
	 * } else if (c.item(i).getNodeName().equals("rng:zeroOrMore")) {
	 * //traverseTree(c.item(i), childArray); handleXNGItem
	 * (c.item(i),childArray); } else if
	 * (c.item(i).getNodeName().equals("rng:optional")) { if
	 * (dom.existAttribute((Element) c.item(i), "selected"))
	 * traverseTree(c.item(i), childArray); } else if
	 * (c.item(i).getNodeName().equals("rng:attribute") &&
	 * dom.existAttribute((Element) c.item(i), "selected") &&
	 * dom.getAttributeValue((Element)c.item(i),"name").equals("xlink:href")) {
	 * childArray.add(c.item(i)); } else if (dom.hasQName(c.item(i),
	 * "sml:member")&& (dom.hasQName(node, "xng:item"))){
	 * childArray.add(c.item(i)); } else if (dom.hasQName(c.item(i),
	 * "sml:identifier") && (dom.hasQName(node, "xng:item"))){
	 * childArray.add(c.item(i)); } else if (dom.hasQName(c.item(i),
	 * "sml:classifier") && (dom.hasQName(node, "xng:item"))){
	 * childArray.add(c.item(i)); } else if (dom.hasQName(c.item(i),
	 * "swe:DataRecord") && (dom.existAttribute((Element) c.item(i),
	 * "xng:selected"))){ childArray.add(c.item(i)); } else if
	 * (dom.hasQName(c.item(i), "swe:field") && (dom.hasQName(node,
	 * "xng:item"))){ stop = false; Element retNode = null; Element chosenOne =
	 * getChosenField((Element)c.item(i),retNode); if (chosenOne != null &&
	 * dom.hasQName(chosenOne,"swe:DataRecord"))
	 * traverseTree(c.item(i),childArray); else childArray.add(c.item(i)); }
	 * else if (dom.hasQName(c.item(i), "sml:input") && (dom.hasQName(node,
	 * "xng:item"))){ childArray.add(c.item(i)); } else if
	 * (dom.hasQName(c.item(i), "sml:output") && (dom.hasQName(node,
	 * "xng:item"))){ childArray.add(c.item(i)); } else if
	 * (dom.hasQName(c.item(i), "sml:parameter") && (dom.hasQName(node,
	 * "xng:item"))){ childArray.add(c.item(i)); } else if
	 * (dom.hasQName(c.item(i), "sml:component") && (dom.hasQName(node,
	 * "xng:item"))){ childArray.add(c.item(i)); } else if
	 * (!Character.isUpperCase(c.item(i).getLocalName() .charAt(0))) {
	 * 
	 * traverseTree(c.item(i), childArray); } else { if (dom.hasQName(c.item(i),
	 * "sml:ProcessModel") || (dom.hasQName(c.item(i), "sml:System"))){
	 * traverseTree(c.item(i), childArray); } childArray.add(c.item(i)); } } } }
	 */

	public Element getChosenField(Element element, Element returnEle) {

		NodeList children = dom.getChildElements(element);
		for (int i = 0; i < children.getLength(); i++) {
			Element oneEle = (Element) children.item(i);
			if (!dom.hasQName(oneEle, "rng:attribute")
					&& !dom.hasQName(oneEle, "a:documentation")
					&& !dom.hasQName(oneEle, "rng:ref")
					&& !dom.hasQName(oneEle, "rng:optional")) {

				if (dom.hasQName(oneEle, "rng:choice")
						|| dom.hasQName(oneEle, "rng:group")) {
					returnEle = getChosenField(oneEle, returnEle);
					if (stop)
						return returnEle;

				} else if (dom.existAttribute(oneEle, "@xng:selected")) {
					stop = true;
					return oneEle;
				}
			}
		}
		return returnEle;
	}

	/*
	 * public void handleChoice(Node node, ArrayList childArray) { NodeList c =
	 * dom.getChildElements(node); for (int i = 0; i < c.getLength(); i++) { if
	 * (!c.item(i).getNamespaceURI().equals(rngaURI) &&
	 * !c.item(i).getNamespaceURI().equals(xngURI)) { if
	 * (dom.existAttribute((Element) c.item(i), "xng:selected")) { if
	 * (!Character.isUpperCase(c.item(i).getLocalName().charAt( 0))) {
	 * traverseTree(c.item(i), childArray); } else { childArray.add(c.item(i));
	 * } } } } }
	 */

	public void handleXNGItem(Node node, ArrayList childArray) {
		NodeList items = dom.getElements((Element) node, "xng:item");
		for (int i = 0; i < items.getLength(); i++) {
			traverseTree(items.item(i), childArray);
		}

	}

	/**
	 * Retrieves the parent of the given object
	 */
	public Object getParent(Object element) {
		return (Object) ((Element) element).getParentNode();
	}

	/**
	 * Decides if this parent actually has children if yes, getChildren will be
	 * called as well
	 */
	public boolean hasChildren(Object parent) {
		if (parent instanceof Element) {
			NodeList children = dom.getChildElements((Node) parent);

			if (children.getLength() == 0)
				return false;
			/*
			 * else if (dom.hasQName((Node) parent, "sml:KeywordList")) return
			 * false; else if (dom.hasQName((Node) parent, "swe:field")) return
			 * false; else if (dom.hasQName((Node) parent, "sml:identifier"))
			 * return false; else if (dom.hasQName((Node) parent,
			 * "sml:classifier")) return false; else if (dom.hasQName((Node)
			 * parent, "sml:input")) return false; else if (dom.hasQName((Node)
			 * parent, "sml:component")) return false; else if
			 * (dom.hasQName((Node) parent, "sml:output")) return false; else if
			 * (dom.hasQName((Node) parent, "sml:ResponsibleParty")) return
			 * false;
			 */

			return true;
		}
		return false;
	}

	/**
	 * Retrieves objects to be used as the tree root element(s)
	 */
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Document)
			return new Object[] { ((Document) inputElement)
					.getDocumentElement() };
		else
			return null;
	}

	public void dispose() {

	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput instanceof Document)
			if (newInput != oldInput)
				dom = new DOMHelper((Document) newInput);
		dom.addUserPrefix("xng", xngURI);
	}

	public IPropertySource getPropertySource(Object object) {
		if (object instanceof Element) {
			Element ele = (Element) object;
		/*	if (dom.hasQName(ele, "sml:SensorML")) {
				return (IPropertySource) new DocumentPropertySource(ele, dom);
			}*/
		}
		if (object instanceof IPropertySource) {
			return (IPropertySource) object;
		} else
			return null;
	}

}