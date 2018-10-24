package dk.aau.cs.ds306e18.tournament;

import dk.aau.cs.ds306e18.tournament.UI.Launcher;



public class Main  {
    public static void main(String[]args){
        System.out.println("hola mundo");
        startLauncher(args);


    }

    private static void startLauncher(String[] args) {
        Launcher launcher = new Launcher().startLauncher(args);
    }

}
