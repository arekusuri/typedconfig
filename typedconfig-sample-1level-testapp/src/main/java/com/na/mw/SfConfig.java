package com.na.mw;

import java.util.Properties;
import typedconfig.Alias;
import typedconfig.Key;
import typedconfig.TypedConfig;
import typedconfig.constraints.EnumMustSetup;
import typedconfig.constraints.IntMustSetup;
import typedconfig.constraints.IntOptionalSetup;


public class SfConfig extends TypedConfig {
  public SfConfig(Properties prop) {
    super(prop);
  }

  static public enum Mode {
    PK_CHUNKING,
    CLIENT
  }
  @Key("sf.partition.mode")
  @EnumMustSetup(options = {"PK_CHUNKING", "CLIENT"})
  public boolean mode;

  @Key("sf.partition.pkChunkingSize")
  @IntMustSetup(from=20_000, to=250_000, dependKey = "sf.partition.mode", equalEnum = "PK_CHUNKING")
  public int pkChunkingSize;

  @Key("sf.fetchRetryLimit")@Alias("salesforce.fetchRetryLimit")
  @IntOptionalSetup(defaultValue = 5, dependKey = "sf.partition.mode")
  public int fetchRetryLimit;



}
