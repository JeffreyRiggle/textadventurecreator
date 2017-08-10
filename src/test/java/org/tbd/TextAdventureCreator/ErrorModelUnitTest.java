package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ilusr.textadventurecreator.error.ErrorModel;
import ilusr.textadventurecreator.error.IEmailService;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;

public class ErrorModelUnitTest {

	private ILanguageService langaugeService;
	private IEmailService emailService;
	
	private ErrorModel model;
	
	@Before
	public void setup() {
		langaugeService = Mockito.mock(ILanguageService.class);
		Mockito.when(langaugeService.getValue(DisplayStrings.ERROR_TEXT)).thenReturn("Error");
		Mockito.when(langaugeService.getValue(DisplayStrings.CONTINUE)).thenReturn("Continue");
		Mockito.when(langaugeService.getValue(DisplayStrings.REPORT)).thenReturn("Report");
		Mockito.when(langaugeService.getValue(DisplayStrings.EXIT)).thenReturn("Exit");
		
		emailService = Mockito.mock(IEmailService.class);
		model = new ErrorModel("callstack :(", langaugeService, emailService);
	}

	@Test
	public void testException() {
		assertEquals("callstack :(", model.exceptionProperty().get());
	}
	
	@Test
	public void testErrorText() {
		assertEquals("Error", model.errorText().get());
	}
	
	@Test
	public void testContinueText() {
		assertEquals("Continue", model.continueText().get());
	}
	
	@Test
	public void testReportText() {
		assertEquals("Report", model.reportText().get());
	}
	
	@Test
	public void testExitText() {
		assertEquals("Exit", model.exitText().get());
	}
	
	@Test
	public void testSendReport() {
		Waiter waiter = new Waiter();
		model.sendReport(waiter);
		
		while (!waiter.ran()) {
			try {
				Thread.sleep(10);
			} catch (Exception e) { }
		}
		
		Mockito.verify(emailService, Mockito.times(1)).sendEmail(Mockito.any());
	}
	
	private class Waiter implements Runnable {

		private boolean ran;
		
		public Waiter() {
			ran = false;
		}
		
		@Override
		public void run() {
			this.ran = true;
		}
		
		public boolean ran() {
			return ran;
		}
	}
}
