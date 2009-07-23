package org.vast.sensormleditor.properties.sections;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.tabbed.AbstractPropertySection;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.sensormleditor.outlineview.SensorMLContentOutlinePage;
import org.vast.sensormleditor.properties.descriptors.AddressPropertySource;
import org.vast.sensormleditor.properties.descriptors.ContactInfoPropertySource;
import org.vast.sensormleditor.properties.descriptors.PhonePropertySource;
import org.vast.sensormleditor.util.DOMHelperAddOn;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

public class ContactInfoSection extends AbstractPropertySection {

	private CLabel phonelabelLabel;
	private String phoneLabel = "Phone:";
	private Text phoneText;

	private String voiceLabel = "Voice:";
	private List voiceList;
	private CLabel voicelabelLabel;
	private Button addVoice;
	private Button remVoice;

	private String faxLabel = "Fax:";
	private CLabel faxlabelLabel;
	private List faxList;
	private Button addFax;
	private Button remFax;

	private CLabel addresslabelLabel;
	private String addressLabel = "Address:";
	private Text addressText;

	private String deliveryLabel = "Delivery Point:";
	private List deliveryList;
	private CLabel deliverylabelLabel;
	private Button addDelivery;
	private Button remDelivery;

	private CLabel citylabelLabel;
	private String cityLabel = "City:";
	private Text cityText;

	private CLabel arealabelLabel;
	private String areaLabel = "Administrative Area:";
	private Text stateText;

	private CLabel postlabelLabel;
	private String postLabel = "Postal Code:";
	private Text postText;

	private CLabel countrylabelLabel;
	private String countryLabel = "Country:";
	private Text countryText;

	//private Label errorLabel;
	//private IObservableValue uiElement;

	private CLabel emaillabelLabel;
	private String emailLabel = "Email Address:";
	private Text emailText;
	
	private SMLTreeEditor smlEditor;
	private Element element;
	private DOMHelper dom;
	private TreeViewer treeViewer;
	private ContactInfoPropertySource propertySource;
	private PhonePropertySource phonePropertySource;
	private AddressPropertySource addressPropertySource;
	private IPropertyDescriptor[] desc;
	//private static final String MESSAGE = "Name must be longer two letters";
	//private ControlDecoration phoneTextDecorator;
	boolean stop=false;

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
		
