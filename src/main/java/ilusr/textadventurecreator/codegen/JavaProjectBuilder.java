package ilusr.textadventurecreator.codegen;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import ilusr.core.environment.EnvironmentUtilities;
import ilusr.core.io.ProcessHelpers;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.statusbars.StatusIndicator;
import ilusr.textadventurecreator.statusbars.StatusItem;
import textadventurelib.persistence.GameStatePersistenceObject;
import textadventurelib.persistence.OptionPersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class JavaProjectBuilder extends BaseProjectBuilder {

	private final String PROJECT_TITLE = "textadventure";
	
	/**
	 * 
	 * @param persistence A @see TextAdventureProjectPersistence representing the game to build.
	 * @param languageService A @see LanaugageService used to localize display strings.
	 */
	public JavaProjectBuilder(TextAdventureProjectPersistence persistence, ILanguageService languageService) {
		this.persistence = persistence;
		this.languageService = languageService;
	}
	
	@Override
	public void build(StatusItem item) {
		LogRunner.logger().info(String.format("Building java project for %s", persistence.getGameInfo().gameName()));
		
		String sanitizedGameName = persistence.getGameInfo().gameName();
		String projectLocation = new String();
		
		if (persistence.getIsDev()) {
			projectLocation = persistence.getProjectLocation() + "/" + sanitizedGameName;
		} else {
			projectLocation = getTempDir() + "/" + sanitizedGameName;
		}
		
		buildProject(projectLocation, item);
		
		if (persistence.getIsDev()) {
			LogRunner.logger().info("Determined game was a dev game. Not compiling project.");
			item.finished().set(true);
			return;
		}
		
		compile(projectLocation, item);
		shipCompiledFile(projectLocation, item, sanitizedGameName);
		cleanTemp(projectLocation.substring(0, projectLocation.lastIndexOf('/')), item);
		item.finished().set(true);
	}
	
	private void buildProject(String projectLocation, StatusItem item) {
		File project = new File(projectLocation);
		if (!project.exists() || project.list().length == 0) {
			firstBuild(project, item);
			return;
		}
		
		updateBuild();
	}
	
	private void compile(String projectLocation, StatusItem item) {
		LogRunner.logger().info("Compiling java project.");
		
		item.indicator().set(StatusIndicator.Normal);
		item.displayText().set(languageService.getValue(DisplayStrings.COMPLILING));
		item.progressAmount().set(.5);
		try {
			item.progressAmount().set(.6);
			Process proc = getCompileProcess(projectLocation);
			
			if (proc == null) {
				LogRunner.logger().info("Unable to build compile process!");
				return;
			}
			
			ProcessHelpers.handleProcessStreams(proc);
			proc.waitFor();
			
			LogRunner.logger().info("Finished compiling project.");
			item.progressAmount().set(1.0);
			item.indicator().set(StatusIndicator.Good);
		} catch (Exception e) {
			e.printStackTrace();
			item.indicator().set(StatusIndicator.Error);
		}
	}
	
	private Process getCompileProcess(String projectLocation) throws IOException {
		Process retVal = null;
		
		if (EnvironmentUtilities.isWindows()) {
			LogRunner.logger().info("Determined environment was windows. Building windows compile process");
			retVal = Runtime.getRuntime().exec(new String[] {"cmd", "/c", "start", "/wait", "mvnlnk.lnk", "clean", "install", "-f",  projectLocation});
		} else if (EnvironmentUtilities.isUnix()) {
			LogRunner.logger().info("Determined environment was linux. Building linux compile process");
			retVal = Runtime.getRuntime().exec(new String[] {"mvn", "clean", "install", "-f", projectLocation});
		} else if (EnvironmentUtilities.isMac()) {
			LogRunner.logger().info("Determined environment was mac. Building mac compile process");
			retVal = Runtime.getRuntime().exec(new String[] {"mvn", "clean", "install", "-f", projectLocation});
		}
		
		return retVal;
	}
	
	private void shipCompiledFile(String location, StatusItem item, String gameName) {
		item.displayText().set(languageService.getValue(DisplayStrings.MOVING_PROJECT));
		item.indicator().set(StatusIndicator.Normal);
		String appName = gameName + "-1.0.0-SNAPSHOT" + ".jar";
		File app = new File(location + "/target/" + appName);
		try {
			item.progressAmount().set(.5);
			File target = new File(persistence.getProjectLocation());
			
			if (!target.exists()) {
				target.mkdirs();
			}
			
			LogRunner.logger().info(String.format("Shipping compiled project to %s", target.getAbsolutePath().toString()));
			Files.copy(app.toPath(), new File(target.getAbsolutePath() + "/" + appName).toPath());
			item.progressAmount().set(1.0);
			item.indicator().set(StatusIndicator.Good);
		} catch (Exception e) {
			e.printStackTrace();
			item.indicator().set(StatusIndicator.Error);
		}
	}
	
	private String getTempDir() {
		String retVal = new String();
		
		try {
			retVal = Files.createTempDirectory("javabuild").toFile().getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retVal;
	}
	
	private void firstBuild(File project, StatusItem item) {
		LogRunner.logger().info(String.format("Building project in location %s", project.getAbsoluteFile().toString()));
		
		item.displayText().set(languageService.getValue(DisplayStrings.BUILDING));
		item.progressAmount().set(.2);
		String sanitizedGameName = persistence.getGameInfo().gameName();
		
		project.mkdirs();
		
		File pomFile = new File(project.getAbsoluteFile() + "/pom.xml");
		writeFileContent(pomFile, String.format(JavaProjectFileHelper.POM, PROJECT_TITLE, sanitizedGameName).getBytes(Charset.forName("UTF-8")));
		
		String src = project.getAbsoluteFile() + "/" + "/src/main/java/org/" + PROJECT_TITLE + "/";
		File srcDir = new File(src + "/" + sanitizedGameName);
		srcDir.mkdirs();
		
		item.progressAmount().set(.4);
		File srcFile = new File(src);
		String gameIcon = buildGameIcon(srcFile);
		String gameBackground = buildGameBackground(new File(srcFile.getAbsolutePath() + "/assets/"));
		
		File assetHelper = new File(srcFile + "/assets/AssetLoader.java");
		writeFileContent(assetHelper, String.format(JavaProjectFileHelper.ASSETLOADER, PROJECT_TITLE).getBytes(Charset.forName("UTF-8")));
		
		File gamePath = new File(srcFile.getAbsolutePath() + "/" + sanitizedGameName);
		if (!gamePath.exists()) {
			gamePath.mkdirs();
		}

		buildGameFile(gamePath, sanitizedGameName, new File(srcFile.getAbsolutePath() + "/assets/"));
		
		item.progressAmount().set(.6);
		File gameApp = new File(src + "/" + sanitizedGameName + "/GameApp.java");
		writeFileContent(gameApp, String.format(JavaProjectFileHelper.GAMEAPP, PROJECT_TITLE, sanitizedGameName).getBytes(Charset.forName("UTF-8")));
		
		File gameStateInitializer = new File(src + "/" + sanitizedGameName + "/GameStateInitializer.java");
		writeFileContent(gameStateInitializer, String.format(JavaProjectFileHelper.GAMESTATEINITIALIZER, PROJECT_TITLE, sanitizedGameName, sanitizedGameName).getBytes(Charset.forName("UTF-8")));
		
		File gameStates = new File(src + "/" + sanitizedGameName + "/GameStates.java");
		writeFileContent(gameStates, String.format(JavaProjectFileHelper.GAMESTATES, PROJECT_TITLE, sanitizedGameName).getBytes(Charset.forName("UTF-8")));
		
		File loadGameModel = new File(src + "/" + sanitizedGameName + "/LoadGameModel.java");
		writeFileContent(loadGameModel, String.format(JavaProjectFileHelper.LOADGAMEMODEL, PROJECT_TITLE, sanitizedGameName, getExternalGameFileName()).getBytes(Charset.forName("UTF-8")));
		
		File loadGameState = new File(src + "/" + sanitizedGameName + "/LoadGameState.java");
		writeFileContent(loadGameState, String.format(JavaProjectFileHelper.LOADGAMESTATE, PROJECT_TITLE, sanitizedGameName).getBytes(Charset.forName("UTF-8")));
		
		File loadGameView = new File(src + "/" + sanitizedGameName + "/LoadGameView.java");
		writeFileContent(loadGameView, String.format(JavaProjectFileHelper.LOADGAMEVIEW, PROJECT_TITLE, sanitizedGameName).getBytes(Charset.forName("UTF-8")));
		
		item.progressAmount().set(.8);
		File loadingView = new File(src + "/" + sanitizedGameName + "/LoadingView.java");
		writeFileContent(loadingView, String.format(JavaProjectFileHelper.LOADINGVIEW, PROJECT_TITLE, sanitizedGameName).getBytes(Charset.forName("UTF-8")));
		
		File mainApp = new File(src + "/" + sanitizedGameName + "/MainApplication.java");
		writeFileContent(mainApp, String.format(JavaProjectFileHelper.MAINAPPLICATION, PROJECT_TITLE, sanitizedGameName, sanitizedGameName, sanitizedGameName, gameIcon).getBytes(Charset.forName("UTF-8")));
		
		File mainMenuGameState = new File(src + "/" + sanitizedGameName + "/MainMenuGameState.java");
		writeFileContent(mainMenuGameState, String.format(JavaProjectFileHelper.MAINMENUGAMESTATE, PROJECT_TITLE, sanitizedGameName).getBytes(Charset.forName("UTF-8")));
		
		File menuModel = new File(src + "/" + sanitizedGameName + "/MenuModel.java");
		writeFileContent(menuModel, String.format(JavaProjectFileHelper.MENUMODEL, PROJECT_TITLE, sanitizedGameName).getBytes(Charset.forName("UTF-8")));
		
		File menuView = new File(src + "/" + sanitizedGameName + "/MenuView.java");
		writeFileContent(menuView, String.format(JavaProjectFileHelper.MENUVIEW, PROJECT_TITLE, sanitizedGameName).getBytes(Charset.forName("UTF-8")));
		
		File transitionGameState = new File(src + "/" + sanitizedGameName + "/TransitionGameState.java");
		writeFileContent(transitionGameState, String.format(JavaProjectFileHelper.TRANSITIONGAMESTATE, PROJECT_TITLE, sanitizedGameName).getBytes(Charset.forName("UTF-8")));
		
		File loadMarkup = new File(src + "/" + sanitizedGameName + "/LoadGameView.fxml");
		writeFileContent(loadMarkup, JavaProjectFileHelper.LOADGAMEMARKUP.getBytes(Charset.forName("UTF-8")));
		
		File loadStyle = new File(src + "/" + sanitizedGameName + "/LoadingView.css");
		writeFileContent(loadStyle, String.format(JavaProjectFileHelper.LOADINGSTYLE, gameBackground).getBytes(Charset.forName("UTF-8")));
		
		File loadViewMarkup = new File(src + "/" + sanitizedGameName + "/LoadingView.fxml");
		writeFileContent(loadViewMarkup, JavaProjectFileHelper.LOADINGMARKUP.getBytes(Charset.forName("UTF-8")));
		
		File menuItem = new File(src + "/" + sanitizedGameName + "/MenuItem.css");
		writeFileContent(menuItem, JavaProjectFileHelper.MENUITEMSTYLE.getBytes(Charset.forName("UTF-8")));
		
		File menuViewStyle = new File(src + "/" + sanitizedGameName + "/MenuView.css");
		writeFileContent(menuViewStyle, String.format(JavaProjectFileHelper.MENUSTYLE, gameBackground).getBytes(Charset.forName("UTF-8")));
		
		File menuMarkup = new File(src + "/" + sanitizedGameName + "/MenuView.fxml");
		writeFileContent(menuMarkup, JavaProjectFileHelper.MENUMARKUP.getBytes(Charset.forName("UTF-8")));
		
		File saveitem = new File(src + "/" + sanitizedGameName + "/SaveItem.java");
		writeFileContent(saveitem, String.format(JavaProjectFileHelper.SAVEITEM, PROJECT_TITLE, sanitizedGameName).getBytes(Charset.forName("UTF-8")));
		
		LogRunner.logger().info("Finished building project");
		item.progressAmount().set(1.0);
		item.indicator().set(StatusIndicator.Good);
	}
	
	private String buildGameIcon(File src) {
		String retVal = new String();
		try {
			LogRunner.logger().info("Moving game icon.");
			File gameAssets = new File(src.getAbsolutePath() + "/assets/");
			if (!gameAssets.exists()) {
				gameAssets.mkdirs();
			}
			
			File originalIcon = new File(persistence.getIconLocation());
			String extension = persistence.getIconLocation().substring(persistence.getIconLocation().lastIndexOf('.'));
			File gameIcon = new File(String.format("%s/gameicon%s", gameAssets.getAbsolutePath(), extension));
			Files.copy(originalIcon.toPath(), gameIcon.toPath());
			
			retVal = gameIcon.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retVal;
	}
	
	private String getExternalGameFileName() {
		if (persistence.getTextAdventure().gameStatesInline()) {
			return new String();
		}
		
		for (GameStatePersistenceObject gameState : persistence.getTextAdventure().gameStates()) {
			for (OptionPersistenceObject option : gameState.options()) {
				if (!option.action().type().equals("Save")) {
					continue;
				}
				
				return "gamestates.xml";
			}
		}
		
		return new String();
	}
	
	private void updateBuild() {
		LogRunner.logger().info("Updating project.");
	}
}
