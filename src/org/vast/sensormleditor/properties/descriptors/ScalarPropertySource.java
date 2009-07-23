package org.vast.sensormleditor.properties.descriptors;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.properties.labelproviders.PropertyLabelProvider;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* Scalar types include any Numerical Type (Quantity,Count,Time) 
 * or Text, Category or Boolean*/

public class ScalarPropertySource extends AbstractPropertySource implements
		IPropertySource {

	public static final String PROPERTY_VALUE = "SensorMLEditor.value";
	public TextPropertyDescriptor valueDescriptor;

	public String title;
	public String header;

	protected static final String[] properties = new String[20];
	protected IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[20];
	protected int propertyCount;

	protected boolean stop = false;

	public ScalarPropertySource(Element element, DOMHelper domHelper,
			TreeViewer viewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, viewer, smlEditor);

		initProperties();
	}

	public String getHeading() {
		return header;
	}

	public void setHeading(String header) {
		this.header = header;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String value) {
		this.title = value;
	}

	public void initProperties() {
		propertyCount = 0;
		// move onto meaningful children
		recursiveInitProperties(ele);
	}

	public void recursiveInitProperties(Node node) {

		NodeList children = dom.getChildElements(node);
		for (int j = 0; j < children.getLength(); j++) {
			if (!children.item(j).getNamespaceURI().equals(rngaURI)) {
				if (dom.hasQName(children.item(j), "rng:attribute")
						&& (dom.hasQName(children.item(j).getParentNode(),
								"swe:field")
								|| dom.hasQName(children.item(j)
										.getParentNode(), "sml:parameter")
								|| dom.hasQName(children.item(j)
										.getParentNode(), "sml:input") || dom
								.hasQName(children.item(j).getParentNode(),
										"sml:output"))) {

					handleValue(children.item(j));

				} else if (dom.hasQName(children.item(j), "swe:Category")
						|| dom.hasQName(children.item(j), "swe:Boolean")
						|| dom.hasQName(children.item(j), "swe:Text")) {
					if (dom.existAttribute((Element) node, "name")) {
						setHeading(dom
								.getAttributeValue((Element) node, "name"));
					}
					setTitle(children.item(j).getLocalName());
					recursiveInitProperties(children.item(j));
				} else {
					recursiveInitProperties(children.item(j));
				}
			}
		}
	}


	public void handleValue(Node node) {
		Node n = node;
		Element dataNode = dom.getElement((Element) n, "rng:data");
		Element valueNode = dom.getElement((Element) n, "rng:data/rng:value");
		String displayName = "";
		if (valueNode != null)
			displayName = valueNode.getTextContent();
		valueDescriptor = new TextPropertyDescriptor(PROPERTY_VALUE, displayName);
		PropertyLabelProvider plp = new PropertyLabelProvider(dataNode, dom);
		valueDescriptor.setLabelProvider(plp);
	}

	public Object getPropertyValue(Object id) {
		String label = null;
		if ((id == PROPERTY_VALUE) && (valueDescriptor != null)) {
			label = valueDescriptor.getLabelProvider().getText("");
			return label;
		}
		return "";

	}

	public void setPropertyValue(Object id, Object value) {
		String str = (String) value;

		if ((id == PROPERTY_VALUE) && (valueDescriptor != null)) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) valueDescriptor
					.getLabelProvider();
			Node changeNode = propProvider.getNode();
			if (!dom.existAttribute((Element) changeNode,
					"rng:value/@selected"))
				dom.setAttributeValue((Element) changeNode,
						"rng:value/@selected", "true");
			dom
					.setElementValue((Element) changeNode,
							"rng:value", str);
			this.firePropertyChange("INSERT", null, null);

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
