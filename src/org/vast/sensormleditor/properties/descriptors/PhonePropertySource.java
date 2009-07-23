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

public class PhonePropertySource extends AbstractPropertySource implements
		IPropertySource {
	public static final String PROPERTY_VOICE = "SensorMLEditor.sml.phone.voice";
	public static final String PROPERTY_FAX = "SensorMLEditor.sml.phone.fax";
	public TextPropertyDescriptor voiceDescriptor;
	public TextPropertyDescriptor faxDescriptor;

	protected static final String[] voiceProperties = new String[20];
	protected IPropertyDescriptor[] voicePropertyDescriptors = new IPropertyDescriptor[20];
	protected int voicePropertyCount;

	protected static final String[] faxProperties = new String[20];
	protected IPropertyDescriptor[] faxPropertyDescriptors = new IPropertyDescriptor[20];
	protected int faxPropertyCount;
	private PropertyDescriptor descriptor;
	private DOMHelperAddOn domUtil;
	private Node phone;

	public PhonePropertySource(Element element, DOMHelper domHelper,
			TreeViewer viewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, viewer, smlEditor);
		domUtil = new DOMHelperAddOn(dom);
		initProperties();
	}

	public void initProperties() {
		Element retNode = null;
		phone = domUtil.findNode(ele, "phone", retNode);
		Node voice = domUtil.findNode(ele, "voice", retNode);
		if (voice != null) {
			if (dom.existElement((Element) voice.getParentNode(), "xng:item")) {
				NodeList nodes = dom.getElements((Element) voice
						.getParentNode(), "xng:item");
				for (int j = 0; j < nodes.getLength(); j++) {
					voiceProperties[voicePropertyCount] = "SensorML.VoiceProperty"
							+ voicePropertyCount;
					descriptor = new TextPropertyDescriptor(
							voiceProperties[voicePropertyCount], nodes.item(j)
									.getLocalName());
					PropertyLabelProvider plp = new PropertyLabelProvider(nodes
							.item(j), dom);
					descriptor.setLabelProvider(plp);
					voicePropertyDescriptors[voicePropertyCount++] = descriptor;
				}
			}
			voiceDescriptor = new TextPropertyDescriptor(PROPERTY_VOICE, "name");
			PropertyLabelProvider plp = new PropertyLabelProvider(voice, dom);
			voiceDescriptor.setLabelProvider(plp);
		}

		Node fax = domUtil.findNode(ele, "facsimile", retNode);
		if (fax != null) {
			if (dom.existElement((Element) fax.getParentNode(), "xng:item")) {
				NodeList nodes = dom.getElements((Element) fax
						.getParentNode(), "xng:item");
				for (int j = 0; j < nodes.getLength(); j++) {
					faxProperties[faxPropertyCount] = "SensorML.FaxProperty"
							+ faxPropertyCount;
					descriptor = new TextPropertyDescriptor(
							faxProperties[faxPropertyCount], nodes.item(j)
									.getLocalName());
					PropertyLabelProvider plp = new PropertyLabelProvider(nodes
							.item(j), dom);
					descriptor.setLabelProvider(plp);
					faxPropertyDescriptors[faxPropertyCount++] = descriptor;
				}
			}
			faxDescriptor = new TextPropertyDescriptor(PROPERTY_FAX, "name");
			PropertyLabelProvider plp = new PropertyLabelProvider(fax, dom);
			faxDescriptor.setLabelProvider(plp);
		}
		// recursiveInitProperties(ele);
	}

