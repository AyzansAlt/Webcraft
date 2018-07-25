package net.discraft.mod;

public class DiscraftLogger {

    /**
     * Print Error - Prints an Log Error Message to Console with a specific terminal color
     *
     * @param givenPrefix  - Given Prefix
     * @param givenMessage - Given Message
     */
    public void printError(String givenPrefix, String givenMessage) {
        /* Prints out to the Console Log */
        System.out.println("\u001B[33m" + "[" + givenPrefix + "] " + givenMessage + "\u001B[0m");
    }

    /**
     * Print Line - Prints a Log Message to Console with a specific terminal color
     *
     * @param givenPrefix  - Given Prefix
     * @param givenMessage - Given Message
     */
    public void printLine(String givenPrefix, String givenMessage) {
        /* Prints out to the Console Log */
        System.out.println("\u001B[36m" + "[" + givenPrefix + "] " + givenMessage + "\u001B[0m");
    }

}