package frc.robot.command_examples;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.subsystems.Intake;
import edu.wpi.first.wpilibj.RobotController;

public class exampleCommand extends Command {

  private final Intake m_intake;
  private final double execution_timer;

   /**
  * Commands the intake forward for a set amount of time then stops it
  * 
  * @param intake passes control of the intake subsystem to the command temporarily
  */
  public exampleCommand(Intake intake) {
    addRequirements();
    m_intake = intake;
    execution_timer = RobotController.getFPGATime();
  }

  @Override
  public void initialize() {
    SmartDashboard.putBoolean("Intake Command Complete", false);
    SmartDashboard.putBoolean("Intake Command Interrupted", false);
    m_intake.setIntakeMotorRunningForward();
  }

  @Override
  public void execute() {
  }

  @Override
  public void end(boolean interrupted) {
    if (interrupted) {
      SmartDashboard.putBoolean("Intake Command Interrupted", true);
      Commands.print("Intake Example Command 1 was interrupted");
    }
    SmartDashboard.putBoolean("Intake Command Complete", true);
    m_intake.setIntakeMotorStop();
  }

  @Override
  public boolean isFinished() {
    if ((RobotController.getFPGATime() - execution_timer) > Constants.IntakeConstants.intakeCommandDuration) {
      return true;
    }
    else {
      return false;
    }
  }
}