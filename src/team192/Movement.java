package team192;


import java.util.ArrayList;

import battlecode.common.*;

public class Movement {
	
	static int[] possibleDirections = new int[]{0,1,-1,2,-2,3,-3,4};
	static ArrayList<MapLocation> pastLocations = new ArrayList<MapLocation>();
	
	
	public static void moveForwardish(Direction ahead, AdvancedRobotController rc) throws GameActionException {
		for(int i:possibleDirections){
			Direction candidateDirection = Direction.values()[(ahead.ordinal()+i+8)%8];
			MapLocation candidateLocation = rc.getLocation().add(candidateDirection);
			
			// check if we can move to a candidate direction which it has not been visited before (in the last 20 locations) 
			if(rc.canMove(candidateDirection) && !pastLocations.contains(candidateLocation)){
				pastLocations.add(rc.getLocation());
				if(pastLocations.size() > 10){
					//remove oldest position
					pastLocations.remove(0);
				}
				rc.move(candidateDirection);
				break;
			}
		}
		if(rc.getType().canClearRubble()){
			//failed to move, look to clear rubble
			MapLocation forward = rc.getLocation().add(ahead);
			if(rc.senseRubble(forward)>=GameConstants.RUBBLE_OBSTRUCTION_THRESH){
				rc.clearRubble(ahead);
			}
		}
		
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
