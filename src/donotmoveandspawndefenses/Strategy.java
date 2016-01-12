package donotmoveandspawndefenses;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotType;
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
			
			boolean result_tmkfieos = false;
			if(rc.getType()==RobotType.TURRET){
				result_tmkfieos = turretsMustKeepFiringIfEnemiesOnSight();
			}
			
			if(!result_tmkfieos){
				// GREEN LINE AND DOT ON COMMANDER
				rc.setIndicatorLine(orderSentFromLocation, rc.getLocation(), 0, 255, 0);
				rc.setIndicatorDot(orderSentFromLocation, 0, 255, 0);
				
				rc.setIndicatorString(0, "I am going "+commanderDirection.opposite());
				
				Movement.moveForwardish(commanderDirection.opposite(), rc);
			}
			break;
		case GATHER_1:
			
			result_tmkfieos = false;
			if(rc.getType()==RobotType.TURRET){
				result_tmkfieos = turretsMustKeepFiringIfEnemiesOnSight();
			}
			
			if(!result_tmkfieos){
				
				int currentRound = rc.getRoundNum();
				if(lastRescueOperationTurn == 0 || (currentRound-RESCUE_OPERATION_DELAY>lastRescueOperationTurn)){
					lastRescueOperationTurn = currentRound;
					
					// BLUE LINE
					rc.setIndicatorLine(orderSentFromLocation, rc.getLocation(), 0, 0, 255);
					rc.setIndicatorString(0, "I am going "+commanderDirection);
					
					Movement.moveForwardish(commanderDirection, rc);
				}
			}
			break;
		case STEP_AHEAD_0:
		
			break;
		default:
			// Move Viper towards a spotted enemy
			if(rc.getType()==RobotType.VIPER){
				MapLocation enemySpottedLocation = new MapLocation(orderKey, orderValue);
				Movement.moveForwardish(rc.getLocation().directionTo(enemySpottedLocation), executor);
			}
			break;
		}
	}
	
	private static boolean turretsMustKeepFiringIfEnemiesOnSight(){
		return (rc.senseHostileRobots(rc.getLocation(), RobotType.TURRET.sensorRadiusSquared).length > 0);
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
