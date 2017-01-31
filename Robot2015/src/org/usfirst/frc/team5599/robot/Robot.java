/*

PIED PIPER

Code found and modified by Michael Rooplall to work with 2016+ XBoxController class.

Created by Edward Woo and Andrew Hurr for FRC 2015's Recycle Rush
Expect minimal documentation


www.team5599.com

*/

package org.usfirst.frc.team5599.robot;

//import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Compressor; //Pneumatics
import edu.wpi.first.wpilibj.DoubleSolenoid; //Pneumatics
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Robot extends SampleRobot {
    Talon frontLeft;
	Talon rearLeft;
	Talon frontRight;
	Talon rearRight;	
	RobotDrive myRobot;  // class that handles basic drive operations
    XBoxController gamepad; // declare gamepad as a Joystick object to get Joystick functionality, but with different buttons
	//Pneumatics info: http://www.chiefdelphi.com/forums/showthread.php?t=91938
    DoubleSolenoid pistonSqueeze;
	DoubleSolenoid pistonLift;
	Compressor compressor;
    
public Robot() {
		//change port to appropriate PWM port on roboRIO 
    	frontLeft = new Talon(0); //1
    	rearLeft = new Talon(1); //2
        frontRight = new Talon(2); //3
    	rearRight = new Talon(3); //4
    	frontLeft.set(0.70);
    	frontRight.set(0.70);
    	rearLeft.set(0.70);
    	rearRight.set(0.70);
        myRobot = new RobotDrive (frontLeft, rearLeft, frontRight, rearRight); // robot drivetrain functionality; takes in front left motor, rear left motor, front right motor, rear right motor ports
        gamepad = new XBoxController(0); // port gamepad is plugged into
		compressor = new Compressor(); //Set to null
        pistonLift = new DoubleSolenoid(0, 1);	//Set to appropriate solenoid port, channel
        pistonSqueeze = new DoubleSolenoid(2, 3);	//Set to appropriate solenoid port, channel
	}

	public void squeezeArms() { //Expanding and Retracting Arms
		compressor.start();
		compressor.setClosedLoopControl(true); //Lets PCM handle the automatic turning on and off of compressor once pressure hits 120 psi	
		if(gamepad.getLeftBumper() == true) {
			pistonSqueeze.set(DoubleSolenoid.Value.kForward); //Pushes piston whenever 'LB' button is pressed
			//Timer.delay(0.5);
			//pistonSqueeze.set(DoubleSolenoid.Value.kOff); //Disables piston
			System.out.println("'LB' button is pressed: Piston moves forward");
		}
		else if(gamepad.getLeftTriggerAbsolute() > 0) {
			pistonSqueeze.set(DoubleSolenoid.Value.kReverse); //Reverses piston  whenever 'RB' button is pressed
			//Timer.delay(0.5);
			//pistonSqueeze.set(DoubleSolenoid.Value.kOff);	//Disables piston
			System.out.println("'RB' button is pressed: Piston moves backward");
		}
		else {
			pistonSqueeze.set(DoubleSolenoid.Value.kOff); //Disables piston
		}
	}

	public void liftArms() { //Lifts and Lowers the Arms 
		compressor.start();
		compressor.setClosedLoopControl(true);	//Lets PCM handle the automatic turning on and off of compressor once pressure hits 120 psi
		if(gamepad.getRightBumper() == true) {
			pistonLift.set(DoubleSolenoid.Value.kForward); //The piston goes up whenever 'LT' button is pressed
			//Timer.delay(0.5);
			//pistonLift.set(DoubleSolenoid.Value.kOff); //Disables piston		
			System.out.println("'LT' button is pressed: Piston moves up");
		}
		
		else if(gamepad.getRightTriggerAbsolute() > 0) {
			pistonLift.set(DoubleSolenoid.Value.kReverse); //The piston goes down whenever 'RT' button is pressed
			//Timer.delay(0.5);
			//pistonSqueeze.set(DoubleSolenoid.Value.kOff);	//Disables piston
			System.out.println("'RT' button is pressed: Piston moves down");
		}
		else {
			pistonLift.set(DoubleSolenoid.Value.kOff); //Disables piston
		}
	}
	
	public void RobotBase() { //Runs before anything else
		//DriverStation();
		//Compressor();
	}
	
	public void DriverStation() { //Takes input from Driver Station itself
		DriverStation ds = DriverStation.getInstance();
		DriverStation.Alliance color;
		color = ds.getAlliance();
		String allianceColor;
		if (color == DriverStation.Alliance.Blue) {
			allianceColor = "Blue";
		}
		else if (color == DriverStation.Alliance.Red) {
			allianceColor = "Red";
		}
		else {
			allianceColor = "Invalid";
		}
		double voltage = ds.getBatteryVoltage();
		boolean brown = ds.isBrownedOut();
		SmartDashboard.putString("Alliance Color", allianceColor);
		SmartDashboard.putNumber("Voltage", voltage);
		SmartDashboard.putBoolean("Brown Out", brown);
	}
	
	public void Compressor() { //The Compressor Information
		double current = compressor.getCompressorCurrent();
		SmartDashboard.putNumber("Current", current);
		boolean tooHigh = compressor.getCompressorCurrentTooHighFault();
		SmartDashboard.putBoolean("Current is too high", tooHigh);
		boolean notConnected = compressor.getCompressorNotConnectedFault();
		SmartDashboard.putBoolean("Current is not Connected", notConnected);
	}
	
	//Autonomous
	public void autonomousInit() {} // Called when the robot enters autonomous
	//compressor.start();
	//compressor.setClosedLoopControl(true);
	public void autonomousPeriodic() {} //Called periodically during autonomous
	//The two above are used when you use CommandBase coding. 
    public void autonomous() { // This is Autonomous
    	while (isAutonomous() && isEnabled()) {
    		DriverStation();
    		Compressor();
    		double herro = Timer.getFPGATimestamp();
            //myRobot.drive(1.0, 0.0);
    		/*
    		pistonLift.set(DoubleSolenoid.Value.kForward);
    		Timer.delay(1.0);
    		pistonLift.set(DoubleSolenoid.Value.kReverse);
    		Timer.delay(1.0);
    		pistonSqueeze.set(DoubleSolenoid.Value.kReverse);
    		Timer.delay(1.0);
    		pistonSqueeze.set(DoubleSolenoid.Value.kForward);
    		Timer.delay(1.0);
    		*/
    		while (herro < 2.0)
    		{
    			myRobot.drive(-0.5, -0.00001); //.5 magnitude, 2 seconds.
    		}
    		Timer.delay(30.0);
    		
    	}
    }
    // Runs the motors with tank steering.
    
    public void operatorControl() { //This is tele-op
    	myRobot.setSafetyEnabled(false); //Safety Controls: On or Off
        while (isOperatorControl() && isEnabled()) {
        	myRobot.tankDrive(gamepad.getRightThumbstickY(), gamepad.getLeftThumbstickY(), true); //Get the Controls
            Timer.delay(0.005); // wait for a motor update time
            DriverStation();
            Compressor();
            squeezeArms();
            liftArms();
        }
    }
}