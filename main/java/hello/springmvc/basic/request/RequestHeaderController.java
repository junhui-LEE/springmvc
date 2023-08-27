package hello.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Slf4j
@RestController
public class RequestHeaderController {
//    애노테이션 기반의 스프링 컨트롤러는 정말 다양한 파라미터들을 받아들일 수 있다.
//    인터페이스로 정형화 되어 있는 것이 아니기 때문에 스프링이 지원하는것은
//    다 된다. 필요한것 다 적으면 웬만하면 다 있다.
    @RequestMapping("/headers")
    public String headers(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpMethod httpMethod,
                          Locale locale, // Locale : 언어 정보이다. 클라이언트가 어떤 언어를 받아들이는지를 요청메시지 헤더에 보내줄 수 있다.
                          // @RequestHeader 헤더정보는 이것을 이용해서 받을 수 있다.
                          @RequestHeader MultiValueMap<String, String> headerMap,  // 헤더정보를 전부 받고 싶을때 이렇게 코드를 작성해서 받으면 된다.
                          @RequestHeader("host") String host, // 헤더정보들 중에서 원하는것 한개만 가져오고 싶으면 이렇게 쓴다.
                            // 헤더정보 안에 있는 쿠키도 이렇게 편하게 받을 수 있다. value에는 쿠키 이름을 써주고 default가 true인데
                            // false로 설정을 해둠으로서 myCookie이름의 쿠키가 없어도 된다라는 것이다.
                          @CookieValue(value="myCookie", required = false) String cookie
                        ){
        log.info("request={}", request);
        log.info("response={}", response);
        log.info("httpMethod={}", httpMethod);
        log.info("locale={}", locale);
        log.info("headerMap={}", headerMap);
        log.info("header hose={}", host);
        log.info("myCookie={}", cookie);

        return "ok";
    }


//    cf : Spring에서 제공하는 MultiaValueMap 인터페이스
//    MAP과 유사한데, 하나의 key에 여러 값을 받을 수 있다.
//    HTTP header, HTTP 쿼리 파라미터와 같이 하나의 키에 여러 값을 받을 때 사용한다.
//     * keyA=value1&keyA=value2
//
//    ex:) MultiValueMap<String, String> map = new LinkedMultiValueMap();
//         map.add("keyA", "value1");
//         map.add("keyA", "value2");

//         이렇게 넣어 놓으면 key이름이 중복 되어도 map에 새롭게(덮어쓰기x) 들어간다.,
//         즉 keyA-value1, keyA-value2 이 두개가 map에 들어간다.
//         List<String> values = map.get("keyA");
//         => [value1, value2]

}
