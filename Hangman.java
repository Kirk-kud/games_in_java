import java.awt.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;

public class Hangman {

    public static void main(String[] args){
        Scanner ask = new Scanner(System.in);
        // Declaring an integer to keep track of a user's decision to continue playing or not
        int userChoice;

        int noOfRedos = 0;

        do{
            if (args.length >= 1) {
                // if statement which is executed when there are command line arguments passed to the main method
                playHangman(pickWord(args));
            }
            else {
                // else statement which is executed when there are no command line arguments
                playHangman(pickWord());
            }

            do{
                if (noOfRedos < 1){
                    // Asking the user whether they would still want to play
                    System.out.print("Do you want to guess another word? Enter y or n>");

                    userChoice = ask.next().charAt(0);
                }
                else{
                    // Asking the user to enter a valid input
                    System.out.print("Do you want to guess another word? Enter y or n>");

                    userChoice = ask.next().charAt(0);
                }

                noOfRedos++;

                if (userChoice == 'n')
                    // Ending the program if the user does not want to play anymore
                    System.exit(0);
                else if (userChoice == 'y')
                    // Breaking out of the loop to continue with a new game
                    break;

            } while (true);

        } while (userChoice != 'n' && userChoice == 'y');
        // Loop terminates only when the user enters y or n
    }

    /**
     * This method is called when an array is passed to the main method in the command line.
     * It picks one the strings from the args at random array to be used in playing the game.
     * @param arguments command line arguments
     * @return arguments[position]  a specific element from the cd line argument
     */
    public static String pickWord(String[] arguments){
        Random asker = new Random();
        // Picking a random index for the POSSIBLE_WORDS array
        int position = asker.nextInt(0, arguments.length);

        return arguments[position];
    }

    /**
     * This method contains an array of words which could be used for the Hangman game.
     * One string from the array is picked at random and returned by the function.
     * @return POSSIBLE_WORDS[position]  a specific element from the array possibleWords
     */
    public static String pickWord(){
        // Array of possible guess words
        final String[] POSSIBLE_WORDS = {"white", "dog", "classroom", "brain"};

        Random asker = new Random();
        // picking a random index for the array, possibleWords
        int position = asker.nextInt(0, POSSIBLE_WORDS.length);

        return POSSIBLE_WORDS[position];
    }

    /**
     * This method repeatedly asks a user to guess the word picked by pickWord
     * till the user gets all the letters in the word right.
     * @param guessWord  the word to be guessed by the user
     */
    public static void playHangman(String guessWord){
        // Creating an array with the letters of the word to be guessed
        char[] wordsInArray = guessWord.toCharArray();

        // Creating a new array to keep track of the user's correct guesses
        char[] records = new char[wordsInArray.length];

        Scanner ask = new Scanner(System.in);

        // Initialising counts for both the correct guesses and total guesses
        int correctGuesses = 0, totalGuesses = 0, totalMisses = 0;


        do{
            // Printing out the prompt and word to guess
            System.out.print("(Guess) Enter in a letter in the word ");
            printBlanks(records);
            System.out.print(" > ");

            char nextGuess = ask.next().charAt(0);

            // Checks whether the letter guessed is correct and has not already been guessed
            if (searchArray(nextGuess, wordsInArray) && !searchArray(nextGuess, records)){
                // Incrementing the correct guess count by one
                correctGuesses += 1;
                // checking the index(es) at which the user's guess can be found in the guess word
                int[] nextIndex = searchArray2(nextGuess, wordsInArray);

                for (int i = 0; i < nextIndex.length; i++){
                    // Filling the array with guesses at the necessary places
                    records[nextIndex[i]] = nextGuess;
                }
            }
            // Checking if the user has already guessed a letter
            else if (searchArray(nextGuess, wordsInArray) && searchArray(nextGuess, records)){
                System.out.println("\t" + nextGuess + " is already a letter in the word");
            }
            else {
                // Printing out a "Try again" message in the case that the user is wrong
                System.out.printf("\t %c is not a letter in the word\n", nextGuess);
                checkInput(nextGuess);
                totalMisses += 1;
            }


            // Incrementing the total number of guesses count by one
            totalGuesses += 1;

        } while (correctGuesses < distinctElements(wordsInArray));

        // Checking if the guess word and the guessed word are the same
        if (Arrays.equals(records,wordsInArray) && (totalMisses) == 1 ){
            // Making time singular if there was just one miss
            System.out.printf("The word is %s. You missed %d time\n",guessWord,totalMisses);
        }
        // Checking if the guess word and the guessed word are the same
        else if (Arrays.equals(records,wordsInArray) && (totalMisses) != 1){
            // Making time plural (times) when the misses are 0 or greater than 1
            System.out.printf("The word is %s. You missed %d times\n",guessWord,totalMisses);
        }

    }

