package team192;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.Signal;

public class Strategy {
	
	private static AdvancedRobotController rc;
	
	
	
	
	
	static final Order[] ordersDictionary = new Order[]{Order.STEP_AHEAD_0,Order.KEEP_MOVING_FOWARD_2,Order.KEEP_MOVING_FOWARD_2};
	
	
	static final int RESCUE_OPERATION_DELAY = 1;
	static int lastRescueOperationTurn = 0;
	
	
	/**
	 * 
	 * @param order
	 * @param orderSentFromLocation
	 * @param executor
	 * @throws GameActionException 
	 */
	public static void applyStrategy(int[] order, MapLocation orderSentFromLocation, AdvancedRobotController executor) throws GameActionException{
		rc = executor;
		
		int orderKey = order[0];
		int orderValue = order[1];
		Direction commanderDirection = rc.getLocation().directionTo(orderSentFromLocation);
		
		
		switch(ordersDictionary[orderKey]){
		case KEEP_MOVING_FOWARD_2:
			
			// GREEN LINE AND DOT ON COMMANDER
			rc.setIndicatorLine(orderSentFromLocation, rc.getLocation(), 0, 255, 0);
			rc.setIndicatorDot(orderSentFromLocation, 0, 255, 0);
			
			rc.setIndicatorString(0, "I am going "+commanderDirection.opposite());
			
			Movement.moveForwardish(commanderDirection.opposite(), rc);
			break;
		case GATHER_1:
			int currentRound = rc.getRoundNum();
			if(lastRescueOperationTurn == 0 || (currentRound-RESCUE_OPERATION_DELAY>lastRescueOperationTurn)){
				lastRescueOperationTurn = currentRound;
				
				// BLUE LINE
				rc.setIndicatorLine(orderSentFromLocation, rc.getLocation(), 0, 0, 255);
				rc.setIndicatorString(0, "I am going "+commanderDirection);
				
				Movement.moveForwardish(commanderDirection, rc);
			}
			break;
		case STEP_AHEAD_0:
		
			break;
		default:
			break;
		}
	}
	
	public static void listenToOrders(AdvancedRobotController listener) throws GameActionException {
		Signal incomingSignal = listener.readAllySignal();
		if(incomingSignal != null){
			int[] message = incomingSignal.getMessage();
			if(message != null){
				// MessageSignal with 2 ints
				applyStrategy(message, incomingSignal.getLocation() ,listener);
			} else {
				// Basic message
			}
		}
	}
}
