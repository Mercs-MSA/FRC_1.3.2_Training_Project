package frc.robot.command_examples;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.subsystems.Intake;

public class exampleCommand extends Command {

  private final Intake m_intake;

  public exampleCommand(Intake intake) {
    addRequirements();
    m_intake = intake;
  }

  @Override
  public void initialize() {
    SmartDashboard.putBoolean("Intake Example Command 1 Complete", false);
    SmartDashboard.putBoolean("Intake Example Command 1 Interrupted", false);
    m_intake.setIntakeMotorRunningForward();
  }

  @Override
  public void execute() {
  }

  @Override
  public void end(boolean interrupted) {
    if (interrupted) {
      SmartDashboard.putBoolean("Intake Example Command 1 Interrupted", true);
      Commands.print("Intake Example Command 1 was interrupted");
    }
    SmartDashboard.putBoolean("Intake Example Command 1 Complete", true);

    m_intake.setIntakeMotorStop();
  }

  @Override
  public boolean isFinished() {
    if (m_intake.getIntakeMotorSpeed() > Constants.IntakeConstants.velocityCommandLimit) {
      return true;
    }
    else {
      return false;
    }
  }
}