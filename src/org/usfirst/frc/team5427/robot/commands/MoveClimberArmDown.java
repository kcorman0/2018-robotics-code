/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5427.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team5427.robot.Robot;
import org.usfirst.frc.team5427.util.Config;
//import org.usfirst.frc.team5427.util.Log;
import org.usfirst.frc.team5427.util.NextLine;

/**
 * @author Blake This command
 */

@NextLine
public class MoveClimberArmDown extends Command {

	public MoveClimberArmDown() {
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {

	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		Robot.motorPWM_ClimberArm.set(Config.CLIMBER_MOTOR_SPEED_DOWN);
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		System.out.print("checking fin");

			return Robot.oi.getJoy().getRawButtonReleased(Config.BUTTON_CLIMBER_ARM_DOWN);
		
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
		Robot.motorPWM_ClimberArm.set(0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
		end();
	}
}