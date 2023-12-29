package jpabook.jpashop.api;


import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;


    // 안 좋은 예시
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    // 좋은 예시
    @GetMapping("/api/v2/members")
    public Result membersV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect);
    }




    @Data
    @AllArgsConstructor
    static class Result<T> {

        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }


    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }




    // 실무에서는 entity를 그대로 파라미터로 받으면 절대 안됨
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }


    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {
        // update 경우는 이 정도에서 끝내는게 좋다.
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);

        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }




    @Data
    static class UpdateMemberRequest {
        @NotEmpty
        private String name;

    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;

    }


    @Data
    static class CreateMemberRequest {
        @NotEmpty
        private String name;

    }

    @Data
    static class CreateMemberResponse {
        private Long id;


        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }
}
