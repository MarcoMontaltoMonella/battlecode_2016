package donotmoveandspawndefenses;

import battlecode.common.GameActionException;
import battlecode.common.MapLocation;

public class Strategy {
	
	private static AdvancedRobotController rc;
	
	enum Order {
		STEP_AHEAD_0,			
		RALLY_1,				
		KEEP_MOVING_FOWARD_2	
	}
	/**
	 * 
	 * @param order
	 * @param orderSentFromLocation
	 * @param executor
	 * @throws GameActionException 
	 */
	public static void applyStrategy(int[] order, MapLocation orderSentFromLocation, AdvancedRobotController executor) throws GameActionException{
		rc = executor;
		
		int orderValue = order[1];
		
		switch(Order.values()[order[0]]){
		case KEEP_MOVING_FOWARD_2:
			Utility.moveForwardish(orderSentFromLocation.directionTo(rc.getLocation()), executor);
			
			break;
		case RALLY_1:
			break;
		case STEP_AHEAD_0:
		
			break;
		default:
			break;
		}
	}
}
