package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;

@TeleOp
public class TestServoIntakeOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        ServoImplEx leftIntakeServo = hardwareMap.get(ServoImplEx.class, "left_intake_servo");
        leftIntakeServo.setPwmRange(new PwmControl.PwmRange(500, 2500));
        ServoImplEx rightIntakeServo = hardwareMap.get(ServoImplEx.class, "right_intake_servo");
        rightIntakeServo.setPwmRange(new PwmControl.PwmRange(500, 2500));

        waitForStart();
        while (!isStopRequested()) {
            double oppositeFactor = gamepad1.left_bumper ? -1.0 : 1.0;
            if (gamepad1.a) {
                leftIntakeServo.setPosition(oppositeFactor * 1.0);
                rightIntakeServo.setPosition(1.0);
            } else if (gamepad1.b) {
                leftIntakeServo.setPosition(oppositeFactor * -1.0);
                rightIntakeServo.setPosition(-1.0);
            } else {
                leftIntakeServo.setPosition(0);
                rightIntakeServo.setPosition(0);
            }
            idle();
        }
    }
}
