package donotmoveandspawndefenses; 

import battlecode.common.*; 

public class AdvancedRobotController implements RobotController {
	
	private RobotController rc;
	
	public AdvancedRobotController(RobotController rc) {
		this.rc = rc;
	}
	
	// Advanced and missing features
	public boolean isIsolated(){
		return rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, rc.getTeam()).length == 0;
	}
	
	public RobotInfo[] senseNearbyAllies(){
		return rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, rc.getTeam());
	}
	
	public RobotInfo[] senseAlliesWhoNeedsToBeRepaired(){
		return rc.senseNearbyRobots(rc.getType().attackRadiusSquared, rc.getTeam());
	}
	
	public RobotInfo[] senseAlliesAroundMe(){
		return rc.senseNearbyRobots(2, rc.getTeam());
	}
	
	public RobotInfo[] senseNeutralRobots(){
		return rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, Team.NEUTRAL);
	}
	
	public MapLocation getAdjacentNeutralBotLocation(){
		RobotInfo[] adjNeutral = rc.senseNearbyRobots(2, Team.NEUTRAL);
		if(adjNeutral.length>0){
			return adjNeutral[0].location;
		}
		return null;
	}
	
	public Signal readAllySignal(){
		Signal s = rc.readSignal();
		if(s != null && s.getTeam()==rc.getTeam())
			return s;	
		return null;
	}
	
	public boolean hasFullHealth(){
		return rc.getHealth()==rc.getType().maxHealth;
	}
	
	public void moveToLocation(MapLocation location) throws GameActionException {
		Direction directionToLocation = rc.getLocation().directionTo(location);
		Movement.moveForwardish(directionToLocation,this);
	}
	
	public MapLocation locationOfNearbyFriendInDanger() {
		MapLocation location = null;
		RobotInfo[] alliesInSight = rc.senseNearbyRobots(rc.getType().sensorRadiusSquared, rc.getTeam());
		for(RobotInfo r : alliesInSight){
			if(r.maxHealth-r.health>0){
				location = r.location;
				break;
			}
		}
		return location;
	}
	
	/**
	 * 
	 * @return a percentage: death is 0.0, full-health is 1.0
	 */
	public double getRelativeLife(){
		double maxH = rc.getType().maxHealth;
		return (maxH - rc.getHealth() )/maxH;
	}
	
	
	
	// RobotController methods
	@Override
	public void activate(MapLocation loc) throws GameActionException {
		rc.activate(loc);
	}
	@Override
	public void addMatchObservation(String observation) {
		rc.addMatchObservation(observation);
	}
	@Override
	public void attackLocation(MapLocation loc) throws GameActionException {
		rc.attackLocation(loc);
	}
	@Override
	public void broadcastMessageSignal(int message1, int message2, int radiusSquared) throws GameActionException {
		rc.broadcastMessageSignal(message1, message2, radiusSquared);
	}
	@Override
	public void broadcastSignal(int radiusSquared) throws GameActionException {
		rc.broadcastSignal(radiusSquared);
	}
	@Override
	public void build(Direction dir, RobotType type) throws GameActionException {
		rc.build(dir, type);
	}
	@Override
	public boolean canAttackLocation(MapLocation loc) {
		return rc.canAttackLocation(loc);
	}
	@Override
	public boolean canBuild(Direction dir, RobotType type) {
		return rc.canBuild(dir, type);
	}
	@Override
	public boolean canMove(Direction dir) {
		return rc.canMove(dir);
	}
	@Override
	public boolean canSense(MapLocation loc) {
		return rc.canSense(loc);
	}
	@Override
	public boolean canSenseLocation(MapLocation loc) {
		return rc.canSenseLocation(loc);
	}
	@Override
	public boolean canSenseRobot(int id) {
		return rc.canSenseRobot(id);
	}
	@Override
	public void clearRubble(Direction dir) throws GameActionException {
		rc.clearRubble(dir);
	}
	@Override
	public void disintegrate() {
		rc.disintegrate();
	}
	@Override
	public Signal[] emptySignalQueue() {
		return rc.emptySignalQueue();
	}
	@Override
	public int getBasicSignalCount() {
		return rc.getBasicSignalCount();
	}
	@Override
	public long getControlBits() {
		return rc.getControlBits();
	}
	@Override
	public double getCoreDelay() {
		return rc.getCoreDelay();
	}
	@Override
	public double getHealth() {
		return rc.getHealth();
	}
	@Override
	public int getID() {
		return rc.getID();
	}
	@Override
	public int getInfectedTurns() {
		return rc.getInfectedTurns();
	}
	@Override
	public MapLocation getLocation() {
		return rc.getLocation();
	}
	@Override
	public int getMessageSignalCount() {
		return rc.getMessageSignalCount();
	}
	@Override
	public int getRobotCount() {
		return rc.getRobotCount();
	}
	@Override
	public int getRoundLimit() {
		return rc.getRoundLimit();
	}
	@Override
	public int getRoundNum() {
		return rc.getRoundNum();
	}
	@Override
	public Team getTeam() {
		return rc.getTeam();
	}
	@Override
	public long[] getTeamMemory() {
		return rc.getTeamMemory();
	}
	@Override
	public double getTeamParts() {
		return rc.getTeamParts();
	}
	@Override
	public RobotType getType() {
		return rc.getType();
	}
	@Override
	public int getViperInfectedTurns() {
		return rc.getViperInfectedTurns();
	}
	@Override
	public double getWeaponDelay() {
		return rc.getWeaponDelay();
	}
	@Override
	public int getZombieInfectedTurns() {
		return rc.getZombieInfectedTurns();
	}
	@Override
	public ZombieSpawnSchedule getZombieSpawnSchedule() {
		return rc.getZombieSpawnSchedule();
	}
	@Override
	public boolean hasBuildRequirements(RobotType type) {
		return rc.hasBuildRequirements(type);
	}
	@Override
	public boolean isCoreReady() {
		return rc.isCoreReady();
	}
	@Override
	public boolean isInfected() {
		return rc.isInfected();
	}
	@Override
	public boolean isLocationOccupied(MapLocation loc) throws GameActionException {
		return rc.isLocationOccupied(loc);
	}
	@Override
	public boolean isWeaponReady() {
		return rc.isWeaponReady();
	}
	@Override
	public void move(Direction dir) throws GameActionException {
		rc.move(dir);
	}
	@Override
	public boolean onTheMap(MapLocation loc) throws GameActionException {
		return rc.onTheMap(loc);
	}
	@Override
	public void pack() throws GameActionException {
		rc.pack();
	}
	@Override
	public Signal readSignal() {
		return rc.readSignal();
	}
	@Override
	public void repair(MapLocation loc) throws GameActionException {
		rc.repair(loc);
	}
	@Override
	public void resign() {
		// Never give up!
		// rc.resign();
	}
	@Override
	public RobotInfo[] senseHostileRobots(MapLocation center, int radiusSquared) {
		return rc.senseHostileRobots(center, radiusSquared);
	}
	@Override
	public RobotInfo[] senseNearbyRobots() {
		return rc.senseNearbyRobots();
	}
	@Override
	public RobotInfo[] senseNearbyRobots(int radiusSquared) {
		return rc.senseNearbyRobots(radiusSquared);
	}
	@Override
	public RobotInfo[] senseNearbyRobots(int radiusSquared, Team team) {
		return rc.senseNearbyRobots(radiusSquared, team);
	}
	@Override
	public RobotInfo[] senseNearbyRobots(MapLocation center, int radiusSquared, Team team) {
		return rc.senseNearbyRobots(center, radiusSquared, team);
	}
	@Override
	public double senseParts(MapLocation loc) {
		return rc.senseParts(loc);
	}
	@Override
	public RobotInfo senseRobot(int id) throws GameActionException {
		return rc.senseRobot(id);
	}
	@Override
	public RobotInfo senseRobotAtLocation(MapLocation loc) throws GameActionException {
		return rc.senseRobotAtLocation(loc);
	}
	@Override
	public double senseRubble(MapLocation loc) {
		return rc.senseRubble(loc);
	}
	@Override
	public void setIndicatorDot(MapLocation loc, int red, int green, int blue) {
		rc.setIndicatorDot(loc, red, green, blue);
	}
	@Override
	public void setIndicatorLine(MapLocation from, MapLocation to, int red, int green, int blue) {
		rc.setIndicatorLine(from, to, red, green, blue);
	}
	@Override
	public void setIndicatorString(int stringIndex, String newString) {
		rc.setIndicatorString(stringIndex, newString);
	}
	@Override
	public void setTeamMemory(int index, long value) {
		rc.setTeamMemory(index, value);
	}
	@Override
	public void setTeamMemory(int index, long value, long mask) {
		rc.setTeamMemory(index, value, mask);
	}
	@Override
	public void unpack() throws GameActionException {
		rc.unpack();
	}

	@Override
	public MapLocation[] sensePartLocations(int radiussquared) {
		return rc.sensePartLocations(radiussquared);
	}
}
