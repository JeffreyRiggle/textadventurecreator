package ilusr.textadventurecreator.codegen;

import java.io.*;
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

public class HtmlProjectBuilder extends BaseProjectBuilder {

	public HtmlProjectBuilder(TextAdventureProjectPersistence persistence, ILanguageService languageService) {
		super(persistence, languageService);
	}

	@Override
	public void build(StatusItem item) {
		LogRunner.logger().info(String.format("Building html project for %s", persistence.getGameInfo().gameName()));
		
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
		
		try {
			compile(projectLocation, item);
			shipCompiled(projectLocation, item, sanitizedGameName);
			cleanTemp(projectLocation.substring(0, projectLocation.lastIndexOf('/')), item);	
		} catch (Exception e) {
			e.printStackTrace();
			item.indicator().set(StatusIndicator.Error);
		} finally {
			item.finished().set(true);
		}
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
		writeFileContent(gitIgnoreFile, String.format(HtmlProjectFileHelper.GITIGNORE).getBytes(Charset.forName("UTF-8")));
		
		File packageFile = new File(project.getAbsoluteFile() + "/package.json");
		writeFileContent(packageFile, String.format(HtmlProjectFileHelper.PACKAGEJSON, gameName, author, description).getBytes(Charset.forName("UTF-8")));

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
			writeFileContent(cssTransform, loader.getResource("cssTransform.js"));
			
			File fileTransform = new File(jestPath.getAbsolutePath() + "/fileTransform.js");
			writeFileContent(fileTransform, loader.getResource("fileTransform.js"));	
			
			File env = new File(configPath.getAbsolutePath() + "/env.js");
			writeFileContent(env, loader.getResource("env.js"));
			
			File paths = new File(configPath.getAbsolutePath() + "/paths.js");
			writeFileContent(paths, loader.getResource("paths.js"));
			
			File poly = new File(configPath.getAbsolutePath() + "/polyfills.js");
			writeFileContent(poly, loader.getResource("polyfills.js"));
			
			File wdev = new File(configPath.getAbsolutePath() + "/webpack.config.dev.js");
			writeFileContent(wdev, loader.getResource("webpack.config.dev.js"));
			
			File wprod = new File(configPath.getAbsolutePath() + "/webpack.config.prod.js");
			writeFileContent(wprod, loader.getResource("webpack.config.prod.js"));
			
			File wServer = new File(configPath.getAbsolutePath() + "/webpackDevServer.config.js");
			writeFileContent(wServer, loader.getResource("webpackDevServer.config.js"));
			
			File build = new File(scriptPath.getAbsolutePath() + "/build.js");
			writeFileContent(build, loader.getResource("build.js"));
			
			File start = new File(scriptPath.getAbsolutePath() + "/start.js");
			writeFileContent(start, loader.getResource("start.js"));
			
			File test = new File(scriptPath.getAbsolutePath() + "/test.js");
			writeFileContent(test, loader.getResource("test.js"));
			item.progressAmount().set(.4);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		File publicPath = new File(projectLocation + "/public");
		publicPath.mkdirs();
		
		File indexHTML = new File(publicPath.getAbsoluteFile() + "/index.html");
		writeFileContent(indexHTML, String.format(HtmlProjectFileHelper.INDEXHTML, gameName, extension).getBytes(Charset.forName("UTF-8")));
		
		File manifestJSON = new File(publicPath.getAbsoluteFile() + "/manifest.json");
		writeFileContent(manifestJSON, String.format(HtmlProjectFileHelper.MANIFESTJSON, gameName, extension).getBytes(Charset.forName("UTF-8")));
		
		item.progressAmount().set(.5);
		File srcPath = new File(projectLocation + "/src");
		srcPath.mkdirs();
		
		String appbg = buildGameBackground(srcPath);
		File appcss = new File(srcPath.getAbsoluteFile() + "/MainPage.css");
		writeFileContent(appcss, String.format(HtmlProjectFileHelper.MAINPAGECSS, appbg).getBytes(Charset.forName("UTF-8")));
		
		String gameFilePath = "./" + gameName + ".xml";
		File appJS = new File(srcPath.getAbsoluteFile() + "/App.js");
		writeFileContent(appJS, String.format(HtmlProjectFileHelper.APPJS, appbg).getBytes(Charset.forName("UTF-8")));
		
		File gameManagerJS = new File(srcPath.getAbsoluteFile() + "/gameManager.js");
		writeFileContent(gameManagerJS, String.format(HtmlProjectFileHelper.GAMEMANAGERJS, gameFilePath).getBytes(Charset.forName("UTF-8")));

		File mainPageJS = new File(srcPath.getAbsoluteFile() + "/MainPage.js");
		writeFileContent(mainPageJS, HtmlProjectFileHelper.MAINPAGEJS.getBytes(Charset.forName("UTF-8")));
		
		File appTestJS = new File(srcPath.getAbsoluteFile() + "/App.test.js");
		writeFileContent(appTestJS, HtmlProjectFileHelper.APPTESTJS.getBytes(Charset.forName("UTF-8")));
		
		File indexcss = new File(srcPath.getAbsoluteFile() + "/index.css");
		writeFileContent(indexcss, HtmlProjectFileHelper.INDEXCSS.getBytes(Charset.forName("UTF-8")));
		
		File indexJS = new File(srcPath.getAbsoluteFile() + "/index.js");
		writeFileContent(indexJS, HtmlProjectFileHelper.INDEXJS.getBytes(Charset.forName("UTF-8")));
		
		File rsw = new File(srcPath.getAbsoluteFile() + "/registerServiceWorker.js");
		writeFileContent(rsw, HtmlProjectFileHelper.REGISTERSERVICEWORKERJS.getBytes(Charset.forName("UTF-8")));
		
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
	
	private void compile(String projectLocation, StatusItem item) throws Exception {
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
	}
	
	private Process createBashProcess(String projectLocation) throws IOException {
		File buildScript = File.createTempFile("buildScript", null);
		Writer sw = new OutputStreamWriter(new FileOutputStream(buildScript));
		PrintWriter pw = new PrintWriter(sw);
		pw.println("#!/bin/bash");
		pw.println("cd " + projectLocation);
		pw.println("npm install");
		pw.println("npm run build");
		pw.println("rm " + buildScript.toString());
		pw.close();

		ProcessBuilder pb = new ProcessBuilder("bash", buildScript.toString());
		return pb.start();
	}

	private Process getCompileProcess(String projectLocation) throws IOException {
		Process retVal = null;
		
		String driveLetter = projectLocation.substring(0, 2);
		
		if (EnvironmentUtilities.isWindows()) {
			LogRunner.logger().info("Determined environment was windows. Building windows compile process.");
			retVal = Runtime.getRuntime().exec(new String[] {"cmd", "/c", driveLetter + " && cd " + projectLocation + " && npm install && npm run build"});
		} else if (EnvironmentUtilities.isUnix()) {
			LogRunner.logger().info("Determined environment was linux. Building linux compile process");
			retVal = createBashProcess(projectLocation);
		} else if (EnvironmentUtilities.isMac()) {
			LogRunner.logger().info("Determined environment was mac. Building mac compile process");
			retVal = Runtime.getRuntime().exec(new String[] {"cd " + projectLocation + " && npm install && npm run build"});
		}
		
		return retVal;
	}
	
	private void shipCompiled(String location, StatusItem item, String gameName) throws IOException {
		item.displayText().set(languageService.getValue(DisplayStrings.MOVING_PROJECT));
		item.indicator().set(StatusIndicator.Normal);
		File app = new File(location + "/build/");
		File target = new File(persistence.getProjectLocation());
		
		if (!target.exists()) {
			target.mkdirs();
		}
		
		LogRunner.logger().info(String.format("Shipping compiled project to %s with app %s", target.getAbsolutePath().toString(), app.getAbsolutePath().toString()));
		copyRelease(target, app, item);
		
		item.progressAmount().set(1.0);
		item.indicator().set(StatusIndicator.Good);
	}
	
	private void copyRelease(File target, File buildDir, StatusItem item) throws IOException {
		File[] buildFiles = buildDir.listFiles();
		for (int i = 0; i < buildFiles.length; i++) {
			File current = buildFiles[i];
			Files.copy(current.toPath(), new File(target.getAbsolutePath() + "/" + current.getName()).toPath());
			double progress = i / buildFiles.length;
			item.progressAmount().set(progress);
			
			if (current.isDirectory()) {
				copyRelease(new File(target.getAbsolutePath() + "/" + current.getName()), current, item);
			}
		}
	}
	
	private String getTempDir() {
		String retVal = new String();
		
		try {
			retVal = Files.createTempDirectory("htmlbuild").toFile().getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retVal;
	}
}
