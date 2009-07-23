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

public class SMLCharacteristicsPropertySource extends AbstractPropertySource
		implements IPropertySource {

	public static final String PROPERTY_SML_CHARACTERISTICS = "SensorMLEditor.sml.characteristcs";
	public TextPropertyDescriptor smlCharacteristcsDescriptor;

	public SMLCharacteristicsPropertySource(Element element,
			DOMHelper domHelper, TreeViewer viewer, SMLTreeEditor smlEditor) {
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
				handleRNGRef(children.item(j));
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
		if (refName.equals("sml.characterstics")) {
			smlCharacteristcsDescriptor = new TextPropertyDescriptor(
					PROPERTY_SML_CHARACTERISTICS, refName);
			smlCharacteristcsDescriptor.setLabelProvider(plp);
		}
	}

	/*
	 * public boolean isEnabled(Object id) { if ((id ==
	 * PROPERTY_SML_CHARACTERISTICS)&& (smlCharacteristcsDescriptor!= null)) {
	 * return ((PropertyLabelProvider)
	 * smlCharacteristcsDescriptor.getLabelProvider()).isEnabled(); } return
	 * false; }
	 * 
	 * public void enableOptional(Object id, boolean add) { boolean enable =
	 * add; if ((id == PROPERTY_SML_CHARACTERISTICS)&&
	 * (smlCharacteristcsDescriptor!= null)) { PropertyLabelProvider
	 * propProvider = (PropertyLabelProvider) smlCharacteristcsDescriptor
	 * .getLabelProvider(); Node changeNode = propProvider.getNode(); if
	 * (!enable) { domUtil.restoreRNGREF(changeNode, "sml.characteristics");
	 * propProvider.setOptionalSelected(false); } else {
	 * domUtil.resolveRNGREF(changeNode); domUtil.resolveRNGREF(changeNode);
	 * propProvider.setOptionalSelected(true); } smlCharacteristcsDescriptor =
	 * null; } initProperties(); this.treeViewer.refresh(); }
	 */

	public void handleSWECodespace(Node node) {
		Node n = node;
		smlCharacteristcsDescriptor = new TextPropertyDescriptor(
				PROPERTY_SML_CHARACTERISTICS, n.getNodeName());
		PropertyLabelProvider plp = new PropertyLabelProvider(
				n.getParentNode(), dom);
		// plp.setOptionalSelected(true);

		smlCharacteristcsDescriptor.setLabelProvider(plp);
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
		if ((id == PROPERTY_SML_CHARACTERISTICS)
				&& (smlCharacteristcsDescriptor != null)) {
			String displayName = (String) smlCharacteristcsDescriptor
					.getDisplayName();
			label = smlCharacteristcsDescriptor.getLabelProvider().getText(
					displayName);
			return label;
		}
		return "";

	}

	public void setPropertyValue(Object id, Object value) {
		String str = (String) value;

		if ((id == PROPERTY_SML_CHARACTERISTICS)
				&& (smlCharacteristcsDescriptor != null)) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) smlCharacteristcsDescriptor
					.getLabelProvider();

			Node changeNode = propProvider.getNode();
			dom.setElementValue((Element) changeNode, str);
			this.firePropertyChange("INSERT", null, null);
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
