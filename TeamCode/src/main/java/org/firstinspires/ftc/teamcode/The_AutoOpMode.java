package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class The_AutoOpMode extends LinearOpMode {

    private DcMotor front_left;
    private DcMotor front_right;
    private DcMotor back_right;
    private DcMotor back_left;
    private boolean wait_choice;
    private boolean right_choice;
    private boolean forward_choice;
    private boolean fancy_auto;
    @Override
    public void runOpMode() throws InterruptedException {
        front_left = hardwareMap.get(DcMotor.class, "Front Left");
        front_right = hardwareMap.get(DcMotor.class, "Front Right");
        back_right = hardwareMap.get(DcMotor.class, "Back Right");
        back_left = hardwareMap.get(DcMotor.class, "Back Left");
        front_left.setDirection(DcMotorSimple.Direction.REVERSE);
        back_left.setDirection(DcMotorSimple.Direction.REVERSE);

        ServoImplEx foundation_left = hardwareMap.get(ServoImplEx.class, "Foundation Left");
        foundation_left.setPwmRange(new PwmControl.PwmRange(500, 2500));
        ServoImplEx foundation_right = hardwareMap.get(ServoImplEx.class, "Foundation Right");
        foundation_right.setPwmRange(new PwmControl.PwmRange(500, 2500));

        foundation_left.setPosition(0.45);
        foundation_right.setPosition(0.44);

        choosewell();
        waitForStart();
        if(fancy_auto){
            drive(-.5,0,0,1500);
            foundation_left.setPosition(0.9);
            foundation_right.setPosition(0);
            waitfor(500);
            if(right_choice) {
                drive(0, 0, 1,2000);
            }
            else {
                drive(0, 0, -1,2000);
            }
        }
        else{
            if (wait_choice) {
                waitfor(20000);
            }

            if (forward_choice) {
                drive(1, 0, 0, 500);
            }

            if (right_choice) {
                drive(0, -1, 0, 1000);
            } else {
                drive(0, 1, 0, 1000);
            }
        }
    }

    private void drive(double speed, double strafe, double rotate, long milis){
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

        waitfor(milis);

        front_left.setPower(0);
        back_left.setPower(0);
        front_right.setPower(0);
        back_right.setPower(0);
    }
    private void choosewell(){
        while (!isStopRequested() && !isStarted()) {
            if (gamepad1.dpad_left) {
                right_choice = false;
            }
            if (gamepad1.dpad_right) {
                right_choice = true;
            }
            if (gamepad1.a) {
                fancy_auto = true;
            }
            if (gamepad1.b) {
                fancy_auto = false;
            }
            telemetry.addData("fancy auto(a/b)", fancy_auto ? "yes" : "no");
            telemetry.addData("robot position(dpad left/dpad right)", right_choice ? "right" : "left");
            if (!fancy_auto){
                if (gamepad1.y) {
                    wait_choice = true;
                }
                if (gamepad1.x) {
                    wait_choice = false;
                }
                if (gamepad1.dpad_up) {
                    forward_choice = true;
                }
                if (gamepad1.dpad_down) {
                    forward_choice = false;
                }
                telemetry.addData("wait(x/y)", wait_choice ? "true" : "false");
                telemetry.addData("forward(up/down dpad)", forward_choice ? "yes" : "no!!!");
            }
            telemetry.update();
        }
    }
    private void waitfor(long milis){
        ElapsedTime timer=new ElapsedTime();
        while (opModeIsActive() && timer.milliseconds()<milis){
            idle();
        }
    }
}
