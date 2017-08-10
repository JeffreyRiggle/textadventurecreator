package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ilusr.textadventurecreator.error.EmailAttachment;
import ilusr.textadventurecreator.error.EmailParameters;
import ilusr.textadventurecreator.error.EmailRecipient;

public class EmailParametersUnitTest {

	@Test
	public void testCreate() {
		EmailParameters params = new EmailParameters();
		assertNotNull(params);
		assertNotNull(params.attachments());
		assertNotNull(params.getContent());
		assertNotNull(params.getFromAddress());
		assertNotNull(params.getReplyToAddress());
		assertNotNull(params.getSubject());
		assertNotNull(params.recipients());
	}

	@Test
	public void testCreateWithFrom() {
		EmailParameters params = new EmailParameters("a@b.c");
		assertNotNull(params);
		assertNotNull(params.attachments());
		assertNotNull(params.getContent());
		assertEquals("a@b.c", params.getFromAddress());
		assertNotNull(params.getReplyToAddress());
		assertNotNull(params.getSubject());
		assertNotNull(params.recipients());
	}
	
	@Test
	public void testCreateWithSubject() {
		EmailParameters params = new EmailParameters("a@b.c", "subject");
		assertNotNull(params);
		assertNotNull(params.attachments());
		assertNotNull(params.getContent());
		assertEquals("a@b.c", params.getFromAddress());
		assertNotNull(params.getReplyToAddress());
		assertEquals("subject", params.getSubject());
		assertNotNull(params.recipients());
	}
	
	@Test
	public void testCreateWithContent() {
		EmailParameters params = new EmailParameters("a@b.c", "subject", "<html><body><h1>test</h1></body></html>");
		assertNotNull(params);
		assertNotNull(params.attachments());
		assertEquals("<html><body><h1>test</h1></body></html>", params.getContent());
		assertEquals("a@b.c", params.getFromAddress());
		assertNotNull(params.getReplyToAddress());
		assertEquals("subject", params.getSubject());
		assertNotNull(params.recipients());
	}
	
	@Test
	public void testCreateWithRecipients() {
		List<EmailRecipient> recps = Arrays.asList(new EmailRecipient("aa@b.c"));
		
		EmailParameters params = new EmailParameters("a@b.c", "subject", "<html><body><h1>test</h1></body></html>", recps);
		assertNotNull(params);
		assertNotNull(params.attachments());
		assertEquals("<html><body><h1>test</h1></body></html>", params.getContent());
		assertEquals("a@b.c", params.getFromAddress());
		assertNotNull(params.getReplyToAddress());
		assertEquals("subject", params.getSubject());
		assertEquals(1, params.recipients().size());
	}
	
	@Test
	public void testCreateFull() {
		List<EmailRecipient> recps = Arrays.asList(new EmailRecipient("aa@b.c"));
		List<EmailAttachment> atts = Arrays.asList(new EmailAttachment("c:/something.txt"));
		
		EmailParameters params = new EmailParameters("a@b.c", "subject", "<html><body><h1>test</h1></body></html>", recps, atts);
		assertNotNull(params);
		assertEquals(1, params.attachments().size());
		assertEquals("<html><body><h1>test</h1></body></html>", params.getContent());
		assertEquals("a@b.c", params.getFromAddress());
		assertNotNull(params.getReplyToAddress());
		assertEquals("subject", params.getSubject());
		assertEquals(1, params.recipients().size());
	}
	
	@Test
	public void testAddress() {
		List<EmailRecipient> recps = Arrays.asList(new EmailRecipient("aa@b.c"));
		List<EmailAttachment> atts = Arrays.asList(new EmailAttachment("c:/something.txt"));
		
		EmailParameters params = new EmailParameters("a@b.c", "subject", "<html><body><h1>test</h1></body></html>", recps, atts);
		params.setFromAddress("b@c.a");
		assertEquals("b@c.a", params.getFromAddress());
	}
	
	@Test
	public void testRecipients() {
		List<EmailRecipient> recps = new ArrayList<EmailRecipient>();
		recps.add(new EmailRecipient("aa@b.c"));
		List<EmailAttachment> atts = Arrays.asList(new EmailAttachment("c:/something.txt"));
		
		EmailParameters params = new EmailParameters("a@b.c", "subject", "<html><body><h1>test</h1></body></html>", recps, atts);
		params.recipients().add(new EmailRecipient("z@q.c"));
		assertEquals(2, params.recipients().size());
	}
	
	@Test
	public void testSubject() {
		List<EmailRecipient> recps = Arrays.asList(new EmailRecipient("aa@b.c"));
		List<EmailAttachment> atts = Arrays.asList(new EmailAttachment("c:/something.txt"));
		
		EmailParameters params = new EmailParameters("a@b.c", "subject", "<html><body><h1>test</h1></body></html>", recps, atts);
		params.setSubject("subject2");
		assertEquals("subject2", params.getSubject());
	}
	
	@Test
	public void testContent() {
		List<EmailRecipient> recps = Arrays.asList(new EmailRecipient("aa@b.c"));
		List<EmailAttachment> atts = Arrays.asList(new EmailAttachment("c:/something.txt"));
		
		EmailParameters params = new EmailParameters("a@b.c", "subject", "<html><body><h1>test</h1></body></html>", recps, atts);
		params.setContent("plain text");
		assertEquals("plain text", params.getContent());
	}
	
	@Test
	public void testAttachments() {
		List<EmailRecipient> recps = Arrays.asList(new EmailRecipient("aa@b.c"));
		List<EmailAttachment> atts = new ArrayList<EmailAttachment>();
		atts.add(new EmailAttachment("c:/something.txt"));
		
		EmailParameters params = new EmailParameters("a@b.c", "subject", "<html><body><h1>test</h1></body></html>", recps, atts);
		params.attachments().add(new EmailAttachment("d:/test.txt"));
		assertEquals(2, params.attachments().size());
	}
	
	@Test
	public void testReplyTo() {
		List<EmailRecipient> recps = Arrays.asList(new EmailRecipient("aa@b.c"));
		List<EmailAttachment> atts = Arrays.asList(new EmailAttachment("c:/something.txt"));
		
		EmailParameters params = new EmailParameters("a@b.c", "subject", "<html><body><h1>test</h1></body></html>", recps, atts);
		params.setReplyToAddress("y@p.z");
		assertEquals("y@p.z", params.getReplyToAddress());
	}
}
