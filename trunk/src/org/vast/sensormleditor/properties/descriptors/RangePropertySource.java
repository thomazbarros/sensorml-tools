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

/* Range includes QuantityRange, CountRange, and TimeRange*/

public class RangePropertySource extends AbstractPropertySource implements
		IPropertySource {

	public static final String PROPERTY_VALUE = "SensorMLEditor.range.value";
	public TextPropertyDescriptor valueDescriptor;
	public String title;
	public String header;
	protected static final String[] properties = new String[20];
	protected IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[20];
	protected int propertyCount;

	/*
	 * public static final String PROPERTY_RANGE = "SensorMLEditor.range.value";
	 * protected static final String[] properties = new String[20]; protected
	 * IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[20];
	 * protected int propertyCount; private PropertyDescriptor descriptor;
	 * public String title; public String header; public TextPropertyDescriptor
	 * quantityRangeDescriptor; boolean stop = false;
	 */

	public RangePropertySource(Element element, DOMHelper domHelper,
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

				} else if (dom.hasQName(children.item(j), "swe:QuantityRange")
						|| dom.hasQName(children.item(j), "swe:CountRange")
						|| dom.hasQName(children.item(j), "swe:TimeRange")) {
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
		valueDescriptor = new TextPropertyDescriptor(PROPERTY_VALUE,
				displayName);
		PropertyLabelProvider plp = new PropertyLabelProvider(dataNode, dom);
		valueDescriptor.setLabelProvider(plp);
	}

	public Object getPropertyValue(Object id) {
		String attValue = "";
		if ((id == PROPERTY_VALUE) && (valueDescriptor != null)) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) valueDescriptor
					.getLabelProvider();
			Element dataNode = (Element) propProvider.getNode();
			attValue = dom.getElementValue((Element) dataNode, "rng:value");
			return attValue;
		}
		return attValue;
	}

	public void setPropertyValue(Object id, Object value) {
		String str = (String) value;
		Node changeNode = null;
		if ((id == PROPERTY_VALUE) && (valueDescriptor != null)) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) valueDescriptor
					.getLabelProvider();
			changeNode = propProvider.getNode();
			if (!dom
					.existAttribute((Element) changeNode, "rng:value/@selected"))
				dom.setAttributeValue((Element) changeNode,
						"rng:value/@selected", "true");
			dom.setElementValue((Element) changeNode, "rng:value", str);
			this.firePropertyChange("INSERT", null, null);
			
		}

	}

	/*
	 * public void recursiveInitProperties(Node node) { NodeList children =
	 * dom.getChildElements(node); for (int j = 0; j < children.getLength();
	 * j++) { if (!children.item(j).getNamespaceURI().equals(rngaURI)) { if
	 * (dom.hasQName(children.item(j), "rng:ref")) { if
	 * (isQuantityRange(children.item(j))) { Relax2Hybrid transform1 = new
	 * Relax2Hybrid(); try { transform1.transform(srcDoc, dom,
	 * children.item(j)); // now resolve the rng:ref
	 * transform1.retrieveRelaxNGRef((Element) children .item(j)); } catch
	 * (DOMTransformException e1) { // TODO Auto-generated catch block
	 * e1.printStackTrace(); } } else continue; recursiveInitProperties(node);
	 * 
	 * } else if (dom.hasQName(children.item(j), "swe:value")) {
	 * quantityRangeDescriptor = new TextPropertyDescriptor( PROPERTY_RANGE,
	 * "name");
	 * 
	 * PropertyLabelProvider plp = new PropertyLabelProvider(node, dom);
	 * quantityRangeDescriptor.setLabelProvider(plp);
	 * quantityRangeDescriptor.setCategory(node.getLocalName());
	 * 
	 * } else { recursiveInitProperties(children.item(j)); } } } }
	 */

	/*
	 * public boolean isQuantityRange(Node node) { String refName =
	 * dom.getAttributeValue((Element) node, "name"); return
	 * refName.equals("swe.quantityRangeValue"); }
	 * 
	 * 
	 * public void handleValueList(Node node) { NodeList children =
	 * dom.getChildElements(node); for (int i = 0; i < children.getLength();
	 * i++) { properties[propertyCount] = "SensorML.Property" + propertyCount;
	 * descriptor = new TextPropertyDescriptor(properties[propertyCount],
	 * children.item(i).getLocalName()); PropertyLabelProvider plp = new
	 * PropertyLabelProvider(children .item(i), dom);
	 * descriptor.setLabelProvider(plp);
	 * descriptor.setCategory(node.getLocalName());
	 * propertyDescriptors[propertyCount++] = descriptor; } }
	 * 
	 * @Override public Object getPropertyValue(Object id) { String label =
	 * null; if ((id == PROPERTY_RANGE) && (quantityRangeDescriptor != null)) {
	 * String displayName = (String) quantityRangeDescriptor.getDisplayName();
	 * label = quantityRangeDescriptor.getLabelProvider().getText(displayName);
	 * return label; } return ""; }
	 * 
	 * public void setPropertyValue(Object id, Object value) { String str =
	 * (String) value; if ((id == PROPERTY_RANGE) && (quantityRangeDescriptor !=
	 * null)) { PropertyLabelProvider propProvider = (PropertyLabelProvider)
	 * quantityRangeDescriptor .getLabelProvider(); Node changeNode =
	 * propProvider.getNode(); Element name = dom.getElement((Element)
	 * changeNode, "swe:value"); if (str != null && !str.equals("")) { if
	 * (!dom.existAttribute(name, "rng:value/@selected")) {
	 * dom.setAttributeValue((Element) name, "rng:data/rng:value/@selected",
	 * "true"); } dom.setElementValue((Element) name, "rng:data/rng:value",
	 * value .toString()); ((Element) changeNode).setAttribute("xng:selected",
	 * "true");
	 * 
	 * } else {
	 * 
	 * Element valueNode = dom.getElement((Element) changeNode,
	 * "swe:value/rng:data/rng:value"); ((Element)
	 * changeNode).removeAttribute("xng:selected");
	 * valueNode.setTextContent(""); valueNode.removeAttribute("selected");
	 * 
	 * }
	 * 
	 * } this.firePropertyChange("INSERT", null, null); }
	 */
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
