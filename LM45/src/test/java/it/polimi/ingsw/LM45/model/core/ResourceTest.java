package it.polimi.ingsw.LM45.model.core;

import java.util.Random;

import junit.framework.TestCase;
import testUtilities.Helper;

public class ResourceTest extends TestCase {
	
	private Random random = new Random();

	public void testGetResourceType() {
		for(ResourceType resourceType : ResourceType.values())
			assertEquals(new Resource(resourceType, random.nextInt(10)).getResourceType(), resourceType);
	}

	public void testGetAmount() {
		ResourceType[] resourceTypes = ResourceType.values();
		for(int i = 0; i < 10; i++)
			assertEquals(new Resource(resourceTypes[random.nextInt(resourceTypes.length)], i).getAmount(), i);
	}

	public void testIncrement() {
		for(int i = 0; i < 10; i++){
			Resource resource = Helper.randomResource();
			int increment = random.nextInt(11) - 5;
			Resource incrementedResource = resource.increment(increment);
			assertEquals(incrementedResource.getAmount(), resource.getAmount() + increment);
			assertEquals(incrementedResource.getResourceType(), resource.getResourceType());
		}
	}

	public void testMultiply() {
		for(int i = 0; i < 10; i++){
			Resource resource = Helper.randomResource();
			int multiplier = random.nextInt(11) - 5;
			Resource incrementedResource = resource.multiply(multiplier);
			assertEquals(incrementedResource.getAmount(), resource.getAmount() * multiplier);
			assertEquals(incrementedResource.getResourceType(), resource.getResourceType());
		}
	}
	
	public void testToString(){
		for(int i = 0; i < 10; i++){
			Resource resource = Helper.randomResource();
			assertEquals(resource.toString(), resource.getAmount() + " " + resource.getResourceType());
		}
	}

}
