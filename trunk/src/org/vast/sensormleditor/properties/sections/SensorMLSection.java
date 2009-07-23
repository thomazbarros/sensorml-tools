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
import org.vast.sensormleditor.properties.descriptors.SensorMLPropertySource;
import org.vast.xml.DOMHelper;
import org.w3c.dom.Element;

public class SensorMLSection extends AbstractPropertySection {
	private String headerLabel = "sml:SensorML";
	private CLabel headerlabelLabel;

	private CLabel alabelLabel;
	private String aLabel = "xmlns:a:";
	private Text aText;

	private CLabel gmllabelLabel;
	private String gmlLabel = "xmlns:gml:";
	private Text gmlText;

	private CLabel rnglabelLabel;
	private String rngLabel = "xmlns:rng:";
	private Text rngText;

	private CLabel smllabelLabel;
	private String smlLabel = "xmlns:sml:";
	private Text smlText;

	private CLabel swelabelLabel;
	private String sweLabel = "xmlns:swe:";
	private Text sweText;

	private CLabel xlinklabelLabel;
	private String xlinkLabel = "xmlns:xlink:";
	private Text xlinkText;

	private CLabel xnglabelLabel;
	private String xngLabel = "xmlns:xng:";
	private Text xngText;

	private CLabel nslabelLabel;
	private String nsLabel = "xsi:ns:";
	private Text nsText;

	private CLabel schemalabelLabel;
	private String schemaLabel = "xsi:schemaLocation:";
	private Text schemaText;

