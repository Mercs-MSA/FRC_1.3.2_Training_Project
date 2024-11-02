package frc.robot;
import com.ctre.phoenix6.signals.NeutralModeValue;

public final class Constants {
    public static class IntakeConstants{
        public static int motorID = 21;
        public static boolean motorInverted = true;

        /* Leader Motor PID Values */
        public static final double kP = 1.5;
        public static final double kI = 0.0;
        public static final double kD = 0.0;
        public static final double kF = 0.0;

        public static double fwdVolt = 14;
        public static double revVolt = -14;
        public static int rotorToSensorRatio = 1;
        public static int sensorToMechanismRatio = 1;
        public static double kIntakeMotorSpeed = 0.25;
        public static boolean kStatorCurrentLimitEnable = true;
        public static double kStatorCurrentLimit = 40;
        public static NeutralModeValue kNeutralMode = NeutralModeValue.Brake;

        public static double intakeCommandDuration = 4000;   // this is in microseconds; so 4 seconds
    }
}