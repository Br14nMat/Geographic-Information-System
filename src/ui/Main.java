package ui;

import model.Controller;

import java.util.Scanner;

public class Main {

    private Scanner sc;
    private Controller controller;

    public Main(){
        sc = new Scanner(System.in);
        controller = new Controller();
    }

    public static void main(String[] args) {

        Main app = new Main();
        app.mainMenu();

    }

    public void mainMenu(){

        boolean exit = false;
        int option = 0;

        while (!exit) {

            System.out.println("1. Insert command");
            System.out.println("2. Import SQL file");
            System.out.println("3. Exit");
            option = sc.nextInt();
            sc.nextLine();

            switch (option) {

                case 1:
                    insertCommand();
                    break;
                case 2:
                    importSQL();
                    break;
                case 3:
                    System.out.println("Saving data...");
                    controller.saveData();
                    exit = true;
                    break;
                default:
                    System.out.println("Enter a valid option");
                    break;
            }

        }

    }

    public void insertCommand(){

        String command = "";

        System.out.println("Insert the command");
        command = sc.nextLine();

        String response = controller.executeCommand(command);
        System.out.println(response);
    }

    public void importSQL(){

        System.out.println("Running file chooser...");
        System.out.println("Choose txt file");
        Chooser chooser = new Chooser();
        String path =  chooser.getPath();

        if(!path.isEmpty())
            System.out.println(controller.importData(path));

    }


}