		stop = false;
		Element retNode = null;
		DOMHelperAddOn domUtil = new DOMHelperAddOn(dom);
		Element chosenOne = domUtil.getChosenField(element, retNode);
		if (chosenOne != null)
			element = chosenOne;
		if (element != null) {
			propertySource = new ContactInfoPropertySource(element, dom,
					treeViewer, smlEditor);
			Element contactInfo = (Element) propertySource.getProvider().getNode();
			phonePropertySource = new PhonePropertySource(contactInfo,
					dom, treeViewer, smlEditor);
			addressPropertySource = new AddressPropertySource(contactInfo,
					dom, treeViewer, smlEditor);
		}
	}

	private ModifyListener listener = new ModifyListener() {
		public void modifyText(ModifyEvent arg0) {
			addressPropertySource.setPropertyValue(
					AddressPropertySource.PROPERTY_EMAIL,
					emailText.getText());
			addressPropertySource.setPropertyValue(
					AddressPropertySource.PROPERTY_CITY,
					cityText.getText());
			addressPropertySource.setPropertyValue(
					AddressPropertySource.PROPERTY_STATE,
					stateText.getText());
			addressPropertySource.setPropertyValue(
					AddressPropertySource.PROPERTY_POSTAL,
					postText.getText());
			addressPropertySource.setPropertyValue(
					AddressPropertySource.PROPERTY_COUNTRY,
					countryText.getText());
		}
	};

	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite = getWidgetFactory()
				.createFlatFormComposite(parent);

		//FormData data;
		phoneText = getWidgetFactory().createText(composite, "");
		phonelabelLabel = getWidgetFactory().createCLabel(composite, phoneLabel);
		
		FormData data1 = new FormData();
		data1.left = new FormAttachment(0,5);
		data1.right = new FormAttachment(10,0);
		data1.top = new FormAttachment(3,0);
		phonelabelLabel.setLayoutData(data1);
		
		FormData data2 = new FormData();
		data2.left = new FormAttachment(phonelabelLabel,5);
		data2.right = new FormAttachment(30,-5);
		data2.top = new FormAttachment(3,0);
		phoneText.setLayoutData(data2);
		
		voicelabelLabel = getWidgetFactory().createCLabel(composite, voiceLabel);
		voiceList = getWidgetFactory().createList(composite,
				SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		voiceList.setBackground(new Color(null, 224, 255, 255));
		
		FormData data3 = new FormData();
		data3.left = new FormAttachment(0,5);
		data3.right = new FormAttachment(10,0);
		data3.top = new FormAttachment(phonelabelLabel,5);
		voicelabelLabel.setLayoutData(data3);
		
		FormData data4 = new FormData();
		data4.left = new FormAttachment(voicelabelLabel,5);
		data4.right = new FormAttachment(30,-5);
		data4.top = new FormAttachment(phonelabelLabel,5);
		data4.height = 40;
		voiceList.setLayoutData(data4);
		
		addVoice = getWidgetFactory().createButton(composite, "Add Voice",
				SWT.PUSH);
		addVoice.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String newPhone = phoneText.getText();
				if (newPhone != null && newPhone != "") {
					voiceList.add(newPhone);
					phonePropertySource.setPropertyValue(
							PhonePropertySource.PROPERTY_VOICE,phoneText.getText());
					propertySource.setPropertyValue(ContactInfoPropertySource.PROPERTY_CONTACT_INFO, null);
					phoneText.setText("");
					phoneText.setFocus();
				}
			}
		});

		remVoice = getWidgetFactory().createButton(composite, "Delete Voice",
				SWT.PUSH);
		
		remVoice.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int selectionIndex = voiceList.getSelectionIndex();
				desc = phonePropertySource.getVoicePropertyDescriptors();
				for (int i = 0; i < desc.length; i++) {
					String item = (String) phonePropertySource
							.getPropertyValue(desc[i].getId());
					if (item.equals(voiceList.getItem(selectionIndex))) {
						phonePropertySource.removeVoiceProperty(desc[i].getId(), selectionIndex);
					}
				}
				voiceList.remove(selectionIndex);
			}
		});
		
		FormData data5 = new FormData();
		data5.top = new FormAttachment(phoneText,5);
		data5.right = new FormAttachment(40,-5);
		data5.left = new FormAttachment(voiceList,5);
		addVoice.setLayoutData(data5);
		
		FormData data6 = new FormData();
		data6.top = new FormAttachment(addVoice,5);
		data6.right = new FormAttachment(40,-5);
		data6.left = new FormAttachment(voiceList,5);
		remVoice.setLayoutData(data6);

		faxlabelLabel = getWidgetFactory().createCLabel(composite, faxLabel);
		faxList = getWidgetFactory().createList(composite,
				SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		faxList.setBackground(new Color(null, 224, 255, 255));
		
		FormData data7 = new FormData();
		data7.left = new FormAttachment(0,5);
		data7.right = new FormAttachment(10,0);
		data7.top = new FormAttachment(voiceList,5);
		faxlabelLabel.setLayoutData(data7);
		
		FormData data8 = new FormData();
		data8.left = new FormAttachment(faxlabelLabel,5);
		data8.right = new FormAttachment(30,-5);
		data8.top = new FormAttachment(voiceList,5);
		data8.height = 40;
		faxList.setLayoutData(data8);
		
		addFax = getWidgetFactory().createButton(composite, "Add Fax", SWT.PUSH);
		addFax.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String newFax = phoneText.getText();
				if (newFax != null && newFax != "") {
					faxList.add(newFax);
					phonePropertySource.setPropertyValue(
							PhonePropertySource.PROPERTY_FAX,phoneText.getText());
					propertySource.setPropertyValue(ContactInfoPropertySource.PROPERTY_CONTACT_INFO, null);
					phoneText.setText("");
					phoneText.setFocus();
				}
			}
		});
		remFax = getWidgetFactory().createButton(composite, "Delete Fax",
				SWT.PUSH);
		remFax.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int selectionIndex = faxList.getSelectionIndex();
				desc = phonePropertySource.getFaxPropertyDescriptors();
				for (int i = 0; i < desc.length; i++) {
					String item = (String) phonePropertySource
							.getPropertyValue(desc[i].getId());
					if (item.equals(faxList.getItem(selectionIndex))) {
						phonePropertySource.removeFaxProperty(desc[i].getId(), selectionIndex);
					}
				}
				faxList.remove(selectionIndex);
			}
		});
		
		FormData data9 = new FormData();
		data9.top = new FormAttachment(voiceList,5);
		data9.right = new FormAttachment(40,-5);
		data9.left = new FormAttachment(faxList,5);
		addFax.setLayoutData(data9);
		
		FormData data10 = new FormData();
		data10.top = new FormAttachment(addFax,5);
		data10.right = new FormAttachment(40,-5);
		data10.left = new FormAttachment(faxList,5);
		remFax.setLayoutData(data10);
		
		addressText = getWidgetFactory().createText(composite, "");
		addresslabelLabel = getWidgetFactory().createCLabel(composite,
				addressLabel);
		
		FormData data11 = new FormData();
		data11.left = new FormAttachment(0,5);
		data11.right = new FormAttachment(10,0);
		data11.top = new FormAttachment(faxList,5);
		addresslabelLabel.setLayoutData(data11);
		
		FormData data12 = new FormData();
		data12.left = new FormAttachment(addresslabelLabel,5);
		data12.top = new FormAttachment(faxList,5);
		data12.right = new FormAttachment(30,-5);
		addressText.setLayoutData(data12);
		
		deliveryList = getWidgetFactory().createList(composite,
				SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		deliveryList.setBackground(new Color(null, 224, 255, 255));
		deliverylabelLabel = getWidgetFactory().createCLabel(composite,
				deliveryLabel);
		
		FormData data13 = new FormData();
		data13.left = new FormAttachment(0,5);
		data13.right = new FormAttachment(10,0);
		data13.top = new FormAttachment(addresslabelLabel,5);
	
		deliverylabelLabel.setLayoutData(data13);
		
		FormData data14 = new FormData();
		data14.left = new FormAttachment(faxlabelLabel,5);
		data14.right = new FormAttachment(30,-5);
		data14.top = new FormAttachment(addressText,5);
		data14.height = 40;
		deliveryList.setLayoutData(data14);
		
		addDelivery = getWidgetFactory().createButton(composite,
				"Add Delivery Point", SWT.PUSH);
		addDelivery.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String newdelivery = addressText.getText();
				if (newdelivery != null && newdelivery != "") {
					deliveryList.add(newdelivery);
					addressPropertySource.setPropertyValue(
							AddressPropertySource.PROPERTY_DELIVERY,addressText.getText());
					propertySource.setPropertyValue(ContactInfoPropertySource.PROPERTY_CONTACT_INFO, null);
					addressText.setText("");
					addressText.setFocus();
				}
			}
		});
		remDelivery = getWidgetFactory().createButton(composite,
				"Delete Delivery Point", SWT.PUSH);
		remDelivery.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int selectionIndex = deliveryList.getSelectionIndex();
				desc = addressPropertySource.getPropertyDescriptors();
				for (int i = 0; i < desc.length; i++) {
					String item = (String) addressPropertySource
							.getPropertyValue(desc[i].getId());
					if (item.equals(deliveryList.getItem(selectionIndex))) {
						addressPropertySource.removeDeliveryProperty(desc[i].getId(),selectionIndex);
					}
				}
				deliveryList.remove(selectionIndex);
			}
		});
		
		FormData data15 = new FormData();
		data15.top = new FormAttachment(addresslabelLabel,5);
		data15.right = new FormAttachment(40,-5);
		data15.left = new FormAttachment(deliveryList,5);
		addDelivery.setLayoutData(data15);
		
		FormData data16 = new FormData();
		data16.top = new FormAttachment(addDelivery,5);
		data16.right = new FormAttachment(40,-5);
		data16.left = new FormAttachment(deliveryList,5);
		remDelivery.setLayoutData(data16);
		
		cityText = getWidgetFactory().createText(composite, "");
		citylabelLabel = getWidgetFactory().createCLabel(composite, cityLabel);
		
		FormData data17 = new FormData();
		data17.left = new FormAttachment(0,5);
		data17.right = new FormAttachment(10,0);
		data17.top = new FormAttachment(deliveryList,5);
		citylabelLabel.setLayoutData(data17);
		
		FormData data18 = new FormData();
		data18.left = new FormAttachment(citylabelLabel,5);
		data18.top = new FormAttachment(deliveryList,5);
		data18.right = new FormAttachment(30,-5);
		cityText.setLayoutData(data18);
		
		stateText = getWidgetFactory().createText(composite, "");
		arealabelLabel = getWidgetFactory().createCLabel(composite, areaLabel);
		
		FormData data19 = new FormData();
		data19.left = new FormAttachment(0,5);
		data19.right = new FormAttachment(10,0);
		data19.top = new FormAttachment(citylabelLabel,5);
		arealabelLabel.setLayoutData(data19);
		
		FormData data20 = new FormData();
		data20.left = new FormAttachment(arealabelLabel,5);
		data20.top = new FormAttachment(cityText,5);
		data20.right = new FormAttachment(30,-5);
		stateText.setLayoutData(data20);
		
		postText = getWidgetFactory().createText(composite, "");
		postlabelLabel = getWidgetFactory().createCLabel(composite, postLabel);
		
		FormData data21 = new FormData();
		data21.left = new FormAttachment(0,5);
		data21.right = new FormAttachment(10,0);
		data21.top = new FormAttachment(arealabelLabel,5);
		postlabelLabel.setLayoutData(data21);
		
		FormData data22 = new FormData();
		data22.left = new FormAttachment(postlabelLabel,5);
		data22.top = new FormAttachment(stateText,5);
		data22.right = new FormAttachment(30,-5);
		postText.setLayoutData(data22);
		
		countryText = getWidgetFactory().createText(composite, "");
		countrylabelLabel = getWidgetFactory().createCLabel(composite,
				countryLabel);
		
		FormData data23 = new FormData();
		data23.left = new FormAttachment(0,5);
		data23.right = new FormAttachment(10,0);
		data23.top = new FormAttachment(postlabelLabel,5);
		countrylabelLabel.setLayoutData(data23);
		
		FormData data24 = new FormData();
		data24.left = new FormAttachment(countrylabelLabel,5);
		data24.top = new FormAttachment(postText,5);
		data24.right = new FormAttachment(30,-5);
		countryText.setLayoutData(data24);
		
		emailText = getWidgetFactory().createText(composite, "");
		emaillabelLabel = getWidgetFactory().createCLabel(composite, emailLabel);
		
		FormData data25 = new FormData();
		data25.left = new FormAttachment(0,5);
		data25.right = new FormAttachment(10,0);
		data25.top = new FormAttachment(countrylabelLabel,5);
		emaillabelLabel.setLayoutData(data25);
		
		FormData data26 = new FormData();
		data26.left = new FormAttachment(emaillabelLabel,5);
		data26.top = new FormAttachment(countryText,5);
		data26.right = new FormAttachment(30,-5);
		emailText.setLayoutData(data26);
		
	}

	public void refresh() {
		voiceList.removeAll();
		desc = phonePropertySource.getVoicePropertyDescriptors();
		for (int i = 0; i < desc.length; i++) {
			String item = (String) phonePropertySource
					.getPropertyValue(desc[i].getId());

			if ((item != null) && !item.equals("")) {
				voiceList.add(item);
			}
		}
	
		
		faxList.removeAll();
		desc = phonePropertySource.getFaxPropertyDescriptors();
		for (int i = 0; i < desc.length; i++) {
			String item = (String) phonePropertySource
					.getPropertyValue(desc[i].getId());

			if ((item != null) && !item.equals("")) {
				faxList.add(item);
			}
		}
		
		
		deliveryList.removeAll();
		desc = addressPropertySource.getPropertyDescriptors();
		for (int i = 0; i < desc.length; i++) {
			String item = (String) addressPropertySource
					.getPropertyValue(desc[i].getId());

			if ((item != null) && !item.equals("")) {
				deliveryList.add(item);
			}
		}
		
		
		emailText.removeModifyListener(listener);
		if (AddressPropertySource.PROPERTY_EMAIL != null) {
			String valueValue = (String) addressPropertySource
					.getPropertyValue(AddressPropertySource.PROPERTY_EMAIL);
			if (valueValue != null)
				emailText.setText(valueValue);
		}
		emailText.addModifyListener(listener);
		
		cityText.removeModifyListener(listener);
		if (AddressPropertySource.PROPERTY_CITY != null) {
			String valueValue = (String) addressPropertySource
					.getPropertyValue(AddressPropertySource.PROPERTY_CITY);
			if (valueValue != null)
				cityText.setText(valueValue);
		}
		cityText.addModifyListener(listener);
		
		stateText.removeModifyListener(listener);
		if (AddressPropertySource.PROPERTY_STATE != null) {
			String valueValue = (String) addressPropertySource
					.getPropertyValue(AddressPropertySource.PROPERTY_STATE);
			if (valueValue != null)
				stateText.setText(valueValue);
		}
		stateText.addModifyListener(listener);
		
		countryText.removeModifyListener(listener);
		if (AddressPropertySource.PROPERTY_COUNTRY != null) {
			String valueValue = (String) addressPropertySource
					.getPropertyValue(AddressPropertySource.PROPERTY_COUNTRY);
			if (valueValue != null)
				countryText.setText(valueValue);
		}
		countryText.addModifyListener(listener);
		
		postText.removeModifyListener(listener);
		if (AddressPropertySource.PROPERTY_POSTAL != null) {
			String valueValue = (String) addressPropertySource
					.getPropertyValue(AddressPropertySource.PROPERTY_POSTAL);
			if (valueValue != null)
				postText.setText(valueValue);
		}
		postText.addModifyListener(listener);
	}
	
