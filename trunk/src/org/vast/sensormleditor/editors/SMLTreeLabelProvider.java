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

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.vast.sensormleditor.apps.SensorMLEditorPlugin;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * *
 * <p>
 * <b>Title:</b> SensorML Tree Label Provider
 * </p>
 * 
 * <p>
 * <b>Description:</b><br/> TODO SMLTreeLabelProvider type description
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
public class SMLTreeLabelProvider extends LabelProvider implements ITableLabelProvider,
		ITableColorProvider, ITableFontProvider, ILabelProvider {
	protected Image folderImg, fileImg, infoImg, inImg, outImg, paramImg,
			procImg, chainImg, linkImg, itemImg, gearImg;
	
	

	protected Font fontNormal, fontBold, fontItalic;

	protected TreeViewer viewer;
	protected boolean stop = false;
	protected DOMHelper dom;
	protected DOMHelperAddOn domUtil;

	String xngURI = "http://xng.org/1.0";
	String rngURI = "http://relaxng.org/ns/structure/1.0";
	String rngaURI = "http://relaxng.org/ns/compatibility/annotations/1.0";
	String smlURI = "http://www.opengis.net/sensorML/1.0.1";
	String sweURI = "http://www.opengis.net/swe/1.0.1";
	String gmlURI = "http://www.opengis.net/gml";

	public SMLTreeLabelProvider(TreeViewer viewer, DOMHelper dom) {
		this.viewer = viewer;
		this.dom = dom;
		

		folderImg = PlatformUI.getWorkbench().getSharedImages().getImage(
				ISharedImages.IMG_OBJ_FOLDER);
		fileImg = PlatformUI.getWorkbench().getSharedImages().getImage(
				ISharedImages.IMG_OBJ_FILE);
		inImg = PlatformUI.getWorkbench().getSharedImages().getImage(
				ISharedImages.IMG_TOOL_FORWARD);
		outImg = PlatformUI.getWorkbench().getSharedImages().getImage(
				ISharedImages.IMG_TOOL_BACK);
		paramImg = PlatformUI.getWorkbench().getSharedImages().getImage(
				ISharedImages.IMG_TOOL_UP);

		infoImg = PlatformUI.getWorkbench().getSharedImages().getImage(
				ISharedImages.IMG_OBJS_INFO_TSK);
		itemImg = PlatformUI.getWorkbench().getSharedImages().getImage(
				ISharedImages.IMG_OBJ_FILE);

		
		procImg = SensorMLEditorPlugin.getImageDescriptor("icons/proc.gif")
				.createImage();
		chainImg = SensorMLEditorPlugin.getImageDescriptor("icons/chain.gif")
				.createImage();

		linkImg = SensorMLEditorPlugin.getImageDescriptor("icons/input.gif")
				.createImage();
		
		gearImg = SensorMLEditorPlugin.getImageDescriptor("icons/gear.gif")
				.createImage();

		fontNormal = new Font(PlatformUI.getWorkbench().getDisplay(), "Tahoma",
				8, SWT.NORMAL);
		fontItalic = new Font(PlatformUI.getWorkbench().getDisplay(), "Tahoma",
				8, SWT.ITALIC);
		fontBold = new Font(PlatformUI.getWorkbench().getDisplay(), "Tahoma",
				8, SWT.BOLD);
	}
	
	public void dispose() {
		fontNormal.dispose();
		fontItalic.dispose();
		fontBold.dispose();

		procImg.dispose();
		chainImg.dispose();
		gearImg.dispose();
		linkImg.dispose();
	}

	/**
	 * Decides what text to print in the tree cell depending on the cell object
	 * and column index
	 */
	public String getColumnText(Object object, int columnIndex) {

		if (object instanceof Element) {

			Element element = (Element) object;
			String label = "";

			if (columnIndex == 0) {

				if (dom.hasQName(element, "rng:optional")) {
					NodeList c = dom.getChildElements(element);
					for (int j = 0; j < c.getLength(); j++) {
						if (!(dom.hasQName(c.item(j), "a:documentation"))) {
							if (dom.existAttribute((Element) c.item(j), "name"))
								return dom.getAttributeValue((Element) c
										.item(j), "name");
						}
					}
				} else if (dom.hasQName(element, "sml:identifier")
						|| dom.hasQName(element, "sml:classifier")){
					if (dom.existAttribute(element, "name"))
						return dom.getAttributeValue(element, "name");
					String label1 = "";
					String label2 = locateValue(element, "rng:value", label1);
					if (label2 != "" && label2 != null)
						return label2;
					return "name";
					
				} else if (dom.hasQName(element, "sml:characteristics")){
					return element.getLocalName();
					
				} else if (dom.hasQName(element, "swe:StandardFormat") ||
						dom.hasQName(element, "swe:TextBlock") ||
						dom.hasQName(element, "swe:BinaryBlock")){
					return "encoding";
					
				} else if (dom.hasQName(element, "swe:field")
						|| dom.hasQName(element,"sml:parameter")
						|| dom.hasQName(element, "sml:input")
						|| //dom.hasQName(element, "sml:component") ||
						dom.hasQName(element, "sml:output")) {
					NodeList c = dom.getChildElements(element);
					for (int j = 0; j < c.getLength(); j++) {
						if (dom.hasQName(c.item(j), "a:documentation"))
							continue;
						else if (dom.hasQName(c.item(j), "rng:attribute")) {
							String attValue = dom.getElementValue((Element) c
									.item(j), "rng:data/rng:value");
							return attValue;
						} else {
							return c.item(j).getLocalName();
						}
					}
					return "";
			
					
				} else if (dom.hasQName(element, "rng:group")) {
					return element.getParentNode().getParentNode()
							.getLocalName();
				} else if (dom.hasQName(element, "rng:attribute"))
					return dom.getAttributeValue(element, "name");

				return element.getLocalName();
			}

			else if (columnIndex == 1) {

				if (dom.hasQName(element, "rng:optional")) {
					NodeList c = dom.getChildElements(element);
					for (int j = 0; j < c.getLength(); j++) {
						if (!(dom.hasQName(c.item(j), "a:documentation"))) {
							return c.item(j).getNodeValue();
						}
					}
					
				} else if (dom.hasQName(element, "sml:connections")){
					return "ConnectionList";
				} else if (dom.hasQName(element, "sml:connection")){
					if (dom.existElement(element, "rng:optional")) {
						Element opt = dom.getElement(element,"rng:optional");
						if (dom.existAttribute(opt,"selected")){
							Element valueEle = dom.getElement(opt,"rng:attribute/rng:data/rng:value");
							if (valueEle!=null)
								return valueEle.getTextContent();
						}
						return "";
					}
						
				} else if (dom.hasQName(element, "sml:securityConstraint")){
					return "Security";
					
				} else if (dom.hasQName(element, "swe:StandardFormat") ||
						dom.hasQName(element, "swe:TextBlock") ||
						dom.hasQName(element, "swe:BinaryBlock")){
					return element.getLocalName();
					
				} else if (dom.hasQName(element, "sml:method")){
					return "xlink:href";
					
				} else if (dom.hasQName(element, "sml:member") || 
						dom.hasQName(element, "sml:identification")||
						dom.hasQName(element, "swe:member")||
						dom.hasQName(element, "sml:classification")||
						dom.hasQName(element, "sml:characteristics")||
						dom.hasQName(element, "sml:capabilities")||
						dom.hasQName(element, "sml:contact")||
						dom.hasQName(element, "sml:documentation")||
						dom.hasQName(element, "sml:validTime")||
						dom.hasQName(element, "gml:timePosition")||
						dom.hasQName(element, "gml:beginPosition")||
						dom.hasQName(element, "gml:endPosition")||
						dom.hasQName(element, "swe:constraint")||
						dom.hasQName(element, "sml:inputs")||
						dom.hasQName(element, "sml:input")||
						dom.hasQName(element, "sml:outputs")||
						dom.hasQName(element, "sml:output")||
						dom.hasQName(element, "swe:quality")||
						dom.hasQName(element, "swe:elementCount")||
						dom.hasQName(element, "swe:elementType")||
						dom.hasQName(element, "swe:coordinate")||
						dom.hasQName(element, "swe:case")||
						dom.hasQName(element, "swe:condition")||
						dom.hasQName(element, "swe:data")||
						dom.hasQName(element, "sml:parameters")||
						dom.hasQName(element, "sml:parameter")||
						dom.hasQName(element, "swe:DataRecord")||
						dom.hasQName(element, "sml:components")||
						dom.hasQName(element, "sml:component")||
						dom.hasQName(element, "swe:field")||
						dom.hasQName(element,"sml:keywords")){
					//stop = false;
					Element retNode = null;
					domUtil = new DOMHelperAddOn(dom);
					//Element chosenOne = domUtil.getChosenOne(element);
					Element chosenOne = domUtil.getChosenField(element, retNode);
					if (chosenOne == null)return "";
					if (chosenOne.getNodeName().equals("rng:attribute")) {
						return dom.getAttributeValue(chosenOne,"name");
					}
					else if (chosenOne.getNodeName().equals("rng:data")){
						return dom.getAttributeValue(chosenOne,"type");
					}
					else
						return chosenOne.getLocalName();
					
				} else if (dom.hasQName(element, "sml:identifier")
						|| (dom.hasQName(element, "sml:classifier"))) {
					String label1 = "";
					boolean found = false;
					//Element newelement = locateNode(element, "sml:value",
					//		element, found);
					Element valueEle = dom.getElement(element,"sml:Term/sml:value/rng:data/rng:value");
					if (valueEle != null)
						return valueEle.getTextContent();
						//return locateValue(newelement, "rng:value", label1);
					else
						return label1;
				} else if (dom.hasQName(element, "rng:attribute")){
					return dom.getAttributeValue(element,"name");
				}

				if (dom.getElementValue(element, "") != null)
					label = dom.getElementValue(element, "");

				if (label == null || label.equals("")) {
					if (element.hasAttributes()) {
						NamedNodeMap attList = element.getAttributes();
						if (attList != null) {
							if (attList.item(0).getLocalName() != null  && 
									!attList.item(0).getLocalName().equals("id")) {
								if (!attList.item(0).getLocalName().equals(
										"selected"))
									return attList.item(0).getLocalName() + "="
											+ attList.item(0).getNodeValue();
							}
						}
					}
				}
				return label;
			}

		}
		return null;

	}
	
/*	public Element getChosenField(Element element, Element returnEle) {

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
						dom.getAttributeValue(oneEle,"name").equals("xlink:href")){
					stop = true;
					return oneEle;
					
				} else if (dom.existAttribute(oneEle, "@xng:selected")) {
					stop = true;
					return oneEle;
				}
			}
		}
		return returnEle;
	}*/

	public Element locateNode(Element startNode, String tag, Element foundEle,
			boolean found) {

		NodeList c = dom.getChildElements(startNode);
		for (int j = 0; j < c.getLength(); j++) {
			if (!found) {
				if (!(dom.hasQName(c.item(j), "a:documentation"))) {
					if (dom.hasQName(c.item(j), tag)) {
						found = true;
						return (Element) c.item(j);
					}
					foundEle = locateNode((Element) c.item(j), tag, foundEle,
							found);
					if (found)
						return foundEle;
				}
			}
		}

		return foundEle;

	}

	public String locateValue(Element element, String tag, String label) {

		NodeList c = dom.getChildElements(element);

		for (int j = 0; j < c.getLength(); j++) {
			if (!(dom.hasQName(c.item(j), "a:documentation"))) {
				if (dom.hasQName(c.item(j), tag)) {
					label = c.item(j).getTextContent();
					return label;
				}
				label = locateValue((Element) c.item(j), tag, label);
				if (!label.equals(""))
					return label;
			}
		}
		return label;
	}

	public Node locateSelectedChoice(Node n, Node retNode) {
		NodeList nodes = dom.getChildElements(n);
		for (int i = 0; i < nodes.getLength(); i++) {
			Node oneNode = nodes.item(i);
			if (!oneNode.getNamespaceURI().equals(rngaURI)
					&& !oneNode.getNodeName().equals("rng:optional")
					&& !oneNode.getNamespaceURI().equals(xngURI)) {
				String selected = dom.getAttributeValue((Element) oneNode,
						"@xng:selected");
				if (dom.hasQName(oneNode, "rng:choice")
						|| (dom.hasQName(oneNode, "rng:group"))) {
					retNode = locateSelectedChoice(oneNode, retNode);
					if (retNode != null)
						return retNode;
				} else if (selected != null
						&& selected.equalsIgnoreCase("TRUE")) {
					return oneNode;
				}
			}
		}
		return retNode;
	}

	/**
	 * Decides what image to display in the tree cell depending on the cell
	 * object and column index
	 */
	public Image getColumnImage(Object object, int columnIndex) {

		if (object instanceof Element) {
			Element element = (Element) object;

			if (columnIndex == 0) { // name
				String type = element.getLocalName();

				if (type.equals("SensorML") || type.equals("System")
						|| type.equals("Component"))
					return gearImg;

				else if (type.equals("ProcessChain"))
					return chainImg;

				else if (type.equals("ProcessModel"))
					return procImg;

				else if (type.equals("DataGroup") || type.equals("Vector")
						|| type.equals("DataArray"))
					return folderImg;

				else if (type.equals("KeywordList")
						|| type.equals("IdentifierList")
						|| type.equals("ClassifierList")
						|| type.equals("DataRecord")
						|| type.equals("DocumentList")
						|| type.equals("ContactList")
						|| type.equals("ResponsibleParty")
						|| type.equals("Document")
						|| type.equals("TimeInstant")
						|| type.equals("TimePeriod"))
					return infoImg;

				else if (type.equals("InputList"))
					return inImg;
				else if (type.equals("OutputList"))
					return outImg;
				else if (type.equals("ParameterList"))
					return paramImg;
				else
					return itemImg;
			}
		}
		return null;
	}

	/**
	 * Decides what foreground color to use for the tree cell depending on the
	 * cell object and column index
	 */
	public Color getForeground(Object element, int columnIndex) {

		return null;
	}

	/**
	 * Decides what background color to use for the tree cell depending on the
	 * cell object and column index
	 */
	public Color getBackground(Object element, int columnIndex) {

		return null;

	}

	/**
	 * Decides what font to use for the tree cell depending on the cell object
	 * and column index
	 */
	public Font getFont(Object element, int columnIndex) {

		return fontNormal;
	}

	/**
	 * Make sure we dispose all OS graphic object that we created.
	 */


	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void addListener(ILabelProviderListener listener) {
	}

	public void removeListener(ILabelProviderListener listener) {
	}

	public Image getImage(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getText(Object element) {
		return this.getColumnText(element, 0);
	}
}