package it.polimi.ingsw.LM45.view.controller;

public class ViewInterfaceFactory {

	private ViewInterfaceFactory() {
	}

	public static ViewInterface create(ViewType viewType) {
		// TODO: add CLI
		switch (viewType) {
			case GUI:
				return new GuiController();
			//case CLI:
				
			default:
				return new GuiController();
		}
	}

}
