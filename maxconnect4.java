import java.io.*;
import java.util.*;

/**
 * 
 * This class controls the game play for the Max Connect-Four game. 
 * To compile the program, use the following command from the maxConnectFour directory:
 * javac *.java
 *
 * the usage to run the program is as follows:
 * ( again, from the maxConnectFour directory )
 *
 *  -- for interactive mode:
 * java MaxConnectFour interactive [ input_file ] [ computer-next / human-next ] [ search depth]
 *
 * -- for one move mode
 * java maxConnectFour.MaxConnectFour one-move [ input_file ] [ output_file ] [ search depth]
 * 
 * description of arguments: 
 *  [ input_file ]
 *  -- the path and filename of the input file for the game
 *  
 *  [ computer-next / human-next ]
 *  -- the entity to make the next move. either computer or human. can be abbreviated to either C or H. This is only used in interactive mode
 *  
 *  [ output_file ]
 *  -- the path and filename of the output file for the game.  this is only used in one-move mode
 *  
 *  [ search depth ]
 *  -- the depth of the minimax search algorithm
 * 
 *   
 */

public class maxconnect4
{
  private static AiPlayer calculon;
  private static String currentPlayer;
  private static String nextPlayer;
  private static GameBoard currentGame;

  public static void decideWinner(){
    int human=0;
    int computer=0;
    if(currentPlayer.equalsIgnoreCase("computer")){
      computer=currentGame.getScore(1);
      human=currentGame.getScore(2);
    }
    else{
      computer=currentGame.getScore(2);
      human=currentGame.getScore(1);
    }
    if(computer>human){
      System.out.println("\nYOU LOST THE GAME. BETTER LUCK NEXT TIME!\n");
    }
    else if(computer==human){
      System.out.println("\nIT'S A TIE. PLAY AGAIN!\n");
    }
    else{
      System.out.println("\nYOU WON THE GAME. CONGRATULATIONS!\n");
    }
  }

  public static boolean checkPlay(int col) {
    if(!currentGame.isValidPlay(col-1)){
      System.out.println("\nIt's Invalid! Please choose another valid column.\n");
      return false;
    }
    else{
      return true;
    }
      
  }

  public static void makeHumanMove(){

    //print the current game board
    System.out.println("\nGame Board:");
    currentGame.printGameBoard();
    // print the current scores
    System.out.println( "Score: Player 1 = " + currentGame.getScore(1) +
      ", Player2 = " + currentGame.getScore(2) + "\n " );


    //  variables to keep up with the game
    int column=99;        //  the players choice of column to play
    Scanner scan=new Scanner(System.in);

      if(currentGame.getPieceCount()<42){

      System.out.println("\nHuman should Play, Choose a Column between 1 to 7: \n");

      // Read the input until Column is valid
      do {
       
          column=scan.nextInt();
          
      } while (!checkPlay(column));


        currentGame.playPiece(column-1);
        System.out.println("Move: " + currentGame.getPieceCount() + " , Player: Human , Column: " + column);
        currentGame.printGameBoardToFile("human.txt");
        makeComputerMove();
      }
      else{
         System.out.println("\nI can't play.\nThe Board is Full\n\nGame Over");
         decideWinner();
         exit_function(0);
      }
    }
    
  public static void makeComputerMove(){
   
    int column=99;  
   //print the current game board
    System.out.println("\nGame Board:");
    currentGame.printGameBoard();
    // print the current scores
    System.out.println( "Score: Player 1 = " + currentGame.getScore(1) +
      ", Player2 = " + currentGame.getScore(2) + "\n " );

    System.out.println("\nComputer is Playing:");
    if(currentGame.getPieceCount()<42) 
    {
        int current_player=currentGame.getCurrentTurn();
        
        // AI play - random play
        column=calculon.findBestPlay(currentGame);
  
        // play the piece
        currentGame.playPiece(column);
          
        // display the current game board
        System.out.println("Move: " + currentGame.getPieceCount() 
                           + ", Player: Computer" 
                           + ", Column: " + (column+1));

      /*  System.out.println("\nGame Board:");
        currentGame.printGameBoard();
    
        // print the current scores
        System.out.println( "Score: Player 1 = " + currentGame.getScore( 1 ) +
                            ", Player2 = " + currentGame.getScore( 2 ) + "\n " );*/

        currentGame.printGameBoardToFile("computer.txt");
        makeHumanMove();
    }
    else 
    {
      System.out.println("\nI can't play.\nThe Board is Full\n\nGame Over");
      decideWinner();
      exit_function(0);
    }
  
  }

