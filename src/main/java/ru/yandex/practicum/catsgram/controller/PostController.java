package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.NotFoundException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public Collection<Post> findAll(@RequestParam(defaultValue = "0") int from,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "desc") String sort) {
        if (size <= 0) {
            throw new IllegalArgumentException("Размер страницы должен быть больше 0");
        }
        return postService.findAll(from, size, sort);
    }

    @PostMapping
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping
    public Post update(@RequestBody Post newPost) {
        return postService.update(newPost);
    }


    @GetMapping("/{postId}")
    public Post getPost(@PathVariable("postId") long postId) {
        return postService.findPostById(postId)
                .orElseThrow(() -> new NotFoundException("Пост с таким id не найден: " + postId));
    }
}