package hello.springmvc.basic.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.springmvc.basic.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

//  {"username":"hello", "age":20}
//  content-type: application/json
//  이렇게 요청 메시지를 보낼 것이다.
@Slf4j
@Controller
public class RequestBodyJsonController {
    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        // HttpServletRequest를 사용해서 직접 HTTP메시지 바디에서 데이터를 읽어와서, 문자로 변환한다.
        log.info("messageBody={}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        // 문자로 된 JSON 데이터를 Jackson 라이브러리인 objectMapper를 사용해서 자바 객체로 변환한다.
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        response.getWriter().write("ok");
    }

//    requestBodyJsonV2 - @RequestBody 문자 변환
//    @RequestBody
//    HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
//
//    @ResponseBody
//    - 모든 메서드에 @ResponseBody 적용
//    - 메시지 바디 정보 직접 반환()
//    - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
    @ResponseBody
    @PostMapping("/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException{
        HelloData data = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}", data.getUsername(), data.getAge());
        return "ok";
    }

//    요청 메시지 바디에 json이 오면 매핑 메서드의 인자에서 문자열json로 받고 그 문자열 json을 jackson라이브러리(objectMapper)을 통해서
//    java객체로 바꾸는 과정 즉, HelloData data = objectMapper.readValue(messageBody, HelloData.class); 이 코드를 쓰는과정이
//    귀찮다. @ModelAttribute처럼 한번에 객체로 변환해서 매핑 메서드의 인자로 들어올 수는 없을까? -> 가능하다.
    @ResponseBody
    @PostMapping("/request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody HelloData data){
        log.info("username={}, age={}", data.getUsername(), data.getAge());
        return "ok";
    }

    // requestBodyJsonV4 - HttpEntity

//    @PostMapping("/request-body-json-v4")
//    public HttpEntity<String> requestBodyJsonV4(HttpEntity<String> httpEntity) throws IOException{
//        String messageBody = httpEntity.getBody();
//        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
//        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
//        return new HttpEntity<>("ok");
//    } => 정상 동작한다.

    @ResponseBody
    @PostMapping("/request-body-json-v4")
    public String requestBodyJsonV4(HttpEntity<HelloData> httpEntity){
        HelloData data = httpEntity.getBody();
        log.info("username={}, age={}", data.getUsername(), data.getAge());
        return "ok";
    }

//    requestBodyJsonV5
//    @RequestBody 생략 불가능(@ModelAttribute가 적용되어 버린다)
//    HttpMessageConverter 사용 -> MappingJackson2HttpMessageConverter (content-type : application/json)
//
//    @ResponseBody 적용
//    - 메시지 바디 정보 직접 반환 (view 조회x)
//    - HttpMessageConverter 사용 -> MappingJackson2HttpMessageConverter 적용 (Accept: application/json)
//     * 매핑 메소드에서 JSON으로 나갈때 요청 메시지 헤더에 Accept: application/json 이 있는지 확인을 해봐야 한다.
//       Accept가 어떤 것인지에 따라서 각각에 맞는 메시지 컨버터가 적용된다(Accept가 어떤 메시지 컨버터를 사용할지 영향을 준다.)
    @ResponseBody
    @PostMapping("request-body-json-v5")
    public HelloData requestBodyJsonV5(@RequestBody HelloData data) throws IOException{
        log.info("username={}, age={}", data.getUsername(), data.getAge());
        return data;
    }
//    메소드의 반환타입과 구현부의 return data;를 보면 알겠지만 HttpMessageConverter가 들어올때도 적용되지만 @ResponseBody가 있으면
//    나갈때도 적용된다. 이렇게 하면 HelloData객체가 HttpMessageConverter로 인해서 문자열JSON으로 바뀐다 그 문자열JSON이 HTTP 응답 메시지 바디에
//    들어가서 고객 응답으로 나가는 것이다.

//    다시~ ㅎㅎ
//    @ResponseBody 응답의 경우에도 @ResponseBody를 사용하면 해당 객체를 HTTP 메시지 바디에 직접 넣어줄 수 있다.
//    물론 이 경우에도 HttpEntity를 사용해도 된다.

//    @RequestBody 요청
//      JSON 요청 -> JSON을 처리하는 HTTP 메시지 컨버터 -> 객체(@RequestBody HelloData data)
//    @ResponseBody
//      객체(return data) -> HTTP 메시지 컨버터 -> 문자열JSON 응답

}
