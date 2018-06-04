package com.sgbf.gladiators;

import com.sgbf.core.state.CState;

/**
 * Represents the current state of a gladiator
 */
public class CGladiatorState extends CState {

   /**
    * The "base id" of the state
    */
   public static final String BASE_ID = "Gladiator_";
   
   /**
    * Helper function to retrieve the state id for the given gladiator name
     * @param name
    * @return The state id for the given gladiator name
    */
   public static String getGladiatorId( String name ) 
   { 
      return CGladiatorState.BASE_ID + name;
   }
   
   /**
    * Override to retrieve an ID for this state
    * @return The state's id
    */
   public String getId()
   {
      return CGladiatorState.getGladiatorId( name );
   }
   
   /**
    * The name of the gladiator this gladiator 
    */
   private String name;
   
   /**
    * The gladiators life when at full health
    */
   private int maximumLife;
   
   /**
    * The gladiators remaining life
    */
   private int life;
   
   /**
    * Whether this gladiator is ready to attack
    */
   private boolean ready;
   
   // Getters
   public final int getLife() { return life; }
   public final String getName() { return name; }
   public final boolean isReady() { return ready; }
      
   /**
    * CONSTRUCTOR!
    * @param name   The name of this gladiator
    * @param life   The gladiator's initial life
    */
   public CGladiatorState( String name, int life )
   {
      super();
      this.name = name;
      this.maximumLife = life;
      this.life = life;
      this.ready = false;
   }
   
   /**
    * To preserve the original object, our setters
    * will return a complete new duplicate of this state
    * rather than update the original value
    * @param life   The new life value
    * @return A new CGladiatorState with the updated life value
    */
   public CGladiatorState setLife( int life )
   {
      CGladiatorState state = new CGladiatorState( 
                                          this.name, 
                                          this.maximumLife 
                                       );
      state.life = ( life > maximumLife ) ? maximumLife : life;
      state.ready = this.ready;
      return state;
   }
   
   /**
    * To preserve the original object, our setters
    * will return a complete new duplicate of this state
    * rather than update the original value
    * @param ready      Set as ready to attack
    * @return A new CGladiatorState with the updated "ready" value
    */
   public CGladiatorState setReady( boolean ready )
   {
      CGladiatorState state = new CGladiatorState( 
                                          this.name, 
                                          this.maximumLife 
                                       );
      state.life = this.life;
      state.ready = ready;
      return state;
   }
}