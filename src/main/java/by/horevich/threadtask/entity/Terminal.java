package by.horevich.threadtask.entity;

import java.util.UUID;

public class Terminal {
  private final String terminalId;

  public Terminal() {
    this.terminalId = UUID.randomUUID().toString().replace("-", "");
  }

  public String getTerminalId() {
    return terminalId;
  }
}
