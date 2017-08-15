package ilusr.textadventurecreator.menus;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.error.IReportIssueService;
import ilusr.textadventurecreator.error.ReportIssueModel;
import ilusr.textadventurecreator.error.ReportIssueView;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.Dialog;
import ilusr.textadventurecreator.views.IDialogProvider;
import ilusr.textadventurecreator.views.LanguageAwareString;
import javafx.scene.control.MenuItem;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ReportIssueMenuItem extends MenuItem {

	private final ILanguageService langauageService;
	private final IDialogService dialogService;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	private final IDialogProvider dialogProvider;
	private final IReportIssueService reportIssueService;
	private LanguageAwareString report;
	
	/**
	 * 
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogService A @see IDialogService to display dialogs.
	 * @param styleService service to manage styles.
	 * @param urlProvider provides internal resources.
	 * @param dialogProvider provides dialogs.
	 * @param reportIssueService reports issues.
	 */
	public ReportIssueMenuItem(ILanguageService languageService, 
							   IDialogService dialogService,
							   IStyleContainerService styleService,
							   InternalURLProvider urlProvider,
							   IDialogProvider dialogProvider,
							   IReportIssueService reportIssueService) {
		this.langauageService = languageService;
		this.dialogService = dialogService;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		this.dialogProvider = dialogProvider;
		this.reportIssueService = reportIssueService;
		
		report = new LanguageAwareString(languageService, DisplayStrings.REPORT_ISSUE);
		
		initialize();
	}
	
	private void initialize() {
		super.textProperty().bind(report);
		super.setOnAction((e) -> {
			LogRunner.logger().info("Help -> Report an Issue Pressed.");
			ReportIssueModel model = new ReportIssueModel(langauageService, reportIssueService);
			Dialog dialog = dialogProvider.create(new ReportIssueView(model, styleService, urlProvider));
			
			dialog.isValid().bind(model.valid());
			dialog.setOnComplete(() -> {
				model.sendRequest();
			});
			
			dialogService.displayModeless(dialog, langauageService.getValue(DisplayStrings.REPORT_ISSUE));
		});
	}
}
