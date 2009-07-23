package org.vast.sensormleditor.properties.descriptors;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.properties.labelproviders.PropertyLabelProvider;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SensorMLPropertySource extends AbstractPropertySource implements
		IPropertySource {

	public static final String PROPERTY_SML = "SensorMLTools.sml";

	public static final String PROPERTY_SWE = "SensorMLTools.swe";

	public static final String PROPERTY_GML = "SensorMLTools.gml";

	public static final String PROPERTY_XLINK = "SensorMLTools.xlink";

	public static final String PROPERTY_RNG = "SensorMLTools.rng";

	public static final String PROPERTY_XNG = "SensorMLTools.xng";

	public static final String PROPERTY_A = "SensorMLTools.a";

	public static final String PROPERTY_NS = "SensorMLTools.ns";

	public static final String PROPERTY_SCHEMA_LOCATION = "SensorMLTools.schema.location";

	public IPropertyDescriptor[] propertyDescriptors;

	public NamedNodeMap attList;

	PropertyDescriptor smlDescriptor;

	PropertyDescriptor gmlDescriptor;

	PropertyDescriptor sweDescriptor;

	PropertyDescriptor xlinkDescriptor;

	PropertyDescriptor aDescriptor;

	PropertyDescriptor rngDescriptor;

	PropertyDescriptor xngDescriptor;

	TextPropertyDescriptor nsDescriptor;

	TextPropertyDescriptor schemaDescriptor;
	
	private DOMHelperAddOn domUtil;

	String smlName, gmlName, sweName, xlinkName, aName, rngName, xngName,
			nsName, schemaName;

	public SensorMLPropertySource(Element element, DOMHelper domHelper,
			TreeViewer treeViewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, treeViewer, smlEditor);
		domUtil = new DOMHelperAddOn(dom);
		initProperties();
	}

	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public void initProperties() {
		if (ele.getNodeName().equals("sml:SensorML")) {
			attList = ele.getAttributes();
			for (int i = 0; i < attList.getLength(); i++) {
				String name = attList.item(i).getLocalName();
				if (name != null) {
					Node node = attList.item(i);
					if (name.equals("sml")) {
						smlDescriptor = new PropertyDescriptor(PROPERTY_SML,
								"sml");
						setSMLName(node.getNodeValue());
						smlDescriptor.setCategory("DTD");
					} else if (name.equals("gml")) {
						gmlDescriptor = new PropertyDescriptor(PROPERTY_GML,
								"gml");
						setGMLName(node.getNodeValue());
						gmlDescriptor.setCategory("DTD");
					} else if (name.equals("swe")) {
						sweDescriptor = new PropertyDescriptor(PROPERTY_SWE,
								"swe");
						setSWEName(node.getNodeValue());
						sweDescriptor.setCategory("DTD");
					} else if (name.equals("xlink")) {
						xlinkDescriptor = new PropertyDescriptor(
								PROPERTY_XLINK, "xlink");
						setXLINKName(node.getNodeValue());
						xlinkDescriptor.setCategory("DTD");
					} else if (name.equals("rng")) {
						rngDescriptor = new PropertyDescriptor(PROPERTY_RNG,
								"rng");
						setRNGName(node.getNodeValue());
						rngDescriptor.setCategory("DTD");
					} else if (name.equals("xng")) {
						xngDescriptor = new PropertyDescriptor(PROPERTY_XNG,
								"xng");
						setXNGName(node.getNodeValue());
						xngDescriptor.setCategory("DTD");
					} else if (name.equals("a")) {
						aDescriptor = new PropertyDescriptor(PROPERTY_A, "a");
						setAName(node.getNodeValue());
						aDescriptor.setCategory("DTD");
					}
				}
			}

			if (dom.existElement(ele, "rng:optional/rng:attribute/")) {
				
				NodeList optionalAtt = dom.getElements(ele,
						"rng:optional/rng:attribute");
				for (int i = 0; i < optionalAtt.getLength(); i++) {
					if (optionalAtt.item(i).getNodeName().equals("rng:attribute")) {
						String name = dom.getAttributeValue((Element) optionalAtt.item(i),
								"name");
						if (name.equals("ns")) {
							PropertyLabelProvider plp = new PropertyLabelProvider(
									optionalAtt.item(i), dom);
							nsDescriptor = new TextPropertyDescriptor(PROPERTY_NS,
									"ns");
							nsDescriptor.setLabelProvider(plp);
							setNSName(dom.getAttributeValue((Element) optionalAtt.item(i),"name"));
							nsDescriptor.setCategory("DTD");
						} else if (name.equals("schemaLocation")) {
							
							PropertyLabelProvider plp = new PropertyLabelProvider(
									optionalAtt.item(i), dom);
							
							schemaDescriptor = new TextPropertyDescriptor(
									PROPERTY_SCHEMA_LOCATION, "name");
							schemaDescriptor.setLabelProvider(plp);
							setSchemaName(dom.getAttributeValue((Element) optionalAtt.item(i),"name"));
							schemaDescriptor.setCategory("DTD");
						}
					}
				}

			}
			propertyDescriptors = new IPropertyDescriptor[] { smlDescriptor,
					gmlDescriptor, sweDescriptor, xlinkDescriptor,
					rngDescriptor, xngDescriptor, aDescriptor, nsDescriptor,
					schemaDescriptor };

		}
	}

	public IPropertyDescriptor[] getPropertyDescriptors() {

		return propertyDescriptors;
	}

	public Object getPropertyValue(Object id) {
		if (smlDescriptor != null && id == PROPERTY_SML)
			return getSMLName();
		else if (gmlDescriptor != null && id == PROPERTY_GML)
			return getGMLName();
		else if (sweDescriptor != null && id.equals(PROPERTY_SWE))
			return getSWEName();
		else if (xlinkDescriptor != null && id.equals(PROPERTY_XLINK))
			return getXLINKName();
		else if (xngDescriptor != null && id.equals(PROPERTY_XNG))
			return getXNGName();
		else if (rngDescriptor != null && id.equals(PROPERTY_RNG))
			return getRNGName();
		else if (aDescriptor != null && id.equals(PROPERTY_A))
			return getAName();
		else if (nsDescriptor != null && id.equals(PROPERTY_NS))
			return getNSName();
		else if (schemaDescriptor != null && id.equals(PROPERTY_SCHEMA_LOCATION)){
			PropertyLabelProvider propProvider = (PropertyLabelProvider) schemaDescriptor
					.getLabelProvider();
			Node changeNode = propProvider.getNode();
			Element valueElement = dom.getElement((Element) changeNode,
					"rng:data/rng:value");
			String attValue=null;
			if (valueElement != null)
				attValue = valueElement.getTextContent();
			return attValue;
		}
			

		return null;

	}

	public boolean isPropertySet(Object id) {

		// other properties are not supported currently
		return false;

	}

	public void resetPropertyValue(Object id) {
		// TODO Auto-generated method stub

	}

	public void setPropertyValue(Object id, Object value) {
		String str = (String) value;
		if (PROPERTY_SML != null && id.equals(PROPERTY_SML)) {
			setSMLName(str);
		} else if (PROPERTY_GML != null && id.equals(PROPERTY_GML)) {
			setGMLName(str);
		} else if (PROPERTY_SWE != null && id.equals(PROPERTY_SWE)) {
			setSWEName(str);
		} else if (PROPERTY_XLINK != null && id.equals(PROPERTY_XLINK)) {
			setXLINKName(str);
		} else if (PROPERTY_XNG != null && id.equals(PROPERTY_XNG)) {
			setXNGName(str);
		} else if (PROPERTY_RNG != null && id.equals(PROPERTY_RNG)) {
			setRNGName(str);
		} else if (PROPERTY_A != null && id.equals(PROPERTY_A)) {
			setAName(str);
		} else if (PROPERTY_NS != null && id.equals(PROPERTY_NS)) {
			setNSName(str);
		} else if (schemaDescriptor != null
				&& id.equals(PROPERTY_SCHEMA_LOCATION)) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) schemaDescriptor
			.getLabelProvider();
			if (propProvider != null) {
				Node changeNode = propProvider.getNode();
				if (changeNode.getParentNode().getNodeName().equals("rng:optional")) {
					((Element) changeNode.getParentNode()).setAttribute(
							"xng:selected", "true");
				}
				changeNode.setTextContent(str);
				//dom.setAttributeValue((Element) changeNode, "schemaLocation",str);
				Node dataNode = dom.getElement((Element) changeNode, "rng:data");
				//dom.setElementValue((Element) dataNode, "rng:value", str);
				//if (!dom.existAttribute((Element) dataNode, "rng:value/@selected")) {
				//	dom.setAttributeValue((Element) dataNode,
				//			"rng:value/@selected", "true");
				//}
				/*if (!dom.existAttribute((Element) dataNode, "id")) {
					String newID = domUtil.findUniqueID();
					dom.setAttributeValue((Element) dataNode, "id", newID);
				}*/
				this.firePropertyChange("INSERT", null, null);
			}
		}

	}

	public void setSMLName(String name) {
		smlName = name;
	}

	public String getSMLName() {
		return smlName;
	}

	public void setGMLName(String name) {
		gmlName = name;
	}

	public String getGMLName() {
		return gmlName;
	}

	public void setSWEName(String name) {
		sweName = name;
	}

	public String getSWEName() {
		return sweName;
	}

	public void setXLINKName(String name) {
		xlinkName = name;
	}

	public String getXLINKName() {
		return xlinkName;
	}

	public void setAName(String name) {
		aName = name;
	}

	public String getAName() {
		return aName;
	}

	public void setRNGName(String name) {
		rngName = name;
	}

	public String getRNGName() {
		return rngName;
	}

	public void setXNGName(String name) {
		xngName = name;
	}

	public String getXNGName() {
		return xngName;
	}

	public void setSchemaName(String name) {
		schemaName = name;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setNSName(String name) {
		nsName = name;
	}

	public String getNSName() {
		return nsName;
	}

}
