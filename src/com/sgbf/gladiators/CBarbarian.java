package com.sgbf.gladiators;

/**
 * Barbarians don't wear armor (undies only!), but
 * carry big swords 
 */
public class CBarbarian extends CGladiator {
   /**
    * The initial life value for this gladiator type
    */
   private static int INITIAL_LIFE = 25;
   
   /**
    * CONSTRUCTOR
    */
   public CBarbarian( String name )
   {
      super( name, INITIAL_LIFE );
   }
   
   /**
    * Returns a string stating the "type" of this gladiator
    * @return The gladiator type
    */
   public String getType() {
      return "Barbarian";
   }
   
   /**
    * Retrieves the amount of damage performed by this gladiator
    * @return The damage amount
    */
   protected int getDamage()
   {
      return (int)Math.floor( Math.random() * 20 ) + 1;
   }
}