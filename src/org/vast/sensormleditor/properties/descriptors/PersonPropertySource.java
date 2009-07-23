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

public class PersonPropertySource extends AbstractPropertySource implements
		IPropertySource {
	public static final String PROPERTY_SURNAME = "SensorMLEditor.sml.person.surname";
	public static final String PROPERTY_NAME = "SensorMLEditor.sml.person.name";
	public static final String PROPERTY_USERID = "SensorMLEditor.sml.person.userid";
	public static final String PROPERTY_EMAIL = "SensorMLEditor.sml.person.email";
	public static final String PROPERTY_AFFILIATION = "SensorMLEditor.sml.person.affiliation";
	public static final String PROPERTY_PHONE = "SensorMLEditor.sml.person.phone";

	protected static final String[] properties = new String[20];
	protected IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[20];
	protected int propertyCount;
	private PropertyDescriptor surnameDescriptor;
	private PropertyDescriptor nameDescriptor;
	private PropertyDescriptor useridDescriptor;
	private PropertyDescriptor emailDescriptor;
	private PropertyDescriptor affiliationDescriptor;
	private PropertyDescriptor phoneDescriptor;
	private DOMHelperAddOn domUtil;

	public PersonPropertySource(Element element, DOMHelper domHelper,
			TreeViewer viewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, viewer, smlEditor);
		domUtil = new DOMHelperAddOn(dom);
		initProperties();
	}

	public void initProperties() {
		Element retNode = null;
		
		Node surname = domUtil.findNode(ele, "surname", retNode);
		if (surname != null) {
			surnameDescriptor = new TextPropertyDescriptor(PROPERTY_SURNAME, "name");
			PropertyLabelProvider plp = new PropertyLabelProvider(surname, dom);
			surnameDescriptor.setLabelProvider(plp);
		}

		Node name = domUtil.findNode(ele, "name", retNode);
		if (name != null) {
			nameDescriptor = new TextPropertyDescriptor(PROPERTY_NAME, "name");
			PropertyLabelProvider plp = new PropertyLabelProvider(name, dom);
			nameDescriptor.setLabelProvider(plp);
		}

		Node userID = domUtil.findNode(ele, "userID", retNode);
		if (userID != null) {
			useridDescriptor = new TextPropertyDescriptor(PROPERTY_USERID,
					"name");
			PropertyLabelProvider plp = new PropertyLabelProvider(userID, dom);
			useridDescriptor.setLabelProvider(plp);
		}

		Node email = domUtil.findNode(ele, "email", retNode);
		if (email != null) {
			emailDescriptor = new TextPropertyDescriptor(PROPERTY_EMAIL, "name");
			PropertyLabelProvider plp = new PropertyLabelProvider(email, dom);
			emailDescriptor.setLabelProvider(plp);
		}
		
		Node affiliation = domUtil.findNode(ele, "affiliation", retNode);
		if (affiliation != null) {
			affiliationDescriptor = new TextPropertyDescriptor(PROPERTY_AFFILIATION,
					"name");
			PropertyLabelProvider plp = new PropertyLabelProvider(affiliation, dom);
			affiliationDescriptor.setLabelProvider(plp);
		}
		
		Node phone = domUtil.findNode(ele, "phoneNumber", retNode);
		if (phone != null) {
			phoneDescriptor = new TextPropertyDescriptor(PROPERTY_PHONE,
					"name");
			PropertyLabelProvider plp = new PropertyLabelProvider(phone, dom);
			phoneDescriptor.setLabelProvider(plp);
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

		if ((id == PROPERTY_EMAIL) && (emailDescriptor != null)) {
			propProvider = (PropertyLabelProvider) emailDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_PHONE) && (phoneDescriptor != null)) {
			propProvider = (PropertyLabelProvider) phoneDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_AFFILIATION) && (affiliationDescriptor != null)) {
			propProvider = (PropertyLabelProvider) affiliationDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_USERID) && (useridDescriptor != null)) {
			propProvider = (PropertyLabelProvider) useridDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_NAME) && (nameDescriptor != null)) {
			propProvider = (PropertyLabelProvider) nameDescriptor
					.getLabelProvider();
			
		} else if ((id == PROPERTY_SURNAME) && (surnameDescriptor != null)) {
			propProvider = (PropertyLabelProvider) surnameDescriptor
					.getLabelProvider();
		} 
	
		
		Node changeNode = propProvider.getNode();
		Element valueElement = dom.getElement((Element) changeNode,
				"rng:data/rng:value");
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
		if ((id == PROPERTY_EMAIL) && (emailDescriptor != null)) {
			propProvider = (PropertyLabelProvider) emailDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_PHONE) && (phoneDescriptor != null)) {
			propProvider = (PropertyLabelProvider) phoneDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_AFFILIATION) && (affiliationDescriptor != null)) {
			propProvider = (PropertyLabelProvider) affiliationDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_USERID) && (useridDescriptor != null)) {
			propProvider = (PropertyLabelProvider) useridDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_NAME) && (nameDescriptor != null)) {
			propProvider = (PropertyLabelProvider) nameDescriptor
					.getLabelProvider();
			
		} else if ((id == PROPERTY_SURNAME) && (surnameDescriptor != null)) {
			propProvider = (PropertyLabelProvider) surnameDescriptor
					.getLabelProvider();
		} 

		if (propProvider != null) {
			Node changeNode = propProvider.getNode();
			if (changeNode.getParentNode().getNodeName().equals("rng:optional")) {
				((Element) changeNode.getParentNode()).setAttribute(
						"xng:selected", "true");
			}
			Node dataNode = dom
						.getElement((Element) changeNode, "rng:data");
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
