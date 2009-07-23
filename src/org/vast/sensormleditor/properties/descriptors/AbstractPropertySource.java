package org.vast.sensormleditor.properties.descriptors;


import org.eclipse.jface.action.Action;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.vast.sensormleditor.config.Configuration;
import org.vast.sensormleditor.editors.SMLTreeEditor;
import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.w3c.dom.Element;

public class AbstractPropertySource extends Action{

	protected Element ele;
	protected DOMHelper dom, srcDoc;
	protected TreeViewer treeViewer;
	protected SMLTreeEditor smlEditor;
	String coreSchema;
	String xngURI = "http://xng.org/1.0";
	String rngURI = "http://relaxng.org/ns/structure/1.0";
	String rngaURI = "http://relaxng.org/ns/compatibility/annotations/1.0";
	String smlURI = "http://www.opengis.net/sensorML/1.0.1";
	String sweURI = "http://www.opengis.net/swe/1.0.1";
	String gmlURI = "http://www.opengis.net/gml";
	protected IPropertyChangeListener propertyChangeListener;

	public AbstractPropertySource(Element element, DOMHelper domHelper,
			TreeViewer viewer, SMLTreeEditor editor) {
		coreSchema = Configuration.getSchema();
		try {
			srcDoc = new DOMHelper(coreSchema,false);
		} catch (DOMHelperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.ele = element;
		this.dom = domHelper;
		this.treeViewer = viewer;
		this.smlEditor = editor;
		propertyChangeListener = (IPropertyChangeListener) smlEditor.getPropertyListener();
		this.addPropertyChangeListener(propertyChangeListener);
		dom.addUserPrefix("rng", rngURI);
		dom.addUserPrefix("xng", xngURI);
		dom.addUserPrefix("a", rngaURI);
		dom.addUserPrefix("sml", smlURI);
		dom.addUserPrefix("swe", sweURI);
		dom.addUserPrefix("gml", gmlURI);
	}

}
