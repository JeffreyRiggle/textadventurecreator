package ilusr.textadventurecreator.codegen;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

import ilusr.core.io.FileUtilities;
import ilusr.logrunner.LogRunner;
import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.statusbars.StatusIndicator;
import ilusr.textadventurecreator.statusbars.StatusItem;
import textadventurelib.persistence.GameStatePersistenceObject;
import textadventurelib.persistence.OptionPersistenceObject;
import textadventurelib.persistence.SaveActionPersistenceObject;
import textadventurelib.persistence.TextAdventurePersistenceObject;

public abstract class BaseProjectBuilder implements IProjectBuilder {
	
	protected final TextAdventureProjectPersistence persistence;
	protected final ILanguageService languageService;
	
	public BaseProjectBuilder(TextAdventureProjectPersistence persistence, ILanguageService languageService) {
		this.persistence = persistence;
		this.languageService = languageService;
	}
	
	protected int writeFileContent(File file, byte[] content) {
		FileOutputStream out = null;
		int retVal = 0;
		try {
			file.createNewFile();
			out = new FileOutputStream(file);
			out.write(content);
		} catch (Exception e) {
			e.printStackTrace();
			retVal = -1;
		} finally {
			if (out == null) {
				return -1;
			}
			
			try {
				out.close();
			} catch (Exception e) { }
		}
		
		return retVal;
	}
	
	protected String buildGameBackground(File src) {
		String retVal = new String();
		try {
			if (!src.exists()) {
				src.mkdirs();
			}
			
			File originalIcon = new File(persistence.getBackgroundLocation());
			File gameIcon = new File(String.format("%s/%s", src.getAbsolutePath(), originalIcon.getName()));
			Files.copy(originalIcon.toPath(), gameIcon.toPath());
			
			retVal = gameIcon.getName();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retVal;
	}
	
	protected void buildGameFile(File src, String sanitizedGameName, File gameAssets) {
		buildGameFile(src, sanitizedGameName, gameAssets, new String());
	}
	
	protected void buildGameFile(File src, String sanitizedGameName, File gameAssets, String relativePath) {
		try {
			LogRunner.logger().info("Building game assets.");
			if (!gameAssets.exists()) {
				gameAssets.mkdirs();
			}
			
			moveGameMedia(persistence.getTextAdventure(), gameAssets, relativePath);
			exportGameStatesIfNeeded(persistence.getTextAdventure(), src.getAbsolutePath());
			
			XmlConfigurationManager man = new XmlConfigurationManager(src.getAbsolutePath() + "/" + sanitizedGameName + ".xml");
			persistence.prepareXml();
			man.addConfigurationObject(persistence.getTextAdventure());
			man.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void moveGameMedia(TextAdventurePersistenceObject textAdventure, File assets, String relativePath) {
		for (GameStatePersistenceObject gameState : textAdventure.gameStates()) {
			if (gameState.layout().getLayoutContent() == null || gameState.layout().getLayoutContent().isEmpty()) {
				continue;
			}
			
			String newFile = tryCopyToAssets(gameState.layout().getLayoutContent(), assets);
			if (!newFile.isEmpty()) {
				String location = relativePath.isEmpty() ? newFile : relativePath + getFileName(newFile);
				gameState.layout().setLayoutContent(location);
			}
		}
	}
	
	private String tryCopyToAssets(String original, File assets) {
		String retVal = "";
		try {
			File originalFile = new File(original);
			String newName = getFileName(original);
			
			File newMedia = new File(assets.getAbsolutePath() + newName);
			Files.copy(originalFile.toPath(), newMedia.toPath());
			retVal = newMedia.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retVal;
	}
	
	protected String getFileName(String file) {
		String newName = new String();
		
		if (file.contains("/")) {
			newName = file.substring(file.lastIndexOf('/'));
		} else {
			newName = "/" + file.substring(file.lastIndexOf('\\') + 1);
		}
		
		return newName;
	}
	
	private void exportGameStatesIfNeeded(TextAdventurePersistenceObject textAdventure, String path) {
		boolean exportDefined = !textAdventure.gameStatesInline();
		boolean shouldExport = false;
		
		for (GameStatePersistenceObject gameState : textAdventure.gameStates()) {
			for (OptionPersistenceObject option : gameState.options()) {
				if (!option.action().type().equals("Save")) {
					continue;
				}
				
				if (exportDefined) {
					((SaveActionPersistenceObject)option.action()).gameStatesLocation(textAdventure.gameStatesLocation());
				} else {
					((SaveActionPersistenceObject)option.action()).gameStatesLocation("./save/gamestates.xml");
					shouldExport = true;
				}
			}
		}
		
		if (!shouldExport) {
			LogRunner.logger().info("Not exporting game states.");
			return;
		}
		
		textAdventure.gameStatesInline(false);
		textAdventure.gameStatesLocation(path + "/gamestates.xml");
	}
	
	protected void cleanTemp(String project, StatusItem item) {
		LogRunner.logger().info("Cleaning up temp directory.");
		item.displayText().set(languageService.getValue(DisplayStrings.CLEANING_UP));
		item.indicator().set(StatusIndicator.Normal);
		item.progressAmount().set(.0);
		File proj = new File(project);
		
		File[] files = proj.listFiles();
		int iter = 1;
		
		for (File file : files) {
			FileUtilities.deleteDir(file);
			item.progressAmount().set(iter / files.length);
			iter++;
		}
		
		proj.delete();
		item.indicator().set(StatusIndicator.Good);
	}
}
