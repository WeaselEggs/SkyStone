package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;

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

        waitForStart();
        while (!isStopRequested()){
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
            }
            else{
                scale = 1;
            }
            front_left.setPower(scale*front_left_power);
            front_right.setPower(scale*front_right_power);
            back_left.setPower(scale*back_left_power);
            back_right.setPower(scale*back_right_power);

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
        }
    }
}
