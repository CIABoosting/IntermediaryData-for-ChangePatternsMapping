package org.jbpm.ui.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.jbpm.ui.DesignerLogger;
import org.jbpm.ui.PluginConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class MappingContentProvider {

	public static final MappingContentProvider INSTANCE = new MappingContentProvider();

	private static final String MAPPING_XML_FILE_NAME = "mapping.xml";

	private static final String MAPPING_ELEMENTS_NAME = "typesMapping";

	private static final String MAPPING_ELEMENT_NAME = "mapping";

	private static final String NAME_ATTRIBUTE_NAME = "name";

	private static final String FORMAT_ATTRIBUTE_NAME = "format";

	public void saveToInput() {
		try {
			IFile[] mappingFiles = getMappingFiles();
			for (int i = 0; i < mappingFiles.length; i++) {
				IFile mappingFile = mappingFiles[i];
				InputStream content = toMappingXml();
				if (!mappingFile.exists()) {
					mappingFile.create(content, true, null);
				} else {
					mappingFile.setContents(content, true, true, null);
				}
			}
		} catch (CoreException e) {
			DesignerLogger.logError(e);
		}
	}

	private InputStream toMappingXml() {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		writeMapping(os);
		return new ByteArrayInputStream(os.toByteArray());
	}

	private void writeMapping(OutputStream outputStream) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setEntityResolver(ProcessDefinitionEntityResolver
					.getInstance());
			Document document = builder.newDocument();
			Element root = document.createElement(MAPPING_ELEMENTS_NAME);
			document.appendChild(root);
			root.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI,
					XMLConstants.XMLNS_ATTRIBUTE, "http://runa.ru/xml");
			root.setAttributeNS(XMLConstants.XMLNS_ATTRIBUTE_NS_URI,
					"xmlns:xsi", XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI);
			root.setAttributeNS(XMLConstants.NULL_NS_URI, "xsi:schemaLocation",
					"http://runa.ru/xml mapping.xsd");

			for (Iterator iter = TypeNameMapping.getMapping().keySet()
					.iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				Element mappingElement = document
						.createElement(MAPPING_ELEMENT_NAME);
				root.appendChild(mappingElement);
				mappingElement.setAttribute(FORMAT_ATTRIBUTE_NAME, key);
				mappingElement.setAttribute(NAME_ATTRIBUTE_NAME,
						TypeNameMapping.getTypeName(key));
			}
			writeDocument(document, outputStream);
		} catch (ParserConfigurationException e) {
			DesignerLogger.logError(e);
		} catch (TransformerException e) {
			DesignerLogger.logError(e);
		}
	}

	private void writeDocument(Document document, OutputStream outputStream)
			throws TransformerFactoryConfigurationError,
			TransformerConfigurationException, TransformerException {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(outputStream);
		transformer.transform(source, result);
	}

	public IFile[] getMappingFiles() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();
		IFile[] files = new IFile[projects.length];
		for (int i = 0; i < projects.length; i++) {
			IProject project = projects[i];
			IFile file = project.getFile(MAPPING_XML_FILE_NAME);
			files[i] = file;
		}
		return files;
	}

	public void addMappingInfo() {
		try {
			IFile file = getMappingFiles()[0];
			for (int i = 0; i < getMappingFiles().length; i++) {
				IFile iFile = getMappingFiles()[i];
				if (iFile.exists()) {
					file = iFile;
					break;
				}
			}
			if (file.exists()) {
				SchemaFactory schemaFactory = SchemaFactory
						.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
				schemaFactory.setErrorHandler(SimpleErrorHandler.getInstance());
				Source schemaSource = new StreamSource(getClass()
						.getResourceAsStream(PluginConstants.MAPPING_XSD));
				Schema schema = schemaFactory.newSchema(schemaSource);
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				factory.setNamespaceAware(true);
				factory.setSchema(schema);
				DocumentBuilder builder = factory.newDocumentBuilder();
				builder.setErrorHandler(SimpleErrorHandler.getInstance());
				builder.setEntityResolver(ProcessDefinitionEntityResolver
						.getInstance());
				Document document = builder.parse(file.getContents());
				NodeList mappingElementsList = document.getDocumentElement()
						.getElementsByTagName(MAPPING_ELEMENT_NAME);
				Map<String, String> mapping = new HashMap<String, String>();
				for (int j = 0; j < mappingElementsList.getLength(); j++) {
					Element variableElement = (Element) mappingElementsList
							.item(j);
					String key = variableElement
							.getAttribute(FORMAT_ATTRIBUTE_NAME);
					String value = variableElement
							.getAttribute(NAME_ATTRIBUTE_NAME);
					mapping.put(key, value);
				}
				TypeNameMapping.setMapping(mapping);
			}
			else{
				Map<String, String> mapping = new HashMap<String, String>();
				addDefault(mapping);
				TypeNameMapping.setMapping(mapping);
			}
		} catch (Exception e) {
			DesignerLogger.logError(e);
		}
	}
	
	private ArrayList<IType> getParentType(String parentClassName){
		try {
			// get all project in workspace
			IJavaProject[] projects = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()).getJavaProjects();

			// iterate over workspace projects
			for (int i = 0; i < projects.length; i++) {
				IJavaProject project = projects[i];

				// find type declared in project
				IType type = project.findType(parentClassName);

				// Verify that parent class really find
				if (type != null) {
					// get all subclasses of parent class
					IType[] types = type.newTypeHierarchy(null).getAllSubtypes(type);

					ArrayList<IType> result = new ArrayList<IType>();
					for (int j = 0; j < types.length; j++) {
							result.add(types[j]);
					}
					return result;
				}	
			}
			return new ArrayList<IType>();
		} catch (JavaModelException e) {
			e.printStackTrace();
			return new ArrayList<IType>();
		}
	}
	
	public void addDefault(Map<String, String> mapping){
		for (Iterator iter = getParentType("org.jbpm.delegation.ActionHandler").iterator(); iter.hasNext();) {
			IType type = (IType) iter.next();
			mapping.put(type.getFullyQualifiedName(), type.getElementName());
		}
		for (Iterator iter = getParentType("org.jbpm.delegation.DecisionHandler").iterator(); iter.hasNext();) {
			IType type = (IType) iter.next();
			mapping.put(type.getFullyQualifiedName(), type.getElementName());
		}
		for (Iterator iter = getParentType("java.text.Format").iterator(); iter.hasNext();) {
			IType type = (IType) iter.next();
			mapping.put(type.getFullyQualifiedName(), type.getElementName());
		}
		for (Iterator iter = getParentType("ru.runa.af.organizationfunction.OrganizationFunction").iterator(); iter.hasNext();) {
			IType type = (IType) iter.next();
			mapping.put(type.getFullyQualifiedName(), type.getElementName());
		}
	}
}
