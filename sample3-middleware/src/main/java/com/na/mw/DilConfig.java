package com.na.mw;

import java.util.Properties;
import typedconfig.Parent;
import typedconfig.Key;
import typedconfig.TypedConfig;
import typedconfig.constraints.EnumExplicitConfig;
import typedconfig.constraints.StringExplicitConfig;


public class DilConfig extends TypedConfig {
  public DilConfig(Properties prop) {
    super(prop);
  }

  static public enum Protocol {
    HTTP,
    SFTP
  }

  @Key("ms.protocol")
  @EnumExplicitConfig(options = {"HTTP", "SFTP"})
  public Protocol protocol;

  @Key("gobblin.flow.sourceIdentifier")
  @Parent("ms.protocol") @StringExplicitConfig(ifParentValueIs = "HTTP")
  public String sourceIdentifier;

  @Key("gobblin.flow.input.dataset.descriptor.path")
  @Parent("ms.protocol") @StringExplicitConfig(ifParentValueIs = "HTTP")
  public String descriptorPth;

  @Key("state.store.enabled")
  public boolean stateStoreEnable;

  @Key("extract.table.type")
  @Parent("ms.protocol") @StringExplicitConfig(ifParentValueIs = "HTTP")
  public String tableType;

  @Key("ms.extractor.class")
  @Parent("ms.protocol") @StringExplicitConfig(ifParentValueIs = "HTTP")
  public String extractorClass;

  @Key("ms.enable.schema.based.filtering")
  public boolean enableFiltering;

  @Key("ms.work.unit.partition")
  @Parent("ms.protocol") @StringExplicitConfig(ifParentValueIs = "HTTP")
  public String unitPartition;

  @Key("job.group")
  @Parent("ms.protocol") @StringExplicitConfig(ifParentValueIs = "HTTP")
  public String jobGroup;

  @Key("launcher.type")
  @Parent("ms.protocol") @StringExplicitConfig(ifParentValueIs = "HTTP")
  public String launcherType;

}
