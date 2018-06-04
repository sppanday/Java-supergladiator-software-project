/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sgbf.main;

import com.sgbf.core.state.CState;
import com.sgbf.core.state.CStateManager;
import com.sgbf.core.state.IStateListener;

/**
 *
 * @author surendrapanday
 */

public class CTestListener implements IStateListener {
	public CTestListener() {
		CStateManager.instance().registerListener(this);
	}
	public void onStateChange(CState newState, CState oldState) {
		if( CGladiatorState.class.isInstance( newState ) ) {
			CGladiatorState gladiatorState = (CGladiatorState) newState;
			if( gladiatorState.getName().equals("Blank") ) {
				CStateManager.instance().setState( new CGladiatorState("Nothing",0) );
			}
		}
	}

    @Override
    public void onStateChange(CState newState, CState oldState) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
