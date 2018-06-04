package com.sgbf.tournament;

import java.util.LinkedList;
import com.sgbf.battle.CBattle;
import com.sgbf.battle.CBattleState;
import com.sgbf.core.state.CState;
import com.sgbf.core.state.CStateManager;
import com.sgbf.core.state.IStateListener;
import com.sgbf.gladiators.CGladiator;
import com.sgbf.gladiators.CGladiatorFactory;

/**
 * Controls a tournament where 
 * a set of Gladiators fight in elimination rounds
 * until a victor is found
 */
public class CTournament implements IStateListener {

   /**
    * The current battle
    */
   private CBattle battle;
   
   /**
    * CONSTRUCTOR
    * @param participants   The number of gladiators to compete
    */
   public CTournament( int participants )
   {
      battle = null;
      
      CStateManager.instance().registerListener( this );
      
      CGladiatorFactory gladiatorFactory = new CGladiatorFactory();
      LinkedList gladiators = new LinkedList();
      for( int i = 0; i < participants; i++ ) {
         gladiators.add( gladiatorFactory.createGladiator() );
      }
      
      CTournamentState state = new CTournamentState( gladiators );
      CStateManager.instance().setState( state );
   }
   
   /**
    * Helper function to get the state
    * @return The current tournament state
    */
   private CTournamentState getState() {
      return (CTournamentState)CStateManager.instance().
               getState( CTournamentState.STATE_ID );
   }
   
   /**
    * Finds the next gladiator in the state that is still alive
    * starting from the given gladiator
    * @param start      The gladiator to start searching from
    * @param inclusive   Whether to include the "start" gladiator in the search
    * @return The next living gladiator
    */
   private CGladiator findNextGladiator( CGladiator start, boolean inclusive )
   {
      CTournamentState state = getState();
      
      boolean returnNext = false;
      for( int i = 0; i < state.getGladiatorCount(); i++ ) {
         CGladiator gladiator = state.getGladiator(i);
         if( returnNext && gladiator.isAlive() ) {
            return gladiator;
         }
         else if( state.getGladiator( i ) == start ) {
            if( inclusive && gladiator.isAlive() ) 
            {
               return gladiator;
            }
            returnNext = true;
         }
      }
      return null;
   }
   
   /**
    * Starts a battle based on the current tournament state
    */
   private void startNextBattle() {
      CTournamentState state = getState();
      
      // Find the next living gladiator
      CGladiator gladiator1 = findNextGladiator( 
                           state.getGladiator( state.getTurn() ), 
                           true );
      
      // Then set above's opponent as the next living 
      // gladiator in the list after him/her
      CGladiator gladiator2 = findNextGladiator( 
                           gladiator1, 
                           false );
      
      // Clean up last battle (if one exists)
      if( battle != null ) battle.cleanUp();
      
      // Start the battle
      battle = new CBattle( 
                        new CGladiator[] {
                              gladiator1,
                              gladiator2
                        }
                     );
   }
   
   /**
    * Checks whether the tournament is complete
    * (i.e. only one living gladiator remaining)
    * @return Whether tournament is complete
    */
   public boolean isTournamentComplete()
   {
      CTournamentState state = getState();
      int livingGladiators = 0;
      for( int i = 0; i < state.getGladiatorCount(); i++ )
      {
         if( state.getGladiator(i).isAlive() )
         {
            livingGladiators += 1;
            if( livingGladiators > 1 ) {
               return false;
            }
         }
      }
      return true;
   }
   
   /**
    * Updates the current turn (and round if relevant)
    * Needs to check whether a gladiator is available for the turn, 
    * and whether there is an opponent; if not, start the next round
    */
   private void updateTurn()
   {
      CTournamentState state = getState().updateTurn();
      
      // If the turn index is greater than the total gladiator count,
      // then instantly go to the next round
      if( state.getTurn() >= state.getGladiatorCount() ) {
         state = state.updateRound();
      }
      else {
         
         // Check if a living gladiator is available 
         CGladiator gladiator1 = findNextGladiator( 
                              state.getGladiator( state.getTurn() ), 
                              true );
         
         // If no gladiator, start a new round
         if( gladiator1 == null ) {
            state = state.updateRound();
         }
         else {
            
            // Now check if a living opponent is available
            CGladiator gladiator2 = findNextGladiator( 
                                             gladiator1, 
                                             false );
            
            // If no opponent, start a new round
            if( gladiator2 == null ) {
               state = state.updateRound();
            }
         }
      }
      
      // Update the state with the new turn/round
      CStateManager.instance().setState( state );
   }

   /**
    * Updates the status of this tournament based on the
    * results of battles
    * @param newState   The new state object
    * @param oldState   The state object prior to modification
    */
   public void onStateChange(CState newState, CState oldState) {
      
      // Check if the tournament has updated
      if( CTournamentState.class.isInstance( newState ) ) {
         
         CTournamentState tournamentState = (CTournamentState)newState;
         CTournamentState oldTournamentState = (CTournamentState)oldState;
         
         // If a new round is starting, display the tournament standings
         if( 
               oldState == null ||
               ( 
                     tournamentState.getRound() != 
                     oldTournamentState.getRound() 
               )
         ) {
            displayTournamentStandings();
         }
         
         // Kickstart the next battle if the turn has changed or if this is a new battle
         if( 
               oldState == null ||
               ( 
                     tournamentState.getTurn() != 
                     oldTournamentState.getTurn() 
               ) ||
               ( 
                     tournamentState.getRound() != 
                     oldTournamentState.getRound() 
               )
         ) {
            
            startNextBattle();
         }
      }
      
      // If a battle has updated, we're only checking whether a 
      // battle has completed then deciding whether to continue
      // the tournament or exit
      else if( CBattleState.class.isInstance( newState ) ) {
         if( oldState != null ) {
            CBattleState battleState = (CBattleState)newState;
            CBattleState oldBattleState = (CBattleState)oldState;
            if( 
                  battleState.isComplete() && 
                  !oldBattleState.isComplete() 
            ) {
               if( isTournamentComplete() ) {
                  showResult();
               }
               else {
                  // Update the turn
                  updateTurn();
               }
            }
         }
      }
   }
   
   /**
    * Shows the remaining participants in the tournament
    */
   private void displayTournamentStandings()
   {
      CTournamentState state = getState();
      
      System.out.println("========================");
      System.out.println("Gladiators");
      for( int i = 0; i < state.getGladiatorCount(); i++ ) {
         CGladiator gladiator = state.getGladiator(i);
         if( gladiator.isAlive() ) {
            System.out.println(
                        "\t" + 
                        gladiator.getState().getName().toUpperCase() +
                        " the " + gladiator.getType().toUpperCase()
                        );
         }
      }
      System.out.println("========================");
   }
   
   /**
    * Shows the final result of the tournament
    */
   private void showResult()
   {
      System.out.println("***********************");
      System.out.println("TOURNAMENT COMPLETE");
      CTournamentState state = getState();
      for( int i = 0; i < state.getGladiatorCount(); i++ )
      {
         CGladiator gladiator = state.getGladiator(i); 
         if(gladiator.isAlive())
         {
            System.out.println(
                           "\t" + gladiator.getState().getName() + 
                           " the " + gladiator.getType() + 
                           " wins the tournament!"
                        );
            
            return;
         }
      }
   }
}
