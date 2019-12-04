package io.vertx.demo.musicstore.serialize;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.thoughtworks.xstream.XStream;

public class XStreamTest {

	private TestType testType = TestType.TEST_1;

	public XStreamTest(final TestType testType) {
		super();
		this.testType = testType;
	}

	public XStreamTest() {
		super();
	}

	public void doSerializationTest() {
		// CVE-2016-0792
		// CVE-2013-7285
		// CVE-2019-10173
		XStream xStream = new XStream();
		// XStream.setupDefaultSecurity(xStream);// 1.4.10
		org.codehaus.groovy.runtime.MethodClosure.ALLOW_RESOLVE = true;
		xStream.fromXML(this.getClass().getResourceAsStream(testType.getTestPayload()));

	}

	public void doSerializationXStream(final String pathFile) {
		// CVE-2016-0792
		// CVE-2013-7285
		// CVE-2019-10173
		XStream xStream = new XStream();
		// XStream.setupDefaultSecurity(xStream);// 1.4.10
		org.codehaus.groovy.runtime.MethodClosure.ALLOW_RESOLVE = true;
		try {
			xStream.fromXML(new FileInputStream(new File(pathFile)));
		}
		catch (FileNotFoundException e) {
			throw new RuntimeException("fileNotFound:" + pathFile);
		}

	}

	protected void generateTestFile() throws Exception {

	}

	public static enum TestType {

		TEST_1("/payloadXStream1.xml"), TEST_2("/payloadXStream2.xml"), TEST_3("/payloadXStream3.xml"), TEST_4("/payloadXStream4.xml");
		private final String type;

		private TestType(final String type) {
			this.type = type;
		}

		public String getTestPayload() {
			return type;
		}
	}
}
