package com.sparta.team4.commerce_payment_system.domain.product;

import com.sparta.team4.commerce_payment_system.domain.product.entity.Product;
import com.sparta.team4.commerce_payment_system.domain.product.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductDataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        if (productRepository.count() > 0) {
            return;   // 이미 있으면 건너뜀 → 재시작해도 중복 안 쌓임
        }

        productRepository.saveAll(List.of(
                Product.builder().name("물복숭아").category("FOOD").price(1000).stockQuantity(20).description("말랑말랑 물복숭아").build(),
                Product.builder().name("딱복숭아").category("FOOD").price(2000).stockQuantity(5).description("딱딱한 딱복숭아").build(),
                Product.builder().name("샤인머스캣").category("FOOD").price(15000).stockQuantity(0).description("품절 테스트용").build(),
                Product.builder().name("아메리카노").category("FOOD").price(3000).stockQuantity(100).description("진한 커피").build(),
                Product.builder().name("우유식빵").category("FOOD").price(4500).stockQuantity(30).description("촉촉한 식빵").build(),
                Product.builder().name("기본 티셔츠").category("CLOTHING").price(19000).stockQuantity(50).description("면 100%").build(),
                Product.builder().name("청바지").category("CLOTHING").price(49000).stockQuantity(15).description("스트레이트 핏").build(),
                Product.builder().name("후드집업").category("CLOTHING").price(59000).stockQuantity(8).description("기모 안감").build(),
                Product.builder().name("양말 세트").category("CLOTHING").price(9000).stockQuantity(200).description("5켤레 묶음").build(),
                Product.builder().name("겨울 패딩").category("CLOTHING").price(159000).stockQuantity(0).description("품절 테스트용").build(),
                Product.builder().name("무선 이어폰").category("ELECTRONICS").price(89000).stockQuantity(25).description("노이즈 캔슬링").build(),
                Product.builder().name("보조배터리").category("ELECTRONICS").price(29000).stockQuantity(40).description("20000mAh").build(),
                Product.builder().name("기계식 키보드").category("ELECTRONICS").price(120000).stockQuantity(3).description("적축").build(),
                Product.builder().name("USB-C 케이블").category("ELECTRONICS").price(8000).stockQuantity(300).description("고속 충전").build(),
                Product.builder().name("모니터 27인치").category("ELECTRONICS").price(210000).stockQuantity(6).description("QHD").build(),
                Product.builder().name("자바의 정석").category("BOOK").price(30000).stockQuantity(12).description("자바 기본서").build(),
                Product.builder().name("클린 코드").category("BOOK").price(27000).stockQuantity(18).description("로버트 마틴").build(),
                Product.builder().name("이펙티브 자바").category("BOOK").price(36000).stockQuantity(9).description("조슈아 블로크").build(),
                Product.builder().name("HTTP 완벽 가이드").category("BOOK").price(52000).stockQuantity(4).description("네트워크 필독서").build(),
                Product.builder().name("오브젝트").category("BOOK").price(38000).stockQuantity(0).description("품절 테스트용").build()
        ));
    }
}