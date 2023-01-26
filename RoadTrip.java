import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Scanner;

public class RoadTrip {
    private static final int NUM_SPACES = 36;
    private static final int MIN_TRAPS_BONUSES = 9;
    private static final int DIE_SIDES = 6;
    private static final int FUEL_CAPACITY = 10;

    private Digraph board;
    private int[] playerPositions;
    private int[] playerFuel;
    private boolean[] traps;
    private boolean[] bonuses;
    private boolean[] fuelStations;
    private boolean[] roadBlocks;
    private String[] weatherConditions;
    private Scanner scanner;
    private int numPlayers;
    private int numTrapsBonuses;

    public RoadTrip() {
        scanner = new Scanner(System.in);
        //read number of players
        System.out.print("Enter number of players (1-4): ");
        numPlayers = scanner.nextInt();
        while (numPlayers < 1 || numPlayers > 4) {
            System.out.print("Invalid input. Enter number of players (1-4): ");
            numPlayers = scanner.nextInt();
        }
        // read number of traps and bonuses
        System.out.print("Enter number of traps and bonuses (minimum 9):");
        while (numTrapsBonuses < MIN_TRAPS_BONUSES) {
            System.out.print("Invalid input. Enter number of traps and bonuses (minimum 9): ");
            numTrapsBonuses = scanner.nextInt();
        }
        // initialize player positions and traps/bonuses/fuelStations/roadBlocks/weatherConditions
        playerPositions = new int[numPlayers];
        playerFuel = new int[numPlayers];
        traps = new boolean[NUM_SPACES];
        bonuses = new boolean[NUM_SPACES];
        fuelStations = new boolean[NUM_SPACES];
        roadBlocks = new boolean[NUM_SPACES];
        weatherConditions = new String[NUM_SPACES];
        for (int i = 0; i < numPlayers; i++) {
            playerPositions[i] = 0; // set all players to start position
            playerFuel[i] = FUEL_CAPACITY; // set all players to have full fuel
        }
        for (int i = 0; i < numTrapsBonuses; i++) {
            int trapOrBonus = (int)(Math.random()* NUM_SPACES);
            if (i < numTrapsBonuses / 4) {
                traps[trapOrBonus] = true;
            } else if (i < numTrapsBonuses / 2) {
                bonuses[trapOrBonus] = true;
            } else if (i < numTrapsBonuses * 3 / 4) {
                fuelStations[trapOrBonus] = true;
            } else {
                roadBlocks[trapOrBonus] = true;
            }
        }
        // initialize weather conditions
        for (int i = 0; i < NUM_SPACES; i++) {
            int weather = (int)(Math.random()* 3);
            if (weather == 0) {
                weatherConditions[i] = "Sunny";
            } else if (weather == 1) {
                weatherConditions[i] = "Rainy";
            } else {
                weatherConditions[i] = "Snowy";
            }
        }
    }

    public void initBoard() {
        // read in node table from text file
        In in = new In("road_trip_table.txt");
        board = new Digraph(in);
    }

    public void play() {
        while (true) {
            for (int i = 0; i < numPlayers; i++) {
                // roll the die
                int move = rollDie();
                int nextPos = playerPositions[i] + move;
                if (nextPos >= NUM_SPACES) {
                    nextPos = NUM_SPACES - 1;
                }
                playerPositions[i] = nextPos;
                // check for traps and bonuses
                checkForTrapsBonuses(nextPos, i);
                // check for fuel station
                checkForFuel(nextPos, i);
                // check for road block
                checkForRoadBlock(nextPos, i);
                // check for weather
                checkForWeather(nextPos, i);
                // check for destination
                checkForDestination(nextPos, i);
                // check for out of fuel
                checkForFuelOut(i);
                // show player positions
                showPlayerPositions();
            }
        }
    }

    public int rollDie() {
        return (int)(Math.random()* DIE_SIDES) + 1;
    }

    public void checkForTrapsBonuses(int nextPos, int i) {
        if (traps[nextPos]) {
            // player encounters a trap
            System.out.println("Player " + (i + 1) + " encountered a trap at space " + (nextPos + 1));
        } else if (bonuses[nextPos]) {
            // player encounters a bonus
            System.out.println("Player " + (i + 1) + " encountered a bonus at space " + (nextPos + 1));
        }
    }

    public void checkForFuel(int nextPos, int i) {
        if (fuelStations[nextPos]) {
            // player encounters a fuel station
            System.out.println("Player " + (i + 1) + " encountered a fuel station at space " + (nextPos + 1));
            playerFuel[i] = FUEL_CAPACITY;
        }
    }

    public void checkForRoadBlock(int nextPos, int i) {
        if (roadBlocks[nextPos]) {
            // player encounters a road block
            System.out.println("Player " + (i + 1) + " encountered a road block at space " + (nextPos + 1));
            playerFuel[i] -= 2;
        }
    }

    public void checkForWeather(int nextPos, int i) {
        if (!weatherConditions[nextPos].equals("Sunny")) {
            // player encounters bad weather
            System.out.println("Player " + (i + 1) + " encountered " + weatherConditions[nextPos] + " weather at space " + (nextPos + 1));
            playerFuel[i]--;
        }
    }

    public void checkForDestination(int nextPos, int i) {
        if (nextPos == NUM_SPACES - 1) {
            StdOut.println("Player " + (i + 1) + " reached the destination!");
            return;
        }
    }

    public void checkForFuelOut(int i) {
        if (playerFuel[i] <= 0) {
            StdOut.println("Player " + (i + 1) + " ran out of fuel!");
            return;
        }
    }

    public void showPlayerPositions() {
        for (int i = 0; i < numPlayers; i++) {
            System.out.println("Player " + (i + 1) + " is at space " + (playerPositions[i] + 1));
        }
    }

    public static void main(String[] args) {
        RoadTrip game = new RoadTrip();
        game.initBoard();
        game.play();
    }
}


