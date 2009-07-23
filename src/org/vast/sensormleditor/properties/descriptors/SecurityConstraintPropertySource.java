package org.vast.sensormleditor.properties.descriptors;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.properties.labelproviders.ChoiceBoxLabelProvider;
import org.vast.sensormleditor.properties.labelproviders.PropertyLabelProvider;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SecurityConstraintPropertySource extends AbstractPropertySource
		implements IPropertySource {
	public static final String PROPERTY_CLASSIFICATION_CHOICE = "SensorMLEditor.sml.classification.choice";
	public static final String PROPERTY_OWNERPRODUCER = "SensorMLEditor.sml.ownerproducer";
	public static final String PROPERTY_SCICONTROLS = "SensorMLEditor.sml.scicontrols";
	public static final String PROPERTY_SARIDENTIFIER = "SensorMLEditor.sml.saridentifier";
	public static final String PROPERTY_DISSEMINATIONCONTROLS = "SensorMLEditor.sml.disseminationcontrols";
	public static final String PROPERTY_FGISOURCEPROTECTED = "SensorMLEditor.sml.fgisourceprotected";
	public static final String PROPERTY_FGISOURCEOPEN = "SensorMLEditor.sml.fgisourceopen";
	public static final String PROPERTY_RELEASABLETO = "SensorMLEditor.sml.releasableto";
	public static final String PROPERTY_NONICMARKINGS = "SensorMLEditor.sml.nonicmarkings";
	public static final String PROPERTY_CLASSIFIEDBY = "SensorMLEditor.sml.classifiedby";
	public static final String PROPERTY_CLASSFICATIONREASON = "SensorMLEditor.sml.classificationreason";
	public static final String PROPERTY_DERIVEDFROM = "SensorMLEditor.sml.derivedfrom";
	public static final String PROPERTY_DECLASSDATE = "SensorMLEditor.sml.declassdate";
	public static final String PROPERTY_DECLASSEVENT = "SensorMLEditor.sml.declassevent";
	public static final String PROPERTY_DECLASSEXCEPTION = "SensorMLEditor.sml.declassexception";
	public static final String PROPERTY_TYPEOFEXEMPTEDSOURCE = "SensorMLEditor.sml.typeofexemptedsource";
	public static final String PROPERTY_DATEOFEXEMPTEDSOURCE = "SensorMLEditor.sml.dateofexemptedsource";
	public static final String PROPERTY_DECLASSMANUALREVIEW = "SensorMLEditor.sml.declassmanualreview";

	protected static final String[] properties = new String[20];
	protected IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[20];
	protected int propertyCount;
	private PropertyDescriptor classChoiceDescriptor;
	private PropertyDescriptor ownerProducerDescriptor;
	private PropertyDescriptor sciControlsDescriptor;
	private PropertyDescriptor sarIdentifierDescriptor;
	private PropertyDescriptor dissemControlDescriptor;
	private PropertyDescriptor fgiProtectedDescriptor;
	private PropertyDescriptor fgiOpenDescriptor;
	private PropertyDescriptor releaseToDescriptor;
	private PropertyDescriptor nonICDescriptor;
	private PropertyDescriptor classifiedByDescriptor;
	private PropertyDescriptor classificationReasonDescriptor;
	private PropertyDescriptor derivedFromDescriptor;
	private PropertyDescriptor deClassDateDescriptor;
	private PropertyDescriptor deClassEventDescriptor;
	private PropertyDescriptor deClassExceptionDescriptor;
	private PropertyDescriptor typeExemptedSourceDescriptor;
	private PropertyDescriptor dateExemptedSourceDescriptor;
	private PropertyDescriptor deClassManualDescriptor;
	private DOMHelperAddOn domUtil;
	//public SensorMLToolsComboPropertyDescriptor choiceDescriptor;
	public String[] choiceLabels;
	protected int labelCount;
	protected boolean stop = false;
	protected Node currentSelectedNode = null;

	public SecurityConstraintPropertySource(Element element,
			DOMHelper domHelper, TreeViewer viewer, SMLTreeEditor smlEditor) {
		super(element, domHelper, viewer, smlEditor);
		domUtil = new DOMHelperAddOn(dom);
		labelCount = 0;
		choiceLabels = new String[20];
		initProperties();
	}

	public void initProperties() {
		Element retNode = null;
		Node security = domUtil.findNode(ele, "Security", retNode);

		NodeList children = dom.getChildElements(security);
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeName().equals("rng:attribute")) {
				if (dom.existElement((Element) child, "rng:choice")) {
					Node choiceNode = dom.getElement((Element) child,
							"rng:choice");
					this.handleChoice(choiceNode);
				}
			} else if (child.getNodeName().equals("rng:optional")) {
				handleOptionalAttributes(child);
			}
		}
	}

	public void handleOptionalAttributes(Node node) {
		NodeList children = dom.getChildElements(node);
		for (int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if (child.getNodeName().equals("rng:attribute")) {

				String attributeName = dom.getAttributeValue((Element) child,
						"name");
				if (attributeName.equals("ism:ownerProducer")) {
					ownerProducerDescriptor = new TextPropertyDescriptor(
							PROPERTY_OWNERPRODUCER, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					ownerProducerDescriptor.setLabelProvider(plp);
				} else if (attributeName.equals("ism:SCIcontrols")) {
					sciControlsDescriptor = new TextPropertyDescriptor(
							PROPERTY_SCICONTROLS, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					sciControlsDescriptor.setLabelProvider(plp);
				} else if (attributeName.equals("ism:SARIdentifier")) {
					sarIdentifierDescriptor = new TextPropertyDescriptor(
							PROPERTY_SARIDENTIFIER, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					sarIdentifierDescriptor.setLabelProvider(plp);
				} else if (attributeName.equals("ism:disseminationControls")) {
					dissemControlDescriptor = new TextPropertyDescriptor(
							PROPERTY_DISSEMINATIONCONTROLS, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					dissemControlDescriptor.setLabelProvider(plp);
				} else if (attributeName.equals("ism:FGIsourceOpen")) {
					fgiOpenDescriptor = new TextPropertyDescriptor(
							PROPERTY_FGISOURCEOPEN, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					fgiOpenDescriptor.setLabelProvider(plp);
				} else if (attributeName.equals("ism:FGIsourceProtected")) {
					fgiProtectedDescriptor = new TextPropertyDescriptor(
							PROPERTY_FGISOURCEPROTECTED, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					fgiProtectedDescriptor.setLabelProvider(plp);
				} else if (attributeName.equals("ism:releasableTo")) {
					releaseToDescriptor = new TextPropertyDescriptor(
							PROPERTY_RELEASABLETO, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					releaseToDescriptor.setLabelProvider(plp);
				} else if (attributeName.equals("ism:nonICmarkings")) {
					nonICDescriptor = new TextPropertyDescriptor(
							PROPERTY_NONICMARKINGS, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					nonICDescriptor.setLabelProvider(plp);
				} else if (attributeName.equals("ism:classifiedBy")) {
					classifiedByDescriptor = new TextPropertyDescriptor(
							PROPERTY_CLASSIFIEDBY, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					classifiedByDescriptor.setLabelProvider(plp);
				} else if (attributeName.equals("ism:classificationReason")) {
					classificationReasonDescriptor = new TextPropertyDescriptor(
							PROPERTY_CLASSFICATIONREASON, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					classificationReasonDescriptor.setLabelProvider(plp);
				} else if (attributeName.equals("ism:derivedFrom")) {
					derivedFromDescriptor = new TextPropertyDescriptor(
							PROPERTY_DERIVEDFROM, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					derivedFromDescriptor.setLabelProvider(plp);
				} else if (attributeName.equals("ism:declassDate")) {
					deClassDateDescriptor = new TextPropertyDescriptor(
							PROPERTY_DECLASSDATE, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					deClassDateDescriptor.setLabelProvider(plp);
				} else if (attributeName.equals("ism:declassEvent")) {
					deClassEventDescriptor = new TextPropertyDescriptor(
							PROPERTY_DECLASSEVENT, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					deClassEventDescriptor.setLabelProvider(plp);
				} else if (attributeName.equals("ism:declassException")) {
					deClassExceptionDescriptor = new TextPropertyDescriptor(
							PROPERTY_DECLASSEXCEPTION, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					deClassExceptionDescriptor.setLabelProvider(plp);
				} else if (attributeName.equals("ism:typeOfExemptedSource")) {
					typeExemptedSourceDescriptor = new TextPropertyDescriptor(
							PROPERTY_TYPEOFEXEMPTEDSOURCE, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					typeExemptedSourceDescriptor.setLabelProvider(plp);
				} else if (attributeName.equals("ism:dateOfExemptedSource")) {
					dateExemptedSourceDescriptor = new TextPropertyDescriptor(
							PROPERTY_DATEOFEXEMPTEDSOURCE, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					dateExemptedSourceDescriptor.setLabelProvider(plp);
				} else if (attributeName.equals("ism:declassManualReview")) {
					deClassManualDescriptor = new TextPropertyDescriptor(
							PROPERTY_DECLASSMANUALREVIEW, "name");
					PropertyLabelProvider plp = new PropertyLabelProvider(
							child, dom);
					deClassManualDescriptor.setLabelProvider(plp);
				}

			}
		}
	}

	public void getValidChoices(Node node) {
		NodeList ns = dom.getChildElements(node);
		for (int i = 0; i < ns.getLength(); i++) {
			if (dom.hasQName(ns.item(i), "rng:value")) {

				choiceLabels[labelCount] = new String();
				choiceLabels[labelCount++] = ns.item(i).getTextContent();
				String selected = dom.getAttributeValue((Element) ns.item(i),
						"xng:selected");
				if (selected != null && selected.equalsIgnoreCase("TRUE")) {
					currentSelectedNode = ns.item(i);
				}
			}

		}
	}

	public void handleChoice(Node node) {

		getValidChoices(node);
		String[] retArray = new String[labelCount];
		System.arraycopy(choiceLabels, 0, retArray, 0, labelCount);

		classChoiceDescriptor = new SensorMLToolsComboPropertyDescriptor(
				PROPERTY_CLASSIFICATION_CHOICE, node.getParentNode()
						.getLocalName(), retArray);
		ChoiceBoxLabelProvider prov = new ChoiceBoxLabelProvider(retArray,
				node, dom);
		prov.setSelectedNode(currentSelectedNode);
		classChoiceDescriptor.setLabelProvider(prov);
		classChoiceDescriptor.setCategory(node.getLocalName());

		stop = true;
		return;
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

		if ((id == PROPERTY_CLASSIFICATION_CHOICE)
				&& (classChoiceDescriptor != null)) {
			ChoiceBoxLabelProvider choiceProvider = (ChoiceBoxLabelProvider) classChoiceDescriptor
					.getLabelProvider();
			Node selected = (Node) choiceProvider.getSelectedNode();
			if (selected != null)
				attValue = selected.getTextContent();

			return attValue;

		} else if ((id == PROPERTY_SARIDENTIFIER)
				&& (sarIdentifierDescriptor != null)) {
			propProvider = (PropertyLabelProvider) sarIdentifierDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_FGISOURCEOPEN)
				&& (fgiOpenDescriptor != null)) {
			propProvider = (PropertyLabelProvider) fgiOpenDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_DISSEMINATIONCONTROLS)
				&& (dissemControlDescriptor != null)) {
			propProvider = (PropertyLabelProvider) dissemControlDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_SCICONTROLS)
				&& (sciControlsDescriptor != null)) {
			propProvider = (PropertyLabelProvider) sciControlsDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_OWNERPRODUCER)
				&& (ownerProducerDescriptor != null)) {
			propProvider = (PropertyLabelProvider) ownerProducerDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_FGISOURCEPROTECTED
				&& (fgiProtectedDescriptor != null)) {
			propProvider = (PropertyLabelProvider) fgiProtectedDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_RELEASABLETO && (releaseToDescriptor != null)) {
			propProvider = (PropertyLabelProvider) releaseToDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_NONICMARKINGS && (nonICDescriptor != null)) {
			propProvider = (PropertyLabelProvider) nonICDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_CLASSIFIEDBY
				&& (classifiedByDescriptor != null)) {
			propProvider = (PropertyLabelProvider) classifiedByDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_CLASSFICATIONREASON
				&& (classificationReasonDescriptor != null)) {
			propProvider = (PropertyLabelProvider) classificationReasonDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_DERIVEDFROM
				&& (derivedFromDescriptor != null)) {
			propProvider = (PropertyLabelProvider) derivedFromDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_DECLASSDATE
				&& (deClassDateDescriptor != null)) {
			propProvider = (PropertyLabelProvider) deClassDateDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_DECLASSEVENT
				&& (deClassEventDescriptor != null)) {
			propProvider = (PropertyLabelProvider) deClassEventDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_DECLASSEXCEPTION
				&& (deClassExceptionDescriptor != null)) {
			propProvider = (PropertyLabelProvider) deClassExceptionDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_TYPEOFEXEMPTEDSOURCE
				&& (typeExemptedSourceDescriptor != null)) {
			propProvider = (PropertyLabelProvider) typeExemptedSourceDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_DATEOFEXEMPTEDSOURCE
				&& (dateExemptedSourceDescriptor != null)) {
			propProvider = (PropertyLabelProvider) dateExemptedSourceDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_DECLASSMANUALREVIEW
				&& (deClassManualDescriptor != null)) {
			propProvider = (PropertyLabelProvider) deClassManualDescriptor
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

		if ((id == PROPERTY_CLASSIFICATION_CHOICE)
				&& (classChoiceDescriptor != null)) {
			ChoiceBoxLabelProvider choiceProvider = (ChoiceBoxLabelProvider) classChoiceDescriptor
					.getLabelProvider();
			Node choice = choiceProvider.getNode();
			NodeList cList = dom.getChildElements(choice);
			for (int i = 0; i < cList.getLength(); i++) {
				if (dom.hasQName(cList.item(i), "rng:value")) {
					String name = cList.item(i).getTextContent();
					if (name.equals(str)) {
						dom.setAttributeValue((Element) cList.item(i),
								"xng:selected", "true");
						currentSelectedNode = cList.item(i);
						choiceProvider.setSelectedNode(currentSelectedNode);
					} else {
						((Element) cList.item(i))
								.removeAttribute("xng:selected");
					}
				}
			}
			this.firePropertyChange("INSERT", null, null);
			return;

		} else if ((id == PROPERTY_SARIDENTIFIER)
				&& (sarIdentifierDescriptor != null)) {
			propProvider = (PropertyLabelProvider) sarIdentifierDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_FGISOURCEOPEN)
				&& (fgiProtectedDescriptor != null)) {
			propProvider = (PropertyLabelProvider) fgiOpenDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_DISSEMINATIONCONTROLS)
				&& (dissemControlDescriptor != null)) {
			propProvider = (PropertyLabelProvider) dissemControlDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_SCICONTROLS)
				&& (sciControlsDescriptor != null)) {
			propProvider = (PropertyLabelProvider) sciControlsDescriptor
					.getLabelProvider();

		} else if ((id == PROPERTY_OWNERPRODUCER)
				&& (ownerProducerDescriptor != null)) {
			propProvider = (PropertyLabelProvider) ownerProducerDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_FGISOURCEPROTECTED
				&& (fgiProtectedDescriptor != null)) {
			propProvider = (PropertyLabelProvider) fgiProtectedDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_RELEASABLETO && (releaseToDescriptor != null)) {
			propProvider = (PropertyLabelProvider) releaseToDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_NONICMARKINGS && (nonICDescriptor != null)) {
			propProvider = (PropertyLabelProvider) nonICDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_CLASSIFIEDBY
				&& (classifiedByDescriptor != null)) {
			propProvider = (PropertyLabelProvider) classifiedByDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_CLASSFICATIONREASON
				&& (classificationReasonDescriptor != null)) {
			propProvider = (PropertyLabelProvider) classificationReasonDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_DERIVEDFROM
				&& (derivedFromDescriptor != null)) {
			propProvider = (PropertyLabelProvider) derivedFromDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_DECLASSDATE
				&& (deClassDateDescriptor != null)) {
			propProvider = (PropertyLabelProvider) deClassDateDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_DECLASSEVENT
				&& (deClassEventDescriptor != null)) {
			propProvider = (PropertyLabelProvider) deClassEventDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_DECLASSEXCEPTION
				&& (deClassExceptionDescriptor != null)) {
			propProvider = (PropertyLabelProvider) deClassExceptionDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_TYPEOFEXEMPTEDSOURCE
				&& (typeExemptedSourceDescriptor != null)) {
			propProvider = (PropertyLabelProvider) typeExemptedSourceDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_DATEOFEXEMPTEDSOURCE
				&& (dateExemptedSourceDescriptor != null)) {
			propProvider = (PropertyLabelProvider) dateExemptedSourceDescriptor
					.getLabelProvider();

		} else if (id == PROPERTY_DECLASSMANUALREVIEW
				&& (deClassManualDescriptor != null)) {
			propProvider = (PropertyLabelProvider) deClassManualDescriptor
					.getLabelProvider();

		}

		if (propProvider != null) {
			Node changeNode = propProvider.getNode();
			if (changeNode.getParentNode().getNodeName().equals("rng:optional")) {
				((Element) changeNode.getParentNode()).setAttribute(
						"xng:selected", "true");
			}
			Node dataNode = dom.getElement((Element) changeNode, "rng:data");
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

	public String[] getChoices(Object id) {

		if (choiceLabels == null)
			initProperties();
		return choiceLabels;
	}

}
