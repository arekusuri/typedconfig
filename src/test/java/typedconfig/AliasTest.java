package typedconfig;

import java.util.Properties;
import org.testng.annotations.Test;

import static org.testng.Assert.*;


public class AliasTest {

  @Test
  public void testAlias() {
    Properties prop = new Properties();
    prop.put("test.port_number", "22");
    Config1 config = new Config1(prop);
    assertEquals(config.port, 22, "fail");
  }
}
class Config1 extends TypedConfig {
  public Config1(Properties prop) {
    super(prop);
  }
  @Key("test.port") @Alias("test.port_number") public int port;
}
