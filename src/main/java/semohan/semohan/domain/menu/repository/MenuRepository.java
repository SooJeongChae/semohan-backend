package semohan.semohan.domain.menu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import semohan.semohan.domain.menu.domain.Menu;

import java.util.Date;
import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("SELECT m FROM Menu m WHERE " +
            "(YEAR(m.mealDate) = YEAR(:today) AND MONTH(m.mealDate) = MONTH(:today) AND DAY(m.mealDate) = DAY(:today)) " +
            "OR (YEAR(m.mealDate) = YEAR(:tomorrow) AND MONTH(m.mealDate) = MONTH(:tomorrow) AND DAY(m.mealDate) = DAY(:tomorrow)) " +
            "AND (m.mainMenu LIKE %:name% OR m.subMenu LIKE %:name%)")
    List<Menu> findMenusByDateAndName(@Param("today") Date today,
                                      @Param("tomorrow") Date tomorrow,
                                      @Param("name") String name);
}
