package org.vast.sensormleditor.properties.descriptors;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.properties.labelproviders.PropertyLabelProvider;
import org.vast.sensormleditor.relaxNG.Relax2Hybrid;
import org.vast.xml.DOMHelper;
import org.vast.xml.transform.DOMTransformException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SWEreferenceTimePropertySource extends AbstractPropertySource
		implements IPropertySource {

	public static final String PROPERTY_REFERENCE_TIME = "SensorMLEditor.reference.time";
	public PropertyDescriptor referenceTimeDescriptor;

	boolean stop = false;

	public SWEreferenceTimePropertySource(Element element,
			DOMHelper domHelper, TreeViewer viewer, SMLTreeEditor smlEditor) {
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
					if (isreferenceTime(children.item(j))) {
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
					if (stop)
						return;

				} else if (dom.hasQName(children.item(j), "rng:attribute")) {
					if (dom.getAttributeValue((Element) children.item(j),
							"name").equals("referenceTime")) {
						referenceTimeDescriptor = new TextPropertyDescriptor(
								PROPERTY_REFERENCE_TIME, "name");
						PropertyLabelProvider plp = new PropertyLabelProvider(
								node, dom);
						referenceTimeDescriptor.setLabelProvider(plp);
						stop = true;

					}
					break;

				} else {
					recursiveInitProperties(children.item(j));
					if (stop)
						return;
				}
			}
		}
	}

	public boolean isreferenceTime(Node node) {
		String refName = dom.getAttributeValue((Element) node, "name");
		return refName.equals("swe.referenceTime");
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
	public Object getPropertyValue(Object id) {
		String label = null;
		if ((id == PROPERTY_REFERENCE_TIME) && (referenceTimeDescriptor != null)) {

			PropertyLabelProvider prop = (PropertyLabelProvider) referenceTimeDescriptor
					.getLabelProvider();
			Node term = prop.getNode();
			Node data = dom
					.getElement((Element) term, "rng:attribute/rng:data");
			if (dom.existElement((Element) data, "rng:value"))
				label = dom.getElementValue((Element) data, "rng:value");
			else
				label = "";
			return label;
		}

		return "";
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

		if ((id == PROPERTY_REFERENCE_TIME) && (referenceTimeDescriptor != null)) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) referenceTimeDescriptor
					.getLabelProvider();
			Node changeNode = propProvider.getNode();
			if (str != null && !str.equals("")) {
				if (!dom.existAttribute((Element) changeNode,
						"rng:attribute/rng:data/rng:value/@selected")) {
					dom.setAttributeValue((Element) changeNode,
							"rng:attribute/rng:data/rng:value/@selected",
							"true");
				}
				dom.setElementValue((Element) changeNode,
						"rng:attribute/rng:data/rng:value", value.toString());
				((Element) changeNode).setAttribute("xng:selected", "true");

			} else {

				Element valueNode = dom.getElement((Element) changeNode,
						"rng:attribute/rng:data/rng:value");
				((Element) changeNode).removeAttribute("xng:selected");
				valueNode.setTextContent("");
				valueNode.removeAttribute("selected");

			}
			this.firePropertyChange("INSERT", null, null);
		}
		
	}

}
