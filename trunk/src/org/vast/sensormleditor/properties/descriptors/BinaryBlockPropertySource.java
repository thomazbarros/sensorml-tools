package org.vast.sensormleditor.properties.descriptors;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.properties.labelproviders.ChoiceBoxLabelProvider;
import org.vast.sensormleditor.properties.labelproviders.PropertyLabelProvider;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BinaryBlockPropertySource extends AbstractPropertySource implements
		IPropertySource {
	public static final String PROPERTY_ENCODING_CHOICE = "SensorMLEditor.byte.encoding.choice";
	public static final String PROPERTY_ORDER_CHOICE = "SensorMLEditor.byte.order.choice";
	public static final String PROPERTY_BYTE_LENGTH = "SensorMLEditor.byte.length";

	protected static final String[] properties = new String[20];
	protected IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[20];
	protected int propertyCount;
	private PropertyDescriptor lengthDescriptor;

	private DOMHelperAddOn domUtil;
	public SensorMLToolsComboPropertyDescriptor encodingChoiceDescriptor;
	public String[] encodingChoiceLabels;
	protected int encodingLabelCount;
	public SensorMLToolsComboPropertyDescriptor orderChoiceDescriptor;
	public String[] orderChoiceLabels;
	protected int orderLabelCount;
	protected boolean stop = false;
	protected Node orderSelectedNode = null;
	protected Node encodingSelectedNode = null;

	public BinaryBlockPropertySource(Element element, DOMHelper domHelper,
			TreeViewer viewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, viewer, smlEditor);
		domUtil = new DOMHelperAddOn(dom);
		encodingLabelCount = 0;
		encodingChoiceLabels = new String[20];
		orderLabelCount = 0;
		orderChoiceLabels = new String[20];
		initProperties();
	}

	public void initProperties() {
		Element retNode = null;
		Node binaryBlock = null;
		if (ele.getNodeName().equals("swe:BinaryBlock")) {
			binaryBlock = ele;
		} else {
			binaryBlock = domUtil.findNode(ele, "BinaryBlock", retNode);
		}

		NodeList children = dom.getChildElements(binaryBlock);
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeName().equals("rng:attribute")) {
				if (dom.existElement((Element) child, "rng:choice")) {
					Node choiceNode = dom.getElement((Element) child,
							"rng:choice");
					if (dom.getAttributeValue((Element) child, "name").equals(
							"byteEncoding"))
						handleEncodingChoice(choiceNode);
					else if (dom.getAttributeValue((Element) child, "name")
							.equals("byteOrder"))
						handleOrderChoice(choiceNode);
				}
			} else if (child.getNodeName().equals("rng:optional")) {
				handleOptionalAttributes(child);
			}
		}
	}

	public void handleOptionalAttributes(Node node) {
		NodeList children = dom.getChildElements(node);
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeName().equals("rng:attribute")) {
				String attributeName = dom.getAttributeValue((Element) child,
						"name");
				if (attributeName.equals("byteLength")) {
					lengthDescriptor = new TextPropertyDescriptor(
							PROPERTY_BYTE_LENGTH, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					lengthDescriptor.setLabelProvider(plp);
				}

			}
		}
	}

	public void getEncodingValidChoices(Node node) {
		NodeList ns = dom.getChildElements(node);
		for (int i = 0; i < ns.getLength(); i++) {
			if (dom.hasQName(ns.item(i), "rng:value")) {

				encodingChoiceLabels[encodingLabelCount] = new String();
				encodingChoiceLabels[encodingLabelCount++] = ns.item(i)
						.getTextContent();
				String selected = dom.getAttributeValue((Element) ns.item(i),
						"xng:selected");
				if (selected != null && selected.equalsIgnoreCase("TRUE")) {
					encodingSelectedNode = ns.item(i);
				}
			}

		}
	}

	public void getOrderValidChoices(Node node) {
		NodeList ns = dom.getChildElements(node);
		for (int i = 0; i < ns.getLength(); i++) {
			if (dom.hasQName(ns.item(i), "rng:value")) {

				orderChoiceLabels[orderLabelCount] = new String();
				orderChoiceLabels[encodingLabelCount++] = ns.item(i)
						.getTextContent();
				String selected = dom.getAttributeValue((Element) ns.item(i),
						"xng:selected");
				if (selected != null && selected.equalsIgnoreCase("TRUE")) {
					orderSelectedNode = ns.item(i);
				}
			}

		}
	}

	public void handleEncodingChoice(Node node) {

		getEncodingValidChoices(node);
		String[] retArray = new String[encodingLabelCount];
		System.arraycopy(encodingChoiceLabels, 0, retArray, 0,
				encodingLabelCount);

		encodingChoiceDescriptor = new SensorMLToolsComboPropertyDescriptor(
				PROPERTY_ENCODING_CHOICE, node.getParentNode().getLocalName(),
				retArray);
		ChoiceBoxLabelProvider prov = new ChoiceBoxLabelProvider(retArray,
				node, dom);
		prov.setSelectedNode(encodingSelectedNode);
		encodingChoiceDescriptor.setLabelProvider(prov);
		encodingChoiceDescriptor.setCategory(node.getLocalName());

		stop = true;
		return;
	}

	public void handleOrderChoice(Node node) {

		getOrderValidChoices(node);
		String[] retArray = new String[orderLabelCount];
		System.arraycopy(orderChoiceLabels, 0, retArray, 0, orderLabelCount);

		orderChoiceDescriptor = new SensorMLToolsComboPropertyDescriptor(
				PROPERTY_ORDER_CHOICE, node.getParentNode().getLocalName(),
				retArray);
		ChoiceBoxLabelProvider prov = new ChoiceBoxLabelProvider(retArray,
				node, dom);
		prov.setSelectedNode(orderSelectedNode);
		orderChoiceDescriptor.setLabelProvider(prov);
		orderChoiceDescriptor.setCategory(node.getLocalName());

		stop = true;
		return;
	}

	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getPropertyValue(Object id) {
		String attValue = "";
		PropertyLabelProvider propProvider = null;

		if ((id == PROPERTY_ENCODING_CHOICE)
				&& (encodingChoiceDescriptor != null)) {
			ChoiceBoxLabelProvider choiceProvider = (ChoiceBoxLabelProvider) encodingChoiceDescriptor
					.getLabelProvider();
			Node selected = (Node) choiceProvider.getSelectedNode();
			if (selected != null)
				attValue = selected.getTextContent();
			return attValue;

		} else if ((id == PROPERTY_ORDER_CHOICE)
				&& (orderChoiceDescriptor != null)) {
			ChoiceBoxLabelProvider choiceProvider = (ChoiceBoxLabelProvider) orderChoiceDescriptor
					.getLabelProvider();
			Node selected = (Node) choiceProvider.getSelectedNode();
			if (selected != null)
				attValue = selected.getTextContent();
			return attValue;

		} else if ((id == PROPERTY_BYTE_LENGTH) && (lengthDescriptor != null)) {
			propProvider = (PropertyLabelProvider) lengthDescriptor
					.getLabelProvider();

		}

		Node changeNode = propProvider.getNode();
		Element valueElement = dom.getElement((Element) changeNode,
				"rng:data/rng:value");
		if (valueElement != null)
			attValue = valueElement.getTextContent();
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

	@Override
	public void setPropertyValue(Object id, Object value) {

		String str = (String) value;
		PropertyLabelProvider propProvider = null;

		if ((id == PROPERTY_ENCODING_CHOICE)
				&& (encodingChoiceDescriptor != null)) {
			ChoiceBoxLabelProvider choiceProvider = (ChoiceBoxLabelProvider) encodingChoiceDescriptor
					.getLabelProvider();
			Node choice = choiceProvider.getNode();
			NodeList cList = dom.getChildElements(choice);
			for (int i = 0; i < cList.getLength(); i++) {
				if (dom.hasQName(cList.item(i), "rng:value")) {
					String name = cList.item(i).getTextContent();
					if (name.equals(str)) {
						dom.setAttributeValue((Element) cList.item(i),
								"xng:selected", "true");
						encodingSelectedNode = cList.item(i);
						choiceProvider.setSelectedNode(encodingSelectedNode);
					} else {
						((Element) cList.item(i))
								.removeAttribute("xng:selected");
					}
				}
			}
			this.firePropertyChange("INSERT", null, null);
			return;

		} else if ((id == PROPERTY_ORDER_CHOICE
				&& orderChoiceDescriptor != null)) {
			ChoiceBoxLabelProvider choiceProvider = (ChoiceBoxLabelProvider) orderChoiceDescriptor
					.getLabelProvider();
			Node choice = choiceProvider.getNode();
			NodeList cList = dom.getChildElements(choice);
			for (int i = 0; i < cList.getLength(); i++) {
				if (dom.hasQName(cList.item(i), "rng:value")) {
					String name = cList.item(i).getTextContent();
					if (name.equals(str)) {
						dom.setAttributeValue((Element) cList.item(i),
								"xng:selected", "true");
						orderSelectedNode = cList.item(i);
						choiceProvider.setSelectedNode(orderSelectedNode);
					} else {
						((Element) cList.item(i))
								.removeAttribute("xng:selected");
					}
				}
			}
			this.firePropertyChange("INSERT", null, null);
			return;

		} else if ((id == PROPERTY_BYTE_LENGTH) && (lengthDescriptor != null)) {
			propProvider = (PropertyLabelProvider) lengthDescriptor
					.getLabelProvider();

		}

		if (propProvider != null) {
			Node changeNode = propProvider.getNode();
			if (changeNode.getParentNode().getNodeName().equals("rng:optional")) {
				((Element) changeNode.getParentNode()).setAttribute(
						"xng:selected", "true");
			}
			Node dataNode = dom.getElement((Element) changeNode, "rng:data");
			dom.setElementValue((Element) dataNode, "rng:value", str);
			if (!dom.existAttribute((Element) dataNode, "rng:value/@selected")) {
				dom.setAttributeValue((Element) dataNode,
						"rng:value/@selected", "true");
			}
			if (!dom.existAttribute((Element) dataNode, "id")) {
				String newID = domUtil.findUniqueID();
				dom.setAttributeValue((Element) dataNode, "id", newID);
			}
			this.firePropertyChange("INSERT", null, null);
		}

	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] retArray;
		if (propertyDescriptors == null || propertyDescriptors.length == 0)
			initProperties();

		retArray = new IPropertyDescriptor[propertyCount];
		System.arraycopy(propertyDescriptors, 0, retArray, 0, propertyCount);
		return retArray;

	}

	public String[] getEncodingChoices(Object id) {

		if (encodingChoiceLabels == null)
			initProperties();
		return encodingChoiceLabels;
	}

	public String[] getOrderChoices(Object id) {

		if (orderChoiceLabels == null)
			initProperties();
		return orderChoiceLabels;
	}

}
