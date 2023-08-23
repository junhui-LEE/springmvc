package hello.springmvc.basic;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LogTestController {

   // private final Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/log-test")
    public String logTest(){
        String name = "Spring";

       // System.out.println("name = " + name);
       // 과거에는 이렇게(위) 했지만 log라이브러리를 사용하면 아래와 같이 해야 한다.
        log.info("hello");

        log.trace("trace log={}", name);
        log.debug("debug log={}", name);
        log.info(" info log={}", name);
        log.warn(" warn log={}", name);
        log.error("error log={}", name);

        log.trace("trace my log=" + name);
//        log의 레벨을 info로 했을 경우에 log.trace() 는 실행되지 않지만 java의 특성상 문자열 합쳐지는것(concatenate)는
//        실행이 된다. concatenate연산이 실행이 된다는것이 핵심이다. 연산을 하기 때문에 메모리도 사용하고 CPU도 사용하기 때문에,
//        즉 쓸모없는 리소스를 사용하기 때문에 성능 측면에서 좋지 않은 것이다.
//        따라서 모든 log를 출력하는 코드는 log.trace("error log={}", name); 이런식으로 써야 한다.
//        이렇게 쓰면 만일 log레벨이 info일때 log.trace()도 실행되지 않고 concatenate도 실행되지 않는다.

        return "ok";
    }
}
