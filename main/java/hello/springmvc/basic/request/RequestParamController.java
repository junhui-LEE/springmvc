package hello.springmvc.basic.request;

import hello.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
public class RequestParamController {
//    반환 타입이 없으면서 이렇게 응담에 값을 직접 집에넣으면, view 조회x
    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        log.info("username={}, age={}", username, age);
        response.getWriter().write("ok");
    }

//    @RequestParam 사용
//    - 파라미터 이름으로 바인딩
//    @ResponseBody 추가
//    - View 조회를 무시하고, HTTP message body에 직접 해당 내용 입력
    @ResponseBody
    @RequestMapping("/request-param-v2")
    public String requestParamV2(@RequestParam("username") String memberName, @RequestParam("age") int memberAge){
//        @RequestParam("username") String memberName 이렇게 하면
//        request.getParameter("username") 을 한 것과 똑같은 효과가 있는 것이다.
        log.info("username={}, age={}", memberName, memberAge);
        return "ok";
    }

//    @RequestParam 사용
//    HTTP 파라미터 이름(요청메시지의 쿼리파리미터의 이름)과 컨트롤러 안의 메소드 안에 있는 인자 변수명과 같으면
//    @RequestParam(name="xx") 에서 (name="xx") 이것 통째로 생략 가능하다.
    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(@RequestParam String username, @RequestParam int age){
        log.info("username={}, age={}", username, age);
        return "ok";
    }

//  인간의 욕심은 끝이 없다. ㅎㅎ 더 생략하고 싶다 ㅎㅎ
//  요청파라미터의 이름과 변수명이 같아서 @RequestParam(name="asdf") 에서 (name="asdf")을
//  생략했어도 변수의 타입이 String, int 등 단순 타입이면 @RequestParam도 생략이 가능하다.
//  여기서 단순타입이라고 함은 래퍼를 포함해서 java나 spring에서 제공하는 예약어 일 경우를 의미한다.
//  그 경우에는 @RequestParam도 생략이 가능하다.
    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(String username, int age){
        log.info("username={}, age={}", username, age);
        return "ok";
    }

//    @RequestParam.required
//    /request-param-required -> username이 없으므로 예외
//    주의!
//    /request-param-required?username= -> 빈문자로 통과
//    주의!
//    /request-param-required
//    int age -> null을 int에 입력하는 것은 불가능, 따라서 Integer 변경해야 함(또는 다음에 나오는 defaultValue 사용)
    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(@RequestParam(required=true) String username, @RequestParam(required=false) Integer age){
        log.info("username={}, age={}", username, age);
        return "ok";
    }

//    기본값 적용 - requestParamDefault
//    @RequestParam
//    - defaultValue 사용
//    참고 : defaultValue는 빈 문자의 경우에도 적용
//    /request-param-default?username=
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(@RequestParam(required=true, defaultValue="guest") String username, @RequestParam(required=false, defaultValue="-1") int age){
//        파라미터에 값이 없는 경우 defaultValue를 사용하면 기본 값을 적용할 수 있다.
//        이미 기본 값이 있기 때문에 required는 의미가 없다.
        log.info("username={}, age={}", username, age);
        return "ok";
    }

//    파라미터를 Map으로 조회하기 - requestParamMap
//    @RequestParam Map, MultiValueMap
//    Map(key=value)
//    MultiValueMap(key=[value1, value2, ...]) ex) (key=userIdx, value=[id1, id2])
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap){
//        map으로 파라미터를 가져올 때에는 변수명을 아무거나 써도 된다.
//        map으로 파라미터를 가져올 때에는 @RequestParam의 name을 쓰면 안된다.
//        @RequestParam(name="asdf") x
        log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }

//    파라미터를 Map, MultiValueMap으로 조회할 수 있다.
//    @RequestParam Map,
//      Map(key=value)
//    @RequestParam
//      MultiValueMap(key=[value1, value2, ...])  ex) (key=userIds, value=[id1, id2])

//   * 파라미터의 값이 1개가 확실하다면 Map을 사용해도 되지만, 그렇지 않다면 MultiValueMap을 사용하자 *


//   실제 개발을 하면 요청 파라미터를 받아서 필요한 객체를 만들고 그 객체에 값을 넣어주어야 한다.
//   보통 아래와 같이 코드를 작성해야 한다. (1, 2, 3의 과정)
    @ResponseBody
    @RequestMapping("/test")
    public String modelAttributeV1(@RequestParam String username, @RequestParam int age){
        HelloData data = new HelloData(); // 1
        data.setUsername(username);       // 2
        data.setAge(age);                 // 3
        log.info("data={}", data);
        return "ok";
    }

//    스프링은 1,2,3 의 과정을 자동화 해 주는 @ModelAttribute기능을 제공한다.
//    @ModelAttribute 사용
//    참고 : model.addAttribute(helloData); 코드도 함께 자동 적용됨, 뒤에 model을 설명할때 자세히 설명

    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(@ModelAttribute HelloData data){
        log.info("username={}, age={}", data.getUsername(), data.getAge());
        return "ok";
    }


//    @ModelAttribute 생략 - modelAttributeV2
//    @ModelAttribute 생략 가능
//    String, int 같은 단순 타입 = @RequestParam을 생략할 수 있다.
//    그외 나머지 클래스 = @ModelAttribute을 생략할 수 있다.
//     예외! ) HttpServletResponse와 같이 argument resolver로 지정해둔 타입 같은 경우에는 ModelAttribute가 적용되지 않는다. (argument resolver는 뒤에서 학습한다.)
    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(HelloData HelloData){
        log.info("username={}, age={}", HelloData.getUsername(), HelloData.getAge());
        return "ok";
    }
//    @ModelAttribute(name="asdf") HelloData helloData 와 같이 ModelAttribute에 name도 적을 수 있는데
//    이 부분은 Spring MVC에서 뷰가 있어야 제대로 이해하기 때문에 나중에 작은 프로젝트 만드는 수업(동일 책 뒷부분) 에서 다뤄보겠다.

}























