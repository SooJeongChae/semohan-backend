package semohan.semohan.domain.review.api;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import semohan.semohan.domain.review.application.ReviewService;
import semohan.semohan.domain.review.dto.ReviewCreationDto;
import semohan.semohan.domain.review.dto.ReviewViewDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<ReviewViewDto>> getReviewsByRestaurantId(@PathVariable("restaurantId") long restaurantId) {
        return ResponseEntity.ok(reviewService.getReviewsByRestaurantId(restaurantId));
    }

    @GetMapping("/my-reviews")
    public ResponseEntity<List<ReviewViewDto>> getMyReviews(HttpServletRequest request) {
        long memberId = (Long) request.getSession().getAttribute("id");
        return ResponseEntity.ok(reviewService.getMyReviews(memberId));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<ReviewViewDto>> getReviewsByMemberId(@PathVariable("memberId") String memberId) {
        return ResponseEntity.ok(reviewService.getReviewsByMemberId(memberId));
    }

    @PostMapping("/{restaurantId}/write")
    public ResponseEntity<Boolean> createReview(@RequestBody ReviewCreationDto reviewCreationDto, @PathVariable("restaurantId") long restaurantId, HttpServletRequest request) {
        long memberId = (Long) request.getSession().getAttribute("id");
        return ResponseEntity.ok(reviewService.createReview(reviewCreationDto, restaurantId, memberId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteReview(@PathVariable("id") long id, HttpServletRequest request) {
        long memberId = (Long) request.getSession().getAttribute("id");
        return ResponseEntity.ok(reviewService.deleteReview(id, memberId));
    }
}
