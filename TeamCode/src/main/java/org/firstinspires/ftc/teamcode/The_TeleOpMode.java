package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.ServoImplEx;
import com.qualcomm.robotcore.util.ElapsedTime;

// order to test programs:
// - TestFoundationOpMode, to measure servo up/down positions for left/right servo
// - TestWebbyOpMode, to measure spin/grab positions for open/close
// - TestWebbyOpMode, to measure winch direction, torque, and speed
@TeleOp
public class The_TeleOpMode extends LinearOpMode {
    private static final int MIN_HEIGHT_TICKS = 900;

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor front_left = hardwareMap.get(DcMotor.class, "Front Left");
        DcMotor front_right = hardwareMap.get(DcMotor.class, "Front Right");
        DcMotor back_right = hardwareMap.get(DcMotor.class, "Back Right");
        DcMotor back_left = hardwareMap.get(DcMotor.class, "Back Left");
        front_right.setDirection(DcMotorSimple.Direction.REVERSE);
        back_right.setDirection(DcMotorSimple.Direction.REVERSE);

        CRServoImplEx leftIntakeServo = hardwareMap.get(CRServoImplEx.class, "Left Intake");
        leftIntakeServo.setPwmRange(new PwmControl.PwmRange(500, 2500));
        CRServoImplEx rightIntakeServo = hardwareMap.get(CRServoImplEx.class, "Right Intake");
        rightIntakeServo.setPwmRange(new PwmControl.PwmRange(500, 2500));

        DcMotor winch = hardwareMap.get(DcMotor.class, "Winch");
        ServoImplEx webby_grab = hardwareMap.get(ServoImplEx.class, "Webby Grab");
        webby_grab.setPwmRange(new PwmControl.PwmRange(	500 ,2500));
        ServoImplEx webby_spin = hardwareMap.get(ServoImplEx.class,"Webby Spin");
        webby_spin.setPwmRange(new PwmControl.PwmRange(553,2425));

        ServoImplEx crap = hardwareMap.get(ServoImplEx.class,"Crap");
        crap.setPwmRange(new PwmControl.PwmRange(553,2425));

        ServoImplEx foundation_left = hardwareMap.get(ServoImplEx.class, "Foundation Left");
        foundation_left.setPwmRange(new PwmControl.PwmRange(500, 2500));
        ServoImplEx foundation_right = hardwareMap.get(ServoImplEx.class, "Foundation Right");
        foundation_right.setPwmRange(new PwmControl.PwmRange(500, 2500));

        RevBlinkinLedDriver led = hardwareMap.get(RevBlinkinLedDriver.class, "LED");

        led.setPattern(RevBlinkinLedDriver.BlinkinPattern.SINELON_PARTY_PALETTE);

        webby_grab.setPosition(0.75);
        webby_spin.setPosition(0);
        crap.setPosition(0.15);
        foundation_left.setPosition(0.45);
        foundation_right.setPosition(0.44);

        waitForStart();
        winch.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        winch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        ElapsedTime winch_timer = new ElapsedTime();
        boolean winch_stable = true;
        boolean webby_open = true;
        boolean blue_alliance = false;
        boolean red_alliance = false;
        boolean foundation_down = false;
        ElapsedTime webby_timer = new ElapsedTime(1000);
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
            if (gamepad1.left_bumper) {
                scale /= 3;
            }
            front_left.setPower(scale*front_left_power);
            front_right.setPower(scale*front_right_power);
            back_left.setPower(scale*back_left_power);
            back_right.setPower(scale*back_right_power);

            // Intake
            boolean intaking = false;
            boolean extaking = false;
            if (gamepad1.a || gamepad1.right_trigger > 0.2) {
                leftIntakeServo.setPower(1.0);
                rightIntakeServo.setPower(-1.0);
                intaking = true;
            } else if (gamepad1.b || gamepad1.left_trigger > 0.2) {
                leftIntakeServo.setPower(-1.0);
                rightIntakeServo.setPower(1.0);
                extaking = true;
            } else {
                leftIntakeServo.setPower(0);
                rightIntakeServo.setPower(0);
            }

