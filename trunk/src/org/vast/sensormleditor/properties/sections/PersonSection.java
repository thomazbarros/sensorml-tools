package org.vast.sensormleditor.properties.sections;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertyConstants;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.outlineview.SensorMLContentOutlinePage;
import org.vast.sensormleditor.properties.descriptors.PersonPropertySource;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

public class PersonSection extends AbstractPropertySection {
	private String headerLabel = "Person";
	private CLabel headerlabelLabel;
	
	private CLabel surnamelabelLabel;
	private String surnameLabel = "Surname:";
	private Text surnameText;

	private CLabel namelabelLabel;
	private String nameLabel = "Name:";
	private Text nameText;
	
	private CLabel useridlabelLabel;
	private String useridLabel = "User ID:";
	private Text useridText;

	private CLabel affiliationlabelLabel;
	private String affiliationLabel = "Affiliation:";
	private Text affiliationText;

	private CLabel phonelabelLabel;
	private String phoneLabel = "Phone:";
	private Text phoneText;

	private CLabel emaillabelLabel;
	private String emailLabel = "Email:";
	private Text emailText;
	
	private SMLTreeEditor smlEditor;
	private Element element;
	private DOMHelper dom;
	private TreeViewer treeViewer;
	private PersonPropertySource personPropertySource;
	private Font fontBold = new Font(PlatformUI.getWorkbench().getDisplay(),
			"Tahoma", 8, SWT.BOLD);
	//boolean stop=false;

	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		Assert.isTrue(selection instanceof IStructuredSelection);
		Object input = ((IStructuredSelection) selection).getFirstElement();
		Assert.isTrue(input instanceof Element);
		this.element = (Element) input;
		if (part instanceof SMLTreeEditor) {
			smlEditor = ((SMLTreeEditor) part);
			dom = smlEditor.getModel();
			treeViewer = (TreeViewer) ((SMLTreeEditor) part).getTreeViewer();
		} else {
			smlEditor = (SMLTreeEditor) ((SensorMLContentOutlinePage) part).smlEditor;
			dom = smlEditor.getModel();
			treeViewer = smlEditor.getTreeViewer();
		}
		
