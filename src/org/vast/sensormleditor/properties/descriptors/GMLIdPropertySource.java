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

public class GMLIdPropertySource extends AbstractPropertySource implements
		IPropertySource {
	public static final String PROPERTY_GML_ID = "SensorMLEditor.gml.id";
	public TextPropertyDescriptor gmlIdDescriptor;
	boolean stop = false;

	public GMLIdPropertySource(Element element, DOMHelper domHelper,
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
					if (isGMLID(children.item(j))) {
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
					// domUtil.resolveRNGREF(children.item(j));
					recursiveInitProperties(node);
					if (stop)
						return;
				} else if (dom.hasQName(children.item(j), "rng:attribute")) {
					if (dom.getAttributeValue((Element) children.item(j),
							"name").equals("gml:id")) {
						gmlIdDescriptor = new TextPropertyDescriptor(
								PROPERTY_GML_ID, "name");
						PropertyLabelProvider plp = new PropertyLabelProvider(
								node, dom);
						gmlIdDescriptor.setLabelProvider(plp);
						stop = true;

					}
					break;

				} else {
					recursiveInitProperties(children.item(j));
					if (stop)
						return;
				}
			}
		}
	}

	public boolean isGMLID(Node node) {
		String refName = dom.getAttributeValue((Element) node, "name");
		return refName.equals("gml.id");
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
		if ((id == PROPERTY_GML_ID) && (gmlIdDescriptor != null)) {
			String displayName = (String) gmlIdDescriptor.getDisplayName();
			label = gmlIdDescriptor.getLabelProvider().getText(displayName);
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

		if ((id == PROPERTY_GML_ID) && (gmlIdDescriptor != null)) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) gmlIdDescriptor
					.getLabelProvider();
			Node changeNode = propProvider.getNode();
			Element gmlName = dom.getElement((Element) changeNode,
					"rng:attribute");
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
						"rng:attribute/rng:data/rng:value");
				((Element) changeNode).removeAttribute("xng:selected");
				valueNode.setTextContent("");
				valueNode.removeAttribute("selected");

			}
			this.firePropertyChange("INSERT", null, null);
		}

		

		/*
		 * String str = (String) value; PropertyLabelProvider propProvider =
		 * (PropertyLabelProvider) gmlIdDescriptor .getLabelProvider(); Node
		 * changeNode = propProvider.getNode(); if (str != null &&
		 * !str.equals("")) { if ((id == PROPERTY_GML_ID) && (gmlIdDescriptor !=
		 * null)) {
		 * 
		 * domUtil.setElementValue(changeNode, str);
		 * domUtil.selectOptionalChild(changeNode); } else {
		 * domUtil.setElementValue(changeNode, "");
		 * domUtil.deSelectOptionalChild(changeNode); }
		 * this.treeViewer.refresh(); }
		 */
	}

}
