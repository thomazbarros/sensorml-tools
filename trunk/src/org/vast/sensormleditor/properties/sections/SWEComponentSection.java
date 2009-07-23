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
import org.vast.sensormleditor.properties.descriptors.SWEComponentPropertySource;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

public class SWEComponentSection extends AbstractPropertySection {
	private String headerLabel = "swe:Component";
	private CLabel headerlabelLabel;
	
	private CLabel reflabelLabel;
	private String refLabel = "ref:";
	private Text refText;

	private CLabel dataTypelabelLabel;
	private String dataTypeLabel = "dataType:";
	private Text dataTypeText;

	private CLabel sigBitslabelLabel;
	private String sigBitsLabel = "significantBits:";
	private Text sigBitsText;

	private CLabel bitLengthlabelLabel;
	private String bitLengthLabel = "bitLength:";
	private Text bitLengthText;

	private CLabel padBeforelabelLabel;
	private String padBeforeLabel = "paddingBits-before:";
	private Text padBeforeText;

	private CLabel padAfterlabelLabel;
	private String padAfterLabel = "paddingBits-after:";
	private Text padAfterText;

	private CLabel encryptlabelLabel;
	private String encryptLabel = "encryption:";
	private Text encryptText;

	private SMLTreeEditor smlEditor;
	private Element element;
	private DOMHelper dom;
	private TreeViewer treeViewer;
	private SWEComponentPropertySource swePropertySource;
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
			swePropertySource = new SWEComponentPropertySource(
					element, dom, treeViewer, smlEditor);
		}
	}

	private ModifyListener listener = new ModifyListener() {
		public void modifyText(ModifyEvent arg0) {
			Object src = arg0.getSource();
			if (src == padAfterText) {
				swePropertySource
						.setPropertyValue(
								SWEComponentPropertySource.PROPERTY_PADDING_AFTER,
								padAfterText.getText());
			} else if (src == refText) {
				swePropertySource
						.setPropertyValue(
								SWEComponentPropertySource.PROPERTY_REF,
								refText.getText());
			
			} else if (src == dataTypeText) {
				swePropertySource.setPropertyValue(
						SWEComponentPropertySource.PROPERTY_DATA_TYPE,
						dataTypeText.getText());
				
			} else if (src == sigBitsText) {
				swePropertySource
						.setPropertyValue(
								SWEComponentPropertySource.PROPERTY_SIGNIFICANT_BITS,
								sigBitsText.getText());
			} else if (src == padBeforeText) {
				swePropertySource
						.setPropertyValue(
								SWEComponentPropertySource.PROPERTY_PADDING_BEFORE,
								padBeforeText.getText());
			} else if (src == encryptText) {
				swePropertySource.setPropertyValue(
						SWEComponentPropertySource.PROPERTY_ENCRYPTION,
						encryptText.getText());
			
			
			} else if (src == bitLengthText) {
				swePropertySource
						.setPropertyValue(
								SWEComponentPropertySource.PROPERTY_BIT_LENGTH,
								bitLengthText.getText());
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

		dataTypeText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(refText);
		dataTypeText.setLayoutData(data);
		dataTypeText.addModifyListener(listener);

		dataTypelabelLabel = getWidgetFactory().createCLabel(composite,
				dataTypeLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(dataTypeText);
		data.top = new FormAttachment(refText,
				ITabbedPropertyConstants.VSPACE);
		dataTypelabelLabel.setLayoutData(data);

		sigBitsText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(dataTypeText);
		sigBitsText.setLayoutData(data);
		sigBitsText.addModifyListener(listener);

		sigBitslabelLabel = getWidgetFactory().createCLabel(composite,
				sigBitsLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(sigBitsText);
		data.top = new FormAttachment(dataTypeText);
		sigBitslabelLabel.setLayoutData(data);

		bitLengthText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(sigBitsText);
		bitLengthText.setLayoutData(data);
		bitLengthText.addModifyListener(listener);

		bitLengthlabelLabel = getWidgetFactory().createCLabel(composite,
				bitLengthLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(bitLengthText);
		data.top = new FormAttachment(sigBitsText);
		bitLengthlabelLabel.setLayoutData(data);

		padBeforeText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(bitLengthText);
		padBeforeText.setLayoutData(data);

		padBeforelabelLabel = getWidgetFactory().createCLabel(composite,
				padBeforeLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(padBeforeText);
		data.top = new FormAttachment(bitLengthText);
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

	}

	public void refresh() {
		

		refText.removeModifyListener(listener);
		if (SWEComponentPropertySource.PROPERTY_REF != null) {
			String valueValue = (String) swePropertySource
					.getPropertyValue(SWEComponentPropertySource.PROPERTY_REF);
			if (valueValue != null)
				refText.setText(valueValue);
		}
		refText.addModifyListener(listener);

		dataTypeText.removeModifyListener(listener);
		if (SWEComponentPropertySource.PROPERTY_DATA_TYPE != null) {
			String valueValue = (String) swePropertySource
					.getPropertyValue(SWEComponentPropertySource.PROPERTY_DATA_TYPE
							);
			if (valueValue != null)
				dataTypeText.setText(valueValue);
		}
		dataTypeText.addModifyListener(listener);

		sigBitsText.removeModifyListener(listener);
		if (SWEComponentPropertySource.PROPERTY_SIGNIFICANT_BITS != null) {
			String valueValue = (String) swePropertySource
					.getPropertyValue(SWEComponentPropertySource.PROPERTY_SIGNIFICANT_BITS);
			if (valueValue != null)
				sigBitsText.setText(valueValue);
		}
		sigBitsText.addModifyListener(listener);

		bitLengthText.removeModifyListener(listener);
		if (SWEComponentPropertySource.PROPERTY_BIT_LENGTH != null) {
			String valueValue = (String) swePropertySource
					.getPropertyValue(SWEComponentPropertySource.PROPERTY_BIT_LENGTH);
			if (valueValue != null)
				bitLengthText.setText(valueValue);
		}
		bitLengthText.addModifyListener(listener);

		padBeforeText.removeModifyListener(listener);
		if (SWEComponentPropertySource.PROPERTY_PADDING_BEFORE != null) {
			String valueValue = (String) swePropertySource
					.getPropertyValue(SWEComponentPropertySource.PROPERTY_PADDING_BEFORE);
			if (valueValue != null)
				padBeforeText.setText(valueValue);
		}
		padBeforeText.addModifyListener(listener);

		padAfterText.removeModifyListener(listener);
		if (SWEComponentPropertySource.PROPERTY_PADDING_AFTER != null) {
			String valueValue = (String) swePropertySource
					.getPropertyValue(SWEComponentPropertySource.PROPERTY_PADDING_AFTER);
			if (valueValue != null)
				padAfterText.setText(valueValue);
		}
		padAfterText.addModifyListener(listener);

		encryptText.removeModifyListener(listener);
		if (SWEComponentPropertySource.PROPERTY_ENCRYPTION != null) {
			String valueValue = (String) swePropertySource
					.getPropertyValue(SWEComponentPropertySource.PROPERTY_ENCRYPTION);
			if (valueValue != null)
				encryptText.setText(valueValue);
		}
		encryptText.addModifyListener(listener);
	}
}
