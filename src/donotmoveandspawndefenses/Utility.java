package donotmoveandspawndefenses;

import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

public class Utility {

	public static boolean canAttackMeFromAfar(RobotInfo r){
		//SOLDIER, VIPER, TURRET, RANGEDZOMBIE
		if(r.type==RobotType.SOLDIER || r.type==RobotType.VIPER || r.type==RobotType.TURRET || r.type==RobotType.RANGEDZOMBIE ){
			return true;
		}
		return false;
	}
	
	public static boolean isAnArchon(RobotInfo r){
		return r.type==RobotType.ARCHON;
	}
}
