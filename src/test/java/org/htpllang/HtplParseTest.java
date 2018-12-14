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
	public void testConst() throws DocumentException {
		String htplCode = "<htpl><const value=\"5\"/></htpl>";
		
		String expected = "5";
		String result = htplParseService.parse(htplCode);
		
		Assert.assertEquals(expected, result.trim());
	}
	
}
