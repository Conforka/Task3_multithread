package by.horevich.threadtask.entity;

import by.horevich.threadtask.exception.MultiThreadException;
import by.horevich.threadtask.state.CamperState;
import by.horevich.threadtask.state.impl.CamperLeavingState;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.StringJoiner;

public class Camper implements Runnable {
  private static final Logger logger = LogManager.getLogger();

  private final String camperId;
  private final int capacity;
  private int occupiedSlots;
  private CamperOperation operation;
  private CamperState camperState;
  private Terminal terminal;

  public Camper(String camperId,
                int capacity,
                int occupiedSlots,
                CamperOperation operation) {

    this.camperId = camperId;
    this.capacity = capacity;
    this.occupiedSlots = occupiedSlots;
    this.operation = operation;
  }

  public String getCamperId() {
    return camperId;
  }

  public CamperOperation getOperation() {
    return operation;
  }



  public void setCamperState(CamperState state) {
    this.camperState = state;
  }

  public void setTerminal(Terminal terminal) {
    this.terminal = terminal;
  }

  public Terminal getTerminal() {
    return terminal;
  }


  public int getOccupiedSlots(){
    return occupiedSlots;
  }

  public void addLoad(){
    occupiedSlots++;
  }

  public void removeLoad(){
    occupiedSlots--;
  }

  public boolean isFull() {
    return occupiedSlots == capacity;
  }

  public boolean isEmpty() {
    return occupiedSlots == 0;
  }

  @Override
  public void run() {
    boolean finished = false;

    while (!finished) {
      try {
        camperState.doAction(this);

        if (camperState instanceof CamperLeavingState) {
          finished = true;

        }

      } catch (MultiThreadException e) {
        logger.error("Camper {} cannot continue work", camperId, e);
        break;
      }
    }
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", Camper.class.getSimpleName() + "[", "]")
            .add("camperId='" + camperId + "'")
            .add("capacity=" + capacity)
            .add("occupiedSlots=" + occupiedSlots)
            .add("operation=" + operation)
            .add("state=" + camperState)
            .toString();
  }


}
