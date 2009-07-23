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

public class OrganizationNamePropertySource extends AbstractPropertySource
		implements IPropertySource {
	public static final String PROPERTY_ORGANIZATION_NAME = "SensorMLEditor.sml.organizationName";
	public TextPropertyDescriptor nameDescriptor;

	public OrganizationNamePropertySource(Element element, DOMHelper domHelper,
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
			if (dom.hasQName(children.item(j), "sml:organizationName")) {
				nameDescriptor = new TextPropertyDescriptor(
						PROPERTY_ORGANIZATION_NAME, "name");

				PropertyLabelProvider plp = new PropertyLabelProvider(node, dom);
				nameDescriptor.setLabelProvider(plp);
				nameDescriptor.setCategory(node.getLocalName());
				/*
				 * String selected = dom.getAttributeValue((Element) node,
				 * "@xng:selected"); if ((selected != null && selected
				 * .equalsIgnoreCase("TRUE"))) plp.setOptionalSelected(true);
				 * else plp.setOptionalSelected(false);
				 */
			} else if (dom.hasQName(children.item(j), "a:documentation")) {
				continue;
			} else {
				recursiveInitProperties(children.item(j));
			}
		}
	}

	/*
	 * public void enableOptional(Object id, boolean add) { boolean enable =
	 * add; if ((id == PROPERTY_ORGANIZATION_NAME ) && (nameDescriptor != null))
	 * { PropertyLabelProvider propProvider = (PropertyLabelProvider)
	 * nameDescriptor .getLabelProvider(); Node changeNode =
	 * propProvider.getNode(); if (!enable) {
	 * domUtil.deSelectOptionalChild(changeNode);
	 * propProvider.setOptionalSelected(false); } else {
	 * domUtil.selectOptionalChild(changeNode);
	 * propProvider.setOptionalSelected(true); } nameDescriptor = null; }
	 * initProperties(); this.treeViewer.refresh(); }
	 * 
	 * public boolean isEnabled(Object id) { if ((id ==
	 * PROPERTY_ORGANIZATION_NAME ) && (nameDescriptor != null)) { return
	 * ((PropertyLabelProvider) nameDescriptor.getLabelProvider()).isEnabled();
	 * } return true; }
	 */

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
		if ((id == PROPERTY_ORGANIZATION_NAME) && (nameDescriptor != null)) {
			String displayName = (String) nameDescriptor.getDisplayName();
			label = nameDescriptor.getLabelProvider().getText(displayName);
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

	/*
	 * @Override public void setPropertyValue(Object id, Object value) { String
	 * str = (String) value; if ((id == PROPERTY_ORGANIZATION_NAME ) &&
	 * (nameDescriptor != null)) { PropertyLabelProvider propProvider =
	 * (PropertyLabelProvider) nameDescriptor .getLabelProvider(); if
	 * (!propProvider.isEnabled()) return; Node changeNode =
	 * propProvider.getNode(); domUtil.setElementValue(changeNode, str); }
	 * this.firePropertyChange("INSERT", null,null); }
	 */

	@Override
	public void setPropertyValue(Object id, Object value) {
		String str = (String) value;
		if ((id == PROPERTY_ORGANIZATION_NAME) && (nameDescriptor != null)) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) nameDescriptor
					.getLabelProvider();
			Node changeNode = propProvider.getNode();
			Element gmlName = dom.getElement((Element) changeNode,
					"sml:organizationName");
			if (str != null && !str.equals("")) {
				if (!dom.existAttribute(gmlName, "rng:value/@selected")) {
					dom.setAttributeValue((Element) gmlName,
							"rng:data/rng:value/@selected", "true");
				}
				dom.setElementValue((Element) gmlName, "rng:data/rng:value",
						value.toString());
				((Element) changeNode).setAttribute("xng:selected", "true");

			} else {
				Element valueNode = dom.getElement((Element) changeNode,
						"sml:organizationName/rng:data/rng:value");
				((Element) changeNode).removeAttribute("xng:selected");
				valueNode.setTextContent("");
				valueNode.removeAttribute("selected");
			}
			this.firePropertyChange("INSERT", null, null);
		}
		
	}

}
