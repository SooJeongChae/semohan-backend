package semohan.semohan.domain.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import semohan.semohan.domain.menu.domain.Menu;

import java.util.Date;
import java.util.Optional;
import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("SELECT m FROM Menu m WHERE " +
            "(YEAR(m.mealDate) = YEAR(:today) AND MONTH(m.mealDate) = MONTH(:today) AND DAY(m.mealDate) = DAY(:today)) " +
            "OR (YEAR(m.mealDate) = YEAR(:tomorrow) AND MONTH(m.mealDate) = MONTH(:tomorrow) AND DAY(m.mealDate) = DAY(:tomorrow)) " +
            "AND (m.mainMenu LIKE %:name% OR m.subMenu LIKE %:name%)")
    List<Menu> findMenusByDateAndName(@Param("today") Date today,
                                      @Param("tomorrow") Date tomorrow,
                                      @Param("name") String name);

    @Query(value = "SELECT * FROM menu WHERE restaurant_id = :restaurantId AND TO_CHAR(meal_date, 'YYYY-MM-DD') = :mealDate", nativeQuery = true)
    Optional<Menu> findMenuByRestaurantIdAndMealDate(@Param("restaurantId") Long restaurantId, @Param("mealDate") String mealDate);

    @Query("SELECT m.mainMenu, COUNT(m.mainMenu) as count FROM Menu m WHERE TO_CHAR(m.mealDate, 'YYYY-MM-DD') = :mealDate GROUP BY m.mainMenu ORDER BY count DESC")
    List<Object[]> findMenusByMealDate(@Param("mealDate") String mealDate);

    @Query(value = "SELECT * FROM menu WHERE restaurant_id = :restaurantId AND TO_CHAR(meal_date, 'YYYY-MM-DD') = :mealDate AND meal_type = :mealType", nativeQuery = true)
    Optional<Menu> findMenuByRestaurantIdAndMealDateAndMealType(@Param("restaurantId") Long restaurantId, @Param("mealDate") String mealDate, @Param("mealType") int mealType);
}