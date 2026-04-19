package hu.bme.mit.ftsrg.bta.assetstore;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.CompositeKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AssetStoreTest {

  private static final ObjectMapper mapper = new ObjectMapper();

  private AssetStore contract;
  @Mock private Context ctx;
  @Mock private ChaincodeStub stub;

  @BeforeEach
  void setUp() {
    contract = new AssetStore();
    given(ctx.getStub()).willReturn(stub);
  }

  @Test
  void given_existing_person_when_create_person_then_throw() throws JsonProcessingException {
    // Arrange
    var p1 = Person.builder().id("p1").name("Tim").build();
    given(stub.getState(new CompositeKey(Person.class.getName(), p1.id()).toString()))
        .willReturn(mapper.writeValueAsBytes(p1));

    // Act & Assert
    assertThatThrownBy(() -> contract.createPerson(ctx, p1.id(), "Josh"))
        .isInstanceOf(ChaincodeException.class);
  }
}
