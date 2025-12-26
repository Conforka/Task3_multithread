package by.horevich.threadtask.state.impl;

import by.horevich.threadtask.entity.Camper;
import by.horevich.threadtask.entity.CamperOperation;
import by.horevich.threadtask.entity.LogisticsBase;
import by.horevich.threadtask.entity.Terminal;
import by.horevich.threadtask.exception.MultiThreadException;
import by.horevich.threadtask.state.CamperState;

public class CamperUnloadingState implements CamperState {

  @Override
  public void doAction(Camper camper) throws MultiThreadException {
    LogisticsBase base = LogisticsBase.getInstance();


    //Terminal terminal = camper.getTerminal();
    //if (terminal != null){
      base.unloadCamper(camper);
    //}
    if (camper.getOperation() == CamperOperation.LOADING_UNLOADING) {
      camper.setCamperState(new CamperLoadingState());
    } else {
      camper.setCamperState(new CamperLeavingState());
    }
  }
}

