package mw;

import com.na.mw.SfConfig;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;


public class FooExtractor {
  public static void main(String[] args) {
    try {
      Properties prop = new Properties();
      String propFileName = "ingest_task.job";

      InputStream inputStream = prop.getClass().getClassLoader().getResourceAsStream(propFileName);

      if (inputStream != null) {
        prop.load(inputStream);
      } else {
        throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
      }
      SfConfig config = new SfConfig(prop);
    } catch (Exception e) {
    }
  }
}
