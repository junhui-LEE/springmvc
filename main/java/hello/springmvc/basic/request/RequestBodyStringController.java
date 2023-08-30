package hello.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyStringController {
    @PostMapping("/request-body-string-v1")
    public void requestBodyString(HttpServletRequest request, HttpServletResponse response) throws IOException{
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        log.info("messageBody={}", messageBody);
        response.getWriter().write("ok");
    }

//    InputStream(Reader) : HTTP 요청 메시지 바디의 내용을 직접 조회
//    OutputStream(Writer) : HTTP 응답 메시지의 바디에 직접 결과 출력
    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException{
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        log.info("messageBody={}", messageBody);
        responseWriter.write("ok");
    }

//    하.. 스트림으로 받는게 싫다. String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
//    이런 코드를 쓰는 것이 싫다. 스프링이 알아서 해 줬으면 좋겠다. 한마디로 나는 HttpServletRequest request를 사용하지 않고
//    인자로 그냥 요청메시지를 통째로 받고 싶다. 그때 사용하는 것이 HttpEntity이다. HttpEntity를 사용하면 HTTP header과 body정보를
//    편리하게 직접 조회할 수 있다. 매핑 메소드의 인자에 요청메시지(HttpEntity)가 통째로 들어 오게 할 수 있다는 의미이다. 그래서 들어온
//    요청메시지를 이용해서 body나 header을 메서드 내에서 바로 처리 할 수 있다. 이때 body에 파라미터를 꺼낼때는 @RequestParam이나
//    @ModelAttribute를 이용해야 하고 그 외의 이를테면 json, xml, text가 요청메시지의 바디에 들어올때에는 HttpEntity를 사용해서
//    요청메시지의 body를 꺼낼 수 있다는 얘기이다. HttpEntity가 메서드의 인자로 주입(바인딩)되는 과정에서 HttpMessageConverter중에서
//    StringHttpMessageConverter을 사용한다.
//
//    메서드 내에서 HttpEntity를 반환 함으로서 응답메시지를 통째로 반환도 할 수 있는데, return new HttpEntity<>("ok"); 로 메서드 내에서
//    반환을 하면 dispatcherServlet으로 가서 view를 조회 하는 것이 아니라 바로 요청한 클라이언트에게 응답메시지를 바로 보내는 것이다.
//    이때 생성자의 인자에 "ok"라는 문자열을 내가 써 줬음으로 메시지 바디에 "ok"라는 text가 있는 응답 메시지 바디를 클라이언트에게 직접 보내는 것이다.
    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity){
        String messageBody = httpEntity.getBody();
        log.info("messageBody={}", messageBody);
        return new HttpEntity<>("ok");
    }
//    추가!
//    HttpEntity를 상속받은 아래듸 두 객체들도 같은 가능을 제공한다.
//    -> 상속받았으니까 기능이 더 있는 것이다.
//    * RequestEntity
//       : HttpMethod, url 정보가 추가된다. 요청에서 사용한다.
//    * ResponseEntity
//       : HTTP 상태 코드 설정 가능하다. 응답에서 사용한다.
//       ex) return new ResponseEntity<String>("Hello World", responseHeaders, HttpStatus.CREATED);

//    참고 :
//    스프링 MVC내부에서 HTTP 메시지 바디를 읽어서 문자나 객체로 변환해서 전달해주는데,
//    이때 HTTP메시지 컨버터(HttpMessageConverter)라는
//    기능을 사용한다. 이것은 조금 뒤에 HTTP메시지 컨버터에서 자세히 설명한다.

//  ** HttpEntity 쓰는 것 은근 귀찮다 그래서 애노테이션이 제공이 된다.(아래의 두개가 실무에서 엄청 많이 사용된다.)  **
//    @RequestBody
//    - 메시지 바디 정보를 직접 조회(@RequestParam x, @ModelAttribute x)
//    - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
//
//    @ResponseBody
//    - 메시지 바디 정보 직접 반환(view 조회x)
//    - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용

    @ResponseBody
    @PostMapping("/request-body-string-v4")
    public String requestBodyStringV4(@RequestBody String messageBody){
        // 헤더를 받고 싶으면 아래와 같이 추가로 더 써주면 된다.
//    public String requestBodyStringV4(@RequestBody String messageBody, @RequestHeader("host") String host)
//    public String requestBodyStringV4(@RequestBody String messageBody, @RequestHeader MultiValueMap<String, String> headerMap)
//    public String requestBodyStringV4(@RequestBody String messageBody, HttpEntity<String> httpEntity)
        log.info("messageBody={}", messageBody);
        return "ok";
    }

}

























