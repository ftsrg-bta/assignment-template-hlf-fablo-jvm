package hu.bme.mit.ftsrg.bta.assetstore;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ledger.CompositeKey;

import static org.hyperledger.fabric.contract.annotation.Transaction.TYPE.EVALUATE;
import static org.hyperledger.fabric.contract.annotation.Transaction.TYPE.SUBMIT;

@Contract(name = "AssetStore")
@Default
public class AssetStore implements ContractInterface {

  private static final ObjectMapper mapper = new ObjectMapper();

  @Transaction(intent = SUBMIT)
  public String createPerson(Context ctx, String id, String name) {
    assertNotExists(ctx, Person.class, id);

    var person = Person.builder().id(id).name(name).build();
    ctx.getStub()
        .putState(new CompositeKey(Person.class.getName(), id).toString(), serialize(person));
    return "ok";
  }

  @Transaction(intent = SUBMIT)
  public String createAsset(
      Context ctx, String id, String ownerId, String description, int assetValue) {
    assertNotExists(ctx, Asset.class, id);
    assertExists(ctx, Person.class, ownerId);

    var asset =
        Asset.builder().id(id).ownerId(ownerId).description(description).value(assetValue).build();
    ctx.getStub()
        .putState(new CompositeKey(Asset.class.getName(), id).toString(), serialize(asset));
    return "ok";
  }

  @Transaction(intent = EVALUATE)
  public String getAsset(Context ctx, String id) {
    var asset = readEntity(ctx, Asset.class, id);
    var owner = readEntity(ctx, Person.class, asset.ownerId());
    var dto = AssetDTO.from(asset, owner);

    return "ok: " + new String(serialize(dto), StandardCharsets.UTF_8);
  }

  private byte[] readRaw(Context ctx, Class<?> type, String... keyParts) {
    return ctx.getStub().getState(new CompositeKey(type.getName(), keyParts).toString());
  }

  private void assertExists(Context ctx, Class<?> type, String... keyParts) {
    byte[] buf = readRaw(ctx, type, keyParts);
    if (buf == null || buf.length == 0) throw new ChaincodeException("NOT_FOUND");
  }

  private void assertNotExists(Context ctx, Class<?> type, String... keyParts) {
    byte[] buf = readRaw(ctx, type, keyParts);
    if (buf != null && buf.length > 0) throw new ChaincodeException("ALREADY_EXISTS");
  }

  private <T> T readEntity(Context ctx, Class<T> type, String... keyParts) {
    byte[] buf = readRaw(ctx, type, keyParts);
    if (buf == null || buf.length == 0) throw new ChaincodeException("NOT_FOUND");
    return deserialize(buf, type);
  }

  private byte[] serialize(Object obj) {
    try {
      return mapper.writeValueAsBytes(obj);
    } catch (JsonProcessingException e) {
      throw new ChaincodeException("SERIALIZATION_FAILURE: " + e.getMessage());
    }
  }

  private <T> T deserialize(byte[] bytes, Class<T> type) {
    try {
      return mapper.readValue(bytes, type);
    } catch (IOException e) {
      throw new ChaincodeException("DESERIALIZATION_FAILURE: " + e.getMessage());
    }
  }
}
