package com.sgbf.battle;

import java.util.LinkedList;
import com.sgbf.core.state.CState;
import com.sgbf.core.state.CStateManager;
import com.sgbf.core.state.IStateListener;
import com.sgbf.gladiators.CGladiator;
import com.sgbf.gladiators.CGladiatorState;

/**
 * Controls a battle between gladiators
 */
public class CBattle implements IStateListener {
   
   /**
    * CONSTRUCTOR
    */
   public CBattle( CGladiator gladiators[] ) {
      
      CStateManager.instance().registerListener( this );
      
      LinkedList gladiatorList = new LinkedList();
      for( int i = 0; i < gladiators.length; i++ ) {
         gladiatorList.add( gladiators[i] );
      }
      CBattleState state = new CBattleState( gladiatorList );
      CStateManager.instance().setState( state );
   }
   
   /**
    * Helper function to get the state
    * @return The current battle state
    */
   private CBattleState getState()
   {
      return (CBattleState)CStateManager.instance().
               getState( CBattleState.STATE_ID );
   }
   
   /**
    * Displays that the battle is starting
    */
   private void announceBattle()
   {
      System.out.println();
      System.out.println("***********************************");
      System.out.println();
      System.out.println("Next Battle:");
      CBattleState state = getState();
      for( int i = 0; i < state.getGladiatorCount(); i++ ) {
         if( i != 0 ) System.out.println("vs");
         
         CGladiator gladiator = state.getGladiator(i);
         System.out.println(
                        gladiator.getState().getName().toUpperCase() +
                        " the " + gladiator.getType().toUpperCase() +
                        " with " +
                        gladiator.getState().getLife() +
                        " life remaining"
                     );
      }
      System.out.println();
   }
   
   /**
    * Checks if the battle is complete
    * @boolean   Whether the battle is complete
    */
   private boolean checkCompletion()
   {
      CBattleState state = getState();
      for( int i = 0; i < state.getGladiatorCount(); i++ ) {
         CGladiatorState gladiatorState = 
                              state.getGladiator(i).getState();
         if( gladiatorState.getLife() <= 0 ) {
            state = state.setComplete( true );
            CStateManager.instance().setState( state );         
            return true;
         }
      }
      return false;
   }
   
   /**
    * Listen for changes to the battle and Gladiator states
    * @param newState   The new state object
    * @param oldState   The state object prior to modification
    */
   public void onStateChange( final CState newState, final CState oldState )
   {
      // Check if the battle state has changed
      if( CBattleState.class.isInstance( newState ) ) {
         CBattleState battleState = (CBattleState)newState;
         CBattleState oldBattleState = (CBattleState)oldState;
         
         // If battle was completed, we don't need to do anything
         if( battleState.isComplete() ) {
            return;
         }
         
         // If this is a new battle, announce the fighters
         if( oldBattleState == null || oldBattleState.isComplete() )
         {
            announceBattle();
         }
                  
         // New turn; tell gladiator to make a move!
         if(
            oldBattleState == null || oldBattleState.isComplete() ||
            battleState.getTurn() != oldBattleState.getTurn() 
         ) {
            // Check if the gladiators are still alive
            if( checkCompletion() ) {
               displayResult();
            }
            else {
               CGladiatorState gladiatorState = battleState.
                                          getGladiator( 
                                                battleState.getTurn() 
                                             ).
                                          getState();
               gladiatorState = gladiatorState.setReady( true );
               CStateManager.instance().setState( gladiatorState );
            }
         }
      }
      // If a gladiator has transitioned from ready to "not ready"
      // It's time to start a new turn
      else if( CGladiatorState.class.isInstance( newState ) ) {
         CGladiatorState gladiatorState = (CGladiatorState)newState;
         CGladiatorState oldGladiatorState = (CGladiatorState)oldState;
         if( !gladiatorState.isReady() && oldGladiatorState.isReady() ) {
            CBattleState battleState = 
                        (CBattleState)CStateManager.instance().
                        getState( CBattleState.STATE_ID );
            battleState = battleState.updateTurn();
            CStateManager.instance().setState( battleState );
         }
      }
   }
   
   /**
    * Finds the losing gladiator
    * @return The losing gladiator
    */
   private CGladiator getLoser()
   {
      CBattleState state = getState();
      for( int i = 0; i < state.getGladiatorCount(); i++ ) {
         CGladiatorState gladiatorState = 
                              state.getGladiator(i).getState();
         if( gladiatorState.getLife() <= 0 ) {
            return state.getGladiator(i);
         }
      }
      return null;
   }
   
   /**
    * Finds the winning gladiator
    * @return The winning gladiator
    */
   private CGladiator getWinner()
   {
      CBattleState state = getState();
      for( int i = 0; i < state.getGladiatorCount(); i++ ) {
         CGladiatorState gladiatorState = 
                              state.getGladiator(i).getState();
         if( gladiatorState.getLife() > 0 ) {
            return state.getGladiator(i);
         }
      }
      return null;
   }
   
   /**
    * Writes the battle result to the screen
    */
   private void displayResult()
   {
      CGladiator winner = getWinner();
      CGladiator loser = getLoser();
      System.out.println();
      System.out.println("Battle Over");
      System.out.println(
                     "" + winner.getState().getName() +
                     " the " + winner.getType() +
                     " has defeated " +
                     loser.getState().getName() +
                     " the " + loser.getType()
                     );
      System.out.println();
      System.out.println("***********************************");
      System.out.println();
      
   }
   
   /**
    * Cleans up the battle after use
    */
   public void cleanUp()
   {
      CStateManager.instance().deregisterListener( this );
   }
}