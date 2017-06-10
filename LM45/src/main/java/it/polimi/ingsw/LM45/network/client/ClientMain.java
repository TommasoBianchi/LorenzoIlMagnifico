package it.polimi.ingsw.LM45.network.client;

import java.util.Scanner;

import it.polimi.ingsw.LM45.model.core.FamiliarColor;
import it.polimi.ingsw.LM45.model.core.SlotType;

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
		
		ClientController clientController = new ClientController(types[selectedType], "127.0.0.1");

		System.out.println("Insert your username");
		String username = scanner.nextLine();
		
		clientController.login(username);
		
		scanner.nextLine();
		
		clientController.placeFamiliar(FamiliarColor.BLACK, SlotType.BUILDING, 99);
		
		scanner.close();
	}
	
}