	private SMLTreeEditor smlEditor;
	private Element element;
	private DOMHelper dom;
	private TreeViewer treeViewer;
	private SensorMLPropertySource smlPropertySource;
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
			smlPropertySource = new SensorMLPropertySource(element, dom,
					treeViewer, smlEditor);
		}
	}

	private ModifyListener listener = new ModifyListener() {
		public void modifyText(ModifyEvent arg0) {
			Object src = arg0.getSource();
			if (src == xlinkText) {
				smlPropertySource.setPropertyValue(
						SensorMLPropertySource.PROPERTY_XLINK, xlinkText
								.getText());
			} else if (src == aText) {
				smlPropertySource.setPropertyValue(
						SensorMLPropertySource.PROPERTY_A, aText.getText());

			} else if (src == gmlText) {
				smlPropertySource.setPropertyValue(
						SensorMLPropertySource.PROPERTY_GML, gmlText.getText());

			} else if (src == rngText) {
				smlPropertySource.setPropertyValue(
						SensorMLPropertySource.PROPERTY_RNG, rngText.getText());
			} else if (src == sweText) {
				smlPropertySource.setPropertyValue(
						SensorMLPropertySource.PROPERTY_SWE, sweText.getText());
			} else if (src == xngText) {
				smlPropertySource.setPropertyValue(
						SensorMLPropertySource.PROPERTY_XNG, xngText.getText());

			} else if (src == smlText) {
				smlPropertySource.setPropertyValue(
						SensorMLPropertySource.PROPERTY_SML, smlText.getText());

			} else if (src == schemaText) {
				smlPropertySource.setPropertyValue(
						SensorMLPropertySource.PROPERTY_SCHEMA_LOCATION, schemaText.getText());

			} else if (src == nsText) {
				smlPropertySource.setPropertyValue(
						SensorMLPropertySource.PROPERTY_NS, nsText.getText());
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

		aText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(headerlabelLabel);
		aText.setLayoutData(data);
		aText.addModifyListener(listener);

		alabelLabel = getWidgetFactory().createCLabel(composite, aLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(aText);
		data.top = new FormAttachment(headerlabelLabel);
		alabelLabel.setLayoutData(data);

		gmlText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(aText);
		gmlText.setLayoutData(data);
		gmlText.addModifyListener(listener);

		gmllabelLabel = getWidgetFactory().createCLabel(composite, gmlLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(gmlText);
		data.top = new FormAttachment(aText, ITabbedPropertyConstants.VSPACE);
		gmllabelLabel.setLayoutData(data);

		rngText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(gmlText);
		rngText.setLayoutData(data);
		rngText.addModifyListener(listener);

		rnglabelLabel = getWidgetFactory().createCLabel(composite, rngLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(rngText);
		data.top = new FormAttachment(gmlText);
		rnglabelLabel.setLayoutData(data);

		smlText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(rngText);
		smlText.setLayoutData(data);
		smlText.addModifyListener(listener);

		smllabelLabel = getWidgetFactory().createCLabel(composite, smlLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(smlText);
		data.top = new FormAttachment(rngText);
		smllabelLabel.setLayoutData(data);

		sweText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(smlText);
		sweText.setLayoutData(data);

		swelabelLabel = getWidgetFactory().createCLabel(composite, sweLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(sweText);
		data.top = new FormAttachment(smlText);
		swelabelLabel.setLayoutData(data);

		xlinkText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(sweText);
		xlinkText.setLayoutData(data);
		xlinkText.addModifyListener(listener);

		xlinklabelLabel = getWidgetFactory()
				.createCLabel(composite, xlinkLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(xlinkText);
		data.top = new FormAttachment(sweText);
		xlinklabelLabel.setLayoutData(data);

		xngText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(xlinkText);
		xngText.setLayoutData(data);
		xngText.addModifyListener(listener);

		xnglabelLabel = getWidgetFactory().createCLabel(composite, xngLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(xngText);
		data.top = new FormAttachment(xlinkText);
		xnglabelLabel.setLayoutData(data);
		
		nsText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(xngText);
		nsText.setLayoutData(data);
		nsText.addModifyListener(listener);

		nslabelLabel = getWidgetFactory().createCLabel(composite, nsLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(nsText);
		data.top = new FormAttachment(xngText);
		nslabelLabel.setLayoutData(data);
		
		schemaText = getWidgetFactory().createText(composite, "");
		data = new FormData();
		data.left = new FormAttachment(0, 150);
		data.right = new FormAttachment(50, ITabbedPropertyConstants.HSPACE);
		data.top = new FormAttachment(nsText);
		schemaText.setLayoutData(data);
		schemaText.addModifyListener(listener);

		schemalabelLabel = getWidgetFactory().createCLabel(composite, schemaLabel);
		data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(schemaText);
		data.top = new FormAttachment(nsText);
		schemalabelLabel.setLayoutData(data);

	}

	public void refresh() {

		aText.removeModifyListener(listener);
		if (SensorMLPropertySource.PROPERTY_A != null) {
			String valueValue = (String) smlPropertySource
					.getPropertyValue(SensorMLPropertySource.PROPERTY_A);
			if (valueValue != null)
				aText.setText(valueValue);
		}
		aText.addModifyListener(listener);

		gmlText.removeModifyListener(listener);
		if (SensorMLPropertySource.PROPERTY_GML != null) {
			String valueValue = (String) smlPropertySource
					.getPropertyValue(SensorMLPropertySource.PROPERTY_GML);
			if (valueValue != null)
				gmlText.setText(valueValue);
		}
		gmlText.addModifyListener(listener);

		rngText.removeModifyListener(listener);
		if (SensorMLPropertySource.PROPERTY_RNG != null) {
			String valueValue = (String) smlPropertySource
					.getPropertyValue(SensorMLPropertySource.PROPERTY_RNG);
			if (valueValue != null)
				rngText.setText(valueValue);
		}
		rngText.addModifyListener(listener);

		smlText.removeModifyListener(listener);
		if (SensorMLPropertySource.PROPERTY_SML != null) {
			String valueValue = (String) smlPropertySource
					.getPropertyValue(SensorMLPropertySource.PROPERTY_SML);
			if (valueValue != null)
				smlText.setText(valueValue);
		}
		smlText.addModifyListener(listener);

		sweText.removeModifyListener(listener);
		if (SensorMLPropertySource.PROPERTY_SWE != null) {
			String valueValue = (String) smlPropertySource
					.getPropertyValue(SensorMLPropertySource.PROPERTY_SWE);
			if (valueValue != null)
				sweText.setText(valueValue);
		}
		sweText.addModifyListener(listener);

		xlinkText.removeModifyListener(listener);
		if (SensorMLPropertySource.PROPERTY_XLINK != null) {
			String valueValue = (String) smlPropertySource
					.getPropertyValue(SensorMLPropertySource.PROPERTY_XLINK);
			if (valueValue != null)
				xlinkText.setText(valueValue);
		}
		xlinkText.addModifyListener(listener);

		xngText.removeModifyListener(listener);
		if (SensorMLPropertySource.PROPERTY_XNG != null) {
			String valueValue = (String) smlPropertySource
					.getPropertyValue(SensorMLPropertySource.PROPERTY_XNG);
			if (valueValue != null)
				xngText.setText(valueValue);
		}
		xngText.addModifyListener(listener);
		
		nsText.removeModifyListener(listener);
		if (SensorMLPropertySource.PROPERTY_NS != null) {
			String valueValue = (String) smlPropertySource
					.getPropertyValue(SensorMLPropertySource.PROPERTY_NS);
			if (valueValue != null)
				nsText.setText(valueValue);
		}
		nsText.addModifyListener(listener);
		
		schemaText.removeModifyListener(listener);
		if (SensorMLPropertySource.PROPERTY_SCHEMA_LOCATION != null) {
			String valueValue = (String) smlPropertySource
					.getPropertyValue(SensorMLPropertySource.PROPERTY_SCHEMA_LOCATION);
			if (valueValue != null)
				schemaText.setText(valueValue);
		}
		schemaText.addModifyListener(listener);
	}
}
