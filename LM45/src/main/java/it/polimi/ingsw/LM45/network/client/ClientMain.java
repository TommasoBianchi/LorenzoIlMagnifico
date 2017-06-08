package it.polimi.ingsw.LM45.network.client;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Scanner;

import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.SlotType;
import it.polimi.ingsw.LM45.network.server.ServerInterface;

public class ClientMain {

	public static void main(String[] args){
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Choose your connection type");
		ConnectionType[] types = ConnectionType.values();
		for(int i = 0; i < types.length; i++)
			System.out.println(i + " - " + types[i]);
		System.out.println("");
		
		int selectedType = scanner.nextInt();
		scanner.nextLine();
		while(selectedType < 0 || selectedType >= types.length){
			System.out.println("Please insert a correct number");
			selectedType = scanner.nextInt();
			scanner.nextLine();
		}
		
		System.out.println("You have selected " + selectedType + " - " + types[selectedType]);
		
		ClientController clientController = new ClientController();
		ServerInterface serverInterface = null;
		
		try {
			serverInterface = ServerInterfaceFactory.create(types[selectedType], "127.0.0.1", clientController);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		System.out.println("Insert your username");
		String username = scanner.nextLine();
		
		try {
			serverInterface.login(username);
			/*serverInterface.playLeaderCard("Sisto IV");
			serverInterface.activateLeaderCard("Sisto IV");
			serverInterface.discardLeaderCard("Sisto IV");
			serverInterface.discardLeaderCard("Carta inesistente");
			serverInterface.increaseFamiliarValue(FamiliarColor.UNCOLORED);
			serverInterface.placeFamiliar(FamiliarColor.UNCOLORED, 5);
			serverInterface.endTurn();*/
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		scanner.nextLine();
		System.out.println("GGG");
		try {
			serverInterface.placeFamiliar(FamiliarColor.BLACK, SlotType.BUILDING, 99);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
