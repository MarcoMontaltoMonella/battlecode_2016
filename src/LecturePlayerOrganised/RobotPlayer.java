package LecturePlayerOrganised;

import java.util.ArrayList;


import battlecode.common.*;
import scala.annotation.meta.param;


public class RobotPlayer {



	public static void run (RobotController rc){

		Direction movingDirection = Direction.EAST;
		while(true){

			if (rc.getTeam()==Team.B){
				movingDirection = Direction.WEST;
			}
			try {

				repeat(movingDirection, rc);
				Clock.yield();
			}

			//				fial("Hey bro you failed!");
			catch (GameActionException e) {
				// TODO: handle exception
				e.printStackTrace();		


			}
		}

	}


	public static void repeat(Direction MovingDirection, RobotController rc) throws GameActionException{
		RobotInfo[] zombies = rc.senseNearbyRobots(rc.getType().attackRadiusSquared, Team.ZOMBIE);
		RobotInfo[] normalEnemies = rc.senseNearbyRobots(rc.getType().attackRadiusSquared,rc.getTeam().opponent());
		RobotInfo[] opponentEnemies = joinRobotInfo(zombies , normalEnemies);
		if(zombies.length > 0 && rc.getType().canAttack()){
			if(rc.isWeaponReady()){
				rc.attackLocation(opponentEnemies[0].location);;
			}
		}else{

			if(rc.isCoreReady()){
				MovingUtility.forwardish(MovingDirection , rc);
			}
		}


	}





	private static RobotInfo[] joinRobotInfo(RobotInfo[] zombies, RobotInfo[] normalEnemies) {
		// TODO Auto-generated method stub

		RobotInfo [] Total = new RobotInfo [zombies.length+normalEnemies.length] ;
		int i = 0;
		for ( RobotInfo zombie : zombies){
			Total[i] = zombie;
		}
		for ( RobotInfo normalEnemy : normalEnemies){
			Total[i] = normalEnemy;
		}


		return Total;
	}



}
