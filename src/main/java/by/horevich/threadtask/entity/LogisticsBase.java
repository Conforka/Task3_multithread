package by.horevich.threadtask.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LogisticsBase {
  private static final Logger logger = LogManager.getLogger();
  private static final int AMOUNT_OF_TERMINALS = 2;
  private static final int CAPACITY = 120;
  private static LogisticsBase instance;
  private static Lock lock = new ReentrantLock();
  private AtomicInteger occupiedSlots;
  private final List<Terminal> freeTerminals = new ArrayList<>();
  private final List<Terminal> busyTerminals = new ArrayList<>();
  private final Lock terminalLock = new ReentrantLock();
  private final Condition terminalCondition = terminalLock.newCondition();

  private LogisticsBase() {
    occupiedSlots = new AtomicInteger();
    for (int i = 0; i < AMOUNT_OF_TERMINALS; i++) {
      Terminal terminal = new Terminal();
      freeTerminals.add(terminal);
    }
  }
  public AtomicInteger getOccupiedSlots() {
    return occupiedSlots;
  }

  public static LogisticsBase getInstance() {
    if (instance == null) {
      lock.lock();
      try {
        if (instance == null) {
          instance = new LogisticsBase();
        }
      } finally {
        lock.unlock();
      }
    }
    return instance;
  }

  public Terminal getTerminal(Camper camper) {
    try {
      lock.lock();
      while (freeTerminals.isEmpty()) {
        try {
          terminalCondition.await();
        } catch (InterruptedException e) {
          logger.error("Thread was interrupted while waiting", e);
          Thread.currentThread().interrupt();
        }
      }
      Terminal terminal = freeTerminals.getFirst();
      freeTerminals.remove(terminal);
      busyTerminals.add(terminal);
      logger.info(
              "Camper {} arrived | Operation: {} | Base load: {} / {} | Terminal {} is now occupied",
              camper.getCamperId(),
              camper.getOperation(),
              getOccupiedSlots(),
              CAPACITY,
              terminal.getTerminalId());
      return terminal;
    } finally {
      lock.unlock();
    }
  }

  public void releaseTerminal(Terminal terminal, Camper camper) {
    try {
      lock.lock();
      busyTerminals.remove(terminal);
      freeTerminals.add(terminal);
      logger.info(
              "Camper {} finished at Terminal {} | Base load: {} / {}",
              camper.getCamperId(),
              terminal.getTerminalId(),
              occupiedSlots.get(),
              CAPACITY);
      terminalCondition.signal();
    } finally {
      lock.unlock();
    }
  }

  public void loadCamper(Camper camper) {
    try {
      terminalLock.lock();
      while (!camper.isFull()) {
        if (occupiedSlots.get() > 0) {
          logger.info(
                  "Camper {} loaded | Camper load: {} | Base load: {} / {}",
                  camper.getCamperId(),
                  camper.getOccupiedSlots(),
                  occupiedSlots.get(),
                  CAPACITY);
          camper.addLoad();
          occupiedSlots.decrementAndGet();
        } else {
          logger.info("Base is empty, cannot load camper {}", camper.getCamperId());//?
          break;
        }
      }
    } finally {
      terminalLock.unlock();
    }
    try {
      TimeUnit.SECONDS.sleep(100);
    } catch (InterruptedException e) {
      logger.error("Thread was interrupted during loading of camper {}", camper.getCamperId(), e);
      Thread.currentThread().interrupt();
    }
  }

  public void unloadCamper(Camper camper) {
    try {
      terminalLock.lock();
      while (!camper.isEmpty()) {
        if (occupiedSlots.get() < CAPACITY) {
          logger.info(
                  "Camper {} unloaded | Camper load: {} | Base load: {} / {}",
                  camper.getCamperId(),
                  camper.getOccupiedSlots(),
                  occupiedSlots.get(),
                  CAPACITY);
          camper.removeLoad();
          occupiedSlots.incrementAndGet();
        } else {
          logger.info("Base is full, cannot unload camper {}", camper.getCamperId());//?
          break;
        }
      }
    } finally {
      terminalLock.unlock();
    }

    try {
      TimeUnit.SECONDS.sleep(100);
    } catch (InterruptedException e) {
      logger.error("Thread was interrupted during unloading of camper {}", camper.getCamperId(), e);
      Thread.currentThread().interrupt();
    }
  }

  @Override
  public String toString() {
    return "LogisticsBase{" +
            "occupiedSlots=" + occupiedSlots +
            ", busyTerminals=" + busyTerminals +
            ", freeTerminals=" + freeTerminals +
            '}';
  }
}
