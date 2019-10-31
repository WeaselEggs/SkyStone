package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;

@TeleOp
public class TestWebbyOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        ServoImplEx webby_grab = hardwareMap.get(ServoImplEx.class, "Webby Grab");
        webby_grab.setPwmRange(new PwmControl.PwmRange(	500 ,2500));
        ServoImplEx webby_spin = hardwareMap.get(ServoImplEx.class,"Webby Spin");
        webby_spin.setPwmRange(new PwmControl.PwmRange(553,2425));
        DcMotor winch = hardwareMap.get(DcMotor.class, "Winch");
        winch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();
        while(!isStopRequested()){
            webby_grab.setPosition(gamepad1.left_trigger);
            webby_spin.setPosition(gamepad1.right_trigger);
            winch.setPower(gamepad1.left_stick_y/2);
            telemetry.addData("LT_Grab", String.format("%.2f",webby_grab.getPosition()));
            telemetry.addData("RT_Spin", String.format("%.2f",webby_spin.getPosition()));
            telemetry.addData("LSY_Winch", String.format("%.2f", winch.getPower()));
            telemetry.addData("Winch_Position", String.format("%d", winch.getCurrentPosition()));
            telemetry.update();
        }
    }
}
