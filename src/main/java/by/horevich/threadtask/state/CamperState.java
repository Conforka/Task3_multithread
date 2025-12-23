package by.horevich.threadtask.state;

import by.horevich.threadtask.entity.Camper;
import by.horevich.threadtask.exception.MultiThreadException;

public interface CamperState {
  void doAction(Camper camper) throws MultiThreadException;
}
