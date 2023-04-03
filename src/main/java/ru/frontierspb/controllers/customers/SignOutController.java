package ru.frontierspb.controllers.customers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sign-out")
@RequiredArgsConstructor
public class SignOutController {
    /* TODO Хоть я и настроил sign-out в SecurityConfig, все равно не понимаю как лучше его реализовать. Вот отдельный
        контроллер, почему бы его не нельзя использовать? Во всяком случае пусть пока побудет здесь

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void signOut(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        new SecurityContextLogoutHandler().logout(request, response, authentication);
    }
    */
}
