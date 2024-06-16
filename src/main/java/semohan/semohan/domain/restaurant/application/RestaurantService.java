package semohan.semohan.domain.restaurant.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import semohan.semohan.domain.member.domain.Member;
import semohan.semohan.domain.member.repository.MemberRepository;
import semohan.semohan.domain.menu.domain.Menu;
import semohan.semohan.domain.menu.repository.MenuRepository;
import semohan.semohan.domain.restaurant.domain.Restaurant;
import semohan.semohan.domain.restaurant.dto.PinnedScrappedRequestDto;
import semohan.semohan.domain.restaurant.dto.PinnedScrappedResponseDto;
import semohan.semohan.domain.restaurant.dto.RestaurantDetailDto;
import semohan.semohan.domain.restaurant.dto.RestaurnatDto;
import semohan.semohan.domain.restaurant.repository.RestaurantRepository;
import semohan.semohan.global.exception.CustomException;
import semohan.semohan.global.exception.ErrorCode;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final MemberRepository memberRepository;

    public List<RestaurnatDto> searchRestaurants(String type, String keyword) {
        List<Restaurant> restaurantList;

        switch (type) {
            // TODO: menu 추가
            case "menu":
                List<Menu> menuList = menuRepository.findMenusByDateAndName(Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now().plusDays(1)), keyword);
                restaurantList = menuList.stream()
                        .map(menu -> menu.getRestaurant())
                        .collect(Collectors.toList());
                break;
            case "name":
                restaurantList = restaurantRepository.findAllByNameContaining(keyword);
                break;
            case "location":
                restaurantList = restaurantRepository.findByAddress_AddressContaining(keyword);
                break;
            default:
                throw new CustomException(ErrorCode.INVALID_FORM_DATA);
        }

        return restaurantList.stream()
                .map(restaurant -> RestaurnatDto.toDto(restaurant))
                .collect(Collectors.toList());
    }

    public RestaurantDetailDto getRestaurant(long id) {
        return RestaurantDetailDto.toDto(restaurantRepository.getReferenceById(id));
    }

    public PinnedScrappedResponseDto getPinnedScrappedRestaurants(long memberId) {
        Member member = memberRepository.findMemberById(memberId).orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER));

        // pin 식당
        RestaurnatDto pinDto = RestaurnatDto.toDto(member.getPin());

        // scrap 식당
        List<RestaurnatDto> scrapDtoList = member.getScrap()
                .stream()
                .map(restaurant -> RestaurnatDto.toDto(restaurant))
                .collect(Collectors.toList());

        // PinnedScrappedDto 리턴
        return new PinnedScrappedResponseDto(pinDto, scrapDtoList);
    }

    @Transactional
    public boolean updatePinnedScrappedRestaurants(Long memberId, PinnedScrappedRequestDto dto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER));

        // pin 식당 수정
        if (dto.getPinnedRestaurantId() != null) {
            Restaurant pinnedRestaurant = restaurantRepository.findById(dto.getPinnedRestaurantId())
                    .orElseThrow(() -> new CustomException(ErrorCode.INVALID_RESTAURANT));
            member.setPin(pinnedRestaurant);
        } else {
            member.setPin(null);  // 또는 기존 핀 유지
        }

        // scrap 수정
        List<Restaurant> scrapList = dto.getScrappedRestaurantIdList() != null ? dto.getScrappedRestaurantIdList()
                .stream()
                .map(id -> restaurantRepository.findById(id)
                        .orElseThrow(() -> new CustomException(ErrorCode.INVALID_RESTAURANT)))
                .collect(Collectors.toList()) : new ArrayList<>();
        member.setScrap(scrapList);

        return true;
    }

    @Transactional
    public boolean scrapRestaurant(long memberId, long restaurantId) {
        Member member = memberRepository.findMemberById(memberId).orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER));
        member.getScrap().add(restaurantRepository.getReferenceById(restaurantId));
        return true;
    }

    @Transactional
    public boolean deleteScrapRestaurant(long memberId, long restaurantId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER));
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new CustomException(ErrorCode.INVALID_RESTAURANT));
        member.getScrap().remove(restaurant);
        return true;
    }
}