package org.vast.sensormleditor.properties.descriptors;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.properties.labelproviders.AttributeLabelProvider;
import org.vast.sensormleditor.properties.labelproviders.PropertyLabelProvider;
import org.vast.sensormleditor.relaxNG.Relax2Hybrid;
import org.vast.xml.DOMHelper;
import org.vast.xml.transform.DOMTransformException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AttributeNamePropertySource extends AbstractPropertySource
		implements IPropertySource {

	public static final String PROPERTY_NAME = "SensorMLEditor.attribute.name";
	public TextPropertyDescriptor nameDescriptor;

	public String header;
	protected static final String[] properties = new String[20];
	protected IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[20];
	protected int propertyCount;
	boolean gotName = false;

	public AttributeNamePropertySource(Element element, DOMHelper domHelper,
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
		/*if (dom.hasQName(ele, "sml:characteristics"))
			if (dom.existAttribute(ele, "name")) {
				handleName(ele);
				gotName = true;
			}*/
		recursiveInitProperties(ele);
	}

	public void recursiveInitProperties(Node node) {

		NodeList children = dom.getChildElements(node);
		for (int j = 0; j < children.getLength(); j++) {

			if (dom.hasQName(children.item(j), "a:documentation"))
				continue;
			else if (dom.hasQName(children.item(j), "rng:attribute")) {

				if (dom.getAttributeValue((Element) children.item(j), "name")
						.equals("name")) {
					nameDescriptor = new TextPropertyDescriptor(PROPERTY_NAME,
							"name");
					PropertyLabelProvider plp = new PropertyLabelProvider(node,
							dom);
					nameDescriptor.setLabelProvider(plp);
					gotName = true;
					break;
				}
			} else if (dom.hasQName(children.item(j), "rng:ref")) {
					if (isSWEName(children.item(j))) {
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
					else 
						continue;
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
	
	public boolean isSWEName(Node node) {
		String refName = dom.getAttributeValue((Element) node, "name");
		return refName.equals("swe.nameAtt");
	}


	public void setPropertyValue(Object id, Object value) {
		String str = (String) value;

		if ((id == PROPERTY_NAME) && (nameDescriptor != null)) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) nameDescriptor
					.getLabelProvider();
			Node changeNode = propProvider.getNode();
			Element gmlName = dom.getElement((Element) changeNode,
					"rng:attribute");
			if (str != null && !str.equals("")) {
				if (!dom.existAttribute(gmlName, "rng:data/rng:value/@selected")) {
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

		
	}

	/*
	 * public void setPropertyValue(Object id, Object value) { Node changeNode =
	 * null; if ((id == PROPERTY_NAME) && (nameDescriptor != null)) {
	 * ILabelProvider propProvider = (ILabelProvider) nameDescriptor
	 * .getLabelProvider(); if (propProvider instanceof AttributeLabelProvider){
	 * changeNode = ((AttributeLabelProvider)propProvider).getNode();
	 * //domUtil.setElementValue(changeNode, str); } else if (propProvider
	 * instanceof PropertyLabelProvider){ changeNode =
	 * ((PropertyLabelProvider)propProvider).getNode();
	 * //domUtil.setElementValue(changeNode, str); } //Element name =
	 * dom.getElement((Element) changeNode, "rng:attribute");
	 * dom.setElementValue((Element) changeNode,
	 * "rng:attribute/rng:data/rng:value", value .toString());
	 * dom.setElementValue((Element) changeNode,
	 * "rng:attribute/rng:data/rng:value/@selected",true);
	 * 
	 * ((Element) changeNode).setAttribute("xng:selected", "true"); }
	 * 
	 * this.firePropertyChange("INSERT", null,null); }
	 */

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		// TODO Auto-generated method stub
		return null;
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

}
