package org.vast.sensormleditor.properties.descriptors;

import java.io.IOException;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.properties.labelproviders.ChoiceBoxLabelProvider;
import org.vast.sensormleditor.properties.labelproviders.PropertyLabelProvider;
import org.vast.sensormleditor.relaxNG.Relax2Hybrid;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.vast.xml.transform.DOMTransformException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DataArrayElementCountPropertySource extends AbstractPropertySource
		implements IPropertySource {

	public static final String PROPERTY_ELEMENT_COUNT = "SensorMLEditor.element.count";
	public static final String PROPERTY_COUNT_CHOICE = "SensorMLEditor.count.choice";
	public PropertyDescriptor elementCountDescriptor;
	public SensorMLToolsComboPropertyDescriptor choiceDescriptor;
	public String[] choiceLabels;
	protected int labelCount;
	protected DOMHelperAddOn domUtil;

	protected boolean stop = false;
	protected Node currentSelectedNode = null;

	public DataArrayElementCountPropertySource(Element element,
			DOMHelper domHelper, TreeViewer viewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, viewer, smlEditor);
		domUtil = new DOMHelperAddOn(dom);
		labelCount = 0;
		choiceLabels = new String[20];
		initProperties();
	}
	
	public void initProperties() {
		Element retNode = null;
		Node elementCount = null;
		
		if (ele.getLocalName().equals("elementCount"))
			elementCount = ele;
		else
			elementCount = domUtil.findNode(ele, "elementCount", retNode);

		if (elementCount != null) {
			Element choice = dom.getElement((Element) elementCount, "rng:choice");
			if (choice!=null)
				handleChoice(choice);
			
		}
	}

