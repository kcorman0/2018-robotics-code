package org.usfirst.frc.team5427.robot.commands;

import org.usfirst.frc.team5427.robot.Robot;
import org.usfirst.frc.team5427.util.Config;
import org.usfirst.frc.team5427.util.SameLine;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.PIDCommand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This command will cause the robot to turn to an exact degree value using the
 * PID Loop TODO add a ramping function
 * 
 * @author Ethan, Varsha
 */
@SameLine
public class PIDTurn extends PIDCommand {
	// two SpeedControllerGroup objects to be controlled by this PID Loop
	private SpeedControllerGroup scgRight, scgLeft;
	double setPoint;
	private double time;
	private Timer timer;

	public PIDTurn(SpeedControllerGroup scgRight, SpeedControllerGroup scgLeft, double setPoint) {
		super(Config.PID_TURN_P, Config.PID_TURN_I, Config.PID_TURN_D);
		this.scgRight = scgRight;
		this.scgLeft = scgLeft;
		this.setPoint = setPoint;
		// lets the PID Loop the range of the input (ahrs)
		super.setInputRange(-180, 180);
		super.setSetpoint(setPoint);
		scgRight.set(0.1);
		scgLeft.set(0.1);
		timer = new Timer();
	}

	// begins the PID loop (enables)
	public void initialize() {
		Robot.ahrs.reset();
		time = 0;
		super.getPIDController().enable();
	}

	// Ends (disables) the PID loop and stops the motors of the
	// SpeedControllerGroups
	public void end() {
		super.free();
		// super.getPIDController().disable();
		// super.getPIDController().free();
		scgRight.set(0);
		scgLeft.set(0);
		super.end();
		System.out.println("PIDTurn ENDED!");
	}

	// Code to run when this command is interrupted
	public void interrupted() {
		end();
	}

	// judge range by what the angle is right now, ex: 91 instead of 90, we want to
	// see if it flatlines
	@Override
	public boolean isFinished() {
		double tolerance = Math.abs(Math.abs(getCurrentAngle()) - Math.abs(super.getSetpoint()));
		boolean inRange = tolerance < Config.PID_TURN_TOLERANCE;
		if (inRange) {
			System.out.println("In Range.");
			if (timer.get() == 0) {
				timer.start();
			}
			if (timer.get() > .75) {
				return true;
			}
		} else {
			timer.reset();
		}
		return false;
	}

	public double getCurrentAngle() {
		return Robot.ahrs.getYaw();
	}

	// return what the PID loop is supposed to read from (feedback value)
	@Override
	protected double returnPIDInput() {
		return Robot.ahrs.getYaw();
	}

	// set motor values with "output"
	@Override
	protected void usePIDOutput(double output) {
		SmartDashboard.putNumber("Yaw", getCurrentAngle());
		SmartDashboard.putNumber("Raw Yaw", getCurrentAngle());
		SmartDashboard.putNumber("PID Output", output);
		System.out.println("Turn Command Running.");
		scgRight.pidWrite(output);
		scgLeft.set(output);
	}
}
