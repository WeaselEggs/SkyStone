package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;

// order to test programs:
// - TestFoundationOpMode, to measure servo up/down positions for left/right servo
// - TestWebbyOpMode, to measure spin/grab positions for open/close
// - TestWebbyOpMode, to measure winch direction, torque, and speed
@TeleOp
public class The_TeleOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor front_left = hardwareMap.get(DcMotor.class, "Front Left");
        DcMotor front_right = hardwareMap.get(DcMotor.class, "Front Right");
        DcMotor back_right = hardwareMap.get(DcMotor.class, "Back Right");
        DcMotor back_left = hardwareMap.get(DcMotor.class, "Back Left");
        front_left.setDirection(DcMotorSimple.Direction.REVERSE);
        back_left.setDirection(DcMotorSimple.Direction.REVERSE);

        ServoImplEx leftIntakeServo = hardwareMap.get(ServoImplEx.class, "left_intake_servo");
        leftIntakeServo.setPwmRange(new PwmControl.PwmRange(500, 2500));
        ServoImplEx rightIntakeServo = hardwareMap.get(ServoImplEx.class, "right_intake_servo");
        rightIntakeServo.setPwmRange(new PwmControl.PwmRange(500, 2500));

        DcMotor winch = hardwareMap.get(DcMotor.class, "Winch");
        ServoImplEx webby_grab = hardwareMap.get(ServoImplEx.class, "Webby Grab");
        webby_grab.setPwmRange(new PwmControl.PwmRange(	500 ,2500));
        ServoImplEx webby_spin = hardwareMap.get(ServoImplEx.class,"Webby Spin");
        webby_spin.setPwmRange(new PwmControl.PwmRange(553,2425));

        ServoImplEx foundation_left = hardwareMap.get(ServoImplEx.class, "Foundation Left");
        foundation_left.setPwmRange(new PwmControl.PwmRange(500, 2500));
        ServoImplEx foundation_right = hardwareMap.get(ServoImplEx.class, "Foundation Right");
        foundation_right.setPwmRange(new PwmControl.PwmRange(500, 2500));

        waitForStart();
        while (!isStopRequested()){
            // Mecanum Drive
            double speed = -gamepad1.left_stick_y;
            double strafe = gamepad1.left_stick_x;
            double rotate = gamepad1.right_stick_x;
            double front_left_power = (speed+strafe+rotate);
            double front_right_power = (speed-strafe-rotate);
            double back_left_power = (speed-strafe+rotate);
            double back_right_power = (speed+strafe-rotate);
            double max = Math.max(Math.max(Math.abs(front_left_power), Math.abs(front_right_power)),
                    Math.max(Math.abs(back_left_power), Math.abs(back_right_power)));
            double scale;
            if (max>1){
                scale = 1/max;
            } else{
                scale = 1;
            }
            front_left.setPower(scale*front_left_power);
            front_right.setPower(scale*front_right_power);
            back_left.setPower(scale*back_left_power);
            back_right.setPower(scale*back_right_power);

            // Intake
            if (gamepad1.a) {
                leftIntakeServo.setPosition(-1.0);
                rightIntakeServo.setPosition(1.0);
            } else if (gamepad1.b) {
                leftIntakeServo.setPosition(1.0);
                rightIntakeServo.setPosition(-1.0);
            } else {
                leftIntakeServo.setPosition(0);
                rightIntakeServo.setPosition(0);
            }

            // Webby
            winch.setPower(gamepad2.left_stick_y);
            if (gamepad2.dpad_down){
                webby_grab.setPosition(0.7); // TODO: Measure Real Number
            } else if (gamepad2.dpad_up){
                webby_grab.setPosition(0); // TODO: Measure Number
            }
            // TODO: Protect against webby spinning when it is too low
            if (gamepad2.b){
                webby_spin.setPosition(1); //TODO: measure in robot position
            } else if (gamepad2.a) {
                webby_spin.setPosition(0); //TODO: Measure Placing position
            }

            // Foundation Mover
            if (gamepad1.dpad_up){
                foundation_left.setPosition(0.5);
                foundation_right.setPosition(0); // TODO: Measure Release Position
            } else if (gamepad1.dpad_down){
                foundation_left.setPosition(0);
                foundation_right.setPosition(0.5); // TODO: Measure Grab Position
            }
        }
    }
}
