package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;

@TeleOp
public class WebbyOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        ServoImplEx webby_grab = hardwareMap.get(ServoImplEx.class, "Webby Grab");
        webby_grab.setPwmRange(new PwmControl.PwmRange(	500 ,2500));
        ServoImplEx webby_spin = hardwareMap.get(ServoImplEx.class,"Webby Spin";
        webby_spin.setPwmRange(new PwmControl.PwmRange(553,2425));
        waitForStart();
        while(!isStopRequested()){
            if(gamepad1.a) {
                webby_grab.setPosition(1);
            }else if(gamepad1.b){
                webby_grab.setPosition(gamepad1.left_trigger);
            }else{
                webby_grab.setPosition(.8);
            }
            idle();

        }
    }
}
