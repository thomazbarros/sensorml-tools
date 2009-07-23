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

public class SMLonlineResourcePropertySource extends AbstractPropertySource
		implements IPropertySource {

	public static final String PROPERTY_SML_ONLINERESOURCE = "SensorMLEditor.sml.onlineResource";
	public TextPropertyDescriptor smlonlineResourceDescriptor;
	private boolean stop = false;
	public SMLonlineResourcePropertySource(Element element,
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
			if (!children.item(j).getNamespaceURI().equals(rngaURI)) {
				if (dom.hasQName(children.item(j), "rng:ref")) {
					if (isSMLOnlineResource(children.item(j))) {
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
				} else if (dom.hasQName(children.item(j), "sml:onlineResource")) {
					handleSMLOnlineResource(children.item(j));
					break;
				} else {
					recursiveInitProperties(children.item(j));
					if (stop)
						return;
				}
			}
		}
	}

/*	public void recursiveInitProperties(Node node) {

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
	}*/

/*	public void handleRNGRef(Node node) {
		PropertyLabelProvider plp = new PropertyLabelProvider(node
				.getParentNode(), dom);
		// plp.setOptionalSelected(false);
		String refName = dom.getAttributeValue((Element) node, "name");
		if (refName.equals("sml.onlineResource")) {
			smlonlineResourceDescriptor = new TextPropertyDescriptor(
					PROPERTY_SML_ONLINERESOURCE, refName);
			smlonlineResourceDescriptor.setLabelProvider(plp);
		}
	}*/

	/*
	 * public boolean isEnabled(Object id) { if ((id ==
	 * PROPERTY_SML_ONLINERESOURCE)&& (smlonlineResourceDescriptor!= null)) {
	 * return ((PropertyLabelProvider)
	 * smlonlineResourceDescriptor.getLabelProvider()).isEnabled(); } return
	 * true; }
	 * 
	 * public void enableOptional(Object id, boolean add) { boolean enable =
	 * add; if ((id == PROPERTY_SML_ONLINERESOURCE)&&
	 * (smlonlineResourceDescriptor!= null)) { PropertyLabelProvider
	 * propProvider = (PropertyLabelProvider) smlonlineResourceDescriptor
	 * .getLabelProvider(); Node changeNode = propProvider.getNode(); if
	 * (!enable) { domUtil.restoreRNGREF(changeNode, "sml.onlineResource");
	 * propProvider.setOptionalSelected(false); } else {
	 * domUtil.resolveRNGREF(changeNode); domUtil.resolveRNGREF(changeNode);
	 * propProvider.setOptionalSelected(true); } smlonlineResourceDescriptor =
	 * null; } initProperties(); this.treeViewer.refresh(); }
	 */
	public void handleSMLOnlineResource(Node node) {
		Node n = node;
		smlonlineResourceDescriptor = new TextPropertyDescriptor(
				PROPERTY_SML_ONLINERESOURCE, n.getNodeName());
		PropertyLabelProvider plp = new PropertyLabelProvider(
				n.getParentNode(), dom);
		smlonlineResourceDescriptor.setLabelProvider(plp);
		stop = true;
	}
	
	public boolean isSMLOnlineResource(Node node) {
		String refName = dom.getAttributeValue((Element) node, "name");
		return refName.equals("sml.onlineResource");
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
		if ((id == PROPERTY_SML_ONLINERESOURCE)
				&& (smlonlineResourceDescriptor != null)) {
			String displayName = (String) smlonlineResourceDescriptor
					.getDisplayName();
			label = smlonlineResourceDescriptor.getLabelProvider().getText(
					displayName);
			return label;
		}
		return "";

	}

	public void setPropertyValue(Object id, Object value) {
		String str = (String) value;

		if ((id == PROPERTY_SML_ONLINERESOURCE)
				&& (smlonlineResourceDescriptor != null)) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) smlonlineResourceDescriptor
					.getLabelProvider();
			Node changeNode = propProvider.getNode();
			Element onlineName = dom.getElement((Element) changeNode,
					"sml:onlineResource");
			if (str != null && !str.equals("")) {
				if (!dom.existAttribute(onlineName, "rng:attribute/rng:data/rng:value/@selected")) {
					dom.setAttributeValue((Element) onlineName,
							"rng:attribute/rng:data/rng:value/@selected", "true");
				}
				dom.setElementValue((Element) onlineName, "rng:attribute/rng:data/rng:value",
						value.toString());
				((Element) changeNode).setAttribute("xng:selected", "true");
				this.firePropertyChange("INSERT", null, null);

			} else {
			
				Element valueNode = dom.getElement((Element) changeNode,
						"sml:onlineResource/rng:attribute/rng:data/rng:value");
				if (valueNode!=null){
					((Element) changeNode).removeAttribute("xng:selected");
					valueNode.setTextContent("");
					valueNode.removeAttribute("selected");
				}
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
