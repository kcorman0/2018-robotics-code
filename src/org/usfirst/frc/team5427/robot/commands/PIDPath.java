package org.usfirst.frc.team5427.robot.commands;

import org.usfirst.frc.team5427.robot.Robot;
import org.usfirst.frc.team5427.util.Config;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Command;

public class PIDPath extends Command
{
	private PIDDriveTrainSide firstDistance, secondDistance, thirdDistance;
	private PIDTurnCommand firstAngle, secondAngle;
	
	
	public PIDPath()
	{
		//creates all of the PID Commands
		firstDistance = new PIDDriveTrainSide(Robot.driveTrain.drive_Right, Robot.driveTrain.drive_Left, 0, 60);//160
		firstAngle = new PIDTurnCommand(Robot.driveTrain.drive_Right, Robot.driveTrain.drive_Left, Config.PID_TURN_P, Config.PID_TURN_I, Config.PID_TURN_D, 25);
		secondDistance = new PIDDriveTrainSide(Robot.driveTrain.drive_Right, Robot.driveTrain.drive_Left, 0, 20);//36
		secondAngle = new PIDTurnCommand(Robot.driveTrain.drive_Right, Robot.driveTrain.drive_Left, Config.PID_TURN_P, Config.PID_TURN_I, Config.PID_TURN_D, 35);
		thirdDistance = new PIDDriveTrainSide(Robot.driveTrain.drive_Right, Robot.driveTrain.drive_Left, 0, 40);//12
	}

	//begins the command
	 public void initialize() {
		 System.out.println("First Dist Starting");
		 firstDistance.start(); 
	  
	 }

	//uses the previous commands being null to check if a certain command needs to be started or not
	public void execute()
	{
		//TODO Check this, we may get null pointer exceptions if this is wrong
		//If firstDistance, first angle, and secondDistance are all null and SecondAngle isFinished
		//and the thirdDistance Command is not running, run the thirdDistance Command
		if(null==firstDistance&&null==firstAngle&&null==secondDistance&&secondAngle.isFinished()&&!thirdDistance.isRunning())
		{
			System.out.println("Third Dist Starting");
			thirdDistance.start();
			secondAngle=null;

		}
		//If firstDistance, first angle are all null and secondDistance isFinished && not null
		//and the secondAngle Command is not running, run the secondAngle Command
		else if(null==firstDistance&&null==firstAngle&&null!=secondDistance&&secondDistance.isFinished()&&!secondAngle.isRunning())
		{
			System.out.println("Sec Angle Starting");
			secondAngle.start();
			secondDistance=null;

		}
		//If firstDistance is null and firstAngle isFinished && not null
		//and the secondDistance Command is not running, run the secondDistance Command
		else if(null==firstDistance&&null!=firstAngle&&firstAngle.isFinished()&&!secondDistance.isRunning())
		{
			System.out.println("Second Dist Starting");
			secondDistance.start();
			firstAngle=null;

		}
		//If firstDistance is NOT null and firstDistance isFinished 
		//and the firstAngle Command is not running, run the firstAngle Command
		else if(null!=firstDistance&&firstDistance.isFinished()&&!firstAngle.isRunning())
		{
			System.out.println("First Angle Starting");
			firstAngle.start();
			firstDistance=null;
		}
		
		//OLDER IDEA
//		//if the firstDistance command exists and has finished, and the angle command is not running
//		if(firstDistance!=null&&firstDistance.isFinished()&&!firstAngle.isRunning())
//		{
//			//set the firstDistance command to null and start the firstAngle command
//			firstDistance=null;
//			firstAngle.start();
//		}
	}

	@Override
	protected boolean isFinished() {
		//returns if the last distance has finished
		return thirdDistance.isFinished();
	}
	
//	@Override
//	protected void  end() {
//		firstAngle.free();
//		firstDisance
//	}
}
