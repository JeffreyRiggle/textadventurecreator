package ilusr.textadventurecreator.codegen;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

import ilusr.core.environment.EnvironmentUtilities;
import ilusr.core.io.FileUtilities;
import ilusr.core.io.ProcessHelpers;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.codegen.webfiles.WebResourceFileLoader;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.statusbars.StatusIndicator;
import ilusr.textadventurecreator.statusbars.StatusItem;

public class ElectronProjectBuilder extends BaseProjectBuilder {
	
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
		item.indicator().set(StatusIndicator.Normal);
		item.progressAmount().set(.1);
		
		String gameName = persistence.getGameInfo().gameName();
		String author = persistence.getGameInfo().creator();
		String description = persistence.getGameInfo().description();
		
		File originalIcon = new File(persistence.getIconLocation());
		String extension = persistence.getIconLocation().substring(persistence.getIconLocation().lastIndexOf('.'));

		File gitIgnoreFile = new File(project.getAbsoluteFile() + "/.gitignore");
		writeFileContent(gitIgnoreFile, String.format(ElectronProjectFileHelper.GITIGNORE).getBytes(Charset.forName("UTF-8")));
		
		File packageFile = new File(project.getAbsoluteFile() + "/package.json");
		writeFileContent(packageFile, String.format(ElectronProjectFileHelper.PACKAGEJSON, gameName, author, description).getBytes(Charset.forName("UTF-8")));

		item.progressAmount().set(.2);
		File configPath = new File(projectLocation + "/config");
		configPath.mkdirs();
		
		File jestPath = new File(configPath.getAbsolutePath() + "/jest");
		jestPath.mkdirs();
		
		File scriptPath = new File(projectLocation + "/scripts");
		scriptPath.mkdirs();
		item.progressAmount().set(.3);

