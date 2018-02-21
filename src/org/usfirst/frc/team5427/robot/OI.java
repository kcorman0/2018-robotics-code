/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package org.usfirst.frc.team5427.robot;

import org.usfirst.frc.team5427.robot.commands.IntakeActivate;
import org.usfirst.frc.team5427.robot.commands.IntakeSolenoidSwitch;
import org.usfirst.frc.team5427.robot.commands.MoveElevator;
import org.usfirst.frc.team5427.util.Config;
import org.usfirst.frc.team5427.util.SameLine;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
/**
 * This file makes the joystick This file will use the same line method
 * 
 * @author Akshat
 */
@SameLine
public class OI {
	//// CREATING BUTTONS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	// Button button = new JoystickButton(stick, buttonNumber);
	// There are a few additional built in buttons you can use. Additionally,
	// by subclassing Button you can create custom triggers and bind those to
	// commands the same as any other Button.
	//// TRIGGERING COMMANDS WITH BUTTONS
	// Once you have a button, it's trivial to bind it to a button in one of
	// three ways:
	// Start the command when the button is pressed and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenPressed(new ExampleCommand());
	// Run the command while the button is being held down and interrupt it once
	// the button is released.
	// button.whileHeld(new ExampleCommand());
	// Start the command when the button is released and let it run the command
	// until it is finished as determined by it's isFinished method.
	// button.whenReleased(new ExampleCommand());
	// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
	public Joystick joy1;
	Button motorIntake;
	Button solenoidIntake;
	Button elevatorForward;
	Button elevatorReverse;
	SendableChooser<Integer> autoColorChooser = new SendableChooser<Integer>();
	SendableChooser<Integer> autoPositionChooser = new SendableChooser<Integer>();
	SendableChooser<Integer> autoCubeChooser = new SendableChooser<Integer>();
	
	public OI() {
		joy1 = new Joystick(0);
		// TODO fix this
		motorIntake = new JoystickButton(joy1, Config.BUTTON_MOTOR_INTAKE);
		solenoidIntake = new JoystickButton(joy1, Config.BUTTON_SOLENOD_INTAKE);
		elevatorForward = new JoystickButton(joy1, Config.BUTTON_ELEVATOR_FORWARD);
		elevatorReverse = new JoystickButton(joy1, Config.BUTTON_ELEVATOR_REVERSE);
		motorIntake.whileHeld(new IntakeActivate());
		solenoidIntake.whenPressed(new IntakeSolenoidSwitch());
		elevatorForward.whileHeld(new MoveElevator(1));
		elevatorReverse.whileHeld(new MoveElevator(2));
		
		
		
		autoColorChooser.addDefault(" ", Config.AUTO_NONE);
		autoColorChooser.addObject("Red", Config.RED);
		autoColorChooser.addObject("Blue", Config.BLUE);
		autoPositionChooser.addObject("Right", Config.RIGHT);
		autoPositionChooser.addObject("Center", Config.CENTER);
		autoPositionChooser.addObject("Left", Config.LEFT);
		autoCubeChooser.addObject("Switch", Config.SWITCH);
		autoCubeChooser.addObject("Scale", Config.SCALE);	
		
		SmartDashboard.putData("Field Position",autoPositionChooser);
		SmartDashboard.putData("Switch or Scale",autoCubeChooser);
		
	}

	public Joystick getJoy() {
		return joy1;
	}
}
