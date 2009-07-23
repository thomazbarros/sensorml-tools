package org.vast.sensormleditor.properties.descriptors;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.properties.labelproviders.PropertyLabelProvider;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AddressPropertySource extends AbstractPropertySource implements
		IPropertySource {
	public static final String PROPERTY_CITY = "SensorMLEditor.sml.address.city";
	public static final String PROPERTY_STATE = "SensorMLEditor.sml.address.state";
	public static final String PROPERTY_POSTAL = "SensorMLEditor.sml.address.postal";
	public static final String PROPERTY_EMAIL = "SensorMLEditor.sml.address.email";
	public static final String PROPERTY_COUNTRY = "SensorMLEditor.sml.address.country";
	public static final String PROPERTY_DELIVERY = "SensorMLEditor.sml.address.delivery";

	protected static final String[] properties = new String[20];
	protected IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[20];
	protected int propertyCount;
	private PropertyDescriptor descriptor;
	private PropertyDescriptor cityDescriptor;
	private PropertyDescriptor stateDescriptor;
	private PropertyDescriptor postalDescriptor;
	private PropertyDescriptor countryDescriptor;
	private PropertyDescriptor emailDescriptor;
	private PropertyDescriptor deliveryDescriptor;
	private DOMHelperAddOn domUtil;
	private Node address;

	public AddressPropertySource(Element element, DOMHelper domHelper,
			TreeViewer viewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, viewer, smlEditor);
		domUtil = new DOMHelperAddOn(dom);
		initProperties();
	}

	public void initProperties() {
		Element retNode = null;
		address = domUtil.findNode(ele, "address", retNode);
		
		Node delivery = domUtil.findNode(ele, "deliveryPoint", retNode);
		if (delivery != null) {
			if (dom.existElement((Element) delivery.getParentNode(), "xng:item")) {
				NodeList nodes = dom.getElements((Element) delivery
						.getParentNode(), "xng:item");
				for (int j = 0; j < nodes.getLength(); j++) {
					properties[propertyCount] = "SensorML.Property"
							+ propertyCount;
					descriptor = new TextPropertyDescriptor(
							properties[propertyCount], nodes.item(j)
									.getLocalName());
					PropertyLabelProvider plp = new PropertyLabelProvider(nodes
							.item(j), dom);
					descriptor.setLabelProvider(plp);
					propertyDescriptors[propertyCount++] = descriptor;
				}
			}
			deliveryDescriptor = new TextPropertyDescriptor(PROPERTY_DELIVERY, "name");
			PropertyLabelProvider plp = new PropertyLabelProvider(delivery, dom);
			deliveryDescriptor.setLabelProvider(plp);
		}

		
		Node city = domUtil.findNode(ele, "city", retNode);
		if (city != null) {
			cityDescriptor = new TextPropertyDescriptor(PROPERTY_CITY, "name");
			PropertyLabelProvider plp = new PropertyLabelProvider(city, dom);
			cityDescriptor.setLabelProvider(plp);
		}

		Node state = domUtil.findNode(ele, "administrativeArea", retNode);
		if (state != null) {
			stateDescriptor = new TextPropertyDescriptor(PROPERTY_STATE, "name");
			PropertyLabelProvider plp = new PropertyLabelProvider(state, dom);
			stateDescriptor.setLabelProvider(plp);
		}

		Node post = domUtil.findNode(ele, "postalCode", retNode);
		if (post != null) {
			postalDescriptor = new TextPropertyDescriptor(PROPERTY_POSTAL,
					"name");
			PropertyLabelProvider plp = new PropertyLabelProvider(post, dom);
			postalDescriptor.setLabelProvider(plp);
		}

		Node country = domUtil.findNode(ele, "country", retNode);
		if (country != null) {
			countryDescriptor = new TextPropertyDescriptor(PROPERTY_COUNTRY,
					"name");
			PropertyLabelProvider plp = new PropertyLabelProvider(country, dom);
			countryDescriptor.setLabelProvider(plp);
		}

		Node email = domUtil.findNode(ele, "electronicMailAddress", retNode);
		if (email != null) {
			emailDescriptor = new TextPropertyDescriptor(PROPERTY_EMAIL, "name");
			PropertyLabelProvider plp = new PropertyLabelProvider(email, dom);
			emailDescriptor.setLabelProvider(plp);
		}

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

		if ((id == PROPERTY_EMAIL) && (emailDescriptor != null)) {
			propProvider = (PropertyLabelProvider) emailDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_STATE) && (stateDescriptor != null)) {
			propProvider = (PropertyLabelProvider) stateDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_CITY) && (cityDescriptor != null)) {
			propProvider = (PropertyLabelProvider) cityDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_POSTAL) && (postalDescriptor != null)) {
			propProvider = (PropertyLabelProvider) postalDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_COUNTRY) && (countryDescriptor != null)) {
			propProvider = (PropertyLabelProvider) countryDescriptor
					.getLabelProvider();
		} else {
			for (int i = 0; i < propertyCount; i++) {
				if (propertyDescriptors[i] != null
						&& id.equals(propertyDescriptors[i].getId())) {
					propProvider = (PropertyLabelProvider) propertyDescriptors[i]
							.getLabelProvider();
					Node changeNode = propProvider.getNode();
					Element valueElement = dom.getElement((Element) changeNode,
							"sml:deliveryPoint/rng:data/rng:value");
					String text = valueElement.getTextContent();

					return text;

				}
			}
			return "";
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

	public void removeDeliveryProperty(Object id, Object value) {
		Integer newInt = new Integer((Integer) value);
		int index = newInt.intValue();
		if (propertyDescriptors[index] != null) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) propertyDescriptors[index]
					.getLabelProvider();
			Node changeNode = propProvider.getNode();
			Node parentNode = changeNode.getParentNode();
			parentNode.removeChild(changeNode);
			this.firePropertyChange("INSERT", null, null);
		}
	}

	@Override
	public void setPropertyValue(Object id, Object value) {

		String str = (String) value;
		PropertyLabelProvider propProvider = null;
		if ((id == PROPERTY_EMAIL) && (emailDescriptor != null)) {
			propProvider = (PropertyLabelProvider) emailDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_CITY) && (cityDescriptor != null)) {
			propProvider = (PropertyLabelProvider) cityDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_STATE) && (stateDescriptor != null)) {
			propProvider = (PropertyLabelProvider) stateDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_COUNTRY) && (countryDescriptor != null)) {
			propProvider = (PropertyLabelProvider) countryDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_POSTAL) && (postalDescriptor != null)) {
			propProvider = (PropertyLabelProvider) postalDescriptor
					.getLabelProvider();
			
		} else if ((id == PROPERTY_DELIVERY) && (deliveryDescriptor != null)) {
			propProvider = (PropertyLabelProvider) deliveryDescriptor
			.getLabelProvider();
		}

		if (propProvider != null) {
			Node changeNode = propProvider.getNode();
			if (changeNode.getParentNode().getNodeName().equals("rng:optional")) {
				((Element) changeNode.getParentNode()).setAttribute(
						"xng:selected", "true");
				Node dataNode = dom
						.getElement((Element) changeNode, "rng:data");
				dom.setElementValue((Element) dataNode, "rng:value", str);
				if (!dom.existAttribute((Element) dataNode, "rng:value/@selected")) {
					dom.setAttributeValue((Element) dataNode,
						"rng:value/@selected", "true");
				}
				if (!dom.existAttribute((Element) dataNode, "id")) {
					String newID = domUtil.findUniqueID();
					dom.setAttributeValue((Element) dataNode, "id", newID);
				}
				if (address != null) {
					if (address.getParentNode().getNodeName().equals(
						"rng:optional")) {
						if (!dom.existAttribute((Element) address
								.getParentNode(), "xng:selected")) {
							((Element) ((Element) address).getParentNode())
							.setAttribute("xng:selected", "true");
						}
					}
				}
				
			
			} else if (changeNode.getParentNode().getNodeName().equals("rng:zeroOrMore") || 
					(changeNode.getParentNode().getNodeName().equals("rng:oneOrMore"))) {
				Element oneOrMore = dom.getElement((Element) changeNode
						.getParentNode(), "");
				Node xngNode = domUtil.createXNGItem(oneOrMore);
				Node copyNode = dom.getElement((Element) oneOrMore, changeNode
						.getNodeName());
				Node nNode = dom.getDocument().importNode(copyNode, true);
				xngNode.appendChild(nNode);
				oneOrMore.appendChild(xngNode);
				Node dataNode = dom.getElement((Element) xngNode,
					"sml:deliveryPoint/rng:data");
				dom.setElementValue((Element) dataNode, "rng:value", str);
				if (!dom.existAttribute((Element) dataNode, "rng:value/@selected")) {
					dom.setAttributeValue((Element) dataNode,
						"rng:value/@selected", "true");
				}
				if (!dom.existAttribute((Element) dataNode, "id")) {
					String newID = domUtil.findUniqueID();
					dom.setAttributeValue((Element) dataNode, "id", newID);
				}
				if (address != null) {
					if (address.getParentNode().getNodeName().equals(
					"rng:optional")) {
						if (!dom
						.existAttribute(
								(Element) address.getParentNode(),
								"xng:selected")) {
					((Element) ((Element) address).getParentNode())
							.setAttribute("xng:selected", "true");
						}
					}
				}
				properties[propertyCount] = "SensorML.Property"+ propertyCount;
				descriptor = new TextPropertyDescriptor(
							properties[propertyCount], str);
				PropertyLabelProvider plp = new PropertyLabelProvider(xngNode, dom);
				descriptor.setLabelProvider(plp);
				propertyDescriptors[propertyCount++] = descriptor;
		
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

}
