package semohan.semohan.domain.resaurant.domain;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Embeddable
public class Address {

    private String postCode;

    private String address;

    private String detailedAddress;

    public String getFullAddress() {
        return address + detailedAddress;
    }

    @Builder
    public Address(String postCode, String address, String detailedAddress) {
        this.postCode = postCode;
        this.address = address;
        this.detailedAddress = detailedAddress;
    }
}
