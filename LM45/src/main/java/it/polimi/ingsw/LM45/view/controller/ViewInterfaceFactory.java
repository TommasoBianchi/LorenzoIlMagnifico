package it.polimi.ingsw.LM45.view.controller;

public class ViewInterfaceFactory {

	private ViewInterfaceFactory() {
	}

	public static ViewInterface create(ViewType viewType) {
	
		switch (viewType) {
			case GUI:
				return new GuiController();
			
			case CLI:
				//return new CliController();
				
			default:
				return new GuiController();
		}
	}

}
