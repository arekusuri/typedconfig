package com.na.mw;

import java.util.Properties;
import typedconfig.Alias;
import typedconfig.ConfigClass;
import typedconfig.Parent;
import typedconfig.Key;
import typedconfig.TypedConfig;
import typedconfig.constraints.EnumExplicitConfig;
import typedconfig.constraints.IntExplicitConfig;
import typedconfig.constraints.IntOptionalConfig;


@ConfigClass
public class SfConfig extends TypedConfig {
  public SfConfig(Properties prop) {
    super(prop);
  }

  static public enum Mode {
    PK_CHUNKING,
    CLIENT
  }

  @Key("sf.partition.mode")
  @EnumExplicitConfig(options = {"PK_CHUNKING", "CLIENT"})
  public Mode mode;

  @Key("sf.partition.pkChunkingSize")@Alias("abc")@Parent("sf.partition.mode")
  @IntExplicitConfig(from=20_000, to=250_000, ifParentValueIs = "PK_CHUNKING")
  @IntExplicitConfig(ifParentValueIs = "CLIENT")
  public int pkChunkingSize;

  @Key("sf.fetchRetryLimit")@Alias("salesforce.fetchRetryLimit")@Parent("sf.partition.mode")
  @IntOptionalConfig(from=1, to=10, defaultValue = 5)
  public int fetchRetryLimit;

}
