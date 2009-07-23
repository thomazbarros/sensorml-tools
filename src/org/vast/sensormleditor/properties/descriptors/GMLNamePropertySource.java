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

public class GMLNamePropertySource extends AbstractPropertySource implements
		IPropertySource {

	public static final String PROPERTY_GML_NAME = "SensorMLEditor.gml.name";
	public TextPropertyDescriptor nameDescriptor;

	boolean stop = false;

	public GMLNamePropertySource(Element element, DOMHelper domHelper,
			TreeViewer viewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, viewer, smlEditor);
		initProperties();
	}

	public void initProperties() {

		recursiveInitProperties(ele);
	}

	public void recursiveInitProperties(Node node) {

		NodeList children = dom.getChildElements(node);
		for (int j = 0; j < children.getLength(); j++) {
			if (!children.item(j).getNamespaceURI().equals(rngaURI)) {
				if (dom.hasQName(children.item(j), "rng:ref")) {
					if (isGMLName(children.item(j))) {
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
					}
					else
						continue;
					recursiveInitProperties(node);
					if (stop)
						return;
				} else if (dom.hasQName(children.item(j), "gml:name")) {
					handleGMLName(children.item(j));
					break;

				} else {
					recursiveInitProperties(children.item(j));
					if (stop)
						return;
				}
			}
		}
	}

	public boolean isGMLName(Node node) {
		String refName = dom.getAttributeValue((Element) node, "name");
		return refName.equals("gml.name");
	}

	public void handleGMLName(Node node) {
		Node n = node;
		nameDescriptor = new TextPropertyDescriptor(PROPERTY_GML_NAME, n
				.getNodeName());
		PropertyLabelProvider plp = new PropertyLabelProvider(
				n.getParentNode(), dom);
		nameDescriptor.setLabelProvider(plp);
		stop = true;
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
		if ((id == PROPERTY_GML_NAME) && (nameDescriptor != null)) {
			PropertyLabelProvider prop = (PropertyLabelProvider) nameDescriptor
					.getLabelProvider();
			Node optional = prop.getNode();
			if (dom.existElement((Element) optional,
					"gml:name/rng:data/rng:value"))
				label = dom.getElementValue((Element) optional,
						"gml:name/rng:data/rng:value");
			else
				label = "";
			return label;
		}
		return label;
	}

	public void setPropertyValue(Object id, Object value) {
		String str = (String) value;
		
		if ((id == PROPERTY_GML_NAME) && (nameDescriptor != null)){
			PropertyLabelProvider propProvider = (PropertyLabelProvider) nameDescriptor
															.getLabelProvider();
			Node changeNode = propProvider.getNode();
			Element gmlName = dom.getElement((Element) changeNode,"gml:name");
			if (str != null && !str.equals("")) {
				if (!dom.existAttribute(gmlName, "rng:value/@selected")) {
					dom.setAttributeValue((Element) gmlName,"rng:data/rng:value/@selected", "true");
				}
				dom.setElementValue((Element) gmlName, "rng:data/rng:value",
						value.toString());
				((Element) changeNode).setAttribute("xng:selected", "true");
				
			} else {
			
				Element valueNode = dom.getElement((Element) changeNode, "gml:name/rng:data/rng:value");
				((Element) changeNode).removeAttribute("xng:selected");
				valueNode.setTextContent("");
				valueNode.removeAttribute("selected");
		
			}
			this.firePropertyChange("INSERT", null,null);
			
		}
		

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