    /**
     * This method searches through an array to find whether the character look
     * is in the array provided. The method returns a boolean depending on if
     * the desired character, look, was found or not.
     * @param look  the character to be searched for
     * @param array  the char array to be searched
     * @return finalAnswer  boolean value indicating presence of absence of look
     */
     public static int[] searchArray2(char look, char[] array) {
         int[] finalAnswer = new int[array.length];
         int numberOfElements = 0;

         for (int i = 0; i < array.length; i++) {
             if (array[i] == look) {
                 // Keeping track of the indexes
                 finalAnswer[numberOfElements] = i;
                 numberOfElements += 1;
             }
         }
         // Shortening the return array to only the number of elements which were found
         return Arrays.copyOf(finalAnswer, numberOfElements);

     }

    /**
     * This method checks whether a character, look, is an element in a given array
     * @param look the character to be looked for in the array
     * @param array the array to be searched for the character look
     * @return finalAnswer, a boolean which is true if the character look is present in the array and false otherwise
     */
    public static boolean searchArray(char look, char[] array){
        // Initialising finalAnswer to false in case the character is not found
        boolean finalAnswer = false;

        for (int i = 0; i < array.length; i++) {
            if (array[i] == look) {
                finalAnswer = true;
                break;
                // Once the character has been found once, the loop can be terminated
            }
        }

        return finalAnswer;
    }


    /**
     * This method prints the characters included in the array
     * including underscores for the letters not yet guessed by the user.
     * @param array  array to be printed out with blanks
     */
    public static void printBlanks(char[] array){

        for (int i = 0; i < array.length; i++){
            // checking whether the element at position i is a default value
            if (array[i] == '\u0000'){
                // Printing an asterisk for an un-guessed word
                System.out.print("* ");
            }
            else {
                // Printing out the character at position i
                System.out.printf("%c ", array[i]);
            }
        }
    }

    /**
    * This method returns the number of distinct elements in an array as an integer.
     * @param array  array to be searched for distinct elements
    */
    public static int distinctElements(char[] array){
        char[] countedElements = new char[array.length];
        // Initialising the count variable for the number of distinct elements
        int numberOfDistinctElements = 0;

        for (int i = 0; i < array.length; i++){
            // Checking whether the next element has been counted
            if (!searchArray(array[i],countedElements)){
                // Adding the element to the array of elements which have been counted
                countedElements[numberOfDistinctElements] = array[i];
                numberOfDistinctElements += 1;
            }
        }
        return numberOfDistinctElements;
    }

    /**
     * This method prints out an error message based on the type of character input the user enters
     * @param nextChar this parameter is the character whose type is determined by the method
     */
    public static void checkInput(char nextChar){
        // Converting the character to an integer to get its Unicode value
        int charInt = (int)nextChar;

        //
        if (charInt >= 65 && charInt <= 90){
            // Checking if the character is an upper case letter
            System.out.printf("Do not include upper case letters like %c\n",nextChar);
        }
        else if (charInt >= 48 && charInt <= 57){
            // Checking if the character is a number between 0-9
            System.out.printf("Do not include numbers like %c\n",nextChar);
        }
        else if (charInt >= 97 && charInt <= 122){
            // Checking if the character is just a lower case letter
            ;
        }
        else{
            // Anything else must be a symbol
            System.out.printf("Do not include symbols like %c\n",nextChar);
        }
    }
}