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

public class VectorPropertySource extends AbstractPropertySource implements
		IPropertySource {

	public static final String PROPERTY_SWE_COORDINATE = "SensorMLEditor.swe.coordinate";
	public TextPropertyDescriptor sweCoordDescriptor;
	protected static final String[] properties = new String[20];
	protected IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[20];
	protected int propertyCount;
	private PropertyDescriptor descriptor;
	private DOMHelperAddOn domUtil;
	protected String title;

	public VectorPropertySource(Element element, DOMHelper domHelper,
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
		PropertyLabelProvider propProvider = (PropertyLabelProvider) sweCoordDescriptor
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

		if ((id == PROPERTY_SWE_COORDINATE) && (sweCoordDescriptor != null)) {
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
			} else if (dom.hasQName(c.item(j), "xng:item")) {
				properties[propertyCount] = "SensorML.Property" + propertyCount;
				descriptor = new TextPropertyDescriptor(
						properties[propertyCount], c.item(j).getLocalName());
				PropertyLabelProvider plp = new PropertyLabelProvider(
						c.item(j), dom);
				descriptor.setLabelProvider(plp);
				descriptor.setCategory(node.getLocalName());
				propertyDescriptors[propertyCount++] = descriptor;

			} else if (dom.hasQName(c.item(j), "swe:coordinate")) {
				handleData(c.item(j));
			}
		}
	}

	public void handleData(Node node) {
		NodeList c = dom.getChildElements(node);
		for (int j = 0; j < c.getLength(); j++) {
			if (dom.hasQName(c.item(j), "rng:attribute")) {
				Element ele = dom.getElement((Element) node,
						"rng:attribute/rng:data");
				sweCoordDescriptor = new TextPropertyDescriptor(
						PROPERTY_SWE_COORDINATE, c.item(j).getLocalName());
				PropertyLabelProvider plp = new PropertyLabelProvider(ele, dom);
				sweCoordDescriptor.setLabelProvider(plp);
			}
		}
	}

	public void removeField(Object id, Object value) {
		String str = (String) value;
		if (str != null && !str.equals("")) {
			for (int i = 0; i < propertyDescriptors.length; i++) {

				PropertyLabelProvider propProvider = (PropertyLabelProvider) propertyDescriptors[i]
						.getLabelProvider();

				Node changeNode = propProvider.getNode();
				Node parentNode = changeNode.getParentNode();
				parentNode.removeChild(changeNode);
			}
		}
	}

	public void setPropertyValue(Object id, Object value) {
		
		String str = (String) value;
		if (str != null && !str.equals("")) {
			if ((id == PROPERTY_SWE_COORDINATE) && (sweCoordDescriptor != null)) {
				
				Element oneOrMore = dom.getElement((Element)ele,"rng:oneOrMore");
				Node xngNode = domUtil.createXNGItem(oneOrMore);
				
				Node copyNode = dom.getElement((Element)oneOrMore,"swe:coordinate");
				Node nNode = dom.getDocument().importNode(copyNode, true);
				xngNode.appendChild(nNode);
				oneOrMore.appendChild(xngNode);
				dom.setElementValue((Element) nNode, "rng:attribute/rng:data/rng:value",str);
				
				dom.setAttributeValue((Element) nNode, "rng:attribute/rng:data/rng:value/@selected",
						"true");
				
				PropertyLabelProvider propProvider = (PropertyLabelProvider) sweCoordDescriptor
						.getLabelProvider();
				propProvider.setNode((Element) xngNode);

				this.firePropertyChange("INSERT", null, null);

			} else {

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
