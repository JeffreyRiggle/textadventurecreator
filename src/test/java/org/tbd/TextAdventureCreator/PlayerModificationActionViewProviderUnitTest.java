package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.shell.TextAdventureProvider;
import ilusr.textadventurecreator.views.action.ChangePlayerPropertyViewProvider;
import ilusr.textadventurecreator.views.action.PlayerModificationActionViewProvider;
import ilusr.textadventurecreator.views.action.RemovePlayerPropertyViewProvider;
import ilusr.textadventurecreator.views.player.AttributeViewProvider;
import ilusr.textadventurecreator.views.player.BodyPartViewProvider;
import ilusr.textadventurecreator.views.player.CharacteristicViewProvider;
import ilusr.textadventurecreator.views.player.InventoryItemViewProvider;
import ilusr.textadventurecreator.views.player.InventorySelectorViewProvider;
import ilusr.textadventurecreator.views.player.RemoveCharacteristicViewProvider;
import textadventurelib.persistence.ModifyPlayerActionPersistence;
import textadventurelib.persistence.TextAdventurePersistenceObject;
import textadventurelib.persistence.player.PlayerPersistenceObject;

public class PlayerModificationActionViewProviderUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private ModifyPlayerActionPersistence action;
	private List<PlayerPersistenceObject> players;
	private ChangePlayerPropertyViewProvider changeProvider;
	private RemovePlayerPropertyViewProvider removeProvider;
	private AttributeViewProvider attributeProvider;
	private CharacteristicViewProvider characteristicProvider;
	private BodyPartViewProvider bodyPartProvider;
	private InventoryItemViewProvider inventoryViewProvider;
	private RemoveCharacteristicViewProvider removeCharacteristicProvider;
	private InventorySelectorViewProvider equipmentProvider;
	private ILanguageService languageService;
	
	@Before
	public void setup() {
		action = mock(ModifyPlayerActionPersistence.class);
		players = Arrays.asList(mock(PlayerPersistenceObject.class));
		changeProvider = mock(ChangePlayerPropertyViewProvider.class);
		removeProvider = mock(RemovePlayerPropertyViewProvider.class);
		attributeProvider = mock(AttributeViewProvider.class);
		characteristicProvider = mock(CharacteristicViewProvider.class);
		bodyPartProvider = mock(BodyPartViewProvider.class);
		inventoryViewProvider = mock(InventoryItemViewProvider.class);
		removeCharacteristicProvider = mock(RemoveCharacteristicViewProvider.class);
		equipmentProvider = mock(InventorySelectorViewProvider.class);
		languageService = mock(ILanguageService.class);
	}
	
	@Test
	public void testProvideWithProject() { 
		TextAdventurePersistenceObject tav = mock(TextAdventurePersistenceObject.class);
		when(tav.players()).thenReturn(players);
		TextAdventureProjectPersistence proj = mock(TextAdventureProjectPersistence.class);
		when(proj.getTextAdventure()).thenReturn(tav);
		TextAdventureProvider prov = mock(TextAdventureProvider.class);
		when(prov.getTextAdventureProject()).thenReturn(proj);
		
		PlayerModificationActionViewProvider provider = new PlayerModificationActionViewProvider(action, prov, changeProvider,
				removeProvider, attributeProvider, characteristicProvider, bodyPartProvider, inventoryViewProvider, removeCharacteristicProvider,
				equipmentProvider, languageService);
		
		assertNotNull(provider.getView());
	}

	@Test
	public void testProvideWithPlayers() {
		PlayerModificationActionViewProvider provider = new PlayerModificationActionViewProvider(action, players, changeProvider,
				removeProvider, attributeProvider, characteristicProvider, bodyPartProvider, inventoryViewProvider, removeCharacteristicProvider,
				equipmentProvider, languageService);
		
		assertNotNull(provider.getView());
	}
}
