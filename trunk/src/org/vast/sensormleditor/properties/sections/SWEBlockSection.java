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
import org.vast.sensormleditor.properties.descriptors.SWEBlockPropertySource;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

public class SWEBlockSection extends AbstractPropertySection {
	private String headerLabel = "swe:Block";
	private CLabel headerlabelLabel;
	
	private CLabel reflabelLabel;
	private String refLabel = "ref:";
	private Text refText;
	
	private CLabel byteLengthlabelLabel;
	private String byteLengthLabel = "byteLength:";
	private Text byteLengthText;
	
	private CLabel padBeforelabelLabel;
	private String padBeforeLabel = "paddingBytes-before:";
	private Text padBeforeText;

	private CLabel padAfterlabelLabel;
	private String padAfterLabel = "paddingBytes-after:";
	private Text padAfterText;

	private CLabel encryptlabelLabel;
	private String encryptLabel = "encryption:";
	private Text encryptText;
	
	private CLabel compressionlabelLabel;
	private String compressionLabel = "compression:";
	private Text compressionText;


	private SMLTreeEditor smlEditor;
	private Element element;
	private DOMHelper dom;
	private TreeViewer treeViewer;
	private SWEBlockPropertySource swePropertySource;
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
			swePropertySource = new SWEBlockPropertySource(
					element, dom, treeViewer, smlEditor);
		}
	}

	private ModifyListener listener = new ModifyListener() {
		public void modifyText(ModifyEvent arg0) {
			Object src = arg0.getSource();
			if (src == padAfterText) {
				swePropertySource
						.setPropertyValue(
								SWEBlockPropertySource.PROPERTY_PADDING_AFTER,
								padAfterText.getText());
			} else if (src == refText) {
				swePropertySource
						.setPropertyValue(
								SWEBlockPropertySource.PROPERTY_REF,
								refText.getText());
			
			} else if (src == compressionText) {
				swePropertySource.setPropertyValue(
						SWEBlockPropertySource.PROPERTY_COMPRESSION,
						compressionText.getText());
				
			} else if (src == padBeforeText) {
				swePropertySource
						.setPropertyValue(
								SWEBlockPropertySource.PROPERTY_PADDING_BEFORE,
								padBeforeText.getText());
			} else if (src == encryptText) {
				swePropertySource.setPropertyValue(
						SWEBlockPropertySource.PROPERTY_ENCRYPTION,
						encryptText.getText());
			
			
			} else if (src == byteLengthText) {
				swePropertySource
						.setPropertyValue(
								SWEBlockPropertySource.PROPERTY_BYTE_LENGTH,
								byteLengthText.getText());
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

		refText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(headerlabelLabel);
		refText.setLayoutData(data);
		refText.addModifyListener(listener);

		reflabelLabel = getWidgetFactory().createCLabel(composite,
				refLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(refText);
		data.top = new FormAttachment(headerlabelLabel);
		reflabelLabel.setLayoutData(data);

	

		byteLengthText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(refText);
		byteLengthText.setLayoutData(data);
		byteLengthText.addModifyListener(listener);

		byteLengthlabelLabel = getWidgetFactory().createCLabel(composite,
				byteLengthLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(byteLengthText);
		data.top = new FormAttachment(refText);
		byteLengthlabelLabel.setLayoutData(data);

		padBeforeText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(byteLengthText);
		padBeforeText.setLayoutData(data);

		padBeforelabelLabel = getWidgetFactory().createCLabel(composite,
				padBeforeLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(padBeforeText);
		data.top = new FormAttachment(byteLengthText);
		padBeforelabelLabel.setLayoutData(data);

		padAfterText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(padBeforeText);
		padAfterText.setLayoutData(data);
		padAfterText.addModifyListener(listener);

		padAfterlabelLabel = getWidgetFactory().createCLabel(composite,
				padAfterLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(padAfterText);
		data.top = new FormAttachment(padBeforeText);
		padAfterlabelLabel.setLayoutData(data);

		encryptText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(padAfterText);
		encryptText.setLayoutData(data);
		encryptText.addModifyListener(listener);

		encryptlabelLabel = getWidgetFactory().createCLabel(composite,
				encryptLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(encryptText);
		data.top = new FormAttachment(padAfterText);
		encryptlabelLabel.setLayoutData(data);
		
		compressionText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(encryptText);
		compressionText.setLayoutData(data);
		compressionText.addModifyListener(listener);

		compressionlabelLabel = getWidgetFactory().createCLabel(composite,
				compressionLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(compressionText);
		data.top = new FormAttachment(encryptText,
				ITabbedPropertyConstants.VSPACE);
		compressionlabelLabel.setLayoutData(data);

	}

	public void refresh() {
		

		refText.removeModifyListener(listener);
		if (SWEBlockPropertySource.PROPERTY_REF != null) {
			String valueValue = (String) swePropertySource
					.getPropertyValue(SWEBlockPropertySource.PROPERTY_REF);
			if (valueValue != null)
				refText.setText(valueValue);
		}
		refText.addModifyListener(listener);

		compressionText.removeModifyListener(listener);
		if (SWEBlockPropertySource.PROPERTY_COMPRESSION != null) {
			String valueValue = (String) swePropertySource
					.getPropertyValue(SWEBlockPropertySource.PROPERTY_COMPRESSION
							);
			if (valueValue != null)
				compressionText.setText(valueValue);
		}
		compressionText.addModifyListener(listener);

		byteLengthText.removeModifyListener(listener);
		if (SWEBlockPropertySource.PROPERTY_BYTE_LENGTH != null) {
			String valueValue = (String) swePropertySource
					.getPropertyValue(SWEBlockPropertySource.PROPERTY_BYTE_LENGTH);
			if (valueValue != null)
				byteLengthText.setText(valueValue);
		}
		byteLengthText.addModifyListener(listener);

		padBeforeText.removeModifyListener(listener);
		if (SWEBlockPropertySource.PROPERTY_PADDING_BEFORE != null) {
			String valueValue = (String) swePropertySource
					.getPropertyValue(SWEBlockPropertySource.PROPERTY_PADDING_BEFORE);
			if (valueValue != null)
				padBeforeText.setText(valueValue);
		}
		padBeforeText.addModifyListener(listener);

		padAfterText.removeModifyListener(listener);
		if (SWEBlockPropertySource.PROPERTY_PADDING_AFTER != null) {
			String valueValue = (String) swePropertySource
					.getPropertyValue(SWEBlockPropertySource.PROPERTY_PADDING_AFTER);
			if (valueValue != null)
				padAfterText.setText(valueValue);
		}
		padAfterText.addModifyListener(listener);

		encryptText.removeModifyListener(listener);
		if (SWEBlockPropertySource.PROPERTY_ENCRYPTION != null) {
			String valueValue = (String) swePropertySource
					.getPropertyValue(SWEBlockPropertySource.PROPERTY_ENCRYPTION);
			if (valueValue != null)
				encryptText.setText(valueValue);
		}
		encryptText.addModifyListener(listener);
	}
}
