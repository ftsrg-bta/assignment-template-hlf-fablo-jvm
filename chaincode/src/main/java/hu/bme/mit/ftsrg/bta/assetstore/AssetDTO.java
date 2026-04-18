package hu.bme.mit.ftsrg.bta.assetstore;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Accessors(fluent = true)
@Jacksonized
public class AssetDTO {

  String id;
  Person owner;
  String description;
  int value;

  public static AssetDTO from(Asset asset, Person owner) {
    return AssetDTO.builder()
        .id(asset.id())
        .owner(owner)
        .description(asset.description())
        .value(asset.value())
        .build();
  }
}
