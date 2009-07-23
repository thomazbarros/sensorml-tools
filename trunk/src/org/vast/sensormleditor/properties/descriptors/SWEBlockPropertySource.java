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

public class SWEBlockPropertySource extends AbstractPropertySource
		implements IPropertySource {

	public static final String PROPERTY_REF = "SensorMLEditor.ref";
	public static final String PROPERTY_BYTE_LENGTH = "SensorMLEditor.byte.length";
	public static final String PROPERTY_PADDING_BEFORE = "SensorMLEditor.padding.before";
	public static final String PROPERTY_PADDING_AFTER = "SensorMLEditor.padding.after";
	public static final String PROPERTY_ENCRYPTION = "SensorMLEditor.encryption";
	public static final String PROPERTY_COMPRESSION = "SensorMLEditor.compression";

	protected static final String[] properties = new String[20];
	protected IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[20];
	protected int propertyCount;

	private PropertyDescriptor refDescriptor;
	private PropertyDescriptor byteLengthDescriptor;
	private PropertyDescriptor padBeforeDescriptor;
	private PropertyDescriptor padAfterDescriptor;
	private PropertyDescriptor encryptionDescriptor;
	private PropertyDescriptor compressionDescriptor;

	private DOMHelperAddOn domUtil;

	protected boolean stop = false;
	protected Node currentSelectedNode = null;

	public SWEBlockPropertySource(Element element, DOMHelper domHelper,
			TreeViewer viewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, viewer, smlEditor);
		domUtil = new DOMHelperAddOn(dom);
		initProperties();
	}

	public void initProperties() {
		Element retNode = null;
		Node sweBlock = null;
		if (ele.getNodeName().equals("Block")) {
			sweBlock = ele;
		} else {
			sweBlock = domUtil.findNode(ele, "Block", retNode);
		}

		NodeList children = dom.getChildElements(sweBlock);
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
				if (attributeName.equals("compression")) {
					compressionDescriptor = new TextPropertyDescriptor(
							PROPERTY_COMPRESSION, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					compressionDescriptor.setLabelProvider(plp);
				} else if (attributeName.equals("byteLength")) {
					byteLengthDescriptor = new TextPropertyDescriptor(
							PROPERTY_BYTE_LENGTH, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					byteLengthDescriptor.setLabelProvider(plp);
				} else if (attributeName.equals("paddingBytes-before")) {
					padAfterDescriptor = new TextPropertyDescriptor(
							PROPERTY_PADDING_AFTER, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					padAfterDescriptor.setLabelProvider(plp);
				} else if (attributeName.equals("paddingBytes-after")) {
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
		if ((id == PROPERTY_REF) && (refDescriptor != null)) {
			propProvider = (PropertyLabelProvider) refDescriptor
					.getLabelProvider();
			
		} else if ((id == PROPERTY_PADDING_AFTER)
				&& (padAfterDescriptor != null)) {
			propProvider = (PropertyLabelProvider) padAfterDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_BYTE_LENGTH) && (byteLengthDescriptor != null)) {
			propProvider = (PropertyLabelProvider) byteLengthDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_COMPRESSION) && (compressionDescriptor != null)) {
			propProvider = (PropertyLabelProvider) compressionDescriptor
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

		} else {
			return null;
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

		} else if ((id == PROPERTY_PADDING_AFTER)
				&& (padBeforeDescriptor != null)) {
			propProvider = (PropertyLabelProvider) padAfterDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_BYTE_LENGTH) && (byteLengthDescriptor != null)) {
			propProvider = (PropertyLabelProvider) byteLengthDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_COMPRESSION) && (compressionDescriptor != null)) {
			propProvider = (PropertyLabelProvider) compressionDescriptor
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