		//stop = false;
		Element retNode = null;
		DOMHelperAddOn domUtil = new DOMHelperAddOn(dom);
		Element chosenOne = domUtil.getChosenField(element, retNode);
		if (chosenOne != null)
			element = chosenOne;
		if (element != null) {
			personPropertySource = new PersonPropertySource(element,
					dom, treeViewer, smlEditor);
		}
	}

	private ModifyListener listener = new ModifyListener() {
		public void modifyText(ModifyEvent arg0) {
			personPropertySource.setPropertyValue(
					PersonPropertySource.PROPERTY_EMAIL,
					emailText.getText());
			personPropertySource.setPropertyValue(
					PersonPropertySource.PROPERTY_SURNAME,
					surnameText.getText());
			personPropertySource.setPropertyValue(
					PersonPropertySource.PROPERTY_NAME,
					nameText.getText());
			personPropertySource.setPropertyValue(
					PersonPropertySource.PROPERTY_USERID,
					useridText.getText());
			personPropertySource.setPropertyValue(
					PersonPropertySource.PROPERTY_PHONE,
					phoneText.getText());
			personPropertySource.setPropertyValue(
					PersonPropertySource.PROPERTY_AFFILIATION,
					affiliationText.getText());
		}
	};

	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite = getWidgetFactory()
				.createFlatFormComposite(parent);

		FormData data;
		
		headerlabelLabel = getWidgetFactory().createCLabel(composite,
				headerLabel); //$NON-NLS-1$
		headerlabelLabel.setFont(fontBold);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);

		data.top = new FormAttachment(0, ITabbedPropertyConstants.VSPACE);
		headerlabelLabel.setLayoutData(data);
		
		surnameText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(headerlabelLabel);
		surnameText.setLayoutData(data);
		surnameText.addModifyListener(listener);

		surnamelabelLabel = getWidgetFactory().createCLabel(composite,
				surnameLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(surnameText);
		data.top = new FormAttachment(headerlabelLabel);
		surnamelabelLabel.setLayoutData(data);
		
		nameText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(surnameText,
				ITabbedPropertyConstants.VSPACE);
		nameText.setLayoutData(data);
		nameText.addModifyListener(listener);

		namelabelLabel = getWidgetFactory().createCLabel(composite, nameLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(nameText);
		data.top = new FormAttachment(surnameText,
				ITabbedPropertyConstants.VSPACE);
		namelabelLabel.setLayoutData(data);
		
		useridText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(nameText, ITabbedPropertyConstants.VSPACE);
		useridText.setLayoutData(data);
		useridText.addModifyListener(listener);

		useridlabelLabel = getWidgetFactory().createCLabel(composite,
				useridLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(useridText);
		data.top = new FormAttachment(nameText, ITabbedPropertyConstants.VSPACE);
		useridlabelLabel.setLayoutData(data);
		
		affiliationText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(useridText, ITabbedPropertyConstants.VSPACE);
		affiliationText.setLayoutData(data);
		affiliationText.addModifyListener(listener);

		affiliationlabelLabel = getWidgetFactory().createCLabel(composite, affiliationLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment (affiliationText);
		data.top = new FormAttachment(useridText, ITabbedPropertyConstants.VSPACE);
		affiliationlabelLabel.setLayoutData(data);
		
		phoneText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(affiliationText, ITabbedPropertyConstants.VSPACE);
		phoneText.setLayoutData(data);
		
		phonelabelLabel = getWidgetFactory()
				.createCLabel(composite, phoneLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(phoneText);
		data.top = new FormAttachment(affiliationText, ITabbedPropertyConstants.VSPACE);
		phonelabelLabel.setLayoutData(data);

		emailText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 125);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(phoneText,
				ITabbedPropertyConstants.VSPACE);
		emailText.setLayoutData(data);
		emailText.addModifyListener(listener);

		emaillabelLabel = getWidgetFactory()
				.createCLabel(composite, emailLabel);
		emaillabelLabel.setToolTipText("Email");
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(emailText);
		data.top = new FormAttachment(phoneText,
				ITabbedPropertyConstants.VSPACE);
		emaillabelLabel.setLayoutData(data);

	}

	public void refresh() {

		
		emailText.removeModifyListener(listener);
		if (PersonPropertySource.PROPERTY_EMAIL != null) {
			String valueValue = (String) personPropertySource
					.getPropertyValue(PersonPropertySource.PROPERTY_EMAIL);
			if (valueValue != null)
				emailText.setText(valueValue);
		}
		emailText.addModifyListener(listener);
		
		surnameText.removeModifyListener(listener);
		if (PersonPropertySource.PROPERTY_SURNAME != null) {
			String valueValue = (String) personPropertySource
					.getPropertyValue(PersonPropertySource.PROPERTY_SURNAME);
			if (valueValue != null)
				surnameText.setText(valueValue);
		}
		surnameText.addModifyListener(listener);
		
		useridText.removeModifyListener(listener);
		if (PersonPropertySource.PROPERTY_USERID != null) {
			String valueValue = (String) personPropertySource
					.getPropertyValue(PersonPropertySource.PROPERTY_USERID);
			if (valueValue != null)
				useridText.setText(valueValue);
		}
		useridText.addModifyListener(listener);
		
		affiliationText.removeModifyListener(listener);
		if (PersonPropertySource.PROPERTY_AFFILIATION != null) {
			String valueValue = (String) personPropertySource
					.getPropertyValue(PersonPropertySource.PROPERTY_AFFILIATION);
			if (valueValue != null)
				affiliationText.setText(valueValue);
		}
		affiliationText.addModifyListener(listener);
		
		nameText.removeModifyListener(listener);
		if (PersonPropertySource.PROPERTY_NAME != null) {
			String valueValue = (String) personPropertySource
					.getPropertyValue(PersonPropertySource.PROPERTY_NAME);
			if (valueValue != null)
				nameText.setText(valueValue);
		}
		nameText.addModifyListener(listener);
		
		phoneText.removeModifyListener(listener);
		if (PersonPropertySource.PROPERTY_PHONE != null) {
			String valueValue = (String) personPropertySource
					.getPropertyValue(PersonPropertySource.PROPERTY_PHONE);
			if (valueValue != null)
				phoneText.setText(valueValue);
		}
		phoneText.addModifyListener(listener);
	}

}
