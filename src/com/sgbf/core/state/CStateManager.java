package com.sgbf.core.state;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * The state manager holds a list of objects that represent 
 * the current "state" of the game.
 * 
 * Any object may register itself as a "state listener".
 * 
 * When the "state" is updated, it is performed via the 
 * state manager, which then alerts any "state listeners"
 * of the new state.
 */
public class CStateManager {

   /**
    * Singleton instance
    */
   static CStateManager singleton = null;
      
   /**
    * The set of states keyed by each state's ID value
    * (i.e. CState.getId() )
    */
   private HashMap states;
   
   /**
    * The queue of states waiting to be written
    */
   private LinkedList stateQueue;

   /**
    * The current set of state listeners
    */
   private LinkedList listeners;
   
   /**
    * Whether the state manager is idle
    * (i.e. not processing state updates)
    */
   private boolean idle;
   
   /**
    * Constructor is private
    * Access must be via the static "instance" function
    */
   private CStateManager() {
      states = new HashMap();
      stateQueue = new LinkedList();
      listeners = new LinkedList();
      idle = true;
   }
   
   /**
    * Singleton retrieval
    */
   public static CStateManager instance()
   {
      if( CStateManager.singleton == null ) {
         CStateManager.singleton = new CStateManager();
      }
      return CStateManager.singleton;
   }
   
   /**
    * Registers the given object as a state listener
    */
   public void registerListener( IStateListener listener )
   {
      listeners.add( listener );
   }
   
   /**
    * Removes the given object as a state listener
    */
   public void deregisterListener( IStateListener listener )
   {
      listeners.remove( listener );
   }
   
   /**
    * Retrieves the state with the given ID
    * @param id   The id of the state (i.e. CState.getId() )
    * @return The requested state object, or null if it doesn't exist
    */
   public final CState getState( String id )
   {
      return ( states.containsKey( id ) ) ? states.get( id ) : null; 
   }
   
   /**
    * Adds the given state to the queue to be stored
    * and dispatched to listeners 
    * @param state      The new state
    */
   public void setState( CState state )
   {
      stateQueue.add( state );
      
      // If not currently processing any state updates,
      // we can update and dispatch the state now!
      if( idle ) {
         updateStates();
      }
   }
   
   /**
    * Performs the actual writing of the state 
    * and dispatches to the listeners
    */
   public void updateStates()
   {
      // Prevent entry if we're already processing states
      if( !idle ) {
         return;
      }
      idle = false;
      
      // Traverse the pending state queue
      while( stateQueue.size() > 0 ) {
         CState newState = stateQueue.removeFirst();
         CState oldState = states.containsKey( newState.getId() ) 
                           ? states.get( newState.getId() ) 
                           : null;
                           
         // Write the new state
         states.put( newState.getId(), newState );
         
         // Dispatch to listeners that the state is modified
         for( int i = 0; i < listeners.size(); i++ ) {
            listeners.get(i).onStateChange( newState, oldState );
         }
      }
      
      // Return to idle when state list is exhausted
      idle = true;
   }
}