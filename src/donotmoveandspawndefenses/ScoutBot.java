package donotmoveandspawndefenses;

import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

public class ScoutBot {

	static AdvancedRobotController rc;
	static final int ENEMY_DETECTED_BROADCAST_LENGTH = RobotType.SCOUT.sensorRadiusSquared * 2;
	
	public static void scoutCode(AdvancedRobotController rcIn) {
		rc = rcIn;
		
		while(true){
			try{
				RobotInfo[] enemiesTeam = rc.senseNearbyRobots(RobotType.SCOUT.sensorRadiusSquared, rc.getTeam().opponent());
				if(enemiesTeam.length > 0 && Math.random() > 0.95){
					
					int enemy_x = enemiesTeam[0].location.x;
					int enemy_y = enemiesTeam[0].location.y;
					
					// If no collision with order keys
					if(enemy_x >= 0 && enemy_x < Order.values().length){
						rc.broadcastMessageSignal(enemy_x, enemy_y, ENEMY_DETECTED_BROADCAST_LENGTH);
					}
				} else {
					// listen to orders
					Strategy.listenToOrders(rc);
					Clock.yield();
				}
			}catch(GameActionException e){
				e.printStackTrace();
			}
		}

	}
}
