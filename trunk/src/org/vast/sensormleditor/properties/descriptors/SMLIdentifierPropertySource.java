package org.vast.sensormleditor.properties.descriptors;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.properties.labelproviders.AttributeLabelProvider;
import org.vast.sensormleditor.properties.labelproviders.PropertyLabelProvider;
import org.vast.sensormleditor.relaxNG.Relax2Hybrid;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.vast.xml.transform.DOMTransformException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SMLIdentifierPropertySource extends AbstractPropertySource
		implements IPropertySource {

	public static final String PROPERTY_SML_IDENTIFIER = "SensorMLEditor.sml.identifier";
	public TextPropertyDescriptor smldentifierDescriptor;
	boolean stop = false;
	protected static final String[] properties = new String[20];
	protected IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[20];
	protected int propertyCount;
	private PropertyDescriptor descriptor;

	protected DOMHelperAddOn domUtil;

	public SMLIdentifierPropertySource(Element element, DOMHelper domHelper,
			TreeViewer viewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, viewer, smlEditor);
		domUtil = new DOMHelperAddOn(dom);
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
					if (isSMLIdentifier(children.item(j))) {
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
				} else if (dom.hasQName(children.item(j), "rng:oneOrMore"))
					handleoneOrMore(children.item(j));
			} else if ((dom.hasQName(children.item(j), "rng:attribute") && dom
					.hasQName(node, "sml:identifier"))
					|| (dom.hasQName(children.item(j), "sml:identifier") && dom
							.existAttribute((Element) children.item(j), "name"))) {
				this.handleIdentifiers(children.item(j));

			} else {
				recursiveInitProperties(children.item(j));
				if (stop)
					return;
			}
		}
	}

	public void handleoneOrMore(Node node) {
		NodeList c = dom.getChildElements(node);
		for (int j = 0; j < c.getLength(); j++) {

			if (dom.hasQName(c.item(j), "a:documentation")) {
				continue;
			} else if (dom.hasQName(c.item(j), "sml:identifier")) {
				handleData(c.item(j));
			} else {
				properties[propertyCount] = "SensorML.Property" + propertyCount;
				descriptor = new TextPropertyDescriptor(
						properties[propertyCount], c.item(j).getLocalName());
				PropertyLabelProvider plp = new PropertyLabelProvider(
						c.item(j), dom);
				descriptor.setLabelProvider(plp);
				descriptor.setCategory(node.getLocalName());
				propertyDescriptors[propertyCount++] = descriptor;
			}
		}
	}

	public void handleData(Node node) {
		NodeList c = dom.getChildElements(node);
		for (int j = 0; j < c.getLength(); j++) {
			if (dom.hasQName(c.item(j), "rng:attribute")) {
				Element ele = dom.getElement((Element) node,
						"rng:attribute/rng:data");
				smldentifierDescriptor = new TextPropertyDescriptor(
						PROPERTY_SML_IDENTIFIER, c.item(j).getLocalName());
				PropertyLabelProvider plp = new PropertyLabelProvider(ele, dom);
				smldentifierDescriptor.setLabelProvider(plp);
			}
		}
	}

	public boolean isSMLIdentifier(Node node) {
		String refName = dom.getAttributeValue((Element) node, "name");
		return refName.equals("sml.identifier");
	}

	public void handleIdentifiers(Node node) {
		if (dom.hasQName(node, "sml:identifier")
				&& dom.existAttribute((Element) node, "name")) {
			properties[propertyCount] = "SensorML.Property" + propertyCount;
			descriptor = new TextPropertyDescriptor(properties[propertyCount],
					"name");
			// PropertyLabelProvider plp = new PropertyLabelProvider(node, dom);
			AttributeLabelProvider plp = new AttributeLabelProvider(dom, node);

			descriptor.setLabelProvider(plp);
			propertyDescriptors[propertyCount++] = descriptor;
		} else {
			properties[propertyCount] = "SensorML.Property" + propertyCount;
			descriptor = new TextPropertyDescriptor(properties[propertyCount],
					"name");
			PropertyLabelProvider plp = new PropertyLabelProvider(node, dom);
			// AttributeLabelProvider plp = new AttributeLabelProvider(dom,
			// node);

			descriptor.setLabelProvider(plp);
			propertyDescriptors[propertyCount++] = descriptor;
		}

	}

	public void handlesmlIdentifier(Node node) {
		Node n = node;
		smldentifierDescriptor = new TextPropertyDescriptor(
				PROPERTY_SML_IDENTIFIER, n.getNodeName());
		PropertyLabelProvider plp = new PropertyLabelProvider(
				n.getParentNode(), dom);

		smldentifierDescriptor.setLabelProvider(plp);
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

		if ((id == PROPERTY_SML_IDENTIFIER) && (smldentifierDescriptor != null)) {
			return "";
		} else {
			for (int i = 0; i < propertyDescriptors.length; i++) {
				if (propertyDescriptors[i] != null
						&& id.equals(propertyDescriptors[i].getId())) {
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

	public void setPropertyValue(Object id, Object value) {
		String str = (String) value;

		if ((id == PROPERTY_SML_IDENTIFIER) && (smldentifierDescriptor != null)) {
			
			
			Element oneOrMore = dom.getElement((Element)ele,"rng:oneOrMore");
			Node xngNode = domUtil.createXNGItem(oneOrMore);
			
			Node copyNode = dom.getElement((Element)oneOrMore,"sml:identifier");
			Node nNode = dom.getDocument().importNode(copyNode, true);
			xngNode.appendChild(nNode);
			oneOrMore.appendChild(xngNode);
			dom.setElementValue((Element) nNode, "rng:attribute/rng:data/rng:value",str);
			
			dom.setAttributeValue((Element) nNode, "rng:attribute/rng:data/rng:value/@selected",
					"true");
		
			this.firePropertyChange("INSERT", null, null);
		
		} else {
			for (int j = 0; j < propertyDescriptors.length; j++) {
				if (propertyDescriptors[j].getId().equals(id)) {
					PropertyLabelProvider propProvider = (PropertyLabelProvider) propertyDescriptors[j]
							.getLabelProvider();

					Node changeNode = propProvider.getNode();
					if (str.equals("")) {
						Node parentNode = changeNode.getParentNode();
						parentNode.removeChild(changeNode);
						break;
					}
				}
			}
			this.firePropertyChange("INSERT", null,null);
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
