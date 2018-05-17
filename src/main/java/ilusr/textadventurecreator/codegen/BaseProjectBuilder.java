package ilusr.textadventurecreator.codegen;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

import ilusr.logrunner.LogRunner;
import ilusr.persistencelib.configuration.XmlConfigurationManager;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import textadventurelib.persistence.GameStatePersistenceObject;
import textadventurelib.persistence.OptionPersistenceObject;
import textadventurelib.persistence.SaveActionPersistenceObject;
import textadventurelib.persistence.TextAdventurePersistenceObject;

public abstract class BaseProjectBuilder implements IProjectBuilder {
	
	protected TextAdventureProjectPersistence persistence;
	
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
		try {
			LogRunner.logger().info("Building game assets.");
			if (!gameAssets.exists()) {
				gameAssets.mkdirs();
			}
			
			moveGameMedia(persistence.getTextAdventure(), gameAssets);
			exportGameStatesIfNeeded(persistence.getTextAdventure(), src.getAbsolutePath() + "/" + sanitizedGameName);
			
			XmlConfigurationManager man = new XmlConfigurationManager(src.getAbsolutePath() + "/" + sanitizedGameName + "/" + sanitizedGameName + ".xml");
			persistence.prepareXml();
			man.addConfigurationObject(persistence.getTextAdventure());
			man.save();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void moveGameMedia(TextAdventurePersistenceObject textAdventure, File assets) {
		for (GameStatePersistenceObject gameState : textAdventure.gameStates()) {
			if (gameState.layout().getLayoutContent() == null || gameState.layout().getLayoutContent().isEmpty()) {
				continue;
			}
			
			String newFile = tryCopyToAssets(gameState.layout().getLayoutContent(), assets);
			if (!newFile.isEmpty()) {
				LogRunner.logger().info(String.format("Moving layout file %s to %s", gameState.layout().getLayoutContent(), newFile));
				gameState.layout().setLayoutContent(newFile);
			}
		}
	}
	
	private String tryCopyToAssets(String original, File assets) {
		String retVal = "";
		try {
			File originalFile = new File(original);
			
			String newName = new String();
			if (original.contains("/")) {
				newName = original.substring(original.lastIndexOf('/'));
			} else {
				newName = original.substring(original.lastIndexOf('\\'));
			}
			
			File newMedia = new File(assets.getAbsolutePath() + newName);
			Files.copy(originalFile.toPath(), newMedia.toPath());
			retVal = newMedia.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return retVal;
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
}
