package org.htpllang;

import org.dom4j.DocumentException;
import org.htpllang.service.HtplParseService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class HtplParseTest {
	
	@Autowired
	private HtplParseService htplParseService;
	
	@Test
	public void testConst1() throws DocumentException {
		String htplCode = "<htpl><const value=\"5\"/></htpl>";
		
		String expected = "5";
		String result = htplParseService.parse(htplCode);
		
		Assert.assertEquals(expected, result);
	}
	
	@Test
	public void testConst2() throws DocumentException {
		String htplCode = "<htpl><const value=\"true\"/></htpl>";
		
		String expected = "True";
		String result = htplParseService.parse(htplCode);
		
		Assert.assertEquals(expected, result);
	}
	
	@Test
	public void testVar() throws DocumentException {
		String htplCode = "<htpl><var name=\"a\">5</var></htpl>";
		
		String expected = "a = 5";
		String result = htplParseService.parse(htplCode);
		
		Assert.assertEquals(expected, result);
	}
	
	@Test
	public void testVal() throws DocumentException {
		String htplCode = "<htpl><val name=\"a\"/></htpl>";
		
		String expected = "a";
		String result = htplParseService.parse(htplCode);
		
		Assert.assertEquals(expected, result);
	}
	
	@Test
	public void testArray() throws DocumentException {
		String htplCode = "<htpl><array name=\"a\">\n" +
				"        <const value=\"1\"/>\n" +
				"        <const value=\"2\"/>\n" +
				"        <const value=\"'la'\"/>\n" +
				"    </array></htpl>";
		
		String expected = "a = [1, 2, 'la', ]";
		String result = htplParseService.parse(htplCode);
		
		Assert.assertEquals(expected, result);
	}
	
	@Test
	public void testCall() throws DocumentException {
		String htplCode = "<htpl><call name=\"print\">\n" +
				"        <const value=\"1\"/>\n" +
				"        <val name=\"a\"/>\n" +
				"    </call></htpl>";
		
		String expected = "print(1, a, )";
		String result = htplParseService.parse(htplCode);
		
		Assert.assertEquals(expected, result);
	}
	
	@Test
	public void testIf1() throws DocumentException {
		String htplCode = "<htpl>" +
				"<if>\n" +
				"        <cond>True</cond>\n" +
				"        <then>\n" +
				"        </then>\n" +
				"        <else>\n" +
				"        </else>\n" +
				"    </if>" +
				"</htpl>";
		
		String expected = "if True:\n" +
				"    pass\n" +
				"else:\n" +
				"    pass";
		String result = htplParseService.parse(htplCode);
		
		Assert.assertEquals(expected, result);
	}
	
	@Test
	public void testIf2() throws DocumentException {
		String htplCode = "<htpl><if>\n" +
				"        <cond><val name=\"a\"/> + <val name=\"b\"/> < <const value=\"0\"/></cond>\n" +
				"        <then>\n" +
				"            <call name=\"print\">\n" +
				"                <val name=\"a\"/>\n" +
				"                <val name=\"b\"/>\n" +
				"            </call>\n" +
				"        </then>\n" +
				"    </if></htpl>";
		
		String expected = "if a + b < 0:\n" +
				"    print(a, b, )\n" +
				"    pass";
		String result = htplParseService.parse(htplCode);
		
		Assert.assertEquals(expected, result);
	}
	
	@Test
	public void testIf3() throws DocumentException {
		String htplCode = "<htpl><if>\n" +
				"        <cond>" +
				"           <val name=\"a\"/> " +
				"           + " +
				"           <val name=\"b\"/> " +
				"           > " +
				"           <const value=\"0\"/>" +
				"           </cond>\n" +
				"        <then>\n" +
				"            <call name=\"print\">\n" +
				"                <val name=\"a\"/>\n" +
				"                <val name=\"b\"/>\n" +
				"            </call>\n" +
				"        </then>\n" +
				"        <else>\n" +
				"            <call name=\"print\">\n" +
				"                <val name=\"a\"/>\n" +
				"                <val name=\"b\"/>\n" +
				"            </call>\n" +
				"        </else>\n" +
				"    </if></htpl>";
		
		String expected = "if a + b < 0:\n" +
				"    print(a, b, )\n" +
				"    pass\n" +
				"else:\n" +
				"    print(a, b, )\n" +
				"    pass";
		String result = htplParseService.parse(htplCode);
		
		Assert.assertEquals(expected, result);
	}
	
	@Test
	public void testWhile1() throws DocumentException {
		String htplCode = "<htpl> <while>\n" +
				"        <cond><const value='True'/></cond>\n" +
				"        <while-body>\n" +
				"            <call name=\"print\">\n" +
				"                <const value=\"'la'\"/>\n" +
				"            </call>\n" +
				"            <break/>\n" +
				"        </while-body>\n" +
				"    </while></htpl>";
		
		String expected = "while True:\n" +
				"    print('la', )\n" +
				"    break\n" +
				"    pass";
		String result = htplParseService.parse(htplCode);
		
		Assert.assertEquals(expected, result);
	}
	
}
