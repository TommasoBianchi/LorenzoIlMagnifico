package it.polimi.ingsw.LM45.view;

import it.polimi.ingsw.LM45.controller.ClientLauncher;
import it.polimi.ingsw.LM45.view.cli.CliController;
import it.polimi.ingsw.LM45.view.gui.GuiController;

/**
 * Based on the viewType it creates the appropriate controller
 * to handle the view : GuiController or CliController
 * 
 * @author Kostandin
 *
 */
public class ViewInterfaceFactory {

	private ViewInterfaceFactory() {
	}

	/**
	 * @param viewType
	 *            the type of the view
	 * @return a viewInterface : GuiController or CliController
	 */
	public static ViewInterface create(ViewType viewType) {
		switch (viewType) {
		case GUI:
			return new GuiController();

		case CLI:
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {				
				@Override
				public void run() {
					System.out.println("CLI shutdown detected");
					ClientLauncher.stop(ViewType.CLI);
				}
			}));
			return new CliController();

		default:
			return new GuiController();
		}
	}

}
