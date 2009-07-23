package org.vast.sensormleditor.properties.descriptors;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.properties.labelproviders.AttributeLabelProvider;
import org.vast.sensormleditor.properties.labelproviders.PropertyLabelProvider;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IdentifierPropertySource extends AbstractPropertySource implements
		IPropertySource {

	public static final String PROPERTY_NAME = "SensorMLEditor.identifier.name";
	public TextPropertyDescriptor nameDescriptor;
	public String header;
	boolean gotName = false;

	public IdentifierPropertySource(Element element, DOMHelper domHelper,
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

	public void initProperties() {
		if (dom.hasQName(ele, "sml:identifier"))
			if (dom.existAttribute(ele, "name")) {
				handleName(ele);
				gotName = true;
			} else
				recursiveInitProperties(ele);
	}

	public void recursiveInitProperties(Node node) {

		NodeList children = dom.getChildElements(node);
		for (int j = 0; j < children.getLength(); j++) {

			if (dom.hasQName(children.item(j), "a:documentation")) {
				continue;
			} else if (dom.hasQName(children.item(j), "rng:attribute")) {

				if (dom.getAttributeValue((Element) children.item(j), "name")
						.equals("name")) {
					nameDescriptor = new TextPropertyDescriptor(PROPERTY_NAME,
							"name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							children.item(j), dom);
					nameDescriptor.setLabelProvider(plp);
					gotName = true;
					break;
				}

			} else {
				if (!gotName)
					recursiveInitProperties(children.item(j));
			}
		}
	}

	public void handleName(Node node) {
		nameDescriptor = new TextPropertyDescriptor(PROPERTY_NAME, "name");
		AttributeLabelProvider plp = new AttributeLabelProvider(dom, node);
		nameDescriptor.setLabelProvider(plp);
	}

	public Object getPropertyValue(Object id) {
		String label = null;
		if ((id == PROPERTY_NAME) && (nameDescriptor != null)) {
			String displayName = (String) nameDescriptor.getDisplayName();
			label = nameDescriptor.getLabelProvider().getText(displayName);
			return label;
		}
		return "";
	}

	public void setPropertyValue(Object id, Object value) {
		Node changeNode = null;
		if ((id == PROPERTY_NAME) && (nameDescriptor != null)) {
			ILabelProvider propProvider = (ILabelProvider) nameDescriptor
					.getLabelProvider();
			if (propProvider instanceof AttributeLabelProvider) {
				changeNode = ((AttributeLabelProvider) propProvider).getNode();
			} else if (propProvider instanceof PropertyLabelProvider) {
				changeNode = ((PropertyLabelProvider) propProvider).getNode();
			}
			dom.setElementValue((Element) changeNode, "rng:data/rng:value",
					value.toString());
			
		}
		
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

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		// TODO Auto-generated method stub
		return null;
	}

}
