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

public class KeywordListPropertySource extends AbstractPropertySource implements
		IPropertySource {

	public static final String PROPERTY_KEYWORD = "SensorMLEditor.keywordlist.keyword";
	protected static final String[] properties = new String[20];
	protected IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[20];
	protected int propertyCount;
	private PropertyDescriptor descriptor;

	public TextPropertyDescriptor keywordDescriptor;
	private DOMHelperAddOn domUtil;
	private Element xngItem;

	public KeywordListPropertySource(Element element, DOMHelper domHelper,
			TreeViewer viewer, SMLTreeEditor smlEditor, Element xngItem) {
		super(element, domHelper, viewer, smlEditor);
		this.xngItem = xngItem;
		domUtil = new DOMHelperAddOn(dom);
		initProperties();
	}

	public void initProperties() {
		// Gather attributes first
		propertyCount = 0;

		// move onto meaningful children
		if (ele.hasChildNodes()) {
			NodeList nodes = dom.getChildElements((Node) ele);
			if (nodes != null) {
				for (int i = 0; i < nodes.getLength(); i++) {
					if (dom.hasQName(nodes.item(i), "rng:oneOrMore"))
						handleoneOrMore(nodes.item(i));
				}
			}
		}
	}

	public Object getPropertyValue(Object id) {

		if (id == PROPERTY_KEYWORD)
			return "";
		else {
			for (int i = 0; i < propertyDescriptors.length; i++) {
				if (id.equals(propertyDescriptors[i].getId())) {
					String displayName = (String) propertyDescriptors[i]
							.getDisplayName();
					String label = propertyDescriptors[i].getLabelProvider()
							.getText(displayName);
					return label;
				}
			}
		}
		return "";

	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		String str = (String) value;

		if (id.equals(PROPERTY_KEYWORD)) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) keywordDescriptor
					.getLabelProvider();

			Element oneOrMoreNode = dom.getElement(xngItem,"sml:keywords/rng:choice/sml:KeywordList/rng:oneOrMore/");
			
			Node xngNode = domUtil.createXNGItem(oneOrMoreNode);
			Node copyNode = dom.getElement((Element)oneOrMoreNode,"sml:keyword");
			Node nNode = dom.getDocument().importNode(copyNode, true);
			xngNode.appendChild(nNode);
			oneOrMoreNode.appendChild(xngNode);
			propProvider.setNode((Element) xngNode);
			Element newNode = dom.getElement((Element) xngNode,
					"sml:keyword/rng:data");
			dom.setElementValue((Element) newNode, "rng:value", value
					.toString());
			dom.setElementValue((Element) newNode, "rng:value", value
					.toString());
			if (!dom.existAttribute(newNode, "rng:value/@selected")) {
				dom.setAttributeValue((Element) newNode,
						"rng:value/@selected", "true");
			}
			String newID = domUtil.findUniqueID();
			newNode.removeAttribute("id");
			dom.setAttributeValue((Element) newNode, "id", newID);
			this.firePropertyChange("INSERT", null,null);
	
		
		} else {
			for (int j = 0; j < propertyDescriptors.length; j++) {
				if (propertyDescriptors[j].getId().equals(id)) {
					PropertyLabelProvider propProvider = (PropertyLabelProvider) propertyDescriptors[j]
							.getLabelProvider();

					Node changeNode = propProvider.getNode();
					if (str.equals("")) {
						Node parentNode = changeNode.getParentNode();
						parentNode.removeChild(changeNode);
						break;
					}
				}
			}
			this.firePropertyChange("INSERT", null,null);
		}
		
	}

	public void handleoneOrMore(Node node) {
		NodeList c = dom.getChildElements(node);
		for (int j = 0; j < c.getLength(); j++) {

			if (dom.hasQName(c.item(j), "a:documentation")) {
				continue;
			} else if (dom.hasQName(c.item(j), "sml:keyword")) {
				handleData(c.item(j));
			} else {
				properties[propertyCount] = "SensorML.Property" + propertyCount;
				descriptor = new TextPropertyDescriptor(
						properties[propertyCount], c.item(j).getLocalName());
				PropertyLabelProvider plp = new PropertyLabelProvider(
						c.item(j), dom);
				descriptor.setLabelProvider(plp);
				descriptor.setCategory(node.getLocalName());
				propertyDescriptors[propertyCount++] = descriptor;
			}
		}
	}

	public void handleData(Node node) {
		NodeList c = dom.getChildElements(node);
		for (int j = 0; j < c.getLength(); j++) {
			if (dom.hasQName(c.item(j), "rng:data")) {
				keywordDescriptor = new TextPropertyDescriptor(
						PROPERTY_KEYWORD, c.item(j).getLocalName());
				PropertyLabelProvider plp = new PropertyLabelProvider(
						c.item(j), dom);
				keywordDescriptor.setLabelProvider(plp);
			}
		}
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] retArray;
		if (propertyDescriptors == null || propertyDescriptors.length == 0)
			initProperties();

		retArray = new IPropertyDescriptor[propertyCount];
		System.arraycopy(propertyDescriptors, 0, retArray, 0, propertyCount);
		return retArray;

	}

	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
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

}
