package it.polimi.ingsw.LM45.network.client;

import java.io.IOException;
import java.util.Scanner;

import it.polimi.ingsw.LM45.controller.ClientLauncher;

public class ClientMain {
	
	public static final Scanner scanner = new Scanner(System.in);

	public static void main(String[] args){
		
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
		System.out.println("Insert your username");
		String username = scanner.nextLine();
		
		try {
			ClientLauncher.launch(username, "127.0.0.1", 7000, types[selectedType] == ConnectionType.RMI, true);
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
