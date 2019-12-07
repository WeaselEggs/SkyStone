package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "LED", group = "Test")
public class TestLEDOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        RevBlinkinLedDriver led = hardwareMap.get(RevBlinkinLedDriver.class, "LED");
        led.setPattern(RevBlinkinLedDriver.BlinkinPattern.RAINBOW_OCEAN_PALETTE);
        waitForStart();
        while (!isStopRequested()) {
            if (gamepad1.a) {
                led.setPattern(RevBlinkinLedDriver.BlinkinPattern.COLOR_WAVES_FOREST_PALETTE);
            } else if (gamepad1.b) {
                led.setPattern(RevBlinkinLedDriver.BlinkinPattern.COLOR_WAVES_LAVA_PALETTE);
            }
            idle();
        }
    }
}