            // Webby
            if (gamepad2.dpad_down){
                webby_grab.setPosition(1);
                webby_open = false;
                webby_timer.reset();
            } else if (gamepad2.dpad_up){
                webby_grab.setPosition(0.75);
                webby_open = true;
                webby_timer.reset();
            }
            // Protect against webby spinning when it is too low
            // TODO: if (winch.getCurrentPosition() >= MIN_HEIGHT_TICKS) {
            if (gamepad2.b) {
                webby_spin.setPosition(0);
            } else if (gamepad2.a) {
                webby_spin.setPosition(1);
            }
            //}

            // Winch
            boolean winch_at_bottom = false;
            double winch_power = -gamepad2.left_stick_y / 1.5;
            winch_power = Math.signum(winch_power) * Math.pow(Math.abs(winch_power) , 1.5);
            if (gamepad2.right_bumper) {
                winch.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                winch_stable = true;
            } else if (winch_power < 0 && winch.getCurrentPosition() < 0) {
                winch.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
                winch_power = 0;
                winch_at_bottom = true;
            }
            if (gamepad2.left_bumper) {
                winch_power *= 0.3;
            }
            if (Math.abs(winch_power) < 0.05) {
                if (!winch_stable) {
                    if (winch_timer.milliseconds() < 500) {
                        winch.setPower(0.08);
                    } else {
                        winch.setTargetPosition(winch.getCurrentPosition());
                        winch.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                        winch.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                        winch.setPower(0);
                        winch_stable = true;
                    }
                }
            } else {
                if (winch_stable) {
                    winch.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    winch_stable = false;
                }
                winch_timer.reset();
                winch.setPower(winch_power);
            }

            // Crapper
            if (gamepad2.y){ //open
                crap.setPosition(1);
            } else if (gamepad2.x){ //closed
                crap.setPosition(0.15);
            }

            // Foundation Mover
            if (gamepad1.dpad_up){
                foundation_left.setPosition(0.45);
                foundation_right.setPosition(0.44);
                foundation_down = false;
            } else if (gamepad1.dpad_down) {
                foundation_left.setPosition(0.9);
                foundation_right.setPosition(0);
                foundation_down = true;
            }

            if (gamepad1.start || gamepad2.start) {
                blue_alliance = true;
                red_alliance = false;
            } else if (gamepad1.back || gamepad2.back) {
                blue_alliance = false;
                red_alliance = true;
            }

            if (winch_at_bottom) {
                led.setPattern(RevBlinkinLedDriver.BlinkinPattern.RAINBOW_LAVA_PALETTE);
            } else if (foundation_down) {
                led.setPattern(RevBlinkinLedDriver.BlinkinPattern.LARSON_SCANNER_GRAY);
            } else if (webby_timer.milliseconds() < 500) {
                if (webby_open) {
                    led.setPattern(RevBlinkinLedDriver.BlinkinPattern.SHOT_BLUE);
                } else {
                    led.setPattern(RevBlinkinLedDriver.BlinkinPattern.SHOT_RED);
                }
            } else if (!webby_open && intaking) {
                led.setPattern(RevBlinkinLedDriver.BlinkinPattern.CONFETTI);
            } else if (webby_open && intaking) {
                led.setPattern(RevBlinkinLedDriver.BlinkinPattern.RAINBOW_WITH_GLITTER);
            } else if (extaking) {
                led.setPattern(RevBlinkinLedDriver.BlinkinPattern.RAINBOW_OCEAN_PALETTE);
            } else {
                if (blue_alliance) {
                    led.setPattern(RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_BLUE);
                } else if (red_alliance) {
                    led.setPattern(RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_RED);
                } else {
                    led.setPattern(RevBlinkinLedDriver.BlinkinPattern.HEARTBEAT_WHITE);
                }
            }

            telemetry.addData("Winch_Position", String.format("%d", winch.getCurrentPosition()));
            telemetry.update();
        }
    }
}
