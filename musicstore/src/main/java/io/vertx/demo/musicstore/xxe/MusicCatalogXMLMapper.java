package io.vertx.demo.musicstore.xxe;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import io.vertx.demo.musicstore.model.Foo;

public class MusicCatalogXMLMapper {

	protected static Foo customMapper(final File fileXml) throws Exception {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new FileInputStream(fileXml));
		Document doc = builder.parse(is);
		Element element = doc.getDocumentElement();
		NodeList nodes = element.getChildNodes();
		if (nodes == null || nodes.getLength() > 1) {
			throw new ParserConfigurationException("Error parsing xml");
		}
		return new Foo(nodes.item(0).getFirstChild().getNodeValue().toString());
	}

	/**
	 * Jackson mapper
	 * @param fileXml
	 * @return
	 * @throws IOException
	 */
	protected static Foo mapperFooJackson(final File fileXml) throws IOException {
		ObjectMapper xmlMapper = new XmlMapper();
		return xmlMapper.readValue(fileXml, Foo.class);
	}

}