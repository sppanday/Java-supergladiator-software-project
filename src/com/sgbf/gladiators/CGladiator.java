package com.sgbf.gladiators;

import com.sgbf.battle.CBattleState;
import com.sgbf.core.state.CState;
import com.sgbf.core.state.CStateManager;
import com.sgbf.core.state.IStateListener;;

public abstract class CGladiator implements IStateListener {

   /**
    * The name of this gladiator
    */
   private String name;
   
   /**
    * CONSTRUCTOR
     * @param name
    */
   public CGladiator( String name, int life )
   {
      this.name = name;
      
      CGladiatorState state = new CGladiatorState( name, life );
      CStateManager.instance().setState( state );
      
      CStateManager.instance().registerListener( this );
   }
   
   /**
    * Helper to get the state for this gladiator
    * @return This gladiator's state
    */
   public CGladiatorState getState()
   {
      return (CGladiatorState)CStateManager.instance().
               getState( CGladiatorState.getGladiatorId( name ) );
   }
   
   /**
    * Returns a string stating the "type" of this gladiator
    * Override this in extended classes
    * @return The gladiator type
    */
   public String getType() {
      return "none";
   }
   
   /**
    * Helper function to query the gladiator's state's life value
    * @return Whether this gladiator is still alive 
    */
   public boolean isAlive() {
      return getState().getLife() > 0;
   }
   
   /**
    * Gladiators need only listen for their turn to attack
    * @param newState   The new state object
    * @param oldState   The state object prior to modification
    */
   public void onStateChange( final CState newState, final CState oldState )
   {
      // Check if we're dealing with a gladiator state
      if( CGladiatorState.class.isInstance( newState ) ) {
         CGladiatorState gladiatorState = (CGladiatorState) newState;
         CGladiatorState oldGladiatorState = (CGladiatorState) oldState;
         
         // Check if it is "ME"
         if( 
            gladiatorState.getName().equals( name ) && 
            oldGladiatorState.getLife() != 0 
         ) {
            // If gladiator is "ready", attack the opponent
            if( gladiatorState.isReady() ) {
               performAttack();
            }
         }
      }
   }
   
   /**
    * Retrieves the amount of damage performed by this gladiator
    * @return The damage amount
    */
   protected int getDamage()
   {
      return (int)Math.floor( Math.random() * 1 ) + 1;
   }
   
   /**
    * Gladiator will perform an attack on its current opponent
    */
   private void performAttack()
   {
      // If we're set to "ready" we must be in a battle so grab it
      CBattleState battle = (CBattleState)CStateManager.
                        instance().getState( CBattleState.STATE_ID );
      
      // Find our opponent
      CGladiator opponent = null;
      for( int i = 0; i < battle.getGladiatorCount(); i++ ) {
         if( battle.getGladiator(i) != this ) {
            opponent = battle.getGladiator(i); 
         }
      }
      if( opponent != null ) {
         
         int damage = this.getDamage();
         
         System.out.println(   
                           "\t" + name + 
                           " the " + getType() + 
                           " attacks " + opponent.name + 
                           " the " + opponent.getType() +
                           " doing " + damage + " damage"
                        );
         CGladiatorState opponentState = opponent.getState();
         
         opponentState = opponentState.
                        setLife( 
                                 opponentState.getLife() - 
                                 damage
                              );
         CStateManager.instance().setState( opponentState );
      }
      
      // Set our ready state to off
      CGladiatorState state = getState();
      state = state.setReady( false );
      CStateManager.instance().setState( state );
   }
}