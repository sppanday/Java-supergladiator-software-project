package com.sgbf.core.state;

/**
 * The current "state" of the game is defined
 * by a set of objects that extend from this
 * CState class.
 * 
 * They are held in the state manager, 
 * and similarly, any changes made to them must be 
 * reported to the state manager for propagation
 * to listeners
 */
public abstract class CState {

   /**
    * Retrieves the identifier value for this state
    * @return The id of this state
    */
   public String getId()
   {
      return "";
   }
}