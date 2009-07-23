/***************************** BEGIN LICENSE BLOCK ***************************

 The contents of this file are subject to the Mozilla Public License Version
 1.1 (the "License"); you may not use this file except in compliance with
 the License. You may obtain a copy of the License at
 http://www.mozilla.org/MPL/MPL-1.1.html
 
 Software distributed under the License is distributed on an "AS IS" basis,
 WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 for the specific language governing rights and limitations under the License.
 
 The Original Code is the "SensorML DataProcessing Engine".
 
 The Initial Developer of the Original Code is the
 University of Alabama in Huntsville (UAH).
 Portions created by the Initial Developer are Copyright (C) 2006
 the Initial Developer. All Rights Reserved.
 
 Contributor(s): 
    Alexandre Robin <robin@nsstc.uah.edu>
 
******************************* END LICENSE BLOCK ***************************/

package org.vast.sensormleditor.relaxNG;

import org.vast.xml.DOMHelper;
import org.vast.xml.DOMHelperException;
import org.vast.xml.QName;
import org.vast.xml.XMLDocument;
import org.vast.xml.XMLFragment;
import org.vast.xml.transform.DOMTransform;
import org.vast.xml.transform.DOMTransformException;
import org.vast.xml.transform.TransformTemplate;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Relax2Hybrid extends DOMTransform
{
    String rngURI = "http://relaxng.org/ns/structure/1.0";
    String rngaURI = "http://relaxng.org/ns/compatibility/annotations/1.0";
    String smlURI = "http://www.opengis.net/sensorML/1.0.1";
    String sweURI = "http://www.opengis.net/swe/1.0.1";
    String gmlURI = "http://www.opengis.net/gml";
    String xlinkURI = "http://www.w3.org/1999/xlink";
    
    
    
    public Relax2Hybrid()
    {
        super();
        TransformTemplate template;
        
        
        /////////////////
        // rng:grammar //
        /////////////////
        template = new TransformTemplate()
        {
            public boolean isMatch(DOMTransform transform, Node srcNode, Node resultNode) throws DOMTransformException
            {
                if (isElementQName(srcNode, "rng:grammar"))
                    return true;
                
                return false;
            }
            
            public void apply(DOMTransform transform, Node srcNode, Node resultNode) throws DOMTransformException
            {
                NodeList elts = srcDom.getDocument().getElementsByTagNameNS(rngURI, "start");
                if (elts.getLength() > 0)
                    transform.applyTemplatesToChildElements(elts.item(0), resultNode);
            }
        };
        this.templates.add(template);
        
        
        /////////////////
        // rng:element //
        /////////////////
        template = new TransformTemplate()
        {
            public boolean isMatch(DOMTransform transform, Node srcNode, Node resultNode) throws DOMTransformException
            {
                if (isElementQName(srcNode, "rng:element"))
                    return true;
                
                return false;
            }
            
            public void apply(DOMTransform transform, Node srcNode, Node resultNode) throws DOMTransformException
            {
                String eltName = srcDom.getAttributeValue((Element)srcNode, "@name");
                QName qName = getResultQName(eltName);
                Element newElt = resDom.addElement(resultNode, "+" + qName.getFullName());
                
                transform.applyTemplatesToChildElements(srcNode, newElt);
            }
        };
        this.templates.add(template);
        
        
        //////////////////////////////////
        // rng:attribute with rng:value //
        //////////////////////////////////
        template = new TransformTemplate()
        {
            public boolean isMatch(DOMTransform transform, Node srcNode, Node resultNode) throws DOMTransformException
            {
                if (isElementQName(srcNode, "rng:attribute") && 
                    isElementQName(srcNode.getParentNode(), "rng:element") &&
                    srcDom.existElement((Element)srcNode, "rng:value"))
                    return true;
                
                return false;
            }
            
            public void apply(DOMTransform transform, Node srcNode, Node resultNode) throws DOMTransformException
            {
                String attValue = srcDom.getElementValue((Element)srcNode, "rng:value");
                String attName = srcDom.getAttributeValue((Element)srcNode, "@name");
                QName qName = getResultQName(attName);                
                ((Element)resultNode).setAttribute(qName.getFullName(), attValue);
            }
        };
        this.templates.add(template);
        
        
        ////////////////////////////////////////////////////////////////////////////////////////////////////
        // rng:ref[not(parent::rng:optional) and not(parent::rng:zeroOrMore) and not(parent::rng:choice)] //
        ////////////////////////////////////////////////////////////////////////////////////////////////////
        template = new TransformTemplate()
        {
            public boolean isMatch(DOMTransform transform, Node srcNode, Node resultNode) throws DOMTransformException
            {
                Node parent = srcNode.getParentNode();
                if (isElementQName(srcNode, "rng:ref") &&
                    !srcDom.hasQName(parent, "rng:optional") &&
                    !srcDom.hasQName(parent, "rng:choice") &&
                    !srcDom.hasQName(parent, "rng:zeroOrMore"))
                    return true;
                
                return false;
            }
            
            public void apply(DOMTransform transform, Node srcNode, Node resultNode) throws DOMTransformException
            {
                String refName = srcDom.getAttributeValue((Element)srcNode, "@name");
                Element defineElt = findDefineElt(srcDom.getXmlDocument(), refName);
                
                if (defineElt != null)
                    transform.applyTemplatesToChildElements(defineElt, resultNode);
            }
        };
        this.templates.add(template);
        
        
        ///////////////////////////////////////////////
        // rng:data[swe:param[@name = 'dictionary']] //
        ///////////////////////////////////////////////
        template = new TransformTemplate()
        {
            public boolean isMatch(DOMTransform transform, Node srcNode, Node resultNode) throws DOMTransformException
            {
                String paramPath = "swe:param/@name";
                if (isElementQName(srcNode, "rng:data") &&
                    (srcDom.getAttributeValue((Element)srcNode, paramPath) != null) &&
                    (srcDom.getAttributeValue((Element)srcNode, paramPath).equalsIgnoreCase("dictionary")))
                    return true;
                
                return false;
            }
            
            public void apply(DOMTransform transform, Node srcNode, Node resultNode) throws DOMTransformException
            {
                String dicoPath = srcDom.getAttributeValue((Element)srcNode, "swe:param/@value");
                
                try
                {
                    XMLFragment dicoFragment = srcDom.getLinkedFragment(srcDom.getXmlDocument(), dicoPath);
                    NodeList entries = srcDom.getElements(dicoFragment.getBaseElement(), "dictionaryEntry");
                    
                    Element choiceElt = resDom.addElement(resultNode, "rng:choice");
                    for (int i=0; i<entries.getLength(); i++)
                    {
                        Element entry = (Element)entries.item(i);
                        String defName = srcDom.getElementValue(entry, "*/name");
                        Element newElt = resDom.addElement(choiceElt, "+rng:value");
                        newElt.setTextContent(defName);
                    }
                }
                catch (Exception e)
                {
                    throw new DOMTransformException("Dictionary " + dicoPath + " cannot be found");
                }
            }
        };
        this.templates.add(template);
        
        
        ////////////////////////
        // All other RNG Tags //
        ////////////////////////
        template = new TransformTemplate()
        {
            public boolean isMatch(DOMTransform transform, Node srcNode, Node resultNode) throws DOMTransformException
            {
                if (srcNode.getNamespaceURI() != null && srcNode.getNamespaceURI().equals(rngURI))
                    return true;
                
                return false;
            }
            
            public void apply(DOMTransform transform, Node srcNode, Node resultNode) throws DOMTransformException
            {
                Node newNode = resDom.getDocument().importNode(srcNode, false);
                newNode.setPrefix("rng");
                resultNode.appendChild(newNode);
                
                if (isElement(srcNode))
                {
                    String text = srcDom.getElementValue((Element)srcNode, "");
                    if (text != null && !text.equals(""))
                        resDom.setElementValue((Element)newNode, text);
                }
                
                transform.applyTemplatesToAttributes(srcNode, newNode);
                transform.applyTemplatesToChildElements(srcNode, newNode);
            }
        };
        this.templates.add(template);
        
        
        /////////////////////
        // All Annotations //
        /////////////////////
        template = new TransformTemplate()
        {
            public boolean isMatch(DOMTransform transform, Node srcNode, Node resultNode) throws DOMTransformException
            {
                if (srcNode.getNamespaceURI() != null && srcNode.getNamespaceURI().equals(rngaURI))
                    return true;
                
                return false;
            }
            
            public void apply(DOMTransform transform, Node srcNode, Node resultNode) throws DOMTransformException
            {
                Node newNode = resultNode.getOwnerDocument().importNode(srcNode, true);
                resultNode.appendChild(newNode);
            }
        };
        this.templates.add(template);
    }
    
    
    public void retrieveRelaxNGRef(Element refElt) throws DOMTransformException
    {
        String refName = resDom.getAttributeValue(refElt, "@name");
        Element defineElt = findDefineElt(srcDom.getXmlDocument(), refName);
        if (defineElt != null)
        {
            Node parentNode = refElt.getParentNode();
            parentNode.removeChild(refElt);
            applyTemplatesToChildElements(defineElt, parentNode);            
        }

    }
    
    
    protected Element findDefineElt(XMLDocument currentDoc, String refName) throws DOMTransformException
    {
        Element docElt = currentDoc.getDocumentElement();
        
        // look through local define elements
        NodeList defineElts = srcDom.getElements(docElt, "rng:define");
        for (int i=0; i<defineElts.getLength(); i++)
        {
            Element defineElt = (Element)defineElts.item(i);
            String defName = defineElt.getAttribute("name");
            if (refName.equals(defName))
                return defineElt;
        }
                
        // look in included content
        NodeList includeElts = srcDom.getElements(docElt, "rng:include");
        for (int i=0; i<includeElts.getLength(); i++)
        {
            Element includeElt = (Element)includeElts.item(i);
            
            // first look into overriden defines (inside the include)
            defineElts = srcDom.getElements(includeElt, "rng:define");
            for (int k=0; k<defineElts.getLength(); k++)
            {
                Element defineElt = (Element)defineElts.item(k);
                String defName = defineElt.getAttribute("name");
                if (refName.equals(defName))
                    return defineElt;
            }
            
            // now recursively look in included files
            String filePath = srcDom.getAttributeValue(includeElt, "href");            
            try
            {
                XMLFragment includedDoc = srcDom.getLinkedFragment(currentDoc, filePath);
                Element defElt = findDefineElt(includedDoc.getXmlDocument(), refName);
                if (defElt != null)
                    return defElt;
            }
            catch (DOMHelperException e)
            {
                throw new DOMTransformException("Included schema " + filePath + " cannot be found");
            }
        }        
        
        return null;
    }
    
    
    @Override
    public void init()
    {
        srcDom.addUserPrefix("rng", rngURI);
        srcDom.addUserPrefix("swe", sweURI);
        
        resDom.addUserPrefix("rng", rngURI);
        resDom.addUserPrefix("sml", smlURI);
        resDom.addUserPrefix("swe", sweURI);
        resDom.addUserPrefix("gml", gmlURI);
        resDom.addUserPrefix("xlink", xlinkURI);
        
        resDom.getXmlDocument().addNS("sml", smlURI);
        resDom.getXmlDocument().addNS("swe", sweURI);
        resDom.getXmlDocument().addNS("gml", gmlURI);
        resDom.getXmlDocument().addNS("xlink", xlinkURI);
        resDom.getXmlDocument().addNS("rng",rngURI);
        resDom.getXmlDocument().addNS("a",rngaURI);
    }
    
    
    public static void main(String[] args)
    {
        try
        {
            //DOMHelper sourceDoc = new DOMHelper("file:///C:/temp/workspace-SensorMLTools/SensorMLTools/RelaxNG_Profiles/eo-instrument.rng", false);
            DOMHelper sourceDoc = new DOMHelper("file:///C:/temp/workspace-SensorMLTools/RelaxNG-v1.0/sml/sensorML.rng", false);
            //DOMHelper sourceDoc = new DOMHelper("file:///D:/Sensia/Projects/SensorML/RelaxNG/sml/sensorML.rng", false);
            Relax2Hybrid transform1 = new Relax2Hybrid();
          
            DOMHelper result1 = transform1.transform(sourceDoc);            
            result1.serialize(result1.getDocument(), System.out, true);
            // set something to selected
            result1.addUserPrefix("xng", "http://xng.org/1.0");
            transform1.retrieveRelaxNGRef(result1.getElement("rng:oneOrMore/sml:member/rng:choice/rng:ref"));
            result1.serialize(result1.getDocument(), System.out, true);
            //result1.setAttributeValue("sml:member/sml:System/rng:optional/@xng:selected", "true");
            //result1.setElementValue("sml:member/sml:System/rng:optional/rng:attribute/rng:value", "MY_ID");
            //result1.setAttributeValue("sml:member/sml:System/sml:keywords/rng:choice/sml:KeywordList/@xng:selected", "true");
            
            //transform1.retrieveRelaxNGRef(result1.getElement("sml:member/sml:System/rng:optional/gml:name/rng:data"));
            //result1.setAttributeValue("sml:member/sml:System/rng:optional/@xng:selected", "true");
            //result1.setElementValue("sml:member/sml:System/rng:optional/gml:name/rng:data/rng:value", "MY_ID");
            //result1.setAttributeValue("sml:member/sml:System/sml:keywords/rng:choice/sml:KeywordList/@xng:selected", "true");
            //result1.serialize(result1.getDocument(), System.out, true);
            
         /*   Hybrid2XML transform2 = new Hybrid2XML();
            DOMHelper result2 = transform2.transform(result1);
            result1.serialize(result1.getDocument(), System.out, true);*/
            //result1.serialize(result1.getDocument(),System.out, true);
           // String filePath = new URL("file:///C:/temp/workspace-SensorMLTools/SensorMLTools/sensorMLGeneral.xml").getPath();
           // OutputStream os = new FileOutputStream(filePath);
            //result1.serialize(result1.getDocument(),os , true);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
