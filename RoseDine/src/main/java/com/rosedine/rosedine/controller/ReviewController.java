package com.rosedine.rosedine.controller;

import com.rosedine.rosedine.service.ReviewService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{menuItemId}")
    public void sendReview(@RequestParam int userId, @PathVariable int menuItemId, @RequestParam int stars) {
        reviewService.sendReview(userId, menuItemId, stars);
    }

    @GetMapping("/{menuItemId}/user-rating")
    public Integer getUserRatingForMenuItem(@RequestParam int userId, @PathVariable int menuItemId) {
        return reviewService.getUserRatingForMenuItem(userId, menuItemId);
    }
}
