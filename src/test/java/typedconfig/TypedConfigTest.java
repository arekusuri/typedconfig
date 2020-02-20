package typedconfig;

import java.util.Properties;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import typedconfig.completime.IntRange;
import static org.testng.Assert.*;

public class TypedConfigTest {
  @BeforeClass
  public void setup() {
  }
  @AfterClass
  public void tearDown() {
  }
  @Test
  public void testToProp() {
    Properties prop = new Properties();
    prop.put("test.port", 22);
    prop.put("test.url", "local");
    Config config = new Config(prop);
    Properties ret = config.toProp();
    assertTrue(ret.equals(prop));
  }
  @Test
  public void testString() {
    Properties prop = new Properties();
    prop.put("test.url", "localhost");
    Config config = new Config(prop);
    assertEquals(config.url, "localhost", "fail");
  }
  @Test
  public void testInt() {
    Properties prop = new Properties();
    prop.put("test.port", 22);
    Config config = new Config(prop);
    assertEquals(config.port, 22, "fail");
  }

  @Test
  public void testInt2() {
    Properties prop = new Properties();
    prop.put("test.port", "22");
    Config config = new Config(prop);
    assertEquals(config.port, 22, "fail");
  }
  @Test
  public void testEnum() {
    Properties prop = new Properties();
    prop.put("test.dbType", "mysql");
    Config config = new Config(prop);
    assertEquals(config.dbType, DbType.mysql, "fail");
  }
  @Test
  public void testDefault() {
    Properties prop = new Properties();
    Config config = new Config(prop);
    assertEquals(config.port, 999, "fail");
  }

  @Test
  public void testAlias() {
    Properties prop = new Properties();
    prop.put("test.port_number", "88");
    Config config = new Config(prop);
    assertEquals(config.port, 88, "fail");
  }

}

enum DbType {
  mysql, sqlserver
}

class Config extends TypedConfig {
  public Config(Properties prop) {
    super(prop);
  }
  @Key("test.dbType")
  public DbType dbType;
  @Key("test.url")
  public String url;
  @Key("test.port")@Alias("test.port_number")@Default("999")@IntRange({1, 1000})
  public int port;

}