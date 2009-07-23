package org.vast.sensormleditor.properties.descriptors;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.properties.labelproviders.AttributeLabelProvider;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MaxPropertySource extends AbstractPropertySource implements
		IPropertySource {

	public static final String PROPERTY_MAX = "SensorMLEditor.max";
	public TextPropertyDescriptor maxDescriptor;

	public MaxPropertySource(Element element, DOMHelper domHelper,
			TreeViewer viewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, viewer, smlEditor);
		initProperties();
		// TODO Auto-generated constructor stub
	}

	public void initProperties() {

		// move onto meaningful children
		recursiveInitProperties(ele);
	}

	public void recursiveInitProperties(Node node) {

		NodeList children = dom.getChildElements(node);
		for (int j = 0; j < children.getLength(); j++) {
			if (dom.hasQName(children.item(j), "rng:param")) {
				handleParams(children.item(j));
			} else if (dom.hasQName(children.item(j), "a:documentation"))
				continue;
			else {
				recursiveInitProperties(children.item(j));
			}
		}
	}

	public void handleParams(Node node) {

		if (dom.getAttributeValue((Element) node, "name")
				.equals("maxExclusive")) {
			maxDescriptor = new TextPropertyDescriptor(PROPERTY_MAX,
					"maxExclusive");
			AttributeLabelProvider plp = new AttributeLabelProvider(dom, node);
			maxDescriptor.setLabelProvider(plp);
		}
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
		if ((id == PROPERTY_MAX) && (maxDescriptor != null)) {
			label = maxDescriptor.getLabelProvider().getText("");
			return label;
		}
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

	@Override
	public void setPropertyValue(Object id, Object value) {
		String str = (String) value;
		if ((id == PROPERTY_MAX) && (maxDescriptor != null)) {
			AttributeLabelProvider propProvider = (AttributeLabelProvider) maxDescriptor
					.getLabelProvider();

			Node changeNode = propProvider.getNode();
			changeNode.setTextContent(str);
			this.firePropertyChange("INSERT", null, null);

		}
	
	}

}
