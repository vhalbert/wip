package org.teiid.translator.exec;

import static org.mockito.Mockito.*;

import org.junit.Assert;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.teiid.metadata.Datatype;
import org.teiid.metadata.MetadataFactory;
import org.teiid.query.metadata.DDLStringVisitor;
import org.teiid.query.metadata.SystemMetadata;
import org.teiid.translator.TranslatorException;

@SuppressWarnings("nls")
public class TestMetadataProcessor {
	protected static ExecTranslatorExecutionFactory TRANSLATOR;
	
	protected static ExecTranslatorConnection CONN;

	@BeforeClass
	public static void setUp() throws TranslatorException {
		
		CONN  = mock(ExecTranslatorConnection.class); 
		
		TRANSLATOR = new ExecTranslatorExecutionFactory();
		TRANSLATOR.start();
	}
	
	@Before public void beforeEach() throws Exception{	

    }

	@Test
	public void testPersonMetadata() throws Exception {
		Map<String, Datatype> types = new HashMap<String, Datatype>();
		
		MetadataFactory mf = new MetadataFactory("vdb", 1, "objectvdb",
				SystemMetadata.getInstance().getRuntimeTypeMap(),
				new Properties(), null);

		

		TRANSLATOR.getMetadataProcessor().process(mf, CONN);

		String metadataDDL = DDLStringVisitor.getDDLString(mf.getSchema(),
				null, null);

		System.out.println("Schema: " + metadataDDL);
		String expected =  "CREATE FOREIGN TABLE ExecTable (\n"
				+ "\targuments string OPTIONS (SELECTABLE FALSE, SEARCHABLE 'Searchable', NATIVE_TYPE 'java.lang.String'),\n"
				+ "\tdelimiter string OPTIONS (SELECTABLE FALSE, SEARCHABLE 'Searchable', NATIVE_TYPE 'java.lang.String'),\n"
				+ "\tresults string OPTIONS (SEARCHABLE 'Unsearchable', NATIVE_TYPE 'java.lang.String')\n"
				+ ");"
				;

		
		Assert.assertEquals(expected, metadataDDL);	

	}


}
