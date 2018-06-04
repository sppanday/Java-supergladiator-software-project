/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sgbf.main;

import com.sgbf.core.state.CStateManager;
import com.sgbf.gladiators.CBarbarian;
import com.sgbf.gladiators.CGladiator;
import com.sgbf.gladiators.CSamnite;
import com.sgbf.gladiators.CGLadiatorState;

/**
 *
 * @author surendrapanday
 */
public class SuperGladiatorBattleFestival {

    /**
     * @param args the command line arguments
     */
   CTestListener test = new CTestListener();
	CGladiator barbarian = new CBarbarian("barbarian");
	CGladiator samnite = new CSamnite("samnite");
	CStateManager.instance().setState( new CGladiatorState("Blank",0) );
    
}
