// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.command_examples.exampleCommand;
import frc.robot.subsystems.Intake;

public class RobotContainer {
  private final CommandXboxController driverJoystick = new CommandXboxController(0);
  public static final Intake m_intake = new Intake();

  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings() {
    driverJoystick.b().onTrue(
      new SequentialCommandGroup(
        Commands.print("Intake Example Command 1 was started"),
        new exampleCommand(m_intake),
        Commands.print("Intake Example Command 1 was completed")
      )
    );
  }

  public Command getAutonomousCommand() {
    return Commands.print("No autonomous command configured");
  }
}