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

public class ConnectionPropertySource extends AbstractPropertySource
		implements IPropertySource {
	public static final String PROPERTY_LINK_SOURCE = "SensorMLEditor.sml.link.source";
	public static final String PROPERTY_LINK_DESTINATION = "SensorMLEditor.sml.link.dest";


	protected static final String[] properties = new String[20];
	protected IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[20];
	protected int propertyCount;
	private PropertyDescriptor sourceDescriptor;
	private PropertyDescriptor destDescriptor;

	private DOMHelperAddOn domUtil;
	
	protected boolean stop = false;
	protected Node currentSelectedNode = null;

	public ConnectionPropertySource(Element element,
			DOMHelper domHelper, TreeViewer viewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, viewer, smlEditor);
		domUtil = new DOMHelperAddOn(dom);
		
		initProperties();
	}

	public void initProperties() {
		Element retNode = null;
		Node link = domUtil.findNode(ele, "Link", retNode);
		if (link!= null){
			
			Node source = domUtil.findNode(link,"source",retNode);
			if (source!=null){
				sourceDescriptor = new TextPropertyDescriptor(
						PROPERTY_LINK_SOURCE, "name");
				PropertyLabelProvider plp = new PropertyLabelProvider(
						source, dom);
				sourceDescriptor.setLabelProvider(plp);
			}
			
			Node dest = domUtil.findNode(link,"destination",retNode);
			if (dest!=null){
				destDescriptor = new TextPropertyDescriptor(
						PROPERTY_LINK_DESTINATION, "name");
				PropertyLabelProvider plp = new PropertyLabelProvider(
						dest, dom);
				destDescriptor.setLabelProvider(plp);
			}
			
		}
	
	}

	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getPropertyValue(Object id) {
		String attValue = "";
		PropertyLabelProvider propProvider = null;

		if ((id == PROPERTY_LINK_SOURCE)
				&& (sourceDescriptor != null)) {
			propProvider = (PropertyLabelProvider) sourceDescriptor.getLabelProvider();
	
		} else if ((id == PROPERTY_LINK_DESTINATION)
				&& (destDescriptor != null)) {
			propProvider = (PropertyLabelProvider) destDescriptor
					.getLabelProvider();
		}

		Node changeNode = propProvider.getNode();
		Element valueElement = dom.getElement((Element) changeNode,
				"rng:attribute/rng:data/rng:value");
		if (valueElement != null)
			attValue = valueElement.getTextContent();
		return attValue;
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
		PropertyLabelProvider propProvider = null;

		if ((id == PROPERTY_LINK_SOURCE)
				&& (sourceDescriptor != null)) {
			propProvider = (PropertyLabelProvider) sourceDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_LINK_DESTINATION)
				&& (destDescriptor != null)) {
			propProvider = (PropertyLabelProvider) destDescriptor
					.getLabelProvider();
		}

		if (propProvider != null) {
			Node changeNode = propProvider.getNode();
			
			Node dataNode = dom.getElement((Element) changeNode, "rng:attribute/rng:data");
			dom.setElementValue((Element) dataNode, "rng:value", str);
			if (!dom.existAttribute((Element) dataNode, "rng:value/@selected")) {
				dom.setAttributeValue((Element) dataNode,
						"rng:value/@selected", "true");
			}
			if (!dom.existAttribute((Element) dataNode, "id")) {
				String newID = domUtil.findUniqueID();
				dom.setAttributeValue((Element) dataNode, "id", newID);
			}
			this.firePropertyChange("INSERT", null, null);
		}

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

}
