package com.na.mw;

import java.util.Properties;
import typedconfig.Alias;
import typedconfig.Key;
import typedconfig.Parent;
import typedconfig.TypedConfig;
import typedconfig.constraints.EnumExplicitConfig;
import typedconfig.constraints.IntExplicitConfig;
import typedconfig.constraints.IntOptionalConfig;


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


  @Key("sf.partition.pkChunkingSize") @Parent("sf.partition.mode")
  @IntExplicitConfig(ifParentValueIs = "PK_CHUNKING", from=20_000, to=250_000)
  @IntOptionalConfig(ifParentValueIs = "CLIENT", defaultValue = 250_000, from=20_000, to=250_000)
  public int pkChunkingSize;

  @Key("sf.fetchRetryLimit")@Alias("salesforce.fetchRetryLimit")
  @IntOptionalConfig(defaultValue = 5)
  public int fetchRetryLimit;

}
