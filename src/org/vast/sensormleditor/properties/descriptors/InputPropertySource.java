package org.vast.sensormleditor.properties.descriptors;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.properties.labelproviders.AttributeLabelProvider;
import org.vast.sensormleditor.properties.labelproviders.PropertyLabelProvider;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class InputPropertySource extends AbstractPropertySource {

	public static final String PROPERTY_INPUT_NAME = "SensorMLEditor.input.name";
	private TextPropertyDescriptor nameDescriptor;

	public InputPropertySource(Element element, DOMHelper domHelper,
			TreeViewer viewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, viewer, smlEditor);
		initProperties();
	}

	public void initProperties() {
		// move onto meaningful children
		recursiveInitProperties((Node) ele);
	}

	public void recursiveInitProperties(Node node) {
		NodeList children = dom.getChildElements(node);
		for (int j = 0; j < children.getLength(); j++) {
			if (dom.hasQName(children.item(j), "rng:attribute")) {
				if (dom.existAttribute((Element) children.item(j), "name"))
					handleNameAttribute(children.item(j));
				recursiveInitProperties(children.item(j));
			} else if (dom.hasQName(children.item(j), "a:documentation")) {
				continue;

			} else {
				recursiveInitProperties(children.item(j));
			}
		}
	}

	public Object getPropertyValue(Object id) {

		if (id == PROPERTY_INPUT_NAME) {
			String displayName = (String) nameDescriptor.getDisplayName();
			String label = nameDescriptor.getLabelProvider().getText(
					displayName);
			return label;
		}
		return "";
	}

	public void handleNameAttribute(Node node) {
		nameDescriptor = new TextPropertyDescriptor(PROPERTY_INPUT_NAME,
				"name");
		AttributeLabelProvider plp = new AttributeLabelProvider(dom, node);
		nameDescriptor.setLabelProvider(plp);
	}

	public void setPropertyValue(Object id, Object value) {
		String str = (String) value;
		if (id.equals(PROPERTY_INPUT_NAME)) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) nameDescriptor
					.getLabelProvider();
			Node changeNode = propProvider.getNode();
			dom.setElementValue((Element)changeNode, str);
			this.firePropertyChange("INSERT", null,null);
		}
		
	}
}