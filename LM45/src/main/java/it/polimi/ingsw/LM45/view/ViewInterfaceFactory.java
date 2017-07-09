package it.polimi.ingsw.LM45.view;

import it.polimi.ingsw.LM45.view.cli.CliController;
import it.polimi.ingsw.LM45.view.gui.GuiController;

public class ViewInterfaceFactory {

	private ViewInterfaceFactory() {
	}

	public static ViewInterface create(ViewType viewType) {	
		switch (viewType) {
			case GUI:
				return new GuiController();
			
			case CLI:
				return new CliController();
				
			default:
				return new GuiController();
		}
	}

}
