package donotmoveandspawndefenses;

import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

public class Utility {

	public static boolean canAttackMeFromAfarOrZombieDen(RobotInfo r){
		//SOLDIER, VIPER, TURRET, RANGEDZOMBIE and ZOMBIEDEN
		if(r.type==RobotType.SOLDIER || r.type==RobotType.VIPER || r.type==RobotType.TURRET || r.type==RobotType.RANGEDZOMBIE || r.type==RobotType.ZOMBIEDEN){
			return true;
		}
		return false;
	}
	
	public static boolean isAnArchon(RobotInfo r){
		return r.type==RobotType.ARCHON;
	}
}
