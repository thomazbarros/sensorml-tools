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

public class TextBlockPropertySource extends AbstractPropertySource
		implements IPropertySource {
	
	public static final String PROPERTY_TOKEN_SEPARATOR = "SensorMLEditor.token.separator";
	public static final String PROPERTY_BLOCK_SEPARATOR = "SensorMLEditor.block.separator";
	public static final String PROPERTY_DECIMAL_SEPARATOR = "SensorMLEditor.decimal.separator";

	protected static final String[] properties = new String[20];
	protected IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[20];
	protected int propertyCount;
	
	private PropertyDescriptor tokenDescriptor;
	private PropertyDescriptor blockDescriptor;
	private PropertyDescriptor decimalDescriptor;
	private DOMHelperAddOn domUtil;
	protected boolean stop = false;
	protected Node currentSelectedNode = null;

	public TextBlockPropertySource(Element element,
			DOMHelper domHelper, TreeViewer viewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, viewer, smlEditor);
		domUtil = new DOMHelperAddOn(dom);
		initProperties();
	}

	public void initProperties() {
		Element retNode = null;
		Node textBlock = null;
		if (ele.getNodeName().equals("swe:TextBlock")){
			textBlock = ele;
		} else {
			textBlock = domUtil.findNode(ele, "TextBlock", retNode);
		}

		NodeList children = dom.getChildElements(textBlock);
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeName().equals("rng:attribute")) {
				String attributeName = dom.getAttributeValue((Element) child,"name");
				if (attributeName.equals("tokenSeparator")) {
					tokenDescriptor = new TextPropertyDescriptor(
							PROPERTY_TOKEN_SEPARATOR, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					tokenDescriptor.setLabelProvider(plp);
				} else if (attributeName.equals("blockSeparator")) {
					blockDescriptor = new TextPropertyDescriptor(
							PROPERTY_BLOCK_SEPARATOR, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					blockDescriptor.setLabelProvider(plp);
				} else if (attributeName.equals("decimalSeparator")) {
					decimalDescriptor = new TextPropertyDescriptor(
							PROPERTY_DECIMAL_SEPARATOR, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					decimalDescriptor.setLabelProvider(plp);
				}
			}
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

	if (id == PROPERTY_TOKEN_SEPARATOR
				&& (tokenDescriptor != null)) {
			propProvider = (PropertyLabelProvider) tokenDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_BLOCK_SEPARATOR
				&& (blockDescriptor != null)) {
			propProvider = (PropertyLabelProvider) blockDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_DECIMAL_SEPARATOR
				&& (decimalDescriptor != null)) {
			propProvider = (PropertyLabelProvider) decimalDescriptor
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

		if (id == PROPERTY_TOKEN_SEPARATOR
				&& (tokenDescriptor != null)) {
			propProvider = (PropertyLabelProvider) tokenDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_BLOCK_SEPARATOR
				&& (blockDescriptor != null)) {
			propProvider = (PropertyLabelProvider) blockDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_DECIMAL_SEPARATOR
				&& (decimalDescriptor != null)) {
			propProvider = (PropertyLabelProvider) decimalDescriptor
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

}
