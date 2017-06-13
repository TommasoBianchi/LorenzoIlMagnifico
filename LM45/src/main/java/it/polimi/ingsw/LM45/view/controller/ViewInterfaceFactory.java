package it.polimi.ingsw.LM45.view.controller;

public class ViewInterfaceFactory {

	private ViewInterfaceFactory() {
	}

	public static ViewInterface create(ViewType viewType) {
		// TODO: implement!!
		switch (viewType) {
			case GUI:
				break;
			case CLI:
				break;

			default:
				break;
		}
		
		return null;
	}

}
