package by.horevich.threadtask.state.impl;

import by.horevich.threadtask.entity.Camper;
import by.horevich.threadtask.entity.LogisticsBase;
import by.horevich.threadtask.entity.Terminal;
import by.horevich.threadtask.exception.MultiThreadException;
import by.horevich.threadtask.state.CamperState;

public class CamperLoadingState implements CamperState {

  @Override
  public void doAction(Camper camper) throws MultiThreadException {
    LogisticsBase base = LogisticsBase.getInstance();

    //Terminal terminal = camper.getTerminal();
    //if (terminal != null) {
      base.loadCamper(camper);
    //}
    camper.setCamperState(new CamperLeavingState());
  }
}
