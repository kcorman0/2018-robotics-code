package org.usfirst.frc.team5427.robot.commands;

import org.usfirst.frc.team5427.robot.Robot;
import org.usfirst.frc.team5427.util.Config;
import org.usfirst.frc.team5427.util.SameLine;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This class is adapted from the WPILib PIDCommand Class. This class is made to
 * make the robot drive a certain distance given a maximum power. It works by allowing
 * the maximum output of the PID loop to be the maximum power and allowing it smoothly
 * reach the desired distance. The other side of the drive train is continually
 * controlled by the PID loop in PIDStraightMovement.
 * 
 * @author Blake
 */
@SameLine
public class PIDDistance extends PIDCommand {
	
	//This SpeedControllerGroup is the side of the robot that this command controls.
	private SpeedControllerGroup scgPIDControlled;
	private Timer timer;
	boolean timerStarted;
	//This is the distance we want to travel.
	double desiredDistance;
	//This is the max speed for the output range of the PID Controller.
	double maximumSpeed;

	/**
	 * Constructor for PIDDistance
	 * @param scgPIDControlled
	 * 	- This receives the side of the robot that we are controlling with this PIDCommand.
	 * @param maximumSpeed
	 *  - This receives the maximum speed that the robot will travel at.
	 * @param desiredDistance
	 *  - This receives the distance that we want to travel.
	 * @param p, i, d
	 *  - These receive the P, I, and D values for the PID Controller.
	 */
	public PIDDistance(SpeedControllerGroup scgPIDControlled, double maximumSpeed, double desiredDistance, double p, double i, double d) {
		super(p, i, d);
		this.desiredDistance = desiredDistance;
		this.scgPIDControlled = scgPIDControlled;
		this.maximumSpeed = maximumSpeed;
		super.getPIDController().setOutputRange(-maximumSpeed, maximumSpeed);
		super.getPIDController().setSetpoint(desiredDistance);
		scgPIDControlled.set(0);
		timer = new Timer();
	}

	/**
	 * Command implemented from PIDCommand
	 * This is called automatically after the constructor of the command is run.
	 * We only use this to start the PIDController of moving a certain distance.
	 */
	@Override
	protected void initialize() {
		super.getPIDController().enable();
		timerStarted=false;
	}

	/**
	 * Command implemented from PIDCommand
	 * This returns the value to be used by the PID loop.
	 * We are returning the distance traveled by the robot as measured by the encoders.
	 */
	@Override
	protected double returnPIDInput() {
		return (Math.abs(Robot.encLeft.getDistance()) + Math.abs(Robot.encRight.getDistance())) / 2.0;
	}

	/**
	 * Command implemented from PIDCommand
	 * This is sent the output from the PID loop for us to use.
	 * We are setting the side of the robot that we control in this PID loop to the output to travel a certain distance.
	 */
	@Override
	protected void usePIDOutput(double output) {
		SmartDashboard.putNumber("PID Output Coasting", output);
		this.scgPIDControlled.pidWrite(output);
//		if(this.returnPIDInput()>this.desiredDistance)
//			super.getPIDController().setOutputRange(-.2, .2);//TODO do not set if already set
//		if(this.returnPIDInput()>(this.desiredDistance*.75))
//		{
//			this.maximumSpeed*=0.95;
//			super.getPIDController().setOutputRange(-this.maximumSpeed,this.maximumSpeed);
//		}
	}

	/*
	 * Command implemented from PIDCommand.
	 * When this returns true, the command runs end()
	 * Our method returns true if the robot has traveled close enough to the certain distance, with our tolerance.
	 */
	@Override
	protected boolean isFinished() {
		// TODO NOT untested !
		double distFromSetpoint = Math.abs(desiredDistance - (Math.abs(Robot.encLeft.getDistance()) + Math.abs(Robot.encRight.getDistance())) / 2.0);
		boolean inRange = distFromSetpoint < Config.PID_STRAIGHT_TOLERANCE;
		if (inRange && Math.abs(Robot.ahrs.getYaw())<1) {
			if (!timerStarted)
			{
				timer.reset();
				timer.start();
				timerStarted=true;
			}
			else if (timer.get() > 0.5&&timerStarted) {
				System.out.println("PIDDistance Returning true");
				System.out.println("Left: " + Robot.encLeft.getDistance() + "Right: " + Robot.encRight.getDistance()); //TODO if this only prints once, is finished us only called once
				return true;
			}
		} else {
			timer.reset();
			timerStarted=false;
		}
		return false;
	}
	
	/**
	 * Command implemented from PIDCommand
	 * This is called whenever either isFinished() returns true or interrupted() runs.
	 * We use this to end the PID loop, reset the encoders' distances, and set the speed of our SpeedController.
	 */
	@Override
	public void end() {
		super.end();
		free();
		System.out.println("PIDDIST ENDS");
	}
	
	/**
	 * Command implemented from PIDCommand
	 * This is called manually and is meant to disable the PID loop and reset values.
	 * We are using this to disable the PID loop, reset the PID loop and the encoders' distances, and set the speed of the side of
	 * the robot that we control to 0;
	 */
	@Override
	public void free() {
		System.out.println("Free in PIDDistance");
		super.free();
		super.getPIDController().disable();
		super.getPIDController().reset();
		scgPIDControlled.set(0);
		Robot.encLeft.reset();
		Robot.encRight.reset();
	}
	
	/**
	 * Returns the average distance between the 2 encoders
	 * 
	 */
	public double getDistance() {
		return (Math.abs(Robot.encLeft.getDistance()) + Math.abs(Robot.encRight.getDistance())) / 2.0;
	}
}