package by.horevich.threadtask.state.impl;

import by.horevich.threadtask.entity.Camper;
import by.horevich.threadtask.entity.LogisticsBase;
import by.horevich.threadtask.entity.Terminal;
import by.horevich.threadtask.exception.MultiThreadException;
import by.horevich.threadtask.state.CamperState;

public class CamperLeavingState implements CamperState {

  @Override
  public void doAction(Camper camper) throws MultiThreadException {
    LogisticsBase base = LogisticsBase.getInstance();
    Terminal terminal = base.getTerminal(camper);

    if (terminal != null) {
      base.releaseTerminal(terminal, camper);
      camper.setTerminal(null);
    } else {
      throw new MultiThreadException("Camper " + camper.getCamperId() + " has no assigned terminal!");
    }
  }
}
