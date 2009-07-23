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

public class SWEComponentPropertySource extends AbstractPropertySource
		implements IPropertySource {

	public static final String PROPERTY_REF = "SensorMLEditor.ref";
	public static final String PROPERTY_DATA_TYPE = "SensorMLEditor.data.type";
	public static final String PROPERTY_SIGNIFICANT_BITS = "SensorMLEditor.significant.bits";
	public static final String PROPERTY_BIT_LENGTH = "SensorMLEditor.bit.length";
	public static final String PROPERTY_PADDING_BEFORE = "SensorMLEditor.padding.before";
	public static final String PROPERTY_PADDING_AFTER = "SensorMLEditor.padding.after";
	public static final String PROPERTY_ENCRYPTION = "SensorMLEditor.encryption";

	protected static final String[] properties = new String[20];
	protected IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[20];
	protected int propertyCount;

	private PropertyDescriptor refDescriptor;
	private PropertyDescriptor dataTypeDescriptor;
	private PropertyDescriptor significantDescriptor;
	private PropertyDescriptor bitLengthDescriptor;
	private PropertyDescriptor padBeforeDescriptor;
	private PropertyDescriptor padAfterDescriptor;
	private PropertyDescriptor encryptionDescriptor;

	private DOMHelperAddOn domUtil;

	protected boolean stop = false;
	protected Node currentSelectedNode = null;

	public SWEComponentPropertySource(Element element, DOMHelper domHelper,
			TreeViewer viewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, viewer, smlEditor);
		domUtil = new DOMHelperAddOn(dom);
		initProperties();
	}

	public void initProperties() {
		Element retNode = null;
		Node sweComponent = null;
		if (ele.getNodeName().equals("Component")) {
			sweComponent = ele;
		} else {
			sweComponent = domUtil.findNode(ele, "Component", retNode);
		}

		NodeList children = dom.getChildElements(sweComponent);
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeName().equals("rng:attribute")) {
				String attributeName = dom.getAttributeValue((Element) child,
						"name");
				if (attributeName.equals("ref")) {
					refDescriptor = new TextPropertyDescriptor(PROPERTY_REF,
							"name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					refDescriptor.setLabelProvider(plp);
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
				if (attributeName.equals("dataType")) {
					dataTypeDescriptor = new TextPropertyDescriptor(
							PROPERTY_DATA_TYPE, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					dataTypeDescriptor.setLabelProvider(plp);
				} else if (attributeName.equals("significantBits")) {
					significantDescriptor = new TextPropertyDescriptor(
							PROPERTY_SIGNIFICANT_BITS, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					significantDescriptor.setLabelProvider(plp);
				} else if (attributeName.equals("bitLength")) {
					bitLengthDescriptor = new TextPropertyDescriptor(
							PROPERTY_BIT_LENGTH, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					bitLengthDescriptor.setLabelProvider(plp);
				} else if (attributeName.equals("paddingBits-before")) {
					padAfterDescriptor = new TextPropertyDescriptor(
							PROPERTY_PADDING_AFTER, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					padAfterDescriptor.setLabelProvider(plp);
				} else if (attributeName.equals("paddingBits-after")) {
					padBeforeDescriptor = new TextPropertyDescriptor(
							PROPERTY_PADDING_BEFORE, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					padBeforeDescriptor.setLabelProvider(plp);
				} else if (attributeName.equals("encryption")) {
					encryptionDescriptor = new TextPropertyDescriptor(
							PROPERTY_ENCRYPTION, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					encryptionDescriptor.setLabelProvider(plp);
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

		if ((id == PROPERTY_SIGNIFICANT_BITS)
				&& (significantDescriptor != null)) {
			propProvider = (PropertyLabelProvider) significantDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_PADDING_AFTER)
				&& (padAfterDescriptor != null)) {
			propProvider = (PropertyLabelProvider) padAfterDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_BIT_LENGTH) && (bitLengthDescriptor != null)) {
			propProvider = (PropertyLabelProvider) bitLengthDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_DATA_TYPE) && (dataTypeDescriptor != null)) {
			propProvider = (PropertyLabelProvider) dataTypeDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_REF) && (refDescriptor != null)) {
			propProvider = (PropertyLabelProvider) refDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_PADDING_BEFORE
				&& (padBeforeDescriptor != null)) {
			propProvider = (PropertyLabelProvider) padBeforeDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_ENCRYPTION && (encryptionDescriptor != null)) {
			propProvider = (PropertyLabelProvider) encryptionDescriptor
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

		if ((id == PROPERTY_REF) && (refDescriptor != null)) {
			propProvider = (PropertyLabelProvider) refDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_SIGNIFICANT_BITS)
				&& (significantDescriptor != null)) {
			propProvider = (PropertyLabelProvider) significantDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_PADDING_AFTER)
				&& (padBeforeDescriptor != null)) {
			propProvider = (PropertyLabelProvider) padAfterDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_BIT_LENGTH) && (bitLengthDescriptor != null)) {
			propProvider = (PropertyLabelProvider) bitLengthDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_DATA_TYPE) && (dataTypeDescriptor != null)) {
			propProvider = (PropertyLabelProvider) dataTypeDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_PADDING_BEFORE
				&& (padBeforeDescriptor != null)) {
			propProvider = (PropertyLabelProvider) padBeforeDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_ENCRYPTION && (encryptionDescriptor != null)) {
			propProvider = (PropertyLabelProvider) encryptionDescriptor
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
