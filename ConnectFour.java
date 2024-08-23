import java.awt.*;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ConnectFour {

    public static void main(String[] args) {
        // Initialising the game with a created template
        playConnectFour(createTemplate());
    }

    /**
     * This method returns a multidimensional array of size 6 x 7
     * @return gameBoard, the multidimensional array which serves as a template for the Connect Four game
     */
    public static char[][] createTemplate() {
        // Creating a new 6 x 7 multidimensional array
        char[][] gameBoard = new char[6][7];
        return gameBoard;
    }

    /**
     * This method prints out the elements in a multidimensional array from top to bottom
     * @param array the array which is printed out by the method
     */
    public static void printArray(char[][] array) {
        // Modify it to print from bottom up
        for (int i = array.length - 1; i >= 0; i--) {
            for (int j = array[i].length - 1; j > -1; j--) {
                /*
                Reversing the order in which a decrement would normally print out the characters
                If the character is null, a | and three empty spaces are printed
                */
                if (array[i][(array[i].length - 1) - j] == '\u0000') {
                    System.out.print("|");
                    System.out.print("   ");
                }
                // Printing out the bar and the character at the given position
                else {
                    System.out.print("|");
                    System.out.printf(" %c ",array[i][(array[i].length-1) - j]);
                }
            }
            // Printing out a last border and moving to a new line
            System.out.print("|");
            System.out.println();
        }
        System.out.println("-----------------------------");
    }

    /**
     * This method repeatedly asks the user for input and fills the board till there is a winner or
     * all the board spaces are filled.
     * @param gameBoard the multidimensional array which represents the game
     */
    public static void playConnectFour(char[][] gameBoard) {
        char nextPlay = '0'; // Initialising the disc for the game board
        int turns = 0; // Counting the total number of turns taken
        int userChoice = -1; // Taking note of where the user wants to play

        // Printing out the empty game board before the game starts
        printArray(gameBoard);

        Scanner ask = new Scanner(System.in);

        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                // Overriding the value of i in to fix errors in disc placement
                i = 0;
                // Determining the user turns using modulus operator
                if (turns%2 == 0){
                    // Even turns are for the red player
                    nextPlay = 'R';
                    System.out.print("Drop a red disc at column (0-6): ");
                    userChoice = ask.nextInt();
                }

                else if (turns%2 == 1){
                    // Odd turns are for the yellow player
                    nextPlay = 'Y';
                    System.out.print("Drop a yellow disc at column (0-6): ");
                    userChoice = ask.nextInt();
                }

                // Checking if the user's choice is an empty space
                if (gameBoard[i][userChoice] == '\u0000') {
                    gameBoard[i][(userChoice)] = nextPlay;
                    printArray(gameBoard);
                    turns++; // Incrementing turns after each turn
                }

                else{
                    // Added this to cater for the filling of the bottom most part of the grid first
                    i = 0;

                    // Using a try and catch since there may be an IndexOutOfBoundsException
                    try{
                        boolean done = false;
                        // Initialising position to i, which in this case is 0
                        int position = i;
                        do {
                            // Testing if the next position of the column is empty
                            if (gameBoard[position][(userChoice)] == '\u0000'){
                                gameBoard[position][(userChoice)] = nextPlay;
                                done = true; // Changing the boolean value to terminate the loop
                                turns++; // Incrementing turns only after the turn is complete
                            }

                            // Incrementing position by one to check for the next column position
                            position++;
                        } while (!done);

                        // Printing the game board after the user has made their choice
                        printArray(gameBoard);

                    } catch (IndexOutOfBoundsException ioe){
                        // Printing out an error message if all the positions in the column are occupied
                        System.out.println("This column is full");

                    }

                }

                // checkMatch has many cases to see whether there is a winner after every iteration
                if (checkMatch(gameBoard)){
                    System.exit(0);
                }

                // checkFullGrid checks whether the board has been filled without a winner emerging
                if (checkFullGrid(gameBoard)){
                    System.out.print("The game has ended in a draw");
                    System.exit(0);
                }

                }
            }
        }

    /**
     * This method checks whether any of the two players have made a winning math
     * @param array - the game board array
     * @return a boolean based on whether a math is found or not
     */
    public static boolean checkMatch(char[][] array) {
        // Boolean to be returned if none of the match conditions are met
        boolean finalResult = false;

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                for (char element : array[i]) {
                    // Checking that either R or Y is present consecutively at least four times
                    if ((searchArrayIndex('R', array[i]).length >= 4) && checkOrder(array[i], 'R')) {
                        System.out.print("The red player won");
                        return true;
                    } else if ((searchArrayIndex('Y', array[i]).length >= 4) && checkOrder(array[i], 'Y')) {
                        System.out.print("The yellow player won");
                        return true;
                    }

                }
                for (int a = 0; a < 6 ;a++){
                    // Checking whether each row has R or Y present at least four times consecutively
                    char[] tempArray = createHorizontalArray(array, a);
                    if((searchArrayIndex('Y', tempArray).length >= 4 && checkOrder(tempArray, 'Y'))){
                        System.out.print("The yellow player won");
                        return true;
                    }
                    else if ((searchArrayIndex('R', tempArray).length >= 4) && checkOrder(tempArray, 'R')){
                        System.out.print("The red player won");
                        return true;
                    }
                }

                for (int b = 0; b < 7; b++){
                    // Checking whether each colum has either R or Y present at least four times consecutively
                    char[] tempArray = createVerticalArray(array, b);
                    if((searchArrayIndex('Y', tempArray).length >= 4) && checkOrder(tempArray, 'Y')){
                        System.out.print("The yellow player won");
                        return true;
                    }
                    else if ((searchArrayIndex('R', tempArray).length >= 4) && checkOrder(tempArray, 'R')){
                        System.out.print("The red player won");
                        return true;
                    }
                }

                // Blocks of code catering for the twelve possible diagonal lines of characters

                /* In each of the twelve cases, checkOrder checks that each diagonal array has
                    either R or Y at least four times in consecutive order
                 */

                // Diagonal 1
                if (checkOrder(createDiagonalArrayDown(array, 3, 0, 4), 'R')){
                    System.out.print("The red player won");
                    return true;
                }
                else if (checkOrder(createDiagonalArrayDown(array, 3, 0, 4), 'Y')) {
                    System.out.print("The yellow player won");
                    return true;
                }

                // Diagonal 2
                if (checkOrder(createDiagonalArrayDown(array, 4, 0, 5), 'R')){
                    System.out.print("The red player won");
                    return true;
                }
                else if (checkOrder(createDiagonalArrayDown(array, 4, 0, 5), 'Y')){
                    System.out.println("The yellow player won");
                    return true;
                }

                // Diagonal 3
                if (checkOrder(createDiagonalArrayDown(array, 5, 0, 6), 'R')){
                    System.out.print("The red player won");
                    return true;
                }
                else if (checkOrder(createDiagonalArrayDown(array, 5, 0, 6), 'Y')){
                    System.out.print("The yellow player won");
                    return true;
                }

                // Diagonal 4
                if (checkOrder(createDiagonalArrayDown(array, 5, 1, 6), 'R')){
                    System.out.print("The red player won");
                    return true;
                }
                else if (checkOrder(createDiagonalArrayDown(array, 5, 1, 6), 'Y')) {
                    System.out.print("The yellow player won");
                    return true;
                }

                // Diagonal 5
                if (checkOrder(createDiagonalArrayDown(array, 5, 2, 5), 'R')){
                    System.out.print("The red player won");
                    return true;
                }
                else if (checkOrder(createDiagonalArrayDown(array, 5, 2, 5), 'Y')){
                    System.out.print("The yellow player won");
                    return true;
                }

                // Diagonal 6
                if (checkOrder(createDiagonalArrayDown(array, 5, 3, 4), 'R')){
                    System.out.print("The red player won");
                    return true;
                }
                else if (checkOrder(createDiagonalArrayDown(array, 5, 3, 4), 'Y')){
                    System.out.print("The yellow player won");
                    return true;
                }

                // Diagonal 7
                if (checkOrder(createDiagonalArrayUp(array, 0, 3, 4), 'R')){
                    System.out.print("The red player won");
                    return true;
                }
                else if (checkOrder(createDiagonalArrayUp(array, 0, 3, 4), 'Y')){
                    System.out.print("The yellow player won");
                    return true;
                }

                // Diagonal 8
                if (checkOrder(createDiagonalArrayUp(array, 0, 2, 5), 'R')){
                    System.out.print("The red player won");
                    return true;
                }
                else if (checkOrder(createDiagonalArrayUp(array, 0, 2, 5), 'Y')){
                    System.out.print("The yellow player won");
                    return true;
                }

                // Diagonal 9
                if (checkOrder(createDiagonalArrayUp(array, 0, 1, 6), 'R')){
                    System.out.print("The red player won");
                    return true;
                }
                else if (checkOrder(createDiagonalArrayUp(array, 0, 1, 6), 'Y')){
                    System.out.print("The yellow player won");
                    return true;
                }

                // Diagonal 10
                if (checkOrder(createDiagonalArrayUp(array, 0, 0, 6), 'R')){
                    System.out.print("The red player won");
                    return true;
                }
                else if (checkOrder(createDiagonalArrayUp(array, 0, 0, 6), 'Y')){
                    System.out.print("The yellow player won");
                    return true;
                }

                // Diagonal 11
                if (checkOrder(createDiagonalArrayUp(array, 1, 0, 5), 'R')){
                    System.out.print("The red player won");
                    return true;
                }
                else if (checkOrder(createDiagonalArrayUp(array, 1, 0, 5), 'Y')){
                    System.out.print("The yellow player won");
                    return true;
                }

                // Diagonal 12
                if (checkOrder(createDiagonalArrayUp(array, 2, 0, 4), 'R')){
                    System.out.print("The red player won");
                    return true;
                }
                else if (checkOrder(createDiagonalArrayUp(array, 2, 0, 4), 'Y')){
                    System.out.print("The yellow player won");
                    return true;
                }
            }
        }
        // Returning finalResult (false) if none of the conditions are met
        return finalResult;
    }

    /**
     * This method returns the indexes at which the character look has been found in the array
     * @param look the character which is looked for in the character array
     * @param array the array which is searched for the character look
     * @return finalAnswer, an array of the indexes
     */
    public static int[] searchArrayIndex(char look, char[] array){
        int[] finalAnswer = new int[array.length];
        int numberOfElements = 0; // Count variable for the appropriate return array length

        for (int i = 0; i < array.length; i++) {
            if (array[i] == look) {
                finalAnswer[numberOfElements] = i; // Keeping track of the indexes
                numberOfElements += 1;
            }
        }
        // Shortening the return array to only the number of elements which were found
        return Arrays.copyOf(finalAnswer, numberOfElements);
    }

    /**
     * This method returns an array representing a single row of a multidimensional array
     * @param multiArray the multidimensional array from which a row is copied
     * @param a the index of the row to be copied
     * @return a new array with the contents of the multidimensional array at a given array
     */
    public static char[] createHorizontalArray(char[][] multiArray, int a){
        // Creating an array for each row of the multidimensional array
        char[] newArray = new char[6];

        for (int i = 0; i < newArray.length-1; i++){
            // Assigning each row position's element to the next newArray vacancy
            newArray[i] = multiArray[a][i];
        }
        return newArray;
    }

    /**
     * This method returns an array representing a whole column of a multidimensional array
     * @param multiArray the multidimensional array from which a column is copied
     * @param a the index of the column to be copied
     * @return a new array with the contents of the multidimensional array at a given column
     */
    public static char[] createVerticalArray(char[][] multiArray, int a){
        // Creating an array for each column of the multidimensional array
        char[] newArray = new char[7];

        for (int i = 0; i < newArray.length-1; i++){
            // Assigning each column position's element to the next newArray vacancy
            newArray[i] = multiArray[i][a];
        }
        return newArray;
    }

    /**
     * This method returns a diagonally down line in a multidimensional array
     * @param multiArray the multidimensional array from which a diagonal line is extracted
     * @param start the start value of the multidimensional array's rows
     * @param end the start value of the multidimensional array's columns
     * @param arrayLength specifies the diagonal array's length
     * @return newArray, which has the elements of a line drawn diagonally down
     */
    public static char[] createDiagonalArrayDown(char[][] multiArray, int start, int end, int arrayLength){
        // Creating an array to represent a series of diagonally down elements in the multidimensional array
        char[] newArray = new char[arrayLength];

        for (int i = 0; i < arrayLength; i++){
            // Assigning each diagonal position to a vacancy in newArray
            newArray[i] = multiArray[start][end];
            // Based on the pattern of diagonal positions, a increments and b decrements after each iteration
            end++; start--;
        }
        return newArray;
    }

    /**
     * This method returns an array of a diagonally up line in a multidimensional array
     * @param multiArray the multidimensional array from which a diagonal line is extracted
     * @param start the start value of the multidimensional array's rows
     * @param end the start value of the multidimensional array's columns
     * @param arrayLength specifies the diagonal array's length
     * @return newArray, which has the elements of a line drawn diagonally up
     */
    public static char[] createDiagonalArrayUp(char[][] multiArray, int start, int end, int arrayLength){
        // Creating an array to represent a series of diagonally up elements in the multidimensional array
        char[] newArray = new char[arrayLength];

        for (int i = 0; i < arrayLength; i++){ // note to self: review the minus one you have here
            // Assigning each diagonal position to a vacancy in newArray
            newArray[i] = multiArray[start][end];
            // Based on the pattern of diagonal up positions, a and b both increment after each iteration
            start++; end++;
        }
        return newArray;
    }

    /**
     * This method checks whether there are four of the same character, look, in an array
     * @param array the array which is searched for four consecutive occurrences of a character in an array
     * @param look the character which should be checked for in the array
     * @return finalAnswer, initialised to false and only assigned true when there are four consecutive occurrences of look
     */
    public static boolean checkOrder(char[] array, char look){
        // Boolean variable which is returned by the function;
        boolean finalAnswer = false;

        for (int i = 0; i < array.length; i++){
            // Using a try, catch in case of an index that is out of bounds
            try{
                // Checking if four consecutive indexes have the character look
                if (array[i] == look && array[i+1] == look && array[i+2] == look && array[i+3] == look){
                    finalAnswer = true;
                    return true;
                }
            } catch(IndexOutOfBoundsException ioe){
                // Returning false once there is an index out of bounds exception
                return false;
            }
        }
        return finalAnswer;
    }

    /**
     * This method returns a boolean based on whether the grid has been fully filled or not
     * @param multiArray a multidimensional array whose contents will be checked by the method
     * @return returns true if the game board has been fully filled and false otherwise
     */
    public static boolean checkFullGrid(char[][] multiArray){
        for (int i = 0; i < multiArray.length; i++){
            for (int j = 0; j < multiArray[i].length; j++){
                // A blank character means that a space can still be filled
                if (multiArray[i][j] == '\u0000'){
                    return false;
                }
            }
        }
        return true;
    }
    }