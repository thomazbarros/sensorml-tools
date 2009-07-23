package org.vast.sensormleditor.properties.sections;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.vast.sensormleditor.properties.descriptors.SecurityConstraintPropertySource;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

public class SecurityConstraintSection extends AbstractPropertySection {
	private String headerLabel = "Security Constraint";
	private CLabel headerlabelLabel;

	private CCombo valueChoice;
	private String valueLabel = "Classification:";
	private CLabel valuelabelLabel;

	private CLabel ownerProducerlabelLabel;
	private String ownerProducerLabel = "Owner/Producer:";
	private Text ownerProducerText;

	private CLabel sciControlslabelLabel;
	private String sciControlsLabel = "SCI Controls:";
	private Text sciControlsText;

	private CLabel sarIdentifierlabelLabel;
	private String sarIdentifierLabel = "SAR Identifier:";
	private Text sarIdentifierText;

	private CLabel dissemControlslabelLabel;
	private String dissemControlsLabel = "Dissemination Controls:";
	private Text dissemControlsText;

	private CLabel fgiOpenlabelLabel;
	private String fgiOpenLabel = "FGI Source Open:";
	private Text fgiOpenText;

	private CLabel fgiProtectedlabelLabel;
	private String fgiProtectedLabel = "FGI Source Protected:";
	private Text fgiProtectedText;

	private CLabel releaseTolabelLabel;
	private String releaseToLabel = "Releasable To:";
	private Text releaseToText;

	private CLabel nonIClabelLabel;
	private String nonICLabel = "NON IC Markings:";
	private Text nonICText;

	private CLabel classBylabelLabel;
	private String classByLabel = "Classified By:";
	private Text classByText;

	private CLabel classReasonlabelLabel;
	private String classReasonLabel = "Classification Reason:";
	private Text classReasonText;

	private CLabel derivedFromlabelLabel;
	private String derivedFromLabel = "Derived From:";
	private Text derivedFromText;

	private CLabel deClassDatelabelLabel;
	private String deClassDateLabel = "Declass Date:";
	private Text deClassDateText;

	private CLabel deClassEventlabelLabel;
	private String deClassEventLabel = "Declass Event:";
	private Text deClassEventText;

	private CLabel deClassExceplabelLabel;
	private String deClassExcepLabel = "Declass Exception:";
	private Text deClassExcepText;
	
	private CLabel typeofExemplabelLabel;
	private String typeofExempLabel = "Type of Exempted Source:";
	private Text typeofExempText;
	
	private CLabel dateofExemplabelLabel;
	private String dateofExempLabel = "Date of Exempted Source:";
	private Text dateofExempText;
	
	private CLabel manReviewlabelLabel;
	private String manReviewLabel = "Declass Manual Review:";
	private Text manReviewText;

	private SMLTreeEditor smlEditor;
	private Element element;
	private DOMHelper dom;
	private TreeViewer treeViewer;
	private SecurityConstraintPropertySource securityPropertySource;
	private Font fontBold = new Font(PlatformUI.getWorkbench().getDisplay(),
			"Tahoma", 8, SWT.BOLD);

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

