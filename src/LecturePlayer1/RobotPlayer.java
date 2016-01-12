package LecturePlayer1;

import java.util.ArrayList;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import battlecode.common.*;
import battlecode.server.serializer.XStreamSerializerFactory.MapLocationConverter;

public class RobotPlayer {
	static int [] possibleDirections = new int [] { 0 , 1 , -1 , 2 , -2 , -2 , 3,  -3, 4 };
	static ArrayList<MapLocation> VisitedNodes = new ArrayList< MapLocation >();




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
				forwardish(MovingDirection , rc);
			}
		}


	}


	private static void forwardish(Direction ahead , RobotController rc) throws GameActionException{
		// TODO Auto-generated method stub

		for(int i : possibleDirections){

			Direction candidateDirection = Direction.values()[(ahead.ordinal()+i)%8];
			MapLocation candidatelocation = rc.getLocation().add(candidateDirection);

			if(rc.canMove(candidateDirection)&& !VisitedNodes.contains(candidatelocation)){

				VisitedNodes.add(candidatelocation);
				if(VisitedNodes.size()>20){
					VisitedNodes.remove(0);
				}
				rc.move(candidateDirection);
				break;
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
