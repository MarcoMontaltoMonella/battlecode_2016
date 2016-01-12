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
		} else if(rc.getType()==RobotType.SOLDIER){
			// Soldier code
		} else if(rc.getType()==RobotType.VIPER){
			// Viper code
		} else if(rc.getType()==RobotType.TURRET){
			// Turret code
		} else if(rc.getType()==RobotType.TTM){
			// TTM code
		}
	}	
	
}
