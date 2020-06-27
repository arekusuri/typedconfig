package com.na.mw;

import java.util.Properties;
import typedconfig.Alias;
import typedconfig.Key;
import typedconfig.TypedConfig;
import typedconfig.constraints.EnumRequired;
import typedconfig.constraints.IntRequired;
import typedconfig.constraints.IntOptional;


public class SfConfig extends TypedConfig {
  public SfConfig(Properties prop) {
    super(prop);
  }

  static public enum Mode {
    PK_CHUNKING,
    CLIENT
  }
  @Key("sf.partition.mode")
  @EnumRequired(options = {"PK_CHUNKING", "CLIENT"})
  public Mode mode;


  @Key("sf.partition.pkChunkingSize")
  @IntRequired(from=20_000, to=250_000, dependKey = "sf.partition.mode", equalEnum = "PK_CHUNKING")
  @IntOptional(from=20_000, to=250_000, dependKey = "sf.partition.mode", equalEnum = "CLIENT")
  public int pkChunkingSize;

  @Key("sf.fetchRetryLimit")@Alias("salesforce.fetchRetryLimit")
  @IntOptional(defaultValue = 5, dependKey = "sf.partition.mode")
  public int fetchRetryLimit;



}
