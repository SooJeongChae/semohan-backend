package semohan.semohan.domain.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import semohan.semohan.domain.restaurant.domain.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    // 메뉴 부분 다시 고민해봐야 함...
//    @Query("SELECT r FROM Restaurant r JOIN r.menus m WHERE m.name LIKE %:menuName%")
//    List<Restaurant> findByMenuName(@Param("menuName") String menuName);

    List<Restaurant> findAllByNameContaining(String name);

    List<Restaurant> findByAddress_AddressContaining(String location);
    Optional<Restaurant> findRestaurantById(Long id);
}