/*	private ControlDecoration createDecorator(Text text, String message) {
		ControlDecoration controlDecoration = new ControlDecoration(text,
				SWT.LEFT | SWT.TOP);
		controlDecoration.setDescriptionText(message);
		FieldDecoration fieldDecoration = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);
		controlDecoration.setImage(fieldDecoration.getImage());
		return controlDecoration;
	}

	private void bindValues() {
		// The DataBindingContext object will manage the databindings
		DataBindingContext bindingContext = new DataBindingContext();
		// First we bind the text field to the model
		// Here we define the UpdateValueStrategy
		UpdateValueStrategy update = new UpdateValueStrategy();
		NumberFormatConverter convert = new NumberFormatConverter(null,null);
		update.setAfterConvertValidator(new StringToIntegerValidator(convert));
		// Bind fistName
		bindingContext.bindValue(SWTObservables.observeText(phoneText,
				SWT.Modify), PojoObservables.observeValue(person, "firstName"),
				update, null);

		// Bind lastName
		bindingContext.bindValue(SWTObservables.observeText(lastName,
				SWT.Modify), PojoObservables.observeValue(person, "lastName"),
				new UpdateValueStrategy()
						.setAfterConvertValidator(new StringLongerThenTwo(
								MESSAGE, lastNameDecorator)), null);

		// We listen to all errors via this binding
		uiElement = SWTObservables.observeText(errorLabel);
		// This one listenes to all changes
		bindingContext.bindValue(uiElement, new AggregateValidationStatus(
				bindingContext.getBindings(),
				AggregateValidationStatus.MAX_SEVERITY), null, null);

		// Lets change the color of the field lastName
		uiElement.addChangeListener(new IChangeListener() {
			@Override
			public void handleChange(ChangeEvent event) {
				if (uiElement.getValue().equals("OK")) {
					phoneText.setBackground(Display.getCurrent().getSystemColor(
							SWT.COLOR_WHITE));

				} else {
					phoneText.setBackground(Display.getCurrent().getSystemColor(
							SWT.COLOR_RED));
				}
			}
		});
	}
*/
}
