package by.horevich.threadtask.reader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class BaseTextReaderImpl implements BaseTextReader {

  private static final Logger LOGGER = LogManager.getLogger();

  @Override
  public List<String> read(String path) {
    try {
      List<String> lines = Files.readAllLines(Paths.get(path));
      LOGGER.info("Read {} lines from file {}", lines.size(), path);
      return lines;
    } catch (Exception e) {
      LOGGER.error("Error reading file {}", path, e);
      return List.of();
    }
  }
}