/*	public void recursiveInitProperties(Node node) {
		NodeList children = dom.getChildElements(node);
		for (int j = 0; j < children.getLength(); j++) {
			if (dom.hasQName(children.item(j), "sml:phone")) {
				voiceDescriptor = new TextPropertyDescriptor(PROPERTY_VOICE,
						"name");

				PropertyLabelProvider plp = new PropertyLabelProvider(node, dom);
				voiceDescriptor.setLabelProvider(plp);
				voiceDescriptor.setCategory(node.getLocalName());
				
				 * String selected = dom.getAttributeValue((Element) node,
				 * "@xng:selected"); if ((selected != null && selected
				 * .equalsIgnoreCase("TRUE"))) plp.setOptionalSelected(true);
				 * else plp.setOptionalSelected(false);
				 
			} else if (dom.hasQName(children.item(j), "a:documentation")) {
				continue;
			} else {
				recursiveInitProperties(children.item(j));
			}
		}
	}*/

	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public IPropertyDescriptor[] getVoicePropertyDescriptors() {
		IPropertyDescriptor[] retArray;
		if (voicePropertyDescriptors == null
				|| voicePropertyDescriptors.length == 0)
			initProperties();

		retArray = new IPropertyDescriptor[voicePropertyCount];
		System.arraycopy(voicePropertyDescriptors, 0, retArray, 0,
				voicePropertyCount);
		return retArray;
	}
	
	public IPropertyDescriptor[] getFaxPropertyDescriptors() {
		IPropertyDescriptor[] retArray;
		if (faxPropertyDescriptors == null
				|| faxPropertyDescriptors.length == 0)
			initProperties();

		retArray = new IPropertyDescriptor[faxPropertyCount];
		System.arraycopy(faxPropertyDescriptors, 0, retArray, 0,
				faxPropertyCount);
		return retArray;
	}

	@Override
	public Object getPropertyValue(Object id) {

		if ((id == PROPERTY_VOICE) && (voiceDescriptor != null)) {
			return "";
		} else if ((id== PROPERTY_FAX) && (faxDescriptor != null)) {
			return "";
		} else {
			for (int i = 0; i < voicePropertyCount; i++) {
				if (voicePropertyDescriptors[i] != null
						&& id.equals(voicePropertyDescriptors[i].getId())) {
					PropertyLabelProvider propProvider = (PropertyLabelProvider) voicePropertyDescriptors[i]
							.getLabelProvider();
					Node changeNode = propProvider.getNode();
					Element valueElement = dom.getElement((Element) changeNode,
							"sml:voice/rng:data/rng:value");
					String text = valueElement.getTextContent();

					return text;

				}
			}
			
			for (int i = 0; i < faxPropertyCount; i++) {
				if (faxPropertyDescriptors[i] != null
						&& id.equals(faxPropertyDescriptors[i].getId())) {
					PropertyLabelProvider propProvider = (PropertyLabelProvider) faxPropertyDescriptors[i]
							.getLabelProvider();
					Node changeNode = propProvider.getNode();
					Element valueElement = dom.getElement((Element) changeNode,
							"sml:facsimile/rng:data/rng:value");
					String text = valueElement.getTextContent();

					return text;

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
		// TODO Auto-generated method stub

	}

	public void removeVoiceProperty(Object id, Object value) {
		Integer newInt = new Integer((Integer) value);
		int index = newInt.intValue();
		if (voicePropertyDescriptors[index] != null) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) 
				voicePropertyDescriptors[index].getLabelProvider();
			Node changeNode = propProvider.getNode();
			Node parentNode = changeNode.getParentNode();
			parentNode.removeChild(changeNode);
			this.firePropertyChange("INSERT", null, null);
		}
	}
	
	public void removeFaxProperty(Object id, Object value) {
		Integer newInt = new Integer((Integer) value);
		int index = newInt.intValue();
		if (faxPropertyDescriptors[index] != null) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) 
				faxPropertyDescriptors[index].getLabelProvider();
			Node changeNode = propProvider.getNode();
			Node parentNode = changeNode.getParentNode();
			parentNode.removeChild(changeNode);
			this.firePropertyChange("INSERT", null, null);
		}
	}
	
	@Override
	public void setPropertyValue(Object id, Object value) {

		String str = (String) value;
		if ((id == PROPERTY_VOICE) && (voiceDescriptor != null)) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) voiceDescriptor
					.getLabelProvider();
			Node changeNode = propProvider.getNode();
			if (changeNode.getParentNode().getNodeName().equals(
					"rng:zeroOrMore")
					|| (changeNode.getParentNode().getNodeName()
							.equals("rng:oneOrMore"))) {
				Element oneOrMore = dom.getElement((Element) changeNode
						.getParentNode(), "");
				Node xngNode = domUtil.createXNGItem(oneOrMore);
				Node copyNode = dom.getElement((Element) oneOrMore, changeNode
						.getNodeName());
				Node nNode = dom.getDocument().importNode(copyNode, true);
				xngNode.appendChild(nNode);
				oneOrMore.appendChild(xngNode);
				Node dataNode = dom.getElement((Element) xngNode,
						"sml:voice/rng:data");
				dom.setElementValue((Element) dataNode, "rng:value", str);
				dom.setAttributeValue((Element) dataNode,
						"rng:value/@selected", "true");
				String newID = domUtil.findUniqueID();
				dom.setAttributeValue((Element) dataNode, "id", newID);
				if (phone != null) {
					if (phone.getParentNode().getNodeName().equals(
							"rng:optional")) {
						if (!dom
								.existAttribute(
										(Element) phone.getParentNode(),
										"xng:selected")) {
							((Element) ((Element) phone).getParentNode())
									.setAttribute("xng:selected", "true");
						}
					}
				}
				voiceProperties[voicePropertyCount] = "SensorML.VoiceProperty"
					+ voicePropertyCount;
				descriptor = new TextPropertyDescriptor(
					voiceProperties[voicePropertyCount], str);
				PropertyLabelProvider plp = new PropertyLabelProvider(xngNode, dom);
				descriptor.setLabelProvider(plp);
				voicePropertyDescriptors[voicePropertyCount++] = descriptor;
				this.firePropertyChange("INSERT", null, null);
			}

		} else if ((id == PROPERTY_FAX) && (faxDescriptor != null)) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) faxDescriptor
					.getLabelProvider();
			Node changeNode = propProvider.getNode();
			if (changeNode.getParentNode().getNodeName().equals(
					"rng:zeroOrMore")
					|| (changeNode.getParentNode().getNodeName()
							.equals("rng:oneOrMore"))) {
				Element oneOrMore = dom.getElement((Element) changeNode
						.getParentNode(), "");
				Node xngNode = domUtil.createXNGItem(oneOrMore);
				Node copyNode = dom.getElement((Element) oneOrMore, changeNode
						.getNodeName());
				Node nNode = dom.getDocument().importNode(copyNode, true);
				xngNode.appendChild(nNode);
				oneOrMore.appendChild(xngNode);
				Node dataNode = dom.getElement((Element) xngNode,
						"sml:facsimile/rng:data");
				dom.setElementValue((Element) dataNode, "rng:value", str);
				dom.setAttributeValue((Element) dataNode,
						"rng:value/@selected", "true");
				String newID = domUtil.findUniqueID();
				dom.setAttributeValue((Element) dataNode, "id", newID);
				if (phone != null) {
					if (phone.getParentNode().getNodeName().equals(
							"rng:optional")) {
						if (!dom
								.existAttribute(
										(Element) phone.getParentNode(),
										"xng:selected")) {
							((Element) ((Element) phone).getParentNode())
									.setAttribute("xng:selected", "true");
						}
					}
				}
				faxProperties[faxPropertyCount] = "SensorML.FaxProperty"
					+ faxPropertyCount;
				descriptor = new TextPropertyDescriptor(
					faxProperties[faxPropertyCount], str);
				PropertyLabelProvider plp = new PropertyLabelProvider(xngNode, dom);
				descriptor.setLabelProvider(plp);
				faxPropertyDescriptors[faxPropertyCount++] = descriptor;
			}
			this.firePropertyChange("INSERT", null, null);
		} 

		

	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		// TODO Auto-generated method stub
		return null;
	}

}
