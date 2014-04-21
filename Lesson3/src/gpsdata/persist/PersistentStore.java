package gpsdata.persist;

import mooc.data.gps.Position;
import mooc.data.gps.Velocity;
import java.io.IOException;

public interface PersistentStore extends AutoCloseable {

  public int saveData(Position position, Velocity velocity) throws IOException;
  
  public String readData(int id);

  public int getRecordCount();
}