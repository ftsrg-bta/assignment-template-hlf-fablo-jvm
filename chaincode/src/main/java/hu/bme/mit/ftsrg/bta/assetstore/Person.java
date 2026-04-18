package hu.bme.mit.ftsrg.bta.assetstore;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Accessors(fluent = true)
@Jacksonized
public class Person {

  String id;
  String name;
}
