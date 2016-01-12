package donotmoveandspawndefenses;

import battlecode.common.RobotController;
import battlecode.common.RobotType;

public class RobotPlayer {
	
	static AdvancedRobotController rc;
	
	/*
	 * DO NOT MOVE AND KEEP SPAWNING DEFENCES
	 */
	public static void run(RobotController rcIn) {
		rc = new AdvancedRobotController(rcIn);
		
		if(rc.getType()==RobotType.ARCHON){
			ArchonBot.archonCode(rc);
		}else if(rc.getType()==RobotType.GUARD){
			GuardBot.guardCode(rc);
		} else if(rc.getType()==RobotType.SCOUT){
			// Scout code
			ScoutBot.scoutCode(rc);
		} else if(rc.getType()==RobotType.SOLDIER){
			// Soldier code
			SoldierBot.soldierCode(rc);
		} else if(rc.getType()==RobotType.VIPER){
			// Viper code
			ViperBot.viperCode(rc);
		} else {
			
			// Avoiding the return, and making the switching possible.
			while(true){
				if(rc.getType()==RobotType.TURRET){
					// Turret code
					TurretBot.turretCode(rc);
				} else if(rc.getType()==RobotType.TTM){
					// TTM code
					TTMBot.ttmCode(rc);
				}
			}
		}
	}	
	
}
