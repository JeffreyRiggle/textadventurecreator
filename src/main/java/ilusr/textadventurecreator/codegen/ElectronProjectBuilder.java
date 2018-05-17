package ilusr.textadventurecreator.codegen;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.statusbars.StatusItem;

public class ElectronProjectBuilder extends BaseProjectBuilder {

	private final ILanguageService languageService;
	
	public ElectronProjectBuilder(TextAdventureProjectPersistence persistence, ILanguageService languageService) {
		this.persistence = persistence;
		this.languageService = languageService;
	}
	
	@Override
	public void build(StatusItem item) {
		LogRunner.logger().info(String.format("Building electron project for %s", persistence.getGameInfo().gameName()));
		
		String sanitizedGameName = persistence.getGameInfo().gameName();
		String projectLocation = new String();
		
		if (persistence.getIsDev()) {
			projectLocation = persistence.getProjectLocation() + "/app";
		} else {
			projectLocation = getTempDir() + "/app";
		}
		
		buildProject(projectLocation, item);
		
		if (persistence.getIsDev()) {
			LogRunner.logger().info("Determined game was a dev game. Not compiling project.");
			item.finished().set(true);
			return;
		}
		
		compile(projectLocation, item);
		shipCompiled(projectLocation, item, sanitizedGameName);
		cleanTemp(projectLocation.substring(0, projectLocation.lastIndexOf('/')), item);
		item.finished().set(true);
	}

	private void buildProject(String projectLocation, StatusItem item) {
		File project = new File(projectLocation);
		LogRunner.logger().info(String.format("Building project in location %s", project.getAbsoluteFile().toString()));
		project.mkdirs();
		
		item.displayText().set(languageService.getValue(DisplayStrings.BUILDING));
		
		String gameName = persistence.getGameInfo().gameName();
		String author = persistence.getGameInfo().creator();
		File originalIcon = new File(persistence.getIconLocation());
		String extension = persistence.getIconLocation().substring(persistence.getIconLocation().lastIndexOf('.'));

		File gitIgnoreFile = new File(project.getAbsoluteFile() + "/.gitignore");
		writeFileContent(gitIgnoreFile, String.format(ElectronProjectFileHelper.GITIGNORE).getBytes(Charset.forName("UTF-8")));
		
		File packageFile = new File(project.getAbsoluteFile() + "/package.json");
		writeFileContent(packageFile, String.format(ElectronProjectFileHelper.PACKAGEJSON, gameName, author).getBytes(Charset.forName("UTF-8")));

		File publicPath = new File(projectLocation + "/public");
		publicPath.mkdirs();
		
		File electronJS = new File(publicPath.getAbsoluteFile() + "/electron.js");
		writeFileContent(electronJS, ElectronProjectFileHelper.ELECTRONJS.getBytes(Charset.forName("UTF-8")));
		
		File indexHTML = new File(publicPath.getAbsoluteFile() + "/index.html");
		writeFileContent(indexHTML, String.format(ElectronProjectFileHelper.INDEXHTML, gameName, extension).getBytes(Charset.forName("UTF-8")));
		
		File manifestJSON = new File(publicPath.getAbsoluteFile() + "/manifest.json");
		writeFileContent(manifestJSON, String.format(ElectronProjectFileHelper.MANIFESTJSON, gameName, extension).getBytes(Charset.forName("UTF-8")));
		
		File srcPath = new File(projectLocation + "/src");
		srcPath.mkdirs();
		
		String appbg = buildGameBackground(srcPath);
		File appcss = new File(srcPath.getAbsoluteFile() + "/App.css");
		writeFileContent(appcss, String.format(ElectronProjectFileHelper.APPCSS, appbg).getBytes(Charset.forName("UTF-8")));
		
		File appJS = new File(srcPath.getAbsoluteFile() + "/App.js");
		writeFileContent(appJS, String.format(ElectronProjectFileHelper.APPJS, appbg).getBytes(Charset.forName("UTF-8")));
		
		File appTestJS = new File(srcPath.getAbsoluteFile() + "/App.test.js");
		writeFileContent(appTestJS, ElectronProjectFileHelper.APPTESTJS.getBytes(Charset.forName("UTF-8")));
		
		File indexcss = new File(srcPath.getAbsoluteFile() + "/index.css");
		writeFileContent(indexcss, ElectronProjectFileHelper.INDEXCSS.getBytes(Charset.forName("UTF-8")));
		
		File indexJS = new File(srcPath.getAbsoluteFile() + "/index.js");
		writeFileContent(indexJS, ElectronProjectFileHelper.INDEXJS.getBytes(Charset.forName("UTF-8")));
		
		File rsw = new File(srcPath.getAbsoluteFile() + "/registerServiceWorker.js");
		writeFileContent(rsw, ElectronProjectFileHelper.REGISTERSERVICEWORKERJS.getBytes(Charset.forName("UTF-8")));
		
		try {
			LogRunner.logger().info("Moving game icon.");
			
			File srcIcon = new File(String.format("%s/logo%s", srcPath.getAbsolutePath(), extension));
			Files.copy(originalIcon.toPath(), srcIcon.toPath());
			
			File publicIcon = new File(String.format("%s/favicon%s", publicPath.getAbsolutePath(), extension));
			Files.copy(originalIcon.toPath(), publicIcon.toPath());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		buildGameFile(project, gameName, publicPath);
	}
	
	private void compile(String projectLocation, StatusItem item) {
		
	}
	
	private void shipCompiled(String projectLocation, StatusItem item, String gameName) {
		
	}
	
	private void cleanTemp(String location, StatusItem item) {
		
	}
	
	private String getTempDir() {
		String retVal = new String();
		
		try {
			retVal = Files.createTempDirectory("electronbuild").toFile().getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retVal;
	}
}
