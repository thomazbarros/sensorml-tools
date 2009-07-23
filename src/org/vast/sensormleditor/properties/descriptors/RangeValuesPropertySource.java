package org.vast.sensormleditor.properties.descriptors;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.properties.labelproviders.PropertyLabelProvider;
import org.vast.sensormleditor.relaxNG.Relax2Hybrid;
import org.vast.xml.DOMHelper;
import org.vast.xml.transform.DOMTransformException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RangeValuesPropertySource extends AbstractPropertySource
		implements IPropertySource {
	public static final String PROPERTY_MIN_VALUE = "SensorMLEditor.min.value";
	public static final String PROPERTY_MAX_VALUE = "SensorMLEditor.max.value";
	public TextPropertyDescriptor minDescriptor;
	public TextPropertyDescriptor maxDescriptor;

	public String min;
	public String max;

	public RangeValuesPropertySource(Element element, DOMHelper domHelper,
			TreeViewer viewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, viewer, smlEditor);

		initProperties();
	}

	public void initProperties() {

		// move onto meaningful children
		recursiveInitProperties(ele);
	}

	public void recursiveInitProperties(Node node) {
		NodeList children = dom.getChildElements(node);
		for (int j = 0; j < children.getLength(); j++) {
			if (!children.item(j).getNamespaceURI().equals(rngaURI)) {
				if (dom.hasQName(children.item(j), "rng:ref")) {
					if (isQRValue(children.item(j))) {
						Relax2Hybrid transform1 = new Relax2Hybrid();
						try {
							transform1.transform(srcDoc, dom, children.item(j));
							// now resolve the rng:ref
							transform1.retrieveRelaxNGRef((Element) children
									.item(j));
						} catch (DOMTransformException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else
						continue;
					recursiveInitProperties(node);

				} else if (dom.hasQName(children.item(j), "swe:value")) {

					minDescriptor = new TextPropertyDescriptor(
							PROPERTY_MIN_VALUE, "name");
					PropertyLabelProvider minPLP = new PropertyLabelProvider(
							node, dom);
					minDescriptor.setLabelProvider(minPLP);
					// minDescriptor.setCategory(node.getLocalName());

					maxDescriptor = new TextPropertyDescriptor(
							PROPERTY_MAX_VALUE, "name");
					PropertyLabelProvider maxPLP = new PropertyLabelProvider(
							node, dom);
					maxDescriptor.setLabelProvider(maxPLP);
					// maxDescriptor.setCategory(node.getLocalName());

				} else {
					recursiveInitProperties(children.item(j));
				}
			}
		}
	}

	@Override
	public Object getPropertyValue(Object id) {
		String label = null;
		String[] values = new String[2];

		if ((id == PROPERTY_MIN_VALUE) && (minDescriptor != null)) {
			String displayName = (String) minDescriptor.getDisplayName();
			label = minDescriptor.getLabelProvider().getText(displayName);
			if (label != "" && label != null) {
				values = label.split(" ");
				return values[0];
			}
		} else if ((id == PROPERTY_MAX_VALUE) && (maxDescriptor != null)) {
			String displayName = (String) maxDescriptor.getDisplayName();
			label = maxDescriptor.getLabelProvider().getText(displayName);
			if (label != "" && label != null) {
				values = label.split(" ");
				if (values.length > 1)
					return values[1];
			}

		}
		return "";
	}

	public boolean isQRValue(Node node) {
		String refName = dom.getAttributeValue((Element) node, "name");
		if (refName.equals("swe.quantityRangeValue")
				|| (refName.equals("swe.countRangeValue") || (refName
						.equals("swe.timeRangeValue"))))
			return true;
		return false;
		//return refName.equals("swe.quantityRangeValue");
	}

	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
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

	public void setPropertyValue(Object id, Object value) {
		// 0String[] values = new String[2];
		String str = (String) value;
		if ((id == PROPERTY_MIN_VALUE) && (minDescriptor != null)) {
			min = str;
		} else if ((id == PROPERTY_MAX_VALUE) && (maxDescriptor != null)) {
			max = str;
		}

		if (min != null && max != null) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) minDescriptor
					.getLabelProvider();
			Node changeNode = propProvider.getNode();
			Element name = dom.getElement((Element) changeNode, "swe:value");
			if (str != null && !str.equals("")) {
				if (!dom.existAttribute(name, "rng:data/rng:value/@selected")) {
					dom.setAttributeValue((Element) name,
							"rng:data/rng:value/@selected", "true");
				}
				str = min + " " + max;
				dom.setElementValue((Element) name, "rng:data/rng:value", str
						.toString());
				((Element) changeNode).setAttribute("xng:selected", "true");

			} else {

				Element valueNode = dom.getElement((Element) changeNode,
						"swe:value/rng:data/rng:value");
				((Element) changeNode).removeAttribute("xng:selected");
				valueNode.setTextContent("");
				valueNode.removeAttribute("selected");

			}
			this.firePropertyChange("INSERT", null, null);

		}

	}
}
