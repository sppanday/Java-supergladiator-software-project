package com.sgbf.tournament;

import java.util.LinkedList;

import com.sgbf.core.state.CState;
import com.sgbf.gladiators.CGladiator;

/**
 * Holds the state of the current tournament
 */
public class CTournamentState extends CState {
   /**
    * The general id for tournament states
    */
   public static final String STATE_ID = "TOURNAMENT";
   
   /**
    * The set of competing gladiators
    */
   private LinkedList gladiators;
   
   /**
    * Override to retrieve an ID for this state
    * @return The state's id
    */
   public String getId()
   {
      return CTournamentState.STATE_ID;
   }
   
   /**
    * The current "round" in this tournament
    * Each "round" consists of one-to-many battles
    */
   private int round;
   
   /**
    * The current turn in the current round
    * That is, in a round with 4 battles, this
    * is which of those battles is taking place
    */
   private int turn;
   
   // Getters
   public int getGladiatorCount() { return gladiators.size(); }
   public CGladiator getGladiator( int i ) { return gladiators.get(i); }
   public int getRound() { return round; }
   public int getTurn() { return turn; }
   
   /**
    * CONSTRUCTOR
     * @param gladiators
    */
   public CTournamentState( LinkedList gladiators )
   {
      this.gladiators = new LinkedList();
      for( int i = 0; i < gladiators.size(); i++ ) {
         this.gladiators.add( gladiators.get(i) );
      }
      round = 0;
      turn = 0;
   }
   
   /**
    * Updates the round
    * @return A new CTournamentState with the round updated and turn reset
    */
   public CTournamentState updateRound()
   {
      CTournamentState newState = new CTournamentState( this.gladiators );
      newState.round = this.round + 1;
      newState.turn = 0;
      return newState;
   }
   
   /**
    * Updates the turn
    * @return A new CTournamentState with the turn updated
    */
   public CTournamentState updateTurn()
   {
      CTournamentState newState = new CTournamentState( this.gladiators );
      newState.turn = this.turn + 2;
      newState.round = this.round;
      return newState;
   }
}