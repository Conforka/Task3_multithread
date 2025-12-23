package by.horevich.threadtask.state.impl;

import by.horevich.threadtask.entity.Camper;
import by.horevich.threadtask.entity.LogisticsBase;
import by.horevich.threadtask.entity.Terminal;
import by.horevich.threadtask.exception.MultiThreadException;
import by.horevich.threadtask.state.CamperState;

public class CamperArrivingState implements CamperState {

  @Override
  public void doAction (Camper camper) throws MultiThreadException {

    LogisticsBase base = LogisticsBase.getInstance();
    Terminal terminal = base.getTerminal(camper);
    camper.setTerminal(terminal);
    switch (camper.getOperation()) {
      case LOADING -> camper.setCamperState(new CamperLoading());
      case UNLOADING, LOADING_UNLOADING -> camper.setCamperState(new CamperUnloading());
      default -> throw new MultiThreadException("Invalid ship target");

    }
  }
}
