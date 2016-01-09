package donotmoveandspawndefenses;


import java.util.ArrayList;

import battlecode.common.*;

public class Utility {
	
	static int[] possibleDirections = new int[]{0,1,-1,2,-2,3,-3,4};
	static ArrayList<MapLocation> pastLocations = new ArrayList<MapLocation>();
	
	public static boolean canAttackMeFromAfar(RobotInfo r){
		//SOLDIER, VIPER, TURRET, RANGEDZOMBIE
		if(r.type==RobotType.SOLDIER || r.type==RobotType.VIPER || r.type==RobotType.TURRET || r.type==RobotType.RANGEDZOMBIE){
			return true;
		}
		return false;
	}
	
	public static void moveForwardish(Direction ahead, AdvancedRobotController rc) throws GameActionException {
		for(int i:possibleDirections){
			Direction candidateDirection = Direction.values()[(ahead.ordinal()+i+8)%8];
			MapLocation candidateLocation = rc.getLocation().add(candidateDirection);
			
			// check if we can move to a candidate direction which it has not been visited before (in the last 20 locations) 
			if(rc.canMove(candidateDirection) && !pastLocations.contains(candidateLocation)){
				pastLocations.add(rc.getLocation());
				if(pastLocations.size() > 7){
					//remove oldest position
					pastLocations.remove(0);
				}
				rc.move(candidateDirection);
				break;
			}
		}
		
	}
}