		WebResourceFileLoader loader = new WebResourceFileLoader();
		try {
			File cssTransform = new File(jestPath.getAbsolutePath() + "/cssTransform.js");
			writeFileContent(cssTransform, FileUtilities.getFileContentWithReturns(loader.getResource("cssTransform.js")).getBytes(Charset.forName("UTF-8")));
			
			File fileTransform = new File(jestPath.getAbsolutePath() + "/fileTransform.js");
			writeFileContent(fileTransform, FileUtilities.getFileContentWithReturns(loader.getResource("fileTransform.js")).getBytes(Charset.forName("UTF-8")));	
			
			File env = new File(configPath.getAbsolutePath() + "/env.js");
			writeFileContent(env, FileUtilities.getFileContentWithReturns(loader.getResource("env.js")).getBytes(Charset.forName("UTF-8")));
			
			File paths = new File(configPath.getAbsolutePath() + "/paths.js");
			writeFileContent(paths, FileUtilities.getFileContentWithReturns(loader.getResource("paths.js")).getBytes(Charset.forName("UTF-8")));
			
			File poly = new File(configPath.getAbsolutePath() + "/polyfills.js");
			writeFileContent(poly, FileUtilities.getFileContentWithReturns(loader.getResource("polyfills.js")).getBytes(Charset.forName("UTF-8")));
			
			File wdev = new File(configPath.getAbsolutePath() + "/webpack.config.dev.js");
			writeFileContent(wdev, FileUtilities.getFileContentWithReturns(loader.getResource("webpack.config.dev.js")).getBytes(Charset.forName("UTF-8")));
			
			File wprod = new File(configPath.getAbsolutePath() + "/webpack.config.prod.js");
			writeFileContent(wprod, FileUtilities.getFileContentWithReturns(loader.getResource("webpack.config.prod.js")).getBytes(Charset.forName("UTF-8")));
			
			File wServer = new File(configPath.getAbsolutePath() + "/webpackDevServer.config.js");
			writeFileContent(wServer, FileUtilities.getFileContentWithReturns(loader.getResource("webpackDevServer.config.js")).getBytes(Charset.forName("UTF-8")));
			
			File build = new File(scriptPath.getAbsolutePath() + "/build.js");
			writeFileContent(build, FileUtilities.getFileContentWithReturns(loader.getResource("build.js")).getBytes(Charset.forName("UTF-8")));
			
			File start = new File(scriptPath.getAbsolutePath() + "/start.js");
			writeFileContent(start, FileUtilities.getFileContentWithReturns(loader.getResource("start.js")).getBytes(Charset.forName("UTF-8")));
			
			File test = new File(scriptPath.getAbsolutePath() + "/test.js");
			writeFileContent(test, FileUtilities.getFileContentWithReturns(loader.getResource("test.js")).getBytes(Charset.forName("UTF-8")));
			item.progressAmount().set(.4);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		File publicPath = new File(projectLocation + "/public");
		publicPath.mkdirs();
		
		File electronJS = new File(publicPath.getAbsoluteFile() + "/electron.js");
		writeFileContent(electronJS, ElectronProjectFileHelper.ELECTRONJS.getBytes(Charset.forName("UTF-8")));
		
		File indexHTML = new File(publicPath.getAbsoluteFile() + "/index.html");
		writeFileContent(indexHTML, String.format(ElectronProjectFileHelper.INDEXHTML, gameName, extension).getBytes(Charset.forName("UTF-8")));
		
		File manifestJSON = new File(publicPath.getAbsoluteFile() + "/manifest.json");
		writeFileContent(manifestJSON, String.format(ElectronProjectFileHelper.MANIFESTJSON, gameName, extension).getBytes(Charset.forName("UTF-8")));
		
		item.progressAmount().set(.5);
		File srcPath = new File(projectLocation + "/src");
		srcPath.mkdirs();
		
		String appbg = buildGameBackground(srcPath);
		File appcss = new File(srcPath.getAbsoluteFile() + "/App.css");
		writeFileContent(appcss, String.format(ElectronProjectFileHelper.APPCSS, appbg).getBytes(Charset.forName("UTF-8")));
		
		String gameFilePath = "./" + gameName + ".xml";
		File appJS = new File(srcPath.getAbsoluteFile() + "/App.js");
		writeFileContent(appJS, String.format(ElectronProjectFileHelper.APPJS, appbg, gameFilePath).getBytes(Charset.forName("UTF-8")));
		
		File appTestJS = new File(srcPath.getAbsoluteFile() + "/App.test.js");
		writeFileContent(appTestJS, ElectronProjectFileHelper.APPTESTJS.getBytes(Charset.forName("UTF-8")));
		
		File indexcss = new File(srcPath.getAbsoluteFile() + "/index.css");
		writeFileContent(indexcss, ElectronProjectFileHelper.INDEXCSS.getBytes(Charset.forName("UTF-8")));
		
		File indexJS = new File(srcPath.getAbsoluteFile() + "/index.js");
		writeFileContent(indexJS, ElectronProjectFileHelper.INDEXJS.getBytes(Charset.forName("UTF-8")));
		
		File rsw = new File(srcPath.getAbsoluteFile() + "/registerServiceWorker.js");
		writeFileContent(rsw, ElectronProjectFileHelper.REGISTERSERVICEWORKERJS.getBytes(Charset.forName("UTF-8")));
		
		item.progressAmount().set(.6);
		
		try {
			LogRunner.logger().info("Moving game icon.");
			
			File srcIcon = new File(String.format("%s/logo%s", srcPath.getAbsolutePath(), extension));
			Files.copy(originalIcon.toPath(), srcIcon.toPath());
			
			File publicIcon = new File(String.format("%s/favicon%s", publicPath.getAbsolutePath(), extension));
			File publicIcon2 = new File(String.format("%s/fav%s", publicPath.getAbsolutePath(), extension));
			Files.copy(originalIcon.toPath(), publicIcon.toPath());
			Files.copy(originalIcon.toPath(), publicIcon2.toPath());
			item.progressAmount().set(.7);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		File assets = new File(srcPath.getAbsolutePath() + "/images");
		if (!assets.exists()) {
			assets.mkdirs();
		}
		
		buildGameFile(srcPath, gameName, assets, "./static/media");
		item.progressAmount().set(.8);
		buildAssetImport(assets);
		item.progressAmount().set(1.0);
	}
	
	private void buildAssetImport(File assetPath) {
		StringBuilder sb = new StringBuilder();
		File[] assets = assetPath.listFiles();
		
		for (int i = 0; i < assets.length; i++) {
			sb.append("import './" + assets[i].getName() + "';\r\n");
		}
		
		File assetLoader = new File(assetPath.getAbsoluteFile() + "/assetLoader.js");
		writeFileContent(assetLoader, sb.toString().getBytes(Charset.forName("UTF-8")));
	}
	
	private void compile(String projectLocation, StatusItem item) {
		try {
			item.displayText().set(languageService.getValue(DisplayStrings.COMPLILING));
			item.indicator().set(StatusIndicator.Normal);
			item.progressAmount().set(.1);
			
			Process proc = getCompileProcess(projectLocation);
			
			item.progressAmount().set(.5);
			
			if (proc == null) {
				LogRunner.logger().info("Unable to build compile process!");
				return;
			}
			
			ProcessHelpers.handleProcessStreams(proc);
			proc.waitFor();
			
			item.progressAmount().set(1.0);
		} catch (Exception e) {
			item.indicator().set(StatusIndicator.Error);
		}
	}
	
	private Process getCompileProcess(String projectLocation) throws IOException {
		Process retVal = null;
		
		String driveLetter = projectLocation.substring(0, 2);
		
		if (EnvironmentUtilities.isWindows()) {
			LogRunner.logger().info("Determined environment was windows. Building windows compile process.");
			retVal = Runtime.getRuntime().exec(new String[] {"cmd", "/c", driveLetter + " && cd " + projectLocation + " && npm install && npm run dist"});
		} else if (EnvironmentUtilities.isUnix()) {
			LogRunner.logger().info("Determined environment was linux. Building linux compile process");
			retVal = Runtime.getRuntime().exec(new String[] {"cd " + projectLocation + " && npm install && npm run dist"});
		} else if (EnvironmentUtilities.isMac()) {
			LogRunner.logger().info("Determined environment was mac. Building mac compile process");
			retVal = Runtime.getRuntime().exec(new String[] {"cd " + projectLocation + " && npm install && npm run dist"});
		}
		
		return retVal;
	}
	
	private void shipCompiled(String location, StatusItem item, String gameName) {
		item.displayText().set(languageService.getValue(DisplayStrings.MOVING_PROJECT));
		item.indicator().set(StatusIndicator.Normal);
		String appName = gameName + " Setup 0.1.0.exe";
		File app = new File(location + "/dist/" + appName);
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
			retVal = Files.createTempDirectory("electronbuild").toFile().getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retVal;
	}
}
