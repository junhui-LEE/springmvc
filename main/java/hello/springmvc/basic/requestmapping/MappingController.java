package hello.springmvc.basic.requestmapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;

@RestController
public class MappingController {
    private Logger log = LoggerFactory.getLogger(getClass());


//    기본요청
//    둘다 허용 /hello-basic, /hello-basic/
//    HTTP 메서드 모두 허용 GET, HEAD, POST, PUT, PATCH, DELETE
    @RequestMapping({"/hello-basic", "hello-go"})
    public String helloBasic(){
        log.info("helloBasic");
        return "ok";
    }

//    method 특정 HTTP 메서드 요청만 허용
//    GET, HEAD, POST, PUT, PATCH, DELETE
//    cf: @RequestMapping의 인자로 2개가 들어갈 때에는 매핑url에 value= 하고 파라미터 키를 넣어 줘야 한다.
    @RequestMapping(value="/mapping-get-v1" , method= RequestMethod.GET)
    public String mappingGetV1(){
        log.info("mappingGetV1");
        return "ok";
    }

//    편리한 축약 애노테이션(코드보기)
//    @GetMapping
//    @PostMapping
//    @PutMapping
//    @DeleteMapping
//    @PatchMapping
    @GetMapping(value="/mapping-get-v2")
    public String mappingGetV2(){
        log.info("mapping-get-v2");
        return "ok";
    }


//        PathVariable 사용 :
//    요청url자체에 값이 들어가 있도록 할 수 있다.
//    예를들어 /mapping/userA 라는 요청url과 /mapping/userB라는 요청url이 있을때
//    userA와 userB가 요청url의 값이 되는 것이다. 그럼 매핑url에서는 그 값을 변수로 담아서 처리 할 수 있다.
//    @GetMapping("/mapping/{userId}") 이렇게 처리하면 매핑url에서 userId라는 변수를 이용해서
//    요청url의 값을 담을 수 있다. /mapping/userA는 매핑url인 @GetMapping("/mapping/{userId}")에 매핑되고
//    매핑url에 있는 userId라는 변수에 userA가 담아지게 되는 것이다.
//    그럼 @PathVariable을 통해서 매핑url에 있는 변수를 가져 올 수 있다.
//    /mapping/userB도 @GetMapping("/mapping/{userId}")에 매핑된다.

//    매핑url의 변수명과 메서드의 인자 변수명이 같으면 생략 가능
//    @PathVariable("userId") String userId -> @PathVariabl String userId
//     => ("userId") 생략 가능
    @GetMapping("/mapping/{userId}")
    public String mappingPath(@PathVariable("userId") String data){
        log.info("mappingPath userId={}", data);
        return "ok";
    }

//  PathVariable 사용 다중
    @GetMapping("/mapping/users/{userId}/orders/{orderId}")
    public String mappingPath(@PathVariable String userId, @PathVariable Long orderId){
        log.info("mappingPath userId={} orderId={}", userId, orderId);
        return "ok";
    }

//    파라미터로 추가 매핑
//    params="mode"
//    params="!mode"
//    params="mode=debug"
//    params="mode!=debug"
//    params={"mode=debug", "data"="good"}

//    params="mode=debug" 이것을 써주면 요청메시지에 있는
//    쿼리 파라미터로 mode=debug가 있어야지 요청이 아래의 메서드와 매핑된다
//    당연히 value="/mapping-param"도 요청url과 맞아야 한다. 둘다 맞아야 메소드가 실행된다.
    @GetMapping(value="/mapping-param", params="mode=debug")
    public String mappingParam(){
        log.info("mappingParam");
        return "ok";
    }

//    특정 헤더 조건 매핑
//    특정 헤더로 추가 매핑
//    headers="mode"
//    headers="!mode"
//    headers="mode=debug"
//    headers="mode!=debug" ( != )
    @GetMapping(value="/mapping-header", headers="mode=debug")
    public String mappingHeader(){
        log.info("mappingHeader");
        return "ok";
    }

//    미디어 타입 조건 매핑 - HTTP 요청 Content-Type, consume
//    Content-Type 헤더 기반 추가 매핑 Media Type
//    consumes="application/json"
//    consumes="!application/json"
//    consumes="application/*"
//    consumes="*\/*"
//    MediaType.APPLICATION_JSON_VALUE
//    @PostMapping(value="/mapping-consume", consumes=MediaType.APPLICATION_JSON_VALUE)
    @PostMapping(value="/mapping-consume", consumes="application/json")
    public String mappingConsumes(){
        log.info("mappingConsumes");
        return "ok";
    }

//    미디어 타입 조건 매핑 - HTTP요청 Accept, produce
//    Accept 헤더 기반 Media Type
//    produces = "text/html"
//    produces = "!text/html"
//    produces = "text/*"
//    produces = "*\/*"

//    아래의 produces="text/html"가 의미하는 것은 컨트롤러가 생산해내는(공급하는)
//    Content-Type은 "text/html" 이라는 뜻이고 이때는 HTTP요청의 Accept와 produces의 값과
//    맞아야(일치해야) 한다.
    @PostMapping(value="/mapping-produce", produces="text/html")
    public String mappingProduces(){
        log.info("mappingProduces");
        return "ok";
    }
}









































