package testUtilities;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.ResourceType;

public class Helper {

	private static Random random = new Random();

	public static Resource randomResource() {
		ResourceType[] resourceTypes = new ResourceType[] { ResourceType.COINS, ResourceType.WOOD, ResourceType.STONE, ResourceType.SERVANTS,
				ResourceType.FAITH, ResourceType.VICTORY, ResourceType.MILITARY };
		return new Resource(resourceTypes[random.nextInt(resourceTypes.length)], random.nextInt(10) + 1);
	}

	public static boolean sameResources(Resource[] resources1, Resource[] resources2) {
		Map<ResourceType, Integer> resourceMap1 = Arrays.stream(resources1)
				.collect(Collectors.toMap(Resource::getResourceType, Resource::getAmount, Integer::sum));
		Map<ResourceType, Integer> resourceMap2 = Arrays.stream(resources2)
				.collect(Collectors.toMap(Resource::getResourceType, Resource::getAmount, Integer::sum));

		boolean result = resourceMap1.entrySet().stream()
				.allMatch(entry -> resourceMap2.containsKey(entry.getKey()) && resourceMap2.get(entry.getKey()) == entry.getValue());

		result = result && resourceMap2.entrySet().stream()
				.allMatch(entry -> resourceMap1.containsKey(entry.getKey()) && resourceMap1.get(entry.getKey()) == entry.getValue());

		return result;
	}

}
