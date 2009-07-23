package org.vast.sensormleditor.properties.descriptors;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.properties.labelproviders.PropertyLabelProvider;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class InputListPropertySource extends AbstractPropertySource {

	public static final String PROPERTY_SWE_FIELD = "SensorMLEditor.swe.field";
	public TextPropertyDescriptor sweFieldDescriptor;
	protected static final String[] properties = new String[20];
	protected IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[20];
	protected int propertyCount;
	private PropertyDescriptor descriptor;
	private DOMHelperAddOn domUtil;
	protected String title;

	public InputListPropertySource(Element element, DOMHelper domHelper,
			TreeViewer viewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, viewer, smlEditor);
		domUtil = new DOMHelperAddOn(dom);
		initProperties();
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return this.title;
	}

	public PropertyLabelProvider getProvider() {
		PropertyLabelProvider propProvider = (PropertyLabelProvider) sweFieldDescriptor
				.getLabelProvider();
		return propProvider;
	}

	public String findTitle(Element element) {
		Node node = element.getParentNode();
		if (node.getPrefix().equals("rng")) {
			findTitle((Element) node);
		} else {
			title = node.getLocalName();
			return title;
		}

		return title;
	}

	public void initProperties() {
		propertyCount = 0;
		setTitle(findTitle(ele));
		// move onto meaningful children
		recursiveInitProperties((Node) ele);
	}

	public void recursiveInitProperties(Node node) {
		NodeList children = dom.getChildElements(node);
		for (int j = 0; j < children.getLength(); j++) {
			if (dom.hasQName(children.item(j), "rng:oneOrMore")) {
				handleoneOrMore(children.item(j));
			} else {
				recursiveInitProperties(children.item(j));
			}
		}
	}

	public Object getPropertyValue(Object id) {

		if ((id == PROPERTY_SWE_FIELD) && (sweFieldDescriptor != null)) {
			return "";
		} else {
			for (int i = 0; i < propertyDescriptors.length; i++) {
				if (propertyDescriptors[i] != null
						&& id.equals(propertyDescriptors[i].getId())) {
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

	public void handleoneOrMore(Node node) {
		NodeList c = dom.getChildElements(node);
		for (int j = 0; j < c.getLength(); j++) {

			if (dom.hasQName(c.item(j), "a:documentation")) {
				continue;
			} else if (dom.hasQName(c.item(j), "sml:input")) {
				handleData(c.item(j));
			} else if (dom.hasQName(c.item(j), "xng:item")) {
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
			if (dom.hasQName(c.item(j), "rng:attribute")) {
				Element ele = dom.getElement((Element) node,
						"rng:attribute/rng:data");
				sweFieldDescriptor = new TextPropertyDescriptor(
						PROPERTY_SWE_FIELD, c.item(j).getLocalName());
				PropertyLabelProvider plp = new PropertyLabelProvider(ele, dom);
				sweFieldDescriptor.setLabelProvider(plp);
			}
		}
	}

	public void setPropertyValue(Object id, Object value) {
		String str = (String) value;
		if (str != null && !str.equals("")) {
			if ((id == PROPERTY_SWE_FIELD) && (sweFieldDescriptor != null)) {
				PropertyLabelProvider propProvider = (PropertyLabelProvider) sweFieldDescriptor
						.getLabelProvider();
				
				Element oneOrMore = dom.getElement(ele, "rng:oneOrMore");
				Element field = dom.getElement(ele, "rng:oneOrMore/sml:input");
				if (oneOrMore != null) {
					Node newxng = domUtil.createXNGItem(oneOrMore);
					Node fieldchild = dom.getDocument().importNode(field, true);
					newxng.appendChild(fieldchild);
					propProvider.setNode((Element) fieldchild);
					Element oneNode = dom.getElement((Element) fieldchild,
							"rng:attribute/rng:data");

					dom.setElementValue((Element) oneNode, "rng:value", value
							.toString());
					String newID = domUtil.findUniqueID();
					oneNode.removeAttribute("id");
					dom.setAttributeValue((Element) oneNode, "id", newID);
					oneNode.removeAttribute("rng:value/@selected");
					dom.setAttributeValue((Element) oneNode,
							"rng:value/@selected", "true");
				}
				
				this.firePropertyChange("INSERT", null,null);
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
}