  public static void playOneMoveMode(String output) {
  
    //print the current game board
    System.out.println("\nGame Board before Move:\n");
    currentGame.printGameBoard();
    // print the current scores
    System.out.println("Score: Player 1 = " + currentGame.getScore(1) +
      ", Player2 = " + currentGame.getScore(2) + "\n ");

    if(currentGame.getPieceCount()<42){

        int current_player=currentGame.getCurrentTurn();
        
        // AI play - random play
        int column=calculon.findBestPlay(currentGame);
  
        // play the piece
        currentGame.playPiece(column);
          
        // display the current game board
        System.out.println("Move: " + currentGame.getPieceCount() 
                           + ", Player: " + current_player
                           + ", Column: " + (column+1));

        System.out.println("\nGame Board after move:\n");
        currentGame.printGameBoard();
    
        // print the current scores
        System.out.println( "Score: Player 1 = " + currentGame.getScore(1) +
                            ", Player2 = " + currentGame.getScore(2) + "\n " );

        currentGame.printGameBoardToFile(output);
    }
    else{
      System.out.println("\n Cannot play further because the board is already full!\n");
      exit_function(0);
    }
  }


  public static void main(String[] args) 
  {
    // check for the correct number of arguments
    if(args.length!=4) 
    {
      System.out.println("Four command-line arguments are needed:\n"
                         + "Usage: java [program name] interactive [input_file] [computer-next / human-next] [depth]\n"
                         + " or:  java [program name] one-move [input_file] [output_file] [depth]\n");

      exit_function(0);
     }
		
    // parse the input arguments
    String game_mode=args[0].toString();				// the game mode
    String input=args[1].toString();					// the input game file
    int depthLevel=Integer.parseInt( args[3] );  		// the depth level of the ai search

 
    boolean playMade=false;     //  set to true once a play has been made
		
    // create and initialize the game board
    currentGame=new GameBoard(input);
    
    // create the Ai Player
    calculon=new AiPlayer(currentGame,depthLevel);
		
    //Game Starts from here
    System.out.println("\nMaxConnect-4 game starts\n");


    if(game_mode.equalsIgnoreCase("one-move")) 
    {
        // get the output file name
        String output=args[2].toString();       // the output game file
        playOneMoveMode(output);
    }
    else if(game_mode.equalsIgnoreCase("interactive")) 
    {
      if(args[2].toString().equalsIgnoreCase("human-next")){
        currentPlayer="human";
        nextPlayer="computer";
        makeHumanMove();
      }
      else if(args[2].toString().equalsIgnoreCase("computer-next")){
        currentPlayer="computer";
        nextPlayer="human";
        makeComputerMove();
      }
      else{
        System.out.println("\nPlease use human-next or computer-next in the command to make the next move.\n");
        exit_function(0);
      }
	
    } 
	  else{
      System.out.println("\n" + game_mode + " is an unrecognized game mode \n try again. \n");
      exit_function(0);
    }
	
    //************************** end computer play
    
} // end of main()
	
  /**
   * This method is used when to exit the program prematurly.
   * @param value an integer that is returned to the system when the program exits.
   */
  private static void exit_function(int value)
  {
      System.out.println("exiting from MaxConnectFour.java!\n\n");
      System.exit(value);
  }
} // end of class connectFour