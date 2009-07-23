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

public class SMLCapabilitiesPropertySource extends AbstractPropertySource
		implements IPropertySource {

	public static final String PROPERTY_SML_CAPABILITIES = "SensorMLEditor.sml.capabilities";
	public TextPropertyDescriptor smlCapabilitiesDescriptor;

	public SMLCapabilitiesPropertySource(Element element, DOMHelper domHelper,
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
			if (dom.hasQName(children.item(j), "rng:ref")) {
				// handleRNGRef(children.item(j));
				Relax2Hybrid transform1 = new Relax2Hybrid();
				try {
					transform1.transform(srcDoc, dom, children.item(j));
					// now resolve the rng:ref
					transform1.retrieveRelaxNGRef((Element) children.item(j));
				} catch (DOMTransformException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				recursiveInitProperties(children.item(j));
			} else if (dom.hasQName(children.item(j), "a:documentation")) {
				continue;

			} else {
				recursiveInitProperties(children.item(j));
			}
		}
	}

	public void handleRNGRef(Node node) {
		PropertyLabelProvider plp = new PropertyLabelProvider(node
				.getParentNode(), dom);
		// plp.setOptionalSelected(false);
		String refName = dom.getAttributeValue((Element) node, "name");
		if (refName.equals("sml.capabilities")) {
			smlCapabilitiesDescriptor = new TextPropertyDescriptor(
					PROPERTY_SML_CAPABILITIES, refName);
			smlCapabilitiesDescriptor.setLabelProvider(plp);
		}
	}

	public void handleSWECodespace(Node node) {
		Node n = node;
		smlCapabilitiesDescriptor = new TextPropertyDescriptor(
				PROPERTY_SML_CAPABILITIES, n.getNodeName());
		PropertyLabelProvider plp = new PropertyLabelProvider(
				n.getParentNode(), dom);
		// plp.setOptionalSelected(true);

		smlCapabilitiesDescriptor.setLabelProvider(plp);
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
		if ((id == PROPERTY_SML_CAPABILITIES)
				&& (smlCapabilitiesDescriptor != null)) {
			String displayName = (String) smlCapabilitiesDescriptor
					.getDisplayName();
			label = smlCapabilitiesDescriptor.getLabelProvider().getText(
					displayName);
			return label;
		}
		return "";

	}

	public void setPropertyValue(Object id, Object value) {
		String str = (String) value;
		if (str != null && !str.equals("")) {
			if ((id == PROPERTY_SML_CAPABILITIES)
					&& (smlCapabilitiesDescriptor != null)) {
				PropertyLabelProvider propProvider = (PropertyLabelProvider) smlCapabilitiesDescriptor
						.getLabelProvider();
				Node changeNode = propProvider.getNode();
				dom.setElementValue((Element) changeNode, str);
				this.firePropertyChange("INSERT", null, null);
			}
			
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
