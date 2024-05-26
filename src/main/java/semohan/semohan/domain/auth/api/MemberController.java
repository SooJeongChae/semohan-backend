package semohan.semohan.domain.auth.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import semohan.semohan.domain.auth.application.MemberService;
import semohan.semohan.domain.auth.dto.MemberUpdateDto;
import semohan.semohan.domain.auth.dto.MemberViewDto;
import semohan.semohan.global.exception.CustomException;
import semohan.semohan.global.exception.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping(value = "/info")
    public ResponseEntity<MemberViewDto> memberInfo(HttpServletRequest request) {
        Long id = (Long) request.getSession().getAttribute("id");
        return ResponseEntity.ok(memberService.getMemberInfo(id));
    }

    @PostMapping("/edit-info")
    public ResponseEntity<Boolean> updateMemberInfo(HttpSession session, @Valid @RequestBody MemberUpdateDto memberUpdateDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomException(ErrorCode.INVALID_FORM_DATA, bindingResult.getFieldErrors().get(0).getDefaultMessage());
        } else {
            Long id = (Long) session.getAttribute("id");
            return ResponseEntity.ok(memberService.updateMemberInfo(id, memberUpdateDto));
        }
    }

}
