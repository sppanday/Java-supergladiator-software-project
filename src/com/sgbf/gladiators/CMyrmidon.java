package com.sgbf.gladiators;

/**
 * Myrmidons are a medium armor, 
 * relatively well armed gladiator 
 */
public class CMyrmidon extends CGladiator {
   
   /**
    * The initial life value for this gladiator type
    */
   private static int INITIAL_LIFE = 50;
   
   /**
    * CONSTRUCTOR
    */
   public CMyrmidon( String name )
   {
      super( name, INITIAL_LIFE );
   }
   
   /**
    * Returns a string stating the "type" of this gladiator
    * @return The gladiator type
    */
   public String getType() {
      return "Myrmidon";
   }
   
   /**
    * Retrieves the amount of damage performed by this gladiator
    * @return The damage amount
    */
   protected int getDamage()
   {
      return (int)Math.floor( Math.random() * 10 ) + 1;
   }
}