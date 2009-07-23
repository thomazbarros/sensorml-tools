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

public class CodeSpacePropertySource extends AbstractPropertySource implements
		IPropertySource {

	public static final String PROPERTY_CODESPACE = "SensorMLEditor.codespace";
	public TextPropertyDescriptor codeSpaceDescriptor;
	boolean stop = false;

	public CodeSpacePropertySource(Element element, DOMHelper domHelper,
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
					if (isCodeSpace(children.item(j))) {
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
				} else if (dom.hasQName(children.item(j), "rng:attribute")
						&& (dom.getAttributeValue((Element) children.item(j),
								"name").equals("codeSpace"))) {
					codeSpaceDescriptor = new TextPropertyDescriptor(
							PROPERTY_CODESPACE, "name");

					PropertyLabelProvider plp = new PropertyLabelProvider(node,
							dom);
					codeSpaceDescriptor.setLabelProvider(plp);
					stop = true;
				} else if (dom.hasQName(children.item(j), "sml:codeSpace")
						|| dom.hasQName(children.item(j), "swe:codeSpace")) {
					handleSweCodeSpace(children.item(j));
					break;
				} else {
					recursiveInitProperties(children.item(j));
					if (stop)
						return;
				}
			}
		}
	}

	public boolean isCodeSpace(Node node) {
		String refName = dom.getAttributeValue((Element) node, "name");
		return (refName.equals("swe.codeSpace") || refName
				.equals("sml.codeSpace"));
	}

	public void handleSweCodeSpace(Node node) {
		Node n = node;
		codeSpaceDescriptor = new TextPropertyDescriptor(PROPERTY_CODESPACE, n
				.getNodeName());
		PropertyLabelProvider plp = new PropertyLabelProvider(
				n.getParentNode(), dom);
		codeSpaceDescriptor.setLabelProvider(plp);
		stop = true;
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
		if ((id == PROPERTY_CODESPACE) && (codeSpaceDescriptor != null)) {
			String displayName = (String) codeSpaceDescriptor.getDisplayName();
			label = codeSpaceDescriptor.getLabelProvider().getText(displayName);
			return label;
		}
		return "";

	}

	public void setPropertyValue(Object id, Object value) {
		String str = (String) value;

		if ((id == PROPERTY_CODESPACE) && (codeSpaceDescriptor != null)) {
			PropertyLabelProvider propProvider = (PropertyLabelProvider) codeSpaceDescriptor
					.getLabelProvider();
			Node changeNode = propProvider.getNode();
			Element baseElement = null;
			if (dom.existElement((Element) changeNode, "sml:codeSpace")) {
				baseElement = dom.getElement((Element) changeNode,
						"sml:codeSpace/rng:attribute");
			} else if (dom.existElement((Element) changeNode, "swe:codeSpace")) {
				baseElement = dom.getElement((Element) changeNode,
						"swe:codeSpace/rng:attribute");
			} else {
				baseElement = dom.getElement((Element) changeNode,
						"rng:attribute");
			}
			// Element name = dom
			// .getElement((Element) changeNode, "rng:attribute");
			if (str != null && !str.equals("")) {
				if (!dom.existAttribute(baseElement,
						"rng:data/rng:value/@selected")) {
					dom.setAttributeValue((Element) baseElement,
							"rng:data/rng:value/@selected", "true");
				}
				dom.setElementValue((Element) baseElement,
						"rng:data/rng:value", value.toString());
				((Element) changeNode).setAttribute("xng:selected", "true");

			} else {
				Element valueNode = dom.getElement((Element) baseElement,
						"rng:data/rng:value");
				((Element) changeNode).removeAttribute("xng:selected");
				valueNode.setTextContent("");
				valueNode.removeAttribute("selected");
			}
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