/*	public void initProperties() {
		this.handleUOM(ele);
		recursiveInitProperties(ele);
	}*/

	public void recursiveInitProperties(Node node) {

		NodeList children = dom.getChildElements(node);
		for (int j = 0; j < children.getLength(); j++) {
			if (!children.item(j).getNamespaceURI().equals(rngaURI)) {
				if (dom.hasQName(children.item(j), "rng:ref")) {
					if (isElementCount(children.item(j))) {
						Relax2Hybrid transform1 = new Relax2Hybrid();
						try {
							transform1.transform(srcDoc, dom, children.item(j));
							// now resolve the rng:ref
							transform1.retrieveRelaxNGRef((Element) children
									.item(j));
						} catch (DOMTransformException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						recursiveInitProperties(node);
						if (stop)
							return;
					}
					continue;

				} else if (dom.hasQName(children.item(j), "rng:choice")
						&& dom.hasQName(children.item(j).getParentNode(),
								"swe:elementCount")) {
					if (dom.existElement((Element) children.item(j), "rng:ref")) {
						NodeList ns = dom.getChildElements(children.item(j));
						for (int i = 0; i < ns.getLength(); i++) {
							if (dom.hasQName(ns.item(i), "rng:ref")) {
								if (isIDREF(ns.item(i))
										|| (isSWECount(ns.item(i)))) {
									Relax2Hybrid transform1 = new Relax2Hybrid();
									try {
										transform1.transform(srcDoc, dom, ns
												.item(i));
										// now resolve the rng:ref
										transform1
												.retrieveRelaxNGRef((Element) ns
														.item(i));
									} catch (DOMTransformException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								}
							}
						}
					}
					handleChoice(children.item(j));
					if (stop)
						return;

				} else {
					recursiveInitProperties(children.item(j));
					if (stop)
						return;
				}
			}
		}
	}

	public String[] getChoices(Object id) {
		/*
		 * if ((id == PROPERTY_UOM_CHOICE) && (choiceDescriptor != null)) {
		 * ChoiceBoxLabelProvider cProv = (ChoiceBoxLabelProvider)
		 * choiceDescriptor.getLabelProvider(); return cProv.getValues(); }
		 * return null;
		 */
		if (choiceLabels == null)
			initProperties();
		return choiceLabels;
	}

	public void getValidChoices(Node node) {
		NodeList ns = dom.getChildElements(node);
		for (int i = 0; i < ns.getLength(); i++) {
			if (dom.hasQName(ns.item(i), "rng:attribute")) {

				choiceLabels[labelCount] = new String();
				choiceLabels[labelCount++] = dom.getAttributeValue((Element) ns
						.item(i), "name");
				String selected = dom.getAttributeValue((Element) ns.item(i),
						"xng:selected");
				if (selected != null && selected.equalsIgnoreCase("TRUE")) {
					currentSelectedNode = ns.item(i);
				}
			} else if (dom.hasQName(ns.item(i), "swe:Count")) {
				choiceLabels[labelCount] = new String();
				choiceLabels[labelCount++] = ns.item(i).getLocalName();
				String selected = dom.getAttributeValue((Element) ns.item(i),
						"xng:selected");
				if (selected != null && selected.equalsIgnoreCase("TRUE")) {
					currentSelectedNode = ns.item(i);
				}

			}

		}
	}

	public void handleChoice(Node node) {

		getValidChoices(node);
		String[] retArray = new String[labelCount];
		System.arraycopy(choiceLabels, 0, retArray, 0, labelCount);

		choiceDescriptor = new SensorMLToolsComboPropertyDescriptor(
				PROPERTY_COUNT_CHOICE, node.getParentNode().getLocalName(),
				retArray);
		ChoiceBoxLabelProvider prov = new ChoiceBoxLabelProvider(retArray,
				node, dom);
		prov.setSelectedNode(currentSelectedNode);
		choiceDescriptor.setLabelProvider(prov);
		choiceDescriptor.setCategory(node.getLocalName());

		stop = true;
		return;
	}

	public boolean isElementCount(Node node) {
		String refName = dom.getAttributeValue((Element) node, "name");
		return refName.equals("swe.elementCount");
	}

	public boolean isSWECount(Node node) {
		String refName = dom.getAttributeValue((Element) node, "name");
		return refName.equals("swe.Count");
	}

	public boolean isIDREF(Node node) {
		String refName = dom.getAttributeValue((Element) node, "name");
		return refName.equals("ref");
	}

/*	public void handleUOM(Node node) {
		elementCountDescriptor = new PropertyDescriptor(PROPERTY_ELEMENT_COUNT,
				"COUNT");
		PropertyLabelProvider plp = new PropertyLabelProvider(node
				.getParentNode(), dom);
		elementCountDescriptor.setLabelProvider(plp);

	}*/

	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;

	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getPropertyValue(Object id) {
		String attValue = "";
		if (choiceDescriptor != null) {
			ChoiceBoxLabelProvider prov = (ChoiceBoxLabelProvider) choiceDescriptor
					.getLabelProvider();

			Node selected = (Node) prov.getSelectedNode();
			if (selected != null) {
				if ((id == PROPERTY_ELEMENT_COUNT)
						&& (elementCountDescriptor != null)) {
					attValue = dom.getElementValue((Element) selected,
							"rng:data/rng:value");
				} else if ((id == PROPERTY_COUNT_CHOICE)
						&& (choiceDescriptor != null)) {
					if (selected.equals("rng:attribute")){
					attValue = dom
							.getAttributeValue((Element) selected, "name");
					} else {
						return selected.getLocalName();
					}
				}
			}
		}
		return attValue;

	}

	@Override
	public boolean isPropertySet(Object id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {
		// TODO Auto-generated method stub

	}

/*	public void setOptional(Element element) {
		Node node = element.getParentNode();
		if (node.getNodeName().equals("rng:optional")) {
			dom.setAttributeValue((Element) node, "xng:selected", "true");
		} else {
			if (node != ele)
				setOptional((Element) node);
			else
				return;
		}
	}*/

	@Override
	public void setPropertyValue(Object id, Object value) {
		String str = (String) value;
		if ((id == PROPERTY_COUNT_CHOICE) && (choiceDescriptor != null)) {
			ChoiceBoxLabelProvider prov = (ChoiceBoxLabelProvider) choiceDescriptor
					.getLabelProvider();
			Node choice = prov.getNode();
			NodeList cList = dom.getChildElements(choice);
			for (int i = 0; i < cList.getLength(); i++) {
				if (dom.hasQName(cList.item(i), "rng:attribute")) {
					String name = dom.getAttributeValue(
							(Element) cList.item(i), "@name");
					if (name.equals(str)) {
						dom.setAttributeValue((Element) cList.item(i),
								"xng:selected", "true");
						Element dataNode = dom.getElement(
								(Element) cList.item(i), "rng:data");
						if (elementCountDescriptor != null) {
							PropertyLabelProvider propProvider = (PropertyLabelProvider) elementCountDescriptor
									.getLabelProvider();
							propProvider.setNode(dataNode);
						}
						currentSelectedNode = cList.item(i);
						prov.setSelectedNode(currentSelectedNode);
					} else {
						((Element) cList.item(i))
								.removeAttribute("xng:selected");
					}
					this.firePropertyChange("INSERT", null, null);
				} else if (dom.hasQName(cList.item(i), "swe:Count")){
					if (str.equals("Count")){
						dom.setAttributeValue((Element) cList.item(i),
							"xng:selected", "true");
						if (elementCountDescriptor != null) {
							PropertyLabelProvider propProvider = (PropertyLabelProvider) elementCountDescriptor
									.getLabelProvider();
							propProvider.setNode((Element) cList.item(i));
						}
						currentSelectedNode = cList.item(i);
						prov.setSelectedNode(currentSelectedNode);
					} else {
						((Element) cList.item(i))
								.removeAttribute("xng:selected");
					}
				}

			}
		} else if ((id == PROPERTY_ELEMENT_COUNT)
				&& (elementCountDescriptor != null)) {
			if (choiceDescriptor != null) {
				ChoiceBoxLabelProvider propProvider = (ChoiceBoxLabelProvider) choiceDescriptor
						.getLabelProvider();
				Node selected = (Node) propProvider.getSelectedNode();
				dom.setElementValue((Element) selected, "rng:data/rng:value",
						value.toString());

				((Element) selected)
						.removeAttribute("rng:data/rng:value/@selected");
				dom.setAttributeValue((Element) selected,
						"rng:data/rng:value/@selected", "true");
				this.firePropertyChange("INSERT", null, null);
			}
		}
	

		

	}

}
