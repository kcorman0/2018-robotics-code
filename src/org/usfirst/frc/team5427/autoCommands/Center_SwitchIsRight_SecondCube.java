package org.usfirst.frc.team5427.autoCommands;

import org.usfirst.frc.team5427.robot.Robot;
import org.usfirst.frc.team5427.robot.commands.AutoInGo;
import org.usfirst.frc.team5427.robot.commands.DriveBackward;
import org.usfirst.frc.team5427.robot.commands.DriveForward;
import org.usfirst.frc.team5427.robot.commands.MoveElevatorAuto;
import org.usfirst.frc.team5427.util.SameLine;

/**
 * Picks up the second cube and places it for Center_SwitchIsLeft.
 * 
 * @author Blake Romero, Andrew Li
 */
@SameLine
public class Center_SwitchIsRight_SecondCube extends AutoPath {
	/**
	 * Moves the robot backwards from the switch while moving the elevator down.
	 */
	public DriveBackward backOff;
	/**
	 * Moves the robot forwards towards the switch while moving the elevator up.
	 */
	public DriveForward moveForward;
	/**
	 * Pulls the second cube into the robot using the intake.
	 */
	public AutoInGo intakeCube;
	/**
	 * Moves the elevator down to the lowest position.
	 */
	public MoveElevatorAuto elevatorDown;
	/**
	 * Moves the elevator up to the middle position.
	 */
	public MoveElevatorAuto elevatorUp;
	/**
	 * Curves the robot towards the cube it intends to pick up.
	 */
	public Center_SwitchIsRight_CurveToCube curveToCube;
	/**
	 * Curves the robot away from the cube it intends to pick up.
	 */
	public Center_SwitchIsRight_CurveAwayFromCube curveBack;

	public Center_SwitchIsRight_SecondCube() {
		backOff = new DriveBackward(2.5);
		moveForward = new DriveForward(1);
		intakeCube = new AutoInGo();
		elevatorDown = new MoveElevatorAuto(4);
		elevatorUp = new MoveElevatorAuto(1);
		curveToCube = new Center_SwitchIsRight_CurveToCube();
		curveBack = new Center_SwitchIsRight_CurveAwayFromCube();
	}

	@Override
	public void initialize() {
		backOff.start();
		elevatorDown.start();
	}

	@Override
	public void execute() {
		
		if(null == backOff && null == curveToCube && null == intakeCube && null != curveBack && curveBack.isFinished()) {
			curveBack.cancel();
			curveBack = null;
			Robot.ahrs.reset();
			Robot.encLeft.reset();
			moveForward.start();
			elevatorUp.start();
		}
		else if(null == backOff && null != curveToCube && curveToCube.isFinished() && null != intakeCube && intakeCube.isFinished()) {
			curveToCube.cancel();
			curveToCube = null;
			intakeCube.cancel();
			intakeCube = null;
			Robot.ahrs.reset();
			Robot.encLeft.reset();
			curveBack.start();
		}
		else if(null != backOff && backOff.isFinished()) {
			backOff.cancel();
			backOff = null;
			Robot.ahrs.reset();
			Robot.encLeft.reset();
			curveToCube.start();
			intakeCube.start();
		}
		
	}

	@Override
	public boolean isFinished() {
		return (null != moveForward && moveForward.isFinished() && null != elevatorUp && elevatorUp.isFinished());
	}
	
	@Override
	public void end() {
		super.end();
	}
}