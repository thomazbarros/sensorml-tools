package org.vast.sensormleditor.properties.descriptors;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.properties.labelproviders.AttributeLabelProvider;
import org.vast.sensormleditor.properties.labelproviders.PropertyLabelProvider;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TermDefinitionPropertySource extends AbstractPropertySource implements
		IPropertySource {

	public static final String PROPERTY_DEFINITION = "SensorMLEditor.definition";
	public PropertyDescriptor definitionDescriptor;

	boolean stop = false;

	public TermDefinitionPropertySource(Element element, DOMHelper domHelper,
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

			if (dom.existAttribute((Element) children.item(j), "definition")) {
				handleDefinitionAttribute(children.item(j));
				stop = true;
				break;
			} else if (dom.hasQName(children.item(j), "a:documentation")) {
				continue;
			} else if (dom.hasQName(children.item(j), "rng:attribute")) {

				if (dom.getAttributeValue((Element) children.item(j), "name")
						.equals("definition")) {
					definitionDescriptor = new TextPropertyDescriptor(
							PROPERTY_DEFINITION, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(node,
							dom);
					definitionDescriptor.setLabelProvider(plp);
					stop = true;
					break;
				}

			} else {
				if (!stop)
					recursiveInitProperties(children.item(j));
			}
		}
	}

	public void handleDefinitionAttribute(Node node) {
		definitionDescriptor = new PropertyDescriptor(PROPERTY_DEFINITION,
				"definition");

		AttributeLabelProvider plp = new AttributeLabelProvider(dom, node);
		definitionDescriptor.setLabelProvider(plp);
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
		if ((id == PROPERTY_DEFINITION) && (definitionDescriptor != null)) {
			/*
			 * String displayName = (String)
			 * definitionDescriptor.getDisplayName(); label =
			 * definitionDescriptor.getLabelProvider() .getText(displayName);
			 */
			PropertyLabelProvider prop = (PropertyLabelProvider) definitionDescriptor
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

		if ((id == PROPERTY_DEFINITION) && (definitionDescriptor != null)) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) definitionDescriptor
					.getLabelProvider();
			Node changeNode = propProvider.getNode();
			if (str != null && !str.equals("")) {
				if (!dom.existAttribute((Element) changeNode,
						"rng:attribute/rng:data/rng:value/@selected")) {
					dom.setAttributeValue((Element) changeNode,
							"rng:attribute/rng:data/rng:value/@selected", "true");
				}
				dom.setElementValue((Element) changeNode, "rng:attribute/rng:data/rng:value",
						value.toString());
				((Element) changeNode).setAttribute("xng:selected", "true");

			} else {

				Element valueNode = dom.getElement((Element) changeNode,
						"rng:attribute/rng:data/rng:value");
				((Element) changeNode).removeAttribute("xng:selected");
				valueNode.setTextContent("");
				valueNode.removeAttribute("selected");

			}
			this.firePropertyChange("INSERT", null,null);
		}
		
	}

}
