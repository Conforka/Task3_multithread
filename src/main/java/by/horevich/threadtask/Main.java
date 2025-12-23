package by.horevich.threadtask;

import by.horevich.threadtask.entity.Camper;
import by.horevich.threadtask.entity.CamperOperation;
import by.horevich.threadtask.entity.LogisticsBase;
import by.horevich.threadtask.reader.BaseTextReader;
import by.horevich.threadtask.reader.BaseTextReaderImpl;
import by.horevich.threadtask.state.impl.CamperArrivingState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Main {
  private static final Logger logger = LogManager.getLogger();
  
  public static void main(String[] args) {
    BaseTextReader reader = new BaseTextReaderImpl();
    List<String> lines = reader.read("textfile/file.txt");
    List<Camper> campers = new ArrayList<>();

    for (String line : lines) {
      if (line.isBlank()) continue;
      String[] parts = line.split(";");
      if (parts.length < 4) continue;

      String camperId = parts[0];
      int capacity = Integer.parseInt(parts[1]);
      int occupiedSlots = Integer.parseInt(parts[2]);
      CamperOperation operation = CamperOperation.valueOf(parts[3]);

      Camper camper = new Camper(camperId, capacity, occupiedSlots, operation);
      camper.setCamperState(new CamperArrivingState());
      campers.add(camper);
    }

    LogisticsBase base = LogisticsBase.getInstance();
    base.getOccupiedSlots().set(10);


    List<Thread> threads = new ArrayList<>();
    for (Camper camper : campers) {
      Thread t = new Thread(camper);
      threads.add(t);
      t.start();
    }

    for (Thread t : threads) {
      try {
        t.join();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        logger.error("Interrupted while waiting for thread", e);
      }
    }

    logger.info("Final logistics base state: {}", base);
    logger.info("Simulation complete.");
  }
}
