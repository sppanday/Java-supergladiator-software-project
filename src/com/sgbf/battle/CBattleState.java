package com.sgbf.battle;

import java.util.LinkedList;

import com.sgbf.core.state.CState;
import com.sgbf.gladiators.CGladiator;

/**
 * Holds the state for a single battle 
 * between two Gladiators
 */
public class CBattleState extends CState {

   /**
    * The general id for battle states
    */
   public static final String STATE_ID = "BATTLE";
   
   /**
    * Override to retrieve an ID for this state
    * @return The state's id
    */
   public String getId()
   {
      return CBattleState.STATE_ID;
   }
   
   
   /**
    * The gladiators
    */
   private LinkedList gladiators;
   
   /**
    * The current "round" in this battle
    * In each "round", the gladiators are given
    * a "turn" (a chance to "attack" each other
    * - reducing the other's life)
    */
   private int round;
   
   /**
    * The current turn in the current round
    */
   private int turn;
   
   /**
    * Whether the battle is complete
    */
   private boolean complete;
   
   // Getters
   public int getGladiatorCount() { return gladiators.size(); }
   public CGladiator getGladiator( int i ) { return gladiators.get(i); }
   public int getRound() { return round; }
   public int getTurn() { return turn; }
   public boolean isComplete() { return complete; }
   public boolean hasGladiator( CGladiator gladiator ) {
      return gladiators.contains( gladiator );
   }
   
   /**
    * CONSTRUCTOR
    */
   public CBattleState( LinkedList gladiators )
   {
      this.gladiators = new LinkedList();
      for( int i = 0; i < gladiators.size(); i++ ) {
         this.gladiators.add( gladiators.get(i) );
      }
      complete = false;
      round = 1;
      turn = 0;
   }
   
   /**
    * Updates the round
    * @return A new CBattleState with the round updated and turn reset
    */
   public CBattleState updateRound()
   {
      CBattleState newState = new CBattleState( this.gladiators );
      newState.round = this.round + 1;
      newState.complete = this.complete;
      return newState;
   }
   
   /**
    * Updates the turn
    * @return A new CBattleState with the turn updated
    */
   public CBattleState updateTurn()
   {
      CBattleState newState = new CBattleState( this.gladiators );
      newState.turn = this.turn + 1;
      newState.round = this.round;
      if( newState.turn >= this.gladiators.size() ) {
         newState.round += 1;
         newState.turn = 0;
      }
      newState.complete = this.complete;
      return newState;
   }
   
   /**
    * Sets the battle completion state
    * @return A new state with the updated value
    */
   public CBattleState setComplete( boolean complete ) {
      CBattleState newState = new CBattleState( this.gladiators );
      newState.round = this.round;
      newState.turn = this.turn;
      newState.complete = complete;
      return newState;
   }
}