package frc.robot.subsystems;

import com.ctre.phoenix6.BaseStatusSignal;
import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.NeutralOut;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.hardware.ParentDevice;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;

public class Intake extends SubsystemBase {  
  private final TalonFX intakeMotor = new TalonFX(IntakeConstants.motorID, "rio");
  private final PositionVoltage intakeMotor_voltagePosition = new PositionVoltage(0, 0, true, 0, 0, false, false, false);
  private final VelocityVoltage intakeMotor_voltageVelocity = new VelocityVoltage(0, 0, true, 0, 0, false, false, false);

  /** Creates a new intake. */
  public Intake() {
    SmartDashboard.putString("sensor debug", "init");

    TalonFXConfiguration configs = new TalonFXConfiguration();
    configs.MotorOutput.NeutralMode = IntakeConstants.kNeutralMode;
    configs.Slot0.kP = IntakeConstants.kP; // An error of 0.5 rotations results in 1.2 volts output
    configs.Slot0.kD = IntakeConstants.kD; // A change of 1 rotation per second results in 0.1 volts output
    configs.Voltage.PeakForwardVoltage = IntakeConstants.fwdVolt;
    configs.Voltage.PeakReverseVoltage = IntakeConstants.revVolt;
    configs.CurrentLimits.StatorCurrentLimitEnable = IntakeConstants.kStatorCurrentLimitEnable;
    configs.CurrentLimits.StatorCurrentLimit = IntakeConstants.kStatorCurrentLimit;
    configs.MotorOutput.Inverted =
    IntakeConstants.motorInverted
        ? InvertedValue.Clockwise_Positive
        : InvertedValue.CounterClockwise_Positive;

    /* Retry config apply up to 5 times, report if failure */
    StatusCode status = StatusCode.StatusCodeNotInitialized;
    for (int i = 0; i < 5; ++i) {
      status = intakeMotor.getConfigurator().apply(configs);
      if (status.isOK()) break;
    }
    if(!status.isOK()) {
      System.out.println("Could not apply configs, error code: " + status.toString());
    }

    setIntakeAbsolutePosition(0.0);  // we assume that the starting position of the mechanism is at the "Zero" location
    optimization_for_CAN();
  }

  /**
   * Commands a motor to particular position vice freeling rotating
   * 
   * @param i Commanded position in rotations at the motor shaft as a double
   */
  private void intakeMotorToPosition(double rotations) {
    intakeMotor.setControl(intakeMotor_voltagePosition.withPosition(rotations));
  }
  /**
   * Requests the intake location at the motor shaft
   * 
   * @return Motor shaft location in rotations as a double
   */
  public double getIntakeMotorPosition() {
    return intakeMotor.getPosition().getValueAsDouble();
  }
    /**
   * Sets the Intake's absolute position at current location
   * 
   * @param pos Intake absolute position to set at the current physical location as double in units of Rotations
   */
  public void setIntakeAbsolutePosition(double pos) {
      intakeMotor.setPosition(pos);
  }
  /**
   * This is a method for causing the intake motor to run; its intended for internal or command usage only
   * 
   */
  public void setIntakeMotorRunningForward() {
    intakeMotor.setControl(intakeMotor_voltageVelocity.withVelocity(IntakeConstants.kIntakeMotorSpeed));
  }
  /**
   * This is a method for causing the intake motor to run in reverse; its intended for internal or command usage only
   * 
   */
  public void setIntakeMotorRunningReverse() {
    intakeMotor.setControl(intakeMotor_voltageVelocity.withVelocity(-IntakeConstants.kIntakeMotorSpeed));
  }
  /**
   * This is a method for stopping the intake motor; its intended for internal or command usage only
   * 
   */
  public void setIntakeMotorStop() {
    intakeMotor.setControl(new NeutralOut());
  }
  /**
   * This is a method for retrieving the intake motor speed in rotations per second
   * 
   * @return Motor Shaft rotations per second as a double
   */
  public double getIntakeMotorSpeed() {
    return intakeMotor.getVelocity().getValueAsDouble();
  }
  @Override
  public void periodic() {
    SmartDashboard.putNumber("Intake Motor Temperature", intakeMotor.getDeviceTemp().getValueAsDouble());
    SmartDashboard.putNumber("intake rpm", intakeMotor.getVelocity().getValueAsDouble());
  }
  /**
   * This is method helps to tune the amount of CAN bus traffic to the minimum necessary to operate the motor. This prevents overloading the CAN bus.
   * 
   */
  public void optimization_for_CAN() {
    StatusSignal<Double> m_IntakeMotor_canbus1signal1 = intakeMotor.getPosition();
    StatusSignal<Double> m_IntakeTemp_canbus1signal1 = intakeMotor.getDeviceTemp();
    BaseStatusSignal.setUpdateFrequencyForAll(60, m_IntakeMotor_canbus1signal1);
    BaseStatusSignal.setUpdateFrequencyForAll(1, m_IntakeTemp_canbus1signal1);
    ParentDevice.optimizeBusUtilizationForAll(intakeMotor);
  }
}