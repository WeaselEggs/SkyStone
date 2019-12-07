package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PwmControl;

@TeleOp(name = "Wiring", group = "Test")
public class TestWiringOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor front_left = hardwareMap.get(DcMotor.class, "Front Left");
        DcMotor front_right = hardwareMap.get(DcMotor.class, "Front Right");
        DcMotor back_right = hardwareMap.get(DcMotor.class, "Back Right");
        DcMotor back_left = hardwareMap.get(DcMotor.class, "Back Left");
        front_left.setDirection(DcMotorSimple.Direction.REVERSE);
        back_left.setDirection(DcMotorSimple.Direction.REVERSE);

        CRServoImplEx leftIntakeServo = hardwareMap.get(CRServoImplEx.class, "Left Intake");
        leftIntakeServo.setPwmRange(new PwmControl.PwmRange(500, 2500));
        CRServoImplEx rightIntakeServo = hardwareMap.get(CRServoImplEx.class, "Right Intake");
        rightIntakeServo.setPwmRange(new PwmControl.PwmRange(500, 2500));

        telemetry.addLine("LT1: Left Intake");
        telemetry.addLine("RT1: Right Intake");
        telemetry.addLine("LSY1: Front Left Drive");
        telemetry.addLine("RSY1: Front Right Drive");
        telemetry.addLine("LSY2: Back Left Drive");
        telemetry.addLine("RSY2: Back Right Drive");
        telemetry.update();
        waitForStart();
        while (!isStopRequested()) {
            leftIntakeServo.setPower(gamepad1.left_trigger);
            rightIntakeServo.setPower(gamepad1.right_trigger);
            front_left.setPower(-gamepad1.left_stick_y);
            front_right.setPower(-gamepad1.right_stick_y);
            back_left.setPower(-gamepad2.left_stick_y);
            back_right.setPower(-gamepad2.right_stick_y);
            idle();
        }
    }
}
