package infectionteam;

import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.GameActionExceptionType;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.RobotType;


public class RobotPlayer {

	static AdvancedRobotController rc;
	static Direction tempDirection = Direction.SOUTH_EAST;
	static int steps = 0;
	static final int LAST_STEP = (int) Math.sqrt(RobotType.SCOUT.sensorRadiusSquared);
	static final int WAVES = 10;
	static int attackTurn;
	
	public static void run(RobotController standardRC) {
		rc = new AdvancedRobotController(standardRC);
		
		attackTurn = rc.getRoundLimit()/WAVES;
		
		RobotType type = rc.getType();
		
		

		while (true) {
			try {
				switch (type) {
				case ARCHON:
					if(rc.isCoreReady() && rc.canBuild(tempDirection, RobotType.SCOUT)){
						rc.build(tempDirection, RobotType.SCOUT);
					}
					break;
				case GUARD:
					break;
				case SCOUT:
					if(steps == LAST_STEP){
						// kaboom!
						rc.disintegrate();
						
					}
					if(Movement.moveForwardish(tempDirection, rc)){						
						steps++;
					}
					break;
				case SOLDIER:
					break;
				case TTM:
					break;
				case TURRET:
					break;
				case VIPER:
					break;
				default:
					throw new GameActionException(GameActionExceptionType.INTERNAL_ERROR,
							"This is not the droid you are looking for...");
				}

			} catch (GameActionException e) {
				e.printStackTrace();
			}
			Clock.yield();
		}
	}
}
