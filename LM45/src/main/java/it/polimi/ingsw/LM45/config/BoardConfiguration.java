package it.polimi.ingsw.LM45.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import it.polimi.ingsw.LM45.model.core.Resource;
import it.polimi.ingsw.LM45.model.core.SlotType;

public class BoardConfiguration implements Configuration, Serializable {

	private static final long serialVersionUID = 1L;

	private class SlotConfiguration implements Serializable {
		
		private static final long serialVersionUID = 1L;

		private SlotType slotType;
		private int slotID;
		private Resource[] bonuses;

		public SlotConfiguration(SlotType slotType, int slotID, Resource[] bonuses) {
			this.slotType = slotType;
			this.slotID = slotID;
			this.bonuses = bonuses;
		}
	}

	private SlotConfiguration[] slotConfigurations;
	private Resource[][] churchSupportResources;

	public BoardConfiguration(Map<SlotType, Resource[][]> slotConfigurations, Resource[][] churchSupportResources) {
		List<SlotConfiguration> usefulSlotConfiguration = new ArrayList<>();
		
		for(Entry<SlotType, Resource[][]> entry : slotConfigurations.entrySet()){
			SlotType slotType = entry.getKey();
			Resource[][] resArr = entry.getValue();
			for(int i = 0; i < resArr.length; i++){
				if(resArr[i].length > 0)
					usefulSlotConfiguration.add(new SlotConfiguration(slotType, i, resArr[i]));
			}
		}
		
		this.slotConfigurations = usefulSlotConfiguration.stream().toArray(SlotConfiguration[]::new);
		this.churchSupportResources = churchSupportResources.clone();
	}

	public Resource[] getSlotBonuses(SlotType slotType, int slotID) {
		return Arrays.stream(slotConfigurations)
				.filter(slotConfiguration -> slotConfiguration.slotType == slotType
						&& slotConfiguration.slotID == slotID)
				.map(slotConfiguration -> slotConfiguration.bonuses).findFirst().orElse(new Resource[] {}).clone();
	}
	
	public Resource[][] getChurchSupportResources(){
		return churchSupportResources.clone();
	}

}
