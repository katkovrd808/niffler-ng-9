package guru.qa.niffler.model.userdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record UdUserJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("username")
        String username,
        @JsonProperty("firstname")
        String firstname,
        @JsonProperty("surname")
        String surname,
        @JsonProperty("fullname")
        String fullname,
        @JsonProperty("currency")
        CurrencyValues currency,
        @JsonProperty("photo")
        String photo,
        @JsonProperty("photoSmall")
        String photoSmall,
        @JsonProperty("friendshipStatus")
        FriendshipStatus friendshipStatus,
        @JsonIgnore
        TestData testData) {

  public static UdUserJson fromEntity(UdUserEntity entity, FriendshipStatus friendshipStatus) {
    return new UdUserJson(
            entity.getId(),
            entity.getUsername(),
            entity.getFirstname(),
            entity.getSurname(),
            entity.getFullname(),
            entity.getCurrency(),
            entity.getPhoto() != null && entity.getPhoto().length > 0 ? new String(entity.getPhoto(), StandardCharsets.UTF_8) : null,
            entity.getPhotoSmall() != null && entity.getPhotoSmall().length > 0 ? new String(entity.getPhotoSmall(), StandardCharsets.UTF_8) : null,
            friendshipStatus,
            null
    );
  }

  public UdUserJson addTestData(TestData testData) {
    return new UdUserJson(
            id,
            username,
            firstname,
            surname,
            fullname,
            currency,
            photo,
            photoSmall,
            friendshipStatus,
            testData
    );
  }
}
