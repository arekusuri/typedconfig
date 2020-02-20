package typedconfig;

import java.util.Properties;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import typedconfig.completime.EnumOptions;
import typedconfig.completime.IntRange;
import typedconfig.completime.LongRange;
import typedconfig.completime.StringRegex;

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
    prop.put("test.port", "22");
    prop.put("test.url", "local");
    Config config = new Config(prop);
    Properties ret = config.toProp();
    assertTrue(ret.get("test.port").equals(prop.get("test.port")));
    assertTrue(ret.get("test.url").equals(prop.get("test.url")));
  }
  @Test
  public void testToPropEnum() {
    Properties prop = new Properties();
    prop.put("test.dbType", "mysql");
    Config config = new Config(prop);
    Properties ret = config.toProp();
    assertTrue(ret.get("test.dbType").equals(prop.get("test.dbType")));
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

  @Test
  public void testIntRange() {
    Properties prop = new Properties();
    prop.put("test.port_number", "11111");
    Config config = new Config(prop);
    assertEquals(config.port, 999, "fail");
  }
  @Test
  public void testLongRange() {
    Properties prop = new Properties();
    prop.put("test.long", "11111");
    Config config = new Config(prop);
    assertEquals(config.longNumber, 2L, "fail");
  }

  @Test
  public void testStringRegex() {
    Properties prop = new Properties();
    prop.put("test.stringRegex", "jdbc:mysql://localhost:DDDD/tablename");
    Config config = new Config(prop);
    assertEquals(config.stringRegex, null, "fail");
  }
  @Test
  public void testStringRegexHappy() {
    Properties prop = new Properties();
    prop.put("test.stringRegex", "jdbc:mysql://localhost:999/tablename");
    Config config = new Config(prop);
    assertEquals(config.stringRegex, "jdbc:mysql://localhost:999/tablename", "fail");
  }
  @Test
  public void testEnumOptions() {
    Properties prop = new Properties();
    prop.put("test.enumOps", "mysql");
    Config config = new Config(prop);
    assertEquals(config.enumOps, DbType.mysql, "fail");
  }
  @Test
  public void testEnumOptionsNull() {
    Properties prop = new Properties();
    prop.put("test.enumOps", "postgresql");
    Config config = new Config(prop);
    assertEquals(config.enumOps, null, "fail");
  }

}

enum DbType {
  mysql, sqlserver, postgresql;
}


class Config extends TypedConfig {
  public Config(Properties prop) {
    super(prop);
  }
  @Key("test.dbType")
  public DbType dbType;
  @Key("test.enumOps")@EnumOptions({"sqlserver", "mysql"})
  public DbType enumOps;
  @Key("test.url")
  public String url;
  @Key("test.port")@Alias("test.port_number")@Default("999")@IntRange({1, 1000})
  public int port;
  @Key("test.long")@Default("2")@LongRange({1L, 3L})
  public long longNumber;
  @Key("test.stringRegex")@StringRegex("jdbc:mysql:.*:[1-9][0-9]*/tablename")
  public String stringRegex;

}