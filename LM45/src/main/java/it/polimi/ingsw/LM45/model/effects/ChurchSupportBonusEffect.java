package it.polimi.ingsw.LM45.model.effects;

import it.polimi.ingsw.LM45.model.core.Resource;

/**
 * This is an effect granting a player a bonus when he supports the Church
 * 
 * @author Tommy
 *
 */
public class ChurchSupportBonusEffect extends Effect {

	private static final long serialVersionUID = 1L;
	
	private Resource resource;
	
	/**
	 * @param resource the resource representing the bonus gained when supporting the Church
	 */
	public ChurchSupportBonusEffect(Resource resource){
		this.resource = resource;
	}

	@Override
	public void resolveEffect(EffectResolutor effectResolutor) {
		effectResolutor.addChurchSupportBonus(resource);
	}
	
	@Override
	public String toString() {
		return "Whenever you support the Church you gain an additional " + resource.toString();
	}

}
