package com.sgbf.gladiators;

/**
 * Samnites are heavily armored but 
 * poorly armed 
 */
public class CSamnite extends CGladiator {
   /**
    * The initial life value for this gladiator type
    */
   private static int INITIAL_LIFE = 100;
   
   /**
    * CONSTRUCTOR
    */
   public CSamnite( String name )
   {
      super( name, INITIAL_LIFE );
   }
   
   /**
    * Returns a string stating the "type" of this gladiator
    * @return The gladiator type
    */
   public String getType() {
      return "Samnite";
   }
   
   /**
    * Retrieves the amount of damage performed by this gladiator
    * @return The damage amount
    */
   protected int getDamage()
   {
      return (int)Math.floor( Math.random() * 5 ) + 1;
   }
}