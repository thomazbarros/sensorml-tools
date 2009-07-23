package org.vast.sensormleditor.editors;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.vast.sensormleditor.actions.DOMChoiceAction;
import org.vast.sensormleditor.actions.DOMInsertAction;
import org.vast.sensormleditor.config.Configuration;
import org.vast.sensormleditor.relaxNG.Relax2Hybrid;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.vast.xml.transform.DOMTransformException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SMLPopupMenuProvider {

	protected DOMHelper dom, srcDoc;
	protected String coreSchema;
	protected IPropertyChangeListener actionListener;
	protected String xngURI = "http://xng.org/1.0";
	protected String rngURI = "http://relaxng.org/ns/structure/1.0";
	protected String rngaURI = "http://relaxng.org/ns/compatibility/annotations/1.0";
	protected String smlURI = "http://www.opengis.net/sensorML/1.0.1";
	protected String sweURI = "http://www.opengis.net/swe/1.0.1";
	protected String gmlURI = "http://www.opengis.net/gml";
	protected DOMHelperAddOn domUtil;
	protected Element selectedElement;

	public SMLPopupMenuProvider(DOMHelper dom,
			IPropertyChangeListener propertyListener) {
		coreSchema = Configuration.getSchema();
		try {
			srcDoc = new DOMHelper(coreSchema, false);

		} catch (DOMHelperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.dom = dom;
		this.actionListener = propertyListener;
	}

	public void setSelectedElement(Element selected) {
		this.selectedElement = selected;
	}

	public void fillContextMenuHelper(Element ele, MenuManager mainMenu) {

		//These first elements will never have children in the tree view
		if (selectedElement.getLocalName().equals("quality") ||
			selectedElement.getLocalName().equals("keywords") ||
			selectedElement.getLocalName().equals("identifier") ||
			selectedElement.getLocalName().equals("classifier" ) ||
			selectedElement.getLocalName().equals("method") ||
			selectedElement.getLocalName().equals("constraint") ||
			selectedElement.getLocalName().equals("securityConstraint")) return;
		
		NodeList nodes = dom.getChildElements((Node) ele);
		for (int i = 0; i < nodes.getLength(); i++) {
			Element node = (Element) nodes.item(i);

			if (!node.getNamespaceURI().equals(rngaURI)
					&& !node.getNamespaceURI().equals(xngURI)) {

				if (node.getLocalName().equals("identifier")
						|| node.getLocalName().equals("method") 
						|| node.getLocalName().equals("connection") 
						|| node.getLocalName().equals("securityConstraint") 
						|| node.getLocalName().equals("classifier")) {
					createInsertAction(node, mainMenu);
					break;

				} else if (node.getNodeName().equals("rng:choice")){ 
					if (node.getParentNode().getLocalName().equals("uom") ||
						(node.getParentNode().getLocalName().equals("value") ||
						(node.getParentNode().getLocalName().equals("list")))) {
						continue;
					}
					this.handleChoiceMenu(node, mainMenu);

				} else if (node.getNodeName().equals("rng:oneOrMore")) {
					fillContextMenuHelper(node, mainMenu);

				} else if (node.getNodeName().equals("rng:zeroOrMore")) {
					fillContextMenuHelper(node, mainMenu);

				} else if (node.getNodeName().equals("rng:optional")) {
					if (!dom.existAttribute(node,"selected")) {
						// (node.getPreviousSibling() != null){
							if ( (node.getPreviousSibling()!= null) && (!node.getPreviousSibling().getNodeName().equals("swe:encoding"))){ 
								fillContextMenuHelper(node, mainMenu);
							} else
								continue;
					
					} else
						continue;
					
				} else if (node.getNodeName().equals("rng:ref")) {
					Relax2Hybrid transform1 = new Relax2Hybrid();
					Node parentNode = node.getParentNode();
					try {
						transform1.transform(srcDoc, dom, node);
						// now resolve the rng:ref
						transform1.retrieveRelaxNGRef((Element) node);
					} catch (DOMTransformException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					fillContextMenuHelper((Element) parentNode, mainMenu);

				} else if (node.getNodeName().equals("sml:connections")) {
					if (ele.getNodeName().equals("sml:ProcessChain"))
						break;
					else 
						fillContextMenuHelper(node, mainMenu);
					
				} else if (node.getNodeName().equals("sml:ConnectionList")){
					if (selectedElement.getNodeName().equals("sml:connections"))
						fillContextMenuHelper(node,mainMenu);
					else
						createInsertAction(node, mainMenu);
					
				} else if (node.getNodeName().equals("swe:data")){
					Element returnEle = null;
					domUtil = new DOMHelperAddOn(dom);
					Node chosen = domUtil.getChosenField(node, returnEle);
					if (node.getParentNode().getNodeName().equals("swe:ConditionalValue") &&
							chosen !=null)
						break;
					else
						fillContextMenuHelper(node,mainMenu);

				} else if (!Character
						.isUpperCase(node.getLocalName().charAt(0))) {
					fillContextMenuHelper(node, mainMenu);
				} else {
					createInsertAction(node, mainMenu);
				}
			}
		}
		return;
	}

	public void createInsertAction(Node node, MenuManager mainMenu) {
		IAction insertAction = new DOMInsertAction(dom, (Element) node);
		insertAction.addPropertyChangeListener(actionListener);
		insertAction.setText(node.getLocalName());
		mainMenu.add(insertAction);
	}

	public void handleChoiceMenu(Element node, MenuManager menu) {

		Node choiceNode = node.getParentNode();
		MenuManager catMenu = new MenuManager(choiceNode.getLocalName());
		menu.add(catMenu);
		getValidChoices(node, menu, catMenu, choiceNode);
	}

	public void getValidChoices(Node node, MenuManager mainMenu,
			MenuManager choiceMenu, Node choiceNode) {
		IAction choiceAction = null;

		NodeList children = dom.getChildElements(node);
		for (int k = 0; k < children.getLength(); k++) {
			Element ele1 = (Element) children.item(k);

			if (!ele1.getNamespaceURI().equals(rngaURI)
					&& !ele1.getNodeName().equals("rng:optional")
					&& !ele1.getNamespaceURI().equals(xngURI)) {

				if (ele1.getNodeName().equals("rng:ref")) {
					String name = dom.getAttributeValue(ele1, "name");
					int index = name.indexOf(".");
					if (dom.getAttributeValue(ele1, "name").contains("Any") || //(dom.getAttributeValue(ele1,"name").equals("sml.ProcessGroup")) ||
							(dom.getAttributeValue(ele1,"name").contains("Group"))) {
						Relax2Hybrid transform1 = new Relax2Hybrid();
						try {
							transform1.transform(srcDoc, dom, ele1);
							// now resolve the rng:ref
							transform1.retrieveRelaxNGRef(ele1);
						} catch (DOMTransformException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						choiceMenu.removeAll();
						getValidChoices(node, mainMenu, choiceMenu, choiceNode);
						break;
					} else if (Character.isUpperCase(name.charAt(index + 1))
							|| (name.equals("xlink.href"))) {

						choiceAction = new DOMChoiceAction(dom, ele1,
								selectedElement, choiceNode);
						choiceAction.addPropertyChangeListener(actionListener);
						choiceAction.setText(name);
						choiceMenu.add(choiceAction);
					}  
					getValidChoices(ele1, mainMenu, choiceMenu, choiceNode);
				} else if (ele1.getNodeName().equals("rng:attribute")
						&& (dom.getAttributeValue(ele1, "name").equals(
								"indeterminatePosition") || dom
								.getAttributeValue(ele1, "name").equals(
										"xlink:href") || dom.getAttributeValue(ele1,"name").equals(
												"ref"))) {
					choiceAction = new DOMChoiceAction(dom, ele1,
							selectedElement, choiceNode);
					choiceAction.addPropertyChangeListener(actionListener);
					String name = dom.getAttributeValue(ele1, "name");
					choiceAction.setText(name);
					choiceMenu.add(choiceAction);

				} else if (ele1.getNodeName().equals("rng:data")
						&& (dom.getAttributeValue(ele1, "type").equals(
								"dateTime") || (dom.getAttributeValue(ele1,
								"type").equals("time")))) {
					choiceAction = new DOMChoiceAction(dom, ele1,
							selectedElement, choiceNode);
					choiceAction.addPropertyChangeListener(actionListener);
					choiceAction.setText(dom.getAttributeValue(ele1, "type"));
					choiceMenu.add(choiceAction);

				/*} else if (ele1.getNodeName().equals("swe:values")) {
					choiceAction = new DOMChoiceAction(dom, ele1,
							selectedElement, choiceNode);
					choiceAction.addPropertyChangeListener(actionListener);
					choiceAction.setText(ele1.getLocalName());
					choiceMenu.add(choiceAction);*/
					
				} else if (Character.isUpperCase(ele1.getLocalName().charAt(0))) {
					choiceAction = new DOMChoiceAction(dom, ele1,
							selectedElement, choiceNode);
					choiceAction.addPropertyChangeListener(actionListener);
					choiceAction.setText(ele1.getLocalName());
					choiceMenu.add(choiceAction);

				} else {
					getValidChoices(ele1, mainMenu, choiceMenu, choiceNode);
				}
			}
		}
	}
}
