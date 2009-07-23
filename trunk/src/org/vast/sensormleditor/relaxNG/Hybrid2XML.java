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
 
 ******************************* END LICENSE BLOCK ***************************/

package org.vast.sensormleditor.relaxNG;

import org.vast.xml.QName;
import org.vast.xml.transform.DOMTransform;
import org.vast.xml.transform.DOMTransformException;
import org.vast.xml.transform.TransformTemplate;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Hybrid2XML extends DOMTransform {
	String xngURI = "http://xng.org/1.0";
	String rngURI = "http://relaxng.org/ns/structure/1.0";
	String rngaURI = "http://relaxng.org/ns/compatibility/annotations/1.0";
	String smlURI = "http://www.opengis.net/sensorML/1.0.1";
	String sweURI = "http://www.opengis.net/swe/1.0.1";
	String gmlURI = "http://www.opengis.net/gml";
	String ismURI = "urn:us:gov:ic:ism:v2";

	public Hybrid2XML() {
		super();
		TransformTemplate template;

		// //////////////
		// a:*, xng:* //
		// //////////////
		template = new TransformTemplate() {
			public boolean isMatch(DOMTransform transform, Node srcNode,
					Node resultNode) throws DOMTransformException {
				if (srcNode.getNamespaceURI() != null
						&& (srcNode.getNamespaceURI().equals(rngaURI) || srcNode
								.getNamespaceURI().equals(xngURI)))
					return true;

				return false;
			}

			public void apply(DOMTransform transform, Node srcNode,
					Node resultNode) throws DOMTransformException {
				// Do Nothing = Skip node and all its children
			}
		};
		this.templates.add(template);

		// ///////////////////////
		// sml:*, swe:*, gml:* //
		// ///////////////////////
		template = new TransformTemplate() {
			public boolean isMatch(DOMTransform transform, Node srcNode,
					Node resultNode) throws DOMTransformException {
				if (srcNode.getNamespaceURI() != null
						&& (srcNode.getNamespaceURI().equals(smlURI)
								|| srcNode.getNamespaceURI().equals(sweURI) || srcNode
								.getNamespaceURI().equals(gmlURI)))
					return true;

				return false;
			}

			public void apply(DOMTransform transform, Node srcNode,
					Node resultNode) throws DOMTransformException {
				// Copy node and process children
				Node newNode = resDom.getDocument().importNode(srcNode, false);
				resultNode.appendChild(newNode);

				// remove xng attributes
				((Element) newNode).removeAttribute("xng:selected");

				transform.applyTemplatesToChildElements(srcNode, newNode);
			}
		};
		this.templates.add(template);

		// ////////////////////////////////
		// rng:attribute with rng:value //
		// ////////////////////////////////
		template = new TransformTemplate() {
			public boolean isMatch(DOMTransform transform, Node srcNode,
					Node resultNode) throws DOMTransformException {
				if (isElementQName(srcNode, "rng:attribute")
						&& isElementQName(srcNode.getParentNode(),
								"rng:element")
						&& srcDom.existElement((Element) srcNode, "rng:value"))
					return true;

				return false;
			}

			public void apply(DOMTransform transform, Node srcNode,
					Node resultNode) throws DOMTransformException {
				String attValue = srcDom.getElementValue((Element) srcNode,
						"rng:value");
				String attName = srcDom.getAttributeValue((Element) srcNode,
						"@name");
				QName qName = getResultQName(attName);
				((Element) resultNode).setAttribute(qName.getFullName(),
						attValue);
			}
		};
		this.templates.add(template);

		// /////////////////
		// rng:attribute //
		// /////////////////
		template = new TransformTemplate() {
			public boolean isMatch(DOMTransform transform, Node srcNode,
					Node resultNode) throws DOMTransformException {
				if (isElementQName(srcNode, "rng:attribute"))
					return true;

				return false;
			}

			public void apply(DOMTransform transform, Node srcNode,
					Node resultNode) throws DOMTransformException {
				String attName = srcDom.getAttributeValue((Element) srcNode,
						"@name");
				resDom.setAttributeValue((Element) resultNode, attName, "");
				Node attNode = resultNode.getAttributes().getNamedItem(attName);
				transform.applyTemplatesToChildElements(srcNode, attNode);
			}
		};
		this.templates.add(template);

		// ////////////////
		// rng:optional //
		// ////////////////
		template = new TransformTemplate() {
			public boolean isMatch(DOMTransform transform, Node srcNode,
					Node resultNode) throws DOMTransformException {
				if (isElementQName(srcNode, "rng:optional"))
					return true;

				return false;
			}

			public void apply(DOMTransform transform, Node srcNode,
					Node resultNode) throws DOMTransformException {
				// Transform children if it has selected attribute
				String selected = srcDom.getAttributeValue((Element) srcNode,
						"@xng:selected");
				if (selected != null && selected.equalsIgnoreCase("TRUE"))
					transform
							.applyTemplatesToChildElements(srcNode, resultNode);
			}
		};
		this.templates.add(template);

	

		// /////////////////////////////////
		// rng:zeroOrMore, rng:oneOrMore //
		// /////////////////////////////////
		template = new TransformTemplate() {
			public boolean isMatch(DOMTransform transform, Node srcNode,
					Node resultNode) throws DOMTransformException {
				if (isElementQName(srcNode, "rng:zeroOrMore")
						|| isElementQName(srcNode, "rng:oneOrMore"))
					return true;

				return false;
			}

			public void apply(DOMTransform transform, Node srcNode,
					Node resultNode) throws DOMTransformException {
				// Transform each xng:item children
				NodeList items = srcDom.getElements((Element) srcNode,
						"xng:item");
				for (int i = 0; i < items.getLength(); i++)
					transform.applyTemplatesToChildElements(items.item(i),
							resultNode);
			}
		};
		this.templates.add(template);

		// //////////////
		// rng:choice //
		// //////////////
		template = new TransformTemplate() {
			public boolean isMatch(DOMTransform transform, Node srcNode,
					Node resultNode) throws DOMTransformException {
				if (isElementQName(srcNode, "rng:choice"))
					return true;

				return false;
			}

			public void apply(DOMTransform transform, Node srcNode,
					Node resultNode) throws DOMTransformException {
				// Transform first child with selected attribute
				NodeList children = srcDom.getChildElements(srcNode);
				for (int i = 0; i < children.getLength(); i++) {
					Element child = (Element) children.item(i);

					// Skip everything in annotation namespace
					if (child.getNamespaceURI() != null
							&& child.getNamespaceURI().equals(rngaURI))
						continue;

					String selected = srcDom.getAttributeValue(child,
							"@xng:selected");
					if (selected != null && selected.equalsIgnoreCase("TRUE")
							|| srcDom.hasQName(child, "rng:choice")
							|| srcDom.hasQName(child, "rng:group")) {
						transform.applyTemplatesToChildElements(srcNode,
								resultNode);
						break;
					}
				}
			}
		};
		this.templates.add(template);

		// ////////////
		// rng:data //
		// ////////////
		template = new TransformTemplate() {
			public boolean isMatch(DOMTransform transform, Node srcNode,
					Node resultNode) throws DOMTransformException {
				if (isElementQName(srcNode, "rng:data"))
					return true;

				return false;
			}

			public void apply(DOMTransform transform, Node srcNode,
					Node resultNode) throws DOMTransformException {

				String selected = srcDom.getAttributeValue((Element) srcNode,
						"rng:value/@selected");
				if (selected != null && selected.equalsIgnoreCase("TRUE")) {
					String val = srcDom.getElementValue((Element) srcNode,
							"rng:value");
					resDom.setNodeValue(resultNode, val);
				}
			}
		};
		this.templates.add(template);

		// /////////////
		// rng:value //
		// /////////////
		template = new TransformTemplate() {
			public boolean isMatch(DOMTransform transform, Node srcNode,
					Node resultNode) throws DOMTransformException {
				if (isElementQName(srcNode, "rng:value") )
					return true;

				return false;
			}

			public void apply(DOMTransform transform, Node srcNode,
					Node resultNode) throws DOMTransformException {
				// Duplicate text content in result element
				String selected = srcDom.getAttributeValue((Element) srcNode,
						"@selected");
				if (selected != null && selected.equalsIgnoreCase("TRUE")) {
					String val = srcDom.getElementValue((Element) srcNode, "");
					resDom.setNodeValue(resultNode, val);
				}
			}
		};
		this.templates.add(template);
		
		
		// ////////////
		// rng:text //
		// ////////////
		template = new TransformTemplate() {
			public boolean isMatch(DOMTransform transform, Node srcNode,
					Node resultNode) throws DOMTransformException {
				if (isElementQName(srcNode, "rng:text"))
					return true;

				return false;
			}

			public void apply(DOMTransform transform, Node srcNode,
					Node resultNode) throws DOMTransformException {

				String selected = srcDom.getAttributeValue((Element) srcNode,
						"@selected");
				if (selected != null && selected.equalsIgnoreCase("TRUE")) {
					String val = srcDom.getElementValue((Element) srcNode,
							"");
					resDom.setNodeValue(resultNode, val);
				}
			}
		};
		this.templates.add(template);

		// ////////////
		// rng:list //
		// ////////////
		template = new TransformTemplate() {
			public boolean isMatch(DOMTransform transform, Node srcNode,
					Node resultNode) throws DOMTransformException {
				if (isElementQName(srcNode, "rng:list"))
					return true;

				return false;
			}

			public void apply(DOMTransform transform, Node srcNode,
					Node resultNode) throws DOMTransformException {
				// Extract list values and write them in resultNode text node as
				// a space separated list
				StringBuffer listValues = new StringBuffer();

				NodeList items = srcDom.getAllChildElements((Element) srcNode);
				if (items.item(0).getNodeName().equals("rng:oneOrMore") ||
						(items.item(0).getNodeName().equals("rng:zeroOrMore"))) {
					NodeList items1 = srcDom.getElements((Element) items
							.item(0), "xng:item");
					for (int i = 0; i < items1.getLength(); i++) {
						Element newElt = resDom.createElement("v");
						transform.applyTemplatesToChildElements(items1.item(i),
										newElt);
						if (i != 0)
							listValues.append(' ');

						listValues.append(resDom.getElementValue(newElt));
					}
						//
				} else {

					for (int i = 0; i < items.getLength(); i++) {
						Element newElt = resDom.createElement("v");
						transform.applyTemplates(items.item(i), newElt);
						if (i != 0)
							listValues.append(' ');

						listValues.append(resDom.getElementValue(newElt));
					}
					
				}
				resDom.setNodeValue(resultNode, listValues.toString());
 			}
		};
		this.templates.add(template);

		// //////////////////////
		// All other RNG Tags //
		// //////////////////////
		template = new TransformTemplate() {
			public boolean isMatch(DOMTransform transform, Node srcNode,
					Node resultNode) throws DOMTransformException {
				if (srcNode.getNamespaceURI() != null
						&& srcNode.getNamespaceURI().equals(rngURI))
					return true;

				return false;
			}

			public void apply(DOMTransform transform, Node srcNode,
					Node resultNode) throws DOMTransformException {
				// Skip node and process all children elements
				transform.applyTemplatesToChildElements(srcNode, resultNode);
			}
		};
		this.templates.add(template);
	}

	@Override
	public void init() {
		srcDom.addUserPrefix("rng", rngURI);
		srcDom.addUserPrefix("xng", xngURI);
		srcDom.addUserPrefix("a", rngaURI);
		srcDom.addUserPrefix("sml", "http://www.opengis.net/sensorML/1.0.1");
		srcDom.addUserPrefix("swe", "http://www.opengis.net/swe/1.0.1");
		srcDom.addUserPrefix("gml", "http://www.opengis.net/gml");
		srcDom.addUserPrefix("xlink", "http://www.w3.org/1999/xlink");
		srcDom.addUserPrefix("ism", ismURI);

		resDom.addUserPrefix("sml", "http://www.opengis.net/sensorML/1.0.1");
		resDom.addUserPrefix("swe", "http://www.opengis.net/swe/1.0.1");
		resDom.addUserPrefix("gml", "http://www.opengis.net/gml");
		resDom.addUserPrefix("xlink", "http://www.w3.org/1999/xlink");
		resDom.addUserPrefix("ism", ismURI);
	}
}