		if (element != null) {
			securityPropertySource = new SecurityConstraintPropertySource(
					element, dom, treeViewer, smlEditor);
		}
	}

	private ModifyListener listener = new ModifyListener() {
		public void modifyText(ModifyEvent arg0) {
			Object src = arg0.getSource();
			if (src == fgiProtectedText) {
				securityPropertySource
						.setPropertyValue(
								SecurityConstraintPropertySource.PROPERTY_FGISOURCEPROTECTED,
								fgiProtectedText.getText());
			} else if (src == ownerProducerText) {
				securityPropertySource
						.setPropertyValue(
								SecurityConstraintPropertySource.PROPERTY_OWNERPRODUCER,
								ownerProducerText.getText());
			} else if (src == classReasonText) {
				securityPropertySource
						.setPropertyValue(
								SecurityConstraintPropertySource.PROPERTY_CLASSFICATIONREASON,
								classReasonText.getText());
			} else if (src == sciControlsText) {
				securityPropertySource.setPropertyValue(
						SecurityConstraintPropertySource.PROPERTY_SCICONTROLS,
						sciControlsText.getText());
			} else if (src == sarIdentifierText) {
				securityPropertySource
						.setPropertyValue(
								SecurityConstraintPropertySource.PROPERTY_SARIDENTIFIER,
								sarIdentifierText.getText());
			} else if (src == fgiOpenText) {
				securityPropertySource
						.setPropertyValue(
								SecurityConstraintPropertySource.PROPERTY_FGISOURCEOPEN,
								fgiOpenText.getText());
			} else if (src == releaseToText) {
				securityPropertySource.setPropertyValue(
						SecurityConstraintPropertySource.PROPERTY_RELEASABLETO,
						releaseToText.getText());
			} else if (src == nonICText) {
				securityPropertySource
						.setPropertyValue(
								SecurityConstraintPropertySource.PROPERTY_NONICMARKINGS,
								nonICText.getText());
			} else if (src == classByText) {
				securityPropertySource.setPropertyValue(
						SecurityConstraintPropertySource.PROPERTY_CLASSIFIEDBY,
						classByText.getText());
			} else if (src == dissemControlsText) {
				securityPropertySource
						.setPropertyValue(
								SecurityConstraintPropertySource.PROPERTY_DISSEMINATIONCONTROLS,
								dissemControlsText.getText());
			} else if (src == derivedFromText) {
				securityPropertySource.setPropertyValue(
						SecurityConstraintPropertySource.PROPERTY_DERIVEDFROM,
						derivedFromText.getText());
			} else if (src == deClassDateText) {
				securityPropertySource.setPropertyValue(
						SecurityConstraintPropertySource.PROPERTY_DECLASSDATE,
						deClassDateText.getText());
			} else if (src == deClassEventText) {
				securityPropertySource.setPropertyValue(
						SecurityConstraintPropertySource.PROPERTY_DECLASSEVENT,
						deClassEventText.getText());
			} else if (src == typeofExempText) {
				securityPropertySource.setPropertyValue(
						SecurityConstraintPropertySource.PROPERTY_TYPEOFEXEMPTEDSOURCE,
						typeofExempText.getText());
			} else if (src == dateofExempText) {
				securityPropertySource.setPropertyValue(
						SecurityConstraintPropertySource.PROPERTY_DATEOFEXEMPTEDSOURCE,
						dateofExempText.getText());
			} else if (src == manReviewText) {
				securityPropertySource.setPropertyValue(
						SecurityConstraintPropertySource.PROPERTY_DECLASSMANUALREVIEW,
						manReviewText.getText());
			} else if (src == deClassExcepText) {
				securityPropertySource
						.setPropertyValue(
								SecurityConstraintPropertySource.PROPERTY_DECLASSEXCEPTION,
								deClassExcepText.getText());
			}
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

		valueChoice = getWidgetFactory().createCCombo(composite,
				SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(headerlabelLabel);
		valueChoice.setLayoutData(data);
		valueChoice.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				int index = valueChoice.getSelectionIndex();
				securityPropertySource
						.setPropertyValue(
								SecurityConstraintPropertySource.PROPERTY_CLASSIFICATION_CHOICE,
								valueChoice.getItem(index));

			}
		});

		valuelabelLabel = getWidgetFactory()
				.createCLabel(composite, valueLabel); //$NON-NLS-1$
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(valueChoice,
				ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(headerlabelLabel);
		valuelabelLabel.setLayoutData(data);

		ownerProducerText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(valueChoice);
		ownerProducerText.setLayoutData(data);
		ownerProducerText.addModifyListener(listener);

		ownerProducerlabelLabel = getWidgetFactory().createCLabel(composite,
				ownerProducerLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(ownerProducerText);
		data.top = new FormAttachment(valuelabelLabel);
		ownerProducerlabelLabel.setLayoutData(data);

		sciControlsText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(ownerProducerText);
		sciControlsText.setLayoutData(data);
		sciControlsText.addModifyListener(listener);

		sciControlslabelLabel = getWidgetFactory().createCLabel(composite,
				sciControlsLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(sciControlsText);
		data.top = new FormAttachment(ownerProducerText,
				ITabbedPropertyConstants.VSPACE);
		sciControlslabelLabel.setLayoutData(data);

		sarIdentifierText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(sciControlsText);
		sarIdentifierText.setLayoutData(data);
		sarIdentifierText.addModifyListener(listener);

		sarIdentifierlabelLabel = getWidgetFactory().createCLabel(composite,
				sarIdentifierLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(sarIdentifierText);
		data.top = new FormAttachment(sciControlsText);
		sarIdentifierlabelLabel.setLayoutData(data);

		dissemControlsText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(sarIdentifierText);
		dissemControlsText.setLayoutData(data);
		dissemControlsText.addModifyListener(listener);

		dissemControlslabelLabel = getWidgetFactory().createCLabel(composite,
				dissemControlsLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(dissemControlsText);
		data.top = new FormAttachment(sarIdentifierText);
		dissemControlslabelLabel.setLayoutData(data);

		fgiOpenText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(dissemControlsText);
		fgiOpenText.setLayoutData(data);

		fgiOpenlabelLabel = getWidgetFactory().createCLabel(composite,
				fgiOpenLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(fgiOpenText);
		data.top = new FormAttachment(dissemControlsText);
		fgiOpenlabelLabel.setLayoutData(data);

		fgiProtectedText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(fgiOpenText);
		fgiProtectedText.setLayoutData(data);
		fgiProtectedText.addModifyListener(listener);

		fgiProtectedlabelLabel = getWidgetFactory().createCLabel(composite,
				fgiProtectedLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(fgiProtectedText);
		data.top = new FormAttachment(fgiOpenText);
		fgiProtectedlabelLabel.setLayoutData(data);

		releaseToText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(fgiProtectedText);
		releaseToText.setLayoutData(data);
		releaseToText.addModifyListener(listener);

		releaseTolabelLabel = getWidgetFactory().createCLabel(composite,
				releaseToLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(releaseToText);
		data.top = new FormAttachment(fgiProtectedText);
		releaseTolabelLabel.setLayoutData(data);

		nonICText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(releaseToText);
		nonICText.setLayoutData(data);
		nonICText.addModifyListener(listener);

		nonIClabelLabel = getWidgetFactory()
				.createCLabel(composite, nonICLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(nonICText);
		data.top = new FormAttachment(releaseToText);
		nonIClabelLabel.setLayoutData(data);

		classByText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(nonICText);
		classByText.setLayoutData(data);
		classByText.addModifyListener(listener);

		classBylabelLabel = getWidgetFactory().createCLabel(composite,
				classByLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(classByText);
		data.top = new FormAttachment(nonICText);
		classBylabelLabel.setLayoutData(data);

		classReasonText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(classByText);
		classReasonText.setLayoutData(data);
		classReasonText.addModifyListener(listener);

		classReasonlabelLabel = getWidgetFactory().createCLabel(composite,
				classReasonLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(classReasonText);
		data.top = new FormAttachment(classByText);
		classReasonlabelLabel.setLayoutData(data);

		derivedFromText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(classReasonText);
		derivedFromText.setLayoutData(data);
		derivedFromText.addModifyListener(listener);

		derivedFromlabelLabel = getWidgetFactory().createCLabel(composite,
				derivedFromLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(derivedFromText);
		data.top = new FormAttachment(classReasonText);
		derivedFromlabelLabel.setLayoutData(data);

		deClassDateText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(derivedFromText);
		deClassDateText.setLayoutData(data);
		deClassDateText.addModifyListener(listener);

		deClassDatelabelLabel = getWidgetFactory().createCLabel(composite,
				deClassDateLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(deClassDateText);
		data.top = new FormAttachment(derivedFromText);
		deClassDatelabelLabel.setLayoutData(data);

		deClassEventText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(deClassDateText);
		deClassEventText.setLayoutData(data);
		deClassEventText.addModifyListener(listener);

		deClassEventlabelLabel = getWidgetFactory().createCLabel(composite,
				deClassEventLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(deClassEventText);
		data.top = new FormAttachment(deClassDateText);
		deClassEventlabelLabel.setLayoutData(data);

		deClassExcepText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(deClassEventText);
		deClassExcepText.setLayoutData(data);
		deClassExcepText.addModifyListener(listener);

		deClassExceplabelLabel = getWidgetFactory().createCLabel(composite,
				deClassExcepLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(deClassExcepText);
		data.top = new FormAttachment(deClassEventText);
		deClassExceplabelLabel.setLayoutData(data);
		
		typeofExempText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(deClassExcepText);
		typeofExempText.setLayoutData(data);
		typeofExempText.addModifyListener(listener);

		typeofExemplabelLabel = getWidgetFactory().createCLabel(composite,
				typeofExempLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(typeofExempText);
		data.top = new FormAttachment(deClassExcepText);
		typeofExemplabelLabel.setLayoutData(data);
		
		dateofExempText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(typeofExempText);
		dateofExempText.setLayoutData(data);
		dateofExempText.addModifyListener(listener);

		dateofExemplabelLabel = getWidgetFactory().createCLabel(composite,
				dateofExempLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(dateofExempText);
		data.top = new FormAttachment(typeofExempText);
		dateofExemplabelLabel.setLayoutData(data);
		
		manReviewText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(dateofExempText);
		manReviewText.setLayoutData(data);
		manReviewText.addModifyListener(listener);

		manReviewlabelLabel = getWidgetFactory().createCLabel(composite,
				manReviewLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(manReviewText);
		data.top = new FormAttachment(dateofExempText);
		manReviewlabelLabel.setLayoutData(data);

	}

	public void refresh() {
		valueChoice.removeAll();
		if (SecurityConstraintPropertySource.PROPERTY_CLASSIFICATION_CHOICE != null) {
			String[] choices = (String[]) securityPropertySource
					.getChoices(SecurityConstraintPropertySource.PROPERTY_CLASSIFICATION_CHOICE);
			for (int i = 0; i < choices.length; i++) {
				if (choices[i] != null)
					valueChoice.add(choices[i]);
			}
			String selected = (String) securityPropertySource
					.getPropertyValue(SecurityConstraintPropertySource.PROPERTY_CLASSIFICATION_CHOICE);
			int index = valueChoice.indexOf(selected);
			valueChoice.select(index);
		}

		ownerProducerText.removeModifyListener(listener);
		if (SecurityConstraintPropertySource.PROPERTY_OWNERPRODUCER != null) {
			String valueValue = (String) securityPropertySource
					.getPropertyValue(SecurityConstraintPropertySource.PROPERTY_OWNERPRODUCER);
			if (valueValue != null)
				ownerProducerText.setText(valueValue);
		}
		ownerProducerText.addModifyListener(listener);

		sciControlsText.removeModifyListener(listener);
		if (SecurityConstraintPropertySource.PROPERTY_SCICONTROLS != null) {
			String valueValue = (String) securityPropertySource
					.getPropertyValue(SecurityConstraintPropertySource.PROPERTY_SCICONTROLS);
			if (valueValue != null)
				sciControlsText.setText(valueValue);
		}
		sciControlsText.addModifyListener(listener);

		sarIdentifierText.removeModifyListener(listener);
		if (SecurityConstraintPropertySource.PROPERTY_SARIDENTIFIER != null) {
			String valueValue = (String) securityPropertySource
					.getPropertyValue(SecurityConstraintPropertySource.PROPERTY_SARIDENTIFIER);
			if (valueValue != null)
				sarIdentifierText.setText(valueValue);
		}
		sarIdentifierText.addModifyListener(listener);

		dissemControlsText.removeModifyListener(listener);
		if (SecurityConstraintPropertySource.PROPERTY_DISSEMINATIONCONTROLS != null) {
			String valueValue = (String) securityPropertySource
					.getPropertyValue(SecurityConstraintPropertySource.PROPERTY_DISSEMINATIONCONTROLS);
			if (valueValue != null)
				dissemControlsText.setText(valueValue);
		}
		dissemControlsText.addModifyListener(listener);

		fgiOpenText.removeModifyListener(listener);
		if (SecurityConstraintPropertySource.PROPERTY_FGISOURCEOPEN != null) {
			String valueValue = (String) securityPropertySource
					.getPropertyValue(SecurityConstraintPropertySource.PROPERTY_FGISOURCEOPEN);
			if (valueValue != null)
				fgiOpenText.setText(valueValue);
		}
		fgiOpenText.addModifyListener(listener);

		fgiProtectedText.removeModifyListener(listener);
		if (SecurityConstraintPropertySource.PROPERTY_FGISOURCEPROTECTED != null) {
			String valueValue = (String) securityPropertySource
					.getPropertyValue(SecurityConstraintPropertySource.PROPERTY_FGISOURCEPROTECTED);
			if (valueValue != null)
				fgiProtectedText.setText(valueValue);
		}
		fgiProtectedText.addModifyListener(listener);

		releaseToText.removeModifyListener(listener);
		if (SecurityConstraintPropertySource.PROPERTY_RELEASABLETO != null) {
			String valueValue = (String) securityPropertySource
					.getPropertyValue(SecurityConstraintPropertySource.PROPERTY_RELEASABLETO);
			if (valueValue != null)
				releaseToText.setText(valueValue);
		}
		releaseToText.addModifyListener(listener);

		nonICText.removeModifyListener(listener);
		if (SecurityConstraintPropertySource.PROPERTY_NONICMARKINGS != null) {
			String valueValue = (String) securityPropertySource
					.getPropertyValue(SecurityConstraintPropertySource.PROPERTY_NONICMARKINGS);
			if (valueValue != null)
				nonICText.setText(valueValue);
		}
		nonICText.addModifyListener(listener);

		classByText.removeModifyListener(listener);
		if (SecurityConstraintPropertySource.PROPERTY_CLASSIFIEDBY != null) {
			String valueValue = (String) securityPropertySource
					.getPropertyValue(SecurityConstraintPropertySource.PROPERTY_CLASSIFIEDBY);
			if (valueValue != null)
				classByText.setText(valueValue);
		}
		classByText.addModifyListener(listener);

		classReasonText.removeModifyListener(listener);
		if (SecurityConstraintPropertySource.PROPERTY_CLASSFICATIONREASON != null) {
			String valueValue = (String) securityPropertySource
					.getPropertyValue(SecurityConstraintPropertySource.PROPERTY_CLASSFICATIONREASON);
			if (valueValue != null)
				classReasonText.setText(valueValue);
		}
		classReasonText.addModifyListener(listener);

		derivedFromText.removeModifyListener(listener);
		if (SecurityConstraintPropertySource.PROPERTY_DERIVEDFROM != null) {
			String valueValue = (String) securityPropertySource
					.getPropertyValue(SecurityConstraintPropertySource.PROPERTY_DERIVEDFROM);
			if (valueValue != null)
				derivedFromText.setText(valueValue);
		}
		derivedFromText.addModifyListener(listener);

		deClassDateText.removeModifyListener(listener);
		if (SecurityConstraintPropertySource.PROPERTY_DECLASSDATE != null) {
			String valueValue = (String) securityPropertySource
					.getPropertyValue(SecurityConstraintPropertySource.PROPERTY_DECLASSDATE);
			if (valueValue != null)
				deClassDateText.setText(valueValue);
		}
		deClassDateText.addModifyListener(listener);

		deClassEventText.removeModifyListener(listener);
		if (SecurityConstraintPropertySource.PROPERTY_DECLASSEVENT != null) {
			String valueValue = (String) securityPropertySource
					.getPropertyValue(SecurityConstraintPropertySource.PROPERTY_DECLASSEVENT);
			if (valueValue != null)
				deClassEventText.setText(valueValue);
		}
		deClassEventText.addModifyListener(listener);

		deClassExcepText.removeModifyListener(listener);
		if (SecurityConstraintPropertySource.PROPERTY_DECLASSEXCEPTION != null) {
			String valueValue = (String) securityPropertySource
					.getPropertyValue(SecurityConstraintPropertySource.PROPERTY_DECLASSEXCEPTION);
			if (valueValue != null)
				deClassExcepText.setText(valueValue);
		}
		deClassExcepText.addModifyListener(listener);
		
		typeofExempText.removeModifyListener(listener);
		if (SecurityConstraintPropertySource.PROPERTY_TYPEOFEXEMPTEDSOURCE != null) {
			String valueValue = (String) securityPropertySource
					.getPropertyValue(SecurityConstraintPropertySource.PROPERTY_TYPEOFEXEMPTEDSOURCE);
			if (valueValue != null)
				typeofExempText.setText(valueValue);
		}
		typeofExempText.addModifyListener(listener);
		
		dateofExempText.removeModifyListener(listener);
		if (SecurityConstraintPropertySource.PROPERTY_DATEOFEXEMPTEDSOURCE != null) {
			String valueValue = (String) securityPropertySource
					.getPropertyValue(SecurityConstraintPropertySource.PROPERTY_DATEOFEXEMPTEDSOURCE);
			if (valueValue != null)
				dateofExempText.setText(valueValue);
		}
		dateofExempText.addModifyListener(listener);
		
		manReviewText.removeModifyListener(listener);
		if (SecurityConstraintPropertySource.PROPERTY_DECLASSMANUALREVIEW != null) {
			String valueValue = (String) securityPropertySource
					.getPropertyValue(SecurityConstraintPropertySource.PROPERTY_DECLASSMANUALREVIEW);
			if (valueValue != null)
				manReviewText.setText(valueValue);
		}
		manReviewText.addModifyListener(listener);



	}

}
