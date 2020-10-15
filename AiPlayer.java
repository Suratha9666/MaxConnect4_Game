import java.util.*;

/**
 * This is the AiPlayer class.  It simulates a minimax player for the max
 * connect four game.
 */

public class AiPlayer 
{   

    public GameBoard game;
    public int depth;
    
    
    //The Game state and depth with which AiPlayer is initialized
    public AiPlayer(GameBoard game,int depth) 
    {
        this.game=game;
        this.depth=depth;
    }

    
    //Increasing the utility of Max Player
    private int winningPlayer(int max,int min,int depth,GameBoard newGame){
        int score=0;
        if(depth<=0 || newGame.getPieceCount()>=42){
            score=newGame.getScore(2)-newGame.getScore(1);
            return score;
        }
        else{

            GameBoard decGame;
            int outcome=0, selected=0;
                
            for(int x=0;x<=6;x++){
                
                if(newGame.isValidPlay(x)){
                    
                    decGame=new GameBoard(newGame.getGameBoard());
                    decGame.playPiece(x);

                    outcome=opponentPlayer(max,min,depth-1,decGame);
                    score=Math.max(outcome,Integer.MIN_VALUE);

                    if(score>=max){
                        return score;
                    }
                    min = Math.max(min,score);
                }

            }
        return score;
        }
  
    }

    
    //Decreasing the utility of Min Player
    private int opponentPlayer(int max,int min,int depth,GameBoard newGame){
        int score=0;
        if(depth<=0 || newGame.getPieceCount()>=42){
            score=newGame.getScore(2)-newGame.getScore(1);
            return score;
        }
        else{

            GameBoard decGame;
            int outcome=0, selected=0;
                
            for(int x=0;x<=6;x++){
                
                if(newGame.isValidPlay(x)){
                    
                    decGame=new GameBoard(newGame.getGameBoard());
                    decGame.playPiece(x);

                    outcome=winningPlayer(max,min,depth-1,decGame);
                    score=Math.max(outcome,Integer.MAX_VALUE);

                    if(score<=min){
                        return score;
                    }
                    max = Math.max(max,score);
                }

            }
        return score;
        }    
    }


    // Choosing the Best strategy so that Max Player wins
    public int findBestPlay(GameBoard currentGame) 
    {
        GameBoard newGame;
    	int outcome=0, selected=0;
        
        if(currentGame.getCurrentTurn()!=1){
            
            for(int x=0;x<=6;x++){
                
                if(currentGame.isValidPlay(x)){
                    
                    newGame=new GameBoard(currentGame.getGameBoard());
                    newGame.playPiece(x);
                    
                    outcome=opponentPlayer(Integer.MAX_VALUE,Integer.MIN_VALUE,depth,newGame);
                    int min=Integer.MIN_VALUE;
                   
                    if(Integer.MIN_VALUE<outcome) {
                        min=outcome;
                        selected=x;
                    }
                
                }
            }
            
        }
        else {
            
            for(int x=0;x<=6;x++){
                
                if(currentGame.isValidPlay(x)){
                    
                    newGame=new GameBoard(currentGame.getGameBoard());
                    newGame.playPiece(x);

                    outcome=winningPlayer(Integer.MAX_VALUE,Integer.MIN_VALUE,depth,newGame);
                    int max=Integer.MAX_VALUE;
                    
                    if(Integer.MAX_VALUE>outcome){
                        max=outcome;
                        selected=x;
                    }
                
                }
            }
            
        }
        
    return selected;
    }
}