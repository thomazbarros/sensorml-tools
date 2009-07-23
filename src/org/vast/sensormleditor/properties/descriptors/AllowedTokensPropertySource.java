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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AllowedTokensPropertySource extends AbstractPropertySource
		implements IPropertySource {

	public static final String PROPERTY_SWELIST_VALUE = "SensorMLEditor.swelist.value";
	public PropertyDescriptor tokenDescriptor;
	protected static final String[] properties = new String[20];
	protected IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[20];
	protected int propertyCount;
	protected int labelCount;
	private PropertyDescriptor descriptor;
	private DOMHelperAddOn domUtil;

	public AllowedTokensPropertySource(Element element, DOMHelper domHelper,
			TreeViewer viewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, viewer, smlEditor);
		domUtil = new DOMHelperAddOn(dom);
		initProperties();
	}

	public void initProperties() {
		Element retNode = null;
		Node allowedTokens = null;

		if (ele.getLocalName().equals("AllowedTokens"))
			allowedTokens = ele;
		else
			allowedTokens = domUtil.findNode(ele, "AllowedTokens", retNode);

		if (allowedTokens != null) {
			
			Node listNode = dom.getElement((Element) allowedTokens,"swe:valueList/rng:list");
			Element oneOrMore = null;
			if (dom.existElement((Element) listNode, "rng:oneOrMore")) {
				oneOrMore = dom.getElement((Element) listNode,"rng:oneOrMore");
			} else if (dom.existElement((Element) listNode,"rng:zeroOrMore")) {
				oneOrMore = dom.getElement((Element) listNode,"rng:zeroOrMore");
			}
			if (oneOrMore != null) {
				handleoneOrMore(oneOrMore);
			}
			
		}
	}
	
	public void handleoneOrMore(Node node) {
		NodeList c = dom.getChildElements(node);
		for (int j = 0; j < c.getLength(); j++) {
			if (dom.hasQName(c.item(j), "xng:item")) {
				properties[propertyCount] = "SensorML.Property" + propertyCount;
				descriptor = new TextPropertyDescriptor(
						properties[propertyCount], c.item(j).getLocalName());
				PropertyLabelProvider plp = new PropertyLabelProvider(
						c.item(j), dom);
				descriptor.setLabelProvider(plp);
				descriptor.setCategory(node.getLocalName());
				propertyDescriptors[propertyCount++] = descriptor;
			} else if (dom.hasQName(c.item(j), "rng:data")){
				tokenDescriptor = new TextPropertyDescriptor(PROPERTY_SWELIST_VALUE, "name");
				PropertyLabelProvider plp = new PropertyLabelProvider(
					c.item(j), dom);
				tokenDescriptor.setLabelProvider(plp);
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
		IPropertyDescriptor[] retArray;
		if (propertyDescriptors == null || propertyDescriptors.length == 0)
			initProperties();

		retArray = new IPropertyDescriptor[propertyCount];
		System.arraycopy(propertyDescriptors, 0, retArray, 0, propertyCount);
		return retArray;
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (id == PROPERTY_SWELIST_VALUE)
			return "";
		else {
			for (int i = 0; i < propertyDescriptors.length; i++) {
				if (id.equals(propertyDescriptors[i].getId())) {
					String displayName = (String) propertyDescriptors[i]
							.getDisplayName();
					String label = propertyDescriptors[i].getLabelProvider()
							.getText(displayName);
					return label;
				}
			}
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
		
	}

	public void removeAllowedValue(Object id, Object value) {
		Integer newInt = new Integer((Integer) value);
		int index = newInt.intValue();
		if (propertyDescriptors[index] != null) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) propertyDescriptors[index]
					.getLabelProvider();
			Node changeNode = propProvider.getNode();
			Node parentNode = changeNode.getParentNode();
			parentNode.removeChild(changeNode);
			this.firePropertyChange("INSERT", null, null);
		}
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		String str = (String) value;
		PropertyLabelProvider propProvider = null;
		if ((id == PROPERTY_SWELIST_VALUE) && (tokenDescriptor != null)) {
			propProvider = (PropertyLabelProvider) tokenDescriptor.getLabelProvider();
			if (propProvider != null) {
				Node changeNode = propProvider.getNode();
				Element oneOrMore = dom.getElement((Element)changeNode.getParentNode(),"");
				if (oneOrMore!=null){
					Node xngNode = null;
					xngNode = domUtil.createXNGItem(oneOrMore);
					Node copyNode = dom.getElement((Element)oneOrMore,"rng:data");
					Node nNode = dom.getDocument().importNode(copyNode, true);
					xngNode.appendChild(nNode);
					oneOrMore.appendChild(xngNode);
					dom.setElementValue((Element) nNode, "rng:value",str);
					dom.setAttributeValue((Element) nNode, "rng:value/@selected","true");
					propProvider.setNode((Element) xngNode);
					this.firePropertyChange("INSERT", null, null);
				}
			}
		}
	}
}
		
			
			
			
		/*	propProvider = (PropertyLabelProvider) tokenDescriptor
					.getLabelProvider();
			if (propProvider != null) {
				Node changeNode = propProvider.getNode();
				Node xngNode = null;
				if (changeNode.getNodeName().equals("xng:item")) {
					xngNode = changeNode;
					dom.setElementValue((Element) changeNode,
							"rng:data/rng:value", str);
					if (!dom.existAttribute((Element) changeNode,
							"rng:data/rng:value/@selected")) {
						dom.setAttributeValue((Element) changeNode,
								"rng:data/rng:value/@selected", "true");
					}
					if (!dom.existAttribute((Element) dom.getElement(
							(Element) changeNode, "rng:data"), "id")) {
						String newID = domUtil.findUniqueID();
						dom.setAttributeValue((Element) dom.getElement(
								(Element) changeNode, "rng:data"), "id", newID);
					}

				} else {

					Node listNode = dom.getElement((Element) changeNode,
							"swe:valueList/rng:list");
					Element oneOrMore = null;
					if (dom.existElement((Element) listNode, "rng:oneOrMore")) {
						oneOrMore = dom.getElement((Element) listNode,
								"rng:oneOrMore");
					} else if (dom.existElement((Element) listNode,
							"rng:zeroOrMore")) {
						oneOrMore = dom.getElement((Element) listNode,
								"rng:zeroOrMore");
					}
					if (oneOrMore != null) {

						xngNode = domUtil.createXNGItem(oneOrMore);

						Node copyNode = dom.getElement((Element) oneOrMore,
								"rng:data");
						Node nNode = dom.getDocument().importNode(copyNode,
								true);
						xngNode.appendChild(nNode);
						oneOrMore.appendChild(xngNode);
						Node dataNode = dom.getElement((Element) xngNode,
								"rng:data");
						dom.setElementValue((Element) dataNode, "rng:value",
								str);
						if (!dom.existAttribute((Element) dataNode,
								"rng:value/@selected")) {
							dom.setAttributeValue((Element) dataNode,
									"rng:value/@selected", "true");
						}
						if (!dom.existAttribute((Element) dataNode, "id")) {
							String newID = domUtil.findUniqueID();
							dom.setAttributeValue((Element) dataNode, "id",
									newID);
						}

					}

				}
				properties[propertyCount] = "SensorML.Property" + propertyCount;
				tokenDescriptor = new TextPropertyDescriptor(
						properties[propertyCount], str);
				PropertyLabelProvider plp = new PropertyLabelProvider(xngNode,
						dom);
				tokenDescriptor.setLabelProvider(plp);
				propertyDescriptors[propertyCount++] = tokenDescriptor;
				this.firePropertyChange("INSERT", null, null);*/
				
			//}
		//}

	//}

//}}
