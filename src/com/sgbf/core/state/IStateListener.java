package com.sgbf.core.state;


/**
 * An interface that should be implemented
 * by any object that wishes to listen for
 * changes to the state
 * 
 * State listeners must register themselves
 * with the CStateMananger (via registerListener)
 * in order to receive updates
 */
public interface IStateListener {

   /**
    * Called by the state manager whenever a state 
    * object is modified
    * @param newState   The new state object
    * @param oldState   The state object prior to modification
    */
   void onStateChange( final CState newState, final CState oldState );
}