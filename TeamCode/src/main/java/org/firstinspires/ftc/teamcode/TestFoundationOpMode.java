package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;

@TeleOp(name = "Foundation", group = "Test")
public class TestFoundationOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        ServoImplEx foundation_left = hardwareMap.get(ServoImplEx.class, "Foundation Left");
        foundation_left.setPwmRange(new PwmControl.PwmRange(500, 2500));
        ServoImplEx foundation_right = hardwareMap.get(ServoImplEx.class, "Foundation Right");
        foundation_right.setPwmRange(new PwmControl.PwmRange(500, 2500));
        waitForStart();
        while(!isStopRequested()){
            foundation_left.setPosition(gamepad1.left_trigger);
            foundation_right.setPosition(gamepad1.right_trigger);
            telemetry.addData("LT_Left", String.format("%.2f",foundation_left.getPosition()));
            telemetry.addData("RT_Right", String.format("%.2f",foundation_right.getPosition()));
            telemetry.update();
        }
    }
}
