package org.vast.sensormleditor.properties.descriptors;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.properties.labelproviders.PropertyLabelProvider;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ContactInfoPropertySource extends AbstractPropertySource implements
		IPropertySource {
	public static final String PROPERTY_CONTACT_INFO = "SensorMLEditor.sml.contactInfo";
	public TextPropertyDescriptor contactDescriptor;
	private DOMHelperAddOn domUtil;

	public ContactInfoPropertySource(Element element, DOMHelper domHelper,
			TreeViewer viewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, viewer, smlEditor);
		domUtil = new DOMHelperAddOn(dom);
		initProperties();
	}

	public void initProperties() {
		Element retNode = null;
		Node contactInfo = domUtil.findNode(ele, "contactInfo", retNode);
		if (contactInfo!= null){
			contactDescriptor = new TextPropertyDescriptor(
					PROPERTY_CONTACT_INFO, "name");

			PropertyLabelProvider plp = new PropertyLabelProvider(contactInfo, dom);
			contactDescriptor.setLabelProvider(plp);
		} else {
			recursiveInitProperties(ele);
		}
	}
	
	public PropertyLabelProvider getProvider() {
		PropertyLabelProvider propProvider = (PropertyLabelProvider) contactDescriptor
				.getLabelProvider();
		return propProvider;
	}

	public void recursiveInitProperties(Node node) {
		NodeList children = dom.getChildElements(node);
		for (int j = 0; j < children.getLength(); j++) {
			if (dom.hasQName(children.item(j), "sml:contactInfo")) {
				contactDescriptor = new TextPropertyDescriptor(
						PROPERTY_CONTACT_INFO, "name");

				PropertyLabelProvider plp = new PropertyLabelProvider(node, dom);
				contactDescriptor.setLabelProvider(plp);
				contactDescriptor.setCategory(node.getLocalName());
				
			} else if (dom.hasQName(children.item(j), "a:documentation")) {
				continue;
			} else {
				recursiveInitProperties(children.item(j));
			}
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
		if ((id == PROPERTY_CONTACT_INFO) && (contactDescriptor != null)) {
			String displayName = (String) contactDescriptor.getDisplayName();
			label = contactDescriptor.getLabelProvider().getText(displayName);
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
		PropertyLabelProvider propProvider = (PropertyLabelProvider) contactDescriptor .getLabelProvider();
		Node changeNode = propProvider.getNode();
		if (dom.hasQName(changeNode.getParentNode(), "rng:optional")){
			((Element) ((Element) changeNode).getParentNode()).setAttribute("xng:selected", "true");
			this.firePropertyChange("INSERT", null, null);
		}
		
	}
	


}
