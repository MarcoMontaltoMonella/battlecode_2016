package donotmoveandspawndefenses;


import java.util.ArrayList;

import battlecode.common.*;

public class Movement {
	
	static int[] possibleDirections = new int[]{0,1,-1,2,-2,3,-3,4};
	static ArrayList<MapLocation> pastLocations = new ArrayList<MapLocation>();
	static final int PAST_LOCATIONS_CACHE_SIZE = 10;
	static boolean patient = true;
	
	
	public static void moveForwardish(Direction ahead, AdvancedRobotController rc) throws GameActionException {
		if(rc.getType()==RobotType.TURRET){
			return;
		}
		for(int i:possibleDirections){
			Direction candidateDirection = Direction.values()[(ahead.ordinal()+i+8)%8];
			MapLocation candidateLocation = rc.getLocation().add(candidateDirection);
			
			if(patient){
				// check if we can move to a candidate direction which it has not been visited before (in the last 20 locations) 
				if(rc.canMove(candidateDirection) && !pastLocations.contains(candidateLocation)){
					pastLocations.add(rc.getLocation());
					if(pastLocations.size() > PAST_LOCATIONS_CACHE_SIZE){
						//remove oldest position
						pastLocations.remove(0);
					}
					rc.move(candidateDirection);
					return;
				}
			} else {
//				if(rc.canMove(candidateDirection)){
//					rc.move(candidateDirection);
//					return;
//				}else{
					// dig
					if(rc.senseRubble(candidateLocation)>GameConstants.RUBBLE_OBSTRUCTION_THRESH){
						rc.clearRubble(candidateDirection);
						return;
					}
				//}
				
			}
		}
		patient = false;
	}

	public static void moveRandomly(AdvancedRobotController rc) throws GameActionException {
		for(int i:possibleDirections){
			int rand = rc.getRoundNum()%8;
			Direction candidateDirection = Direction.values()[((rand)+i+8)%8];
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
