package org.vast.sensormleditor.properties.descriptors;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.properties.labelproviders.PropertyLabelProvider;
import org.vast.sensormleditor.relaxNG.Relax2Hybrid;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.vast.xml.transform.DOMTransformException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XLinkHrefPropertySource extends AbstractPropertySource implements
		IPropertySource {
	public static final String PROPERTY_XLINK_HREF = "SensorMLEditor.xlink.href";
	public TextPropertyDescriptor xlinkDescriptor;
	boolean stop = false;
	private DOMHelperAddOn domUtil;

	public XLinkHrefPropertySource(Element element, DOMHelper domHelper,
			TreeViewer viewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, viewer, smlEditor);
		domUtil = new DOMHelperAddOn(dom);
		initProperties();
	}

	public void initProperties() {
		if (dom.hasQName(ele, "rng:attribute")) {
			if (dom.getAttributeValue((Element) ele, "name").equals(
					"xlink:href")) {
				xlinkDescriptor = new TextPropertyDescriptor(
						PROPERTY_XLINK_HREF, "name");
				PropertyLabelProvider plp = new PropertyLabelProvider(ele, dom);
				xlinkDescriptor.setLabelProvider(plp);
				stop = true;
			}
		} else {
			recursiveInitProperties(ele);
		}
	}

	public void recursiveInitProperties(Node node) {
		NodeList children = dom.getChildElements(node);
		for (int j = 0; j < children.getLength(); j++) {
			if (!children.item(j).getNamespaceURI().equals(rngaURI) &&
					(!children.item(j).getNodeName().equals("rng:optional"))) {
				if (dom.hasQName(children.item(j), "rng:ref")) {
					if (isXLinkHRef(children.item(j))) {
						Relax2Hybrid transform1 = new Relax2Hybrid();
						try {
							transform1.transform(srcDoc, dom, node);
							// now resolve the rng:ref
							transform1.retrieveRelaxNGRef((Element) node);
						} catch (DOMTransformException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else
						continue;
					recursiveInitProperties(node);
					if (stop)
						return;
				} else if (dom.hasQName(children.item(j), "rng:attribute")) {
					if (dom.getAttributeValue((Element) children.item(j),
							"name").equals("xlink:href")) {
						xlinkDescriptor = new TextPropertyDescriptor(
								PROPERTY_XLINK_HREF, "name");
						PropertyLabelProvider plp = new PropertyLabelProvider(
								children.item(j), dom);
						xlinkDescriptor.setLabelProvider(plp);
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

	public boolean isXLinkHRef(Node node) {
		String refName = dom.getAttributeValue((Element) node, "name");
		return refName.equals("xlink.href");
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
		if ((id == PROPERTY_XLINK_HREF) && (xlinkDescriptor != null)) {
			String displayName = (String) xlinkDescriptor.getDisplayName();
			label = xlinkDescriptor.getLabelProvider().getText(displayName);
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
		PropertyLabelProvider propProvider = (PropertyLabelProvider) xlinkDescriptor
				.getLabelProvider();
		Node changeNode = propProvider.getNode();
		if (str != null && !str.equals("")) {
			if ((id == PROPERTY_XLINK_HREF) && (xlinkDescriptor != null)) {
				Element dataNode = dom.getElement((Element) changeNode,
						"rng:data");
				dom.setElementValue((Element) dataNode, "rng:value", str);
				if (!dom.existAttribute(dataNode, "rng:value/@selected")) {
					dom.setAttributeValue((Element) dataNode,
							"rng:value/@selected", "true");
					String newID = domUtil.findUniqueID();
					dataNode.removeAttribute("id");
					dom.setAttributeValue((Element) dataNode, "id", newID);

				}
				this.firePropertyChange("INSERT", null, null);
			}

		} else {

			dom.setElementValue((Element) changeNode, "rng:data/rng:value", "");
			dom.getElement((Element) changeNode, "rng:data/rng:value")
					.removeAttribute("xng:selected");
			((Element) changeNode).removeAttribute("xng:selected");
			this.firePropertyChange("INSERT", null, null);
		}

	}
}
